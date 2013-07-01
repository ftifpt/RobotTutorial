package com.fpt.robot.example.robotmotiondemoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.motion.RobotMotionLocomotionController;
import com.fpt.robot.motion.RobotMoveTargetPosition;
import com.fpt.robot.ui.RobotActivity;

public class MainActivity extends RobotActivity {

	private EditText vX;
	private EditText vY;
	private EditText vT;
	private EditText vF;

	private EditText dX;
	private EditText dY;
	private EditText dT;

	private Button walk;
	private Button move;
	private Button stop;

	private boolean vSet = false;
	private boolean dSet = false;
	private TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		vX = (EditText) findViewById(R.id.editTextX);
		vY = (EditText) findViewById(R.id.editTextY);
		vT = (EditText) findViewById(R.id.editTextT);
		vF = (EditText) findViewById(R.id.editTextF);

		dX = (EditText) findViewById(R.id.editTextDX);
		dY = (EditText) findViewById(R.id.editTextDY);
		dT = (EditText) findViewById(R.id.editTextDT);

		status = (TextView) findViewById(R.id.textStatus);

		walk = (Button) findViewById(R.id.bWalk);
		move = (Button) findViewById(R.id.bMove);
		stop = (Button) findViewById(R.id.bStop);

		walk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				status.setText("");
				float x = getVx();
				float y = getVy();
				float t = getVt();
				float f = getVf();
				if (vSet) {
					vSet = false;
					// Log.i(getPackageName(),
					// " Valule of Walk " + String.valueOf(x)
					// + String.valueOf(y) + String.valueOf(t)
					// + String.valueOf(f));
					final Robot mRobot = getRobot();
					RobotMoveTargetPosition target = new RobotMoveTargetPosition(
							x, y, t);
					try {
						RobotMotionLocomotionController.setWalkTargetVelocity(
								mRobot, target, f);
					} catch (RobotException e) {
						e.printStackTrace();
					}
				} else {
					status.setText("Please set correct value !");
				}
			}
		});

		move.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float x = getDx();
				float y = getDy();
				float t = getDt();
				if (dSet) {
					dSet = false;
					// Log.i(getClass().getName(),
					// "Value of Move " + String.valueOf(x)
					// + String.valueOf(y) + String.valueOf(t));
					final Robot mRobot = getRobot();
					RobotMoveTargetPosition target = new RobotMoveTargetPosition(
							x, y, t);
					try {
						RobotMotionLocomotionController.moveTo(mRobot, target);
					} catch (RobotException e) {
						e.printStackTrace();
					}
				} else {
					status.setText("Please set correct value !");
				}

			}
		});

		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Robot mRobot = getRobot();
				try {
					RobotMotionLocomotionController.moveStop(mRobot);
				} catch (RobotException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private float getVf() {
		if (vF.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(vF.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 1.0f || f < -1.0f) {
			vF.setText("");
			vF.setHint("Choice Value between [0,1]");
			return 0;
		} else {
			vSet = true;
			return f;
		}
	}

	private float getDt() {
		if (dT.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(dT.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 3.14f || f < -3.14f) {
			dT.setText("");
			dT.setHint("Choice Value between [-3.14,3.14]");
			return 0;
		} else {
			dSet = true;
			return f;
		}
	}

	private float getDy() {
		if (dY.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(dY.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 5.0f || f < -5.0f) {
			dY.setText("");
			dY.setHint("Choice Value between [-5,5]");
			return 0;
		} else {
			dSet = true;
			return f;
		}
	}

	private float getDx() {
		if (dX.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(dX.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 10.0f || f < -5.0f) {
			dX.setText("");
			dX.setHint("Choice Value between [-5,10]");
			return 0;
		} else {
			dSet = true;
			return f;
		}
	}

	private float getVt() {
		if (vT.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(vT.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 1.0f || f < -1.0f) {
			vT.setText("");
			vT.setHint("Choice Value between [-1,1]");
			return 0;
		} else {
			vSet = true;
			return f;
		}
	}

	private float getVy() {
		if (vY.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(vY.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 1.0f || f < -1.0f) {
			vY.setText("");
			vY.setHint("Choice Value between [-1,1]");
			return 0;
		} else {
			vSet = true;
			return f;
		}
	}

	private float getVx() {
		if (vX.getText().toString() == null)
			return 0;
		float f = Float.parseFloat(vX.getText().toString());
		Log.i(getClass().getName(), "Value: " + String.valueOf(f));
		if (f > 1.0f || f < -1.0f) {
			vX.setText("");
			vX.setHint("Choice Value between [-1,1]");
			return 0;
		} else {
			vSet = true;
			return f;
		}
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
