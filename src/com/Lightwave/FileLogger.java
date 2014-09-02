/*
 * Basic logging to text file.  Operation is open, log, close.
 */

package com.Lightwave;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

public class FileLogger extends Thread{
	
	String m_filename = "Lightwave_Default_Logger_File.csv"; //Default filename if none given
	
	private static PrintStream out; 

	/*
	 * Constructor
	 */
	public FileLogger(String filename) {
			if (filename == "") filename=m_filename; // Assign default filename if none given
			else m_filename = filename;
			this.openFile(m_filename);
		    start();
		  }
	
	//Open File to output to filesystem.
	private void openFile(String filename){
		
		String m_filename = filename;
		  try {
		      		out = new PrintStream(new FileOutputStream(m_filename));
		      		System.out.println("---LOG: Logging file opened.");
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    }
	}
	
	//Close file that has output
	public void closeFile(){
		  try {
		      		out.close();
		      		System.out.println("---LOG: Logging file closed.");

		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	
	/* Take text entry and log it to an open file.
		String logEntry - any text to be logged.  Preferably CSV.
	*/
	
	public void logData(String logEntry){
	   try {
		   	
		    Calendar c = Calendar.getInstance();
		    String log = c.getTime() + "," + logEntry;
		    out.println(log);
	      	System.out.println("---LOG: Logging entry to file: " + c.getTime() + "," + logEntry);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
	
}