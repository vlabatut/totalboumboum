package fr.free.totalboumboum.gui.menus.tournament;

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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.match.MatchSplitPanel;
import fr.free.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

/**
 * Description :
 * New
 * 		créé une nouvelle partie à partir du modèle courant
 * 		toujours possible, car il doit y avoir toujours un modèle chargé
 * 
 * Load
 * 		charge une partie enregistrée avec Save as
 * 		toujours possible
 * 
 * Save as
 * 		sauve une partie
 * 		a noter que le jeu enregistre automatiquement le partie courante au cours de son évolution :
 * 			si elle est dans un fichier spécifique (entregistrement Save as), ce fichier est mis à jour
 * 			dans tout les cas, le fichier de la partie par défaut est toujours mis à jour
 * 		possible seulement si la partie est prête
 * -----------------------------------------
 * Rules
 * 		affiche et édite le modèle de tournoi
 * 		toujours possible, mais l'édition n'est pas possible si la partie a commencé
 * 		a noter que : 
 * 			le dernier modèle utilisé est toujours chargé par défaut
 * 			s'il s'agit d'une modèle stocké dans un fichier spécifique, il est copié à l'emplacement par défaut
 * 
 * Players
 * 		permet de voir les joueurs qui vont participer au tournoi
 * 		tourjours possible, mais ne sera pas éditable si la partie a déjà commencé
 * -----------------------------------------
 * Play
 * 		Start si la partie n'est pas encore commencée
 * 		Continue si la partie a déjà commencé
 * 		possible seulement si la partie est prête
 * -----------------------------------------
 * Back
 * 		revient au menu principal
 */

public class TournamentMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private MenuPanel tournamentGamePanel;
	private InnerDataPanel tournamentData;
	
	private JButton buttonNew; 
	private JButton buttonLoad;
	private JButton buttonSaveAs;
	private JButton buttonRules;
	private JButton buttonPlayers;
	private JButton buttonPlay;
	private JButton buttonBack;
	
	public TournamentMenu(SplitMenuPanel container, MenuPanel parent) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	super(container,parent);
	
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// buttons
		add(Box.createVerticalGlue());
		buttonNew = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_NEW,this,getConfiguration());
buttonNew.setEnabled(false);
		buttonLoad = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_LOAD,this,getConfiguration());
buttonLoad.setEnabled(false);
		buttonSaveAs = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_SAVE_AS,this,getConfiguration());
buttonSaveAs.setEnabled(false);
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonRules = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_RULES,this,getConfiguration());
buttonSaveAs.setEnabled(false);
		buttonPlayers = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_PLAYERS,this,getConfiguration());
buttonPlayers.setEnabled(false);
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonPlay = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_START,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonBack = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_BACK,this,getConfiguration());
		add(Box.createVerticalGlue());
		
		// panels
		tournamentData = new TournamentData(container);
		container.setDataPart(tournamentData);
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_NEW))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_LOAD))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_SAVE_AS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_RULES))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_PLAYERS))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_START))
		{	//NOTE c icite qu'il faut tester que les profils sont OK
			try
			{	AbstractTournament tournament = getConfiguration().getCurrentTournament();
				tournament.init();
				if(tournament instanceof SingleTournament)
				{	tournament.progress();
					tournamentGamePanel = new MatchSplitPanel(container.getContainer(),container);					
				}
				else
					tournamentGamePanel = new TournamentSplitPanel(container.getContainer(),container);
			}
			catch (IllegalArgumentException e1)
			{	e1.printStackTrace();
			}
			catch (SecurityException e1)
			{	e1.printStackTrace();
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			catch (IllegalAccessException e1)
			{	e1.printStackTrace();
			}
			catch (NoSuchFieldException e1)
			{	e1.printStackTrace();
			}
			catch (ClassNotFoundException e1)
			{	e1.printStackTrace();
			}
			replaceWith(tournamentGamePanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_CONTINUE))
		{	replaceWith(tournamentGamePanel);
	    }
		else if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 

	public void refresh()
	{	AbstractTournament tournament = getConfiguration().getCurrentTournament();
	
		// New: always possible
		// Load: always possible
		// Rules: always possible
		// Players: always possible
		// Back: always possible

		// Save as: tournament must be ready
		// Play: tournament must be ready
		if(tournament.isReady())
		{	buttonSaveAs.setEnabled(true);
buttonSaveAs.setEnabled(false);		
			buttonPlay.setEnabled(true);
			if(tournament.hasBegun())
			{	GuiTools.setButtonContent(GuiTools.MENU_TOURNAMENT_BUTTON_CONTINUE,buttonPlay,getConfiguration());
				buttonPlayers.setEnabled(false);
				buttonRules.setEnabled(false);
			}
			else
			{	GuiTools.setButtonContent(GuiTools.MENU_TOURNAMENT_BUTTON_START,buttonPlay,getConfiguration());
				buttonPlayers.setEnabled(true);
buttonPlayers.setEnabled(false);		
				buttonRules.setEnabled(true);
buttonRules.setEnabled(false);		
			}
		}
	}

}
