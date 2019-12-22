package Util;

public class CONFIG {
	
	public static boolean DEBUG = true;
	public static String MY_HOST = "192.168.0.147";
	
	public static void print(String str) {
		if (DEBUG) {
			System.out.println("> " + str);
		}
	}
}
