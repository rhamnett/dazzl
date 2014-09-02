//Java Version - Eddie Pratt - eddie_pratt AT hotmail.com
package com.Lightwave;

/*
 * Basic API for controlling LightwaveRF Wifi box using UDP commands sent to port 9760 broadcast on 255.255.255.255
 * Also receive commands on port 9761 as a response from the LWRF box
 * Separate thread for logging UDP commands
 * Separate thread for receiving UDP responses from box, and buffering and sending UDP commands to box
 * Separate energy monitoring thread for periodically polling the box for electricity usage through wireless energy clamp
 */
public class LightwaveAPI {
	
	public static final int STOP = 0, stop = 0; //Flags for relay stop
	public static final int OPEN = 1, open = 1; //Flags for relay open
	public static final int CLOSE = 2, close = 2; // Flags for relay close
	public static final int ON = 1, on = 1, LOCK = 1, lock = 1; //Flags on
	public static final int OFF = 0, off = 0, UNLOCK = 0, unlock = 0; //Flags off
	public static final int MaxRooms = 8; // Max number of rooms
	static ReceiveUDP server_in; // Server for receiving UDP from the wifi box
	static SendUDP server_out; // Server for buffering and sending UDP from direct commands or polling from meter poller
	static FileLogger logger; // Logging server
	//static Console console_input;//  Console server
	
	public LightwaveAPI() {
		
		logger = new FileLogger("LightwaveRF_Test.csv"); //Separate logging thread, log UDP traffic to CSV file
		//server_in = new ReceiveUDP(logger); //Separate UDP receiving server thread
		server_out = new SendUDP(logger);   //Separate 
		
	}
	
	// Main Entry - Example Commands calling API 
	public static void main(String[] args) {	
		
		//logger = new FileLogger("LightwaveRF_Test.csv"); //Separate logging thread, log UDP traffic to CSV file
		//server_in = new ReceiveUDP(logger); //Separate UDP receiving server thread
		server_out = new SendUDP(logger);   //Separate buffered UDP sending server thread
//		console_input = new Console(server_out); //Simple console server to accept raw UDP commands and send to LWRF wifi box
		
		//PollEnergyMeter m_meter = new PollEnergyMeter(server_out, 5000); //Separate energy meter polling thread, delay between polls
		//m_meter.setPollingPeriod(120000); //Alter polling period during operation (milliseconds)

		/*
		 * Example commands
		 * 
		 */
		
		
		//sendRoomOff(2); // Turn everything off assigned in Room 1
		/*littlePause (3500); // Wait to see the result!
		sendRoomMood (1,3); // Activate predefined Mood 3 in Room 1
		littlePause (3500); // Wait to see the result!
		
		sendDeviceDim (1, 3, 25); // Dim Device 3 in Room 1 to 25% dim level
		littlePause (3500); // Wait to see the result!
		*/
		sendDeviceDim (3, 1, 20); // Dim Device 2 in Room 1 to 100% dim level
		/*littlePause (3500); // Wait to see the result!
		*/
		//sendDeviceOnOff (2,1,OFF); // Turn off Device 3 in Room 1 
		/*littlePause (3500); // Wait to see the result!
		
		sendOpenCloseStop(1,6,OPEN); //For for inline relays. Open connects common and circuit 1 connection.
		littlePause (3500); // Wait to see the result!
		
		sendOpenCloseStop(1,6,CLOSE); //For inline relays. Close connects common and circuit 2 connection.
		littlePause (3500); // Wait to see the result!
		
		sendOpenCloseStop(1,6,STOP); //For inline relays. Stop disconnects both common and circuit 1 and circuit 2.
		littlePause (3500); // Wait to see the result!
		
		sendHeatOnOff(1,ON); // Turn On radiator TRV (radiator valve) in Room 1
		littlePause (3500); // Wait to see the result!
		
		sendAllRoomsOff(); // Turn everything off assigned in All Rooms (1 through to MaxRooms)
		*/
	}
	
	/* Force registration with the LWRF wifi box - use only once, before the other commands, to
	 * force your computer to try to register with the LWRF box. After calling, look at LWRF box display 
	 * and it will ask you to approve the device connecting. You'll need to click a button on the box.
	 * Once done you just use other commands as documented. 
	 */
	
	public static void forceRegistration(){
        String text = ",!R1Fa"; //693 hasn't any relevance - just arbitrary instead of 000
        server_out.sendUDP(text);
	}
	
	// Send Raw UDP Command
	public static void sendRawUDP(String text){
        text = text + "\0";
        server_out.sendUDP(text);
	}

	// Switches off all devices in Room
	public static void sendRoomOff(int Room){
        String text = ",!R" + Room + "Fa\0";
        server_out.sendUDP(text);
	}

	// Switches off all devices in all Rooms
	public static void sendAllRoomsOff(){
		for (int i = 1; i<=MaxRooms; ++i){
			sendRoomOff(i);
			}
		}
	
	// Sends Mood change request for Room 
    public static void sendRoomMood(int Room, int Mood)
    {
        String text = ",!R"+ Room + "FmP" + Mood + "|\0";
        server_out.sendUDP(text);
    }
	
    // Send change request for Percent dim level to Device in Room
    public static void sendDeviceDim(int Room, int Device, int Percent)
    { 
        String pstr;
        pstr = "" + (int)(Math.floor(0.01* Percent * 32));
        String text = ",!R" + Room + "D" + Device + "FdP" + pstr + "|\0";
        server_out.sendUDP(text);
        
    }
    
    // Send on/off State to Device in Room
    public static void sendDeviceOnOff(int Room, int Device, int State)
    {
        String statestr;
        if(State==ON) statestr = "1"; 
        		else statestr = "0";
        String text = ",!R" + Room + "D" + Device + "F" + statestr + "|\0";
        server_out.sendUDP(text);
    }
   
	// Send Lock/Unlock to a switching Device in Room
	public static void sendDeviceLockUnlock(int Room, int Device, int State){
        String statestr;
        if(State==lock) statestr = "l"; //lock 
        		else statestr = "u"; //unlock
        String text = ",!R" + Room + "D" + Device + "F" + statestr + "|\0";
        server_out.sendUDP(text);	
		}
		
	// Send Open/Close/Stop to a relay Device in Room
	public static void sendOpenCloseStop(int Room, int Device, int State){
        String statestr;
   
        switch (State) {
	        case STOP    :      statestr =  "^"; break; // Breaks circuit between common and both inputs.
	        case OPEN   :       statestr =  ">"; break; // Connects circuit between common and input 1.
	        case CLOSE   :      statestr =  "<"; break; // Connects circuit between common and input 2.
	        default		 :      statestr =  "^"; break;
	    }
        
        String text = ",!R" + Room + "D" + Device + "F" + statestr + "\0|";
        server_out.sendUDP(text);	
		}
    
    // Send on/off State to radiator TRV heating valve in Room
    public static void sendHeatOnOff(int Room, int State)
    {
    	 String statestr;
         if(State==ON) statestr = "1"; 
         		else statestr = "0";
        String text = ",!R" + Room + "DhF" + statestr + "|\0";
        server_out.sendUDP(text);
    }
    
	// Simple Pause where timedelay is in milliseconds
	private static void littlePause(int timedelay)
	{
		try 
		{
		Thread.sleep(timedelay); // do nothing for delay milliseconds 
		} 
		catch(InterruptedException e)
		{
		e.printStackTrace();
		}
	}
	

}