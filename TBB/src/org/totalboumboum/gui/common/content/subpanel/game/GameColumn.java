package org.totalboumboum.gui.common.content.subpanel.game;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.totalboumboum.configuration.profile.Portraits;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.players.Players;
import org.totalboumboum.game.network.game.GameInfo;
import org.totalboumboum.game.network.host.HostState;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

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
			result = null;
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
		else if(this==TOURNAMENT_STATE)
			result = GuiKeys.COMMON_GAME_LIST_HEADER_PLAYED;

		return result;
	}
	
	public void setLabelContent(GameListSubPanel container, TableSubPanel panel, int colWidths[], int line, int col, GameInfo gameInfo)
	{	if(this==GENERAL_BUTTON)
		{	String key;
			if(playerRating==null)
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER;
			else
				key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER;
			panel.setLabelKey(line,col,key,true);
			MyLabel label = panel.getLabel(line,col);
			label.addMouseListener(container);
			label.setMouseSensitive(true);
		}
//TODO 	BUTTON
	
		else if(this==PREFERRED)
		{	String key;
			if(gameInfo.getHostInfo().isPreferred())
				key = GuiKeys.COMMON_GAME_LIST_DATA_FAV_PREFERRED;
			else
				key = GuiKeys.COMMON_GAME_LIST_DATA_FAV_REGULAR;
			if(key!=null)
			{	panel.setLabelKey(line,col,key,true);
				MyLabel label = panel.getLabel(line,col);
				label.addMouseListener(container);
				label.setMouseSensitive(true);
			}
		}
		else if(this==HOST_NAME)
		{	// content
			String name = gameInfo.getHostInfo().getName();
			String text = "?";
			if(name!=null)
				text = name;
			String tooltip = text;
			panel.setLabelText(line,col,text,tooltip);
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==HOST_IP)
		{	// content
			InetAddress ip = gameInfo.getHostInfo().getLastIp();
			String text = "?";
			if(ip!=null)
				text = ip.getHostName();
			String tooltip = text;
			panel.setLabelText(line,col,text,tooltip);
//			MyLabel label = panel.getLabel(line,col);
//			label.addMouseListener(container);
//			label.setMouseSensitive(true);
// TODO better doing it in the specific subpanel
// same thing with other stuff allowing to edit a new direct connection (?)
// or take it automatically from the ip, maybe that's better!
			// column width
			int temp = GuiTools.getPixelWidth(panel.getLineFontSize(),text);
			if(temp>colWidths[col])
				colWidths[col] = temp;
		}
		else if(this==TOURNAMENT_TYPE)
		{	TournamentType type = gameInfo.getTournamentType();
			if(type!=null)
			{	String key;
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
		else if(this==PLAYER_COUNT)
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
		else if(this==ALLOWED_PLAYER)
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
		else if(this==AVERAGE_LEVEL)
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
		else if(this==TOURNAMENT_STATE)
		{	HostState state = gameInfo.getHostInfo().getState();
			String key;
			if(state.equals(HostState.CLOSED))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_CLOSED;
			else if(state.equals(HostState.FINISHED))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_FINISHED;
			else if(state.equals(HostState.OPEN))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_OPEN;
			else if(state.equals(HostState.PLAYING))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_PLAYING;
			else if(state.equals(HostState.UNKOWN))
				key = GuiKeys.COMMON_GAME_LIST_DATA_STATE_UNKNOWN;
			if(key!=null)
				panel.setLabelKey(line,col,key,true);
		}
		else if(this==PLAYED)
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

	@SuppressWarnings("rawtypes")
	public void updateValues(GameListSubPanel container, HashMap<String,List<Comparable>> valuesMap, HashMap<String,GameInfo> gamesMap)
	{	valuesMap.clear();
		for(GameInfo gameInfo: gamesMap.values())
		{	// init
			List<Comparable> list = new ArrayList<Comparable>();
			// process
			if(this==BUTTON || this==HOST_NAME)
			{	String name = gameInfo.getTournamentName();
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(name);
				list.add(ip);
			}
			else if(this==PREFERRED)
			{	Boolean preferred = gameInfo.getHostInfo().isPreferred();
				String name = gameInfo.getTournamentName();
				list.add(preferred);
				list.add(name);
			}
			if(this==HOST_IP)
			{	String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				String name = gameInfo.getTournamentName();
				list.add(ip);
				list.add(name);
			}
			else if(this==TOURNAMENT_TYPE)
			{	TournamentType type = gameInfo.getTournamentType();
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(type);
				list.add(ip);
			}
			else if(this==PLAYER_COUNT)
			{	int playerCount = gameInfo.getPlayerCount();
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(playerCount);
				list.add(ip);
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
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(allowedPlayer);
				list.add(ip);
			}
			else if(this==AVERAGE_LEVEL)
			{	Double level = gameInfo.getAverageScore();
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(level);
				list.add(ip);
			}
			else if(this==TOURNAMENT_STATE)
			{	HostState state = gameInfo.getHostInfo().getState();
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(state);
				list.add(ip);
			}
			else if(this==PLAYED)
			{	Integer played = gameInfo.getHostInfo().getUses();
				String ip = gameInfo.getHostInfo().getLastIp().getHostName();
				list.add(played);
				list.add(ip);
			}

			valuesMap.put(gameInfo.getHostInfo().getId(),list);
		}
	}
}	
