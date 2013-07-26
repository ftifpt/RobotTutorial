package com.fpt.robot.example.apis.motion;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;


import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.R.layout;
import com.fpt.robot.example.apis.R.menu;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionJointController;
import com.fpt.robot.motion.RobotMotionLocomotionController;
import com.fpt.robot.types.RobotMoveTargetPosition;
import com.fpt.robot.ui.RobotActivity;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MotionLocomotionController extends RobotActivity {
	
	private Button moveTo;
	private Button setWalk;
	private Button stand_Up;
	private Button stopWalkBt;
	

	private EditText distanceX;
	private EditText distanceY;
	private EditText targetAngle;
	
	private EditText stepX;
	private EditText stepY;
	private EditText stepAngle;
	private EditText speed;
	

	private TextView RposX;
	private TextView RposY;
	private TextView RangleTheta;
	
	
	

	private RobotMoveTargetPosition currentRobotPosition=new RobotMoveTargetPosition(0.0f, 0.0f, 0.0f);
	private float distanceXValue;
	private float distanceYValue;
	private float targetAngleValue;
	private float stepXValue;
	private float stepYValue;
	private float stepAngleValue;
	private float speedValue;
	
	private Robot mRobot;
	

	private Timer timerHandlingUpdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_locomotion_controller);

		RposX = (TextView) findViewById(R.id.tvRPositionX);
		RposY = (TextView) findViewById(R.id.tvRPositionY);
		RangleTheta = (TextView) findViewById(R.id.tvRPositionZ);
		
		
		moveTo = (Button) findViewById(R.id.btn_MoveTo);
		setWalk = (Button) findViewById(R.id.btn_SetWalk);
		stand_Up = (Button) findViewById(R.id.btn_standUp);
		stopWalkBt = (Button) findViewById(R.id.btn_stopWalk);
		
		distanceX = (EditText) findViewById(R.id.editDistanceX);
		distanceY = (EditText) findViewById(R.id.editDistanceY);
		targetAngle = (EditText) findViewById(R.id.editTargetAngle);
		
		stepX = (EditText) findViewById(R.id.editStepX);
		stepY = (EditText) findViewById(R.id.editStepY);
		stepAngle = (EditText) findViewById(R.id.editStepTheta);
		speed = (EditText) findViewById(R.id.editSpeed);
		
		
		mRobot = getRobot();
		
		timerHandlingUpdate = new Timer();
		timerHandlingUpdate.schedule(new TimerTask() {
		public void run() {
			UpdateJointAngle();
			}

		}, 0, 1000);
		

		setWalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				stepXValue= Float.parseFloat(stepX.getText().toString());
				stepYValue = Float.parseFloat(stepY.getText().toString());
				stepAngleValue= Float.parseFloat(stepAngle.getText().toString());
				speedValue= Float.parseFloat(speed.getText().toString());
				
				//final RobotMoveTargetPosition target = new RobotMoveTargetPosition(0.5f, 0.0f, 0.0f);
				//Log.i(getPackageName()," moveTo " );
					new Thread(new Runnable() {
						@Override
						public void run() {
							//final Robot mRobot = getRobot();
							RobotMoveTargetPosition target = new RobotMoveTargetPosition(stepXValue, stepYValue, stepAngleValue);
							try {
								Log.i(getPackageName()," moveTo " );
								RobotMotionLocomotionController.setWalkTargetVelocity(mRobot, target, speedValue);
							} catch (RobotException e) {
								e.printStackTrace();
							}
						}
					}).start();


			}
		});
		
		moveTo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				distanceXValue = Float.parseFloat(distanceX.getText().toString());
				distanceYValue= Float.parseFloat(distanceY.getText().toString());
				targetAngleValue = Float.parseFloat(targetAngle.getText().toString());
				final float x=distanceXValue;
				final float y=distanceYValue;
				final float th=targetAngleValue;
				
				
					new Thread(new Runnable() {
						@Override
						public void run() {
							//final Robot mRobot = getRobot();
							RobotMoveTargetPosition target = new RobotMoveTargetPosition(x, y, th);
							try {
								Log.i(getPackageName()," moveTo " );
								RobotMotionLocomotionController.moveTo(mRobot,target);
							} catch (RobotException e) {
								e.printStackTrace();
							}
						}
					}).start();


			}
		});
		
		stand_Up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					@Override
					public synchronized void run() {
						try {
							// execute function
							Log.i(getClass().getName(), "Start standing up");
							RobotMotionAction.standUp(mRobot, 0.5f);
						} catch (RobotException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
			}
		});
		
		stopWalkBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// execute function
							RobotMotionLocomotionController.moveStop(mRobot);
							Log.i(getClass().getName(), "Start sitting down");
							//RobotMotionAction.sitDown(mRobot, 0.5f);

						} catch (RobotException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
			}
		});
		
	}
	private void UpdateJointAngle() {
		// This method is called directly by the timer and runs in the same thread as the timer.

		// We call the method that will work with the UI through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {		
		public void run() {	
			new Thread(new Runnable() {
				@Override
				public synchronized void run() {
					updatePosition();
				}
			}).start();
			
			RposX.setText(String.format("%.2f", currentRobotPosition.x));
			RposY.setText(String.format("%.2f", currentRobotPosition.y));			
			RangleTheta.setText(String.format("%.2f", currentRobotPosition.theta));
		}
		
	};
	private void updatePosition() {
		try {
			// execute function
			currentRobotPosition = RobotMotionLocomotionController
					.getRobotPosition(mRobot, false);
			Log.i(getClass().getSimpleName(),
					"get current robot position " +currentRobotPosition.x+" "+currentRobotPosition.y);
			} catch (RobotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locomotion_controller, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_locomotion_controller;
	}

}
