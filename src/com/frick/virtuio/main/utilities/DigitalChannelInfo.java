package com.frick.virtuio.main.utilities;

public class DigitalChannelInfo {

	private int boardID;
	private int channel;
	private int languageIndex;
	private String language;
	private int id;

	public DigitalChannelInfo(int id, int boardID, int channel, String language, int languageIndex) {
		this.boardID = boardID - 4;
		this.channel = channel;
		this.language = language;
		this.languageIndex = languageIndex;
		this.id = id;

	}

	@Override
	public String toString() {

		return "Digital Channel[ boardID: " + boardID + "   channel: " + channel + "   language: " + language + "]";

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

}
