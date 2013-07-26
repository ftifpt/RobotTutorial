package com.fpt.robot.example.apis.motion;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.leds.RobotLedEmotion;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.ui.RobotActivity;

public class MotionGestureController extends RobotActivity {
	
	private Button getListGesture;
	private Button runGesture;
	private Button standUp;
	private Button sitDown;
	private ListView lvGestures;
	private ListView lvLedEmotions;
	
	private EditText gName;
	private EditText lName;
	
	private Robot mRobot;
	private String gestureName;
	private String ledName;
	
	
	private String[] gestureList;
	private String[] getAnimationList={"Angry","Annoyed","Anxious","Attention","Bored",
										"Cautious","Confident","Confused","Determined","Disappoint",
										"Enthusiastic","Excited","Exhausted","Fear","Frustrated",
										"Happy","Hey","Humilliated","Hurt","Innocent",
										"Interested","Late","Laugh","Laugh2","Lonely",
										"Optimistic","Proud","Relieved","Sad","Shocked",
										"Shy","Sure","Surprise","Suspicious","Winner"};
	
	
	private ArrayList<String> lvGestureData,lvLedsData;
	private ArrayAdapter<String> adapterGestureList,adapterLedsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_gesture_motion);
		getListGesture = (Button) findViewById(R.id.btn_getGestureList);
		runGesture = (Button) findViewById(R.id.btn_RunGesture);
		sitDown= (Button) findViewById(R.id.btn_sitDown);
		standUp= (Button) findViewById(R.id.btn_standUp);
		
		lvGestures = (ListView) findViewById(R.id.lvGestures);
		lvLedEmotions=(ListView) findViewById(R.id.lvLeds);
		TextView tv = (TextView) findViewById(android.R.id.empty);
		lvGestures.setEmptyView(tv);
		
		gName=(EditText) findViewById(R.id.editGestureName);
		lName=(EditText) findViewById(R.id.editLedsName);
		
		mRobot=getRobot();
		
		lvLedEmotions.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {              
	        	lName.setText(getAnimationList[position]); 	
	        	              

	        }
	    });
		
		lvGestures.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)  {              
	        	gName.setText(gestureList[arg2]); 		         

	        }
	    });
		
		
		
		
		runGesture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gestureName=gName.getText().toString();
				ledName=lName.getText().toString();
				new Thread(new Runnable() {
					@Override
					public synchronized void run() {
						try {
							// execute function
							Log.i(getClass().getName(), "Start gesture");
							RobotGesture.runGesture(mRobot, gestureName);
						} catch (RobotException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
				
				new Thread (new Runnable() {
	    			
	    			@Override
	    			public void run() {
	    				// TODO Auto-generated method stub
	    				try {
	    					RobotLedEmotion.startEmotion(mRobot,ledName);
	    					
	    				} catch (RobotException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    			}
	    		}).start(); 
			}
		});
		
		getListGesture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					@Override
					public synchronized void run() {
						try {
							// execute function
							Log.i(getClass().getName(), "Start standing up");
							
							gestureList=RobotGesture.getGestureList(mRobot);
							
							//getAnimationList={"";""};
							
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if(gestureList !=null){
										lvGestureData = new ArrayList<String>();
										for (int count = 0;count<gestureList.length;count++) {
											lvGestureData.add(String.valueOf(count+"."+gestureList[count]));
										}				
										adapterGestureList = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, lvGestureData);
										lvGestures.setAdapter(adapterGestureList);
										
									}
									
								}
							});
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if(gestureList !=null){
										lvLedsData = new ArrayList<String>();
										for (int count1 = 0;count1<getAnimationList.length;count1++) {
											lvLedsData.add(String.valueOf(count1+"."+getAnimationList[count1]));
										}				
										adapterLedsList = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, lvLedsData);
										lvLedEmotions.setAdapter(adapterLedsList);
										
									}
									
								}
							});
						} catch (RobotException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
				
				new Thread (new Runnable() {
	    			
	    			@Override
	    			public void run() {
	    				// TODO Auto-generated method stub
	    				try {
	    					RobotLedEmotion.stopEmotion(mRobot);
	    					
	    				} catch (RobotException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    			}
	    		}).start(); 
				
				
			}
		});
		
		standUp.setOnClickListener(new OnClickListener() {

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
		
		sitDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String[] jName = {"Body"};// Get joint name from editText box of joint name
				final float[] st ={0.0f};// Get stiffness from editText box
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// execute function
							Log.i(getClass().getName(), "Start sitting down");
							RobotMotionAction.sitDown(mRobot, 0.5f);
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
		getMenuInflater().inflate(R.menu.gesture_motion, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_gesture_motion;
	}

	
}
