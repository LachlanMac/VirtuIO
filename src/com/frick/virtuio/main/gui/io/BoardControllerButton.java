package com.frick.virtuio.main.gui.io;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.frick.virtuio.main.gui.ClientGUI;
import com.frick.virtuio.main.gui.MenuGUI;
import com.frick.virtuio.main.utilities.ResourceLoader;



public class BoardControllerButton extends JButton {
	VIOBoard b;
	JPanel p;
	ClientGUI ui;
	MenuGUI menu;

	public BoardControllerButton(JPanel p, VIOBoard b, ClientGUI ui, MenuGUI menu) {

		this.ui = ui;
		this.p = p;
		this.b = b;
		this.menu = menu;

		if (b.getBoardType() == 1) {
			// this.setForeground(Color.white);
			this.setText("Digital Board " + (b.getBoardID() + 1));
			this.setBorder(BorderFactory.createLineBorder(ResourceLoader.digitalGreen, 2));
			this.validate();
			this.repaint();
		} else {
			// this.setForeground(Color.white);
			this.setText("Analog Board " + (b.getBoardID() + 1));
			this.setBorder(BorderFactory.createLineBorder(ResourceLoader.analogRed, 2));
			this.validate();
			this.repaint();
		}

	}

	public JPanel getParentPanel() {
		return p;
	}

	public JButton getXButton() {

		JButton button = new JButton("X");
		button.setBackground(Color.RED);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				menu.removeFromBoardDisplay(p);
				ui.removeBoard(b);

			}

		});
		return button;

	}

	public JButton getSaveButton() {
		JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame parentFrame = new JFrame();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");

				int userSelection = fileChooser.showSaveDialog(parentFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {

					File fileToSave = new File(fileChooser.getSelectedFile() + ".BDX");
					String path = fileToSave.getPath();
					try {

						String[] data = b.getFileBoardData();
						fileToSave.createNewFile();
						BufferedWriter br = new BufferedWriter(new FileWriter(fileToSave));
						br.write(data[0] + "\n");
						br.write(data[1] + "\n");
						br.write(data[2] + "\n");
						br.close();

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		});
		return saveBtn;
	}

}

