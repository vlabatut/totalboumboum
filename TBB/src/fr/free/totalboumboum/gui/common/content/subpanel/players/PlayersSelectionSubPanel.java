package fr.free.totalboumboum.gui.common.content.subpanel.players;

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiStringTools;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PlayersSelectionSubPanel extends UntitledSubPanelTable implements MouseListener
{	private static final long serialVersionUID = 1L;

	public PlayersSelectionSubPanel(int width, int height)
	{	super(width,height,1,1,1,true);
		
		// limits
		setPlayers(null);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Profile> players;

	public ArrayList<Profile> getPlayers()
	{	return players;	
	}
	
	public Profile getPlayer(int index)
	{	return players.get(index);	
	}
	
	public void setPlayers(ArrayList<Profile> players)
	{	// init
		if(players==null)
			players = new ArrayList<Profile>();
		this.players = players;
		controlTexts = new ArrayList<String>();
		controlTooltips = new ArrayList<String>();
		colorTexts = new ArrayList<String>();
		colorTooltips = new ArrayList<String>();
		colorBackgrounds = new ArrayList<Color>();

		// sizes
		cols = 6;
		lines = 16+1;
		reinit(cols,lines);
		int headerHeight = getHeaderHeight();
		int lineHeight = getLineHeight();
		int deleteWidth = lineHeight;
		int controlWidth = GuiStringTools.initControlsTexts(getLineFontSize(),controlTexts,controlTooltips);
		int colorWidth = GuiStringTools.initColorTexts(getLineFontSize(),colorTexts,colorTooltips,colorBackgrounds);
		int typeWidth = headerHeight;
		int heroWidth = headerHeight;
		int fixedSum = GuiTools.subPanelMargin*(cols+1) + deleteWidth + heroWidth + controlWidth + colorWidth + typeWidth;
		int nameWidth = width - fixedSum;
		
		// headers
		{	String keys[] = 
			{	null,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_PROFILE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_TYPE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_HERO,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_COLOR,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_CONTROLS				
			};
			int sizes[] = 
			{	deleteWidth,
				nameWidth,
				typeWidth,
				heroWidth,
				colorWidth,
				controlWidth
			};
			for(int col=0;col<keys.length;col++)
			{	setColSubMinWidth(col,sizes[col]);
				setColSubPreferredWidth(col,sizes[col]);
				setColSubMaxWidth(col,sizes[col]);
				if(keys[col]!=null)
				{	setLabelKey(0,col,keys[col],true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(0,col,bg);
				}
			}
		}
		
		// data
		for(int line=1;line<lines;line++)
		{	for(int col=0;col<cols;col++)
			{	Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
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
			PredefinedColor clr = profile.getSpriteSelectedColor();
			Color color = clr.getColor();
			// delete
			{	// content
				setLabelKey(line,COL_DELETE,GuiKeys.COMMON_PLAYERS_SELECTION_DATA_DELETE,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				setLabelBackground(line,COL_DELETE,bg);
				// listener
				JLabel lbl = getLabel(line,COL_DELETE);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
			// name
			{	// content
				String text = profile.getName();
				String tooltip = profile.getName();
				setLabelText(line,COL_PROFILE,text,tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				setLabelBackground(line,COL_PROFILE,bg);
				// mouse listener
				JLabel lbl = getLabel(line,COL_PROFILE);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
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
				JLabel lbl = getLabel(line,COL_HERO);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
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
				JLabel lbl = getLabel(line,COL_COLOR);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
			// controls
			{	// content
				int ctrlIndex = profile.getControlSettingsIndex();
				String text = controlTexts.get(ctrlIndex);
				String tooltip = controlTooltips.get(ctrlIndex);
				setLabelText(line,COL_CONTROLS,text, tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				setLabelBackground(line,COL_CONTROLS,bg);
				// mouse listener
				JLabel lbl = getLabel(line,COL_CONTROLS);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
		}
		// if there's no player on this line
		else if(line<lines)
		{	for(int col=0;col<cols;col++)
			{	JLabel lbl = getLabel(line,col);
				lbl.setText(null);
				lbl.setToolTipText(null);
				lbl.setIcon(null);
				lbl.removeMouseListener(this);
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				setLabelBackground(line,col,bg);
			}
			if(index==players.size())
			{	int col = COL_DELETE;
				String key = GuiKeys.COMMON_PLAYERS_SELECTION_DATA_ADD;
				setLabelKey(line,col,key,true);
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				setLabelBackground(line,col,bg);
				JLabel lbl = getLabel(line,col);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);			
			}
		}
	}
	
	public void refresh()
	{	for(int line=1;line<lines;line++)
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

	/////////////////////////////////////////////////////////////////
	// DISPLAY	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// sizes
	private int cols = 6;
	private int lines = 16+1;
	// controls
	private ArrayList<String> controlTexts;
	private ArrayList<String> controlTooltips;
	// colors
	private ArrayList<String> colorTexts;
	private ArrayList<String> colorTooltips;
	private ArrayList<Color> colorBackgrounds;
	// indices
	private static final int COL_DELETE = 0;
	private static final int COL_PROFILE = 1;
	private static final int COL_TYPE = 2;
	private static final int COL_HERO = 3;
	private static final int COL_COLOR = 4;
	private static final int COL_CONTROLS = 5;
		
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
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = getLabelPosition(label);
		switch(pos[2])
		{	case COL_DELETE:
				{	int index = pos[0]-1;
					// add a profile
					if(index==players.size())
					{	firePlayerAdded(index);
					}
					// or remove a profile
					else if(index<players.size())
					{	players.remove(index);
						refresh();
					}
				}
				break;
			case COL_PROFILE:
				{	int index = pos[0]-1;
					fireProfileSet(index);
				}
				break;
			case COL_HERO:
				{	int index = pos[0]-1;
					fireHeroSet(index);
				}
				break;
			case COL_COLOR:
				{	Profile profile = players.get(pos[0]-1);
					PredefinedColor color = profile.getSpriteSelectedColor();
					color = Configuration.getProfilesConfiguration().getNextFreeColor(players,profile,color);
					profile.setSpriteSelectedColor(color);
					reloadPortraits(pos[0]);
					refreshPlayer(pos[0]);
				}
				break;
			case COL_CONTROLS:
				{	Profile profile = players.get(pos[0]-1);
					int index = profile.getControlSettingsIndex();
					index = Configuration.getProfilesConfiguration().getNextFreeControls(players,index);
					profile.setControlSettingsIndex(index);
					setLabelText(pos[0],pos[2],controlTexts.get(index),controlTooltips.get(index));
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
	private ArrayList<PlayersSelectionSubPanelListener> listeners = new ArrayList<PlayersSelectionSubPanelListener>();
	
	public void addListener(PlayersSelectionSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PlayersSelectionSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	public void firePlayerAdded(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionPlayerAdded(index);
	}

	public void fireProfileSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionProfileSet(index);
	}

	public void fireHeroSet(int index)
	{	for(PlayersSelectionSubPanelListener listener: listeners)
			listener.playerSelectionHeroSet(index);
	}
}
