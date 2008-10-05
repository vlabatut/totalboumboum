package fr.free.totalboumboum.gui.menus.tournament;

/*
 * New
 * 		cr�� une nouvelle partie � partir du mod�le courant
 * 		toujours possible, car il doit y avoir toujours un mod�le charg�
 * 
 * Load
 * 		charge une partie enregistr�e avec Save as
 * 		toujours possible
 * 
 * Save as
 * 		sauve une partie
 * 		a noter que le jeu enregistre automatiquement le partie courante au cours de son �volution :
 * 			si elle est dans un fichier sp�cifique (entregistrement Save as), ce fichier est mis � jour
 * 			dans tout les cas, le fichier de la partie par d�faut est toujours mis � jour
 * 		possible seulement si la partie est pr�te
 * -----------------------------------------
 * Rules
 * 		affiche et �dite le mod�le de tournoi
 * 		toujours possible, mais l'�dition n'est pas possible si la partie a commenc�
 * 		a noter que : 
 * 			le dernier mod�le utilis� est toujours charg� par d�faut
 * 			s'il s'agit d'une mod�le stock� dans un fichier sp�cifique, il est copi� � l'emplacement par d�faut
 * 
 * Players
 * 		permet de voir les joueurs qui vont participer au tournoi
 * 		tourjours possible, mais ne sera pas �ditable si la partie a d�j� commenc�
 * -----------------------------------------
 * Play
 * 		Start si la partie n'est pas encore commenc�e
 * 		Continue si la partie a d�j� commenc�
 * 		possible seulement si la partie est pr�te
 * -----------------------------------------
 * Back
 * 		revient au menu principal
 */



import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.ContentPanel;
import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

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
		
		// size
		int height = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_MENU_PANEL_HEIGHT);
		int width = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_MENU_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		// buttons
		add(Box.createVerticalGlue());
		buttonNew = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_NEW,this,getConfiguration());
		buttonLoad = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_LOAD,this,getConfiguration());
		buttonSaveAs = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_SAVE_AS,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonRules = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_RULES,this,getConfiguration());
		buttonPlayers = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_PLAYERS,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonPlay = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_START,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,GuiTools.getSize(GuiTools.MENU_VERTICAL_BUTTON_SPACE))));
		buttonBack = GuiTools.createSecondaryVerticalMenuButton(GuiTools.MENU_TOURNAMENT_BUTTON_BACK,this,getConfiguration());
		add(Box.createVerticalGlue());
		
		// panels
		tournamentData = new TournamentData(container);
		container.setDataPart(tournamentData);
		dataPart = tournamentData;
	}
	
	public void actionPerformed(ActionEvent e)
	{	System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(GuiTools.MENU_TOURNAMENT_BUTTON_NEW))
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
			AbstractTournament tournament = getConfiguration().getCurrentTournament();
			try
			{	tournament.init();
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
			buttonPlay.setEnabled(true);
			if(tournament.hasBegun())
			{	GuiTools.setButtonContent(GuiTools.MENU_TOURNAMENT_BUTTON_CONTINUE,buttonPlay,getConfiguration());
				buttonPlayers.setEnabled(false);
				buttonRules.setEnabled(false);
			}
			else
			{	GuiTools.setButtonContent(GuiTools.MENU_TOURNAMENT_BUTTON_START,buttonPlay,getConfiguration());
				buttonPlayers.setEnabled(true);
				buttonRules.setEnabled(true);
			}
		}
	}

}
