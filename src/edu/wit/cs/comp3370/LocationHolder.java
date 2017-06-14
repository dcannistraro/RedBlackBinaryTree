package edu.wit.cs.comp3370;

/*
 * An abstract class to insert and retrieve some info about disk locations
 */
public abstract class LocationHolder {

	public static DiskLocation nil = new DiskLocation(-1, -1);
	protected DiskLocation root;

	public abstract DiskLocation find(DiskLocation d);	// returns the object or nil
	public abstract DiskLocation next(DiskLocation d);	// returns the next object or nil
	public abstract DiskLocation prev(DiskLocation d);	// returns the previous object or nil
	public abstract void insert(DiskLocation d);		// inserts the object
	public abstract int height();						// returns the largest tree height
	
}
