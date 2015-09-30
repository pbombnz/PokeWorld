package game.avatar;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Avatar implements Serializable {
	private static final long serialVersionUID = 3217955654737927285L;
	
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

	public Avatar() {
		avatarName = null;
		faceleft = null;
		faceright = null;
		backleft = null;
		backright = null;
		normal = null;
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((avatarName == null) ? 0 : avatarName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Avatar other = (Avatar) obj;
		if (avatarName == null) {
			if (other.avatarName != null)
				return false;
		} else if (!avatarName.equals(other.avatarName))
			return false;
		return true;
	}
}
