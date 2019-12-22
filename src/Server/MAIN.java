package Server;

import java.io.IOException;

import Server.*;

public class MAIN {

	public static void main(String[] args) {
		try {
			Manager_PLAIN s = new Manager_PLAIN(21);
			s.start();
			Manager_SSL ss = new Manager_SSL(990);
			ss.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
