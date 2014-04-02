package todo.list;

import java.util.ArrayList;
import todo.models.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ListPagerAdapter extends FragmentStatePagerAdapter  {

	private ArrayList<List> lists;
	
	public ListPagerAdapter(FragmentManager fm, ArrayList<List> lists) {
		super(fm);
		this.lists = lists;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(ToDoListFragment.LIST_ID, lists.get(i).id);
        fragment.setArguments(args);
        return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return lists.get(position).name;
    }

}
