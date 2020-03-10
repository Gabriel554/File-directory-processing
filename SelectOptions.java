package homework5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * File: SelectOptions.java
 * Date: Nov 19, 2019
 * Author: Gabriel Gallegos
 * Purpose: This file contains all the operations and work for it's main file. This program
 * prompts the user with 8 options from which to choose from, that will then execute 8 different
 * operations, depending on the choice selected. The main functions of the program are: listing first
 * and all levels of a directory's content, deleting a file, displaying a file in hexadecimal view, and
 * encrypting and decrypting a file, using XOR with password.
 */

public class SelectOptions {
	
	private static boolean userEnteredDirectory=false;
	private static File directoryOfSelectedFile;
	private static int address=0;
	private static Scanner scan = new Scanner(System.in);
	
	
		// Simple method for increasing data offset (address) by 16, every time it's called.
		private int address() {
			address+=16;
			return address;
		}
	
	
		// Lists the content of all levels of the directory selected
		private void allLevelsDirectoryContent() {
			File[] content = directoryOfSelectedFile.listFiles();
			// Calling recursive method
			recursiveListAllFiles(content, 0, 0);
		}
		
		
		/*
		 * Uses pretty much the same code and logic as the encryption method. At the end of
		 * the method, in addition to the original file, the user will have two new files: an encrypted file
		 * of the original and a decrypted file of the encrypted file. 
		 */
		@SuppressWarnings("resource")
		private boolean decryptFile() throws FileNotFoundException, IOException {
			System.out.println("Enter the password used for encryption:");
			Scanner scanner = new Scanner(System.in);
			String password = scanner.nextLine();
			// Input only filename without its type, e.g spaghetti, and not spaghetti.txt
			System.out.println("Enter the filename to decrypt (from the selected directory):");
			String filenameToDecrypt = scanner.nextLine();
			System.out.println("Enter the type of the filename to decrypt (.txt, .pdf, .docx:");
			String fileType = scanner.nextLine();
			// Only filename and not it's type
			System.out.println("Enter a new name for the creation of the decryted file:");
			String decryptedFilename = scanner.nextLine();
			File decryptedFile = new File(directoryOfSelectedFile.getAbsoluteFile()+"\\"+decryptedFilename+fileType);
			File fileToDecrypt = new File(directoryOfSelectedFile.getAbsoluteFile()+"\\"+filenameToDecrypt+fileType);
			InputStream reader = null;
			var outputStream = new FileOutputStream(decryptedFile);
			
			try {
				reader = new FileInputStream(fileToDecrypt);
			} catch (FileNotFoundException e) {
				return false;
			}
			char temp, decryptedByte;
			int count=0;
			int data=0;
			if (fileToDecrypt.exists()) {
				while ((data = reader.read()) !=-1) {
					if (count == password.length()) {
						count=0;
					}
					temp = (char)data;
					decryptedByte =(char) ((char) (password.charAt(count))^(char)(temp));
					outputStream.write(decryptedByte);
					count++;
				}
				outputStream.close();
				System.out.println("A copy of the encrypted file was decrypted.");
				return true;
			} else {
				outputStream.close();
				return false;
			}
		}
		
	
		/*
		 * Deletes the filename input. Filename must include type, e.g. test.txt. 
		 * Returns false if filename input exists.
		 */
		private boolean deleteFile() {
			System.out.println("Enter a filename to delete:");
			scan = new Scanner(System.in);
			String filename = scan.nextLine();
			File fileToDelete = new File(directoryOfSelectedFile.getAbsoluteFile()+"\\" + filename);
			if (fileToDelete.delete()) {
				System.out.println(filename + " was deleted");
				return true;
			} else {
				return false;
			}
		}
		
		
		private void displayMenu() {
			System.out.println("\n\n\n\nSelect an option:"
					+ "\n0 - Exit"
					+ "\n1 - Select directory"
					+ "\n2 - List directory content (first level)"
					+ "\n3 - List directory content (all levels)"
					+ "\n4 - Delete file"
					+ "\n5 - Display file (hexadecimal view)"
					+ "\n6 - Encrypt file (XOR with password)"
					+ "\n7 - Decrypt file (XOR with password)");
		}
		
		
		private void endProgram() {
		System.out.println("The program ended.");
		System.exit(0);
	}
	
		
	/*
	 * This method prompts user to enter a password for the XOR encryption (the user will enter the same password
	 * in order to decrypt the encrypted file), the name of the file to encrypt (only the name),
	 * its type, and then to input a new filename (not type) for the encrypted file. At the end of the operation,
	 * there will be two files: the original file and the new encrypted file.
	 */
	@SuppressWarnings("resource")
	private boolean encryptFile() throws FileNotFoundException, IOException {
		System.out.println("Enter a password:");
		Scanner scanner = new Scanner(System.in);
		String password = scanner.nextLine();
		// Input only filename without its type, e.g spaghetti, and not spaghetti.txt
		System.out.println("Enter the filename to encrypt (from the selected directory):");
		String filename = scanner.nextLine();
		// This is so to ensure that the new file has the same type of the original
		System.out.println("Enter the type of the filename to encrypt (.txt, .pdf, .docx):");
		String fileType = scanner.nextLine();
		// Enter only the filename, not the type
		System.out.println("Enter a new name for the creation of the encrypted file:");
		String encryptedFilename = scanner.nextLine();
		File encryptedFile = new File(directoryOfSelectedFile.getAbsoluteFile()+"\\"+encryptedFilename+fileType);
		File fileToEncrypt = new File(directoryOfSelectedFile.getAbsoluteFile()+"\\"+filename+fileType);
		InputStream reader = null;
		var outputStream = new FileOutputStream(encryptedFile);
		try {
			reader = new FileInputStream(fileToEncrypt);
		} catch (FileNotFoundException e) {
			return false;
		}
		char temp, encryptedByte;
		int count=0;
		int data=0;
		
		if (fileToEncrypt.exists()) {
			while ((data = reader.read()) !=-1) {
				/*
				 * Set count to 0 so that when the password becomes the same
				 * length of the number of bytes to read. e.g.
				 * passwordpasswordpas
				 * content to encrypt.
				 */
				if (count == password.length()) {
					count=0;
				}
				
				temp = (char)data;
				encryptedByte =(char) ((char) (password.charAt(count))^(char)(temp));
				outputStream.write(encryptedByte);
				count++;
			}
			outputStream.close();
			System.out.println("A copy of the original file was encrypted.");
			return true;
			
		} else {
			outputStream.close();
			return false;
		}
	}
	
	
	/*
	 * Lists the content of the first level of the directory selected
	 * in option 1.
	 */
	private void firstLevelDirectoryContent() {
		String[] content = directoryOfSelectedFile.list();
		System.out.println("\nList directory content (first level):");
		for (int i=0; i<content.length; i++) {
			System.out.println(content[i]);
		}
	}
	
	
	// Returns false if no file was found
	private boolean hexadecimalView() throws IOException {
		System.out.println("Enter a filename (from the selected directory):");
		scan = new Scanner(System.in);
		String filename = scan.nextLine();
		File hexViewOfFile = new File(directoryOfSelectedFile.getAbsoluteFile()+"\\"+filename);
		
		if (hexViewOfFile.exists()) {
			InputStream reader = new FileInputStream(hexViewOfFile);
			StringBuilder dataOffset = new StringBuilder();
			StringBuilder lineOfHexData = new StringBuilder();
			StringBuilder hexData = new StringBuilder();
			int i=0;
			int count=0;
			int data=0;
			while ((data = reader.read()) != -1) {
				lineOfHexData.append(String.format("%02x ", data));
				// Enter a new line when count reaches 15
				if (count ==15) {
					// Puts data offset 00 for the first line of hex representation. Works just once.
					if (i==0) {
						dataOffset.append(String.format("%02x ", 0));
						hexData.append(dataOffset).append("\t").append(lineOfHexData).append("\n");
						// Delete stringbuilders' content
						dataOffset.setLength(0);
						lineOfHexData.setLength(0);
						i++;
						count=0;
					} else {
						// Appends data offset for each new line, before the hex representation
						dataOffset.append(String.format("%02x ", address()));
						hexData.append(dataOffset).append("\t").append(lineOfHexData).append("\n");
						dataOffset.setLength(0);
						lineOfHexData.setLength(0);
						count=0;
					}
				} else {
					count++;
				}
			}
			// If they are still bytes to read, that are less than 16, convert them to hex.
			if(count !=0){
				dataOffset.append(String.format("%02x ", address()));
			    for(; count<16; count++) {
			    	lineOfHexData.append("   ");
			    }
			    hexData.append(dataOffset).append("\t").append(lineOfHexData + "\n");
		    }
			System.out.println("****************** HEXADECIMAL VIEW *******************");
	        System.out.println(hexData.toString());
	        reader.close();
	        return true;
		} else {
			return false;
		}
	}
	
	
	private void noDirectoryMessage() {
		JOptionPane.showMessageDialog(null, "You must first select a directory.", "Error message", 0);
	}
	
	
	private void noFileInDirectoryMessage() {
		JOptionPane.showMessageDialog(null, "The current directory does not contain the file specified.", "Error message", 0);
	}
	
	
	/*
	 * Calls method depending on user input. If no directory was selected,
	 * an error message is displayed. Each switch option calls a method,
	 * which are pretty self explanatory, e.g. encryptFile() encrypts a file.
	 */
	void optionSelected() throws IOException {
		while (true) {
			displayMenu();
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			try {
				int optionChosen = input.nextInt();
				switch (optionChosen) {
				
					case 0:
						endProgram();
						
					case 1:
						if (!selectDirectory()) {
							System.out.println("Enter a valid directory");
							break;
						}
						break;
					
					case 2:
						if (userEnteredDirectory == true) {
							firstLevelDirectoryContent();
							break;
						} else {
							noDirectoryMessage();
							break;
						}
						
					case 3:
						if (userEnteredDirectory == true) {
							allLevelsDirectoryContent();
							break;
						} else {
							noDirectoryMessage();
							break;
						}
						
					case 4:
						if (userEnteredDirectory == true) {
							if (deleteFile()) {
								break;
							} else {
								noFileInDirectoryMessage();
								break;
							}
						} else {
							noDirectoryMessage();
							break;
						}
						
					case 5:
						if (userEnteredDirectory == true) {
							if (hexadecimalView()) {
								break;
							} else {
								noFileInDirectoryMessage();
								break;
							}
						} else {
							noDirectoryMessage();
							break;
						}
						
					case 6:
						if (userEnteredDirectory == true) {
							if (encryptFile()) {
								break;
							} else {
								noFileInDirectoryMessage();
								break;
							}
						} else {
							noDirectoryMessage();
							break;
						}
						
					case 7:
						if (userEnteredDirectory == true) {
							if (decryptFile()) {
								break;
							} else {
								noFileInDirectoryMessage();
								break;
							}
						} else {
							noDirectoryMessage();
							break;
						}
						
					default:
						System.out.println("Please enter a valid option.");
				}
			}catch (InputMismatchException e) {
				JOptionPane.showMessageDialog(null, "Enter a valid option", "Error", JOptionPane.ERROR_MESSAGE);
			}	
		}
	}
	
	
	/*
	 * Recursive function that displays all content in the selected directory. If
	 * content is a directory and not a file, it displays all the files inside that
	 * folder and so on.
	 */
	private void recursiveListAllFiles(File[] content, int i, int subDirectoryLevel) {
		if (i == content.length) {
			return;
		}
		// Inserts a tab for each child of a parent file, for a cleaner output
		for (int j=0; j<subDirectoryLevel; j++) {
			System.out.print("\t");
		}
		// If content is a file and not a directory
		if(content[i].isFile()) {
			System.out.println(content[i].getName());
		} else {
			// If content is a folder or directory (parent), call recursive method to
			// display all files in the folder.
			System.out.println(content[i].getName()+ " <== Parent");
			recursiveListAllFiles(content[i].listFiles(), 0, subDirectoryLevel+1);
		}
		// Do this for all files
		recursiveListAllFiles(content, ++i, subDirectoryLevel);
	}
	
	
	/*
	 * User is prompted to enter a valid directory. If directory is not valid,
	 * returns false to the method that called this method which then displays 
	 * an error message.
	 */
	private boolean selectDirectory() {
		System.out.println("Enter a directory [absolute] name:");
		scan = new Scanner(System.in);
		String directoryStr = scan.nextLine();
		Path absolutePath = Paths.get(directoryStr);
		if (Files.exists(absolutePath)) {
			directoryOfSelectedFile = new File(directoryStr);
			System.out.println("Succesfully entered a valid directory.");
			// userEnteredDirectory boolean is used in the whole program to 
			// ensure that before user selects any option, the user must enter a valid directory.
			userEnteredDirectory=true;
			return true;
		} else {
			
			userEnteredDirectory=false;
			return false;
		}
	}
}
