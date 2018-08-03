package com.frick.virtuio.main.gui.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.frick.virtuio.main.Launcher;

public class AnalogOutputSelector extends AnalogSelector {

	private boolean selected = false;
	private int channelID, module, offset;
	VAnalogChannel[] channels;
	VAnalogBoard board;
	JPanel infoPanel, chan1, chan2, chan3, chan4, chan5, chan6, chan7, chan8;
	JLabel chan1Value, chan2Value, chan3Value, chan4Value, chan5Value, chan6Value, chan7Value, chan8Value;
	JLabel[] chanValues = new JLabel[] { chan1Value, chan2Value, chan3Value, chan4Value, chan5Value, chan6Value,
			chan7Value, chan8Value };

	JLabel chan1Display, chan2Display, chan3Display, chan4Display, chan5Display, chan6Display, chan7Display,
			chan8Display;
	JLabel[] buttonValues = new JLabel[] { chan1Display, chan2Display, chan3Display, chan4Display, chan5Display,
			chan6Display, chan7Display, chan8Display };

	JPanel smallDisplayPanel;

	public AnalogOutputSelector(VAnalogChannel[] channels, VAnalogBoard board, int module) {

		this.module = module;
		this.board = board;
		this.channels = channels;

		this.setFont(this.getFont().deriveFont(14f));
		this.setFont(this.getFont().deriveFont(Font.BOLD));
		this.setOpaque(true);
		this.setBorderPainted(true);
		this.setBorder(null);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.setBackground(new Color(200, 200, 200, 0));
		this.setLayout(new GridLayout(1, 4, 0, 0));

		for (int i = 0; i < chanValues.length; i++) {
			chanValues[i] = new JLabel("0");
			chanValues[i].setFont(this.getFont().deriveFont(14f));
			chanValues[i].setFont(this.getFont().deriveFont(Font.BOLD));
		}

		if (this.module == 1) {
			offset = 0;

			for (int i = 0; i < 4; i++) {

				buttonValues[i + offset] = new JLabel("0%");
				if (Launcher.LOCAL) {
					buttonValues[i + offset].setFont(this.getFont().deriveFont(10f));
				} else {
					buttonValues[i + offset].setFont(this.getFont().deriveFont(10f));
					buttonValues[i + offset].setFont(this.getFont().deriveFont(Font.BOLD));
				}

				JPanel valuePanel = new JPanel();
				valuePanel.setLayout(new BorderLayout());
				valuePanel.setOpaque(false);
				JPanel tempo = new JPanel();

				tempo.setOpaque(false);
				tempo.add(buttonValues[i + offset]);
				JPanel northBuffer = new JPanel();
				northBuffer.setOpaque(false);
				northBuffer.setPreferredSize(new Dimension(0, 23));
				valuePanel.add(northBuffer, BorderLayout.NORTH);
				valuePanel.add(tempo, BorderLayout.CENTER);
				this.add(valuePanel);
			}

		} else {
			offset = 4;
			for (int i = 0; i < 4; i++) {

				buttonValues[i + offset] = new JLabel("0%");
				if (Launcher.LOCAL) {
					buttonValues[i + offset].setFont(this.getFont().deriveFont(10f));
				} else {
					buttonValues[i + offset].setFont(this.getFont().deriveFont(10f));
					buttonValues[i + offset].setFont(this.getFont().deriveFont(Font.BOLD));
				}

				JPanel valuePanel = new JPanel();
				valuePanel.setLayout(new BorderLayout());
				valuePanel.setOpaque(false);
				JPanel tempo = new JPanel();

				tempo.setOpaque(false);
				tempo.add(buttonValues[i + offset]);
				JPanel northBuffer = new JPanel();
				northBuffer.setOpaque(false);
				northBuffer.setPreferredSize(new Dimension(0, 23));
				valuePanel.add(northBuffer, BorderLayout.NORTH);
				valuePanel.add(tempo, BorderLayout.CENTER);
				this.add(valuePanel);
			}

		}

		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (selected == false) {
					select();
				} else {
					deselect();
				}

			}
		});

		buildPanel();

	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public void buildPanel() {
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension((int) (board.getCenterPanel().getPreferredSize().getWidth() * 0.8f),
				(int) (board.getCenterPanel().getPreferredSize().getHeight() / 1.2)));
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		infoPanel.setLayout(new GridLayout(2, 4, 1, 1));
		chan1 = new JPanel();
		chan2 = new JPanel();
		chan3 = new JPanel();
		chan4 = new JPanel();
		chan5 = new JPanel();
		chan6 = new JPanel();
		chan7 = new JPanel();
		chan8 = new JPanel();
		buildChannel(0, chan1);
		buildChannel(1, chan2);
		buildChannel(2, chan3);
		buildChannel(3, chan4);
		buildChannel(4, chan5);
		buildChannel(5, chan6);
		buildChannel(6, chan7);
		buildChannel(7, chan8);

		infoPanel.add(chan1);
		infoPanel.add(chan2);
		infoPanel.add(chan3);
		infoPanel.add(chan4);
		infoPanel.add(chan5);
		infoPanel.add(chan6);
		infoPanel.add(chan7);
		infoPanel.add(chan8);

	}

	public void buildChannel(int id, JPanel panel) {
		VAnalogChannel c = channels[id];
		panel.setLayout(new GridLayout(2, 0));
		JLabel title = new JLabel("Output Channel " + (c.getID() + 1));
		title.setFont(this.getFont().deriveFont(14f));
		title.setFont(this.getFont().deriveFont(Font.BOLD));
		panel.add(title);

		chanValues[id].setText("Value : 0");
		panel.add(chanValues[id]);

	}

	public void select() {

		board.unselectAll();
		board.getCenterPanel().add(infoPanel);
		this.setOpaque(false);
		this.selected = true;
		this.setBorderPainted(true);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		this.setBackground(new Color(200, 200, 200, 170));
		board.updateCenter();
		this.validate();
		this.repaint();
	}

	public void deselect() {
		board.getCenterPanel().removeAll();
		this.setOpaque(false);
		this.setBorderPainted(false);
		this.selected = false;
		this.setBackground(new Color(200, 200, 200, 0));
		this.validate();
		this.repaint();
		board.updateCenter();
	}

	public void updateValue(int channelID, int channelValue) {

		float percentValue = ((float) channelValue / 4095) * 100;
		String buttonText = percentValue + "%";

		for (int i = 0 + offset; i < 4 + offset; i++) {

			if (channelID == channels[i].getID()) {

				buttonValues[channelID].setText((int) percentValue + "%");

			}

			chanValues[channelID].setText("Value : " + Integer.toString((int) (percentValue)) + "%");
			chanValues[channelID].validate();
			chanValues[channelID].repaint();

		}

	}

}
