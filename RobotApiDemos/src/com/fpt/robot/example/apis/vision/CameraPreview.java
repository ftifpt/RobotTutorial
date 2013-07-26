package com.fpt.robot.example.apis.vision;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.ui.RobotActivity;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;

public class CameraPreview extends RobotActivity {
	private static final String TAG = "CameraPreview";

	private Button btStartCameraPreview;
	private Button btStopCameraPreview;
	
	private RobotCameraPreview mRobotCameraPreview;
	private CameraPreviewView mCameraPreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_camera_preview);
		
		btStartCameraPreview = (Button)findViewById(R.id.btStartCameraPreview);
        btStartCameraPreview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startCameraPreview();
			}
		});
        
        btStopCameraPreview = (Button)findViewById(R.id.btStopCameraPreview);
        btStopCameraPreview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopCameraPreview();
			}
		});
        
        mCameraPreview = (CameraPreviewView)findViewById(R.id.cameraPreview);
        mRobotCameraPreview = RobotCamera.getCameraPreview(getRobot(), 0);
        mRobotCameraPreview.setPreviewDisplay(mCameraPreview.getHolder());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_preview, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_camera_preview;
	}

	protected void startCameraPreview() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean result = false;
				try {
					result = mRobotCameraPreview.startPreview();
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("start camera preview failed! " + e.getMessage());
					return;
				}
				if (result) {
					makeToast("camera preview started!");
				} else {
					makeToast("start camera preview failed!");
				}
			}
		}).start();
	}

	protected void stopCameraPreview() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean result = false;
				try {
					result = mRobotCameraPreview.stopPreview();
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("stop camera preview failed! " + e.getMessage());
					return;
				}
				if (result) {
					makeToast("camera preview stopped!");
				} else {
					makeToast("stop camera preview failed!");
				}
			}
		}).start();
	}
	
	protected void makeToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(CameraPreview.this, toast, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
