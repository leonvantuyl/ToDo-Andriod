package todo.list;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import todo.board.BoardActivity;
import todo.list.ToDoListFragment.TaskListListener;
import todo.main.R;
import todo.models.List;
import todo.models.Task;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ToDoListActivity extends FragmentActivity implements TaskListListener{

	private int b_id;
	private API api;
	private ListPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_list);

		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			b_id = extras.getInt(BoardActivity.BOARD_ID);
		}

		if(b_id <= 0) {
			Intent intent = new Intent(this, BoardActivity.class);
			startActivity(intent);
		}

		loadLists();


		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}
	
	//interface implementation
	@Override
	public void onItemLongSelected(Task t) {
		dialogRemoveTask(t);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		List currentList = mPagerAdapter.getCurrentList();
		switch (item.getItemId()) {
		case R.id.add_list:
			dialogAddList();
			return true;		
		case R.id.add_task:
			if(currentList != null)
				dialogAddTask(currentList);
			else
				Toast.makeText(getApplicationContext(), "Create a list first", Toast.LENGTH_LONG).show();
			return true;
		case R.id.remove_list:
			if(currentList != null)
				dialogRemoveList(currentList);
			else
				Toast.makeText(getApplicationContext(), "There are no lists to remove", Toast.LENGTH_LONG).show();
			return true;
		case android.R.id.home:
	            NavUtils.navigateUpFromSameTask(this);
	            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}	

	private void loadLists() {

		api = new API(new OnAPIRequestListener() {		

			@Override
			public void onSuccess(int statusCode, String result) {
				ArrayList<List> lists = readResponse(result);
				initViewPager(lists);				
			}

			@Override
			public void onError(int statusCode, String result) {
				if(statusCode == 404) {
					Toast.makeText(getApplicationContext(), "404 error :(", Toast.LENGTH_LONG).show();
				}
			}
		},this);
		HashMap<String, Object> qs = new HashMap<String, Object>();
		qs.put("bid", b_id);
		api.request(RequestMethod.GET, "list", "listsonly", qs, null);
	}

	private void initViewPager(ArrayList<List> lists) {
		mPagerAdapter = new ListPagerAdapter(getSupportFragmentManager(), lists);
		mViewPager = (ViewPager) findViewById(R.id.list_pager);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) 
			{  
				mPagerAdapter.currentIndex = position;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				//nothing	
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				//nothing			
			}
		});
		mViewPager.setAdapter(mPagerAdapter);
	}

	public ArrayList<List> readResponse(String result) {
		try {
			JSONArray lists = new JSONArray(result);
			ArrayList<List> reArray = new ArrayList<List>();
			for(int i = 0; i < lists.length(); i++) {
				List l = new List();
				l.name = lists.getJSONObject(i).getString("name");
				l.id = lists.getJSONObject(i).getInt("id");
				reArray.add(l);
			}
			return reArray;

		} catch (JSONException e) {			
			e.printStackTrace();
			return null;
		}
	}

	private void dialogRemoveList(final List currentList) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);		
		alert.setTitle(getString(R.string.dialog_removelist_title) +  " \"" + currentList.name + "\"");		
		alert.setMessage(R.string.dialog_removelist_description);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mPagerAdapter.removeList(currentList);
			}
		});

		alert.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		alert.show();

	}

	private void dialogAddList() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.dialog_addlist_title);		
		alert.setMessage(R.string.dialog_addlist_description);

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if(!value.isEmpty()) {
					mPagerAdapter.addList(value, b_id);
				} else {
					Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_LONG).show();
				}
			}
		});

		alert.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		alert.show();
	}

	private void dialogAddTask(final List currentList) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.dialog_addtask_title) +  " \"" + currentList.name + "\"");		
		alert.setMessage(R.string.dialog_addtask_description);

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if(!value.isEmpty()) {
					ToDoListFragment fragment = (ToDoListFragment) mPagerAdapter.getCurrentFragment();
					fragment.addTask(value);
				} else {
					Toast.makeText(getApplicationContext(), "Name can't be empty", Toast.LENGTH_LONG).show();
				}
			}
		});

		alert.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		alert.show();
	}
	
	private void dialogRemoveTask(final Task currentTask) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.dialog_removetask_title) +  " \"" + currentTask.name + "\"");		
		alert.setMessage(R.string.dialog_removetask_description);
 
		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ToDoListFragment fragment = (ToDoListFragment) mPagerAdapter.getCurrentFragment();
				fragment.removeTask(currentTask);
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
