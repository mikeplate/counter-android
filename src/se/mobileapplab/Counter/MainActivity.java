package se.mobileapplab.Counter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	// Store the current value of the counter in memory
	int _count;
	
	// Keep a pointer to the TextView with the counter value
	TextView _countView;
	
	// This function is called when the application starts
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        openCount();
        _countView = (TextView)findViewById(R.id.count);
    	_countView.setText(Integer.toString(_count));
    }
    
    // This function is called when the user presses the menu button on the device
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Add two commands to the menu. We only care about the unique id, 0 or 1, and the text for the items
    	menu.add(Menu.NONE, 0, Menu.NONE, R.string.reset_menu);
    	menu.add(Menu.NONE, 1, Menu.NONE, R.string.about_menu);
    	return true;
    }
    
    // This function is called when the user has chosen a menu item created in onCreateOptionsMenu
    @Override public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId()==0) {
    		// Set counter to zero, update value in user interface and on disk
        	_count = 0;
        	_countView.setText(Integer.toString(_count));
            saveCount();
    	}
    	else {
			Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/mikeplate/counter-android-demo"));
			startActivity(browserIntent);
    	}
    	return true;
    }
    
    // This function is called when the user touches the increase button
    public void onIncrease(View button) {
		// Increase counter by one, update value in user interface and on disk
    	_count++;
    	_countView.setText(Integer.toString(_count));
        saveCount();
    }

    // This function is called when the user touches the decrease button
    public void onDecrease(View button) {
		// Decrease counter by one, update value in user interface and on disk
    	_count--;
    	_countView.setText(Integer.toString(_count));
        saveCount();
    }
    
    // This function reads the stored value from a file on the device
    void openCount() {
    	try {
    		// Open a file and data stream
        	FileInputStream out = openFileInput("count.bin");
        	DataInputStream data = new DataInputStream(out);
        	
        	// Read the binary value from the stream and store in the integer
        	_count = data.readInt();
        	
        	// Remember to close the streams
        	data.close();
        	out.close();
    	}
    	catch (FileNotFoundException err) {
    		// Ignore this error, since is should just mean that the program is run for the first time
    	}
    	catch (IOException err) {
    		showError(err);
    	}
    }
    
    // This function stores the current value in a file on the device
    void saveCount() {
    	try {
    		// Open a file and data stream
        	FileOutputStream out = openFileOutput("count.bin", MODE_PRIVATE);
        	DataOutputStream data = new DataOutputStream(out);
        	
        	// Write the binary value of the integer to the stream
        	data.writeInt(_count);
        	
        	// Remember to close the streams
        	data.close();
        	out.close();
    	}
    	catch (FileNotFoundException err) {
    		showError(err);
    	}
    	catch (IOException err) {
    		showError(err);
    	}
    }

    // This function is called to show the error message of an exception in an alert dialog
    void showError(Exception err) {
    	String msg = getString(R.string.file_error, err.getLocalizedMessage());
		new AlertDialog.Builder(this).setMessage(msg).show();
    }
}
