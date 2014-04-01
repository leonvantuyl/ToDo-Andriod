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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.board_list, container, false);
		ListView listView = (ListView) view.findViewById(R.id.bList);
		final ArrayAdapter<Board> list = new BoardListAdapter(getActivity(), R.id.bListFragment);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				bListener.onItemSelected(list.getItem(position));
			}
			
		});
		listView.setAdapter(list);
		
		/* TODO uncomment
		api = new API(new OnAPIRequestListener() {
			
			@Override
			public void onRequestComplete(String result) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "Response: "+result, Toast.LENGTH_LONG).show();
				readResponse(result, list);
			}
		});
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("token",User.TOKEN);
		// Send request
		api.request(RequestMethod.GET, "board", map, null);
		*/
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

	public static void addBoard(String value) {
		// TODO Auto-generated method stub
		// Mag geen static methode zijn
	}
	
}
