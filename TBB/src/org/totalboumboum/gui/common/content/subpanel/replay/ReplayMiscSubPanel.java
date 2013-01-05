package org.totalboumboum.gui.common.content.subpanel.replay;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.file.replay.FileClientStream;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ReplayMiscSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public ReplayMiscSubPanel(int width, int height, int lines)
	{	super(width,height,SubPanel.Mode.BORDER,lines,COL_GROUPS,COL_SUBS,true);
		
		setReplay(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ARCHIVE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private FileClientStream replay;

	public FileClientStream getReplay()
	{	return replay;	
	}
	
	public void setReplay(FileClientStream replay)
	{	this.replay = replay;
		
		// sizes
		reinit(getLineCount(),COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_REPLAY_NAME);
		if(showPack)
			keys.add(GuiKeys.COMMON_REPLAY_PACK);
		if(showSave)
			keys.add(GuiKeys.COMMON_REPLAY_SAVE);
		
		if(replay!=null)
		{	// text
			List<String> textValues = new ArrayList<String>();
			List<String> tooltipValues = new ArrayList<String>();
			if(showName)
			{	textValues.add(replay.getLevelName());
				tooltipValues.add(replay.getLevelName());
			}
			if(showPack)
			{	textValues.add(replay.getLevelPack());
				tooltipValues.add(replay.getLevelPack());
			}
			if(showSave)
			{	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String save = sdf.format(replay.getSaveDate());
				textValues.add(save);
				tooltipValues.add(save);
			}

			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = textValues.get(line);
					String tooltip = tooltipValues.get(line);
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(line>0)
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
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
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line>0)
						bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					else
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
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
	private boolean showPack = true;
	private boolean showSave = true;

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowPack(boolean showPack)
	{	this.showPack = showPack;
	}

	public void setShowSave(boolean showSave)
	{	this.showSave = showSave;
	}

}
