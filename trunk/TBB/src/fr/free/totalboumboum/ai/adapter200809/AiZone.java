package fr.free.totalboumboum.ai.adapter200809;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.player.Player;

public class AiZone
{
	public AiZone(Level level, Player player)
	{	initMatrix(level);
		updateMatrix();
		initOwnHero(player);
	}
	
	void update()
	{	updateMatrix();
	}
	
	void finish()
	{	// matrix
		Iterator<ArrayList<AiTile>> it1 = lines.iterator();
		while(it1.hasNext())
		{	ArrayList<AiTile> temp1 = it1.next();
			Iterator<AiTile> it2 = temp1.iterator();
			while(it2.hasNext())
			{	AiTile temp2 = it2.next();
				temp2.finish();
			}
		}
		lines.clear();
		// sprites
		blocks.clear();
		bombs.clear();
		fires.clear();
		floors.clear();
		heroes.clear();
		items.clear();
		ownHero = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice représentant la zone et tous les sprites qu'elle contient */
	private final ArrayList<ArrayList<AiTile>> lines = new ArrayList<ArrayList<AiTile>>();
	/** hauteur totale de la zone de jeu exprimée en cases (ie: nombre de lignes) */
	private int height;
	/** largeur totale de la zone de jeu exprimée en cases (ie: nombre de colonnes) */
	private int width;
	
	/** 
	 * initialise cette représentation de la zone en fonction du niveau passé en paramètre
	 */
	private void initMatrix(Level level)
	{	Tile[][] matrix = level.getMatrix();
		height = level.getGlobalHeight();
		width = level.getGlobalWidth();
		for(int lineIndex=0;lineIndex<height;lineIndex++)
		{	ArrayList<AiTile> line = new ArrayList<AiTile>();
			for(int colIndex=0;colIndex<width;colIndex++)
			{	Tile tile = matrix[lineIndex][colIndex];
				AiTile aiTile = new AiTile(lineIndex,colIndex,tile,this);
				line.add(aiTile);
			}
			lines.add(line);
		}
	}
	private void updateMatrix()
	{	// démarque tous les sprites
		uncheckAll(blocks);
		uncheckAll(bombs);
		uncheckAll(fires);
		uncheckAll(floors);
		uncheckAll(heroes);
		uncheckAll(items);
		// met à jour chaque case et sprite 
		Iterator<ArrayList<AiTile>> it1 = lines.iterator();
		while(it1.hasNext())
		{	ArrayList<AiTile> temp1 = it1.next();
			Iterator<AiTile> it2 = temp1.iterator();
			while(it2.hasNext())
			{	AiTile temp2 = it2.next();
				temp2.update();
			}
		}
		// supprime les sprites non-marqués
		removeUnchecked(blocks);
		removeUnchecked(bombs);
		removeUnchecked(fires);
		removeUnchecked(floors);
		removeUnchecked(heroes);
		removeUnchecked(items);
	}
	/** 
	 * renvoie la hauteur totale (y compris les éventuelles cases situées hors de l'écran)
	 *  de la zone de jeu exprimée en cases (ie: nombre de lignes)
	 */
	public int getHeigh()
	{	return height;	
	}
	/** 
	 * renvoie la largeur totale (y compris les éventuelles cases situées hors de l'écran)
	 *  de la zone de jeu exprimée en cases (ie: nombre de colonnes)
	 */
	public int getWidth()
	{	return width;	
	}
	/**
	 * renvoie la case située dans la zone à la position passée en paramètre.
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
		ArrayList<AiTile> collec = getLine(line);
		result = collec.get(col);
		return result;
	}
	/**
	 * renvoie la ligne de la zone de jeu dont le numéro est passé en paramètre.
	 * Cette ligne est une liste des cases qui la composent. Si la ligne n'existe pas,
	 * la valeur null est renvoyée.
	 * ATTENTION: cette méthode est définie pour un usage ponctuel (accès à
	 * une seule ligne et à ses cases). Si vous effectuez un traitement itératif ou 
	 * récursif sur un grand nombre de cases (par exemple : l'ensemble de la zone), 
	 * il sera plus efficace et surtout plus rapide d'utiliser directement un Iterator 
	 * obtenu avec la méthode getLines, plutôt que de faire de nombreux appels à getLine.
	 */
	public ArrayList<AiTile> getLine(int line)
	{	return lines.get(line);
	}
	/**
	 * renvoie la liste des lignes composant la zone de jeu. Chaque ligne
	 * est elle même une liste de cases.
	 */
	public ArrayList<ArrayList<AiTile>> getLines()
	{	return lines;
	}

	/**
	 * renvoie le voisin de la case passée en paramètre, situé dans la direction
	 * passée en paramètre.
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). Même chose pour les bordures haut et bas.
	 * 
	 * @param line	ligne de la case dont on veut le voisin
	 * @param col	colonne de la case dont on veut le voisin
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de la case située à la position (line,col) et situé dans la direction indiquée
	 */
	public AiTile getNeighbourTile(int line, int col, Direction direction)
	{	AiTile result = null;
		int c,l;
		Direction p[] = direction.getPrimaries(); 
		//
		if(p[0]==Direction.LEFT)
			c = (col+width-1)%width;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%width;
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (line+height-1)%height;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%height;
		else
			l = line;
		//
		result = getTile(l,c);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des blocks contenus dans cette zone */
	private final ArrayList<AiBlock> blocks = new ArrayList<AiBlock>();
	/** liste des bombes contenues dans cette zone */
	private final ArrayList<AiBomb> bombs = new ArrayList<AiBomb>();
	/** liste des feux contenus dans cette zone */
	private final ArrayList<AiFire> fires = new ArrayList<AiFire>();
	/** liste des sols contenus dans cette zone */
	private final ArrayList<AiFloor> floors = new ArrayList<AiFloor>();
	/** liste des personnages contenus dans cette zone */
	private final ArrayList<AiHero> heroes = new ArrayList<AiHero>();
	/** liste des items contenus dans cette zone */
	private final ArrayList<AiItem> items = new ArrayList<AiItem>();
	
	/** 
	 * renvoie la liste des blocks contenues dans cette zone
	 * (la liste peut être vide)
	 */
	public ArrayList<AiBlock> getBlocks()
	{	return blocks;	
	}
	AiBlock getBlock(Block block)
	{	AiBlock result = null;
		Iterator<AiBlock> it = blocks.iterator();
		while(it.hasNext() && result==null)
		{	AiBlock b = it.next();
			if(b.isSprite(block))
				result = b;			
		}
		return result;
	}
	void addBlock(AiBlock block)
	{	blocks.add(block);	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut être vide)
	 */
	public ArrayList<AiBomb> getBombs()
	{	return bombs;	
	}
	AiBomb getBomb(Bomb bomb)
	{	AiBomb result = null;
		Iterator<AiBomb> it = bombs.iterator();
		while(it.hasNext() && result==null)
		{	AiBomb b = it.next();
			if(b.isSprite(bomb))
				result = b;			
		}
		return result;
	}
	void addBomb(AiBomb bomb)
	{	bombs.add(bomb);	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut être vide)
	 */
	public ArrayList<AiFire> getFires()
	{	return fires;	
	}
	AiFire getFire(Fire fire)
	{	AiFire result = null;
		Iterator<AiFire> it = fires.iterator();
		while(it.hasNext() && result==null)
		{	AiFire f = it.next();
			if(f.isSprite(fire))
				result = f;			
		}
		return result;
	}
	void addFire(AiFire fire)
	{	fires.add(fire);	
	}
	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 */
	public ArrayList<AiFloor> getFloors()
	{	return floors;	
	}
	AiFloor getFloor(Floor floor)
	{	AiFloor result = null;
		Iterator<AiFloor> it = floors.iterator();
		while(it.hasNext() && result==null)
		{	AiFloor f = it.next();
			if(f.isSprite(floor))
				result = f;			
		}
		return result;
	}
	void addFloor(AiFloor floor)
	{	floors.add(floor);	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette zone 
	 * (les joueurs éliminés n'apparaissent plus dans cette liste ni dans cette représentation de la zone)
	 */
	public ArrayList<AiHero> getHeroes()
	{	return heroes;	
	}
	AiHero getHero(Hero hero)
	{	AiHero result = null;
		Iterator<AiHero> it = heroes.iterator();
		while(it.hasNext() && result==null)
		{	AiHero h = it.next();
			if(h.isSprite(hero))
				result = h;			
		}
		return result;
	}
	void addHero(AiHero hero)
	{	heroes.add(hero);	
	}
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut être vide)
	 */
	public ArrayList<AiItem> getItems()
	{	return items;	
	}
	AiItem getItem(Item item)
	{	AiItem result = null;
		Iterator<AiItem> it = items.iterator();
		while(it.hasNext() && result==null)
		{	AiItem i = it.next();
			if(i.isSprite(item))
				result = i;			
		}
		return result;
	}
	void addItem(AiItem item)
	{	items.add(item);	
	}
	private <T extends AiSprite<?>> void uncheckAll(ArrayList<T> list)
	{	Iterator<T> it = list.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.uncheck();
		}
	}
	private <T extends AiSprite<?>> void removeUnchecked(ArrayList<T> list)
	{	Iterator<T> it = list.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			if(!temp.isChecked())
				it.remove();
		}
	}
	
//		blocks = Collections.unmodifiableCollection(blocks);

	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contrôlé par l'IA */
	private AiHero ownHero;

	/** 
	 * renvoie le personnage qui est contrôlé par l'IA
	 */
	public AiHero getOwnHero()
	{	return ownHero;	
	}
	/**
	 * initialise le personnage qui est contrôlé par l'IA
	 */
	private void initOwnHero(Player player)
	{	PredefinedColor color = player.getColor(); 
		Iterator<AiHero> i = heroes.iterator();
		boolean found = false;
		while(i.hasNext() && !found)
		{	AiHero temp = i.next();
			if(temp.getColor()==color)
			{	ownHero = temp;
				found = true;
			}
		}
	}
}
