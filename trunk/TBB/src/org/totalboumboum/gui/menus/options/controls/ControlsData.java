package org.totalboumboum.gui.menus.options.controls;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel.Mode;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.classes.ClassTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ControlsData extends EntitledDataPanel implements MouseListener,KeyListener
{	
	private static final long serialVersionUID = 1L;

	private static final int COL_COMMAND = 0;
	private static final int COL_KEY = 1;
	private static final int COL_AUTO = 2;
	
	private TableSubPanel keysPanel;
	private ControlSettings controlSettings;
	private String actions[] = 
	{	ControlEvent.UP,
		ControlEvent.RIGHT,
		ControlEvent.DOWN,
		ControlEvent.LEFT,
		ControlEvent.DROPBOMB,
		ControlEvent.PUNCHBOMB,
		ControlEvent.TRIGGERBOMB
	};
	private int selectedRow = -1;
	
	public ControlsData(SplitMenuPanel container, int index)
	{	super(container);

		// title
		{	String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_CONTROLS_TITLE)+" "+index;
			String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(GuiKeys.MENU_OPTIONS_CONTROLS_TITLE+GuiKeys.TOOLTIP);
			setTitleText(text,tooltip);
		}
	
		// data
		{	
			String head[] = 
			{	GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_COMMAND,
				GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_KEY,
				GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_AUTO
		};
			int lines = 20;
			int cols = head.length;
			int w = getDataWidth();
			int h = getDataHeight();
			keysPanel = new TableSubPanel(w,h,Mode.BORDER,lines,cols,true);

			// headers
			{	List<String> keys = new ArrayList<String>();
				List<Boolean> imageFlags = new ArrayList<Boolean>();
				for(int i=0;i<head.length;i++)
				{	keys.add(head[i]);
					imageFlags.add(true);
				}
				keysPanel.setLineKeysSimple(0,keys,imageFlags);
			}			
			
			// data
			{	controlSettings = Configuration.getControlsConfiguration().getControlSettings().get(index).copy();;
				for(int line=1;line<actions.length+1;line++)
				{	Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					keysPanel.setLineBackground(line, bg);

					// command
					{	String name = actions[line-1].toUpperCase().substring(0,1);
						name = name + actions[line-1].toLowerCase().substring(1,actions[line-1].length());
						String key = "MenuOptionsControlsLineCommand"+name;
						keysPanel.setLabelKey(line,COL_COMMAND,key,false);
					}
					// key
					{	int key = controlSettings.getOnKeyFromEvent(actions[line-1]);
						setKey(line,COL_KEY,key);
						MyLabel label = keysPanel.getLabel(line,COL_KEY);
						label.addMouseListener(this);
						label.setMouseSensitive(true);
					}
					// autofire
					{	boolean auto = controlSettings.isAutofire(actions[line-1]);
						setAuto(line,COL_AUTO,auto);
						MyLabel label = keysPanel.getLabel(line,COL_AUTO);
						label.addMouseListener(this);
						label.setMouseSensitive(true);
					}
				}
			}
			int autofireWidth = keysPanel.getHeaderHeight();
			int commandWidth = (keysPanel.getDataWidth() - 2*GuiTools.subPanelMargin - autofireWidth)/2;
			int keyWidth = keysPanel.getDataWidth() - 2*GuiTools.subPanelMargin - autofireWidth - commandWidth;
			keysPanel.setColSubMinWidth(0,commandWidth);
			keysPanel.setColSubPrefWidth(0,commandWidth);
			keysPanel.setColSubMaxWidth(0,commandWidth);
			keysPanel.setColSubMinWidth(1,keyWidth);
			keysPanel.setColSubPrefWidth(1,keyWidth);
			keysPanel.setColSubMaxWidth(1,keyWidth);
			keysPanel.setColSubMinWidth(2,autofireWidth);
			keysPanel.setColSubPrefWidth(2,autofireWidth);
			keysPanel.setColSubMaxWidth(2,autofireWidth);
			
			setDataPart(keysPanel);
			keysPanel.addKeyListener(this);
		}
	}
	
	private void setAuto(int line, int col, boolean auto)
	{	String key;
		if(auto)
			key = GuiKeys.MENU_OPTIONS_CONTROLS_LINE_AUTO_TRUE;
		else
			key = GuiKeys.MENU_OPTIONS_CONTROLS_LINE_AUTO_FALSE;
		keysPanel.setLabelKey(line,col,key,true);
	}

	public void deselect()
	{	if(selectedRow!=-1)
		{	keysPanel.setLabelBackground(selectedRow,1,GuiTools.COLOR_TABLE_REGULAR_BACKGROUND);
			selectedRow = -1;		
		}		
	}
	
	private void setKey(int line, int col, int key)
	{	String text = null;
		if(key>=0)
		{	try
			{	Field field = ClassTools.getFieldFromValue(key,KeyEvent.class);
				text = field.getName();
				text = text.substring(3,text.length());
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
		}
		keysPanel.setLabelText(line,col,text,text);
		keysPanel.setFocusable(true);
	}
	
	@Override
	public void refresh()
	{	// nothing to do here
	}

	public ControlSettings getControlSettings()
	{	return controlSettings;
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
		int[] pos = keysPanel.getLabelPositionSimple(label);
		switch(pos[1])
		{	// key
			case COL_KEY:
				deselect();
				selectedRow = pos[0];
				keysPanel.setLabelBackground(pos[0],pos[1],GuiTools.COLOR_TABLE_SELECTED_BACKGROUND);
				keysPanel.requestFocusInWindow();
				break;
			// autofire
			case COL_AUTO:
				String action = actions[pos[0]-1];
				boolean auto = !controlSettings.isAutofire(action);
				if(auto)
					controlSettings.addAutofire(action);
				else
					controlSettings.removeAutofire(action);
				setAuto(pos[0],2,auto);
				break;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// KEY LISTENER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public void keyPressed(KeyEvent e)
	{	if(selectedRow!=-1)
		{	// init
			int keyCode = e.getKeyCode();
			String action = actions[selectedRow-1];
			// update control settings
			controlSettings.addOnKey(keyCode,action);
			controlSettings.addOffKey(keyCode,action);
			// 	update panel
			setKey(selectedRow,1,keyCode);
			deselect();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{	
	}

	@Override
	public void keyTyped(KeyEvent e)
	{	
	}
}
