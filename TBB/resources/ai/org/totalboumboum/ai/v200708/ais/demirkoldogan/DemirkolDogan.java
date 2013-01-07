package org.totalboumboum.ai.v200708.ais.demirkoldogan;

import java.util.Iterator;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Turkalp Göker Demirkol
 * @author Emre Doğan
 *
 */
@SuppressWarnings("deprecation")
public class DemirkolDogan extends ArtificialIntelligence{

	
	//FIELDS
	/** */
	private Vector<Block> zoneAccessible; //les block qu'on a accès
	/** */
	private Vector<Block> path; //la serie des block qu'on doit passer pour arriver a targetBlock
	/** */
	private int[] oldPosition; //la position de l'IA dans la derniere appel
	/** */
	private Integer oldAction; //l'action de l'IA dans la derniere appel
	/** */
	private int actionMode;   //le mode de l'action
	/** */
	private int lastActionMode; //le mode de l'action de l'IA dans la derniere appel
	
	//CONSTANTS
	/** */
	private final static int DD_DANGER_POINT = 7;
	/** */
	private final static int DD_MODE_HIDE = 1;
	/** */
	private final static int DD_MODE_GET_BONUS = 2;
	/** */
	private final static int DD_MODE_ATTACK = 3;
	/** */
	private final static int DD_MODE_SHRINK = 4;
	/** */
	private final static int DD_TRACKING_CONSTANT = 10;
	/** */
	private final static long DD_SHRINK_TIME_LIMIT = 10000;
	
	//CONSTRUCTOR
	/**
	 * 
	 */
	public DemirkolDogan() {
		//initalization
		super("DoganDmrkl");
		this.zoneAccessible = new Vector<Block>();
		this.path = new Vector<Block>();
		this.oldPosition = new int[2];
		this.oldPosition[0] = -1; 
		this.oldPosition[1] = -1;
		this.oldAction = -1;
		this.actionMode = -1;
		this.lastActionMode = -1;
	}


	/** */
	public static final long serialVersionUID = 1L;
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{		
		//Les variables locaux
		//----------------------------------------
		Integer result = AI_ACTION_DO_NOTHING;
		if(firstTime)
			firstTime = false;
		else
		{	
		
		int[][] matrix = this.updateMatrix(); //matrice amelioré
		int[] ownPosition = getOwnPosition(); //le position de l'IA
		int[] bonusPosition; //si il y a une bonus, ceci contiendra sa position
		int[] bombPosition;  //si il y a une bombe, ceci contiendra sa position
		int[] firePosition; //si il y a du feu
		int bombDirection = 0; //la position du bombe relative a IA
	    int ownFirePower = getOwnFirePower(); //la portee du bombe de l'IA
	    int trackDistance = ownFirePower + DD_TRACKING_CONSTANT; //limite pour suivre un adversaire
		boolean foundOpponent = false; //vrai si un adversaire est trouvé
		Block targetBlock; //block cible
		Block ownBlock = new Block(ownPosition[0], ownPosition[1], 0); //block qu'on se trouve
	    //----------------------------------------
	    
	    //nettoyage & remettre en jour du zoneAccessible
		this.zoneAccessible.clear();
		this.updateZoneAccessible(ownBlock);
		
		
		// ########################################
		// Controle pour le Danger
	    // ########################################
		bombPosition = getClosestBlockPosition(ownPosition[0], ownPosition[1], AI_BLOCK_BOMB, matrix);
		firePosition = getClosestBlockPosition(ownPosition[0], ownPosition[1], AI_BLOCK_FIRE, matrix);
		//si il s'agit d'un bombe ou du feu dans le zone accessible
		if(((bombPosition[0] != -1) || firePosition[0] != -1)
			&& zoneAccessibleContains(new Block(bombPosition[0], bombPosition[1])) || zoneAccessibleContains(new Block(firePosition[0], firePosition[1])))
		{
				bombDirection = getBombPosition();	
				this.actionMode = DD_MODE_HIDE;
		}
		else 
		{
			// ########################################
			// Controle pour le Shrink
			// ########################################
			if (getTimeBeforeShrink() < DD_SHRINK_TIME_LIMIT)
				this.actionMode = DD_MODE_SHRINK;
			else 
			{
				// #########################################
				// Controle pour le Bonus
				// #########################################
				bonusPosition = getClosestBlockPosition(ownPosition[0], ownPosition[1], AI_BLOCK_ITEM_BOMB, matrix);
				if (bonusPosition[0] == -1)
					bonusPosition = getClosestBlockPosition(ownPosition[0], ownPosition[1],  AI_BLOCK_ITEM_FIRE, matrix);
				//si il s'agit d'un bonus dans le zone accessible
				if ((bonusPosition[0] != -1) && zoneAccessibleContains(new Block(bonusPosition[0], bonusPosition[1])))
				{
						this.actionMode = DD_MODE_GET_BONUS;
				}
				else
				{ // ######################################
				  // sinon on attaque
				  // ######################################
					this.actionMode = DD_MODE_ATTACK;
				}

			}
		}
		
		// --------------------------------------------------------
		// Si on n'a pas completé la derniere action de deplacement
		// --------------------------------------------------------
		if (this.oldPosition[0] == ownPosition[0] && this.oldPosition[1]==ownPosition[1])
			if (this.oldAction != AI_ACTION_DO_NOTHING 
				&& this.oldAction != AI_ACTION_PUT_BOMB
				&& actionMode == lastActionMode)
			{	
				//on continue a faire l'action derniere
				return this.oldAction;
			}

		
		
		//--------------------------------------------------------
		// Si on a changé le mode d'action, on nettoie le path
		if (this.actionMode != this.lastActionMode || this.actionMode==DD_MODE_HIDE){
			this.path.removeAllElements();
		}
		//--------------------------------------------------------
		
		
		// -------------------------------------------------------
		// d'apres le mode de l'action, on choisit un block cible
		// -------------------------------------------------------
		switch (this.actionMode)
		{
		case DD_MODE_HIDE:
			if (bombDirection == AI_DIR_NONE){
				targetBlock = findSafeBlock(matrix,ownBlock);
			}
			else{
				targetBlock = getAdjacentBlock(ownPosition, bombDirection,matrix);
			}
			break;
		case DD_MODE_GET_BONUS:
			if (this.path.size() == 0) {
				targetBlock = findBonus(matrix, ownBlock);
			}
			else 
				targetBlock = null;
			break;
		case DD_MODE_ATTACK:
			targetBlock = findReasonableOpponent(ownPosition);
			//si on n'a pas trouve un opponent raisonable
			if (!(targetBlock != null && (distance(ownPosition[0], ownPosition[1], targetBlock.getX(), targetBlock.getY()) <= trackDistance)))
			{
				if (this.path.size() == 0) {
					//on continue a detruire les murs
					targetBlock = findAccessibleSoftWall(ownBlock, matrix);
				}
				else 
					targetBlock = null;
			}
			else
				foundOpponent = true;
			break;
		case DD_MODE_SHRINK:
			if (this.path.size() == 0) {
				targetBlock = findBlockToAvoidShrink(matrix);
			}
			else 
				targetBlock = null;
			break;
		default:
			targetBlock = findSafeBlock(matrix,ownBlock);
			break;

		}
		
		// ---------------------------------------------
		// Calcul de block qu'on va aller a l'appel prochaine
		// utilisant l'algorithme A*
		// ---------------------------------------------
		if (targetBlock != null) {
			//on cree & initalise un objet qui fera la recherche
			AStar astar;
			astar = new AStar(ownBlock, targetBlock, matrix);
			this.path = astar.findShortestPath(); // on prend la route pour aller au block cible
		}

			
			
		// si il existe des blocks a traverser
		if (this.path.size()>0) {
			targetBlock = this.path.remove(0); //on prend la premiere element du path
			// si on se cache, on detruit la liste path
			if (this.actionMode == DD_MODE_HIDE)
			{
				this.path.clear();
			}
			// si c'est convenable, on met une bombe
			if(this.path.size() <= ownFirePower-1 && this.actionMode == DD_MODE_ATTACK && foundOpponent)
			{
				result = AI_ACTION_PUT_BOMB;
			}
			else //sinon on passe au block suivant
				{
				result = this.goToBlock(ownBlock, targetBlock); 
				}
		}
		else // si le path est vide
			if (this.actionMode == DD_MODE_ATTACK || 
					(this.actionMode == DD_MODE_SHRINK && (ownPosition[0]!=getZoneMatrixDimX()/2 || ownPosition[1]!=getZoneMatrixDimY()/2)))
			// si on est arrivé a un block qu'on veut mettre une bombe
			{
				result = AI_ACTION_PUT_BOMB;
			}
			else // si on se cache ou on n'a pas de block a aller
			{
				result = AI_ACTION_DO_NOTHING;
			}
		
		// mise en jour des variable globale
		this.oldAction = result;
		this.oldPosition = ownPosition;
		this.lastActionMode = this.actionMode;
		}
		return result;
		
	}							



	/**
	 *  Methode qui trouve un block secure qui est plus proche de l'IA 
	 *  parmi les block qu'on a acces
	 * @param matrix : la zoneMatrix amélioré
	 * @param ownBlock : le block qu'on se trouve
	 * @return un Block qui est secure
	 */
	public Block findSafeBlock(int[][] matrix, Block ownBlock)
	{		
		Block safeBlock=null;
		Iterator<Block> blockIterator = this.zoneAccessible.iterator();
		int minDistance= Integer.MAX_VALUE;
		Block tempBlock;
		while (blockIterator.hasNext())
		{
			tempBlock = blockIterator.next();
			if (matrix[tempBlock.getX()][tempBlock.getY()] == AI_BLOCK_ITEM_BOMB &&
					distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY()) < minDistance)
			{
				minDistance=distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY());
				safeBlock=tempBlock;
				
			}
			else 
				if (matrix[tempBlock.getX()][tempBlock.getY()] == AI_BLOCK_ITEM_FIRE &&
						distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY()) < minDistance)
				{
					minDistance=distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY());
					safeBlock=tempBlock;
					
				}
				else 
					if (matrix[tempBlock.getX()][tempBlock.getY()] == AI_BLOCK_EMPTY &&
							distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY()) < minDistance)
					{
						minDistance=distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY());
						safeBlock=tempBlock;
						
					}
		}
		return safeBlock; 
	}
	
	/**
	 * Methode qui trouve le bonus plus proche parmi les block qu'on a acces
	 * @param matrix la zoneMatrix amélioré
	 * @param ownBlock : le block qu'on se trouve
	 * @return un Block qui contient un bonus
	 */
	public Block findBonus(int[][] matrix, Block ownBlock)
	{

		Block bonusBlock = null;
		Iterator<Block> blockIterator = this.zoneAccessible.iterator();
		int minDistance = Integer.MAX_VALUE;
		Block tempBlock;
		while (blockIterator.hasNext())
		{
			tempBlock = blockIterator.next();
			if (matrix[tempBlock.getX()][tempBlock.getY()] == AI_BLOCK_ITEM_BOMB &&
					distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY()) < minDistance)
			{
				minDistance=distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY());
				bonusBlock=tempBlock;
				
			}
			else 
				if (matrix[tempBlock.getX()][tempBlock.getY()] == AI_BLOCK_ITEM_FIRE &&
						distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY()) < minDistance)
				{
					minDistance=distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY());
					bonusBlock=tempBlock;
					
				}
		}
		return bonusBlock;  
	}
	
	/**
	 * Methode qui trouve le block plus proche qui est entouré par un soft_wall, 
	 * qu'on veut mettre une bombe pour avancer, parmi les blocks qu'on a acces
	 * @param ownBlock le block qu'on se trouve
	 * @param matrix la zoneMatrix amélioré
	 * @return un Block qui est entouré par un soft_wall
	 */
	public Block findAccessibleSoftWall(Block ownBlock, int[][] matrix)
	{
		//on controle d'abord le block qu'on se trouve:
		if (isSurroundedBy(ownBlock, AI_BLOCK_WALL_SOFT, matrix))
		{
			return ownBlock;
		}
		else //sinon on cherche un autre
		{
			Block targetBlock = null;
			Iterator<Block> blockIterator = this.zoneAccessible.iterator();
			int minDistance= Integer.MAX_VALUE;
			Block tempBlock;
			while(blockIterator.hasNext())
			{
				tempBlock = blockIterator.next();
						
				if (isSurroundedBy(tempBlock, AI_BLOCK_WALL_SOFT, matrix) &&
						distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY()) < minDistance)
				{
					minDistance=distance(ownBlock.getX(), ownBlock.getY(), tempBlock.getX(), tempBlock.getY());
					targetBlock = tempBlock;
				}
			}
			return targetBlock;
		}
			
		
	}


	/**
	 * Cette methode ressemble a "findAccessibleSoftWall", mais il essaie
	 * de choisir un block qui est proche au centre du zone
	 * @param matrix la zoneMatrix amélioré
	 * @return un Block qui est entouré par un soft_wall proche au centre du zone
	 */
	public Block findBlockToAvoidShrink(int[][] matrix)
	{
		Block centerBlock = new Block(getZoneMatrixDimX()/2, getZoneMatrixDimY()/2);
		if (zoneAccessibleContains(centerBlock)) //si le block au centre est accessible
		{
			return centerBlock;
		}
		else
		{
			Iterator<Block> blockIterator = this.zoneAccessible.iterator();
			int distanceMin = Integer.MAX_VALUE;
			while (blockIterator.hasNext())
			{
				Block tempBlock = blockIterator.next();
				if (distance(getZoneMatrixDimX()/2, getZoneMatrixDimY()/2, tempBlock.getX(), tempBlock.getY()) < distanceMin)
				{
					distanceMin = distance(getZoneMatrixDimX()/2, getZoneMatrixDimY()/2, tempBlock.getX(), tempBlock.getY());
					centerBlock = tempBlock;
				}
								
			}
			return centerBlock;
		}
		
	}
	
	/**
	 * Une modification du methode "isObstacle" qui controle si un block
	 * est mur ou pas
	 * @param x coordonné horizontale du block
	 * @param y coordonné verticale du block
	 * @return vrai si ce block est mur, faux sinon.
	 */
	private boolean isWall(int x, int y)
	{	int[][] matrix = getZoneMatrix();
		boolean result = false;
		// murs
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		result = result || (getTimeBeforeShrink()==-1 && x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1]);
		return result;
	}
	
	/**
	 * Renvoie la position du personnage le plus proche de la
	 * position passée en paramètres. 
	 * @param x	position de référence
	 * @param y	position de référence
	 * @param blockType 
	 * @param zoneMatrix 
	 * @return	position du joueur le plus proche
	 */
	private int[] getClosestBlockPosition(int x, int y, int blockType, int[][] zoneMatrix)
	{	int minDistance = Integer.MAX_VALUE;
		int result[] = {-1,-1}; 
		for(int i=0;i<getZoneMatrixDimX();i++)
			for(int j=0;j<getZoneMatrixDimY();j++)
				if(zoneMatrix[i][j] == blockType)
				{	int tempDistance = distance(x,y,i,j); 	
					if(tempDistance<minDistance)
					{	minDistance = tempDistance;
						result[0] = i;
						result[1] = j;
					}
				}
		return result;
	}
	
	/**
	 * Calcule et renvoie la distance de Manhattan 
	 * (cf. : http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29)
	 * entre le point de coordonnées (x1,y1) et celui de coordonnées (x2,y2). 
	 * @param x1	position du premier point
	 * @param y1	position du premier point
	 * @param x2	position du second point
	 * @param y2	position du second point
	 * @return	la distance de Manhattan entre ces deux points
	 */
	private int distance(int x1,int y1,int x2,int y2)
	{	int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	}
	
	/**
	 * Methode recursive qui met en jour le blocks qu'on a acces
	 * @param b Block qu'on se trouve
	 */
	public void updateZoneAccessible(Block b)
	{
		if (!zoneAccessibleContains(b))
			this.zoneAccessible.add(b);
		
		Block adjacentBlock;
		if (!isWall(b.getX()+1, b.getY()))
		{
			adjacentBlock = new Block(b.getX()+1, b.getY());
			if (!zoneAccessibleContains(adjacentBlock))
			{
				this.zoneAccessible.add(adjacentBlock);
				updateZoneAccessible(adjacentBlock);
			}

		}
		if (!isWall(b.getX()-1, b.getY()))
		{
			adjacentBlock = new Block(b.getX()-1, b.getY());
			if (!zoneAccessibleContains(adjacentBlock)) {
				this.zoneAccessible.add(adjacentBlock);
				updateZoneAccessible(adjacentBlock);
			}
		}
		if (!isWall(b.getX(), b.getY()+1))
		{
			adjacentBlock = new Block(b.getX(), b.getY()+1);
			if (!zoneAccessibleContains(adjacentBlock)) {
				this.zoneAccessible.add(adjacentBlock);
				updateZoneAccessible(adjacentBlock);
			}
		}
		if (!isWall(b.getX(), b.getY()-1))
		{
			adjacentBlock = new Block(b.getX(), b.getY()-1);
			if (!zoneAccessibleContains(adjacentBlock)) {
				this.zoneAccessible.add(adjacentBlock);
				updateZoneAccessible(adjacentBlock);
			}
		}
	}
		
	
	/**
	 * Methode qui controle si le zoneAccessible contient un block
	 * @param b Block qu'on veut controler
	 * @return vrai si zoneAccessible contient ce block, faux sinon.
	 */
	public boolean zoneAccessibleContains(Block b)
	{
		Iterator<Block> blockIterator = this.zoneAccessible.iterator();
		boolean contains = false;
		Block tempBlock;
		while (blockIterator.hasNext() && !contains)
		{
			tempBlock = blockIterator.next();
			if ((tempBlock.getX() == b.getX()) && (tempBlock.getY() == b.getY()))
				contains = true;
		}
		return contains;	
	}
	
	/**
	 * Methode qui renvoie une action pour passer d'un block a l'autre
	 * @param ownBlock Block qu'On se trouve
	 * @param targetBlock Block qu'on veut aller
	 * @return l'action qu'il faut realiser
	 */
	public int goToBlock (Block ownBlock, Block targetBlock)
	{
		if (targetBlock.getX() > ownBlock.getX())
			return AI_ACTION_GO_RIGHT;
		else
			if (targetBlock.getX() < ownBlock.getX()) 
				return AI_ACTION_GO_LEFT;
			else
				if (targetBlock.getY() > ownBlock.getY()) 
					return AI_ACTION_GO_DOWN;
				else
					if (targetBlock.getY() < ownBlock.getY())
						return AI_ACTION_GO_UP;
					else
						return AI_ACTION_DO_NOTHING;
	}

	/**
	 * Methode qui améliore la zoneMatrix. 
	 * Si il y a une bombe, on marque les block autour de ceci avec une valeur.
	 * @return Un matrix amélioré
	 */
	public int[][] updateMatrix()
	{
		int[][] zoneMatrix = getZoneMatrix();
		for (int i=0; i<zoneMatrix.length; i++) //horizontalement
			for (int j=0; j<zoneMatrix[0].length; j++) //verticalement
				if (zoneMatrix[i][j] == AI_BLOCK_BOMB) //s'il y a une bombe
				{
					int range = getBombPowerAt(i, j);
					//d'abord on modifie vers la droite
					int k=1;
					boolean foundBombLimit = false;
					while (k <= range && !foundBombLimit)
					{
						if (!isWall(i+k, j) && zoneMatrix[i+k][j]!=AI_BLOCK_BOMB)
						{
							zoneMatrix[i+k][j] = DD_DANGER_POINT;
							
						}
						else
							foundBombLimit = true;
						k++;
					}
					//on modifie vers la gauche
					k=-1;
					foundBombLimit = false;
					while (k >= -(range) && !foundBombLimit)
					{
						if (!isWall(i+k, j) && zoneMatrix[i+k][j]!=AI_BLOCK_BOMB)
						{
							zoneMatrix[i+k][j] = DD_DANGER_POINT;
							
						}
						else
							foundBombLimit = true;
						k--;
					}
					//on modifie vers le bas
					k=1;
					foundBombLimit = false;
					while (k <= range && !foundBombLimit)
					{
						if (!isWall(i, j+k) && zoneMatrix[i][j+k]!=AI_BLOCK_BOMB)
						{
							zoneMatrix[i][j+k] = DD_DANGER_POINT;
							
						}
						else
							foundBombLimit = true;
						k++;
					}
					//on modifie vers le haut
					k=-1;
					foundBombLimit = false;
					while (k >= -(range) && !foundBombLimit)
					{
						if (!isWall(i, j+k) && zoneMatrix[i][j+k]!=AI_BLOCK_BOMB)
						{
							zoneMatrix[i][j+k] = DD_DANGER_POINT;
							
						}
						else
							foundBombLimit = true;
						k--;
					}
				}
		return zoneMatrix;
	}

	/**
	 * Methode qui controle si un block est entouré par un type de block donnée.
	 * @param center Block qu'on veut controller
	 * @param blockType le type du block
	 * @param matrix matrix amélioré
	 * @return vrai si il existe un block de type blockType autour de block donnée,
	 * faux sinon
	 */
	public boolean isSurroundedBy (Block center, int blockType, int[][] matrix)
	{
		
		if (  matrix[center.getX()+1][center.getY()] == blockType
		   || matrix[center.getX()-1][center.getY()] == blockType
		   || matrix[center.getX()][center.getY()+1] == blockType
		   || matrix[center.getX()][center.getY()-1] == blockType )
			return true;
		else
			return false;
	}
	
	/**
	 * Methode qui renvoie un block adjacent qu'on peut aller,
	 * dans le cas on est meme block qu'un bombe
	 * @param ownPosition Block qu'on se trouve
	 * @param bombDirection direction relative du bombe
	 * @param matrix matrix amélioré
	 * @return le Block adjacent
	 */
	public Block getAdjacentBlock(int[] ownPosition, int bombDirection, int[][] matrix)
	{
		Block nextBlock = null;
		Vector<Block> adjacentBlocks = new Vector<Block>();
		Block leftBlock = new Block(ownPosition[0]-1, ownPosition[1]);
		Block rightBlock = new Block(ownPosition[0]+1, ownPosition[1]);
		Block downBlock = new Block(ownPosition[0], ownPosition[1]+1);
		Block upBlock = new Block(ownPosition[0], ownPosition[1]-1);
		
		//on ajoute tous les blocks dans une liste
		adjacentBlocks.add(rightBlock);
		adjacentBlocks.add(leftBlock);
		adjacentBlocks.add(downBlock);
		adjacentBlocks.add(upBlock);
		
		//on supprime celui qui est en bombDirection
		switch (bombDirection)
		{			
		case AI_DIR_LEFT: //je dois pas aller en gauche
			adjacentBlocks.remove(leftBlock);
			break;
		case AI_DIR_RIGHT: 
			adjacentBlocks.remove(rightBlock);
			break;
		case AI_DIR_DOWN: 
			adjacentBlocks.remove(downBlock);
			break;
		case AI_DIR_UP: 
			adjacentBlocks.remove(upBlock);
			break;
		}
		//supprimer les block qui sont des possible "dead-end"s.
		boolean deadEnd = false;
		for (int i=1; i<=getBombPowerAt(ownPosition[0], ownPosition[1]) && (ownPosition[0]+i <16); i++)
		{
			if (matrix[ownPosition[0]+i][ownPosition[1]] == DD_DANGER_POINT)
				if (isSurroundedBy(new Block(ownPosition[0]+i, ownPosition[1]), AI_BLOCK_EMPTY, matrix))
					deadEnd = false;
				else
					deadEnd = true;
		}
		if (deadEnd)
			adjacentBlocks.remove(rightBlock); 
		
		deadEnd = false;
		for (int i=1; i<=getBombPowerAt(ownPosition[0], ownPosition[1]) && (ownPosition[0]-i > 0); i++)
		{
			if (matrix[ownPosition[0]-i][ownPosition[1]] == DD_DANGER_POINT)
				if (isSurroundedBy(new Block(ownPosition[0]-i, ownPosition[1]), AI_BLOCK_EMPTY, matrix))
					deadEnd = false;
				else
					deadEnd = true;
		}
		if (deadEnd)
			adjacentBlocks.remove(leftBlock);
		
		deadEnd = false;
		for (int i=1; i<=getBombPowerAt(ownPosition[0], ownPosition[1]) && (ownPosition[1]-i > 0); i++)
		{
			if (matrix[ownPosition[0]][ownPosition[1]-i] == DD_DANGER_POINT)
				if (isSurroundedBy(new Block(ownPosition[0], ownPosition[1]-i), AI_BLOCK_EMPTY, matrix))
					deadEnd = false;
				else
					deadEnd = true;
		}
		if (deadEnd)
			adjacentBlocks.remove(upBlock);
		
		deadEnd = false;
		for (int i=1; i<=getBombPowerAt(ownPosition[0], ownPosition[1]) && (ownPosition[1]+i < 14); i++)
		{
			if (matrix[ownPosition[0]][ownPosition[1]+i] == DD_DANGER_POINT)
				if (isSurroundedBy(new Block(ownPosition[0], ownPosition[1]+i), AI_BLOCK_EMPTY, matrix))
					deadEnd = false;
				else
					deadEnd = true;
		}
		if (deadEnd)
			adjacentBlocks.remove(downBlock);
		//on choisit un block du liste
		for (int i = 0; i<adjacentBlocks.size(); i++)
			if (zoneAccessibleContains(adjacentBlocks.get(i)))
				nextBlock =  adjacentBlocks.get(i);
		return nextBlock;
	}

	
	/**
	 * Methode qui retrouve un adversaire, si c'est convenable
	 * @param ownPosition le position de l'IA
	 * @return le Block de l'adversaire qu'on voudrait attaquer
	 */
	public Block findReasonableOpponent(int[] ownPosition)
	{
		Block opponentBlock = null;
		Vector<Block> opponents = new Vector<Block>();
		for (int i=0; i<getPlayerCount(); i++)
		{
			if (isPlayerAlive(i))
			{
				int[] opponentposition = getPlayerPosition(i);
				
				if (opponentposition[0] != -1) { //si ce n'est pas moi
					opponentBlock = new Block(opponentposition[0],
							opponentposition[1]);
					if (ownPosition[0] == opponentposition[0]
							&& ownPosition[1] == opponentposition[1])
						return opponentBlock;
					else {
							if (zoneAccessibleContains(opponentBlock))
								opponents.add(new Block(opponentposition[0], opponentposition[1]));
					}
				}
			}
		}
		
		Iterator<Block> blockIterator = opponents.iterator();
		int distanceMin = Integer.MAX_VALUE; 
		while (blockIterator.hasNext())
		{
			Block tempBlock = blockIterator.next();
			if (distance(ownPosition[0], ownPosition[1], tempBlock.getX(), tempBlock.getY()) < distanceMin)
				distanceMin = distance(ownPosition[0], ownPosition[1], tempBlock.getX(), tempBlock.getY());
				opponentBlock = tempBlock;			
		}
		return opponentBlock;
	}

}
