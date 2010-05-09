package fr.free.totalboumboum.gui.menus.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class MainMenu extends SimpleMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

	private MenuPanel tournamentMainPanel;
	private MenuPanel optionsMenuPanel;
	
	private JButton buttonOptions;
	private JButton buttonProfiles;
	private JButton buttonStats;
	private JButton buttonHeroes;
	private JButton buttonLevels;
	private JButton buttonTournament;
	private JButton buttonQuickMatch;
	
	public MainMenu(MenuContainer container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container, parent);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		// size
		setPreferredSize(getConfiguration().getPanelDimension());
		// background
		image = getConfiguration().getBackground();
		// panels
		tournamentMainPanel = new TournamentMain(getContainer(),this);
		optionsMenuPanel = new OptionsMenu(getContainer(),this);
		
		// buttons
		add(Box.createVerticalGlue());
		buttonOptions = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_MAIN_BUTTON_OPTIONS,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonProfiles = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_MAIN_BUTTON_PROFILES,this,getConfiguration());
		buttonStats = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_MAIN_BUTTON_STATISTICS,this,getConfiguration());
		buttonHeroes = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_MAIN_BUTTON_HEROES,this,getConfiguration());
		buttonLevels = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_MAIN_BUTTON_LEVELS,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonTournament = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_TOURNAMENT,this,getConfiguration());
		buttonQuickMatch = GuiTools.createPrincipalVerticalMenuButton(GuiTools.MENU_MAIN_BUTTON_QUICKMATCH,this,getConfiguration());
		add(Box.createVerticalGlue());		
	}
	
	@Override
	public void paintComponent(Graphics g)
	{	g.drawImage(image, 0, 0, null);
	}
	
	public void actionPerformed(ActionEvent e)
	{	System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(GuiTools.MENU_MAIN_BUTTON_OPTIONS))
		{	replaceWith(optionsMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_MAIN_BUTTON_PROFILES))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_MAIN_BUTTON_STATISTICS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_MAIN_BUTTON_HEROES))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_MAIN_BUTTON_LEVELS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_TOURNAMENT))
		{	replaceWith(tournamentMainPanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_MAIN_BUTTON_QUICKMATCH))
		{	
	    }
	}
	
	public void refresh()
	{	// nothing to do here
	}
}
