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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.RescaleOp;
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
import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.LimitPoints;
import fr.free.totalboumboum.game.limit.LimitScore;
import fr.free.totalboumboum.game.limit.LimitTime;
import fr.free.totalboumboum.game.limit.LimitTotal;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsConstant;
import fr.free.totalboumboum.game.points.PointsDiscretize;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsRankings;
import fr.free.totalboumboum.game.points.PointsRankpoints;
import fr.free.totalboumboum.game.points.PointsScores;
import fr.free.totalboumboum.game.points.PointsTotal;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.panel.data.InnerDataPanel;
import fr.free.totalboumboum.gui.common.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.panel.menu.SimpleMenuPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.menus.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.options.OptionsMenu;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class RoundDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;

	public RoundDescription(SplitMenuPanel container)
	{	super(container);
	
		// title
		String key = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_DESCRIPTION_TITLE);
		setTitleKey(key);
	
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
			SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}

			int margin = GuiTools.getSize(GuiTools.GAME_DATA_MARGIN_SIZE);
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			infoPanel.setOpaque(false);
			
			// left panel
			{	JPanel leftPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(leftPanel,BoxLayout.PAGE_AXIS); 
					leftPanel.setLayout(layout);
				}
				leftPanel.setOpaque(false);
				Dimension dim = new Dimension(leftWidth,dataHeight);
				leftPanel.setPreferredSize(dim);
				leftPanel.setMinimumSize(dim);
				leftPanel.setMaximumSize(dim);
				
				// preview label
				{	int innerHeight = leftWidth;
					JLabel previewLabel = makePreviewLabel(leftWidth,innerHeight,preview);
					previewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(previewLabel);
				}

				leftPanel.add(Box.createVerticalGlue());
				
				// itemset panel
				{	int innerHeight = dataHeight - leftWidth - margin;
					JPanel itemsetPanel = makeItemsetPanel(leftWidth,innerHeight,preview);
					itemsetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(itemsetPanel);
				}
				//
				infoPanel.add(leftPanel);
			}

			infoPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				Dimension dim = new Dimension(rightWidth,dataHeight);
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				int upHeight = (dataHeight - margin)/2;
				int downHeight = dataHeight - upHeight - margin;
				
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
					{	SubPanel limitsPanel = makeLimitsPanel(innerWidth,downHeight,round.getLimits(),"Round");
						downPanel.add(limitsPanel);
					}
					rightPanel.add(downPanel);
				}
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
		}
	}

	private JLabel makePreviewLabel(int width, int height, LevelPreview levelPreview)
	{	// init
		JLabel result = new JLabel();
		String text = "No preview";
		String tooltip = "Zone preview";
		result.setText(text);
		result.setToolTipText(tooltip);
		Dimension dim = new Dimension(width,height);
		result.setPreferredSize(dim);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setVerticalAlignment(SwingConstants.CENTER);
		int fontSize = GuiTools.getFontSize(width, height, text);
		Font font = getConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
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
		{	PointsTotal pt = (PointsTotal) pp;
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
	
	public static <T extends Limit>SubPanel makeLimitsPanel(int width, int height, Limits<T> limits, String prefix)
	{	// init
		prefix = "Game"+prefix+"DescriptionLimit";
		int lines = 8;
		int colSubs = 2;
		int colGroups = 1;
		if(limits.size()>lines*colGroups)
			colGroups = 2;
		
		EntitledSubPanelTable limitsPanel = new EntitledSubPanelTable(width,height,colGroups,colSubs,lines);
		String titleKey = prefix+"Title";
		limitsPanel.setTitleKey(titleKey,true);
		for(int colGroup=0;colGroup<colGroups;colGroup++)
			limitsPanel.getTable().setColumnWidth(colGroup,1,Integer.MAX_VALUE);
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		Iterator<T> i = limits.iterator();
		prefix = prefix+"Header";
		int line = 0;
		int colGroup = 0;
		while(i.hasNext() && colGroup<colGroups)
		{	Limit limit = i.next();
			String iconName = null;
			String value = null;
			if(limit instanceof LimitConfrontation)
			{	LimitConfrontation l = (LimitConfrontation)limit;
				iconName = prefix+"Confrontations";
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitTime)
			{	LimitTime l = (LimitTime)limit;
				iconName = prefix+"Time";
				value = StringTools.formatTimeWithSeconds(l.getLimit());
			}
			else if(limit instanceof LimitPoints)
			{	LimitPoints l = (LimitPoints)limit;
				iconName = prefix+"Custom";
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitTotal)
			{	LimitTotal l = (LimitTotal)limit;
				iconName = prefix+"Total";
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitScore)
			{	LimitScore l = (LimitScore) limit;
				switch(l.getScore())
				{	case BOMBS:
						iconName = prefix+"Bombs";
						value = nf.format(l.getLimit());
						break;
					case CROWNS:
						iconName = prefix+"Crowns";
						value = nf.format(l.getLimit());
						break;
					case DEATHS:
						iconName = prefix+"Deaths";
						value = nf.format(l.getLimit());
						break;
					case ITEMS:
						iconName = prefix+"Items";
						value = nf.format(l.getLimit());
						break;
					case KILLS:
						iconName = prefix+"Kills";
						value = nf.format(l.getLimit());
						break;
					case PAINTINGS:
						iconName = prefix+"Paintings";
						value = nf.format(l.getLimit());
						break;
					case TIME:
						iconName = prefix+"Time";
						value = nf.format(l.getLimit());
						break;
				}
			}
			//
			int colSub = 0;
			{	limitsPanel.getTable().setLabelKey(line,colGroup,colSub,iconName,true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				limitsPanel.getTable().setLabelForeground(line,colGroup,colSub,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				limitsPanel.getTable().setLabelBackground(line,colGroup,colSub,bg);
				colSub++;
			}
			{	String text = value;
				String tooltip = value;
				limitsPanel.getTable().setLabelText(line,colGroup,colSub,text,tooltip);
				colSub++;
			}
			line++;
			if(line==lines)
			{	line = 0;
				colGroup++;
			}
		}			

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
