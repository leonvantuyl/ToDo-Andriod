package todo.user;

import java.util.HashMap;

import todo.main.R;
import todo.utils.API;
import todo.utils.API.OnAPIRequestListener;
import todo.utils.API.RequestMethod;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	private API api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void registerClick(View view) {
		EditText nameT = (EditText) findViewById(R.id.usernameRegister);
		EditText passwordT = (EditText) findViewById(R.id.passwordRegister);
		EditText re_passwordT = (EditText) findViewById(R.id.re_passwordRegister);
		
		String name = nameT.getText().toString();
		String password = passwordT.getText().toString();
		String re_password = re_passwordT.getText().toString();
		
		Boolean validN = false;
		Boolean validP = false;
		Boolean validPr = false;
		Boolean pwmatch = false;
		
		if(TextUtils.isEmpty(name)) {
			nameT.setError(getString(R.string.error_field_required));
		} else 
			validN = true;
		
		if(TextUtils.isEmpty(password)) {
			passwordT.setError(getString(R.string.error_field_required));
		} else
			validP = true;
		
		if(TextUtils.isEmpty(re_password)) {
			re_passwordT.setError(getString(R.string.error_field_required));
		} else
			validPr = true;
		if(validP && validPr){
			if(!password.equals(re_password)) {
				passwordT.setError(getString(R.string.error_nomatch_password));
				re_passwordT.setError(getString(R.string.error_nomatch_password));
			} else
				pwmatch = true;		
		}
		if(validN && pwmatch) {
			register(name, password);
		}
	}

	
	private void register(String name, String password) {
		final RegisterActivity ra = this;
		/*
		api = new API(new OnAPIRequestListener() {
			
			@Override
			public void onRequestComplete(String result) {
				//TODO kijk of je een error krijgt.							
				Intent intent = new Intent(ra, LoginActivity.class);
				startActivity(intent);				
			}
			
		});
		HashMap<String, Object> qs = new HashMap<String, Object>();
		qs.put("name", name);
		qs.put("password", password);
		api.request(RequestMethod.POST, "user", "register", null, qs);
		*/
	
		
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}
