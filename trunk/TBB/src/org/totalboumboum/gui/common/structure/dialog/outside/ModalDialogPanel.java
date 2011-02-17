package org.totalboumboum.gui.common.structure.dialog.outside;


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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.totalboumboum.gui.common.structure.dialog.inside.ModalDialogSubPanel;
import org.totalboumboum.gui.common.structure.dialog.inside.ModalDialogSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.ContentPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.common.structure.subpanel.BasicPanel;
import org.totalboumboum.gui.frames.NormalFrame;
import org.totalboumboum.gui.tools.GuiTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ModalDialogPanel<T extends ModalDialogSubPanel> extends ContentPanel implements ModalDialogSubPanelListener, MouseListener
{	private static final long serialVersionUID = 1L;

	public ModalDialogPanel(MenuPanel parent, T subPanel)
	{	super(parent.getFrame().getMenuWidth(),parent.getFrame().getMenuHeight());
		this.parent = parent;
		
		setOpaque(false);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setAlignmentY(Component.CENTER_ALIGNMENT);
		
		// layout
		{	BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
			setLayout(layout);
		}
		
		// inter panel
		interPanel = new BasicPanel(getWidth(),getHeight());
		{	BoxLayout layout = new BoxLayout(interPanel,BoxLayout.PAGE_AXIS); 
			interPanel.setLayout(layout);
		}
		interPanel.setBackground(GuiTools.COLOR_DIALOG_BACKGROUND);
		interPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		interPanel.addMouseListener(this);
		add(interPanel);
		// sub panel
		this.subPanel = subPanel;
		subPanel.addListener(this);
		interPanel.add(Box.createVerticalGlue());
		interPanel.add(subPanel);
		interPanel.add(Box.createVerticalGlue());
		subPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
	}

	/////////////////////////////////////////////////////////////////
	// SUB PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private T subPanel;
	private BasicPanel interPanel;
	
	public T getSubPanel()
	{	return subPanel;
	}
	
	public void setSubPanel(T subPanel)
	{	interPanel.remove(subPanel);
		this.subPanel = subPanel;
		interPanel.add(subPanel);
	}
	
	/////////////////////////////////////////////////////////////////
	// PARENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected MenuPanel parent;
	
	public MenuPanel getMenuParent()
	{	return parent;
	}

	public void setMenuParent(MenuPanel parent)
	{	this.parent = parent;
	}
	
	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public NormalFrame getFrame()
	{	return parent.getFrame();
	}
/*
	protected void showDialog()
	{	getFrame().setModalDialog(this);		
	}
	
	protected void hideDialog()
	{	getFrame().unsetModalDialog(this);		
	}
*/	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ModalDialogPanelListener> listeners = new ArrayList<ModalDialogPanelListener>();
	
	public void addListener(ModalDialogPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(ModalDialogPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	protected void fireModalDialogButtonClicked(String buttonCode)
	{	List<ModalDialogPanelListener> temp = new ArrayList<ModalDialogPanelListener>(listeners);
		for(ModalDialogPanelListener listener: temp)
			listener.modalDialogButtonClicked(buttonCode);
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
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
