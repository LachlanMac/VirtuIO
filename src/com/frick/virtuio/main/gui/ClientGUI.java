package com.frick.virtuio.main.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.frick.virtuio.main.Launcher;
import com.frick.virtuio.main.gui.io.VIOBoard;
import com.frick.virtuio.main.serial.BoardManager;
import com.frick.virtuio.main.utilities.ResourceLoader;

public class ClientGUI extends JFrame {
	JPanel controllerPanel, analogPanel_1, analogPanel_2, digitalPanel_1, digitalPanel_2, statusPanel, boardPanel,
			boardSelectorPanel, backDrop, headerPanel;
	ArrayList<VIOBoard> boardList;
	ArrayList<JButton> navigationButtonList;
	JButton submitBoardCount, homeBtn;

	JLabel unityHeader;
	JButton menuButton;
	MenuGUI menu;
	Dimension buttonSize;
	CommLayer layer;

	public ClientGUI(CommLayer layer) {
		this.layer = layer;
		this.menu = new MenuGUI(layer, this);
		boardList = new ArrayList<VIOBoard>();
		navigationButtonList = new ArrayList<JButton>();
		buildGUI();
		layer.updateBoards(boardList);
		layer.startUpdater();

	}

	public void changeBoard(VIOBoard b) {

		for (JButton btn : navigationButtonList) {
			setDeactiveButton(btn);
			setDeactiveButton(menuButton);

		}

		if (b == null) {
			setActiveButton(menuButton);
			boardPanel.removeAll();
			boardPanel.add(menu);
			// add menu
		} else {
			setActiveButton(b.getBoardNavigationButton());
			boardPanel.removeAll();
			boardPanel.add(b);
		}
		validate();
		repaint();

	};

	public void addBoard(VIOBoard b) {

		boardList.add(b);

		JButton ctl = b.makeBoardNavigationButton();

		ctl.setPreferredSize(buttonSize);
		ctl.setBackground(Color.WHITE);
		ctl.setForeground(Color.BLACK);
		ctl.setOpaque(true);
		ctl.setBorderPainted(true);
		ctl.setPreferredSize(buttonSize);
		ctl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

		navigationButtonList.add(ctl);
		controllerPanel.add(ctl);
		controllerPanel.validate();
		controllerPanel.repaint();

		layer.updateBoards(boardList);

	}

	public void removeBoard(VIOBoard b) {
		boardList.remove(b);
		layer.removeBoard(b);
		navigationButtonList.remove(b.getBoardNavigationButton());
		controllerPanel.remove(b.getBoardNavigationButton());
		controllerPanel.validate();
		controllerPanel.repaint();

		layer.updateBoards(boardList);

	}

	public ArrayList<VIOBoard> getBoards() {
		return boardList;
	}

	public ArrayList<VIOBoard> getAnalogBoards() {

		ArrayList<VIOBoard> aBoards = new ArrayList<VIOBoard>();

		for (VIOBoard b : boardList) {
			if (b.getBoardType() == 2) {
				aBoards.add(b);
			}
		}
		return aBoards;
	}

	public ArrayList<VIOBoard> getDigitalBoards() {

		ArrayList<VIOBoard> dBoards = new ArrayList<VIOBoard>();

		for (VIOBoard b : boardList) {
			if (b.getBoardType() == 1) {

				dBoards.add(b);
			}
		}
		return dBoards;
	}

	public void buildGUI() {

		this.setTitle("Commify " + SettingsGUI.boardVersion);
		this.setSize(new Dimension(SettingsGUI.xResolution, SettingsGUI.yResolution));
		this.setUndecorated(true);

		headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(1, 1));
		headerPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * .106f)));
		headerPanel.setBackground(ResourceLoader.frickBlue);

		boardPanel = new JPanel();
		boardPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * .81f)));
		controllerPanel = new JPanel();
		controllerPanel.setBackground(ResourceLoader.frickBackground);
		controllerPanel.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * .08f)));

		buttonSize = new Dimension(new Dimension((int) (controllerPanel.getPreferredSize().getHeight() * 3),
				(int) (controllerPanel.getPreferredSize().getHeight() * 0.5f)));

		backDrop = new JPanel();
		boardPanel.setOpaque(false);
		backDrop.setBackground(ResourceLoader.frickBackground);
		backDrop.setOpaque(true);
		backDrop.setLayout(new BorderLayout());
		this.add(backDrop);

		menuButton = new JButton("Menu");
		menuButton.setBackground(ResourceLoader.frickLightBlue);
		menuButton.setForeground(Color.white);
		menuButton.setOpaque(true);
		menuButton.setBorderPainted(false);
		menuButton.setPreferredSize(buttonSize);

		menuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeBoard(null);

			}

		});

		controllerPanel.add(menuButton);
		boardPanel.add(menu);
		makeUnityHeader();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		backDrop.add(headerPanel, BorderLayout.PAGE_START);
		backDrop.add(controllerPanel, BorderLayout.CENTER);
		backDrop.add(boardPanel, BorderLayout.PAGE_END);

		this.setVisible(true);

	}

	public void makeUnityHeader() {

		int width = (int) headerPanel.getPreferredSize().getWidth();
		int height = (int) headerPanel.getPreferredSize().getHeight();

		ImageIcon unityHeaderIcon = new ImageIcon(
				ResourceLoader.unityHeader.getScaledInstance((int) (width), height, Image.SCALE_SMOOTH));
		ImageIcon homeIcon = new ImageIcon(ResourceLoader.homeBtn.getScaledInstance((int) (width * .16f),
				height - (int) (height * .16f), Image.SCALE_SMOOTH));

		homeBtn = new JButton();

		homeBtn.setPreferredSize(new Dimension((int) (width * .13f), height - (int) (height)));
		homeBtn.setOpaque(false);
		homeBtn.setBorderPainted(false);

		homeBtn.setContentAreaFilled(false);
		homeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Launcher.LOCAL) {
					Process p;
					try {
						p = Runtime.getRuntime().exec("AltTabify");

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {

				}

			}

		});

		JButton spacer = new JButton();
		spacer.setPreferredSize(new Dimension(width - (int) (homeBtn.getPreferredSize().getWidth()), height));
		spacer.setOpaque(false);
		spacer.setBorderPainted(false);

		spacer.setContentAreaFilled(false);
		unityHeader = new JLabel();

		unityHeader.setIcon(unityHeaderIcon);
		unityHeader.setPreferredSize(new Dimension(width, height));
		unityHeader.setLayout(new BorderLayout());

		unityHeader.add(spacer, BorderLayout.WEST);
		unityHeader.add(homeBtn, BorderLayout.EAST);
		headerPanel.add(unityHeader);
		unityHeader.validate();
		unityHeader.repaint();

	}

	public void setActiveButton(JButton btn) {
		btn.setBackground(ResourceLoader.frickLightBlue);
		btn.setForeground(Color.white);
		btn.setOpaque(true);
		btn.setBorderPainted(false);
		btn.validate();
		btn.repaint();

	}

	public void setDeactiveButton(JButton btn) {
		btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		btn.setBackground(Color.WHITE);
		btn.setForeground(Color.BLACK);
		btn.setOpaque(true);
		btn.setBorderPainted(true);
		btn.validate();
		btn.repaint();

	}

}
