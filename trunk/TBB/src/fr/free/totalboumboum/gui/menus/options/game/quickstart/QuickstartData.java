package fr.free.totalboumboum.gui.menus.options.game.quickstart;

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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.game.match.description.MatchDescription;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.hero.SelectHeroSplitPanel;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.profile.SelectProfileSplitPanel;
import fr.free.totalboumboum.gui.menus.options.game.quickstart.round.SelectRoundSplitPanel;
import fr.free.totalboumboum.gui.menus.quickmatch.players.PlayersData;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class QuickstartData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.06f;
	
	private static final int LINE_COUNT = 16+1;
	private static final int COL_COUNT = 6;

	private static final int COL_DELETE = 0;
	private static final int COL_PROFILE = 1;
	private static final int COL_TYPE = 2;
	private static final int COL_HERO = 3;
	private static final int COL_COLOR = 4;
	private static final int COL_CONTROLS = 5;
	
	private UntitledSubPanelTable playersPanel;
	private UntitledSubPanelTable roundPanel;
	private SubPanel mainPanel;
	private int roundHeight;
	private int playersHeight;

	// controls
	private ArrayList<String> controlTexts = new ArrayList<String>();
	private ArrayList<String> controlTooltips = new ArrayList<String>();
	// colors
	private ArrayList<String> colorTexts = new ArrayList<String>();
	private ArrayList<String> colorTooltips = new ArrayList<String>();
	private ArrayList<Color> colorBackgrounds = new ArrayList<Color>();
	
	// profiles
	private ArrayList<Profile> profiles;
	private StringBuffer roundFile;
	
	public QuickstartData(SplitMenuPanel container, ArrayList<Profile> selectedProfiles, String roundFolder)
	{	super(container);
		
		// profiles
		profiles = selectedProfiles;
		roundFile = new StringBuffer(roundFolder);
	
		// title
		String key = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_TITLE;
		setTitleKey(key);
		
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			roundHeight = (int)(dataHeight*SPLIT_RATIO); 
			playersHeight = dataHeight - roundHeight - margin;
			mainPanel.setOpaque(false);
			
			// round panel
			{	roundPanel = makeRoundPanel(dataWidth,roundHeight);
				mainPanel.add(roundPanel);
			}

			mainPanel.add(Box.createVerticalGlue());
			
			// players panel
			{	playersPanel = makePlayersPanel(dataWidth,playersHeight);
				mainPanel.add(playersPanel);
			}
			
			
			setDataPart(mainPanel);
			
		}

		setDataPart(mainPanel);
		refreshRound();
		for(int line=1;line<LINE_COUNT;line++)
			refreshPlayer(line);
	}

	@Override
	public void refresh()
	{	for(int line=1;line<LINE_COUNT;line++)
			refreshPlayer(line);
		refreshRound();
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
	
	private UntitledSubPanelTable makePlayersPanel(int width, int height)
	{	int headerCols = 6;
		int margin = GuiTools.subPanelMargin;
		UntitledSubPanelTable result = new UntitledSubPanelTable(width,height,COL_COUNT,LINE_COUNT,true);
		int headerHeight = result.getHeaderHeight();
		int lineHeight = result.getLineHeight();
		int deleteWidth = lineHeight;
		int controlWidth = MatchDescription.initControlsTexts(result.getLineFontSize(),controlTexts,controlTooltips);
		int colorWidth = PlayersData.initColorTexts(result.getLineFontSize(),colorTexts,colorTooltips,colorBackgrounds);
		int typeWidth = headerHeight;
		int heroWidth = headerHeight;
		int fixedSum = margin*(headerCols+1) + deleteWidth + heroWidth + controlWidth + colorWidth + typeWidth;
		int nameWidth = width - fixedSum;
		
		// headers
		{	String keys[] = 
			{	null,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_HEADER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_HEADER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_HEADER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_COLOR,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_CONTROLS
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
			{	result.setSubColumnsMinWidth(col,sizes[col]);
				result.setSubColumnsPreferredWidth(col,sizes[col]);
				result.setSubColumnsMaxWidth(col,sizes[col]);
				if(keys[col]!=null)
				{	result.setLabelKey(0,col,keys[col],true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					result.setLabelBackground(0,col,bg);
				}
			}
		}
		
		// data
		for(int line=1;line<LINE_COUNT;line++)
		{	for(int col=0;col<COL_COUNT;col++)
			{	Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				result.setLabelBackground(line,col,bg);
			}
		}
		
		return result;	
	}

	private UntitledSubPanelTable makeRoundPanel(int width, int height)
	{	int cols = 2;
		int lines = 1;
		int margin = GuiTools.subPanelMargin;
		UntitledSubPanelTable result = new UntitledSubPanelTable(width,height,cols,lines,false);
		@SuppressWarnings("unused")
		int headerHeight = result.getHeaderHeight();
		int lineHeight = result.getLineHeight();
		int browseWidth = lineHeight;
		int fileWidth = width - (browseWidth + 3*margin);		
		
		{	int line = 0;
			int col = 0;
			// name
			{	// size
				result.setSubColumnsMinWidth(col,fileWidth);
				result.setSubColumnsPreferredWidth(col,fileWidth);
				result.setSubColumnsMaxWidth(col,fileWidth);
				// color
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// next
				col++;
			}
			// browse
			{	// size
				result.setSubColumnsMinWidth(col,browseWidth);
				result.setSubColumnsPreferredWidth(col,browseWidth);
				result.setSubColumnsMaxWidth(col,browseWidth);
				// icon
				String key = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_ROUND_BROWSE;
				result.setLabelKey(line,col,key,true);
				// color
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				result.setLabelBackground(line,col,bg);
				// listener
				JLabel lbl = result.getLabel(line,col);
				lbl.addMouseListener(this);
				// next
				col++;
			}
		}
		
		return result;	
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
				playersPanel.setLabelKey(line,COL_DELETE,GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_DELETE,true);
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
					profileType = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_COMPUTER;
				else
					profileType = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_HUMAN;
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
				colorKey = GuiKeys.COLOR+colorKey;
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
				String key = GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_ADD;
				playersPanel.setLabelKey(line,col,key,true);
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				playersPanel.setLabelBackground(line,col,bg);
				JLabel lbl = playersPanel.getLabel(line,col);
				lbl.removeMouseListener(this); //just in case
				lbl.addMouseListener(this);			
			}
		}
	}

	private void refreshRound()
	{	// if there's a selected round
		if(roundFile!=null)
			roundPanel.setLabelText(0,0,roundFile.toString(),roundFile.toString());
		else
			roundPanel.setLabelText(0,0,null,null);
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
		// round
		if(pos[0]==-1)
		{	SelectRoundSplitPanel selectRoundPanel = new SelectRoundSplitPanel(container.getContainer(),container,roundFile);
			getContainer().replaceWith(selectRoundPanel);
		}
		// players
		else
		{	switch(pos[2])
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
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
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

	public String getSelectedRound()
	{	return roundFile.toString();	
	}
}
