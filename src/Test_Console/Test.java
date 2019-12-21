package Test_Console;

import java.io.IOException;

import Server.*;

public class Test {

	public static void main(String[] args) {
		try {
			//Manager_PLAIN s = new Manager_PLAIN(2222);
			//s.start();
			Manager_SSL ss = new Manager_SSL(990);
			ss.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
