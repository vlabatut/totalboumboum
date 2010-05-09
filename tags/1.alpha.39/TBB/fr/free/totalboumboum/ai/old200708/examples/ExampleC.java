package fr.free.totalboumboum.ai.old200708.examples;

import java.util.Vector;

import fr.free.totalboumboum.ai.old200708.ArtificialIntelligence;


/**
 * Classe implémentant un comportement discret : 
 * le robot rase les murs et ne pose aucune bombe.
 */
public class ExampleC extends ArtificialIntelligence
{	private static final long serialVersionUID = 1L;
	/** position du mur suivi */
	private int[] lastGuide = null;
	/** dernière position du personnage */
	private int[] lastPosition;
	
	/**
	 * Constructeur.
	 */
	public ExampleC()
	{	super("Discret");
	}
	
	public Integer call() throws Exception
	{	// init du guide
		int guide[];
		if(lastGuide == null)
			guide = initFields();
		else
			guide = updateGuide();
		// on détermine la position actuelle
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		// on détermine les déplacements possibles
		Vector<Integer> possibleMoves = getPossibleMoves(x,y);
		// on détermine le prochain déplacement
		int tries = 0;
		do
		{	int temp = getNextMoveFromGuide(guide);
			if(possibleMoves.contains(temp))
				result = temp;
			else
			{	moveGuide(guide,temp);
				tries++;
			}
		}
		while(result==ArtificialIntelligence.AI_ACTION_DO_NOTHING && tries<4);
		//
		lastPosition = getOwnPosition();
		lastGuide = guide;
		//
		return result;
	}
	
	/**
	 * Initialise le guide lors du premier appel.
	 * @return	la valeur initiale du guide
	 */
	private int[] initFields()
	{	int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		//
		lastGuide = new int[2];
		lastGuide[1] = 0;
		if(getZoneMatrix()[x+1][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD
				|| getZoneMatrix()[x+1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
			lastGuide[0] = 1;
		else
			lastGuide[0] = -1;
		lastPosition = getOwnPosition();
		return lastGuide;
	}
	
	/**
	 * Renvoie le déplacement à effectuer en fonction du guide.
	 * @param guide	le guide à traiter 
	 * @return	le déplacement à effectuer
	 */
	private int getNextMoveFromGuide(int[] guide)
	{	int result;
		if(guide[0]==1 && guide[1]<=0)
			result = ArtificialIntelligence.AI_ACTION_GO_UP;
		else if(guide[1]==-1 && guide[0]<=0)
				result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
		else if(guide[0]==-1 && guide[1]>=0)
			result = ArtificialIntelligence.AI_ACTION_GO_DOWN;
		else// if(lastGuide[1]==1 && lastGuide[0]>=0)
			result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
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
	
	/**
	 * Place le guide à la position indiquée 
	 * par l'action passée en paramètre.
	 * @param guide	guide à traiter
	 * @param move	action indiquant la nouvelle position du guide
	 */
	private void moveGuide(int[] guide, int move)
	{	switch(move)
		{	case ArtificialIntelligence.AI_ACTION_GO_UP:
				guide[0] = 0;
				guide[1] = -1;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				guide[0] = 0;
				guide[1] = 1;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				guide[0] = -1;
				guide[1] = 0;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				guide[0] = 1;
				guide[1] = 0;
				break;
		}
	}
	
	/**
	 * Met à jour la position du guide par rapport au dernier déplacement effectué
	 * @return	la nouvelle position du guide
	 */
	private int[] updateGuide()
	{	int dx = getOwnPosition()[0]-lastPosition[0];
		int dy = getOwnPosition()[1]-lastPosition[1];
		int guide[] = new int[2];
		guide[0] = lastGuide[0];
		guide[1] = lastGuide[1];
		// mise à jour à condition qu'il y ait eu un déplacement effectif
		if((dx!=0 || dy!=0) 
				// et que le nouveau guide désigne une case sans obstacle
				&& !isObstacle(getOwnPosition()[0]+lastGuide[0],getOwnPosition()[1]+lastGuide[1]))
		{	guide[0] = guide[0] - dx;
			guide[1] = guide[1] - dy;
		}
		// sinon il est inutile de modifier le guide
		return guide;
	}
}
