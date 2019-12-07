package Test_Console;

import java.io.IOException;

import Server.*;

public class Test_Server_PI_SSL {

	public static void main(String[] args) {
		try {
			zManager_SSL s = new zManager_SSL(21);
			s.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
