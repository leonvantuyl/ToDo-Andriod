package todo.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class StringUtils {

	public static String mapToQueryString(HashMap<String, Object> dict) {
		String qs = null;
		
		for ( Map.Entry<String, Object> entry : dict.entrySet() ) {
		    String key = entry.getKey();
		    String value = entry.getValue().toString();
		    
		    if(qs == null) {
		    	qs = "?"+key+"="+value;
		    } else {
		    	qs += "&"+key+"="+value;
		    }
		}
		
		return qs;
	}
	
	public static String mapToJSONString(HashMap<String, Object> map) {
		JSONObject obj = new JSONObject(map);
		return obj.toString();
	}
	
}
