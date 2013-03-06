package org.totalboumboum.ai.v200708.ais.sahinyildirim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.Iterator;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Serkan Şahin
 * @author Mehmet Yıldırım
 *
 */
@SuppressWarnings("deprecation")
public class SahinYildirim extends ArtificialIntelligence {

	/** */
	Runtime r = Runtime.getRuntime();
	/** */
	public static final long serialVersionUID = 1L;	
	/** */
	private Integer lastMove;	
	/** */
	boolean bombVar = false;
	/** */
	private int tryNum = 1;
	
	// A****************************
	/** */
	private List<Integer> path = new ArrayList<Integer>();
	/** */
	private Vector<ParentChild> perler = new Vector<ParentChild>();
   
	/** */
	int pathtoFait = 0;
	/** */
	int nextMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
	/** */
	boolean lastMoveFromPutbomb = false;

	/** */
	Point lastPoint;
	/** */
	PointFind pointMeRoot;
	/** */
	PointFind pointFind;
	/** */
	PointFind pointShrink;
	
	/** */
	int monIndex;
	/** */
	int closestPlayerIndex;
	/** */
	int initialCost;
	/** */
//	private int bonusBomb = 0;
	/** */
	private int bonusFire = 1;
	
	/**
	 * Constructeur.
	 */
	public SahinYildirim() {
		super("SahnYildrm");
		lastMove = ArtificialIntelligence.AI_ACTION_GO_UP;
	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{
		int result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(firstTime)
			firstTime = false;
		else
		{	
		Point pointMe;

		// get own position
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];

		pointMe = new Point(x, y);

//		if (getZoneMatrix()[x][y] == ArtificialIntelligence.AI_BLOCK_ITEM_BOMB)
//			bonusBomb++;
		if (getZoneMatrix()[x][y] == ArtificialIntelligence.AI_BLOCK_ITEM_FIRE)
			bonusFire++;
		
		//  possible moves
		Vector<Integer> possibleMoves = new Vector<Integer>();
		
		// listes des bombes qui sont proches
		List<Integer> bombPositions = getClosestBlockPosition(x, y,
				ArtificialIntelligence.AI_BLOCK_BOMB);
				
		// si il y a des bombs alors defence()
		  if(bombPositions.size()>0) {	  
		  possibleMoves = defence(x, y, bombPositions); }
		  		  
		  else {
		  //pas de bombes donc on peut determiner les mouvements possibles
		  possibleMoves = getPossibleMoves(x, y);
		  
		  
		 //si il ya au moins un wallSoft alors on peut poser une bombe
		 if(possibleMoves.size()<2 &&
		  getZoneMatrix()[x][y]==ArtificialIntelligence.AI_BLOCK_EMPTY) {
		  if(getZoneMatrix()[x+1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT ||
		  getZoneMatrix()[x-1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT ||
		  getZoneMatrix()[x][y+1]== ArtificialIntelligence.AI_BLOCK_WALL_SOFT ||
		  getZoneMatrix()[x][y-1]== ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
		  possibleMoves.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB); } }
		 
		  
		  if(possibleMoves.contains(lastMove)) 
			  result = lastMove;
		  
		  else { // on détermine aléatoirement l'action qui va être effectuée
		  int index; do { index = (int)(Math.random()*(possibleMoves.size())); }
		  while(index==possibleMoves.size());
		 
		 
		  result = possibleMoves.get(index);
		 
		 lastMove = result; 
		 }
		 
		 //si le shrink commence alors notre joueur va au point(7,7)
		if(getTimeBeforeShrink() <= 0)
		{
		 if (bombPositions.size() == 0) {
			if (path.size() == 0) {
				
			
				pointFind = new PointFind(new Point(7,7),0,false);
				
				initialCost = distance(x, y, 7, 7);

				pointMeRoot = new PointFind(pointMe, initialCost, false);
				//cette a* algorithme va etre executer si le shrink commence
				
				findPath();
			}
			if (path.size() > 0) {
				pathtoFait++;
				bombVar = false;
		
				result = path.get(0);
				if (path.get(0) == ArtificialIntelligence.AI_ACTION_PUT_BOMB)
					path.remove(0);
				else {
					if (pathtoFait % 8 == 0) {
						path.remove(0);
						pathtoFait = 0;
					}
				}

			}
		 }
		 else
				possibleMoves = defence(x, y, bombPositions);/*
				
				//si aucun moves possible alors defence */
	}
		

		if (tryNum % 100 == 0)
			r.gc();

		tryNum++;
		
		//si il y a bonus a cote -+1 
		result = bonusNearMe(x, y, result);
		
		// si il y a des personnes qui sont dans la portee de notre bombe alors putBomb
		if(lastMoveFromPutbomb)
			result = nextMove;
		
		lastMoveFromPutbomb = false;
		nextMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(putBomb(x, y)){
			
			result = ArtificialIntelligence.AI_ACTION_PUT_BOMB;
			
		}
		}
		
		return result;
	}
	
	/**si il y a des bonus a cote mon position -+1 le prend
	 * 
	 * @param x point_x de moi
	 * @param y point_y de moi
	 * @param result 
	 * 		Description manquante !
	 * @return l'action pour prendre le bonus
	 */
	private int bonusNearMe(int x,int y,int result)
	{	
		
		
		if((getZoneMatrix()[x][y+1]== ArtificialIntelligence.AI_BLOCK_ITEM_BOMB
				|| getZoneMatrix()[x][y+1]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE))
			result =  ArtificialIntelligence.AI_ACTION_GO_DOWN;
		else if((getZoneMatrix()[x][y-1]== ArtificialIntelligence.AI_BLOCK_ITEM_BOMB
				|| getZoneMatrix()[x][y-1]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE))
			result =  ArtificialIntelligence.AI_ACTION_GO_UP;
		else if((getZoneMatrix()[x+1][y]== ArtificialIntelligence.AI_BLOCK_ITEM_BOMB
				|| getZoneMatrix()[x+1][y]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE))
			result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else if((getZoneMatrix()[x-1][y]== ArtificialIntelligence.AI_BLOCK_ITEM_BOMB
				|| getZoneMatrix()[x-1][y]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE))
			result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
	
		return result;
		
			
		
		
	}
	
	/**
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return vrai si au moins un person est dans la portee de mon bombe 
	 */
	private boolean putBomb(int x,int y)
	{
		boolean result = false;
		
		List<Integer> playerPos = getClosestPlayerPosition(x, y);
		
		for(int j=0;j<playerPos.size();j+=2)
		{
			if(bonusFire >= Math.abs(x-playerPos.get(j)) && 
					bonusFire >= Math.abs(y-playerPos.get(j+1)))
			{	
				if(monIndex == ArtificialIntelligence.AI_ACTION_GO_DOWN){
					if(isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_UP))
					nextMove = ArtificialIntelligence.AI_ACTION_GO_UP;
					else
						nextMove = ArtificialIntelligence.AI_ACTION_GO_DOWN;
				}
				if(monIndex == ArtificialIntelligence.AI_ACTION_GO_UP){
					if(isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_DOWN))
					nextMove = ArtificialIntelligence.AI_ACTION_GO_DOWN;
					else
						nextMove = ArtificialIntelligence.AI_ACTION_GO_UP;
				}
				if(monIndex == ArtificialIntelligence.AI_ACTION_GO_LEFT){
					if(isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_RIGHT))
					nextMove = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
					else
						nextMove = ArtificialIntelligence.AI_ACTION_GO_LEFT;
				}
				if(monIndex == ArtificialIntelligence.AI_ACTION_GO_RIGHT){
					if(isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_LEFT))
					nextMove = ArtificialIntelligence.AI_ACTION_GO_LEFT;
					else
						nextMove = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
				}
				
				
				lastMoveFromPutbomb = true;
					
				result = true;
			
			}
			if(x == playerPos.get(0) && y == playerPos.get(1))
				result = true;
				
		}
		
		return result;
		
		
		
	}
	
	
	

	/**  A* algorithme pour trouver le chemin qu'on va suivre    
	 * 
	 */
	private void findPath() {
		path.clear();
	

		aStarComparator nodeaSearch = new aStarComparator();

		PriorityQueue<PointFind> prq = new PriorityQueue<PointFind>(30,
				nodeaSearch);

		PriorityQueue<PointFind> prqFils = new PriorityQueue<PointFind>(30,
				nodeaSearch);
		
		
			
		
			prq.offer(pointMeRoot);

//		int count = 1;
		
		int iteration =1;

		lastPoint = pointMeRoot.getRootPoint();

	
		PointFind solution = null;

		while (!prq.isEmpty() && solution == null ) {
			PointFind node = prq.poll();

		
			
			if (node == pointFind) {
				solution = node;

			} else {
				Iterator<PointFind> i = developpeNode(node);

				while (i.hasNext()) {

					PointFind temp = i.next();

//					System.out.print("premier while** Fils = "
//							+ temp.nodeYazdir());

					prqFils.offer(temp);

				}

				if (!prq.isEmpty() && !prqFils.isEmpty()) {
				
					// si le cout du fils est plus petit que l'autre pere
					if (prq.peek().getCost() >= prqFils.peek().getCost()) {
					
						node.setNodeVisited(true);
						// si le point suivant est un wall soft alors putBomb
						if (prqFils.peek().isPointMure()) {
//							count++;
							path.add(addToPath(prqFils.peek(), node));
//							count++;
							path.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
							solution = pointFind;
						} else {
							if(prqFils.peek().equals(pointFind))
								solution = new PointFind(pointFind.getRootPoint(),0,false);
//							count++;
							path.add(addToPath(prqFils.peek(), node));
						}

//						for (int j = 0; j < path.size(); j++)
//							System.out.print("**" + path.get(j) + " Count = "
//									+ count);
						prq.clear();
						prq.addAll(prqFils);

						lastPoint = node.getRootPoint();

						prqFils.clear();

						perler.clear();

					} else if (prq.peek().getCost() < prqFils.peek().getCost()) {
					
						node = prq.poll();

						Iterator<PointFind> it = developpeNode(node);

				
						while (it.hasNext()) {

							PointFind temp = it.next();

				
							prqFils.offer(temp);

						}

						if (perler.get(0).getChildVector().contains(prqFils.peek())) {

							if (prqFils.peek().isPointMure()) {
					
								path.add(addToPath(prqFils.peek(), perler
										.get(0).getPere()));
//								count++;
								path
										.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
								solution = pointFind;
							} else {
								if (lastPoint
										.equals(pointMeRoot.getRootPoint()))
											path.add(addToPath(perler.get(0).getPere(),
													pointMeRoot));
//								count++;
								if(prqFils.peek().equals(pointFind))
									solution = new PointFind(pointFind.getRootPoint(),0,false);
								
								path.add(addToPath(prqFils.peek(), perler
										.get(0).getPere()));
							}
							lastPoint = perler.get(0).getPere().getRootPoint();
							prq.clear();
							for (int j = 0; j < perler.get(0).getChildVector()
									.size(); j++)
								prq
										.offer(perler.get(0).getChildVector()
												.get(j));

//							for (int j = 0; j < path.size(); j++)
//								System.out.print("*get(0)*" + path.get(j)
//										+ " Count = " + count);

							perler.clear();

							prqFils.clear();

						} else if (perler.get(1).getChildVector().contains(prqFils.peek())) {
							if (prqFils.peek().isPointMure()) {
							
//								count++;
								path.add(addToPath(prqFils.peek(), perler
										.get(1).getPere()));

								path
										.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
								solution = pointFind;
							} else {
								if (lastPoint
										.equals(pointMeRoot.getRootPoint()))
									path.add(addToPath(perler.get(0).getPere(),
											pointMeRoot));
								if(prqFils.peek().equals(pointFind))
									solution = new PointFind(pointFind.getRootPoint(),0,false);
								
								path.add(addToPath(prqFils.peek(), perler
										.get(1).getPere()));
//								count++;
								}
							lastPoint = perler.get(1).getPere().getRootPoint();
							prq.clear();
							for (int j = 0; j < perler.get(1).getChildVector()
									.size(); j++)
								prq
										.offer(perler.get(1).getChildVector()
												.get(j));

//							for (int j = 0; j < path.size(); j++)
//								System.out.print("*get(1)*" + path.get(j)
//										+ " Count = " + count);
							perler.clear();

							prqFils.clear();

						}
					}
				} else if (prq.isEmpty() && !prqFils.isEmpty()) {

					if (prqFils.peek().isPointMure()) {
						path.add(addToPath(prqFils.peek(), node));
//						count++;
						path.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
						solution = pointFind;
					} else {
						if (!node.equals(pointMeRoot)) {
							path.add(addToPath(prqFils.peek(), node));
							} else if (node.equals(pointMeRoot)
								&& prqFils.size() < 2) {
							if(prqFils.peek().equals(pointFind))
								solution = new PointFind(pointFind.getRootPoint(),0,false);
							
							path.add(addToPath(prqFils.peek(), node));
		
						}
					}

//					for (int j = 0; j < path.size(); j++)
//						System.out.print("*get(1)*" + path.get(j) + " Count = "
//								+ count);
					lastPoint = node.getRootPoint();
					perler.clear();
					prq.addAll(prqFils);
					prqFils.clear();

				}
				iteration ++;
		
				if(iteration == 15)
					solution = pointFind;
			}

		}

	}

	/**
	 * @param x1
	 * 		Description manquante !
	 * @param y1
	 * 		Description manquante !
	 * @param x2
	 * 		Description manquante !
	 * @param y2
	 * 		Description manquante !
	 * @return la distance entre deux points
	 */
	private int getDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);

	}

	/**
	 * la methode pour ajouter l'action suivant au path
	 * @param pointChild
	 * 		Description manquante !
	 * @param pointPere
	 * 		Description manquante !
	 * @return l'action 
 	 */
	private int addToPath(PointFind pointChild, PointFind pointPere) {
		int result;
		if (pointChild.getRootPoint().x == pointPere.getRootPoint().x) {
			if (pointChild.getRootPoint().y < pointPere.getRootPoint().y)
				result = ArtificialIntelligence.AI_ACTION_GO_UP;
			else
				result = ArtificialIntelligence.AI_ACTION_GO_DOWN;

		} else if (pointChild.getRootPoint().x > pointPere.getRootPoint().x)
			result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else
			result = ArtificialIntelligence.AI_ACTION_GO_LEFT;

		return result;

	}

	/**
	 * developpe  un point par rapport a leurs couts 
	 * on ne regarde pas le point pere, on construit un pereChild donc on sait
	 * le pere des points possibles. 
	 * @param pointDev
	 * 		Description manquante !
	 * @return un iterator pour les actions possibles
	 */
	private Iterator<PointFind> developpeNode(PointFind pointDev) {
	
		int x = pointDev.getRootPoint().x;
		int y = pointDev.getRootPoint().y;

		// on construit le pere , le point qui va se developper
		ParentChild pere = new ParentChild(pointDev);

		if (pointDev != pointFind) {
			Vector<Integer> possibleMoves = possibleAStarMoves(x, y);

			for (int j = 0; j < possibleMoves.size(); j++) {
				if (possibleMoves.get(j) == ArtificialIntelligence.AI_ACTION_GO_DOWN) {
					boolean isBlockMure = false;
					Point pointToBeAdded = new Point(x, y + 1);

					if (getZoneMatrix()[x][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
						isBlockMure = true;

					PointFind pointFindAdd = new PointFind(pointToBeAdded,
							getCost(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN,
									pointDev.getCost()), isBlockMure);
					// on ajoute le fils dans le vector du pere
					pere.addChild(pointFindAdd);

				} else if (possibleMoves.get(j) == ArtificialIntelligence.AI_ACTION_GO_UP) {
					boolean isBlockMure = false;

					Point pointToBeAdded = new Point(x, y - 1);
					if (getZoneMatrix()[x][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
						isBlockMure = true;

					PointFind pointFindAdd = new PointFind(pointToBeAdded,
							getCost(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP,
									pointDev.getCost()), isBlockMure);
					// on ajoute le fils dans le vector du pere
					pere.addChild(pointFindAdd);

				} else if (possibleMoves.get(j) == ArtificialIntelligence.AI_ACTION_GO_LEFT) {
					boolean isBlockMure = false;
					Point pointToBeAdded = new Point(x - 1, y);

					if (getZoneMatrix()[x-1][y ] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
						isBlockMure = true;

					PointFind pointFindAdd = new PointFind(pointToBeAdded,
							getCost(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT,
									pointDev.getCost()), isBlockMure);
					// on ajoute le fils dans le vector du pere
					pere.addChild(pointFindAdd);

				} else if (possibleMoves.get(j) == ArtificialIntelligence.AI_ACTION_GO_RIGHT) {
					boolean isBlockMure = false;
					Point pointToBeAdded = new Point(x + 1, y);
					if (getZoneMatrix()[x+1][y ] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
						isBlockMure = true;

					PointFind pointFindAdd = new PointFind(pointToBeAdded,
							getCost(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT,
									pointDev.getCost()), isBlockMure);
					// on ajoute le fils dans le vector du pere
					pere.addChild(pointFindAdd);

				}

			}

		}
		perler.add(pere);
		return pere.getChildVector().iterator();
	}

	/**
	 * on calcule le  cost des points
	 * 
	 * 
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param move
	 * 		Description manquante !
	 * @param pereCost
	 * 		Description manquante !
	 * @return le cost de cette point
	 */
	private int getCost(int x, int y, int move, int pereCost) {
		int cost = pereCost;

//		System.out.print("ilk Cost = " + cost);
		int differnce;//la distance de cette point au point target
		if (move == ArtificialIntelligence.AI_ACTION_GO_DOWN) {
			differnce = getDistance(x, y + 1, pointFind.getRootPoint().x,
					pointFind.getRootPoint().y);
//			System.out.print(" DownCostDifference = " + differnce);

			cost = cost + differnce;

			if (getZoneMatrix()[x][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
				cost = cost + 10;
			if (getZoneMatrix()[x][y + 1] == ArtificialIntelligence.AI_BLOCK_EMPTY)
				cost = cost + 1;

		} else if (move == ArtificialIntelligence.AI_ACTION_GO_UP) {
			differnce = getDistance(x, y - 1, pointFind.getRootPoint().x,
					pointFind.getRootPoint().y);
//			System.out.print(" UPCostDifference = " + differnce);
			cost = cost + differnce;
			if (getZoneMatrix()[x][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
				cost = cost + 10;

			if (getZoneMatrix()[x][y - 1] == ArtificialIntelligence.AI_BLOCK_EMPTY)
				cost = cost + 1;

		} else if (move == ArtificialIntelligence.AI_ACTION_GO_LEFT) {
			differnce = getDistance(x - 1, y, pointFind.getRootPoint().x,
					pointFind.getRootPoint().y);
//			System.out.print(" LeftCostDifference = " + differnce);
			cost = cost + differnce;
			if (getZoneMatrix()[x - 1][y] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
				cost = cost + 10;
			if (getZoneMatrix()[x - 1][y] == ArtificialIntelligence.AI_BLOCK_EMPTY)
				cost = cost + 1;

		} else if (move == ArtificialIntelligence.AI_ACTION_GO_RIGHT) {
			differnce = getDistance(x + 1, y, pointFind.getRootPoint().x,
					pointFind.getRootPoint().y);
//			System.out.print(" RightCostDifference = " + differnce);
			cost = cost + differnce;
			if (getZoneMatrix()[x + 1][y] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
				cost = cost + 10;
			if (getZoneMatrix()[x + 1][y] == ArtificialIntelligence.AI_BLOCK_EMPTY)
				cost = cost + 1;

		}
	
		return cost;
	}

	/**Si il y a des bombes qui sont dans la portee
	 * on calcule tous les mouvements possibles par rapport a toutes les bombes 
	 * 
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param bombs
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	private Vector<Integer> defence(int x, int y, List<Integer> bombs) {
		int xB, yB;
		int possibleLeft = 0;
		int possibleRight = 0;
		int possibleUp = 0;
		int possibleDown = 0;
		
		int t;
		Vector<Integer> possibleMoves = new Vector<Integer>();
		int bombPower[] = new int[6];
		

		t = bombs.size() / 2;

		int j = 0;
		int numBomb = 0;

		while (j < bombs.size()) {

			xB = bombs.get(j);// position x du bomb
			yB = bombs.get(j + 1);// position y
			bombPower[numBomb] = getBombPowerAt(xB, yB);
			if (Math.abs(x - xB) >= Math.abs(y - yB))// si elle est proche 
													 // a coté y
			{
				if (y == yB) {
					if (x == xB) {

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							if (isMovePossible(x, y + 1,
									ArtificialIntelligence.AI_ACTION_GO_DOWN)
									|| isMovePossible(
											x,
											y + 1,
											ArtificialIntelligence.AI_ACTION_GO_LEFT)
									|| isMovePossible(
											x,
											y + 1,
											ArtificialIntelligence.AI_ACTION_GO_RIGHT))

								possibleDown++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_LEFT))
							if (isMovePossible(x - 1, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT)
									|| isMovePossible(
											x - 1,
											y,
											ArtificialIntelligence.AI_ACTION_GO_DOWN)
									|| isMovePossible(
											x - 1,
											y,
											ArtificialIntelligence.AI_ACTION_GO_UP))

								possibleLeft++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_RIGHT))
							if (isMovePossible(x + 1, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT)
									|| isMovePossible(
											x + 1,
											y,
											ArtificialIntelligence.AI_ACTION_GO_DOWN)
									|| isMovePossible(
											x + 1,
											y,
											ArtificialIntelligence.AI_ACTION_GO_UP))

								possibleRight++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							if (isMovePossible(x, y - 1,
									ArtificialIntelligence.AI_ACTION_GO_UP)
									|| isMovePossible(
											x,
											y - 1,
											ArtificialIntelligence.AI_ACTION_GO_LEFT)
									|| isMovePossible(
											x,
											y - 1,
											ArtificialIntelligence.AI_ACTION_GO_RIGHT))

								possibleUp++;

					} else if (x < xB)// si elle est a droite
					{

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							possibleDown++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							possibleUp++;

						if (x + 1 < xB - bombPower[numBomb])
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT))
								possibleRight++;
						if (possibleDown != ((j + 2) / 2)
								&& possibleUp != ((j + 2) / 2))
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT))
								possibleLeft++;
					} else if (xB < x)// si elle est a gauche
					{
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							possibleDown++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							possibleUp++;

						if (x - 1 > xB + bombPower[numBomb])
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT))
								possibleLeft++;
						if (possibleDown != ((j + 2) / 2)
								&& possibleUp != ((j + 2) / 2))
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT))
								possibleRight++;
					}
				}

				else if (y < yB) {
					if (x < xB)// si elle est au_dessous a droite
					{
						if ((x + 1) == xB) {
							if ((y < yB - bombPower[numBomb])
									|| (getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD)
									|| (getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT))
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_RIGHT))
									possibleRight++;

						} else if ((x + 1) < xB) {
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT))
								possibleRight++;
						}
						if ((y + 1) == yB) {
							if ((x < xB - bombPower[numBomb])
									|| getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_DOWN))
									possibleDown++;

						} else if ((y + 1) < yB) {
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN))
								possibleDown++;

						}

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							possibleUp++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_LEFT))
							possibleLeft++;

					} else// si elle est au dessous a gauche
					{
						if ((x - 1) == xB) {
							if ((y < yB - bombPower[numBomb])
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_LEFT))
									possibleLeft++;

						} else if ((x - 1) > xB) {
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT))
								possibleRight++;

						}

						if ((y + 1) == yB)

						{
							if ((xB + bombPower[numBomb] < x)
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_DOWN))
									possibleDown++;

						} else if ((y + 1) < yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN))
								possibleDown++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							possibleUp++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_RIGHT))
							possibleRight++;

					}

				} else {
					if (x < xB)// si elle est au dessus a droite
					{
						if ((x + 1) == xB) {
							if (y > yB + bombPower[numBomb]
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_RIGHT))
									possibleRight++;

						} else if (x + 1 < xB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT))
								possibleRight++;
						if ((y - 1) == yB) {
							if (x < xB - bombPower[numBomb]
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(x, y,
										ArtificialIntelligence.AI_ACTION_GO_UP))
									possibleUp++;

						} else if (y - 1 > yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP))
								possibleUp++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							possibleDown++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_LEFT))
							possibleLeft++;

					} else// si elle est au dessus a gauche
					{
						if ((y - 1) == yB) {
							if (x > xB + bombPower[numBomb]
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(x, y,
										ArtificialIntelligence.AI_ACTION_GO_UP))
									possibleUp++;

						} else if (y - 1 > yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP))
								possibleUp++;

						if ((x - 1) == xB) {
							if (y > yB + bombPower[numBomb]
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_LEFT))
									possibleLeft++;

						} else if (x - 1 > xB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT))
								possibleLeft++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							possibleDown++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_RIGHT))
							possibleRight++;
					}

				}

			} else if (Math.abs(y - yB) > Math.abs(x - xB))// si elle est proche
															// a coté x
			{
				if (x == xB)//si les abscisses sont egales
				{
					if (isMovePossible(x, y,
							ArtificialIntelligence.AI_ACTION_GO_LEFT))
						possibleLeft++;
					if (isMovePossible(x, y,
							ArtificialIntelligence.AI_ACTION_GO_RIGHT))
						possibleRight++;
					if (y < yB)// si elle est au dessous
					{
						if (y + 1 < yB - bombPower[numBomb])
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN))
								possibleDown++;
						if (possibleLeft != ((j + 2) / 2)
								&& possibleRight != ((j + 2) / 2))
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP))
								possibleUp++;
					} else // si elle est au dessus
					{
						if (y - 1 > yB + bombPower[numBomb])
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP))
								possibleUp++;
						if (possibleLeft != ((j + 2) / 2)
								&& possibleRight != ((j + 2) / 2))
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN))
								possibleDown++;
					}
				} else if (x < xB) {
					if (y < yB)// si elle au dessous a droite
					{
						if ((x + 1) == xB) {
							if (y < yB - bombPower[numBomb]
									|| getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_RIGHT))
									possibleRight++;

						} else if (x + 1 < xB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT))
								possibleRight++;

						if ((y + 1) == yB) {
							if (x < xB - bombPower[numBomb]
									|| getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_DOWN))
									possibleDown++;

						} else if (y + 1 < yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN))
								possibleDown++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							possibleUp++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_LEFT))
							possibleLeft++;
					} else// si elle est au dessus a droite
					{
						if ((y - 1) == yB) {
							if (x < xB - bombPower[numBomb]
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(x, y,
										ArtificialIntelligence.AI_ACTION_GO_UP))
									possibleUp++;

						} else if (y - 1 > yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP))
								possibleUp++;
						if ((x + 1) == xB) {
							if (y - 1 > yB + bombPower[numBomb]
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x + 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_RIGHT))
									possibleRight++;

						} else if (x + 1 < xB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_RIGHT))
								possibleRight++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							possibleDown++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_LEFT))
							possibleLeft++;

					}
				} else// si elle est a gauche
				{
					if (y < yB)// si elle est au dessous a gauche
					{
						if ((x - 1) == xB) {
							if (y < yB - bombPower[numBomb]
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_LEFT))
									possibleLeft++;

						} else if (x - 1 > xB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT))
								possibleLeft++;
						if ((y + 1) == yB) {
							if (x > xB + bombPower[numBomb]
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y + 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_DOWN))
									possibleDown++;

						} else if (y + 1 < yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_DOWN))
								possibleDown++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_UP))
							possibleUp++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_RIGHT))
							possibleRight++;
					} else// si elle est au dessus a gauche
					{
						if ((x - 1) == xB) {
							if (y > yB + bombPower[numBomb]
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(
										x,
										y,
										ArtificialIntelligence.AI_ACTION_GO_LEFT))
									possibleLeft++;

						} else if (x - 1 > xB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_LEFT))
								possibleLeft++;

						if ((y - 1) == yB) {
							if (x > xB + bombPower[numBomb]
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_HARD
									|| getZoneMatrix()[x - 1][y - 1] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
								if (isMovePossible(x, y,
										ArtificialIntelligence.AI_ACTION_GO_UP))
									possibleUp++;

						} else if (y - 1 > yB)
							if (isMovePossible(x, y,
									ArtificialIntelligence.AI_ACTION_GO_UP))
								possibleUp++;

						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_DOWN))
							possibleDown++;
						if (isMovePossible(x, y,
								ArtificialIntelligence.AI_ACTION_GO_RIGHT))
							possibleRight++;

					}

				}
			}
			j += 2;

			numBomb++;
		}
		// t = nombre des bombes
		if (possibleDown == t)
			possibleMoves.add(ArtificialIntelligence.AI_ACTION_GO_DOWN);
		
		if (possibleLeft == t)
			possibleMoves.add(ArtificialIntelligence.AI_ACTION_GO_LEFT);
		
		if (possibleRight == t)
			possibleMoves.add(ArtificialIntelligence.AI_ACTION_GO_RIGHT);
		
		if (possibleUp == t)
			possibleMoves.add(ArtificialIntelligence.AI_ACTION_GO_UP);
		
		//si aucun  mouvements  alors on ne fait rien
		if (possibleMoves.size() == 0)
			possibleMoves.add(ArtificialIntelligence.AI_ACTION_DO_NOTHING);

		return possibleMoves;

	}

	/**Indique si la case située à la position passée en paramètre constitue un
	 * obstacle pour un personnage : bombe, feu, wallHard, lastPosition.
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return si il est possible
	 */
	private boolean isObstacleAStar(int x, int y) {
		int[][] matrix = getZoneMatrix();

		Point p = new Point(x, y);

		boolean result = false;

		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_WALL_HARD;

		//result = result
			//	|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		result = result || matrix[x][y] == ArtificialIntelligence.AI_BLOCK_FIRE;
		result = result || matrix[x][y] == ArtificialIntelligence.AI_BLOCK_BOMB;

		
		result = result || (x==getNextShrinkPosition()[0] &&
										y==getNextShrinkPosition()[1]);
		//on ne regarde pas au point pere
		result = result || (p.equals(lastPoint));

		return result;

	}

	/**
	 * Indique si le déplacement dont le code a été passé en paramètre est
	 * possible pour un personnage situé en (x,y).
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param move
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	private boolean isMovePossibleAStar(int x, int y, int move) {
		boolean result;
		// calcum
		switch (move) {
		case ArtificialIntelligence.AI_ACTION_GO_UP:
			result = y > 0 && !isObstacleAStar(x, y - 1);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_DOWN:
			result = y < (getZoneMatrixDimY() - 1)
					&& !isObstacleAStar(x, y + 1);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_LEFT:
			result = x > 0 && !isObstacleAStar(x - 1, y);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
			result = x < (getZoneMatrixDimX() - 1)
					&& !isObstacleAStar(x + 1, y);
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	/** Renvoie la liste de tous les déplacements possibles pour un personnage
	 * situé à la position (x,y)
	 * 
	 * @param x 
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	private Vector<Integer> possibleAStarMoves(int x, int y) {
		Vector<Integer> result = new Vector<Integer>();
		for (int move = AI_ACTION_GO_UP; move <= AI_ACTION_GO_RIGHT; move++)
			if (isMovePossibleAStar(x, y, move))
				result.add(move);
		return result;

	}

	/**
	 * Indique si la case située à la position passée en paramètre constitue un
	 * obstacle pour un personnage : bombe, feu, mur.
	 * 
	 * @param x
	 *            position à étudier
	 * @param y
	 *            position à étudier
	 * @return vrai si la case contient un obstacle
	 */
	private boolean isObstacle(int x, int y) {
		int[][] matrix = getZoneMatrix();
		
		
		boolean result = false;
		// bombes
		result = result || matrix[x][y] == ArtificialIntelligence.AI_BLOCK_BOMB;
		// feu
		result = result || matrix[x][y] == ArtificialIntelligence.AI_BLOCK_FIRE;
		// murs
		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		if(getTimeBeforeShrink()<=3)
			result = result || (x==getNextShrinkPosition()[0] &&
						y==getNextShrinkPosition()[1]);
		return result;
	}

	/**
	 * Indique si le déplacement dont le code a été passé en paramètre est
	 * possible pour un personnage situé en (x,y).
	 * 
	 * @param x
	 *            position du personnage
	 * @param y
	 *            position du personnage
	 * @param move
	 *            le déplacement à étudier
	 * @return vrai si ce déplacement est possible
	 */
	private boolean isMovePossible(int x, int y, int move) {
		boolean result;
		// calcum
		switch (move) {
		case ArtificialIntelligence.AI_ACTION_GO_UP:
			result = y > 0 && !isObstacle(x, y - 1);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_DOWN:
			result = y < (getZoneMatrixDimY() - 1) && !isObstacle(x, y + 1);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_LEFT:
			result = x > 0 && !isObstacle(x - 1, y);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
			result = x < (getZoneMatrixDimX() - 1) && !isObstacle(x + 1, y);
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	/**
	 * Renvoie la liste de tous les déplacements possibles pour un personnage
	 * situé à la position (x,y)
	 * 
	 * @param x
	 *            position du personnage
	 * @param y
	 *            position du personnage
	 * @return la liste des déplacements possibles
	 */
	private Vector<Integer> getPossibleMoves(int x, int y) {
		Vector<Integer> result = new Vector<Integer>();
		for (int move = AI_ACTION_GO_UP; move <= AI_ACTION_GO_RIGHT; move++)
			if (isMovePossible(x, y, move))
				result.add(move);
		return result;
	}

	/**
	 * liste des block type qu'on veut
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param blockType
	 * 		Description manquante !
	 * @return un arraylist contient la position des blocks
	 */
	private List<Integer> getClosestBlockPosition(int x, int y,
			int blockType) {
		List<Integer> bombalar = new ArrayList<Integer>();
		
		int[][] matrix = getZoneMatrix();
		for (int i = 0; i < getZoneMatrixDimX(); i++)
			for (int j = 0; j < getZoneMatrixDimY(); j++)
				if (matrix[i][j] == blockType) {
					
					if ((Math.abs(x - i) <= 5 && Math.abs(y - j) <= 5)) {

						bombalar.add(i);
						bombalar.add(j);

					}
				}

		return bombalar;
	}

	/**
	 * Calcule et renvoie la distance de Manhattan (cf. :
	 * http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29) entre le
	 * point de coordonnées (x1,y1) et celui de coordonnées (x2,y2).
	 * 
	 * @param x1
	 *            position du premier point
	 * @param y1
	 *            position du premier point
	 * @param x2
	 *            position du second point
	 * @param y2
	 *            position du second point
	 * @return la distance de Manhattan entre ces deux points
	 */
	private int distance(int x1, int y1, int x2, int y2) {
		int result = 0;
		result = result + Math.abs(x1 - x2);

	
		result = result + Math.abs(y1 - y2);

	
		return result;
	}

	/**
	 * Renvoie la position du personnage si il est plus proche  
	 * @param x	position de référence
	 * @param y	position de référencepol
	 * @return	position du joueur le plus proche
	 */
	private List<Integer> getClosestPlayerPosition(int x, int y) {
		List<Integer> players = new ArrayList<Integer>();
		int minDistance = Integer.MAX_VALUE;
		for (int i = 0; i < getPlayerCount(); i++) {
			if (isPlayerAlive(i)) {

				int pos[] = getPlayerPosition(i);
				int temp = getDistance(x, y, pos[0],pos[1]);
				
				if(temp< minDistance)
					closestPlayerIndex = i;
				
				if ((pos[0] != -1) && (pos[1] != -1)) {
					players.add(pos[0]);

					players.add(pos[1]);
					
					if(pos[0]==x&&pos[1]==y)
						monIndex =getPlayerDirection(i);
				}
			}
		}

		return players;
	}

}
