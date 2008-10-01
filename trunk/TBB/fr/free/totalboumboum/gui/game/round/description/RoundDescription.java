package fr.free.totalboumboum.gui.game.round.description;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
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

import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.LevelPreview;
import fr.free.totalboumboum.engine.container.level.LevelPreviewer;
import fr.free.totalboumboum.engine.container.zone.Zone;
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
import fr.free.totalboumboum.game.points.PointsDiscretize;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsRankings;
import fr.free.totalboumboum.game.points.PointsRankpoints;
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
import fr.free.totalboumboum.tools.StringTools;

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
			HollowLevel hollowLevel = round.getHollowLevel();
			LevelPreview preview = null;
			try
			{	preview = LevelPreviewer.previewLevel(hollowLevel.getLevelFolder());
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
					JPanel itemsetPanel = makeItemsetPanel(leftWidth,innerHeight,preview);
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
					{	JPanel miscPanel = makeMiscPanel(innerWidth,upHeight,preview);
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

	private JLabel makePreviewLabel(int width, int height, LevelPreview levelPreview)
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
		BufferedImage img = levelPreview.getVisualPreview(); 
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
	
	private JPanel makeItemsetPanel(int width, int height, LevelPreview levelPreview)
	{	// init
		String id = GuiTools.GAME_ROUND_HEADER_ITEMSET;
		int colGrps[] = {5, 6};
		int lns[] = {4, 5};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		HashMap<String,BufferedImage> itemsetPreview = levelPreview.getItemsetPreview();
		Zone zone = getConfiguration().getCurrentRound().getHollowLevel().getZone();
		HashMap<String,Integer> itemList = zone.getItemCount();
		Iterator<Entry<String,BufferedImage>> i = itemsetPreview.entrySet().iterator();
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
			Entry<String,BufferedImage> temp = i.next();
			String name = temp.getKey();
			BufferedImage image = temp.getValue();
			int number = 0;
			if(itemList.containsKey(name))
				number = itemList.get(name);
			String tooltip;
			tooltip = name+": "+number;				
			if(number==0)
			{	BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null); 
				image = op.filter(image,null);
			}
			// lists
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			dt.add(image);
			tt.add(tooltip);
			String value = Integer.toString(number);
			dt.add(value);
			tt.add(tooltip);			
		}			

		EntitledSubPanel itemsetPanel = makeSubTable(width,height,id,colGrps,lns,data,tooltips);
		return itemsetPanel;
	}

	private JPanel makeMiscPanel(int width, int height, LevelPreview preview)
	{	// init
		String id = GuiTools.GAME_ROUND_HEADER_MISC;
		int colGrps[] = {1};
		int lns[] = {8};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		String names[] = 
		{	GuiTools.GAME_ROUND_HEADER_PACK,
			GuiTools.GAME_ROUND_HEADER_TITLE,
			GuiTools.GAME_ROUND_HEADER_SOURCE,
			GuiTools.GAME_ROUND_HEADER_AUTHOR,
			GuiTools.GAME_ROUND_HEADER_INSTANCE,
			GuiTools.GAME_ROUND_HEADER_THEME,
			GuiTools.GAME_ROUND_HEADER_DIMENSION
		};
		Round round = getConfiguration().getCurrentRound();
		HollowLevel hollowLevel = round.getHollowLevel();
		String texts[] = 
		{	hollowLevel.getPackName(),
			preview.getTitle(),
			preview.getSource(),
			preview.getAuthor(),
			hollowLevel.getInstanceName(),
			hollowLevel.getThemeName(),
			Integer.toString(hollowLevel.getVisibleHeight())+new Character('\u00D7').toString()+Integer.toString(hollowLevel.getVisibleHeight())
		};
		for(int i=0;i<names.length;i++)
		{	// lists
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			BufferedImage image = GuiTools.getIcon(names[i]);
			dt.add(image);
			String tooltip = getConfiguration().getLanguage().getText(names[i]+"Tooltip");
			tt.add(tooltip);
			dt.add(texts[i]);
			tt.add(texts[i]);
		}		
		
		EntitledSubPanel miscPanel = makeSubTable(width,height,id,colGrps,lns,data,tooltips);
		return miscPanel;
	}

	private JPanel makeInitialItemsPanel(int width, int height, LevelPreview levelPreview)
	{	// init
		String id = GuiTools.GAME_ROUND_HEADER_INITIAL_ITEMS;
		int colGrps[] = {2, 4};
		int lns[] = {4, 8};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		HashMap<String,BufferedImage> itemsetPreview = levelPreview.getItemsetPreview();
		HashMap<String,Integer> initialItems = levelPreview.getInitialItems();
		Iterator<Entry<String,Integer>> i = initialItems.entrySet().iterator();
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
			Entry<String,Integer> temp = i.next();
			String name = temp.getKey();
			int number = temp.getValue();
			BufferedImage image = itemsetPreview.get(name);
			String tooltip = name+": "+number;
			// lists
			ArrayList<Object> dt = new ArrayList<Object>();
			data.add(dt);
			ArrayList<String> tt = new ArrayList<String>();
			tooltips.add(tt);
			// data
			dt.add(image);
			tt.add(tooltip);
			String value = Integer.toString(number);
			dt.add(value);
			tt.add(tooltip);			
		}			
		
		// result
		EntitledSubPanel itemsPanel = makeSubTable(width,height,id,colGrps,lns,data,tooltips);
		return itemsPanel;
	}

	private JPanel makePointsPanel(int width, int height)
	{	// init
		String id = GuiTools.GAME_ROUND_HEADER_POINTSPROCESS;
		int colGrps[] = {1, 1};
		int lns[] = {8, 12};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		Round round = getConfiguration().getCurrentRound();
		PointsProcessor mainPP = round.getPointProcessor();
		makePointsPanelRec(mainPP,data,tooltips);

		EntitledSubPanel pointsPanel = makeSubTable(width,height,id,colGrps,lns,data,tooltips);
		return pointsPanel;
	}
	public void makePointsPanelRec(PointsProcessor pp, ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	// format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);
		
		// rankpoints
		if(pp instanceof PointsRankpoints)
		{	PointsRankpoints pr = (PointsRankpoints) pp;
			// this pp
			{	ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				String text = "Rp";
				String tooltip = "Rankpoints: calculates points according to a ranking";
				dt.add(text);
				tt.add(tooltip);
				boolean exaequoShare = pr.getExaequoShare();
				if(exaequoShare)
				{	text = "share";
					tooltip = "In case of tie, points are shared";
				}
				else
				{	text = "don't share";
					tooltip = "In case of tie, points are not shared";
				}
				dt.add(text);
				tt.add(tooltip);
			}
			// source
			{	PointsRankings prk = pr.getSource();
				ArrayList<PointsProcessor> sources = prk.getSources();
				Iterator<PointsProcessor> i = sources.iterator();
				while(i.hasNext())
				{	PointsProcessor source = i.next();
					makePointsPanelRec(source,data,tooltips);
				}
			}
			// values
			{	float[] values = pr.getValues();
				for(int i=0;i<values.length;i++)
				{	String nbr = "#"+(i+1); 
					String value = nf.format(values[i]);
					String tooltip = nbr+new Character('\u2192').toString()+value;
					ArrayList<Object> dt = new ArrayList<Object>();
					ArrayList<String> tt = new ArrayList<String>();
					data.add(dt);
					tooltips.add(tt);
					dt.add(nbr);
					tt.add(tooltip);
					dt.add(value);
					tt.add(tooltip);			
				}
			}
		}
		// discretize
		else if(pp instanceof PointsDiscretize)
		{	PointsDiscretize pd = (PointsDiscretize) pp;
			// this pp
			{	ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				String text = "D";
				String tooltip = "Discretize: calculates points according to some intervals";
				dt.add(text);
				tt.add(tooltip);				
				text = "";
				dt.add(text);
				tt.add(tooltip);				
			}
			// source
			{	PointsProcessor source = pd.getSource();
				makePointsPanelRec(source,data,tooltips);
			}
			// values & thresholds
			{	float[] values = pd.getValues();
				float[] thresholds = pd.getThresholds();
				String previousThreshold;
				String threshold = new Character('\u2212').toString()+new Character('\u221E').toString();
				for(int i=0;i<values.length;i++)
				{	String value = nf.format(values[i]);
					previousThreshold = threshold; 
					threshold = nf.format(thresholds[i]);
					String interval;
					interval = "]"+previousThreshold+";"+threshold+"]";
					String tooltip = interval+"->"+value;
					ArrayList<Object> dt = new ArrayList<Object>();
					ArrayList<String> tt = new ArrayList<String>();
					data.add(dt);
					tooltips.add(tt);
					dt.add(interval);
					tt.add(tooltip);
					dt.add(value);
					tt.add(tooltip);			
				}
			}
		}
		// rankings
		else if(pp instanceof PointsRankings)
		{	PointsRankings pr = (PointsRankings) pp;
			// this PP
			{	boolean inverted = pr.isInverted();
				ArrayList<Object> dt = new ArrayList<Object>();
				ArrayList<String> tt = new ArrayList<String>();
				data.add(dt);
				tooltips.add(tt);
				dt.add("Rk");
				tt.add("Rankings: caculates a ranking according to one or several points");
				String value,tooltip;
				if(inverted)
				{	value = "inverted";
					tooltip = "inverted order";
				}
				else
				{	value = "regular";
					tooltip = "regular order";
				}
				dt.add(value);
				tt.add(tooltip);
			}
			// sources
			{	ArrayList<PointsProcessor> sources = pr.getSources();
				Iterator<PointsProcessor> i = sources.iterator();
				while(i.hasNext())
				{	PointsProcessor source = i.next();
					makePointsPanelRec(source,data,tooltips);
				}
			}
		}
		// others
		else
		{	ArrayList<Object> dt = new ArrayList<Object>();
			ArrayList<String> tt = new ArrayList<String>();
			data.add(dt);
			tooltips.add(tt);
			// icon
			//BufferedImage icon = GuiTools.getIcon(GuiTools.GAME_ROUND_HEADER_PARTIAL);
			String icon = "-";
			String tooltip = getConfiguration().getLanguage().getText(GuiTools.GAME_ROUND_HEADER_PARTIAL+"Tooltip");
			dt.add(icon);
			tt.add(tooltip);
			// text
			String text = pp.toString();
			dt.add(text);
			tt.add(text);
		}
	}
	
	private JPanel makeLimitsPanel(int width, int height)
	{	// init
		String id = GuiTools.GAME_ROUND_HEADER_LIMITS;
		int colGrps[] = {1,2};
		int lns[] = {8, 8};
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<ArrayList<String>> tooltips = new ArrayList<ArrayList<String>>();
		
		// data
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		Round round = getConfiguration().getCurrentRound();
		Limits<RoundLimit> limitsList = round.getLimits();
		Iterator<RoundLimit> i = limitsList.iterator();
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
			if(limit instanceof LimitTime)
			{	LimitTime l = (LimitTime)limit;
				iconName = GuiTools.GAME_ROUND_LIMIT_TIME;
				value = StringTools.formatTimeWithSeconds(l.getLimit());
			}
			else if(limit instanceof LimitPoints)
			{	LimitPoints l = (LimitPoints)limit;
				iconName = GuiTools.GAME_ROUND_LIMIT_POINTS;
				value = nf.format(l.getLimit());
			}
			else if(limit instanceof LimitScore)
			{	LimitScore l = (LimitScore) limit;
				switch(l.getScore())
				{	case BOMBS:
						iconName = GuiTools.GAME_ROUND_LIMIT_BOMBS;
						value = nf.format(l.getLimit());
						break;
					case CROWNS:
						iconName = GuiTools.GAME_ROUND_LIMIT_CROWNS;
						value = nf.format(l.getLimit());
						break;
					case DEATHS:
						iconName = GuiTools.GAME_ROUND_LIMIT_DEATHS;
						value = nf.format(l.getLimit());
						break;
					case ITEMS:
						iconName = GuiTools.GAME_ROUND_LIMIT_ITEMS;
						value = nf.format(l.getLimit());
						break;
					case KILLS:
						iconName = GuiTools.GAME_ROUND_LIMIT_KILLS;
						value = nf.format(l.getLimit());
						break;
					case PAINTINGS:
						iconName = GuiTools.GAME_ROUND_LIMIT_PAINTINGS;
						value = nf.format(l.getLimit());
						break;
					case TIME:
						iconName = GuiTools.GAME_ROUND_LIMIT_TIME;
						value = nf.format(l.getLimit());
						break;
				}
			}
			// lists
			String tooltip = getConfiguration().getLanguage().getText(iconName+"Tooltip");
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
		EntitledSubPanel limitsPanel = makeSubTable(width,height,id,colGrps,lns,data,tooltips);
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

	private EntitledSubPanel makeSubTable(int width, int height, String id, int colGrps[], int lns[], ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips)
	{	EntitledSubPanel panel = new EntitledSubPanel(width,height,getConfiguration());
		
		// title
		String tooltip = getConfiguration().getLanguage().getText(id+"Tooltip");
		BufferedImage hd = GuiTools.getIcon(id);
		panel.setTitle(hd, tooltip);
		
		// init table
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		panel.remove(0);
		panel.add(Box.createRigidArea(new Dimension(margin,margin)),0);
		int titleHeight = panel.getComponent(1).getPreferredSize().height;
		height = height-margin-titleHeight;
		int columnGroups;
		int lines;
		int k = 0;
		do
		{	columnGroups = colGrps[k];
			lines = lns[k];
			k++;			
		}
		while(data.size()>columnGroups*lines && k<lns.length);
		int subColumns = data.get(0).size();
		int columns = columnGroups*subColumns;
		TablePanel tablePanel = new TablePanel(width,height,columns,lines,false,getConfiguration());
		tablePanel.setOpaque(false);
		int lineHeight = (height-margin*(lines+1))/lines;
		Graphics g = getFrame().getGraphics();
		float fontSize = GuiTools.getFontSize(lineHeight*0.8, getConfiguration(),g);
		Font regularFont = getConfiguration().getFont().deriveFont((float)fontSize);
		boolean firstIcon = data.get(0).get(0) instanceof BufferedImage;
		
		int maxWidth = (width - margin*(columns+1) - columnGroups*lineHeight)/columnGroups;
		// empty
		for(int line=0;line<lines;line++)
		{	for(int col=0;col<columns;col=col+subColumns)
			{	// icon
				JLabel lbl = tablePanel.getLabel(line,col+0);
				lbl.setFont(regularFont);
				lbl.setText(null);
				if(firstIcon)
				{	lbl.setPreferredSize(new Dimension(lineHeight,lineHeight));
					lbl.setMaximumSize(new Dimension(lineHeight,lineHeight));
				}
				else
					lbl.setMaximumSize(new Dimension(maxWidth,lineHeight));
				lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
				// text
				lbl = tablePanel.getLabel(line,col+1);
				lbl.setFont(regularFont);
				lbl.setText(null);
				lbl.setMaximumSize(new Dimension(maxWidth,lineHeight));
				lbl.setMinimumSize(new Dimension(lineHeight,lineHeight));
			}
		}
		
		// data
		{	Iterator<ArrayList<Object>> i = data.iterator();
			Iterator<ArrayList<String>> j = tooltips.iterator();
			k = 0;
			while(i.hasNext() && k<columnGroups*lines)
			{	// init
				ArrayList<Object> dat = i.next();
				ArrayList<String> tt = j.next();
				int baseCol = (k/lines)*subColumns;
				int baseLine = k%lines;
				k++;
				//
				Iterator<?> i2 = dat.iterator();
				Iterator<String> j2 = tt.iterator();
				int c = 0;
				while(i2.hasNext())
				{	JLabel lbl = tablePanel.getLabel(baseLine,baseCol+c);
					c++;
					Object o = i2.next();
					tooltip = j2.next();
					if(tooltip!=null)
						lbl.setToolTipText(tooltip);
					// icon
					if(o instanceof BufferedImage)
					{	BufferedImage image = (BufferedImage)o;
						lbl.setText(null);
						float zoomX = lineHeight/(float)image.getWidth();
						float zoomY = lineHeight/(float)image.getHeight();
						float zoom = Math.min(zoomX,zoomY);
						image = ImageTools.resize(image,zoom,true);
						ImageIcon ic = new ImageIcon(image);
						lbl.setIcon(ic);
						Color bg = GuiTools.COLOR_TABLE_HEADER_BACKGROUND;
						lbl.setBackground(bg);
					}
					else
					// value
					{	String txt = (String)o;
						lbl.setText(txt);
						Color bg = GuiTools.COLOR_TABLE_REGULAR_BACKGROUND;
						lbl.setBackground(bg);
					}	
				}
			}
		}
		panel.setDataPanel(tablePanel);
		return panel;
	}
}
