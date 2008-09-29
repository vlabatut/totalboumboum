package fr.free.totalboumboum.gui.game.round.description;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.Map.Entry;

import fr.free.totalboumboum.engine.container.level.LevelDescription;
import fr.free.totalboumboum.engine.container.level.LevelLoader;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewer;
import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.LimitConfrontation;
import fr.free.totalboumboum.game.limit.LimitPoints;
import fr.free.totalboumboum.game.limit.LimitScore;
import fr.free.totalboumboum.game.limit.LimitTotal;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
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
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class RoundDescription extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public RoundDescription(SplitMenuPanel container)
	{	super(container);
	
		// title
		String txt = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_TITLE_DESCRIPTION);
		setTitle(txt);
	
		// data
		{	Round round = getConfiguration().getCurrentRound();
			LevelDescription levelDescription = round.getLevelDescription();
			LevelPreview preview = null;
			try
			{	preview = LevelPreviewer.previewLevel(levelDescription.getPath());
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			JPanel infoPanel = new JPanel();
			{	BoxLayout layout = new BoxLayout(infoPanel,BoxLayout.LINE_AXIS); 
				infoPanel.setLayout(layout);
			}
			int width = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_WIDTH);
			int height = GuiTools.getSize(GuiTools.GAME_DATA_PANEL_HEIGHT);
			int margin = GuiTools.getSize(GuiTools.GAME_DATA_MARGIN_SIZE);
			int leftWidth = (int)(width*0.4); 
			int rightWidth = width - leftWidth - margin; 
			Dimension dim = new Dimension(width,height);
			infoPanel.setPreferredSize(dim);
			infoPanel.setMinimumSize(dim);
			infoPanel.setMaximumSize(dim);
			infoPanel.setOpaque(false);
			// left panel
			{	JPanel leftPanel = new JPanel();
				{	BoxLayout layout = new BoxLayout(leftPanel,BoxLayout.PAGE_AXIS); 
					leftPanel.setLayout(layout);
				}
				leftPanel.setOpaque(false);
				dim = new Dimension(leftWidth,height);
				leftPanel.setPreferredSize(dim);
				leftPanel.setMinimumSize(dim);
				leftPanel.setMaximumSize(dim);
				// preview label
				{	int innerHeight = leftWidth;
					JLabel previewLabel = makePreviewLabel(leftWidth,innerHeight,preview);
					previewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(previewLabel);
				}
				//
				leftPanel.add(Box.createVerticalGlue());
				// itemset panel
				{	int innerHeight = height - leftWidth - margin;
					JPanel itemsetPanel = makeItemsetPanel(leftWidth,innerHeight);
					itemsetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
					leftPanel.add(itemsetPanel);
				}
				//
				infoPanel.add(leftPanel);
			}
			//
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
				// up panel
				{	JPanel upPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(upPanel,BoxLayout.LINE_AXIS); 
						upPanel.setLayout(layout);
					}
					upPanel.setOpaque(false);
					dim = new Dimension(rightWidth,upHeight);
					upPanel.setPreferredSize(dim);
					upPanel.setMinimumSize(dim);
					upPanel.setMaximumSize(dim);
					int innerWidth = (rightWidth - margin)/2;
					// misc panel
					{	JPanel miscPanel = makeMiscPanel(innerWidth,upHeight);
						upPanel.add(miscPanel);
					}
					upPanel.add(Box.createHorizontalGlue());
					// initial items panel
					{	JPanel initialItemsPanel = makeInitialItemsPanel(innerWidth,upHeight,preview);
						upPanel.add(initialItemsPanel);
					}
					rightPanel.add(upPanel);
				}
				//
				rightPanel.add(Box.createVerticalGlue());
				// down panel
				{	JPanel downPanel = new JPanel();
					{	BoxLayout layout = new BoxLayout(downPanel,BoxLayout.LINE_AXIS); 
						downPanel.setLayout(layout);
					}
					downPanel.setOpaque(false);
					dim = new Dimension(rightWidth,upHeight);
					downPanel.setPreferredSize(dim);
					downPanel.setMinimumSize(dim);
					downPanel.setMaximumSize(dim);
					int innerWidth = (rightWidth - margin)/2;
					// points panel
					{	JPanel pointsPanel = makePointsPanel(innerWidth,downHeight);
						downPanel.add(pointsPanel);
					}
					downPanel.add(Box.createHorizontalGlue());
					// limits panel
					{	JPanel limitsPanel = makeLimitsPanel(innerWidth,downHeight);
						downPanel.add(limitsPanel);
					}
					rightPanel.add(downPanel);
				}
				//
				infoPanel.add(rightPanel);
			}
			//
			setDataPanel(infoPanel);
		}
	}

	private JLabel makePreviewLabel(int width, int height, LevelPreview preview)
	{	// init
		String txt = "No preview";
		JLabel result = new JLabel(txt);
		Dimension dim = new Dimension(width,height);
		result.setPreferredSize(dim);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setVerticalAlignment(SwingConstants.CENTER);
		result.setFont(getConfiguration().getFont().deriveFont((float)GuiTools.getSize(GuiTools.GAME_RESULTS_HEADER_FONT_SIZE)));
		result.setBackground(GuiTools.COLOR_TABLE_HEADER_BACKGROUND);
		result.setForeground(GuiTools.COLOR_TABLE_HEADER_FOREGROUND);
		result.setOpaque(true);
		// image
		BufferedImage img = preview.getVisualPreview(); 
		if(img!=null)
		{	float zoomX = width/(float)img.getWidth();
			float zoomY = height/(float)img.getHeight();
			float zoom = Math.min(zoomX,zoomY);
			img = ImageTools.resize(img,zoom,true);
			ImageIcon icon = new ImageIcon(img);
			result.setIcon(icon);
			result.setText(null);
		}
		//
		return result;
	}
	
	private JPanel makeItemsetPanel(int width, int height)
	{	JPanel result = new JPanel();
		Dimension dim = new Dimension(width,height);
		result.setPreferredSize(dim);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		return result;
	}

	private JPanel makeMiscPanel(int width, int height)
	{	JPanel result = new JPanel();
		Dimension dim = new Dimension(width,height);
		result.setPreferredSize(dim);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		return result;
	}

	private JPanel makeInitialItemsPanel(int width, int height, LevelPreview levelPreview)
	{	// init
		EntitledSubPanel itemsPanel = new EntitledSubPanel(width,height,getConfiguration());
		HashMap<String,BufferedImage> itemsetPreview = levelPreview.getItemsetPreview();
		HashMap<String,Integer> initialItems = levelPreview.getInitialItems();
		// title
//		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_INITIAL_ITEMS);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_INITIAL_ITEMS+"Tooltip");
//		itemsPanel.setTitle(text,tooltip);
		BufferedImage hd = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_INITIAL_ITEMS);
		itemsPanel.setTitle(hd, tooltip);
		// table
		{	// init
			int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
			itemsPanel.remove(0);
			itemsPanel.add(Box.createRigidArea(new Dimension(margin,margin)),0);
			int titleHeight = itemsPanel.getComponent(1).getPreferredSize().height;
			height = height-margin-titleHeight;
			int columnGroups = 2;
			int lines = 4;
			if(initialItems.size()>columnGroups*lines)
			{	lines = 8;
				columnGroups = 4;
			}
			int subColumns = 2;
			int columns = columnGroups*subColumns;
			TablePanel tablePanel = new TablePanel(width,height,columns,lines,false,getConfiguration());
			tablePanel.setOpaque(false);
			int lineHeight = (height-margin*(lines+1))/lines;
			// empty
			for(int line=0;line<lines;line++)
			{	for(int col=0;col<columns;col=col+subColumns)
				{	// icon
					JLabel lbl = tablePanel.getLabel(line,col+0);
					lbl.setText(null);
					lbl.setPreferredSize(new Dimension(lineHeight,lineHeight));
					lbl.setMaximumSize(new Dimension(lineHeight,lineHeight));
					lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
					// text
					lbl = tablePanel.getLabel(line,col+1);
					lbl.setText(null);
					lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
					int maxWidth = (width - margin*(columns+1) - columnGroups*lineHeight)/columnGroups;
					lbl.setMaximumSize(new Dimension(maxWidth,lineHeight));
				}
			}
			// data
			{	Iterator<Entry<String,Integer>> i = initialItems.entrySet().iterator();
				int k = 0;
				while(i.hasNext() && k<columnGroups*lines)
				{	// init
					Entry<String,Integer> temp = i.next();
					String name = temp.getKey();
					int number = temp.getValue();
					BufferedImage image = itemsetPreview.get(name);
					tooltip = name+": "+number;
					int baseCol = (k/lines)*subColumns;
					int baseLine = k%lines;
					// icon
					{	JLabel lbl = tablePanel.getLabel(baseLine,baseCol+0);
						lbl.setText(null);
						lbl.setToolTipText(tooltip);
						float zoomX = lineHeight/(float)image.getWidth();
						float zoomY = lineHeight/(float)image.getHeight();
						float zoom = Math.min(zoomX,zoomY);
						image = ImageTools.resize(image,zoom,true);
						ImageIcon ic = new ImageIcon(image);
						lbl.setIcon(ic);
						Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
						lbl.setBackground(bg);
					}
					// value
					{	JLabel lbl = tablePanel.getLabel(baseLine,baseCol+1);
						lbl.setText(Integer.toString(number));
						lbl.setToolTipText(tooltip);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						lbl.setBackground(bg);
					}	
					k++;
				}
			}
			itemsPanel.setDataPanel(tablePanel);
		}
		return itemsPanel;
	}

	private JPanel makePointsPanel(int width, int height)
	{	JPanel result = new JPanel();
		Dimension dim = new Dimension(width,height);
		result.setPreferredSize(dim);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		return result;
	}

	private JPanel makeLimitsPanel(int width, int height)
	{	// init
		EntitledSubPanel limitsPanel = new EntitledSubPanel(width,height,getConfiguration());
		// title
//		String text = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_LIMITS);
		String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_LIMITS+"Tooltip");
//		limitsPanel.setTitle(text,tooltip);
		BufferedImage hd = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_LIMITS);
		limitsPanel.setTitle(hd,tooltip);
		// table
		{	// init
			Match match = getConfiguration().getCurrentMatch();
			Limits<MatchLimit> limitsList = match.getLimits();
			int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
			limitsPanel.remove(0);
			limitsPanel.add(Box.createRigidArea(new Dimension(margin,margin)),0);
			int titleHeight = limitsPanel.getComponent(1).getPreferredSize().height;
			height = height-margin-titleHeight;
			int columnGroups = 2;
			int lines = 4;
			if(limitsList.size()>columnGroups*lines)
			{	lines = 8;
				columnGroups = 4;
			}
			int subColumns = 2;
			int columns = columnGroups*subColumns;
			TablePanel tablePanel = new TablePanel(width,height,columns,lines,false,getConfiguration());
			tablePanel.setOpaque(false);
			int lineHeight = (height-margin*(lines+1))/lines;
			// empty
			for(int line=0;line<lines;line++)
			{	for(int col=0;col<columns;col=col+subColumns)
				{	// icon
					JLabel lbl = tablePanel.getLabel(line,col+0);
					lbl.setText(null);
					lbl.setPreferredSize(new Dimension(lineHeight,lineHeight));
					lbl.setMaximumSize(new Dimension(lineHeight,lineHeight));
					lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
					// text
					lbl = tablePanel.getLabel(line,col+1);
					lbl.setText(null);
					lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
					int maxWidth = (width - margin*(columns+1) - columnGroups*lineHeight)/columnGroups;
					lbl.setMaximumSize(new Dimension(maxWidth,lineHeight));
				}
			}
			// data
			{	Iterator<MatchLimit> i = limitsList.iterator();
				int k = 0;
				while(i.hasNext() && k<columnGroups*lines)
				{	// init
					Limit limit = i.next();
					int baseCol = (k/lines)*subColumns;
					int baseLine = k%lines;
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
							case CROWNS:
								iconName = GuiTools.GAME_MATCH_LIMIT_CROWNS;
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
							case PAINTINGS:
								iconName = GuiTools.GAME_MATCH_LIMIT_PAINTINGS;
								value = nf.format(l.getLimit());
								break;
							case TIME:
								iconName = GuiTools.GAME_MATCH_LIMIT_TIME;
								value = nf.format(l.getLimit());
								break;
						}
					}
					tooltip = getConfiguration().getLanguage().getText(iconName+"Tooltip");
					// icon
					{	BufferedImage icon = GuiTools.getIcon(iconName);
						JLabel lbl = tablePanel.getLabel(baseLine,baseCol+0);
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
					{	JLabel lbl = tablePanel.getLabel(baseLine,baseCol+1);
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

	@Override
	public void refresh()
	{	// nothing to do here
	}

	@Override
	public void updateData()
	{	// nothing to do here
	}
}