package fr.free.totalboumboum.gui.common.structure.subpanel.archive;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;

public interface SubPanelTable
{
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void reinit(int columns, int lines);
	public void reinit(int colGroups, int colSubs, int lines);

	/////////////////////////////////////////////////////////////////
	// SIZE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getLineFontSize();
	public int getHeaderFontSize();
	public int getHeaderHeight();
	public int getLineHeight();
	
	/////////////////////////////////////////////////////////////////
	// COLUMNS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setColSubMinWidth(int colSub, int width);
	public void setColSubPreferredWidth(int colSub, int width);
	public void setColSubMaxWidth(int colSub, int width);
	public void unsetColSubMinWidth(int colSub);
	public void unsetColSubPreferredWidth(int colSub);
	public void unsetColSubMaxWidth(int colSub);
	public ArrayList<JLabel> getColumn(int col);
	public ArrayList<JLabel> getColSub(int colGroup, int colSub);
	public int getColumnCount();
	public int getColGroupCount();
	public int getColSubCount();
	public void addColumn(int index);
	public void addColSub(int subIndex);
	public void addColGroup(int groupIndex);
	public void setColumnKeys(int col, ArrayList<String> keys, ArrayList<Boolean> imageFlags);
	public void setColumnKeys(int colGroup, int colSub, ArrayList<String> keys, ArrayList<Boolean> imageFlags);
	public void setColumnIcons(int col, ArrayList<BufferedImage> icons, ArrayList<String> tooltips);
	public void setColumnIcons(int colGroup, int colSub, ArrayList<BufferedImage> icons, ArrayList<String> tooltips);
	public void setColumnTexts(int col, ArrayList<String> texts, ArrayList<String> tooltips);
	public void setColumnTexts(int colGroup, int colSub, ArrayList<String> texts, ArrayList<String> tooltips);
	public void setColumnBackground(int col, Color bg);
	public void setColumnBackground(int colGroup, int colSub, Color bg);
	public void setColumnForeground(int col, Color fg);
	public void setColumnForeground(int colGroup, int colSub, Color fg);

	/////////////////////////////////////////////////////////////////
	// LINES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getLineCount();
	public ArrayList<JLabel> getLine(int index);
	public void addLine(int index);
	public void setLineKeysSimple(int line, ArrayList<String> keys, ArrayList<Boolean> imageFlags);
	public void setLineKeys(int line, ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags);
	public void setLineIconsSimple(int line, ArrayList<BufferedImage> icons, ArrayList<String> tooltips);
	public void setLineIcons(int line, ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips);
	public void setLineTextsSimple(int line, ArrayList<String> texts, ArrayList<String> tooltips);
	public void setLineTexts(int line, ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips);
	public void setLineBackground(int line, Color bg);
	public void setLineForeground(int line, Color fg);

	/////////////////////////////////////////////////////////////////
	// LABELS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public JLabel getLabel(int line, int col);
	public JLabel getLabel(int line, int colGroup, int colSub);
	public void setLabelKey(int line, int col, String key, boolean imageFlag);
	public void setLabelKey(int line, int colGroup, int colSub, String key, boolean imageFlag);
	public void setLabelIcon(int line, int col, BufferedImage icon, String tooltip);
	public void setLabelIcon(int line, int colGroup, int colSub, BufferedImage icon, String tooltip);
	public void setLabelText(int line, int col, String text, String tooltip);
	public void setLabelText(int line, int colGroup, int colSub, String text, String tooltip);
	public void setLabelBackground(int line, int col, Color bg);
	public void setLabelBackground(int line, int colGroup, int colSub, Color bg);
	public void setLabelForeground(int line, int col, Color fg);
	public void setLabelForeground(int line, int colGroup, int colSub, Color fg);
	public int[] getLabelPositionSimple(JLabel label);
	public int[] getLabelPosition(JLabel label);
	
	/////////////////////////////////////////////////////////////////
	// CONTENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setContentBySubLineKeys(ArrayList<ArrayList<String>> keys, ArrayList<ArrayList<Boolean>> imageFlags, boolean wholeLine);
	public void setContentBySubLineIcon(ArrayList<ArrayList<BufferedImage>> icons, ArrayList<ArrayList<String>> tooltips, boolean wholeLine);
	public void setContentBySubLineText(ArrayList<ArrayList<String>> texts, ArrayList<ArrayList<String>> tooltips, boolean wholeLine);
}
