package todo.list;

import todo.main.R;
import todo.models.Task;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ToDoListFragmentAdapter extends ArrayAdapter<Task> {

	
	private CheckboxListener cbListener;

	public ToDoListFragmentAdapter(Context context, int resource, CheckboxListener cbl ) {
		super(context, resource);
		cbListener = cbl;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if(v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.task_item, null);
		}
		
		final Task t = getItem(position);		
		if(t != null) {
			// Set board item layout here
			TextView tNameText = (TextView) v.findViewById(R.id.taskDescription);
			TextView tDate = (TextView) v.findViewById(R.id.taskEnd_date);
			CheckBox tStatus = (CheckBox) v.findViewById(R.id.taskStatus);
			
			tNameText.setText(t.name);
			tDate.setText(t.deadline);
			
			if(t.status.equals("completed")){
				tStatus.setChecked(true);
			} else{
				tStatus.setChecked(false);
			}
			
			//listener for the checkbox
			tStatus.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
					cbListener.onCheckboxChanged(t, isChecked);
				}			
			});
			
		}
		
		return v;
		
	}
	
	public interface CheckboxListener {
		public void onCheckboxChanged(Task t, boolean isChecked );
	}
	
}
