package game.avatar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Avatar {
	private static File avatarPath = new File("./sprites/avatars/");
	
	public static List<Avatar> getAllAvatars()
	{
		List<Avatar> avatars;
		
		if(avatarPath.exists() && avatarPath.isDirectory()) {
			avatars = new ArrayList<Avatar>();
		} else {
			throw new RuntimeException("Game Files Corrupted. The Avatar folder in sprites does not exist or is not readable.");
		}

		File[] fList = avatarPath.listFiles();
		
	    for (File file : fList) {
	        if (file.isDirectory()) {
	        	Avatar avatar = new Avatar(file.getName(), 
	        	new ImageIcon(file.getAbsolutePath() + "/faceleft.png"),
	        	new ImageIcon(file.getAbsolutePath() + "/faceright.png"),
	        	new ImageIcon(file.getAbsolutePath() + "/backleft.png"),
	        	new ImageIcon(file.getAbsolutePath() + "/backright.png"),
	        	new ImageIcon(file.getAbsolutePath() + "/normal.png"));
	        	
	        	
	        	avatars.add(avatar);
	            //System.out.println(file.getAbsolutePath());
	            //System.out.println(file.getName());
	        	//System.out.println(file.getParentFile());
	        }
	    }
		return avatars;
	       
	}
	
	
	private final String avatarName;
	private final ImageIcon faceleft;
	private final ImageIcon faceright;
	private final ImageIcon backleft;
	private final ImageIcon backright;
	private final ImageIcon normal;

	public Avatar(String avatarName, ImageIcon faceleft, ImageIcon faceright,
			ImageIcon backleft, ImageIcon backright, ImageIcon normal) {
		super();
		this.avatarName = avatarName;
		this.faceleft = faceleft;
		this.faceright = faceright;
		this.backleft = backleft;
		this.backright = backright;
		this.normal = normal;
	}
	
	//public static void main(String[] args) {
	//	getAllAvatars();
	//}

	public String getName() {
		return avatarName;
	}

	public ImageIcon getFaceleft() {
		return faceleft;
	}


	public ImageIcon getFaceright() {
		return faceright;
	}


	public ImageIcon getBackleft() {
		return backleft;
	}

	public ImageIcon getBackright() {
		return backright;
	}

	public ImageIcon getNormal() {
		return normal;
	}
}
