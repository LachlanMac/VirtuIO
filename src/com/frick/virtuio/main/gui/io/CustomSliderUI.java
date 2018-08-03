package com.frick.virtuio.main.gui.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import com.frick.virtuio.main.Launcher;
import com.frick.virtuio.main.utilities.ResourceLoader;

public class CustomSliderUI extends BasicSliderUI {

	private static final int MAX_THUMB_SIZE = 60;

	public CustomSliderUI(JSlider b) {
		super(b);

	}

	@Override
	public void paint(Graphics g, JComponent c) {
		recalculateIfInsetsChanged();
		recalculateIfOrientationChanged();
		Rectangle clip = g.getClipBounds();

		if (slider.getPaintTrack() && clip.intersects(trackRect)) {
			paintTrack(g);
		}
		if (slider.getPaintTicks() && clip.intersects(tickRect)) {
			paintTicks(g);
		}
		if (slider.getPaintLabels() && clip.intersects(labelRect)) {
			paintLabels(g);
		}
		if (slider.hasFocus() && clip.intersects(focusRect)) {
			paintFocus(g);
		}
		if (clip.intersects(thumbRect)) {
			Color savedColor = slider.getBackground();
			slider.setBackground(Color.BLACK);
			paintThumb(g);
			slider.setBackground(savedColor);
		}
	}

	protected Dimension getThumbSize() {

		Dimension sliderSize = slider.getPreferredSize();

		Dimension size = new Dimension();

		int diameter = 0;
		if (slider.getOrientation() == JSlider.VERTICAL) {
			System.out.println("VERT");
			size.width = 20;
			size.height = 11;
		} else {

			if (Launcher.LOCAL) {
				diameter = Math.min(MAX_THUMB_SIZE, (int) (sliderSize.getHeight() / 2));
			} else {
				diameter = Math.min(MAX_THUMB_SIZE, (int) (sliderSize.getHeight() / 3));
			}
			size.width = diameter;
			size.height = diameter;
		}

		return size;
	}

	@Override
	public void paintThumb(Graphics g) {
		Rectangle knobBounds = thumbRect;
		int w = knobBounds.width;
		int h = knobBounds.height;

		g.translate(knobBounds.x, knobBounds.y);

		if (slider.isEnabled()) {
			g.setColor(slider.getBackground());
		} else {
			g.setColor(slider.getBackground().darker());
		}

		int cw = w / 2;

		g.setColor(ResourceLoader.frickLightBlue);
		g.fillOval(1, 1, w, h);

		g.setColor(ResourceLoader.frickBlue);
		g.drawOval(1, 1, w, h);
		g.translate(-knobBounds.x, -knobBounds.y);
	}

}
