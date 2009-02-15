package fr.free.totalboumboum.gui.common.content.subpanel.leg;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.free.totalboumboum.game.tournament.cup.CupLeg;
import fr.free.totalboumboum.game.tournament.cup.CupPart;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.gui.common.content.subpanel.part.PartSubPanel;
import fr.free.totalboumboum.gui.common.content.subpanel.part.PartSubPanelListener;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.EmptySubPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class LegSubPanel extends EmptySubPanel implements MouseListener, PartSubPanelListener
{	private static final long serialVersionUID = 1L;

	public LegSubPanel(int width, int height)
	{	super(width,height);
	
		// background
		{	Color bg = GuiTools.COLOR_COMMON_BACKGROUND;
			setBackground(bg);
		}
		
		// layout
		{	BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
			setLayout(layout);
		}
		
		// sizes
		int buttonsHeight = (int)(height*0.06);
		int partsHeight = height - 4*GuiTools.subPanelMargin - 2*buttonsHeight;
		int buttonsWidth = width - 2*GuiTools.subPanelMargin;
		int partsWidth = width - 2*GuiTools.subPanelMargin;
		int externalButtonsWidth = (buttonsWidth - 2*GuiTools.subPanelMargin)/3;
		int centralButtonWidth = buttonsWidth - 2*GuiTools.subPanelMargin - 2*externalButtonsWidth;
		
		add(Box.createVerticalGlue());

		// buttons up panel
		{	upPanel = new JPanel();
			upPanel.setOpaque(false);
			Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
			// layout
			{	BoxLayout layout = new BoxLayout(upPanel,BoxLayout.LINE_AXIS); 
				upPanel.setLayout(layout);
				upPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
			}
			// size
			{	Dimension dim = new Dimension(buttonsWidth,buttonsHeight);
				upPanel.setMinimumSize(dim);
				upPanel.setPreferredSize(dim);
				upPanel.setMaximumSize(dim);
			}
			
			upPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// left
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(bg);
				Dimension dim = new Dimension(externalButtonsWidth,buttonsHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_LEG_LEFT;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				upPanel.add(label);
			}
			upPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// up
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(bg);
				Dimension dim = new Dimension(centralButtonWidth,buttonsHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_LEG_UP;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				upPanel.add(label);
			}
			upPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// right
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(bg);
				Dimension dim = new Dimension(externalButtonsWidth,buttonsHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_LEG_RIGHT;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				upPanel.add(label);
			}
//			upPanel.add(Box.createHorizontalGlue());
			
			add(upPanel);
		}

		add(Box.createVerticalGlue());

		// parts panel
		{	partsPanel = new JPanel();
			partsPanel.setOpaque(false);
			// layout
			{	BoxLayout layout = new BoxLayout(partsPanel,BoxLayout.PAGE_AXIS); 
				partsPanel.setLayout(layout);
			}
			// size
			{	Dimension dim = new Dimension(partsWidth,partsHeight);
				partsPanel.setMinimumSize(dim);
				partsPanel.setPreferredSize(dim);
				partsPanel.setMaximumSize(dim);
			}
			add(partsPanel);
		}
		
		add(Box.createVerticalGlue());

		// buttons down panel
		{	downPanel = new JPanel();
			downPanel.setOpaque(false);
			Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
			// layout
			{	BoxLayout layout = new BoxLayout(downPanel,BoxLayout.LINE_AXIS); 
				downPanel.setLayout(layout);
			}
			// size
			{	Dimension dim = new Dimension(buttonsWidth,buttonsHeight);
				downPanel.setMinimumSize(dim);
				downPanel.setPreferredSize(dim);
				downPanel.setMaximumSize(dim);
			}
			
			downPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// left
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(bg);
				Dimension dim = new Dimension(externalButtonsWidth,buttonsHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_LEG_LEFT;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				downPanel.add(label);
			}
			downPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// up
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(bg);
				Dimension dim = new Dimension(centralButtonWidth,buttonsHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_LEG_DOWN;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				downPanel.add(label);
			}
			downPanel.add(Box.createRigidArea(new Dimension(GuiTools.subPanelMargin,GuiTools.subPanelMargin)));
			// right
			{	JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBackground(bg);
				Dimension dim = new Dimension(externalButtonsWidth,buttonsHeight);
				label.setMinimumSize(dim);
				label.setPreferredSize(dim);
				label.setMaximumSize(dim);
				String key = GuiKeys.COMMON_LEG_RIGHT;
				GuiTools.setLabelKey(label,key,true);
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				label.addMouseListener(this);
				downPanel.add(label);
			}
//			downPanel.add(Box.createHorizontalGlue());
			
			add(downPanel);
		}
		
		add(Box.createVerticalGlue());

		// leg
		setLeg(null,2);
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final int COL_LEFT = 1;
	private static final int COL_CENTER = 3;
	private static final int COL_RIGHT = 5;
	private JPanel upPanel;
	private JPanel downPanel;
	private JPanel partsPanel;
	private int partsPerPage;
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupLeg leg;
	private CupPart selectedPart;
	private int currentPage = 0;
	private ArrayList<JPanel> pagePanels;
	private int pageCount;	
	
	public void setLeg(CupLeg leg, int partsPerPage)
	{	// init
		this.leg = leg;
		this.partsPerPage = partsPerPage;
		pagePanels = new ArrayList<JPanel>();
		currentPage = 0;
		selectedPart = null;
		fireLegSelectionChanged();
		
		// size
		int partsWidth = partsPanel.getPreferredSize().width;
		int partsHeight = partsPanel.getPreferredSize().height;
		int partWidth = partsWidth;
		int partHeight = (int)(partsHeight - (partsPerPage-1)*GuiTools.subPanelMargin)/partsPerPage;
		
		pageCount = getPageCount();
		
		for(int panelIndex=0;panelIndex<pageCount;panelIndex++)
		{	JPanel tempPanel = new JPanel();
			tempPanel.setOpaque(false);
			// layout
			{	BoxLayout layout = new BoxLayout(tempPanel,BoxLayout.PAGE_AXIS); 
				tempPanel.setLayout(layout);
			}
			// size
			{	Dimension dim = new Dimension(partsWidth,partsHeight);
				tempPanel.setMinimumSize(dim);
				tempPanel.setPreferredSize(dim);
				tempPanel.setMaximumSize(dim);
			}
			// content
			for(int p=0;p<partsPerPage;p++)
			{	CupPart part = null;
				int partIndex = p+panelIndex*partsPerPage;
				if(leg!=null && leg.getParts().size()>partIndex)
					part = leg.getParts().get(partIndex);
				PartSubPanel partPanel = new PartSubPanel(partWidth,partHeight);
				partPanel.setPart(part);
				partPanel.addListener(this);
				tempPanel.add(partPanel);
				if(p<partsPerPage-1)
					tempPanel.add(Box.createVerticalGlue());					
			}
			pagePanels.add(tempPanel);
		}
		
		refreshList();
	}
	
	private int getPageCount()
	{	int result = 1;
		if(leg!=null)
		{	ArrayList<CupPart> parts = leg.getParts();
			result = parts.size()/partsPerPage;
			if(parts.size()%partsPerPage>0)
				result++;
			else if(result==0)
				result = 1;
		}
		return result;
	}		
	
	public CupPart getSelectedPart()
	{	return selectedPart;
	}
	
	private void selectPart(CupPart part)
	{	// init
		ArrayList<CupPart> parts = leg.getParts();
		int index;
	
		// unselect
		if(selectedPart!=null)
		{	index = parts.indexOf(selectedPart);
			int page = index/partsPerPage;
			int line = (index%partsPerPage)*2;
			JPanel panel = pagePanels.get(page);
			PartSubPanel partPanel = (PartSubPanel)panel.getComponent(line);
			partPanel.setSelected(false);
			selectedPart = null;
		}
		
		//select
		if(part!=null)
		{	// same leg, or other leg ?
			index = parts.indexOf(part);
			if(index==-1)
			{	CupTournament tournament = leg.getTournament();
				ArrayList<CupLeg> legs = tournament.getLegs();
				Iterator<CupLeg> it = legs.iterator();
				CupLeg lg = null;
				while(index==-1 && it.hasNext())
				{	lg = it.next();
					index = lg.getParts().indexOf(part);
				}
				// refresh leg
				setLeg(lg,partsPerPage);
			}
			// refresh page
			int page = index/partsPerPage;
			if(page!=currentPage)
			{	currentPage = page;
				refreshList();
			}
			// change color
			int line = (index%partsPerPage)*2;
			JPanel panel = pagePanels.get(currentPage);
			PartSubPanel partPanel = (PartSubPanel)panel.getComponent(line);
			partPanel.setSelected(true);
			selectedPart = part;
		}
		
		// update listeners
		fireLegSelectionChanged();
	}

	private void refreshList()
	{	int index = GuiTools.indexOfComponent(this,partsPanel);
		remove(partsPanel);
		partsPanel = pagePanels.get(currentPage);
		add(partsPanel,index);
		validate();
		repaint();
	}
	
	public void refresh()
	{	setLeg(leg,partsPerPage);		
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
	{	// init
		JLabel label = (JLabel)e.getComponent();
		boolean up = true;
		int index = GuiTools.indexOfComponent(upPanel,label);
		if(index<0)
		{	up = false;
			index = GuiTools.indexOfComponent(downPanel,label);
		}

		// previous leg
		if(index==COL_LEFT)
		{	ArrayList<CupLeg> legs = leg.getTournament().getLegs();
			int legNumber = leg.getNumber();
			if(legNumber>0)
			{	selectPart(null);
				CupLeg newLeg = legs.get(legNumber-1);
				setLeg(newLeg,partsPerPage);
				fireLegBeforeClicked();
			}		
		}
		// center buttons
		else if(index==COL_CENTER)
		{	// up button
			if(up)
			{	if(currentPage>0)
				{	selectPart(null);
					currentPage--;
					refreshList();
				}
			}
			// down button
			else
			{	if(currentPage<getPageCount()-1)
				{	selectPart(null);
					currentPage++;
					refreshList();
				}
			}
		}
		// previous leg
		else if(index==COL_RIGHT)
		{	ArrayList<CupLeg> legs = leg.getTournament().getLegs();
			int legNumber = leg.getNumber();
			if(legNumber<legs.size()-1)
			{	selectPart(null);
				CupLeg newLeg = legs.get(legNumber+1);
				setLeg(newLeg,partsPerPage);
				fireLegAfterClicked();
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<LegSubPanelListener> listeners = new ArrayList<LegSubPanelListener>();
	
	public void addListener(LegSubPanelListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);		
	}

	public void removeListener(LegSubPanelListener listener)
	{	listeners.remove(listener);		
	}
	
	private void fireLegSelectionChanged()
	{	for(LegSubPanelListener listener: listeners)
			listener.legSelectionChanged();
	}

	private void fireLegBeforeClicked()
	{	for(LegSubPanelListener listener: listeners)
			listener.legBeforeClicked();
	}

	private void fireLegAfterClicked()
	{	for(LegSubPanelListener listener: listeners)
			listener.legAfterClicked();
	}

	/////////////////////////////////////////////////////////////////
	// PART SUB PANEL LISTENER		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void partAfterClicked(CupPart part)
	{	selectPart(part);
	}

	@Override
	public void partBeforeClicked(CupPart part)
	{	selectPart(part);
	}

	@Override
	public void partTitleClicked(CupPart part)
	{	selectPart(part);
	}
}
