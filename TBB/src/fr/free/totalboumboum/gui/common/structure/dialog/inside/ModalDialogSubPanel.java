package fr.free.totalboumboum.gui.common.structure.dialog.inside;


/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.common.structure.subpanel.inside.EmptyContentPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public abstract class ModalDialogSubPanel extends SubPanel<EmptyContentPanel> implements MouseListener
{	private static final long serialVersionUID = 1L;
	
	public ModalDialogSubPanel(int width, int height)
	{	super(width,height);
	}
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	protected JLabel initButton(String key, Font font, int buttonsHeight)
	{	JLabel result = new JLabel();
		result.setFont(font);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
		result.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
		result.setOpaque(true);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		BufferedImage icon = GuiTools.getIcon(key);
		ImageIcon icn = null;
		if(icon!=null)
		{	double zoom = buttonsHeight/(double)icon.getHeight();
			icon = ImageTools.resize(icon,zoom,true);
			icn = new ImageIcon(icon);
		}
		result.setText(null);
		result.setIcon(icn);
		result.setToolTipText(tooltip);
		result.setMaximumSize(new Dimension(Integer.MAX_VALUE,buttonsHeight));
		result.addMouseListener(this);
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
	private ArrayList<ModalDialogSubPanelListener> listeners = new ArrayList<ModalDialogSubPanelListener>();
	
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
