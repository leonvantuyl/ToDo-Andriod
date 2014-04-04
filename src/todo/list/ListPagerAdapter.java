package todo.list;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import todo.models.List;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class ListPagerAdapter extends FragmentStatePagerAdapter  {

	private ArrayList<List> lists;
	public int currentIndex;
	private API api;
	private Fragment mCurrentFragment;

	public ListPagerAdapter(FragmentManager fm, ArrayList<List> lists) {
		super(fm);
		this.lists = lists;
		resetLocation();
	}

	@Override
	public Fragment getItem(int i) {
		ToDoListFragment fragment = new ToDoListFragment();
		Bundle args = new Bundle();
		args.putInt(ToDoListFragment.LIST_ID, lists.get(i).id);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (mCurrentFragment != object) {
			mCurrentFragment = (Fragment) object;
		}
		super.setPrimaryItem(container, position, object);
	}
	
	public Fragment getCurrentFragment()
	{
		return mCurrentFragment;
	}

	@Override
	public int getItemPosition(Object object){
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public int getCount() {
		if(lists != null)
			return lists.size();
		else
			return 0;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return lists.get(position).name;
	}

	public List getCurrentList()
	{
		if(currentIndex >= 0)
		{
			List list = lists.get(currentIndex);
			return list;
		}
		return null;
	}


	public void addList(final String name, final int bid) {

		api = new API(new OnAPIRequestListener() {

			@Override
			public void onSuccess(int statusCode, String result) {
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					List l = new List();
					l.id = obj.getInt("id");
					l.name = name;
					l.board_id = bid;
					lists.add(l);
					if(currentIndex <0)
						currentIndex = 0;
					notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}					
			}			

			@Override
			public void onError(int statusCode, String result) {
			}
		},this.getCurrentFragment().getActivity());

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("name", name);
		args.put("board_id", bid);
		api.request(RequestMethod.POST, "list", null, args);
	}

	public void removeList(final List currentList)
	{
		if(currentList != null)
		{
			api = new API(new OnAPIRequestListener() {

				@Override
				public void onSuccess(int statusCode, String result) {
					lists.remove(currentList);
					if(lists.isEmpty())
						currentIndex = -1;
					notifyDataSetChanged();
				}			

				@Override
				public void onError(int statusCode, String result) {				
				}
			},this.getCurrentFragment().getActivity());
			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("id", currentList.id);
			api.request(RequestMethod.DELETE, "list", null, args);
		}
	}

	private void resetLocation()
	{
		if(lists.size() >0) 
			currentIndex = 0;
		else
			currentIndex = -1;
	}

}
