package com.frick.virtuio.main.gui.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import com.frick.virtuio.main.gui.ClientGUI;
import com.frick.virtuio.main.gui.CommLayer;
import com.frick.virtuio.main.gui.MenuGUI;
import com.frick.virtuio.main.gui.SettingsGUI;
import com.frick.virtuio.main.utilities.Feedback;
import com.frick.virtuio.main.utilities.ResourceLoader;

public class VDigitalBoard extends VIOBoard {

	private JButton uiButton;
	private BoardControllerButton controllerButton;
	private static final int BOARD_TYPE = 1;
	private static final long serialVersionUID = 1L;
	private static final float widthMultiple = 2.4f;
	VDigitalChannel[] channels;
	ImageIcon boardImg, onLED, offLED, module, onActiveLED, offActiveLED;
	int width, height, id;
	private String data[];
	JPanel top, center, bottom, rxtx, activePanel;
	JLabel rx, tx, activeOn, activeOff;

	public VDigitalBoard(ClientGUI ui, CommLayer layer, MenuGUI menu, int id) {
		super(ui, layer, menu, id);
		boardType = BOARD_TYPE;
		layer.addDigitalBoard(this);
		buildUI();

		buildChannels();

		startTransmit();
	}

	public VDigitalBoard(ClientGUI ui, CommLayer layer, MenuGUI menu, int id, String[] data) {
		super(ui, layer, menu, id);
		boardType = BOARD_TYPE;
		this.data = data;
		buildUI();
		buildChannels();
		loadData();
		layer.addDigitalBoard(this);
		startTransmit();
	}

	public void buildUI() {

		center = new JPanel();
		top = new JPanel();
		bottom = new JPanel();

		height = SettingsGUI.boardHeight;
		width = (int) (height * widthMultiple);
		this.setLayout(new BorderLayout());

		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEtchedBorder());

		ImageIcon dbImg = new ImageIcon(
				ResourceLoader.digitalBoard.getScaledInstance((int) this.getPreferredSize().getWidth(),
						(int) this.getPreferredSize().getHeight(), Image.SCALE_SMOOTH));
		top.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .28f)));
		center.setPreferredSize(new Dimension(
				(int) this.getPreferredSize().getWidth() - ((int) (this.getPreferredSize().getWidth() * .12f)),
				(int) (this.getPreferredSize().getHeight() * .74f)));
		bottom.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() * .05f)));

		bottom.setOpaque(false);

		center.setLayout(new FlowLayout());
		rxtx = new JPanel();
		rxtx.setBackground(ResourceLoader.digitalGreen);

		rxtx.setLayout(new FlowLayout());
		rxtx.setPreferredSize(new Dimension((int) (top.getPreferredSize().getWidth() * .14f),
				(int) (top.getPreferredSize().getHeight() * 0.3f)));
		rxtx.setOpaque(false);

		JLabel rxLabel = new JLabel("RX");
		JLabel txLabel = new JLabel("TX");
		rxLabel.setForeground(Color.WHITE);
		txLabel.setForeground(Color.WHITE);
		rxLabel.setFont(new Font(rxLabel.getFont().getName(), Font.PLAIN, 7));

		txLabel.setFont(new Font(txLabel.getFont().getName(), Font.PLAIN, 7));

		rxLabel.setPreferredSize(new Dimension((int) (rxtx.getPreferredSize().getWidth() / 8),
				(int) (rxtx.getPreferredSize().getWidth() / 8)));
		txLabel.setPreferredSize(new Dimension((int) (rxtx.getPreferredSize().getWidth() / 8),
				(int) (rxtx.getPreferredSize().getWidth() / 8)));

		rxtx.setOpaque(true);

		onLED = new ImageIcon(
				ResourceLoader.digital_onLED.getScaledInstance((int) (rxtx.getPreferredSize().getWidth() / 7 + 5),
						(int) (rxtx.getPreferredSize().getWidth() / 7 + 5), Image.SCALE_SMOOTH));
		offLED = new ImageIcon(
				ResourceLoader.digital_offLED.getScaledInstance((int) (rxtx.getPreferredSize().getWidth() / 7 + 5),
						(int) (rxtx.getPreferredSize().getWidth() / 7 + 5), Image.SCALE_SMOOTH));

		tx = new JLabel(offLED);

		rx = new JLabel(offLED);

		rxtx.add(txLabel);
		rxtx.add(tx);
		rxtx.add(rxLabel);
		rxtx.add(rx);

		activePanel = new JPanel();
		activePanel.setBackground(ResourceLoader.digitalGreen);
		activePanel.setPreferredSize(new Dimension((int) (top.getPreferredSize().getWidth() * .035f),
				(int) (top.getPreferredSize().getWidth() * .035f)));
		activePanel.setOpaque(false);

		onActiveLED = new ImageIcon(
				ResourceLoader.active_onLED.getScaledInstance((int) (activePanel.getPreferredSize().getWidth()),
						(int) activePanel.getPreferredSize().getWidth(), Image.SCALE_SMOOTH));
		offActiveLED = new ImageIcon(
				ResourceLoader.active_offLED.getScaledInstance((int) (activePanel.getPreferredSize().getWidth()),
						(int) (activePanel.getPreferredSize().getWidth()), Image.SCALE_SMOOTH));

		activeOff = new JLabel(offActiveLED);

		activeOn = new JLabel(onActiveLED);
		activePanel.add(activeOn);

		center.setOpaque(false);
		top.setOpaque(false);

		SpringLayout sLayout = new SpringLayout();
		top.setLayout(sLayout);

		sLayout.putConstraint(SpringLayout.WEST, rxtx, (int) (top.getPreferredSize().getWidth() * .3f),
				SpringLayout.WEST, top);
		sLayout.putConstraint(SpringLayout.NORTH, rxtx, (int) (top.getPreferredSize().getHeight() * .05f),
				SpringLayout.NORTH, top);

		sLayout.putConstraint(SpringLayout.EAST, activePanel, (int) (top.getPreferredSize().getWidth() * .41f),
				SpringLayout.WEST, top);
		sLayout.putConstraint(SpringLayout.SOUTH, activePanel, (int) (top.getPreferredSize().getHeight() * .76f),
				SpringLayout.NORTH, top);

		top.add(rxtx);

		top.add(activePanel);
		
		this.setIcon(dbImg);
		this.add(top, BorderLayout.PAGE_START);
		this.add(center, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.PAGE_END);

	}

	public void TX() {
		setTXOn();
		String state = null;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < channels.length - 1; i++) {

			sb.append(channels[i].getType() + ":" + channels[i].getStatus() + "-");
		}
		sb.append(channels[channels.length - 1].getType() + ":" + channels[channels.length - 1].getStatus());

		state = new String(sb);
		layer.sendDigitalState(state, this);
	}

	public void RX(String updateString) {
		setRXOn();

		String goodString = updateString.substring(1, updateString.length() - 1);

		String[] channelData = goodString.split("-");

		for (int i = 0; i < channels.length; i++) {
			if (channelData.length != channels.length) {
				return;
			}
			if (channels[i].getType() == 'O') {
				channels[i].setState(channelData[i].charAt(2));
			}
			// channels[i].setType(channelData[i].charAt(0));

		}

	}

	public void startTransmit() {

		transmissionThread = new Thread() {
			int activeCounter = 1;

			public void run() {

				while (true) {

					try {
						if (activeCounter > 20) {
							activeCounter = 1;
							activePanel.removeAll();
							activePanel.add(activeOff);
							activePanel.validate();
							activePanel.repaint();
						} else if (activeCounter >= 10 && activeCounter <= 20) {
							activePanel.removeAll();
							activePanel.add(activeOn);
							activePanel.validate();
							activePanel.repaint();
						}

						Thread.sleep(100);
						activeCounter++;

					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (txOn == true) {
						txCounter = 0;
						txOn = false;
						setTXOff();
					}
					if (rxOn == true) {
						rxCounter = 0;
						rxOn = false;
						setRXOff();
					}

				}

			}
		};

		transmissionThread.start();

	}

	public void setRXOff() {
		rx.setIcon(offLED);
		rx.validate();
		rx.repaint();
	}

	public void setTXOff() {
		tx.setIcon(offLED);
		tx.validate();
		tx.repaint();
	}

	public void setRXOn() {
		rxOn = true;
		rx.setIcon(onLED);
		rx.validate();
		rx.repaint();

	}

	public void setTXOn() {
		txOn = true;
		tx.setIcon(onLED);
		tx.validate();
		tx.repaint();
	}

	public String[] getFileBoardData() {
		String[] data = new String[3];

		StringBuffer sb = new StringBuffer();

		sb.append(channels[0].getStatus() + ":" + channels[0].getType());

		for (int i = 1; i < 24; i++) {
			sb.append("-" + channels[i].getStatus() + ":" + channels[i].getType());
		}

		data[0] = "Digital Board " + id;
		data[1] = "digital";
		data[2] = sb.toString();

		return data;

	}

	public JPanel getCenterPanel() {
		return center;
	}

	@Override
	public void buildChannels() {
		channels = new VDigitalChannel[24];

		for (int i = 0; i < channels.length; i++) {

			channels[i] = new VDigitalChannel(i, this, false, VDigitalChannel.TYPE.output);
			center.add(channels[i]);
		}

	}

	public void changeBoard() {
		ui.changeBoard(this);
	}

	public JButton getBoardNavigationButton() {
		return uiButton;
	}

	@Override
	public JButton makeBoardNavigationButton() {
		uiButton = new JButton("Digital Board: " + (boardID + 1));

		uiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeBoard();
			}

		});

		return uiButton;
	}

	@Override
	public BoardControllerButton makeBoardControllerButton(JPanel parent) {

		controllerButton = new BoardControllerButton(parent, this, ui, menu);

		return controllerButton;
	}

	@Override
	public BoardControllerButton getBoardControllerButton() {
		return controllerButton;
	}

	@Override
	public void loadData() {

		if (data.length != 24) {
			Feedback.error("Data Mismatch");
			return;
		}

		for (int i = 0; i < 24; i++) {

			String[] channelData = data[i].split(":");
			String state = channelData[0].trim();
			String type = channelData[1].trim();
			if (type.equals("I")) {
				channels[i].setType('I');
				if (state.equals("0")) {
					channels[i].setState('0');
				} else {
					channels[i].setState('1');
				}
			} else {
				channels[i].setType('O');
				channels[i].setState('0');
			}
		}

	}

}
