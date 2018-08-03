package com.frick.virtuio.main.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.UIManager;

public class SettingsGUI {
	public static int xResolution, yResolution, boardHeight;
	public static String boardVersion, serverHost, clientPort, serverPort;

	public static void loadSettings() {

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		xResolution = (int) screenSize.getWidth();
		yResolution = (int) screenSize.getHeight();

		boardHeight = (int) (screenSize.getHeight() / 2);
		boardVersion = "unknown version";

	}
}
