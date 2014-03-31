package todo.list;

import todo.board.BoardActivity;
import todo.main.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoListActivity extends Activity {

	private int b_id;
	
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
		
		//Toast.makeText(getApplicationContext(), "BoardID: "+b_id, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_list, menu);
		return true;
	}

}
