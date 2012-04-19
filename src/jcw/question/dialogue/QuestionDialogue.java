package jcw.question.dialogue;

/**
 * Question Dialogue App for Android [James Wang Prototype]
 * 
 * Order of questions should be random.
 */

import jcw.question.database.Constants;
import jcw.question.database.Database;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionDialogue extends Activity {
	int[] imres = { R.drawable.icon, R.drawable.stub, R.drawable.start };

	static final int DIALOG_PAUSED_ID = 0;
	static final int DIALOG_TO_MENU_ID = 1;
	static final int DIALOG_FINISHED_ID = 2;

	// GUI and other Android necessities
	private TextView questionDisplay;
	private ListView list;
	private Button nextButton, menuButton; // nextButton doubles as checkButton
	QDListAdapter adapter;
	// Database variables
	QuestionListCreator qlc;
	Database db;

	// don't like following two lines
	/*
	 * { "What is the capital of China?", "Beijing", "Shanghai", "Hong Kong" },
	 * { "What are the colors of the Chinese flag?", "Red and Yellow",
	 * "Red and Orange", "Orange and Yellow" }, {
	 * "How many hours are in a day?", "24", "12", "20" } };
	 */
	private QuestionList ql;
	private int[] questionOrder;
	private int currentQuestion = -1;
	private boolean isTutorial;
	private boolean isCorrect;
	private boolean isTest = QDMenu.getModeType(); // potentially useless.
	private boolean[] correctChoice;
	private int score;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogue);
		db = new Database(this);
		db.open();
		qlc = new QuestionListCreator();

		// setting data for the first ql
		ql = qlc.getData();

		// Finding id's of question, choices and next button
		questionDisplay = (TextView) findViewById(R.id.Question);
		list = (ListView) findViewById(R.id.List);
		nextButton = (Button) findViewById(R.id.NextButton);
		menuButton = (Button) findViewById(R.id.MenuButton);
		// ImageView image = (ImageView) findViewById(R.id.test_image);
		// image.setImageResource(R.drawable.trollface);

		questionOrder = new int[ql.size()];
		// orders the questions
		/** TODO Make Random */
		for (int i = 0; i < questionOrder.length; i++) {
			questionOrder[i] = i;
		}

		correctChoice = new boolean[questionOrder.length];

		// sets text to tutorial
		String tutorialQuestion = "0. Hit \"Answer\" to continue";
		String[] tutorialAnswers = { "Answer", "Distractor 1", "Distractor 2" };
		isTutorial = true;
		if (isTutorial) {
			questionDisplay.setText(tutorialQuestion);
			setChoiceListAdapter(list, tutorialAnswers);
		}
		menuButton.setText("Quit");
		if (isTest)
			nextButton.setText("Next");
		else
			nextButton.setText("Check");

		// disable button
		nextButton.setEnabled(false);

		nextButton.setOnClickListener(nextButtonListener);
		list.setOnItemClickListener(listListener);
		menuButton.setOnClickListener(menuButtonListener);
	}

	OnClickListener menuButtonListener = new OnClickListener() {
		public void onClick(View v) {
			showDialog(DIALOG_TO_MENU_ID);
		}
	};

	OnClickListener nextButtonListener = new OnClickListener() {
		public void onClick(View v) {
			if (isCorrect) {
				onCorrect();
			} else {
				onIncorrect();
			}
		}
	};

	OnItemClickListener listListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int SelectedId,
				long arg3) {
			if (isTutorial) {
				isCorrect = SelectedId == 0 ? true : false;
			} else {
				checkSelection(SelectedId, questionOrder[currentQuestion]);
				if (isCorrect)
					correctChoice[currentQuestion] = true;
			}
			nextButton.setEnabled(true);
		}
	};

	private void showToastText(String str) {
		Toast.makeText(QuestionDialogue.this, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * checks the user's selection with answer
	 * 
	 * @param SelectedId
	 * @param ResText
	 */
	private void checkSelection(int SelectedId, int index) {
		isCorrect = ql.get(index).checkAnswer(SelectedId);
	}

	private void onIncorrect() {
		if (!isTest) {
			showToastText("Sorry. Please try again.");
			nextButton.setEnabled(false);
		} else {
			if (currentQuestion >= questionOrder.length) {
				atLastQuestion();

			} else {
				toNextQuestion();
			}
		}
	}

	private void onCorrect() {
		list.setEnabled(false);
		if (nextButton.getText().equals("Check")) {
			showToastText("You Are Correct!");
			if (isLastQuestion()) {
				nextButton.setText("Finish");
			} else {
				nextButton.setText("Next");
			}
		} else {
			if (!isTutorial && correctChoice[currentQuestion]) {
				score++;
			}
			if (isLastQuestion()) {
				atLastQuestion();
			} else {
				toNextQuestion();
			}
			isTutorial = false;
		}
	}

	private boolean isLastQuestion() {
		return currentQuestion >= questionOrder.length - 1;
	}

	private void atLastQuestion() {
		currentQuestion = -1; // sets to first question;
		isTutorial = true;
		nextButton.setEnabled(false);
		if (!isTest) {
			returnToMenu();
		} else {
			showDialog(DIALOG_FINISHED_ID);
		}
	}

	/**
	 * returns screen to menu screen
	 */
	private void returnToMenu() {
		ql.clear();
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * calls goToQuestion and redisables next button
	 * 
	 * @param next
	 */
	private void toNextQuestion() {
		currentQuestion++;
		String qText = (currentQuestion + 1) + ". "
				+ ql.get(questionOrder[currentQuestion]).getQuestion();
		questionDisplay.setText(qText);
		setChoiceListAdapter(list, questionOrder[currentQuestion]);
		if (isLastQuestion()) {
			nextButton.setText("Finish");
		}
		if (!isTest)
			nextButton.setText("Check");
		nextButton.setEnabled(false); // redisables button for next question
	}

	/**
	 * sets the adapter that listview will use.
	 * 
	 * @param list
	 */
	private void setChoiceListAdapter(ListView list, int index) {
		setChoiceListAdapter(list, ql.get(index).getAnswerChoices());
	}

	private void setChoiceListAdapter(ListView list, String[] str) {
		adapter = new QDListAdapter(this, str, imres);
		list.setAdapter(adapter);
		list.setEnabled(true);
	}

	public class QuestionListCreator {
		QuestionList list = new QuestionList();
		String[][] data;
		int numQuestions;

		public QuestionList getData() {
			Cursor c = db.getQuestions();
			numQuestions = c.getCount();
			data = new String[numQuestions][];
			if (c.moveToFirst()) {
				int i = 0;
				do {
					int questionType = c.getInt(c
							.getColumnIndex(Constants.QUESTIONTYPE_ID));
					String question = c.getString(c
							.getColumnIndex(Constants.QUESTION));
					String answer = c.getString(c
							.getColumnIndex(Constants.ANSWER));
					String distractor1 = c.getString(c
							.getColumnIndex(Constants.DISTRACTOR1));
					String distractor2 = c.getString(c
							.getColumnIndex(Constants.DISTRACTOR2));
					String[] temp = { Integer.toString(questionType), question,
							answer, distractor1, distractor2 };
					data[i] = temp;
					i++;
				} while (c.moveToNext());
			}
			list.add(data);
			return list;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DIALOG_PAUSED_ID:
			AlertDialog.Builder buildPause = new AlertDialog.Builder(
					QuestionDialogue.this);
			buildPause
					.setMessage("Click \"Ready\" to resume.")
					.setCancelable(false)
					.setPositiveButton("Ready",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			dialog = buildPause.create();
			break;
		case DIALOG_TO_MENU_ID:
			AlertDialog.Builder buildMenu = new AlertDialog.Builder(
					QuestionDialogue.this);
			buildMenu
					.setMessage(
							"Are you sure you want to exit?\nProgress will not be saved!")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									returnToMenu();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			dialog = buildMenu.create();
			break;
		case DIALOG_FINISHED_ID:
			AlertDialog.Builder buildFinish = new AlertDialog.Builder(
					QuestionDialogue.this);
			buildFinish
					.setMessage(
							"Your score: " + score + "/" + questionOrder.length)
					.setCancelable(false)
					.setPositiveButton("Finish and return to menu",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									returnToMenu();
								}
							});
			dialog = buildFinish.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
}