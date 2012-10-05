package org.totalboumboum.ai.v200708.ais.gokselmenges;

import java.awt.Point;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Atahan Göksel
 * @author Erdem Mengeş
 *
 */
@SuppressWarnings("deprecation")
public class GokselMenges  extends ArtificialIntelligence
{

	private static final long serialVersionUID = 7954120902478357292L;
	int UP;
	int DOWN;
	int RIGHT;
	int LEFT;
	int MIDDLE;
	
	int CoutInitial=0;
	Vector<Integer> Costs=new Vector<Integer>();
	int x=0;
	int y=0;
	Point Goal;
	boolean path = false;
	int j1 = 0;
	int i1 = 0;
	int path1 = -1;
	int BombNumber = 1;
	boolean escaping = false;
	boolean BombNumberIncremented = false;
	

	boolean escapingFromBombIteratorNumber100 = false;
	
	
	int[][] oldSituation = null;
	Vector<Point> bombPosition = new Vector<Point>();
	int getPlayerCount = -1;
	Vector<Point> blockPoints = new Vector<Point>();
	
	boolean poserBombe = false;
	
	Vector< Point> MoveCosts = new Vector<Point>();
	Vector<Point> ZoneAccessible = new Vector<Point>();
	Vector<Point> ZoneAccessible2 = new Vector<Point>();

	Vector<Point> ZoneAccessibleEscapeBomb = new Vector<Point>();
	Vector<Point> MovedPoints = new Vector<Point>();
	Vector<Integer> pathToGo = new Vector<Integer>();
	
	
	// les couts pour l'algorithm A* pour trouver le meilleur chemin
	final int cost_empty = 1;
	final int cost_softwall = 3;
	final int cost_bonus = 2;
	final int cost_bomba = 100000;
	final int cost_feu = 100000;
	final int cost_hardwall = 100000;
	final int cost_portee = 1000;
	final int cost_distance_multiplier = 10;
	final int cost_ennemi = 5;
	
	/**
	 * 
	 */
	public GokselMenges()

	{
		super("GokslMengs");

	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{
		int result= AI_ACTION_DO_NOTHING;
		if(firstTime)
			firstTime = false;
		else
		{	

		x = getOwnPosition()[0];
		y = getOwnPosition()[1];
		
		Point CurrentPlayerPos=new Point(x,y);

				
		BombNumber += isBombExploded();
		

		

		if (path == false)
		{

		
			Goal = getClosestPlayerPositionAlive(CurrentPlayerPos);

			ZoneAccessible.clear();

			getZoneAccessible(CurrentPlayerPos);

			Goal = getObjective(ZoneAccessible);
			MovedPoints.clear();
			CoutInitial = 0;



			if (Goal == null) 
			{
				
				Goal = getClosestPlayerPositionAlive(CurrentPlayerPos);

				if (CurrentPlayerPos != Goal)
				{
					path = true;
					if (BombNumber > 0)
					{
						poserBombe = true;
						
						oldSituation = getZoneMatrix();
						if (!bombPosition.contains(CurrentPlayerPos)) 
						{
							if ((getZoneMatrix()[CurrentPlayerPos.x][CurrentPlayerPos.y] != 3)
								&& (getZoneMatrix()[CurrentPlayerPos.x][CurrentPlayerPos.y] != 7))
							{	
								
								bombPosition.add(CurrentPlayerPos);
								BombNumber--;
								
								return AI_ACTION_PUT_BOMB;
							} 
							else
							{
								
								return AI_ACTION_DO_NOTHING;
							}
						} 
						else 
						{
						
							return AI_ACTION_DO_NOTHING;
						}
					} 
					else 
					{
						
						
						path = false; 
						
						return AI_ACTION_DO_NOTHING;
					}
				} 
				else 
				{
					
					
					path = false;
				}
			}

		} 
		else 
		{
			if (poserBombe == true) 
			{
				MovedPoints.clear();
				pathToGo.clear();
				Goal = getObjectiveRunFromBomb(CurrentPlayerPos,
						BringClosestBlockPosition(CurrentPlayerPos,
								AI_BLOCK_BOMB), true);
				
				if(Goal.equals(new Point(-10,-10)))
					return AI_ACTION_DO_NOTHING;
				
				
				getPathBomb(CurrentPlayerPos, Goal);
				pathToGo = refine(MovedPoints);
				MovedPoints.clear();
				path = true;
				poserBombe = false;
				escapingFromBombIteratorNumber100 = true;

			}

			Costs.clear();
			MoveCosts.clear();
		}







		int i = 0;
		MovedPoints.add(CurrentPlayerPos);

		if(path == false)	
		{

			pathToGo.clear();

			int pathIterationCounter = 0;

			int pathIterationCounterFinish;
			if(escapingFromBombIteratorNumber100 == true) 
			{
				
				pathIterationCounterFinish = 10;
				escapingFromBombIteratorNumber100 = false;
			}
			else if(getDistance(CurrentPlayerPos, getClosestPlayerPositionAlive(CurrentPlayerPos)) <  6)
			{
				pathIterationCounterFinish = 1;
			}
			else 
				pathIterationCounterFinish = 4;
		

	
			while ((x != Goal.x) || (y != Goal.y) && (pathIterationCounter < pathIterationCounterFinish))	
			{
				pathIterationCounter++;
				
				Vector<Point> MoveablePoints = new Vector<Point>();
				Vector<Point> temp = getPossibleMoves(x, y,false);
				MoveablePoints.addAll(temp);
				Vector<Integer> MoveablePointDistances = getPointDistances(MoveablePoints, Goal);

				int pt;

				try {
					pt = minimumCostCalculation(MoveablePoints, MoveablePointDistances)[1];
				} catch (RuntimeException e) {
					e.printStackTrace();
					return AI_ACTION_DO_NOTHING;
				}



				x = MoveCosts.get(pt).x;
				y = MoveCosts.get(pt).y;


				i++;

				RemoveFromVector(pt);

				MoveablePoints.clear();
				MoveablePointDistances.clear();



			}		


			pathToGo = refine(MovedPoints);
			MovedPoints.clear();



			path = true;

		}


		if (path == true)
		{

			while (i1 < pathToGo.size())
			{
				try{
				result = pathToGo.get(i1);

				}
				catch(Exception ex)
				{

				}
				if(checkNextMoveOK(result) == true)
				{

					
					j1++;
					if ((j1 % 8 == 0) && (j1 != 0))
					{
						i1++;
					
					}

					return result; 

				}
				else {
					path = false;
					poserBombe = false; 
					return AI_ACTION_DO_NOTHING;
				}
			}

			path = false;
			i1 = 0; 
			j1 = 0;
			if(BombNumberIncremented == true)
			{
				
				
				BombNumber++;
				BombNumberIncremented = false;
			}

		}

		
		i1 = 0;			
		j1 = 0;			
		}
		return AI_ACTION_DO_NOTHING;
	}

	

	
	/**
	 * 
	 * @param directionToGO
	 * @return
	 * 		? 
	 */
	// renvoi true si le prochaine pas est sur.
	// @param directionToGo le sens a aller
	// @returns vrai si le prochain pas est sur et false sinon
	
	public boolean checkNextMoveOK(int directionToGO)
	{
		int result = 0;
		boolean currentmoveBAD = false;
		Point CurrentPos =  new Point(getOwnPosition()[0], getOwnPosition()[1]);
		if ((BetterMatrix()[getOwnPosition()[0]][getOwnPosition()[1]] == 7) 
				|| (BetterMatrix()[getOwnPosition()[0]][getOwnPosition()[1]] == 4)   )
		{
			result++;
	
			currentmoveBAD = true;
	
		}
		

		if (directionToGO == AI_ACTION_GO_UP) {
			Point temp = new Point(x, y - 1);
			// BOMBNUMBER
			if(BetterMatrix()[x][y-1] == AI_BLOCK_ITEM_BOMB)
			{
				BombNumberIncremented = true;
			}
			
			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x][y - 1] == 7)
						|| (BetterMatrix()[x][y - 1] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;
				
				}
			} else {
				return true;
			}
		} else if (directionToGO == AI_ACTION_GO_DOWN) {
			Point temp = new Point(x, y + 1);
			// BOMBNUMBER
			if(BetterMatrix()[x][y+1] == AI_BLOCK_ITEM_BOMB)
			{
				BombNumberIncremented = true;
			}
			
			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x][y + 1] == 7) 
						|| (BetterMatrix()[x][y + 1] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)){
					result++;
				
				}
			} else {
				return true;
			}
		} else if (directionToGO == AI_ACTION_GO_LEFT) {
			Point temp = new Point(x - 1, y);
			// BOMBNUMBER
			if(BetterMatrix()[x-1][y] == AI_BLOCK_ITEM_BOMB)
			{
				
				BombNumberIncremented = true;
			}
			
			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x - 1][y] == 7)
						|| (BetterMatrix()[x - 1][y] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;
				
				}
			} else {
				return true;
			}
		} else if (directionToGO == AI_ACTION_GO_RIGHT) {
			Point temp = new Point(x + 1, y);
			// BOMBNUMBER
			if(BetterMatrix()[x+1][y] == AI_BLOCK_ITEM_BOMB)
			{
				
				BombNumberIncremented = true;
			}
			
			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x + 1][y] == 7)
					|| (BetterMatrix()[x + 1][y] == 4)
					|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;
			
				}
			} else {
			
				return true;
			}
		} 

		
		
		if(currentmoveBAD == true)
		{
			return true;
		}
		else if((result == 1) && (currentmoveBAD == true))
		{
			return true;
		}
		else if((result == 1) && (currentmoveBAD == false))
		{
			path = false;
			return false;
		}
		else if(result == 0)
		{
			return true;
		}
		else
		{

			return true;
		}
		
	}
	
	/**
	 * 
	 * @return
	 * 		? 
	 */
	// @returns renvoi 0 si la bombe est detruite.
	public int isBombExploded()
	{
		int result1 = 0;
		for(int i = 0;i<bombPosition.size();i++)
		{

			if(BetterMatrix()[bombPosition.get(i).x][bombPosition.get(i).y] == 0)
			{
				bombPosition.remove(i);
				result1++;
			}
		}
			return result1;
	}
	
	/**
	 * 
	 * @param CurrentPlayerPos
	 * @param bomb
	 * @param seven
	 * @return
	 * 		? 
	 */
	// @returns renvoi le point a aller pour eviter la bombe
	public Point getObjectiveRunFromBomb(Point CurrentPlayerPos, Point bomb,boolean seven)
	{


		Point result = new Point();
		
		ZoneAccessible.clear();
		getZoneAccessibleForBomb(CurrentPlayerPos);
		
		for(int i = 0;i<ZoneAccessible.size();i++)
			if(getDistance(CurrentPlayerPos, ZoneAccessible.get(i)) == 0)
				ZoneAccessible.remove(i);
		
		if (seven == true) {
			ZoneAccessible2.addAll(ZoneAccessible);
			
			for (int i = 0; i < ZoneAccessible.size(); i++) {

				if (BetterMatrix()[ZoneAccessible.get(i).x][ZoneAccessible.get(i).y] == 7) {

					ZoneAccessible.remove(i);
					i--; 	
				}
			}
		}
		
		
		int temp;
	
		try {
			temp = getDistance(CurrentPlayerPos, ZoneAccessible.get(0));
		} catch (Exception e){
			return new Point(-10,-10);
		}
		
		int tempind = 0;

		for(int i = 0;i<ZoneAccessible.size();i++)
		{	
			if(temp > getDistance(CurrentPlayerPos, ZoneAccessible.get(i)))
			{
				temp = getDistance(CurrentPlayerPos, ZoneAccessible.get(i));
				tempind = i;
			}
		}
		
		result = ZoneAccessible.get(tempind);
		
		return result;
	}
	
	/**
	 * 
	 * @param p
	 */
	// change le vecteur contenant les pas possibles.
	// @param p les coordonnees de la bombe
		public void getZoneAccessibleForBomb(Point p)
	{
		
		if ( !ZoneAccessible.contains(p))
			ZoneAccessible.add(p);
		
		Vector<Point> result= getPossibleMovesForBomb(p.x, p.y,ZoneAccessible);
				
		for (int i=0;i<result.size();i++) 
		{
			getZoneAccessibleForBomb(result.get(i));
		}
		
		

	}
	
	
		/**
		 * 
		 * @param x
		 * @param y
		 * @param zone
	 * @return
	 * 		? 
		 */
		// change le vecteur contenant les pas possibles.
		// @param p les coordonnees de la bombe
	public Vector<Point> getPossibleMovesForBomb2 (int x,int y,Vector<Point> zone) 
	{
		
	int[] direction = new int[4];
		
		Vector<Point> points = new Vector<Point>();
		
		try {
			direction[0] = BetterMatrix()[x][y-1]; // UP
		} catch (RuntimeException e) {
			direction[0] = -1;
			
		}
		try {
			direction[1] = BetterMatrix()[x][y+1]; // DOWN
		} catch (RuntimeException e) {
			direction[1] = -1;
			
		}
		try {
			direction[2] = BetterMatrix()[x-1][y]; // LEFT
		} catch (RuntimeException e) {
			direction[2] = -1;
			
		}
		try {
			direction[3] = BetterMatrix()[x+1][y]; // RIGHT
		} catch (RuntimeException e) {
			direction[3] = -1;
			
		}
		
		
			if ((zone.contains(new Point(x, y - 1)))) {
				if (!MovedPoints.contains(new Point(x, y - 1))) {
					if ((direction[0] == 0) || (direction[0] == 5)
							|| (direction[0] == 6)|| (direction[0] == 7))
						points.add(new Point(x, y - 1));
				}
			}
			if (zone.contains(new Point(x, y + 1))) {
				if (!MovedPoints.contains(new Point(x, y + 1))) {
					if ((direction[1] == 0) || (direction[1] == 5)
							|| (direction[1] == 6)|| (direction[1] == 7))
						points.add(new Point(x, y + 1));
				}
			}
			if (zone.contains(new Point(x - 1, y))) {
				if (!MovedPoints.contains(new Point(x - 1, y))) {
					if ((direction[2] == 0) || (direction[2] == 5)
							|| (direction[2] == 6)|| (direction[2] == 7))
						points.add(new Point(x - 1, y));
				}
			}
			if (zone.contains(new Point(x + 1, y))) {
				if (!MovedPoints.contains(new Point(x + 1, y))) {
					if ((direction[3] == 0) || (direction[3] == 5)
							|| (direction[3] == 6)|| (direction[3] == 7))
						points.add(new Point(x + 1, y));
				}
			}

		return points;
	
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param zone
	 * @return
	 * 		? 
	 */
	// change le vecteur contenant les pas possibles.
	// @param p les coordonnees de la bombe
	public Vector<Point> getPossibleMovesForBomb (int x,int y,Vector<Point> zone) 
	{
		
		int[] direction = new int[4];
		
		Vector<Point> points = new Vector<Point>();

			try {
				direction[0] = oldSituation[x][y-1]; // UP
			} catch (RuntimeException e) {
				direction[0] = -1;

			}
			try {
				direction[1] = oldSituation[x][y+1]; // DOWN
			} catch (RuntimeException e) {
				direction[1] = -1;

			}
			try {
				direction[2] = oldSituation[x-1][y]; // LEFT
			} catch (RuntimeException e) {
				direction[2] = -1;

			}
			try {
				direction[3] = oldSituation[x+1][y]; // RIGHT
			} catch (RuntimeException e) {
				direction[3] = -1;

			}
			
			if (!zone.contains(new Point(x, y - 1))) {
				if ((direction[0] == 0) || (direction[0] == 5)
						|| (direction[0] == 6)|| (direction[0] == 7))  
					points.add(new Point(x, y - 1));                    
			}
			if (!zone.contains(new Point(x, y + 1))) {
				if ((direction[1] == 0) || (direction[1] == 5)
						|| (direction[1] == 6)|| (direction[1] == 7))
					points.add(new Point(x, y + 1));
			}
			if (!zone.contains(new Point(x - 1, y))) {
				if ((direction[2] == 0) || (direction[2] == 5)
						|| (direction[2] == 6)|| (direction[2] == 7))
					points.add(new Point(x - 1, y));
			}
			if (!zone.contains(new Point(x + 1, y))) {
				if ((direction[3] == 0) || (direction[3] == 5)
						|| (direction[3] == 6)|| (direction[3] == 7))
					points.add(new Point(x + 1, y));
			}

	
return points;
	
	}
	
	
	

	/**
	 * 
	 * @param CurrentPlayerPos
	 * @param Goal
	 * @return
	 * 		? 
	 */
	public Vector<Point> getPathBomb(Point CurrentPlayerPos , Point Goal)
	{

		MovedPoints.clear();
		Vector<Point> result = new Vector<Point>();
		int i = 0;
		pathToGo.clear();
		MovedPoints.add(CurrentPlayerPos);
		int x = CurrentPlayerPos.x;
		int y = CurrentPlayerPos.y;
		while ((x != Goal.x) || (y != Goal.y))	
		{
			
			Vector<Point> MoveablePoints = new Vector<Point>();
			MoveablePoints.clear();

			Vector<Point> temp = getPossibleMovesForBomb2(x, y, ZoneAccessible2);

			Vector<Point> temp2 = new Vector<Point>();
			temp2.addAll(temp);

			temp.clear();
			
			Point Ptemp = null;
			for(int aa = 0 ; aa < temp2.size() ; aa++)
			{
				if( (oldSituation[temp2.get(aa).x][temp2.get(aa).y] != AI_BLOCK_WALL_HARD) 
					&& (oldSituation[temp2.get(aa).x][temp2.get(aa).y] != AI_BLOCK_WALL_SOFT) )
					{
					Ptemp = new Point(temp2.get(aa));
						temp.add(Ptemp);

					}
			}
			
			MoveablePoints.addAll(temp);
			Vector<Integer> MoveablePointDistances = getPointDistances(MoveablePoints, Goal);

			int pt;
			try {
				pt = minimumCostCalculation(MoveablePoints, MoveablePointDistances)[1];
			} catch (RuntimeException e) {
				return null;
			}


			x = MoveCosts.get(pt).x;
			y = MoveCosts.get(pt).y;

			RemoveFromVector(pt);

			MoveablePoints.clear();
			MoveablePointDistances.clear();
			
			i++;


			
		}		
		result = MovedPoints;
		
		return result;
	}
	
	
	/**
	 * 
	 * @param p
	 */
	// retourne la zone accessible a partir d'un point donné.
	//@param p point actuel de notre bonhomme
	public void getZoneAccessible(Point p)
	{
		if ( !ZoneAccessible.contains(p))
			ZoneAccessible.add(p);
		
		Vector<Point> result= getPossibleMoves(p.x, p.y,true);
				
		for (int i=0;i<result.size();i++)  
		{
			getZoneAccessible(result.get(i));
		}
		

	}
	
	/**
	 * 
	 * @param zoneAccessible
	 * @return
	 * 		? 
	 */
	public Point getObjective(Vector<Point> zoneAccessible)
	{
		Point result = new Point();
		Vector<Point> objectives = new Vector<Point>();
		Vector<Integer> objectivesCost = new Vector<Integer>();
		int[][] matrix=BetterMatrix();
		Point myPos = new Point(getOwnPosition()[0], getOwnPosition()[1]);
		Point ennemiPos = new Point(getClosestPlayerPositionAlive(myPos));
		
		for(int i = 0;i<zoneAccessible.size();i++)
		{
			if((matrix[zoneAccessible.get(i).x][zoneAccessible.get(i).y] == AI_BLOCK_ITEM_BOMB)
					|| (matrix[zoneAccessible.get(i).x][zoneAccessible.get(i).y] == AI_BLOCK_ITEM_FIRE)
					|| (zoneAccessible.get(i).equals(ennemiPos)) )
			{
				objectives.add(new Point(zoneAccessible.get(i).x,zoneAccessible.get(i).y));
				int cost = BetterMatrix()[zoneAccessible.get(i).x][zoneAccessible.get(i).y];
				
				switch(cost)
				{
					case 5: objectivesCost.add(cost_bonus); break;
					case 6: objectivesCost.add(cost_bonus); break;
					default: objectivesCost.add(cost_ennemi); break;
				}
			}
		}
		if(objectives.size() != 0)
		{
			
			int dist = getDistance(myPos , objectives.get(0)) + objectivesCost.get(0);
			int distindice = 0;
			for(int i = 0;i<objectives.size();i++)
			{
				
				if(dist > getDistance(myPos, objectives.get(i))+ objectivesCost.get(i))
				{
					dist = getDistance(myPos, objectives.get(i)) + objectivesCost.get(i);
					distindice = i;
				}
			}
			result = objectives.get(distindice);
			
			objectives.clear();
			objectivesCost.clear();
		}
		else
		{
			int dist = getDistance(zoneAccessible.get(0) ,ennemiPos);
			int distindice = 0;
			for(int i = 0;i<zoneAccessible.size();i++)
			{
				if(dist > getDistance(zoneAccessible.get(i), ennemiPos))
				{
					dist = getDistance(zoneAccessible.get(i), ennemiPos);
					distindice = i;
				}
			}
			result = zoneAccessible.get(distindice);
			
			objectives.clear();
			objectivesCost.clear();
		}
	

			if(myPos.equals(result))
			{
				return null;
			}
			else {
			
				return result;
			}
	}
	
	
	
	
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 * 		? 
	 */
	public boolean isAdjacent(Point p1 , Point p2)
	{
		if ((((p1.x == p2.x) && (Math.abs(p1.y - p2.y) ==1))) || (((p1.y == p2.y) && (Math.abs(p1.x-p2.x) ==1))))
		{
			
			return true;
		}
		else 
		{
		
			return false;
		}
	}
	
	/**
	 * 
	 * @param MovedPoints
	 * @return
	 * 		? 
	 */
	public Vector<Integer> refine(Vector<Point> MovedPoints)
	{
		
		Vector<Integer> result = new Vector<Integer>();
		Vector<Point> preresult = new Vector<Point>();
		Vector<Point> temp = new Vector<Point>();
		
		
		preresult.add(MovedPoints.get(MovedPoints.size()-1));
		int j = 1;
		for(int i = MovedPoints.size()-1 ;i > 0 ;i--)
		{
			try{
			if(isAdjacent(MovedPoints.get(i),MovedPoints.get(i-j)) )
				{
					preresult.add(MovedPoints.get(i-j));
					i = i-j+1;
					j = 1;
					
				}
			else {
					i++;
					j++;
				}
			}
			catch(Exception ee)
			{
//				System.out.println(ee.toString()); 
			}
		}
		for (int i = preresult.size()-1;i>=0;i--)
		{
			temp.add(preresult.get(i));
		}
				
				
		for(int i = 0; i<temp.size()-1 ; i++)
		{
			if(temp.get(i).x - temp.get(i+1).x == 1 )
				result.add(AI_ACTION_GO_LEFT);
			else if(temp.get(i).x - temp.get(i+1).x == -1 )
				result.add(AI_ACTION_GO_RIGHT);
			else if(temp.get(i).y - temp.get(i+1).y == 1 )
			result.add(AI_ACTION_GO_UP);
			else if(temp.get(i).y - temp.get(i+1).y == -1 )
				result.add(AI_ACTION_GO_DOWN);	
		}
		
		for (int i = 0;i<result.size();i++)
		
			path = true;
		
		
		return result;	
	}
	
	
	
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param objORnot
	 * @return
	 * 		? 
	 */
	public Vector<Point> getPossibleMoves (int x,int y,boolean objORnot) 
	{

		
		int[] direction = new int[4];
		
		Vector<Point> points = new Vector<Point>();
		
		try {
			direction[0] = BetterMatrix()[x][y-1]; // UP
		} catch (RuntimeException e) {
			direction[0] = -1;

		}
		try {
			direction[1] = BetterMatrix()[x][y+1]; // DOWN
		} catch (RuntimeException e) {
			direction[1] = -1;

		}
		try {
			direction[2] = BetterMatrix()[x-1][y]; // LEFT
		} catch (RuntimeException e) {
			direction[2] = -1;

		}
		try {
			direction[3] = BetterMatrix()[x+1][y]; // RIGHT
		} catch (RuntimeException e) {
			direction[3] = -1;

		}
		
			
		if (objORnot == true) {
			
			
			if (!ZoneAccessible.contains(new Point(x, y - 1))) {
				if ((direction[0] == 0) || (direction[0] == 5)
						|| (direction[0] == 6))
					points.add(new Point(x, y - 1));
			}
			if (!ZoneAccessible.contains(new Point(x, y + 1))) {
				if ((direction[1] == 0) || (direction[1] == 5)
						|| (direction[1] == 6))
					points.add(new Point(x, y + 1));
			}
			if (!ZoneAccessible.contains(new Point(x - 1, y))) {
				if ((direction[2] == 0) || (direction[2] == 5)
						|| (direction[2] == 6))
					points.add(new Point(x - 1, y));
			}
			if (!ZoneAccessible.contains(new Point(x + 1, y))) {
				if ((direction[3] == 0) || (direction[3] == 5)
						|| (direction[3] == 6))
					points.add(new Point(x + 1, y));
			}

		}
		else 
		{
			if ((ZoneAccessible.contains(new Point(x, y - 1)))) {
				if (!MovedPoints.contains(new Point(x, y - 1))) {
					if ((direction[0] == 0) || (direction[0] == 5)
							|| (direction[0] == 6))
						points.add(new Point(x, y - 1));
				}
			}
			if (ZoneAccessible.contains(new Point(x, y + 1))) {
				if (!MovedPoints.contains(new Point(x, y + 1))) {
					if ((direction[1] == 0) || (direction[1] == 5)
							|| (direction[1] == 6))
						points.add(new Point(x, y + 1));
				}
			}
			if (ZoneAccessible.contains(new Point(x - 1, y))) {
				if (!MovedPoints.contains(new Point(x - 1, y))) {
					if ((direction[2] == 0) || (direction[2] == 5)
							|| (direction[2] == 6))
						points.add(new Point(x - 1, y));
				}
			}
			if (ZoneAccessible.contains(new Point(x + 1, y))) {
				if (!MovedPoints.contains(new Point(x + 1, y))) {
					if ((direction[3] == 0) || (direction[3] == 5)
							|| (direction[3] == 6))
						points.add(new Point(x + 1, y));
				}
			}

		}
		
		
	
		return points;
	
	}

	private int getDistance(Point point1,Point point2)
	{	int result = 0;
		result = result + Math.abs(point1.x-point2.x);
		result = result + Math.abs(point1.y-point2.y);
		return result;
	}
	
	/**
	 * 
	 * @param points
	 * @param Goal
	 * @return
	 * 		? 
	 */
	public Vector<Integer> getPointDistances ( Vector<Point> points,Point Goal ) {
		Vector<Integer> result= new Vector<Integer>();
		
		for (int i=0;i<points.size();i++)
		{
			result.add(getDistance(points.elementAt(i), Goal));
		}
		return result;
		
	}
	
	
	private Point getClosestPlayerPositionAlive(Point CurrentPoint)
	{	int minDistance = Integer.MAX_VALUE;
	Point result=CurrentPoint;
	for(int i=0;i<getPlayerCount();i++)
	{	
		int pos[] = getPlayerPosition(i);
		Point PlayerPos=new Point(pos[0],pos[1]);
		int temp = getDistance(CurrentPoint,PlayerPos);
		if ((temp<minDistance) && (isPlayerAlive(i)))  
		{	
			result=PlayerPos;
			minDistance = temp;
		}

	}

	return result;
	}

	
	/**
	 * 
	 * @param MinimumIndice
	 */
	public void RemoveFromVector(int MinimumIndice)
	{

		MovedPoints.add(MoveCosts.get(MinimumIndice));
		
		Costs.remove(MinimumIndice);
		MoveCosts.remove(MinimumIndice);
	}
	
	
	
	/**
	 * 
	 * @param MoveablePoints
	 * @param MoveablePointDistances
	 * @return
	 * 		? 
	 */
	// calcule le cout minimal en considerant les points a ou le bonhomme peut aller.
	public int[] minimumCostCalculation(Vector<Point> MoveablePoints,Vector<Integer> MoveablePointDistances)
	{
		int[] MinimumCost = new int[2];
		
		int TempCost=CoutInitial ;

		for (int i=0;i<MoveablePoints.size();i++)
		{
			
			
			Costs.add(TempCost + cost_empty + MoveablePointDistances.get(i));
			MoveCosts.add(MoveablePoints.get(i)) ;
			
		}

		
		int TempMini;
		try {
			TempMini = Costs.get(0);
		} catch (RuntimeException e) {
			return null;
		
		}
		
		int MiniIndice=0;
		for(int i=0;i<Costs.size();i++)
		{
			if(Costs.get(i) < TempMini)
				
			{
				TempMini=Costs.get(i);
				MiniIndice=i;
			}
		}
		
		MinimumCost[0]=TempMini;
		MinimumCost[1]=MiniIndice;
		

		CoutInitial = TempCost + cost_empty;

		
		return MinimumCost;
		
	}
	
	
	
	/**
	 * 
	 * @param MoveablePoints
	 * @param MoveablePointDistances
	 * @return
	 * 		? 
	 */
	// calcule le cout minimal en considerant les points a ou le bonhomme peut aller.
	public int[] calculateMinimumCost(Vector<Point> MoveablePoints,Vector<Integer> MoveablePointDistances)
	{
		int[] MinimumCost = new int[2];
		int TempCost=CoutInitial ;
		
		for (int i=0;i<MoveablePoints.size();i++)
		{
			int[][] tempo=BetterMatrix();
			int cost = tempo[MoveablePoints.get(i).x][MoveablePoints.get(i).y];
			
			switch (cost)
			{
				case 0 : Costs.add(TempCost + cost_empty + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break ;
				case 1 : Costs.add(TempCost + cost_softwall + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				case 2 : Costs.add(TempCost + cost_hardwall + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				case 3 : Costs.add(TempCost + cost_feu + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				case 4 : Costs.add(TempCost + cost_bomba + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				case 5 : Costs.add(TempCost + cost_bonus + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				case 6 : Costs.add(TempCost + cost_bonus + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				case 7 : Costs.add(TempCost + cost_portee + cost_distance_multiplier*MoveablePointDistances.get(i)); MoveCosts.add(MoveablePoints.get(i)) ;break;
				
			}
		}

		
		int TempMini=Costs.get(0);
		int MiniIndice=0;
		for(int i=0;i<Costs.size();i++)
		{
			if(Costs.get(i)<TempMini)
			{
				TempMini=Costs.get(i);
				MiniIndice=i;
			}
		}
		
		MinimumCost[0]=TempMini;
		MinimumCost[1]=MiniIndice;
		
		
		CoutInitial = CoutInitial + MinimumCost[0]  ; 

		return MinimumCost;
		
	}
	
	/**
	 * 
	 * @return
	 * 		? 
	 */
	// prend les donnees de getZoneMatris et renvoi une matrice amelioré en l'ajoutant les couts specifiques.
	public int[][] BetterMatrix()
	{
		int[][] ZoneMatrix= getZoneMatrix();
		
		Vector<Point> BombPos = new Vector<Point>();
		blockPoints.clear();
		BombPos = BringAllBlockPositions(AI_BLOCK_BOMB) ;
		
		for(int p = 0;p<BombPos.size();p++)
		{
			int BombePortee=getBombPowerAt(BombPos.get(p).x, BombPos.get(p).y);

			for ( int i=1;i<=BombePortee;i++)
			{
				if(BombPos.get(p).x+i<getZoneMatrixDimX()-1)
				{
					ZoneMatrix[BombPos.get(p).x+i][BombPos.get(p).y]=7;
				}
				if(BombPos.get(p).x-i>0) 
				{
					ZoneMatrix[BombPos.get(p).x-i][BombPos.get(p).y]=7;
				}
			}

			for ( int i=1;i<=BombePortee;i++)
			{
				if(BombPos.get(p).y+i<getZoneMatrixDimY()-1)
				{
					ZoneMatrix[BombPos.get(p).x][BombPos.get(p).y+i]=7;
				}
				if(BombPos.get(p).y-i>0) 
				{
					ZoneMatrix[BombPos.get(p).x][BombPos.get(p).y-i]=7;
				}
			}
		}

		
		
		return ZoneMatrix;
	}
	
	
	
	
	
	
	private Point BringClosestBlockPosition(Point CurrentPos , int blockType)
	{	int minDistance = Integer.MAX_VALUE;
		Point result=new Point(CurrentPos.x,CurrentPos.y); 
		int[][] matrix = getZoneMatrix();
		for(int i=0;i<getZoneMatrixDimX();i++)
			for(int j=0;j<getZoneMatrixDimY();j++)
				if(matrix[i][j] == blockType)
				{	int tempDistance = distance(CurrentPos.x,CurrentPos.y,i,j); 	
					if(tempDistance<minDistance)
					{	minDistance = tempDistance;
						result.x=i;
						result.y=j;
						
					}
				}
		return result;
	}
	
	private Vector<Point> BringAllBlockPositions(int blockType)
	{	


		int[][] matrix = getZoneMatrix();
		for(int i=0;i<getZoneMatrixDimX();i++)
		{
			for(int j=0;j<getZoneMatrixDimY();j++)
			{
				if(matrix[i][j] == blockType)
				{	
					Point Presult= new Point(i,j);
					blockPoints.add(Presult);
				}
			}
		}
		
		
		return blockPoints;
	}

	
	// calcule la distance entre deuz points donnes.
	private int distance(int x1,int y1,int x2,int y2)
	{	int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	}
	
	/**
	 * 
	 */
	// imprime la meilleure matrice
	public void printBetterZoneMatrix()
	{	
		int[][] zoneMatrix=BetterMatrix();

		for(int i1=0;i1<zoneMatrix[0].length;i1++)
		{	//for(int i2=0;i2<zoneMatrix.length;i2++)
				//System.out.print(zoneMatrix[i2][i1]+" ");
			//System.out.println();
		}
	}
	

	
	
}
