package tournament200708.demirkoldogan;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Vector;

import fr.free.totalboumboum.ai.adapter200708.ArtificialIntelligence;





public class AStar {
	
	private Block ownBlock;   //point de depart
	private Block targetBlock;//point d'arrivee
	private PriorityQueue<Block> openList; //liste qui contient les block a controller
	private Vector<Block> closedList; //contient les noeuds controlles
	private int[][] matrix; //zone matrix am�lior�
    
	//CONSTRUCTEUR
	public AStar(Block ownBlock, Block targetBlock, int[][] matrix) 
	{
		this.matrix = matrix;
		this.ownBlock = ownBlock;
		this.targetBlock = targetBlock;
	}
	
	/**
	 * Methode qui develope un block. Elle ajoute & supprime les blocks sur les listes globales.
	 * @param blockDeveloped
	 */
	public void developBlock(Block blockDeveloped)
	{
		Block newBlock;
		//on ajoute les nouveaux blocks a la liste ouverte
		if ( !isObstacle(blockDeveloped.getX()+1, blockDeveloped.getY()))  
		{
			newBlock = new Block(blockDeveloped.getX()+1, blockDeveloped.getY(), blockDeveloped.getCost()+1 );
			newBlock.setHeuristic(this.targetBlock.getX(), this.targetBlock.getY());
			newBlock.setParentBlock(blockDeveloped);
			if (!closedListContains(newBlock)) //si on n'a pas control� ce block
				this.openList.offer(newBlock); //on va le controller
		}
		if (!isObstacle(blockDeveloped.getX()-1, blockDeveloped.getY()))
		{
			newBlock = new Block(blockDeveloped.getX()-1, blockDeveloped.getY(), blockDeveloped.getCost()+1 );
			newBlock.setHeuristic(this.targetBlock.getX(), this.targetBlock.getY());
			newBlock.setParentBlock(blockDeveloped);
			if (!closedListContains(newBlock))
				this.openList.offer(newBlock);
		}
		if (!isObstacle(blockDeveloped.getX(), blockDeveloped.getY()+1))
		{
			newBlock = new Block(blockDeveloped.getX(), blockDeveloped.getY()+1, blockDeveloped.getCost()+1 );
			newBlock.setHeuristic(this.targetBlock.getX(), this.targetBlock.getY());
			newBlock.setParentBlock(blockDeveloped);
			if (!closedListContains(newBlock))
				this.openList.offer(newBlock);
		}
		if (!isObstacle(blockDeveloped.getX(), blockDeveloped.getY()-1))
		{
			newBlock = new Block(blockDeveloped.getX(), blockDeveloped.getY()-1, blockDeveloped.getCost()+1 );
			newBlock.setHeuristic(this.targetBlock.getX(), this.targetBlock.getY());
			newBlock.setParentBlock(blockDeveloped);
			if (!closedListContains(newBlock))
				this.openList.offer(newBlock);
		}
		
		//on ajoute le block developp� au liste
		this.closedList.add(blockDeveloped);
		
		
	}
	
	/**
	 * methode qui retrouve la serie des block qu'on doit traverser d'un block a l'autre
	 * @return un vecteur des block qu'on doit traverser
	 */
	public Vector<Block> findShortestPath()
	{
		
		BlockComparator comparator = new BlockComparator();
		//une liste qui contient les blocks a developper
		this.openList = new PriorityQueue<Block>(1, comparator);
		this.openList.offer(this.ownBlock);
		//une liste qui contient les blocks d�ja developp�s.
		this.closedList = new Vector<Block>();
		//le path en inverse
		Vector<Block> pathReverse = new Vector<Block>();
		//le path
		Vector<Block> path = new Vector<Block>();
		
		while (!this.openList.isEmpty() && !this.closedListContains(this.targetBlock))
		{
			Block blockDeveloped = this.openList.poll();
			if (!closedListContains(blockDeveloped))
				developBlock(blockDeveloped);
		}
		
		//les deux listes sont prets
		Block tempParentBlock = this.closedList.lastElement(); //on prend la derniere block du liste
		
		while (tempParentBlock != null) // on avance en passant vers son pere
		{
			pathReverse.add(tempParentBlock);
			tempParentBlock = tempParentBlock.getParentBlock();
		}
		//on veut que la liste ne contienne pas le block qu'on se trouve, donc "-2"
		for (int i=pathReverse.size()-2; i>=0; i--)
			path.add(pathReverse.elementAt(i));
		return path;
		
	}
	
	/**
	 * Methode controllant si un block est dans "openList"
	 * @param b block controll�
	 * @return vrai si la liste contient, faux sinon
	 */
	public boolean openListContains(Block b)
	{
		Iterator<Block> blockIterator = this.openList.iterator();
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
	 * Methode controllant si un block est dans "closedList"
	 * @param b block controll�
	 * @return vrai si la liste contient, faux sinon
	 */
	public boolean closedListContains(Block b)
	{
		Iterator<Block> blockIterator = this.closedList.iterator();
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
	 * Indique si la case situ�e � la position pass�e en param�tre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position � �tudier
	 * @param y	position � �tudier
	 * @return	vrai si la case contient un obstacle
	 */
	private boolean isObstacle(int x, int y)
	{	int[][] matrix = this.matrix;
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
		return result;
	}
	
	
	
	

}
