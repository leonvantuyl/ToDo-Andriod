package todo.list;

import java.util.ArrayList;

import todo.board.BoardActivity;
import todo.models.List;
import todo.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListActivity extends FragmentActivity {

	private int b_id;
	
	private ListPagerAdapter mPagerAdapter ;
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
		
		// ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
		ArrayList<List> lists = new ArrayList<List>();
		List l1 = new List();
		l1.name = "List nr1";
		l1.id = 1;
		List l2 = new List();
		l2.name = "List nr 2";
		l2.id = 2;
 		
		lists.add(l1);
 		lists.add(l2);
 		lists.add(l1);
 		lists.add(l2);
 		lists.add(l2);
		
        mPagerAdapter = new ListPagerAdapter(getSupportFragmentManager(), lists);
        mViewPager = (ViewPager) findViewById(R.id.list_pager);
        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between pages, select the
//                // corresponding tab.
//                //getActionBar().setSelectedNavigationItem(position);
//            	//titleStrip.set
//            }
//        });
//		
		//Toast.makeText(getApplicationContext(), "BoardID: "+b_id, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}

}
