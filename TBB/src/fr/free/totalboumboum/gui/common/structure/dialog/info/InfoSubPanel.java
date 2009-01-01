package fr.free.totalboumboum.gui.common.structure.dialog.info;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.game.tournament.league.LeagueTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.StringTools;

public class InfoSubPanel extends EntitledSubPanel
{	private static final long serialVersionUID = 1L;
	
	public InfoSubPanel(int width, int height, String title, String tooltip, ArrayList<String> text)
	{	super(width,height);
		
		setTitleText(title,tooltip);
	
		setText(text);
	}
		
	/////////////////////////////////////////////////////////////////
	// TEXT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setText(ArrayList<String> text)
	{	
		for(String txt: text)
		{	JLabel label = new JLabel();
			label.setText(txt);
			label.setFont(lineFont);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label);
			
		}
		
		
		
		
		
		// sizes
		int lines =;
		int colSubs = 2;
		int colGroups = 1;
		reinit(colGroups,colSubs,lines);
		
		// icons
		ArrayList<String> keys = new ArrayList<String>();
		allowedPlayersLine = 0;
		if(showName)
		{	keys.add(GuiKeys.COMMON_TOURNAMENT_NAME);
			allowedPlayersLine++;
		}
		if(showAuthor)
		{	keys.add(GuiKeys.COMMON_TOURNAMENT_AUTHOR);
			allowedPlayersLine++;
		}
		if(showType)
		{	keys.add(GuiKeys.COMMON_TOURNAMENT_TYPE);
			allowedPlayersLine++;
		}
		if(showAllowedPlayerNumbers)
			keys.add(GuiKeys.COMMON_TOURNAMENT_ALLOWED_PLAYERS);
		
		if(tournament!=null)
		{	// text
			ArrayList<String> textValues = new ArrayList<String>();
			ArrayList<String> tooltipValues = new ArrayList<String>();
			if(showName)
			{	textValues.add(tournament.getName());
				tooltipValues.add(tournament.getName());
			}
			if(showAuthor)
			{	textValues.add(tournament.getAuthor());
				tooltipValues.add(tournament.getAuthor());
			}
			if(showType)
			{	String key = "";
				if(tournament instanceof CupTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_CUP;
				else if(tournament instanceof LeagueTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_LEAGUE;
				else if(tournament instanceof SequenceTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_SEQUENCE;
				else if(tournament instanceof SingleTournament)
					key = GuiKeys.COMMON_TOURNAMENT_TYPES_SINGLE;
				textValues.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(key));
				tooltipValues.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP));
			}
			if(showAllowedPlayerNumbers)
			{	Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
				textValues.add(StringTools.formatAllowedPlayerNumbers(allowedPlayers));
				tooltipValues.add(StringTools.formatAllowedPlayerNumbers(allowedPlayers));
			}
			
			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = textValues.get(line);
					String tooltip = tooltipValues.get(line);
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(line>0)
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		else
		{	for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line>0)
						bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		
		int maxWidth = width-3*GuiTools.subPanelMargin-getHeaderHeight();
		setColSubMaxWidth(1,maxWidth);
		setColSubPreferredWidth(1,maxWidth);
	}
	
	public void selectAllowedPlayers(boolean flag)
	{	Color hbg,dbg;
		if(showAllowedPlayerNumbers && flag)
		{	hbg = GuiTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND;
			dbg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
		}
		else
		{	hbg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
			dbg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
		}
		setLabelBackground(allowedPlayersLine,0,hbg);
		setLabelBackground(allowedPlayersLine,1,dbg);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showName = true;
	private boolean showAuthor = true;
	private boolean showType = true;
	private boolean showAllowedPlayerNumbers = true;

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
	}

	public void setShowAllowedPlayerNumbers(boolean showAllowedPlayerNumbers)
	{	this.showAllowedPlayerNumbers = showAllowedPlayerNumbers;
	}

}
