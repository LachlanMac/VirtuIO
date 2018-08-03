package com.frick.virtuio.main.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UnityActions {

	private static int digitalChannels = 100;

	public static ArrayList<DigitalChannelInfo> digitalChannelInfoList;
	public static ArrayList<AnalogChannelInfo> analogChannelInfoList;

	public static void updateChannelInfoLists() {

		loadDigitalChannelData();
		loadAnalogChannelData();

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

	public static void loadAnalogChannelData() {
		analogChannelInfoList = new ArrayList<AnalogChannelInfo>();
		Database db = new Database();
		db.connect();

		for (int i = 0; i < digitalChannels; i++) {

			int board = Integer.parseInt(readSetPoint("r:0:" + i + ":3:0:1"));
			int channel = Integer.parseInt(readSetPoint("r:0:" + i + ":4:0:1"));
			int languageIndex = Integer.parseInt(readSetPoint("r:0:" + i + ":15:0:1"));
			int unitType = Integer.parseInt(readSetPoint("r:0:" + i + ":20:0:1"));
			int sensorType = Integer.parseInt(readSetPoint("r:0:" + i + ":21:0:1"));

			if (board != 0) {
				String language = db.getLanguage(languageIndex);
				analogChannelInfoList
						.add(new AnalogChannelInfo(i, board, channel, language, languageIndex, unitType, sensorType));
			}

		}

		for (AnalogChannelInfo info : analogChannelInfoList) {
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

	public static AnalogChannelInfo getAnalogChannelInfo(int boardID, int channelID) {

		AnalogChannelInfo channelInfo = null;

		for (AnalogChannelInfo info : analogChannelInfoList) {

			if (info.getBoardID() == boardID && info.getChannel() == (channelID + 1)) {
				channelInfo = info;
				return channelInfo;
			}

		}

		return channelInfo;

	}

	public static String getUnitType(int unitTypeID, String product) {

		String unitType = "Undefined";

		if (product.equals("compressor")) {

			switch (unitTypeID) {
			case 0:
				unitType = "None";
				break;
			case 1:
				unitType = "Pressure";
				break;
			case 2:
				unitType = "Pressure (Magnitude)";
				break;
			case 3:
				unitType = "Pressure (Positive)";
				break;
			case 4:
				unitType = "Temperature";
				break;
			case 5:
				unitType = "Temperature (Magnitude)";
				break;
			case 6:
				unitType = "Percent %";
				break;
			case 7:
				unitType = "Amps";
				break;
			case 8:
				unitType = "RPM";
				break;
			case 9:
				unitType = "kW";
				break;
			case 10:
				unitType = "Seconds";
				break;
			case 11:
				unitType = "Minutes";
				break;
			case 12:
				unitType = "Hours";
				break;
			case 13:
				unitType = "Integer";
				break;
			case 14:
				unitType = "Real (#.#)";
				break;
			case 15:
				unitType = "Real (#.##)";
				break;
			case 16:
				unitType = "Volts";
				break;
			case 17:
				unitType = "Vibration (Fg)";
				break;
			}
		}
		return unitType;
	}

	public static String getSensorSignal(int sensorTypeID, String product) {

		String sensorType = "Undefined";

		if (product.equals("compressor")) {

			switch (sensorTypeID) {
			case 0:
				sensorType = "None";
				break;
			case 1:
				sensorType = "0-5V";
				break;
			case 2:
				sensorType = "1-5V";
				break;
			case 3:
				sensorType = "4-20mA";
				break;
			case 4:
				sensorType = "Potentiometer";
				break;
			case 5:
				sensorType = "ICTD";
				break;
			case 6:
				sensorType = "RTD";
				break;
			case 7:
				sensorType = "CT";
				break;
			case 8:
				sensorType = "0-20mA";
				break;
			case 9:
				sensorType = "Vibration";
				break;
			case 10:
				sensorType = "0-10V";
				break;
			case 11:
				sensorType = "2-10V";
				break;
			default:
				Feedback.analogLog("Could not load Sensor Type");
				break;
			}
		}

		return sensorType;

	}

}
