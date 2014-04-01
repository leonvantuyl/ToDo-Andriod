package todo.board;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import todo.board.BoardList.BoardListListener;
import todo.list.ToDoListActivity;
import todo.main.R;
import todo.models.Board;
import todo.user.LoginActivity;
import todo.user.User;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class BoardActivity extends Activity implements BoardListListener {

	public final static String BOARD_ID = "bid";
	public BoardList boardList;
	public SharedPreferences prefs;
	private API api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		prefs = this.getSharedPreferences("todo", Context.MODE_PRIVATE);
		boardList = (BoardList) this.getFragmentManager().findFragmentById(R.id.bListFragment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.board, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public void onBackPressed() {
		// Do nothing
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.add_board:
			addBoardDialog();
			return true;		
		case R.id.logout:
			logoutDialog();
			return true;
		case R.id.action_settings:
			//niks
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void logout() {
		api = new API(new OnAPIRequestListener() {		
			@Override
			public void onSuccess(int statusCode, String result) {
				//TODO moeten we hier op wachten ? can ook gewoon altijd dus buiten de request om.
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}

			@Override
			public void onError(int statusCode, String result) {
				if(statusCode == 404) {
					//TODO passende error 
				}
			}
		});
		HashMap<String, Object> qs = new HashMap<String, Object>();
		qs.put("toke", User.TOKEN);
		api.request(RequestMethod.GET, "user", "logout", qs, null);
		
		User.logout(prefs);			
		//TODO dit kan ook in de onSucces
		
	}

	@Override
	public void onItemSelected(Board b) {
		Intent intent = new Intent(getApplicationContext(), ToDoListActivity.class);
		intent.putExtra(BOARD_ID, b.id);
		startActivity(intent);
	}

	@Override
	public void onItemLongSelected(Board b) {
		removeBoardDialog(b);
	}
	
	private void addBoardDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.dialog_addboard_title);
		alert.setMessage(R.string.dialog_addboard_description);

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if(boardList != null) {
					boardList.addBoard(value);
				} else {
					Toast.makeText(getApplicationContext(), "BoardList null", Toast.LENGTH_LONG).show();
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

	private void removeBoardDialog(final Board b) {	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(getString(R.string.dialog_removeboard_title) +  " \"" + b.name + "\"");		
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
	
	private void logoutDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(R.string.dialog_logout_title);		
		alert.setMessage(R.string.dialog_logout_description);

		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				logout();
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
