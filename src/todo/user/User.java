package todo.user;

import android.content.SharedPreferences;

public class User {

	public static String TOKEN;

	public static boolean loggedIn(SharedPreferences prefs) {
		String token = prefs.getString("token", null);
		if(token != null && !token.isEmpty())
		{
			TOKEN = token;
		}
		return false;
	}

	public static void login(String insertedToken, SharedPreferences prefs) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("token", insertedToken);
		editor.commit();
		if(!insertedToken.isEmpty())
		{
			TOKEN = insertedToken;
		}		
	}

}
