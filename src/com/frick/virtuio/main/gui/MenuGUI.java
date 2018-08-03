package com.frick.virtuio.main.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.frick.virtuio.main.gui.io.BoardControllerButton;
import com.frick.virtuio.main.gui.io.VAnalogBoard;
import com.frick.virtuio.main.gui.io.VDigitalBoard;
import com.frick.virtuio.main.gui.io.VIOBoard;
import com.frick.virtuio.main.utilities.Feedback;
import com.frick.virtuio.main.utilities.ResourceLoader;

public class MenuGUI extends JPanel {

	private int width, height;

	private JTextField ipField, serverPortField;
	JPanel title, centerGrid, center, bottomGrid, boardConfig, addBoardPopup, boardDisplay, statusPanel,
			netSettingTitle, bottom, boardSettingTitle;
	JButton addBoardBtn, loadBoardBtn, connectBtn, homeBtn;
	JLabel connectionStatus;

	CommLayer layer;
	ClientGUI ui;
	Thread connectionListener;
	InetAddress hostAddress = null;

	MenuGUI(CommLayer layer, ClientGUI ui) {
		this.layer = layer;
		this.ui = ui;
		buildUI();
	}

	public void buildUI() {

		height = 800;
		width = (int) (height);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder());

		buildTop();

		buildCenter();

		buildBottom();

		this.add(bottom, BorderLayout.CENTER);

	}

	public void buildTop() {

		title = new JPanel();
		title.setBackground(ResourceLoader.analogRed);
		JLabel titleLabel = new JLabel("Commify Client Set-Up");
		titleLabel.setForeground(Color.WHITE);
		title.add(titleLabel);
		title.setPreferredSize(new Dimension(width, (int) (height * 0.1f)));
		title.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
		
	}

	public void buildCenter() {
		center = new JPanel();

		// center.setPreferredSize(new Dimension(width, (int) (height * 0.1f)));
		center.setLayout(new BorderLayout());

		centerGrid = new JPanel();
		centerGrid.setLayout(new BorderLayout());

		centerGrid.setBorder(BorderFactory.createLineBorder(Color.black));

		netSettingTitle = new JPanel();
		netSettingTitle.setBackground(ResourceLoader.frickBlue);
		netSettingTitle.setPreferredSize(new Dimension(this.width, 50));
		JLabel netSettingTitleString = new JLabel("Remote Network Settings");
		netSettingTitleString.setFont(new Font(netSettingTitleString.getFont().getName(), Font.BOLD, 14));
		netSettingTitleString.setForeground(Color.WHITE);
		netSettingTitle.add(netSettingTitleString);

		JPanel ipPanel = new JPanel();
		ipPanel.add(new JLabel("Server Address"));
		ipField = new JTextField(20);
		ipField.setBackground(ResourceLoader.textFieldColor);
		ipField.setBorder(BorderFactory.createLineBorder(ResourceLoader.frickBlue));
		ipPanel.add(ipField);

		ipPanel.add(new JLabel("Port"));
		serverPortField = new JTextField(4);
		serverPortField.setBackground(ResourceLoader.textFieldColor);
		serverPortField.setBorder(BorderFactory.createLineBorder(ResourceLoader.frickBlue));
		ipPanel.add(serverPortField);

		JPanel connectPanel = new JPanel();

		connectBtn = new JButton("Connect");

		connectPanel.add(connectBtn);

		statusPanel = new JPanel();
		connectionStatus = new JLabel();
		connectionStatus.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

		connectionStatus.setBackground(ResourceLoader.frickAlarm);
		connectionStatus.setForeground(Color.white);
		statusPanel.add(connectionStatus);

		center.add(netSettingTitle, BorderLayout.NORTH);

		centerGrid.add(ipPanel, BorderLayout.NORTH);
		centerGrid.add(connectPanel, BorderLayout.CENTER);
		centerGrid.add(statusPanel, BorderLayout.SOUTH);
		center.add(centerGrid, BorderLayout.CENTER);

	}

	public void buildBottom() {
		bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.setPreferredSize(new Dimension(width, (int) (height * 0.8f)));
		bottomGrid = new JPanel();
		bottomGrid.setLayout(new BorderLayout());

		bottomGrid.setBorder(BorderFactory.createLineBorder(Color.black));

		boardSettingTitle = new JPanel();
		boardSettingTitle.setBackground(ResourceLoader.frickBlue);

		boardSettingTitle.setPreferredSize(new Dimension(this.width, 50));
		JLabel boardSettingsTitleString = new JLabel("I/O Board Configuration");
		boardSettingsTitleString.setFont(new Font(boardSettingsTitleString.getFont().getName(), Font.BOLD, 14));
		boardSettingsTitleString.setForeground(Color.WHITE);
		boardSettingTitle.add(boardSettingsTitleString);

		bottom.add(boardSettingTitle, BorderLayout.NORTH);
		bottom.add(bottomGrid, BorderLayout.CENTER);

		buildBoardConfigurator();

	}

	public void buildBoardConfigurator() {

		boardConfig = new JPanel();
		addBoardBtn = new JButton("Add New Board");
		

		addBoardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				popUpAdd();
			}

		});

		loadBoardBtn = new JButton("Load Board");

		loadBoardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				popUpLoad();

			}

		});

		boardDisplay = new JPanel();
		boardDisplay.setLayout(new GridLayout(14, 1, 5, 5));

		boardConfig.add(addBoardBtn);
		boardConfig.add(loadBoardBtn);
		bottomGrid.add(boardConfig, BorderLayout.NORTH);
		bottomGrid.add(boardDisplay, BorderLayout.CENTER);

	}

	public void saveBoardToFile() {

	}

	public void popUpLoad() {

		FileNameExtensionFilter filter = new FileNameExtensionFilter("BDX Files", "bdx");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(boardConfig);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fc.getSelectedFile();
			loadBoardFromFile(file);

		} else {

		}

	}

	public void popUpAdd() {
		// addBoardPopup = new JPanel();

		Object[] possibilities = { "Analog", "Digital" };
		String s = (String) JOptionPane.showInputDialog(bottomGrid, "Select Board Type", "Board Configuration",
				JOptionPane.PLAIN_MESSAGE, null, possibilities, "WHAT");

		// If a string was returned, say so.
		if ((s != null) && (s.length() > 0)) {
			boolean[] digitalIDs = new boolean[] { false, false, false, false };
			boolean[] analogIDs = new boolean[] { false, false, false, false };
			if (s.equals("Analog")) {
				int boardID = 0;
				ArrayList<VIOBoard> boards = ui.getAnalogBoards();
				if (boards.size() == 0) {

					boardID = 0;
				} else if (boards.size() >= 4) {

					Feedback.error("Maximum number of Analog Boards reached Maximum Boards Error");

					return;
				} else {

					for (VIOBoard b : boards) {
						int currentID = b.getBoardID();
						analogIDs[currentID] = true;
					}
					for (int i = 0; i < analogIDs.length; i++) {
						if (analogIDs[i] == false) {
							boardID = i;
							break;
						}
					}

				}

				VAnalogBoard b = new VAnalogBoard(ui, layer, this, boardID);
				ui.addBoard(b);

				JPanel aBoardPanel = new JPanel();
				aBoardPanel.setLayout(new BorderLayout());

				BoardControllerButton aButton = b.makeBoardControllerButton(aBoardPanel);

				aBoardPanel.add(aButton.getXButton(), BorderLayout.WEST);

				aBoardPanel.add(aButton, BorderLayout.CENTER);
				aBoardPanel.add(aButton.getSaveButton(), BorderLayout.EAST);
				boardDisplay.add(aBoardPanel);

				boardDisplay.validate();
				boardDisplay.repaint();

			} else if (s.equals("Digital")) {
				int boardID = 0;
				ArrayList<VIOBoard> dboards = ui.getDigitalBoards();
				if (dboards.size() == 0) {
					boardID = 0;
				} else if (dboards.size() >= 4) {

					Feedback.error("TOO MANT BOARDS");

					return;
				} else {

					for (VIOBoard b : dboards) {
						int currentID = b.getBoardID();
						digitalIDs[currentID] = true;
					}
					for (int i = 0; i < digitalIDs.length; i++) {
						if (digitalIDs[i] == false) {
							boardID = i;
							break;
						}
					}

				}

				VDigitalBoard b = new VDigitalBoard(ui, layer, this, boardID);
				ui.addBoard(b);

				JPanel dBoardPanel = new JPanel();
				dBoardPanel.setLayout(new BorderLayout());

				BoardControllerButton dButton = b.makeBoardControllerButton(dBoardPanel);

				dBoardPanel.add(dButton.getXButton(), BorderLayout.WEST);
				dBoardPanel.add(dButton, BorderLayout.CENTER);
				dBoardPanel.add(dButton.getSaveButton(), BorderLayout.EAST);
				boardDisplay.add(dBoardPanel);
				boardDisplay.validate();
				boardDisplay.repaint();

			}
			return;
		}
	}
	
	public void loadBoardFromFile(File file) {

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));

			String name = bufferedReader.readLine();
			String type = bufferedReader.readLine();
			String values = bufferedReader.readLine();
			String[] data = values.split("-");
			boolean[] digitalIDs = new boolean[] { false, false, false, false };
			boolean[] analogIDs = new boolean[] { false, false, false, false };

			if (type.toLowerCase().trim().equals("analog")) {

				int boardID = 0;
				ArrayList<VIOBoard> boards = ui.getAnalogBoards();
				if (boards.size() == 0) {

					boardID = 0;
				} else if (boards.size() >= 4) {

					System.out.println("MAX BOARDS REACHED");
					bufferedReader.close();
					return;
				} else {

					for (VIOBoard b : boards) {
						int currentID = b.getBoardID();
						analogIDs[currentID] = true;
					}
					for (int i = 0; i < analogIDs.length; i++) {
						if (analogIDs[i] == false) {
							boardID = i;
							break;
						}
					}

				}

				VAnalogBoard b = new VAnalogBoard(ui, layer, this, boardID, data);
				ui.addBoard(b);

				JPanel aBoardPanel = new JPanel();
				aBoardPanel.setLayout(new BorderLayout());

				BoardControllerButton aButton = b.makeBoardControllerButton(aBoardPanel);

				aBoardPanel.add(aButton.getXButton(), BorderLayout.WEST);

				aBoardPanel.add(aButton, BorderLayout.CENTER);
				aBoardPanel.add(aButton.getSaveButton(), BorderLayout.EAST);

				boardDisplay.add(aBoardPanel);

			} else if (type.toLowerCase().trim().equals("digital")) {
				int boardID = 0;
				ArrayList<VIOBoard> dboards = ui.getDigitalBoards();
				if (dboards.size() == 0) {
					boardID = 0;
				} else if (dboards.size() >= 4) {

					System.out.println("MAX BOARDS REACHED");
					bufferedReader.close();
					return;
				} else {

					for (VIOBoard b : dboards) {
						int currentID = b.getBoardID();
						digitalIDs[currentID] = true;
					}
					for (int i = 0; i < digitalIDs.length; i++) {
						if (digitalIDs[i] == false) {
							boardID = i;
							break;
						}
					}

				}

				VDigitalBoard b = new VDigitalBoard(ui, layer, this, boardID, data);
				ui.addBoard(b);

				JPanel dBoardPanel = new JPanel();
				dBoardPanel.setLayout(new BorderLayout());

				BoardControllerButton dButton = b.makeBoardControllerButton(dBoardPanel);

				dBoardPanel.add(dButton.getXButton(), BorderLayout.WEST);
				dBoardPanel.add(dButton, BorderLayout.CENTER);
				dBoardPanel.add(dButton.getSaveButton(), BorderLayout.EAST);
				boardDisplay.add(dBoardPanel);

			}
			boardDisplay.validate();
			boardDisplay.repaint();
			bufferedReader.close();
		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void removeFromBoardDisplay(JPanel p) {

		boardDisplay.remove(p);
		boardDisplay.validate();
		boardDisplay.repaint();

	}

}
