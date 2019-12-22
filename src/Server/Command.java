package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import Util.CONFIG;
import Util.FilesUtil;
import Model.BEAN.*;

public class Command {
	
	private static final String TLS = "TLS";
	private static final String PLAIN = "PLAIN";

	/**
	 * 
	 */
	public static void AUTHcommand(String auth, BufferedWriter bwPI) throws IOException {
		if (auth.equalsIgnoreCase(TLS)) {
			BufferUtil.Write(bwPI, "234 AUTH TLS successful");
		}
		else {
			BufferUtil.Write(bwPI, "500");
		}
	}
	/**
	 * 
	 */
	public static void USERcommand(User user, BufferedWriter bwPI) throws IOException {
		if (_CheckUser(user)) {
			BufferUtil.Write(bwPI, "331 User " + user.getUsername() + " OK. Password required");
		}
		else {
			BufferUtil.Write(bwPI, "500 USER: Operation not permitted");
		}
	}
	/**
	 * 
	 */
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
	/**
	 * 
	 */
	public static void SYSTcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "215 " + System.getProperty("os.name"));
	}
	/**
	 * 
	 */
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
	/**
	 * 
	 */
	public static void PWDcommand(String path, BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "257 \"" + path + "\" is your current location");
	}
	/**
	 * 
	 */
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
	/**
	 * 
	 */
	public static void PASVcommand(int port, BufferedWriter bwPI) throws IOException {
		int a, b;
		a = port/256;
		b = port%256;
		String host = CONFIG.MY_HOST.replaceAll("\\.", ",");
		BufferUtil.Write(bwPI, "227 Entering Passive Mode (" + host + "," + a + "," + b + ")");
	}
	/**
	 * 
	 */
	public static void LISTcommand(String auth, String basePath, String currentPath, DTP_PLAIN dTP, DTP_SSL dTP_SSL, BufferedWriter bwPI) throws IOException {
		BufferedWriter bwDTP = null;
		Socket socketDTP = null;
		SSLSocket socketDTP_SSL = null;
		switch (auth) {
			case PLAIN:
				socketDTP = dTP.Accept();
				bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
				break;
			case TLS:
				socketDTP_SSL = dTP_SSL.Accept();
				bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP_SSL.getOutputStream(), "UTF-8"));
				break;
		}
		String path = basePath + currentPath;
		BufferUtil.Write(bwPI, "200 List command");
		ArrayList<String> listFileAndFolder = FilesUtil.ListFileAndFolder(path);	
		for (String string : listFileAndFolder) {
			if (FilesUtil.isFolder(path + "\\" + string)) { //13bytes
				BufferUtil.Write(bwDTP, "drwxr-xr-x 1 user user             0 Jan 11 11:01 " + string);
			}
			else if (FilesUtil.isFile(path + "\\" + string)) {
				BufferUtil.Write(bwDTP, "-rw-r--r-- 1 user user             0 Jan 11 11:01 " + string);
			}
		}	
		BufferUtil.Write(bwPI, "226 LIST command success");
		if (socketDTP != null) {
			socketDTP.close();
		}
		if (socketDTP_SSL != null) {
			socketDTP_SSL.close();
		}
	}
	/**
	 * Chi chuyen den duong dan SUB FOLDER
	 */
	public static String CWDcommand(String basePath, String currentPath, BufferedWriter bwPI) throws IOException {
		String path = basePath + currentPath;
		if (FilesUtil.isFolder(path)) {
			BufferUtil.Write(bwPI, "250 Okay.");
			return currentPath;
		}
		BufferUtil.Write(bwPI, "550 " + currentPath + ": No such file or directory.");
		return "";
	}
	/**
	 * Chuyen den duong dan PARENT FOLDER  
	 */
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
	/**
	 * 
	 */
	public static void RERTcommand(String auth, String type, String basePath, String currentPath, String fileName, DTP_PLAIN dTP, DTP_SSL dTP_SSL, BufferedWriter bwPI) throws IOException {
		BufferedWriter bwDTP = null;
		DataOutputStream dosDTP = null;
		Socket socketDTP = null;
		SSLSocket socketDTP_SSL = null;
		
		switch (auth) {
			case PLAIN:
				socketDTP = dTP.Accept();
				if (type.contentEquals("I"))
					dosDTP = new DataOutputStream(socketDTP.getOutputStream());
				else if (type.contentEquals("A"))
					bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP.getOutputStream(), "UTF-8"));
				break;
			case TLS:
				socketDTP_SSL = dTP_SSL.Accept();
				if (type.contentEquals("I"))
					dosDTP = new DataOutputStream(socketDTP_SSL.getOutputStream());
				else if (type.contentEquals("A"))
					bwDTP = new BufferedWriter(new OutputStreamWriter(socketDTP_SSL.getOutputStream(), "UTF-8"));
				break;
		}
		BufferUtil.Write(bwPI, "150 Opening");
		String path = FilesUtil.getAbsoluteFilePath(basePath, currentPath, fileName);
		Send(dosDTP, bwDTP, path, type);
		BufferUtil.Write(bwPI, "226 Successfully transferred");
		if (socketDTP != null) {
			socketDTP.close();
		}
		if (socketDTP_SSL != null) {
			socketDTP_SSL.close();
		}
	}
	public static void STORcommand(String auth, String type, String basePath, String currentPath, String fileName, DTP_PLAIN dTP, DTP_SSL dTP_SSL, BufferedWriter bwPI) throws IOException {
		BufferedReader brDTP = null;
		DataInputStream dinDTP = null;
		Socket socketDTP = null;
		SSLSocket socketDTP_SSL = null;
		
		switch (auth) {
			case PLAIN:
				socketDTP = dTP.Accept();
				if (type.contentEquals("I"))
					dinDTP = new DataInputStream(socketDTP.getInputStream());
				else if (type.contentEquals("A"))
					brDTP = new BufferedReader(new InputStreamReader(socketDTP.getInputStream(), "UTF-8"));
				break;
			case TLS:
				socketDTP_SSL = dTP_SSL.Accept();
				if (type.contentEquals("I"))
					dinDTP = new DataInputStream(socketDTP_SSL.getInputStream());
				else if (type.contentEquals("A"))
					brDTP = new BufferedReader(new InputStreamReader(socketDTP_SSL.getInputStream(), "UTF-8"));
				break;
		}
		BufferUtil.Write(bwPI, "150 Connecting to server");
		String path = FilesUtil.getAbsoluteFilePath(basePath, currentPath, fileName);
		Receive(dinDTP, brDTP, path, type);
		BufferUtil.Write(bwPI, "226 Successfully transferred");
		if (socketDTP != null) {
			socketDTP.close();
		}
		if (socketDTP_SSL != null) {
			socketDTP_SSL.close();
		}
	}
	/**
	 * 
	 */
	private static void Receive(DataInputStream dinDTP, BufferedReader brDTP, String path, String type) throws IOException {
		File f = new File(path);
		if (FilesUtil.isFile(path) == false) {
			f.createNewFile();
		}
		FileOutputStream fout;
    	switch (type) {
		case "I":
			fout = new FileOutputStream(f);
			fout.write(dinDTP.readAllBytes());
			fout.close();
			break;
		case "A":
			fout = new FileOutputStream(f);
			String data;
			while ((data = BufferUtil.Read(brDTP)) != null) {
				fout.write(data.getBytes("UTF-8"));
				fout.write("\r\n".getBytes("UtF-8"));
			}
			fout.close();
			break;
    	}
	}
	/**
	 * 
	 */
	private static void Send(DataOutputStream dosDTP, BufferedWriter bwDTP, String path, String type) throws IOException {
		File f = new File(path);
    	FileInputStream fin = new FileInputStream(f);
    	byte[] data = fin.readAllBytes();
    	int i = 0;
    	switch (type) {
			case "I":
				while (true) {
					if (i + 1024 > data.length) {
						dosDTP.write(data, 0, data.length);
						break;
					}
					else {
						dosDTP.write(data, i, 1024);
						i += 1024;
					}
				}
				dosDTP.writeBytes("\r\n");
				break;
			case "A":
				while (true) {
					if (i + 1024 > data.length) {
						BufferUtil.Write_No_Newline(bwDTP, new String(data, 0, data.length, "UTF-8"));
						break;
					}
					else {
						BufferUtil.Write_No_Newline(bwDTP, new String(data, i, 1024, "UTF-8"));
						i += 1024;
					}
				}
				BufferUtil.Write_Newline(bwDTP);
				break;
		}
        fin.close();
	}
	/**
	 * 
	 */
	public static void PBSZcommand(BufferedWriter bwPI) throws IOException {
		BufferUtil.Write(bwPI, "200 PBSZ 0 successful");

	}
	/**
	 * 
	 */
	public static void PROTcommand(String protectMode, BufferedWriter bwPI) throws IOException {
		if(protectMode.equalsIgnoreCase("P")) {
			BufferUtil.Write(bwPI, "200 Protection set to Private");
		}
	}
	/**
	 * 
	 */
	public static void MKDcommand(String basePath, String currentPath, String folderName, BufferedWriter bwPI) throws IOException {
		String path = FilesUtil.getAbsoluteFilePath(basePath, currentPath, folderName);
		if (FilesUtil.isExistFile(path) == false && FilesUtil.mkdFolder(path)) {
				BufferUtil.Write(bwPI, "257 " + folderName + " : The directory was successfully created.");
		}
		else {
			BufferUtil.Write(bwPI, "550 the creation failed.");
		}
	}
	
	
	public static void DELEcommand(String basePath, String currentPath, String fileName, BufferedWriter bwPI) throws IOException {
		String path = FilesUtil.getAbsoluteFilePath(basePath, currentPath, fileName);
		if (FilesUtil.isExistFile(path) == true && FilesUtil.deleteFile(path)) {
				BufferUtil.Write(bwPI, "250 " + fileName + " : The directory was successfully created.");
		}
		else {
			BufferUtil.Write(bwPI, "550 the deletion failed.");
		}
	}
	public static boolean RNFRcommand(String basePath, String currentPath, String fileName, BufferedWriter bwPI) throws IOException {
		String path = FilesUtil.getAbsoluteFilePath(basePath, currentPath, fileName);
		if (FilesUtil.isExistFile(path) == true) {
			BufferUtil.Write(bwPI, "350 RNFR accepted - file exists, ready for destination");
			return true;
		}
		else {
			BufferUtil.Write(bwPI, "550 Failed.");
			return false;
		}
	}
	public static boolean RNTOcommand(String basePath, String currentPath, String source, String target, BufferedWriter bwPI) throws IOException {
		source = FilesUtil.getAbsoluteFilePath(basePath, currentPath, source);
		target = FilesUtil.getAbsoluteFilePath(basePath, "\\", target);
		if (FilesUtil.renameFile(source, target)) {
			BufferUtil.Write(bwPI, "250 File successfully renamed or moved");
			return true;
		}
		else {
			BufferUtil.Write(bwPI, "550 Failed.");
			return false;
		}
	}
	
	/**
	 * 
	 */
	private static boolean _CheckUser(User user) {
		CheckLoginBO checkLoginBO = new CheckLoginBO();
    	return checkLoginBO.isValidUser(user.getUsername());
    }
	/**
	 * 
	 */
	private static boolean _CheckPass(User user) {
		CheckLoginBO checkLoginBO = new CheckLoginBO();
    	return checkLoginBO.isValidPassword(user.getUsername(), user.getPassword());
    }
	public static void RMDcommand(String basePath, String currentPath, String folderName, BufferedWriter bwPI) throws IOException {
		String path = FilesUtil.getAbsoluteFilePath(basePath, currentPath, folderName);
		if (FilesUtil.isExistFile(path) == true && FilesUtil.deleteFolder(path)) {
				BufferUtil.Write(bwPI, "250 " + folderName + " : The directory was successfully created.");
		}
		else {
			BufferUtil.Write(bwPI, "550 the deletion failed.");
		}
	}
	
}
