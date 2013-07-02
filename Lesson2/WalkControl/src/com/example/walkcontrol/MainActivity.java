package com.example.walkcontrol;

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
import com.fpt.robot.motion.RobotMotionLocomotionController;
import com.fpt.robot.types.RobotMoveTargetPosition;
import com.fpt.robot.ui.RobotActivity;
public class MainActivity extends RobotActivity {

	private EditText dX;
	private EditText dY;
	private EditText dT;

	private Button move;
	private Button stop;

	float f1,f2,f3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		dX = (EditText) findViewById(R.id.editTextDX);
		dY = (EditText) findViewById(R.id.editTextDY);
		dT = (EditText) findViewById(R.id.editTextDT);

		move = (Button) findViewById(R.id.bMove);
		stop = (Button) findViewById(R.id.bStop);
		
		move.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Toast.makeText(MainActivity.this, "goto here", Toast.LENGTH_SHORT).show();
				f1 = Float.parseFloat(dT.getText().toString());
				f2 = Float.parseFloat(dY.getText().toString());
				f3 = Float.parseFloat(dX.getText().toString());
				final Robot mRobot = getRobot();
				final float x = f3;
				final float y = f2;
				final float t = f1;
				//final RobotMoveTargetPosition target = new RobotMoveTargetPosition(0.5f, 0.0f, 0.0f);
				//Log.i(getPackageName()," moveTo " );
					new Thread(new Runnable() {
						@Override
						public void run() {
							//final Robot mRobot = getRobot();
							RobotMoveTargetPosition target = new RobotMoveTargetPosition(x, y, t);
							try {
								Log.i(getPackageName()," moveTo " );
								RobotMotionLocomotionController.moveTo(mRobot,target);
							} catch (RobotException e) {
								e.printStackTrace();
								Toast.makeText(MainActivity.this, "Can not run MoveTo()", Toast.LENGTH_SHORT).show();
							}
						}
					}).start();


			}
		});

		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						final Robot mRobot = getRobot();
						try {
							RobotMotionLocomotionController.moveStop(mRobot);
						} catch (RobotException e) {
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
