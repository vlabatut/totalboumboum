package fr.free.totalboumboum.gui.menus.explore.rounds.select;

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
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FileBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FileBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.image.ImageSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.round.RoundSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedRoundData extends EntitledDataPanel implements FileBrowserSubPanelListener, LimitsSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;
	
	private SubPanel mainPanel;
	private SubPanel rightPanel;
	private ImageSubPanel imagePanel;
	private LimitsSubPanel<RoundLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	private RoundSubPanel miscPanel;
	private FileBrowserSubPanel listPanel;
	
	private Round selectedRound = null;
	private int leftWidth;
	private int rightWidth;
	private int rightHeight;
	private int rightUpWidth;
	private int leftUpWidth;
	
	
	public SelectedRoundData(SplitMenuPanel container, String baseFolder)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_ROUND_TITLE);
	
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// list panel
			{	listPanel = new FileBrowserSubPanel(leftWidth,dataHeight);
				String targetFile = FileTools.FILE_ROUND+FileTools.EXTENSION_DATA;
				listPanel.setShowParent(false);
				listPanel.setFolder(baseFolder,targetFile);
				listPanel.addListener(this);
				mainPanel.add(listPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	rightHeight = (int)((dataHeight - 2*margin)*0.375);
				int previewHeight = dataHeight - 2*rightHeight - 2*margin; 
				
				rightPanel = new SubPanel(rightWidth,dataHeight);
				rightPanel.setOpaque(false);
				mainPanel.add(rightPanel);
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				
				rightPanel.add(Box.createVerticalGlue());

				{	
					SubPanel upPanel = new SubPanel(rightWidth,previewHeight);
					upPanel.setOpaque(false);
					{	BoxLayout layout = new BoxLayout(upPanel,BoxLayout.LINE_AXIS); 
						upPanel.setLayout(layout);
					}
					rightUpWidth = (rightWidth - margin) / 2;
					leftUpWidth = rightWidth - rightUpWidth - margin;
									
					// preview
					{	miscPanel = new RoundSubPanel(leftUpWidth,previewHeight);
						upPanel.add(miscPanel);
					}
					
					upPanel.add(Box.createHorizontalGlue());

					// level preview
					{	imagePanel = new ImageSubPanel(rightUpWidth,previewHeight);
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
					limitSelectionChange();
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
			String pack = hollowLevel.getPackName();
			String folder = hollowLevel.getFolderName();
			LevelPreview levelPreview;
			try
			{	levelPreview = LevelPreviewLoader.loadLevelPreview(pack, folder);
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
		listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
	}
*/
	
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

	/////////////////////////////////////////////////////////////////
	// FILE BROWSER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void fileBrowserSelectionChange()
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
	}

	@Override
	public void fileBrowserParent()
	{	
	}
}
