package com.fpt.robot.example.apis.vision;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.ui.RobotActivity;
import com.fpt.robot.vision.RobotCamera;

public class GetCameraImage extends RobotActivity {
	private static final String TAG = "GetCameraImage";
    
    private ImageView ivCameraImage;    
    private Button btGetImage;
    
    RobotCamera mCamera;
    private static final int IMAGE_WIDTH = 640;
    private static final int IMAGE_HEIGHT = 480;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_get_camera_image);

		ivCameraImage = (ImageView)findViewById(R.id.ivCameraImage);
		
		btGetImage = (Button)findViewById(R.id.btGetImage);
		btGetImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getCameraImage();
            }
        });
        
        mCamera = RobotCamera.getCamera(getRobot(), RobotCamera.CAMERA_BOTTOM);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_camera_image, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_get_camera_image;
	}

	protected void getCameraImage() {
		new Thread(new Runnable() {
            
            @Override
            public void run() {
            	byte[] image = null;
                showProgress("getting image from robot camera...");
                try {
                	mCamera.setConfig(RobotCamera.PICTURE_RESOLUTION_QVGA, 
                			RobotCamera.PICTURE_COLORSPACE_GRAY);
                	image = mCamera.getImage();
                } catch (RobotException e) {
                    e.printStackTrace();
                    cancelProgress();
                    makeToast(e.getMessage());
                    return;
                }
                cancelProgress();
                if (image == null || image.length == 0) {
                    makeToast("Can not get image from robot camera!");
                    return;
                } else {
                	makeToast("Get image from robot camera successfully!");
                	// convert image to Bitmap object
    				ByteArrayOutputStream out = new ByteArrayOutputStream();
    				YuvImage yuvImage = new YuvImage(image, ImageFormat.YUY2, 
    						IMAGE_WIDTH, IMAGE_HEIGHT, null);
    				yuvImage.compressToJpeg(new Rect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT), 80, out);
    				byte[] imageBytes = out.toByteArray();
    				final Bitmap bitmap = decodeSampledBitmap(imageBytes, 480, 320);
    				if (bitmap != null) {
    					runOnUiThread(new Runnable() {
    		                public void run() {
    		                	ivCameraImage.setImageBitmap(bitmap);
    		                }
    		            });
    				}
    				try {
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
    				out = null;
                }
            }
        }).start();
	}

	private static synchronized Bitmap decodeSampledBitmap(byte[] b,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(b, 0, b.length, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(b, 0, b.length, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
			final float totalPixels = width * height;
			// Anything more than 2x the requested pixels we'll sample down
			// further.
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

    private ProgressDialog progressDialog = null;
    
    protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(GetCameraImage.this);
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
                Toast.makeText(GetCameraImage.this, toast, 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
