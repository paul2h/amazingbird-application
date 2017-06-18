package com.wavegis.global.tools;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONTool {

	public static JSONObject getJSONObject(String jsonString) {
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) new JSONParser().parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
