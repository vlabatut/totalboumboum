package fr.free.totalboumboum.engine.content.feature.event;

public class ControlEvent extends AbstractEvent
{	// directions
	public static final String DOWN = "DOWN";
//	public static final String DOWNLEFT = "DOWNLEFT";
//	public static final String DOWNRIGHT = "DOWNRIGHT";
	public static final String LEFT = "LEFT";
	public static final String RIGHT = "RIGHT";
	public static final String UP = "UP";
//	public static final String UPLEFT = "UPLEFT";
//	public static final String UPRIGHT = "UPRIGHT";
	// boutons
	public static final String DROPBOMB = "DROPBOMB";
	public static final String PICKBOMB = "PICKBOMB";
	public static final String PUNCHBOMB = "PUNCHBOMB";
	public static final String THROWBOMB = "THROWBOMB";
	public static final String PICKHERO = "PICKHERO";
	public static final String THROWHERO = "THROWHERO";
	public static final String TRIGGERBOMB = "TRIGGERBOMB";
	public static final String JUMP = "JUMP";
	public static final String STOPBOMB = "STOPBOMB";
	
	private String name;
	private boolean mode;
	
	public ControlEvent(String name, boolean mode)
	{	this.name = name;	
		this.mode = mode;
	}

	public String getName()
	{	return name;	
	}
	public boolean getMode()
	{	return mode;	
	}
/*
	public boolean isDirection()
	{	boolean result;
		result = name.equals(DOWN) || name.equals(LEFT) || name.equals(RIGHT) || name.equals(UP);
		return result;
	}
*/	
	public boolean equals(Object object)
	{	boolean result = false;
		if(object instanceof ControlEvent)
		{	ControlEvent ce = (ControlEvent) object;
			result = name.equals(ce.getName()) && mode==ce.getMode();			
		}
		return result;
	}
	
	public String toString()
	{	return name;	
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
