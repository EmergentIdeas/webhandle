package com.emergentideas.webhandle.files;


public interface DirectoryManipulator {

	/**
	 * Creates a new directory. Throws a runtime exception the directory can not be created.
	 * @param path The path of the directory to create
	 */
	public void makeDirectory(String path);
	
	/**
	 * Deletes a directory and possibly its contents.
	 * @param path The path to the directory to delete
	 * @param failIfNotEmpty If true, this method will not try to delete any sub directories and files if they exist
	 * and the delete operation will fail
	 */
	public void removeDirectory(String path, boolean failIfNotEmpty);
}
