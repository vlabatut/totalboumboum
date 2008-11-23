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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.StringTools;

public class SelectedMatchData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	private static final int POINTS_PANEL_INDEX = 3;
	private static final int LIMITS_PANEL_INDEX = 5;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int VIEW_LINE_COUNT = 4;
	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_AUTHOR = 1;
	private static final int VIEW_LINE_ROUNDS = 2;
	private static final int VIEW_LINE_PLAYERS = 3;

	private static final int LIST_PANEL_INDEX = 0;
	
	private ArrayList<UntitledSubPanelTable> listPanels;
	private int currentPage = 0;
	private int selectedRow = -1;
	
	private SubPanel mainPanel;
	private SubPanel rightPanel;
	private SubPanel limitsPanel;
	private SubPanel pointsPanel;
	private UntitledSubPanelTable previewPanel;

	private ArrayList<String> matches;
	private Match selectedMatch = null;
	private String selectedMatchFolder = null;
	private int leftWidth;
	private int rightWidth;
	private int rightHeight;
	
	private String baseFolder;
	
	public SelectedMatchData(SplitMenuPanel container, String baseFolder)
	{	super(container);
		this.baseFolder = baseFolder;

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_MATCH_SELECT_TITLE);
	
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			initMatches();
			
			// list panel
			{	makeListPanels(leftWidth,dataHeight);
				mainPanel.add(listPanels.get(currentPage));
			}
			
			mainPanel.add(Box.createHorizontalGlue());
			
			// right panel
			{	rightHeight = (int)((dataHeight - 2*margin)*0.4);
				int previewHeight = dataHeight - 2*rightHeight - 2*margin; 
				
				rightPanel = new SubPanel(rightWidth,dataHeight);
				rightPanel.setOpaque(false);
				mainPanel.add(rightPanel);
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				
				rightPanel.add(Box.createVerticalGlue());

				{	makePreviewPanel(rightWidth,previewHeight);
					rightPanel.add(previewPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	limitsPanel = makeLimitsPanel(rightWidth,rightHeight);
					rightPanel.add(limitsPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	pointsPanel = makePointsPanel(rightWidth,previewHeight);
					rightPanel.add(pointsPanel);
				}

				rightPanel.add(Box.createVerticalGlue());
			}
			
			setDataPart(mainPanel);
			
		}
	}
		
	private void initMatches()
	{	matches = new ArrayList<String>();
		File heroMainFile = new File(baseFolder);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = pathname.exists() && pathname.isDirectory();
				return result;
			}
		};
		List<File> matchesFolders = Arrays.asList(heroMainFile.listFiles(filter));
		Collections.sort(matchesFolders,new Comparator<File>()
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
		for(File f:matchesFolders)
		{	List<File> files = Arrays.asList(f.listFiles());
			String fileName = FileTools.FILE_MATCH+FileTools.EXTENSION_DATA;
			Iterator<File> it = files.iterator();
			boolean found = false;
			while(it.hasNext() && !found)
			{	File file = it.next();
				if(file.getName().equalsIgnoreCase(fileName))
					found = true;
			}
			if(found)
				matches.add(f.getName());
		}
	}
	
	private void makeListPanels(int width, int height)
	{	int lines = LIST_LINE_COUNT;
		int cols = 1;
		listPanels = new ArrayList<UntitledSubPanelTable>();
		
		for(int panelIndex=0;panelIndex<getPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = 1;
			int matchIndex = panelIndex*(LIST_LINE_COUNT-2);
			while(line<LIST_LINE_NEXT && matchIndex<matches.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = matches.get(matchIndex);
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				matchIndex++;
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiKeys.MENU_RESOURCES_MATCH_SELECT_LIST_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiKeys.MENU_RESOURCES_MATCH_SELECT_LIST_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPanels.add(listPanel);
		}
	}
	
	private void makePreviewPanel(int width, int height)
	{	int colSubs = 2;
		int colGroups = 1;
		previewPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,VIEW_LINE_COUNT,true);
		
		// data
		String keys[] = 
		{	GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_NAME,
			GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_AUTHOR,
			GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_ROUNDS,
			GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_PLAYERS,
		};
		for(int line=0;line<keys.length;line++)
		{	int colSub = 0;
			{	previewPanel.setLabelKey(line,colSub,keys[line],true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				previewPanel.setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				previewPanel.setLabelBackground(line,colSub,bg);
				colSub++;
			}
			{	String text = null;
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys[line]+GuiKeys.TOOLTIP);
				previewPanel.setLabelText(line,colSub,text,tooltip);
				if(line>0)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					previewPanel.setLabelBackground(line,colSub,bg);
				}
				colSub++;
			}
		}
		int maxWidth = width-3*GuiTools.subPanelMargin-previewPanel.getHeaderHeight();
		previewPanel.setSubColumnsMaxWidth(1,maxWidth);
		previewPanel.setSubColumnsPreferredWidth(1,maxWidth);
	}
	
	private void refreshPreview()
	{	String values[] = new String[VIEW_LINE_COUNT];
		// no match selected
		if(selectedRow<0)
		{	for(int i=0;i<values.length;i++)
				values[i] = null;	
			selectedMatch = null;
			selectedMatchFolder = null;
		}
		// one match selected
		else
		{	try
			{	selectedMatchFolder = matches.get((selectedRow-1)+currentPage*(LIST_LINE_COUNT-2));
				String folderPath = baseFolder+File.separator+selectedMatchFolder;
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
			values[VIEW_LINE_NAME] = selectedMatchFolder;
			values[VIEW_LINE_AUTHOR]= selectedMatch.getAuthor();
			values[VIEW_LINE_ROUNDS] = Integer.toString(selectedMatch.getRound().size());
			values[VIEW_LINE_PLAYERS] = StringTools.formatAllowedPlayerNumbers(selectedMatch.getAllowedPlayerNumbers());
		}
		// common
		for(int line=0;line<values.length;line++)
		{	int colSub = 1;
			String text = values[line];
			String tooltip = text;
			previewPanel.setLabelText(line,colSub,text,tooltip);
		}
	}

	private SubPanel makePointsPanel(int width, int height)
	{	EntitledSubPanel result = new EntitledSubPanel(width,height);
		String key = GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_POINTS;
		result.setTitleKey(key,true);
		return result;
	}
	
	private SubPanel makeLimitsPanel(int width, int height)
	{	EntitledSubPanel result = new EntitledSubPanel(width,height);
		String key = GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_LIMIT;
		result.setTitleKey(key,true);
		return result;
	}
	
	private void refreshPoints()
	{	if(selectedMatch==null)
			pointsPanel = makePointsPanel(rightWidth,rightHeight);
		else
			pointsPanel = RoundDescription.makePointsPanel(rightWidth,rightHeight,selectedMatch.getPointProcessor(),"Match");		
		rightPanel.remove(POINTS_PANEL_INDEX);
		rightPanel.add(pointsPanel,POINTS_PANEL_INDEX);
	}
	
	private void refreshLimits()
	{	if(selectedMatch==null)
			limitsPanel = makeLimitsPanel(rightWidth,rightHeight);
		else
			limitsPanel = RoundDescription.makeLimitsPanel(rightWidth,rightHeight,selectedMatch.getLimits(),"Match");
		rightPanel.remove(LIMITS_PANEL_INDEX);
		rightPanel.add(limitsPanel,LIMITS_PANEL_INDEX);
	}
	
	@Override
	public void refresh()
	{	initMatches();
		makeListPanels(leftWidth,dataHeight);
		refreshList();
		if(selectedRow!=-1)
			listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
		refreshPoints();
		refreshLimits();
//		rightPanel.validate();
//		rightPanel.repaint();
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
		int[] pos = listPanels.get(currentPage).getLabelPosition(label);
		switch(pos[0])
		{	// previous page
			case LIST_LINE_PREVIOUS:
				if(currentPage>0)
				{	unselectList();
					currentPage--;
					refreshList();
				}
				break;
			// next page
			case LIST_LINE_NEXT:
				if(currentPage<getPageCount()-1)
				{	unselectList();
					currentPage++;
					refreshList();
				}
				break;
			// profile selected
			default:
				unselectList();
				selectedRow = pos[0];
				listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
				refreshPreview();
				refreshPoints();
				refreshLimits();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private int getPageCount()
	{	int result = matches.size()/(LIST_LINE_COUNT-2);
		if(matches.size()%(LIST_LINE_COUNT-2)>0)
			result++;
		else if(result==0)
			result = 1;
		return result;
	}
	
	public void unselectList()
	{	if(selectedRow!=-1)
		{	listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedRow = -1;
			refreshPreview();
		}		
	}
	
	private void refreshList()
	{	mainPanel.remove(LIST_PANEL_INDEX);
		mainPanel.add(listPanels.get(currentPage),LIST_PANEL_INDEX);
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public Match getSelectedMatch()
	{	return selectedMatch;
	}
	public String getSelectedMatchFile()
	{	return selectedMatchFolder;
	}

	public void setSelectedMatch(String fileName)
	{	int index = matches.indexOf(matches);
		currentPage = index/(LIST_LINE_COUNT-2);
		refreshList();
		unselectList();
		selectedRow = index%(LIST_LINE_COUNT-2)+1;
		listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
	}
}
