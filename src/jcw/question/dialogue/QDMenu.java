/**
 * Title Menu for QuestionDialogue. Ask no questions, tell no lies
 */

package jcw.question.dialogue;

import jcw.question.database.Database;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QDMenu extends Activity {
	private TextView title;
	private Button practiceButton, testButton, insertButton;
	private static boolean isTest;
	Database db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		title = (TextView) findViewById(R.id.Title);
		practiceButton = (Button) findViewById(R.id.PracticeButton);
		testButton = (Button) findViewById(R.id.TestButton);
		insertButton = (Button) findViewById(R.id.InsertButton);

		title.setText("Click \"Practice\" to begin a practice round.\nClick \"Test\" to begin the test");
		practiceButton.setText("Practice");
		testButton.setText("Test");
		insertButton.setText("Insert");

		db = new Database(this);
		db.open();

		practiceButton.setOnClickListener(practiceButtonListener);
		testButton.setOnClickListener(testButtonListener);
		insertButton.setOnClickListener(insertButtonListener);

	}

	OnClickListener practiceButtonListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				isTest = false;
				Intent intent = new Intent((Context) QDMenu.this,
						QuestionDialogue.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	OnClickListener testButtonListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				isTest = true;
				Intent intent = new Intent((Context) QDMenu.this,
						QuestionDialogue.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	OnClickListener insertButtonListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				Intent intent = new Intent((Context) QDMenu.this,
						InsertRecord.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public static boolean getModeType() {
		return isTest;
	}
}
