package Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import Model.BO.CheckLoginBO;
import Model.BO.UserPathBO;
import Util.BufferUtil;
import Util.FilesUtil;
import Model.BEAN.*;

public class Command {
	
	private static final String TLS = "TLS";
	private static final String PLAIN = "PLAIN";

	
	public static void AUTHcommand(String auth, BufferedWriter bwPI) throws IOException {
		if (auth.equalsIgnoreCase(TLS)) {
			BufferUtil.Write(bwPI, "234 AUTH TLS successful");
		}
		else {
			BufferUtil.Write(bwPI, "500");
		}
	}
	
	public static void USERcommand(User user, BufferedWriter bwPI) throws IOException {
		if (_CheckUser(user)) {
			BufferUtil.Write(bwPI, "331 User " + user.getUsername() + " OK. Password required");
		}
		else {
			BufferUtil.Write(bwPI, "500 USER: Operation not permitted");
		}
	}
	
	public static String PASScommand(User user, BufferedWriter bwPI) throws IOException {
		String basePath = null;
		if (_CheckPass(user)) {
			UserPathBO userPathBO = new UserPathBO();
			basePath = userPathBO.getPath(user.getUsername());
			BufferUtil.Write(bwPI, "230-Your bandwidth usage is restricted");
			BufferUtil.Write(bwPI,  "230 Current directory is \\");
		}
		else {
			BufferUtil.Write(bwPI, "500 USER: Operation not permitted");
		}
		return basePath;
	}
	
	public static void SYSTcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "215 " + System.getProperty("os.name"));
	}
	
	public static void FEATcommand(String type, BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "211-Features:");
		if (type.equals(PLAIN)) {
			BufferUtil.Write(bwPI, "AUTH PLAIN");
		}
		else if (type.equals(TLS)) {
			BufferUtil.Write(bwPI, "AUTH TLS");
		}
		BufferUtil.Write(bwPI, "PASV");
		BufferUtil.Write(bwPI, "211 End");
	}
	
	public static void PWDcommand(String path, BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "257 \"" + path + "\" is your current location");
	}
	
	public static String TYPEcommand(String type, BufferedWriter bwPI) throws IOException {
		if(type.equalsIgnoreCase("I")) {
			BufferUtil.Write(bwPI, "200 TYPE is now 8-bit binary");
			return "I";
		}
		else if(type.equalsIgnoreCase("A")) {
			BufferUtil.Write(bwPI, "200 TYPE is now ASCII");
			return "A";
		}
		return "";
	}
	
	public static void PASVcommand(int port, BufferedWriter bwPI) throws IOException {
		int a, b;
		a = port/256;
		b = port%256;
		BufferUtil.Write(bwPI, "227 Entering Passive Mode (127,0,0,1," + a + "," + b + ")");
	}
	

//	private static void LISTcommand(String basePath, String currentPath, BufferedWriter bwDTP, BufferedWriter bwPI) throws IOException {
//		String path = basePath + currentPath;
//		BufferUtil.Write(bwPI, "200 Port command");
//		ArrayList<String> listFileAndFolder = FilesUtil.ListFileAndFolder(path);	
//		for (String string : listFileAndFolder) {
//			if (FilesUtil.isFolder(path + "\\" + string)) { //13bytes
//				BufferUtil.Write(bwDTP, "drwxr-xr-x 1 user user          4422 Jan 11 11:01 " + string);
//			}
//			else if (FilesUtil.isFile(path + "\\" + string)) {
//				BufferUtil.Write(bwDTP, "-rw-r--r-- 1 user user           213 Jan 11 11:01 " + string);
//			}
//		}	
//		BufferUtil.Write(bwPI, "226 Xong LIST command");		
//	}
//	
//	public static void LISTcommand_TLS(String basePath, String currentPath, DTP_SSL dTP, BufferedWriter bwPI) throws IOException {
//		SSLSocket socketDTP = dTP.Accept();
//		BufferedWriter bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
//    	LISTcommand(basePath, currentPath, bwDTP, bwPI);
//		socketDTP.close();
//	}

	public static void LISTcommand(String type, String basePath, String currentPath, DTP dTP, DTP_SSL dTP_SSL, BufferedWriter bwPI) throws IOException {
		BufferedWriter bwDTP = null;
		Socket socketDTP = null;
		SSLSocket socketDTP_SSL = null;
		if (type.equals(PLAIN)) {
			socketDTP = dTP.Accept();
			bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));		}
		else if (type.equals(TLS)) {
			socketDTP_SSL = dTP_SSL.Accept();
			bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP_SSL.getOutputStream(), "UTF-8"));
		}
		String path = basePath + currentPath;
		BufferUtil.Write(bwPI, "200 Port command");
		ArrayList<String> listFileAndFolder = FilesUtil.ListFileAndFolder(path);	
		for (String string : listFileAndFolder) {
			if (FilesUtil.isFolder(path + "\\" + string)) { //13bytes
				BufferUtil.Write(bwDTP, "drwxr-xr-x 1 user user             ? Jan 11 11:01 " + string);
			}
			else if (FilesUtil.isFile(path + "\\" + string)) {
				BufferUtil.Write(bwDTP, "-rw-r--r-- 1 user user             ? Jan 11 11:01 " + string);
			}
		}	
		BufferUtil.Write(bwPI, "226 Xong LIST command");
		if (socketDTP != null) {
			socketDTP.close();
		}
		if (socketDTP_SSL != null) {
			socketDTP_SSL.close();
		}
	}
	
	public static String CWDcommand(String basePath, String currentPath, BufferedWriter bwPI) throws IOException {
		String path = basePath + currentPath;
		if (FilesUtil.isFolder(path)) {
			BufferUtil.Write(bwPI, "250 Okay.");
			return currentPath;
		}
		BufferUtil.Write(bwPI, "550 " + currentPath + ": No such file or directory.");
		return "";
	}
	
	public static String CDUPcommand(String basePath, String currentPath, BufferedWriter bwPI) throws IOException {
		String path;
		String parentPath = FilesUtil.getParent(currentPath);
		path = basePath + currentPath;
		if (FilesUtil.isFolder(path)) {
			BufferUtil.Write(bwPI, "250 Okay.");
			return parentPath;
		}
		BufferUtil.Write(bwPI, "550 " + currentPath + ": No such file or directory.");
		return "";
	}
	
	public static void RERTcommand_TLS(DTP_SSL dTP, BufferedWriter bwPI) throws IOException {
		SSLSocket socketDTP = dTP.Accept();
		BufferedWriter bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
    	//RERTcommand(bwDTP, bwPI);
		socketDTP.close();
	}
	
	public static void RERTcommand_PLAIN(String basePath, String currentPath, String fileName, String type, DTP dTP, BufferedWriter bwPI) throws IOException {
		Socket socketDTP = dTP.Accept();
		//BufferedWriter bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
		DataOutputStream dosDTP = new DataOutputStream(socketDTP.getOutputStream());
    	RERTcommand2(basePath, currentPath, fileName, type, dosDTP, bwPI);
		socketDTP.close();
	}
	
//	public static void RERTcommand(String basePath, String currentPath, String fileName, String type, BufferedWriter bwDTP, BufferedWriter bwPI) throws IOException {
//    	BufferUtil.Write(bwPI, "150 Opening");
//		String path = basePath + "\\" + currentPath + "\\" + fileName;
//		Send(bwDTP, path, type);
//		BufferUtil.Write(bwPI, "226 Successfully transferred");
//	}
	public static void RERTcommand2(String basePath, String currentPath, String fileName, String type, DataOutputStream dosDTP, BufferedWriter bwPI) throws IOException {
    	BufferUtil.Write(bwPI, "150 Opening");
		String path = basePath + "\\" + currentPath + "\\" + fileName;
		Send(null, dosDTP, path, type);
		BufferUtil.Write(bwPI, "226 Successfully transferred");
	}
	//
	//
	//
	//
	public static void STORcommand_TLS(DTP_SSL dTP, BufferedWriter bwPI) throws IOException {
//		SSLSocket socketDTP = dTP.Accept();
//		BufferedWriter bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
//    	//RERTcommand(bwDTP, bwPI);
//		socketDTP.close();
	}
	
	public static void STORcommand_PLAIN(String basePath, String currentPath, String fileName, DTP dTP, BufferedWriter bwPI) throws IOException {
		Socket socketDTP = dTP.Accept();
		BufferedReader brDTP = new BufferedReader(new InputStreamReader(socketDTP.getInputStream(), "UTF-8"));
    	STORcommand(basePath, currentPath, fileName, brDTP, bwPI);
		socketDTP.close();
	}
	
	public static void STORcommand(String basePath, String currentPath, String fileName, BufferedReader brDTP, BufferedWriter bwPI) throws IOException {
    	BufferUtil.Write(bwPI, "150 Connecting to server");
		String path = basePath + "\\" + currentPath + "\\" + fileName;
		Receive(brDTP, path);
		BufferUtil.Write(bwPI, "226 Successfully transferred");
	}
	
	private static void Receive(BufferedReader brDTP, String path) throws IOException {
		File f = new File(path);
		if (FilesUtil.isFile(path) == false) {
			f.createNewFile();
		}
    	FileOutputStream fout = new FileOutputStream(f);
    	String str;
        char c = 0;
        do {
            str= BufferUtil.Read(brDTP);
            if (str == null) 
            	break;
            for (int i = 0; i < str.length(); i++) {
            	c = str.charAt(i);
            	if(c != -1)
                	fout.write(c);
                else
                	break;
			}
            if(c == -1)
            	break;
            fout.write('\r');
            fout.write('\n');
        } while(true);
        fout.close();
	}
	
	private static void Send(BufferedWriter bwDTP, DataOutputStream dosDTP, String path, String type) throws IOException {
		File f = new File(path);
    	FileInputStream fin = new FileInputStream(f);
        if (type == "I") {
			dosDTP.write(fin.readAllBytes());
		}
        else {
        	int c;
            do {
                c = fin.read();
                if(c != -1)
                	BufferUtil.Write(bwDTP, (char) c);
                else
                	break;
            } while(true);
		}
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
		CheckLoginBO checkLoginBO = new CheckLoginBO();
    	return checkLoginBO.isValidUser(user.getUsername());
    }
	
	private static boolean _CheckPass(User user) {
		CheckLoginBO checkLoginBO = new CheckLoginBO();
    	return checkLoginBO.isValidPassword(user.getUsername(), user.getPassword());
    }
}
