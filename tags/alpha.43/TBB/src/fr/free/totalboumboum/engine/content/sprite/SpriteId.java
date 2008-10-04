package fr.free.totalboumboum.engine.content.sprite;

public class SpriteId
{
	private String name;
	private String packname;
	
	public SpriteId(String packname, String name)
	{	this.name = name;
		this.packname = packname;
	}
	
	public String getName()
	{	return name;	
	}
	
	public String getPackName()
	{	return packname;	
	}
}
