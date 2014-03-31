package todo.user;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import todo.board.BoardActivity;
import todo.main.R;
import todo.main.R.layout;
import todo.main.R.menu;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private API api;
	public SharedPreferences prefs; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		prefs = this.getSharedPreferences("todo", Context.MODE_PRIVATE);		
		if(prefs!= null && User.loggedIn(prefs))
		{
			Intent intent = new Intent(this, BoardActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void registerClick(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void loginClick(View view) {
		EditText nameT = (EditText) findViewById(R.id.username);
		EditText passwordT = (EditText) findViewById(R.id.password);

		String name = nameT.getText().toString();
		String password = passwordT.getText().toString();

		Boolean validN = false;
		Boolean validP = false;

		if(TextUtils.isEmpty(name)) {
			nameT.setError(getString(R.string.error_field_required));
		} else 
			validN = true;

		if(TextUtils.isEmpty(password)) {
			passwordT.setError(getString(R.string.error_field_required));
		} else
			validP = true;

		if(validN && validP) {
			login(name, password);
		}
	}

	public void login(String name, String password) {
		final LoginActivity la = this;
		api = new API(new OnAPIRequestListener() {		
			@Override
			public void onRequestComplete(String result) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(result) && !result.equals("null")) {
					try {
						JSONObject tokenObj = new JSONObject(result);
						if(!tokenObj.isNull("token")) {
							User.login(tokenObj.getString("token"), prefs);

							Intent intent = new Intent(la, BoardActivity.class);
							startActivity(intent);
						} else {
							// Something went wrong
						}


					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Not found
					Toast.makeText(la.getApplicationContext(), "Wrong username or password", Toast.LENGTH_LONG).show();

					//TODO Remove test code					
					Intent intent = new Intent(la, BoardActivity.class);
					startActivity(intent);

				}
			}
		});
		HashMap<String, Object> qs = new HashMap<String, Object>();
		qs.put("name", name);
		qs.put("password", password);
		api.request(RequestMethod.GET, "user", "login", qs, null);
	}

}
