package fr.free.totalboumboum.gui.profiles;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.engine.EngineConfiguration;
import fr.free.totalboumboum.configuration.profile.Portraits;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.gui.common.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.subpanel.Line;
import fr.free.totalboumboum.gui.common.subpanel.SubPanel;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelLines;
import fr.free.totalboumboum.gui.common.subpanel.UntitledSubPanelTable;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class ProfilesData extends EntitledDataPanel implements MouseListener
{	
	private static final long serialVersionUID = 1L;
	private static final float SPLIT_RATIO = 0.5f;
	
	private static final int LIST_LINE_COUNT = 20;
	private static final int LIST_LINE_PREVIOUS = 0;
	private static final int LIST_LINE_NEXT = LIST_LINE_COUNT-1;
	private static final int VIEW_LINE_HERO = 0;
	private static final int VIEW_LINE_NAME = 1;
	private static final int VIEW_LINE_AI = 2;
	private static final int VIEW_LINE_COLOR01 = 3;
	private static final int VIEW_LINE_COLOR02 = 4;
	private static final int VIEW_LINE_COLOR03 = 5;
	private static final int VIEW_LINE_COLOR04 = 6;
	private static final int VIEW_LINE_COLOR05 = 7;
	private static final int VIEW_LINE_COLOR06 = 8;
	private static final int VIEW_LINE_COLOR07 = 9;
	private static final int VIEW_LINE_COLOR08 = 10;
	private static final int VIEW_LINE_COLOR09 = 11;
	private static final int VIEW_LINE_COLOR10 = 12;
	private static final int VIEW_LINE_COLOR11 = 13;
	private static final int VIEW_LINE_COLOR12 = 14;
	private static final int VIEW_LINE_COLOR13 = 15;
	private static final int VIEW_LINE_COLOR14 = 16;
	private static final int VIEW_LINE_COLOR15 = 17;
	private static final int VIEW_LINE_COLOR16 = 18;

	private int currentPage = 0;
	
	private ProfilesConfiguration profilesConfiguration;
	private UntitledSubPanelTable listPanel;
	private SubPanel previewPanel;
	private ArrayList<Entry<String,String>> profiles;
	
	public ProfilesData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiTools.MENU_PROFILES_LIST_TITLE);
	
		// data
		{	SubPanel infoPanel = new SubPanel(dataWidth,dataHeight);
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			
			int margin = GuiTools.panelMargin;
			int leftWidth = (int)(dataWidth*SPLIT_RATIO); 
			int rightWidth = dataWidth - leftWidth - margin; 
			infoPanel.setOpaque(false);
			profilesConfiguration = Configuration.getProfilesConfiguration().copy();
			initProfiles();
			
			// list panel
			{	makeListPanel(leftWidth,dataHeight);
				infoPanel.add(listPanel);
			}
			
			infoPanel.add(Box.createHorizontalGlue());
			
			// preview panel
			{	makePreviewPanel(rightWidth,dataHeight);
				infoPanel.add(previewPanel);
			}
			
			setDataPart(infoPanel);
			
		}
	}
		
	private void initProfiles()
	{	profiles = new ArrayList<Entry<String,String>>(profilesConfiguration.getProfiles().entrySet());
		Collections.sort(profiles,new Comparator<Entry<String,String>>()
		{	@Override
			public int compare(Entry<String,String> arg0, Entry<String,String> arg1)
			{	int result;
				String name0 = arg0.getValue();
				String name1 = arg1.getValue();
				result = name0.compareTo(name1);
				return result;
			}
		});
	}
	
	public void makeListPanel(int width, int height)
	{	int lines = LIST_LINE_COUNT;
		int cols = 1;
		listPanel = new UntitledSubPanelTable(width,height,cols,lines,false);
		listPanel.setSubColumnsMaxWidth(0,Integer.MAX_VALUE);
		
		// data
		for(int line=1;line<LIST_LINE_COUNT-1;line++)
		{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
			Entry<String,String> profile = profiles.get((line-1)+currentPage*(LIST_LINE_COUNT-2));
			String name = profile.getValue();
			listPanel.setLabelBackground(line,0,bg);
			listPanel.setLabelText(line,0,name,name);
			JLabel label = listPanel.getLabel(line,0);
			label.addMouseListener(this);
		}
		
		// page up
		{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
			listPanel.setLabelBackground(LIST_LINE_PREVIOUS,0,bg);
			String key = GuiTools.MENU_PROFILES_LIST_PAGEUP;
			listPanel.setLabelKey(LIST_LINE_PREVIOUS,0,key,true);
			JLabel label = listPanel.getLabel(LIST_LINE_PREVIOUS,0);
			label.addMouseListener(this);
		}
		{	Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
			listPanel.setLabelBackground(LIST_LINE_NEXT,0,bg);
			String key = GuiTools.MENU_PROFILES_LIST_PAGEDOWN;
			listPanel.setLabelKey(LIST_LINE_NEXT,0,key,true);
			JLabel label = listPanel.getLabel(LIST_LINE_NEXT,0);
			label.addMouseListener(this);
		}
	}
	
	public void makePreviewPanel(int width, int height)
	{	int lines = LIST_LINE_COUNT;
		previewPanel = new UntitledSubPanelLines(width,height,lines,false);
		
		// data
		
	}
	
/*			
			
			
			int lines = 20;
			int w = getDataWidth();
			int h = getDataHeight();
			optionsPanel = new UntitledSubPanelLines(w,h,lines,false);
			int tWidth = (int)(w*0.66);
			
			// data
			{	engineConfiguration = Configuration.getEngineConfiguration().copy();;
				
				// #0 FPS
				{	Line ln = optionsPanel.getLine(LINE_FPS);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setFps();
						col++;
					}
					// plus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
				}
				
				// #1 AUTO ADJUST
				{	Line ln = optionsPanel.getLine(LINE_ADJUST);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_ADJUST_TITLE,false);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setAdjust();
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
				}
				
				// #2 GAME SPEED
				{	Line ln = optionsPanel.getLine(LINE_SPEED);
					ln.addLabel(0);
					ln.addLabel(0);
					ln.addLabel(0);
					int col = 0;
					// name
					{	ln.setLabelMaxWidth(col,tWidth);
						ln.setLabelPreferredWidth(col,tWidth);
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE,false);
						col++;
					}
					// minus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
					// value
					{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
						setGameSpeed();
						col++;
					}
					// plus button
					{	ln.setLabelMaxWidth(col,ln.getHeight());
						ln.setLabelKey(col,GuiTools.MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS,true);
						ln.getLabel(col).addMouseListener(this);
						col++;
					}
				}
			}
			
			setDataPart(optionsPanel);
		}
	}
	
	private void setAdjust()
	{	boolean adjust = engineConfiguration.getAutoFps();
		String key;
		if(adjust)
			key = GuiTools.MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED;
		else
			key = GuiTools.MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED;
		optionsPanel.getLine(LINE_ADJUST).setLabelKey(1,key,true);
	}
	
	private void setFps()
	{	int fps = engineConfiguration.getFps();
		String text = Integer.toString(fps);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_FPS).setLabelText(2,text,tooltip);
	}
	
	private void setGameSpeed()
	{	double speed = engineConfiguration.getSpeedCoeff();
		String text = null;
		int i = 0;
		while(i<speedValues.length && text==null)
		{	if(speedValues[i]==speed)
				text = speedTexts[i];
			else
				i++;
		}
		if(text==null)
		{	NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);
			text = nf.format(speed);
		}
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiTools.MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE+GuiTools.TOOLTIP); 
		optionsPanel.getLine(LINE_SPEED).setLabelText(2,text,tooltip);
	}
*/	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}

	public ProfilesConfiguration getProfilesConfiguration()
	{	return profilesConfiguration;
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
	{	
/*		
		JLabel label = (JLabel)e.getComponent();
		int[] pos = optionsPanel.getLabelPosition(label);
		switch(pos[0])
		{	// panel dimension
			case LINE_FPS:
				int fps = engineConfiguration.getFps();
				// minus
				if(pos[1]==1)
				{	if(fps>=35)
						fps = fps-5;
				}
				// plus
				else //if(pos[1]==3)
				{	if(fps<=95)
						fps = fps + 5;
				}
				// common
				fps = 5*(fps/5);
				engineConfiguration.setFps(fps);
				setFps();
				break;
			// border
			case LINE_ADJUST:
				boolean adjust = !engineConfiguration.getAutoFps();
				engineConfiguration.setAutoFps(adjust);
				setAdjust();
				break;
			// smooth graphics
			case LINE_SPEED:
				double speed = engineConfiguration.getSpeedCoeff();
				int index;
				// minus
				if(pos[1]==1)
				{	index = 0;
					while(speedValues[index]<speed && index<speedValues.length)
						index++;
					if(index>0)
						index --;
				}
				// plus
				else //if(pos[1]==3)
				{	index = speedValues.length-1;
					while(speedValues[index]>speed && index>=0)
						index--;
					if(index<speedValues.length-1)
						index ++;
				}
				// common
				engineConfiguration.setSpeedCoeff(speedValues[index]);
				setGameSpeed();
		}
*/
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
