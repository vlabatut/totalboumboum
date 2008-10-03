package fr.free.totalboumboum.ai.adapter200809;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;

public abstract class Notes
{	
	/** bloc occup� par un mur destructible*/ 
	public final static int AI_BLOCK_WALL_SOFT = 1;
	/** bloc occup� par un mur indestructible*/ 
	public final static int AI_BLOCK_WALL_HARD = 2;
	/** bloc occup� par un bonus augmentant le nombre de bombes*/ 
	public final static int AI_BLOCK_ITEM_BOMB = 5;
	/** bloc occup� par un bonus augmentant la port�e des bombes*/ 
	public final static int AI_BLOCK_ITEM_FIRE = 6;
	
	// constantes repr�sentant des actions
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
	
	/** position du personnage de l'IA */
	private int[] ownPosition;
	
	/** temps restant avant le shrink (-1 si le shrink a d�j� commenc�) */
	private long timeBeforeShrink;
	/** position du prochain shrink */
	private int nextShrinkPosition[];
	
	/** port�e des bombes du personnage contr�l� par l'IA */
	private int ownFirePower;
	/** nombre de bombes que le personnage contr�l� par l'IA peut encore poser */
	private int ownBombCount;
	/** nombre de bombes que les personnages autres que celui contr�l� par l'IA peuvent encore poser */
	private Vector<Integer> bombCounts;
	
	/**
	 * Renvoie la position du personnage de l'IA.
	 * @return	les coordonn�es du personnage de l'IA
	 */
	protected int[] getOwnPosition()
	{	return ownPosition;
	}

	/**
	 * Renvoie la port�e des bombes du joueur contr�l� par l'IA.
	 * @return	puissance des bombes (longueur de la flamme exprim�e en nombre de cases)
	 */
	protected int getOwnFirePower()
	{	return ownFirePower;
	}
	
	/**
	 * Renvoie une valeur correspondant au nombre de bombes que le joueur contr�l� par l'IA
	 * peut encore poser. Ce nombre correspond donc au nombre de bombes posables total moins 
	 * le nombre de bombes d�j� pos�es.
	 * @return	nombre de bombes restant � poser
	 */
	protected int getOwnBombCount()
	{	return ownBombCount;
	}

	/**
	 * Renvoie la port�e des bombes des joueurs autre que celui contr�l� par l'IA.
	 * @param	le num�ro du joueur consid�r�
	 * @return	puissance des bombes (longueur de la flamme exprim�e en nombre de cases)
	 */
	protected int getPlayerFirePower(int index)
	{	return firePowers.get(index);
	}

	/**
	 * Renvoie une valeur correspondant aux nombres de bombes que les joueurs autres
	 * que celui contr�l� par l'IA peuvent encore poser. 
	 * Ce nombre correspond donc au nombre de bombes posables total moins 
	 * le nombre de bombes d�j� pos�es.
	 * @param	le num�ro du joueur consid�r�
	 * @return	nombre de bombes restant � poser
	 */
	protected int getPlayerBombCount(int index)
	{	return bombCounts.get(index);
	}
	
	/**
	 * Permet d'initialiser les percepts avant que l'IA ne calcule l'action � effectuer
	 * via la m�thode call. 
	 * @param zoneMatrix	matrice repr�sentant la zone de jeu
	 * @param bombs	liste des bombes
	 * @param players	liste des joueurs
	 * @param playersStates	liste des �tats (mort ou vif) des joueurs
	 * @param ownPosition	position du personnage de l'IA
	 * @param timeBeforeShrink	temps avant le d�but du shrink
	 * @param nextShrinkPosition	prochain bloc qui va �tre shrink�
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
	 * Affiche la matrice repr�sentant la zone de jeu pass� � l'IA
	 * (pour le d�boggage). 
	 */
	public void printZoneMatrix()
	{	System.out.println();
		for(int i1=0;i1<zoneMatrix[0].length;i1++)
		{	for(int i2=0;i2<zoneMatrix.length;i2++)
				System.out.print(zoneMatrix[i2][i1]+" ");
			System.out.println();
		}
	}
}
