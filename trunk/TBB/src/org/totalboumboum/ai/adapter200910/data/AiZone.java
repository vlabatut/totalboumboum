package org.totalboumboum.ai.adapter200910.data;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.configuration.profile.PredefinedColor;
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
import org.totalboumboum.engine.loop.LocalLoop;
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.tools.calculus.CalculusTools;


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
 * @author Vincent
 *
 */

public class AiZone
{	
	/**
	 * construit une repr�sentation du niveau pass� en param�tre,
	 * du point de vue du joueur pass� en param�tre.
	 * 
	 * @param level	niveau � repr�senter
	 * @param player	joueur dont le point de vue est � adopter
	 */
	public AiZone(Level level, Player player)
	{	this.level = level;
		this.player = player;
		initMatrix();
//		updateMatrix();
		initOwnHero();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met � jour cette repr�sentation ainsi que tous ses constituants.
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
	/** temps �coul� depuis le d�but du jeu */
	private long totalTime = 0;
	/** temps �coul� depuis la mise � jour pr�c�dente de l'IA consid�r�e */
	private long elapsedTime = 0;
	/** dur�e maximale de la partie */
	private long limitTime = 0;
	
	/**
	 * renvoie le temps total �coul� depuis le d�but du jeu
	 * 
	 * @return	le temps total �coul� exprim� en millisecondes
	 */
	public long getTotalTime()
	{	return totalTime;		
	}
	
	/**
	 * renvoie le temps �coul� depuis la mise � jour pr�c�dente
	 * de l'IA consid�r�e.
	 * 
	 * @return	le temps �coul� exprim� en millisecondes
	 */
	public long getElapsedTime()
	{	return elapsedTime;		
	}
	
	/**
	 * renvoie la dur�e maximale de la partie
	 * (elle peut �ventuellement durer moins longtemps)
	 * 
	 * @return	la dur�e maximale de la partie
	 */
	public long getLimitTime()
	{	return limitTime;		
	}
	
	/**
	 * met � jour les donn�es temporelles
	 * 
	 * @param elapsedTime
	 */
	private void updateTimes(long elapsedTime)
	{	LocalLoop loop = level.getLoop();
		this.totalTime = loop.getTotalGameTime();
		this.elapsedTime = elapsedTime;
		this.limitTime = loop.getRound().getLimits().getTimeLimit().getThreshold();
	}

	/////////////////////////////////////////////////////////////////
	// META DATA		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** rangs des joueurs pour la manche en cours (ces rangs peuvent �voluer) */
	private final HashMap<AiHero,Integer> roundRanks = new HashMap<AiHero, Integer>();
	/** rangs des joueurs pour la rencontre en cours (ces rangs n'�voluent pas pendant la manche) */
	private final HashMap<AiHero,Integer> matchRanks = new HashMap<AiHero, Integer>();
	/** rangs des joueurs au classement global du jeu (ces rangs n'�voluent pas pendant la manche) */
	private final HashMap<AiHero,Integer> statsRanks = new HashMap<AiHero, Integer>();

	/** 
	 * met � jour des donn�es qui ne sont pas directement reli�es
	 * � l'action en cours, telles que l'�volution du classement des joueurs
	 */
	private void updateMeta()
	{	List<Player> players = level.getLoop().getPlayers();
		// stats
		statsRanks.clear();
		RankingService rankingService = GameStatistics.getRankingService();
		for(int i=0;i<players.size();i++)
		{	Player player = players.get(i);
			Hero hero = (Hero)player.getSprite();
			AiHero aiHero = heroMap.get(hero);
			int playerId = player.getId();
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
			AiHero aiHero = heroMap.get(hero);
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
			AiHero aiHero = heroMap.get(hero);
			int rank = ranks[i];
			matchRanks.put(aiHero,rank);
		}
	}
	
	/**
	 * Renvoie le classement du personnage pass� en param�tre, pour la manche en cours.
	 * Ce classement est susceptible d'�voluer d'ici la fin de la manche actuellement jou�e, 
	 * par exemple si ce joueur est �limin�.
	 * 
	 * @param hero	le personnage consid�r�
	 * @return	son classement dans la manche en cours
	 */
	int getRoundRank(AiHero hero)
	{	return roundRanks.get(hero);
	}
	
	/**
	 * Renvoie le classement du personnage pass� en param�tre, pour la rencontre en cours.
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @param hero	le personnage consid�r�
	 * @return	son classement dans la rencontre en cours
	 */
	int getMatchRank(AiHero hero)
	{	return matchRanks.get(hero);
	}
	
	/**
	 * Renvoie le classement du personnage pass� en param�tre, dans le classement g�n�ral du jeu (Glicko-2)
	 * Ce classement n'�volue pas pendant la manche actuellement jou�e.
	 * 
	 * @param hero	le personnage consid�r�
	 * @return	son classement g�n�ral (Glicko-2)
	 */
	int getStatsRank(AiHero hero)
	{	return statsRanks.get(hero);
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** niveau repr�sent� par cette classe */
	private Level level;
	
	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** joueur contr�l� par l'IA */
	private Player player;
	
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
	
	/** 
	 * renvoie la hauteur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 * de la zone de jeu exprim�e en cases (ie: nombre de lignes)
	 *  
	 *  @return	hauteur de la zone
	 */
	public int getHeigh()
	{	return height;	
	}
	
	/** 
	 * renvoie la largeur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 * de la zone de jeu exprim�e en cases (ie: nombre de colonnes)
	 *  
	 *  @return	largeur de la zone
	 */
	public int getWidth()
	{	return width;	
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
	public AiTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	/**
	 * renvoie la case qui contient le pixel pass� en param�tre
	 *   
	 *  @param	x	abscisse du pixel concern�
	 *  @param	y	ordonn�e du pixel concern�
	 *  @return	case contenant le pixel situ� aux coordonn�es sp�cifi�es en param�tres
	 */
	public AiTile getTile(double x, double y)
	{	Tile tile = level.getTile(x, y);
		int line = tile.getLine();
		int col = tile.getCol();
		AiTile result = matrix[line][col];
		return result;
	}
		
	/**
	 * renvoie la direction de la case target relativement � la case source.
	 * Par exemple, la case target de coordonn�es (5,5) est � droite de
	 * la case source de coordonn�es (5,6).</br>
	 * 
	 * Cette fonction peut �tre utile quand on veut savoir dans quelle direction
	 * il faut se d�placer pour aller de la case source � la case target.</br>
	 * 
	 * ATTENTION 1 : si les deux cases ne sont pas des voisines directes (ie. ayant un cot� commun),
	 * il est possible que cette m�thode renvoie une direction composite,
	 * c'est � dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. R�f�rez-vous � 
	 * la classe Direction pour plus d'informations sur ces valeurs.</br>
	 *  
	 * ATTENTION 2 : comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette m�thode renvoie la direction du plus court chemin (sans consid�rer les �ventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le r�sultat sera
	 * RIGHT, car LEFT permet �galement d'atteindre la case, mais en parcourant un chemin plus long.
	 * <br><t> S>>>>>>>>>>T  distance=11
	 * <br><t>>S..........T> distance=1
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
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des blocks contenus dans cette zone */
	private final HashMap<Block,AiBlock> blockMap = new HashMap<Block,AiBlock>();
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiBlock> blockList = new ArrayList<AiBlock>();
	
	/** 
	 * renvoie la liste des blocks contenues dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	public List<AiBlock> getBlocks()
	{	return blockList;	
	}
	
	/**
	 * met � jour la liste externe des blocs
	 */
	private void updateBlockList()
	{	blockList.clear();
		for(Entry<Block,AiBlock> entry: blockMap.entrySet())
		{	AiBlock block = entry.getValue();
			blockList.add(block);
		}
	}
	
	/**
	 * renvoie la repr�sentation du bloc pass� en param�tre.
	 * 
	 * @param block	le bloc dont on veut la repr�sentation
	 * @return	le AiBlock correspondant
	 */
	AiBlock getBlock(Block block)
	{	return blockMap.get(block);
	}
	
	/**
	 * ajoute un bloc dans la liste de blocs de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param block	le bloc � rajouter � la liste
	 */
	void addBlock(AiBlock block)
	{	blockMap.put(block.getSprite(),block);	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des bombes contenues dans cette zone */
	private final HashMap<Bomb,AiBomb> bombMap = new HashMap<Bomb,AiBomb>();
	/** liste externe des bombes contenues dans cette zone */
	private final List<AiBomb> bombList = new ArrayList<AiBomb>();
	
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	public List<AiBomb> getBombs()
	{	return bombList;	
	}
	
	/**
	 * met � jour la liste externe des bombes
	 */
	private void updateBombList()
	{	bombList.clear();
		for(Entry<Bomb,AiBomb> entry: bombMap.entrySet())
		{	AiBomb bomb = entry.getValue();
			bombList.add(bomb);
		}
	}
	
	/**
	 * renvoie la repr�sentation de la bombe pass�e en param�tre.
	 * 
	 * @param bomb	la bombz dont on veut la repr�sentation
	 * @return	le AiBomb correspondant
	 */
	AiBomb getBomb(Bomb bomb)
	{	return bombMap.get(bomb);
	}
	
	/**
	 * ajoute une bombe dans la liste de bombes de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param bomb	la bombe � rajouter � la liste
	 */
	void addBomb(AiBomb bomb)
	{	bombMap.put(bomb.getSprite(),bomb);	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des feux contenus dans cette zone */
	private final HashMap<Fire,AiFire> fireMap = new HashMap<Fire,AiFire>();
	/** liste externe des feux contenus dans cette zone */
	private final List<AiFire> fireList = new ArrayList<AiFire>();
	
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	public List<AiFire> getFires()
	{	return fireList;	
	}
	
	/**
	 * met � jour la liste externe des feux
	 */
	private void updateFireList()
	{	fireList.clear();
		for(Entry<Fire,AiFire> entry: fireMap.entrySet())
		{	AiFire fire = entry.getValue();
			fireList.add(fire);
		}
	}
	
	/**
	 * renvoie la repr�sentation du feu pass� en param�tre.
	 * 
	 * @param fire	le feu dont on veut la repr�sentation
	 * @return	le AiFire correspondant
	 */
	AiFire getFire(Fire fire)
	{	return fireMap.get(fire);
	}
	
	/**
	 * ajoute un feu dans la liste de feux de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param fire	le feu � rajouter � la liste
	 */
	void addFire(AiFire fire)
	{	fireMap.put(fire.getSprite(),fire);	
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des sols contenus dans cette zone */
	private final HashMap<Floor,AiFloor> floorMap = new HashMap<Floor,AiFloor>();
	/** liste externe des sols contenus dans cette zone */
	private final List<AiFloor> floorList = new ArrayList<AiFloor>();

	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	public List<AiFloor> getFloors()
	{	return floorList;	
	}
	
	/**
	 * met � jour la liste externe des sols
	 */
	private void updateFloorList()
	{	floorList.clear();
		for(Entry<Floor,AiFloor> entry: floorMap.entrySet())
		{	AiFloor floor = entry.getValue();
			floorList.add(floor);
		}
	}
	
	/**
	 * renvoie la repr�sentation du sol pass� en param�tre.
	 * 
	 * @param floor	le sol dont on veut la repr�sentation
	 * @return	le AiFloor correspondant
	 */
	AiFloor getFloor(Floor floor)
	{	return floorMap.get(floor);
	}
	
	/**
	 * ajoute un sol dans la liste de sols de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param floor	le sol � rajouter � la liste
	 */
	void addFloor(AiFloor floor)
	{	floorMap.put(floor.getSprite(),floor);	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des personnages contenus dans cette zone */
	private final HashMap<Hero,AiHero> heroMap = new HashMap<Hero,AiHero>();
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
	public List<AiHero> getHeroes()
	{	return heroList;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu.
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	public List<AiHero> getRemainingHeroes()
	{	return remainingHeroList;	
	}
	
	/**
	 * met � jour les listes externes des personnages
	 */
	private void updateHeroLists()
	{	heroList.clear();
		remainingHeroList.clear();
		for(Entry<Hero,AiHero> entry: heroMap.entrySet())
		{	AiHero hero = entry.getValue();
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
	AiHero getHero(Hero hero)
	{	return heroMap.get(hero);
	}
	
	/**
	 * ajoute un personnage dans la liste de personnages de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param hero	le personnage � rajouter � la liste
	 */
	void addHero(AiHero hero)
	{	heroMap.put(hero.getSprite(),hero);	
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des items contenus dans cette zone */
	private final HashMap<Item,AiItem> itemMap = new HashMap<Item,AiItem>();
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
	public List<AiItem> getItems()
	{	return itemList;	
	}
	
	/**
	 * met � jour la liste externe des items
	 */
	private void updateItemList()
	{	itemList.clear();
		for(Entry<Item,AiItem> entry: itemMap.entrySet())
		{	AiItem item = entry.getValue();
			itemList.add(item);
		}
	}
	
	/**
	 * renvoie la repr�sentation de l'item pass� en param�tre.
	 * 
	 * @param item	l'item dont on veut la repr�sentation
	 * @return	le AiItem correspondant
	 */
	AiItem getItem(Item item)
	{	return itemMap.get(item);
	}
	
	/**
	 * ajoute un item dans la liste d'items de cette zone
	 * (m�thode appel�e depuis une AiTile)
	 * 
	 * @param item	l'item � rajouter � la liste
	 */
	void addItem(AiItem item)
	{	itemMap.put(item.getSprite(),item);	
	}
	
	/**
	 * permet de modifier le nombre d'items encore cach�s dans ce niveau
	 * 
	 * @param hiddenItemsCount	le nouveau nombre d'items cach�s dans le niveau
	 */
	void setHiddenItemsCount(int hiddenItemsCount)
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
		{	Entry<U,T> entry = it.next();
			T temp = entry.getValue();
			//U sprite = entry.getKey();
			if(!temp.isChecked())
			{	temp.setEnded();
				//Sprite sprite = temp.getSprite();
				//if(sprite.isEnded())
				if(!(temp instanceof AiHero)) //we always keep the hero, cause they may come back...
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
		Iterator<Entry<Hero,AiHero>> i = heroMap.entrySet().iterator();
		boolean found = false;
		while(i.hasNext() && !found)
		{	AiHero temp = i.next().getValue();
			if(temp.getColor()==color)
			{	ownHero = temp;
				found = true;
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule la direction pour aller du sprite source au sprite target.
	 * Le niveau est consid�r� comme cyclique, i.e. le bord de droite est 
	 * reli� au bord de gauche, et le bord du haut est reli� au bord du bas.
	 * Cette m�thode consid�re la direction correspondant � la distance la plus
	 * courte (qui peut correspondre � un chemin passant par les bords du niveau)
	 * La direction peut �tre NONE si jamais les deux sprites sont au m�me endroit
	 * 
	 * @param source	sprite de d�part
	 * @param target	sprite de destination
	 * @return	la direction pour aller de source vers target
	 */
	public Direction getDirection(AiSprite<?> source, AiSprite<?> target)
	{	Direction result;
		if(source==null || target==null)
			result = Direction.NONE;
		else
		{	double x1 = source.getPosX();
			double y1 = source.getPosY();
			double x2 = target.getPosX();
			double y2 = target.getPosY();
			result = getDirection(x1,y1,x2,y2);
		}
		return result;		
	}
	
	/**
	 * Calcule la direction pour aller du sprite � la case pass�s en param�tres.
	 * Le niveau est consid�r� comme cyclique, i.e. le bord de droite est 
	 * reli� au bord de gauche, et le bord du haut est reli� au bord du bas.
	 * Cette m�thode consid�re la direction correspondant � la distance la plus
	 * courte (qui peut correspondre � un chemin passant par les bords du niveau)
	 * La direction peut �tre NONE si jamais les deux sprites sont au m�me endroit
	 * 
	 * @param sprite	sprite en d�placement
	 * @param tile	case de destination
	 * @return	la direction pour aller du sprite vers la case
	 */
	public Direction getDirection(AiSprite<?> sprite, AiTile tile)
	{	Direction result;
		if(sprite==null || tile==null)
			result = Direction.NONE;
		else
		{	double x1 = sprite.getPosX();
			double y1 = sprite.getPosY();
			double x2 = tile.getPosX();
			double y2 = tile.getPosY();
			result = getDirection(x1,y1,x2,y2);
		}
		return result;
	}
	
	/**
	 * Calcule la direction pour aller de la position (x1,y1) � la position (x2,y2)
	 * Le niveau est consid�r� comme cyclique, i.e. le bord de droite est 
	 * reli� au bord de gauche, et le bord du haut est reli� au bord du bas.
	 * Cette m�thode consid�re la direction correspondant � la distance la plus
	 * courte (qui peut correspondre � un chemin passant par les bords du niveau).
	 * La direction peut �tre NONE si jamais les deux positions sont �quivalentes.
	 * 
	 * @param x1	premi�re position horizontale en pixels
	 * @param y1	premi�re position verticale en pixels
	 * @param x2	seconde position horizontale en pixels
	 * @param y2	seconde position verticale en pixels
	 * @return	la direction correspondant au chemin le plus court
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	double dx = RoundVariables.level.getDeltaX(x1,x2);
		if(CalculusTools.isRelativelyEqualTo(dx,0))
			dx = 0;
		double dy = RoundVariables.level.getDeltaY(y1,y2);
		if(CalculusTools.isRelativelyEqualTo(dy,0))
			dy = 0;
		Direction result = Direction.getCompositeFromRelativeDouble(dx,dy);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les cases de coordonn�es
	 * (line1,col1) et (line2,col2), exprim�e en cases. Attention, le 
	 * niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param line1	ligne de la premi�re case
	 * @param col1	colonne de la premi�re case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2, Direction direction)
	{	int result = level.getTileDistance(line1,col1,line2,col2,direction);
		return result;
	}

	/**
	 * renvoie la distance de Manhattan entre les cases de coordonn�es
	 * (line1,col1) et (line2,col2), exprim�e en cases. 
	 * Attention, le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param line1	ligne de la premi�re case
	 * @param col1	colonne de la premi�re case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2)
	{	int result = level.getTileDistance(line1, col1, line2, col2, Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases pass�es en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premi�re case
	 * @param sprite2	seconde case
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2)
	{	int result = getTileDistance(tile1,tile2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases pass�es en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premi�re case
	 * @param sprite2	seconde case
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2, Direction direction)
	{	int line1 = tile1.getLine();
		int col1 = tile1.getCol();
		int line2 = tile2.getLine();
		int col2 = tile2.getCol();
		int result = level.getTileDistance(line1,col1,line2,col2);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public int getTileDistance(AiSprite<?> sprite1, AiSprite<?> sprite2)
	{	int result = getTileDistance(sprite1,sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(AiSprite<?> sprite1, AiSprite<?> sprite2, Direction direction)
	{	AiTile tile1 = sprite1.getTile();
		AiTile tile2 = sprite2.getTile();
		int result = getTileDistance(tile1,tile2);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les points de coordonn�es
	 * (x1,y1) et (x2,y2), exprim�e en pixels. Attention, le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonn�e du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonn�e du second point
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	double result = level.getPixelDistance(x1,y1,x2,y2);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les points de coordonn�es
	 * (x1,y1) et (x2,y2), exprim�e en pixels. Attention, le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonn�e du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonn�e du second point
	 * @param direction	direction � consid�rer
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	double result = level.getPixelDistance(x1,y1,x2,y2,direction);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en pixels. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public double getPixelDistance(AiSprite<?> sprite1, AiSprite<?> sprite2)
	{	double result = getPixelDistance(sprite1, sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres, exprim�e en pixels. 
	 * Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction indiqu�e par le 
	 * param�tre direction, qui peut correspondre � un chemin passant par 
	 * les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction � consid�rer
	 */
	public double getPixelDistance(AiSprite<?> sprite1, AiSprite<?> sprite2, Direction direction)
	{	double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		double result = level.getPixelDistance(x1,y1,x2,y2,direction);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE DELTAS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// PIXEL DELTAS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// COORDINATE NORMALIZING	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * prend n'importe quelles coordonn�es exprim�es en pixels et les normalise
	 * de mani�re � ce qu'elles appartiennent � la zone de jeu. Si les coordonn�es
	 * d�signent une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param x	abscisse
	 * @param y	ordonn�e
	 * @return	un tableau contenant les versions normalis�es de x et y
	 */
	public double[] normalizePosition(double x, double y)
	{	return level.normalizePosition(x, y);
	}

	/**
	 * prend n'importe quelle abscisse exprim�e en pixels et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� gauche et le
	 * c�t� droit sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param x	abscisse
	 * @return	la version normalis�e de x
	 */
	public double normalizePositionX(double x)
	{	return level.normalizePositionX(x);
	}
	
	/**
	 * prend n'importe quelle ordonn�e exprim�e en pixels et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� haut et le
	 * c�t� bas sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param y	ordonn�e
	 * @return	la version normalis�e de y
	 */
	public double normalizePositionY(double y)
	{	return level.normalizePositionY(y);
	}
	
	/**
	 * prend n'importe quelles coordonn�es exprim�es en cases et les normalise
	 * de mani�re � ce qu'elles appartiennent � la zone de jeu. Si les coordonn�es
	 * d�signent une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param line	ligne de la case
	 * @param col	colonne de la case
	 * @return	un tableau contenant les versions normalis�es de line et col
	 */
	public int[] normalizePosition(int line, int col)
	{	return level.normalizePosition(line, col);
	}

	/**
	 * prend n'importe quelle abscisse exprim�e en cases et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� gauche et le
	 * c�t� droit sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param col	colonne de la case
	 * @return	la version normalis�e de col
	 */
	public int normalizePositionCol(int col)
	{	return level.normalizePositionCol(col);
	}

	/**
	 * prend n'importe quelle ordonn�e exprim�e en cases et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� haut et le
	 * c�t� bas sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param line	ligne de la case
	 * @return	la version normalis�e de line
	 */
	public int normalizePositionLine(int line)
	{	return level.normalizePositionLine(line);
	}
	
	/////////////////////////////////////////////////////////////////
	// SAME PIXEL POSITION		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si les deux sprites pass�s en param�tres occupent la
	 * m�me position au pixel pr�s
	 * 
	 * @param sprite1	le premier sprite
	 * @param sprite2	le second sprite
	 * @return	vrai ssi les deux sprites sont au m�me endroit
	 */
	public boolean hasSamePixelPosition(AiSprite<?> sprite1, AiSprite<?> sprite2)
	{	boolean result;
		double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * teste si le sprite pass� en param�tre occupent le
	 * centre de la case pass�e en param�tre, au pixel pr�s
	 * 
	 * @param sprite	le sprite
	 * @param tile	la case
	 * @return	vrai ssi le sprite est au centre de la case
	 */
	public boolean hasSamePixelPosition(AiSprite<?> sprite, AiTile tile)
	{	boolean result;	
		double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}

	/**
	 * teste si les deux points pass�s en param�tres occupent la
	 * m�me position au pixel pr�s
	 * 
	 * @param x1	l'abscisse de la premi�re position
	 * @param y1	l'ordonn�e de la premi�re position
	 * @param x2	l'abscisse de la seconde position
	 * @param y21	l'ordonn�e de la seconde position
	 * @return	vrai ssi les deux positions sont �quivalentes au pixel pr�s
	 */
	public boolean hasSamePixelPosition(double x1, double y1, double x2, double y2)
	{	boolean result = true;	
		result = result && CalculusTools.isRelativelyEqualTo(x1,x2);
		result = result && CalculusTools.isRelativelyEqualTo(y1,y2);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	}
}
