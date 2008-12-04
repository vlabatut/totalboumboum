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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.content.subpanel.image.ImageSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.items.AvailableItemsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.items.InitialItemsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.level.LevelSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class RoundDescription extends EntitledDataPanel implements LimitsListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;

	private JPanel downPanel;
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
		{	Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
			HollowLevel hollowLevel = round.getHollowLevel();
			LevelPreview preview = null;
			try
			{	preview = LevelPreviewLoader.loadLevelPreview(hollowLevel.getPackName(),hollowLevel.getFolderName());
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
					BufferedImage image = preview.getVisualPreview();
					String key = GuiKeys.GAME_ROUND_DESCRIPTION_PREVIEW_TITLE+GuiKeys.TOOLTIP;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					imagePanel.setImage(image,tooltip);
					leftPanel.add(imagePanel);
				}

				leftPanel.add(Box.createVerticalGlue());
				
				// itemset panel
				{	int innerHeight = dataHeight - leftWidth - margin;
					availableItemsPanel = new AvailableItemsSubPanel(leftWidth,innerHeight);
					availableItemsPanel.setLevelPreview(preview);
//					itemsetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
						miscPanel.setLevelPreview(preview,8);
						upPanel.add(miscPanel);
					}
					
					upPanel.add(Box.createHorizontalGlue());
					
					// initial items panel
					{	initialItemsPanel = new InitialItemsSubPanel(innerWidth,upHeight);
						initialItemsPanel.setLevelPreview(preview);
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
						limitSelectionChange();
//						SubPanel pointsPanel = makePointsPanel(innerWidth,downHeight,round.getPointProcessor(),GuiKeys.ROUND);
//						downPanel.add(pointsPanel);
					}
					
					rightPanel.add(downPanel);
				}
				infoPanel.add(rightPanel);
			}

			setDataPart(infoPanel);
			
			limitsPanel.setLimits(round.getLimits());
		}
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
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
