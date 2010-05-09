package fr.free.totalboumboum.gui.menus.tournament;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;

import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;

public class TournamentData extends InnerDataPanel
{	private static final long serialVersionUID = 1L;
	
	public TournamentData(SplitMenuPanel container)
	{	super(container);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		// size
		setPreferredSize(new Dimension(300,300));
		// background
		setBackground(Color.YELLOW);
	}

	public void actionPerformed(ActionEvent e)
	{	/*System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(BUTTON_NEW))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_LOAD))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_EDIT))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_PLAYERS))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_START))
		{	
	    }
		else if(e.getActionCommand().equals(BUTTON_BACK))
		{	switchToPanel(tournamentMainPanel);
	    }*/
	}

	public void refresh()
	{	// NOTE à compléter
	}

	@Override
	public void updateData()
	{	// no use
	}
}
