package ui;

import java.util.ArrayList;
import java.util.List;

/**
 *@author Wang Zhen
 */
public class PointArrayStorage {
	public int[] xPoint = new int[4];
	public int[] yPoint = new int[4];
	public List<PointArrayStorageLeft> leftList = new ArrayList<PointArrayStorageLeft>();
	public List<PointArrayStorageRight> rightlList = new ArrayList<PointArrayStorageRight>();
}
