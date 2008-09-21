package fr.free.totalboumboum.gui.generic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.free.totalboumboum.gui.tools.SwingTools;

public abstract class EntitledDataPanel extends InnerDataPanel
{	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JPanel data;
	
	public EntitledDataPanel(SplitMenuPanel container)
	{	super(container);

		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.PAGE_AXIS); 
		setLayout(layout);

		// background
		//setBackground(new Color(50,50,50));
		setOpaque(false);
		
		// size
		int height = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT);
		int width = SwingTools.getSize(SwingTools.HORIZONTAL_SPLIT_DATA_PANEL_WIDTH);
		setPreferredSize(new Dimension(width,height));
		
		add(Box.createVerticalGlue());
	
		// title label
		{	String text = "N/A";
			title = new JLabel(text);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			Font font = getConfiguration().getFont().deriveFont((float)SwingTools.getSize(SwingTools.GAME_TITLE_FONT_SIZE));
			title.setForeground(SwingTools.COLOR_TITLE_FOREGROUND);
			title.setBackground(SwingTools.COLOR_COMMON_BACKGROUND);
			title.setOpaque(true);
			title.setFont(font);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_LABEL_TITLE_HEIGHT));
			title.setPreferredSize(dim);
			title.setMinimumSize(dim);
			title.setMaximumSize(dim);
			add(title);
		}
	
		add(Box.createVerticalGlue());
		
		// data panel
		{	data = new JPanel();
			data.setBackground(SwingTools.COLOR_COMMON_BACKGROUND);
			Dimension dim = new Dimension(SwingTools.getSize(SwingTools.GAME_DATA_PANEL_WIDTH),SwingTools.getSize(SwingTools.GAME_DATA_PANEL_HEIGHT));
			data.setPreferredSize(dim);
			data.setMinimumSize(dim);
			data.setMaximumSize(dim);
			add(data);
		}
		
		add(Box.createVerticalGlue());
	}	

	public void setTitle(String text)
	{	title.setText(text);
//		revalidate();
//		repaint();
	}
	
	public void setDataPanel(JPanel panel)
	{	remove(data);
		data = panel;	
		add(data,3);
	}
	
	public JPanel getDataPanel()
	{	return data;	
	}
}
