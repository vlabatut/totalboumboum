package fr.free.totalboumboum.ai.adapter200809;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.loop.Loop;

public class AiZone
{
	
	public AiZone(Loop loop)
	{	// zone
		Level level = loop.getLevel();
		initZone(level);
		initSprites();
		// players
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice représentant la zone et tous les sprites qu'elle contient */
	private Collection<Collection<AiTile>> lines;
	/** hauteur totale de la zone de jeu exprimée en cases (ie: nombre de lignes) */
	private int height;
	/** largeur totale de la zone de jeu exprimée en cases (ie: nombre de colonnes) */
	private int width;
	
	/** 
	 * initialise cette représentation de la zone en fonction du niveau passé en paramètre
	 */
	private void initZone(Level level)
	{	Tile[][] matrix = level.getMatrix();
		height = level.getGlobalHeight();
		width = level.getGlobalWidth();
		lines = new ArrayList<Collection<AiTile>>();		
		for(int lineIndex=0;lineIndex<height;lineIndex++)
		{	Collection<AiTile> line = new ArrayList<AiTile>();
			for(int colIndex=0;colIndex<width;colIndex++)
			{	Tile tile = matrix[lineIndex][colIndex];
				AiTile aiTile = new AiTile(lineIndex,colIndex,tile);
				line.add(aiTile);
			}
			line = Collections.unmodifiableCollection(line);
			lines.add(line);
		}
		lines = Collections.unmodifiableCollection(lines);
	}
	/** 
	 * renvoie la hauteur total (y compris les éventuelles cases situées hors de l'écran)
	 *  de la zone de jeu exprimée en cases (ie: nombre de lignes)
	 */
	public int getHeigh()
	{	return height;	
	}
	/** 
	 * renvoie la largeur total (y compris les éventuelles cases situées hors de l'écran)
	 *  de la zone de jeu exprimée en cases (ie: nombre de colonnes)
	 */
	public int getWidth()
	{	return width;	
	}
	/**
	 * renvoie la case de la zone située à la position passée en paramètre.
	 * Si la case n'existe pas, la valeur null est renvoyée.
	 * ATTENTION : cette méthode est définie pour un usage ponctuel (accès à
	 * une seule case). Si vous effectuez un traitement itératif ou récursif sur 
	 * un grand nombre de cases (par exemple : l'ensemble de la zone), il sera 
	 * plus efficace et surtout plus rapide d'utiliser directement un Iterator 
	 * obtenu avec la méthode getLines ou getLine, plutôt que de faire de nombreux 
	 * appels à getTile.  
	 */
	public AiTile getTile(int line, int col)
	{	AiTile result = null;
		Collection<AiTile> collec = getLine(line);
		if(collec!=null && collec.size()<col)
		{	Iterator<AiTile> it = collec.iterator();
			for(int i=0;i<col;i++)
				result = it.next();
		}
		return result;
	}
	/**
	 * renvoie la la ligne de la zone de jeu dont le numéro est passé en paramètre.
	 * Cette ligne est une liste des cases qui la composent. Si la ligne n'existe pas,
	 * la valeur null est renvoyée.
	 * ATTENTION: cette méthode est définie pour un usage ponctuel (accès à
	 * une seule ligne et à ses cases). Si vous effectuez un traitement itératif ou 
	 * récursif sur un grand nombre de cases (par exemple : l'ensemble de la zone), 
	 * il sera plus efficace et surtout plus rapide d'utiliser directement un Iterator 
	 * obtenu avec la méthode getLines, plutôt que de faire de nombreux appels à getLine.
	 */
	public Collection<AiTile> getLine(int line)
	{	Collection<AiTile> result = null;
		if(lines.size()<line)
		{	Iterator<Collection<AiTile>> it = lines.iterator();
			for(int i=0;i<line;i++)
				result = it.next();			
		}
		return result;
	}
	/**
	 * renvoie la liste des lignes composant la zone de jeu. Chaque ligne
	 * est elle même une liste de cases.
	 */
	public Collection<Collection<AiTile>> getLines()
	{	return lines;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des blocks contenus dans cette zone */
	private Collection<AiBlock> blocks;
	/** liste des bombes contenues dans cette zone */
	private Collection<AiBomb> bombs;
	/** liste des feux contenus dans cette zone */
	private Collection<AiFire> fires;
	/** liste des sols contenus dans cette zone */
	private Collection<AiFloor> floors;
	/** liste des personnages contenus dans cette zone */
	private Collection<AiHero> heroes;
	/** liste des items contenus dans cette zone */
	private Collection<AiItem> items;
	
	/**
	 * initialise la liste de sprites pour la zone entière
	 * (à partir des listes de sprites de chaque case de cette zone)
	 */
	private void initSprites()
	{	// init lists
		blocks = new ArrayList<AiBlock>();
		bombs = new ArrayList<AiBomb>();
		fires = new ArrayList<AiFire>();
		floors = new ArrayList<AiFloor>();
		heroes = new ArrayList<AiHero>();
		items = new ArrayList<AiItem>();
		// fill lists
		Iterator<Collection<AiTile>> it1 = lines.iterator();
		while(it1.hasNext())
		{	Collection<AiTile> line = it1.next();
			Iterator<AiTile> it2 = line.iterator();
			while(it2.hasNext())
			{	AiTile tile = it2.next();
				// block
				AiBlock block = tile.getBlock();
				if(block!=null)
					blocks.add(block);
				// bombs
				Collection<AiBomb> bomb = tile.getBombs();
				bombs.addAll(bomb);
				// fires
				Collection<AiFire> fire = tile.getFires();
				fires.addAll(fire);
				// floor
				AiFloor floor = tile.getFloor();
				if(floor!=null)
					floors.add(floor);
				// heroes
				Collection<AiHero> hero = tile.getHeroes();
				heroes.addAll(hero);
				// item
				AiItem item = tile.getItem();
				if(item!=null)
					items.add(item);
			}
		}
		// make lists unmutable
		blocks = Collections.unmodifiableCollection(blocks);
		bombs = Collections.unmodifiableCollection(bombs);
		fires = Collections.unmodifiableCollection(fires);
		floors = Collections.unmodifiableCollection(floors);
		heroes = Collections.unmodifiableCollection(heroes);
		items = Collections.unmodifiableCollection(items);
	}
	
	
}
