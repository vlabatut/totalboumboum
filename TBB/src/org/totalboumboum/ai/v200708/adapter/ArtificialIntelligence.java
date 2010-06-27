package org.totalboumboum.ai.v200708.adapter;

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

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ArtificialIntelligence implements Callable<Integer>
{	// constantes de sens de déplacement
	/** constante représentant la direction : sur-place (pas de déplacement)*/ 
	public final static int AI_DIR_NONE = 0;
	/** constante représentant la direction : vers le haut*/ 
	public final static int AI_DIR_UP = 1;
	/** constante représentant la direction : vers la bas*/ 
	public final static int AI_DIR_DOWN = 2;
	/** constante représentant la direction : vers la droite*/ 
	public final static int AI_DIR_RIGHT = 3;
	/** constante représentant la direction : vers la gauche*/ 
	public final static int AI_DIR_LEFT = 4;

	// constantes de codes de blocs
	/** bloc inconnu (débug)*/ 
	public final static int AI_BLOCK_UNKNOWN = -1;
	/** bloc vide*/ 
	public final static int AI_BLOCK_EMPTY = 0;
	/** bloc occupé par un mur destructible*/ 
	public final static int AI_BLOCK_WALL_SOFT = 1;
	/** bloc occupé par un mur indestructible*/ 
	public final static int AI_BLOCK_WALL_HARD = 2;
	/** bloc occupé par une flamme*/ 
	public final static int AI_BLOCK_FIRE = 3;
	/** bloc occupé par une bombe (n'ayant pas encore explosé)*/ 
	public final static int AI_BLOCK_BOMB = 4;
	/** bloc occupé par un bonus augmentant le nombre de bombes*/ 
	public final static int AI_BLOCK_ITEM_BOMB = 5;
	/** bloc occupé par un bonus augmentant la portée des bombes*/ 
	public final static int AI_BLOCK_ITEM_FIRE = 6;
	
	// constantes représentant des actions
	/** ne rien faire*/ 
	public final static int AI_ACTION_DO_NOTHING = 0;
	/** aller vers le haut*/ 
	public final static int AI_ACTION_GO_UP = 1;
	/** aller vers le bas*/ 
	public final static int AI_ACTION_GO_DOWN = 2;
	/** aller vers la gauche*/ 
	public final static int AI_ACTION_GO_LEFT = 3;
	/** aller vers la droite*/ 
	public final static int AI_ACTION_GO_RIGHT = 4;
	/** poser une bombe*/ 
	public final static int AI_ACTION_PUT_BOMB = 5;
	
	
	// champs généraux
	private String name;
	
	// champs représentant le percept
	/** matrice correspondant au percept de l'agent */
	private int zoneMatrix[][];
	/** liste des bombes avec leur position et leur puissance */
	private Vector<int[]> bombs;
	/** liste des joueurs avec leur position et leur sens de déplacement */
	private Vector<int[]> players;
	/** liste des joueurs avec leur état : vrai pour vivant */
	private Vector<Boolean> playersStates;
	/** position du personnage de l'IA */
	private int[] ownPosition;
	/** temps restant avant le shrink (-1 si le shrink a déjà commencé) */
	private long timeBeforeShrink;
	/** position du prochain shrink */
	private int nextShrinkPosition[];
	/** position relative de la bombe  */
	private int bombPosition;
	/** portée des bombes du personnage contrôlé par l'IA */
	private int ownFirePower;
	/** nombre de bombes que le personnage contrôlé par l'IA peut encore poser */
	private int ownBombCount;
	/** portée des bombes des personnages autres que celui contrôlé par l'IA */
	private Vector<Integer> firePowers;
	/** nombre de bombes que les personnages autres que celui contrôlé par l'IA peuvent encore poser */
	private Vector<Integer> bombCounts;
	
	/**
	 * Constructeur. 
	 * @param name	nom de la classe
	 */
	public ArtificialIntelligence(String name)
	{	if(name.length()>10)
			this.name = name.substring(0, 10);
		else
			this.name = name;
	}
	
	/**
	 * Renvoie le nom attributé à cette IA
	 * (en général : le nom de la classe elle-même)
	 * @return	le nom de l'IA
	 */
	public String getName()
	{	return name;
	}
	
	/**
	 * Renvoie le percept du personnage sous la forme d'une matrice d'entiers.
	 * Chaque entier est un code représentant la case correspondante en utilisant
	 * les constantes AI_BLOCK_XXXXX.
	 * @return	la matrice représentant la zone de jeu
	 */
	protected int[][] getZoneMatrix()
	{	return zoneMatrix;
	}

	/**
	 * Renvoie la largeur de la zone de jeu 
	 * @return	la largeur de la matrice
	 */
	protected int getZoneMatrixDimX()
	{	return zoneMatrix.length;
	}

	/**
	 * Renvoie la hauteur de la zone de jeu 
	 * @return	la hauteur de la matrice
	 */
	protected int getZoneMatrixDimY()
	{	return zoneMatrix[0].length;
	}

	/**
	 * Renvoie la portée de la bombe située à la position passée en paramètres.
	 * S'il n'y a pas de bombe à cette position, la valeur -1 est renvoyée.
	 * @param	x	position de la bombe
	 * @param	y	position de la bombe
	 * @return	la portée de la bombe
	 */
	protected int getBombPowerAt(int x, int y)
	{	int result = -1;
		Iterator<int[]> i = bombs.iterator();
		while(i.hasNext() && result==-1)
		{	int[] temp = i.next();
			if(temp[0]==x && temp[1]==y)
				result = temp[2];
		}
		return result;
	}
	
	/**
	 * Renvoie le nombre de joueurs participant à la partie, en plus
	 * de celui dirigé par cette IA. 
	 * @return	le nombre de joueurs.
	 */
	protected int getPlayerCount()
	{	return players.size();
	}
	
	/**
	 * Renvoie la position du personnage dont l'index est passé
	 * en paramètre. S'il n'y a pas de personnage ayant cet index, la valeur 
	 * {-1,-1} est renvoyée. Attention, le personnage dirigé par cette IA n'est 
	 * jamais considéré. 
	 * @param index	numéro du personnage
	 * @return	position du personnage 
	 */
	protected int[] getPlayerPosition(int index)
	{	int result[] = new int[2];
		if(index<players.size())
		{	result[0] = players.get(index)[0];
			result[1] = players.get(index)[1];
		}
		else
		{	result[0] = -1;
			result[1] = -1;
		}
		return result;
	}
	
	/**
	 * Renvoie le sens de déplacement du personnage dont l'index est passé
	 * en paramètre. S'il n'y a pas de personnage ayant cet index, la valeur 
	 * -1 est renvoyée. Sinon, il s'agit d'un entier AI_DIR_NONE, AI_DIR_UP, AI_DIR_DOWN, 
	 * AI_DIR_RIGHT ou AI_DIR_LEFT. Attention, le personnage dirigé par cette IA n'est 
	 * jamais considéré.
	 * @param index	numéro du personnage
	 * @return	le sens de déplacement du personnage 
	 */
	protected int getPlayerDirection(int index)
	{	if(index<players.size())
			return players.get(index)[2];
		else
			return -1;
	}

	/**
	 * Renvoie vrai si la personnage dont l'index est passé
	 * en paramètre est vivant. Si le personnage est mort ou en train de mourir,
	 * ou bien s'il n'y a pas de personnage ayant cet index, la valeur 
	 * faux est renvoyée. Attention, le personnage dirigé par cette IA n'est 
	 * jamais considéré.
	 * @param index	numéro du personnage
	 * @return	un booléan représentant l'état du personnage 
	 */
	protected boolean isPlayerAlive(int index)
	{	if(index<playersStates.size())
			return playersStates.get(index);
		else
			return false;
	}
	
	/**
	 * Renvoie le temps restant avant le début du shrink (la valeur
	 * est négative si le shrink a déjà commencé).
	 * @return	temps avant le shrink en millisecondes
	 */
	protected long getTimeBeforeShrink()
	{	return timeBeforeShrink;	
	}
	
	/**
	 * Renvoie la position du bloc qui sera le prochain à se faire écraser
	 * par un bloc lors du shrink.
	 * @return	un tableau de deux entiers représentant la position du prochain bloc du shrink
	 */
	protected int[] getNextShrinkPosition()
	{	return nextShrinkPosition;
	}
	
	/**
	 * Renvoie la position du personnage de l'IA.
	 * @return	les coordonnées du personnage de l'IA
	 */
	protected int[] getOwnPosition()
	{	return ownPosition;
	}

	/**
	 * Renvoie la position de la bombe relativement au personnage de l'IA,
	 * dans le cas où une bombe occupe la même case. Une constante de 
	 * la forme AI_DIR_XXXX est renvoyée. La constante AI_DIR_NONE est renvoyée
	 * s'il n'y a pas de bombe dans la case, ou bien si la bombe et le joueur sont placés
	 * au même endroit.
	 * @return	la position relative de la bombe
	 */
	protected int getBombPosition()
	{	return bombPosition;
	}
	
	/**
	 * Renvoie la portée des bombes du joueur contrôlé par l'IA.
	 * @return	puissance des bombes (longueur de la flamme exprimée en nombre de cases)
	 */
	protected int getOwnFirePower()
	{	return ownFirePower;
	}
	
	/**
	 * Renvoie une valeur correspondant au nombre de bombes que le joueur contrôlé par l'IA
	 * peut encore poser. Ce nombre correspond donc au nombre de bombes posables total moins 
	 * le nombre de bombes déjà posées.
	 * @return	nombre de bombes restant à poser
	 */
	protected int getOwnBombCount()
	{	return ownBombCount;
	}

	/**
	 * Renvoie la portée des bombes des joueurs autre que celui contrôlé par l'IA.
	 * @param	le numéro du joueur considéré
	 * @return	puissance des bombes (longueur de la flamme exprimée en nombre de cases)
	 */
	protected int getPlayerFirePower(int index)
	{	return firePowers.get(index);
	}

	/**
	 * Renvoie une valeur correspondant aux nombres de bombes que les joueurs autres
	 * que celui contrôlé par l'IA peuvent encore poser. 
	 * Ce nombre correspond donc au nombre de bombes posables total moins 
	 * le nombre de bombes déjà posées.
	 * @param	le numéro du joueur considéré
	 * @return	nombre de bombes restant à poser
	 */
	protected int getPlayerBombCount(int index)
	{	return bombCounts.get(index);
	}
	
	/**
	 * Permet d'initialiser les percepts avant que l'IA ne calcule l'action à effectuer
	 * via la méthode call. 
	 * @param zoneMatrix	matrice représentant la zone de jeu
	 * @param bombs	liste des bombes
	 * @param players	liste des joueurs
	 * @param playersStates	liste des états (mort ou vif) des joueurs
	 * @param ownPosition	position du personnage de l'IA
	 * @param timeBeforeShrink	temps avant le début du shrink
	 * @param nextShrinkPosition	prochain bloc qui va être shrinké
	 * @param bombPosition	position relative de la bombe
	 */
	public void setPercepts(int zoneMatrix[][],Vector<int[]> bombs,Vector<int[]> players,
			Vector<Boolean> playersStates, int[] ownPosition, long timeBeforeShrink,
			int nextShrinkPosition[],int bombPosition, int ownFirePower, int ownBombCount,
			Vector<Integer> firePowers, Vector<Integer> bombCounts)
	{	this.zoneMatrix = zoneMatrix;
		this.bombs = bombs;
		this.players = players;
		this.playersStates = playersStates;
		this.ownPosition = ownPosition;
		this.timeBeforeShrink = timeBeforeShrink;
		this.nextShrinkPosition = nextShrinkPosition;
		this.bombPosition = bombPosition;
		this.ownFirePower = ownFirePower;
		this.ownBombCount = ownBombCount;
		this.firePowers = firePowers;
		this.bombCounts = bombCounts;		
	}
	
	/**
	 * Affiche la matrice représentant la zone de jeu passé à l'IA
	 * (pour le déboggage). 
	 */
	public void printZoneMatrix()
	{	System.out.println();
		for(int i1=0;i1<zoneMatrix[0].length;i1++)
		{	for(int i2=0;i2<zoneMatrix.length;i2++)
				System.out.print(zoneMatrix[i2][i1]+" ");
			System.out.println();
		}
	}
	
	/**
	 * libère les ressources occupées par l'IA
	 */
	public void finish()
	{	bombCounts = null;
		bombs = null;
		firePowers = null;
		name = null;
		nextShrinkPosition = null;
		ownPosition = null;
		players = null;
		playersStates = null;
		zoneMatrix = null;
	}
}
