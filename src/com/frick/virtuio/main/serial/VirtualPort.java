package com.frick.virtuio.main.serial;

import jssc.SerialPort;
import jssc.SerialPortException;

public class VirtualPort extends SerialPort {

	private static final int DEFAULT_BAUDRATE = 19200;
	private static final int DEFAULT_PARITY = 0;
	private static final int DEFAULT_STOP_BITS = 1;
	private static final int DEFAULT_DATABIT_LENGTH = 8;

	private int baudRate = DEFAULT_BAUDRATE;
	private int parity = DEFAULT_PARITY;
	private int stopbits = DEFAULT_STOP_BITS;
	private int databitLength = DEFAULT_DATABIT_LENGTH;

	public VirtualPort(String port) {
		super(port);

	}

	public boolean openVirtualPort() {

		try {
			this.openPort();
			this.setParams(baudRate, databitLength, stopbits, parity);
			int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
			this.setEventsMask(mask);
			return true;

		} catch (SerialPortException ex) {

			ex.printStackTrace();
			return false;
		}

	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}

	public int getStopbits() {
		return stopbits;
	}

	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}

	public int getDatabitLength() {
		return databitLength;
	}

	public void setDatabitLength(int databitLength) {
		this.databitLength = databitLength;
	}
}
