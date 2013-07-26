package com.fpt.robot.example.apis.motion;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.motion.RobotMotionCartesianController;
import com.fpt.robot.motion.RobotMotionSelfCollisionProtection;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.types.RobotPosition6D;
import com.fpt.robot.ui.RobotActivity;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MotionPositionChange extends RobotActivity {
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
	private EditText speed;
	
	private String nameValue;
	private int spaceValue;
	private float posXValue;
	private float posYValue;
	private float posZValue;
	private float posWxValue;
	private float posWyValue;
	private float posWzValue;
	private int axisMarkValue;
	private float speedValue;
	private String jN;
	private Timer timerHandlingUpdate;

	private RobotPosition6D currentRobotPosition=new RobotPosition6D(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	
	private TextView vPosX;
	private TextView vPosY;
	private TextView vPosZ;
	private TextView vPosWx;
	private TextView vPosWy;
	private TextView vPosWz;
	
	private Robot mRobot;
	
	private boolean isStiffNessRunning=true;
	
	private RobotPosition6D originalPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_change_position);
		runBt = (Button) findViewById(R.id.btn_runChangePosition);
		returnBt = (Button) findViewById(R.id.btn_returnChangePosition);
		setStiffnessBt= (Button) findViewById(R.id.btn_stiffnessOnChangePosition);
		
		name = (EditText) findViewById(R.id.editJointNameChangePosition);
		space = (EditText) findViewById(R.id.editSpaceChangePosition);
		posX=(EditText) findViewById(R.id.editXChangePosition);
		posY=(EditText) findViewById(R.id.editYChangePosition);
		posZ=(EditText) findViewById(R.id.editZChangePosition);
		posWx=(EditText) findViewById(R.id.editWxChangePosition);
		posWy=(EditText) findViewById(R.id.editWyChangePosition);
		posWz=(EditText) findViewById(R.id.editWzChangePosition);
		axisMark = (EditText) findViewById(R.id.editAxisMaskChangePosition);
		speed = (EditText) findViewById(R.id.EditSpeedChangePosition);
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
				speedValue=Float.parseFloat(speed.getText().toString());
				
				final String jName = nameValue;//Get joint name from editText box of joint name 
				final int sp =spaceValue;//get angle value from editText box of angle
				final int aM = axisMarkValue;//Get duration form editText box of time
				final float speed1=speedValue;
				//handler.sendEmptyMessage(0);
				
				final RobotPosition6D positions = new RobotPosition6D(posXValue, posYValue, 
						posZValue, posWxValue, posWyValue, posWzValue);
				
				new Thread(new Runnable() {
				@Override
				  public void run() {
					try {
						//execute function
						originalPosition=RobotMotionCartesianController.getPosition(mRobot,jName,sp,false);
						Log.i(getClass().getName(), "Start Cartesian Control");
						//RobotMotionSelfCollisionProtection.setEnable(mRobot, jName, true);
						RobotMotionCartesianController.changePosition(mRobot, jName, sp, positions, aM, speed1);
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
				speedValue=Float.parseFloat(speed.getText().toString());
				//handler.sendEmptyMessage(0);
				
				final Robot mRobot = getRobot();
				final String jName = nameValue;//Get joint name from editText box of joint name 
				final int sp =spaceValue;//get angle value from editText box of angle
				final int aM = axisMarkValue;//Get duration form editText box of time
				final float speed1=speedValue;
				new Thread(new Runnable() {
					@Override
					  public void run() {
						try {
							//execute function
							Log.i(getClass().getName(), "Return");
							//RobotMotionSelfCollisionProtection.setEnable(mRobot, jName, true);
							RobotMotionCartesianController.changePosition(mRobot, jName, sp, originalPosition, aM, speed1);
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
				
				//handler.sendEmptyMessage(0);
				if(isStiffNessRunning==true){

					final Robot mRobot = getRobot();// get robot from robot service
					jN = name.getText().toString();
					final String[] jName = {jN};// Get joint name from editText box of joint name
					final float[] st ={1.0f};// Get stiffness from editText box
					
					new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									//Execute function
									Log.i(getPackageName(),"stiffness on" );
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
	
	/*Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what==0) {				
				vPosX.setText(String.format("%.2f", currentRobotPosition.x));
				vPosY.setText(String.format("%.2f", currentRobotPosition.y));			
				vPosX.setText(String.format("%.2f", currentRobotPosition.z));
				vPosWx.setText(String.format("%.2f", currentRobotPosition.wx));
				vPosWy.setText(String.format("%.2f", currentRobotPosition.wy));			
				vPosWz.setText(String.format("%.2f", currentRobotPosition.wz));
				timerHandlingUpdate = new Timer();
				timerHandlingUpdate.schedule(new TimerTask() {
				public void run() {
					updatePosition();
					}
	
				}, 0, TIMER_INTERVAL);
			} else {
				timerHandlingUpdate = new Timer();
				timerHandlingUpdate.schedule(new TimerTask() {
				public void run() {
					updatePosition();
					}

				}, 0, TIMER_INTERVAL);	
			}
		};
	};
	*/
	//private int TIMER_INTERVAL=1000;
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
		return R.layout.activity_position_change;
	}

}
