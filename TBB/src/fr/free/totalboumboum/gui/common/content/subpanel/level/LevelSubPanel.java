package fr.free.totalboumboum.gui.common.content.subpanel.level;

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
import java.util.ArrayList;

import javax.swing.BoxLayout;

import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanelTable;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class LevelSubPanel extends SubPanel
{	private static final long serialVersionUID = 1L;
	
	public LevelSubPanel(int width, int height)
	{	super(width,height);
		setOpaque(false);
		
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		setLevelPreview(null,8);
	}
		
	
/*
 * TODO 
 * - pb d'alignement vertical dans le game description
 * - mettre à jour les fichiers de langue
 * - mettre à jour l'icone Level dans la description de round	
 */
	
	
	
	
	/////////////////////////////////////////////////////////////////
	// INNER PANEM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SubPanelTable innerPanel;
	
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelPreview levelPreview;
	private int lines;
	
	public LevelPreview getLevelPreview()
	{	return levelPreview;	
	}
	
	public void setLevelPreview(LevelPreview levelPreview, int lines)
	{	this.levelPreview = levelPreview;
		this.lines = lines;
		
		// sizes
//		int lines = 8;
		int colSubs = 2;
		int colGroups = 1;
		if(innerPanel!=null)
			remove((SubPanel)innerPanel);
		if(showPanelTitle)
		{	innerPanel = new EntitledSubPanelTable(width,height,colGroups,colSubs,lines);
			String titleKey = GuiKeys.GAME_ROUND_DESCRIPTION_MISC_TITLE;
			((EntitledSubPanelTable)innerPanel).setTitleKey(titleKey,true);
		}
		else
			innerPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		add((SubPanel)innerPanel);
		
		// icons
		ArrayList<String> keys = new ArrayList<String>();
		if(showTitle)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE);
		if(showPack)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK);
		if(showSource)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE);
		if(showAuthor)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR);
		if(showInstance)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE);
		if(showTheme)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME);
		if(showDimension)
			keys.add(GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION);
		
		if(levelPreview!=null)
		{	// text
			ArrayList<String> values = new ArrayList<String>();
			if(showTitle)
				values.add(levelPreview.getTitle());
			if(showPack)
				values.add(levelPreview.getPack());
			if(showSource)
				values.add(levelPreview.getSource());
			if(showAuthor)
				values.add(levelPreview.getAuthor());
			if(showInstance)
				values.add(levelPreview.getInstanceName());
			if(showTheme)
				values.add(levelPreview.getThemeName());
			if(showDimension)
				values.add(Integer.toString(levelPreview.getVisibleHeight())+new Character('\u00D7').toString()+Integer.toString(levelPreview.getVisibleWidth()));
			
			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	innerPanel.setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					innerPanel.setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = values.get(line);
					String tooltip = text;
					innerPanel.setLabelText(line,colSub,text,tooltip);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					innerPanel.setLabelForeground(line,0,fg);
					Color bg;
					if(line==0 && !showPanelTitle)
						bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					innerPanel.setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		else
		{	for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	innerPanel.setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					innerPanel.setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					innerPanel.setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line==0 && !showPanelTitle)
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					innerPanel.setLabelBackground(line,colSub,bg);
					colSub++;
				}
			}
		}
		
		int maxWidth = width-3*GuiTools.subPanelMargin-innerPanel.getHeaderHeight();
		innerPanel.setColSubMaxWidth(1,maxWidth);
		innerPanel.setColSubPreferredWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showPanelTitle = true;
	private boolean showTitle = true;
	private boolean showPack = true;
	private boolean showSource = true;
	private boolean showAuthor = true;
	private boolean showInstance = true;
	private boolean showTheme = true;
	private boolean showDimension = true;

	public void setShowPanelTitle(boolean showPanelTitle)
	{	this.showPanelTitle = showPanelTitle;
		setLevelPreview(levelPreview,lines);
	}

	public void setShowPack(boolean showPack)
	{	this.showPack = showPack;
	}

	public void setShowTitle(boolean showTitle)
	{	this.showTitle = showTitle;
	}

	public void setShowSource(boolean showSource)
	{	this.showSource = showSource;
	}

	public void setShowAuthor(boolean showAuthor)
	{	this.showAuthor = showAuthor;
	}

	public void setShowInstance(boolean showInstance)
	{	this.showInstance = showInstance;
	}

	public void setShowTheme(boolean showTheme)
	{	this.showTheme = showTheme;
	}

	public void setShowDimension(boolean showDimension)
	{	this.showDimension = showDimension;
	}
}
