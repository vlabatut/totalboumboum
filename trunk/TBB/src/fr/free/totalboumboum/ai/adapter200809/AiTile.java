package fr.free.totalboumboum.ai.adapter200809;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;

public class AiTile
{
	
	public AiTile(int line, int col, Tile tile)
	{	initLocation(line,col);
		initSPrites(tile);
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
	private AiBlock block;
	/** liste des bombes contenues dans cette case */
	private Collection<AiBomb> bombs;
	/** liste des feux contenus dans cette case */
	private Collection<AiFire> fires;
	/** sol de cette case */
	private AiFloor floor;
	/** liste des personnages contenus dans cette case */
	private Collection<AiHero> heroes;
	/** item contenu dans cette case */
	private AiItem item;

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
	{	return bombs;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide)
	 */
	public Collection<AiFire> getFires()
	{	return fires;	
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
	{	return heroes;	
	}
	/** 
	 * renvoie l'item (apparent) contenu dans cette case 
	 * ou null s'il n'y a pas d'item apparent dans cette case
	 */
	public AiItem getItem()
	{	return item;	
	}
	
	/** 
	 * initialise les représentations des sprites contenus dans cette case
	 */
	private void initSPrites(Tile tile)
	{	// block
		{	Block b = tile.getBlock();
			String gesture = b.getCurrentGesture();
			if(b!=null && !gesture.equalsIgnoreCase(GestureConstants.NONE) && !gesture.equalsIgnoreCase(GestureConstants.ENDED))
				block = new AiBlock(this,b);
			else
				block = null;
		}
		// bombs
		{	ArrayList<AiBomb> tempBombs = new ArrayList<AiBomb>();
			Iterator<Bomb> i = tile.getBombs().iterator();
			while(i.hasNext())
			{	Bomb bomb = i.next();
				String gesture = bomb.getCurrentGesture();
				if(!gesture.equalsIgnoreCase(GestureConstants.NONE) && !gesture.equalsIgnoreCase(GestureConstants.ENDED))
				{	AiBomb tempBomb = new AiBomb(this,bomb);
					tempBombs.add(tempBomb);
				}
			}
			bombs = Collections.unmodifiableCollection(tempBombs);
		}
		// fires
		{	ArrayList<AiFire> tempFires = new ArrayList<AiFire>();
			Iterator<Fire> i = tile.getFires().iterator();
			while(i.hasNext())
			{	Fire fire = i.next();
				String gesture = fire.getCurrentGesture();
				if(!gesture.equalsIgnoreCase(GestureConstants.NONE) && !gesture.equalsIgnoreCase(GestureConstants.ENDED))
				{	AiFire tempFire = new AiFire(this,fire);
					tempFires.add(tempFire);
				}
			}
			fires = Collections.unmodifiableCollection(tempFires);
		}
		// floor
		{	Floor f = tile.getFloor();
			String gesture = f.getCurrentGesture();
			if(f!=null && !gesture.equalsIgnoreCase(GestureConstants.NONE) && !gesture.equalsIgnoreCase(GestureConstants.ENDED))
				floor = new AiFloor(this,f);
			else
				floor = null;
		}
		// heroes
		{	ArrayList<AiHero> tempHeroes = new ArrayList<AiHero>();
			Iterator<Hero> i = tile.getHeroes().iterator();
			while(i.hasNext())
			{	Hero hero = i.next();
				String gesture = hero.getCurrentGesture();
				if(!gesture.equalsIgnoreCase(GestureConstants.NONE) && !gesture.equalsIgnoreCase(GestureConstants.ENDED))
				{	AiHero tempHero = new AiHero(this,hero);
					tempHeroes.add(tempHero);
				}
			}
			heroes = Collections.unmodifiableCollection(tempHeroes);
		}
		// item
		{	Item i = tile.getItem();
			String gesture = i.getCurrentGesture();
			if(i!=null && !gesture.equalsIgnoreCase(GestureConstants.NONE) && !gesture.equalsIgnoreCase(GestureConstants.ENDED))
				item = new AiItem(this,i);
			else
				item = null;
		}
	}
}
