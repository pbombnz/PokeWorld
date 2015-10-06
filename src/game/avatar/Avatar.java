package game.avatar;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Avatar implements Serializable {
	private static final long serialVersionUID = 3217955654737927285L;
	
	private static File avatarPath = new File("./sprites/avatars/");
	
	public static List<Avatar> getAllAvatars() throws IOException
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
	        	String avatarName = file.getName();
	        	String evoluton1_name = file.getName();
	        	ImageIcon evolution1_faceleft = new ImageIcon(file.getAbsolutePath() + "/faceleft.png");
	        	ImageIcon evolution1_faceright = new ImageIcon(file.getAbsolutePath() + "/faceright.png");
    			ImageIcon evolution1_backleft = new ImageIcon(file.getAbsolutePath() + "/backleft.png");
				ImageIcon evolution1_backright = new ImageIcon(file.getAbsolutePath() + "/backright.png");
				ImageIcon normal = new ImageIcon(file.getAbsolutePath() + "/normal.png");
	        	
				File[] directories = new File(file.getAbsolutePath()).listFiles(new FilenameFilter() {
				  @Override
				  public boolean accept(File current, String name) {
				    return new File(current, name).isDirectory();
				  }
				});
				
				if(directories.length != 1) {
	        		throw new IOException("Second Evolution for \""+avatarName+"\" Sprite doesn't Exist");
	        	}
	        	
				String evolution2_name = directories[0].getName();
	        	ImageIcon evolution2_faceleft = new ImageIcon(directories[0].getAbsolutePath() + "/faceleft.png");
	        	ImageIcon evolution2_faceright = new ImageIcon(directories[0].getAbsolutePath() + "/faceright.png");
    			ImageIcon evolution2_backleft = new ImageIcon(directories[0].getAbsolutePath() + "/backleft.png");
				ImageIcon evolution2_backright = new ImageIcon(directories[0].getAbsolutePath() + "/backright.png");

				//directories = new File(directories[0].getAbsolutePath()).listFiles(File::isDirectory);
				directories = new File(directories[0].getAbsolutePath()).listFiles(new FilenameFilter() {
					  @Override
					  public boolean accept(File current, String name) {
					    return new File(current, name).isDirectory();
					  }
					});	 
				if(directories.length != 1) {
	        		throw new IOException("Third Evolution for \""+avatarName+"\" Sprite doesn't Exist");
	        	}

	        	String evolution3_name = directories[0].getName();
	        	ImageIcon evolution3_faceleft = new ImageIcon(directories[0].getAbsolutePath() + "/faceleft.png");
	        	ImageIcon evolution3_faceright = new ImageIcon(directories[0].getAbsolutePath() + "/faceright.png");
    			ImageIcon evolution3_backleft = new ImageIcon(directories[0].getAbsolutePath() + "/backleft.png");
				ImageIcon evolution3_backright = new ImageIcon(directories[0].getAbsolutePath() + "/backright.png");

				Avatar avatar = new Avatar(avatarName, evolution1_faceleft,
						 evolution1_faceright, evolution1_backleft,
						 evolution1_backright, normal,
						 evolution2_name, evolution2_faceleft, evolution2_faceright,
						 evolution2_backleft, evolution2_backright,
						 evolution3_name, evolution3_faceleft, evolution3_faceright,
						 evolution3_backleft, evolution3_backright);		
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
	private final String evolution1_name;
	private final ImageIcon evolution1_faceleft;
	private final ImageIcon evolution1_faceright;
	private final ImageIcon evolution1_backleft;
	private final ImageIcon evolution1_backright;
	private final ImageIcon normal;

	// Second Evolution Sprites
	private final String evolution2_name;
	private final ImageIcon evolution2_faceleft;
	private final ImageIcon evolution2_faceright;
	private final ImageIcon evolution2_backleft;
	private final ImageIcon evolution2_backright;

	// Third Evolution Sprites
	private final String evolution3_name;
	private final ImageIcon evolution3_faceleft;
	private final ImageIcon evolution3_faceright;
	private final ImageIcon evolution3_backleft;
	private final ImageIcon evolution3_backright;
	

	public Avatar(String avatarName, ImageIcon evolution1_faceleft,
			ImageIcon evolution1_faceright, ImageIcon evolution1_backleft,
			ImageIcon evolution1_backright, ImageIcon normal, 
			String evolution2_name,
			ImageIcon evolution2_faceleft, ImageIcon evolution2_faceright,
			ImageIcon evolution2_backleft, ImageIcon evolution2_backright,
			String evolution3_name,
			ImageIcon evolution3_faceleft, ImageIcon evolution3_faceright,
			ImageIcon evolution3_backleft, ImageIcon evolution3_backright) {
		
		this.avatarName = avatarName;
		this.evolution1_name = avatarName;
		this.evolution1_faceleft = evolution1_faceleft;
		this.evolution1_faceright = evolution1_faceright;
		this.evolution1_backleft = evolution1_backleft;
		this.evolution1_backright = evolution1_backright;
		
		this.normal = normal;
		
		this.evolution2_name = evolution2_name;
		this.evolution2_faceleft = evolution2_faceleft;
		this.evolution2_faceright = evolution2_faceright;
		this.evolution2_backleft = evolution2_backleft;
		this.evolution2_backright = evolution2_backright;
		
		this.evolution3_name = evolution3_name;
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
		this.evolution1_name = null;
		this.evolution1_faceleft = null;
		this.evolution1_faceright = null;
		this.evolution1_backleft = null;
		this.evolution1_backright = null;
		
		this.normal = null;
		
		this.evolution2_name = null;
		this.evolution2_faceleft = null;
		this.evolution2_faceright = null;
		this.evolution2_backleft = null;
		this.evolution2_backright = null;
		
		this.evolution3_name = null;
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

	public String getAvatarName() {
		return avatarName;
	}

	public ImageIcon getEvolution1_faceleft() {
		return evolution1_faceleft;
	}

	public ImageIcon getEvolution1_faceright() {
		return evolution1_faceright;
	}

	public ImageIcon getEvolution1_backleft() {
		return evolution1_backleft;
	}

	public ImageIcon getEvolution1_backright() {
		return evolution1_backright;
	}

	public ImageIcon getEvolution2_faceleft() {
		return evolution2_faceleft;
	}

	public ImageIcon getEvolution2_faceright() {
		return evolution2_faceright;
	}

	public ImageIcon getEvolution2_backleft() {
		return evolution2_backleft;
	}

	public ImageIcon getEvolution2_backright() {
		return evolution2_backright;
	}

	public ImageIcon getEvolution3_faceleft() {
		return evolution3_faceleft;
	}

	public ImageIcon getEvolution3_faceright() {
		return evolution3_faceright;
	}

	public ImageIcon getEvolution3_backleft() {
		return evolution3_backleft;
	}

	public ImageIcon getEvolution3_backright() {
		return evolution3_backright;
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
	
	public static void main(String[] args) {
		try {
			Avatar.getAllAvatars();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
