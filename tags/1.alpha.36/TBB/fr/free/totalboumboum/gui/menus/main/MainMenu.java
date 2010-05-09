package fr.free.totalboumboum.gui.menus.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.tools.GuiTools;

public class MainMenu extends SimpleMenuPanel
{	private static final long serialVersionUID = 1L;
	
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
		setBackground(Color.GRAY);
		
		// panels
		tournamentMainPanel = new TournamentMain(getContainer(),this);
		optionsMenuPanel = new OptionsMenu(getContainer(),this);
		
		// buttons
		add(Box.createVerticalGlue());
		buttonOptions = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_OPTIONS,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,getConfiguration().getVerticalMenuButtonSpace())));
		buttonProfiles = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_PROFILES,this,getConfiguration());
		buttonStats = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_STATISTICS,this,getConfiguration());
		buttonHeroes = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_HEROES,this,getConfiguration());
		buttonLevels = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_LEVELS,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,getConfiguration().getVerticalMenuButtonSpace())));
		buttonTournament = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_TOURNAMENT,this,getConfiguration());
		buttonQuickMatch = GuiTools.createVerticalMenuButton(GuiTools.MAIN_MENU_BUTTON_QUICKMATCH,this,getConfiguration());
		add(Box.createVerticalGlue());		
	}
	
	public void actionPerformed(ActionEvent e)
	{	System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_OPTIONS))
		{	replaceWith(optionsMenuPanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_PROFILES))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_STATISTICS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_HEROES))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_LEVELS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_TOURNAMENT))
		{	replaceWith(tournamentMainPanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.MAIN_MENU_BUTTON_QUICKMATCH))
		{	
	    }
	}
	
	public void refresh()
	{	// nothing to do here
	}
}
