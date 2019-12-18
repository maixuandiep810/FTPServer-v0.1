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
    private Socket _SocketPI = null;
    private BufferedReader _BrPI = null;
    private BufferedWriter _BwPI = null;
    private DTP _DTP = null;
    
    private String _BasePath;
    private String _CurrentPath;
    private HashMap<String, String> _Option;
    
    private String _Username;
    private boolean _Validate;
    
    public PI(Socket socket) throws IOException {
    	NumOfPI++;
    	_DTP = new DTP(10000 + NumOfPI);
    	_SocketPI = socket;
        _BrPI = new BufferedReader(new InputStreamReader(_SocketPI.getInputStream()));
        _BwPI = new BufferedWriter(new OutputStreamWriter(_SocketPI.getOutputStream()));
        _Option = new HashMap<String, String>();
        _Username = "";
        _Validate = false;
    } 
    
    public void run() {   	
    	boolean checkLog = true; 
    	try {
			BufferUtil.Write(_BwPI, "220 Connection established");
			String Request = null;
	    	String cmd = null;
	    	User user = new User();
	    	while (true) {
	        	Request = BufferUtil.Read(_BrPI);
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
						_Option.put("AUTH", "PLAIN");
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
							int port = _DTP.get_Port();
							Command.PASVcommand(port, _BwPI);
							break;
	//					case "PORT":
	//						BufferUtil.Write(bw, "125 abc");
	//						break; 
						case "LIST": 
							Command.LISTcommand_PLAIN(_Option.get("AUTH"), _BasePath, _CurrentPath, _DTP, null, _BwPI);
							break;
						case "CWD":
							String currentPath = Request.split("\\s")[1];
							if (currentPath.charAt(0) != '\\') {
								currentPath = _CurrentPath + "\\" + currentPath;
							}
							if ( (currentPath = Command.CWDcommand(_BasePath, currentPath, _BwPI)) != null) {
								_CurrentPath = currentPath;
							}
							break;
						case "CDUP": 
							if ((currentPath = Command.CDUPcommand(_BasePath, _CurrentPath, _BwPI)) != null) {
								_CurrentPath = currentPath;
							}
							break;
						case "RETR":
							String fileName = Request.substring(5);
							Command.RERTcommand_PLAIN(_BasePath, _CurrentPath, fileName, _Option.get("TYPE"),  _DTP, _BwPI);
							break;
							
						case "STOR":
							fileName = Request.split("\\s")[1];
							Command.STORcommand_PLAIN(_BasePath, _CurrentPath, fileName, _DTP, _BwPI);
							break;	
							
						case "PBSZ": // Tham so 0
							Command.PBSZcommand(_BwPI);
							break;
						case "PROT":
							String protectMode = Request.split("\\s")[1];
							Command.PROTcommand(protectMode, _BwPI);
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
		} finally {
			try {
				if (_SocketPI != null) {
					_SocketPI.close();
				}
				if (_DTP != null) {
					_DTP.Close();
				}
			} catch (IOException e2) {
			}
		}	
    NumOfPI--;
    }
}