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
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
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
import de.erichseifert.gral.plots.lines.SmoothLineRenderer2D;
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
//		Random random = new Random();
//		DataTable data = new DataTable(Double.class, Double.class, Double.class,
//				Double.class, Double.class, Double.class);
//		for (double x = 1.0; x <= 400.0; x *= 1.5) {
//			double x2 = x/5.0;
//			data.add(x2, -Math.sqrt(x2) + 5.0,  5.0*Math.log10(x2),
//				random.nextDouble() + 1.0, random.nextDouble() + 0.5, 1.0 + 2.0*random.nextDouble());
//		}
//
//		// Create data series
//		DataSeries series1 = new DataSeries(data, 0, 2, 3, 4);
//		DataSeries series2 = new DataSeries(data, 0, 1, 5);

// creating fake data
List<int[][]> data = new ArrayList<int[][]>();
int data1[][] = {
	{120,1},{250,1},{300,1},{400,1},{560,1},{659,1},{805,1}
};data.add(data1);
int data2[][] = {
	{105,1},{235,1},{345,1},{368,1},{456,1},{865,1},{875,1},{986,1}
};data.add(data2);
// creating data model
List<DataTable> dataTables = new ArrayList<DataTable>(); 
for(int i=0;i<data.size();i++)
{	DataTable dataTable = new DataTable(Integer.class, Integer.class);
	int[][] d = data.get(i);
	for(int j=0;j<d.length;j++)
		dataTable.add(d[j][0],d[j][1]);
	dataTables.add(dataTable);
}
DataSeries series1 = new DataSeries(dataTables.get(0), 0, 1);
DataSeries series2 = new DataSeries(dataTables.get(1), 0, 1);
		
		// Create new xy-plot
		XYPlot plot = new XYPlot(series1,series2);

		// Format plot
//		plot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
		plot.setSetting(XYPlot.BACKGROUND, GuiTools.COLOR_COMMON_BACKGROUND);
//		plot.setSetting(XYPlot.TITLE, "Descriptionnn");

		// Format plot area
//		plot.getPlotArea().setSetting(PlotArea.BACKGROUND, new RadialGradientPaint(
//			new Point2D.Double(0.5, 0.5),
//			0.75f,
//			new float[] { 0.6f, 0.8f, 1.0f },
//			new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 32), new Color(0, 0, 0, 128) }
//		));
		plot.getPlotArea().setSetting(PlotArea.BACKGROUND, null);
		plot.getPlotArea().setSetting(PlotArea.BORDER, null);

		// Format axes
		AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
		AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		axisRendererX.setSetting(AxisRenderer.LABEL, "Time");
//		BasicStroke stroke = new BasicStroke(2f);
//		axisRendererX.setSetting(AxisRenderer.SHAPE_STROKE, stroke);
		axisRendererY.setSetting(AxisRenderer.LABEL, "Bombs");
		// Change intersection point of Y axis
//		axisRendererY.setSetting(AxisRenderer.INTERSECTION, 1.0);
		// Change tick spacing
//		axisRendererX.setSetting(AxisRenderer.TICKS_SPACING, 2.0);

		// Format rendering of data points
		PointRenderer pointRenderer1 = new DefaultPointRenderer2D();
		Shape circle = new Ellipse2D.Double(-4.0, -4.0, 8.0, 8.0);
		pointRenderer1.setSetting(PointRenderer.SHAPE, circle);
		pointRenderer1.setSetting(PointRenderer.COLOR, Color.RED);
		plot.setPointRenderer(series2, pointRenderer1);
		PointRenderer pointRenderer2 = new DefaultPointRenderer2D();
		pointRenderer2.setSetting(PointRenderer.SHAPE, circle);
		pointRenderer2.setSetting(PointRenderer.COLOR, Color.BLUE);
		plot.setPointRenderer(series1, pointRenderer2);

		// Format data lines
		BasicStroke stroke = new BasicStroke(2f);
		LineRenderer lineRenderer1 = new SmoothLineRenderer2D();
		lineRenderer1.setSetting(LineRenderer.COLOR, Color.RED);
		lineRenderer1.setSetting(LineRenderer.GAP, 2.0);
		lineRenderer1.setSetting(LineRenderer.STROKE, stroke);
		plot.setLineRenderer(series2, lineRenderer1);
		LineRenderer lineRenderer2 = new SmoothLineRenderer2D();
		lineRenderer2.setSetting(LineRenderer.COLOR, Color.BLUE);
		lineRenderer1.setSetting(LineRenderer.GAP, 2.0);
		lineRenderer1.setSetting(LineRenderer.STROKE, stroke);
		plot.setLineRenderer(series1, lineRenderer2);

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
