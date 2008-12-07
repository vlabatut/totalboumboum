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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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

import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.image.ImageSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanelListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.round.RoundSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class SelectedRoundData extends EntitledDataPanel implements MouseListener, LimitsSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int LIST_PANEL_INDEX = 0;
	
	private ArrayList<UntitledSubPanelTable> listPanels;
	private int currentPage = 0;
	private int selectedRow = -1;
	
	private SubPanel mainPanel;
	private SubPanel rightPanel;
	private ImageSubPanel imagePanel;
	private LimitsSubPanel<RoundLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	private RoundSubPanel miscPanel;

	private ArrayList<String> rounds;
	private Round selectedRound = null;
	private String selectedRoundFolder = null;
	private int leftWidth;
	private int rightWidth;
	private int rightHeight;
	private int rightUpWidth;
	private int leftUpWidth;
	
	private String baseFolder;
	
	public SelectedRoundData(SplitMenuPanel container, String baseFolder)
	{	super(container);
		this.baseFolder = baseFolder;

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_ROUND_SELECT_TITLE);
	
		// data
		{	mainPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(mainPanel,BoxLayout.LINE_AXIS); 
				mainPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			rightWidth = dataWidth - leftWidth - margin; 
			mainPanel.setOpaque(false);
			initRounds();
			
			// list panel
			{	makeListPanels(leftWidth,dataHeight);
				mainPanel.add(listPanels.get(currentPage));
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
		
	private void initRounds()
	{	rounds = new ArrayList<String>();
		File roundsMainFile = new File(baseFolder);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = pathname.exists() && pathname.isDirectory();
				return result;
			}
		};
		List<File> roundsFolders = Arrays.asList(roundsMainFile.listFiles(filter));
		Collections.sort(roundsFolders,new Comparator<File>()
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
		for(File f:roundsFolders)
		{	List<File> files = Arrays.asList(f.listFiles());
			String fileName = FileTools.FILE_ROUND+FileTools.EXTENSION_DATA;
			Iterator<File> it = files.iterator();
			boolean found = false;
			while(it.hasNext() && !found)
			{	File file = it.next();
				if(file.getName().equalsIgnoreCase(fileName))
					found = true;
			}
			if(found)
				rounds.add(f.getName());
		}
	}
	
	private void makeListPanels(int width, int height)
	{	int lines = LIST_LINE_COUNT;
		int cols = 1;
		listPanels = new ArrayList<UntitledSubPanelTable>();
		
		for(int panelIndex=0;panelIndex<getPageCount();panelIndex++)
		{	UntitledSubPanelTable listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
			listPanel.setColSubMaxWidth(0,Integer.MAX_VALUE);
		
			// data
			int line = 1;
			int roundIndex = panelIndex*(LIST_LINE_COUNT-2);
			while(line<LIST_LINE_NEXT && roundIndex<rounds.size())
			{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				String name = rounds.get(roundIndex);
				listPanel.setLabelBackground(line,0,bg);
				listPanel.setLabelText(line,0,name,name);
				JLabel label = listPanel.getLabel(line,0);
				label.addMouseListener(this);
				roundIndex++;
				line++;
			}			
			// page up
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
				String key = GuiKeys.MENU_RESOURCES_ROUND_SELECT_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiKeys.MENU_RESOURCES_ROUND_SELECT_PAGEDOWN;
				listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
				label.addMouseListener(this);
			}
			listPanels.add(listPanel);
		}
	}
	
	private void refreshPreview()
	{	if(selectedRow<0)
		{	selectedRound = null;
			selectedRoundFolder = null;
		}
		// one round selected
		else
		{	try
			{	selectedRoundFolder = rounds.get((selectedRow-1)+currentPage*(LIST_LINE_COUNT-2));
				String folderPath = baseFolder+File.separator+selectedRoundFolder;
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
		miscPanel.setRound(selectedRound);
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
	{	initRounds();
		makeListPanels(leftWidth,dataHeight);
		refreshList();
		if(selectedRow!=-1)
			listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
		refreshImage();
		refreshLimits();
//		rightPanel.validate();
//		rightPanel.repaint();
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
		{	switch(pos[0])
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
				// round selected
				default:
					unselectList();
					selectedRow = pos[0];
					listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
					refreshPreview();
					refreshImage();
					refreshLimits();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private int getPageCount()
	{	int result = rounds.size()/(LIST_LINE_COUNT-2);
		if(rounds.size()%(LIST_LINE_COUNT-2)>0)
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
	
	public Round getSelectedRound()
	{	return selectedRound;
	}
	public String getSelectedRoundFile()
	{	return selectedRoundFolder;
	}

	public void setSelectedRound(String fileName)
	{	int index = rounds.indexOf(rounds);
		currentPage = index/(LIST_LINE_COUNT-2);
		refreshList();
		unselectList();
		selectedRow = index%(LIST_LINE_COUNT-2)+1;
		listPanels.get(currentPage).setLabelBackground(selectedRow,0,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		refreshPreview();
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
