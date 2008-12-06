package fr.free.totalboumboum.gui.menus.quickmatch.players;

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
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.match.description.MatchDescription;
import fr.free.totalboumboum.gui.menus.quickmatch.players.hero.SelectHeroSplitPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.players.profile.SelectProfileSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PlayersData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 16+1;
	private static final int COL_COUNT = 6;

	private static final int COL_DELETE = 0;
	private static final int COL_PROFILE = 1;
	private static final int COL_TYPE = 2;
	private static final int COL_HERO = 3;
	private static final int COL_COLOR = 4;
	private static final int COL_CONTROLS = 5;
	
	private UntitledSubPanelTable playersPanel;

	// controls
	private ArrayList<String> controlTexts = new ArrayList<String>();
	private ArrayList<String> controlTooltips = new ArrayList<String>();
	// colors
	private ArrayList<String> colorTexts = new ArrayList<String>();
	private ArrayList<String> colorTooltips = new ArrayList<String>();
	private ArrayList<Color> colorBackgrounds = new ArrayList<Color>();
	
	// profiles
	private ArrayList<Profile> profiles;
	
	public PlayersData(SplitMenuPanel container, ArrayList<Profile> selectedProfiles)
	{	super(container);
		
		// profiles
		profiles = selectedProfiles;
	
		// title
		String key = GuiKeys.MENU_QUICKMATCH_PLAYERS_TITLE;
		setTitleKey(key);
		
		// data
		playersPanel = makePlayersPanel(dataWidth,dataHeight);
		setDataPart(playersPanel);
		for(int line=1;line<LINE_COUNT;line++)
			refreshPlayer(line);
	}

	@Override
	public void refresh()
	{	for(int line=1;line<LINE_COUNT;line++)
			refreshPlayer(line);
	}
	
	private UntitledSubPanelTable makePlayersPanel(int width, int height)
	{	int headerCols = 6;
		int margin = GuiTools.subPanelMargin;
		UntitledSubPanelTable playersPanel = new UntitledSubPanelTable(width,height,COL_COUNT,LINE_COUNT,true);
		int headerHeight = playersPanel.getHeaderHeight();
		int lineHeight = playersPanel.getLineHeight();
		int deleteWidth = lineHeight;
		int controlWidth = MatchDescription.initControlsTexts(playersPanel.getLineFontSize(),controlTexts,controlTooltips);
		int colorWidth = initColorTexts(playersPanel.getLineFontSize(),colorTexts,colorTooltips,colorBackgrounds);
		int typeWidth = headerHeight;
		int heroWidth = headerHeight;
		int fixedSum = margin*(headerCols+1) + deleteWidth + heroWidth + controlWidth + colorWidth + typeWidth;
		int nameWidth = width - fixedSum;
		
		// headers
		{	String keys[] = 
			{	null,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_PROFILE_HEADER,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_TYPE_HEADER,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_HERO_HEADER,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_COLOR,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_CONTROLS
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
			{	playersPanel.setColSubMinWidth(col,sizes[col]);
				playersPanel.setColSubPreferredWidth(col,sizes[col]);
				playersPanel.setColSubMaxWidth(col,sizes[col]);
				if(keys[col]!=null)
				{	playersPanel.setLabelKey(0,col,keys[col],true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					playersPanel.setLabelBackground(0,col,bg);
				}
			}
		}
		
		// data
		for(int line=1;line<LINE_COUNT;line++)
		{	for(int col=0;col<COL_COUNT;col++)
			{	Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				playersPanel.setLabelBackground(line,col,bg);
			}
		}
		
		return playersPanel;	
	}
	
	private void refreshPlayer(int line)
	{	int index = line-1;
		// if there's a player on this line 
		if(profiles.size()>index)
		{	// init
			Profile profile = profiles.get(index);
			PredefinedColor clr = profile.getSpriteSelectedColor();
			Color color = clr.getColor();
			// delete
			{	// content
				playersPanel.setLabelKey(line,COL_DELETE,GuiKeys.MENU_QUICKMATCH_PLAYERS_PROFILE_DELETE,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				playersPanel.setLabelBackground(line,COL_DELETE,bg);
				// listener
				JLabel lbl = playersPanel.getLabel(line,COL_DELETE);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
			// name
			{	// content
				String text = profile.getName();
				String tooltip = profile.getName();
				playersPanel.setLabelText(line,COL_PROFILE,text,tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				playersPanel.setLabelBackground(line,COL_PROFILE,bg);
				// mouse listener
				JLabel lbl = playersPanel.getLabel(line,COL_PROFILE);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
			// type
			{	// content
				String profileType;
				if(profile.hasAi())
					profileType = GuiKeys.MENU_QUICKMATCH_PLAYERS_TYPE_COMPUTER;
				else
					profileType = GuiKeys.MENU_QUICKMATCH_PLAYERS_TYPE_HUMAN;
				playersPanel.setLabelKey(line,COL_TYPE,profileType,true);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				playersPanel.setLabelBackground(line,COL_TYPE,bg);
			}
			// hero
			{	// content
				String tooltip = profile.getSpriteName();
				BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
				playersPanel.setLabelIcon(line,COL_HERO,image,tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				playersPanel.setLabelBackground(line,COL_HERO,bg);
				// mouse listener
				JLabel lbl = playersPanel.getLabel(line,COL_HERO);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
			// color
			{	// content
				String colorKey = clr.toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;
				playersPanel.setLabelKey(line,COL_COLOR,colorKey,false);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
				playersPanel.setLabelBackground(line,COL_COLOR,bg);
				// mouse listener
				JLabel lbl = playersPanel.getLabel(line,COL_COLOR);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
			// controls
			{	// content
				int ctrlIndex = profile.getControlSettingsIndex();
				String text = controlTexts.get(ctrlIndex);
				String tooltip = controlTooltips.get(ctrlIndex);
				playersPanel.setLabelText(line,COL_CONTROLS,text, tooltip);
				// color
				Color bg = new Color(color.getRed(),color.getGreen(),color.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2);
				playersPanel.setLabelBackground(line,COL_CONTROLS,bg);
				// mouse listener
				JLabel lbl = playersPanel.getLabel(line,COL_CONTROLS);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);
			}
		}
		// if there's no player on this line
		else if(line<LINE_COUNT)
		{	for(int col=0;col<COL_COUNT;col++)
			{	JLabel lbl = playersPanel.getLabel(line,col);
				lbl.setText(null);
				lbl.setToolTipText(null);
				lbl.setIcon(null);
				lbl.removeMouseListener(this);
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				playersPanel.setLabelBackground(line,col,bg);
			}
			if(index==profiles.size())
			{	int col = COL_DELETE;
				String key = GuiKeys.MENU_QUICKMATCH_PLAYERS_PROFILE_ADD;
				playersPanel.setLabelKey(line,col,key,true);
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				playersPanel.setLabelBackground(line,col,bg);
				JLabel lbl = playersPanel.getLabel(line,col);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);			
			}
		}
	}

/*	
		// empty
		{	playersPanel.setSubColumnsPreferredWidth(2,ctrlColWidth);
			playersPanel.setSubColumnsMaxWidth(3,Integer.MAX_VALUE);
		}
		// data
		{	ArrayList<Profile> players = match.getProfiles();
			Iterator<Profile> i = players.iterator();
			int line = 1;
			while(i.hasNext())
			{	int col = 0;
				Profile profile = i.next();
				// color
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				playersPanel.setLineBackground(line,bg);
				// portrait
				{	BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					String tooltip = profile.getName();
					playersPanel.setLabelIcon(line,col,image,tooltip);
					col++;
				}
				// profile type
				{	String aiName = profile.getAiName();
					String key;
					if(aiName==null)
						key = GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_HUMAN;
					else
						key = GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_COMPUTER;
					playersPanel.setLabelKey(line,col,key,true);
					col++;
				}
				// controls
				{	int index = profile.getControlSettingsIndex();
					playersPanel.setLabelText(line,col,controlTexts.get(index),controlTooltips.get(index));
					JLabel label = playersPanel.getLabel(line,col);
					label.addMouseListener(this);
					col++;
				}
				// name
				{	String text = profile.getName();
					String tooltip = profile.getName();
					playersPanel.setLabelText(line,col,text,tooltip);
					col++;
				}
				// rank
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(0);
					String text = "-";
					String tooltip = "-";
					playersPanel.setLabelText(line,col,text,tooltip);
					col++;
				}
				//
				line++;
			}
		}
 */
	
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
		int[] pos = playersPanel.getLabelPosition(label);
		switch(pos[2])
		{	case COL_DELETE:
				{	int index = pos[0]-1;
					// add a profile
					if(index==profiles.size())
					{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getContainer(),container,index,profiles);
						getContainer().replaceWith(selectProfilePanel);						
					}
					// or remove a profile
					else if(index<profiles.size())
					{	profiles.remove(index);
						refresh();
					}
				}
				break;
			case COL_PROFILE:
				{	int index = pos[0]-1;
					SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getContainer(),container,index,profiles);
					getContainer().replaceWith(selectProfilePanel);
				}
				break;
			case COL_HERO:
				{	Profile profile = profiles.get(pos[0]-1);
					SelectHeroSplitPanel selectHeroPanel = new SelectHeroSplitPanel(container.getContainer(),container,profile);
					getContainer().replaceWith(selectHeroPanel);					
				}
				break;
			case COL_COLOR:
				{	Profile profile = profiles.get(pos[0]-1);
					PredefinedColor color = profile.getSpriteSelectedColor();
					color = Configuration.getProfilesConfiguration().getNextFreeColor(profiles,profile,color);
					profile.setSpriteSelectedColor(color);
					reloadPortraits(pos[0]);
					refreshPlayer(pos[0]);
				}
				break;
			case COL_CONTROLS:
				{	Profile profile = profiles.get(pos[0]-1);
					int index = profile.getControlSettingsIndex();
					index = Configuration.getProfilesConfiguration().getNextFreeControls(profiles,index);
					profile.setControlSettingsIndex(index);
					playersPanel.setLabelText(pos[0],pos[2],controlTexts.get(index),controlTooltips.get(index));
				}
				break;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	public static int initColorTexts(int fontSize, ArrayList<String> colorTexts, ArrayList<String> colorTooltips, ArrayList<Color> colorBackgrounds)
	{	int result = 0;
		for(PredefinedColor color : PredefinedColor.values())
		{	// text
			String colorKey = color.toString();
			colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
			colorKey = GuiKeys.COMMON_COLOR+colorKey;
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
			String tooltip = text;
			Color clr = color.getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
			//
			colorTexts.add(text);
			colorTooltips.add(tooltip);
			colorBackgrounds.add(bg);
			// width
			int temp = GuiTools.getPixelWidth(fontSize,text);
			if(temp>result)
				result = temp;
		}
		return result;
	}

	private void reloadPortraits(int line)
	{	int index = line - 1;
		Profile profile = profiles.get(index);
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

	public ArrayList<Profile> getSelectedProfiles()
	{	return profiles;	
	}
}
