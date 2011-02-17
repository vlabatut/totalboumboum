package org.totalboumboum.gui.common.content.subpanel.host;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.network.data.host.HostInfo;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HostInfoSubPanel extends TableSubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	private static final int LINES = 8;
	private static final int COL_SUBS = 2;
	private static final int COL_GROUPS = 1;
	
	public HostInfoSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER,LINES,COL_GROUPS,COL_SUBS,true);
		
		setHostInfo(null);
	}
		
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HostInfo hostInfo;

	public HostInfo getHostInfo()
	{	return hostInfo;	
	}
	
	public void setHostInfo(HostInfo hostInfo)
	{	this.hostInfo = hostInfo;
		
		// sizes
		reinit(LINES,COL_GROUPS,COL_SUBS);
		
		// icons
		List<String> keys = new ArrayList<String>();
		if(showName)
			keys.add(GuiKeys.COMMON_HOST_INFO_NAME);
		if(showIp)
			keys.add(GuiKeys.COMMON_HOST_INFO_IP);
		if(showType)
			keys.add(GuiKeys.COMMON_HOST_INFO_TYPE);
		if(showPlayed)
			keys.add(GuiKeys.COMMON_HOST_INFO_PLAYED);
		if(showPreferred)
			keys.add(GuiKeys.COMMON_HOST_INFO_PREFERRED);
		
		if(hostInfo!=null)
		{	// text
			List<String> texts = new ArrayList<String>();
			List<String> tooltips = new ArrayList<String>();
			if(showName)
			{	String name = hostInfo.getName();
				String text = "?";
				if(name!=null)
					text = name;
				texts.add(text);
				tooltips.add(text);
			}
			if(showIp)
			{	String ip = hostInfo.getLastIp();
				String text = "?";
				if(ip!=null)
					text = ip;
				texts.add(text);
				tooltips.add(text);
			}
			if(showType)
			{	String text;
				String key;
				String tooltip;
				if(hostInfo.isCentral())
				{	if(hostInfo.isDirect())
						key = GuiKeys.COMMON_HOST_INFO_TYPE_DATA_BOTH;
					else
						key = GuiKeys.COMMON_HOST_INFO_TYPE_DATA_CENTRAL;
				}
				else
				{	if(hostInfo.isDirect())
						key = GuiKeys.COMMON_HOST_INFO_TYPE_DATA_DIRECT;
					else
						key = null;
				}
				if(key==null)
				{	text = "?";
					tooltip = text;
				}
				else
				{	text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
				}
				texts.add(text);
				tooltips.add(tooltip);
			}
			if(showPlayed)
			{	Integer value = hostInfo.getUses();
				String text = "?";
				if(value!=null)
					text = Integer.toString(value);
				texts.add(text);
				tooltips.add(text);
			}
			if(showPreferred)
			{	Boolean preferred = hostInfo.isPreferred();
				String text = "?";
				String tooltip = text;
				if(preferred!=null)
				{	String key;
					if(preferred)
						key = GuiKeys.COMMON_HOST_INFO_PREFERRED_DATA_PREFERRED;
					else
						key = GuiKeys.COMMON_HOST_INFO_PREFERRED_DATA_NON_PREFERRED;
					text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
					tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
				}
				texts.add(text);
				tooltips.add(tooltip);
			}
			
			// content
			for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					if(keys.get(line).equals(GuiKeys.COMMON_HOST_INFO_IP))
					{	MyLabel lbl = getLabel(line,colSub);
						lbl.addMouseListener(this);
						lbl.setMouseSensitive(true);
					}
					colSub++;
				}
				// data
				{	String text = texts.get(line);
					String tooltip = tooltips.get(line);
					setLabelText(line,colSub,text,tooltip);
					Color fg = GuiTools.COLOR_TABLE_HEADER_FOREGROUND;
					setLabelForeground(line,0,fg);
					Color bg;
					if(line>0)
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					if(keys.get(line).equals(GuiKeys.COMMON_HOST_INFO_IP))
					{	MyLabel lbl = getLabel(line,colSub);
						lbl.addMouseListener(this);
						lbl.setMouseSensitive(true);
					}
					colSub++;
				}
			}
		}
		else
		{	for(int line=0;line<keys.size();line++)
			{	// header
				int colSub = 0;
				{	setLabelKey(line,colSub,keys.get(line),true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					if(keys.get(line).equals(GuiKeys.COMMON_HOST_INFO_IP))
					{	MyLabel lbl = getLabel(line,colSub);
						lbl.removeMouseListener(this);
						lbl.setMouseSensitive(false);
					}
					colSub++;
				}
				// data
				{	String text = null;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(keys.get(line)+GuiKeys.TOOLTIP);
					setLabelText(line,colSub,text,tooltip);
					Color bg;
					if(line>0)
						bg = GuiTools.COLOR_TABLE_NEUTRAL_BACKGROUND;
					else
						bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					setLabelBackground(line,colSub,bg);
					if(keys.get(line).equals(GuiKeys.COMMON_HOST_INFO_IP))
					{	MyLabel lbl = getLabel(line,colSub);
						lbl.removeMouseListener(this);
						lbl.setMouseSensitive(false);
					}
					colSub++;
				}
			}
		}
		
		// col widths
		int iconWidth = getHeaderHeight();
		setColSubMinWidth(0,iconWidth);
		setColSubPrefWidth(0,iconWidth);
		setColSubMaxWidth(0,iconWidth);
		int maxWidth = getDataWidth()-(COL_SUBS-1)*GuiTools.subPanelMargin-iconWidth;
		setColSubMinWidth(1,maxWidth);
		setColSubPrefWidth(1,maxWidth);
		setColSubMaxWidth(1,maxWidth);
	}
	
	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean showName = true;
	private boolean showIp = true;
	private boolean showPlayed = true;
	private boolean showPreferred = true;
	private boolean showType = true;

	public void setShowName(boolean showName)
	{	this.showName = showName;
	}

	public void setShowIp(boolean showIp)
	{	this.showIp = showIp;
	}

	public void setShowPlayed(boolean showPlayed)
	{	this.showPlayed = showPlayed;
	}

	public void setShowPreferred(boolean showPreferred)
	{	this.showPreferred = showPreferred;
	}

	public void setShowType(boolean showType)
	{	this.showType = showType;
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
	{	fireIpClicked();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<HostInfoSubPanelListener> listeners = new ArrayList<HostInfoSubPanelListener>();
	
	public void addListener(HostInfoSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(HostInfoSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireIpClicked()
	{	for(HostInfoSubPanelListener listener: listeners)
			listener.ipClicked();
	}
}
