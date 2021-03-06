package org.totalboumboum.gui.common.content.subpanel.limits;

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
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * Panel displaying limits details.
 * 
 * @param <T>
 * 		Type of concerned limit.
 *  
 * @author Vincent Labatut
 */
public class LimitsSubPanel<T extends Limit> extends TableSubPanel implements MouseListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	/** Number of column groups */
	private static final int COL_GROUPS = 1;
	/** Number of columns by group */
	private static final int COL_SUBS = 2;
	/** Number of lines */
	private static final int LINES = 8;
	/** Gui key */
	private String prefix;

	/**
	 * Builds a new panel.
	 * 
	 * @param width
	 * 		Width of the panel.
	 * @param height
	 * 		Height of the panel.
	 * @param type
	 * 		Limit type.
	 */
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
	/** Limits displayed */
	private Limits<T> limits;
	/** Row currently selected */
	private int selectedRow;
	
	/**
	 * Returns the currently displayed limits.
	 * 
	 * @return
	 * 		Limits currently displayed.
	 */
	public Limits<T> getLimits()
	{	return limits;	
	}
	
	/**
	 * Changes the limits currently displayed.
	 * 
	 * @param limits
	 * 		New limits to display.
	 */
	public void setLimits(Limits<T> limits)
	{	this.limits = limits;
		selectedRow = -1;
		int colGroups = COL_GROUPS;
		
		if(limits!=null)
		{	// title
			{	Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
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
					Color fg = GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,colGroup,colSub,fg);
					Color bg = GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					MyLabel lbl = getLabel(line,colGroup,colSub);
					lbl.addMouseListener(this);
					lbl.setMouseSensitive(true);
					colSub++;
				}
				{	String text = value;
					String tooltip = value;
					setLabelText(line,colGroup,colSub,text,tooltip);
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
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
			Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
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
		int textWidth = getDataWidth()-(COL_SUBS-1)*GuiSizeTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,textWidth);
		setColSubPrefWidth(1,textWidth);
		setColSubMaxWidth(1,textWidth);
	}

	/**
	 * Changes limit selection.
	 * 
	 * @param row
	 * 		New selection.
	 */
	private void selectLimit(int row)
	{	// paint line
		selectedRow = row;
		setLabelBackground(selectedRow,0,GuiColorTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND);
		setLabelBackground(selectedRow,1,GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND);
		// update listeners
		fireLimitSelectionChange();
	}
	
	/**
	 * Returns the currently selected limit.
	 * 
	 * @return
	 * 		Currently selected limit.
	 */
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
		
		// init
		MyLabel label = (MyLabel)e.getComponent();
		int[] pos = getLabelPositionMultiple(label);
		// unselect
		if(selectedRow!=-1)
		{	setLabelBackground(selectedRow,0,GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
			setLabelBackground(selectedRow,1,GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedRow = -1;
		}		
		// select
		selectLimit(pos[0]);
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	// 
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Objects currently listening to this panel */
	private List<LimitsSubPanelListener> listeners = new ArrayList<LimitsSubPanelListener>();
	
	/**
	 * Registers a new listener to this object.
	 * 
	 * @param listener
	 * 		New listener.
	 */
	public void addListener(LimitsSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	/**
	 * Unregister an existing listener from this object.
	 * 
	 * @param listener
	 * 		Listener to unregister.
	 */
	public void removeListener(LimitsSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	/**
	 * Spread the fact the selection changed.
	 */
	private void fireLimitSelectionChange()
	{	for(LimitsSubPanelListener listener: listeners)
			listener.limitSelectionChanged();
	}

	/**
	 * Spread the fact the mouse was pressed.
	 * 
	 * @param e
	 * 		Corresponding mouse event.
	 */
	private void fireMousePressed(MouseEvent e)
	{	for(LimitsSubPanelListener listener: listeners)
			listener.mousePressed(e);
	}
}
