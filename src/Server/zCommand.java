package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import Util.BufferUtil;
import Util.FilesUtil;
import Util.User;

public class zCommand {
	
	public static void USERcommand(User user, BufferedWriter bwPI) throws IOException {
		if (_CheckUser(user)) {
			BufferUtil.Write(bwPI, "331 User " + user.getUsername() + " OK. Password required");
		}
		else {
			BufferUtil.Write(bwPI, "500 USER: Operation not permitted");
		}
	}
	
	public static void PASScommand(User user, BufferedWriter bwPI) throws IOException {
		if (_CheckPass(user)) {
			BufferUtil.Write(bwPI, "230-Your bandwidth usage is restricted");
			BufferUtil.Write(bwPI,  "230 Current directory is /");
		}
		else {
			BufferUtil.Write(bwPI, "500 USER: Operation not permitted");
		}
	}
	
	public static void SYSTcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "215 " + System.getProperty("os.name"));
	}
	
	public static void FEATcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "211-Features:");
		BufferUtil.Write(bwPI, "AUTH PLAIN");
		BufferUtil.Write(bwPI, "CCC");
		BufferUtil.Write(bwPI, "CLNT");
		BufferUtil.Write(bwPI, "EPRT");
		BufferUtil.Write(bwPI, "PASV");
		BufferUtil.Write(bwPI, "211 End");
	}
	
	public static void PWDcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "257 \"/\" is your current location");
	}
	
	public static void TYPEcommand(String type, BufferedWriter bwPI) throws IOException {
		if(type.equalsIgnoreCase("I")) {
			BufferUtil.Write(bwPI, "200 TYPE is now 8-bit binary");
		}
		else if(type.equalsIgnoreCase("A")) {
			BufferUtil.Write(bwPI, "200 TYPE is now ASCII");
		}
	}
	
	public static void PASVcommand(int port, BufferedWriter bwPI) throws IOException {
		int a, b;
		a = port/256;
		b = port%256;
		BufferUtil.Write(bwPI, "227 Entering Passive Mode (127,0,0,1," + a + "," + b + ")");
	}
	
	public static void LISTcommand_TLS(zDTP_SSL dTP, BufferedWriter bwPI) throws IOException {
		SSLSocket socketDTP = null;
		BufferedWriter bwDTP = null;
		//PORT_TLS(dTP, socketDTP, bwDTP, bwPI);
		socketDTP = dTP.Accept();
		bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
		BufferUtil.Write(bwPI, "200 Port command");
		ArrayList<String> listFileAndFolder = FilesUtil.ListFileAndFolder("H:\\CLIENT\\TEST_01");
		for (String string : listFileAndFolder) {
			BufferUtil.Write(bwDTP, string);
		}			
		BufferUtil.Write(bwPI, "226 Xong Port command");
		socketDTP.close();
	}
	
	public static void CWDcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "250 Okay.");
	}
	
	public static void RERTcommand(zDTP_SSL dTP, BufferedWriter bwPI) throws IOException {
		SSLSocket socketDTP = null;
		BufferedWriter bwDTP = null;
		socketDTP = dTP.Accept();
    	bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
    	BufferUtil.Write(bwPI, "150 Opening");
		String path = "H:\\CLIENT\\TEST_01\\Test_01_f_01.txt";
		Send(bwDTP, path);
		BufferUtil.Write(bwPI, "226 Successfully transferred");
		socketDTP.close();
	}
	
	private static void Send(BufferedWriter bwDTP, String path) throws IOException {
    	File f = new File(path);
    	FileInputStream fin = new FileInputStream(f);
        int c;
        do {
            c = fin.read();
            if(c != -1)
            	BufferUtil.Write(bwDTP, String.valueOf(c));
            else
            	break;
        } while(true);
        fin.close();
    }
	
	public static void PBSZcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "200 PBSZ 0 successful");

	}
	
	public static void PROTcommand(String protectMode, BufferedWriter bwPI) throws IOException {
		if(protectMode.equalsIgnoreCase("P")) {
			BufferUtil.Write(bwPI, "200 Protection set to Private");
		}
	}

	private static boolean _CheckUser(User user) {
    	return true;
    }
	
	private static boolean _CheckPass(User user) {
    	return true;
    }
	
//	private static void PORT_PLAIN(BufferedWriter bwPI) throws IOException {
//
//	}
//	
//	private static void PORT_TLS(zDTP_SSL dTP, SSLSocket socketDTP, BufferedWriter bwDTP, BufferedWriter bwPI) throws IOException {
//		socketDTP = dTP.Accept();
//		bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
//		BufferUtil.Write(bwPI, "200 Port command");
//	}
}
