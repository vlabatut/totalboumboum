package org.totalboumboum.gui.common.content.subpanel.limits;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.game.limit.Limit;
import org.totalboumboum.game.limit.LimitConfrontation;
import org.totalboumboum.game.limit.LimitLastStanding;
import org.totalboumboum.game.limit.LimitPoints;
import org.totalboumboum.game.limit.LimitScore;
import org.totalboumboum.game.limit.LimitTime;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LimitsSubPanel<T extends Limit> extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int COL_GROUPS = 1;
	private static final int COL_SUBS = 2;
	private static final int LINES = 8;

	private String prefix;

	public LimitsSubPanel(int width, int height, String type)
	{	super(width,height,SubPanel.Mode.TITLE,LINES,COL_GROUPS,COL_SUBS,false);
		
		// init	
		this.prefix = GuiKeys.COMMON_LIMIT+type;
		
		// title
		String titleKey = prefix+GuiKeys.TITLE;
		setTitleKey(titleKey,true);
		
		// limits
		setLimits(null);
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<T> limits;
	private int selectedRow;
	
	public Limits<T> getLimits()
	{	return limits;	
	}
	
	public void setLimits(Limits<T> limits)
	{	this.limits = limits;
		selectedRow = -1;
		int colGroups = COL_GROUPS;
		
		if(limits!=null)
		{	// title
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				setTitleBackground(bg);
			}
	
			// data
			if(limits.size()>LINES*colGroups)
				colGroups++;
			reinit(LINES,colGroups,COL_SUBS);
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(0);
			Iterator<T> i = limits.iterator();
			String headerPrefix = prefix+GuiKeys.HEADER;
			int line = 0;
			int colGroup = 0;
			
			while(i.hasNext() && colGroup<getColGroupCount())
			{	Limit limit = i.next();
				String iconName = null;
				String value = null;
				if(limit instanceof LimitConfrontation)
				{	LimitConfrontation l = (LimitConfrontation)limit;
					iconName = headerPrefix+GuiKeys.CONFRONTATIONS;
					value = nf.format(l.getThreshold());
				}
				else if(limit instanceof LimitTime)
				{	LimitTime l = (LimitTime)limit;
					iconName = headerPrefix+GuiKeys.TIME;
					value = TimeTools.formatTime(l.getThreshold(),TimeUnit.SECOND,TimeUnit.MILLISECOND,false);
				}
				else if(limit instanceof LimitPoints)
				{	LimitPoints l = (LimitPoints)limit;
					iconName = headerPrefix+GuiKeys.CUSTOM;
					value = nf.format(l.getThreshold());
				}
				else if(limit instanceof LimitLastStanding)
				{	LimitLastStanding l = (LimitLastStanding)limit;
					iconName = headerPrefix+GuiKeys.LAST_STANDING;
					value = nf.format(l.getThreshold());
				}
				else if(limit instanceof LimitScore)
				{	LimitScore l = (LimitScore) limit;
					switch(l.getScore())
					{	case BOMBS:
							iconName = headerPrefix+GuiKeys.BOMBS;
							value = nf.format(l.getThreshold());
							break;
						case CROWNS:
							iconName = headerPrefix+GuiKeys.CROWNS;
							value = nf.format(l.getThreshold());
							break;
						case BOMBEDS:
							iconName = headerPrefix+GuiKeys.BOMBEDS;
							value = nf.format(l.getThreshold());
							break;
						case ITEMS:
							iconName = headerPrefix+GuiKeys.ITEMS;
							value = nf.format(l.getThreshold());
							break;
						case BOMBINGS:
							iconName = headerPrefix+GuiKeys.BOMBINGS;
							value = nf.format(l.getThreshold());
							break;
						case PAINTINGS:
							iconName = headerPrefix+GuiKeys.PAINTINGS;
							value = nf.format(l.getThreshold());
							break;
						case SELF_BOMBINGS:
							iconName = headerPrefix+GuiKeys.SELF+GuiKeys.BOMBINGS;
							value = nf.format(l.getThreshold());
							break;
						case TIME:
							iconName = headerPrefix+GuiKeys.TIME;
							value = nf.format(l.getThreshold());
							break;
					}
				}
				//
				int colSub = 0;
				{	setLabelKey(line,colGroup,colSub,iconName,true);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,colGroup,colSub,fg);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					MyLabel lbl = getLabel(line,colGroup,colSub);
					lbl.addMouseListener(this);
					lbl.setMouseSensitive(true);
					colSub++;
				}
				{	String text = value;
					String tooltip = value;
					setLabelText(line,colGroup,colSub,text,tooltip);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					MyLabel lbl = getLabel(line,colGroup,colSub);
					lbl.addMouseListener(this);
					lbl.setMouseSensitive(true);
					colSub++;
				}
				line++;
				if(line==getLineCount())
				{	line = 0;
					colGroup++;
				}
			}
			
			selectLimit(0);
		}
		else
		{	// title
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			setTitleBackground(bg);			
			
			// empty lines
			reinit(LINES,colGroups,COL_SUBS);
			
			fireLimitSelectionChange();
		}

		// col widths
		int iconWidth = getLineHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int textWidth = getDataWidth()-(COL_SUBS-1)*GuiTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,textWidth);
		setColSubPrefWidth(1,textWidth);
		setColSubMaxWidth(1,textWidth);
}

	private void selectLimit(int row)
	{	// paint line
		selectedRow = row;
		setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND);
		setLabelBackground(selectedRow,1,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		// update listeners
		fireLimitSelectionChange();
	}
	
	public Limit getSelectedLimit()
	{	Limit result = null;
		if(selectedRow>-1 && limits!=null)
			result = limits.getLimit(selectedRow);
		return result;
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
	{	// init
		MyLabel label = (MyLabel)e.getComponent();
		int[] pos = getLabelPositionMultiple(label);
		// unselect
		if(selectedRow!=-1)
		{	setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
			setLabelBackground(selectedRow,1,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedRow = -1;
		}		
		// select
		selectLimit(pos[0]);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<LimitsSubPanelListener> listeners = new ArrayList<LimitsSubPanelListener>();
	
	public void addListener(LimitsSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(LimitsSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireLimitSelectionChange()
	{	for(LimitsSubPanelListener listener: listeners)
			listener.limitSelectionChanged();
	}
}
