package todo.board;

import todo.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import todo.models.Board;

public class BoardListAdapter extends ArrayAdapter<Board> {

	public BoardListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if(v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.board_list_item, null);
		}
		
		Board b = getItem(position);
		
		if(b != null) {
			// Set board item layout here
			TextView bText = (TextView) v.findViewById(R.id.boardText);
			bText.setText(b.name);
		}
		
		return v;
		
	}

}
