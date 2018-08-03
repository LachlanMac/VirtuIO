package com.frick.virtuio.main.serial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import com.frick.virtuio.main.io.AnalogBoard;
import com.frick.virtuio.main.io.DigitalBoard;
import com.frick.virtuio.main.io.IOBoard;
import com.frick.virtuio.main.utilities.Feedback;
import com.frick.virtuio.main.utilities.UnityActions;

/*******************************************************************
 * CLASS CommifyManager AUTHOR : Lachlan R McCallum This class manages the
 * creation of the IO Boards and their initial set up. Boards can be added with
 * default data or by a custom configuration text file.
 ********************************************************************/
public class BoardManager {
	// list of all boards
	Vector<IOBoard> boards;

	

	// Constructor
	public BoardManager() {
		boards = new Vector<IOBoard>();
		
	}

	public boolean addBoard(IOBoard board) {
		Feedback.log("Adding Board");
		if (board == null) {
			return false;
		}

		for (int i = 0; i < boards.size(); i++) {
			int boardID = boards.get(i).getBoardID();
			int boardType = boards.get(i).getBoardType();
			if (board.getBoardID() == boardID && board.getBoardType() == boardType) {
				Feedback.networkError("Board was already added");
				return false;
			}

		}
		
		boards.add(board);
		
		UnityActions.redectIO();
		
		return true;

	}

	public DigitalBoard getDigitalBoard(int boardID) {

		IOBoard b = null;
		for (int i = 0; i < boards.size(); i++) {

			if (boards.get(i).getBoardID() == boardID && boards.get(i).getBoardType() == IOBoard.DIGITAL_BOARD_TYPE) {
				b = boards.get(i);
			}
		}
		return (DigitalBoard) b;

	}

	public AnalogBoard getAnalogBoard(int boardID) {

		IOBoard b = null;
		for (int i = 0; i < boards.size(); i++) {

			if (boards.get(i).getBoardID() == boardID && boards.get(i).getBoardType() == IOBoard.ANALOG_BOARD_TYPE) {
				b = boards.get(i);
			}
		}
		return (AnalogBoard) b;

	}

	public boolean removeBoard(IOBoard board) {

		Feedback.log("Removing Board");
		if (board == null) {
			Feedback.log("Could not remove Board");
			return false;
		}

		if (boards.contains(board)) {
			Feedback.log("Board Removed");
			boards.remove(board);
			UnityActions.redectIO();
			return true;
		} else {
			Feedback.log("Could not remove Board");
			return false;
		}

	}

	public void startServices() {
		stopServices();
	}

	public void stopServices() {
		boards.clear();
		UnityActions.redectIO();
	}

	// [[ GETTERS ]]
	public Vector<IOBoard> getIOBoards() {
		return boards;
	}

	// returns the current Board by ID and Type
	public IOBoard getBoard(int boardID, int boardType) {

		IOBoard b = null;
		for (int i = 0; i < boards.size(); i++) {

			if (boards.get(i).getBoardID() == boardID && boards.get(i).getBoardType() == boardType) {
				b = boards.get(i);
			}
		}
		return b;
	}

	
	

}
