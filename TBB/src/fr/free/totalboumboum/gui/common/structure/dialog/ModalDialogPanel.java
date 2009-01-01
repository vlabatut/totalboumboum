package fr.free.totalboumboum.gui.common.structure.dialog;

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

import java.util.ArrayList;

import javax.swing.BoxLayout;

import fr.free.totalboumboum.gui.common.structure.panel.ContentPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.frames.NormalFrame;
import fr.free.totalboumboum.gui.tools.GuiTools;


public abstract class ModalDialogPanel<T extends SubPanel> extends ContentPanel
{	private static final long serialVersionUID = 1L;

	public ModalDialogPanel(MenuPanel parent, T subPanel)
	{	super(parent.getFrame().getMenuWidth(),parent.getFrame().getMenuHeight());
		this.parent = parent;
		
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		
		// sub panel
		interPanel = new SubPanel(subPanel.getWidth(),subPanel.getHeight());
		interPanel.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
		add(interPanel);
		this.subPanel = subPanel;
		interPanel.add(subPanel);
	}

	/////////////////////////////////////////////////////////////////
	// SUB PANEL		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private T subPanel;
	private SubPanel interPanel;
	
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

	protected void showDialog()
	{	getFrame().setModalDialog(this);		
	}
	
	protected void hideDialog()
	{	getFrame().unsetModalDialog(this);		
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<ModalDialogPanelListener> listeners = new ArrayList<ModalDialogPanelListener>();
	
	public void addListener(ModalDialogPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(ModalDialogPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	protected void fireModalDialogButtonClicked(String buttonCode)
	{	for(ModalDialogPanelListener listener: listeners)
			listener.modalDialogButtonClicked(buttonCode);
	}
}
