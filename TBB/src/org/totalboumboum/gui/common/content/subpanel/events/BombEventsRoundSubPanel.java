package org.totalboumboum.gui.common.content.subpanel.events;

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
import javax.swing.JPanel;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.ColumnsSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Column;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
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
public class BombEventsRoundSubPanel extends ColumnsSubPanel implements MouseListener
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
	public BombEventsRoundSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,3);
		
		// sizes
		int colorLines;
		int scoreLines;
		{	// colors
			{	PredefinedColor colorValues[] = PredefinedColor.values();		
				colorLines = colorValues.length;
				colorWidth = (getDataHeight() - (colorLines-1)*GuiTools.subPanelMargin) / colorLines;
				int temp = getDataHeight() - (colorLines-1)*GuiTools.subPanelMargin - colorLines*colorWidth;
				colorFirstLineHeight = colorWidth + temp/2;
				colorLastLineHeight = colorWidth + (temp - temp/2);
			}
			
			// scores
			{	Score scoreValues[] = Score.values();
				scoreLines = scoreValues.length;
				scoreWidth = (getDataHeight() - (scoreLines-1)*GuiTools.subPanelMargin) / scoreLines;
				scoreFirstLineHeight = getDataHeight() - (scoreLines-1)*GuiTools.subPanelMargin - (scoreLines-1)*scoreWidth;
			}
			
			// plot
			{	plotWidth = getDataWidth() - colorWidth - colorWidth - 2*GuiTools.subPanelMargin;
//				plotWidth = getDataWidth() - scoreWidth - colorWidth - 2*GuiTools.subPanelMargin;
			}
		}
		
		// buttons
		{	// scores
			{	Column cl = getColumn(COL_SCORES);
//				cl.setDim(scoreWidth,getDataHeight());
				cl.setDim(colorWidth,getDataHeight());
				for(int i=1;i<scoreLines;i++)
					cl.addLabel(0);
				Map<Integer,String> keys = new HashMap<Integer, String>();
				keys.put(LINE_BOMBEDS, GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBEDS);
				keys.put(LINE_SELFBOMBINGS, GuiKeys.COMMON_EVOLUTION_BUTTON_SELF_BOMBINGS);
				keys.put(LINE_BOMBINGS, GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBINGS);
				keys.put(LINE_TIME, GuiKeys.COMMON_EVOLUTION_BUTTON_TIME);
				keys.put(LINE_ITEMS, GuiKeys.COMMON_EVOLUTION_BUTTON_ITEMS);
				keys.put(LINE_BOMBS, GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBS);
				keys.put(LINE_PAINTINGS, GuiKeys.COMMON_EVOLUTION_BUTTON_PAINTINGS);
				keys.put(LINE_CROWNS, GuiKeys.COMMON_EVOLUTION_BUTTON_CROWNS);
				//keys.put(LINE_POINTS, GuiKeys.COMMON_EVOLUTION_BUTTON_POINTS);
				
				for(int line=0;line<scoreLines;line++)
				{	int h = scoreWidth;
					if(line==0)
						h = scoreFirstLineHeight;
					cl.setLabelMinHeight(line,h);
					cl.setLabelPreferredHeight(line,h);
					cl.setLabelMaxHeight(line,h);
					MyLabel label = cl.getLabel(line);
					String key = keys.get(line);
					label.setKey(key,true);
					label.setHorizontalAlignment(JLabel.CENTER);
					label.setVerticalAlignment(JLabel.CENTER);
					label.addMouseListener(this);
					label.setMouseSensitive(true);
				}
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				cl.setBackgroundColor(bg);
			}
			
			// plot
			{	Column cl = getColumn(COL_PLOT);
				cl.setDim(plotWidth,getDataHeight());
				Color bg = GuiTools.COLOR_COMMON_BACKGROUND;
				cl.setBackgroundColor(bg);
				plot = new XYPlot();
				plot.setSetting(XYPlot.BACKGROUND, GuiTools.COLOR_COMMON_BACKGROUND);
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
				Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
				cl.setBackgroundColor(bg);
				Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
				cl.setForegroundColor(fg);
			}
		}
		
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
	{	AbstractTournament tournament = round.getMatch().getTournament();
		
		// init player data
		StatisticRound statisticRound = round.getStats();
		List<String> ids = statisticRound.getPlayersIds();
		Map<String,Integer> series = new HashMap<String,Integer>();
		List<DataTable> dataTables = new ArrayList<DataTable>();
		List<Integer> counts = new ArrayList<Integer>();
		List<List<Long>> eliminations = new ArrayList<List<Long>>();
		for(int i=0;i<ids.size();i++)
		{	String id = ids.get(i);
			series.put(id,i);
			@SuppressWarnings("unchecked")
			DataTable dataTable = new DataTable(Long.class, Integer.class);
			dataTables.add(dataTable);
			counts.add(0);
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
			if(action==StatisticAction.DROP_BOMB)
			{	long time = event.getTime();
				String actorId = event.getActorId();
				int index = series.get(actorId);
				int count = counts.get(index);
				count++;
				counts.set(index, count);
				DataTable dataTable = dataTables.get(index);
				dataTable.add(time,count);
			}
			else if(action==StatisticAction.BOMB_PLAYER)
			{	long time = event.getTime();
				String targetId = event.getTargetId();
				int index = series.get(targetId);
				List<Long> elim = eliminations.get(index);
				elim.add(time);
				counts.set(index, 0);
				DataTable dataTable = dataTables.get(index);
				dataTable.add(time,0);
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
		plot.setSetting(XYPlot.BACKGROUND, GuiTools.COLOR_COMMON_BACKGROUND);
		plot.getPlotArea().setSetting(PlotArea.BACKGROUND, null);
		plot.getPlotArea().setSetting(PlotArea.BORDER, null);

		// Format axes
		AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
		AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		axisRendererX.setSetting(AxisRenderer.LABEL, "Time");
		axisRendererY.setSetting(AxisRenderer.LABEL, "Bombs");
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
			//LineRenderer lineRenderer = new SmoothLineRenderer2D();
			LineRenderer lineRenderer = new DefaultLineRenderer2D();
			lineRenderer.setSetting(LineRenderer.COLOR, color);
			lineRenderer.setSetting(LineRenderer.GAP, 2.0);
			lineRenderer.setSetting(LineRenderer.STROKE, stroke);
			plot.setLineRenderer(dataSerie, lineRenderer);
		}
		
		// Add plot to this panel
		insertPlotPanel();
	}
	
	/**
	 * Inserts the plot panel into
	 * this table panel, at the
	 * appropriate position.
	 */
	private void insertPlotPanel()
	{	Column cl = getColumn(COL_PLOT);
		cl.removeAll();
		JPanel tempPanel = new InteractivePanel(plot);
		tempPanel.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		Dimension dim = new Dimension(plotWidth,getDataHeight());
		tempPanel.setPreferredSize(dim);
		tempPanel.setMaximumSize(dim);
		tempPanel.setMinimumSize(dim);
		cl.add(tempPanel);
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
	public void setScore(Score score)
	{	selectedScore = score;
		updatePlot();
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
	public void switchColor(PredefinedColor color)
	{	List<PredefinedColor> colors = Arrays.asList(PredefinedColor.values());
		Color c = color.getColor();
		int line = colors.indexOf(color);
		if(selectedColors.contains(color))
		{	int index = playerColors.indexOf(color);
			selectedColors.remove(color);
			Color bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
			setLabelBackground(line,COL_COLORS,bg);
			List<DataSource> dataSources = plot.getData();
			if(dataSources!=null && !dataSources.isEmpty())
				plot.setVisible(dataSources.get(index), false);
		}
		else if(playerColors.contains(color))
		{	int index = playerColors.indexOf(color);
			selectedColors.add(color);
			Color bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
			setLabelBackground(line,COL_COLORS,bg);
			List<DataSource> dataSources = plot.getData();
			if(dataSources!=null && !dataSources.isEmpty())
				plot.setVisible(dataSources.get(index), true);
		}
		
		if(round!=null)
		{	// TODO display/hide the corresponding series
			
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
				bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			}
			else
			{	label.setMouseSensitive(true);
				bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1);
				String colorKey = color.toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;
				String colorStr = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey);
				String playerStr = profiles.get(index).getName();
				text = colorStr + " - " + playerStr;
				tooltip = text;
				if(selectedColors.contains(color))
					bg = new Color(c.getRed(),c.getGreen(),c.getBlue(),GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3);
			}
			setLabelBackground(line,COL_COLORS,bg);
			setLabelText(line,COL_COLORS,text,tooltip);
			label.validate();
		}
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
	{	int[] pos = {0,COL_PLOT};
		if(e.getComponent() instanceof MyLabel)
		{	MyLabel label = (MyLabel)e.getComponent();
			pos = getLabelPosition(label);
		}
		
		// scores
		if(pos[1]==COL_SCORES)
		{
			
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
	{	
	}
}
