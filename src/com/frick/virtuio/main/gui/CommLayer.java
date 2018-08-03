package com.frick.virtuio.main.gui;

import java.util.ArrayList;
import com.frick.virtuio.main.gui.io.VIOBoard;
import com.frick.virtuio.main.io.AnalogBoard;
import com.frick.virtuio.main.io.DigitalBoard;
import com.frick.virtuio.main.io.IOBoard;
import com.frick.virtuio.main.serial.BoardManager;

public class CommLayer {

	BoardManager boardManager;
	ArrayList<VIOBoard> vBoards;
	Thread updateThread;

	public CommLayer(BoardManager boardManager) {
		this.boardManager = boardManager;

	}

	public void addDigitalBoard(VIOBoard board) {

		DigitalBoard b = new DigitalBoard(board.getBoardID());
		boardManager.addBoard(b);

	}

	public void addAnalogBoard(VIOBoard board) {

		AnalogBoard b = new AnalogBoard(board.getBoardID());
		boardManager.addBoard(b);
	}

	public void sendDigitalState(String state, VIOBoard board) {

		DigitalBoard b = boardManager.getDigitalBoard(board.getBoardID());
		byte[] heyy = b.setPacketData(state.getBytes());

	}

	public void sendAnalogState(String state, VIOBoard board) {

		AnalogBoard b = boardManager.getAnalogBoard(board.getBoardID());

		byte[] heyy = b.setPacketData(state.getBytes());

	}

	public void getState(VIOBoard board) {
		IOBoard b = boardManager.getBoard(board.getBoardID(), board.getBoardType());
		board.RX(new String(b.getPacketData()));
	}

	public void getAnalogState(IOBoard board) {

	}

	public void getDigitalState(IOBoard board) {

	}

	public void removeBoard(VIOBoard board) {
		IOBoard b = boardManager.getBoard(board.getBoardID(), board.getBoardType());
		
	}

	public void updateBoards(ArrayList<VIOBoard> vBoards) {
		this.vBoards = vBoards;
	}

	public void sendBoardStates() {
		for (int i = 0; i < boardManager.getIOBoards().size(); i++) {

			// addToQueue(parser.getBoardData(boardManager.getIOBoards().get(i)));

		}
	}

	public void startUpdater() {

		updateThread = new Thread() {
			public void run() {

				while (true) {

					try {
						Thread.sleep(500);
						for (VIOBoard b : vBoards) {

							CommLayer.this.getState(b);

						}
						Thread.sleep(500);
						for (VIOBoard b : vBoards) {

							b.TX();

						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}
		};

		updateThread.start();

	}
}
