package fr.free.totalboumboum.gui.frames;

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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.loop.LoopRenderPanel;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundRenderPanel;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.data.configuration.misc.MiscConfiguration;
import fr.free.totalboumboum.gui.game.round.results.QuickResults;
import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.gui.tools.GuiTools;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuickFrame extends JFrame implements ActionListener, WindowListener, LoopRenderPanel, RoundRenderPanel
{	private static final long serialVersionUID = 1L;

	private BufferStrategy bufferStrategy;
	private Loop loop;
	private JProgressBar loadProgressBar;
	private Canvas canvas;
	
	private GraphicsDevice device;
	private DisplayMode currentMode;
	private DisplayMode newMode;
	private boolean fullScreen;
	private Dimension dim;
	
	public QuickFrame() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super("TBB v."+GameConstants.VERSION+" (Quicklaunch)");
		// graphic conf
		GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphEnv.getDefaultScreenDevice();
		
		// listener
		addWindowListener(this);
		setFocusable(true);
		
		// set icon
		String iconPath = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FILE_FRAME;
		Image icon = Toolkit.getDefaultToolkit().getImage(iconPath);
		setIconImage(icon);
		
		// set tooltip delay
		ToolTipManager.sharedInstance().setInitialDelay(200);
		
		// set dimensions
		dim = Configuration.getVideoConfiguration().getPanelDimension();
		fullScreen = false;
		setFullscreenDim();	
		setResizable(false);	       
		
		// GUI config
		GuiTools.quickInit();
		MiscConfiguration miscConfig = new MiscConfiguration();
		miscConfig.setFont(null,new Font("Arial",Font.PLAIN,10));
		GuiConfiguration.setMiscConfiguration(miscConfig);
		
	    // create canvas
	    canvas = new Canvas();
	    canvas.setIgnoreRepaint(true);
//	    canvas.setSize(dim);
	    canvas.setMinimumSize(dim);
	    canvas.setPreferredSize(dim);
	    canvas.setMaximumSize(dim);
	    add(canvas);
		
		// create progress bar
		remove(canvas);
		loadProgressBar = new JProgressBar();
//		loadProgressBar.setSize(dim);
		loadProgressBar.setMinimumSize(dim);
		loadProgressBar.setPreferredSize(dim);
		loadProgressBar.setMaximumSize(dim);
		loadProgressBar.setStringPainted(true);
		loadProgressBar.setFont(GuiConfiguration.getMiscConfiguration().getFont().deriveFont(dim.height/2f));
		getContentPane().add(loadProgressBar);
		
		// tournament
	    Configuration.getGameConfiguration().loadQuickstart();
		AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
		ArrayList<Profile> selectedProfiles = new ArrayList<Profile>();
		ProfilesSelection profilesSelection = Configuration.getGameConfiguration().getQuickStartSelected();
		selectedProfiles = ProfileLoader.loadProfiles(profilesSelection);
		tournament.init(selectedProfiles);
	    tournament.progress();
	    
	    // match
	    Match match = tournament.getCurrentMatch();
	    match.progress();
		       
	    //round
	    Round round = match.getCurrentRound();
	    round.setPanel(this);
		int limit = round.getProfiles().size()+3;
		loadProgressBar.setMinimum(0);
		loadProgressBar.setMaximum(limit);		
	}
	
	/////////////////////////////////////////////////////////////////
	// INIT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void setFullscreenDim()
	{	if(Configuration.getVideoConfiguration().getFullScreen())
		{	if(device.isFullScreenSupported()/* && device.isDisplayChangeSupported()*/)
			{	fullScreen = true;
				final int width = dim.width;
				final int height = dim.height;
				currentMode = device.getDisplayMode();
				final int bitDepth = currentMode.getBitDepth();
				final int refreshRate = currentMode.getRefreshRate();
				DisplayMode modes[] = device.getDisplayModes();
				List<DisplayMode> modeList = Arrays.asList(modes);
				Collections.sort(modeList,new Comparator<DisplayMode>()
				{	@Override
					public int compare(DisplayMode arg0, DisplayMode arg1)
					{	int result;
						// width
						{	int w0 = arg0.getWidth();
							int w1 = arg1.getWidth();
							result = Math.abs(w0-width)-Math.abs(w1-width);
						}
						// height
						if(result==0)
						{	int h0 = arg0.getHeight();
							int h1 = arg1.getHeight();
							result = Math.abs(h0-height)-Math.abs(h1-height);
						}
						// refresh rate
						if(result==0)
						{	int rr0 = arg0.getRefreshRate();
							int rr1 = arg1.getRefreshRate();
							result = Math.abs(rr0-refreshRate)-Math.abs(rr1-refreshRate);
						}
						// bit depth
						if(result==0)
						{	int bd0 = arg0.getBitDepth();
							int bd1 = arg1.getBitDepth();
							result = Math.abs(bd0-bitDepth)-Math.abs(bd1-bitDepth);
						}
						return result;
					}
				});
				newMode = modeList.get(0);
				dim.setSize(newMode.getWidth(),newMode.getHeight());
			}
		}		
	}
	
	public void makeVisible()
	{	if(fullScreen)
		{	try
			{	setUndecorated(true);
				device.setFullScreenWindow(this);
				device.setDisplayMode(newMode);
//				setSize(newMode.getWidth(),newMode.getHeight());
				validate();
			}
			catch(Exception e)
			{	device.setFullScreenWindow(null);
				fullScreen = false;
				dispose();
				makeVisible();
			}
		}
		else
		{	setUndecorated(false);
			// size the frame
			setSize(dim);
			pack();
			// center the frame
		    Toolkit tk = Toolkit.getDefaultToolkit();
		    Dimension screenSize = tk.getScreenSize();
		    int screenHeight = screenSize.height;
		    int screenWidth = screenSize.width;
		    setLocation((screenWidth-getSize().width)/2,(screenHeight-getSize().height)/2);						
			// show the frame
			setVisible(true);
			toFront();
			validate();
			repaint();
		}
	}
	
	public void begin() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	AbstractTournament tournament = Configuration.getGameConfiguration().getTournament();
		Match match = tournament.getCurrentMatch();
	    Round round = match.getCurrentRound();
	    round.progress();
	}
	
	/////////////////////////////////////////////////////////////////
	// WINDOW LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	{	if(fullScreen)
			device.setDisplayMode(currentMode);
		System.exit(0);		
	}

	/////////////////////////////////////////////////////////////////
	// LOOP RENDERER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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

	/////////////////////////////////////////////////////////////////
	// ROUND RENDERER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
				Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
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
		Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		QuickResults roundResults = new QuickResults(dim);
		contentPane.add(roundResults,BorderLayout.NORTH);
		JButton exitButton = new JButton("OK");
		exitButton.addActionListener(this);
		getContentPane().add(exitButton, BorderLayout.SOUTH);		
//		contentPane.setLayout(layout);
		validate();
		repaint();
	}

	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e)
	{	exit();
	}
}

