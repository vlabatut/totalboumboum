import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Test extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GraphicsDevice graphicsDevice;
	private DisplayMode origDisplayMode;
	private JButton exitButton = new JButton("Exit Full-Screen Mode");

	public static void main(String[] args) {

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		GraphicsDevice[] devices = graphicsEnvironment.getScreenDevices();

		for (int cnt = 0; cnt < 1; cnt++) {
			System.out.println(devices[cnt]);
		}// end for loop
		new Test(devices[0]);
	}// end main

	public Test(GraphicsDevice graphicsDevice) {

		this.graphicsDevice = graphicsDevice;
		setTitle("This title will be hidden (undecorated)");
		origDisplayMode = graphicsDevice.getDisplayMode();
		exitButton.addActionListener(this);
		getContentPane().add(exitButton, BorderLayout.NORTH);

		// Place four labels in the JFrame solely for the
		// purpose of showing that it is a full-screen
		// undecorated JFrame.
		JLabel eastLabel = new JLabel("     East     ");
		eastLabel.setOpaque(true);
		eastLabel.setBackground(Color.RED);
		getContentPane().add(eastLabel, BorderLayout.EAST);

		JLabel southLabel = new JLabel("South", SwingConstants.CENTER);
		southLabel.setOpaque(true);
		southLabel.setBackground(Color.GREEN);
		getContentPane().add(southLabel, BorderLayout.SOUTH);

		JLabel westLabel = new JLabel("     West     ");
		westLabel.setOpaque(true);
		westLabel.setBackground(Color.RED);
		getContentPane().add(westLabel, BorderLayout.WEST);

		JLabel centerLabel = new JLabel("Center", SwingConstants.CENTER);
		centerLabel.setOpaque(true);
		centerLabel.setBackground(Color.WHITE);
		getContentPane().add(centerLabel, BorderLayout.CENTER);

		if (graphicsDevice.isFullScreenSupported()) {
			// Enter full-screen mode with an undecorated,
			// non-resizable JFrame object.
			setUndecorated(true);
			setResizable(false);
			// Make it happen!
			graphicsDevice.setFullScreenWindow(this);
			validate();
		} else {
			System.out.println("Full-screen mode not supported");
		}// end else

	}// end constructor

	public void actionPerformed(ActionEvent evt) {
		// Restore the original display mode
		graphicsDevice.setDisplayMode(origDisplayMode);
		// Terminate the program
		System.exit(0);
	}// end actionPerformed

}// end class
