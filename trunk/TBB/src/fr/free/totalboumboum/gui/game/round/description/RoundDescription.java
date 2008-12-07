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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.content.subpanel.image.ImageSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.items.AvailableItemsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.items.InitialItemsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.level.LevelSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class RoundDescription extends EntitledDataPanel implements LimitsSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;

	private JPanel downPanel;
	//
	private ImageSubPanel imagePanel;
	private InitialItemsSubPanel initialItemsPanel;
	private AvailableItemsSubPanel availableItemsPanel;
	private LimitsSubPanel<RoundLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	private LevelSubPanel miscPanel;
	
	public RoundDescription(SplitMenuPanel container)
	{	super(container);
	
		// title
		{	String key = GuiKeys.GAME_ROUND_DESCRIPTION_TITLE;
			setTitleKey(key);
		}
	
		// data
		{	SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
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
				
				// image panel
				{	int innerHeight = leftWidth;
					imagePanel = new ImageSubPanel(leftWidth,innerHeight);
					leftPanel.add(imagePanel);
				}

				leftPanel.add(Box.createVerticalGlue());
				
				// available itemset panel
				{	int innerHeight = dataHeight - leftWidth - margin;
					availableItemsPanel = new AvailableItemsSubPanel(leftWidth,innerHeight);
					leftPanel.add(availableItemsPanel);
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
					{	miscPanel = new LevelSubPanel(innerWidth,upHeight);
						upPanel.add(miscPanel);
					}
					
					upPanel.add(Box.createHorizontalGlue());
					
					// initial items panel
					{	initialItemsPanel = new InitialItemsSubPanel(innerWidth,upHeight);
						upPanel.add(initialItemsPanel);
					}
					rightPanel.add(upPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
				
				// down panel
				{	downPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(downPanel,BoxLayout.LINE_AXIS); 
						downPanel.setLayout(layout);
					}
					downPanel.setOpaque(false);
					dim = new Dimension(rightWidth,upHeight);
					downPanel.setPreferredSize(dim);
					downPanel.setMinimumSize(dim);
					downPanel.setMaximumSize(dim);
					int innerWidth = (rightWidth - margin)/2;
					
					// limits panel
					{	limitsPanel = new LimitsSubPanel<RoundLimit>(innerWidth,downHeight,GuiKeys.ROUND);
						limitsPanel.addListener(this);
						downPanel.add(limitsPanel);
					}
					
					downPanel.add(Box.createHorizontalGlue());

					// points panel
					{	pointsPanel = new PointsSubPanel(innerWidth,downHeight,GuiKeys.ROUND);
						downPanel.add(pointsPanel);
					}
					
					rightPanel.add(downPanel);
				}
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private Round round;
	private HollowLevel hollowLevel;
	private LevelPreview levelPreview;
	
	public void setRound(Round round)
	{	// init
		this.round = round;
		BufferedImage image = null;
		Limits<RoundLimit> limits = null;
		if(round==null)
		{	hollowLevel = null;
			levelPreview = null;
		}
		else
		{	hollowLevel = round.getHollowLevel();
			limits = round.getLimits();
			try
			{	levelPreview = LevelPreviewLoader.loadLevelPreview(hollowLevel.getPackName(),hollowLevel.getFolderName());
				image = levelPreview.getVisualPreview();
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
		}
		// image panel
		String key = GuiKeys.GAME_ROUND_DESCRIPTION_PREVIEW+GuiKeys.TOOLTIP;
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
		imagePanel.setImage(image,tooltip);
		// level
		miscPanel.setLevelPreview(levelPreview,8);
		// initial items panel
		initialItemsPanel.setLevelPreview(levelPreview);
		// available items panel
		availableItemsPanel.setLevel(levelPreview,hollowLevel);
		// limits & points
		limitsPanel.setLimits(limits);
//		limitSelectionChange();
	}
	
	public Round getRound()
	{	return round;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	setRound(round);
	}

	/////////////////////////////////////////////////////////////////
	// LIMITS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void limitSelectionChange()
	{	Limit limit = limitsPanel.getSelectedLimit();
		PointsProcessor pointsProcessor = null;
		if(limit!=null)
			pointsProcessor = limit.getPointProcessor();
		pointsPanel.setPointsProcessor(pointsProcessor);
	}
}
