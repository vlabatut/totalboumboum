package fr.free.totalboumboum.gui.menus.explore.heroes.select;

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
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.engine.content.sprite.SpritePreviewLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.PackBrowserSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.browser.PackBrowserSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.Column;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelColumns;
import fr.free.totalboumboum.gui.common.structure.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class SelectedHeroData extends EntitledDataPanel implements MouseListener, PackBrowserSubPanelListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int VIEW_LINE_NAME = 0;
	private static final int VIEW_LINE_PACK = 1;
	private static final int VIEW_LINE_AUTHOR = 2;
	private static final int VIEW_LINE_SOURCE = 3;

	private SubPanel mainPanel;
	private SubPanel previewPanel;
	private UntitledSubPanelTable infosPanel;
	private UntitledSubPanelColumns imagePanel;
	private PackBrowserSubPanel packPanel;
	private int listWidth;
	private int listHeight;
	private PredefinedColor selectedColor;
	private SpritePreview selectedSprite;
		
	public SelectedHeroData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_RESOURCES_HERO_SELECT_TITLE);
	
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
			{	listWidth = leftWidth;
				listHeight = dataHeight;
				packPanel = new PackBrowserSubPanel(listWidth,listHeight);
				String baseFolder = FileTools.getHeroesPath();
				String targetFile = FileTools.FILE_SPRITE+FileTools.EXTENSION_DATA;
				packPanel.setFolder(baseFolder,targetFile);
				packPanel.addListener(this);
				mainPanel.add(packPanel);
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
				
				makeInfosPanel(rightWidth,upHeight);
				previewPanel.add(infosPanel);

				previewPanel.add(Box.createVerticalGlue());

				makeImagePanel(rightWidth,downHeight);
				previewPanel.add(imagePanel);
				
				mainPanel.add(previewPanel);
			}
			
			setDataPart(mainPanel);
		}
	}

	private void makeInfosPanel(int width, int height)
	{	int lines = 10;
		int colSubs = 2;
		int colGroups = 1;
		infosPanel = new UntitledSubPanelTable(width,height,colGroups,colSubs,lines,true);
		
		// data
		String keys[] = 
		{	GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_NAME,
			GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_PACKAGE,
			GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_AUTHOR,
			GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_SOURCE
		};
		for(int line=0;line<keys.length;line++)
		{	int colSub = 0;
			{	infosPanel.setLabelKey(line,colSub,keys[line],true);
				Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
				infosPanel.setLabelForeground(line,0,fg);
				Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
				infosPanel.setLabelBackground(line,colSub,bg);
				colSub++;
			}
			{	String text = null;
				String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys[line]+GuiKeys.TOOLTIP);
				infosPanel.setLabelText(line,colSub,text,tooltip);
				if(line>0)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					infosPanel.setLabelBackground(line,colSub,bg);
				}
				colSub++;
			}
		}
		int maxWidth = width-3*GuiTools.subPanelMargin-infosPanel.getHeaderHeight();
		infosPanel.setColSubMaxWidth(1,maxWidth);
		infosPanel.setColSubPreferredWidth(1,maxWidth);
	}
	
	private void makeImagePanel(int width, int height)
	{	int cols = 3;
		imagePanel = new UntitledSubPanelColumns(width,height,cols);
		PredefinedColor[] colors = PredefinedColor.values();
		int lines = colors.length/2;
		int margin = GuiTools.subPanelMargin;
		if(colors.length%2 > 0)
			lines++;
		int lineHeight = (height - (lines-1)*margin)/lines;
		int rightWidth = width - 2*lineHeight - 5*margin;

		int col = 0;

		// colors 0
		{	Column cl = imagePanel.getColumn(col);
			cl.setWidth(lineHeight);
			for(int i=1;i<lines;i++)
				cl.addLabel(0);
			for(int line=0;line<lines;line++)
			{	cl.setLabelMinHeight(line,lineHeight);
				cl.setLabelPreferredHeight(line,lineHeight);
				cl.setLabelMaxHeight(line,lineHeight);
				JLabel label = cl.getLabel(line);
				label.addMouseListener(this);
			}
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		// colors 1
		{	Column cl = imagePanel.getColumn(col);
			cl.setWidth(lineHeight);
			for(int i=1;i<lines;i++)
				cl.addLabel(0);
			for(int line=0;line<lines;line++)
			{	cl.setLabelMinHeight(line,lineHeight);
				cl.setLabelPreferredHeight(line,lineHeight);
				cl.setLabelMaxHeight(line,lineHeight);
				JLabel label = cl.getLabel(line);
				label.addMouseListener(this);
			}
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
		
		// image
		{	Column cl = imagePanel.getColumn(col);
			cl.setWidth(rightWidth);
			int line = 0;
			cl.setLabelMinHeight(line,height);
			cl.setLabelPreferredHeight(line,height);
			cl.setLabelMaxHeight(line,height);
			JLabel label = cl.getLabel(line);
			label.addMouseListener(this);
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			cl.setBackgroundColor(bg);
			col++;
		}
	}
	
	private void refreshPreview()
	{	String infosValues[] = new String[10];
		BufferedImage image = null;
		PredefinedColor colorValues[] = PredefinedColor.values();
		boolean colors[] = new boolean[PredefinedColor.values().length];
		// no player selected
		if(selectedSprite==null)
		{	// notes
			image = null;
			for(int i=0;i<colors.length;i++)
				colors[i] = false;
			// infos
			for(int i=0;i<infosValues.length;i++)
				infosValues[i] = null;
		}
		// one player selected
		else
		{	// image
			image = selectedSprite.getImage(selectedColor);
			for(int i=0;i<colors.length;i++)
				if(selectedSprite.hasColor(colorValues[i]))
					colors[i] = true;
			// infos
			infosValues[VIEW_LINE_NAME] = selectedSprite.getName();
			infosValues[VIEW_LINE_PACK]= selectedSprite.getPack();
			infosValues[VIEW_LINE_AUTHOR] = selectedSprite.getAuthor();
			infosValues[VIEW_LINE_SOURCE] = selectedSprite.getSource();
		}
		// infos
		for(int line=0;line<infosValues.length;line++)
		{	int colSub = 1;
			String text = infosValues[line];
			String tooltip = text;
			infosPanel.setLabelText(line,colSub,text,tooltip);
		}
		// image
		int line = 0;
		int col = 0;
		int cols = 2;
//		int lines = imagePanel.getColumn(0).getLineCount();
		for(int i=0;i<colors.length;i++)
		{	// colors
			String text = null;
			String tooltip = null;
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			if(colors[i])
			{	String colorKey = colorValues[i].toString();
				colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
				colorKey = GuiKeys.COMMON_COLOR+colorKey;
				text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
				tooltip = text;
				bg = colorValues[i].getColor();
			}
			imagePanel.setLabelBackground(line,col,bg);
			imagePanel.setLabelText(line,col,text,tooltip);
			// index
			col++;
			if(col==cols)
			{	col=0;
				line++;
			}
		}
		JLabel label = imagePanel.getLabel(0,2);
		Dimension prefDim = label.getPreferredSize();
		int imgWidth = (int)(prefDim.width*0.9);
		int imgHeight = (int)(prefDim.height*0.9);
		if(image!=null)
		{	float zoomX = imgWidth/(float)image.getWidth();
			float zoomY = imgHeight/(float)image.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			image = ImageTools.resize(image,zoom,true);
			ImageIcon icon = new ImageIcon(image);
			label.setIcon(icon);
			label.setText(null);
			Color bg;
			if(selectedColor==null)
				bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			else
			{	Color clr = selectedColor.getColor();
				int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1;
				bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
				
			}
			label.setBackground(bg);
		}
		else
		{	label.setIcon(null);
			label.setText(null);
			Color bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
			label.setBackground(bg);
		}
		
	}

	public SpritePreview getSelectedHeroPreview()
	{	return selectedSprite;
		
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
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
		// colors
		int[] pos = imagePanel.getLabelPosition(label);
		selectedColor = null;
		if(pos[1]==0 || pos[1]==1)
		{	PredefinedColor colors[] = PredefinedColor.values();
			int index = pos[0]*2+pos[1];
			selectedColor = colors[index];
			
		}
		refreshPreview();
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// PACK BROWSER LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void packBrowserSelectionChange()
	{	String pack = packPanel.getSelectedPack();
		String folder = packPanel.getSelectedName();
		if(pack==null || folder==null)
			selectedSprite = null;
		else
		{	try
			{	selectedSprite = SpritePreviewLoader.loadHeroPreview(pack,folder);
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
		refreshPreview();
	}
}
