package arduino;

import java.awt.Dimension;
import java.io.PrintWriter;
import java.util.Scanner;

import com.fazecast.jSerialComm.*;


public class Arduino {
	protected SerialPort comPort;
	protected String portDescription;
	protected int baud_rate;
	
	public Arduino() {
		//empty constructor if port undecided
	}
	
	public Arduino(String portDescription) {
		//make sure to set baud rate after
		this.portDescription = portDescription;
		comPort = SerialPort.getCommPort(this.portDescription);
	}
	
	public Arduino(String portDescription, int baud_rate) {
		//preferred constructor
		this.portDescription = portDescription;
		comPort = SerialPort.getCommPort(this.portDescription);
		this.baud_rate = baud_rate;
		comPort.setBaudRate(this.baud_rate);
	}
	
	
	
	public boolean openConnection(){
		if(comPort.openPort()){
			try {Thread.sleep(100);} catch(Exception e){}
			return true;
		}
		else {
			AlertBox alert = new AlertBox(new Dimension(400,100),"Error Connecting", "Try Another port");
			alert.display();
			return false;
		}
	}
	
	public void closeConnection() {
		comPort.closePort();
	}
	
	public void setPortDescription(String portDescription){
		this.portDescription = portDescription;
		comPort = SerialPort.getCommPort(this.portDescription);
	}
	
	public void setBaudRate(int baud_rate){
		this.baud_rate = baud_rate;
		comPort.setBaudRate(this.baud_rate);
	}
	
	public String getPortDescription(){
		return portDescription;
	}
	
	public SerialPort getSerialPort(){
		return comPort;
	}
	
	/**
	 * Read from string until get a '\n'
	 * @return
	 */
	public String serialRead(){
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		byte[] b = new byte[1];
		String s = "";
		comPort.readBytes(b, 1);
		while((char)b[0]!='\n') {
			s = s + (char)b[0];
			comPort.readBytes(b, 1);
		}
		return s;
	}
	
	public String serialRead(int limit){
		//in case of unlimited incoming data, set a limit for number of readings
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		String out="";
		int count=1;
		byte[] b = new byte[1];
		comPort.readBytes(b, 1);
		while((char)b[0]!='\n'&&count<=limit) {
			out = out + (char)b[0];
			comPort.readBytes(b, 1);
			count++;
		}
		return out;
	}
	
	/**
	 * Read all the available bytes
	 */
	public String serialReadAll(){
		   int bufferSize = comPort.bytesAvailable();
		   byte[] readBuffer = new byte[bufferSize];
		   int numRead = comPort.readBytes(readBuffer, readBuffer.length);
		   return new String(readBuffer);
		}
	
	public void serialWrite(String s){
		//writes the entire string at once.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try{Thread.sleep(5);} catch(Exception e){}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		pout.print(s);
		pout.flush();
		
	}
	
	public void serialWrite(String s,int noOfChars, int delay){
		//writes the entire string, 'noOfChars' characters at a time, with a delay of 'delay' between each send.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try{Thread.sleep(5);} catch(Exception e){}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		int i = 0;
		for(i = 0; i < s.length() - noOfChars; i += noOfChars){
			pout.write(s.substring(i,i+noOfChars));
			pout.flush();
			try{Thread.sleep(delay);}catch(Exception e){}
		}
		pout.write(s.substring(i));
		pout.flush();
		
	}
	
	public void serialWrite(char c){
		//writes the character to output stream.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try{Thread.sleep(5);} catch(Exception e){}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());pout.write(c);
		pout.flush();
	}
	
	public void serialWrite(char c, int delay){
		//writes the character followed by a delay of 'delay' milliseconds.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try{Thread.sleep(5);} catch(Exception e){}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());pout.write(c);
		pout.flush();
		try{Thread.sleep(delay);}catch(Exception e){}
	}
}
