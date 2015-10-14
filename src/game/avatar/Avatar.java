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

	private final String avatarName; // Although redundant due to 'evolution1_name', it is keep in the code for compatibility
	private final ImageIcon displayPic; // The picture used when a player chooses a character
	private ArrayList<Evolution> evolutions = new ArrayList<Evolution>(); // The list of evolutions for a particualr avatar

	/**
	 * Gets All Avatars dynamically from the Avatar resources folder, and parses images into
	 * categories of evolutions for easier use.
	 * 
	 * @return A list of avatars retrieved from the file.
	 * @throws FileNotFoundException Thrown if the files for a particular evolution are not found.
	 */
	public static List<Avatar> getAllAvatars() throws FileNotFoundException
	{
		// Initialize the avatars list for returning
		List<Avatar> avatars;

		// Checks if the Avatar Path even exist
		if(avatarPath.exists() && avatarPath.isDirectory()) {
			avatars = new ArrayList<Avatar>();
		} else {
			throw new FileNotFoundException("Sprite Avatar Folder Does not exist or cannot be found.");
		}

		// Get all Avatar directories
		File[] fList = avatarPath.listFiles();

		// Iterate through all characters and create the evolutions for the specific avatar
		for (File file : fList) {
			// Only enter a file if it is a directory
			if (file.isDirectory()) {	
				// Holds the sub-directories of the characters
				File[] directories = null;
				
				// Firstly check if all first evolution images of character exist
				if(!checkImagesExist(file.getAbsolutePath(), 1)) {
					throw new FileNotFoundException("Sprite Avatar Folder, in \""+file.getAbsolutePath()+"\"  are not Structured Correctly.");
				}
				
				// Assuming they exist from the above boolean statement, then retrieve all images.
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

				// Pack all images and name variables into an Evolution object
				Evolution firstEvolution = new Evolution(name, displayPicGIF, faceLeft, faceRight, backLeft, backRight, attackGIF, dieGIF, evolvingGIF);
				
				// Looking for the next directory (next evolution) as a sub directory
				directories = new File(file.getAbsolutePath()).listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});
				
				// Check if only ONE directory is a subdirectory of the current directory (which is assumed to be the next evolution)
				if(directories.length != 1) {
					// If the folder has no sub-directories, then we cannot continue hence through an error OR if there
					// is more than one sub-directory then the program will not know which one to choose so it throws an error.
					throw new FileNotFoundException("Second Evolution for \""+avatarName+"\" Sprite doesn't Exist");
				}
				
				// Firstly check if all first evolution images of character exist
				if(!checkImagesExist(directories[0].getAbsolutePath(), 2)) {
					throw new FileNotFoundException("Sprite Avatar Folder, in \""+directories[0].getAbsolutePath()+"\"  are not Structured Correctly.");
				}

				// Assuming they exist from the above boolean statement, then retrieve all images.
				name = directories[0].getName();
				displayPicGIF  = new ImageIcon(directories[0].getAbsolutePath() + "/dp.gif");
				faceLeft = new ImageIcon(directories[0].getAbsolutePath() + "/faceleft.png");
				faceRight = new ImageIcon(directories[0].getAbsolutePath() + "/faceright.png");
				backLeft = new ImageIcon(directories[0].getAbsolutePath() + "/backleft.png");
				backRight = new ImageIcon(directories[0].getAbsolutePath() + "/backright.png");
				attackGIF = new ImageIcon(directories[0].getAbsolutePath() + "/attack.gif");
				dieGIF = new ImageIcon(directories[0].getAbsolutePath() + "/die.gif");
				evolvingGIF = new ImageIcon(directories[0].getAbsolutePath() + "/evolve.gif");				
				
				// Pack all images and name variables into an Evolution object
				Evolution secondEvolution = new Evolution(name, displayPicGIF, faceLeft, faceRight, backLeft, backRight, attackGIF, dieGIF, evolvingGIF);
						
				// Looking for the next directory (next evolution) as a sub directory
				directories = new File(directories[0].getAbsolutePath()).listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});	 

				// Check if only ONE directory is a subdirectory of the current directory (which is assumed to be the next evolution)
				if(directories.length != 1) {
					// If the folder has no sub-directories, then we cannot continue hence through an error OR if there
					// is more than one sub-directory then the program will not know which one to choose so it throws an error.
					throw new FileNotFoundException("Third Evolution for \""+avatarName+"\" Sprite doesn't Exist");
				}
				
				// Firstly check if all third evolution images of character exist
				if(!checkImagesExist(directories[0].getAbsolutePath(), 3)) {
					throw new FileNotFoundException("Sprite Avatar Folder, in \""+directories[0].getAbsolutePath()+"\"  are not Structured Correctly.");
				}

				// Assuming they exist from the above boolean statement, then retrieve all images.
				name = directories[0].getName();
				displayPicGIF  = new ImageIcon(directories[0].getAbsolutePath() + "/dp.gif");
				faceLeft = new ImageIcon(directories[0].getAbsolutePath() + "/faceleft.png");
				faceRight = new ImageIcon(directories[0].getAbsolutePath() + "/faceright.png");
				backLeft = new ImageIcon(directories[0].getAbsolutePath() + "/backleft.png");
				backRight = new ImageIcon(directories[0].getAbsolutePath() + "/backright.png");
				attackGIF = new ImageIcon(directories[0].getAbsolutePath() + "/attack.gif");
				dieGIF = new ImageIcon(directories[0].getAbsolutePath() + "/die.gif");				
				
				// Pack all images and name variables into an Evolution object
				Evolution thirdEvolution = new Evolution(name, displayPicGIF, faceLeft, faceRight, backLeft, backRight, attackGIF, dieGIF);
				
				// Construct an Avatar from the evolutions, name and display picture add append it to the list of Avatars
				avatars.add(new Avatar(avatarName, displayPic, firstEvolution, secondEvolution, thirdEvolution));
			}
		}
		
		// Finally, after all dynamically created Avatars have been created, we return them
		return avatars;
	}
	
	/**
	 * Checks if the correct images exist within the specified directory for that evolution level.
	 * The reason why evolutionLevel needs to be specified is because the different evolutions
	 * have different sprite requirements from each other.
	 * 
	 * @param directory The Directory which the specified evolution sprites are located in.
	 * @param evolutionLevel The evolution level of the directory
	 * @return
	 */
	private static boolean checkImagesExist(String directory, int evolutionLevel) {
		// Check the certain image files exist depending in the evolution level 
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
			return false; // DEAD CODE - Should never get here (as there are always only 3 evolutions)
		}
	}
	
	/**
	 * Creates an Avatar object. Although not used publicly, it is used by the static method
	 * to get all the Avatar images in a structured way.
	 * 
	 * @param avatarName Name of the Avatar
	 * @param displayPic Display picture of Avatar
	 * @param firstEvolution The first evolution of Avatar
	 * @param secondEvolution The Second evolution of Avatar
	 * @param thirdEvolution The Second evolution of Avatar
	 */
	private Avatar(String avatarName, ImageIcon displayPic,
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

	/**
	 * @return The name of the Avatar
	 */
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

	/**
	 * @param playerEvolutionLevel, The current evolution level
	 * @return
	 */
	public Evolution getCurrentEvolution(int playerEvolutionLevel) {
		if(playerEvolutionLevel < 1 || playerEvolutionLevel > 3) {
			throw new IllegalArgumentException("The Evolution Level of a Player must be between 1-3.");
		}
		return evolutions.get(playerEvolutionLevel-1);
	}
	
	/**
	 * @param playerEvolutionLevel, The current evolution level
	 * @return Evolution, the next evolution
	 */
	public Evolution getNextEvolution(int playerEvolutionLevel) {
		if(playerEvolutionLevel < 1 || playerEvolutionLevel >= 3) {
			throw new IllegalArgumentException("The Evolution Level of a Player must be between 1-3.");
		}
		return evolutions.get(playerEvolutionLevel);
	}
	
	/**
	 * @return the character's display picture
	 */
	public ImageIcon getDisplayPic() {
		return displayPic;
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
}
