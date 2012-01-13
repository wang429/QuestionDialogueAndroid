package jcw.question.dialogue;

public class QuestionData {
	// Just standard data.
	private String question;
	// type of question (0 for text, 1 for image, anything else tbd)
	private int questionType;

	private String[] choices;
	private int numChoices; // number of answer choices
	private String[] answerChoices; // array formatted for output
	private int answerIndex; // index of answer in answerChoice

	private void insertData(String[] temp) {
		question = temp[1];
		numChoices = temp.length - 2;
		questionType = Integer.parseInt(temp[0]);
		answerChoices = new String[numChoices];
		answerIndex = (int) (Math.random() * numChoices);
		choices = new String[3];
		for (int i = 0, j = 2; i < numChoices; i++, j++)
			choices[i] = temp[j];
	}

	public QuestionData(String[] str) {
		insertData(str);
	}

	public QuestionData() {
	}

	/**
	 * randomizes the order of answerChoices
	 */
	private void randomizeChoices() {
		answerChoices = new String[numChoices];
		answerIndex = (int) (Math.random() * numChoices);

		if (Math.random() < 0.5)
			for (int i = 0, j = answerIndex; i < numChoices; i++, j--) {
				j = j < 0 ? j + numChoices : j;
				setAnswerChoices(choices[i], j);
			}
		else
			for (int i = 0, j = answerIndex; i < numChoices; i++, j++) {
				j = j > 2 ? j - numChoices : j;
				setAnswerChoices(choices[i], j);
			}

		// adds A), B), C)

		for (int i = 0; i < numChoices; i++) {
			answerChoices[i] = (char) ('A' + i) + ") " + answerChoices[i];
		}
	}

	/**
	 * sets answerChoices
	 * 
	 * @param str
	 * @param j
	 */
	private void setAnswerChoices(String str, int j) {
		answerChoices[j] = str;
	}

	/**
	 * gets 0-2 for user choice returns true if right, false if wrong
	 * 
	 * @param user
	 * @return
	 */
	public boolean checkAnswer(int user) {
		return user == answerIndex;
	}

	// The below are all getters
	/**
	 * returns question
	 * 
	 * @return String question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * returns answerChoices
	 * 
	 * @return String[] answerChoices
	 */
	public String[] getAnswerChoices() {
		randomizeChoices();
		return answerChoices;
	}

	/**
	 * returns the index of the correct answer
	 * 
	 * @return int answerIndex
	 */
	public int getAnswerIndex() {
		return answerIndex;
	}

	/**
	 * returns the questionType
	 * 
	 * @return int questionType
	 */
	public int getQuestionType() {
		return questionType;
	}
}