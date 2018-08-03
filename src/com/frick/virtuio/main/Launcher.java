package com.frick.virtuio.main;

import java.net.InetAddress;
import com.frick.virtuio.main.gui.ClientGUI;
import com.frick.virtuio.main.gui.CommLayer;
import com.frick.virtuio.main.gui.SettingsGUI;
import com.frick.virtuio.main.net.TCPServer;
import com.frick.virtuio.main.serial.BoardManager;
import com.frick.virtuio.main.serial.VirtualComms;
import com.frick.virtuio.main.serial.VirtualPort;
import com.frick.virtuio.main.utilities.Feedback;
import com.frick.virtuio.main.utilities.ResourceLoader;
import com.frick.virtuio.main.utilities.UnityActions;

/**
 * The Launcher Class contains the main method and starts the VirtuIO program
 * with either a local or server only option.
 * 
 * @author Lachlan R McCallum
 */
public class Launcher {

	public static final String PRODUCT = "compressor";
	public static final String resPath = "/home/frick/VirtuIO/res";
	public static InetAddress hostIP;
	public static boolean LOCAL = false;
	public static final int DEFAULT_SERVER_PORT = 8888;
	public static int serverPort = DEFAULT_SERVER_PORT;
	public static final String DEFAULT_SERVER_ADDRESS = "192.168.0.183";
	public static String serverAddress = DEFAULT_SERVER_ADDRESS;
	public static String portName = "/dev/ttyS2";

	public static void main(String[] args) {
		UnityActions.updateChannelInfoLists();

		if (args.length == 1) {

			if (args[0].toLowerCase().equals("server")) {
				startLocalServer();
			} else if (args[0].toLowerCase().equals("client")) {
				startLocalClient();
			}

		} else {
			startLocalClient();
		}

	}

	// Method that starts the Client in local mode which enables GUI
	public static void startLocalClient() {

		Feedback.log("Starting Local Client");

		Feedback.log("Creating Virtual IO Board Manager");
		BoardManager cm = new BoardManager();

		Feedback.log("Creating Virtual Port for " + portName);
		VirtualPort port = new VirtualPort(portName);

		Feedback.log("Starting Virtual Communication Layer");
		VirtualComms comms = new VirtualComms(port, cm);
		comms.initializeComms();
		comms.startCommInterface();

		Feedback.log("Loading UI Settings");
		SettingsGUI.loadSettings();

		Feedback.log("Loading Resources");
		ResourceLoader.loadResources();

		Feedback.log("Loading GUI");
		CommLayer layer = new CommLayer(cm);
		ClientGUI gui = new ClientGUI(layer);

	}

	// Method that starts only the Server for remote access
	public static void startLocalServer() {
		Feedback.log("Starting Local Server");

		Feedback.log("Creating Virtual IO Board Manager");
		BoardManager cm = new BoardManager();

		Feedback.log("Creating Virtual Port for " + portName);
		VirtualPort port = new VirtualPort(portName);

		Feedback.log("Starting Virtual Communication Layer");
		VirtualComms comms = new VirtualComms(port, cm);
		comms.initializeComms();
		comms.startCommInterface();

		TCPServer server = new TCPServer(cm, serverPort);
		Feedback.log("Starting Server on " + serverAddress + ":" + serverPort);
		server.start();

	}
}
