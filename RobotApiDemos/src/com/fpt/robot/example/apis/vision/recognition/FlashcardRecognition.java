package com.fpt.robot.example.apis.vision.recognition;

import android.os.Bundle;
import android.view.Menu;

import com.fpt.robot.example.apis.R;
import com.fpt.robot.ui.RobotActivity;

public class FlashcardRecognition extends RobotActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_flashcard_recognition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flashcard_recognition, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_flashcard_recognition;
	}
}
