package Test_Console;

import java.io.IOException;

import Server.*;

public class Test_Server_PI {

	public static void main(String[] args) {
		try {
			Manager s = new Manager(21);
			s.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
