package org.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.awt.Font;
import java.awt.event.KeyListener;

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface LoopRenderPanel
{	public void paintScreen();

	public void loopOver();
	public void playerOut(int index);
	
	public void addKeyListener(KeyListener listener);
	public void removeKeyListener(KeyListener listener);
	
	public String getMessageTextReady();
	public String getMessageTextSet();
	public String getMessageTextGo();
	public Font getMessageFont(double width, double height);
}
