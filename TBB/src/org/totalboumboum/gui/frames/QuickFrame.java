package org.totalboumboum.gui.frames;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.engine.loop.RegularLoop;
import org.totalboumboum.engine.loop.LoopRenderPanel;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundRenderPanel;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.game.round.results.QuickResults;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Main GUI class for the quickmode
 * (simplified interface).
 * 
 * @author Vincent Labatut
 */
public class QuickFrame extends AbstractFrame implements ActionListener, LoopRenderPanel, RoundRenderPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new frame.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the game data.
	 * @throws SecurityException
	 * 		Problem while accessing the game data.
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the game data.
	 * @throws SAXException
	 * 		Problem while accessing the game data.
	 * @throws IOException
	 * 		Problem while accessing the game data.
	 * @throws IllegalAccessException
	 * 		Problem while accessing the game data.
	 * @throws NoSuchFieldException
	 * 		Problem while accessing the game data.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the game data.
	 */
	public QuickFrame() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// init
		super("TBB v."+GameData.VERSION+" (Quicklaunch)");

		// panel size
		Dimension contentDimension = Configuration.getVideoConfiguration().getPanelDimension();
		
	    // create canvas
	    canvas = new Canvas();
	    canvas.setIgnoreRepaint(true);
//	    canvas.setSize(dim);
	    canvas.setMinimumSize(contentDimension);
	    canvas.setPreferredSize(contentDimension);
	    canvas.setMaximumSize(contentDimension);
	    add(canvas);
		
		// create progress bar
		remove(canvas);
		loadProgressBar = new JProgressBar();
//		loadProgressBar.setSize(dim);
		loadProgressBar.setMinimumSize(contentDimension);
		loadProgressBar.setPreferredSize(contentDimension);
		loadProgressBar.setMaximumSize(contentDimension);
		loadProgressBar.setStringPainted(true);
		loadProgressBar.setFont(GuiConfiguration.getMiscConfiguration().getFont().deriveFont(contentDimension.height/2f));
		getContentPane().add(loadProgressBar);
		
		// tournament
		QuickStartConfiguration quickStartConfiguration = Configuration.getGameConfiguration().getQuickStartConfiguration(); 
		tournament = quickStartConfiguration.loadQuickstart();
		List<Profile> selectedProfiles = new ArrayList<Profile>();
		ProfilesSelection profilesSelection = quickStartConfiguration.getProfilesSelection();
		selectedProfiles = ProfileLoader.loadProfiles(profilesSelection);
		tournament.setProfiles(selectedProfiles);
		tournament.init();
	    tournament.progress();
	    
	    // match
	    Match match = tournament.getCurrentMatch();
	    match.progress();
		       
	    //round
	    round = match.getCurrentRound();
	    round.setPanel(this);
		int limit = round.getProfiles().size()+3;
		loadProgressBar.setMinimum(0);
		loadProgressBar.setMaximum(limit);		
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Starts the game.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the game data.
	 * @throws SecurityException
	 * 		Problem while accessing the game data.
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the game data.
	 * @throws SAXException
	 * 		Problem while accessing the game data.
	 * @throws IOException
	 * 		Problem while accessing the game data.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the game data.
	 * @throws IllegalAccessException
	 * 		Problem while accessing the game data.
	 * @throws NoSuchFieldException
	 * 		Problem while accessing the game data.
	 */
	public void begin() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	Match match = tournament.getCurrentMatch();
	    Round round = match.getCurrentRound();
	    round.progress();
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP RENDERER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Component used for drawing */
	private Canvas canvas;
	/** Progress bar updated during loading */
	private JProgressBar loadProgressBar;
	/** Option for painting */
	private BufferStrategy bufferStrategy;
	/** Game main loop */
	private RegularLoop loop;

	@Override
	public void loopOver()
	{	//
	}

	@Override
	public void paintScreen()
	{	// retrieve the graphical object
		Graphics g = bufferStrategy.getDrawGraphics();
		
		//possibly perform screen capture
		boolean sc = loop.mustScreenCapture();
		if(sc)
			captureScreen();
		
		// draw stuff in the buffer
		//NOTE: draw background?	
		loop.draw(g);
		g.dispose();
		
		// copy the buffer on the canvas
		bufferStrategy.show();
		
		//Tell the System to do the drawing now
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void playerOut(int index)
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	//
			}
		});	
	}

	@Override
	public void captureScreen()
	{	// re-draw the game panel, this time in a new image
		BufferedImage copy = new BufferedImage(canvas.getWidth(),canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = copy.getGraphics();
		loop.draw(g);
		
		// get current date and time
		Calendar cal = new GregorianCalendar();
		Date currentTime = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
		String currentStr = sdf.format(currentTime);
		
		// set up file name
		String path = FilePaths.getScreenCapturePath() + File.separator + currentStr + FileNames.EXTENSION_BMP;
		File file = new File(path);
		
		// record image as bmp
		try
		{	ImageIO.write(copy, "bmp", file);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND RENDERER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current tournament */
	private AbstractTournament tournament;
	/** Current round */
	private Round round;

	@Override
	public void loadStepOver()
	{	int val = loadProgressBar.getValue();
		loadProgressBar.setValue(val+1);
		switch(val)
		{	// firesetmap
			case 0:
				loadProgressBar.repaint();
				break;
			// itemset
			case 1:
				loadProgressBar.repaint();
				break;
			// theme
			case 2:
				loadProgressBar.repaint();
				break;
			// players
			default:
				Round round = tournament.getCurrentMatch().getCurrentRound();
				if(val==round.getProfiles().size()+3)
				{	// progress bar
					loadProgressBar.repaint();
					getContentPane().remove(loadProgressBar);
				    add(canvas);
					validate();
					repaint();
				    
					// loop
			        requestFocus();					
				    loop = (RegularLoop)round.getLoop();
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
	public void simulationStepOver()
	{	//useless here
	}

	@Override
	public void roundOver()
	{	remove(canvas);
		Container contentPane = getContentPane();
		Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		QuickResults roundResults = new QuickResults(dim,round);
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
	{	exit(true);
	}

	/////////////////////////////////////////////////////////////////
	// READY-SET-GO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getMessageTextGo()
	{	String result = null;
		return result;
	}

	@Override
	public String getMessageTextReady() 
	{	String result = null;
		return result;
	}

	@Override
	public String getMessageTextSet()
	{	String result = null;
		return result;
	}

	@Override
	public Font getMessageFont(double width, double height)
	{	Font result = GuiConfiguration.getMiscConfiguration().getFont();
		float fontSize = (float)height/4;
		result = result.deriveFont(fontSize);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// WINDOW LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void windowClosing(WindowEvent e)
	{	exit(true);
	}
}
