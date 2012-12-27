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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.EmptyContentPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.PlotArea;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LogarithmicRenderer2D;
import de.erichseifert.gral.plots.lines.DiscreteLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.DefaultPointRenderer2D;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.plots.points.SizeablePointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.Insets2D;
import de.erichseifert.gral.util.Orientation;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class BombEventsRoundSubPanel extends EmptySubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;

	public BombEventsRoundSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER);

		// set panel
		EmptyContentPanel dataPanel = getDataPanel();
		dataPanel.setOpaque(false);
		
		// background
		{	Color bg = GuiTools.COLOR_COMMON_BACKGROUND;
			setBackground(bg);
		}
		
		// layout
		{	BoxLayout layout = new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS); 
			dataPanel.setLayout(layout);
		}
		
		// main panel
		{	plotPanel = new XYPlot();
			tempPanel = new InteractivePanel(plotPanel);
tempPanel.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
Dimension dim = new Dimension(getDataWidth(),getDataHeight());
tempPanel.setPreferredSize(dim);
tempPanel.setMaximumSize(dim);
tempPanel.setMinimumSize(dim);

			dataPanel.add(tempPanel);
		}

	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private XYPlot plotPanel;
	private Round round;
	private InteractivePanel tempPanel;
	
	public void setRound(Round round)
	{	
//		plotPanel.clear();
//		this.round = round;
//		AbstractTournament tournament = round.getMatch().getTournament();
//	
//		// init player data
//		StatisticRound statisticRound = round.getStats();
//		List<String> ids = statisticRound.getPlayersIds();
//		Map<String,Integer> series = new HashMap<String,Integer>();
//		List<DataTable> data = new ArrayList<DataTable>();
//		for(int i=0;i<ids.size();i++)
//		{	String id = ids.get(i);
//			series.put(id,i);
//			@SuppressWarnings("unchecked")
//			DataTable dataTable = new DataTable(Long.class, Integer.class);
//			data.add(dataTable);
//		}
//		
//		// init player colors
//		List<PredefinedColor> colors = new ArrayList<PredefinedColor>();
//		for(String id: ids)
//		{	Profile profile = tournament.getProfileById(id);
//			colors.add(profile.getSpriteColor());
//		}
//		
//		// setting data model
//		List<StatisticEvent> events = statisticRound.getStatisticEvents();
//		for(StatisticEvent event: events)
//		{	StatisticAction action = event.getAction();
//			if(action==StatisticAction.DROP_BOMB)
//			{	long time = event.getTime();
//				String id = event.getActorId();
//				int index = series.get(id);
//				DataTable dataTable = data.get(index);
//				dataTable.add(time,1);
//			}
//		}
//		
//		// adding to panel
//		for(int i=0;i<ids.size();i++)
//		{	DataTable dataTable = data.get(i);
//			plotPanel.add(dataTable);
//			Color color = colors.get(i).getColor();
//	        plotPanel.getPointRenderer(dataTable).setSetting(PointRenderer.COLOR,color);
//		}
		
		
		
		// Generate data
		Random random = new Random();
		DataTable data = new DataTable(Double.class, Double.class, Double.class,
				Double.class, Double.class, Double.class);
		for (double x = 1.0; x <= 400.0; x *= 1.5) {
			double x2 = x/5.0;
			data.add(x2, -Math.sqrt(x2) + 5.0,  5.0*Math.log10(x2),
				random.nextDouble() + 1.0, random.nextDouble() + 0.5, 1.0 + 2.0*random.nextDouble());
		}

		// Create data series
		DataSeries seriesLog = new DataSeries(data, 0, 2, 3, 4);
		DataSeries seriesLin = new DataSeries(data, 0, 1, 5);

		// Create new xy-plot
		XYPlot plot = new XYPlot(seriesLog, seriesLin);

		// Format plot
		plot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
		plot.setSetting(XYPlot.BACKGROUND, Color.WHITE);
		plot.setSetting(XYPlot.TITLE, "Descriptionnn");

		// Format plot area
		plot.getPlotArea().setSetting(PlotArea.BACKGROUND, new RadialGradientPaint(
			new Point2D.Double(0.5, 0.5),
			0.75f,
			new float[] { 0.6f, 0.8f, 1.0f },
			new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 32), new Color(0, 0, 0, 128) }
		));
		plot.getPlotArea().setSetting(PlotArea.BORDER, null);

		// Format axes
		AxisRenderer axisRendererX = new LogarithmicRenderer2D();
		AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		axisRendererX.setSetting(AxisRenderer.LABEL, "Logarithmic axis");
		plot.setAxisRenderer(XYPlot.AXIS_X, axisRendererX);
		// Custom tick labels
		Map<Double, String> labels = new HashMap<Double, String>();
		labels.put(2.0, "Two");
		labels.put(1.5, "OnePointFive");
		axisRendererX.setSetting(AxisRenderer.TICKS_CUSTOM, labels);
		// Custom stroke for the x-axis
		BasicStroke stroke = new BasicStroke(2f);
		axisRendererX.setSetting(AxisRenderer.SHAPE_STROKE, stroke);
		axisRendererY.setSetting(AxisRenderer.LABEL, "Linear axis");
		// Change intersection point of Y axis
		axisRendererY.setSetting(AxisRenderer.INTERSECTION, 1.0);
		// Change tick spacing
		axisRendererX.setSetting(AxisRenderer.TICKS_SPACING, 2.0);

		// Format rendering of data points
		PointRenderer sizeablePointRenderer = new SizeablePointRenderer();
		sizeablePointRenderer.setSetting(PointRenderer.COLOR, GraphicsUtils.deriveDarker(Color.RED));
		plot.setPointRenderer(seriesLin, sizeablePointRenderer);
		PointRenderer defaultPointRenderer = new DefaultPointRenderer2D();
		defaultPointRenderer.setSetting(PointRenderer.COLOR, GraphicsUtils.deriveDarker(Color.BLUE));
		defaultPointRenderer.setSetting(PointRenderer.ERROR_DISPLAYED, true);
		defaultPointRenderer.setSetting(PointRenderer.ERROR_COLOR, Color.BLUE);
		plot.setPointRenderer(seriesLog, defaultPointRenderer);

		// Format data lines
		LineRenderer discreteRenderer = new DiscreteLineRenderer2D();
		discreteRenderer.setSetting(LineRenderer.COLOR, Color.RED);
		discreteRenderer.setSetting(LineRenderer.STROKE, new BasicStroke(
			3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
			10.0f, new float[] {3f, 6f}, 0.0f));
		plot.setLineRenderer(seriesLin, discreteRenderer);
		// Custom gaps for points
		discreteRenderer.setSetting(LineRenderer.GAP, 2.0);
		discreteRenderer.setSetting(LineRenderer.GAP_ROUNDED, true);
		// Custom ascending
		discreteRenderer.setSetting(DiscreteLineRenderer2D.ASCENT_DIRECTION,
			Orientation.VERTICAL);
		discreteRenderer.setSetting(DiscreteLineRenderer2D.ASCENDING_POINT,
			0.5);

		// Add plot to Swing component
		EmptyContentPanel dataPanel = getDataPanel();
		dataPanel.remove(tempPanel);
		tempPanel = new InteractivePanel(plot);
		dataPanel.add(tempPanel, BorderLayout.CENTER);		
		
		
		
		
		
		
	}
	
	public void refresh()
	{	setRound(round);
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
	{	
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
