package org.totalboumboum.gui.common.content.subpanel.players;

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.game.profile.Portraits;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiStringTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayersSelectionSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 16+1;
	private static final int COLS = 7;

	public PlayersSelectionSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,COLS,true);
		
		// limits
		setPlayers(null,null);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Profile> players;
	private int rankWidth;
	private Set<Integer> allowedPlayers;
	
	public List<Profile> getPlayers()
	{	return players;	
	}
	
	public Profile getPlayer(int index)
	{	return players.get(index);	
	}
	
	public void setPlayers(List<Profile> players, Set<Integer> allowedPlayers)
	{	// init
		if(players==null)
			players = new ArrayList<Profile>();
		this.players = players;
		if(allowedPlayers==null)
			allowedPlayers = new TreeSet<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16));
		this.allowedPlayers = allowedPlayers;
		controlTexts = new ArrayList<String>();
		controlTooltips = new ArrayList<String>();
		colorTexts = new ArrayList<String>();
		colorTooltips = new ArrayList<String>();
		colorBackgrounds = new ArrayList<Color>();

		// sizes
		reinit(LINES,COLS);
		int headerHeight = getHeaderHeight();
		//int lineHeight = getLineHeight();
		int deleteWidth = headerHeight;
		int controlWidth = GuiStringTools.initControlsTexts(getLineFontSize(),controlTexts,controlTooltips);
		int colorWidth = GuiStringTools.initColorTexts(getLineFontSize(),colorTexts,colorTooltips,colorBackgrounds);
		int typeWidth = headerHeight;
		int heroWidth = headerHeight;
		rankWidth = headerHeight;
		int fixedSum = GuiTools.subPanelMargin*(COLS-1) + deleteWidth + heroWidth + rankWidth + controlWidth + colorWidth + typeWidth;
		int nameWidth = getDataWidth() - fixedSum;
		
		// headers
		{	String keys[] = 
			{	GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_DELETE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_PROFILE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_TYPE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_HERO,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_RANK,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_COLOR,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_CONTROLS				
			};
			int sizes[] = 
			{	deleteWidth,
				nameWidth,
				typeWidth,
				heroWidth,
				rankWidth,
				colorWidth,
				controlWidth
			};
			for(int col=0;col<keys.length;col++)
			{	setColSubMinWidth(col,sizes[col]);
				setColSubPrefWidth(col,sizes[col]);
				setColSubMaxWidth(col,sizes[col]);
				if(keys[col]!=null)
				{	setLabelKey(0,col,keys[col],true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(0,col,bg);
				}
			}
			// delete all button
			MyLabel lbl = getLabel(0,COL_DELETE);
			lbl.addMouseListener(this);
			lbl.setMouseSensitive(true);
			// random selection button
			lbl = getLabel(0,COL_PROFILE);
			lbl.addMouseListener(this);			
			lbl.setMouseSensitive(true);
		}
		
		// data
		for(int line=1;line<LINES;line++)
		{	for(int col=0;col<COLS;col++)
			{	Color bg;
				if(!allowedPlayers.contains(line) && col==COL_PROFILE)
					bg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
				else
					bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
			}
		}
		refresh();
	}

	public void refreshPlayer(int line)
	{	int index = line-1;
		
		// if there's a player on this line 
		if(players.size()>index)
		{	// init
			Profile profile = players.get(index);
			PredefinedColor clr = profile.getSpriteColor();
			Color color = clr.getColor();
			// delete
			{	// content
				setLabelKey(line,COL_DELETE,GuiKeys.COMMON_PLAYERS_SELECTION_DATA_DELETE,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				setLabelBackground(line,COL_DELETE,bg);
				// listener
				MyLabel lbl = getLabel(line,COL_DELETE);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
				lbl.setMouseSensitive(true);
			}
			// name
			{	// content
				String text = profile.getName();
				String tooltip = profile.getName();
				setLabelText(line,COL_PROFILE,text,tooltip);
				// color
				//Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				Color bg;
				if(allowedPlayers.contains(line))
					bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				else
					bg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
				setLabelBackground(line,COL_PROFILE,bg);
				// mouse listener
				MyLabel lbl = getLabel(line,COL_PROFILE);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
				lbl.setMouseSensitive(true);
			}
			// type
			{	// content
				String profileType;
				if(profile.hasAi())
					profileType = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_COMPUTER;
				else
					profileType = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_HUMAN;
				setLabelKey(line,COL_TYPE,profileType,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				setLabelBackground(line,COL_TYPE,bg);
			}
			// hero
			{	// content
				String tooltip = profile.getSpriteName();
				BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				setLabelIcon(line,COL_HERO,image,tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				setLabelBackground(line,COL_HERO,bg);
				// mouse listener
				MyLabel lbl = getLabel(line,COL_HERO);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
				lbl.setMouseSensitive(true);
			}
			// rank
			{	// content
				RankingService rankingService = GameStatistics.getRankingService();
				String playerId = profile.getId();
				int rank = rankingService.getPlayerRank(playerId);
				String text,tooltip;
				if(rank<0)
				{	String key = GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_NO_RANK;
					text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
				}
				else
				{	text = Integer.toString(rank);
					tooltip = text;
				}
				setLabelText(line,COL_RANK,text,tooltip);
				// size
				int temp = GuiTools.getPixelWidth(getLineFontSize(),text);
				if(temp>rankWidth)
				{	rankWidth = temp;
					setColSubMinWidth(COL_RANK,rankWidth);
					setColSubPrefWidth(COL_RANK,rankWidth);
					setColSubMaxWidth(COL_RANK,rankWidth);
				}
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				setLabelBackground(line,COL_RANK,bg);
				// mouse listener
				MyLabel lbl = getLabel(line,COL_RANK);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
				lbl.setMouseSensitive(true);
			}
			// color
			{	// content
				String colorKey = clr.toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;
				setLabelKey(line,COL_COLOR,colorKey,false);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
				setLabelBackground(line,COL_COLOR,bg);
				// mouse listener
				MyLabel lbl = getLabel(line,COL_COLOR);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
				lbl.setMouseSensitive(true);
			}
			// controls
			{	// content
				if(profile.isRemote())
					setLabelKey(line,COL_CONTROLS,GuiKeys.COMMON_PLAYERS_SELECTION_DATA_REMOTE,true);
				else 
				{	int ctrlIndex = profile.getControlSettingsIndex();
					String text = controlTexts.get(ctrlIndex);
					String tooltip = controlTooltips.get(ctrlIndex);
					setLabelText(line,COL_CONTROLS,text,tooltip);
					// mouse listener
					MyLabel lbl = getLabel(line,COL_CONTROLS);
					lbl.removeMouseListener(this); //just in case
					lbl.addMouseListener(this);
					lbl.setMouseSensitive(true);
				}
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				setLabelBackground(line,COL_CONTROLS,bg);
			}
		}
		
		// if there's no player on this line
		else if(line<LINES)
		{	for(int col=0;col<COLS;col++)
			{	MyLabel lbl = getLabel(line,col);
				lbl.setText(null);
				lbl.setToolTipText(null);
				lbl.setIcon(null);
				lbl.removeMouseListener(this);
				Color bg;
				if(!allowedPlayers.contains(line) && col==COL_PROFILE)
					bg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
				else
					bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
			}
			if(index==players.size())
			{	int col = COL_DELETE;
				String key = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_ADD;
				setLabelKey(line,col,key,true);
				Color bg;
				if(!allowedPlayers.contains(line) && col==COL_PROFILE)
					bg = GuiTools.COLOR_TABLE_SELECTED_BACKGROUND;
				else
					bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
				MyLabel lbl = getLabel(line,col);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);			
				lbl.setMouseSensitive(true);
			}
		}
	}
	
	public void refresh()
	{	for(int line=1;line<LINES;line++)
			refreshPlayer(line);
	}

	private void reloadPortraits(int line)
	{	int index = line - 1;
		Profile profile = players.get(index);
		try
		{	ProfileLoader.reloadPortraits(profile);
		}
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
	}

	public void setAllowedPlayers(Set<Integer> allowedPlayers)
	{	this.allowedPlayers = allowedPlayers;
		refresh();
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// controls
	private List<String> controlTexts;
	private List<String> controlTooltips;
	// colors
	private List<String> colorTexts;
	private List<String> colorTooltips;
	private List<Color> colorBackgrounds;
	// indices
	private static final int COL_DELETE = 0;
	private static final int COL_PROFILE = 1;
	private static final int COL_TYPE = 2;
	private static final int COL_HERO = 3;
	private static final int COL_RANK = 4;
	private static final int COL_COLOR = 5;
	private static final int COL_CONTROLS = 6;
		
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	MyLabel label = (MyLabel)e.getComponent();
		int[] pos = getLabelPositionMultiple(label);
		ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		switch(pos[2])
		{	case COL_DELETE:
				{	// delete all
					if(pos[0]==0)
					{	if(connection==null) //only if not a client
						{	while(players.size()>0)
							{	players.remove(0);
								refresh();
								firePlayerRemoved(0);
							}
						}
					}
					// delete player
					else
					{	int index = pos[0]-1;
						// add a profile
						if(index==players.size())
						{	firePlayerAdded(index);
						}
						// or remove a profile
						else if(index<players.size())
						{	// only if host is not a client
							Profile profile = players.get(index);
							if(connection==null)
							{	players.remove(index);
								refresh();
								firePlayerRemoved(index);
							}
							//  or profile is local
							else if(!profile.isRemote())
							{	firePlayerRemoved(index);
							}
						}
					}
				}
				break;
			case COL_PROFILE:
				{	// random selection
					if(pos[0]==0)
					{	// only if not a client
						if(connection==null)
						{	try
							{	ProfilesConfiguration.randomlyCompleteProfiles(players,LINES-1);
								refresh();
								firePlayersAdded();
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
							catch (ClassNotFoundException e1)
							{	e1.printStackTrace();
							}
							catch (IllegalArgumentException e1)
							{	e1.printStackTrace();
							} 
							catch (SecurityException e1)
							{	e1.printStackTrace();
							}
							catch (IllegalAccessException e1)
							{	e1.printStackTrace();
							}
							catch (NoSuchFieldException e1)
							{	e1.printStackTrace();
							}
						}
					}
					else
					{	int index = pos[0]-1;
						fireProfileSet(index);
					}
				}
				break;
			case COL_HERO:
				{	int index = pos[0]-1;
					Profile profile = players.get(index);
					// only if host is not a client or profile is local
					if(connection==null || !profile.isRemote())
						fireHeroSet(index);
				}
				break;
			case COL_RANK:
				{	// only if host is not a client
					if(connection==null)
					{	int index = pos[0]-1;
						Iterator<Profile> it = players.iterator();
						RankingService rankingService = GameStatistics.getRankingService();
						Profile profile = players.get(index);
						String playerId = profile.getId();
						PlayerRating playerRating = rankingService.getPlayerRating(playerId);
						if(playerRating==null)
						{	while(it.hasNext())
							{	profile = it.next();
								playerId = profile.getId();
								playerRating = rankingService.getPlayerRating(playerId);
								if(playerRating==null)
								{	it.remove();
									refresh();
									firePlayerRemoved(index);
								}
							}
						}
						else
						{	try
							{	ProfilesConfiguration.rankCompleteProfiles(players,LINES-1,profile);
								refresh();
								firePlayersAdded();
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
							catch (ClassNotFoundException e1)
							{	e1.printStackTrace();
							}
							catch (IllegalAccessException e1)
							{	e1.printStackTrace();
							}
							catch (NoSuchFieldException e1)
							{	e1.printStackTrace();
							}
						}
					}
				}
				break;
			case COL_COLOR:
				{	int index = pos[0]-1;
					Profile profile = players.get(index);
					if(connection==null)
					{	PredefinedColor color = profile.getSpriteColor();
						color = Configuration.getProfilesConfiguration().getNextFreeColor(players,profile,color);
						profile.getSelectedSprite().setColor(color);
						reloadPortraits(pos[0]);
						refreshPlayer(pos[0]);
						fireColorSet(index);
					}
					else if(!profile.isRemote())
					{	fireColorSet(index);
					}
				}
				break;
			case COL_CONTROLS:
				{	int index = pos[0]-1;
					Profile profile = players.get(index);
					if(!profile.isRemote())
					{	int ctrlIndex = profile.getControlSettingsIndex();
						ctrlIndex = Configuration.getProfilesConfiguration().getNextFreeControls(players,ctrlIndex);
						profile.setControlSettingsIndex(ctrlIndex);
						setLabelText(pos[0],pos[2],controlTexts.get(ctrlIndex),controlTooltips.get(ctrlIndex));
						fireControlsSet(index);
					}
				}
				break;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<PlayersSelectionSubPanelListener> listeners = new ArrayList<PlayersSelectionSubPanelListener>();
	
	public void addListener(PlayersSelectionSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PlayersSelectionSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	public void firePlayerRemoved(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayerRemoved(index);
	}

	public void firePlayerAdded(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayerAdded(index);
	}
	
	public void firePlayersAdded()
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayersAdded();
	}

	public void fireProfileSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionProfileSet(index);
	}

	public void fireHeroSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionHeroSet(index);
	}
	
	public void fireColorSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionColorSet(index);
	}
	
	public void fireControlsSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionControlsSet(index);
	}
}
