package fr.free.totalboumboum.ai.adapter200809;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;

public class AiTile
{
	
	public AiTile(int line, int col, Tile tile)
	{	initLocation(line,col);
		initSPrites(tile);
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int line;
	private int col;
		
	public int getLine()
	{	return line;	
	}
	public int getCol()
	{	return col;	
	}
	
	private void initLocation(int line, int col)
	{	this.line = line;
		this.col = col;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiBlock block;
	private Collection<AiBomb> bombs;
	private Collection<AiFire> fires;
	private AiFloor floor;
	private Collection<AiHero> heroes;
	private AiItem item;

	public AiBlock getBlock()
	{	return block;	
	}
	public Collection<AiBomb> getBombs()
	{	return bombs;	
	}
	public Collection<AiFire> getFires()
	{	return fires;	
	}
	public AiFloor getFloor()
	{	return floor;	
	}
	public Collection<AiHero> getHeroes()
	{	return heroes;	
	}
	public AiItem getItem()
	{	return item;	
	}
	
	private void initSPrites(Tile tile)
	{	// block
		block = new AiBlock(this,tile.getBlock());
		// bombs
		{	ArrayList<AiBomb> tempBombs = new ArrayList<AiBomb>();
			Iterator<Bomb> i = tile.getBombs().iterator();
			while(i.hasNext())
			{	Bomb bomb = i.next();
				AiBomb tempBomb = new AiBomb(this,bomb);
				tempBombs.add(tempBomb);
			}
			bombs = Collections.unmodifiableCollection(tempBombs);
		}
		// fires
		{	ArrayList<AiFire> tempFires = new ArrayList<AiFire>();
			Iterator<Fire> i = tile.getFires().iterator();
			while(i.hasNext())
			{	Fire fire = i.next();
				AiFire tempFire = new AiFire(this,fire);
				tempFires.add(tempFire);
			}
			fires = Collections.unmodifiableCollection(tempFires);
		}
		// floor
		floor = new AiFloor(this,tile.getFloor());
		// heroes
		{	ArrayList<AiHero> tempHeroes = new ArrayList<AiHero>();
			Iterator<Hero> i = tile.getHeroes().iterator();
			while(i.hasNext())
			{	Hero hero = i.next();
				AiHero tempHero = new AiHero(this,hero);
				tempHeroes.add(tempHero);
			}
			heroes = Collections.unmodifiableCollection(tempHeroes);
		}
		// item
		item = new AiItem(this,tile.getItem());		
	}
}
