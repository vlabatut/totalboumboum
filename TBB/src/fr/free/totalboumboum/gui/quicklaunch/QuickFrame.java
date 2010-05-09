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

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.loop.LoopRenderPanel;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundRenderPanel;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.tools.GuiFileTools;


import java.awt.Canvas;
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
import java.io.File;
import java.io.IOException;

public class QuickFrame extends JFrame implements WindowListener,LoopRenderPanel,RoundRenderPanel
{	private static final long serialVersionUID = 1L;

	private BufferStrategy bufferStrategy;
	private Configuration configuration;
	private Loop loop;
	private JProgressBar loadProgressBar;
	private Canvas canvas;

	public QuickFrame(Configuration configuration, GraphicsConfiguration gconf) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super("TBB v."+GameConstants.VERSION,gconf);
		this.configuration = configuration;
		
		// listener
		addWindowListener(this);
		setFocusable(true);
		
		// set icon
		String iconPath = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FILE_FRAME;
		Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
		setIconImage(icon);
		
		// set dimensions
		Dimension dim = configuration.getPanelDimension();
		setResizable(false);
		
	    // create canvas
	    canvas = new Canvas();
	    canvas.setIgnoreRepaint( true );
	    canvas.setSize(dim);
	    add(canvas);
		canvas.setIgnoreRepaint(true);
		
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
	    configuration.loadQuickstart();
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
		remove(canvas);
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
//	
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
				    add(canvas);
					validate();
					repaint();
				    // loop
			        requestFocus();					
				    loop = round.getLoop();
				    loop.setPanel(this);
					// init the BufferStrategy
					canvas.createBufferStrategy(2);
					bufferStrategy = canvas.getBufferStrategy();
				}
				else
				{	loadProgressBar.repaint();
				}
				break;
		}
	}

	@Override
	public void roundOver()
	{	remove(canvas);
		Container contentPane = getContentPane();
		Dimension dim = configuration.getPanelDimension();
		QuickResults roundResults = new QuickResults(dim,configuration);
		contentPane.add(roundResults);
//		contentPane.setLayout(layout);
		validate();
		repaint();
	}
}

