package com.fpt.robot.example.apis.audio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.ui.RobotActivity;

public class AudioPlayLocalFile extends RobotActivity {
	private static final String TAG="AudioPlayLocalFile";
	
	private EditText etAudioFilePath;
	
	private Button btBrowseFile;
	private Button btPlayAudioFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_audio_player);
		
		etAudioFilePath = (EditText) findViewById(R.id.etAudioFilePath);
		btBrowseFile = (Button) findViewById(R.id.btBrowseFile);
		btPlayAudioFile = (Button) findViewById(R.id.btPlayAudioFile);
		
		btBrowseFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				browseFile();
			}
		});
		
		btPlayAudioFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playAudioFile();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio_play_local_file, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_audio_play_local_file;
	}

	protected void browseFile() {
		Intent pickAudioIntent = new Intent(Intent.ACTION_GET_CONTENT);
		pickAudioIntent.setType("audio/*");
		startActivityForResult(pickAudioIntent, REQUEST_CODE_PICK_AUDIO_FILE);
	}

	private static final int REQUEST_CODE_PICK_AUDIO_FILE = 0;
	
	protected void playAudioFile() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				String filePath = etAudioFilePath.getText().toString();
				if (!filePath.isEmpty()) {
					boolean result = false;
					showProgress("playing local file...");
					try {
						result = RobotAudioPlayer.playLocalFile(getRobot(), filePath);	
					} catch (final RobotException e) {
						e.printStackTrace();
						cancelProgress();
						makeToast("play local audio file failed! " + e.getMessage());
						return;
					}
					cancelProgress();
					Log.d(TAG, "playLocalFile('" + filePath + "'): result=" + result);
					if (!result) {
						makeToast("play local audio file failed!");
					}
				} else {
					Log.d(TAG, "empty file path!");
					makeToast("file path is empty! ");
				}		
			}
		}).start();
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK 
				&& requestCode == REQUEST_CODE_PICK_AUDIO_FILE) {
			String path = data.getDataString();
			Log.d(TAG, "path=" + path);
			
			Uri selectedAudioFile = data.getData();
			
            String[] filePathColumn = { MediaStore.Audio.Media.DATA };
            Cursor cursor = getContentResolver().query(
            		selectedAudioFile, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            
            makeToast("selected: " + filePath);
            etAudioFilePath.setText(filePath);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

    private ProgressDialog progressDialog = null;
    
	protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AudioPlayLocalFile.this);
                }
                // no title
                if (message != null) {
                    progressDialog.setMessage(message);
                }
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void cancelProgress() {
        //Log.d(TAG, "cancelProgress()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    //progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }
        });
    }
    
	protected void makeToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(AudioPlayLocalFile.this, toast, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
