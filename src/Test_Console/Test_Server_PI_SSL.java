package Test_Console;

import java.io.IOException;

import Server.*;

public class Test_Server_PI_SSL {

	public static void main(String[] args) {
		try {
			Manager_SSL s = new Manager_SSL(21);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
