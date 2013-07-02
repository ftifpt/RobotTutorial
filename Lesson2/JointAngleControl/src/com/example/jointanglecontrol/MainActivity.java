package com.example.jointanglecontrol;

import android.os.Bundle;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotMotionJointController;
import com.fpt.robot.ui.RobotActivity;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends RobotActivity {

	private Button runBt;
	private Button returnBt;

	private EditText jointName;
	private EditText angle;
	private EditText time;
	private EditText isAbsolute;
	private String jN;
	float anglevalue;
	float timevalue;
	boolean isAbsolutevalue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		runBt = (Button) findViewById(R.id.btn_RunAI);
		returnBt = (Button) findViewById(R.id.btn_returnAI);

		jointName = (EditText) findViewById(R.id.editJointNameAI);
		angle = (EditText) findViewById(R.id.editAngleAI);
		time = (EditText) findViewById(R.id.editTimeAI);
		isAbsolute = (EditText) findViewById(R.id.editIsAbsoluteAI);
		
		
		// ====================================================================================
				runBt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//status.setText("");
						
						jN = jointName.getText().toString();
						anglevalue = Float.parseFloat(angle.getText().toString());
						timevalue = Float.parseFloat(time.getText().toString());
						isAbsolutevalue = Boolean.parseBoolean(isAbsolute.getText().toString());
						
						final Robot mRobot = getRobot();
						final String[] jName = {jN};//Get joint name from editText box of joint name 
						final float[] al ={anglevalue};//get angle value from editText box of angle
						final float[] t = {timevalue};//Get duration form editText box of time
						final boolean iA = isAbsolutevalue;//Get coordinate from editText box of isAbsolute
						new Thread(new Runnable() {
						@Override
						  public void run() {
							try {
								//execute function		
								RobotMotionJointController.angleInterpolation(mRobot, jName, al, t, iA);
							
								} catch (RobotException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}).start();
					}

				});

				// ====================================================================================
				returnBt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						//status.setText("");
						final Robot mRobot = getRobot();
						final String[] jName = {jN};//Get joint name from editText box of joint name
						final float[] al ={0.0f};//Set joint angle to original point
						final float[] t = {timevalue};//Get duration form editText box of time
						final boolean iA = true;//Set default coordinate
						new Thread(new Runnable() {
						@Override
						  public void run() {
							try {
										
								RobotMotionJointController.angleInterpolation(mRobot, jName, al, t, iA);
										
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
