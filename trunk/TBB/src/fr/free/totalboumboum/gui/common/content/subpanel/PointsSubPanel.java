package fr.free.totalboumboum.gui.common.content.subpanel;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.game.points.PointsConstant;
import fr.free.totalboumboum.game.points.PointsDiscretize;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsRankings;
import fr.free.totalboumboum.game.points.PointsRankpoints;
import fr.free.totalboumboum.game.points.PointsScores;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class PointsSubPanel extends EntitledSubPanelTable
{	private static final long serialVersionUID = 1L;
	
	private PointsProcessor pointsProcessor;

	private String prefix;

	public PointsSubPanel(int width, int height, PointsProcessor pointsProcessor, String prefix)
	{	super(width,height,1,1,1);
		
		// init
		this.prefix = GuiKeys.COMMON_POINTS+prefix;
		
		// title
		String titleKey = prefix+GuiKeys.TITLE;
		setTitleKey(titleKey,true);
		
		setPointsProcessor(pointsProcessor);
	}
		
	/////////////////////////////////////////////////////////////////
	// POINTS PROCESSOR	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public PointsProcessor getPointsProcessor()
	{	return pointsProcessor;	
	}
	
	public void setPointsProcessor(PointsProcessor pointsProcessor)
	{	this.pointsProcessor = pointsProcessor;
		// init data
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		if(pointsProcessor!=null)
			makePointsProcessorPanel(pointsProcessor,data,tooltips);
		
		int lines = Math.max(8,data.size());
		int colSubs = 2;
		int colGroups = 1;

		setNewTable(colGroups,colSubs,lines);
		getTable().setColSubMaxWidth(1,Integer.MAX_VALUE);
		
		Iterator<ArrayList<Object>> dt = data.iterator();
		Iterator<ArrayList<String>> tt = tooltips.iterator();
		int line = 0;
		int colGroup = 0;
		while(dt.hasNext())
		{	// init
			ArrayList<Object> tempDt = dt.next();
			ArrayList<String> tempTt = tt.next();
			int colSub = 0;
			// left
			{	String tooltip = tempTt.get(colSub);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				getTable().setLabelForeground(line,colGroup,colSub,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				getTable().setLabelBackground(line,colGroup,colSub,bg);
				if(tempDt.get(colSub) instanceof BufferedImage)
				{	BufferedImage image = (BufferedImage)tempDt.get(colSub);
					getTable().setLabelIcon(line,colGroup,colSub,image,tooltip);
				}
				else
				{	String text = (String)tempDt.get(colSub);
					getTable().setLabelText(line,colGroup,colSub,text,tooltip);
				}
				colSub++;
			}
			// right
			{	String tooltip = tempTt.get(colSub);
				if(tempDt.get(colSub) instanceof BufferedImage)
				{	BufferedImage image = (BufferedImage)tempDt.get(colSub);
					getTable().setLabelIcon(line,colGroup,colSub,image,tooltip);
				}
				else
				{	String text = (String)tempDt.get(colSub);
					getTable().setLabelText(line,colGroup,colSub,text,tooltip);
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				getTable().setLabelBackground(line,colGroup,colSub,bg);
				colSub++;
			}
			line++;
			if(line==lines)
			{	line = 0;
				colGroup++;
			}
		}
//		validate();
//		repaint();
	}
	
	private void makePointsProcessorPanel(PointsProcessor pp, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
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
	
	private void makePointsRankpointsPanel(PointsRankpoints pr, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		// this pp
		{	ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
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
				name = prefix+GuiKeys.DATA+GuiKeys.NO_SHARE;
			image = GuiTools.getIcon(name);
			tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name+GuiKeys.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
		}
		// source
		{	PointsRankings prk = pr.getSource();
			ArrayList<PointsProcessor> sources = prk.getSources();
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
				ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				dt.add(nbr);
				tt.add(tooltip);
				dt.add(value);
				tt.add(tooltip);			
			}
		}		
	}

	private void makePointsDiscretizePanel(PointsDiscretize pd, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		
		// this pp
		{	ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
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

	private void makePointsRankingsPanel(PointsRankings pr, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	// this PP
		{	boolean inverted = pr.isInverted();
			ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
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
		}
		// sources
		{	ArrayList<PointsProcessor> sources = pr.getSources();
			Iterator<PointsProcessor> i = sources.iterator();
			while(i.hasNext())
			{	PointsProcessor source = i.next();
				makePointsProcessorPanel(source,data,tooltips);
			}
		}
	}

	private void makePointsConstantPanel(PointsConstant pc, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		
		float value = pc.getValue();
		ArrayList<Object> dt = new ArrayList<Object>();
		ArrayList<String> tt = new ArrayList<String>();
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

	private void makePointsTotalPanel(PointsTotal pt, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	ArrayList<Object> dt = new ArrayList<Object>();
		ArrayList<String> tt = new ArrayList<String>();
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

	private void makePointsScoresPanel(PointsScores ps, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	Score score = ps.getScore();
		ArrayList<Object> dt = new ArrayList<Object>();
		ArrayList<String> tt = new ArrayList<String>();
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
