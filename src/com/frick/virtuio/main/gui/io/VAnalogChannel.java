package com.frick.virtuio.main.gui.io;

import java.awt.BorderLayout;
import java.awt.Dimension;

public class VAnalogChannel extends VIOChannel {
	TYPE type;
	int value, height, width;

	public VAnalogChannel(int channelID, VIOBoard board, int value, TYPE type) {
		super(channelID, board);
		this.value = value;
		this.type = type;

		this.height = (int) (board.getCenterPanel().getPreferredSize().getHeight() / 3) - 1;
		this.width = (int) board.getCenterPanel().getPreferredSize().getWidth() / 32 - 5;

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

}
