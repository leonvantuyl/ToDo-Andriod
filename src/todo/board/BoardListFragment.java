package todo.board;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import todo.main.R;
import todo.models.Board;
import todo.user.User;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BoardListFragment extends Fragment {

	private API api;
	private BoardListListener bListener;
	private ArrayAdapter<Board> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//initiate and connect
		View view = inflater.inflate(R.layout.board_list, container, false);
		ListView listView = (ListView) view.findViewById(R.id.bList);
		list = new BoardListAdapter(getActivity(), R.id.bListFragment);		
		listView.setAdapter(list);

		//set listeners for the objects in the list
		setListeners(listView);

		//load the 'lists' for in the listview
		loadBoardList();

		return view;
	}

	private void setListeners(ListView listView) {
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				bListener.onItemSelected(list.getItem(position));
			}

		});

		listView.setLongClickable(true);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				bListener.onItemLongSelected(list.getItem(position));
				return true;
			}
		});

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			bListener = (BoardListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement BoardListListener");
		}
	}

	public void readResponse(String result, ArrayAdapter<Board> list) {
		try {
			JSONArray boards = new JSONArray(result);

			for(int i = 0; i < boards.length(); i++) {
				Board b = new Board();
				b.name = boards.getJSONObject(i).getString("name");
				b.id = boards.getJSONObject(i).getInt("id");
				list.add(b);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public interface BoardListListener {
		public void onItemSelected(Board b);
		public void onItemLongSelected(Board b);
	}

	public void addBoard(final String name) {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					Board b = new Board();
					b.id = obj.getInt("id");
					b.name = name;			
					list.add(b);
				} catch (JSONException e) {
					e.printStackTrace();
				}					
			}			

			@Override
			public void onError(int statusCode, String result) {
				Toast.makeText(getActivity(), "error bij POST board", Toast.LENGTH_LONG).show();
			}
		});

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("bName", name);
		args.put("token", User.TOKEN);
		api.request(RequestMethod.POST, "board", null, args);

	}

	public void removeBoard(final int id) {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				int size = list.getCount();
				for(int i = 0; i<size; i++)
				{
					Board currentBoard = list.getItem(i);
					if(currentBoard.id == id)
					{
						i = size;
						list.remove(currentBoard);
					}
				}		
			}			

			@Override
			public void onError(int statusCode, String result) {				
				if(statusCode == 404) {
					// Not found
					Toast.makeText(getActivity().getApplicationContext(), "Board was already removed/didn't exist", Toast.LENGTH_LONG).show();
				}

			}
		});
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("bid", id);
		api.request(RequestMethod.DELETE, "board", null, args);

	}

	public void loadBoardList() {
		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				readResponse(result, list);				
			}

			@Override
			public void onError(int statusCode, String result) {
				Toast.makeText(getActivity(), "error bij GET board", Toast.LENGTH_LONG).show();
			}
		});
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("token",User.TOKEN);
		// Send request
		api.request(RequestMethod.GET, "board", map, null);
	}


}
