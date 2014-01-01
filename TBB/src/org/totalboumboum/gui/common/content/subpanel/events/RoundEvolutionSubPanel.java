package org.totalboumboum.gui.common.content.subpanel.events;

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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.ColumnsSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Column;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.images.PredefinedColor;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

/**
 * This class is used to display plots representing the evolution
 * of certain scores during a round. It includes various controls
 * allowing to hide/display players and switch to other scores.
 * 
 * @author Vincent Labatut
 */
public class RoundEvolutionSubPanel extends ColumnsSubPanel implements MouseListener
{	/** Class id for serialization */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds an empty panel
	 * 
	 * @param width
	 * 		Width of the panel in pixels.
	 * @param height
	 * 		Height of the panel in pixels.
	 */
	public RoundEvolutionSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,3);
		
		// sizes
		int colorLines;
		int scoreLines;
		{	// colors
			{	PredefinedColor colorValues[] = PredefinedColor.values();		
				colorLines = colorValues.length;
				colorWidth = (getDataHeight() - (colorLines-1)*GuiSizeTools.subPanelMargin) / colorLines;
				int temp = getDataHeight() - (colorLines-1)*GuiSizeTools.subPanelMargin - colorLines*colorWidth;
				colorFirstLineHeight = colorWidth + temp/2;
				colorLastLineHeight = colorWidth + (temp - temp/2);
			}
			
			// scores
			{	Score scoreValues[] = Score.values();
				scoreLines = scoreValues.length;
				scoreWidth = (getDataHeight() - (scoreLines-1)*GuiSizeTools.subPanelMargin) / scoreLines;
				scoreFirstLineHeight = getDataHeight() - (scoreLines-1)*GuiSizeTools.subPanelMargin - (scoreLines-1)*scoreWidth;
			}
			
			// plot
			{	plotWidth = getDataWidth() - colorWidth - colorWidth - 2*GuiSizeTools.subPanelMargin;
//				plotWidth = getDataWidth() - scoreWidth - colorWidth - 2*GuiSizeTools.subPanelMargin;
			}
		}
		
		// buttons
		{	// scores
			{	Column cl = getColumn(COL_SCORES);
//				cl.setDim(scoreWidth,getDataHeight());
				cl.setDim(colorWidth,getDataHeight());
				for(int i=1;i<scoreLines;i++)
					cl.addLabel(0);
				
				for(int line=0;line<scoreLines;line++)
				{	int h = scoreWidth;
					if(line==0)
						h = scoreFirstLineHeight;
					cl.setLabelMinHeight(line,h);
					cl.setLabelPreferredHeight(line,h);
					cl.setLabelMaxHeight(line,h);
					MyLabel label = cl.getLabel(line);
					String key = SCORE_KEYS.get(line);
					label.setKey(key,true);
					label.setHorizontalAlignment(JLabel.CENTER);
					label.setVerticalAlignment(JLabel.CENTER);
					Color bg;
					if(line==LINE_CROWNS || line==LINE_PAINTINGS)
					{	label.setMouseSensitive(false);
						bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					}
					else
					{	label.addMouseListener(this);
						label.setMouseSensitive(true);
						bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					}
					label.setBackground(bg);
				}
			}
			
			// plot
			{	Column cl = getColumn(COL_PLOT);
				cl.setDim(plotWidth,getDataHeight());
				Color bg = GuiColorTools.COLOR_COMMON_BACKGROUND;
				cl.setBackgroundColor(bg);
				plot = new XYPlot();
				plot.setSetting(XYPlot.BACKGROUND, GuiColorTools.COLOR_COMMON_BACKGROUND);
				plot.getPlotArea().setSetting(PlotArea.BACKGROUND, null);
				plot.getPlotArea().setSetting(PlotArea.BORDER, null);
				insertPlotPanel();
			}
			
			// colors
			{	Column cl = getColumn(COL_COLORS);
				cl.setDim(colorWidth,getDataHeight());
				for(int i=1;i<colorLines;i++)
					cl.addLabel(0);
				for(int line=0;line<colorLines;line++)
				{	int h = colorWidth;
					if(line==0)
						h = colorFirstLineHeight;
					else if(line==colorLines-1)
						h = colorLastLineHeight;
					cl.setLabelMinHeight(line,h);
					cl.setLabelPreferredHeight(line,h);
					cl.setLabelMaxHeight(line,h);
					MyLabel label = cl.getLabel(line);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
				}
				Color bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				cl.setBackgroundColor(bg);
				Color fg = GuiColorTools.COLOR_TABLE_REGULAR_FOREGROUND;
				cl.setForegroundColor(fg);
			}
		}
		
		setScore(Score.BOMBS);
//		updatePlot();
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Round displayed in this panel */
	private Round round = null;
	
	/**
	 * Changes the round displayed in
	 * this panel.
	 * 
	 * @param round
	 * 		New round displayed in this panel.
	 */
	public void setRound(Round round)
	{	if(this.round!=round)
		{	this.round = round;
		
			// update displayed players
			selectedColors.clear();
			List<Profile> profiles = round.getProfiles();
			for(Profile profile: profiles)
			{	PredefinedColor color = profile.getSpriteColor();
				selectedColors.add(color);
			}
			
			updateColorButtons();
		}
		
		updateScoreButtons(selectedScore);
		updatePlot();
	}
	
	/////////////////////////////////////////////////////////////////
	// PLOT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column number of the plot panel */
	private final static int COL_PLOT = 1;
	/** Width of the color buttons */
	private int plotWidth;
	/** Drawable object used to plot */
	private XYPlot plot = null;

	/**
	 * Updates the plot depending on the
	 * current parameters : round, colors, score.
	 */
	private void updatePlot()
	{	if(round!=null)
		{	AbstractTournament tournament = round.getMatch().getTournament();
			
			// init player data
			StatisticRound statisticRound = round.getStats();
			List<String> ids = statisticRound.getPlayersIds();
			Map<String,Integer> series = new HashMap<String,Integer>();
			List<DataTable> dataTables = new ArrayList<DataTable>();
			List<Long> counts = new ArrayList<Long>();
			List<List<Long>> eliminations = new ArrayList<List<Long>>();
			for(int i=0;i<ids.size();i++)
			{	String id = ids.get(i);
				series.put(id,i);
				@SuppressWarnings("unchecked")
				DataTable dataTable = new DataTable(Long.class, Long.class);
				dataTable.add(0l,0l);
				dataTables.add(dataTable);
				if(selectedScore==Score.TIME)
					counts.add(1l);
				else
					counts.add(0l);
				eliminations.add(new ArrayList<Long>());
			}
			
			// init player colors
			List<PredefinedColor> colors = new ArrayList<PredefinedColor>();
			for(String id: ids)
			{	Profile profile = tournament.getProfileById(id);
				colors.add(profile.getSpriteColor());
			}
			
			// setting data model
			List<StatisticEvent> events = statisticRound.getStatisticEvents();
			for(StatisticEvent event: events)
			{	StatisticAction action = event.getAction();
				// bombeds
				if(selectedScore==Score.BOMBEDS)
				{	if(action==StatisticAction.BOMB_PLAYER)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						String targetId = event.getTargetId();
						if(actorId==null || !actorId.equals(targetId))
						{	int index = series.get(targetId);
							long count = counts.get(index);
							count++;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}
				
				// bombings
				else if(selectedScore==Score.BOMBINGS)
				{	if(action==StatisticAction.BOMB_PLAYER)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						String targetId = event.getTargetId();
						if(actorId!=null && !actorId.equals(targetId))
						{	int index = series.get(actorId);
							long count = counts.get(index);
							count++;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}
				
				// bombs
				else if(selectedScore==Score.BOMBS)
				{	if(action==StatisticAction.DROP_BOMB)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						count++;
						counts.set(index, count);
						DataTable dataTable = dataTables.get(index);
						dataTable.add(time,count);
					}
				}
				
				// crowns
				else if(selectedScore==Score.CROWNS)
				{	if(action==StatisticAction.GATHER_CROWN)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						count++;
						counts.set(index, count);
						DataTable dataTable = dataTables.get(index);
						dataTable.add(time,count);
					}
					else if(action==StatisticAction.LOSE_CROWN)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						if(count>0)
						{	count--;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}
	
				// items
				else if(selectedScore==Score.ITEMS)
				{	if(action==StatisticAction.GATHER_ITEM || action==StatisticAction.RECEIVE_ITEM)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						count++;
						counts.set(index, count);
						DataTable dataTable = dataTables.get(index);
						dataTable.add(time,count);
					}
					else if(action==StatisticAction.LOSE_ITEM || action==StatisticAction.TRANSMIT_ITEM)
						{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						if(count>0)
						{	count--;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}
	
				// paintings
				else if(selectedScore==Score.PAINTINGS)
				{	if(action==StatisticAction.WIN_TILE)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						count++;
						counts.set(index, count);
						DataTable dataTable = dataTables.get(index);
						dataTable.add(time,count);
					}
					else if(action==StatisticAction.LOSE_TILE)
						{	long time = event.getTime();
						String actorId = event.getActorId();
						int index = series.get(actorId);
						long count = counts.get(index);
						if(count>0)
						{	count--;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}
	
				// self-bombings
				else if(selectedScore==Score.SELF_BOMBINGS)
				{	if(action==StatisticAction.BOMB_PLAYER)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						String targetId = event.getTargetId();
						if(actorId!=null && actorId.equals(targetId))
						{	int index = series.get(actorId);
							long count = counts.get(index);
							count++;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}

				// time
				else if(selectedScore==Score.TIME)
				{	if(action==StatisticAction.BOMB_PLAYER)
					{	long time = event.getTime();
						String actorId = event.getActorId();
						String targetId = event.getTargetId();
						if(actorId!=null)
						{	int index = series.get(actorId);
							long count = counts.get(index);
							if(count>0)
							{	count = time;
								counts.set(index, count);
								DataTable dataTable = dataTables.get(index);
								dataTable.add(time,count);
							}
						}
						if(actorId==null || !actorId.equals(targetId))
						{	int index = series.get(targetId);
							long count = counts.get(index);
							count = time;
							counts.set(index, count);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
					}
				}

				// common (to certain scores) // TODO should be corrected for game rules where players can come back all the time
				if(selectedScore==Score.BOMBS 
					|| selectedScore==Score.BOMBINGS
					|| selectedScore==Score.ITEMS
					|| selectedScore==Score.TIME)
				{	if(action==StatisticAction.BOMB_PLAYER)
					{	long time = event.getTime();
						String targetId = event.getTargetId();
						int index = series.get(targetId);
						// update elimination times list
						List<Long> elim = eliminations.get(index);
						elim.add(time);
						/// possibly repeat the last value in the data series
						if(selectedScore!=Score.TIME)
						{	long count = counts.get(index);
							DataTable dataTable = dataTables.get(index);
							dataTable.add(time,count);
						}
						// add zero value to the data series
						DataTable dataTable = dataTables.get(index);
						dataTable.add(time,0l);
						counts.set(index,0l);
					}
				}
			}
			
			// set the final points
			long totalTime = statisticRound.getTotalTime();
			for(DataTable dataTable: dataTables)
			{	int last = dataTable.getRowCount() - 1;
				long time = (Long)dataTable.get(0,last);
				long value = (Long)dataTable.get(1,last);
				if(time<totalTime && value!=0)
				{	if(selectedScore==Score.TIME)
						dataTable.add(totalTime,totalTime);
					else
						dataTable.add(totalTime,value);
				}
				else if(time==0)
				{	if(selectedScore==Score.TIME)
						dataTable.add(totalTime,totalTime);
				}
			}
			
			// creating series
			List<DataSeries> dataSeries = new ArrayList<DataSeries>();
			for(DataTable dataTable: dataTables)
			{	DataSeries dataSerie = new DataSeries(dataTable, 0, 1);
				dataSeries.add(dataSerie);
			}
					
			// Create new xy-plot
			plot = new XYPlot();
			for(DataSeries dataSerie: dataSeries)
				plot.add(dataSerie);
	
			// Format plot
			plot.setSetting(XYPlot.BACKGROUND, GuiColorTools.COLOR_COMMON_BACKGROUND);
			plot.getPlotArea().setSetting(PlotArea.BACKGROUND, null);
			plot.getPlotArea().setSetting(PlotArea.BORDER, null);
	
			// Format axes
			AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
			AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
			String xLabel = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_EVOLUTION_BUTTON_TIME);
			axisRendererX.setSetting(AxisRenderer.LABEL, xLabel);
			int index = getLineForScore(selectedScore);
			String yLabel = GuiConfiguration.getMiscConfiguration().getLanguage().getText(SCORE_KEYS.get(index));
			axisRendererY.setSetting(AxisRenderer.LABEL, yLabel);
	        Map<Double, String> labels = new HashMap<Double, String>();
			for(int i=0;i<ids.size();i++)
			{	String id = ids.get(i);
				Profile profile = tournament.getProfileById(id);
				String name = profile.getName();
				List<Long> elim = eliminations.get(i);
				for(Long time: elim)
					labels.put(time.doubleValue(), name);
			}
	//		axisRendererX.setSetting(AxisRenderer.TICKS_CUSTOM, labels);
	
			// set format
			Shape circle = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);
			BasicStroke stroke = new BasicStroke(2f);
			for(int i=0;i<ids.size();i++)
			{	DataSeries dataSerie = dataSeries.get(i);
				Color color = colors.get(i).getColor();
				
				// Format rendering of data points
				PointRenderer pointRenderer = new DefaultPointRenderer2D();
				pointRenderer.setSetting(PointRenderer.SHAPE, circle);
				pointRenderer.setSetting(PointRenderer.COLOR, color);
				plot.setPointRenderer(dataSerie, pointRenderer);
	
				// Format data lines
				LineRenderer lineRenderer;
				if(curves)
					lineRenderer = new SmoothLineRenderer2D();
				else
					lineRenderer = new DefaultLineRenderer2D();
				lineRenderer.setSetting(LineRenderer.COLOR, color);
				lineRenderer.setSetting(LineRenderer.GAP, 2.0);
				lineRenderer.setSetting(LineRenderer.STROKE, stroke);
				plot.setLineRenderer(dataSerie, lineRenderer);
			}
			
			// update visibility
			for(int i=0;i<ids.size();i++)
			{	PredefinedColor color = playerColors.get(i);
				if(!selectedColors.contains(color))
					plot.setVisible(dataSeries.get(i), false);
			}
			
			// Add plot to this panel
			insertPlotPanel();
		}
	}
	
	/**
	 * Inserts the plot panel into
	 * this table panel, at the
	 * appropriate position.
	 */
	private void insertPlotPanel()
	{	Column cl = getColumn(COL_PLOT);
		cl.removeAll();
		InteractivePanel tempPanel = new InteractivePanel(plot);
		tempPanel.setPopupMenuEnabled(false);
		tempPanel.setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);
		Dimension dim = new Dimension(plotWidth,getDataHeight());
		tempPanel.setPreferredSize(dim);
		tempPanel.setMaximumSize(dim);
		tempPanel.setMinimumSize(dim);
		cl.add(tempPanel);
		
		revalidate();
//		repaint();
	}
	
	/**
	 * Updates this panel
	 * (called after a change, unlikely for rounds)
	 */
	public void refresh()
	{	setRound(round);
	}

	/////////////////////////////////////////////////////////////////
	// SCORES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Line for the number of times bombed by other players */
	private final static int LINE_BOMBEDS = 0;
	/** Line for the number of times the player bombed himself */
	private final static int LINE_SELFBOMBINGS = 1;
	/** Line for the number of other players bombed */
	private final static int LINE_BOMBINGS = 2;
	/** Line for the time played */
	private final static int LINE_TIME = 3;
	/** Line for the number of items picked up */
	private final static int LINE_ITEMS = 4;
	/** Line for the number of bombs dropped */
	private final static int LINE_BOMBS = 5;
	/** Line for the number of tiles painted */
	private final static int LINE_PAINTINGS = 6;
	/** Line for the number of crowns picked up */
	private final static int LINE_CROWNS = 7;
//	/** Line for the number of points scored */
//	private final static int LINE_POINTS = 8;
	/** Column number of the score buttons */
	private final static int COL_SCORES = 0;
	/** Map associating GUI keys with each button */
	private final static Map<Integer,String> SCORE_KEYS;
	static
	{	SCORE_KEYS = new HashMap<Integer, String>();
		SCORE_KEYS.put(LINE_BOMBEDS, GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBEDS);
		SCORE_KEYS.put(LINE_SELFBOMBINGS, GuiKeys.COMMON_EVOLUTION_BUTTON_SELF_BOMBINGS);
		SCORE_KEYS.put(LINE_BOMBINGS, GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBINGS);
		SCORE_KEYS.put(LINE_TIME, GuiKeys.COMMON_EVOLUTION_BUTTON_TIME);
		SCORE_KEYS.put(LINE_ITEMS, GuiKeys.COMMON_EVOLUTION_BUTTON_ITEMS);
		SCORE_KEYS.put(LINE_BOMBS, GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBS);
		SCORE_KEYS.put(LINE_PAINTINGS, GuiKeys.COMMON_EVOLUTION_BUTTON_PAINTINGS);
		SCORE_KEYS.put(LINE_CROWNS, GuiKeys.COMMON_EVOLUTION_BUTTON_CROWNS);
		//SCORE_KEYS.put(LINE_POINTS, GuiKeys.COMMON_EVOLUTION_BUTTON_POINTS);
	}
	/** Width of the score buttons */
	private int scoreWidth;
	/** Height of the first score button */
	private int scoreFirstLineHeight;
	/** Type of data currently displayed in the plot */
	private Score selectedScore = Score.BOMBS;
	
	/**
	 * Changes the currently displayed score.
	 * 
	 * @param score
	 * 		New score to display in the plot.
	 */
	private void setScore(Score score)
	{	if(score!=selectedScore)
		{	updateScoreButtons(score);
			updatePlot();
		}
	}
	
	/**
	 * Updates the state of the score buttons.
	 * 
	 * @param score
	 * 		New selected score.
	 */
	private void updateScoreButtons(Score score)
	{	// unselected previously selected score
		if(selectedScore!=null)
		{	int pos = getLineForScore(selectedScore);
			Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
			setLabelBackground(pos,COL_SCORES,bg);
		}
		
		// select new score
		selectedScore = score;
		int pos = getLineForScore(selectedScore);
		Color bg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
		setLabelBackground(pos,COL_SCORES,bg);
	}
	
	/**
	 * Results the line of the button
	 * associated to the specified score.
	 * 
	 * @param score
	 * 		Score to look for.
	 * @return
	 * 		Position of the corresponding button.
	 */
	private int getLineForScore(Score score)
	{	int result = -1;
		
		if(score==Score.BOMBEDS)
			result = LINE_BOMBEDS;
		else if(score==Score.BOMBINGS)
			result = LINE_BOMBINGS;
		else if(score==Score.BOMBS)
			result = LINE_BOMBS;
		else if(score==Score.CROWNS)
			result = LINE_CROWNS;
		else if(score==Score.ITEMS)
			result = LINE_ITEMS;
		else if(score==Score.PAINTINGS)
			result = LINE_PAINTINGS;
		else if(score==Score.SELF_BOMBINGS)
			result = LINE_SELFBOMBINGS;
		else if(score==Score.TIME)
			result = LINE_TIME;
	//	else if(score==Score.TIME)
	//		result = LINE_POINTS);
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column number of the color buttons */
	private final static int COL_COLORS = 2;
	/** Width of the color buttons */
	private int colorWidth;
	/** Height of the the first color button */
	private int colorFirstLineHeight;
	/** Height of the the last color button */
	private int colorLastLineHeight;
	/** Colors of the players displayed in the plot */
	private List<PredefinedColor> selectedColors = new ArrayList<PredefinedColor>();
	/** Colors used by the players of this round */
	private List<PredefinedColor> playerColors = new ArrayList<PredefinedColor>();
	
	/**
	 * Hide/show the player corresponding to the specified
	 * color.
	 * 
	 * @param color
	 * 		Color of the player to hide/show.
	 */
	private void switchColor(PredefinedColor color)
	{	List<PredefinedColor> colors = Arrays.asList(PredefinedColor.values());
		Color c = color.getColor();
		int line = colors.indexOf(color);
		if(selectedColors.contains(color))
		{	int index = playerColors.indexOf(color);
			selectedColors.remove(color);
			Color bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
			setLabelBackground(line,COL_COLORS,bg);
			List<DataSource> dataSources = plot.getData();
			if(dataSources!=null && !dataSources.isEmpty())
				plot.setVisible(dataSources.get(index), false);
		}
		else if(playerColors.contains(color))
		{	int index = playerColors.indexOf(color);
			selectedColors.add(color);
			Color bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
			setLabelBackground(line,COL_COLORS,bg);
			List<DataSource> dataSources = plot.getData();
			if(dataSources!=null && !dataSources.isEmpty())
				plot.setVisible(dataSources.get(index), true);
		}
	}
	
	/**
	 * Draws the color buttons when the
	 * round is changed.
	 */
	private void updateColorButtons()
	{	// init colors
		PredefinedColor colors[] = PredefinedColor.values();
		playerColors.clear();
		List<Profile> profiles = round.getProfiles();
		for(Profile profile: profiles)
		{	PredefinedColor color = profile.getSpriteColor();
			playerColors.add(color);
		}
		
		Column cl = getColumn(COL_COLORS);
		for(int line=0;line<cl.getLineCount();line++)
		{	MyLabel label = getLabel(line,COL_COLORS);
			PredefinedColor color = colors[line];
			Color c = color.getColor();
			String text = null;
			String tooltip = null;
			Color bg = null;
			int index = playerColors.indexOf(color);
			if(index==-1)
			{	label.setMouseSensitive(false);
				bg = GuiColorTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			}
			else
			{	label.setMouseSensitive(true);
				bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				String colorKey = color.toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;
				String colorStr = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey);
				String playerStr = profiles.get(index).getName();
				text = colorStr + " - " + playerStr;
				tooltip = text;
				if(selectedColors.contains(color))
					bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiColorTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
			}
			setLabelBackground(line,COL_COLORS,bg);
			setLabelText(line,COL_COLORS,text,tooltip);
			label.validate();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if curves or straight lines should be drawn in the plot */
	private boolean curves = false;
	
	/**
	 * Changes the fact this plot contains straight
	 * lines or curves.
	 * 
	 * @param curves
	 * 		{@code true} to draw curves instead of straight lines.
	 */
	public void setCurves(boolean curves)
	{	this.curves = curves;
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
		
		int[] pos = {0,COL_PLOT};
		if(e.getComponent() instanceof MyLabel)
		{	MyLabel label = (MyLabel)e.getComponent();
			pos = getLabelPosition(label);
		}
		
		// scores
		if(pos[1]==COL_SCORES)
		{	if(pos[0]==LINE_BOMBEDS)
				setScore(Score.BOMBEDS);
			else if(pos[0]==LINE_BOMBINGS)
				setScore(Score.BOMBINGS);
			else if(pos[0]==LINE_BOMBS)
				setScore(Score.BOMBS);
			else if(pos[0]==LINE_CROWNS)
				setScore(Score.CROWNS);
			else if(pos[0]==LINE_ITEMS)
				setScore(Score.ITEMS);
			else if(pos[0]==LINE_PAINTINGS)
				setScore(Score.PAINTINGS);
			else if(pos[0]==LINE_SELFBOMBINGS)
				setScore(Score.SELF_BOMBINGS);
			else if(pos[0]==LINE_TIME)
				setScore(Score.TIME);
//			else if(pos[0]==LINE_POINTS)
//				setScore(Score.TIME);
		}

		// colors
		if(pos[1]==COL_COLORS)
		{	PredefinedColor colors[] = PredefinedColor.values();
			PredefinedColor color = colors[pos[0]];
			switchColor(color);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	//
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of objects listening to this panel */
	private List<EvolutionSubPanelListener> listeners = new ArrayList<EvolutionSubPanelListener>();
	
	/**
	 * Adds a new listener to this panel.
	 * 
	 * @param listener
	 * 		New listener.
	 */
	public void addListener(EvolutionSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	/**
	 * Removes an existing listener from this panel.
	 * 
	 * @param listener
	 * 		Listener to remove.
	 */
	public void removeListener(EvolutionSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	/**
	 * Fires a mouse pressed event, transmits it to all listeners.
	 * 
	 * @param e
	 * 		Event to transmit to listeners.
	 */
	private void fireMousePressed(MouseEvent e)
	{	for(EvolutionSubPanelListener listener: listeners)
			listener.mousePressed(e);
	}
}
