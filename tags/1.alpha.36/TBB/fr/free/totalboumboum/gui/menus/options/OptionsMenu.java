package fr.free.totalboumboum.gui.menus.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.tools.GuiTools;

public class OptionsMenu extends SimpleMenuPanel
{	private static final long serialVersionUID = 1L;
	
	private JButton buttonGamePlay;
	private JButton buttonVideo;
	private JButton buttonBack;
	
	public OptionsMenu(MenuContainer container, MenuPanel parent)
	{	super(container, parent);
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);
		// size
		setPreferredSize(getConfiguration().getPanelDimension());
		// background
		setBackground(Color.RED);
		
		// buttons
		add(Box.createVerticalGlue());
		buttonGamePlay = GuiTools.createVerticalMenuButton(GuiTools.OPTIONS_MENU_BUTTON_GAMEPLAY,this,getConfiguration());
		buttonVideo = GuiTools.createVerticalMenuButton(GuiTools.OPTIONS_MENU_BUTTON_VIDEO,this,getConfiguration());
		add(Box.createRigidArea(new Dimension(0,getConfiguration().getVerticalMenuButtonSpace())));
		buttonBack = GuiTools.createVerticalMenuButton(GuiTools.OPTIONS_MENU_BUTTON_BACK,this,getConfiguration());
		add(Box.createVerticalGlue());		
	}
	
	public void actionPerformed(ActionEvent e)
	{	System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals(GuiTools.OPTIONS_MENU_BUTTON_VIDEO))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.OPTIONS_MENU_BUTTON_GAMEPLAY))
		{	
	    }
		else if(e.getActionCommand().equals(GuiTools.OPTIONS_MENU_BUTTON_BACK))
		{	replaceWith(parent);
	    }
	} 
	
	public void refresh()
	{	// nothing to do here
	}
}
