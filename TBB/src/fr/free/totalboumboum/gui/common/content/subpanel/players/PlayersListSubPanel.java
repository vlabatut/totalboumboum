package fr.free.totalboumboum.gui.common.content.subpanel.players;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiStringTools;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;

public class PlayersListSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 16+1;
	private static final int COLS = 4;
	
	public PlayersListSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,COLS,true);
		
		setPlayers(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Profile> players;
	ArrayList<String> controlTexts;
	ArrayList<String> controlTooltips;

	public ArrayList<Profile> getPlayers()
	{	return players;	
	}
	
	public void setPlayers(ArrayList<Profile> players)
	{	if(players==null)
			players = new ArrayList<Profile>();
		this.players = players;

		// sizes
		int cols = COLS;
		if(showControls)
			cols++;

		reinit(LINES,cols);
		
		// col widths
		int iconWidth = getHeaderHeight();		
		controlTexts = new ArrayList<String>();
		controlTooltips = new ArrayList<String>();
		int controlWidth = GuiStringTools.initControlsTexts(getLineFontSize(),controlTexts,controlTooltips);
		int rankWidth = iconWidth;

		// headers
		{	int col = 0;
			// hero
			{	String key = GuiKeys.COMMON_PLAYERS_LIST_HEADER_HERO;
				setLabelKey(0,col,key,true);
				col++;
			}
			// profile
			{	String key = GuiKeys.COMMON_PLAYERS_LIST_HEADER_PROFILE;
				setLabelKey(0,col,key,true);
				col++;
			}
			// controls
			if(showControls)
			{	String key = GuiKeys.COMMON_PLAYERS_LIST_HEADER_CONTROLS;
				setLabelKey(0,col,key,true);
				col++;
			}			
			// name
			{	String key = GuiKeys.COMMON_PLAYERS_LIST_HEADER_NAME;
				setLabelKey(0,col,key,true);
				col++;
			}
			// rank
			{	String key = GuiKeys.COMMON_PLAYERS_LIST_HEADER_RANK;
				setLabelKey(0,col,key,true);
				col++;
			}
		}
		
		// data
		{	RankingService rankingService = GameStatistics.getRankingService();
			Iterator<Profile> i = players.iterator();
			int line = 1;
			while(i.hasNext())
			{	int col = 0;
				Profile profile = i.next();
				// color
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				setLineBackground(line,bg);
				// portrait
				{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getSpriteName();
					setLabelIcon(line,col,image,tooltip);
					col++;
				}
				// profile type
				{	String aiName = profile.getAiName();
					String key;
					if(aiName==null)
						key = GuiKeys.COMMON_PLAYERS_LIST_DATA_HUMAN;
					else
						key = GuiKeys.COMMON_PLAYERS_LIST_DATA_COMPUTER;
					setLabelKey(line,col,key,true);
					col++;
				}
				// controls
				if(showControls)
				{	int index = profile.getControlSettingsIndex();
					setLabelText(line,col,controlTexts.get(index),controlTooltips.get(index));
					JLabel label = getLabel(line,col);
					label.addMouseListener(this);
					col++;
				}
				// name
				{	String text = profile.getName();
					String tooltip = profile.getName();
					setLabelText(line,col,text,tooltip);
					col++;
				}
				// rank
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(0);
					int playerId = profile.getId();
					int playerRank = rankingService.getPlayerRank(playerId);
					String text,tooltip;
					if(playerRank<0)
					{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_NO_RANK;
						text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
						tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
					}
					else
					{	text = Integer.toString(playerRank);
						tooltip = text;
					}
					int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
					if(temp>rankWidth)
						rankWidth = temp;
					setLabelText(line,col,text,tooltip);
					col++;
				}
				//
				line++;
			}
		}
		
		// widths
		{	int nameWidth = getDataWidth() - (cols-1)*GuiTools.subPanelMargin - 2*iconWidth - rankWidth;
			if(showControls)
				nameWidth = nameWidth - controlWidth;
			int col = 0;
			// hero
			{	setColSubMinWidth(col,iconWidth);
				setColSubPrefWidth(col,iconWidth);
				setColSubMaxWidth(col,iconWidth);
				col++;
			}
			// profile
			{	setColSubMinWidth(col,iconWidth);
				setColSubPrefWidth(col,iconWidth);
				setColSubMaxWidth(col,iconWidth);
				col++;
			}
			// controls
			if(showControls)
			{	setColSubMinWidth(col,controlWidth);
				setColSubPrefWidth(col,controlWidth);
				setColSubMaxWidth(col,controlWidth);
				col++;
			}			
			// name
			{	setColSubMinWidth(col,nameWidth);
				setColSubPrefWidth(col,nameWidth);
				setColSubMaxWidth(col,nameWidth);
				col++;
			}
			// rank
			{	setColSubMinWidth(col,rankWidth);
				setColSubPrefWidth(col,rankWidth);
				setColSubMaxWidth(col,rankWidth);
				col++;
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showControls = true;
	
	public void setShowControls(boolean showControls)
	{	this.showControls = showControls;
		setPlayers(players);
	}

	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	Component component = e.getComponent();
		GuiTools.changeColorMouseEntered(component);
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	Component component = e.getComponent();
		GuiTools.changeColorMouseExited(component);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPositionSimple(label);
		// controls
		if(pos[1]==2)
		{	Profile profile = players.get(pos[0]-1);
			int index = profile.getControlSettingsIndex();
			if(profile.hasAi())
			{	if(index==GameData.CONTROL_COUNT)
					index = 0;
				else
					index = Configuration.getProfilesConfiguration().getNextFreeControls(players,index);
			}
			else
				index = Configuration.getProfilesConfiguration().getNextFreeControls(players,index);
			profile.setControlSettingsIndex(index);
			setLabelText(pos[0],pos[1],controlTexts.get(index),controlTooltips.get(index));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

}
