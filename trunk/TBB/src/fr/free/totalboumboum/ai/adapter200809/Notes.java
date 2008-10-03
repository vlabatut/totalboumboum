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
	
}
