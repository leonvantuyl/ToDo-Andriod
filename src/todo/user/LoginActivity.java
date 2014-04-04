package todo.user;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import todo.board.BoardActivity;
import todo.main.R;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private API api;
	public SharedPreferences prefs; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		prefs = this.getSharedPreferences("todo", Context.MODE_PRIVATE);
		checkIsLogged();
	}
	
//	public void onResume() {
//		super.onResume();
//		checkIsLogged();
//	}

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
			public void onSuccess(int statusCode, String result) {
				JSONObject tokenObj;
				try {
					tokenObj = new JSONObject(result);
					if(!tokenObj.isNull("token")) {
						User.login(tokenObj.getString("token"), prefs);
	
						Intent intent = new Intent(la, BoardActivity.class);
						startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(int statusCode, String result) {
				if(statusCode == 404) {
					// Not found
					EditText nameT = (EditText) findViewById(R.id.username);
					EditText passwordT = (EditText) findViewById(R.id.password);
					
					nameT.setError(getString(R.string.error_wronguserpass));
					passwordT.setError(getString(R.string.error_wronguserpass));
				}
			}
		},this);
		HashMap<String, Object> qs = new HashMap<String, Object>();
		qs.put("name", name);
		qs.put("password", password);
		api.request(RequestMethod.GET, "user", "login", qs, null);
		
	}
	
	public void checkIsLogged() {
		if(prefs!= null && User.loggedIn(prefs))
		{
			Intent intent = new Intent(this, BoardActivity.class);
			startActivity(intent);
		}
	}

}
