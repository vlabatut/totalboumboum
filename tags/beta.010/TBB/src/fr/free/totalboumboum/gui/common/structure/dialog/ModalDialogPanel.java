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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;

import fr.free.totalboumboum.gui.common.structure.panel.ContentPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.SubPanel;
import fr.free.totalboumboum.gui.frames.NormalFrame;
import fr.free.totalboumboum.gui.tools.GuiTools;

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
		interPanel = new SubPanel(getWidth(),getHeight());
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

	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
