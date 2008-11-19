package fr.free.totalboumboum.gui.menus.quickmatch.profiles;

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
import java.util.ArrayList;

import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Line;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.match.description.MatchDescription;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class ProfilesData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private UntitledSubPanelTable playersPanel;

	// controls
	private ArrayList<String> controlTexts = new ArrayList<String>();
	private ArrayList<String> controlTooltips = new ArrayList<String>();
	// colors
	private ArrayList<String> colorTexts = new ArrayList<String>();
	private ArrayList<String> colorTooltips = new ArrayList<String>();
	private ArrayList<Color> colorBackgrounds = new ArrayList<Color>();
	
	
	public ProfilesData(SplitMenuPanel container)
	{	super(container);
	
		// title
		String key = GuiKeys.MENU_QUICKMATCH_TITLE;
		setTitleKey(key);
		
		// data
		{	UntitledSubPanelLines playersPanel = makePlayersPanel(dataWidth,dataHeight);
			setDataPart(playersPanel);
		}
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
	
	private UntitledSubPanelLines makePlayersPanel(int width, int height)
	{	//Match match = Configuration.getGameConfiguration().getTournament().getCurrentMatch();
		
		int lines = 16+1;
		int headerCols = 6;
		@SuppressWarnings("unused")
		int lineCols = 1+4+1+4+3+1;
		int margin = GuiTools.subPanelMargin;
		UntitledSubPanelLines playersPanel = new UntitledSubPanelLines(width,height,lines,true);
		int headerHeight = playersPanel.getHeaderHeight();
		int lineHeight = playersPanel.getLineHeight();
		int deleteColWidth = lineHeight;
		int controlColWidth = MatchDescription.initControlsTexts(playersPanel.getLineFontSize(),controlTexts,controlTooltips);
		int colorWidth = initColorTexts(playersPanel.getLineFontSize(),colorTexts,colorTooltips,colorBackgrounds);
		int colorColWidth = colorWidth + 2*margin + 2*lineHeight;
		int typeColWidth = headerHeight;
		int fixedSum = margin*(headerCols+1) +deleteColWidth + controlColWidth + colorColWidth + typeColWidth;
		int heroColWidth = (width - fixedSum)/2;
		int nameColWidth = width - fixedSum - heroColWidth;
		int nameWidth = nameColWidth - 3*margin - 3*lineHeight;
		int heroWidth = heroColWidth - 3*margin - 3*lineHeight;
		
		// headers
		{	String keys[] = 
			{	null,
				GuiKeys.MENU_QUICKMATCH_PROFILES_PROFILE_VALUE,
				GuiKeys.MENU_QUICKMATCH_PROFILES_TYPE_VALUE,
				GuiKeys.MENU_QUICKMATCH_PROFILES_HERO_VALUE,
				GuiKeys.MENU_QUICKMATCH_PROFILES_COLOR_VALUE,
				GuiKeys.MENU_QUICKMATCH_PROFILES_CONTROLS_VALUE
			};
			int sizes[] = 
			{	deleteColWidth,
				nameColWidth,
				typeColWidth,
				heroColWidth,
				colorColWidth,
				controlColWidth
			};
			Line ln = playersPanel.getLine(0);
			for(int i=0;i<keys.length-1;i++)
				ln.addLabel(0);
			for(int col=0;col<keys.length;col++)
			{	ln.setLabelMinWidth(col,sizes[col]);
				ln.setLabelPreferredWidth(col,sizes[col]);
				ln.setLabelMaxWidth(col,sizes[col]);
				if(keys[col]!=null)
				{	ln.setLabelKey(col,keys[col],true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
				}
			}
		}
		
		// data
		for(int line=1;line<lines;line++)
		{	String keys[] = 
			{	GuiKeys.MENU_QUICKMATCH_PROFILES_PROFILE_DELETE,
				GuiKeys.MENU_QUICKMATCH_PROFILES_PROFILE_PREVIOUS,
				null,
				GuiKeys.MENU_QUICKMATCH_PROFILES_PROFILE_NEXT,
				GuiKeys.MENU_QUICKMATCH_PROFILES_PROFILE_BROWSE,
				null,
				GuiKeys.MENU_QUICKMATCH_PROFILES_HERO_PREVIOUS,
				null,
				GuiKeys.MENU_QUICKMATCH_PROFILES_HERO_NEXT,
				GuiKeys.MENU_QUICKMATCH_PROFILES_HERO_BROWSE,
				GuiKeys.MENU_QUICKMATCH_PROFILES_COLOR_PREVIOUS,
				null,
				GuiKeys.MENU_QUICKMATCH_PROFILES_COLOR_NEXT,
				null
			};
			int sizes[] = 
			{	lineHeight,
				lineHeight,
				nameWidth,
				lineHeight,
				lineHeight,
				headerHeight,
				lineHeight,
				heroWidth,
				lineHeight,
				lineHeight,
				lineHeight,
				colorWidth,
				lineHeight,
				controlColWidth
			};
			Line ln = playersPanel.getLine(line);
			for(int i=0;i<keys.length-1;i++)
				ln.addLabel(0);
			for(int col=0;col<keys.length;col++)
			{	ln.setLabelMinWidth(col,sizes[col]);
				ln.setLabelPreferredWidth(col,sizes[col]);
				ln.setLabelMaxWidth(col,sizes[col]);
				Color bg;
				if(keys[col]!=null)
				{	ln.setLabelKey(col,keys[col],true);
					bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				}
				else
					bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				ln.setLabelBackground(col,bg);
			}
		}

		return playersPanel;	
		
		
		
		
//TODO une seule ligne avec les boutons au début, puis faire apparaitre au fur et à mesure		
		
		
		
		
		
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
		int[] pos = playersPanel.getLabelPositionSimple(label);
		// controls
		if(pos[1]==2)
		{	ArrayList<Profile> profiles = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getProfiles();
			Profile profile = profiles.get(pos[0]-1);
			int index = profile.getControlSettingsIndex();
			if(profile.hasAi())
			{	if(index==GameConstants.CONTROL_COUNT)
					index = 0;
				else
					index = MatchDescription.getNextFreeControls(profiles,index);
			}
			else
				index = MatchDescription.getNextFreeControls(profiles,index);
			profile.setControlSettingsIndex(index);
			playersPanel.setLabelText(pos[0],pos[1],controlTexts.get(index),controlTooltips.get(index));
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
			colorKey = GuiKeys.COLOR+colorKey;
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
}
