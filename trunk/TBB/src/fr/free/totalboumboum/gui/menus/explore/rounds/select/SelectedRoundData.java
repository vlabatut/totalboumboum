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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class SelectedRoundData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;
	private static final int LIMITS_PANEL_INDEX = 3;
	private static final int POINTS_PANEL_INDEX = 5;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;

	private static final int VIEW_LINE_COUNT = 5;
	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_AUTHOR = 1;
	private static final int VIEW_LINE_PLAYERS = 2;
	private static final int VIEW_LINE_LEVEL_NAME = 3;
	private static final int VIEW_LINE_LEVEL_PACK = 4;

	private static final int LIST_PANEL_INDEX = 0;
	
	private ArrayList<UntitledSubPanelTable> listPanels;
	private int currentPage = 0;
	private int selectedRow = -1;
	
	private SubPanel mainPanel;
	private SubPanel rightPanel;
	private SubPanel imagePanel;
	private JLabel imageLabel;
	private EntitledSubPanel limitsPanel;
	private EntitledSubPanel pointsPanel;
	private UntitledSubPanelTable previewPanel;

	private ArrayList<String> rounds;
	private Round selectedRound = null;
	private String selectedRoundFolder = null;
	private int leftWidth;
	private int rightWidth;
	private int rightHeight;
	private int rightUpWidth;
	private int leftUpWidth;
	private int imageLabelHeight;
	private int imageLabelWidth;
	
	private String baseFolder;
	private int selectedLimitRow = -1;
	
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
					{	makePreviewPanel(leftUpWidth,previewHeight);
						upPanel.add(previewPanel);
					}
					
					upPanel.add(Box.createHorizontalGlue());

					// level preview
					{	makeImagePanel(rightUpWidth,previewHeight);
						upPanel.add(imagePanel);
					}
					
					rightPanel.add(upPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	limitsPanel = makeLimitsPanel(rightWidth,rightHeight);
					rightPanel.add(limitsPanel);
				}

				rightPanel.add(Box.createRigidArea(new Dimension(margin,margin)));

				{	pointsPanel = makePointsPanel(rightWidth,rightHeight);
					rightPanel.add(pointsPanel);
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
			listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
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
				String key = GuiKeys.MENU_RESOURCES_ROUND_SELECT_LIST_PAGEUP;
				listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
				JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
				label.addMouseListener(this);
			}
			// page down
			{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
				String key = GuiKeys.MENU_RESOURCES_ROUND_SELECT_LIST_PAGEDOWN;
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
		{	GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_NAME,
			GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_AUTHOR,
			GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_PLAYERS,
			GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_LEVEL_FOLDER,
			GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_LEVEL_PACK
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
		// no round selected
		if(selectedRow<0)
		{	for(int i=0;i<values.length;i++)
				values[i] = null;	
			selectedRound = null;
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
			values[VIEW_LINE_NAME] = selectedRoundFolder;
			values[VIEW_LINE_AUTHOR]= selectedRound.getAuthor();
			values[VIEW_LINE_PLAYERS] = StringTools.formatAllowedPlayerNumbers(selectedRound.getAllowedPlayerNumbers());
			values[VIEW_LINE_LEVEL_NAME] = selectedRound.getHollowLevel().getFolderName();
			values[VIEW_LINE_LEVEL_PACK] = selectedRound.getHollowLevel().getPackName();
		}
		// common
		for(int line=0;line<values.length;line++)
		{	int colSub = 1;
			String text = values[line];
			String tooltip = text;
			previewPanel.setLabelText(line,colSub,text,tooltip);
		}
	}

	private EntitledSubPanel makePointsPanel(int width, int height)
	{	EntitledSubPanel result = new EntitledSubPanel(width,height);
		String key = GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_POINTS;
		result.setTitleKey(key,true);
		return result;
	}
	
	private EntitledSubPanel makeLimitsPanel(int width, int height)
	{	EntitledSubPanel result = new EntitledSubPanel(width,height);
		String key = GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_LIMIT;
		result.setTitleKey(key,true);
		return result;
	}

	private void makeImagePanel(int width, int height)
	{	imagePanel = new SubPanel(width,height);
		int margin = GuiTools.subPanelMargin;
		imagePanel.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		BoxLayout layout = new BoxLayout(imagePanel,BoxLayout.PAGE_AXIS);
		imagePanel.setLayout(layout);

		imageLabel = new JLabel();
		imagePanel.add(Box.createVerticalGlue());
		imagePanel.add(imageLabel);
		imagePanel.add(Box.createVerticalGlue());
		imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

		imageLabelHeight = height - 2*margin;
		imageLabelWidth = width - 2*margin;
		Dimension dim = new Dimension(imageLabelWidth,imageLabelHeight);
		imageLabel.setMinimumSize(dim);
		imageLabel.setPreferredSize(dim);
		imageLabel.setMaximumSize(dim);
		
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		imageLabel.setVerticalAlignment(SwingConstants.CENTER);
		
		String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE+GuiKeys.TOOLTIP);
		imageLabel.setText(null);
		imageLabel.setToolTipText(tooltip);

		int fontSize = GuiTools.getFontSize(imageLabelWidth, imageLabelHeight, text);
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		imageLabel.setFont(font);
		imageLabel.setBackground(GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND);
		imageLabel.setForeground(GuiTools.COLOR_TABLE_REGULAR_FOREGROUND);
		imageLabel.setOpaque(true);
	}
	
	private void refreshLimits()
	{	if(selectedRound==null)
			limitsPanel = makeLimitsPanel(rightWidth,rightHeight);
		else
			limitsPanel = RoundDescription.makeLimitsPanel(this,rightWidth,rightHeight,selectedRound.getLimits(),GuiKeys.ROUND);
		rightPanel.remove(LIMITS_PANEL_INDEX);
		rightPanel.add(limitsPanel,LIMITS_PANEL_INDEX);
	}
	
	private void refreshPoints()
	{	if(selectedRound==null)
		{	pointsPanel = makePointsPanel(rightWidth,rightHeight);
			rightPanel.remove(POINTS_PANEL_INDEX);
			rightPanel.add(pointsPanel,POINTS_PANEL_INDEX);
		}
		else
		{	
//			pointsPanel = RoundDescription.makePointsPanel(rightWidth,rightHeight,selectedRound.getPointProcessor(),GuiKeys.ROUND);
			selectedLimitRow = 0;
			selectLimit(selectedLimitRow);
		}
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
		if(image!=null)
		{	float zoomX = imageLabelWidth/(float)image.getWidth();
			float zoomY = imageLabelHeight/(float)image.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			image = ImageTools.resize(image,zoom,true);
			ImageIcon icon = new ImageIcon(image);
			imageLabel.setIcon(icon);
			imageLabel.setText(null);
			Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			imageLabel.setBackground(bg);
		}
		else
		{	String text = null;//GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_LEVEL_SELECT_PREVIEW_IMAGE);
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE+GuiKeys.TOOLTIP);
			imageLabel.setText(text);
			imageLabel.setToolTipText(tooltip);
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			imageLabel.setBackground(bg);
		}
		
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
		// limits panel
		if(pos[0]==-1)
		{	pos = ((EntitledSubPanelTable)limitsPanel).getTable().getLabelPositionSimple(label);
			// unselect
			if(selectedLimitRow!=-1)
			{	((EntitledSubPanelTable)limitsPanel).getTable().setLabelBackground(selectedLimitRow,0,GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
				((EntitledSubPanelTable)limitsPanel).getTable().setLabelBackground(selectedLimitRow,1,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
				selectedLimitRow = -1;
			}		
			// select
			selectLimit(pos[0]);
		}
		// round list
		else
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
					refreshPoints();
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

	private void selectLimit(int row)
	{	// paint line
		selectedLimitRow = row;
		((EntitledSubPanelTable)limitsPanel).getTable().setLabelBackground(selectedLimitRow,0,GuiTools.COLOR_TABLE_SELECTED_DARK_BACKGROUND);
		((EntitledSubPanelTable)limitsPanel).getTable().setLabelBackground(selectedLimitRow,1,GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
		// update points panel
		Limit limit = selectedRound.getLimits().getLimit(row);
		PointsProcessor pp = limit.getPointProcessor();
		pointsPanel = RoundDescription.makePointsPanel(rightWidth,rightHeight,pp,GuiKeys.ROUND);
		rightPanel.remove(POINTS_PANEL_INDEX);
		rightPanel.add(pointsPanel,POINTS_PANEL_INDEX);
		validate();
		repaint();
	}
	
}
