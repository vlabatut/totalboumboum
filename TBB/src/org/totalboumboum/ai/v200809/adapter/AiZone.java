package org.totalboumboum.ai.v200809.adapter;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.tools.images.PredefinedColor;


/**
 * représente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'IA a accès.
 * <p>
 * A chaque fois que l'IA est sollicit�e par le jeu pour conna�tre l'action
 * qu'elle veut effectuer, cette représentation est mise à jour. L'IA ne reçoit
 * pas une nouvelle AiZone : l'AiZone existante est modifiée en fonction de l'évolution
 * du jeu. De la même façon, les cases (AiTile) restent les mêmes, ainsi que les sprites et
 * les autres objets. Si l'IA a besoin d'une trace des états précédents du jeu, son
 * concepteur doit se charger de l'implémenter lui-même.
 * 
 * @author Vincent Labatut
 *
 */

public class AiZone
{	/** niveau représenté par cette classe */
	private Level level;
	/** joueur contrôlé par l'IA */
	private AbstractPlayer player;
	
	/**
	 * construit une représentation du niveau passé en paramètre,
	 * du point de vue du joueur passé en paramètre.
	 * @param level	niveau à représenter
	 * @param player	joueur dont le point de vue est à adopter
	 */
	AiZone(Level level, AbstractPlayer player)
	{	this.level = level;
		this.player = player;
		initMatrix();
//		updateMatrix();
		initOwnHero();
	}
	
	/**
	 * met à jour cette représentation ainsi que tous ses constituants.
	 */
	void update(long elapsedTime)
	{	updateTime(elapsedTime);
		updateMatrix();
	}
	
	/**
	 * termine proprement cette représentation (une fois que l'IA n'en a plus besoin).
	 */
	void finish()
	{	// matrix
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].finish();
		
		// sprites
		blocks.clear();
		bombs.clear();
		fires.clear();
		floors.clear();
		heroes.clear();
		items.clear();
		ownHero = null;
		
		// misc
		level = null;
		player = null;
	}
	
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiZone)
		{	
//			AiZone zone = (AiZone)o;	
//			result = level==zone.level && player==zone.player;
			result = this==o;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis la mise à jour précédente */
	private long elapsedTime = 0;
	
	/**
	 * renvoie le temps écoulé depuis la mise à jour précédente
	 * @return	le temps écoulé exprimé en millisecondes
	 */
	public long getElapsedTime()
	{	return elapsedTime;		
	}
	
	/**
	 * met à jour le temps écoulé depuis la dernière mise à jour
	 * @param elapsedTime
	 */
	private void updateTime(long elapsedTime)
	{	this.elapsedTime = elapsedTime;		
	}

	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice représentant la zone et tous les sprites qu'elle contient */
	private AiTile[][] matrix;
	/** hauteur totale de la zone de jeu exprimée en cases (ie: nombre de lignes) */
	private int height;
	/** largeur totale de la zone de jeu exprimée en cases (ie: nombre de colonnes) */
	private int width;
	
	/** 
	 * initialise cette représentation de la zone en fonction du niveau passé en paramètre
	 */
	private void initMatrix()
	{	Tile[][] m = level.getMatrix();
		height = level.getGlobalHeight();
		width = level.getGlobalWidth();
		matrix = new AiTile[height][width];
		for(int lineIndex=0;lineIndex<height;lineIndex++)
		{	for(int colIndex=0;colIndex<width;colIndex++)
			{	Tile tile = m[lineIndex][colIndex];
				AiTile aiTile = new AiTile(tile,this);
				matrix[lineIndex][colIndex] = aiTile;
			}
		}
	}
	
	/**
	 * met à jour la matrice en fonction de l'évolution du jeu
	 */
	private void updateMatrix()
	{	// démarque tous les sprites
		uncheckAll(blocks);
		uncheckAll(bombs);
		uncheckAll(fires);
		uncheckAll(floors);
		uncheckAll(heroes);
		uncheckAll(items);
		// met à jour chaque case et sprite 
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].update();
		// supprime les sprites non-marqués
		removeUnchecked(blocks);
		removeUnchecked(bombs);
		removeUnchecked(fires);
		removeUnchecked(floors);
		removeUnchecked(heroes);
		removeUnchecked(items);
		if(ownHero!=null && !ownHero.isChecked())
			ownHero = null;
	}
	
	/** 
	 * renvoie la hauteur totale (y compris les éventuelles cases situées hors de l'écran)
	 *  de la zone de jeu exprimée en cases (ie: nombre de lignes)
	 *  
	 *  @return	hauteur de la zone
	 */
	public int getHeight()
	{	return height;	
	}
	
	/** 
	 * renvoie la largeur totale (y compris les éventuelles cases situées hors de l'écran)
	 *  de la zone de jeu exprimée en cases (ie: nombre de colonnes)
	 *  
	 *  @return	largeur de la zone
	 */
	public int getWidth()
	{	return width;	
	}
	
	/**
	 * renvoie la case située dans la zone à la position passée en paramètre.
	 *   
	 *  @param	line	numéro de la ligne contenant la case à renvoyer
	 *  @param	col	numéro de la colonne contenant la case à renvoyer
	 *  @return	case située aux coordonnées spécifiées en paramètres
	 */
	public AiTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	/**
	 * renvoie le voisin de la case passée en paramètre, situé dans la direction
	 * passée en paramètre.
	 * <p>
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). même chose pour les bordures haut et bas.
	 * 
	 * @param row	ligne de la case dont on veut le voisin
	 * @param col	colonne de la case dont on veut le voisin
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de la case passée en paramètre et situé dans la direction indiquée
	 */
	public AiTile getNeighborTile(AiTile tile, Direction direction)
	{	AiTile result = null;
		int c,col=tile.getCol();
		int l,line=tile.getLine();
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
	
	/**
	 * renvoie la liste des voisins de la case passée en paramètre.
	 * Il s'agit des voisins directs situés en haut, à gauche, en bas et à droite.
	 * <p>
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). même chose pour les bordures haut et bas.
	 * 
	 * @param tile	la case dont on veut les voisins
	 * @return	la liste des voisins situés en haut, à gauche, en bas et à droite de la case passée en paramètre
	 */
	public Collection<AiTile> getNeighborTiles(AiTile tile)
	{	Collection<AiTile> result = new ArrayList<AiTile>();
		List<Direction> directions = Direction.getPrimaryValues();
		Iterator<Direction> d = directions.iterator();
		while(d.hasNext())
		{	Direction dir = d.next();
			AiTile neighbor = getNeighborTile(tile, dir);
			result.add(neighbor);
		}
		result = Collections.unmodifiableCollection(result);
		return result;
	}
	
	/**
	 * renvoie la direction de la case target relativement à la case source.
	 * Par exemple, la case target de coordonnées (5,5) est à droite de
	 * la case source de coordonnées (5,6).
	 * <p>
	 * Cette fonction peut être utile quand on veut savoir dans quelle direction
	 * il faut se déplacer pour aller de source à target.
	 * <p>
	 * ATTENTION 1 : si les deux cases ne sont pas des voisines directes (ie. ayant un coté commun),
	 * il est possible que cette méthode renvoie une direction composite,
	 * c'est à dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. référez-vous à 
	 * la classe Direction pour plus d'informations sur ces valeurs. 
	 * <p>
	 * ATTENTION 2 : comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette méthode renvoie la direction du plus court chemin (sans considérer les éventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le résultat sera
	 * RIGHT, car LEFT permet également d'atteindre la case, mais en parcourant un chemin plus long. 
	 * 
	 * @param source	case de référence
	 * @param target	case dont on veut connaitre la direction
	 * @return	la direction de target par rapport à source
	 */
	public Direction getDirection(AiTile source, AiTile target)
	{	// differences
		int dx = target.getCol()-source.getCol();
		int dy = target.getLine()-source.getLine();
		// direction
		Direction temp = Direction.getCompositeFromDouble(dx,dy);
		Direction tempX = temp.getHorizontalPrimary();
		Direction tempY = temp.getVerticalPrimary();
		// distances
		int distDirX = Math.abs(dx);
		int distIndirX = getWidth()-distDirX;
		if(distDirX>distIndirX)
			tempX = tempX.getOpposite();
		int distDirY = Math.abs(dy);
		int distIndirY = getHeight()-distDirY;
		if(distDirY>distIndirY)
			tempY = tempY.getOpposite();
		// result
		Direction result = Direction.getComposite(tempX,tempY);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des blocks contenus dans cette zone */
	private final HashMap<Block,AiBlock> blocks = new HashMap<Block,AiBlock>();
	/** liste des bombes contenues dans cette zone */
	private final HashMap<Bomb,AiBomb> bombs = new HashMap<Bomb,AiBomb>();
	/** liste des feux contenus dans cette zone */
	private final HashMap<Fire,AiFire> fires = new HashMap<Fire,AiFire>();
	/** liste des sols contenus dans cette zone */
	private final HashMap<Floor,AiFloor> floors = new HashMap<Floor,AiFloor>();
	/** liste des personnages contenus dans cette zone */
	private final HashMap<Hero,AiHero> heroes = new HashMap<Hero,AiHero>();
	/** liste des items contenus dans cette zone */
	private final HashMap<Item,AiItem> items = new HashMap<Item,AiItem>();
	
	/** 
	 * renvoie la liste des blocks contenues dans cette zone
	 * (la liste peut être vide). 
	 * Cette instance de liste change à chaque appel de l'IA. 
	 * Il ne faut donc pas réutiliser la même liste, mais redemander la nouvelle
	 * liste en utilisant cette méthode.
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	public Collection<AiBlock> getBlocks()
	{	Collection<AiBlock> result = Collections.unmodifiableCollection(blocks.values());
		return result;	
	}
	
	/**
	 * renvoie la représentation du bloc passé en paramètre.
	 * @param block	le bloc dont on veut la représentation
	 * @return	le AiBlock correspondant
	 */
	AiBlock getBlock(Block block)
	{	return blocks.get(block);
	}
	
	/**
	 * ajoute un bloc dans la liste de blocs de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param block	le bloc à rajouter à la liste
	 */
	void addBlock(AiBlock block)
	{	blocks.put(block.getSprite(),block);	
	}
	
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut être vide)
	 * Cette instance de liste change à chaque appel de l'IA. 
	 * Il ne faut donc pas réutiliser la même liste, mais redemander la nouvelle
	 * liste en utilisant cette méthode.
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	public Collection<AiBomb> getBombs()
	{	Collection<AiBomb> result = Collections.unmodifiableCollection(bombs.values());
		return result;	
	}
	
	/**
	 * renvoie la représentation de la bombe passée en paramètre.
	 * @param bomb	la bombz dont on veut la représentation
	 * @return	le AiBomb correspondant
	 */
	AiBomb getBomb(Bomb bomb)
	{	return bombs.get(bomb);
	}
	
	/**
	 * ajoute une bombe dans la liste de bombes de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param bomb	la bombe à rajouter à la liste
	 */
	void addBomb(AiBomb bomb)
	{	bombs.put(bomb.getSprite(),bomb);	
	}
	
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut être vide)
	 * Cette instance de liste change à chaque appel de l'IA. 
	 * Il ne faut donc pas réutiliser la même liste, mais redemander la nouvelle
	 * liste en utilisant cette méthode.
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	public Collection<AiFire> getFires()
	{	Collection<AiFire> result = Collections.unmodifiableCollection(fires.values());
		return result;	
	}
	
	/**
	 * renvoie la représentation du feu passé en paramètre.
	 * @param fire	le feu dont on veut la représentation
	 * @return	le AiFire correspondant
	 */
	AiFire getFire(Fire fire)
	{	return fires.get(fire);
	}
	
	/**
	 * ajoute un feu dans la liste de feux de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param fire	le feu à rajouter à la liste
	 */
	void addFire(AiFire fire)
	{	fires.put(fire.getSprite(),fire);	
	}
	
	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * Cette instance de liste change à chaque appel de l'IA. 
	 * Il ne faut donc pas réutiliser la même liste, mais redemander la nouvelle
	 * liste en utilisant cette méthode.
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	public Collection<AiFloor> getFloors()
	{	Collection<AiFloor> result = Collections.unmodifiableCollection(floors.values());
		return result;	
	}
	
	/**
	 * renvoie la représentation du sol passé en paramètre.
	 * @param floor	le sol dont on veut la représentation
	 * @return	le AiFloor correspondant
	 */
	AiFloor getFloor(Floor floor)
	{	return floors.get(floor);
	}
	
	/**
	 * ajoute un sol dans la liste de sols de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param floor	le sol à rajouter à la liste
	 */
	void addFloor(AiFloor floor)
	{	floors.put(floor.getSprite(),floor);	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone 
	 * (les joueurs éliminés n'apparaissent plus dans cette liste ni dans cette représentation de la zone)
	 * Cette instance de liste change à chaque appel de l'IA. 
	 * Il ne faut donc pas réutiliser la même liste, mais redemander la nouvelle
	 * liste en utilisant cette méthode.
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	public Collection<AiHero> getHeroes()
	{	Collection<AiHero> result = Collections.unmodifiableCollection(heroes.values());
		return result;	
	}
	
	/**
	 * renvoie la représentation du personnage passé en paramètre.
	 * @param hero	le personnage dont on veut la représentation
	 * @return	le AiHero correspondant
	 */
	AiHero getHero(Hero hero)
	{	return heroes.get(hero);
	}
	
	/**
	 * ajoute un personnage dans la liste de personnages de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param hero	le personnage à rajouter à la liste
	 */
	void addHero(AiHero hero)
	{	heroes.put(hero.getSprite(),hero);	
	}
	
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut être vide)
	 * Cette instance de liste change à chaque appel de l'IA. 
	 * Il ne faut donc pas réutiliser la même liste, mais redemander la nouvelle
	 * liste en utilisant cette méthode.
	 * 
	 * @return	liste de tous les items contenus dans cette zone
	 */
	public Collection<AiItem> getItems()
	{	Collection<AiItem> result = Collections.unmodifiableCollection(items.values());
		return result;	
	}
	
	/**
	 * renvoie la représentation de l'item passé en paramètre.
	 * @param item	l'item dont on veut la représentation
	 * @return	le AiItem correspondant
	 */
	AiItem getItem(Item item)
	{	return items.get(item);
	}
	
	/**
	 * ajoute un item dans la liste d'items de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param item	l'item à rajouter à la liste
	 */
	void addItem(AiItem item)
	{	items.put(item.getSprite(),item);	
	}
	
	/**
	 * démarque toutes les représentations de sprites d'une liste determinée en fonction du type
	 * T param�trant cette méthode. méthode appelée au début de la mise à jour :
	 * les représentations de sprites qui n'ont pas été marquées à la fin de la mise à jour
	 * correspondent à des sprites qui ne font plus partie du jeu, et doivent être
	 * supprim�es de cette représentation.
	 * 
	 * @param <T>	type de la liste à traiter
	 * @param list	liste à traiter
	 */
	private <U extends Sprite, T extends AiSprite<?>> void uncheckAll(HashMap<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	T temp = it.next().getValue();
			temp.uncheck();
		}
	}
	/**
	 * méthode compl�mentaire de uncheckAll, et chargée de supprimer
	 * les représentations de sprites non-marquées à la fin de la mise à jour.
	 * 
	 * @param <T>	type de la liste à traiter
	 * @param list	liste à traiter
	 */
	private <U extends Sprite, T extends AiSprite<?>> void removeUnchecked(HashMap<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	T temp = it.next().getValue();
			if(!temp.isChecked())
			{	//Sprite sprite = temp.getSprite();
				//if(sprite.isEnded())
					it.remove();
			}
		}
	}

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
	private void initOwnHero()
	{	PredefinedColor color = player.getColor(); 
		Iterator<Entry<Hero,AiHero>> i = heroes.entrySet().iterator();
		boolean found = false;
		while(i.hasNext() && !found)
		{	AiHero temp = i.next().getValue();
			if(temp.getColor()==color)
			{	ownHero = temp;
				found = true;
			}
		}
	}
}
