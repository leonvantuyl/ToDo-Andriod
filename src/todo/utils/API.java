package todo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import todo.main.R;
import todo.utils.http.HttpDeleteWithBody;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class API {

	private String host = "http://sloot.homeip.net/todo/api";
	private OnAPIRequestListener apiListener;
	private Context ctx;

	public enum RequestMethod { GET, POST, PUT, DELETE };

	public API(OnAPIRequestListener listener, Context context) {
		this.apiListener = listener;
		ctx = context;
	}

	public void request(RequestMethod method, String controller, HashMap<String, Object> qs, HashMap<String, Object> bodyArgs) {
		request(method, controller, method.name().toLowerCase(), qs, bodyArgs);
	}

	public void request(RequestMethod method, String controller, String action, HashMap<String, Object> qs, HashMap<String, Object> bodyArgs) {
		String url = this.host + "/" +controller + "/" + action;
		requestWithUrl(method, url, qs, bodyArgs);
	}

	@SuppressLint("DefaultLocale")
	public void requestWithUrl(RequestMethod method, String url, HashMap<String, Object> qs, HashMap<String, Object> bodyArgs) {
		if(isNetworkAvailable())
		{
			String queryString = "";
			if(qs != null) {
				queryString = StringUtils.mapToQueryString(qs);
			}
			String bodyArgsStr = null;
			if(bodyArgs != null && method != RequestMethod.GET) {
				bodyArgsStr = StringUtils.mapToJSONString(bodyArgs);
			}
			new HttpAsyncTask().execute(method.name().toUpperCase(), url + queryString, bodyArgsStr);
		}else
		{
			noInternetAlert();
		}
	}

	private void noInternetAlert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
		alert.setTitle(R.string.dialog_noInternet_title);		
		alert.setMessage(R.string.dialog_noInternet_description);
 
		alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//close
			}
		});	
		
		alert.show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static String doRequest(String method, String url, String bodyArgsStr) {
		InputStream inputStream = null;
		String result = "";
		int statusCode = 0;
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			RequestMethod m = RequestMethod.valueOf(method);
			HttpUriRequest request = null;
			HttpEntityEnclosingRequestBase entityRequest = null;
			switch(m) {
			case GET:
				request = new HttpGet(url);
				break;
			case POST:
				entityRequest = new HttpPost(url);
			case PUT:
				entityRequest = new HttpPut(url);
			case DELETE:
				entityRequest = new HttpDeleteWithBody(url);
				if(bodyArgsStr != null) {
					StringEntity strEnt = new StringEntity(bodyArgsStr);
					entityRequest.setEntity(strEnt);
				}
				break;
			}
			//request.setHeader("Content-Type", "application/json");

			HttpResponse httpResponse = httpclient.execute((request != null) ? request : entityRequest);
			//HttpResponse httpResponse = httpclient.execute(request);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			//result = "Something";
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return statusCode + "___" + result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public interface OnAPIRequestListener {
		public void onSuccess(int statusCode, String result);
		public void onError(int statusCode, String result);
		//public void onBeforeSend(String url);
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			return doRequest(args[0], args[1], args[2]);
		}

		protected void onPostExecute(String resultStr) {
			String[] args = resultStr.split("___");
			int statusCode = Integer.parseInt(args[0]);
			String result = args[1];

			if(statusCode >= 200 && statusCode < 300) {
				apiListener.onSuccess(statusCode, result);
			} else {
				apiListener.onError(statusCode, result);
			}
		}

	}

}
