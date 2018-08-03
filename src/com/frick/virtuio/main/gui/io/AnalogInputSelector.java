package com.frick.virtuio.main.gui.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.frick.virtuio.main.Launcher;


public class AnalogInputSelector extends AnalogSelector {
	private boolean selected = false;
	private int channelID;
	private float fontSize;
	private static final float LOCAL_FONT_SIZE = 12f;
	private static final float REMOTE_FONT_SIZE = 15f;
	
	VAnalogChannel channel;
	VAnalogBoard board;
	JPanel infoPanel, sliderPanel, valuePanel, titlePanel;
	JSlider channelSlider;
	JLabel min, max, value;

	public AnalogInputSelector(int channelID, VAnalogBoard board) {
		
		if(Launcher.LOCAL) {
			fontSize = LOCAL_FONT_SIZE;
		}else {
			fontSize = REMOTE_FONT_SIZE;
		}
		
		this.setFont(this.getFont().deriveFont(fontSize));
		this.setFont(this.getFont().deriveFont(Font.BOLD));
		this.channelID = channelID;
		this.board = board;
		this.channel = board.getChannelByID(channelID);

		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension((int) (board.getCenterPanel().getPreferredSize().getWidth() * 0.8f),
				(int) (board.getCenterPanel().getPreferredSize().getHeight() / 1.2)));
		buildPanel();

		this.setOpaque(true);
		this.setBorderPainted(true);
		this.setBorder(null);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.setBackground(new Color(200, 200, 200, 0));

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
	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
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

	public boolean isSelected() {
		return selected;
	}

	public void buildPanel() {

		String channelType;
		if (channel.getType() == 'O') {
			channelType = "Output";
		} else {
			channelType = "Input";
		}

		sliderPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);

		infoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

		valuePanel = new JPanel();
		titlePanel = new JPanel();
		titlePanel.setPreferredSize(new Dimension((int) infoPanel.getPreferredSize().getWidth(),
				(int) (infoPanel.getPreferredSize().getHeight() * .16f)));
		valuePanel.setPreferredSize(new Dimension((int) infoPanel.getPreferredSize().getWidth(),
				(int) (infoPanel.getPreferredSize().getHeight() * .16f)));
		sliderPanel.setPreferredSize(new Dimension((int) infoPanel.getPreferredSize().getWidth(),
				(int) (infoPanel.getPreferredSize().getHeight() * .66f)));
		channelSlider = new JSlider(JSlider.HORIZONTAL);
		channelSlider.setMaximum(4095);
		channelSlider.setMinimum(0);
		channelSlider.setPaintTicks(true);

		channelSlider.setPreferredSize(new Dimension((int) (sliderPanel.getPreferredSize().getWidth() / 1.2),
				(int) sliderPanel.getPreferredSize().getHeight()));

		channelSlider.setUI(new CustomSliderUI(channelSlider));

		infoPanel.setLayout(new BorderLayout());
		min = new JLabel();
		max = new JLabel();

		min.setFont(min.getFont().deriveFont(fontSize));
		max.setFont(max.getFont().deriveFont(fontSize));
		min.setFont(min.getFont().deriveFont(Font.BOLD));
		max.setFont(max.getFont().deriveFont(Font.BOLD));

		min.setText("0 %");
		max.setText("100 %");

		channelSlider.setValue(0);

		sliderPanel.add(min);
		sliderPanel.add(channelSlider);
		sliderPanel.add(max);

		channelSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				updateValue();

			}

		});

		value = new JLabel();
		value.setFont(value.getFont().deriveFont(fontSize));
		value.setFont(value.getFont().deriveFont(Font.BOLD));

		updateValue();

		valuePanel.add(value);

		JLabel ctitle = new JLabel(channelType + " Channel " + channelID);

		ctitle.setFont(value.getFont().deriveFont(15f));
		ctitle.setFont(value.getFont().deriveFont(Font.BOLD));

		titlePanel.add(ctitle);

		infoPanel.add(titlePanel, BorderLayout.NORTH);
		infoPanel.add(sliderPanel, BorderLayout.CENTER);
		infoPanel.add(valuePanel, BorderLayout.SOUTH);

	}

	public void loadValue(int val) {
		channelSlider.setValue(val);
		float sliderPercent = (float) val / (float) channelSlider.getMaximum();
		int percentVal = (int) (100 * sliderPercent);
		value.setText("Current Value: " + Integer.toString(percentVal) + "%");
		this.setText("" + Integer.toString(percentVal) + "%");
		this.validate();
		this.repaint();

	}

	public void updateValue() {
		int channelValue = channelSlider.getValue();
		float sliderPercent = (float) channelValue / (float) channelSlider.getMaximum();
		int val = (int) (100 * sliderPercent);
		value.setText("Current Value: " + Integer.toString(val) + "%");
		if (!channelSlider.getValueIsAdjusting()) {
			if (channelValue != channel.getStatus()) {
				channel.setValue(channelValue);
				board.TX();
				this.setText("" + Integer.toString(val) + "%");
				this.validate();
				this.repaint();

			}
		}

	}
}

