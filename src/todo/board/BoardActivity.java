package todo.board;

import todo.board.BoardList.BoardListListener;
import todo.list.ToDoListActivity;
import todo.main.R;
import todo.models.Board;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class BoardActivity extends Activity implements BoardListListener {

	public final static String BOARD_ID = "bid";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board, menu);
		return true;
	}

	@Override
	public void onItemSelected(Board b) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(getApplicationContext(), ToDoListActivity.class);
		intent.putExtra(BOARD_ID, b.id);
		startActivity(intent);
	}

}
