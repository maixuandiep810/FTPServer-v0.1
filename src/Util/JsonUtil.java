package Util;

import java.util.HashMap;

import com.google.gson.Gson;

public class JsonUtil {
	public static String ToJson(String[][] _StringArray) {
		String Result = "";
		HashMap<String, String> StringMap = new HashMap<>();
		for (String[] StringPair : _StringArray) {
			StringMap.put(StringPair[0], StringPair[1]);
		}
		Result = new Gson().toJson(StringMap, StringMap.getClass());
		return Result;
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> FromJson(String _JsonString) {
		HashMap<String, String> StringMap = new HashMap<>();
		StringMap = new Gson().fromJson(_JsonString, StringMap.getClass());
		return StringMap;
	}
}
