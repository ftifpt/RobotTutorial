package com.fpt.robot.example.apis.motion;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.motion.RobotMotionCartesianController;
import com.fpt.robot.motion.RobotMotionSelfCollisionProtection;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.types.RobotPosition6D;
import com.fpt.robot.ui.RobotActivity;

public class MotionPositionInterpolation extends RobotActivity {
	private Button runBt;
	private Button returnBt;
	private Button setStiffnessBt;

	private EditText name;
	private EditText space;
	private EditText posX;
	private EditText posY;
	private EditText posZ;
	private EditText posWx;
	private EditText posWy;
	private EditText posWz;
	private EditText axisMark;
	private EditText duration;
	private EditText isAbsolute;
	
	private String nameValue;
	private int spaceValue;
	private float posXValue;
	private float posYValue;
	private float posZValue;
	private float posWxValue;
	private float posWyValue;
	private float posWzValue;
	private int axisMarkValue;
	private float durationValue;
	private boolean isAbsolutevalue;
	private String jN;
	
	private TextView vPosX;
	private TextView vPosY;
	private TextView vPosZ;
	private TextView vPosWx;
	private TextView vPosWy;
	private TextView vPosWz;
	private Robot mRobot;

	private Timer timerHandlingUpdate;

	private RobotPosition6D currentRobotPosition=new RobotPosition6D(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	
	private boolean isStiffNessRunning=true;
	private RobotPosition6D[] originalPosition = new RobotPosition6D[1];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_position_interpolation);
		runBt = (Button) findViewById(R.id.btn_run);
		returnBt = (Button) findViewById(R.id.btn_return);
		setStiffnessBt= (Button) findViewById(R.id.btn_stiffnessOn);
		
		name = (EditText) findViewById(R.id.editJointName);
		space = (EditText) findViewById(R.id.editSpace);
		posX=(EditText) findViewById(R.id.editX);
		posY=(EditText) findViewById(R.id.editY);
		posZ=(EditText) findViewById(R.id.editZ);
		posWx=(EditText) findViewById(R.id.editWx);
		posWy=(EditText) findViewById(R.id.editWy);
		posWz=(EditText) findViewById(R.id.editWz);
		axisMark = (EditText) findViewById(R.id.editAxisMask);
		duration = (EditText) findViewById(R.id.EditDuration);
		isAbsolute = (EditText) findViewById(R.id.editIsAbsolute);
		
		nameValue = name.getText().toString();
		spaceValue = Integer.parseInt(space.getText().toString());
		posXValue = Float.parseFloat(posX.getText().toString());
		posYValue = Float.parseFloat(posY.getText().toString());
		posZValue = Float.parseFloat(posZ.getText().toString());
		posWxValue = Float.parseFloat(posWx.getText().toString());
		posWyValue = Float.parseFloat(posWy.getText().toString());
		posWzValue = Float.parseFloat(posWz.getText().toString());
		axisMarkValue=Integer.parseInt(axisMark.getText().toString());
		durationValue=Float.parseFloat(duration.getText().toString());
		isAbsolutevalue = Boolean.parseBoolean(isAbsolute.getText().toString());
		
		
		mRobot = getRobot();

		
		vPosX = (TextView) findViewById(R.id.TextViewX);
		vPosY = (TextView) findViewById(R.id.TextViewY);
		vPosZ = (TextView) findViewById(R.id.TextViewZ);
		vPosWx = (TextView) findViewById(R.id.TextViewWx);
		vPosWy = (TextView) findViewById(R.id.TextViewWy);
		vPosWz = (TextView) findViewById(R.id.TextViewWz);
		
		timerHandlingUpdate = new Timer();
		timerHandlingUpdate.schedule(new TimerTask() {
		public void run() {
			UpdatePartPosition();
			}

		}, 0, 1000);
				 
		runBt.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				//status.setText("");
				nameValue = name.getText().toString();
				spaceValue = Integer.parseInt(space.getText().toString());
				posXValue = Float.parseFloat(posX.getText().toString());
				posYValue = Float.parseFloat(posY.getText().toString());
				posZValue = Float.parseFloat(posZ.getText().toString());
				posWxValue = Float.parseFloat(posWx.getText().toString());
				posWyValue = Float.parseFloat(posWy.getText().toString());
				posWzValue = Float.parseFloat(posWz.getText().toString());
				axisMarkValue=Integer.parseInt(axisMark.getText().toString());
				durationValue=Float.parseFloat(duration.getText().toString());
				isAbsolutevalue = Boolean.parseBoolean(isAbsolute.getText().toString());
				
				final float[] dr={durationValue};
				
				final RobotPosition6D[] positions = new RobotPosition6D[1];
				RobotPosition6D pos1 = new RobotPosition6D(posXValue, posYValue, 
						posZValue, posWxValue, posWyValue, posWzValue);
				
				positions[0] = pos1;

				try {
					originalPosition[0]=RobotMotionCartesianController.getPosition(mRobot,nameValue,spaceValue,false);
				} catch (RobotException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				new Thread(new Runnable() {
				@Override
				  public void run() {
					try {
						//execute function
						
						Log.i(getClass().getName(), "Start Cartesian Control");
						//RobotMotionSelfCollisionProtection.setEnable(mRobot, jName, true);
						RobotMotionCartesianController.positionInterpolation(mRobot, nameValue, spaceValue, 
								positions, axisMarkValue, dr, isAbsolutevalue);
						//RobotMotionSelfCollisionProtection.setEnable(mRobot, jName, false);
						} catch (RobotException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}).start();
			}

		});
		returnBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nameValue = name.getText().toString();
				spaceValue = Integer.parseInt(space.getText().toString());
				posXValue = Float.parseFloat(posX.getText().toString());
				posYValue = Float.parseFloat(posY.getText().toString());
				posZValue = Float.parseFloat(posZ.getText().toString());
				posWxValue = Float.parseFloat(posWx.getText().toString());
				posWyValue = Float.parseFloat(posWy.getText().toString());
				posWzValue = Float.parseFloat(posWz.getText().toString());
				axisMarkValue=Integer.parseInt(axisMark.getText().toString());
				durationValue=Float.parseFloat(duration.getText().toString());
				isAbsolutevalue = Boolean.parseBoolean(isAbsolute.getText().toString());
				
				
				final float[] dr={durationValue};
				
				new Thread(new Runnable() {
					@Override
					  public void run() {
						try {
							//execute function
							Log.i(getClass().getName(), "Return");
							//RobotMotionSelfCollisionProtection.setEnable(mRobot, jName, true);
							RobotMotionCartesianController.positionInterpolation(mRobot, nameValue, 
									spaceValue, originalPosition, axisMarkValue, dr, isAbsolutevalue);
							
							//RobotMotionSelfCollisionProtection.setEnable(mRobot, jName, false);
							} catch (RobotException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}).start();
			}
		});
		
		setStiffnessBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(isStiffNessRunning==true){

					jN = name.getText().toString();
					final String[] jName = {jN};// Get joint name from editText box of joint name
					final float[] st ={1.0f};// Get stiffness from editText box
					
					new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									//Execute function
									Log.i(getPackageName()," stiffness on " );
									RobotMotionSelfCollisionProtection.setEnable(mRobot, "Body", true);
									RobotMotionStiffnessController.setStiffnesses(mRobot, jName, st);
									} catch (RobotException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}).start();
				}
				else {
					final Robot mRobot = getRobot();// get robot from robot service
					jN = name.getText().toString();
					final String[] jName = {jN};// Get joint name from editText box of joint name
					final float[] st ={0.0f};//
					
					new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									//Execute function
									Log.i(getPackageName()," stiffness off " );
									RobotMotionSelfCollisionProtection.setEnable(mRobot, "Body", false);
									RobotMotionStiffnessController.setStiffnesses(mRobot, jName, st);
									} catch (RobotException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}).start();
				
					
				}
				if(isStiffNessRunning==false){
					isStiffNessRunning=true;
					setStiffnessBt.setText("Stiffness Off/On");
					setStiffnessBt.setBackgroundResource(R.drawable.color_green);
				}
				else {
					isStiffNessRunning=false;
					setStiffnessBt.setText("Stiffness On/Off");
					setStiffnessBt.setBackgroundResource(R.drawable.color_red);
				}
			}

			
		});
	}
	private void UpdatePartPosition() {
		// This method is called directly by the timer
		// and runs in the same thread as the timer.

		// We call the method that will work with the UI
		// through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {
		
		
		public void run() {

			// This method runs in the same thread as the UI.

			// Do something to the UI thread here
			if (currentRobotPosition != null) {
				vPosX.setText(String.format("%.2f", currentRobotPosition.x));
				vPosY.setText(String.format("%.2f", currentRobotPosition.y));			
				vPosZ.setText(String.format("%.2f", currentRobotPosition.z));
				vPosWx.setText(String.format("%.2f", currentRobotPosition.wx));
				vPosWy.setText(String.format("%.2f", currentRobotPosition.wy));			
				vPosWz.setText(String.format("%.2f", currentRobotPosition.wz));
			}
			new Thread(new Runnable() {
				
				@Override
				public synchronized void run() {
					updatePosition();
				}
			}).start();
			
			
			
		}
	};
private void updatePosition() {
		
		nameValue = name.getText().toString();
		spaceValue = Integer.parseInt(space.getText().toString());
		try {
			// execute function
			currentRobotPosition = RobotMotionCartesianController.getPosition(mRobot, nameValue, spaceValue, false);
//			Log.i(getClass().getSimpleName(),
//					"get current robot position " +currentRobotPosition.x+" "+currentRobotPosition.y);
			} catch (RobotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	

	@Override
	protected int getLayoutID() {
		return R.layout.activity_position_interpolation;
	}

}
