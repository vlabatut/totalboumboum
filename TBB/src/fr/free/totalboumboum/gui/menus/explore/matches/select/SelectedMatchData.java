package fr.free.totalboumboum.gui.menus.explore.matches.select;

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
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FolderBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.FolderBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.match.MatchSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedMatchData extends EntitledDataPanel implements FolderBrowserSubPanelListener, LimitsSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;

	private FolderBrowserSubPanel folderPanel;
	private LimitsSubPanel<MatchLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	private MatchSubPanel miscPanel;

	private Match selectedMatch = null;	
	private String baseFolder;
	
	public SelectedMatchData(SplitMenuPanel container, String baseFolder)
	{	super(container);
		this.baseFolder = baseFolder;

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_MATCH_TITLE);
		
		SubPanel mainPanel;
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			
			// list panel
			{	folderPanel = new FolderBrowserSubPanel(leftWidth,dataHeight);
				String targetFile = FileTools.FILE_MATCH+FileTools.EXTENSION_DATA;
				folderPanel.setShowParent(false);
				folderPanel.setFolder(baseFolder,targetFile);
				folderPanel.addListener(this);
				mainPanel.add(folderPanel);
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	int rightHeight = (int)((dataHeight - 2*margin)*0.4);
				int previewHeight = dataHeight - 2*rightHeight - 2*margin; 
				
				SubPanel rightPanel = new SubPanel(rightWidth,dataHeight);
				rightPanel.setOpaque(false);
				mainPanel.add(rightPanel);
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				
				rightPanel.add(Box.createVerticalGlue());

				{	miscPanel = new MatchSubPanel(rightWidth,previewHeight);
					rightPanel.add(miscPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	limitsPanel = new LimitsSubPanel<MatchLimit>(rightWidth,rightHeight,GuiKeys.MATCH);
					limitsPanel.addListener(this);
					rightPanel.add(limitsPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	pointsPanel = new PointsSubPanel(rightWidth,rightHeight,GuiKeys.MATCH);
					rightPanel.add(pointsPanel);
					limitSelectionChange();
				}

				rightPanel.add(Box.createVerticalGlue());
			}
			
			setDataPart(mainPanel);
			
		}
	}
		
	private void refreshLimits()
	{	if(selectedMatch==null)
			limitsPanel.setLimits(null);
		else
			limitsPanel.setLimits(selectedMatch.getLimits());
	}
	
	@Override
	public void refresh()
	{	folderPanel.refresh();
		miscPanel.setMatch(selectedMatch);
		refreshLimits();
/*
		initMatches();
		makeListPanels(leftWidth,dataHeight);
		refreshList();
		if(selectedRow!=-1)
			listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
		refreshLimits();
*/		
//		rightPanel.validate();
//		rightPanel.repaint();
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED MATCH	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Match getSelectedMatch()
	{	return selectedMatch;
	}
	
	public String getSelectedMatchFile()
	{	return folderPanel.getSelectedName();
	}
/*
	public void setSelectedMatch(String fileName)
	{	int index = matches.indexOf(matches);
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
	// PACK BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void folderBrowserParent()
	{	// no use here
	}
	
	@Override
	public void folderBrowserSelectionChange()
	{	String folder = folderPanel.getSelectedName();
		if(folder==null)
			selectedMatch = null;
		else
		{	try
			{	String folderPath = baseFolder+File.separator+folder;
				selectedMatch = MatchLoader.loadMatchFromFolderPath(folderPath,null);			
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
		miscPanel.setMatch(selectedMatch);
		refreshLimits();
	}
}
