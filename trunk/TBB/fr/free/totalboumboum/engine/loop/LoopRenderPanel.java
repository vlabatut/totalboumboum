package fr.free.totalboumboum.engine.loop;

import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public interface LoopRenderPanel
{
	public void paintScreen();
	public BufferedImage getBackgroundImage();
	public void addKeyListener(KeyListener listener);
	public void removeKeyListener(KeyListener listener);
	public void loopOver();
	public void playerOut(int index);
}
