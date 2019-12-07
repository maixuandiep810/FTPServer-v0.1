package Test_Console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.google.gson.Gson;

public class Test_ClientDTP {
	
	private static void Write(BufferedWriter bw, String _str) throws IOException {
		bw.write(_str);
		bw.newLine();
		bw.flush();
	}

	public static void main(String[] args) {
		
	}

	public static void a () {
		
	}
	
	public static void b () {
		Socket socket = null;
		
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			socket = new Socket("localhost", 2019);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader brS = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bwS = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));			
			while (true) {				
				while (br.ready()) {
					br.readLine();
				}
				System.out.print("Client: ");
				HashMap<String, String> RequestMap = new HashMap<>();
				RequestMap.put("action", "isFile");
				RequestMap.put("payload", "TEST_01\\Test_01_f_01.txt");
				String clientMessage = new Gson().toJson(RequestMap);
//				String clientMessage = "{" + 
//						"\"action\":\"isFile\"," + 
//						"\"payload\":\"H:\\\\TEST\\\\TEST_01\\\\Test_01_f_01.txt\"" + 
//						"}";
				// khi trong buffer con thi sao ?????
				Write(bwS, clientMessage);
				br.readLine();
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
