/*
 * Copyright (C) 2013 FPT Corporation
 * Author: Robot Team (FTI)
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cartesiancontrol;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotMotionCartesianController;
import com.fpt.robot.types.RobotPosition6D;
import com.fpt.robot.ui.RobotActivity;

public class MainActivity extends RobotActivity {

	
	private Button runBt;
	private Button returnBt;

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
	int spaceValue;
	float posXValue;
	float posYValue;
	float posZValue;
	float posWxValue;
	float posWyValue;
	float posWzValue;
	int axisMarkValue;
	float durationValue;
	boolean isAbsolutevalue;
	
	//RobotPosition6D[] position6D = new RobotPosition6D[6];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		runBt = (Button) findViewById(R.id.btn_Run);
		returnBt = (Button) findViewById(R.id.btn_return);
		
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
				 
		runBt.setOnClickListener(new OnClickListener() {
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
				final Robot mRobot = getRobot();
				final String jName = nameValue;//Get joint name from editText box of joint name 
				final int sp =spaceValue;//
				final int aM = axisMarkValue;//
				final float[] dr={durationValue};
				final boolean iA = isAbsolutevalue;//
				final RobotPosition6D[] positions = new RobotPosition6D[1];
				RobotPosition6D pos1 = new RobotPosition6D(posXValue, posYValue, 
						posZValue, posWxValue, posWyValue, posWzValue);
				positions[0] = pos1;
				
				new Thread(new Runnable() {
				@Override
				  public void run() {
					try {
						//execute function								
						RobotMotionCartesianController.positionInterpolation(mRobot, jName, sp, positions, aM, dr, iA);
						
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
				final Robot mRobot = getRobot();
				final String jName = nameValue;//Get joint name from editText box of joint name 
				final int sp =spaceValue;//
				final int aM = axisMarkValue;//
				final float[] dr={durationValue};
				final boolean iA = isAbsolutevalue;//
				final RobotPosition6D[] positions = new RobotPosition6D[1];
				RobotPosition6D pos1 = new RobotPosition6D(0.0f, 0.0f, 
						0.0f, 0.0f, 0.0f, 0.0f);
				positions[0] = pos1;
				new Thread(new Runnable() {
					@Override
					  public void run() {
						try {
							//execute function								
							RobotMotionCartesianController.positionInterpolation(mRobot, jName, sp, positions, aM, dr, iA);
							
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
