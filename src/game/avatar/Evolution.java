package game.avatar;

import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * This class is co-related with Avatar. An Evolution consists of all sprites and name
 * of that specific evolution for a particular character.
 * 
 * @author Prashant Bhikhu
 */
public class Evolution implements Serializable {
	private static final long serialVersionUID = -4465642051843622394L;
	
	// Basically the varibles name are clear enough to distinguish which image is used for what
	private final String name;
	private final ImageIcon displayPictureGIF;
	private final ImageIcon faceLeft;
	private final ImageIcon faceRight;
	private final ImageIcon backLeft;
	private final ImageIcon backRight;
	private final ImageIcon attackGIF;
	private final ImageIcon dieGIF;
	private final ImageIcon evolvingGIF;

	/**
	 * No-args Constructor (Needed for Kyro Serialization) 
	 * DO NOT USE DIRECTLY
	 */
	public Evolution() {
		this.name = null;
		this.displayPictureGIF = null;
		this.faceLeft = null;
		this.faceRight = null;
		this.backLeft = null;
		this.backRight = null;
		this.attackGIF = null;
		this.dieGIF = null;
		this.evolvingGIF = null;	
	}
	
	/**
	 *  This Constructor is used for when the Evolution is either to the first or second. 
	 *  This is because these Evolutions will always have a 'evolvingGIF'
	 * 
	 * @param name The Name of the Evolution
	 * @param displayPictureGIF The display picture used within the game to display what character you have
	 * @param faceLeft The image used when drawing the front left side of the character
	 * @param faceRight The image used when drawing the front right side of the character
	 * @param backLeft The image used when drawing the back left side of the character
	 * @param backRight The image used when drawing the back right side of the character
	 * @param attackGIF The GIF image used for when the player attacks
	 * @param dieGIF The GIF image used for when the the player dies
	 * @param evolvingGIF The GIF image used for when evolving occurs
	 */
	public Evolution(String name, ImageIcon displayPictureGIF,
			ImageIcon faceLeft, ImageIcon faceRight, ImageIcon backLeft,
			ImageIcon backRight, ImageIcon attackGIF, ImageIcon dieGIF, ImageIcon evolvingGIF) {
		super();
		this.name = name;
		this.displayPictureGIF = displayPictureGIF;
		this.faceLeft = faceLeft;
		this.faceRight = faceRight;
		this.backLeft = backLeft;
		this.backRight = backRight;
		this.attackGIF = attackGIF;
		this.dieGIF = dieGIF;
		this.evolvingGIF = evolvingGIF;
	}	

	/**
	 *  This Constructor is used when the Evolution is the third. This is because these
	 *  Evolutions will never have a 'evolvingGIF'
	 * 
	 * @param name The Name of the Evolution
	 * @param displayPictureGIF The display picture used within the game to display what character you have
	 * @param faceLeft The image used when drawing the front left side of the character
	 * @param faceRight The image used when drawing the front right side of the character
	 * @param backLeft The image used when drawing the back left side of the character
	 * @param backRight The image used when drawing the back right side of the character
	 * @param attackGIF The GIF image used for when the player attacks
	 * @param dieGIF The GIF image used for when the the player dies
	 */
	public Evolution(String name, ImageIcon displayPictureGIF,
			ImageIcon faceLeft, ImageIcon faceRight, ImageIcon backLeft,
			ImageIcon backRight, ImageIcon attackGIF, ImageIcon dieGIF) {
		this.name = name;
		this.displayPictureGIF = displayPictureGIF;
		this.faceLeft = faceLeft;
		this.faceRight = faceRight;
		this.backLeft = backLeft;
		this.backRight = backRight;
		this.attackGIF = attackGIF;
		this.dieGIF = dieGIF;
		this.evolvingGIF = null;
	}

	// ===============================
	// GETTERS
	// ===============================
	
	/**
	 * @return the name of evolution
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The display picture used within the game to display what character you have
	 */
	public ImageIcon getDisplayPictureGIF() {
		return displayPictureGIF;
	}
	
	/**
	 * @return The image used when drawing the front left side of the character
	 */
	public ImageIcon getFaceLeft() {
		return faceLeft;
	}

	/**
	 * @return The image used when drawing the front right side of the character
	 */
	public ImageIcon getFaceRight() {
		return faceRight;
	}

	/**
	 * @return The image used when drawing the back left side of the character
	 */
	public ImageIcon getBackLeft() {
		return backLeft;
	}

	/**
	 * @return The image used when drawing the back right side of the character
	 */
	public ImageIcon getBackRight() {
		return backRight;
	}

	/**
	 * @return The GIF image used for when the player attacks
	 */
	public ImageIcon getAttackGIF() {
		return attackGIF;
	}

	/**
	 * @return The GIF image used for when the the player dies
	 */
	public ImageIcon getDieGIF() {
		return dieGIF;
	}

	/**
	 * @return The GIF image used for when evolving occurs, returns null for the third evolution
	 */
	public ImageIcon getEvolvingGIF() {
		return evolvingGIF;
	}
}
