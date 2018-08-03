package com.frick.virtuio.main.utilities;

import com.frick.virtuio.main.Launcher;

public class AnalogChannelInfo {
	private int boardID;
	private int channel;
	private int languageIndex;
	private int unitTypeID, sensorTypeID;
	private String language;
	private int id;

	public AnalogChannelInfo(int id, int boardID, int channel, String language, int languageIndex, int unitTypeID,
			int sensorTypeID) {
		this.boardID = boardID - 4;
		this.channel = channel;
		this.language = language;
		this.languageIndex = languageIndex;
		this.unitTypeID = unitTypeID;
		this.sensorTypeID = sensorTypeID;
		this.id = id;

	}

	@Override
	public String toString() {

		return "Analog Channel[ boardID: " + boardID + "   channel: " + channel + "   language: " + language
				+ "    unit type: " + UnityActions.getUnitType(unitTypeID, Launcher.PRODUCT) + "    sensor type: "
				+ UnityActions.getSensorSignal(sensorTypeID, Launcher.PRODUCT) + "]";

	}

	public int getLanguageIndex() {
		return languageIndex;
	}

	public int getBoardID() {
		return boardID;
	}

	public int getChannel() {
		return channel;
	}

	public int getID() {
		return id;
	}

	public int getSensorID() {
		return sensorTypeID;
	}

	public int getUnitID() {
		return unitTypeID;
	}

	public String getSensor() {
		return UnityActions.getSensorSignal(sensorTypeID, Launcher.PRODUCT);
	}

	public String getUnit() {
		return UnityActions.getSensorSignal(unitTypeID, Launcher.PRODUCT);
	}
}
