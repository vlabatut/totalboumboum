package org.totalboumboum.gui.common.content.subpanel.file;

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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.TableContentPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * like the FolderBrowser, but for specific folders called packs,
 * with a slightly different structure (eg AI agents packages)
 * top level folder, so there's no ".." button like in FolderBrowser
 * 
 * @author Vincent Labatut
 *
 */
public class PackBrowserSubPanel extends TableSubPanel implements MouseListener, FolderBrowserSubPanelListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 20;
	private static final int COLS = 1;

	public PackBrowserSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COLS,false);
		
		// pages
		setFolder(null,null,new ArrayList<String>());
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String baseFolder;
	private String additionalFolder;
	private List<String> targetFiles;
	private int linePrevious;
	private int lineNext;
	private int controlTotalCount;
	private int controlUpCount;
	private int currentPage = 0;
	private ArrayList<TableContentPanel> listPanels;
	private ArrayList<String> names;
	private int pageCount;
	private FolderBrowserSubPanel filePanel;
	private String selectedName;
	
	public String getBaseFolder()
	{	return baseFolder;	
	}
	
	public List<String> getTargetFiles()
	{	return targetFiles;	
	}
	
	public void setFolder(String baseFolder, String additionalFolder, String targetFile)
	{	ArrayList<String> targetFiles = new ArrayList<String>();
		targetFiles.add(targetFile);
		setFolder(baseFolder,additionalFolder,targetFiles);
	}
	
	public void setFolder(String baseFolder, String additionalFolder, List<String> targetFiles)
	{	// init
		this.baseFolder = baseFolder;
		this.additionalFolder = additionalFolder;
		this.targetFiles = targetFiles;
		listPanels = new ArrayList<TableContentPanel>();
		currentPage = 0;
		filePanel = null;
		selectedName = null;
		
		// size
		linePrevious = 0;
		lineNext = LINES-1;
		controlUpCount = 1;
		controlTotalCount = controlUpCount+1;
//		int textMaxWidth = getDataWidth() - GuiTools.subPanelMargin*2;
		int textMaxWidth = getDataWidth();
		
		initNames();
		pageCount = getPageCount();
		
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	TableContentPanel listPanel = new TableContentPanel(getDataWidth(),getDataHeight(),LINES,COLS,false);
			listPanel.setColSubMinWidth(0,textMaxWidth);
			listPanel.setColSubPrefWidth(0,textMaxWidth);
			listPanel.setColSubMaxWidth(0,textMaxWidth);
		
			// data
			int line = controlUpCount;
			int nameIndex = panelIndex*(LINES-controlTotalCount);
			while(line<LINES && nameIndex<names.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = names.get(nameIndex);
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				MyLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
				nameIndex++;
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(linePrevious,0,bg);
				String key = GuiKeys.COMMON_BROWSER_PACK_PAGEUP;
				listPanel.setLabelKey(linePrevious,0,key,true);
				MyLabel label = listPanel.getLabel(linePrevious,0);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(lineNext,0,bg);
				String key = GuiKeys.COMMON_BROWSER_PACK_PAGEDOWN;
				listPanel.setLabelKey(lineNext,0,key,true);
				MyLabel label = listPanel.getLabel(lineNext,0);
				label.addMouseListener(this);
				label.setMouseSensitive(true);
			}
			listPanels.add(listPanel);
		}
		
		refreshList();
	}

	private void initNames()
	{	names = new ArrayList<String>();
		if(baseFolder!=null && targetFiles.size()>0)
		{	File fileBaseFolder = new File(baseFolder);
			FileFilter filter = new FileFilter()
			{	@Override
				public boolean accept(File pathname)
				{	boolean result = pathname.exists() && pathname.isDirectory();
					return result;
				}
			};
			List<File> fileFolders = Arrays.asList(fileBaseFolder.listFiles(filter));
			Collections.sort(fileFolders,new Comparator<File>()
			{	@Override
				public int compare(File arg0, File arg1)
				{	int result;
					String name0 = arg0.getName();
					String name1 = arg1.getName();
					Collator collator = Collator.getInstance(Locale.ENGLISH);
					result = collator.compare(name0,name1);
					return result;
				}
			});
			for(File f:fileFolders)
			{	File folder = f;
				if(additionalFolder!=null)
					folder = new File(f.getPath()+File.separator+additionalFolder);
				if(folder.exists())
				{	File[] folders = folder.listFiles();
					int i = 0;
					boolean foundAll = false;
					while(i<folders.length && !foundAll)
					{	if(folders[i].isDirectory())
						{	List<File> files = Arrays.asList(folders[i].listFiles());
							boolean found = true;
							Iterator<String> it = targetFiles.iterator();
							while(it.hasNext() && found)
							{	String targetFile = it.next();
								File testFile = new File(folders[i].getPath()+File.separator+targetFile);
								found = files.contains(testFile);
							}
							if(found)
								foundAll = true;
						}
						i++;
					}
					if(foundAll)
						names.add(f.getName());
				}
			}
		}
	}
	
	private int getPageCount()
	{	int result = names.size()/(LINES-controlTotalCount);
		if(names.size()%(LINES-controlTotalCount)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}		
	
	public String getSelectedName()
	{	String result = null;
		if(filePanel!=null)
			result = filePanel.getSelectedName();		
		return result;
	}
	
	public String getSelectedPack()
	{	String result = selectedName;
		return result;
	}
	
	private void selectPack(int row)
	{	if(row==-1)
		{	
//			if(filePanel!=null)
//				filePanel.removeListener(this);
			filePanel = null;
			selectedName = null;
		}
		else
		{	filePanel = new FolderBrowserSubPanel(getWidth(),getHeight());
			int selectedIndex = (row-controlUpCount)+currentPage*(LINES-controlTotalCount);
			selectedName = names.get(selectedIndex);
			String bFolder = baseFolder;
			bFolder = bFolder + File.separator + selectedName;
			if(additionalFolder!=null)
				bFolder = bFolder + File.separator + additionalFolder;
			filePanel.setFolder(bFolder,targetFiles);
			filePanel.addListener(this);
		}
		refreshList();
		// update listeners
//		firePackBrowserSelectionChange();
	}

	private void refreshList()
	{	getDataPanel().switchDisplay(false);
		TableContentPanel panel; 
		if(filePanel == null)
			panel = listPanels.get(currentPage);
		else
			panel = filePanel.getDataPanel();
		panel.switchDisplay(true);
		setDataPanel(panel);
		validate();
		repaint();
	}
	
	public void refresh()
	{	setFolder(baseFolder,additionalFolder,targetFiles);		
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
	{	MyLabel label = (MyLabel)e.getComponent();
		int[] pos = listPanels.get(currentPage).getLabelPosition(label);
		
		// previous page
		if(pos[0]==linePrevious)
		{	if(currentPage>0)
			{	currentPage--;
				refreshList();
			}
		}
		// next page
		else if(pos[0]==lineNext)
		{	if(currentPage<getPageCount()-1)
			{	currentPage++;
				refreshList();
			}
		}
		// select a name
		else if(pos[0]>=0)
		{	selectPack(pos[0]);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<PackBrowserSubPanelListener> listeners = new ArrayList<PackBrowserSubPanelListener>();
	
	public void addListener(PackBrowserSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(PackBrowserSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void firePackBrowserSelectionChanged()
	{	for(PackBrowserSubPanelListener listener: listeners)
			listener.packBrowserSelectionChanged();
	}

	/////////////////////////////////////////////////////////////////
	// FILE BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void folderBrowserSelectionChanged()
	{	firePackBrowserSelectionChanged();
	}

	@Override
	public void folderBrowserParentReached()
	{	selectPack(-1);
	}

	@Override
	public void folderBrowserPageChanged()
	{	refreshList();
	}
}
