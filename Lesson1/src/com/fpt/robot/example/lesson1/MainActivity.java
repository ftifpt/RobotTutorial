package com.fpt.robot.example.lesson1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.ui.RobotActivity;
import com.fpt.robot.utils.DialogUtils;

public class MainActivity extends RobotActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btnSpeak =(Button) findViewById(R.id.btn_speak);
		Button btnStand =(Button) findViewById(R.id.btn_standup);
		Button btnSit=(Button) findViewById(R.id.btn_sitdown);
		btnStand.setOnClickListener(this);
		btnSit.setOnClickListener(this);
		btnSpeak.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_main;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_speak:
			speak("xin chào các bạn");
			break;
		case R.id.btn_standup:
			motion(0);
			break;
			
		case R.id.btn_sitdown:
			motion(1);
			break;
		default:
			break;
		}
		
	}

	private ProgressDialog progressDialog;
	
	private void motion(final int action) {
		progressDialog = ProgressDialog.show(this, null, "Doing the action...", false, true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					RobotTextToSpeech.say(getRobot(), "xin đợi một chút", RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					if (action==0) {
						RobotMotionAction.standUp(getRobot(), 0.5f);
					} else if (action==1) {
						RobotMotionAction.sitDown(getRobot(), 0.5f);
					}
				} catch (RobotException e) {
					e.printStackTrace();
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						try {
							DialogUtils.hideDialog(progressDialog);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}
	
	private void speak(final String text) {
		progressDialog = ProgressDialog.show(this, null, "Speaking...", false, true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					RobotTextToSpeech.say(getRobot(), text, RobotTextToSpeech.ROBOT_TTS_LANG_VI);
				} catch (RobotException e) {
					e.printStackTrace();
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						try {
							DialogUtils.hideDialog(progressDialog);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			DialogUtils.hideDialog(progressDialog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (!Thread.currentThread().isInterrupted()) {
				Thread.currentThread().interrupt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
