package org.totalboumboum.gui.common.structure.dialog.inside;


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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ModalDialogSubPanel extends EmptySubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public ModalDialogSubPanel(int width, int height)
	{	super(width,height,Mode.TITLE);
	}
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	protected MyLabel initButton(String key, Font font, int buttonsHeight)
	{	MyLabel result = new MyLabel();
		result.setFont(font);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setBackground(GuiColorTools.COLOR_TABLE_HEADER_BACKGROUND);
		result.setForeground(GuiColorTools.COLOR_TABLE_HEADER_FOREGROUND);
		result.setOpaque(true);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		BufferedImage icon = GuiTools.getIcon(key);
		ImageIcon icn = null;
		if(icon!=null)
		{	double zoom = buttonsHeight/(double)icon.getHeight();
			icon = ImageTools.getResizedImage(icon,zoom,true);
			icn = new ImageIcon(icon);
		}
		result.setText(null);
		result.setIcon(icn);
		result.setToolTipText(tooltip);
		result.setMaximumSize(new Dimension(Integer.MAX_VALUE,buttonsHeight));
		result.addMouseListener(this);
		result.setMouseSensitive(true);
		return result;
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
	public void mouseReleased(MouseEvent e)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ModalDialogSubPanelListener> listeners = new ArrayList<ModalDialogSubPanelListener>();
	
	public void addListener(ModalDialogSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(ModalDialogSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	protected void fireModalDialogButtonClicked(String buttonCode)
	{	for(ModalDialogSubPanelListener listener: listeners)
			listener.modalDialogButtonClicked(buttonCode);
	}
}
