package com.frick.virtuio.main.net;

import java.net.DatagramPacket;

import com.frick.virtuio.main.communication.SerialProtocol;
import com.frick.virtuio.main.io.AnalogBoard;
import com.frick.virtuio.main.io.DigitalBoard;
import com.frick.virtuio.main.io.IOBoard;
import com.frick.virtuio.main.serial.BoardManager;
import com.frick.virtuio.main.utilities.Feedback;



public class RequestReader {

	BoardManager boardManager;
	DatagramPacket outPacket;

	public RequestReader(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	// This method parses a request based on the Commify CommPacket protocol.
	// Current commands are GET, SET, ADD, DELETE and START
	public String parseRequest(String inData) {

		if (inData == null) {
			return "Invalid Packet\n";
		}
		// initialize byte data
		String returnData = "";
		// split incoming data
		String[] dataArray = new String(inData).trim().split("=");
		// get packetID ( Currently not used )
		try {
			int pID = Integer.parseInt(dataArray[0]);
		} catch (NumberFormatException e) {
			return "Invalid Packet\n";
		}

		// get Command Code
		String cmd = dataArray[1].trim();
		char cmdChar = cmd.charAt(0);
		// get Board ID and Type ( ex. 01 = Digital Board with ID 0 )
		int boardID = Character.getNumericValue(cmd.charAt(1));
		int boardType = Character.getNumericValue(cmd.charAt(2));
		// get Board from board ID and Type
		IOBoard b = boardManager.getBoard(boardID, boardType);
		// the data of the packet
		String data = dataArray[2];

		// Parses based on command recieved
		switch (cmdChar) {
		case SerialProtocol.GET_CODE:
			returnData = getBoardData(b);
			break;
		case SerialProtocol.SET_CODE:
			returnData = setBoardData(b, data);
			break;
		case SerialProtocol.START_CODE:
			returnData = startService();
			break;
		case SerialProtocol.SYNC_CODE:
			returnData = sync(data);
			break;
		case SerialProtocol.HEART_BEAT_CODE:
			returnData = heartBeat();
			break;
		default:
			Feedback.networkError("Unknown Packet Code\n");
			break;
		}

		return returnData;

	}

	// Gets the data associated with the board
	public String getBoardData(IOBoard b) {
		String returnData;
		if (b == null) {
			returnData = new String(SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.SET + SerialProtocol.FAILURE
					+ "00=error getting board data\n");
		} else if (!b.isCommunicating()) {
			returnData = new String(SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.SET + SerialProtocol.FAILURE
					+ b.getBoardID() + b.getBoardType() + "=board is not communicating\n");
		} else {
			returnData = SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.SET + SerialProtocol.SUCCESS
					+ b.getBoardID() + b.getBoardType() + "=" + new String(b.getPacketData()) + "\n";
		}
		return returnData;

	}

	// sends data from the board
	public String setBoardData(IOBoard b, String setData) {

		String returnData;
		if (b == null) {

			returnData = SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.CONFIRM + SerialProtocol.FAILURE
					+ "00=error setting board data\n";
		} else if (!b.isCommunicating()) {
			returnData = new String(SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.CONFIRM + SerialProtocol.FAILURE
					+ b.getBoardID() + b.getBoardType() + "=board is not communicating\n");
		} else {
			b.setPacketData(setData.getBytes());
			returnData = SerialProtocol.CONFIRM;
		}
		return returnData;

	}

	// start services
	public String startService() {

		Feedback.log("Starting Services");
		boardManager.startServices();
		return new String(
				SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.START + SerialProtocol.SUCCESS + "00=START\n");

	}

	public String sync(String data) {

		String[] boardInfo = data.split("x");

		if (data.equals("xEMPTY")) {
			boardManager.getIOBoards().clear();
			return new String(
					SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.SYNC + SerialProtocol.SUCCESS + "00=sync\n");
		} else {
			
			for (int i = 1; i < boardInfo.length; i++) {
			
				int id = Character.getNumericValue(boardInfo[i].charAt(0));
				int type = Character.getNumericValue(boardInfo[i].charAt(1));

				IOBoard b = boardManager.getBoard(id, type);
				if (b == null) {
					IOBoard newBoard;
					if (type == 1) {
						newBoard = new DigitalBoard(id);
					} else {
						newBoard = new AnalogBoard(id);
					}

					boardManager.addBoard(newBoard);
				}

			}

		}

		return new String(
				SerialProtocol.CRYPTO_KEY + "=" + SerialProtocol.SYNC + SerialProtocol.SUCCESS + "00=sync\n");
	}

	public String heartBeat() {
	
		return new String("heartbeat");

	}

}
