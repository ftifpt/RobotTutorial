package com.example.stiffnesscontrol;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.ui.RobotActivity;

public class MainActivity extends RobotActivity {

	
	private Button runBt;
	private Button stop;
	
	private EditText jointName;
	private EditText stiffness;
	
	private String jN;
	float stiff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		runBt = (Button) findViewById(R.id.btn_Run);
		stop = (Button) findViewById(R.id.btn_Stop);
		stiffness=(EditText) findViewById(R.id.editStiffness);
		jointName=(EditText) findViewById(R.id.editJointName);
		
		
		
		runBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Robot mRobot = getRobot();// get robot from robot service
				jN = jointName.getText().toString();
				stiff = Float.parseFloat(stiffness.getText().toString());
				final String[] jName = {jN};// Get joint name from editText box of joint name
				final float[] st ={stiff};// Get stiffness from editText box
				Toast.makeText(MainActivity.this, "start turning on stiffness", Toast.LENGTH_SHORT).show();
				
				new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								//Execute function
								Log.i(getPackageName()," stiffness on " );
								RobotMotionStiffnessController.setStiffnesses(mRobot, jName, st);
								} catch (RobotException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}).start();
			}

		});

		// ====================================================================================
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Robot mRobot = getRobot();// get robot from robot service			
				final String[] jName = {jN};// get joint name
				final float[] st ={0.0f};// set zero stiffness
				Toast.makeText(MainActivity.this, "start turning off stiffness", Toast.LENGTH_SHORT).show();
				
				new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Log.i(getPackageName()," Stiffness off " );	
								RobotMotionStiffnessController.setStiffnesses(mRobot, jName, st);
									} catch (RobotException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}).start();
			}
		});

		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
				
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_main;
	}

}
