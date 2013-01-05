package org.totalboumboum.ai.v201112.adapter.data.internal;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiFloor;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
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
import org.totalboumboum.tools.computing.CombinatoricsTools;
import org.totalboumboum.tools.computing.LevelsTools;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * représente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'agent a accès.
 * <br/>
 * A chaque fois que l'agent est sollicité par le jeu pour connaître l'action
 * qu'elle veut effectuer, cette représentation est mise à jour. L'agent ne reçoit
 * pas une nouvelle AiZone : l'AiZone existante est modifiée en fonction de l'évolution
 * du jeu. De la même façon, les cases (AiTile) restent les mêmes, ainsi que les sprites et
 * les autres objets. Si l'agent a besoin d'une trace des états précédents du jeu, son
 * concepteur doit se charger de l'implémenter lui-même.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AiDataZone extends AiZone
{	
	/**
	 * construit une représentation du niveau passé en paramètre,
	 * du point de vue du joueur passé en paramètre.
	 * 
	 * @param level
	 * 		niveau à représenter
	 * @param player
	 * 		joueur dont le point de vue est à adopter
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
	 * met à jour cette représentation ainsi que tous ses constituants.
	 * usage interne, méthode non-destinée à la création des agent.
	 * 
	 * @param elapsedTime
	 * 		le temps écoulé
	 */
	public void update(long elapsedTime)
	{	updateTimes(elapsedTime);
		updateMatrix(elapsedTime);
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
	 * met à jour les données temporelles
	 * 
	 * @param elapsedTime
	 * 		le temps écoulé
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
	 * met à jour des données qui ne sont pas directement reliées
	 * à l'action en cours, telles que l'évolution du classement des joueurs
	 */
	private void updateMeta()
	{	List<AbstractPlayer> players = level.getLoop().getPlayers();
		// stats
		statsRanks.clear();
		RankingService rankingService = GameStatistics.getRankingService();
		// les stats n'ont pas été chargées
		if(rankingService==null)
		{	for(int i=0;i<players.size();i++)
			{	AbstractPlayer player = players.get(i);
				Hero hero = (Hero)player.getSprite();
				AiHero aiHero = heroMap.get(hero);
				statsRanks.put(aiHero,0);
				roundRanks.put(aiHero,0);
				matchRanks.put(aiHero,0);
			}
		}
		// les stats ont été chargées
		else
		{	for(int i=0;i<players.size();i++)
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
			int ranks[] = CombinatoricsTools.getRanks(points);
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
			ranks = CombinatoricsTools.getRanks(points);
			for(int i=0;i<players.size();i++)
			{	Hero hero = (Hero)players.get(i).getSprite();
				AiDataHero aiHero = heroMap.get(hero);
				int rank = ranks[i];
				matchRanks.put(aiHero,rank);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** joueur contrôlé par l'agent */
	private AbstractPlayer player;
	
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** niveau représenté par cette classe */
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice représentant la zone et tous les sprites qu'elle contient */
	private AiDataTile[][] matrix;
	
	@Override
	public AiDataTile[][] getMatrix()
	{	return matrix;
	}
	
	/** 
	 * initialise cette représentation de la zone en fonction du niveau passé en paramètre
	 */
	private void initMatrix()
	{	Tile[][] m = level.getMatrix();
		height = level.getGlobalHeight();
		width = level.getGlobalWidth();
		pixelLeftX = level.getPixelLeftX();
		pixelTopY = level.getPixelTopY();
		pixelWidth = level.getPixelWidth();
		pixelHeight = level.getPixelHeight();
		matrix = new AiDataTile[height][width];
		for(int rowIndex=0;rowIndex<height;rowIndex++)
		{	for(int colIndex=0;colIndex<width;colIndex++)
			{	Tile tile = m[rowIndex][colIndex];
				AiDataTile aiTile = new AiDataTile(tile,this);
				matrix[rowIndex][colIndex] = aiTile;
				tiles.add(aiTile);
			}
		}
		
		for(int rowIndex=0;rowIndex<height;rowIndex++)
			for(int colIndex=0;colIndex<width;colIndex++)
				matrix[rowIndex][colIndex].initNeighbors();
	}	
	
	/**
	 * met à jour la matrice en fonction de l'évolution du jeu
	 * 
	 * @param elapsedTime
	 * 		le temps écoulé
	 */
	private void updateMatrix(long elapsedTime)
	{	// réinitialise les compteurs d'items
		hiddenItemsCount = 0;
		hiddenItemsCounts.clear();
		
		// démarque tous les sprites
		uncheckAll(blockMap);
		uncheckAll(bombMap);
		uncheckAll(fireMap);
		uncheckAll(floorMap);
		uncheckAll(heroMap);
		uncheckAll(itemMap);
		
		// met à jour chaque case et sprite 
		for(int row=0;row<height;row++)
			for(int col=0;col<width;col++)
				matrix[row][col].update(elapsedTime);
		
		// supprime les sprites non-marqués
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
	/** liste de toutes les cases de cette zone */
	private final List<AiTile> tiles = new LinkedList<AiTile>(); 
	
	@Override
	public List<AiTile> getTiles()
	{	return tiles;
	}

	@Override
	public AiDataTile getTile(int row, int col)
	{	return matrix[row][col];
	}
	
	@Override
	public AiDataTile getTile(double x, double y)
	{	int[] coord = LevelsTools.getTile(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth,height,width);
		AiDataTile result = matrix[coord[0]][coord[1]];
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des blocks contenus dans cette zone */
	private final HashMap<Block,AiDataBlock> blockMap = new HashMap<Block,AiDataBlock>();
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiBlock> blockList = new ArrayList<AiBlock>();
	/** liste externe des blocks destructibles contenus dans cette zone */
	private final List<AiBlock> softBlockList = new ArrayList<AiBlock>();
	
	@Override
	public List<AiBlock> getBlocks()
	{	return blockList;	
	}
	
	@Override
	public List<AiBlock> getDestructibleBlocks()
	{	return softBlockList;
	}
	
	/**
	 * met à jour la liste externe des blocs
	 */
	private void updateBlockLists()
	{	// reinit lists
		blockList.clear();
		softBlockList.clear();
		
		// fill them
		for(Entry<Block,AiDataBlock> entry: blockMap.entrySet())
		{	AiDataBlock block = entry.getValue();
			blockList.add(block);
			if(block.isDestructible())
				softBlockList.add(block);
		}
	}
	
	/**
	 * renvoie la représentation du bloc passé en paramètre.
	 * 
	 * @param block
	 * 		le bloc dont on veut la représentation
	 * @return	
	 * 		le AiBlock correspondant
	 */
	protected AiDataBlock getBlock(Block block)
	{	return blockMap.get(block);
	}
	
	/**
	 * ajoute un bloc dans la liste de blocs de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param block
	 * 		le bloc à rajouter à la liste
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
	
	@Override
	public List<AiBomb> getBombs()
	{	return bombList;	
	}
	
	/**
	 * met à jour la liste externe des bombes
	 */
	private void updateBombList()
	{	bombList.clear();
		for(Entry<Bomb,AiDataBomb> entry: bombMap.entrySet())
		{	AiDataBomb bomb = entry.getValue();
			bombList.add(bomb);
		}
		
		threatenedBombs = null;
		delaysByBombs = null;
		bombsByDelays = null;
	}
	
	/**
	 * renvoie la représentation de la bombe passée en paramètre.
	 * 
	 * @param bomb
	 * 		la bombe dont on veut la représentation
	 * @return	
	 * 		le AiBomb correspondant
	 */
	protected AiDataBomb getBomb(Bomb bomb)
	{	return bombMap.get(bomb);
	}
	
	/**
	 * ajoute une bombe dans la liste de bombes de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param bomb
	 * 		la bombe à rajouter à la liste
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
	
	@Override
	public List<AiFire> getFires()
	{	return fireList;	
	}
	
	/**
	 * met à jour la liste externe des feux
	 */
	private void updateFireList()
	{	fireList.clear();
		for(Entry<Fire,AiDataFire> entry: fireMap.entrySet())
		{	AiDataFire fire = entry.getValue();
			fireList.add(fire);
		}
	}
	
	/**
	 * renvoie la représentation du feu passé en paramètre.
	 * 
	 * @param fire
	 * 		le feu dont on veut la représentation
	 * @return	
	 * 		le AiFire correspondant
	 */
	protected AiDataFire getFire(Fire fire)
	{	return fireMap.get(fire);
	}
	
	/**
	 * ajoute un feu dans la liste de feux de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param fire
	 * 		le feu à rajouter à la liste
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

	@Override
	public List<AiFloor> getFloors()
	{	return floorList;	
	}
	
	/**
	 * met à jour la liste externe des sols
	 */
	private void updateFloorList()
	{	floorList.clear();
		for(Entry<Floor,AiDataFloor> entry: floorMap.entrySet())
		{	AiDataFloor floor = entry.getValue();
			floorList.add(floor);
		}
	}
	
	/**
	 * renvoie la représentation du sol passé en paramètre.
	 * 
	 * @param floor
	 * 		le sol dont on veut la représentation
	 * @return	
	 * 		le AiFloor correspondant
	 */
	protected AiDataFloor getFloor(Floor floor)
	{	return floorMap.get(floor);
	}
	
	/**
	 * ajoute un sol dans la liste de sols de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param floor
	 * 		le sol à rajouter à la liste
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
	
	@Override
	public List<AiHero> getHeroes()
	{	return heroList;	
	}
	
	@Override
	public List<AiHero> getRemainingHeroes()
	{	return remainingHeroList;	
	}
	
	@Override
	public List<AiHero> getRemainingOpponents()
	{	List<AiHero> result = new ArrayList<AiHero>(remainingHeroList);
		result.remove(ownHero);
		return result;	
	}
	
	/**
	 * met à jour les listes externes des personnages
	 */
	private void updateHeroLists()
	{	// reset lists
		heroList.clear();
		remainingHeroList.clear();
		
		// update them
		for(Entry<Hero,AiDataHero> entry: heroMap.entrySet())
		{	AiDataHero hero = entry.getValue();
			heroList.add(hero);
			if(!hero.hasEnded())
				remainingHeroList.add(hero);
		}
	}
	
	/**
	 * renvoie la représentation du personnage passé en paramètre.
	 * 
	 * @param hero
	 * 		le personnage dont on veut la représentation
	 * @return	
	 * 		le AiHero correspondant
	 */
	protected AiDataHero getHero(Hero hero)
	{	return heroMap.get(hero);
	}
	
	/**
	 * ajoute un personnage dans la liste de personnages de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param hero
	 * 		le personnage à rajouter à la liste
	 */
	protected void addHero(AiDataHero hero)
	{	heroMap.put(hero.getSprite(),hero);	
	}
	
	@Override
	public AiHero getHeroByColor(PredefinedColor color)
	{	AiHero result = null;
		Iterator<AiHero> it = heroList.iterator();
		while(result==null && it.hasNext())
		{	AiHero hero = it.next();
			if(hero.getColor()==color)
				result = hero;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des items contenus dans cette zone */
	private final HashMap<Item,AiDataItem> itemMap = new HashMap<Item,AiDataItem>();
	/** liste externe des items contenus dans cette zone */
	private final List<AiItem> itemList = new ArrayList<AiItem>();
	
	@Override
	public List<AiItem> getItems()
	{	return itemList;	
	}
	
	/**
	 * met à jour la liste externe des items
	 */
	private void updateItemList()
	{	itemList.clear();
		for(Entry<Item,AiDataItem> entry: itemMap.entrySet())
		{	AiDataItem item = entry.getValue();
			itemList.add(item);
		}
	}
	
	/**
	 * renvoie la représentation de l'item passé en paramètre.
	 * 
	 * @param item
	 * 		l'item dont on veut la représentation
	 * @return	
	 * 		le AiItem correspondant
	 */
	protected AiDataItem getItem(Item item)
	{	return itemMap.get(item);
	}
	
	/**
	 * ajoute un item dans la liste d'items de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param item
	 * 		l'item à rajouter à la liste
	 */
	protected void addItem(AiDataItem item)
	{	itemMap.put(item.getSprite(),item);	
	}
	
	/**
	 * Permet de modifier le nombre d'items encore cachés dans ce niveau.
	 * 
	 * @param type
	 * 		Le type d'item concerné.
	 */
	protected void signalHiddenItem(AiItemType type)
	{	// general count
		hiddenItemsCount++;
		
		// type-related count
		Integer temp = hiddenItemsCounts.get(type);
		if(temp==null)
			temp = 1;
		else
			temp ++;
		hiddenItemsCounts.put(type,temp);
	}

	/////////////////////////////////////////////////////////////////
	// ALL SPRITES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * démarque toutes les représentations de sprites d'une liste determinée en fonction du type
	 * T paramétrant cette méthode. Méthode appelée au début de la mise à jour :
	 * les représentations de sprites qui n'ont pas été marquées à la fin de la mise à jour
	 * correspondent à des sprites qui ne font plus partie du jeu, et doivent être
	 * supprimées de cette représentation.
	 * 
	 * @param <U> 
	 * @param <T>	
	 * 		type de la liste à traiter
	 * 
	 * @param list
	 * 		liste à traiter
	 */
	private <U extends Sprite, T extends AiDataSprite<?>> void uncheckAll(HashMap<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	T temp = it.next().getValue();
			temp.uncheck();
		}
	}
	/**
	 * méthode complémentaire de uncheckAll, et chargée de supprimer
	 * les représentations de sprites non-marquées à la fin de la mise à jour.
	 * @param <U> 
	 * @param <T>
	 * 		type de la liste à traiter
	 * 
	 * @param list
	 * 		liste à traiter
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
	 * met à jour toutes les listes externes de sprites
	 */
	private void updateSpriteLists()
	{	updateBlockLists();
		updateBombList();
		updateFireList();
		updateFloorList();
		updateHeroLists();
		updateItemList();
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contrôlé par l'agent */
	private AiDataHero ownHero;

	@Override
	public AiDataHero getOwnHero()
	{	return ownHero;	
	}
	
	/**
	 * initialise le personnage qui est contrôlé par l'agent
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
	 * termine proprement cette représentation (une fois que l'agent n'en a plus besoin).
	 */
	public void finish()
	{	// matrix
		for(int row=0;row<height;row++)
			for(int col=0;col<width;col++)
				matrix[row][col].finish();
		
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
