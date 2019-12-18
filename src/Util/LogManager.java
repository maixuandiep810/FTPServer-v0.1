package Util;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
	public static Logger log = Logger.getLogger("ftpServerLog");
	public static FileHandler fl;
	public static String fileLog = "FtpPasswLog.log";
	public static boolean activated = false;
	


	public static Logger getInstance(){
		if (activated == false){
			return null;
		}
		else
			return log;
	}


//	public static void setInstance (Path path) throws SecurityException, IOException {
//		FolderManager.createDirectory(path.getParent());
//		fl = new FileHandler(path.toString(), true);
//		log.setLevel(Level.FINEST);
//		fl.setLevel(Level.FINEST);
//		log.addHandler(fl);
//		fl.setFormatter(new SimpleFormatter());
//		log.log(Level.FINE,"Created log in " + path + "\n");
//		System.out.println("Created log in " + path + "\n");
//		activated = true;
//	}
	
	/*public static void main(String[] args) throws IOException, SecurityException {
		Path path = FolderManager.defaultFolder();
		setInstance(path);
		LogManager.getInstance().log(Level.FINE, "msg");
		LogManager.getInstance().log(Level.WARNING, "msg");
	}*/
}
