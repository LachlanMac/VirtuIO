package com.frick.virtuio.main.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.frick.virtuio.main.Launcher;
import com.frick.virtuio.main.communication.SerialProtocol;
import com.frick.virtuio.main.serial.BoardManager;
import com.frick.virtuio.main.utilities.Feedback;
import com.frick.virtuio.main.utilities.HeartBeat;
import com.frick.virtuio.main.utilities.Timed;
import com.frick.virtuio.main.utilities.Timer;

public class TCPServer extends Thread implements Timed {
	private boolean readyToSend = false;
	private boolean readyToReceive = false;
	private ServerSocket server;
	private int port;
	BoardManager boardManager;
	RequestReader parser;
	private BlockingQueue<String> sendQueue;
	private HeartBeat hb;
	Socket client;
	private String clientAddress;
	BufferedReader in;
	PrintWriter out;
	int ticker = 0;

	public TCPServer(BoardManager boardManager, int port) {
		this.port = port;
		this.boardManager = boardManager;
		this.parser = new RequestReader(boardManager);
		sendQueue = new ArrayBlockingQueue<String>(1024);
		Timer t = new Timer(10, this);
		startServer();
	}

	public void startServer() {

		InetAddress address = null;

		try {

			address = InetAddress.getByName(Launcher.serverAddress);
			server = new ServerSocket(port, 1, address);
			Feedback.networkLog(
					"TCP Server started on " + server.getInetAddress().getHostAddress() + ":" + server.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		startListener();

		while (true) {

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (client == null || client.isClosed()) {
				startListener();
			} else {

				if (readyToReceive) {
					receive();
					readyToReceive = false;
				}
				if (readyToSend) {
					send();
					readyToSend = false;
				}

			}

		}

	}

	public void startListener() {
		try {

			hb = new HeartBeat(10, this);
			client = server.accept();
			clientAddress = client.getInetAddress().getHostAddress();
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
			hb.start();

			Feedback.networkLog("Connection received from " + clientAddress);

		} catch (IOException e) {
			Feedback.networkError("Could not create Listener socket");
		}

	}

	public void addToQueue(String msg) {

		if (sendQueue.size() > 1000) {
			sendQueue.clear();
		}
		this.sendQueue.add(msg);
	}

	public void receive() {
		String received;
		try {
			if (readyToReceive) {
				String response = "";
				if (in.ready()) {
					if ((received = in.readLine()) != null) {
						response = parser.parseRequest(received);
					}
				}

				if (response.trim().equals("heartbeat")) {
					refreshHeartbeat();
				} else if (response.trim().equals(SerialProtocol.CONFIRM)) {
					// NO NOTHING
				} else {
					addToQueue(response);
				}

				readyToReceive = false;
			}
		} catch (IOException e) {

			e.printStackTrace();
			try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void send() {

		while (!sendQueue.isEmpty()) {
			out.println(sendQueue.poll());
			out.flush();
		}
	}

	public void refreshHeartbeat() {
		hb.refresh();
	}

	public void disconnect() {
		try {

			client.close();
			client = null;

		} catch (IOException e) {
			Feedback.networkLog("Client Socket Closed");
		}

	}

	public void sendBoardStates() {
		for (int i = 0; i < boardManager.getIOBoards().size(); i++) {

			addToQueue(parser.getBoardData(boardManager.getIOBoards().get(i)));

		}
	}

	@Override
	public void tick() {
		ticker++;
		readyToReceive = true;

		if (ticker > 5) {
			ticker = 0;

			sendBoardStates();

			readyToSend = true;
		}

	}
}
