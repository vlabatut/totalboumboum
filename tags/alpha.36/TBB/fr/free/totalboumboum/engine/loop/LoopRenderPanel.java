package fr.free.totalboumboum.engine.loop;

import java.awt.Image;
import java.awt.event.KeyListener;

public interface LoopRenderPanel
{
	public void paintScreen();
	public Image getImage();
	public void addKeyListener(KeyListener listener);
	public void loopOver();
	public void playerOut(int index);
}
