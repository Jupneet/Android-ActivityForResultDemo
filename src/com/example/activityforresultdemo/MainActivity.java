package com.example.activityforresultdemo;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final int PICK_CONTACT_REQUEST = 1; 
	static final int TAKE_A_PICTURE_REQUEST = 2;
	
	
	private Uri fileUri;
	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		text = (TextView)findViewById(R.id.text);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(1, 1, 0, "CONTACT").setIcon(R.drawable.ic_launcher);
		menu.add(1, 2, 0, "CAMERA").setIcon(R.drawable.ic_launcher);
		
		
		return true;
	}

	@SuppressLint({ "ShowToast", "InlinedApi" })
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		// Handle action bar actions click
		switch (item.getItemId()) {
		case 1:
			pickContact();
			return true;
		case 2:
			text.setText("CAMERA");
			
			try {
			    File root = android.os.Environment.getExternalStorageDirectory();
			    File imagesFolder = new File(root.getAbsolutePath() + "/DemoImages/");
			    if(imagesFolder.exists() == false){
			    	imagesFolder.mkdirs();  
			    }
			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    File image = new File(imagesFolder, "image_001.jpg");
			    Uri uriSavedImage = Uri.fromFile(image);
			    fileUri = uriSavedImage;
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
			    startActivityForResult(intent, TAKE_A_PICTURE_REQUEST);

				return true;
			}
			catch (Exception e) {
			}
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void pickContact() {
	    Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
	    pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
	    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}
	
	@SuppressLint({ "ShowToast", "InlinedApi" })
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 Log.d("NUMBER", "1");
	    // Check which request it is that we're responding to
	    if (requestCode == PICK_CONTACT_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // Get the URI that points to the selected contact
	            Uri contactUri = data.getData();
	            // We only need the NUMBER column, because there will be only one row in the result
	            String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

	 
	            Cursor cursor = getContentResolver()
	                    .query(contactUri, projection, null, null, null);
	            cursor.moveToFirst();

	            // Retrieve the phone number from the NUMBER column
	            int column = cursor.getColumnIndex(Phone.NUMBER);
	            int columnNameIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
	            
	            String number = cursor.getString(column);
	            String name = cursor.getString(columnNameIndex);
	            
	            Log.d("NUMBER", number);
	            text.setText(number + "  " + name);
	            Toast.makeText(MainActivity.this, number, Toast.LENGTH_LONG);
	            // Do something with the phone number...
	        }
	    }
	    
	    if (requestCode == TAKE_A_PICTURE_REQUEST) {
	        if (resultCode == RESULT_OK) {
	          
	        	File f = new File("/mnt/sdcard/DemoImages/image_001.jpg");
	        	ImageView image = (ImageView)findViewById(R.id.image);
	        	Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
	        	image.setImageBitmap(bmp);
	            
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}
}
