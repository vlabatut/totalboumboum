package fr.free.totalboumboum.gui.generic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.tools.GuiTools;
import fr.free.totalboumboum.tools.ImageTools;

public class EntitledSubPanelTable extends EntitledSubPanel
{	private static final long serialVersionUID = 1L;

	public EntitledSubPanelTable(int width, int height, String id, int colGrps[], int lns[], ArrayList<ArrayList<Object>> data, ArrayList<ArrayList<String>> tooltips, GuiConfiguration configuration)
	{	super(width,height,configuration);

		// title
		String tooltip = configuration.getLanguage().getText(id+"Tooltip");
		BufferedImage hd = GuiTools.getIcon(id);
		setTitle(hd, tooltip);
		
		// init table
		int margin = GuiTools.getSize(GuiTools.GAME_RESULTS_MARGIN_SIZE);
		remove(0);
		add(Box.createRigidArea(new Dimension(margin,margin)),0);
		int titleHeight = getComponent(1).getPreferredSize().height;
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
		TablePanel tablePanel = new TablePanel(width,height,columns,lines,false,configuration);
		tablePanel.setOpaque(false);
		int lineHeight = (height-margin*(lines+1))/lines;
		float fontSize = GuiTools.getFontSize(lineHeight*0.8, configuration);
		Font regularFont = configuration.getFont().deriveFont((float)fontSize);
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
		setDataPanel(tablePanel);	
	}
}