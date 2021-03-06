package org.totalboumboum.gui.common.content.subpanel.players;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import javax.swing.SwingConstants;
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
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFontTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiStringTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * Panel used for player selection.
 * 
 * @author Vincent Labatut
 */
public class PlayersSelectionSubPanel extends TableSubPanel implements MouseListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of lines */
	private static final int LINES = 16+1;
	/** Number of columns */
	private static final int COLS = 7;

	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 */
	public PlayersSelectionSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,1,COLS,true);
		
		// limits
		setPlayers(null,null);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of selected players */
	private List<Profile> players;
	/** Rank column dimension */
	private int rankWidth;
	/** Numbers of allowed players for the current confrontation */
	private Set<Integer> allowedPlayers;
	/** Number of columns */
	private int cols = COLS;
	
	/**
	 * Returns the selected players.
	 * 
	 * @return
	 * 		List of profiles.
	 */
	public List<Profile> getPlayers()
	{	return players;	
	}
	
	/**
	 * Returns the profile of the specified player.
	 * 
	 * @param index
	 * 		Position of the player.
	 * @return
	 * 		Corresponding profile.
	 */
	public Profile getPlayer(int index)
	{	return players.get(index);	
	}
	
	/**
	 * Returns the allowed numbers of players.
	 * 
	 * @return
	 * 		Possible numbers of players for the current confrontation.
	 */
	public Set<Integer> getAllowedPlayers()
	{	return allowedPlayers;
	}
	
	/**
	 * Changes the selected players.
	 * 
	 * @param players
	 * 		New selection of players.
	 * @param allowedPlayers
	 * 		Allowed numbers of players.
	 */
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
		
		// ready
		ClientGeneralConnection clientConfig = Configuration.getConnectionsConfiguration().getClientConnection();
		ServerGeneralConnection serverConfig = Configuration.getConnectionsConfiguration().getServerConnection();
		showReady = clientConfig!=null || serverConfig!=null;

		// sizes
		cols = COLS;
		if(showReady)
			cols ++;
		reinit(LINES,cols);
		int headerHeight = getHeaderHeight();
		//int lineHeight = getLineHeight();
		int deleteWidth = headerHeight;
		int controlWidth = GuiStringTools.initControlsTexts(getLineFontSize(),controlTexts,controlTooltips);
		int colorWidth = GuiStringTools.initColorTexts(getLineFontSize(),colorTexts,colorTooltips,colorBackgrounds);
		int typeWidth = headerHeight;
		int heroWidth = headerHeight;
		int readyWidth = 0;
		if(showReady)
			readyWidth = headerHeight;
		rankWidth = headerHeight;
		int fixedSum = GuiSizeTools.subPanelMargin*(cols-1) + deleteWidth + heroWidth + rankWidth + controlWidth + colorWidth + typeWidth + readyWidth;
		int nameWidth = getDataWidth() - fixedSum;
		
		// headers
		{	List<String> keys = new ArrayList<String>(Arrays.asList(
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_DELETE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_PROFILE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_TYPE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_HERO,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_RANK,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_COLOR,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_CONTROLS
			));
			List<Integer> sizes = new ArrayList<Integer>(Arrays.asList(
				deleteWidth,
				nameWidth,
				typeWidth,
				heroWidth,
				rankWidth,
				colorWidth,
				controlWidth
			));
			if(showReady)
			{	keys.add(GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_READY);
				sizes.add(readyWidth);
			}
			for(int col=0;col<keys.size();col++)
			{	setColSubMinWidth(col,sizes.get(col));
				setColSubPrefWidth(col,sizes.get(col));
				setColSubMaxWidth(col,sizes.get(col));
				if(keys.get(col)!=null)
				{	setLabelKey(0,col,keys.get(col),true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(0,col,bg);
				}
				if(keys.get(col).equals(GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_PROFILE))
				{	setColSubAlignment(col, SwingConstants.LEFT);
					setLabelAlignment(0, col, SwingConstants.CENTER);
				}
			}
			// delete all listeners (buttons)
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
		{	for(int col=0;col<cols;col++)
			{	Color bg;
				if(!allowedPlayers.contains(line) && col==COL_PROFILE)
					bg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
				else
					bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
			}
		}
		refresh();
	}

	/**
	 * Refresh the specified player.
	 * 
	 * @param line
	 * 		Position of the concerned player.
	 */
	public void refreshPlayer(int line)
	{	int index = line-1;
		
		// if there's a player on this line 
		if(players.size()>index)
		{	// init
			Profile profile = players.get(index);
			PredefinedColor clr = profile.getSpriteColor();
			Color color = clr.getColor();
			Color fColor = clr.getSecondaryColor();
			// delete
			{	// content
				setLabelKey(line,COL_DELETE,GuiKeys.COMMON_PLAYERS_SELECTION_DATA_DELETE,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
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
					bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				else
					bg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
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
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				setLabelBackground(line,COL_TYPE,bg);
			}
			// hero
			{	// content
				String tooltip = profile.getSpriteName();
				BufferedImage image = profile.getPortraits().getOffgamePortrait(Portraits.OUTGAME_HEAD);
				setLabelIcon(line,COL_HERO,image,tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
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
				int temp = GuiFontTools.getPixelWidth(getLineFontSize(),text);
				if(temp>rankWidth)
				{	rankWidth = temp;
					setColSubMinWidth(COL_RANK,rankWidth);
					setColSubPrefWidth(COL_RANK,rankWidth);
					setColSubMaxWidth(COL_RANK,rankWidth);
				}
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
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
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
				setLabelBackground(line,COL_COLOR,bg);
				setLabelForeground(line,COL_COLOR,fColor);
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
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				setLabelBackground(line,COL_CONTROLS,bg);
			}
			// ready
			if(showReady)
			{	// content
				String profileType;
				if(profile.isReady())
					profileType = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_CONFIRMED;
				else
					profileType = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_UNCONFIRMED;
				setLabelKey(line,COL_READY,profileType,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				setLabelBackground(line,COL_READY,bg);
			}
		}
		
		// if there's no player on this line
		else if(line<LINES)
		{	for(int col=0;col<cols;col++)
			{	MyLabel lbl = getLabel(line,col);
				lbl.setText(null);
				lbl.setToolTipText(null);
				lbl.setIcon(null);
				lbl.removeMouseListener(this);
				Color bg;
				if(!allowedPlayers.contains(line) && col==COL_PROFILE)
					bg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
				else
					bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
			}
			if(index==players.size())
			{	int col = COL_DELETE;
				String key = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_ADD;
				setLabelKey(line,col,key,true);
				Color bg;
				if(!allowedPlayers.contains(line) && col==COL_PROFILE)
					bg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
				else
					bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
				MyLabel lbl = getLabel(line,col);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);			
				lbl.setMouseSensitive(true);
			}
		}
	}
	
	/**
	 * Refreshes the panel.
	 */
	public void refresh()
	{	for(int line=1;line<LINES;line++)
			refreshPlayer(line);
	}

	/**
	 * Reloads the picture for the specified player.
	 * 
	 * @param line
	 * 		Position of the player.
	 */
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

	/**
	 * Changes the allowed numbers of players for
	 * the current confrontation.
	 * 
	 * @param allowedPlayers
	 * 		Allowed numbers of players.
	 */
	public void setAllowedPlayers(Set<Integer> allowedPlayers)
	{	this.allowedPlayers = allowedPlayers;
		refresh();
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// ready
	/** String associated to the ready state */
	private boolean showReady = false;
	// controls
	/** Strings associated to controls */
	private List<String> controlTexts;
	/** Tooltips associated to controls */
	private List<String> controlTooltips;
	// colors
	/** Strings associated to colors */
	private List<String> colorTexts;
	/** Tooltips associated to colors */
	private List<String> colorTooltips;
	/** Colors used for background */
	private List<Color> colorBackgrounds;
	// indices
	/** Index of the "delete" column */
	private static final int COL_DELETE = 0;
	/** Index of the "profile" column */
	private static final int COL_PROFILE = 1;
	/** Index of the "type" column */
	private static final int COL_TYPE = 2;
	/** Index of the "sprite" column */
	private static final int COL_HERO = 3;
	/** Index of the "rank" column */
	private static final int COL_RANK = 4;
	/** Index of the "color" column */
	private static final int COL_COLOR = 5;
	/** Index of the "controls" column */
	private static final int COL_CONTROLS = 6;
	/** Index of the "readu state" column */
	private static final int COL_READY = 7;
		
	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	//
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	//
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	fireMousePressed(e);
		
		MyLabel label = (MyLabel)e.getComponent();
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
					// add/delete button
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
							{	ProfilesConfiguration.completeProfilesRandomly(players,LINES-1);
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
					// only if profile is local
					if(!profile.isRemote())
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
							{	ProfilesConfiguration.completeProfilesByGlicko(players,LINES-1,profile);
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
					if(connection==null && !profile.isRemote())
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
	{	//
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of objects listening to this panel */
	private List<PlayersSelectionSubPanelListener> listeners = new ArrayList<PlayersSelectionSubPanelListener>();
	
	/**
	 * Adds a new listener to this panel.
	 * 
	 * @param listener
	 * 		New listener.
	 */
	public void addListener(PlayersSelectionSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	/**
	 * Removes an existing listener from this panel.
	 * 
	 * @param listener
	 * 		Listener to remove.
	 */
	public void removeListener(PlayersSelectionSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	/**
	 * Indicates to all listeners that a player was removed.
	 * 
	 * @param index
	 * 		Position of the removed player.
	 */
	public void firePlayerRemoved(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayerRemoved(index);
	}

	/**
	 * Indicates to all listeners that a player was added.
	 * 
	 * @param index
	 * 		Position of the added player.
	 */
	public void firePlayerAdded(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayerAdded(index);
	}
	
	/**
	 * Indicates to all listeners that several players were added.
	 */
	public void firePlayersAdded()
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayersAdded();
	}

	/**
	 * Indicates to all listeners that a profile was changed.
	 * 
	 * @param index
	 * 		Position of the changed profile.
	 */
	public void fireProfileSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionProfileSet(index);
	}

	/**
	 * Indicates to all listeners that a hero was changed.
	 * 
	 * @param index
	 * 		Position of the changed player.
	 */
	public void fireHeroSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionHeroSet(index);
	}
	
	/**
	 * Indicates to all listeners that a color was changed.
	 * 
	 * @param index
	 * 		Position of the changed color.
	 */
	public void fireColorSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionColorSet(index);
	}
	
	/**
	 * Indicates to all listeners that some controls were changed.
	 * 
	 * @param index
	 * 		Position of the changed controls.
	 */
	public void fireControlsSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionControlsSet(index);
	}

	/**
	 * Fires a mouse pressed event, transmits it to all listeners.
	 * 
	 * @param e
	 * 		Event to transmit to listeners.
	 */
	public void fireMousePressed(MouseEvent e)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.mousePressed(e);
	}
}
