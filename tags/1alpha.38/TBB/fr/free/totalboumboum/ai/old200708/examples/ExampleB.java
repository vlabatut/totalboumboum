package fr.free.totalboumboum.ai.old200708.examples;

import java.util.Vector;

import fr.free.totalboumboum.ai.old200708.ArtificialIntelligence;


/**
 * Classe implémentant un comportement de promeneur : le robot
 * se va un peu partout, il pose rarement des bombes.
 */
public class ExampleB extends ArtificialIntelligence
{	private static final long serialVersionUID = 1L;
	/** le dernier déplacement effectué */
	private Integer lastMove;
	/**
	 * Constructeur.
	 */
	public ExampleB()
	{	super("Déambule");
		lastMove = ArtificialIntelligence.AI_ACTION_GO_UP;
	}
	
	public Integer call() throws Exception
	{	Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		// on détermine les déplacements possibles
		Vector<Integer> possibleMoves = getPossibleMoves(x,y);
		// on teste s'il est possible d'effectuer le même déplacement que précédemment
		if(possibleMoves.contains(lastMove))
			result = lastMove;
		// sinon : soit on se déplace, soit on pose une bombe
		else if(possibleMoves.size()>0)
		{	// on peut poser une bombe si on est à la fois dans un cul de sac 
			// (1 seul déplacement possible) et sur une case vide
			if(possibleMoves.size()<2 && getZoneMatrix()[x][y]==ArtificialIntelligence.AI_BLOCK_EMPTY)
				possibleMoves.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
			// on détermine aléatoirement l'action qui va être effectuée
			int index;
			do
			{	index = (int)(Math.random()*(possibleMoves.size()));			
			}
			while(index==possibleMoves.size());
			result = possibleMoves.get(index);
		}
		lastMove = result;
		return result;
	}
	
	/**
	 * Indique si la case située à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à étudier
	 * @param y	position à étudier
	 * @return	vrai si la case contient un obstacle
	 */
	private boolean isObstacle(int x, int y)
	{	int[][] matrix = getZoneMatrix();
		boolean result = false;
		// bombes
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB;
		// feu
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_FIRE;
		// murs
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		result = result || (x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1]);
		return result;
	}

	/**
	 * Indique si le déplacement dont le code a été passé en paramètre 
	 * est possible pour un personnage situé en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le déplacement à étudier
	 * @return	vrai si ce déplacement est possible
	 */
	private boolean isMovePossible(int x, int y, int move)
	{	boolean result;
		// calcum
		switch(move)
		{	case ArtificialIntelligence.AI_ACTION_GO_UP:
				result = y>0 && !isObstacle(x,y-1);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				result = y<(getZoneMatrixDimY()-1) && !isObstacle(x,y+1);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				result = x>0 && !isObstacle(x-1,y);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result = x<(getZoneMatrixDimX()-1) && !isObstacle(x+1,y);
				break;
			default:
				result = false;
				break;
		}
		return result;
	}
	
	/**
	 * Renvoie la liste de tous les déplacements possibles
	 * pour un personnage situé à la position (x,y)
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @return	la liste des déplacements possibles
	 */
	private Vector<Integer> getPossibleMoves(int x, int y)
	{	Vector<Integer> result = new Vector<Integer>();
		for(int move=AI_ACTION_GO_UP;move<=AI_ACTION_GO_RIGHT;move++)
			if(isMovePossible(x,y,move))
				result.add(move);
		return result;
	}
}
