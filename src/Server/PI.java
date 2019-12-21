package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import Util.*;
import Model.BEAN.*;


public class PI extends Thread {
	public static int NumOfPI = 0;
    protected Socket _SocketPI = null;
    protected BufferedReader _BrPI = null;
    protected BufferedWriter _BwPI = null;
    protected DTP_PLAIN _DTP_PLAIN = null;
    protected DTP_SSL _DTP_SSL = null;
    
    
    protected String _BasePath;
    protected String _CurrentPath;
    protected HashMap<String, String> _Option;
    
    protected boolean _Validate;
    private static final String TLS = "TLS";
	private static final String PLAIN = "PLAIN";
	
    public PI(Socket socket, String auth) throws IOException {
    	NumOfPI = (NumOfPI >= 50000) ? 0 : NumOfPI;
    	NumOfPI++;
    	_Option = new HashMap<String, String>();
    	_Option.put("AUTH", auth);
    	switch (auth) {
		case PLAIN:
			_DTP_PLAIN = new DTP_PLAIN(10000 + NumOfPI);
			break;
		case TLS:
			_DTP_SSL = new DTP_SSL(10000 + NumOfPI);
			break;
    	}
    	_SocketPI = socket;
        _BrPI = new BufferedReader(new InputStreamReader(_SocketPI.getInputStream()));
        _BwPI = new BufferedWriter(new OutputStreamWriter(_SocketPI.getOutputStream()));
        _Validate = false;
    } 
    
    public void run() {   	
    	boolean checkLog = true; 
    	try {
			BufferUtil.Write(_BwPI, "220 Connection established");
			String Request = null;
	    	String cmd = null;
	    	User user = new User();
	    	boolean quit = false;
	    	while (quit == false) {
	        	Request = BufferUtil.Read(_BrPI);
	        	if (Request == null) {
					break;
				}
	        	System.out.println(Request);
	        	cmd = Request.split("\\s")[0];
	        	switch (cmd) {
		        	case "USER":
						user.setUsername(Request.split("\\s")[1]);
						Command.USERcommand(user, _BwPI);
						break;
					case "PASS":
						user.setPassword(Request.split("\\s")[1]);
						_BasePath = Command.PASScommand(user, _BwPI);
						if (_BasePath == null) {
							checkLog = false;
							break;
						}
						_Validate = true;
						_CurrentPath = "\\";
						break;
				}
	        	if (checkLog == false) {
					break;
				}
	        	if (_Validate == true) {
	        		switch (cmd) {
						case "SYST":
							Command.SYSTcommand(_BwPI);
						case "FEAT":
							Command.FEATcommand(_Option.get("AUTH"), _BwPI);				
							break;
						case "PWD":
							Command.PWDcommand(_CurrentPath, _BwPI);
							break;
						case "TYPE":
							String type = Request.split("\\s")[1];
							if ((type = Command.TYPEcommand(type, _BwPI)) == "") {
							}
							else {
								_Option.put("TYPE", type);
							}
							break;
						case "PASV":
							int port = 0;;
							switch (_Option.get("AUTH")) {
								case PLAIN:
									port = _DTP_PLAIN.get_Port(); 
									break;
								case TLS:
									port = _DTP_SSL.get_Port();
									break;
					    	}
							Command.PASVcommand(port, _BwPI);
							break;
	//					case "PORT":
	//						BufferUtil.Write(bw, "125 abc");
	//						break; 
						case "LIST": 
							Command.LISTcommand(_Option.get("AUTH"), _BasePath, _CurrentPath, _DTP_PLAIN, _DTP_SSL, _BwPI);
							break;
						case "CWD":
							String tempPath = Request.split("\\s")[1];
							if (tempPath.charAt(0) != '\\') {
								tempPath = _CurrentPath + "\\" + tempPath;
							}
							if ( (tempPath = Command.CWDcommand(_BasePath, tempPath, _BwPI)) != null) {
								_CurrentPath = tempPath;
							}
							break;
						case "CDUP": 
							if ((tempPath = Command.CDUPcommand(_BasePath, _CurrentPath, _BwPI)) != null) {
								_CurrentPath = tempPath;
							}
							break;
						case "RETR":
							String fileName = Request.substring(5);
							Command.RERTcommand(_Option.get("AUTH"), _Option.get("TYPE"), _BasePath, _CurrentPath, fileName,  _DTP_PLAIN, _DTP_SSL, _BwPI);
							break;
							
						case "STOR":
							fileName = Request.substring(5);
							Command.STORcommand(_Option.get("AUTH"), _Option.get("TYPE"), _BasePath, _CurrentPath, fileName,  _DTP_PLAIN, _DTP_SSL, _BwPI);
							break;	
							
						case "PBSZ": // Tham so 0
							Command.PBSZcommand(_BwPI);
							break;
						case "PROT":
							String protectMode = Request.split("\\s")[1];
							Command.PROTcommand(protectMode, _BwPI);
							break;
						case "MKD":
							String folderName = Request.substring(4);
							Command.MKDcommand(_BasePath, _CurrentPath, folderName, _BwPI);
							break;
						case "RMD":
							folderName = Request.substring(4);
							Command.RMDcommand(_BasePath, _CurrentPath, folderName, _BwPI);
							break;
						case "DELE":
							fileName = Request.substring(5);
							Command.DELEcommand(_BasePath, _CurrentPath, fileName, _BwPI);
							break;
						case "RNFR":
							fileName = Request.substring(5);
							if (Command.RNFRcommand(_BasePath, _CurrentPath, fileName, _BwPI)) {
								_Option.put("RNFR", fileName);
							}
							break;
						case "RNTO":
							fileName = Request.substring(5);
							Command.RNTOcommand(_BasePath, _CurrentPath, _Option.get("RNFR"), fileName, _BwPI);
							break;
						case "QUIT":
							quit = true;
							BufferUtil.Write(_BwPI, "221 BYE");
							break;
							/*
						case:
							BufferUtil.Write(bw, "");
							break;
							*/
						default:
							break;
					}	    			
				}
	    	}
		} catch (IOException e) {
			CONFIG.print(e.toString());
			System.err.println(e);
		}	
    }
}