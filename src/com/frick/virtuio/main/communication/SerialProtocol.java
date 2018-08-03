package com.frick.virtuio.main.communication;

public class SerialProtocol {
	
	public static final String CRYPTO_KEY = "34887584";
	public static final String SUCCESS = "Z";
	public static final String FAILURE = "X";
	public static final String GET = "g";
	public static final String SET = "s";
	public static final String START = "f";
	public static final String split = "=";
	public static final String SYNC = "y";
	public static final String HEART_BEAT = "h";
	public static final String CONFIRM = "c";

	public static final char CONFIRM_CODE = 'c';
	public static final char GET_CODE = 'g';
	public static final char SET_CODE = 's';
	public static final char START_CODE = 'f';
	public static final char SYNC_CODE = 'y';
	public static final char HEART_BEAT_CODE = 'h';

	public static String getPacketData(int packetID, String cmdType, String cmdStatus, String data) {

		return new String(packetID + split + cmdType + cmdStatus + data);

	}
}
