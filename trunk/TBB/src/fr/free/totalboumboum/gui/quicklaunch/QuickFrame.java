package fr.free.totalboumboum.gui.quicklaunch;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.configuration.ConfigurationLoader;
import fr.free.totalboumboum.data.configuration.ConfigurationSaver;
import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.loop.LoopRenderPanel;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundRenderPanel;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.data.configuration.GuiConfigurationLoader;
import fr.free.totalboumboum.gui.game.loop.LoopPanel;
import fr.free.totalboumboum.gui.game.round.results.RoundResults;
import fr.free.totalboumboum.gui.tools.FullRepaintManager;
import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

public class QuickFrame extends JFrame implements WindowListener,LoopRenderPanel,RoundRenderPanel
{	private static final long serialVersionUID = 1L;

	private BufferStrategy bufferStrategy;
	private Configuration configuration;
	private Loop loop;
	private JProgressBar loadProgressBar;

	public QuickFrame(Configuration configuration) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super("TBB v."+GameConstants.VERSION);
		this.configuration = configuration;
		// listener
		addWindowListener(this);
		// set icon
		String iconPath = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FILE_FRAME;
		Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
		setIconImage(icon);
		// set dimensions
		Container contentPane = getContentPane();
		Dimension dim = configuration.getPanelDimension();
		contentPane.setPreferredSize(dim);
		setResizable(false);
		setFocusable(true);
		// size the frame
		pack();
		// center the frame
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setLocation((screenWidth-getSize().width)/2,(screenHeight-getSize().height)/2);
	   
	    // init the game
	 // tournament
		AbstractTournament tournament = configuration.getTournament();  
	    tournament.init();
	    tournament.progress();
	    // match
	    Match match = tournament.getCurrentMatch();
	    match.progress();
	    
	    // show the frame
		setVisible(true);
        toFront();
        
	    //round
	    Round round = match.getCurrentRound();
	    round.setPanel(this);
		int limit = round.getProfiles().size()+2;
		loadProgressBar = new JProgressBar(0,limit);
		loadProgressBar.setStringPainted(true);
		loadProgressBar.setFont(new Font("Arial",Font.PLAIN,dim.height/2));
		getContentPane().add(loadProgressBar);
		validate();
		repaint();
	    round.progress();
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
	{	//
		System.exit(0);		
	}

	
	
	
	@Override
	public void loopOver()
	{	
	}

	@Override
	public void paintScreen()
	{	Graphics g = bufferStrategy.getDrawGraphics();
		// draw stuff in the buffer
//NOTE: draw background?	
		loop.drawLevel(g);
		g.dispose();
		// copy the buffer on the panel
		bufferStrategy.show();
		//Tell the System to do the drawing now
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void playerOut(int index)
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	
			}
		});	
	}

	@Override
	public void loadStepOver()
	{	int val = loadProgressBar.getValue();
		loadProgressBar.setValue(val+1);
		switch(val)
		{	// itemset
			case 0:
				loadProgressBar.repaint();
				break;
			// theme
			case 1:
				loadProgressBar.repaint();
				break;
			// players
			default:
				Round round = configuration.getTournament().getCurrentMatch().getCurrentRound();
				if(val==round.getProfiles().size()+2)
				{	// progress bar
					loadProgressBar.repaint();
					getContentPane().remove(loadProgressBar);
					validate();
					repaint();
				    // loop
			        requestFocus();					
				    loop = round.getLoop();
				    loop.setPanel(this);
					// init the BufferStrategy
					createBufferStrategy(2);
					bufferStrategy = getBufferStrategy();
				}
				else
				{	loadProgressBar.repaint();
				}
				break;
		}
	}

	@Override
	public void roundOver()
	{	Container contentPane = getContentPane();
		Dimension dim = contentPane.getPreferredSize();
		QuickResults roundResults = new QuickResults(dim,configuration);
		contentPane.add(roundResults);
//		contentPane.setLayout(layout);
		validate();
		repaint();
	}
}

