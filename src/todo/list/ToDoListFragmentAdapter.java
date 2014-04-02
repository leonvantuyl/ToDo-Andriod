package todo.list;

import todo.main.R;
import todo.models.Task;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ToDoListFragmentAdapter extends ArrayAdapter<Task> {

	public ToDoListFragmentAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if(v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.task_item, null);
		}
		
		Task t = getItem(position);
		
		if(t != null) {
			// Set board item layout here
			TextView tNameText = (TextView) v.findViewById(R.id.taskDescription);
			tNameText.setText(t.name + ", " + t.list_id);
		}
		
		return v;
		
	}
	
}
