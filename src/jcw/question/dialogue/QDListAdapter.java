package jcw.question.dialogue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QDListAdapter extends BaseAdapter {
	private Activity activity;
	private String[] textData;
	private int[] imageData;
	private static LayoutInflater inflater = null;

	public QDListAdapter(Activity a, String[] answers, int[] imd) {
		activity = a;
		textData = answers;
		imageData = imd;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		vi = inflater.inflate(R.layout.list, null);
		holder = new ViewHolder();
		holder.text = (TextView) vi.findViewById(R.id.text);
		holder.image = (ImageView) vi.findViewById(R.id.image);
		vi.setTag(holder);

		holder.text.setText(textData[position]);
		holder.image.setImageResource(R.drawable.trollface);
		return vi;
	}

	public int getTextCount() {
		return textData.length;
	}

	public int getImageCount() {
		return imageData.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return textData.length;
	}
}
