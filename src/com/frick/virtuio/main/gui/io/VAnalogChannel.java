package com.frick.virtuio.main.gui.io;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.frick.virtuio.main.utilities.AnalogChannelInfo;
import com.frick.virtuio.main.utilities.UnityActions;

public class VAnalogChannel extends VIOChannel {
	TYPE type;
	int value, height, width;
	AnalogChannelInfo info;

	public VAnalogChannel(int channelID, VIOBoard board, int value, TYPE type) {
		super(channelID, board);
		this.value = value;
		this.type = type;

		this.height = (int) (board.getCenterPanel().getPreferredSize().getHeight() / 3) - 1;
		this.width = (int) board.getCenterPanel().getPreferredSize().getWidth() / 32 - 5;

		info = UnityActions.getAnalogChannelInfo(board.getBoardID(), channelID);

		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(width, height));

		loadImages();

	}

	public void loadImages() {

	}

	public char getType() {
		if (this.type == TYPE.input) {
			return 'I';
		} else {
			return 'O';
		}

	}

	public char getConfiguration() {

		if (this.type == TYPE.input) {
			return '1';
		} else {
			return '2';
		}

	}

	public void setValue(int val) {
		this.value = val;

	}

	public int getStatus() {
		return this.value;

	}

	public int getID() {
		if (this.type == TYPE.input) {
			return channelID;
		} else if (this.type == TYPE.output) {
			return channelID - 24;
		} else {
			return 0;
		}

	}

	public AnalogChannelInfo getChannelInfo() {
		return info;
	}

}
