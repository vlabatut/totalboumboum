package fr.free.totalboumboum.gui.menus.explore.levels.select;

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.image.ImageSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.level.LevelSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedLevelData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_PARENT = 1;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int LIST_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int PREVIEW_PANEL_INDEX = 2;
	
	@SuppressWarnings("unused")
	private static final int INFOS_PANEL_INDEX = 0;
	@SuppressWarnings("unused")
	private static final int IMAGE_PANEL_INDEX = 2;

	private SubPanel mainPanel;
	private SubPanel previewPanel;
	private LevelSubPanel infosPanel;
	private ImageSubPanel imagePanel;
	private int listWidth;
	private int listHeight;
	
	private ArrayList<UntitledSubPanelTable> listPackagePanels;
	private int currentPackagePage = 0;
	private int selectedPackageRow = -1;
	private ArrayList<String> levelPackages;
	private ArrayList<ArrayList<String>> levelFolders;

	private ArrayList<UntitledSubPanelTable> listLevelPanels;
	private int currentLevelPage = 0;
	private int selectedLevelRow = -1;
	private LevelPreview selectedLevelPreview = null;
		
	private boolean packageMode = true; //false if levels are displayed 
	
	public SelectedLevelData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_TITLE);
	
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			initPackages();
			
			// list panel
			{	listWidth = leftWidth;
				listHeight = dataHeight;
				makePackagesListPanels(leftWidth,dataHeight);
				mainPanel.add(listPackagePanels.get(currentPackagePage));
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	previewPanel = new SubPanel(rightWidth,dataHeight);
				{	BoxLayout layout = new BoxLayout(previewPanel,BoxLayout.PAGE_AXIS); 
					previewPanel.setLayout(layout);
				}
				previewPanel.setOpaque(false);
				
				int upHeight = (int)(dataHeight*0.5); 
				int downHeight = dataHeight - upHeight - margin; 
				
				infosPanel = new LevelSubPanel(rightWidth,upHeight);
				infosPanel.setShowTitle(false);
				infosPanel.setLevelPreview(selectedLevelPreview,10);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				imagePanel = new ImageSubPanel(rightWidth,downHeight);
				previewPanel.add(imagePanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}
		
	/**
	 * builds the list of levels (main) packages and of levels folders (or secondary packages).
	 * for each folder, tests if a level.xml file is present.
	 */
	private void initPackages()
	{	levelPackages = new ArrayList<String>();
		levelFolders = new ArrayList<ArrayList<String>>();
		
		// packages
		String levelMainName = FileTools.getLevelsPath();
		File levelMainFile = new File(levelMainName);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = pathname.exists() && pathname.isDirectory();
				return result;
			}
		};
		File levelPackageFilesTemp[] = levelMainFile.listFiles(filter);
		ArrayList<File> levelPackageFiles = new ArrayList<File>();
		for(int i=0;i<levelPackageFilesTemp.length;i++)
			levelPackageFiles.add(levelPackageFilesTemp[i]);
		Comparator<File> comparator = new Comparator<File>()
		{	@Override
			public int compare(File o1, File o2)
			{	String n1 = o1.getName();
				String n2 = o2.getName();
				int result = n1.compareTo(n2);				
				return result;
			}			
		};	
		Collections.sort(levelPackageFiles,comparator);

		// levels
		Iterator<File> p = levelPackageFiles.iterator();
		while(p.hasNext())
		{	// get the list of folders in this package
			File levelPackageFile = p.next();
			String packageName = levelPackageFile.getName();
			File levelFolderFiles[] = levelPackageFile.listFiles(filter);
			ArrayList<File> levelFiles = new ArrayList<File>();
			for(int i=0;i<levelFolderFiles.length;i++)
				levelFiles.add(levelFolderFiles[i]);
			Collections.sort(levelFiles,comparator);
			ArrayList<String> levelFoldersTemp = new ArrayList<String>();
			Iterator<File> q = levelFiles.iterator();
			while(q.hasNext())
			{	// find the XML file
				File levelFolderFile = q.next();
				String folderName = levelFolderFile.getName();
				File[] content = levelFolderFile.listFiles();
				String xmlFileName = FileTools.FILE_LEVEL+FileTools.EXTENSION_DATA;
				File xmlFile = FileTools.getFile(xmlFileName,content);
				if(xmlFile!=null)
					levelFoldersTemp.add(folderName);
			}
			// add to the list of levels for this package
			if(levelFoldersTemp.size()>0)
			{	levelFolders.add(levelFoldersTemp);
				levelPackages.add(packageName);
			}
		}
	}
	
	private void makePackagesListPanels(int width, int height)
	{	int cols = 1;
		listPackagePanels = new ArrayList<UntitledSubPanelTable>();		
		Iterator<String> it = levelPackages.iterator(); 
		for(int panelIndex=0;panelIndex<getPackagesPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setColSubMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = LIST_LINE_PREVIOUS+1;
			while(line<LIST_LINE_NEXT && it.hasNext())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = it.next();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPackagePanels.add(listPanel);
		}
	}

	private void makeFoldersListPanels(int width, int height)
	{	int cols = 1;
		listLevelPanels = new ArrayList<UntitledSubPanelTable>();
		int selectedPackageIndex = currentPackagePage*(LIST_LINE_COUNT-2)+(selectedPackageRow-1);
		Iterator<String> it = levelFolders.get(selectedPackageIndex).iterator();
		for(int panelIndex=0;panelIndex<getFoldersPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,LIST_LINE_COUNT,false);
			listPanel.setColSubMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = LIST_LINE_PARENT+1;
			while(line<LIST_LINE_NEXT && it.hasNext())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String levelFolder = it.next();
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,levelFolder,levelFolder);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// parent
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PARENT,0,bg);
				String key = GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PARENT;
				listPanel.setLabelKey(LIST_LINE_PARENT,0,key,false);
				JLabel label = listPanel.getLabel(LIST_LINE_PARENT,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listLevelPanels.add(listPanel);
		}
	}

	private void refreshPreview()
	{	BufferedImage image;
		// no level selected
		if(packageMode || selectedLevelRow<0)
		{	// image
			image = null;
			// infos
			selectedLevelPreview = null;
		}
		// one level selected
		else
		{	// image
			image = selectedLevelPreview.getVisualPreview();
			// infos
		}
		// infos
		infosPanel.setLevelPreview(selectedLevelPreview,10);
		// image
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE+GuiKeys.TOOLTIP);
		imagePanel.setImage(image,tooltip);
	}

	@Override
	public void refresh()
	{	refreshPreview();
	}

	@Override
	public void updateData()
	{	// nothing to do here
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
	{	JLabel label = (JLabel)e.getComponent();
		// list = packages
		if(packageMode)
		{	int[] pos = listPackagePanels.get(currentPackagePage).getLabelPosition(label);
			switch(pos[0])
			{	// previous page
				case LIST_LINE_PREVIOUS:
					if(currentPackagePage>0)
					{	unselectList();
						currentPackagePage--;
						refreshList();
					}
					break;
				// next page
				case LIST_LINE_NEXT:
					if(currentPackagePage<getPackagesPageCount()-1)
					{	unselectList();
						currentPackagePage++;
						refreshList();
					}
					break;
				// package selected
				default:
					unselectList();
					selectedPackageRow = pos[0];
					listPackagePanels.get(currentPackagePage).setLabelBackground(selectedPackageRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
					makeFoldersListPanels(listWidth,listHeight);
					packageMode = false;
					currentLevelPage = 0;
					refreshList();
			}
		}
		// list = level
		else
		{	int[] pos = listLevelPanels.get(currentLevelPage).getLabelPosition(label);
			switch(pos[0])
			{	// previous page
				case LIST_LINE_PREVIOUS:
					if(currentLevelPage>0)
					{	unselectList();
						currentLevelPage--;
						refreshList();
					}
					break;
				// go to package
				case LIST_LINE_PARENT:
					unselectList();
					packageMode = true;
					refreshPreview();
					unselectList();
					refreshList();
					break;
				// next page
				case LIST_LINE_NEXT:
					if(currentLevelPage<getFoldersPageCount()-1)
					{	unselectList();
						currentLevelPage++;
						refreshList();
					}
					break;
				// level selected
				default:
					unselectList();
					selectedLevelRow = pos[0];
					loadSelectedLevelPreview();
					listLevelPanels.get(currentLevelPage).setLabelBackground(selectedLevelRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
					refreshPreview();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private void loadSelectedLevelPreview()
	{	// package
		int selectedPackageIndex = currentPackagePage*(LIST_LINE_COUNT-2)+(selectedPackageRow-1);
		String packageName = levelPackages.get(selectedPackageIndex);
		// folder
		int selectedFolderIndex = currentLevelPage*(LIST_LINE_COUNT-3)+(selectedLevelRow-2);
		ArrayList<String> levelList = levelFolders.get(selectedPackageIndex);
		String folderName = levelList.get(selectedFolderIndex);
		try
		{	selectedLevelPreview = LevelPreviewLoader.loadLevelPreview(packageName,folderName);
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
	
	private int getPackagesPageCount()
	{	int result = levelPackages.size()/(LIST_LINE_COUNT-2);
		if(levelPackages.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	private int getFoldersPageCount()
	{	int selectedPackageIndex = currentPackagePage*(LIST_LINE_COUNT-2)+(selectedPackageRow-1);
		int zize = levelFolders.get(selectedPackageIndex).size();
		int result = zize/(LIST_LINE_COUNT-2);
		if(result>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	public void unselectList()
	{	// packages displayed
		if(packageMode && selectedPackageRow!=-1)
		{	listPackagePanels.get(currentPackagePage).setLabelBackground(selectedPackageRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedPackageRow = -1;
			//refreshPreview();
		}
		// hero displayed
		else if(!packageMode && selectedLevelRow!=-1)
		{	listLevelPanels.get(currentLevelPage).setLabelBackground(selectedLevelRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedLevelRow = -1;
			selectedLevelPreview = null;
			refreshPreview();
		}
	}
	
	private void refreshList()
	{	// packages displayed
		if(packageMode)
		{	mainPanel.remove(LIST_PANEL_INDEX);
			mainPanel.add(listPackagePanels.get(currentPackagePage),LIST_PANEL_INDEX);
		}
		// hero displayed
		else
		{	mainPanel.remove(LIST_PANEL_INDEX);
			mainPanel.add(listLevelPanels.get(currentLevelPage),LIST_PANEL_INDEX);
		}
		// common
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public LevelPreview getSelectedLevelPreview()
	{	return selectedLevelPreview;
	}

}
