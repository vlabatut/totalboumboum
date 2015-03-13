package org.totalboumboum.ai.v201314.adapter.data.internal;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiFloor;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.SuddenDeathEvent;
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
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.tools.computing.RankingTools;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.level.PositionTools;

/**
 * Représente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'agent a accès.
 * <br/>
 * A chaque fois que l'agent est sollicité par le jeu pour connaître l'action
 * qu'elle veut effectuer, cette représentation est mise à jour. L'agent ne reçoit
 * pas une nouvelle {@code AiZone} : l'{@code AiZone} existante est modifiée en fonction de 
 * l'évolution du jeu. De la même façon, les cases ({@code AiTile}) restent les mêmes, ainsi 
 * que les sprites et les autres objets. Si l'agent a besoin d'une trace des états précédents 
 * du jeu, son concepteur doit se charger de l'implémenter lui-même.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AiDataZone extends AiZone
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit une représentation du niveau passé en paramètre,
	 * du point de vue du joueur passé en paramètre.
	 * 
	 * @param level
	 * 		Niveau à représenter.
	 * @param player
	 * 		Joueur dont le point de vue est à adopter.
	 */
	public AiDataZone(Level level, AbstractPlayer player)
	{	this.level = level;
		this.player = player;
		
		initTileSize(RoundVariables.scaledTileDimension);
		
		initMatrix();
		initTime();
//		updateMatrix();
		initOwnHero();
		initSuddenDeath();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour cette représentation ainsi que tous ses constituants.
	 * usage interne, méthode non-destinée à la création des agent.
	 * 
	 * @param elapsedTime
	 * 		Le temps écoulé.
	 */
	public void update(long elapsedTime)
	{	updateTimes(elapsedTime);
		updateMatrix(elapsedTime);
		updateSpriteLists();
		updateMeta();
		updateSuddenDeath();
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initialise les constantes temporelles pour la partie en cours
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
	 * Met à jour les données temporelles.
	 * 
	 * @param elapsedTime
	 * 		Le temps écoulé.
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
	 * Met à jour des données qui ne sont pas directement reliées
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
			int ranks[] = RankingTools.getRanks(points);
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
			ranks = RankingTools.getRanks(points);
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
	/** Joueur contrôlé par l'agent */
	private transient AbstractPlayer player;
	
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Niveau représenté par cette classe */
	private transient Level level;
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrice représentant la zone et tous les sprites qu'elle contient */
	private AiDataTile[][] matrix;
	
	@Override
	public AiDataTile[][] getMatrix()
	{	return matrix;
	}
	
	/** 
	 * Initialise cette représentation de la zone en fonction 
	 * du niveau passé en paramètre.
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
	 * Met à jour la matrice en fonction de l'évolution du jeu.
	 * 
	 * @param elapsedTime
	 * 		Le temps écoulé.
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
	/** Liste de toutes les cases de cette zone */
	private final List<AiTile> tiles = new LinkedList<AiTile>(); 
	/** Version immuable de la liste des cases */
	private final List<AiTile> externalTiles = Collections.unmodifiableList(tiles); 
	
	@Override
	public List<AiTile> getTiles()
	{	return externalTiles;
	}

	@Override
	public AiDataTile getTile(int row, int col)
	{	return matrix[row][col];
	}
	
	@Override
	public AiDataTile getTile(double x, double y)
	{	int[] coord = PositionTools.getTile(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth,height,width);
		AiDataTile result = matrix[coord[0]][coord[1]];
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map des blocks contenus dans cette zone */
	private final transient Map<Block,AiDataBlock> blockMap = new HashMap<Block,AiDataBlock>();
	/** Liste des blocks contenus dans cette zone */
	private final List<AiBlock> blockList = new ArrayList<AiBlock>();
	/** Version immuable de la liste des blocks */
	private final List<AiBlock> externalBlockList = Collections.unmodifiableList(blockList);
	/** Liste des blocks destructibles contenus dans cette zone */
	private final List<AiBlock> softBlockList = new ArrayList<AiBlock>();
	/** Version immuable de la liste des blocks destructibles */
	private final List<AiBlock> externalSoftBlockList = Collections.unmodifiableList(softBlockList);
	
	@Override
	public List<AiBlock> getBlocks()
	{	return externalBlockList;	
	}
	
	@Override
	public List<AiBlock> getDestructibleBlocks()
	{	return externalSoftBlockList;
	}
	
	/**
	 * Met à jour la liste des blocs.
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
	 * Renvoie la représentation du bloc passé en paramètre.
	 * 
	 * @param block
	 * 		Le bloc dont on veut la représentation.
	 * @return	
	 * 		Le {@link AiDataBlock} correspondant.
	 */
	protected AiDataBlock getBlock(Block block)
	{	return blockMap.get(block);
	}
	
	/**
	 * Ajoute un bloc dans la map de blocs de cette zone
	 * (méthode appelée depuis une {@link AiTile}).
	 * 
	 * @param block
	 * 		Le bloc à rajouter à la map.
	 */
	protected void addBlock(AiDataBlock block)
	{	blockMap.put(block.getSprite(),block);	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map des bombes contenues dans cette zone */
	private final transient Map<Bomb,AiDataBomb> bombMap = new HashMap<Bomb,AiDataBomb>();
	/** Liste des bombes contenues dans cette zone */
	private final List<AiBomb> bombList = new ArrayList<AiBomb>();
	/** Version immuable de la liste des bombes */
	private final List<AiBomb> externalBombList = Collections.unmodifiableList(bombList);
	
	@Override
	public List<AiBomb> getBombs()
	{	return externalBombList;	
	}
	
	/**
	 * Met à jour la liste des bombes.
	 */
	private void updateBombList()
	{	bombList.clear();
		for(Entry<Bomb,AiDataBomb> entry: bombMap.entrySet())
		{	AiDataBomb bomb = entry.getValue();
			bombList.add(bomb);
		}
		
		threatenedBombs.clear();
		delaysByBombs.clear();
		bombsByDelays.clear();
		bombmapsUpdated = false;
	}
	
	/**
	 * Renvoie la représentation de la bombe passée en paramètre.
	 * 
	 * @param bomb
	 * 		La bombe dont on veut la représentation.
	 * @return	
	 * 		Le AiBomb correspondant.
	 */
	protected AiDataBomb getBomb(Bomb bomb)
	{	return bombMap.get(bomb);
	}
	
	/**
	 * Ajoute une bombe dans la map de bombes de cette zone
	 * (méthode appelée depuis une {@link AiTile}).
	 * 
	 * @param bomb
	 * 		La bombe à rajouter à la map.
	 */
	protected void addBomb(AiDataBomb bomb)
	{	bombMap.put(bomb.getSprite(),bomb);	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map des feux contenus dans cette zone */
	private final transient Map<Fire,AiDataFire> fireMap = new HashMap<Fire,AiDataFire>();
	/** Liste des feux contenus dans cette zone */
	private final List<AiFire> fireList = new ArrayList<AiFire>();
	/** Version immuable de la liste des feux */
	private final List<AiFire> externalFireList = Collections.unmodifiableList(fireList);
	
	@Override
	public List<AiFire> getFires()
	{	return externalFireList;	
	}
	
	/**
	 * Met à jour la liste des feux.
	 */
	private void updateFireList()
	{	fireList.clear();
		for(Entry<Fire,AiDataFire> entry: fireMap.entrySet())
		{	AiDataFire fire = entry.getValue();
			fireList.add(fire);
		}
	}
	
	/**
	 * Renvoie la représentation du feu passé en paramètre.
	 * 
	 * @param fire
	 * 		Le feu dont on veut la représentation.
	 * @return	
	 * 		Le {@link AiFire} correspondant.
	 */
	protected AiDataFire getFire(Fire fire)
	{	return fireMap.get(fire);
	}
	
	/**
	 * Ajoute un feu dans la map de feux de cette zone
	 * (méthode appelée depuis une {@link AiTile}).
	 * 
	 * @param fire
	 * 		Le feu à rajouter à la map.
	 */
	protected void addFire(AiDataFire fire)
	{	fireMap.put(fire.getSprite(),fire);	
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map des sols contenus dans cette zone */
	private final transient Map<Floor,AiDataFloor> floorMap = new HashMap<Floor,AiDataFloor>();
	/** Liste des sols contenus dans cette zone */
	private final List<AiFloor> floorList = new ArrayList<AiFloor>();
	/** Version immuable de la liste des sols */
	private final List<AiFloor> externalFloorList = Collections.unmodifiableList(floorList);

	@Override
	public List<AiFloor> getFloors()
	{	return externalFloorList;	
	}
	
	/**
	 * Met à jour la liste des sols.
	 */
	private void updateFloorList()
	{	floorList.clear();
		for(Entry<Floor,AiDataFloor> entry: floorMap.entrySet())
		{	AiDataFloor floor = entry.getValue();
			floorList.add(floor);
		}
	}
	
	/**
	 * Renvoie la représentation du sol passé en paramètre.
	 * 
	 * @param floor
	 * 		Le sol dont on veut la représentation.
	 * @return	
	 * 		Le {@link AiFloor} correspondant.
	 */
	protected AiDataFloor getFloor(Floor floor)
	{	return floorMap.get(floor);
	}
	
	/**
	 * Ajoute un sol dans la map de sols de cette zone
	 * (méthode appelée depuis une {@link AiTile}).
	 * 
	 * @param floor
	 * 		Le sol à rajouter à la map.
	 */
	protected void addFloor(AiDataFloor floor)
	{	floorMap.put(floor.getSprite(),floor);	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map des personnages contenus dans cette zone (y compris ceux éliminés) */
	private final transient Map<Hero,AiDataHero> heroMap = new HashMap<Hero,AiDataHero>();
	/** Liste de tous les personnages contenus dans cette zone */
	private final List<AiHero> heroList = new ArrayList<AiHero>();
	/** Version immuable de la liste de tous les personnages */
	private final List<AiHero> externalHeroList = Collections.unmodifiableList(heroList);
	/** Liste des personnages restant encore dans cette zone */
	private final List<AiHero> remainingHeroList = new ArrayList<AiHero>();
	/** Version immuable de la liste des personnages restant */
	private final List<AiHero> externalRemainingHeroList = Collections.unmodifiableList(remainingHeroList);
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalHeroList;	
	}
	
	@Override
	public List<AiHero> getRemainingHeroes()
	{	return externalRemainingHeroList;	
	}
	
	@Override
	public List<AiHero> getRemainingOpponents()
	{	List<AiHero> result = new ArrayList<AiHero>(remainingHeroList);
		result.remove(ownHero);
		return result;	
	}
	
	/**
	 * Met à jour les listes de personnages.
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
	 * Renvoie la représentation du personnage passé en paramètre.
	 * 
	 * @param hero
	 * 		Le personnage dont on veut la représentation.
	 * @return	
	 * 		Le {@code AiHero} correspondant.
	 */
	protected AiDataHero getHero(Hero hero)
	{	return heroMap.get(hero);
	}
	
	/**
	 * Ajoute un personnage dans la map de personnages de cette zone
	 * (méthode appelée depuis une AiTile)
	 * 
	 * @param hero
	 * 		Le personnage à rajouter à la map.
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
	/** Map des items contenus dans cette zone */
	private final transient Map<Item,AiDataItem> itemMap = new HashMap<Item,AiDataItem>();
	/** Liste des items contenus dans cette zone */
	private final List<AiItem> itemList = new ArrayList<AiItem>();
	/** Version immuable de la liste des items */
	private final List<AiItem> externalItemList = Collections.unmodifiableList(itemList);
	
	@Override
	public List<AiItem> getItems()
	{	return externalItemList;	
	}
	
	/**
	 * Met à jour la liste des items.
	 */
	private void updateItemList()
	{	itemList.clear();
		for(Entry<Item,AiDataItem> entry: itemMap.entrySet())
		{	AiDataItem item = entry.getValue();
			if(item.getTile()!=null)
				itemList.add(item);
		}
	}
	
	/**
	 * Renvoie la représentation de l'item passé en paramètre,
	 * ou {@code null} si cet item est inconnu.
	 * 
	 * @param item
	 * 		L'item dont on veut la représentation.
	 * @return	
	 * 		Le {@code AiItem} correspondant, ou {@code null} s'il est inconnu.
	 */
	protected AiDataItem getItem(Item item)
	{	return itemMap.get(item);
	}
	
	/**
	 * Ajoute un item dans la map d'items de cette zone
	 * (méthode appelée depuis une {@code AiTile})
	 * 
	 * @param item
	 * 		L'item à rajouter à la map.
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
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Démarque toutes les représentations de sprites d'une liste determinée en fonction du type
	 * T paramétrant cette méthode. Méthode appelée au début de la mise à jour :
	 * les représentations de sprites qui n'ont pas été marquées à la fin de la mise à jour
	 * correspondent à des sprites qui ne font plus partie du jeu, et doivent être
	 * supprimées de cette représentation.
	 * 
	 * @param <U>	
	 * 		Type de sprite à traiter.
	 * @param <T>	
	 * 		Type de la liste à traiter.
	 * @param list
	 * 		Liste à traiter.
	 */
	private <U extends Sprite, T extends AiDataSprite<?>> void uncheckAll(Map<U,T> list)
	{	Iterator<Entry<U,T>> it = list.entrySet().iterator();
		while(it.hasNext())
		{	T temp = it.next().getValue();
			temp.uncheck();
		}
	}
	/**
	 * Méthode complémentaire de uncheckAll, et chargée de supprimer
	 * les représentations de sprites non-marquées à la fin de la mise à jour.
	 * 
	 * @param <U>
	 * 		Type de sprite à traiter.
	 * @param <T>
	 * 		Type de la liste à traiter.
	 * @param list
	 * 		Liste à traiter.
	 */
	private <U extends Sprite, T extends AiDataSprite<?>> void removeUnchecked(Map<U,T> list)
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
	 * Met à jour toutes les listes externes de sprites.
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
	/** Le personnage contrôlé par l'agent */
	private AiDataHero ownHero;

	@Override
	public AiDataHero getOwnHero()
	{	return ownHero;	
	}
	
	/**
	 * Initialise le personnage qui est contrôlé par l'agent.
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
	// SUDDEN DEATH				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initialise la map contenant les évènements décrivant
	 * la mort subite. Cette map peut être vide s'il n'y a pas de mort
	 * subite.
	 */
	private void initSuddenDeath()
	{	// init
		VisibleLoop loop = level.getLoop();
		Round round = loop.getRound();
		HollowLevel hollowLevel = round.getHollowLevel();
		suddenDeathEvents.clear();
		
		// insert events
		List<SuddenDeathEvent> sdeList = hollowLevel.getSuddenDeathEvents();
		for(SuddenDeathEvent sde: sdeList)
		{	long time = sde.getTime();
			Map<Tile,List<Sprite>> sprites = sde.getSprites();
			AiDataSuddenDeathEvent event = new AiDataSuddenDeathEvent(this,time,sprites);
			suddenDeathEvents.add(event);
		}
		Collections.sort(suddenDeathEvents);
	}
	
	/**
	 * Met à jour la liste contenant les évènements 
	 * décrivant la mort subite.
	 */
	private void updateSuddenDeath()
	{	if(!suddenDeathEvents.isEmpty())
		{	// init
			VisibleLoop loop = level.getLoop();
			Round round = loop.getRound();
			HollowLevel hollowLevel = round.getHollowLevel();
			List<SuddenDeathEvent> events = hollowLevel.getSuddenDeathEvents();
			
			// update
			if(events.isEmpty())
				suddenDeathEvents.clear();
			else 
			{	long time0 = suddenDeathEvents.get(0).getTime();
				long time = events.get(0).getTime();
				while(time0!=time && !suddenDeathEvents.isEmpty())
				{	suddenDeathEvents.remove(0);
					time0 = suddenDeathEvents.get(0).getTime();
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
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
	// SERIALIZABLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Override of the {@code defaultWriteObject} method, allows writing the
	 * static {@code AiTile.size} field.
	 * 
	 * @param oos
	 * 		Previously opened object stream.
	 * @throws IOException
	 * 		Problem while writing in the stream.
	 */
	private void writeObject(ObjectOutputStream oos) throws IOException
	{	// default serialization 
		oos.defaultWriteObject();
		
		// write the tile size
		double tileSize = AiTile.getSize();
		oos.writeObject(tileSize);
	}

	/**
	 * Override of the {@code defaultReadObject} method, allows reading the
	 * static {@code AiTile.size} field.
	 * 
	 * @param ois
	 * 		Previously opened object stream.
	 * @throws ClassNotFoundException 
	 * 		Problem while reading from the stream.
	 * @throws IOException
	 * 		Problem while reading from the stream.
	 */
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
	{	// default deserialization
		ois.defaultReadObject();

		// read the tile size
		double tileSize = (Double)ois.readObject();
//		double tileSize = 53;
		initTileSize(tileSize);
		RoundVariables.scaledTileDimension = tileSize;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement cette représentation 
	 * (une fois que l'agent n'en a plus besoin).
	 */
	public void finish()
	{	// matrix
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
				matrix[row][col].finish();
		}
		
		// sprites
		blockMap.clear();
		bombMap.clear();
		fireMap.clear();
		floorMap.clear();
		heroMap.clear();
		itemMap.clear();
		ownHero = null;
		
		// misc
		suddenDeathEvents.clear();
		level = null;
		player = null;
	}
}
