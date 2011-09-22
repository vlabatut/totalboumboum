package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 *
 * @author Nadin Kökciyan
 * @author Hikmet Mazmanoğlu
 *
 */
public class KokciyanMazmanoglu extends ArtificialIntelligence
{
	/** la case occupée actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;

	private double FieldMatrix[][];
	private AiZone zone;
	private Tree tree;

	int bombeLine=-1;
	int bombeCol=-1;
	private AiTile ciblePos=null;



	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		zone = getPercepts();

		Collection<AiBomb> bombeListe = zone.getBombs();

		AiHero ownHero = zone.getOwnHero();
		this.currentTile = zone.getTile(ownHero.getLine(), ownHero.getCol());
		FieldMatrix = new double[zone.getWidth()][zone.getHeight()];

		AiAction result = new AiAction(AiActionName.NONE);
		getMatrixValues(FieldMatrix);

		if (ownHero != null) {

			if (!isTimeToRun2()) {

				int[] coor = getClosestPlayerPosition();

				if (distance(ownHero.getTile(), zone.getTile(coor[0], coor[1])) <= 5
						&& coor[0] != -1
						&& coor[1] != -1
						&& !isTrap(zone.getTile(coor[0], coor[1]), ownHero
								.getTile())) {
					if (bombeListe.size() == 0) 
						result = new AiAction(AiActionName.DROP_BOMB);



				} else {
					tree = new Tree(coor[0], coor[1], this);
					AiAction turn = algoAEtoile(ownHero.getTile(), zone
							.getTile(coor[0], coor[1]));
					result = turn;

				}

			}// ifrun
			else {
				int[] coor = getClosestPlayerPosition();
				tree = new Tree(coor[0], coor[1], this);
				AiAction turn = algoAEtoile(ownHero.getTile(), zone.getTile(coor[0], coor[1]));
				result = turn;
				if (turn.getName() == AiActionName.NONE) {
					ciblePos = FindSafeCible(bombeListe.iterator().next()
							.getTile(), ownHero.getTile());
					if (ciblePos != null) {
						tree = new Tree(ciblePos.getLine(), ciblePos.getCol(), this);

						result = algoAEtoile(ownHero.getTile(), ciblePos);
					} else
						result = new AiAction(AiActionName.NONE);


				}// elserun
			}
		}

		return result;
	}
	/**
	 * 
	 * @param startTile
	 * @param targetTile
	 * @return
	 * @throws StopRequestException
	 */
	public  AiAction algoAEtoile(AiTile startTile, AiTile targetTile) throws StopRequestException{
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		this.nextTile = this.currentTile;
		Node startNode = tree.convertToNode(startTile);

		Node endNode = tree.convertToNode(targetTile);


		boolean control = false; //test si le noeud est final.

		NodeComparator nc = new NodeComparator(startNode, endNode);
		PriorityQueue<Node> qNode = new PriorityQueue<Node>(1,nc);
		qNode.offer(startNode);
		while(!control && !qNode.isEmpty()){
			checkInterruption();
			Node nActual = qNode.poll();
			if((nActual.getLine() == endNode.getLine()) && (nActual.getCol() == endNode.getCol())){
				Vector<Link> path = tree.getPath(nActual);
				//System.out.println("N "  + nActual.getName());
				if(!path.isEmpty()){
					//System.out.println(path.size());
					//for(int i = 0; i < path.size() ; i++){
					//checkInterruption();
					//System.out.println("P "  + path.elementAt(i).getAction().getDirection().name());

					//}

					result =  path.elementAt(0).getAction();	
					this.nextTile = path.elementAt(0).getChild().convertToTile();
				}
				control = true;
			}
			else{
				Iterator<Link> isl =  tree.developNode(nActual);
				while(isl.hasNext()){
					checkInterruption();
					Node nextNode = isl.next().getChild();
					qNode.offer(nextNode);
				}
			}
		}

		this.previousTile = this.currentTile;
		this.currentTile = this.nextTile;

		/*
		System.out.println("Startnode : " + startNode.getCol() + " " + startNode.getLine());
		System.out.println("Endnode : " + endNode.getCol() + " " + endNode.getLine());
		System.out.println(result.getDirection().name());
		System.out.println();
		 */
		return result;
	}


	/**
	 * @param FieldMatrix[][]
	 * @throws StopRequestException 
	 */	
	public void getMatrixValues(double FieldMatrix[][]) throws StopRequestException{
		checkInterruption();


		for(int i = 0; i<zone.getWidth(); i++){
			checkInterruption();	
			for(int j = 0; i<zone.getHeight(); i++){
				checkInterruption();

				AiTile tActual = zone.getTile(i, j);
				if(i==zone.getOwnHero().getLine() && j == zone.getOwnHero().getCol())
					FieldMatrix[i][j]=0;
				else if(isObstacle(tActual)){
					FieldMatrix[i][j]=Integer.MAX_VALUE;
				}
				//else if(!(tActual.getHeroes().isEmpty()))
				//FieldMatrix[i][j]=1.0;
				else{
					FieldMatrix[i][j]=distance(getCurrentTile(), zone.getTile(i, j));
					//FieldMatrix[i][j]=1;
				}
			}
			getbombs();

		}

	}

	/**
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException 
	 */
	public boolean isObstacle(AiTile tile) throws StopRequestException{
		checkInterruption();
		if(tile.getBlock()==null && tile.getBombs().size()==0 && tile.getFires().size()==0)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws StopRequestException 
	 */
	public boolean isObstacle(int line, int col) throws StopRequestException{
		checkInterruption();
		AiTile tile= zone.getTile(line,col);
		return isObstacle(tile);
	}
	/**
	 * Regarde le contenu de la case
	 * @param tile
	 * @return true si le contenu est une bombe, false sinon
	 * @throws StopRequestException 
	 */
	public boolean isBomb(AiTile tile) throws StopRequestException{
		checkInterruption();
		Collection<AiBomb> bombCol= tile.getBombs();
		if(bombCol.size()==0)
			return false; // pas de bombe
		else
			return true; // il y a au moins une bombe
	}

	/**
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public boolean isWall(AiTile tile) throws StopRequestException{
		checkInterruption();
		AiBlock block= tile.getBlock();
		if(block==null)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public boolean isSoft(AiTile tile) throws StopRequestException{
		checkInterruption();
		AiBlock block= tile.getBlock();
		if(block!=null){
			if(block.isDestructible())
				return true;
			else 
				return false;
		}
		else return false;
	}

	/**
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public boolean isFire(AiTile tile) throws StopRequestException{
		checkInterruption();
		Collection<AiFire> fireCol= tile.getFires();
		if(fireCol.size()==0)
			return false;
		else
			return true;
	}

	/**
	 * distance de Manhattan
	 * @param source
	 * @param target
	 * @return
	 * @throws StopRequestException 
	 */
	public  int distance(AiTile source, AiTile target) throws StopRequestException{
		checkInterruption();
		int res=0;

		res=Math.abs(source.getLine()-target.getLine())+ Math.abs(source.getCol()-target.getCol());

		return res;
	}

	/**
	 * 
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException 
	 */
	public  boolean isMovePossible(AiTile tile, Direction d) throws StopRequestException
	{	checkInterruption();
	boolean result;

	switch(d)
	{	case UP:
		result = tile.getLine()>0 && !isObstacle(tile.getLine()-1,tile.getCol());
		break;
	case DOWN:
		result = tile.getLine()<(zone.getHeight()-1) && !isObstacle(tile.getLine()+1,tile.getCol());
		break;
	case LEFT:
		result = tile.getCol()>0 && !isObstacle(tile.getLine(),tile.getCol()-1);
		break;
	case RIGHT:
		result = tile.getCol()<(zone.getWidth()-1) && !isObstacle(tile.getLine(),tile.getCol()+1);
		break;
	default:
		result = false;
	break;
	}
	return result;
	}

	/**
	 * 
	 * @param line
	 * @param col
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	public  boolean isMovePossible(int line,int col, Direction d) throws StopRequestException{
		checkInterruption();
		AiTile tile= zone.getTile(line, col);
		return isMovePossible(tile, d);
	}

	/**
	 * 
	 * @param ai
	 * @param bombTile
	 * @return
	 * @throws StopRequestException
	 */
	public  boolean isTrap(AiTile ai,AiTile bombTile) throws StopRequestException
	{	checkInterruption();
	Collection<AiBomb> bombList= bombTile.getBombs();
	Iterator<AiBomb> iterBomb= bombList.iterator();

	boolean result = false;

	if (iterBomb.hasNext()) {
		AiBomb bombe = iterBomb.next();
		int power = bombe.getRange();//s'il existe une bombe en ces coordonnées on parle d'un désir du défense (le personnage veut s'enfuir par une bombe)
		result = false;
		if (ai.getCol() - bombTile.getCol() > 0) {//si la bombe est à gauche
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers le bas c'est une piège
			if (!isMovePossible(ai, Direction.RIGHT)
					&& !isMovePossible(ai, Direction.UP)
					&& !isMovePossible(ai, Direction.DOWN)) {
				result = true;
			} else {
				while (i < power)//on va étudier une distance i partant 1 jusqu'à la portée de la bombe
				{
					if (bombTile.getCol() + i < zone.getWidth())//si le personnage+la portée de la bombe est dans la zone du jeu
					{
						if (possibleMoveD(ai.getLine(), ai.getCol(), i, 1))//s'il est possible d'aller vers la droite pour la distance i
						{
							//s'il est impossible de trouver une place à se cacher en se déplacant pour une distance i
							if (!isMovePossible(ai.getLine() + i, ai.getCol(),
									Direction.UP)
									&& !isMovePossible(ai.getLine() + i, ai
											.getCol(), Direction.DOWN)) {
								result = true;
							} else//il est possible de se cacher
							{
								result = false;
								break;//pas besoin de continuer à étudier
							}
						} else//s'il est impossible d'aller vers la droite pour la distance i, il sera impossible de s'enfuir 
						{
							result = true;
							break;
						}
					}
					i++;
				}
			}
		}
		if (ai.getCol() - bombTile.getCol() < 0)//si la bombe est à droite
		{
			int i = 1;
			//s'il est impossible de faire un mouvement vers la gauche, vers le haut ou vers le bas c'est une piège
			if (!isMovePossible(ai.getLine(), ai.getCol(), Direction.LEFT)
					&& !isMovePossible(ai.getLine(), ai.getCol(), Direction.UP)
					&& !isMovePossible(ai.getLine(), ai.getCol(),
							Direction.DOWN)) {
				result = true;
			} else {
				while (i < power) {
					if (bombTile.getCol() - i > 0) {
						if (possibleMoveD(ai.getLine(), ai.getCol(), i, -1)) {
							if (!isMovePossible(ai.getLine() - i, ai.getCol(),
									Direction.UP)
									&& !isMovePossible(ai.getLine() - i, ai
											.getCol(), Direction.DOWN)) {
								result = true;
							} else {
								result = false;
								break;
							}
						} else {
							result = true;
							break;
						}
					}
					i++;
				}
			}
		}
		if (ai.getLine() - bombTile.getLine() > 0) {//si la bombe est en haut
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers la gauche ou vers le bas c'est une piège
			if (!isMovePossible(ai.getLine(), ai.getCol(), Direction.LEFT)
					&& !isMovePossible(ai.getLine(), ai.getCol(),
							Direction.RIGHT)
							&& !isMovePossible(ai.getLine(), ai.getCol(),
									Direction.DOWN)) {
				result = true;
			} else {
				while (i < power) {
					if (bombTile.getLine() + i < zone.getHeight()) {
						if (possibleMoveD(ai.getLine(), ai.getCol(), i, -2)) {
							if (!isMovePossible(ai.getLine(), ai.getCol() + i,
									Direction.LEFT)
									&& !isMovePossible(ai.getLine(), ai
											.getCol()
											+ i, Direction.RIGHT)) {
								result = true;
							} else {
								result = false;
								break;
							}
						} else {
							result = true;
							break;
						}
					}
					i++;
				}
			}
		}
		if (ai.getLine() - bombTile.getLine() < 0) {//si la bombe est en bas
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers la gauche c'est une piège
			if (!isMovePossible(ai.getLine(), ai.getCol(), Direction.LEFT)
					&& !isMovePossible(ai.getLine(), ai.getCol(),
							Direction.RIGHT)
							&& !isMovePossible(ai.getLine(), ai.getCol(), Direction.UP)) {
				result = true;
			} else {
				while (i < power) {
					if (bombTile.getLine() - i > 0) {
						if (possibleMoveD(ai.getLine(), ai.getCol(), i, 2)) {
							if (!isMovePossible(ai.getLine(), ai.getCol() - i,
									Direction.LEFT)
									&& !isMovePossible(ai.getLine(), ai
											.getCol()
											- i, Direction.RIGHT)) {
								result = true;
							} else {
								result = false;
								break;
							}
						} else {
							result = true;
							break;
						}
					}
					i++;
				}
			}
		}
		int i = power + 1;
		//si la bombe est à gauche et le personnage peut aller vers la droite pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		if (ai.getCol() - bombTile.getCol() > 0
				&& possibleMoveD(bombTile.getLine(), ai.getCol(), i, 1))
			result = false;
		//si la bombe est à droite et le personnage peut aller vers la gauche pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		else if (ai.getCol() - bombTile.getCol() < 0
				&& possibleMoveD(bombTile.getLine(), ai.getCol(), i, -1))
			result = false;
		//si la bombe est en bas et le personnage peut aller vers le haut pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		if (ai.getLine() - bombTile.getLine() < 0
				&& possibleMoveD(ai.getLine(), bombTile.getCol(), i, 2))
			result = false;
		//si la bombe est en haut et le personnage peut aller vers le bas pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		else if (ai.getLine() - bombTile.getLine() > 0
				&& possibleMoveD(ai.getLine(), bombTile.getCol(), i, -2))
			result = false;
	}
	return result;
	}


	/**
	 * d distanci kadar, s dogrultusunda hareket
	 * @param x
	 * @param y
	 * @param d
	 * @param s
	 * @return
	 * @throws StopRequestException 
	 */
	public  boolean possibleMoveD(int x,int y,int d, int s) throws StopRequestException{ boolean result=false;
	checkInterruption();  
	switch(s){

	case 1: // x right
		result=true;
		while(d>0){
			if(x+d<zone.getWidth()){
				if(!isObstacle(x+d,y))//s'il n'y existe pas un bloc
					result=true;
				else//il y a un bloc
				{
					result=false;
					break;// car il est impossible d'y aller
				}
			}
			d--;
		}
		break;

	case -1: // x left
		result=true;
		while(d>0){
			if(x-d>=0){
				if(!isObstacle(x-d,y))
					result=true;
				else
				{
					result=false;
					break;
				}
			}
			d--;
		}
		break;

	case 2: // y up
		result=true;
		while(d>0){
			if(y-d>=0){
				if(!isObstacle(x,y-d))
					result=true;
				else
				{
					result=false;
					break;
				}
			}
			d--;
		}
		break;

	case -2: // y down
		result=true;
		while(d>0){
			if(y+d<zone.getHeight()){
				if(!isObstacle(x,y+d))
					result=true;
				else
				{
					result=false;
					break;
				}
			}
			d--;
		}
		break;
	}
	return result;
	}

	/**
	 * en yakin oyuncunun x ve y koordinatlari
	 * @return
	 * @throws StopRequestException 
	 */
	public  int[] getClosestPlayerPosition() throws StopRequestException
	{ checkInterruption();
	int minDistance = Integer.MAX_VALUE;
	int result[] = {-1,-1};
	for(int i=0;i<zone.getHeroes().size();i++)
	{ AiHero hero = zone.getHeroes().iterator().next();
	int temp = distance(zone.getTile(zone.getOwnHero().getLine(),zone.getOwnHero().getCol()),zone.getTile(hero.getLine(),hero.getCol()));
	if(temp<minDistance)
	{ result[0] = hero.getLine();
	result[1] = hero.getCol();
	minDistance = temp;
	}
	}
	return result;
	}

	/**
	 * en yakin bombayi bul ve x,y dondur
	 * @param tile
	 * @return
	 * @throws StopRequestException 
	 */
	public int[] getClosestBombPosition(AiTile tile) throws StopRequestException {
		checkInterruption();
		int minDistance = Integer.MAX_VALUE;
		int result[] = { -1, -1 };

		Iterator<AiBomb> iterBomb = zone.getBombs().iterator();

		while (iterBomb.hasNext()) {
			AiBomb it= iterBomb.next();
			if (distance(tile, it.getTile()) < minDistance) {
				minDistance = distance(tile, it.getTile());
				result[0] = it.getLine();
				result[1] = it.getCol();
			}
		}

		return result;
	}

	/**
	 * komsularda bomba gorursen kac
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public  boolean isTimeToRun(AiTile tile) throws StopRequestException {
		checkInterruption();
		Iterator<AiTile> iterTile = zone.getNeighborTiles(tile).iterator();
		boolean res = false;
		if(isBomb(tile))
			res= true;
		else{
			while (iterTile.hasNext() && !res) {
				AiTile t=iterTile.next();
				if (isBomb(t))
					res = true;
			}
		}

		return res;
	}



	/**
	 * bombanin etkisinden uzak miyim
	 * @param bombeTile
	 * @param me
	 * @return
	 * @throws StopRequestException 
	 */
	public  boolean isSafe(AiTile bombeTile,AiTile me) throws StopRequestException{
		checkInterruption();
		boolean result=true;
		AiBomb bombe= bombeTile.getBombs().iterator().next();
		if((bombeTile.getLine()==me.getLine() || bombeTile.getCol()==me.getCol()) && bombe.getRange()+2>= distance(bombeTile,me) ){
			result=result && isBlockBetween(bombeTile,me);
		}
		else
			result=true;		
		return result;
	}

	/**
	 * 2 case arasinda duvar var mi
	 * @param t1
	 * @param t2
	 * @return
	 * @throws StopRequestException
	 */
	public  boolean isBlockBetween(AiTile t1, AiTile t2) throws StopRequestException{
		checkInterruption();
		boolean result=false;
		if(t2.getLine()==t1.getLine()){
			if(t2.getCol() < t1.getCol()){
				int count=t1.getCol()-t2.getCol();
				for(int i=1;i<count;i++){
					if(isVoisinMur(zone.getTile(t2.getLine(), t2.getCol()+i),Direction.DOWN)){
						result=true;
						break;
					}
				}

			}
			if(t2.getCol() > t1.getCol()){
				int count=t2.getCol()-t1.getCol();
				for(int i=1;i<count;i++){
					if(isVoisinMur(zone.getTile(t2.getLine(), t2.getCol()-i), Direction.UP)){
						result=true;
						break;
					}
				}

			}
		}
		else if(t2.getCol()==t1.getCol()){
			if(t2.getLine()<t1.getLine()){
				int count=t1.getLine()-t2.getLine();
				for(int i=1;i<count;i++){
					if(isVoisinMur(zone.getTile(t2.getLine()+i,t2.getCol()),Direction.RIGHT)){
						result=true;
						break;
					}
				}
			}
			if(t2.getLine()>t1.getLine()){
				int count=t2.getLine()-t1.getLine();
				for(int i=1;i<count;i++){
					if(isVoisinMur(zone.getTile(t2.getLine()-i, t2.getCol()),Direction.LEFT)){
						result=true;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * komsular arasinda wall var mi
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	public  boolean isVoisinMur(AiTile tile,Direction d) throws StopRequestException{
		checkInterruption();
		boolean res=false;
		Iterator<AiTile> iterTile = zone.getNeighborTiles(tile).iterator();
		int xTemp;
		int yTemp;
		AiTile tileTemp;
		while(iterTile.hasNext()){
			switch(d){
			case UP:
				xTemp=tile.getLine();
				yTemp=tile.getCol()-1;
				tileTemp=zone.getTile(yTemp,xTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			case DOWN:
				xTemp=tile.getLine();
				yTemp=tile.getCol()+1;
				tileTemp=zone.getTile(yTemp,xTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			case RIGHT:
				xTemp=tile.getLine()+1;
				yTemp=tile.getCol();
				tileTemp=zone.getTile(yTemp,xTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			case LEFT:
				xTemp=tile.getLine()-1;
				yTemp=tile.getCol();
				tileTemp=zone.getTile(yTemp,xTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			}

		}
		return res;
	}

	/**
	 * olasi yapilabilecek hareketler
	 * @param x
	 * @param y
	 * @return
	 * @throws StopRequestException
	 */
	public  Vector<Direction> getPossibleMoves(int line, int col) throws StopRequestException
	{	checkInterruption();
	Vector<Direction> result = new Vector<Direction>();

	if(isMovePossible(line,col,Direction.UP))
		result.add(Direction.UP);
	if(isMovePossible(line,col,Direction.DOWN))
		result.add(Direction.DOWN);
	if(isMovePossible(line,col,Direction.RIGHT))
		result.add(Direction.RIGHT);
	if(isMovePossible(line,col,Direction.LEFT))
		result.add(Direction.LEFT);

	return result;
	}



	/**
	 * 
	 * @param bombe
	 * @param me
	 * @return
	 * @throws StopRequestException
	 */
	public AiTile FindSafeCible(AiTile bombe, AiTile me) throws StopRequestException {
		checkInterruption();
		Collection<AiTile> cases = getPercepts().getNeighborTiles(bombe);
		Iterator<AiTile> iterCase = cases.iterator();
		Vector<AiTile> liste = new Vector<AiTile>();
		AiTile cible = null;
		//int dist = 1000;
		int i = 0; // 0: yukari 1: sol 2: asagi 3: sag
		//int temp = 0;
		while (iterCase.hasNext()) {
			checkInterruption();
			AiTile t = iterCase.next();
			if (i == 0 && isWall(t)) {
//				System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, -2)) { // 2 kadar
					// asagi
					liste
					.add(zone.getTile(bombe.getLine()+2, bombe
							.getCol()));

				}
			}
			if (i == 1 && isWall(t)) {
//				System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, -1)) {// 2 kadar
					// sol
					liste
					.add(zone.getTile(bombe.getLine(),
							bombe.getCol() - 2));

				}
			}
			if (i == 2 && isWall(t)) {
//				System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, 1)) { // 2
					// kadar
					// saga
					liste
					.add(zone.getTile(bombe.getLine(), bombe.getCol()+2));

				}
			}
			if (i == 3 && isWall(t)) {
//				System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, 2)) { // 2 kadar
					// yukari
					liste
					.add(zone.getTile(bombe.getLine()-2,
							bombe.getCol()));

				}
			}
			i++;
		}

		if (liste.size() != 0) {

			Iterator<AiTile> iterListe = liste.iterator();
			int mindistance = 1000;
			while (iterListe.hasNext()) {
				checkInterruption();
				AiTile t2 = iterListe.next();
				if (distance(t2, me) < mindistance) {
					mindistance = distance(t2, me);
					cible = t2;
				}
			}
		} else {
			List<AiTile> possible = new ArrayList<AiTile>();
			possible.add(zone.getTile(me.getLine()-2,me.getCol()-1));// sol ust capraz
			possible.add(zone.getTile(me.getLine()-2,me.getCol()+1));// sag ust capraz
			possible.add(zone.getTile(me.getLine()+2,me.getCol()-1));// sol alt capraz
			possible.add(zone.getTile(me.getLine()+2,me.getCol()+1));// sag alt capraz

			List<AiTile> result= new ArrayList<AiTile>();

			for(int k=0;k<possible.size();k++){
				if(!isObstacle(possible.get(k))){
					result.add(possible.get(k));
				}
			}
			if(result.size()>0)
				cible= result.get(0);
			else result = null;
		}

		return cible;
	}



	/**
	 * 
	 * @return
	 */
	public  double[][] getFieldMatrix() {
		return FieldMatrix;
	}
	/**
	 * 
	 * @return
	 */
	public  AiTile getCurrentTile() {
		return currentTile;
	}
	/**
	 * 
	 * @return
	 */
	public  AiTile getPreviousTile() {
		return previousTile;
	}




	/**
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public  List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

	// liste des cases autour de la case de référence
	Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
	// on garde les cases sans bloc ni bombe ni feu
	List<AiTile> result = new ArrayList<AiTile>();
	Iterator<AiTile> it = neighbors.iterator();
	while(it.hasNext())
	{	checkInterruption(); //APPEL OBLIGATOIRE

	AiTile t = it.next();
	if(!isObstacle(t))
		if(distance(tile,t)<=1)
			result.add(t);
	}
	return result;
	}


	/**
	 * 
	 * @param bomb
	 * @return
	 * @throws StopRequestException
	 */
	public Vector<AiBomb> bombsInRange(AiBomb bomb) throws StopRequestException{
		Vector<AiBomb> result = new Vector<AiBomb>();
		for(int i = 1;i<=bomb.getRange();i++){
			checkInterruption();

			if (zone.getOwnHero().getCol() + i < zone.getWidth() - 3) {
				AiTile t = zone.getTile(bomb.getLine(), bomb.getCol() + i);
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				while (bombIt.hasNext()) {
					checkInterruption();
					result.add(bombIt.next());
				}
			}


		}
		for(int i = 0;i<bomb.getRange();i++){
			checkInterruption();
			if (zone.getOwnHero().getCol() - i > 2) {
				AiTile t = zone.getTile(bomb.getLine(), bomb.getCol() - i);
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				while (bombIt.hasNext()) {
					checkInterruption();
					result.add(bombIt.next());
				}
			}
		}
		for(int i = 0;i<bomb.getRange();i++){
			checkInterruption();
			if (zone.getOwnHero().getLine() + i < zone.getHeight() - 3) {
				AiTile t = zone.getTile(bomb.getLine() + i, bomb.getCol());
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				while (bombIt.hasNext()) {
					checkInterruption();
					result.add(bombIt.next());
				}
			}
		}
		for(int i = 0;i<bomb.getRange();i++){
			checkInterruption();
			if (zone.getOwnHero().getLine() - i > 2) {
				AiTile t = zone.getTile(bomb.getLine() - i, bomb.getCol());
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				while (bombIt.hasNext()) {
					checkInterruption();
					result.add(bombIt.next());
				}
			}
		}

		if(result.size()==0) return null;
		else return result;
	}

	/**
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	public AiTile findCible() throws StopRequestException {
		AiTile cible=null;
		double min=100000;

		for (int i = 0; i < zone.getWidth(); i++){
			checkInterruption();
			for (int j = 0; j < zone.getHeight(); j++){
				checkInterruption();
				if(FieldMatrix[i][j]<min){
					min=FieldMatrix[i][j];
					cible=zone.getTile(i,j);
				}
			} //j
		} //i


		return cible;

	}

	/**
	 * 
	 * @param mainbomb
	 * @param bombinrange
	 * @return
	 */
	public double shorterFuseTime(AiBomb mainbomb, AiBomb bombinrange){
		double timemain = (double)mainbomb.getNormalDuration();
		double timerange = (double)bombinrange.getNormalDuration();
		return Math.min(timemain, timerange);

	}

	/**
	 * 
	 * @throws StopRequestException
	 */
	public void getbombs() throws StopRequestException{
		checkInterruption();
		Iterator<AiBomb> iterIB = zone.getBombs().iterator();

		while(iterIB.hasNext()){
			checkInterruption();


			AiBomb temp = iterIB.next();
			double time = Double.MAX_VALUE;

			int bx = temp.getLine();
			int by = temp.getCol();
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(bx-k>0)
					FieldMatrix[bx-k][by] = time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(bx+k<zone.getHeight()-3)
					FieldMatrix[bx+k][by] = time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(by-k>0)
					FieldMatrix[bx][by-k] = time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(by+k<zone.getWidth()-3)
					FieldMatrix[bx][by+k] = time;
			}	

		}
	}


	/**
	 * 
	 * @param iB
	 * @throws StopRequestException
	 */
	public void sortbombs(Collection<AiBomb> iB) throws StopRequestException{
		checkInterruption();
		HashMap<AiBomb, Double> bombMap = new HashMap<AiBomb, Double>(0,9);

		Iterator<AiBomb> iterIB = iB.iterator();
		Vector<AiBomb> iBomb = new Vector<AiBomb>();
		while(iterIB.hasNext()){
			iBomb.add(iterIB.next());
		}

		while(iBomb.size()>0){
			checkInterruption();

			Vector<AiBomb> tempVect = new Vector<AiBomb>();
			Vector<AiBomb> tempVectaDev = new Vector<AiBomb>();

			AiBomb bomb = iBomb.iterator().next();
			iBomb.remove(bomb);

			Iterator<AiBomb>  vectB = bombsInRange(bomb).iterator();
			if(vectB == null){
				bombMap.put(bomb, (double)bomb.getNormalDuration());
			}
			else{		
				double min = bomb.getNormalDuration();	
				tempVect.add(bomb);
				while(vectB.hasNext()){
					tempVectaDev.add(vectB.next());	

				}

				while(tempVectaDev.size() > 0){

					AiBomb temp = tempVectaDev.elementAt(0);
					tempVect.add(temp);
					tempVectaDev.removeElementAt(0);
					Iterator<AiBomb>  vect = bombsInRange(bomb).iterator();
					if(vect != null){
						while(vect.hasNext()){
							AiBomb b = vect.next();
							if(!tempVectaDev.contains(b) && !tempVect.contains(b))
								tempVectaDev.add(b);

						}//W
					}//IF

				}//W

				Iterator<AiBomb> b = tempVect.iterator();
				while(b.hasNext()){

					min = shorterFuseTime(bomb, b.next());

				}
				b = tempVect.iterator();
				while(b.hasNext()){
					AiBomb bmb = b.next();
					bombMap.put(bmb, min);
					iBomb.remove(bmb);

				}//W
			}//else

		}//W
		Object[] set = bombMap.keySet().toArray();
		for(int i = 0; i<set.length ;i++){
			AiBomb temp = (AiBomb) set[i];
			double time = Math.pow(10,(2500-bombMap.get(temp))/400);

			int bx = temp.getLine();
			int by = temp.getCol();
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(bx-k>0)
					FieldMatrix[bx-k][by] += time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(bx+k<zone.getHeight()-3)
					FieldMatrix[bx+k][by] += time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(by-k>0)
					FieldMatrix[bx][by-k] += time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(by+k<zone.getWidth()-3)
					FieldMatrix[bx][by+k] += time;
			}		

		}


	}

	/**
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	public Boolean isTimeToRun2() throws StopRequestException{
		Boolean result = false;
		AiTile bomb = zone.getOwnHero().getTile();
		for(int i = 0;i<=4;i++){
			checkInterruption();

			if (zone.getOwnHero().getCol() + i < zone.getWidth() - 3) {
				AiTile t = zone.getTile(bomb.getLine(), bomb.getCol() + i);
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()) result = true;
			}


		}
		for(int i = 0;i<=4;i++){
			checkInterruption();
			if (zone.getOwnHero().getCol() - i > 2) {
				AiTile t = zone.getTile(bomb.getLine(), bomb.getCol() - i);
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()) result = true;
			}
		}
		for(int i = 0;i<=4;i++){
			checkInterruption();
			if (zone.getOwnHero().getLine() + i < zone.getHeight() - 3) {
				AiTile t = zone.getTile(bomb.getLine() + i, bomb.getCol());
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()) result = true;
			}
		}
		for(int i = 0;i<=4;i++){
			checkInterruption();
			if (zone.getOwnHero().getLine() - i > 2) {
				AiTile t = zone.getTile(bomb.getLine() - i, bomb.getCol());
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()) result = true;
			}
		}

		return result;
	}



}



