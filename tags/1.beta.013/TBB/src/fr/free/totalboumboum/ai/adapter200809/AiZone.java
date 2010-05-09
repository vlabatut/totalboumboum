package fr.free.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.player.Player;

/**
 * repr�sente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'IA a acc�s.
 * <p>
 * A chaque fois que l'IA est sollicit�e par le jeu pour conna�tre l'action
 * qu'elle veut effectuer, cette repr�sentation est mise � jour. L'IA ne re�oit
 * pas une nouvelle AiZone : l'AiZone existante est modifi�e en fonction de l'�volution
 * du jeu. De la m�me fa�on, les cases (AiTile) restent les m�mes, ainsi que les sprites et
 * les autres objets. Si l'IA a besoin d'une trace des �tats pr�c�dents du jeu, son
 * concepteur doit se charger de l'impl�menter lui-m�me.
 * 
 * @author Vincent
 *
 */

public class AiZone
{	/** niveau repr�sent� par cette classe */
	private Level level;
	/** joueur contr�l� par l'IA */
	private Player player;
	
	/**
	 * construit une repr�sentation du niveau pass� en param�tre,
	 * du point de vue du joueur pass� en param�tre.
	 * @param level	niveau � repr�senter
	 * @param player	joueur dont le point de vue est � adopter
	 */
	AiZone(Level level, Player player)
	{	this.level = level;
		this.player = player;
		initMatrix();
		updateMatrix();
		initOwnHero();
	}
	
	/**
	 * met � jour cette repr�sentation ainsi que tous ses constituants.
	 */
	void update(long elapsedTime)
	{	updateTime(elapsedTime);
		updateMatrix();
	}
	
	/**
	 * termine proprement cette repr�sentation (une fois que l'IA n'en a plus besoin).
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
	/** temps �coul� depuis la mise � jour pr�c�dente */
	private long elapsedTime = 0;
	
	/**
	 * renvoie le temps �coul� depuis la mise � jour pr�c�dente
	 * @return	le temps �coul� exprim� en millisecondes
	 */
	public long getElapsedTime()
	{	return elapsedTime;		
	}
	
	/**
	 * met � jour le temps �coul� depuis la derni�re mise � jour
	 * @param elapsedTime
	 */
	private void updateTime(long elapsedTime)
	{	this.elapsedTime = elapsedTime;		
	}

	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice repr�sentant la zone et tous les sprites qu'elle contient */
	private AiTile[][] matrix;
	/** hauteur totale de la zone de jeu exprim�e en cases (ie: nombre de lignes) */
	private int height;
	/** largeur totale de la zone de jeu exprim�e en cases (ie: nombre de colonnes) */
	private int width;
	
	/** 
	 * initialise cette repr�sentation de la zone en fonction du niveau pass� en param�tre
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
	 * met � jour la matrice en fonction de l'�volution du jeu
	 */
	private void updateMatrix()
	{	// d�marque tous les sprites
		uncheckAll(blocks);
		uncheckAll(bombs);
		uncheckAll(fires);
		uncheckAll(floors);
		uncheckAll(heroes);
		uncheckAll(items);
		// met � jour chaque case et sprite 
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].update();
		// supprime les sprites non-marqu�s
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
	 * renvoie la hauteur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 *  de la zone de jeu exprim�e en cases (ie: nombre de lignes)
	 *  
	 *  @return	hauteur de la zone
	 */
	public int getHeigh()
	{	return height;	
	}
	
	/** 
	 * renvoie la largeur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 *  de la zone de jeu exprim�e en cases (ie: nombre de colonnes)
	 *  
	 *  @return	largeur de la zone
	 */
	public int getWidth()
	{	return width;	
	}
	
	/**
	 * renvoie la case situ�e dans la zone � la position pass�e en param�tre.
	 *   
	 *  @param	line	num�ro de la ligne contenant la case � renvoyer
	 *  @param	col	num�ro de la colonne contenant la case � renvoyer
	 *  @return	case situ�e aux coordonn�es sp�cifi�es en param�tres
	 */
	public AiTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	/**
	 * renvoie le voisin de la case pass�e en param�tre, situ� dans la direction
	 * pass�e en param�tre.
	 * <p>
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @param line	ligne de la case dont on veut le voisin
	 * @param col	colonne de la case dont on veut le voisin
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de la case pass�e en param�tre et situ� dans la direction indiqu�e
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
	 * renvoie la liste des voisins de la case pass�e en param�tre.
	 * Il s'agit des voisins directs situ�s en haut, � gauche, en bas et � droite.
	 * <p>
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @param tile	la case dont on veut les voisins
	 * @return	la liste des voisins situ�s en haut, � gauche, en bas et � droite de la case pass�e en param�tre
	 */
	public Collection<AiTile> getNeighborTiles(AiTile tile)
	{	Collection<AiTile> result = new ArrayList<AiTile>();
		ArrayList<Direction> directions = Direction.getAllPrimaries();
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
	 * renvoie la direction de la case target relativement � la case source.
	 * Par exemple, la case target de coordonn�es (5,5) est � droite de
	 * la case source de coordonn�es (5,6).
	 * <p>
	 * Cette fonction peut �tre utile quand on veut savoir dans quelle direction
	 * il faut se d�placer pour aller de source � target.
	 * <p>
	 * ATTENTION 1 : si les deux cases ne sont pas des voisines directes (ie. ayant un cot� commun),
	 * il est possible que cette m�thode renvoie une direction composite,
	 * c'est � dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. R�f�rez-vous � 
	 * la classe Direction pour plus d'informations sur ces valeurs. 
	 * <p>
	 * ATTENTION 2 : comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette m�thode renvoie la direction du plus court chemin (sans consid�rer les �ventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le r�sultat sera
	 * RIGHT, car LEFT permet �galement d'atteindre la case, mais en parcourant un chemin plus long. 
	 * 
	 * @param source	case de r�f�rence
	 * @param target	case dont on veut connaitre la direction
	 * @return	la direction de target par rapport � source
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
		int distIndirY = getHeigh()-distDirY;
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
	 * (la liste peut �tre vide). 
	 * Cette instance de liste change � chaque appel de l'IA. 
	 * Il ne faut donc pas r�utiliser la m�me liste, mais redemander la nouvelle
	 * liste en utilisant cette m�thode.
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	public Collection<AiBlock> getBlocks()
	{	Collection<AiBlock> result = Collections.unmodifiableCollection(blocks.values());
		return result;	
	}
	
	/**
	 * renvoie la repr�sentation du bloc pass� en param�tre.
	 * @param block	le bloc dont on veut la repr�sentation
	 * @return	le AiBlock correspondant
	 */
	AiBlock getBlock(Block block)
	{	return blocks.get(block);
	}
	
	/**
	 * ajoute un bloc dans la liste de blocs de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param block	le bloc � rajouter � la liste
	 */
	void addBlock(AiBlock block)
	{	blocks.put(block.getSprite(),block);	
	}
	
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut �tre vide)
	 * Cette instance de liste change � chaque appel de l'IA. 
	 * Il ne faut donc pas r�utiliser la m�me liste, mais redemander la nouvelle
	 * liste en utilisant cette m�thode.
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	public Collection<AiBomb> getBombs()
	{	Collection<AiBomb> result = Collections.unmodifiableCollection(bombs.values());
		return result;	
	}
	
	/**
	 * renvoie la repr�sentation de la bombe pass�e en param�tre.
	 * @param bomb	la bombz dont on veut la repr�sentation
	 * @return	le AiBomb correspondant
	 */
	AiBomb getBomb(Bomb bomb)
	{	return bombs.get(bomb);
	}
	
	/**
	 * ajoute une bombe dans la liste de bombes de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param bomb	la bombe � rajouter � la liste
	 */
	void addBomb(AiBomb bomb)
	{	bombs.put(bomb.getSprite(),bomb);	
	}
	
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * Cette instance de liste change � chaque appel de l'IA. 
	 * Il ne faut donc pas r�utiliser la m�me liste, mais redemander la nouvelle
	 * liste en utilisant cette m�thode.
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	public Collection<AiFire> getFires()
	{	Collection<AiFire> result = Collections.unmodifiableCollection(fires.values());
		return result;	
	}
	
	/**
	 * renvoie la repr�sentation du feu pass� en param�tre.
	 * @param fire	le feu dont on veut la repr�sentation
	 * @return	le AiFire correspondant
	 */
	AiFire getFire(Fire fire)
	{	return fires.get(fire);
	}
	
	/**
	 * ajoute un feu dans la liste de feux de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param fire	le feu � rajouter � la liste
	 */
	void addFire(AiFire fire)
	{	fires.put(fire.getSprite(),fire);	
	}
	
	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * Cette instance de liste change � chaque appel de l'IA. 
	 * Il ne faut donc pas r�utiliser la m�me liste, mais redemander la nouvelle
	 * liste en utilisant cette m�thode.
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	public Collection<AiFloor> getFloors()
	{	Collection<AiFloor> result = Collections.unmodifiableCollection(floors.values());
		return result;	
	}
	
	/**
	 * renvoie la repr�sentation du sol pass� en param�tre.
	 * @param floor	le sol dont on veut la repr�sentation
	 * @return	le AiFloor correspondant
	 */
	AiFloor getFloor(Floor floor)
	{	return floors.get(floor);
	}
	
	/**
	 * ajoute un sol dans la liste de sols de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param floor	le sol � rajouter � la liste
	 */
	void addFloor(AiFloor floor)
	{	floors.put(floor.getSprite(),floor);	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone 
	 * (les joueurs �limin�s n'apparaissent plus dans cette liste ni dans cette repr�sentation de la zone)
	 * Cette instance de liste change � chaque appel de l'IA. 
	 * Il ne faut donc pas r�utiliser la m�me liste, mais redemander la nouvelle
	 * liste en utilisant cette m�thode.
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	public Collection<AiHero> getHeroes()
	{	Collection<AiHero> result = Collections.unmodifiableCollection(heroes.values());
		return result;	
	}
	
	/**
	 * renvoie la repr�sentation du personnage pass� en param�tre.
	 * @param hero	le personnage dont on veut la repr�sentation
	 * @return	le AiHero correspondant
	 */
	AiHero getHero(Hero hero)
	{	return heroes.get(hero);
	}
	
	/**
	 * ajoute un personnage dans la liste de personnages de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param hero	le personnage � rajouter � la liste
	 */
	void addHero(AiHero hero)
	{	heroes.put(hero.getSprite(),hero);	
	}
	
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * Cette instance de liste change � chaque appel de l'IA. 
	 * Il ne faut donc pas r�utiliser la m�me liste, mais redemander la nouvelle
	 * liste en utilisant cette m�thode.
	 * 
	 * @return	liste de tous les items contenus dans cette zone
	 */
	public Collection<AiItem> getItems()
	{	Collection<AiItem> result = Collections.unmodifiableCollection(items.values());
		return result;	
	}
	
	/**
	 * renvoie la repr�sentation de l'item pass� en param�tre.
	 * @param item	l'item dont on veut la repr�sentation
	 * @return	le AiItem correspondant
	 */
	AiItem getItem(Item item)
	{	return items.get(item);
	}
	
	/**
	 * ajoute un item dans la liste d'items de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param item	l'item � rajouter � la liste
	 */
	void addItem(AiItem item)
	{	items.put(item.getSprite(),item);	
	}
	
	/**
	 * d�marque toutes les repr�sentations de sprites d'une liste determin�e en fonction du type
	 * T param�trant cette m�thode. M�thode appel�e au d�but de la mise � jour :
	 * les repr�sentations de sprites qui n'ont pas �t� marqu�es � la fin de la mise � jour
	 * correspondent � des sprites qui ne font plus partie du jeu, et doivent �tre
	 * supprim�es de cette repr�sentation.
	 * 
	 * @param <T>	type de la liste � traiter
	 * @param list	liste � traiter
	 */
	private <U extends Sprite, T extends AiSprite<?>> void uncheckAll(HashMap<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	T temp = it.next().getValue();
			temp.uncheck();
		}
	}
	/**
	 * m�thode compl�mentaire de uncheckAll, et charg�e de supprimer
	 * les repr�sentations de sprites non-marqu�es � la fin de la mise � jour.
	 * 
	 * @param <T>	type de la liste � traiter
	 * @param list	liste � traiter
	 */
	private <U extends Sprite, T extends AiSprite<?>> void removeUnchecked(HashMap<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	T temp = it.next().getValue();
			if(!temp.isChecked())
			{	Sprite sprite = temp.getSprite();
				if(sprite.isEnded() || sprite.isToBeRemovedFromTile())
					it.remove();
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contr�l� par l'IA */
	private AiHero ownHero;

	/** 
	 * renvoie le personnage qui est contr�l� par l'IA
	 */
	public AiHero getOwnHero()
	{	return ownHero;	
	}
	
	/**
	 * initialise le personnage qui est contr�l� par l'IA
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
