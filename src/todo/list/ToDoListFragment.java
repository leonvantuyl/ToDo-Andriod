package todo.list;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import todo.list.ToDoListFragmentAdapter.CheckboxListener;
import todo.main.R;
import todo.models.Task;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ToDoListFragment extends Fragment implements CheckboxListener {

	public static final String LIST_ID = "lid";

	private ToDoListFragmentAdapter list;
	private TaskListListener tListener;
	private API api;
	private int lid;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Get info out of bundle
		Bundle args = getArguments();
		lid = args.getInt(LIST_ID);

		//initiate and connect
		View view = inflater.inflate(R.layout.list_fragment, container, false);
		ListView listView = (ListView) view.findViewById(R.id.list);
		list = new ToDoListFragmentAdapter(getActivity(), 0, this);
		listView.setAdapter(list);

		//Listeners for objects in the list		
		setListeners(listView);

		//get the task data
		LoadTasks();		

		return view;
	}

	private void setListeners(final ListView listView) {;
	//listener for the long click on a item
	listView.setLongClickable(true);		
	listView.setOnItemLongClickListener(new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			tListener.onItemLongSelected(list.getItem(position));
			return true;
		}
	});		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			tListener = (TaskListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement TaskListListener");
		}
	}

	private void LoadTasks() {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				readResponse(result);	
			}

			@Override
			public void onError(int statusCode, String result) {
				Toast.makeText(getActivity(), "404 error bij task", Toast.LENGTH_LONG).show();
			}
		},getActivity());

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("lid", lid);
		// Send request
		api.request(RequestMethod.GET, "task", map, null);
	}


	private void readResponse(String result) {
		try {
			JSONArray tasks = new JSONArray(result);

			for(int i = 0; i < tasks.length(); i++) {
				Task t = new Task();
				JSONObject taskObj = tasks.getJSONObject(i);
				t.name = taskObj.getString("name");
				t.deadline = taskObj.getString("end_date");
				t.id = taskObj.getInt("id");
				t.list_id = taskObj.getInt("list_id");
				t.status = taskObj.getString("status");
				list.add(t);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void changeStatus(final Task item, final boolean isChecked) {		
		String status = (isChecked) ? "completed" : "not_completed";

		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				//klaar
			}

			@Override
			public void onError(int statusCode, String result) {
				Toast.makeText(getActivity(), "error bij status update task", Toast.LENGTH_LONG).show();
			}
		},getActivity());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", item.id);
		map.put("id", status);
		// Send request
		api.request(RequestMethod.PUT, "task", "putStatus", map, null);
	}


	public interface TaskListListener {
		public void onItemLongSelected(Task t);
	}


	@Override
	public void onCheckboxChanged(Task t, boolean isChecked) {
		changeStatus(t, isChecked);
	}

	
	public void removeTask(final Task currentTask) {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				list.remove(currentTask); 
			}

			@Override
			public void onError(int statusCode, String result) {
				Toast.makeText(getActivity(), "error bij delete task", Toast.LENGTH_LONG).show();
			}
		},getActivity());
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("id", currentTask.id);
		api.request(RequestMethod.DELETE, "task", null, args);
	}

	public void addTask(final String value) {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				JSONObject obj;
				try {
					Task t = new Task();
					obj = new JSONObject(result);	
					t.id = obj.getInt("id");
					t.name = value;
					t.list_id = lid;
					t.deadline = "00-00-0000 00:00:00";
					t.status = "";
					list.add(t); 
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			@Override
			public void onError(int statusCode, String result) {
				Toast.makeText(getActivity(), "error bij POST task", Toast.LENGTH_LONG).show();
			}
		},getActivity());
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("list_id", lid);
		args.put("name", value);
		// Send request
		api.request(RequestMethod.POST, "task", null, args);
	}
}
