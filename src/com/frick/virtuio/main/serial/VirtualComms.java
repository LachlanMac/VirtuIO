package com.frick.virtuio.main.serial;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.frick.virtuio.main.communication.SerialCommand;
import com.frick.virtuio.main.io.IOBoard;
import com.frick.virtuio.main.utilities.Feedback;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


public class VirtualComms implements SerialPortEventListener, Runnable {

	private static final int LOOP_DELAY_TIMER_MS = 50;
	private static final int READ_WRITE_BUFFER_DELAY_NS = 500000;
	private static final int TIME_OUT_COUNTER = 100;
	private VirtualPort serialPort;
	private final char CARRIAGE_RETURN = 10;
	private final char LINE_RETURN = 13;
	// thread that listens for serial events
	private Thread commThread;
	// Thread safe queue where Received commands are kept and processed
	private BlockingQueue<SerialCommand> commandQueue;
	// Holds a reference to the current command being built by the listener
	private SerialCommand currentCommand;
	// Manager object
	private BoardManager cm;
	// List of all virtual IO boards
	private Vector<IOBoard> boardList;
	// status boolean for writing to serial port
	static boolean outputBufferEmptyFlag = false;

	// CONSTRUCTOR
	public VirtualComms(VirtualPort serialPort, BoardManager cm) {
		this.cm = cm;
		this.serialPort = serialPort;
		this.boardList = cm.getIOBoards();

		currentCommand = new SerialCommand();
		commandQueue = new ArrayBlockingQueue<SerialCommand>(1024);

	}

	// Starts the Comm Interface
	public void startCommInterface() {
		commThread.start();
	}

	// Sets initial variables and configuration for the commport connection
	public void initializeComms() {
		
		try {
			serialPort.openVirtualPort();
			serialPort.addEventListener(this);
			
		} catch (SerialPortException e) {
			Feedback.error("Could not add listener to Comm port");
			e.printStackTrace();
		}
		commThread = new Thread(this);

	}

	// Thread Loop
	@Override
	public void run() {
		int timeout = 0;
		try {
			Thread.sleep(5);
			while (true) {

				timeout++;

				if (timeout > TIME_OUT_COUNTER) {
					Feedback.error("Timeout - No Communication detected");
					timeout = 0;
				}
				Thread.sleep(LOOP_DELAY_TIMER_MS);
				if (!commandQueue.isEmpty()) {
					timeout = 0;
					SerialCommand c = commandQueue.peek();

					int boardType = c.getType();
					int boardID = c.getBoardID();

					if (boardList.size() == 0) {
						// throw out packet if there is no board to send it to.
						commandQueue.poll();
					}

					for (int i = 0; i < boardList.size(); i++) {

						if ((boardList.get(i).getBoardType() == boardType)
								&& (boardList.get(i).getBoardID() == boardID)) {

							if (c.hasBeenParsed()) {
								// Receive command
								boardList.get(i).rx(c);
								// generate response
								String out = boardList.get(i).tx();
								// buffer delay
								Thread.sleep(0, READ_WRITE_BUFFER_DELAY_NS);
								// write response
								serialPort.writeBytes(out.getBytes());

								// DEQUEUE
								if (!commandQueue.isEmpty()) {
									commandQueue.poll();
								}
							} else {
								// bad packet
								if (!commandQueue.isEmpty()) {
									commandQueue.poll();
								}
								currentCommand = new SerialCommand();
							}

						} else {
							// bad packet
							commandQueue.poll();
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR()) {

			byte[] readBuffer = new byte[32];

			boolean endOfCommand = false;

			try {
				readBuffer = serialPort.readBytes();
			} catch (SerialPortException e) {

				e.printStackTrace();
			}

			for (int j = 0; j < readBuffer.length; j++) {
				if (readBuffer[j] == CARRIAGE_RETURN || readBuffer[j] == LINE_RETURN) {
					endOfCommand = true;
				}
			}

			if (endOfCommand) {
				currentCommand.addData(new String(readBuffer));
				currentCommand.parseCommand();
				commandQueue.add(currentCommand);
				currentCommand = new SerialCommand();
			} else {
				currentCommand.addData(new String(readBuffer));
			}

		} else if (event.isCTS()) {// If CTS line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				System.out.println("CTS - ON");
			} else {
				System.out.println("CTS - OFF");
			}
		} else if (event.isDSR()) {/// If DSR line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				System.out.println("DSR - ON");
			} else {
				System.out.println("DSR - OFF");
			}
		}
	}

}
