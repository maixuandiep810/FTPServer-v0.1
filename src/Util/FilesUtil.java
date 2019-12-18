package Util;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.filechooser.FileSystemView;
import javax.xml.datatype.DatatypeFactory;

public class FilesUtil {
	private static int id = 1;
	/*
	 * 
	 */
	public static ArrayList<HashMap<String, String>> ListFolderAndSubFolder(ArrayList<HashMap<String, String>> _Result, String _DirectoryName, int _ParentId) {
		File Directory = new File(_DirectoryName);
		File[] fList = Directory.listFiles();
		System.out.println(fList.length);
		
		if (fList.length > 0 && fList != null) {
			for (File item : fList) {
				if (item.isDirectory()) {
					System.out.println("currentId: " + id +  ", parentId: " + _ParentId + " -- " + item.getName());
					HashMap<String, String> Folder = new HashMap<String, String>();
					_Result.add(Folder);
					_Result.get(_ParentId).put(id+"", item.getName());
					ListFolderAndSubFolder(_Result, item.getAbsolutePath(), id++);
				}
			}
		}
		return _Result;
	} 
	/*
	 * 
	 */
	public static ArrayList<String> ListFileAndFolder(String _DirectoryName) {
		ArrayList<String> list = new ArrayList<String>();
		File Directory = new File(_DirectoryName);
		File[] fList = Directory.listFiles();
		for (File item : fList) {
			list.add(item.getName());
		}
		return list;
	}
	/*
	 * 
	 */
	public static ArrayList<String> ListFolder(String _DirectoryName) {
		ArrayList<String> list = new ArrayList<String>();
		File Directory = new File(_DirectoryName);
		File[] fList = Directory.listFiles();
		for (File item : fList) {
			if (item.isDirectory()) {
				list.add(item.getName());
			}
		}
		return list;
	}
	/*
	 * 
	 */
	public static ArrayList<String> ListFile(String _DirectoryName) {
		ArrayList<String> list = new ArrayList<String>();
		File Directory = new File(_DirectoryName);
		File[] fList = Directory.listFiles();
		for (File item : fList) {
			if (item.isFile()){
				list.add(item.getName());
			}
		}
		return list;
	}
	/*
	 * 
	 */
	public static void ListDriver() {
		File[] paths;
		FileSystemView fsv = FileSystemView.getFileSystemView();
		paths = File.listRoots();
		for (File path : paths) {
			System.out.println("Drive Name : " + path);
			System.out.println("Dercription : " + fsv.getSystemTypeDescription(path));
		}
	}
	/*
	 * 
	 */
	public static boolean isFolder(String path) {
	        File f = new File(path);
	        return f.isDirectory();
	    }
	public static boolean isFile(String path) {
	        File f = new File(path);
	        return f.isFile();
	    }
	/*
	 * 
	 */
	public static String getParent(String path) {
		return Paths.get(path).getParent().toString();
	}
	//
	//
	//
	public static void main(String[] args) {
//		ArrayList<HashMap<String, String>> Result = new ArrayList<HashMap<String,String>>();
//		String path = "H:\\TEST";
//		//ListFolderAndSubFolder(Result, path, 0);
//		System.out.println(ListFolder(path));
//		System.out.println(ListFileAndFolder(path + "\\TEST_01"));
//		System.out.println(ListFile(path + "\\TEST_01\\TEST_01_a"));
		System.out.println(getParent("Administrator\\Desktop\\138\\Ay"));
	}
}
