package com.frick.virtuio.main.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UnityActions {

	private static int digitalChannels = 100;

	public static ArrayList<DigitalChannelInfo> digitalChannelInfoList;

	public static void updateChannelInfoLists() {
		
		loadDigitalChannelData();

	}

	public static void redectIO() {

		Process p;
		try {
			p = Runtime.getRuntime().exec("redetectIO");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static String readSetPoint(String setpoint) {

		Process p;
		try {
			p = Runtime.getRuntime().exec("setpoint-util " + setpoint);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			br.readLine();
			String response = br.readLine();

			return response.trim();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		return "Unidentified";
	}

	public static void loadDigitalChannelData() {
		digitalChannelInfoList = new ArrayList<DigitalChannelInfo>();
		Database db = new Database();
		db.connect();

		for (int i = 0; i < digitalChannels; i++) {

			int board = Integer.parseInt(readSetPoint("r:1:" + i + ":3:0:1"));
			int channel = Integer.parseInt(readSetPoint("r:1:" + i + ":4:0:1"));
			int languageIndex = Integer.parseInt(readSetPoint("r:1:" + i + ":15:0:1"));

			if (board != 0) {
				String language = db.getLanguage(languageIndex);
				digitalChannelInfoList.add(new DigitalChannelInfo(i, board, channel, language, languageIndex));
			}

		}

		for (DigitalChannelInfo info : digitalChannelInfoList) {
			System.out.println(info.toString());
		}
		db.close();
	}

	public static DigitalChannelInfo getDigitalChannelInfo(int boardID, int channelID) {

		DigitalChannelInfo channelInfo = null;

		for (DigitalChannelInfo info : digitalChannelInfoList) {

			if (info.getBoardID() == boardID && info.getChannel() == (channelID + 1)) {
				channelInfo = info;
				return channelInfo;

			}

		}

		return channelInfo;

	}

}
