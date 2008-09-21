package fr.free.totalboumboum.gui.menus.tournament;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;

import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TournamentData extends InnerDataPanel
{	private static final long serialVersionUID = 1L;
	
	public TournamentData(SplitMenuPanel container)
	{	super(container);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		// size
		int height = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = GuiTools.getSize(GuiTools.VERTICAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
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
