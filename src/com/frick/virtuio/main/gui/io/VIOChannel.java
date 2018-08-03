package com.frick.virtuio.main.gui.io;

import javax.swing.JPanel;

public class VIOChannel extends JPanel {
	protected enum TYPE {
		input, output
	};

	protected int channelID;
	protected VIOBoard board;

	public VIOChannel(int channelID, VIOBoard board) {
		this.channelID = channelID;
		this.board = board;
	}

}
