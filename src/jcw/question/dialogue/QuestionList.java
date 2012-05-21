package jcw.question.dialogue;

import java.util.Vector;

public class QuestionList {
	private Vector<QuestionData> dataList;

	/**
	 * Adds a QuestionData to the list.
	 * 
	 * @param QuestionData
	 *            qd
	 */
	public void add(QuestionData qd) {
		dataList.addElement(qd);
	}

	public void add(String[] question) {
		add(new QuestionData(question));
	}

	public void add(String[][] questions) {
		dataList = new Vector<QuestionData>(questions.length);
		for (int i = 0; i < questions.length; i++) {
			add(questions[i]);
		}
	}

	/**
	 * returns the number of questions
	 * 
	 * @return dataList.size();
	 */
	public int size() {
		return dataList.size();
	}

	/**
	 * returns QuestionData at specified index if index is valid otherwise,
	 * returns null
	 * 
	 * @param index
	 * @return QuestionData at index
	 */
	public QuestionData get(int index) {
		if (index >= dataList.size() || index < 0)
			return null;
		return dataList.get(index);
	}
	
	public void clear(){
		dataList.removeAllElements();
	}
}
