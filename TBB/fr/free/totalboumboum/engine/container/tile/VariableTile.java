package fr.free.totalboumboum.engine.container.tile;

import java.util.ArrayList;


public class VariableTile
{
	private ArrayList<ValueTile> values = new ArrayList<ValueTile>();
	private String name;//debug
	
	public VariableTile(String name)
	{	this.name = name;
	}
	
	public ArrayList<ValueTile> getValues()
	{	return values;
	}
	public void addValue(ValueTile valueItem)
	{	values.add(valueItem);		
	}
	public void setProba(int index, float proba)
	{	values.get(index).setProba(proba);				
	}
	public float getProba(int index)
	{	return values.get(index).getProba();				
	}

	public String getName()
	{	return name;
	}
}
