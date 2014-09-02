package com.Lightwave;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
 * Accepts: polling interval between polls, start polling, stop polling, length of time to poll until
 * 
 */

public class ReceiveUDP extends Thread {
	
	private static int threadCount = 0;
	private FileLogger m_logger;
	private static final int LightwavePortOut = 9761; // Port out from Lightwave Wifi hub
	public static DatagramSocket receiveSocket; // For UDP receiving
	public static boolean methodLocked = false;
	
	/*
	 * Constructor defaults to logging every 30 seconds
	 */
	public ReceiveUDP() {
		    super("" + ++threadCount); // Store the thread name
		    initialiseSockets();
		    start();
		  }
	
	
	/*
	 * Constructor defaults to logging every 30 seconds
	 */
	public ReceiveUDP(FileLogger logger) {
		    super("" + ++threadCount); // Store the thread name
			m_logger = logger;
		    initialiseSockets();
		    start();
		  }
		
	  public void run() {
		    while(true) {
		    	receiveUDP();
		    }
		  }
	
	  
	  // Initialise receive sockets for UDP
	  public static void initialiseSockets(){
	    	try{
	    		receiveSocket = new DatagramSocket(LightwavePortOut);	
	    	} catch (IOException e){
	    		e.printStackTrace();
	    	}
	    	
	    }
	    
		
		//Network sockets to deal with receiving UDP responses from LightwaveRF box.  UDP response is specific to LightwaveRF
		public String receiveUDP(){
			
			String receivedMessage = "";

			try {
				    byte[] receiveData = new byte[1024];
				    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				    receiveSocket.receive(receivePacket);
				    receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
					//System.out.println("Received UDP Message: " + receivedMessage); // Command line output to see UDP command
				    if(m_logger != null )m_logger.logData("RECEIVED," + receivedMessage);
				    			    	    
			} 
			catch (IOException e) {
				e.printStackTrace(); // Display if something went wrong
			}
				
		    return receivedMessage;
		    
		}	

}
