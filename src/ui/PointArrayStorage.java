package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * storages are for  store the positions to storage and load information from storages 
 * This class is used to print object in 1st person view.
 * (save positions and read and print object is because the object need to be printed reverse)
 * @author Wang Zhen
 */
public class PointArrayStorage {
	public int[] xPoint = new int[4];
	public int[] yPoint = new int[4];
	public List<PointArrayStorageLeft> leftList = new ArrayList<PointArrayStorageLeft>();
	public List<PointArrayStorageRight> rightlList = new ArrayList<PointArrayStorageRight>();
}
