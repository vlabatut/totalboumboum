package org.totalboumboum.gui.menus.options.gui;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.TreeSet;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.LinesSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.common.structure.subpanel.content.Line;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.data.configuration.misc.MiscConfiguration;
import org.totalboumboum.gui.data.language.Language;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.files.FileNames;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;

	private static final int LINE_COUNT = 20;
	private static final int LINE_LANGUAGE = 0;
	private static final int LINE_FONT = 1;
	private static final int LINE_BACKGROUND = 2;

	private LinesSubPanel optionsPanel;
	private MiscConfiguration miscConfiguration;
	
	private String[] languages;
	private String[] fonts;
	private String[] backgrounds;
	
	public GuiData(SplitMenuPanel container)
	{	super(container);

		// title
		{	setTitleKey(GuiKeys.MENU_OPTIONS_GUI_TITLE);
		}
	
		// data
		{	int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new LinesSubPanel(w,h,Mode.BORDER,LINE_COUNT,1,false);
			int titleWidth = (int)(optionsPanel.getDataWidth()*0.5);
			int iconWidth = optionsPanel.getLineHeight();
			
			initLanguages();
			initFonts();
			initBackgrounds();
			
			// data
			{	miscConfiguration = GuiConfiguration.getMiscConfiguration().copy();;
				
				// #0 LANGUAGE
				{	Line ln = optionsPanel.getLine(LINE_LANGUAGE);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setLanguage();
						col++;
					}
					// next button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #1 FONT
				{	Line ln = optionsPanel.getLine(LINE_FONT);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setFont();
						col++;
					}
					// next button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}
				
				// #2 BACKGROUND
				{	Line ln = optionsPanel.getLine(LINE_BACKGROUND);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMinWidth(col,titleWidth);
						ln.setLabelPrefWidth(col,titleWidth);
						ln.setLabelMaxWidth(col,titleWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	int valueWidth = optionsPanel.getDataWidth() - titleWidth - 3*GuiSizeTools.subPanelMargin - 2*iconWidth;
						ln.setLabelMinWidth(col,valueWidth);
						ln.setLabelPrefWidth(col,valueWidth);
						ln.setLabelMaxWidth(col,valueWidth);
						setBackground();
						col++;
					}
					// next button
					{	ln.setLabelMinWidth(col,iconWidth);
						ln.setLabelPrefWidth(col,iconWidth);
						ln.setLabelMaxWidth(col,iconWidth);
						ln.setLabelKey(col,GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					Color bg = GuiColorTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setBackgroundColor(bg);
				}

				// EMPTY
				{	for(int line=LINE_BACKGROUND+1;line<LINE_COUNT;line++)
					{	Line ln = optionsPanel.getLine(line);
						int col = 0;
						int minWidth = ln.getWidth();
						ln.setLabelMinWidth(col,minWidth);
						ln.setLabelPrefWidth(col,minWidth);
						ln.setLabelMaxWidth(col,minWidth);
						col++;
					}
				}
			}
			
			setDataPart(optionsPanel);
		}
	}
	
	private void setLanguage()
	{	String text = miscConfiguration.getLanguageName();
		text = text.toUpperCase().substring(0,1)+text.toLowerCase().substring(1,text.length());
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_LANGUAGE).setLabelText(2,text,tooltip);
	}
	
	private void setFont()
	{	String text = miscConfiguration.getFontName();
		text = text.toUpperCase().substring(0,1)+text.toLowerCase().substring(1,text.length());
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_FONT).setLabelText(2,text,tooltip);
	}
	
	private void setBackground()
	{	String text = miscConfiguration.getBackgroundName();
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_TITLE+GuiKeys.TOOLTIP); 
		optionsPanel.getLine(LINE_BACKGROUND).setLabelText(2,text,tooltip);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	public MiscConfiguration getMiscConfiguration()
	{	return miscConfiguration;
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
		int[] pos = optionsPanel.getLabelPosition(label);
		int index;
		switch(pos[0])
		{	// LANGUAGE
			case LINE_LANGUAGE:
				String language = miscConfiguration.getLanguageName();
				// previous
				if(pos[1]==1)
				{	index = 0;
					while(languages[index].compareTo(language)<0 && index<languages.length)
						index++;
					if(index>0)
						index --;
				}
				// next
				else //if(pos[1]==3)
				{	index = languages.length-1;
					while(languages[index].compareTo(language)>0 && index>=0)
						index--;
					if(index<languages.length-1)
						index ++;
				}
				// common
				Language l = miscConfiguration.getLanguage();
				miscConfiguration.setLanguage(languages[index],l);
				setLanguage();
				break;
			// FONT
			case LINE_FONT:
				String font = miscConfiguration.getFontName();
				// previous
				if(pos[1]==1)
				{	index = 0;
					while(fonts[index].compareTo(font)<0 && index<fonts.length)
						index++;
					if(index>0)
						index --;
				}
				// next
				else //if(pos[1]==3)
				{	index = fonts.length-1;
					while(fonts[index].compareTo(font)>0 && index>=0)
						index--;
					if(index<fonts.length-1)
						index ++;
				}
				// common
				Font f = miscConfiguration.getFont();
				miscConfiguration.setFont(fonts[index],f);
				setFont();
				break;
			// BACKGROUND
			case LINE_BACKGROUND:
				String background = miscConfiguration.getBackgroundName();
				// previous
				if(pos[1]==1)
				{	index = 0;
					while(backgrounds[index].compareTo(background)<0 && index<backgrounds.length)
						index++;
					if(index>0)
						index --;
				}
				// next
				else //if(pos[1]==3)
				{	index = backgrounds.length-1;
					while(backgrounds[index].compareTo(background)>0 && index>=0)
						index--;
					if(index<backgrounds.length-1)
						index ++;
				}
				// common
				BufferedImage image = miscConfiguration.getBackground();
				miscConfiguration.setBackground(backgrounds[index],image);
				setBackground();
				break;
		}

	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	private void initFonts()
	{	File folder = new File(GuiFileTools.getFontsPath());
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result;
				String name = pathname.getName();
				int beginIndex = name.length()-FileNames.EXTENSION_FONT.length();
				result = name.substring(beginIndex,name.length()).equalsIgnoreCase(FileNames.EXTENSION_FONT);
				return result;
			}
			
		};
		File[] files = folder.listFiles(filter);
		TreeSet<String> temp = new TreeSet<String>();
		for(int i=0;i<files.length;i++)
		{	String name = files[i].getName();
			temp.add(name.substring(0,name.length()-FileNames.EXTENSION_FONT.length()));
		}
		fonts = new String[files.length];
		Iterator<String> it = temp.iterator();
		int i =0;
		while (it.hasNext())
		{	String str = it.next();
			fonts[i] = str;
			i++;
		}
	}

	private void initLanguages()
	{	File folder = new File(GuiFileTools.getLanguagesPath());
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result;
				String name = pathname.getName();
				int beginIndex = name.length()-FileNames.EXTENSION_XML.length();
				result = name.substring(beginIndex,name.length()).equalsIgnoreCase(FileNames.EXTENSION_XML);
				return result;
			}
			
		};
		File[] files = folder.listFiles(filter);
		TreeSet<String> temp = new TreeSet<String>();
		for(int i=0;i<files.length;i++)
		{	String name = files[i].getName();
			temp.add(name.substring(0,name.length()-FileNames.EXTENSION_XML.length()));
		}
		languages = new String[files.length];
		Iterator<String> it = temp.iterator();
		int i =0;
		while (it.hasNext())
		{	String str = it.next();
			languages[i] = str;
			i++;
		}
	}

	private void initBackgrounds()
	{	File folder = new File(GuiFileTools.getBackgroundsPath());
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	boolean result = false;
				String name = pathname.getName();
				String extensions[] = {".jpg", ".jpeg", ".png"};
				int i = 0;
				while(i<extensions.length && !result)
				{	String ext = extensions[i];
					int beginIndex = name.length()-ext.length();
					result = beginIndex>0 && name.substring(beginIndex,name.length()).equalsIgnoreCase(ext);
					i++;
				}
				return result;
			}
			
		};
		File[] files = folder.listFiles(filter);
		TreeSet<String> temp = new TreeSet<String>();
		for(int i=0;i<files.length;i++)
			temp.add(files[i].getName());
		backgrounds = new String[files.length];
		Iterator<String> it = temp.iterator();
		int i =0;
		while (it.hasNext())
		{	String str = it.next();
			backgrounds[i] = str;
			i++;
		}
	}
}
