package com.frick.virtuio.main.gui.io;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.frick.virtuio.main.gui.ClientGUI;
import com.frick.virtuio.main.gui.CommLayer;
import com.frick.virtuio.main.gui.MenuGUI;

public abstract class VIOBoard extends JLabel {

	protected int boardType;
	protected int boardID;
	protected boolean isNewBoard = true;
	protected Thread transmissionThread;
	protected boolean txOn = false, rxOn = false;
	protected int txCounter = 0, rxCounter = 0;
	protected int ledTimeOut = 1000;

	protected CommLayer layer;
	protected ClientGUI ui;
	protected MenuGUI menu;

	public VIOBoard(ClientGUI ui, CommLayer layer, MenuGUI menu, int boardID) {
		this.ui = ui;
		this.boardID = boardID;
		this.menu = menu;
		this.layer = layer;
	}

	public int getBoardType() {
		return boardType;
	}

	public int getBoardID() {
		return boardID;
	}

	public abstract void buildUI();

	public abstract void RX(String updateString);

	public abstract void TX();

	public abstract JPanel getCenterPanel();

	public abstract void setRXOff();

	public abstract void setTXOff();

	public abstract void setRXOn();

	public abstract void setTXOn();

	public abstract void loadData();

	public abstract JButton makeBoardNavigationButton();

	public abstract JButton getBoardNavigationButton();

	public abstract BoardControllerButton makeBoardControllerButton(JPanel parent);

	public abstract BoardControllerButton getBoardControllerButton();

	public void activateBoard() {
		this.isNewBoard = false;
	}

	public boolean isActivated() {
		return this.isNewBoard;
	}

	public abstract String[] getFileBoardData();

	public abstract void buildChannels();

}