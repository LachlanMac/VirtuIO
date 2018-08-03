package com.frick.virtuio.main.utilities;

import com.frick.virtuio.main.net.TCPServer;

public class HeartBeat extends Thread {
	int timeout;
	TCPServer net;
	int heartBeatCounter = 0;

	public HeartBeat(int timeout, TCPServer net) {
		this.timeout = timeout;
		this.net = net;
	}

	public void refresh() {
		heartBeatCounter = 0;
	}

	public void run() {

		while (true) {
			try {
				Thread.sleep(1000);

				heartBeatCounter++;

				if (heartBeatCounter >= timeout) {
					Feedback.networkLog("Client lost connection with Server");
					net.disconnect();
					heartBeatCounter = 0;
					return;
				}
			} catch (InterruptedException e) {

				Feedback.log("HeartBeat Thread error");
			}
		}

	}

}