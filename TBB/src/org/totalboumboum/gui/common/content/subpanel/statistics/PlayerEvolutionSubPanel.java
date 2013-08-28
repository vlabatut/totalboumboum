package org.totalboumboum.gui.common.content.subpanel.statistics;

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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.ColumnsSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.Column;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.statistics.overall.PlayerStats.Value;
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
import de.erichseifert.gral.ui.InteractivePanel;

/**
 * This class is used to display plots representing the evolution
 * of Glicko-2 related statistics. It includes various controls
 * allowing to hide/display players and switch to other stats.
 * 
 * @author Vincent Labatut
 */
public class PlayerEvolutionSubPanel extends ColumnsSubPanel implements MouseListener
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
	public PlayerEvolutionSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,3);
		
		// sizes
		{	// colors
			{	colorWidth = (getDataHeight() - (COLOR_NBR-1)*GuiSizeTools.subPanelMargin) / COLOR_NBR;
				int temp = getDataHeight() - (COLOR_NBR-1)*GuiSizeTools.subPanelMargin - COLOR_NBR*colorWidth;
				colorFirstLineHeight = colorWidth + temp/2;
				colorLastLineHeight = colorWidth + (temp - temp/2);
			}
			
//			// values
//			{	valueWidth = (getDataHeight() - (VALUE_NBR-1)*GuiSizeTools.subPanelMargin) / VALUE_NBR;
//				valueFirstLineHeight = getDataHeight() - (VALUE_NBR-1)*GuiSizeTools.subPanelMargin - (VALUE_NBR-1)*valueWidth;
//			}
			
			// plot
			{	plotWidth = getDataWidth() - colorWidth - colorWidth - 2*GuiSizeTools.subPanelMargin;
//				plotWidth = getDataWidth() - valueWidth - colorWidth - 2*GuiSizeTools.subPanelMargin;
			}
		}
		
		// buttons
		{	// values
//			{	setValueButtons();
//			}
			
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
				for(int i=1;i<COLOR_NBR;i++)
					cl.addLabel(0);
				for(int line=0;line<COLOR_NBR;line++)
				{	int h = colorWidth;
					if(line==0)
						h = colorFirstLineHeight;
					else if(line==COLOR_NBR-1)
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
		
//		setValue(Value.RANK);
//		updatePlot();
	}
	
	/////////////////////////////////////////////////////////////////
	// SELECTED PLAYERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Players displayed in this panel */
	private List<String> selectedPlayers = new ArrayList<String>();
	/** Player profiles */
	private Map<String,Profile> profiles = new HashMap<String,Profile>();
	
	/**
	 * Updates the map of all profiles.
	 * 
	 * @param profiles
	 * 		New map of profiles.
	 */
	public void setProfiles(Map<String,Profile> profiles)
	{	this.profiles = profiles;
	}
	
	/**
	 * Updates the players displayed in
	 * this panel.
	 */
	public void updateSelectedPlayers()
	{	// retrieve selected players
		List<String> selectedPlayers = new ArrayList<String>();
		Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		for(Entry<String,PlayerStats> e: playersStats.entrySet())
		{	String id = e.getKey();
			PlayerStats ps = e.getValue();
			if(ps.isSelected())
				selectedPlayers.add(id);
		}
		
		// compare with current list, update only if needed
		if(!this.selectedPlayers.containsAll(selectedPlayers) || selectedPlayers.containsAll(this.selectedPlayers))
		{	this.selectedPlayers = selectedPlayers;
			
			// update displayed players
			selectedColors.clear();
			for(String playerId: selectedPlayers)
			{	PlayerStats playerStats = playersStats.get(playerId);
				PredefinedColor color = playerStats.getSelectedColor();
				selectedColors.add(color);
			}
			
			updateColorButtons();
		}
		
		updateValueButtons(selectedValue);
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
	 * current parameters.
	 */
	@SuppressWarnings("unchecked")
	private void updatePlot()
	{	if(selectedPlayers!=null && !selectedPlayers.isEmpty())
		{	// init player data
			Map<String,Integer> series = new HashMap<String,Integer>();
			List<DataTable> dataTables = new ArrayList<DataTable>();
			List<List<Long>> eliminations = new ArrayList<List<Long>>();
			for(int i=0;i<selectedPlayers.size();i++)
			{	String id = selectedPlayers.get(i);
				series.put(id,i);
				DataTable dataTable = new DataTable(Float.class, Float.class);
				dataTables.add(dataTable);
				eliminations.add(new ArrayList<Long>());
			}
			
			// init player colors
			HashMap<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
			List<PredefinedColor> colors = new ArrayList<PredefinedColor>();
			for(String id: selectedPlayers)
			{	PlayerStats playerStats = playersStats.get(id);
				PredefinedColor color = playerStats.getSelectedColor();
				colors.add(color);
			}
			
			// setting data series
			for(int i=0;i<selectedPlayers.size();i++)
			{	String playerId = selectedPlayers.get(i);
				PlayerStats playerStats = playersStats.get(playerId);
				DataTable dataTable = dataTables.get(i);
				float count = 0;
				List<Float> list = playerStats.getHistoryValues(selectedValue);
				if(list==null || list.isEmpty())
				{	dataTable.add(0f,0f);
				}
				else
				{	for(int j=0;j<list.size();j++)
					{	float v = list.get(j);
						count = count + v;
						dataTable.add((float)j+1,count);
						if(NON_CUMULATIVE_VALUES.contains(selectedValue))
							count = 0;
					}
				}
			}
			
			// inserting series
			List<DataSeries> dataSeries = new ArrayList<DataSeries>();
			for(DataTable dataTable: dataTables)
			{	DataSeries dataSerie = new DataSeries(dataTable, 0, 1);
				dataSeries.add(dataSerie);
			}
					
			// create new plot
			plot = new XYPlot();
			for(DataSeries dataSerie: dataSeries)
				plot.add(dataSerie);
	
			// format plot
			plot.setSetting(XYPlot.BACKGROUND, GuiColorTools.COLOR_COMMON_BACKGROUND);
			plot.getPlotArea().setSetting(PlotArea.BACKGROUND, null);
			plot.getPlotArea().setSetting(PlotArea.BORDER, null);
	
			// format axes
			AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
			AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
			String xLabel = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_EVOLUTION_LABEL_ROUND);
			axisRendererX.setSetting(AxisRenderer.LABEL, xLabel);
			int index = availableValues.indexOf(selectedValue);
			String key = VALUE_KEYS.get(availableValues.get(index));
			String yLabel = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
			axisRendererY.setSetting(AxisRenderer.LABEL, yLabel);
//	        Map<Double, String> labels = new HashMap<Double, String>();
//			String prefix = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.COMMON_EVOLUTION_LABEL_ROUND_PREFIX);
//			for(int i=0;i<statisticRounds.size();i++)
//			{	String label = prefix + (i+1);
//				labels.put((double)(i+1),label);
//			}
//			axisRendererX.setSetting(AxisRenderer.TICKS_CUSTOM, labels);
	
			// set format
//			Shape circle = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);
			BasicStroke stroke = new BasicStroke(2f);
			for(int i=0;i<selectedPlayers.size();i++)
			{	DataSeries dataSerie = dataSeries.get(i);
				Color color = colors.get(i).getColor();
				
				// Format rendering of data points
//				PointRenderer pointRenderer = new DefaultPointRenderer2D();
//				pointRenderer.setSetting(PointRenderer.SHAPE, circle);
//				pointRenderer.setSetting(PointRenderer.COLOR, color);
//				plot.setPointRenderer(dataSerie, pointRenderer);
                plot.setPointRenderer(dataSerie, null);

				// Format data lines
				LineRenderer lineRenderer;
				if(curves)
					lineRenderer = new SmoothLineRenderer2D();
				else
					lineRenderer = new DefaultLineRenderer2D();
				lineRenderer.setSetting(LineRenderer.COLOR, color);
//				lineRenderer.setSetting(LineRenderer.GAP, 2.0);
				lineRenderer.setSetting(LineRenderer.STROKE, stroke);
				plot.setLineRenderer(dataSerie, lineRenderer);
			}
			
			// update visibility
			for(int i=0;i<selectedPlayers.size();i++)
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
	 * (called after a change)
	 */
	public void refresh()
	{	updateSelectedPlayers();
	}

	/////////////////////////////////////////////////////////////////
	// VALUES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column number of the values buttons */
	private final static int COL_VALUES = 0;
	/** List of values to be displayed */
	private List<Value> availableValues = new ArrayList<Value>();
	/** Width of the value buttons */
	private int valueWidth;
	/** Height of the first value button */
	private int valueFirstLineHeight;
	/** Type of data currently displayed in the plot */
	private Value selectedValue = Value.RANK;
	/** List of values which should not be plot cumulatively */
	private List<Value> NON_CUMULATIVE_VALUES = Arrays.asList(Value.RANK,Value.MEAN,Value.STDEV);
	/** GUI keys associated to each possible value */
	private final static Map<Value,String> VALUE_KEYS = new HashMap<PlayerStats.Value, String>();
	static
	{	// Glicko-2
		VALUE_KEYS.put(Value.RANK, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_RANKS);
		VALUE_KEYS.put(Value.MEAN, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_MEANS);
		VALUE_KEYS.put(Value.STDEV, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_STDEVS);
		// scores
		VALUE_KEYS.put(Value.BOMBEDS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_BOMBEDS);
		VALUE_KEYS.put(Value.SELF_BOMBINGS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_SELF_BOMBINGS);
		VALUE_KEYS.put(Value.BOMBINGS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_BOMBINGS);
		VALUE_KEYS.put(Value.ITEMS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ITEMS);
		VALUE_KEYS.put(Value.BOMBS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_BOMBS);
		VALUE_KEYS.put(Value.PAINTINGS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_PAINTINGS);
		VALUE_KEYS.put(Value.CROWNS, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_CROWNS);
		// confrontations
		VALUE_KEYS.put(Value.CONFR_TOTAL, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_TOTAL);
		VALUE_KEYS.put(Value.CONFR_WON, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_WON);
		VALUE_KEYS.put(Value.CONFR_DRAW, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_DRAWN);
		VALUE_KEYS.put(Value.CONFR_LOST, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_LOST);
		VALUE_KEYS.put(Value.TIME, GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_TIME);
	}
	
	/**
	 * Changes the currently displayed value.
	 * 
	 * @param value
	 * 		New value type to display in the plot.
	 */
	private void setValue(Value value)
	{	//if(value!=selectedValue)
		{	updateValueButtons(value);
			updatePlot();
		}
	}
	
	/**
	 * Updates the state of the value buttons.
	 * 
	 * @param value
	 * 		New selected value.
	 */
	private void updateValueButtons(Value value)
	{	// unselect previously selected value (if there's one)
		{	int pos = availableValues.indexOf(selectedValue);
			if(pos>=0)
			{	Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
				setLabelBackground(pos,COL_VALUES,bg);
			}
		}
		
		// select new value
		{	selectedValue = value;
			int pos = availableValues.indexOf(selectedValue);
			Color bg = GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND;
			setLabelBackground(pos,COL_VALUES,bg);
		}
	}
	
	/**
	 * Insert the appropriate value buttons.
	 */
	private void setValueButtons()
	{	// process button dimension
		int valueNbr = availableValues.size();
		valueWidth = (getDataHeight() - (valueNbr-1)*GuiSizeTools.subPanelMargin) / valueNbr;
		valueFirstLineHeight = getDataHeight() - (valueNbr-1)*GuiSizeTools.subPanelMargin - (valueNbr-1)*valueWidth;

		// reset column
		Column cl = getColumn(COL_VALUES);
		cl.clear();
//		cl.setDim(valueWidth,getDataHeight());
		cl.setDim(colorWidth,getDataHeight());
		for(int i=0;i<valueNbr;i++)
			cl.addLabel(0);
		
		for(int line=0;line<valueNbr;line++)
		{	int h = valueWidth;
			if(line==0)
				h = valueFirstLineHeight;
			cl.setLabelMinHeight(line,h);
			cl.setLabelPreferredHeight(line,h);
			cl.setLabelMaxHeight(line,h);
			MyLabel label = cl.getLabel(line);
			Value v = availableValues.get(line);
			String key = VALUE_KEYS.get(v);
			label.setKey(key,true);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			Color bg;
			if(key.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_PAINTINGS) || key.equals(GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_CROWNS))
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
	
	/**
	 * Changes the available value buttons, and consequently
	 * the type of data the plot can display.
	 *  
	 * @param values
	 * 		New available values.
	 */
	public void setAvailableValues(List<Value> values)
	{	// update available values
		availableValues.clear();
		availableValues.addAll(values);
		
		// update value buttons
		setValueButtons();
		
		// update plot
		Value value = null;
		if(!values.isEmpty())
			value = values.get(0);
		setValue(value);
		
		// update content
		updateSelectedPlayers();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of colors to be plot */
	private static final int COLOR_NBR = PredefinedColor.values().length;
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
	/** Colors used to plot the players */
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
	 * player selection is changed.
	 */
	private void updateColorButtons()
	{	// init colors
		PredefinedColor colors[] = PredefinedColor.values();
		playerColors.clear();
		HashMap<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
		for(String playerId: selectedPlayers)
		{	PlayerStats playerStats = playersStats.get(playerId);
			PredefinedColor color = playerStats.getSelectedColor();
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
				String id = selectedPlayers.get(index);
				String playerStr = profiles.get(id).getName();
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
	{	int[] pos = {0,COL_PLOT};
		if(e.getComponent() instanceof MyLabel)
		{	MyLabel label = (MyLabel)e.getComponent();
			pos = getLabelPosition(label);
		}
		
		// values
		if(pos[1]==COL_VALUES)
		{	Value value = availableValues.get(pos[0]);
			setValue(value);
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
}
