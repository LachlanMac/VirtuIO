package com.frick.virtuio.main.io;

import com.frick.virtuio.main.communication.SerialCommand;

/*******************************************************************
 * CLASS IOBoard [SUPERCLASS] AUTHOR : Lachlan R McCallum This class represents
 * a virtual IO Board which can receive and response to commands from a
 * controller. Can be implemented as Digital or Analog Board.
 ********************************************************************/
public abstract class IOBoard {
	// board type and ID variables.
	protected int boardType = 999;
	protected int boardID = 999;
	public final static int ANALOG_BOARD_TYPE = 2;
	public final static int DIGITAL_BOARD_TYPE = 1;
	protected int baudRate = 19200;
	protected boolean hasReceived = false;

	// DEFAULT CONSTRUCTOR
	public IOBoard(int boardID) {
		this.boardID = boardID;
	}

	// All IO boards must have a transmit function
	public abstract String tx();

	// All IO boards must have a receive function that takes a command
	public abstract void rx(SerialCommand in);

	// logic that determines response
	public abstract String getResponse(char cmd);

	// Network Communication Functions for receiving and sending packets
	public abstract byte[] getPacketData();

	public abstract byte[] setPacketData(byte[] setData);

	// [[ GETTERS ]]
	public int getBoardType() {
		return boardType;
	}

	public int getBoardID() {
		return boardID;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public boolean isCommunicating() {
		return hasReceived;
	}

	// [[ SETTERS ]]
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

}
