package fr.free.totalboumboum.ai.adapter200809;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;

public class AiTile
{
	private AiZone zone;
	private Tile tile;
	
	AiTile(int line, int col, Tile tile, AiZone zone)
	{	this.zone = zone;
		this.tile = tile;
		initLocation(line,col);
		updateSprites();
	}
	
	void update()
	{	updateSprites();		
	}
	
	void finish()
	{	// block
		if(block!=null)
		{	block.finish();	
			block = null;
		}
		// bombs
		finishSprites(bombs);
		// fires
		finishSprites(fires);
		// floor
		if(floor!=null)
		{	floor.finish();	
			floor = null;
		}
		// heroes
		finishSprites(heroes);
		// item
		if(item!=null)
		{	item.finish();	
			item = null;
		}	
	}
	
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiTile)
		{	
//			AiTile t = (AiTile)o;	
//			result = tile==t.tile && zone==t.zone;
			result = this==o;
		}
		return result;
	}
	
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("("+col+";"+line+")");
		return result.toString();
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ligne de la zone contenant cette case */
	private int line;
	/** colonne de la zone contenant cette case */
	private int col;
		
	/** 
	 * renvoie le numéro de la ligne contenant cette case 
	 */
	public int getLine()
	{	return line;	
	}
	/** 
	 * renvoie le numéro de la colonne contenant cette case 
	 */
	public int getCol()
	{	return col;	
	}
	
	/** 
	 * initialise les numéros de ligne et colonne de cette case 
	 */
	private void initLocation(int line, int col)
	{	this.line = line;
		this.col = col;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** block contenu dans cette case */
	private AiBlock block = null;
	/** liste des bombes contenues dans cette case */
	private final ArrayList<AiBomb> bombs = new ArrayList<AiBomb>();
	/** liste des feux contenus dans cette case */
	private final ArrayList<AiFire> fires = new ArrayList<AiFire>();
	/** sol de cette case */
	private AiFloor floor = null;
	/** liste des personnages contenus dans cette case */
	private final ArrayList<AiHero> heroes = new ArrayList<AiHero>();
	/** item contenu dans cette case */
	private AiItem item = null;

	/** 
	 * renvoie le block contenu dans cette case 
	 * ou null s'il n'y a pas de block dans cette case
	 */
	public AiBlock getBlock()
	{	return block;	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut être vide)
	 */
	public Collection<AiBomb> getBombs()
	{	Collection<AiBomb> result = Collections.unmodifiableCollection(bombs);
		return result;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide)
	 */
	public Collection<AiFire> getFires()
	{	Collection<AiFire> result = Collections.unmodifiableCollection(fires);
		return result;	
	}
	/** 
	 * renvoie le sol de cette case 
	 * (il y a forcément un sol)
	 */
	public AiFloor getFloor()
	{	return floor;	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut être vide)
	 */
	public Collection<AiHero> getHeroes()
	{	Collection<AiHero> result = Collections.unmodifiableCollection(heroes);
		return result;	
	}
	/** 
	 * renvoie l'item (apparent) contenu dans cette case 
	 * ou null s'il n'y a pas d'item apparent dans cette case
	 */
	public AiItem getItem()
	{	return item;	
	}
	
	/** 
	 * met à jour les représentations des sprites contenus dans cette case
	 */
	private void updateSprites()
	{	// block
		{	Block b = tile.getBlock();
			if(b!=null)
			{	String gesture = b.getCurrentGesture();
				if(!(gesture.equalsIgnoreCase(GestureConstants.NONE) 
					|| gesture.equalsIgnoreCase(GestureConstants.HIDING)
					|| gesture.equalsIgnoreCase(GestureConstants.ENDED)))
				{	block = zone.getBlock(b);
					if(block==null)
					{	block = new AiBlock(this,b);
						zone.addBlock(block);
					}
					block.update(this);
				}
			}
			else
				block = null;
		}
		// bombs
		{	bombs.clear();
			Iterator<Bomb> i = tile.getBombs().iterator();
			while(i.hasNext())
			{	Bomb b = i.next();
				String gesture = b.getCurrentGesture();
				if(!(gesture.equalsIgnoreCase(GestureConstants.NONE) 
					|| gesture.equalsIgnoreCase(GestureConstants.HIDING)
					|| gesture.equalsIgnoreCase(GestureConstants.ENDED)))
				{	AiBomb bomb = zone.getBomb(b);
					if(bomb==null)
					{	bomb = new AiBomb(this,b);
						zone.addBomb(bomb);
					}
					bomb.update(this);
					bombs.add(bomb);
				}
			}
		}
		// fires
		{	fires.clear();
			Iterator<Fire> i = tile.getFires().iterator();
			while(i.hasNext())
			{	Fire f = i.next();
				String gesture = f.getCurrentGesture();
				if(!(gesture.equalsIgnoreCase(GestureConstants.NONE) 
					|| gesture.equalsIgnoreCase(GestureConstants.HIDING)
					|| gesture.equalsIgnoreCase(GestureConstants.ENDED)))
				{	AiFire fire = zone.getFire(f);
					if(fire==null)
					{	fire = new AiFire(this,f);
						zone.addFire(fire);
					}
					fire.update(this);
					fires.add(fire);
				}
			}
		}
		// floor
		{	Floor f = tile.getFloor();
			if(f!=null)
			{	String gesture = f.getCurrentGesture();
				if(!(gesture.equalsIgnoreCase(GestureConstants.NONE) 
					|| gesture.equalsIgnoreCase(GestureConstants.HIDING)
					|| gesture.equalsIgnoreCase(GestureConstants.ENDED)))
				{	floor = zone.getFloor(f);
					if(floor==null)
					{	floor = new AiFloor(this,f);
						zone.addFloor(floor);
					}
					floor.update(this);
				}
			}
			else
				floor = null;
		}
		// heroes
		{	heroes.clear();
			Iterator<Hero> i = tile.getHeroes().iterator();
			while(i.hasNext())
			{	Hero h = i.next();
				String gesture = h.getCurrentGesture();
				if(!(gesture.equalsIgnoreCase(GestureConstants.NONE) 
					|| gesture.equalsIgnoreCase(GestureConstants.HIDING)
					|| gesture.equalsIgnoreCase(GestureConstants.ENDED)))
				{	AiHero hero = zone.getHero(h);
					if(hero==null)
					{	hero = new AiHero(this,h);
						zone.addHero(hero);
					}
					hero.update(this);
					heroes.add(hero);
				}
			}
		}
		// item
		{	Item i = tile.getItem();
			if(i!=null)
			{	String gesture = i.getCurrentGesture();
				if(!(gesture.equalsIgnoreCase(GestureConstants.NONE) 
					|| gesture.equalsIgnoreCase(GestureConstants.HIDING)
					|| gesture.equalsIgnoreCase(GestureConstants.ENDED)))
				{	item = zone.getItem(i);
					if(item==null)
					{	item = new AiItem(this,i);
						zone.addItem(item);
					}
					item.update(this);
				}
			}
			else
				item = null;
		}
	}
	private <T extends AiSprite<?>> void finishSprites(ArrayList<T> list)
	{	Iterator<T> it = list.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.finish();
		}
		list.clear();
	}

	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * renvoie le voisin de cette case passée en paramètre, situé dans la direction
	 * passée en paramètre.
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). Même chose pour les bordures haut et bas.
	 * 
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de cette case, situé dans la direction indiquée
	 */
	public AiTile getNeighbour(Direction direction)
	{	AiTile result;
		result = zone.getNeighbourTile(this,direction);
		return result;
	}
	
}
