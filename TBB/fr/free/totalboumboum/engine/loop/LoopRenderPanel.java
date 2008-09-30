package fr.free.totalboumboum.engine.loop;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public interface LoopRenderPanel
{
	public void paintScreen();
	public void addKeyListener(KeyListener listener);
	public void removeKeyListener(KeyListener listener);
	public void loopOver();
	public void playerOut(int index);
}
