package fr.free.totalboumboum.engine.content.feature.permission;

public class StateModulation extends AbstractPermission
{	
	String name;
	
	public StateModulation(String name)
	{	super();
		this.name = name;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}	

	public boolean equals(Object modulation)
	{	boolean result = false;
		if(modulation instanceof StateModulation)
		{	StateModulation m = (StateModulation) modulation;
			result = name.equalsIgnoreCase(m.getName());
		}
		return result;
	}
	
	public String toString()
	{	String result = name;
		result = "<"+strength+","+frame+">";
		return result;
	}
	
	public StateModulation copy()
	{	StateModulation result = new StateModulation(name);
		result.name = name;
		result.finished = finished;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}

}
