package todo.board;

import todo.board.BoardList.BoardListListener;
import todo.list.ToDoListActivity;
import todo.main.R;
import todo.models.Board;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class BoardActivity extends Activity implements BoardListListener {

	public final static String BOARD_ID = "bid";
	public BoardList boardList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		boardList = (BoardList) this.getFragmentManager().findFragmentById(R.layout.board_list);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.board, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.add_board:
			addBoardDialog();
			return true;
		case R.id.action_settings:
			//niks
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	@Override
	public void onItemSelected(Board b) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(getApplicationContext(), ToDoListActivity.class);
		intent.putExtra(BOARD_ID, b.id);
		startActivity(intent);
	}

	@Override
	public void onItemLongSelected(Board b) {
		removeBoardDialog(b);
	}
	
	private void addBoardDialog() {
		//TODO hier een fragment van maken ?
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.dialog_addboard_title);
		alert.setMessage(R.string.dialog_addboard_description);

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				boardList.addBoard(value);
			}
		});

		alert.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();

	}

	private void removeBoardDialog(final Board b) {
		//TODO hier een fragment van maken ?
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(getString(R.string.dialog_removeboard_title) +  " \"" + b.name + " \"");		
		alert.setMessage(R.string.dialog_removeboard_description);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				boardList.removeBoard(b.id);
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
