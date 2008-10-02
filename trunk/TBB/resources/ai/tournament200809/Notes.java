package tournament200809;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;

public abstract class Notes implements Callable<Integer>, Serializable
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
	
	
	// champs g�n�raux
	private String name;
	
	// champs repr�sentant le percept
	/** matrice correspondant au percept de l'agent */
	private int zoneMatrix[][];
	/** liste des bombes avec leur position et leur puissance */
	private Vector<int[]> bombs;
	/** liste des joueurs avec leur position et leur sens de d�placement */
	private Vector<int[]> players;
	/** liste des joueurs avec leur �tat : vrai pour vivant */
	private Vector<Boolean> playersStates;
	/** position du personnage de l'IA */
	private int[] ownPosition;
	/** temps restant avant le shrink (-1 si le shrink a d�j� commenc�) */
	private long timeBeforeShrink;
	/** position du prochain shrink */
	private int nextShrinkPosition[];
	/** position relative de la bombe  */
	private int bombPosition;
	/** port�e des bombes du personnage contr�l� par l'IA */
	private int ownFirePower;
	/** nombre de bombes que le personnage contr�l� par l'IA peut encore poser */
	private int ownBombCount;
	/** port�e des bombes des personnages autres que celui contr�l� par l'IA */
	private Vector<Integer> firePowers;
	/** nombre de bombes que les personnages autres que celui contr�l� par l'IA peuvent encore poser */
	private Vector<Integer> bombCounts;
	
	/**
	 * Constructeur. 
	 * @param name	nom de la classe
	 */
	public Notes(String name)
	{	if(name.length()>10)
			this.name = name.substring(0, 10);
		else
			this.name = name;
	}
	
	/**
	 * Renvoie le nom attribut� � cette IA
	 * (en g�n�ral : le nom de la classe elle-m�me)
	 * @return	le nom de l'IA
	 */
	public String getName()
	{	return name;
	}
	
	/**
	 * Renvoie le percept du personnage sous la forme d'une matrice d'entiers.
	 * Chaque entier est un code repr�sentant la case correspondante en utilisant
	 * les constantes AI_BLOCK_XXXXX.
	 * @return	la matrice repr�sentant la zone de jeu
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
	 * Renvoie la port�e de la bombe situ�e � la position pass�e en param�tres.
	 * S'il n'y a pas de bombe � cette position, la valeur -1 est renvoy�e.
	 * @param	x	position de la bombe
	 * @param	y	position de la bombe
	 * @return	la port�e de la bombe
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
	 * Renvoie le nombre de joueurs participant � la partie, en plus
	 * de celui dirig� par cette IA. 
	 * @return	le nombre de joueurs.
	 */
	protected int getPlayerCount()
	{	return players.size();
	}
	
	/**
	 * Renvoie la position du personnage dont l'index est pass�
	 * en param�tre. S'il n'y a pas de personnage ayant cet index, la valeur 
	 * {-1,-1} est renvoy�e. Attention, le personnage dirig� par cette IA n'est 
	 * jamais consid�r�. 
	 * @param index	num�ro du personnage
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
	 * Renvoie le sens de d�placement du personnage dont l'index est pass�
	 * en param�tre. S'il n'y a pas de personnage ayant cet index, la valeur 
	 * -1 est renvoy�e. Sinon, il s'agit d'un entier AI_DIR_NONE, AI_DIR_UP, AI_DIR_DOWN, 
	 * AI_DIR_RIGHT ou AI_DIR_LEFT. Attention, le personnage dirig� par cette IA n'est 
	 * jamais consid�r�.
	 * @param index	num�ro du personnage
	 * @return	le sens de d�placement du personnage 
	 */
	protected int getPlayerDirection(int index)
	{	if(index<players.size())
			return players.get(index)[2];
		else
			return -1;
	}

	/**
	 * Renvoie vrai si la personnage dont l'index est pass�
	 * en param�tre est vivant. Si le personnage est mort ou en train de mourir,
	 * ou bien s'il n'y a pas de personnage ayant cet index, la valeur 
	 * faux est renvoy�e. Attention, le personnage dirig� par cette IA n'est 
	 * jamais consid�r�.
	 * @param index	num�ro du personnage
	 * @return	un bool�an repr�sentant l'�tat du personnage 
	 */
	protected boolean isPlayerAlive(int index)
	{	if(index<playersStates.size())
			return playersStates.get(index);
		else
			return false;
	}
	
	/**
	 * Renvoie le temps restant avant le d�but du shrink (la valeur
	 * est n�gative si le shrink a d�j� commenc�).
	 * @return	temps avant le shrink en millisecondes
	 */
	protected long getTimeBeforeShrink()
	{	return timeBeforeShrink;	
	}
	
	/**
	 * Renvoie la position du bloc qui sera le prochain � se faire �craser
	 * par un bloc lors du shrink.
	 * @return	un tableau de deux entiers repr�sentant la position du prochain bloc du shrink
	 */
	protected int[] getNextShrinkPosition()
	{	return nextShrinkPosition;
	}
	
	/**
	 * Renvoie la position du personnage de l'IA.
	 * @return	les coordonn�es du personnage de l'IA
	 */
	protected int[] getOwnPosition()
	{	return ownPosition;
	}

	/**
	 * Renvoie la position de la bombe relativement au personnage de l'IA,
	 * dans le cas o� une bombe occupe la m�me case. Une constante de 
	 * la forme AI_DIR_XXXX est renvoy�e. La constante AI_DIR_NONE est renvoy�e
	 * s'il n'y a pas de bombe dans la case, ou bien si la bombe et le joueur sont plac�s
	 * au m�me endroit.
	 * @return	la position relative de la bombe
	 */
	protected int getBombPosition()
	{	return bombPosition;
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
