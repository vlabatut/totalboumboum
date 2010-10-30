package org.totalboumboum.ai.v201011.adapter.data.actual;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.game.limit.LimitTime;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.tools.calculus.CalculusTools;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * repr�sente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'IA a acc�s.</br>
 * 
 * A chaque fois que l'IA est sollicit�e par le jeu pour conna�tre l'action
 * qu'elle veut effectuer, cette repr�sentation est mise � jour. L'IA ne re�oit
 * pas une nouvelle AiZone : l'AiZone existante est modifi�e en fonction de l'�volution
 * du jeu. De la m�me fa�on, les cases (AiTile) restent les m�mes, ainsi que les sprites et
 * les autres objets. Si l'IA a besoin d'une trace des �tats pr�c�dents du jeu, son
 * concepteur doit se charger de l'impl�menter lui-m�me.
 * 
 * @author Vincent Labatut
 *
 */
public final class AiDataZone extends AiZone
{	
	/**
	 * construit une repr�sentation du niveau pass� en param�tre,
	 * du point de vue du joueur pass� en param�tre.
	 * 
	 * @param level	niveau � repr�senter
	 * @param player	joueur dont le point de vue est � adopter
	 */
	public AiDataZone(Level level, AbstractPlayer player)
	{	this.level = level;
		this.player = player;
		initMatrix();
		initTime();
//		updateMatrix();
		initOwnHero();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met � jour cette repr�sentation ainsi que tous ses constituants.
	 * usage interne, m�thode non-destin�e � la cr�ation des IA.
	 */
	public void update(long elapsedTime)
	{	updateTimes(elapsedTime);
		updateMatrix();
		updateSpriteLists();
		updateMeta();
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * initialise les constantes temporelles pour la partie en cours
	 */
	private void initTime()
	{	// init
		VisibleLoop loop = level.getLoop();
		
		// time limit
		this.limitTime = Long.MAX_VALUE;
		LimitTime lt = loop.getRound().getLimits().getTimeLimit();
		if(lt!=null)
			this.limitTime = lt.getThreshold();
	}
	
	/**
	 * met � jour les donn�es temporelles
	 * 
	 * @param elapsedTime
	 */
	private void updateTimes(long elapsedTime)
	{	// init
		VisibleLoop loop = level.getLoop();
		
		// total time
		this.totalTime = loop.getTotalGameTime();
		
		// elapsed time
		this.elapsedTime = elapsedTime;
	}

	/////////////////////////////////////////////////////////////////
	// META DATA		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * met � jour des donn�es qui ne sont pas directement reli�es
	 * � l'action en cours, telles que l'�volution du classement des joueurs
	 */
	private void updateMeta()
	{	List<AbstractPlayer> players = level.getLoop().getPlayers();
		// stats
		statsRanks.clear();
		RankingService rankingService = GameStatistics.getRankingService();
		for(int i=0;i<players.size();i++)
		{	AbstractPlayer player = players.get(i);
			Hero hero = (Hero)player.getSprite();
			AiDataHero aiHero = heroMap.get(hero);
			String playerId = player.getId();
			int rank = rankingService.getPlayerRank(playerId);
			statsRanks.put(aiHero,rank);
		}
		// round
		roundRanks.clear();
		Round round = level.getLoop().getRound();
		float points[] = round.getCurrentPoints();
		int ranks[] = CalculusTools.getRanks(points);
		for(int i=0;i<players.size();i++)
		{	Hero hero = (Hero)players.get(i).getSprite();
			AiDataHero aiHero = heroMap.get(hero);
			int rank = ranks[i];
			roundRanks.put(aiHero,rank);
		}
		// match
		matchRanks.clear();
		Match match = round.getMatch();
		points = match.getStats().getTotal();
		ranks = CalculusTools.getRanks(points);
		for(int i=0;i<players.size();i++)
		{	Hero hero = (Hero)players.get(i).getSprite();
			AiDataHero aiHero = heroMap.get(hero);
			int rank = ranks[i];
			matchRanks.put(aiHero,rank);
		}
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** joueur contr�l� par l'IA */
	private AbstractPlayer player;
	
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** niveau repr�sent� par cette classe */
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice repr�sentant la zone et tous les sprites qu'elle contient */
	private AiDataTile[][] matrix;
	
	/**
	 * renvoie la matrice de cases repr�sentant la zone de jeu
	 * 
	 * @return	la matrice correspondant � la zone de jeu
	 */
	@Override
	public AiDataTile[][] getMatrix()
	{	return matrix;
	}
	
	/** 
	 * initialise cette repr�sentation de la zone en fonction du niveau pass� en param�tre
	 */
	private void initMatrix()
	{	Tile[][] m = level.getMatrix();
		height = level.getGlobalHeight();
		width = level.getGlobalWidth();
		matrix = new AiDataTile[height][width];
		for(int lineIndex=0;lineIndex<height;lineIndex++)
		{	for(int colIndex=0;colIndex<width;colIndex++)
			{	Tile tile = m[lineIndex][colIndex];
				AiDataTile aiTile = new AiDataTile(tile,this);
				matrix[lineIndex][colIndex] = aiTile;
			}
		}
		
		for(int lineIndex=0;lineIndex<height;lineIndex++)
			for(int colIndex=0;colIndex<width;colIndex++)
				matrix[lineIndex][colIndex].initNeighbors();
	}	
	
	/**
	 * met � jour la matrice en fonction de l'�volution du jeu
	 */
	private void updateMatrix()
	{	hiddenItemsCount = 0;
		// d�marque tous les sprites
		uncheckAll(blockMap);
		uncheckAll(bombMap);
		uncheckAll(fireMap);
		uncheckAll(floorMap);
		uncheckAll(heroMap);
		uncheckAll(itemMap);
		// met � jour chaque case et sprite 
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].update();
		// supprime les sprites non-marqu�s
		removeUnchecked(blockMap);
		removeUnchecked(bombMap);
		removeUnchecked(fireMap);
		removeUnchecked(floorMap);
		removeUnchecked(heroMap);
		removeUnchecked(itemMap);
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la case situ�e dans la zone � la position pass�e en param�tre.
	 *   
	 *  @param	line	num�ro de la ligne contenant la case � renvoyer
	 *  @param	col	num�ro de la colonne contenant la case � renvoyer
	 *  @return	case situ�e aux coordonn�es sp�cifi�es en param�tres
	 */
	@Override
	public AiDataTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	/**
	 * renvoie la case qui contient le pixel pass� en param�tre
	 *   
	 *  @param	x	abscisse du pixel concern�
	 *  @param	y	ordonn�e du pixel concern�
	 *  @return	case contenant le pixel situ� aux coordonn�es sp�cifi�es en param�tres
	 */
	@Override
	public AiDataTile getTile(double x, double y)
	{	Tile tile = level.getTile(x, y);
		int line = tile.getLine();
		int col = tile.getCol();
		AiDataTile result = matrix[line][col];
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des blocks contenus dans cette zone */
	private final HashMap<Block,AiDataBlock> blockMap = new HashMap<Block,AiDataBlock>();
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiBlock> blockList = new ArrayList<AiBlock>();
	
	/** 
	 * renvoie la liste des blocks contenus dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	@Override
	public List<AiBlock> getBlocks()
	{	return blockList;	
	}
	
	/** 
	 * renvoie la liste des blocks destructibles contenus dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs destructibles contenus dans cette zone
	 */
	@Override
	public List<AiBlock> getDestructibleBlocks()
	{	List<AiBlock> result = new ArrayList<AiBlock>();

		for(AiBlock block: blockList)
		{	if(block.isDestructible())
				result.add(block);
		}
		
		return result;
	}
	
	/**
	 * met � jour la liste externe des blocs
	 */
	private void updateBlockList()
	{	blockList.clear();
		for(Entry<Block,AiDataBlock> entry: blockMap.entrySet())
		{	AiDataBlock block = entry.getValue();
			blockList.add(block);
		}
	}
	
	/**
	 * renvoie la repr�sentation du bloc pass� en param�tre.
	 * 
	 * @param block	le bloc dont on veut la repr�sentation
	 * @return	le AiBlock correspondant
	 */
	protected AiDataBlock getBlock(Block block)
	{	return blockMap.get(block);
	}
	
	/**
	 * ajoute un bloc dans la liste de blocs de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param block	le bloc � rajouter � la liste
	 */
	protected void addBlock(AiDataBlock block)
	{	blockMap.put(block.getSprite(),block);	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des bombes contenues dans cette zone */
	private final HashMap<Bomb,AiDataBomb> bombMap = new HashMap<Bomb,AiDataBomb>();
	/** liste externe des bombes contenues dans cette zone */
	private final List<AiBomb> bombList = new ArrayList<AiBomb>();
	
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	@Override
	public List<AiBomb> getBombs()
	{	return bombList;	
	}
	
	/**
	 * met � jour la liste externe des bombes
	 */
	private void updateBombList()
	{	bombList.clear();
		for(Entry<Bomb,AiDataBomb> entry: bombMap.entrySet())
		{	AiDataBomb bomb = entry.getValue();
			bombList.add(bomb);
		}
	}
	
	/**
	 * renvoie la repr�sentation de la bombe pass�e en param�tre.
	 * 
	 * @param bomb	la bombe dont on veut la repr�sentation
	 * @return	le AiBomb correspondant
	 */
	protected AiDataBomb getBomb(Bomb bomb)
	{	return bombMap.get(bomb);
	}
	
	/**
	 * ajoute une bombe dans la liste de bombes de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param bomb	la bombe � rajouter � la liste
	 */
	protected void addBomb(AiDataBomb bomb)
	{	bombMap.put(bomb.getSprite(),bomb);	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des feux contenus dans cette zone */
	private final HashMap<Fire,AiDataFire> fireMap = new HashMap<Fire,AiDataFire>();
	/** liste externe des feux contenus dans cette zone */
	private final List<AiFire> fireList = new ArrayList<AiFire>();
	
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	@Override
	public List<AiFire> getFires()
	{	return fireList;	
	}
	
	/**
	 * met � jour la liste externe des feux
	 */
	private void updateFireList()
	{	fireList.clear();
		for(Entry<Fire,AiDataFire> entry: fireMap.entrySet())
		{	AiDataFire fire = entry.getValue();
			fireList.add(fire);
		}
	}
	
	/**
	 * renvoie la repr�sentation du feu pass� en param�tre.
	 * 
	 * @param fire	le feu dont on veut la repr�sentation
	 * @return	le AiFire correspondant
	 */
	protected AiDataFire getFire(Fire fire)
	{	return fireMap.get(fire);
	}
	
	/**
	 * ajoute un feu dans la liste de feux de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param fire	le feu � rajouter � la liste
	 */
	protected void addFire(AiDataFire fire)
	{	fireMap.put(fire.getSprite(),fire);	
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des sols contenus dans cette zone */
	private final HashMap<Floor,AiDataFloor> floorMap = new HashMap<Floor,AiDataFloor>();
	/** liste externe des sols contenus dans cette zone */
	private final List<AiFloor> floorList = new ArrayList<AiFloor>();

	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	@Override
	public List<AiFloor> getFloors()
	{	return floorList;	
	}
	
	/**
	 * met � jour la liste externe des sols
	 */
	private void updateFloorList()
	{	floorList.clear();
		for(Entry<Floor,AiDataFloor> entry: floorMap.entrySet())
		{	AiDataFloor floor = entry.getValue();
			floorList.add(floor);
		}
	}
	
	/**
	 * renvoie la repr�sentation du sol pass� en param�tre.
	 * 
	 * @param floor	le sol dont on veut la repr�sentation
	 * @return	le AiFloor correspondant
	 */
	protected AiDataFloor getFloor(Floor floor)
	{	return floorMap.get(floor);
	}
	
	/**
	 * ajoute un sol dans la liste de sols de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param floor	le sol � rajouter � la liste
	 */
	protected void addFloor(AiDataFloor floor)
	{	floorMap.put(floor.getSprite(),floor);	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des personnages contenus dans cette zone */
	private final HashMap<Hero,AiDataHero> heroMap = new HashMap<Hero,AiDataHero>();
	/** liste externe de tous les personnages contenus dans cette zone */
	private final List<AiHero> heroList = new ArrayList<AiHero>();
	/** liste externe des personnages restant encore dans cette zone */
	private final List<AiHero> remainingHeroList = new ArrayList<AiHero>();
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone,
	 * y compris ceux qui ont �t� �limin�s. 
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	@Override
	public List<AiHero> getHeroes()
	{	return heroList;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone
	 */
	@Override
	public List<AiHero> getRemainingHeroes()
	{	return remainingHeroList;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu, et sauf le personnage contr�l� par l'IA.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone, sauf celui de l'IA
	 */
	@Override
	public List<AiHero> getRemainingOpponents()
	{	List<AiHero> result = new ArrayList<AiHero>(remainingHeroList);
		result.remove(ownHero);
		return result;	
	}
	
	/**
	 * met � jour les listes externes des personnages
	 */
	private void updateHeroLists()
	{	heroList.clear();
		remainingHeroList.clear();
		for(Entry<Hero,AiDataHero> entry: heroMap.entrySet())
		{	AiDataHero hero = entry.getValue();
			heroList.add(hero);
			if(!hero.hasEnded())
				remainingHeroList.add(hero);
		}
	}
	
	/**
	 * renvoie la repr�sentation du personnage pass� en param�tre.
	 * 
	 * @param hero	le personnage dont on veut la repr�sentation
	 * @return	le AiHero correspondant
	 */
	protected AiDataHero getHero(Hero hero)
	{	return heroMap.get(hero);
	}
	
	/**
	 * ajoute un personnage dans la liste de personnages de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param hero	le personnage � rajouter � la liste
	 */
	protected void addHero(AiDataHero hero)
	{	heroMap.put(hero.getSprite(),hero);	
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des items contenus dans cette zone */
	private final HashMap<Item,AiDataItem> itemMap = new HashMap<Item,AiDataItem>();
	/** liste externe des items contenus dans cette zone */
	private final List<AiItem> itemList = new ArrayList<AiItem>();
	/** nombre d'items cach�s, i.e. pas encore ramass�s */
	private int hiddenItemsCount;
	
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les items contenus dans cette zone
	 */
	@Override
	public List<AiItem> getItems()
	{	return itemList;	
	}
	
	/**
	 * met � jour la liste externe des items
	 */
	private void updateItemList()
	{	itemList.clear();
		for(Entry<Item,AiDataItem> entry: itemMap.entrySet())
		{	AiDataItem item = entry.getValue();
			itemList.add(item);
		}
	}
	
	/**
	 * renvoie la repr�sentation de l'item pass� en param�tre.
	 * 
	 * @param item	l'item dont on veut la repr�sentation
	 * @return	le AiItem correspondant
	 */
	protected AiDataItem getItem(Item item)
	{	return itemMap.get(item);
	}
	
	/**
	 * ajoute un item dans la liste d'items de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param item	l'item � rajouter � la liste
	 */
	protected void addItem(AiDataItem item)
	{	itemMap.put(item.getSprite(),item);	
	}
	
	/**
	 * permet de modifier le nombre d'items encore cach�s dans ce niveau
	 * 
	 * @param hiddenItemsCount	le nouveau nombre d'items cach�s dans le niveau
	 */
	protected void setHiddenItemsCount(int hiddenItemsCount)
	{	this.hiddenItemsCount = hiddenItemsCount;	
	}
	
	/**
	 * renvoie le nombre d'items cach�s restant dans le niveau.
	 * Il s'agit des items qui sont encore cach�s dans des blocs, 
	 * et qui n'ont pas �t� ramass�s. Cette information permet de
	 * savoir s'il est encore n�cessaire de faire exploser des blocs 
	 * pour trouver des items, ou pas.
	 * 
	 * @return	le nombre d'items restant � d�couvrir
	 */
	@Override
	public int getHiddenItemsCount()
	{	return hiddenItemsCount;		
	}
	
	/////////////////////////////////////////////////////////////////
	// ALL SPRITES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	private <U extends Sprite, T extends AiDataSprite<?>> void uncheckAll(HashMap<U,T> list)
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
	private <U extends Sprite, T extends AiDataSprite<?>> void removeUnchecked(HashMap<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	Entry<U,T> entry = it.next();
			T temp = entry.getValue();
			//U sprite = entry.getKey();
			if(!temp.isChecked())
			{	temp.setEnded();
				//Sprite sprite = temp.getSprite();
				//if(sprite.isEnded())
				if(!(temp instanceof AiDataHero)) //we always keep the hero, cause they may come back...
					it.remove();
			}
		}
	}

	/**
	 * met � jour toutes les listes externes de sprites
	 */
	private void updateSpriteLists()
	{	updateBlockList();
		updateBombList();
		updateFireList();
		updateFloorList();
		updateHeroLists();
		updateItemList();
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contr�l� par l'IA */
	private AiDataHero ownHero;

	/** 
	 * renvoie le personnage qui est contr�l� par l'IA
	 */
	@Override
	public AiDataHero getOwnHero()
	{	return ownHero;	
	}
	
	/**
	 * initialise le personnage qui est contr�l� par l'IA
	 */
	private void initOwnHero()
	{	PredefinedColor color = player.getColor(); 
		Iterator<Entry<Hero,AiDataHero>> i = heroMap.entrySet().iterator();
		boolean found = false;
		while(i.hasNext() && !found)
		{	AiDataHero temp = i.next().getValue();
			if(temp.getColor()==color)
			{	ownHero = temp;
				found = true;
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiDataZone)
		{	
//			AiZone zone = (AiZone)o;	
//			result = level==zone.level && player==zone.player;
			result = this==o;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cette repr�sentation (une fois que l'IA n'en a plus besoin).
	 */
	public void finish()
	{	// matrix
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].finish();
		
		// sprites
		blockMap.clear();
		bombMap.clear();
		fireMap.clear();
		floorMap.clear();
		heroMap.clear();
		itemMap.clear();
		ownHero = null;
		
		// misc
		level = null;
		player = null;
	}
}
