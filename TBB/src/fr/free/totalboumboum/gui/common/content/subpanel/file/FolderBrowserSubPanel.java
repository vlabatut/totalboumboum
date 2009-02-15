package fr.free.totalboumboum.gui.common.content.subpanel.file;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.structure.subpanel.inside.TableContentPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.TableSubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class FolderBrowserSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;

	public FolderBrowserSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,1,1,1,false);
		
		// pages
		setFolder(null,new ArrayList<String>());
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String baseFolder;
	private ArrayList<String> targetFiles;
	private int lines;
	private int linePrevious;
	private int lineParent;
	private int lineNext;
	private int controlTotalCount;
	private int controlUpCount;
	private int selectedRow;
	private int currentPage = 0;
	private ArrayList<TableContentPanel> listPanels;
	private ArrayList<String> names;
	private int pageCount;	
	
	public String getBaseFolder()
	{	return baseFolder;	
	}
	
	public ArrayList<String> getTargetFiles()
	{	return targetFiles;	
	}
	
	public void setFolder(String baseFolder, String targetFile)
	{	ArrayList<String> targetFiles = new ArrayList<String>();
		targetFiles.add(targetFile);
		setFolder(baseFolder,targetFiles);
	}
	
	public void setFolder(String baseFolder, ArrayList<String> targetFiles)
	{	// init
		this.baseFolder = baseFolder;
		this.targetFiles = targetFiles;
		listPanels = new ArrayList<TableContentPanel>();
		currentPage = 0;
		selectedRow = -1;fireFolderBrowserSelectionChanged();
		
		// size
		lines = 20;
		linePrevious = 0;
		lineParent = 1;
		lineNext = lines-1;
		controlUpCount = 1;
		if(showParent)
			controlUpCount = 2;
		controlTotalCount = controlUpCount+1;
		int cols = 1;
		int textMaxWidth = getDataWidth() - GuiTools.subPanelMargin*2;
		
		initNames();
		pageCount = getPageCount();
		
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	TableContentPanel listPanel = new TableContentPanel(getDataWidth(),getDataHeight(),cols,lines,false);
			listPanel.setColSubMinWidth(0,textMaxWidth);
			listPanel.setColSubPreferredWidth(0,textMaxWidth);
			listPanel.setColSubMaxWidth(0,textMaxWidth);
		
			// data
			int line = controlUpCount;
			int nameIndex = panelIndex*(lines-controlTotalCount);
			while(line<lines && nameIndex<names.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = names.get(nameIndex);
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				nameIndex++;
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(linePrevious,0,bg);
				String key = GuiKeys.COMMON_BROWSER_FILE_PAGEUP;
				listPanel.setLabelKey(linePrevious,0,key,true);
				JLabel label = listPanel.getLabel(linePrevious,0);
				label.addMouseListener(this);
			}
			// parent
			if(showParent)
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(lineParent,0,bg);
				String key = GuiKeys.COMMON_BROWSER_FILE_PARENT;
				listPanel.setLabelKey(lineParent,0,key,false);
				JLabel label = listPanel.getLabel(lineParent,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(lineNext,0,bg);
				String key = GuiKeys.COMMON_BROWSER_FILE_PAGEDOWN;
				listPanel.setLabelKey(lineNext,0,key,true);
				JLabel label = listPanel.getLabel(lineNext,0);
				label.addMouseListener(this);
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
			{	List<File> files = Arrays.asList(f.listFiles());
				boolean found = true;
				Iterator<String> it = targetFiles.iterator();
				while(it.hasNext() && found)
				{	String targetFile = it.next();
					File testFile = new File(f.getPath()+File.separator+targetFile);
					found = files.contains(testFile);
				}
				if(found)
					names.add(f.getName());
			}
		}
	}
	
	private int getPageCount()
	{	int result = names.size()/(lines-controlTotalCount);
		if(names.size()%(lines-controlTotalCount)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}		
	
	public String getSelectedName()
	{	String result = null;
		if(selectedRow!=-1)
		{	int selectedIndex = (selectedRow-controlUpCount)+currentPage*(lines-controlTotalCount);
			result = names.get(selectedIndex);		
		}
		return result;
	}
	
	private void selectName(int row)
	{	TableContentPanel table = listPanels.get(currentPage);
		// unselect the previous selected line
		if(selectedRow!=-1)
			table.setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
		// select the new line
		selectedRow = row;
		if(selectedRow!=-1)
			table.setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		// update listeners
		fireFolderBrowserSelectionChanged();
	}

	private void refreshList()
	{	TableContentPanel table = listPanels.get(currentPage);
		setDataPanel(table);
		validate();
		repaint();
	}
	
	public void refresh()
	{	setFolder(baseFolder,targetFiles);		
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showParent = true;
	
	public boolean getShowParent()
	{	return showParent;
	}

	public void setShowParent(boolean showParent)
	{	this.showParent = showParent;
		refresh();
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
		int[] pos = listPanels.get(currentPage).getLabelPosition(label);
		
		// previous page
		if(pos[0]==linePrevious)
		{	if(currentPage>0)
			{	selectName(-1);
				currentPage--;
				refreshList();
			}
		}
		// parent
		else if(pos[0]==lineParent && showParent)
		{	selectName(-1);
			refreshList();
			fireFolderBrowserParentClicked();
		}
		// next page
		else if(pos[0]==lineNext)
		{	if(currentPage<getPageCount()-1)
			{	selectName(-1);
				currentPage++;
				refreshList();
			}
		}
		// select a name
		else
		{	selectName(pos[0]);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<FolderBrowserSubPanelListener> listeners = new ArrayList<FolderBrowserSubPanelListener>();
	
	public void addListener(FolderBrowserSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(FolderBrowserSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireFolderBrowserSelectionChanged()
	{	for(FolderBrowserSubPanelListener listener: listeners)
			listener.packBrowserSelectionChanged();
	}

	private void fireFolderBrowserParentClicked()
	{	for(FolderBrowserSubPanelListener listener: listeners)
			listener.packBrowserParentReached();
	}
}
