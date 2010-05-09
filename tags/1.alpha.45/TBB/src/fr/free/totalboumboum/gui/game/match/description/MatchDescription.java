package fr.free.totalboumboum.gui.game.match.description;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.security.auth.callback.TextOutputCallback;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import sun.swing.SwingUtilities2;

import fr.free.totalboumboum.data.profile.Portraits;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticRound;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.LimitPoints;
import fr.free.totalboumboum.game.limit.LimitScore;
import fr.free.totalboumboum.game.limit.LimitTime;
import fr.free.totalboumboum.game.limit.LimitTotal;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.common.EntitledDataPanel;
import fr.free.totalboumboum.gui.common.EntitledSubPanel;
import fr.free.totalboumboum.gui.common.EntitledSubPanelTable;
import fr.free.totalboumboum.gui.common.InnerDataPanel;
import fr.free.totalboumboum.gui.common.MenuContainer;
import fr.free.totalboumboum.gui.common.MenuPanel;
import fr.free.totalboumboum.gui.common.SimpleMenuPanel;
import fr.free.totalboumboum.gui.common.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.TablePanel;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.game.round.description.RoundDescription;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.StringTools;

public class MatchDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public MatchDescription(SplitMenuPanel container)
	{	super(container);
		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_DESCRIPTION_TITLE);
		setTitle(txt);
		
		// data
		{	JPanel infoPanel = new JPanel();
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			int width = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_WIDTH);
			int height = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT);
			int margin = GuiTools.getSize(GuiTools.GAME_DATA_MARGIN_SIZE);
			int leftWidth = (int)(width*0.6); 
			int rightWidth = width - leftWidth - margin; 


			Dimension dim = new Dimension(width,height);
			infoPanel.setPreferredSize(dim);
			infoPanel.setMinimumSize(dim);
			infoPanel.setMaximumSize(dim);
			infoPanel.setOpaque(false);
			// players panel
			{	JPanel playersPanel = makePlayersPanel(leftWidth,height);
				infoPanel.add(playersPanel);
			}
			infoPanel.add(Box.createHorizontalGlue());
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				dim = new Dimension(rightWidth,height);
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				int upHeight = (height - margin)/2;
				int downHeight = height - upHeight - margin;
				// points panel
				{	JPanel pointsPanel = makePointsPanel(rightWidth,upHeight);
					rightPanel.add(pointsPanel);
				}
				//
				rightPanel.add(Box.createVerticalGlue());
				// limit panel
				{	JPanel limitsPanel = makeLimitsPanel(rightWidth,downHeight);
					rightPanel.add(limitsPanel);
				}
				//
				infoPanel.add(rightPanel);
			}
			//
			setDataPanel(infoPanel);
		}
	}

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
	
	private JPanel makePlayersPanel(int width, int height)
	{	Match match = getConfiguration().getCurrentMatch();
		int lines = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_NUMBER)+1;
		int cols = 2+1;
		TablePanel playersPanel = new TablePanel(width,height,cols,lines,true,getConfiguration());
		// headers
		{	{	JLabel lbl = playersPanel.getLabel(0,0);
				lbl.setText(null);
				lbl.setPreferredSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
				lbl.setMaximumSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
				lbl.setMinimumSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
				lbl.setOpaque(false);
			}
			String names[] = 
			{	GuiTools.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME,
				GuiTools.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK
			};
			for(int i=0;i<names.length;i++)
			{	BufferedImage image = GuiTools.getIcon(names[i]);
				String tooltip = getConfiguration().getLanguage().getText(names[i]+GuiTools.TOOLTIP); 
				JLabel lbl = playersPanel.getLabel(0,1+i);
				lbl.setText(null);
				lbl.setToolTipText(tooltip);
				int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
				double zoom = lineHeight/(double)image.getHeight();
				image = ImageTools.resize(image,zoom,true);
				ImageIcon icon = new ImageIcon(image);
				lbl.setIcon(icon);
			}
		}
		// empty
		{	for(int i=1;i<lines;i++)
			{	// name
				{	JLabel lbl = playersPanel.getLabel(i,1);
					Dimension dimension = new Dimension(Integer.MAX_VALUE,GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
					lbl.setMaximumSize(dimension);
					dimension = new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT));
					lbl.setMinimumSize(dimension);
				}
			}
		}
		// data
		{	ArrayList<Profile> players = match.getProfiles();
			Iterator<Profile> i = players.iterator();
			int k = cols;
			int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT);
			while(i.hasNext())
			{	Profile profile = i.next();
				// color
				Color clr = profile.getSpriteColor().getColor();
				// portrait
				{	JLabel portraitLabel = (JLabel)playersPanel.getComponent(k++);
					BufferedImage image = profile.getPortraits().getOutgamePortrait(Portraits.OUTGAME_HEAD);
					double zoom = lineHeight/(double)image.getHeight();
					image = ImageTools.resize(image,zoom,true);
					ImageIcon icon = new ImageIcon(image);
					portraitLabel.setIcon(icon);
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					portraitLabel.setBackground(bg);			
					portraitLabel.setText("");
				}
				// name
				{	JLabel nameLabel = (JLabel)playersPanel.getComponent(k++);
					nameLabel.setText(profile.getName());
					nameLabel.setToolTipText(profile.getName());
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					nameLabel.setBackground(bg);
				}
				// rank
				{	NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(0);
					JLabel rankLabel = (JLabel)playersPanel.getComponent(k++);
					rankLabel.setText("0");
					int alpha = GuiTools.ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3;
					Color bg = new Color(clr.getRed(),clr.getGreen(),clr.getBlue(),alpha);
					rankLabel.setBackground(bg);
				}			
			}
		}
		return playersPanel;		
	}
/*	
	private JPanel makeNotesPanel()
	{	// init
		int width = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH);
		int height = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_HEIGHT);
		EntitledSubPanel notesPanel = new EntitledSubPanel(width,height,getConfiguration());
		// title
		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NOTES);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NOTES+GuiTools.TOOLTIP);
		notesPanel.setTitle(text,tooltip);
		// text panel
		{	JTextPane textPane = new JTextPane()
			{	public void paintComponent(Graphics g)
			    {	Graphics2D g2 = (Graphics2D) g;
		        	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		        	g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		        	super.paintComponent(g2);
			    }			
			};
			textPane.setEditable(false);
			textPane.setHighlighter(null);
//			textPane.putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, new Boolean(true));
//			textPane.setSelectedTextColor(null);
//			textPane.setEnabled(false);
//			textPane.setDisabledTextColor(Color.BLACK);

//			textPane.setCaret(new Caret()
//	        {	public void install(JTextComponent c){}
//	            public void deinstall(JTextComponent c){}
//	            public void paint(Graphics g){}
//	            public void addChangeListener(ChangeListener l){}
//	            public void removeChangeListener(ChangeListener l){}
//	            public boolean isVisible(){return false;}
//	            public void setVisible(boolean v){}
//	            public boolean isSelectionVisible(){return false;}
//	            public void setSelectionVisible(boolean v){}
//	            public void setMagicCaretPosition(Point p){}
//	            public Point getMagicCaretPosition(){return new Point(0,0);}
//	            public void setBlinkRate(int rate){}
//	            public int getBlinkRate(){return 10000;}
//	            public int getDot(){return 0;}
//	            public int getMark(){return 0;}
//	            public void setDot(int dot){}
//	            public void moveDot(int dot){}
//	        });
				        
			SimpleAttributeSet sa = new SimpleAttributeSet();
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_JUSTIFIED);
			Font font = getConfiguration().getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE));
			StyleConstants.setFontFamily(sa, font.getFamily());
			StyleConstants.setFontSize(sa, font.getSize());
			StyledDocument doc = textPane.getStyledDocument();
			Match match = getConfiguration().getCurrentMatch();			
			text = "";
			ArrayList<String> list = match.getNotes();
			Iterator<String> i = list.iterator();
			while (i.hasNext())
			{	String temp = i.next();
				text = text + temp + "\n";
			}
			try
			{	doc.insertString(0,text,sa);
			}
			catch (BadLocationException e)
			{	e.printStackTrace();
			}
			doc.setParagraphAttributes(0,doc.getLength()-1,sa,true);
			//
			JComponent dataComp = notesPanel.getDataPanel();
			textPane.setBackground(dataComp.getBackground());
			Dimension dim = dataComp.getPreferredSize();
			textPane.setPreferredSize(dim);
			textPane.setMinimumSize(dim);
			textPane.setMaximumSize(dim);
			textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
			notesPanel.setDataPanel(textPane);
		}
		return notesPanel;		
	}
*/	
	
	private JPanel makePointsPanel(int width, int height)
	{	// init
		String id = GuiTools.GAME_MATCH_DESCRIPTION_POINTS_TITLE;
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		Match match = getConfiguration().getCurrentMatch();
		PointsProcessor mainPP = match.getPointProcessor();
		RoundDescription.makePointsPanelRec(mainPP,data,tooltips,getConfiguration());

		int n = 6;
		if(data.size()<6)
			n = 6;
		else if(data.size()<10)
			n = data.size();
		else
			n = 10;
		int colGrps[] = {1};
		int lns[] = {n};

		EntitledSubPanelTable pointsPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,false);
		return pointsPanel;
	}
	
	private JPanel makeLimitsPanel(int width, int height)
	{	// init
		String id = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_TITLE;
		int colGrps[] = {1,2};
		int lns[] = {8, 8};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		Match match = getConfiguration().getCurrentMatch();
		Limits<MatchLimit> limitsList = match.getLimits();
		Iterator<MatchLimit> i = limitsList.iterator();
		if(!i.hasNext())
		{	ArrayList<?> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			// icon
			dt.add(null);
			tt.add(null);
			// value
			dt.add(null);
			tt.add(null);
		}
		while(i.hasNext())
		{	// init
			Limit limit = i.next();
			String iconName = null;
			String value = null;
			if(limit instanceof LimitConfrontation)
			{	LimitConfrontation l = (LimitConfrontation)limit;
				iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS;
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitPoints)
			{	LimitPoints l = (LimitPoints)limit;
				iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CUSTOM;
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitTotal)
			{	LimitTotal l = (LimitTotal)limit;
				iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TOTAL;
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitScore)
			{	LimitScore l = (LimitScore) limit;
				switch(l.getScore())
				{	case BOMBS:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_BOMBS;
						value = nf.format(l.getLimit());
						break;
					case CROWNS:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CROWNS;
						value = nf.format(l.getLimit());
						break;
					case DEATHS:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_DEATHS;
						value = nf.format(l.getLimit());
						break;
					case ITEMS:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_ITEMS;
						value = nf.format(l.getLimit());
						break;
					case KILLS:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_KILLS;
						value = nf.format(l.getLimit());
						break;
					case PAINTINGS:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_PAINTINGS;
						value = nf.format(l.getLimit());
						break;
					case TIME:
						iconName = GuiTools.GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TIME;
						value = nf.format(l.getLimit());
						break;
				}
			}
			// lists
			String tooltip = getConfiguration().getLanguage().getText(iconName+GuiTools.TOOLTIP);
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			BufferedImage icon = GuiTools.getIcon(iconName);
			dt.add(icon);
			tt.add(tooltip);
			dt.add(value);
			tt.add(value);			
		}			
			
		// result
		EntitledSubPanelTable limitsPanel = new EntitledSubPanelTable(width,height,id,colGrps,lns,data,tooltips,getConfiguration(),true,true);
		return limitsPanel;
	}
}
