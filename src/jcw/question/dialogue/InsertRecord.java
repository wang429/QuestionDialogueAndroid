package jcw.question.dialogue;

import jcw.question.database.Database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertRecord extends Activity {
	Database db;
	EditText questionTypeIDET, questionET, answerET, distractor1ET,
			distractor2ET;
	Button insertButton, menuButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert);

		db = new Database(this);
		db.open();
		questionTypeIDET = (EditText) findViewById(R.id.QuestionTypeIDET);
		questionET = (EditText) findViewById(R.id.QuestionET);
		answerET = (EditText) findViewById(R.id.AnswerET);
		distractor1ET = (EditText) findViewById(R.id.Distractor1ET);
		distractor2ET = (EditText) findViewById(R.id.Distractor2ET);
		insertButton = (Button) findViewById(R.id.InsertRecordButton);
		menuButton = (Button) findViewById(R.id.MenuButton);
		insertButton.setOnClickListener(insertButtonListener);
		menuButton.setOnClickListener(menuButtonListener);
	}

	OnClickListener insertButtonListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				insertToDB();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	OnClickListener menuButtonListener = new OnClickListener() {
		public void onClick(View v) {
			returnToMenu();
		}
	};

	public void insertToDB() {
		boolean isSafeToInsert = true;
		String[] str = { questionTypeIDET.getText().toString(),
				questionET.getText().toString(), answerET.getText().toString(),
				distractor1ET.getText().toString(),
				distractor2ET.getText().toString() };
		if(str[0] == null || !str[0].equals("1")){
			str[0] = "1";
		}
		for (int i = 0; i < str.length; i++) {
			if (str[i] == null || str[i].length() == 0) {
				isSafeToInsert = false;
				break;
			}
		}
		if (isSafeToInsert) {
			long index = db.insertQuestion(str);
			resetEditTexts();
		} else {
			Toast.makeText(this, "Missing field parameter", Toast.LENGTH_SHORT);
		}
		db.close();
	}

	private void resetEditTexts() {
		questionTypeIDET.setText("");
		questionET.setText("");
		answerET.setText("");
		distractor1ET.setText("");
		distractor2ET.setText("");
	}

	/**
	 * returns screen to menu screen
	 */
	private void returnToMenu() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
