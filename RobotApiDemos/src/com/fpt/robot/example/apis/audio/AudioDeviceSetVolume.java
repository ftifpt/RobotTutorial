package com.fpt.robot.example.apis.audio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioDevice;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.ui.RobotActivity;

public class AudioDeviceSetVolume extends RobotActivity {
	private static final String TAG="AudioDeviceSetVolume";
	
	private SeekBar sbAudioVolume;
	private Button btSetVolume;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sbAudioVolume = (SeekBar) findViewById(R.id.sbAudioVolume);
		
		btSetVolume = (Button) findViewById(R.id.btSetVolume);
		btSetVolume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setVolume();
			}
		});
		
		updateVolume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio_device_set_volume, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_audio_device_set_volume;
	}

	private void updateVolume() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				int volume = 0;
				showProgress("getting output volume...");
				try {
					volume = RobotAudioDevice.getOutputVolume(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("get robot's volume failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				Log.d(TAG, "getVolume(): volume=" + volume);
				final int vol = volume;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sbAudioVolume.setProgress(vol);
					}
				});				
			}
		}).start();
	}
	
	private void setVolume() {
		final int volume = sbAudioVolume.getProgress();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean result = false;
				showProgress("setting output volume...");
				try {
					result = RobotAudioDevice.setOutputVolume(getRobot(), volume);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("set robot's volume failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("set robot's volume failed!");
				}
			}
		}).start();
	}

    private ProgressDialog progressDialog = null;
    
    protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AudioDeviceSetVolume.this);
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
				Toast.makeText(AudioDeviceSetVolume.this, toast, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
