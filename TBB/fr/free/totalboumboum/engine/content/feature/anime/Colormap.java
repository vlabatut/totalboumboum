package fr.free.totalboumboum.engine.content.feature.anime;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Colormap extends HashMap<Integer,byte[]>
{	private static final long serialVersionUID = 1L;	
//	public final static String COL_WHITE = "white";

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// misc
			clear();
		}
	}
}
