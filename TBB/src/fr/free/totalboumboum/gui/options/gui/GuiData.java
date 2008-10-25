package fr.free.totalboumboum.gui.options.gui;

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

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JLabel;

import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Line;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.data.configuration.misc.MiscConfiguration;
import fr.free.totalboumboum.gui.data.language.Language;
import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;

public class GuiData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final int LINE_LANGUAGE = 0;
	private static final int LINE_FONT = 1;
	private static final int LINE_BACKGROUND = 2;

	private UntitledSubPanelLines optionsPanel;
	private MiscConfiguration miscConfiguration;
	
	private String[] languages;
	private String[] fonts;
	private String[] backgrounds;
	
	public GuiData(SplitMenuPanel container)
	{	super(container);

		// title
		{	setTitleKey(GuiTools.MENU_OPTIONS_ADVANCED_TITLE);
		}
	
		// data
		{	int lines = 20;
			int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new UntitledSubPanelLines(w,h,lines,false);
			int tWidth = (int)(w*0.5);
			
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
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_LANGUAGE_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setLanguage();
						col++;
					}
					// next button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
				}
				
				// #1 FONT
				{	Line ln = optionsPanel.getLine(LINE_FONT);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_FONT_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setFont();
						col++;
					}
					// next button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_FONT_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
				}
				
				// #2 BACKGROUND
				{	Line ln = optionsPanel.getLine(LINE_BACKGROUND);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_BACKGROUND_TITLE,false);
						col++;
					}
					// previous button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setBackground();
						col++;
					}
					// next button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT,true);
						ln.getLabel(col).addMouseListener(this);
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
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_GUI_LINE_LANGUAGE_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_LANGUAGE).setLabelText(2,text,tooltip);
	}
	
	private void setFont()
	{	String text = miscConfiguration.getFontName();
		text = text.toUpperCase().substring(0,1)+text.toLowerCase().substring(1,text.length());
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_GUI_LINE_FONT_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_FONT).setLabelText(2,text,tooltip);
	}
	
	private void setBackground()
	{	String text = miscConfiguration.getBackgroundName();
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_GUI_LINE_BACKGROUND_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_BACKGROUND).setLabelText(2,text,tooltip);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
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
	{	JLabel label = (JLabel)e.getComponent();
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
				int beginIndex = name.length()-FileTools.EXTENSION_FONT.length();
				result = name.substring(beginIndex,name.length()).equalsIgnoreCase(FileTools.EXTENSION_FONT);
				return result;
			}
			
		};
		File[] files = folder.listFiles(filter);
		TreeSet<String> temp = new TreeSet<String>();
		for(int i=0;i<files.length;i++)
		{	String name = files[i].getName();
			temp.add(name.substring(0,name.length()-FileTools.EXTENSION_FONT.length()));
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
				int beginIndex = name.length()-FileTools.EXTENSION_DATA.length();
				result = name.substring(beginIndex,name.length()).equalsIgnoreCase(FileTools.EXTENSION_DATA);
				return result;
			}
			
		};
		File[] files = folder.listFiles(filter);
		TreeSet<String> temp = new TreeSet<String>();
		for(int i=0;i<files.length;i++)
		{	String name = files[i].getName();
			temp.add(name.substring(0,name.length()-FileTools.EXTENSION_DATA.length()));
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
