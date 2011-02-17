package org.totalboumboum.gui.tools;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiVideoTools
{
	/**
	 * defines the list of all available graphic resolutions on the
	 * current default display, sorted by growing order
	 * @return
	 */
	public static TreeSet<Dimension> getAvailableResolutions()
	{	GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphEnv.getDefaultScreenDevice();
		DisplayMode modes[] = device.getDisplayModes();
		TreeSet<Dimension> result = new TreeSet<Dimension>(new Comparator<Dimension>()
		{	@Override
			public int compare(Dimension arg0, Dimension arg1)
			{	int result;
				// width
					result = arg0.width-arg1.width;
				// height
				if(result==0)
					result = arg0.height-arg1.height;
				return result;
			}
		});
		for(DisplayMode m: modes)
		{	Dimension dim = new Dimension(m.getWidth(),m.getHeight());
			result.add(dim);
		}
		return result;
	}	
}
