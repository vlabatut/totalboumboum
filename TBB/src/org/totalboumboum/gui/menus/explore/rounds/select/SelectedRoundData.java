package org.totalboumboum.gui.menus.explore.rounds.select;

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

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.engine.container.level.preview.LevelPreviewLoader;
import org.totalboumboum.game.limit.Limit;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundLoader;
import org.totalboumboum.gui.common.content.subpanel.file.FolderBrowserSubPanel;
import org.totalboumboum.gui.common.content.subpanel.file.FolderBrowserSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import org.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import org.totalboumboum.gui.common.content.subpanel.round.RoundMiscSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.ImageSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.totalboumboum.tools.files.FileNames;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SelectedRoundData extends EntitledDataPanel implements FolderBrowserSubPanelListener, LimitsSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;
	
	private ImageSubPanel imagePanel;
	private LimitsSubPanel<RoundLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	private RoundMiscSubPanel miscPanel;
	private FolderBrowserSubPanel listPanel;
	
	private Round selectedRound = null;
	
	public SelectedRoundData(SplitMenuPanel container, String baseFolder)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_ROUND_TITLE);
		
		BasicPanel mainPanel;
		// data
		{	mainPanel = new BasicPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiSizeTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// list panel
			{	listPanel = new FolderBrowserSubPanel(leftWidth,dataHeight);
				String targetFile = FileNames.FILE_ROUND+FileNames.EXTENSION_XML;
				listPanel.setShowParent(false);
				listPanel.setFolder(baseFolder,targetFile);
				listPanel.addListener(this);
				mainPanel.add(listPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	int rightHeight = (int)((dataHeight - 2*margin)*0.375);
				int previewHeight = dataHeight - 2*rightHeight - 2*margin; 
				
				BasicPanel rightPanel = new BasicPanel(rightWidth,dataHeight);
				rightPanel.setOpaque(false);
				mainPanel.add(rightPanel);
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				
				rightPanel.add(Box.createVerticalGlue());

				{	
					BasicPanel upPanel = new BasicPanel(rightWidth,previewHeight);
					upPanel.setOpaque(false);
					{	BoxLayout layout = new BoxLayout(upPanel,BoxLayout.LINE_AXIS); 
						upPanel.setLayout(layout);
					}
					int rightUpWidth = (rightWidth - margin) / 2;
					int leftUpWidth = rightWidth - rightUpWidth - margin;
									
					// preview
					{	miscPanel = new RoundMiscSubPanel(leftUpWidth,previewHeight);
						upPanel.add(miscPanel);
					}
					
					upPanel.add(Box.createHorizontalGlue());

					// level preview
					{	imagePanel = new ImageSubPanel(rightUpWidth,previewHeight,Mode.BORDER);
						upPanel.add(imagePanel);
					}
					
					rightPanel.add(upPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	limitsPanel = new LimitsSubPanel<RoundLimit>(rightWidth,rightHeight,GuiKeys.ROUND);
					limitsPanel.addListener(this);
					rightPanel.add(limitsPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	pointsPanel = new PointsSubPanel(rightWidth,rightHeight,GuiKeys.ROUND);
					rightPanel.add(pointsPanel);
					limitSelectionChanged();
				}

				rightPanel.add(Box.createVerticalGlue());
			}
			
			setDataPart(mainPanel);
		}
	}
		
	private void refreshLimits()
	{	if(selectedRound==null)
			limitsPanel.setLimits(null);
		else
			limitsPanel.setLimits(selectedRound.getLimits());
	}
	
	private void refreshImage()
	{	BufferedImage image = null;
		if(selectedRound!=null)
		{	HollowLevel hollowLevel = selectedRound.getHollowLevel();
			String pack = hollowLevel.getLevelInfo().getPackName();
			String folder = hollowLevel.getLevelInfo().getFolder();
			try
			{	LevelPreview levelPreview = LevelPreviewLoader.loadLevelPreviewOnlyImage(pack, folder);
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
		// put the image
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE+GuiKeys.TOOLTIP);
		imagePanel.setImage(image,tooltip);
	}

	@Override
	public void refresh()
	{	listPanel.refresh();
		miscPanel.setRound(selectedRound);
		refreshImage();
		refreshLimits();
	}

	public Round getSelectedRound()
	{	return selectedRound;
	}
	public String getSelectedRoundFile()
	{	return listPanel.getSelectedName();
	}
	
/*
	public void setSelectedRound(String fileName)
	{	int index = rounds.indexOf(rounds);
		currentPage = index/(LIST_LINE_COUNT-2);
		refreshList();
		unselectList();
		selectedRow = index%(LIST_LINE_COUNT-2)+1;
		listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiColorTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
	}
*/
	
	/////////////////////////////////////////////////////////////////
	// LIMITS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void limitSelectionChanged()
	{	Limit limit = limitsPanel.getSelectedLimit();
		PointsProcessor pointsProcessor = null;
		if(limit!=null)
			pointsProcessor = limit.getPointProcessor();
		pointsPanel.setPointsProcessor(pointsProcessor);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	//
	}

	/////////////////////////////////////////////////////////////////
	// FILE BROWSER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void folderBrowserSelectionChanged()
	{	// load new selected round
		String selectedRoundFolder = listPanel.getSelectedName();
		if(selectedRoundFolder!=null)
		{	try
			{	String folderPath = listPanel.getBaseFolder()+File.separator+selectedRoundFolder;
				selectedRound = RoundLoader.loadRoundFromFolderPath(folderPath,null);			
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (SecurityException e)
			{	e.printStackTrace();
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
		else
			selectedRound = null;
		// refresh
		miscPanel.setRound(selectedRound);
		refreshImage();
		refreshLimits();
		fireDataPanelSelectionChange(null);
	}

	@Override
	public void folderBrowserParentReached()
	{	// useless here
	}

	@Override
	public void folderBrowserPageChanged()
	{	// useless here
	}
}
