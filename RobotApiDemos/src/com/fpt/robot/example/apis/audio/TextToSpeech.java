package com.fpt.robot.example.apis.audio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.ui.RobotActivity;

public class TextToSpeech extends RobotActivity implements OnClickListener, 
	OnCheckedChangeListener {
	private static final String TAG = "TextToSpeech";

	private Button btSay;
	private EditText etSay;
	
	private RadioGroup rgLanguageSelection;
	private RadioButton rdEnglish;
	private RadioButton rdVietnamese;
	private RadioButton rdFrench;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_text_to_speech);
		
        btSay = (Button)findViewById(R.id.btSay);
        btSay.setOnClickListener(this);        
        
        etSay = (EditText)findViewById(R.id.etSay);
        
        rgLanguageSelection = (RadioGroup)findViewById(R.id.rgLanguageSelection);
        rdEnglish = (RadioButton)findViewById(R.id.rdEnglish);
        rdVietnamese = (RadioButton)findViewById(R.id.rdVietnamese);
        rdFrench = (RadioButton)findViewById(R.id.rdFrench);
        
        rgLanguageSelection.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_to_speech, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_text_to_speech;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btSay) {
			// test say()
			say();
		}
	}

	private void say() {		
		new Thread(new Runnable() {			
			@Override
			public void run() {
				String textToSay = etSay.getText().toString();
				if (textToSay.isEmpty()) {
					makeToast("nothing to say!");
					return;
				}
				String language = RobotTextToSpeech.ROBOT_TTS_LANG_EN;
				boolean result = false;
				if (rdVietnamese.isChecked()){
					language = RobotTextToSpeech.ROBOT_TTS_LANG_VI;
				} else if (rdFrench.isChecked()) {
					language = RobotTextToSpeech.ROBOT_TTS_LANG_FR;
				} else {
					language = RobotTextToSpeech.ROBOT_TTS_LANG_EN;
				}
				showProgress("saying...");
				try {
					result = RobotTextToSpeech.say(getRobot(), textToSay, language);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("say text failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("say text failed!");
				}
				Log.d(TAG, "say('" + textToSay + "'): result=" + result);
			}
		}).start();
	}

    private ProgressDialog progressDialog = null;
    
    protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(TextToSpeech.this);
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
				Toast.makeText(TextToSpeech.this, toast, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.rdVietnamese) {
			etSay.setText("xin chào!");
		} else if (checkedId == R.id.rdFrench) {
			etSay.setText("bonjour!");
		} else if (checkedId == R.id.rdEnglish) {
			etSay.setText("hello!");
		}
	}
}
