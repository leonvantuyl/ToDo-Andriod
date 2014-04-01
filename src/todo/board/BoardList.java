package todo.board;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BoardList extends Fragment {

	private API api;
	private BoardListListener bListener;
	private ArrayAdapter<Board> list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.board_list, container, false);
		ListView listView = (ListView) view.findViewById(R.id.bList);
		list = new BoardListAdapter(getActivity(), R.id.bListFragment);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				bListener.onItemSelected(list.getItem(position));
			}
			
		});
		listView.setAdapter(list);
		
		loadBoardList();
		
		return view;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public interface BoardListListener {
		public void onItemSelected(Board b);
	}

	public void addBoard(String value) {
		final String name = value;
		api = new API(new OnAPIRequestListener() {
			
			@Override
			public void onSuccess(int statusCode, String result) {
				loadBoardList();				
			}			

			@Override
			public void onError(int statusCode, String result) {
				//TODO in de API stond geen error code bij de post method van de boardcontroller. Dus het gaat alleen fout als er geen connectie is.
				
			}
		});
		
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("name", name);
		api.request(RequestMethod.POST, "board", "post", null, args);
		
	}
	
	public void loadBoardList() {
		api = new API(new OnAPIRequestListener() {
			
			@Override
			public void onSuccess(int statusCode, String result) {
				readResponse(result, list);				
			}

			@Override
			public void onError(int statusCode, String result) {
				// TODO Auto-generated method stub
				
			}
		});
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("token",User.TOKEN);
		// Send request
		api.request(RequestMethod.GET, "board", map, null);
	}
}
