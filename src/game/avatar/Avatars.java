package game.avatar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Avatars {
	private static File avatarPath = new File("./sprites/avatars/");
	
	public static Map<String, File> getAllAvatars()
	{
		HashMap<String, File> avatars;
		
		if(avatarPath.exists() && avatarPath.isDirectory()) {
			avatars = new HashMap<String, File>();
		} else {
			throw new RuntimeException("Game Files Corrupted. The Avatar folder in sprites does not exist or is not readable.");
		}

		File[] fList = avatarPath.listFiles();
		
	    for (File file : fList) {
	        if (file.isDirectory()) {
	        	avatars.put(file.getName(), file);
	            //System.out.println(file.getAbsolutePath());
	            //System.out.println(file.getName());
	        }
	    }
		return avatars;
	       
	}
}
