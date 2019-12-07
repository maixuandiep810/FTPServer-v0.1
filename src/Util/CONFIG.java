package Util;

public class CONFIG {
	public static boolean DEBUG = true;
	public static String PATH_UPLOAD = "H:\\TEST";
	
	public static void print(String str) {
		if (DEBUG) {
			System.out.println("> " + str);
		}
	}
}
