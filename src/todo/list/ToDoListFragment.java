package todo.list;

import todo.main.R;
import todo.models.Task;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ToDoListFragment extends Fragment {

	public static final String LIST_ID = "lid";
	
	private ToDoListFragmentAdapter list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        Bundle args = getArguments();
        int list_id = args.getInt(LIST_ID);
        
        ListView listView = (ListView) view.findViewById(R.id.list);
        list = new ToDoListFragmentAdapter(getActivity(), 0);
        Task t = new Task();
        t.list_id = list_id;
        t.name = "Test task name";
        list.add(t);
        
        Toast.makeText(getActivity(), "Called onCreate for: "+list_id, Toast.LENGTH_LONG).show();
        
        listView.setAdapter(list);
        
        return view;
    }
	
}
