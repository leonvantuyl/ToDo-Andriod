package todo.list;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import todo.main.R;
import todo.models.Board;
import todo.models.List;
import todo.models.Task;
import todo.user.User;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private API api;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View view = inflater.inflate(R.layout.list_fragment, container, false);
		Bundle args = getArguments();
		int list_id = args.getInt(LIST_ID);

		ListView listView = (ListView) view.findViewById(R.id.list);
		list = new ToDoListFragmentAdapter(getActivity(), 0);
		LoadTasks(list_id);

		listView.setAdapter(list);

		return view;
	}

	private void LoadTasks(final int listId) {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				readResponse(result);	
			}

			@Override
			public void onError(int statusCode, String result) {
				// TODO error code
			}
		});
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("lid", listId);
		// Send request
		api.request(RequestMethod.GET, "task", map, null);
	}


	private void readResponse(String result) {
		try {
			JSONArray tasks = new JSONArray(result);

			for(int i = 0; i < tasks.length(); i++) {
				Task t = new Task();
				t.name = tasks.getJSONObject(i).getString("name");
				t.deadline = tasks.getJSONObject(i).getString("end_date");
				t.id = tasks.getJSONObject(i).getInt("id");
				t.list_id = tasks.getJSONObject(i).getInt("list_id");
				t.status = tasks.getJSONObject(i).getString("status");
				list.add(t);
				//TODO word alle data goed gevult ?
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void dialogRemoveTask(final Task currentTask) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle(getString(R.string.dialog_removetask_title) +  " \"" + currentTask.name + "\"");		
		alert.setMessage(R.string.dialog_removetask_description);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//TODO remove
			}
		});

		alert.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		alert.show();
	}


}
