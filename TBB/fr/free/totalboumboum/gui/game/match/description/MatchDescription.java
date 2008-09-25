package fr.free.totalboumboum.gui.game.match.description;

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
import fr.free.totalboumboum.game.limit.LimitTotal;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.point.PlayerPoints;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.generic.EntitledDataPanel;
import fr.free.totalboumboum.gui.generic.EntitledSubPanel;
import fr.free.totalboumboum.gui.generic.InnerDataPanel;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;
import fr.free.totalboumboum.gui.generic.SplitMenuPanel;
import fr.free.totalboumboum.gui.generic.TablePanel;
import fr.free.totalboumboum.gui.menus.options.OptionsMenu;
import fr.free.totalboumboum.gui.menus.tournament.TournamentMain;
import fr.free.totalboumboum.gui.tools.SpringUtilities;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class MatchDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public MatchDescription(SplitMenuPanel container)
	{	super(container);
		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_TITLE_DESCRIPTION);
		setTitle(txt);
		
		// data
		{	JPanel infoPanel = new JPanel();
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			Dimension dim = new Dimension(GuiTools.getSize(GuiTools.GAME_DATA_PANEL_WIDTH),GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT));
			infoPanel.setPreferredSize(dim);
			infoPanel.setMinimumSize(dim);
			infoPanel.setMaximumSize(dim);
			infoPanel.setOpaque(false);
			// players panel
			{	JPanel playersPanel = makePlayersPanel();
				infoPanel.add(playersPanel);
			}
			infoPanel.add(Box.createHorizontalGlue());
			// right panel
			{	JPanel rightPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS); 
					rightPanel.setLayout(layout);
				}
				rightPanel.setOpaque(false);
				dim = new Dimension(GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH),GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT));
				rightPanel.setPreferredSize(dim);
				rightPanel.setMinimumSize(dim);
				rightPanel.setMaximumSize(dim);
				// notes panel
				{	JPanel notesPanel = makeNotesPanel();
					rightPanel.add(notesPanel);
				}
				//
				rightPanel.add(Box.createVerticalGlue());
				// notes panel
				{	JPanel pointsPanel = makePointsPanel();
					rightPanel.add(pointsPanel);
				}
				//
				rightPanel.add(Box.createVerticalGlue());
				// limit panel
				{	JPanel limitsPanel = makeLimitsPanel();
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
	
	private JPanel makePlayersPanel()
	{	Match match = getConfiguration().getCurrentMatch();
		int lines = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_NUMBER)+1;
		int cols = 2+1;
		int width = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH);
		int height = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT); 
		TablePanel playersPanel = new TablePanel(width,height,cols,lines,true,getConfiguration());
		// headers
		{	{	JLabel lbl = playersPanel.getLabel(0,0);
				lbl.setText(null);
				lbl.setPreferredSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
				lbl.setMaximumSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
				lbl.setMinimumSize(new Dimension(GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_LINE_HEIGHT),GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT)));
				lbl.setOpaque(false);
			}
			BufferedImage[] icons = 
			{	GuiTools.getIcon(GuiTools.GAME_MATCH_HEADER_NAME),
				GuiTools.getIcon(GuiTools.GAME_MATCH_HEADER_RANK)
			};
			String[] tooltips = 
			{	getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NAME+"Tooltip"),
				getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_RANK+"Tooltip")
			};
			for(int i=0;i<icons.length;i++)
			{	JLabel lbl = playersPanel.getLabel(0,1+i);
				lbl.setText(null);
				lbl.setToolTipText(tooltips[i]);
				int lineHeight = GuiTools.getSize(GuiTools.GAME_RESULTS_LABEL_HEADER_HEIGHT);
				double zoom = lineHeight/(double)icons[i].getHeight();
					icons[i] = ImageTools.resize(icons[i],zoom,true);
				ImageIcon icon = new ImageIcon(icons[i]);
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
	
	private JPanel makeNotesPanel()
	{	// init
		int width = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH);
		int height = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_HEIGHT);
		EntitledSubPanel notesPanel = new EntitledSubPanel(width,height,getConfiguration());
		// title
		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NOTES);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_NOTES+"Tooltip");
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
/*
			textPane.setCaret(new Caret()
	        {	public void install(JTextComponent c){}
	            public void deinstall(JTextComponent c){}
	            public void paint(Graphics g){}
	            public void addChangeListener(ChangeListener l){}
	            public void removeChangeListener(ChangeListener l){}
	            public boolean isVisible(){return false;}
	            public void setVisible(boolean v){}
	            public boolean isSelectionVisible(){return false;}
	            public void setSelectionVisible(boolean v){}
	            public void setMagicCaretPosition(Point p){}
	            public Point getMagicCaretPosition(){return new Point(0,0);}
	            public void setBlinkRate(int rate){}
	            public int getBlinkRate(){return 10000;}
	            public int getDot(){return 0;}
	            public int getMark(){return 0;}
	            public void setDot(int dot){}
	            public void moveDot(int dot){}
	        });
*/				        
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
	
	private JPanel makePointsPanel()
	{	// init
		int width = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH);
		int height = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_HEIGHT);
		EntitledSubPanel pointsPanel = new EntitledSubPanel(width,height,getConfiguration());
		// title
		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_POINTSPROCESS);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_POINTSPROCESS+"Tooltip");
		pointsPanel.setTitle(text,tooltip);
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
			SimpleAttributeSet sa = new SimpleAttributeSet();
			StyleConstants.setAlignment(sa,StyleConstants.ALIGN_JUSTIFIED);
			Font font = getConfiguration().getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE));
			StyleConstants.setFontFamily(sa, font.getFamily());
			StyleConstants.setFontSize(sa, font.getSize());
			StyledDocument doc = textPane.getStyledDocument();
			PointProcessor pp = getConfiguration().getCurrentMatch().getPointProcessor();			
			text = "";
			ArrayList<String> list = pp.getNotes();
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
			JComponent dataComp = pointsPanel.getDataPanel();
			textPane.setBackground(dataComp.getBackground());
			Dimension dim = dataComp.getPreferredSize();
			textPane.setPreferredSize(dim);
			textPane.setMinimumSize(dim);
			textPane.setMaximumSize(dim);
			textPane.setAlignmentX(Component.CENTER_ALIGNMENT);
			pointsPanel.setDataPanel(textPane);
		}
		return pointsPanel;		
	}
	
	private JPanel makeLimitsPanel()
	{	// init
		int width = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_WIDTH);
		int height = GuiTools.getSize(GuiTools.GAME_DESCRIPTION_PANEL_HEIGHT);
		EntitledSubPanel limitsPanel = new EntitledSubPanel(width,height,getConfiguration());
		// title
		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_LIMITS);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_MATCH_HEADER_LIMITS+"Tooltip");
		limitsPanel.setTitle(text,tooltip);
		// table
		{	// init
			int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
			limitsPanel.remove(0);
			limitsPanel.add(Box.createRigidArea(new Dimension(margin,margin)),0);
			int titleHeight = limitsPanel.getComponent(1).getPreferredSize().height;
			height = height-margin-titleHeight;
			int columns = 4;
			int lines = 4;
			TablePanel tablePanel = new TablePanel(width,height,columns,lines,false,getConfiguration());
			tablePanel.setOpaque(false);
			int lineHeight = (height-margin*(lines+1))/columns;
			// empty
			for(int line=0;line<lines;line++)
			{	for(int col=0;col<columns;col=col+2)
				{	// icon
					JLabel lbl = tablePanel.getLabel(line,col);
					lbl.setText(null);
					lbl.setPreferredSize(new Dimension(lineHeight,lineHeight));
					lbl.setMaximumSize(new Dimension(lineHeight,lineHeight));
					lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
					// text
					lbl = tablePanel.getLabel(line,col+1);
					lbl.setText(null);
					lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
					lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE,lineHeight));
				}
			}
			// data
			{	Match match = getConfiguration().getCurrentMatch();
				Iterator<Limit> i = match.getLimits().iterator();
				int k = 0;
				while(i.hasNext() && k<2*lines)
				{	// init
					Limit limit = i.next();
					String iconName = null;
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMinimumFractionDigits(0);
					String value = null;
					if(limit instanceof LimitConfrontation)
					{	LimitConfrontation l = (LimitConfrontation)limit;
						iconName = GuiTools.GAME_MATCH_LIMIT_CONFRONTATIONS;
						value = nf.format(l.getLimit());
					}
					else if(limit instanceof LimitPoints)
					{	LimitPoints l = (LimitPoints)limit;
						iconName = GuiTools.GAME_MATCH_LIMIT_POINTS;
						value = nf.format(l.getLimit());
					}
					else if(limit instanceof LimitTotal)
					{	LimitTotal l = (LimitTotal)limit;
						iconName = GuiTools.GAME_MATCH_LIMIT_TOTAL;
						value = nf.format(l.getLimit());
					}
					else if(limit instanceof LimitScore)
					{	LimitScore l = (LimitScore) limit;
						switch(l.getScore())
						{	case BOMBS:
								iconName = GuiTools.GAME_MATCH_LIMIT_BOMBS;
								value = nf.format(l.getLimit());
								break;
							case DEATHS:
								iconName = GuiTools.GAME_MATCH_LIMIT_DEATHS;
								value = nf.format(l.getLimit());
								break;
							case ITEMS:
								iconName = GuiTools.GAME_MATCH_LIMIT_ITEMS;
								value = nf.format(l.getLimit());
								break;
							case KILLS:
								iconName = GuiTools.GAME_MATCH_LIMIT_KILLS;
								value = nf.format(l.getLimit());
								break;
						}
					}
					tooltip = getConfiguration().getLanguage().getText(iconName+"Tooltip");
					// icon
					{	BufferedImage icon = GuiTools.getIcon(iconName);
						JLabel lbl = tablePanel.getLabel(k%lines,(k/lines)*2+0);
						lbl.setText(null);
						lbl.setToolTipText(tooltip);
						double zoom = lineHeight/(double)icon.getHeight();
						icon = ImageTools.resize(icon,zoom,true);
						ImageIcon ic = new ImageIcon(icon);
						lbl.setIcon(ic);
						Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
						lbl.setBackground(bg);
					}
					// value
					{	JLabel lbl = tablePanel.getLabel(k%lines,(k/lines)*2+1);
						lbl.setText(value);
						lbl.setToolTipText(tooltip);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						lbl.setBackground(bg);
					}	
					k++;
				}
			}
			limitsPanel.setDataPanel(tablePanel);
		}
		return limitsPanel;
	}
}
