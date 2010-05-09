package fr.free.totalboumboum.gui.game.round.description;

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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.Map.Entry;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewer;
import fr.free.totalboumboum.engine.container.zone.Zone;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.LimitPoints;
import fr.free.totalboumboum.game.limit.LimitScore;
import fr.free.totalboumboum.game.limit.LimitTime;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.points.PointsConstant;
import fr.free.totalboumboum.game.points.PointsDiscretize;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsRankings;
import fr.free.totalboumboum.game.points.PointsRankpoints;
import fr.free.totalboumboum.game.points.PointsScores;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public RoundDescription(SplitMenuPanel container, int w, int h)
	{	super(container,w,h);
	
		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_DESCRIPTION_TITLE);
		setTitle(txt);
	
		// data
		{	Round round = getConfiguration().getCurrentRound();
			HollowLevel hollowLevel = round.getHollowLevel();
			LevelPreview preview = null;
			try
			{	preview = LevelPreviewer.previewLevel(hollowLevel.getLevelFolder());
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
			JPanel infoPanel = new JPanel();
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			int width = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_WIDTH);
			int height = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT);
			int margin = GuiTools.getSize(GuiTools.GAME_DATA_MARGIN_SIZE);
			int leftWidth = (int)(width*0.4); 
			int rightWidth = width - leftWidth - margin; 
			Dimension dim = new Dimension(width,height);
			infoPanel.setPreferredSize(dim);
			infoPanel.setMinimumSize(dim);
			infoPanel.setMaximumSize(dim);
			infoPanel.setOpaque(false);
			// left panel
			{	JPanel leftPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(leftPanel,BoxLayout.PAGE_AXIS); 
					leftPanel.setLayout(layout);
				}
				leftPanel.setOpaque(false);
				dim = new Dimension(leftWidth,height);
				leftPanel.setPreferredSize(dim);
				leftPanel.setMinimumSize(dim);
				leftPanel.setMaximumSize(dim);
				// preview label
				{	int innerHeight = leftWidth;
					JLabel previewLabel = makePreviewLabel(leftWidth,innerHeight,preview);
					previewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(previewLabel);
				}
				//
				leftPanel.add(Box.createVerticalGlue());
				// itemset panel
				{	int innerHeight = height - leftWidth - margin;
					JPanel itemsetPanel = makeItemsetPanel(leftWidth,innerHeight,preview);
					itemsetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(itemsetPanel);
				}
				//
				infoPanel.add(leftPanel);
			}
			//
			infoPanel.add(Box.createHorizontalGlue());
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				dim = new Dimension(rightWidth,height);
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				int upHeight = (height - margin)/2;
				int downHeight = height - upHeight - margin;
				// up panel
				{	JPanel upPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(upPanel,BoxLayout.LINE_AXIS); 
						upPanel.setLayout(layout);
					}
					upPanel.setOpaque(false);
					dim = new Dimension(rightWidth,upHeight);
					upPanel.setPreferredSize(dim);
					upPanel.setMinimumSize(dim);
					upPanel.setMaximumSize(dim);
					int innerWidth = (rightWidth - margin)/2;
					// misc panel
					{	JPanel miscPanel = makeMiscPanel(innerWidth,upHeight,preview);
						upPanel.add(miscPanel);
					}
					upPanel.add(Box.createHorizontalGlue());
					// initial items panel
					{	JPanel initialItemsPanel = makeInitialItemsPanel(innerWidth,upHeight,preview);
						upPanel.add(initialItemsPanel);
					}
					rightPanel.add(upPanel);
				}
				//
				rightPanel.add(Box.createVerticalGlue());
				// down panel
				{	JPanel downPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(downPanel,BoxLayout.LINE_AXIS); 
						downPanel.setLayout(layout);
					}
					downPanel.setOpaque(false);
					dim = new Dimension(rightWidth,upHeight);
					downPanel.setPreferredSize(dim);
					downPanel.setMinimumSize(dim);
					downPanel.setMaximumSize(dim);
					int innerWidth = (rightWidth - margin)/2;
					// points panel
					{	JPanel pointsPanel = makePointsPanel(innerWidth,downHeight);
						downPanel.add(pointsPanel);
					}
					downPanel.add(Box.createHorizontalGlue());
					// limits panel
					{	JPanel limitsPanel = makeLimitsPanel(innerWidth,downHeight);
						downPanel.add(limitsPanel);
					}
					rightPanel.add(downPanel);
				}
				//
				infoPanel.add(rightPanel);
			}
			//
			setDataPanel(infoPanel);
		}
	}

	private JLabel makePreviewLabel(int width, int height, LevelPreview levelPreview)
	{	// init
		String txt = "No preview";
		JLabel result = new JLabel(txt);
		Dimension dim = new Dimension(width,height);
		result.setPreferredSize(dim);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setVerticalAlignment(SwingConstants.CENTER);
		result.setFont(getConfiguration().getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_RESULTS_HEADER_FONT_SIZE)));
		result.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
		result.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
		result.setOpaque(true);
		// image
		BufferedImage img = levelPreview.getVisualPreview(); 
		if(img!=null)
		{	float zoomX = width/(float)img.getWidth();
			float zoomY = height/(float)img.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			img = ImageTools.resize(img,zoom,true);
			ImageIcon icon = new ImageIcon(img);
			result.setIcon(icon);
			result.setText(null);
		}
		//
		return result;
	}
	
	private JPanel makeItemsetPanel(int width, int height, LevelPreview levelPreview)
	{	// init
		String id = GuiTools.GAME_ROUND_DESCRIPTION_ITEMSET_TITLE;
		int colGrps[] = {5, 6};
		int lns[] = {4, 5};
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		for(int i=0;i<lns.length;i++)
		{	float line = (int)((height-margin*(lns[i]+1))/lns[i]);
			colGrps[i] = (int)(width/line/2);
		}
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		HashMap<String,BufferedImage> itemsetPreview = levelPreview.getItemsetPreview();
		Zone zone = getConfiguration().getCurrentRound().getHollowLevel().getZone();
		HashMap<String,Integer> itemList = zone.getItemCount();
		Iterator<Entry<String,BufferedImage>> i = itemsetPreview.entrySet().iterator();
		if(!i.hasNext())
		{	ArrayList<?> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			// icon
			dt.add(null);
			tt.add(null);
			// value
			dt.add(null);
			tt.add(null);
		}
		while(i.hasNext())
		{	// init
			Entry<String,BufferedImage> temp = i.next();
			String name = temp.getKey();
			BufferedImage image = temp.getValue();
			int number = 0;
			if(itemList.containsKey(name))
				number = itemList.get(name);
			String tooltip;
			tooltip = name+": "+number;				
			if(number==0)
			{	image = ImageTools.getGreyScale(image);
			}
			// lists
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			dt.add(image);
			tt.add(tooltip);
			String value = Integer.toString(number);
			dt.add(value);
			tt.add(tooltip);			
		}			

		EntitledSubPanel itemsetPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,true);
		return itemsetPanel;
	}

	private JPanel makeMiscPanel(int width, int height, LevelPreview preview)
	{	// init
		String id = GuiTools.GAME_ROUND_DESCRIPTION_MISC_TITLE;
		int colGrps[] = {1};
		int lns[] = {8};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		String names[] = 
		{	GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK,
			GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE,
			GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE,
			GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR,
			GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE,
			GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME,
			GuiTools.GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION
		};
		Round round = getConfiguration().getCurrentRound();
		HollowLevel hollowLevel = round.getHollowLevel();
		String texts[] = 
		{	hollowLevel.getPackName(),
			preview.getTitle(),
			preview.getSource(),
			preview.getAuthor(),
			hollowLevel.getInstanceName(),
			hollowLevel.getThemeName(),
			Integer.toString(hollowLevel.getVisibleHeight())+new Character('\u00D7').toString()+Integer.toString(hollowLevel.getVisibleWidth())
		};
		for(int i=0;i<names.length;i++)
		{	// lists
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			BufferedImage image = GuiTools.getIcon(names[i]);
			dt.add(image);
			String tooltip = getConfiguration().getLanguage().getText(names[i]+GuiTools.TOOLTIP);
			tt.add(tooltip);
			dt.add(texts[i]);
			tt.add(texts[i]);
		}		
		
		EntitledSubPanelTable miscPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,true);
		return miscPanel;
	}

	private JPanel makeInitialItemsPanel(int width, int height, LevelPreview levelPreview)
	{	// init
		String id = GuiTools.GAME_ROUND_DESCRIPTION_INITIALITEMS_TITLE;
		int colGrps[] = {2, 4};
		int lns[] = {4, 8};
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		for(int i=0;i<lns.length;i++)
		{	float line = (int)((height-margin*(lns[i]+1))/lns[i]);
			colGrps[i] = (int)(width/line/2);
		}
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		HashMap<String,BufferedImage> itemsetPreview = levelPreview.getItemsetPreview();
		HashMap<String,Integer> initialItems = levelPreview.getInitialItems();
		Iterator<Entry<String,Integer>> i = initialItems.entrySet().iterator();
		if(!i.hasNext())
		{	ArrayList<?> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			// icon
			dt.add(null);
			tt.add(null);
			// value
			dt.add(null);
			tt.add(null);
		}
		while(i.hasNext())
		{	// init
			Entry<String,Integer> temp = i.next();
			String name = temp.getKey();
			int number = temp.getValue();
			BufferedImage image = itemsetPreview.get(name);
			String tooltip = name+": "+number;
			// lists
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			dt.add(image);
			tt.add(tooltip);
			String value = Integer.toString(number);
			dt.add(value);
			tt.add(tooltip);			
		}			
		
		// result
		EntitledSubPanelTable itemsPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,true);
		return itemsPanel;
	}

	private JPanel makePointsPanel(int width, int height)
	{	// init
		String id = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_TITLE;
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		Round round = getConfiguration().getCurrentRound();
		PointsProcessor mainPP = round.getPointProcessor();
		makePointsPanelRec(mainPP,data,tooltips,getConfiguration());

		int n = 6;
		if(data.size()<6)
			n = 6;
		else if(data.size()<10)
			n = data.size();
		else
			n = 10;
		int colGrps[] = {1};
		int lns[] = {n};

		EntitledSubPanelTable pointsPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,false);
		return pointsPanel;
	}
	public static void makePointsPanelRec(PointsProcessor pp, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips, GuiConfiguration configuration)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		
		// rankpoints
		if(pp instanceof PointsRankpoints)
		{	PointsRankpoints pr = (PointsRankpoints) pp;
			// this pp
			{	ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				String name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKPOINTS;
				BufferedImage image = GuiTools.getIcon(name);
				String tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
				dt.add(image);
				tt.add(tooltip);
				boolean exaequoShare = pr.getExaequoShare();
				if(exaequoShare)
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_SHARE;
				else
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_NOSHARE;
				image = GuiTools.getIcon(name);
				tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
				dt.add(image);
				tt.add(tooltip);
			}
			// source
			{	PointsRankings prk = pr.getSource();
				ArrayList<PointsProcessor> sources = prk.getSources();
				Iterator<PointsProcessor> i = sources.iterator();
				while(i.hasNext())
				{	PointsProcessor source = i.next();
					makePointsPanelRec(source,data,tooltips,configuration);
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
		// discretize
		else if(pp instanceof PointsDiscretize)
		{	PointsDiscretize pd = (PointsDiscretize) pp;
			// this pp
			{	ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				String name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_HEADER_DISCRETIZE;
				BufferedImage image = GuiTools.getIcon(name);
				String tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
				dt.add(image);
				tt.add(tooltip);				
				String text = "";
				dt.add(text);
				tt.add(tooltip);				
			}
			// source
			{	PointsProcessor source = pd.getSource();
				makePointsPanelRec(source,data,tooltips,configuration);
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
		// rankings
		else if(pp instanceof PointsRankings)
		{	PointsRankings pr = (PointsRankings) pp;
			// this PP
			{	boolean inverted = pr.isInverted();
				ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				String name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKINGS;
				BufferedImage image = GuiTools.getIcon(name);
				String tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
				if(inverted)
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_INVERTED;
				else
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_REGULAR;
				dt.add(image);
				tt.add(tooltip);
			}
			// sources
			{	ArrayList<PointsProcessor> sources = pr.getSources();
				Iterator<PointsProcessor> i = sources.iterator();
				while(i.hasNext())
				{	PointsProcessor source = i.next();
					makePointsPanelRec(source,data,tooltips,configuration);
				}
			}
		}
		// constant
		else if(pp instanceof PointsConstant)
		{	PointsConstant pc = (PointsConstant) pp;
			float value = pc.getValue();
			ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			String name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_HEADER_CONSTANT;
			BufferedImage image = GuiTools.getIcon(name);
			String tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
			String text = nf.format(value);
			dt.add(text);
			tt.add(text);
		}
		// total
		else if(pp instanceof PointsTotal)
		{	//PointsTotal pt = (PointsTotal) pp;
			ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			String name = GuiTools.GAME_MATCH_DESCRIPTION_POINTS_HEADER_TOTAL;
			BufferedImage image = GuiTools.getIcon(name);
			String tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
			String text = "";
			dt.add(text);
			tt.add(tooltip);
		}
		// scores
		else if(pp instanceof PointsScores)
		{	PointsScores ps = (PointsScores) pp;
			Score score = ps.getScore();
			ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			String name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_HEADER_SCORE;
			BufferedImage image = GuiTools.getIcon(name);
			String tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
			name = null;
			switch(score)
			{	case BOMBS:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_BOMBS;
					break;
				case CROWNS:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_CROWNS;
					break;					
				case DEATHS:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_DEATHS;
					break;					
				case ITEMS:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_ITEMS;
					break;					
				case KILLS:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_KILLS;
					break;					
				case PAINTINGS:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_PAINTINGS;
					break;					
				case TIME:
					name = GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_TIME;
					break;					
			}
			image = GuiTools.getIcon(name);
			tooltip = configuration.getLanguage().getText(name+GuiTools.TOOLTIP);
			dt.add(image);
			tt.add(tooltip);
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
			String tooltip = configuration.getLanguage().getText(GuiTools.GAME_ROUND_DESCRIPTION_POINTS_DATA_PARTIAL+GuiTools.TOOLTIP);
			dt.add(icon);
			tt.add(tooltip);
			// text
			String text = pp.toString();
			dt.add(text);
			tt.add(text);
		}
	}
	
	private JPanel makeLimitsPanel(int width, int height)
	{	// init
		String id = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_TITLE;
		int colGrps[] = {1,2};
		int lns[] = {8, 8};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		Round round = getConfiguration().getCurrentRound();
		Limits<RoundLimit> limitsList = round.getLimits();
		Iterator<RoundLimit> i = limitsList.iterator();
		if(!i.hasNext())
		{	ArrayList<?> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			// icon
			dt.add(null);
			tt.add(null);
			// value
			dt.add(null);
			tt.add(null);
		}
		while(i.hasNext())
		{	// init
			Limit limit = i.next();
			String iconName = null;
			String value = null;
			if(limit instanceof LimitTime)
			{	LimitTime l = (LimitTime)limit;
				iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_TIME;
				value = StringTools.formatTimeWithSeconds(l.getLimit());
			}
			else if(limit instanceof LimitPoints)
			{	LimitPoints l = (LimitPoints)limit;
				iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CUSTOM;
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitScore)
			{	LimitScore l = (LimitScore) limit;
				switch(l.getScore())
				{	case BOMBS:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_BOMBS;
						value = nf.format(l.getLimit());
						break;
					case CROWNS:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CROWNS;
						value = nf.format(l.getLimit());
						break;
					case DEATHS:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_DEATHS;
						value = nf.format(l.getLimit());
						break;
					case ITEMS:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_ITEMS;
						value = nf.format(l.getLimit());
						break;
					case KILLS:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_KILLS;
						value = nf.format(l.getLimit());
						break;
					case PAINTINGS:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_PAINTINGS;
						value = nf.format(l.getLimit());
						break;
					case TIME:
						iconName = GuiTools.GAME_ROUND_DESCRIPTION_LIMIT_HEADER_TIME;
						value = nf.format(l.getLimit());
						break;
				}
			}
			// lists
			String tooltip = getConfiguration().getLanguage().getText(iconName+GuiTools.TOOLTIP);
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			BufferedImage icon = GuiTools.getIcon(iconName);
			dt.add(icon);
			tt.add(tooltip);
			dt.add(value);
			tt.add(value);			
		}			
			
		// result
		EntitledSubPanelTable limitsPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,true);
		return limitsPanel;
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
}
