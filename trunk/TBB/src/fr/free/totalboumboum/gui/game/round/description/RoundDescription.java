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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.itemset.ItemsetPreview;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewLoader;
import fr.free.totalboumboum.engine.container.zone.Zone;
import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.content.subpanel.image.ImageSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.items.InitialItemsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsListener;
import fr.free.totalboumboum.gui.common.content.subpanel.limits.LimitsSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.points.PointsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class RoundDescription extends EntitledDataPanel implements LimitsListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.4f;

	private JPanel downPanel;
	private ImageSubPanel imagePanel;
	private InitialItemsSubPanel initialItemsPanel;
	
	private LimitsSubPanel<RoundLimit> limitsPanel;
	private PointsSubPanel pointsPanel;
	
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
					JPanel itemsetPanel = makeItemsetPanel(leftWidth,innerHeight,preview);
					itemsetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(itemsetPanel);
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
					{	JPanel miscPanel = makeMiscPanel(innerWidth,upHeight,preview);
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

	private SubPanel makeItemsetPanel(int width, int height, LevelPreview levelPreview)
	{	// init
		int lines = 4;
		int colSubs = 2;
		int colGroups = 5;
		if(levelPreview.getItemsetPreview().size()>lines*colGroups)
		{	lines = 5;
			colGroups = 6;
		}

		EntitledSubPanelTable itemsetPanel = new EntitledSubPanelTable(width,height,colGroups,colSubs,lines);
		String titleKey = GuiKeys.COMMON_ITEMS_AVAILABLE_TITLE;
		itemsetPanel.setTitleKey(titleKey,true);
		itemsetPanel.getTable().setColSubMaxWidth(1,Integer.MAX_VALUE);
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		ItemsetPreview itemsetPreview = levelPreview.getItemsetPreview();
		Zone zone = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound().getHollowLevel().getZone();
		HashMap<String,Integer> itemList = zone.getItemCount();
		Iterator<Entry<String,SpritePreview>> i = itemsetPreview.getItemPreviews().entrySet().iterator();
		int line = 0;
		int colGroup = 0;
		while(i.hasNext())
		{	// init
			Entry<String,SpritePreview> temp = i.next();
			String itemName = temp.getKey();
			SpritePreview spritePreview = temp.getValue();
			String name = spritePreview.getName();
			BufferedImage image = spritePreview.getImage(null);
			int number = 0;
			if(itemList.containsKey(itemName))
				number = itemList.get(itemName);
			String tooltip;
			tooltip = name+": "+number;				
			if(number==0)
			{	image = ImageTools.getGreyScale(image);
			}
			String value = Integer.toString(number);
			//
			int colSub = 0;
			{	itemsetPanel.getTable().setLabelIcon(line,colGroup,colSub,image,tooltip);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				itemsetPanel.getTable().setLabelForeground(line,colGroup,colSub,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				itemsetPanel.getTable().setLabelBackground(line,colGroup,colSub,bg);
				colSub++;
			}
			{	String text = value;
				itemsetPanel.getTable().setLabelText(line,colGroup,colSub,text,tooltip);
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				itemsetPanel.getTable().setLabelBackground(line,colGroup,colSub,bg);
				colSub++;
			}
			line++;
			if(line==lines)
			{	line = 0;
				colGroup++;
			}
		}			

		return itemsetPanel;
	}

	private SubPanel makeMiscPanel(int width, int height, LevelPreview preview)
	{	// init
		int lines = 8;
		int colSubs = 2;
		int colGroups = 1;

		EntitledSubPanelTable miscPanel = new EntitledSubPanelTable(width,height,colGroups,colSubs,lines);
		String titleKey = GuiKeys.GAME_ROUND_DESCRIPTION_MISC_TITLE;
		miscPanel.setTitleKey(titleKey,true);
		miscPanel.getTable().setColSubMaxWidth(1,Integer.MAX_VALUE);
		
		// data
		String keys[] = 
		{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK,
			GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE,
			GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE,
			GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR,
			GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE,
			GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME,
			GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION
		};
		Round round = Configuration.getGameConfiguration().getTournament().getCurrentMatch().getCurrentRound();
		HollowLevel hollowLevel = round.getHollowLevel();
		String texts[] = 
		{	hollowLevel.getPackName(),
			preview.getTitle(),
			preview.getSource(),
			preview.getAuthor(),
			hollowLevel.getInstanceName(),
			hollowLevel.getThemeName(),
			Integer.toString(hollowLevel.getVisibleHeight())+new Character('\u00D7').toString()+Integer.toString(hollowLevel.getVisibleWidth())
		};
		int line = 0;
		int colGroup = 0;
		for(int i=0;i<keys.length;i++)
		{	int colSub = 0;
			{	miscPanel.getTable().setLabelKey(line,colGroup,colSub,keys[i],true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				miscPanel.getTable().setLabelForeground(line,colGroup,colSub,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				miscPanel.getTable().setLabelBackground(line,colGroup,colSub,bg);
				colSub++;
			}
			{	String text = texts[i];
				String tooltip = texts[i];
				miscPanel.getTable().setLabelText(line,colGroup,colSub,text,tooltip);
				Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
				miscPanel.getTable().setLabelBackground(line,colGroup,colSub,bg);
				colSub++;
			}
			line++;
			if(line==lines)
			{	line = 0;
				colGroup++;
			}
		}		
		
		return miscPanel;
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
