package fr.free.totalboumboum.gui.menus.profiles.edit;

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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.inside.Line;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.LinesSubPanel;
import fr.free.totalboumboum.gui.common.structure.subpanel.outside.SubPanel.Mode;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.menus.profiles.ais.SelectedAiSplitPanel;
import fr.free.totalboumboum.gui.menus.profiles.heroes.SelectedHeroSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class EditProfileData extends EntitledDataPanel implements MouseListener, DocumentListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINE_COUNT = 19;

	private static final int LINE_AI = 0;
	private static final int LINE_HERO = 1;
	private static final int LINE_COLOR = 2;

	private Profile profile;
	private LinesSubPanel editPanel;
	private JTextPane textPane;
	private StyledDocument doc;
	private SimpleAttributeSet sa;
	
	public EditProfileData(SplitMenuPanel container, Profile profile)
	{	super(container);
		this.profile = profile.copy();
		
		// title
		setTitleKey(GuiKeys.MENU_PROFILES_EDIT_TITLE);
	
		// data
		{	int w = getDataWidth();
			int margin = GuiTools.subPanelMargin;
			int lineHeight = (int)((getDataHeight() - ((LINE_COUNT+1)+1)*margin)/((float)(LINE_COUNT+1)));
			int lineFontSize = GuiTools.getFontSize(lineHeight*GuiTools.FONT_RATIO);
			int h = lineHeight*LINE_COUNT+margin*(LINE_COUNT+1);
			int nameHeight = getDataHeight()-h-margin;
			editPanel = new LinesSubPanel(w,h,Mode.BORDER,LINE_COUNT,1,false);
			editPanel.setOpaque(false);

			// main panel
			{	BoxLayout layout = new BoxLayout(dataPart,BoxLayout.PAGE_AXIS); 
				dataPart.setLayout(layout);
				dataPart.add(Box.createRigidArea(new Dimension(margin,margin)));
			}
			
			// NAME
			{	JPanel namePanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(namePanel,BoxLayout.LINE_AXIS);
					namePanel.setLayout(layout);
					namePanel.setOpaque(false);
					//namePanel.setBackground(GuiTools.COLOR_COMMON_BACKGROUND);
					Dimension dim = new Dimension(w,nameHeight);
					namePanel.setMinimumSize(dim);
					namePanel.setPreferredSize(dim);
					namePanel.setMaximumSize(dim);		
				}
				namePanel.add(Box.createRigidArea(new Dimension(margin,margin)));
				// icon
				{	JLabel label = new JLabel();
					Dimension dim = new Dimension(nameHeight,nameHeight);
					label.setMinimumSize(dim);
					label.setPreferredSize(dim);
					label.setMaximumSize(dim);
					label.setOpaque(true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					label.setBackground(bg);
					String key = GuiKeys.MENU_PROFILES_EDIT_NAME;
					String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
					label.setToolTipText(tooltip);
					BufferedImage icon = GuiTools.getIcon(key);
					double zoom = nameHeight/(double)icon.getHeight();
					icon = ImageTools.resize(icon,zoom,true);
					ImageIcon icn = new ImageIcon(icon);
					label.setText(null);
					namePanel.add(label);
					label.setIcon(icn);		
				}				
				namePanel.add(Box.createRigidArea(new Dimension(margin,margin)));
				// text pane
				{	textPane = new JTextPane()
					{	private static final long serialVersionUID = 1L;
						public void paintComponent(Graphics g)
					    {	Graphics2D g2 = (Graphics2D) g;
				        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
				        	super.paintComponent(g2);
					    }			
					};
					textPane.setEditable(true);
					textPane.setOpaque(true);
					int lnH = nameHeight;
					int lnW = w - 3*margin - nameHeight;
					Dimension dim = new Dimension(lnW,lnH);
					textPane.setPreferredSize(dim);
					textPane.setMinimumSize(dim);
					textPane.setMaximumSize(dim);
					textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					textPane.setBackground(bg);
					Color fg = GuiTools.COLOR_TABLE_REGULAR_FOREGROUND;
					textPane.setForeground(fg);
					sa = new SimpleAttributeSet();
					StyleConstants.setAlignment(sa,StyleConstants.ALIGN_CENTER/*JUSTIFIED*/);
					Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)(lineFontSize));
					StyleConstants.setFontFamily(sa,font.getFamily());
					StyleConstants.setFontSize(sa,font.getSize());
					StyleConstants.setForeground(sa,fg);
					doc = textPane.getStyledDocument();
					doc.setParagraphAttributes(0,doc.getLength()+1,sa,true);
					doc.addDocumentListener(this);
					namePanel.add(textPane);
				}
				namePanel.add(Box.createRigidArea(new Dimension(margin,margin)));
				dataPart.add(namePanel);
			}

			// AI
			{	Line ln = editPanel.getLine(LINE_AI);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_AI,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// pack
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// name
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// reset
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_AI_RESET,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					JLabel label = editPanel.getLabel(LINE_AI,col);
					label.addMouseListener(this);
					col++;
				}
				// change
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_AI_CHANGE,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					JLabel label = editPanel.getLabel(LINE_AI,col);
					label.addMouseListener(this);
					col++;
				}
			}

			// HERO
			{	Line ln = editPanel.getLine(LINE_HERO);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_HERO,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// pack
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// name
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// change
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					ln.setLabelKey(col,GuiKeys.MENU_PROFILES_EDIT_HERO_CHANGE,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					JLabel label = editPanel.getLabel(LINE_HERO,col);
					label.addMouseListener(this);
					col++;
				}
			}
			
			// COLORS
			{	int line = LINE_COLOR;
				Line ln = editPanel.getLine(line);
				ln.addLabel(0);
				ln.addLabel(0);
				ln.addLabel(0);
				int col = 0;
				// icon
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					String key = GuiKeys.MENU_PROFILES_EDIT_COLOR;
					ln.setLabelKey(col,key,true);
					Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
					ln.setLabelBackground(col,bg);
					col++;
				}
				// value
				{	ln.setLabelMaxWidth(col,Integer.MAX_VALUE);
					col++;
				}
				// previous
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					String key = GuiKeys.MENU_PROFILES_EDIT_COLOR_PREVIOUS;
					ln.setLabelKey(col,key,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					JLabel label = editPanel.getLabel(line,col);
					label.addMouseListener(this);
					col++;
				}
				// next
				{	ln.setLabelMinWidth(col,ln.getHeight());
					ln.setLabelMaxWidth(col,ln.getHeight());
					String key = GuiKeys.MENU_PROFILES_EDIT_COLOR_NEXT;
					ln.setLabelKey(col,key,true);
					Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
					ln.setLabelBackground(col,bg);
					JLabel label = editPanel.getLabel(line,col);
					label.addMouseListener(this);
					col++;
				}
			}
			
			// EMPTY
			{	for(int line=LINE_COLOR+1;line<LINE_COUNT;line++)
				{	Line ln = editPanel.getLine(line);
					int col = 0;
					int mw = ln.getWidth();
					ln.setLabelMinWidth(col,mw);
					ln.setLabelPrefWidth(col,mw);
					ln.setLabelMaxWidth(col,mw);
					col++;
				}
			}
			
			refreshData();
		}
		
		dataPart.add(editPanel);
	}

	private void refreshColor()
	{	PredefinedColor color = profile.getDefaultSprite().getColor();
		String text = null;
		String tooltip = null;
		Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
		if(color!=null)
		{	String colorKey = color.toString();
			colorKey = colorKey.toUpperCase().substring(0,1)+colorKey.toLowerCase().substring(1,colorKey.length());
			colorKey = GuiKeys.COMMON_COLOR+colorKey;
			text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(colorKey); 
			tooltip = text;
			Color clr = color.getColor();
			int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
			bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
		}
		editPanel.setLabelText(LINE_COLOR,1,text,tooltip);
		editPanel.setLabelBackground(LINE_COLOR,1,bg);
	}
	
	
	
	
	@Override
	public void refresh()
	{	refreshData();
	}
	
	public Profile getProfile()
	{	return profile;	
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
	{	JLabel label = (JLabel)e.getComponent();
		int[] pos = editPanel.getLabelPosition(label);
		switch(pos[0])
		{	// AI
			case LINE_AI:
				if(pos[1]==3)
				{	profile.setAiName(null);
					profile.setAiPackname(null);
					refreshAi();
				}
				else if(pos[1]==4)
				{	SelectedAiSplitPanel aiPanel = new SelectedAiSplitPanel(container.getContainer(),container,profile);
					getContainer().replaceWith(aiPanel);
				}
				break;
			// HERO
			case LINE_HERO:
				SelectedHeroSplitPanel heroPanel = new SelectedHeroSplitPanel(container.getContainer(),container,profile);
				getContainer().replaceWith(heroPanel);
				break;
			// COLOR
			case LINE_COLOR:
				PredefinedColor color = profile.getDefaultSprite().getColor();
				PredefinedColor allColors[] = PredefinedColor.values();
				if(pos[1]==2)
				{	int i = allColors.length-1;
					if(color!=null)
					{	while(i>=0 && allColors[i]!=color)
							i--;
					}
					if(i==0)
						color = null;
					else
						color = allColors[i-1];
				}
				else //if(pos{1==3)
				{	int i = 0;
					if(color!=null)
					{	while(i<allColors.length && allColors[i]!=color)
							i++;
					}
					if(i==allColors.length-1)
						color = null;
					else
						color = allColors[i+1];
				}
				profile.getDefaultSprite().setColor(color);
				refreshColor();
		}	
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}	
	
	private void refreshData()
	{	refreshName();
		refreshAi();
		refreshHero();
		refreshColor();
	}
	
	private void refreshName()
	{	// name
		String text = profile.getName();
		String tooltip = text;
		try
		{	doc.remove(0,doc.getLength());
			doc.insertString(0,text,sa);
			doc.setParagraphAttributes(0,doc.getLength()+1,sa,true);
		}
		catch (BadLocationException e)
		{	e.printStackTrace();
		}
		textPane.setToolTipText(tooltip);				
	}
	
	private void refreshAi()
	{	// init
		Line ln = editPanel.getLine(LINE_AI);
		int col = 1;		
		// pack
		{	String text = profile.getAiPackname();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}		
		// name
		{	String text = profile.getAiName();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}
	}
	
	private void refreshHero()
	{	// init
		Line ln = editPanel.getLine(LINE_HERO);
		int col = 1;		
		// pack
		{	String text = profile.getSpritePack();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}		
		// name
		{	String text = profile.getSpriteName();
			String tooltip = text;
			ln.setLabelText(col,text,tooltip);
			col++;
		}
	}

	/////////////////////////////////////////////////////////////////
	// DOCUMENT LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void changedUpdate(DocumentEvent e)
	{	// not usefull here
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{	Document document = e.getDocument();
		try
		{	//doc.setParagraphAttributes(0,doc.getLength()+1,sa,true);
			String name = document.getText(0,document.getLength());
			profile.setName(name);
			textPane.setToolTipText(name);
		}
		catch (BadLocationException e1)
		{	e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{	Document document = e.getDocument();
		try
		{	String name = document.getText(0,document.getLength());
			profile.setName(name);
			textPane.setToolTipText(name);
		}
		catch (BadLocationException e1)
		{	e1.printStackTrace();
		}
	}
}
