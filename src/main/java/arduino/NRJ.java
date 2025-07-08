package arduino;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

//import com.fazecast.jSerialComm.*;

import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;


public class NRJ {
	protected CommPortIdentifier portId;
	protected SerialPort comPort;
	protected String portDescription;
	protected int baud_rate;
	
	public NRJ() {
		//empty constructor if port undecided
	}
	
	
	
	public NRJ(String portDescription) throws NoSuchPortException, PortInUseException {
		//make sure to set baud rate after
		this.portDescription = portDescription;
		portId = CommPortIdentifier.getPortIdentifier(portDescription);
		comPort = (SerialPort) portId.open("MyAppName", 2000); // nome da aplicação e timeout em ms
	}
	
	public NRJ(String portDescription, int baud_rate) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
		//preferred constructor
		this.portDescription = portDescription;
		portId = CommPortIdentifier.getPortIdentifier(portDescription);
		comPort = (SerialPort) portId.open("MyAppName", 2000); // nome da aplicação e timeout em ms
		this.baud_rate = baud_rate;
		comPort.setSerialPortParams(this.baud_rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		

	}
	
	
	
	public boolean openConnection(){
//		if(comPort.openPort()){
//			try {Thread.sleep(100);} catch(Exception e){}
//			return true;
//		}
//		else {
//			AlertBox alert = new AlertBox(new Dimension(400,100),"Error Connecting", "Try Another port");
//			alert.display();
//			return false;
//		}
		return true;
	}
	
	public void closeConnection() {
		if (comPort != null) {
		    comPort.close();   // equivalente a comPort.closePort() do jSerialComm
		}

	}
	
	public void setPortDescription(String portDescription){
//		this.portDescription = portDescription;
//		comPort = SerialPort.getCommPort(this.portDescription);
	}
	
	public void setBaudRate(int baud_rate) throws UnsupportedCommOperationException{
		this.baud_rate = baud_rate;
		comPort.setSerialPortParams(this.baud_rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
	public String serialRead() throws IOException {
	    InputStream in = comPort.getInputStream();
	    StringBuilder sb = new StringBuilder();
	    int data = in.read(); // leitura bloqueante de 1 byte

	    while (data != -1 && (char)data != '\n') {
	        sb.append((char)data);
	        data = in.read();
	    }

	    return sb.toString();
	}

	
	public String serialRead( int limit) throws IOException {
	    InputStream in = comPort.getInputStream();
	    StringBuilder out = new StringBuilder();
	    int count = 1;
	    int data = in.read(); // leitura bloqueante de 1 byte

	    while (data != -1 && (char) data != '\n' && count <= limit) {
	        out.append((char) data);
	        data = in.read();
	        count++;
	    }

	    return out.toString();
	}

	
	/**
	 * Read all the available bytes
	 */
	public String serialReadAll(SerialPort serialPort) throws IOException {
	    InputStream in = serialPort.getInputStream();
	    int bufferSize = in.available(); // número de bytes disponíveis (estimado)

	    if (bufferSize > 0) {
	        byte[] readBuffer = new byte[bufferSize];
	        int numRead = in.read(readBuffer, 0, bufferSize); // leitura de bytes disponíveis
	        return new String(readBuffer, 0, numRead);
	    } else {
	        return ""; // nada disponível
	    }
	}
	
	public void serialWrite(String s) {
	    try {
	        Thread.sleep(5); // pequena espera para garantir sincronização
	        OutputStream out = comPort.getOutputStream();
	        PrintWriter pout = new PrintWriter(out);
	        pout.print(s);   // escreve a string
	        pout.flush();    // força envio imediato
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public void serialWrite(String s, int noOfChars, int delay) {
	    try {
	        Thread.sleep(5);  // espera inicial
	        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
	        int i = 0;
	        for (i = 0; i < s.length() - noOfChars; i += noOfChars) {
	            pout.write(s.substring(i, i + noOfChars));
	            pout.flush();
	            Thread.sleep(delay);  // delay entre os blocos
	        }
	        pout.write(s.substring(i));  // resto da string
	        pout.flush();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public void serialWrite(char c) {
	    try {
	        Thread.sleep(5); // espera inicial
	        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
	        pout.write(c);
	        pout.flush();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void serialWrite(char c, int delay) {
	    try {
	        Thread.sleep(5); // espera inicial
	        PrintWriter pout = new PrintWriter(comPort.getOutputStream());
	        pout.write(c);
	        pout.flush();
	        Thread.sleep(delay); // delay após o envio
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
