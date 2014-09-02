package com.Lightwave;

/*
 * Separate thread that polls the wireless energy monitor connected to the LightwaveRF wifi box
 */
public class PollEnergyMeter extends Thread {

	private SendUDP m_sender; // Sever object for buffering and sending UDP requests to LWRF box
	private int m_pollingperiod; // Period of time between polls to LWRF box
	
	/*
	 * Constructor.
	 */
	public PollEnergyMeter(SendUDP server, int pollingperiod){
		m_sender=server;
		setPollingPeriod(pollingperiod);
		start();
	}
	

	/*
	 * Do the polling task with this thread running in background
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */

	  public void run() {
		    while(true) {
		    	doPolling();
		    }
		  }
	  
	  /*
	   * Polling UDP call to the LWRF box
	   */
	  private void doPolling(){
			m_sender.sendUDP(",@?\0"); // Poll for energy value from wireless energy monitor on LWRF box
			PollEnergyMeter.littlePause(m_pollingperiod); // Pause thread before polling again.
	  }
	  
		/*
		 * Simple Pause where timedelay is in milliseconds
		 */
		private static void littlePause(int timedelay)
		{
			try 
			{
			Thread.sleep(timedelay); // do nothing for timedelay milliseconds 
			} 
			catch(InterruptedException e)
			{
			e.printStackTrace();
			}
		}

		/*
		 * Setter method to set polling period to pollingperiod milliseconds
		 */
		public void setPollingPeriod(int pollingperiod) {
				m_pollingperiod = pollingperiod;
				System.out.println("Energy Monitoring Polling Period (ms) = " + m_pollingperiod);
			
		}
}