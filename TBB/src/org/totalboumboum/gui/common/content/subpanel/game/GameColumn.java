package org.totalboumboum.gui.common.content.subpanel.game;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostState;

/**
 * 
 * @author Vincent Labatut
 *
 */
public enum GameColumn
{	BUTTON,
	PREFERRED,
	HOST_NAME,
	HOST_IP,
	TOURNAMENT_TYPE,
	PLAYER_COUNT,
	ALLOWED_PLAYER,
	AVERAGE_LEVEL,
	TOURNAMENT_STATE,
	PLAYED;
	
	public boolean isInverted()
	{	boolean result = false;			

		if(this==BUTTON)
			result = false;
		else if(this==PREFERRED)
			result = false;
		else if(this==HOST_NAME)
			result = false;
		else if(this==HOST_IP)
			result = false;
		else if(this==TOURNAMENT_TYPE)
			result = false;
		else if(this==PLAYER_COUNT)
			result = true;
		else if(this==ALLOWED_PLAYER)
			result = true;
		else if(this==AVERAGE_LEVEL)
			result = true;
		else if(this==TOURNAMENT_STATE)
			result = true;
		else if(this==PLAYED)
			result = true;

		return result;
	}
	
	public String getHeaderKey()
	{	String result = null;			

		if(this==BUTTON)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_BUTTON;
		else if(this==PREFERRED)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_PREFERRED;
		else if(this==HOST_NAME)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_HOST_NAME;
		else if(this==HOST_IP)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_HOST_IP;
		else if(this==TOURNAMENT_TYPE)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_TOURNAMENT_TYPE;
		else if(this==PLAYER_COUNT)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_PLAYER_COUNT;
		else if(this==ALLOWED_PLAYER)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_ALLOWED_PLAYER;
		else if(this==AVERAGE_LEVEL)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_AVERAGE_LEVEL;
		else if(this==TOURNAMENT_STATE)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_TOURNAMENT_STATE;
		else if(this==PLAYED)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_PLAYED;

		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public Comparable getDataValue(GameInfo gameInfo)
	{	Comparable result = null;			

		if(this==BUTTON)
		{	result = 0;
		}
		else if(this==PREFERRED)
		{	result = gameInfo.getHostInfo().isPreferred();
		}
		else if(this==HOST_NAME)
		{	result = gameInfo.getHostInfo().getName();
		}
		else if(this==HOST_IP)
		{	result = gameInfo.getHostInfo().getLastIp();
		}
		else if(this==TOURNAMENT_TYPE)
		{	result = gameInfo.getTournamentType();
		}
		else if(this==PLAYER_COUNT)
		{	result = gameInfo.getPlayerCount();
		}
		else if(this==ALLOWED_PLAYER)
		{	final class Temp extends TreeSet<Integer> implements Comparable<TreeSet<Integer>>
			{	private static final long serialVersionUID = 1L;
				@Override
				public int compareTo(TreeSet<Integer> s)
				{	int result = 0;
					Iterator<Integer> it1 = this.iterator();
					Iterator<Integer> it2 = s.iterator();
					while(result==0 && it1.hasNext() && it2.hasNext())
					{	Integer i1 = it1.next();
						Integer i2 = it2.next();
						result = i1 - i2;
					}
					if(result==0)
					{	if(it1.hasNext() && !it2.hasNext())
							result = 1;
						else if(!it1.hasNext() && it2.hasNext())
							result = -1;
					}			
					return result;
				}
			}
			Temp allowedPlayer = new Temp();
			allowedPlayer.addAll(gameInfo.getAllowedPlayers());
			result = allowedPlayer;
		}
		else if(this==AVERAGE_LEVEL)
		{	result = gameInfo.getAverageScore();
		}
		else if(this==TOURNAMENT_STATE)
		{	result = gameInfo.getHostInfo().getState();
		}
		else if(this==PLAYED)
		{	result = gameInfo.getHostInfo().getUses();
		}

		return result;
	}
	
	public void setLabelContent(GameListSubPanel container, TableSubPanel panel, int colWidths[], int line, int col, GameInfo gameInfo)
	{	if(this==BUTTON)
		{	// content
			String key;
			if(gameInfo==null)
				key = GuiKeys.COMMON_GAME_LIST_BUTTON_ADD;
			else
				key = GuiKeys.COMMON_GAME_LIST_BUTTON_REMOVE;
			panel.setLabelKey(line,col,key,true);
			// listener
			MyLabel label = panel.getLabel(line,col);
			label.removeMouseListener(container);
			label.addMouseListener(container);
			label.setMouseSensitive(true);
		}
		else if(this==PREFERRED)
		{	if(gameInfo!=null)
			{	// content
				String key;
				if(gameInfo.getHostInfo().isPreferred())
					key = GuiKeys.COMMON_GAME_LIST_DATA_FAV_PREFERRED;
				else
					key = GuiKeys.COMMON_GAME_LIST_DATA_FAV_REGULAR;
				panel.setLabelKey(line,col,key,true);
				// listener
				MyLabel label = panel.getLabel(line,col);
				label.removeMouseListener(container);
				label.addMouseListener(container);
				label.setMouseSensitive(true);
			}
		}
		else if(this==HOST_NAME)
		{	if(gameInfo!=null)
			{	// content
				String name = gameInfo.getHostInfo().getName();
				String text = "?";
				if(name!=null)
					text = name;
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				// listener
				MyLabel label = panel.getLabel(line,col);
				label.removeMouseListener(container);
				label.addMouseListener(container);
				label.setMouseSensitive(true);
				// column width
				int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==HOST_IP)
		{	if(gameInfo!=null)
			{	// content
				String ip = gameInfo.getHostInfo().getLastIp();
				String text = "?";
				if(ip!=null)
					text = ip;
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				// listener
//				MyLabel label = panel.getLabel(line,col);
//				label.removeMouseListener(container);
//				label.addMouseListener(container);
//				label.setMouseSensitive(true);
				// column width
				int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==TOURNAMENT_TYPE)
		{	if(gameInfo!=null)
			{	TournamentType type = gameInfo.getTournamentType();
				if(type!=null)
				{	String key = null;
					if(type.equals(TournamentType.CUP))
						key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_CUP;
					else if(type.equals(TournamentType.LEAGUE))
						key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_LEAGUE;
					else if(type.equals(TournamentType.SEQUENCE))
						key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_SEQUENCE;
					else if(type.equals(TournamentType.SINGLE))
						key = GuiKeys.COMMON_GAME_LIST_DATA_TYPE_SINGLE;
					if(key!=null)
						panel.setLabelKey(line,col,key,true);
				}
				else
				{	String text = "?";
					String tooltip = text;
					panel.setLabelText(line,col,text,tooltip);
				}
			}
		}
		else if(this==PLAYER_COUNT)
		{	if(gameInfo!=null)
			{	// content
				Integer value = gameInfo.getPlayerCount();
				String text = "?";
				if(value!=null)
					text = Integer.toString(value);
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				// column width
				int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==ALLOWED_PLAYER)
		{	if(gameInfo!=null)
			{	// content
				Set<Integer> value = gameInfo.getAllowedPlayers();
				String text = "?";
				if(value!=null)
					text = Players.formatAllowedPlayerNumbers(value);
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				// column width
				int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==AVERAGE_LEVEL)
		{	if(gameInfo!=null)
			{	// content
				Double mean = gameInfo.getAverageScore();
				String text = "?";
				String tooltip = text;
				if(mean!=null)
				{	NumberFormat nfText = NumberFormat.getInstance();
					nfText.setMaximumFractionDigits(0);
					text = nfText.format(mean);
					NumberFormat nfTooltip = NumberFormat.getInstance();
					nfTooltip.setMaximumFractionDigits(6);
					tooltip = nfTooltip.format(mean);
				}
				panel.setLabelText(line,col,text,tooltip);
				// column width
				int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
		else if(this==TOURNAMENT_STATE)
		{	// listener
			MyLabel label = panel.getLabel(line,col);
			label.removeMouseListener(container);
			label.setMouseSensitive(false);
			// content
			if(gameInfo!=null)
			{	HostState state = gameInfo.getHostInfo().getState();
				String key = null;
				if(state.equals(HostState.CLOSED))
					key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_CLOSED;
				else if(state.equals(HostState.FINISHED))
					key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_FINISHED;
				else if(state.equals(HostState.OPEN))
					key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_OPEN;
				else if(state.equals(HostState.PLAYING))
					key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_PLAYING;
				else if(state.equals(HostState.RETRIEVING))
					key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_RETRIEVING;
				else if(state.equals(HostState.UNKOWN))
				{	key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_UNKNOWN;
					label.addMouseListener(container);
					label.setMouseSensitive(true);
				}
				if(key!=null)
					panel.setLabelKey(line,col,key,true);
			}
		}
		else if(this==PLAYED)
		{	if(gameInfo!=null)
			{	// content
				Integer played = gameInfo.getHostInfo().getUses();
				String text = "?";
				if(played!=null)
					text = Integer.toString(played);
				String tooltip = text;
				panel.setLabelText(line,col,text,tooltip);
				// column width
				int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
				if(temp>colWidths[col])
					colWidths[col] = temp;
			}
		}
	}
}	
