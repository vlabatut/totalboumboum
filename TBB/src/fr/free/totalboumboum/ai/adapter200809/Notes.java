package fr.free.totalboumboum.ai.adapter200809;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;

public abstract class Notes
{	
	/** bloc occupé par un mur destructible*/ 
	public final static int AI_BLOCK_WALL_SOFT = 1;
	/** bloc occupé par un mur indestructible*/ 
	public final static int AI_BLOCK_WALL_HARD = 2;
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
	
	/** position du personnage de l'IA */
	private int[] ownPosition;
	
	/** temps restant avant le shrink (-1 si le shrink a déjà commencé) */
	private long timeBeforeShrink;
	/** position du prochain shrink */
	private int nextShrinkPosition[];
	
	/** portée des bombes du personnage contrôlé par l'IA */
	private int ownFirePower;
	/** nombre de bombes que le personnage contrôlé par l'IA peut encore poser */
	private int ownBombCount;
	/** nombre de bombes que les personnages autres que celui contrôlé par l'IA peuvent encore poser */
	private Vector<Integer> bombCounts;
	
	/**
	 * Renvoie la position du personnage de l'IA.
	 * @return	les coordonnées du personnage de l'IA
	 */
	protected int[] getOwnPosition()
	{	return ownPosition;
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
}
