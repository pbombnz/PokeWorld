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
	        	new ImageIcon(file.getAbsolutePath() + "/evolution1_faceleft.png"),
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
	
	// Intital Evolution Sprites
	private final ImageIcon evolution1_faceleft;
	private final ImageIcon evolution1_faceright;
	private final ImageIcon evolution1_backleft;
	private final ImageIcon evolution1_backright;
	private final ImageIcon normal;

	// Second Evolution Sprites
	private final ImageIcon evolution2_faceleft;
	private final ImageIcon evolution2_faceright;
	private final ImageIcon evolution2_backleft;
	private final ImageIcon evolution2_backright;

	// Third Evolution Sprites
	private final ImageIcon evolution3_faceleft;
	private final ImageIcon evolution3_faceright;
	private final ImageIcon evolution3_backleft;
	private final ImageIcon evolution3_backright;
	

	
	public Avatar(String avatarName, ImageIcon evolution1_faceleft,
			ImageIcon evolution1_faceright, ImageIcon evolution1_backleft,
			ImageIcon evolution1_backright, ImageIcon normal,
			ImageIcon evolution2_faceleft, ImageIcon evolution2_faceright,
			ImageIcon evolution2_backleft, ImageIcon evolution2_backright,
			ImageIcon evolution3_faceleft, ImageIcon evolution3_faceright,
			ImageIcon evolution3_backleft, ImageIcon evolution3_backright) {
		
		this.avatarName = avatarName;
		this.evolution1_faceleft = evolution1_faceleft;
		this.evolution1_faceright = evolution1_faceright;
		this.evolution1_backleft = evolution1_backleft;
		this.evolution1_backright = evolution1_backright;
		
		this.normal = normal;
		
		this.evolution2_faceleft = evolution2_faceleft;
		this.evolution2_faceright = evolution2_faceright;
		this.evolution2_backleft = evolution2_backleft;
		this.evolution2_backright = evolution2_backright;
		
		this.evolution3_faceleft = evolution3_faceleft;
		this.evolution3_faceright = evolution3_faceright;
		this.evolution3_backleft = evolution3_backleft;
		this.evolution3_backright = evolution3_backright;
	}

	/**
	 * No-args Constructor for Network Serialization (DO NOT USE)
	 */
	public Avatar() {
		
		this.avatarName = null;
		this.evolution1_faceleft = null;
		this.evolution1_faceright = null;
		this.evolution1_backleft = null;
		this.evolution1_backright = null;
		
		this.normal = null;
		
		this.evolution2_faceleft = null;
		this.evolution2_faceright = null;
		this.evolution2_backleft = null;
		this.evolution2_backright = null;
		
		this.evolution3_faceleft = null;
		this.evolution3_faceright = null;
		this.evolution3_backleft = null;
		this.evolution3_backright = null;
	}	
	
	public String getName() {
		return avatarName;
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
