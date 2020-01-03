package homework5;

import java.io.IOException;

/*
 * File: Main.java
 * Date: Nov 19, 2019
 * Author: Gabriel Gallegos
 * Purpose: Main class that calls the main method of the worker class, from which the user is 
 * prompted multiple options, each one performing a different operation.
 */

public class Main {

	public static void main(String[] args) throws IOException {
		SelectOptions options = new SelectOptions();
		options.optionSelected();
	}
}
 