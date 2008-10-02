package fr.free.totalboumboum.engine.container.tile;


public class ValueTile
{
	private float proba;
	private String block;
	private String item;
	private String floor;
	
	public ValueTile(String floor, String block, String item, float proba)
	{	this.block = block;
		this.item = item;
		this.floor = floor;
		this.proba = proba;
	}
	
	public String getBlock()
	{	return block;
	}
	public String getItem()
	{	return item;
	}
	public String getFloor()
	{	return floor;
	}
	
	public float getProba()
	{	return proba;	
	}
	public void setProba(float proba)
	{	this.proba = proba;
	}
}
