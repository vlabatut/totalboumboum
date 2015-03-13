package org.totalboumboum.zraphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.awt.image.BufferedImageOp;
import java.awt.image.BufferedImage;

@SuppressWarnings("javadoc")
public class BrightnessChanger extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage originalImage;
	private BufferedImage convolvedImage;
	private JSlider slide = new JSlider(1, 50, 10);

	BrightnessChanger() {
		createBufferedImages();
		setUpJFrame();
	}

	private void createBufferedImages() {
		Image image = new ImageIcon("restmp/heroes/totalboumboum/prognathe/graphics/blue/standing/down.png").getImage();
		originalImage = new BufferedImage(image.getWidth(null), image
				.getHeight(null), BufferedImage.TYPE_INT_RGB);
		convolvedImage = new BufferedImage(image.getWidth(null), image
				.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = originalImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		setBrightnessFactor(1);
	}

	private void setUpJFrame() {
		JFrame myFrame = new JFrame("Image Brightness");
		myFrame.setSize(convolvedImage.getWidth(), convolvedImage.getHeight());
		myFrame.getContentPane().setLayout(new BorderLayout());
		myFrame.getContentPane().add(this, BorderLayout.CENTER);
		slide.addChangeListener(new BrightnessListener());
		myFrame.getContentPane().add(slide, BorderLayout.SOUTH);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setVisible(true);
	}

	private void setBrightnessFactor(float multiple) {
		float[] brightKernel = { multiple };
		BufferedImageOp bright = new ConvolveOp(new Kernel(1, 1, brightKernel));
		bright.filter(originalImage, convolvedImage);
		repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(convolvedImage, 0, 0, this);
	}

	class BrightnessListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent changeEvent) {
			float factor = (float) (slide.getValue()) / 10;
			setBrightnessFactor(factor);
			System.out.println(factor);
		}
	}

	public static void main(String[] args) {
		new BrightnessChanger();
	}

}
