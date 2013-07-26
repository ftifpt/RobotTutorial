
package com.fpt.robot.example.apis.vision;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.ui.RobotActivity;
import com.fpt.robot.vision.RobotCamera;

public class TakePicture extends RobotActivity {
    private static final String TAG = "TakePicture";
    
    private ImageView ivTakenPicture;    
    private Button btTakePicture;
    private TextView tvTakenPicturePath;

    RobotCamera mCamera;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_take_picture);
        
        tvTakenPicturePath = (TextView)findViewById(R.id.tvTakenPicturePath);
        ivTakenPicture = (ImageView)findViewById(R.id.ivTakenPicture);

        btTakePicture = (Button)findViewById(R.id.btTakePicture);
        btTakePicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        
        mCamera = RobotCamera.getCamera(getRobot(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.take_picture, menu);
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_take_picture;
    }
    
    protected void takePicture() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                String picture = null;
                showProgress("taking picture...");
                try {
                	mCamera.setConfig(RobotCamera.PICTURE_RESOLUTION_VGA, 
                			RobotCamera.PICTURE_COLORSPACE_GRAY);
                    picture = mCamera.takePicture(RobotCamera.PICTURE_RESOLUTION_VGA, 
                            RobotCamera.PICTURE_FORMAT_JPG);
                } catch (RobotException e) {
                    e.printStackTrace();
                    cancelProgress();
                    makeToast(e.getMessage());
                    return;
                }
                cancelProgress();
                if (picture == null || picture.isEmpty()) {
                    Log.e(TAG, "can not take picture!");
                    makeToast("Can not take picture!");
                    return;
                } else {
                    Log.d(TAG, "Picture saved to " + picture + "!");
                    displayPicture(picture);
                }
            }
        }).start();
    }
    
    protected void displayPicture(final String picture) {
        final String picturePath = picture; 
        final Bitmap bm = BitmapFactory.decodeFile(picturePath);
        if (bm != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ivTakenPicture.setImageBitmap(bm);
                    tvTakenPicturePath.setText(picture);
                }
            });
        } else {
            makeToast("Picture saved to " + picturePath + "!");
        }
    }
    
    private ProgressDialog progressDialog = null;
    
    protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(TakePicture.this);
                }
                // no title
                if (message != null) {
                    progressDialog.setMessage(message);
                }
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void cancelProgress() {
        //Log.d(TAG, "cancelProgress()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    //progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }
        });
    }
    
    protected void makeToast(final String toast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TakePicture.this, toast, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
