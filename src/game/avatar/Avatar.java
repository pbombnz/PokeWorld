package game.avatar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * This class manages all the sprites of a particular avatar. Although
 * previously it did every sprite for a particular avatar, thanks to the 
 * continued addition of sprites of other team members, NOT all sprites
 * can be accessible here (mainly the GIFs).
 * 
 * This Class dynamically searches for the avatar therefore it requires all avatars 
 * to be in the "./sprites/avatars/". The name of sub-directories within the avatar 
 * folder are the avatars and the name of the avatar is collected from the folder name
 * Within an avatar sprite folder there are nested sub-directories indicating
 * the next "evolution" of a sprite.
 * 
 * @author Prashant Bhikhu
 *
 */
public class Avatar implements Serializable {
	private static final long serialVersionUID = 3217955654737927285L; // Generated Serial UID
	
	private static File avatarPath = new File("./sprites/avatars/"); // The file path that locates the avatar sprites

	// Although redunant due to 'evolution1_name', it is keep in the code for compatibility
	private final String avatarName;
	private final ImageIcon displayPic;
	private ArrayList<Evolution> evolutions = new ArrayList<Evolution>();


	public static List<Avatar> getAllAvatars() throws FileNotFoundException
	{
		List<Avatar> avatars;

		if(avatarPath.exists() && avatarPath.isDirectory()) {
			avatars = new ArrayList<Avatar>();
		} else {
			throw new FileNotFoundException("Sprite Avatar Folder Does not exist or cannot be found.");
		}

		File[] fList = avatarPath.listFiles();

		for (File file : fList) {
			if (file.isDirectory()) {	
				File[] directories = null;
				
				//System.out.println(checkImagesExist(file.getAbsolutePath(), 1));
				if(!checkImagesExist(file.getAbsolutePath(), 1)) {
					throw new FileNotFoundException("Sprite Avatar Folder, in \""+file.getAbsolutePath()+"\"  are not Structured Correctly.");
				}
				
				
				String avatarName = file.getName();
				ImageIcon displayPic  = new ImageIcon(file.getAbsolutePath() + "/dp.png");				
				
				String name = file.getName();
				ImageIcon displayPicGIF  = new ImageIcon(file.getAbsolutePath() + "/dp.gif");
				ImageIcon faceLeft = new ImageIcon(file.getAbsolutePath() + "/faceleft.png");
				ImageIcon faceRight = new ImageIcon(file.getAbsolutePath() + "/faceright.png");
				ImageIcon backLeft = new ImageIcon(file.getAbsolutePath() + "/backleft.png");
				ImageIcon backRight = new ImageIcon(file.getAbsolutePath() + "/backright.png");
				ImageIcon attackGIF = new ImageIcon(file.getAbsolutePath() + "/attack.gif");
				ImageIcon dieGIF = new ImageIcon(file.getAbsolutePath() + "/die.gif");
				ImageIcon evolvingGIF = new ImageIcon(file.getAbsolutePath() + "/evolve.gif");

				
				Evolution firstEvolution = new Evolution(name, displayPicGIF, faceLeft, faceRight, backLeft, backRight, attackGIF, dieGIF, evolvingGIF);
				
				
				directories = new File(file.getAbsolutePath()).listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});
				
				if(directories.length != 1) {
					throw new FileNotFoundException("Second Evolution for \""+avatarName+"\" Sprite doesn't Exist");
				}
				
				if(!checkImagesExist(directories[0].getAbsolutePath(), 2)) {
					throw new FileNotFoundException("Sprite Avatar Folder, in \""+directories[0].getAbsolutePath()+"\"  are not Structured Correctly.");
				}

				name = directories[0].getName();
				displayPicGIF  = new ImageIcon(directories[0].getAbsolutePath() + "/dp.gif");
				faceLeft = new ImageIcon(directories[0].getAbsolutePath() + "/faceleft.png");
				faceRight = new ImageIcon(directories[0].getAbsolutePath() + "/faceright.png");
				backLeft = new ImageIcon(directories[0].getAbsolutePath() + "/backleft.png");
				backRight = new ImageIcon(directories[0].getAbsolutePath() + "/backright.png");
				attackGIF = new ImageIcon(directories[0].getAbsolutePath() + "/attack.gif");
				dieGIF = new ImageIcon(directories[0].getAbsolutePath() + "/die.gif");
				evolvingGIF = new ImageIcon(directories[0].getAbsolutePath() + "/evolve.gif");				
				
				Evolution secondEvolution = new Evolution(name, displayPicGIF, faceLeft, faceRight, backLeft, backRight, attackGIF, dieGIF, evolvingGIF);
						
				directories = new File(directories[0].getAbsolutePath()).listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});	 
				if(directories.length != 1) {
					throw new FileNotFoundException("Third Evolution for \""+avatarName+"\" Sprite doesn't Exist");
				}
				
				if(!checkImagesExist(directories[0].getAbsolutePath(), 3)) {
					throw new FileNotFoundException("Sprite Avatar Folder, in \""+directories[0].getAbsolutePath()+"\"  are not Structured Correctly.");
				}

				name = directories[0].getName();
				displayPicGIF  = new ImageIcon(directories[0].getAbsolutePath() + "/dp.gif");
				faceLeft = new ImageIcon(directories[0].getAbsolutePath() + "/faceleft.png");
				faceRight = new ImageIcon(directories[0].getAbsolutePath() + "/faceright.png");
				backLeft = new ImageIcon(directories[0].getAbsolutePath() + "/backleft.png");
				backRight = new ImageIcon(directories[0].getAbsolutePath() + "/backright.png");
				attackGIF = new ImageIcon(directories[0].getAbsolutePath() + "/attack.gif");
				dieGIF = new ImageIcon(directories[0].getAbsolutePath() + "/die.gif");				
				
				Evolution thirdEvolution = new Evolution(name, displayPicGIF, faceLeft, faceRight, backLeft, backRight, attackGIF, dieGIF);
				
				
				avatars.add(new Avatar(avatarName, displayPic, firstEvolution, secondEvolution, thirdEvolution));
			}
		}
		return avatars;

	}
	
	private static boolean checkImagesExist(String directory, int evolutionLevel) {
		if(evolutionLevel == 1) {
			return new File(directory + "/dp.png").exists() 
				   && new File(directory + "/dp.gif").exists() 
				   && new File(directory + "/faceleft.png").exists() 
				   && new File(directory + "/faceright.png").exists()
				   && new File(directory + "/backleft.png").exists()
				   && new File(directory + "/backright.png").exists()
				   && new File(directory + "/attack.gif").exists()
				   && new File(directory + "/die.gif").exists()
				   && new File(directory + "/evolve.gif").exists();
		}
		else if(evolutionLevel == 2) {
			return new File(directory + "/dp.gif").exists() &&
				   new File(directory + "/faceleft.png").exists() &&
				   new File(directory + "/faceright.png").exists() &&
				   new File(directory + "/backleft.png").exists() &&
				   new File(directory + "/backright.png").exists() && 
				   new File(directory + "/attack.gif").exists() &&
				   new File(directory + "/die.gif").exists() &&
				   new File(directory + "/evolve.gif").exists();
		} else if (evolutionLevel == 3) {
			return new File(directory + "/dp.gif").exists() &&
				   new File(directory + "/faceleft.png").exists() &&
				   new File(directory + "/faceright.png").exists() &&
				   new File(directory + "/backleft.png").exists() &&
				   new File(directory + "/backright.png").exists() && 
				   new File(directory + "/attack.gif").exists() &&
				   new File(directory + "/die.gif").exists();			
		} else {
			return false;
		}
	}
	
	public Avatar(String avatarName, ImageIcon displayPic,
			Evolution firstEvolution, Evolution secondEvolution, Evolution thirdEvolution) {
		this.avatarName = avatarName;
		this.displayPic = displayPic;
		
		this.evolutions.add(firstEvolution);
		this.evolutions.add(secondEvolution);
		this.evolutions.add(thirdEvolution);
	}

	/**
	 * No-args Constructor (Used for Kyro Serialization)
	 */
	public Avatar() {
		this.avatarName = null;
		this.displayPic = null;
	}	

	
	
	public String getName() {
		return avatarName;
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

	public Evolution getCurrentEvolution(int playerEvolutionLevel) {
		if(playerEvolutionLevel < 1 || playerEvolutionLevel > 3) {
			throw new IllegalArgumentException("The Evolution Level of a Player must be between 1-3.");
		}
		return evolutions.get(playerEvolutionLevel-1);
	}
	
	public Evolution getNextEvolution(int playerEvolutionLevel) {
		if(playerEvolutionLevel < 1 || playerEvolutionLevel > 3) {
			throw new IllegalArgumentException("The Evolution Level of a Player must be between 1-3.");
		}
		return evolutions.get(playerEvolutionLevel);
	}
	
	/**
	 * Main Method used for debugging. On Execution, if an IOException is produced
	 * it indicates that the file structure is wrong therefore you need to fix it
	 * before you run the game, otherwise if no error is produced, the file structure
	 * is fine.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Avatar.getAllAvatars();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public ImageIcon getDisplayPic() {
		return displayPic;
	}

}
