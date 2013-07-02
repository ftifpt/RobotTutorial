package com.fpt.robot.example.tts.audiogen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fpt.lib.tts.audiogen.ITtsCallback;
import com.fpt.lib.tts.audiogen.TtsAudioGen;
import com.fpt.lib.tts.audiogen.objects.ServerInfo;
import com.fpt.lib.tts.audiogen.utils.Util;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.ui.RobotActivity;
import com.fpt.robot.utils.DialogUtils;

@SuppressLint({ "HandlerLeak", "NewApi" })
public class MainActivity extends RobotActivity {
	private static final String TAG = "SpeakVNTest";
	public final static int SUCCESS = -1;
	public final static int FAIL  = 0;
	public final static int PLAY_SUCCESS  = 3;
	
	private String fileName = null;
	private String message = null;
	private boolean play = false;
	private ToggleButton button;
	
	//Server info
	private final String URL ="http://184.73.236.63/tts/";
	private final String PASS = "12345678";
	private ServerInfo serverInfo = new ServerInfo(URL, PASS);
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (progressDialog!=null){
				progressDialog.dismiss();
			}
			switch (msg.what) {
				case PLAY_SUCCESS:
					if (button!=null){
						button.setChecked(false);
						play = false;
					}
					break;
				default:
					break;
			}
		};
	};
	private ProgressDialog progressDialog;
	private EditText edText;
	private Spinner spVoice;
	private TtsAudioGen audioGen;
	private ITtsCallback callback = new ITtsCallback() {
		@Override
		public void onFinished(String arg0) {
			try {
				DialogUtils.hideDialog(progressDialog);
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileName = arg0;
			if (fileName!=null){
				//Play file 
				button.setEnabled(true);
				Toast.makeText(MainActivity.this, fileName, Toast.LENGTH_SHORT).show();
				button.setChecked(false);
				play = false;
			}
			else {
				button.setChecked(false);
				play = false;
				button.setEnabled(false);
			}
		}
		
		@Override
		public void onError(Exception arg0) {
			try {
				DialogUtils.hideDialog(progressDialog);
			} catch (Exception e) {
				e.printStackTrace();
			}
			message = arg0.getMessage();
			if (message!=null) {
				//Play file 
				Toast.makeText(MainActivity.this,"error: "+ message, Toast.LENGTH_SHORT).show();
			}
			button.setChecked(false);
			play = false;
			button.setEnabled(false);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideSoftKeyboard(this);
		//Set volume
		AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,max, 0);

		//Set view
		setContentView(R.layout.mainactivity_main_layout);
		Button btnGet = (Button) findViewById(R.id.btnGet);
		button = (ToggleButton) findViewById(R.id.btnPlay);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!play){
					//Start
					//play(path+fileName);
					playOnRobot(fileName);	
				}
				
				play = !play;
			}
		});
		edText = (EditText) findViewById(R.id.edText);
		spVoice = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Util.SERVER_VOICE_TYPE);
		spVoice.setAdapter(adapter);
		btnGet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = edText.getText().toString();
				if ((text==null)||(text.trim().equals(""))){
					Toast.makeText(MainActivity.this,"The content is not empty", Toast.LENGTH_SHORT).show();
					return;
				}
				//Set voice
				String voice = String.valueOf(spVoice.getSelectedItem());
				voice = Util.SERVER_VOICE_TYPE[spVoice.getSelectedItemPosition()];
				//Convert text to voice
				progressDialog = ProgressDialog.show(MainActivity.this, null, "Getting data...");
				if (audioGen==null) {
					audioGen = TtsAudioGen.getNewInstance(callback);
					audioGen.setServerInfo(serverInfo);
				}
				
				String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mp3/";
				audioGen.audioGenerate(path, "test.mp3",voice, text.trim());
			}
		});
		//initMedia();
		button.setChecked(false);
		play = false;
		button.setEnabled(false);
	}
	
	private void playOnRobot(String localFilePath) {
		final String filePath = localFilePath;
		Log.d(TAG, "playOnRobot(): localFilePath=" + localFilePath);
		progressDialog = ProgressDialog.show(MainActivity.this, null, "Playing...");
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try{
					if (!filePath.isEmpty()) {
						boolean result =  RobotAudioPlayer.playLocalFile(getRobot(), filePath);
						Log.d(TAG, "playLocalFile('" + filePath + "'): result=" + result);
					} else {
						Log.d(TAG, "empty file path!");
					}
				}	
				catch (Exception e) {
					e.printStackTrace();
				}
				finally{
					handler.sendEmptyMessage(PLAY_SUCCESS);
				}
			}
		}).start();		
	}
	
	
	public static void hideSoftKeyboard(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		
	}
	
	

	@Override
	protected int getLayoutID() {
		return R.layout.mainactivity_main_layout;
	}

}
