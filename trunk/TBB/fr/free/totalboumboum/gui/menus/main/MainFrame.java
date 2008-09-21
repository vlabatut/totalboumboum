package fr.free.totalboumboum.gui.menus.main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.configuration.ConfigurationLoader;
import fr.free.totalboumboum.data.configuration.ConfigurationSaver;
import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.tools.SwingTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame implements WindowListener,MenuContainer
{	private static final long serialVersionUID = 1L;

	private MainMenu mainMenuPanel;

	public MainFrame() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	super("TBB v."+GameConstants.VERSION);
		// init
		XmlTools.init();
		this.configuration = loadConfiguration();
		// frame
		addWindowListener(this);
		// icon
		String iconPath = FileTools.getIconsPath()+File.separator+"frame.png";
		Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
		setIconImage(icon);
		// dimensions
		setPreferredSize(getConfiguration().getPanelDimension());
		setResizable(false);
		setVisible(true);
		//
		pack();

		final MainFrame f = this;
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	// end init
				SwingTools.init(configuration,getGraphics());
				// panel
				try
				{	mainMenuPanel = new MainMenu(f,null);
				}
				catch (Exception e)
				{	e.printStackTrace();
				}
				currentPanel = mainMenuPanel;
				getContentPane().add(mainMenuPanel, BorderLayout.CENTER);
				pack();
			}
		});		
	}

	// ----------------- window listener methods -------------

	public void windowActivated(WindowEvent e)
	{	//engine.setPause(false);
	}

	public void windowDeactivated(WindowEvent e)
	{	//engine.setPause(true);
	}

	public void windowDeiconified(WindowEvent e)
	{	//engine.setPause(false);
	}

	public void windowIconified(WindowEvent e)
	{	//engine.setPause(true);
	}

	public void windowClosing(WindowEvent e)
	{	//engine.setRunning(false);
		exit();
	}

	public void windowClosed(WindowEvent e)
	{
	}

	public void windowOpened(WindowEvent e)
	{
	}
	
	public void exit()
	{	saveConfiguration();
		System.exit(0);		
	}
	

	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Configuration configuration;

	@Override
	public Configuration getConfiguration()
	{	return configuration;
	}

	private Configuration loadConfiguration() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Configuration result = ConfigurationLoader.loadConfiguration();
		return result;
	}
	
	private void saveConfiguration()
	{	ConfigurationSaver.saveConfiguration();
	}

	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public MainFrame getFrame()
	{	return this;
	}

	/////////////////////////////////////////////////////////////////
	// MENU PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private MenuPanel currentPanel;

	@Override
	public void setMenuPanel(MenuPanel newPanel)
	{	Container contentPane = getContentPane();
		contentPane.remove(currentPanel);
		currentPanel = newPanel;
		currentPanel.refresh();
		contentPane.add(newPanel, BorderLayout.CENTER);
		contentPane.validate();
		contentPane.repaint();
	}
	
	public void setMainMenuPanel()
	{	setMenuPanel(mainMenuPanel);	
	}

	
	
	
	public Image getImage()
	{	int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		Image result = createImage(width,height);
		return result;
	}
	

		
}

