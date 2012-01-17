package org.totalboumboum.gui.common.content.subpanel.profile;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ProfileSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 21;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public ProfileSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		
		setProfile(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// PROFILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Profile profile;

	public Profile getProfile()
	{	return profile;	
	}
	
	public void setProfile(Profile profile)
	{	this.profile = profile;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_PROFILES_NAME);
		if(showAiName)
			keys.add(GuiKeys.COMMON_PROFILES_AI_NAME);
		if(showAiPack)
			keys.add(GuiKeys.COMMON_PROFILES_AI_PACK);
		if(showHeroName)
			keys.add(GuiKeys.COMMON_PROFILES_HERO_NAME);
		if(showHeroPack)
			keys.add(GuiKeys.COMMON_PROFILES_HERO_PACK);
		if(showColor)
			keys.add(GuiKeys.COMMON_PROFILES_COLOR);
		if(showRank)
			keys.add(GuiKeys.COMMON_PROFILES_RANK);
		
		if(profile!=null)
		{	String playerId = profile.getId();
			// text
			List<String> values = new ArrayList<String>();
			if(showName)
				values.add(profile.getName());
			if(showAiName)
				values.add(profile.getAiName());
			if(showAiPack)
				values.add(profile.getAiPackname());
			if(showHeroName)
				values.add(profile.getSpriteName());
			if(showHeroPack)
				values.add(profile.getSpritePack());
			Color colorBg = null;
			if(showColor)
			{	String colorKey = profile.getSpriteColor().toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;				 
				values.add(GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey));			
				Color clr = profile.getSpriteColor().getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
				colorBg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
			}
			if(showRank)
			{	RankingService rankingService = GameStatistics.getRankingService();
				PlayerRating playerRating = rankingService.getPlayerRating(playerId);
				String text;
				if(playerRating==null)
					text = "-";//GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_PROFILES_NORANK);				
				else
					text = Integer.toString(rankingService.getPlayerRank(playerId));
				values.add(text);
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
				{	String text = values.get(line);
					String tooltip = text;
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(keys.get(line).equals(GuiKeys.COMMON_PROFILES_COLOR))
						bg = colorBg;
					else if(line>0)
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
		
		// col widths
		int iconWidth = getHeaderHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showName = true;
	private boolean showAiName = true;
	private boolean showAiPack = true;
	private boolean showHeroName = true;
	private boolean showHeroPack = true;
	private boolean showColor = true;
	private boolean showRank = true;

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowAiName(boolean showAiName)
	{	this.showAiName = showAiName;
	}

	public void setShowAiPack(boolean showAiPack)
	{	this.showAiPack = showAiPack;
	}

	public void setShowHeroName(boolean showHeroName)
	{	this.showHeroName = showHeroName;
	}

	public void setShowHeroPack(boolean showHeroPack)
	{	this.showHeroPack = showHeroPack;
	}

	public void setShowColor(boolean showColor)
	{	this.showColor = showColor;
	}
	
	public void setShowRank(boolean showRank)
	{	this.showRank = showRank;
	}
}
