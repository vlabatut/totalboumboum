package org.totalboumboum.gui.common.content.subpanel.points;

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
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.game.points.PointsConstant;
import org.totalboumboum.game.points.PointsDiscretize;
import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.game.points.PointsRankings;
import org.totalboumboum.game.points.PointsRankpoints;
import org.totalboumboum.game.points.PointsScores;
import org.totalboumboum.game.points.PointsTotal;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.detailed.Score;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PointsSubPanel extends TableSubPanel
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 8;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	private String prefix;

	public PointsSubPanel(int width, int height, String type)
	{	super(width,height,SubPanel.Mode.TITLE,LINES,COL_GROUPS,COL_SUBS,false);
		
		// init
		this.prefix = GuiKeys.COMMON_POINTS+type;
		
		// title
		String titleKey = prefix+GuiKeys.TITLE;
		setTitleKey(titleKey,true);
		
		setPointsProcessor(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// POINTS PROCESSOR	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor pointsProcessor;

	public PointsProcessor getPointsProcessor()
	{	return pointsProcessor;	
	}
	
	public void setPointsProcessor(PointsProcessor pointsProcessor)
	{	this.pointsProcessor = pointsProcessor;
		// init data
		ArrayList<List<Object>> data = new ArrayList<List<Object>>();
		ArrayList<List<String>> tooltips = new ArrayList<List<String>>();
		if(pointsProcessor!=null)
			makePointsProcessorPanel(pointsProcessor,data,tooltips);
		
		int lines = Math.max(LINES,data.size());
		reinit(lines,COL_GROUPS,COL_SUBS);

		if(pointsProcessor==null)
		{	// title
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			setTitleBackground(bg);
		}
		else
		{	// title
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				setTitleBackground(bg);
			}
		
			// data
			Iterator<List<Object>> dt = data.iterator();
			Iterator<List<String>> tt = tooltips.iterator();
			int line = 0;
			int colGroup = 0;
			while(dt.hasNext())
			{	// init
				List<Object> tempDt = dt.next();
				List<String> tempTt = tt.next();
				int colSub = 0;
				// left
				{	String tooltip = tempTt.get(colSub);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,colGroup,colSub,fg);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					if(tempDt.get(colSub) instanceof BufferedImage)
					{	BufferedImage image = (BufferedImage)tempDt.get(colSub);
						setLabelIcon(line,colGroup,colSub,image,tooltip);
					}
					else
					{	String text = (String)tempDt.get(colSub);
						setLabelText(line,colGroup,colSub,text,tooltip);
					}
					colSub++;
				}
				// right
				{	String tooltip = tempTt.get(colSub);
					if(tempDt.get(colSub) instanceof BufferedImage)
					{	BufferedImage image = (BufferedImage)tempDt.get(colSub);
						setLabelIcon(line,colGroup,colSub,image,tooltip);
					}
					else
					{	String text = (String)tempDt.get(colSub);
						setLabelText(line,colGroup,colSub,text,tooltip);
					}
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colGroup,colSub,bg);
					colSub++;
				}
				line++;
				if(line==lines)
				{	line = 0;
					colGroup++;
				}
			}
		}

		// col widths
		int iconWidth = getLineHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int textWidth = getDataWidth() - (COL_SUBS-1)*GuiTools.subPanelMargin - iconWidth;
		setColSubMinWidth(1,textWidth);
		setColSubPrefWidth(1,textWidth);
		setColSubMaxWidth(1,textWidth);

//		validate();
//		repaint();
	}
	
	private void makePointsProcessorPanel(PointsProcessor pp, List<List<Object>> data, List<List<String>> tooltips)
	{	// rankpoints
		if(pp instanceof PointsRankpoints)
		{	PointsRankpoints pr = (PointsRankpoints) pp;
			makePointsRankpointsPanel(pr,data,tooltips);
		}
		// discretize
		else if(pp instanceof PointsDiscretize)
		{	PointsDiscretize pd = (PointsDiscretize) pp;
			makePointsDiscretizePanel(pd,data,tooltips);
		}
		// rankings
		else if(pp instanceof PointsRankings)
		{	PointsRankings pr = (PointsRankings) pp;
			makePointsRankingsPanel(pr,data,tooltips);
		}
		// constant
		else if(pp instanceof PointsConstant)
		{	PointsConstant pc = (PointsConstant) pp;
			makePointsConstantPanel(pc,data,tooltips);
		}
		// total
		else if(pp instanceof PointsTotal)
		{	PointsTotal pt = (PointsTotal) pp;
			makePointsTotalPanel(pt,data,tooltips);
		}
		// scores
		else if(pp instanceof PointsScores)
		{	PointsScores ps = (PointsScores) pp;
			makePointsScoresPanel(ps,data,tooltips);
		}
		// others
		else
		{	ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			// icon
			//BufferedImage icon = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_PARTIAL);
			String icon = "-";
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(prefix+GuiKeys.DATA+GuiKeys.PARTIAL+GuiKeys.TOOLTIP);
			dt.add(icon);
			tt.add(tooltip);
			// text
			String text = pp.toString();
			dt.add(text);
			tt.add(text);
		}
	}
	
	private void makePointsRankpointsPanel(PointsRankpoints pr, List<List<Object>> data, List<List<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		// this pp
		{	List<Object> dt = new ArrayList<Object>();
		List<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			String name = prefix+GuiKeys.HEADER+GuiKeys.RANKPOINTS;
			BufferedImage image = GuiTools.getIcon(name);
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
			boolean exaequoShare = pr.getExaequoShare();
			if(exaequoShare)
				name = prefix+GuiKeys.DATA+GuiKeys.SHARE;
			else
				name = prefix+GuiKeys.DATA+GuiKeys.NO+GuiKeys.SHARE;
			image = GuiTools.getIcon(name);
			tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
		}
		// source
		{	PointsRankings prk = pr.getSource();
		List<PointsProcessor> sources = prk.getSources();
			Iterator<PointsProcessor> i = sources.iterator();
			while(i.hasNext())
			{	PointsProcessor source = i.next();
				makePointsProcessorPanel(source,data,tooltips);
			}
		}
		// values
		{	float[] values = pr.getValues();
			for(int i=0;i<values.length;i++)
			{	String nbr = "#"+(i+1); 
				String value = nf.format(values[i]);
				String tooltip = nbr+new Character('\u2192').toString()+value+"pts";
				List<Object> dt = new ArrayList<Object>();
				List<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				dt.add(nbr);
				tt.add(tooltip);
				dt.add(value);
				tt.add(tooltip);			
			}
		}		
	}

	private void makePointsDiscretizePanel(PointsDiscretize pd, List<List<Object>> data, List<List<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		
		// this pp
		{	List<Object> dt = new ArrayList<Object>();
			List<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			String name = prefix+GuiKeys.HEADER+GuiKeys.DISCRETIZE;
			BufferedImage image = GuiTools.getIcon(name);
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);				
			String text = "";
			dt.add(text);
			tt.add(tooltip);				
		}
		// source
		{	PointsProcessor source = pd.getSource();
			makePointsProcessorPanel(source,data,tooltips);
		}
		// values & thresholds
		{	float[] values = pd.getValues();
			float[] thresholds = pd.getThresholds();
			String previousThreshold;
			String threshold = new Character('\u2212').toString()+new Character('\u221E').toString();
			for(int i=0;i<values.length;i++)
			{	String value = nf.format(values[i]);
				previousThreshold = threshold; 
				threshold = nf.format(thresholds[i]);
				String interval;
				interval = "]"+previousThreshold+";"+threshold+"]";
				String tooltip = interval+new Character('\u2192').toString()+value;
				ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				dt.add(interval);
				tt.add(tooltip);
				dt.add(value);
				tt.add(tooltip);			
			}
		}
	}

	private void makePointsRankingsPanel(PointsRankings pr, List<List<Object>> data, List<List<String>> tooltips)
	{	// this PP
		{	boolean inverted = pr.isInverted();
			List<Object> dt = new ArrayList<Object>();
			List<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			String name = prefix+GuiKeys.HEADER+GuiKeys.RANKINGS;
			BufferedImage image = GuiTools.getIcon(name);
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
			if(inverted)
				name = prefix+GuiKeys.DATA+GuiKeys.INVERTED;
			else
				name = prefix+GuiKeys.DATA+GuiKeys.REGULAR;
			dt.add(image);
			tt.add(tooltip);
			String text = "";
			dt.add(text);
			tt.add(tooltip);
		}
		// sources
		{	List<PointsProcessor> sources = pr.getSources();
			Iterator<PointsProcessor> i = sources.iterator();
			while(i.hasNext())
			{	PointsProcessor source = i.next();
				makePointsProcessorPanel(source,data,tooltips);
			}
		}
	}

	private void makePointsConstantPanel(PointsConstant pc, List<List<Object>> data, List<List<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		
		float value = pc.getValue();
		List<Object> dt = new ArrayList<Object>();
		List<String> tt = new ArrayList<String>();
		data.add(dt);
		tooltips.add(tt);
		String name = prefix+GuiKeys.HEADER+GuiKeys.CONSTANT;
		BufferedImage image = GuiTools.getIcon(name);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
		dt.add(image);
		tt.add(tooltip);
		String text = nf.format(value);
		dt.add(text);
		tt.add(text);
	}

	private void makePointsTotalPanel(PointsTotal pt, List<List<Object>> data, List<List<String>> tooltips)
	{	List<Object> dt = new ArrayList<Object>();
		List<String> tt = new ArrayList<String>();
		data.add(dt);
		tooltips.add(tt);
		String name = prefix+GuiKeys.HEADER+GuiKeys.TOTAL;
		BufferedImage image = GuiTools.getIcon(name);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
		dt.add(image);
		tt.add(tooltip);
		String text = "";
		dt.add(text);
		tt.add(tooltip);
	}

	private void makePointsScoresPanel(PointsScores ps, List<List<Object>> data, List<List<String>> tooltips)
	{	Score score = ps.getScore();
		List<Object> dt = new ArrayList<Object>();
		List<String> tt = new ArrayList<String>();
		data.add(dt);
		tooltips.add(tt);
		String name = prefix+GuiKeys.HEADER+GuiKeys.SCORE;
		BufferedImage image = GuiTools.getIcon(name);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
		dt.add(image);
		tt.add(tooltip);
		name = null;
		switch(score)
		{	case BOMBS:
				name = prefix+GuiKeys.DATA+GuiKeys.BOMBS;
				break;
			case CROWNS:
				name = prefix+GuiKeys.DATA+GuiKeys.CROWNS;
				break;					
			case BOMBEDS:
				name = prefix+GuiKeys.DATA+GuiKeys.BOMBEDS;
				break;					
			case ITEMS:
				name = prefix+GuiKeys.DATA+GuiKeys.ITEMS;
				break;					
			case BOMBINGS:
				name = prefix+GuiKeys.DATA+GuiKeys.BOMBINGS;
				break;					
			case PAINTINGS:
				name = prefix+GuiKeys.DATA+GuiKeys.PAINTINGS;
				break;					
			case SELF_BOMBINGS:
				name = prefix+GuiKeys.DATA+GuiKeys.SELF+GuiKeys.BOMBINGS;
				break;					
			case TIME:
				name = prefix+GuiKeys.DATA+GuiKeys.TIME;
				break;					
		}
		image = GuiTools.getIcon(name);
		tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
		dt.add(image);
		tt.add(tooltip);
	}
}
