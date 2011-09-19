package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiItemType;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
*
* @author Nadin Kokciyan
* @author Hikmet Mazmanoglu
*
*/
public class KokciyanMazmanoglu extends ArtificialIntelligence
{
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	/** la case suivant à aller */
	private AiTile nextTile = null;
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;

	/** la case sur laquelle on veut aller */
	private AiTile targetTile=null;
	/** Ce qu'on va faire.*/
	private Mission mission; 	
	private Mission lastmission; 	

	private double FieldMatrix[][];
	private double ActionMatrix[][];


	private AiZone zone;
	private Tree tree;

	private int tilecounter=0;
	private int missioncounter=0; 



	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		zone = getPercepts();

		AiHero ownHero = zone.getOwnHero();
		this.currentTile =ownHero.getTile();

		FieldMatrix = new double[zone.getHeight()][zone.getWidth()];
		ActionMatrix = new double[zone.getHeight()][zone.getWidth()];
		AiAction result = new AiAction(AiActionName.NONE);


		//-----------------------------------------------------------------------------------------------


		if (ownHero != null) {

			if(currentTile == previousTile)
				tilecounter ++;
			if(mission == lastmission)
				missioncounter ++;
			
			if(missioncounter == 75 && tilecounter == 7 ){
				mission = Mission.ATTACK_RIVAL;
				missioncounter = 0;
				tilecounter  = 0;
			}
			
			if(mission == null)
				mission = Mission.STAND_IDLY;
			getActionMatrixValues();
			updateAMWithTraps();
			getMatrixValues();


			//printmatrix(ActionMatrix);
			//System.out.println(mission);

			double newmaxval = MaxValor();
			//if(targetTile != null)
				//System.out.println(newmaxval + " " +  ActionMatrix[targetTile.getLine()][targetTile.getCol()]);


			if (isTimeToRun2()) {

				if(mission == Mission.RUN_RUN_RUN_FAR_AWAY && targetTile != null && isSafe2(targetTile)){

					//System.out.println(targetTile.getLine() +" " + targetTile.getCol());
					tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
					AiAction turn = algoAEtoile(currentTile, targetTile);
					result = turn;


				}
				else{
					this.lastmission = mission;
					this.mission = Mission.RUN_RUN_RUN_FAR_AWAY;
					getActionMatrixValuesV2();

					targetTile = findSafeTile();
					if(targetTile !=null)
					{
						//System.out.println(targetTile.getLine() +" " + targetTile.getCol());
						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
					}
					else
						targetTile = currentTile;
					AiAction turn = algoAEtoile(currentTile, targetTile);

					result = turn;
				}
			}
			else{
				if(targetTile == null || newmaxval > ActionMatrix[targetTile.getLine()][targetTile.getCol()] || mission == Mission.RUN_RUN_RUN_FAR_AWAY )
				{
					targetTile = chooseTile();
					decodeAction(targetTile);
				}
				//System.out.println(targetTile.getLine() +" " + targetTile.getCol());
				//----------------------------------------------------------------------------------------------
				int count = countPos();
				if(count != 2 && !canIMove()){
					result = new AiAction(AiActionName.NONE);
				}
				else if(mission == Mission.DESTROY_WALL){
					if(currentTile != targetTile){

						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
						AiAction turn = algoAEtoile(currentTile, targetTile);
						result = turn;
					}
					else{
						if(!isTrap3(currentTile)){
							
							result = new AiAction(AiActionName.DROP_BOMB);
							}
					}
				}
				else if(mission == Mission.GATHER_EXTRA_BOMB){
					if(currentTile != targetTile){
						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
						AiAction turn = algoAEtoile(currentTile, targetTile);
						result = turn;
					}

				}
				else if(mission == Mission.GATHER_RANGE_EXTENDER){
					if(currentTile != targetTile){
						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
						AiAction turn = algoAEtoile(currentTile, targetTile);
						result = turn;
					}
				}
				else if(mission == Mission.DESTROY_SURPLUS_BOMB){
					if(!isBlockBetween(currentTile, targetTile) && distance(currentTile,targetTile)<=ownHero.getBombRange()){
						if(!isTrap3(currentTile))
							result = new AiAction(AiActionName.DROP_BOMB);
					}
					else{
						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
						AiAction turn = algoAEtoile(currentTile, targetTile);
						result = turn;
					}

				}
				else if(mission == Mission.DESTROY_SURPLUS_RANGE_EXTENDER){
					if(!isBlockBetween(currentTile, targetTile) && distance(currentTile,targetTile)<=ownHero.getBombRange()){
						if(!isTrap3(currentTile))
							result = new AiAction(AiActionName.DROP_BOMB);
					}
					else{
						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
						AiAction turn = algoAEtoile(currentTile, targetTile);
						result = turn;
					}

				}
				else if(mission == Mission.STAND_IDLY){
					result = new AiAction(AiActionName.NONE);
				}
				else if(mission == Mission.ATTACK_RIVAL){
					//!isBlockBetween(currentTile, targetTile) && 
					if(distance(currentTile,targetTile)<=ownHero.getBombRange()){
						if(!isTrap3(currentTile))
							result = new AiAction(AiActionName.DROP_BOMB);
					}
					else{
						tree = new Tree(targetTile.getLine(),targetTile.getCol(), this);
						AiAction turn = algoAEtoile(currentTile, targetTile);
						result = turn;
					}

				}
				else{
					result = new AiAction(AiActionName.NONE);
				}

			}			

		}
		if(result==null)
			result = new AiAction(AiActionName.NONE);

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
		Node startNode = tree.convertToNode(startTile);

		Node endNode = tree.convertToNode(targetTile);


		boolean control = false; //test si le noeud est final.

		NodeComparator nc = new NodeComparator(startNode, endNode,this);
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
	public void getMatrixValues() throws StopRequestException{
		checkInterruption();


		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();	
			for(int j = 0; j<zone.getWidth(); j++){
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

	public void getActionMatrixValuesV2() throws StopRequestException{
		checkInterruption();

		LinkedList<AiTile> list = new LinkedList<AiTile>();
		list.offer(currentTile);
		LinkedList<AiTile> checked = new LinkedList<AiTile>();

		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();

				ActionMatrix[i][j] = -1;
			}
		}

		while(list.size()>0){
			checkInterruption();
			AiTile temp = list.poll();
			checked.offer(temp);

			if (temp.getBlock()==null) {
				Iterator<AiTile> neigh = zone.getNeighborTiles(temp).iterator();
				while (neigh.hasNext()) {
					checkInterruption();
					AiTile temp2 = neigh.next();
					if (!checked.contains(temp2))
						list.offer(temp2);
				}
			}
			ActionMatrix[temp.getLine()][temp.getCol()] = evaluateTile(temp);
		}

		getActionMatrixBombRanges();
	}

	public void getActionMatrixValues() throws StopRequestException{
		checkInterruption();

		LinkedList<AiTile> list = new LinkedList<AiTile>();
		list.offer(currentTile);
		LinkedList<AiTile> checked = new LinkedList<AiTile>();

		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();

				ActionMatrix[i][j] = -1;
			}
		}





		while(list.size()>0){
			checkInterruption();
			AiTile temp = list.poll();
			checked.offer(temp);

			if (temp.getBlock()==null) {
				Iterator<AiTile> neigh = zone.getNeighborTiles(temp).iterator();
				while (neigh.hasNext()) {
					checkInterruption();
					AiTile temp2 = neigh.next();
					if (!checked.contains(temp2))
						list.offer(temp2);
				}
			}
			ActionMatrix[temp.getLine()][temp.getCol()] = evaluateTile(temp);
		}

		getActionMatrixBombRanges();
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
		int power = bombe.getRange();//s'il existe une bombe en ces coordonnées on parle d'un d�sir du d�fense (le personnage veut s'enfuir par une bombe)
		result = false;
		if (ai.getCol() - bombTile.getCol() > 0) {//si la bombe est à gauche
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers le bas c'est une piège
			if (!isMovePossible(ai, Direction.RIGHT)
					&& !isMovePossible(ai, Direction.UP)
					&& !isMovePossible(ai, Direction.DOWN)) {
				result = true;
			} else {
				while (i < power)//on va �tudier une distance i partant 1 jusqu'� la port�e de la bombe
				{	checkInterruption();
					if (bombTile.getCol() + i < zone.getWidth())//si le personnage+la port�e de la bombe est dans la zone du jeu
					{
						if (possibleMoveD(ai.getLine(), ai.getCol(), i, 1))//s'il est possible d'aller vers la droite pour la distance i
						{
							//s'il est impossible de trouver une place à se cacher en se d�placant pour une distance i
							if (!isMovePossible(ai.getLine() + i, ai.getCol(),
									Direction.UP)
									&& !isMovePossible(ai.getLine() + i, ai
											.getCol(), Direction.DOWN)) {
								result = true;
							} else//il est possible de se cacher
							{
								result = false;
								break;//pas besoin de continuer à �tudier
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
					checkInterruption();
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
					checkInterruption();
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
					checkInterruption();
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
		//si la bombe est à gauche et le personnage peut aller vers la droite pour une distance plus grande que la port�e de la bombe alors ce n'est pas une piège
		if (ai.getCol() - bombTile.getCol() > 0
				&& possibleMoveD(bombTile.getLine(), ai.getCol(), i, 1))
			result = false;
		//si la bombe est à droite et le personnage peut aller vers la gauche pour une distance plus grande que la port�e de la bombe alors ce n'est pas une piège
		else if (ai.getCol() - bombTile.getCol() < 0
				&& possibleMoveD(bombTile.getLine(), ai.getCol(), i, -1))
			result = false;
		//si la bombe est en bas et le personnage peut aller vers le haut pour une distance plus grande que la port�e de la bombe alors ce n'est pas une piège
		if (ai.getLine() - bombTile.getLine() < 0
				&& possibleMoveD(ai.getLine(), bombTile.getCol(), i, 2))
			result = false;
		//si la bombe est en haut et le personnage peut aller vers le bas pour une distance plus grande que la port�e de la bombe alors ce n'est pas une piège
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
	public  boolean possibleMoveD(int x,int y,int d, int s) throws StopRequestException{ 
	checkInterruption();
	boolean result=false;
	switch(s){

	case 1: // x right
		result=true;
		while(d>0){
			checkInterruption();
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
			checkInterruption();
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
			checkInterruption();
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
			checkInterruption();
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
	{ checkInterruption();
	AiHero hero = zone.getHeroes().iterator().next();
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
			checkInterruption();
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
				checkInterruption();
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

	public  boolean isSafe2(AiTile tile) throws StopRequestException{
		checkInterruption();
		boolean result=true;
		Iterator<AiBomb> a = zone.getBombs().iterator();
		if(a.hasNext()){
			AiBomb bomb = a.next();
			if((bomb.getLine()==tile.getLine() || bomb.getCol()==tile.getCol()) && ((bomb.getColor()!= zone.getOwnHero().getColor() && bomb.getRange()> distance(bomb.getTile(),tile)) || ((bomb.getColor() == zone.getOwnHero().getColor() && zone.getOwnHero().getBombRange() > distance(bomb.getTile(),tile))))){
				result=result && isBlockBetween(bomb.getTile(),tile);
			}
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
					checkInterruption();
					if(isVoisinMur(zone.getTile(t2.getLine(), t2.getCol()+i),Direction.RIGHT)){
						result=true;
						break;
					}
				}

			}
			if(t2.getCol() > t1.getCol()){
				int count=t2.getCol()-t1.getCol();
				for(int i=1;i<count;i++){
					checkInterruption();
					if(isVoisinMur(zone.getTile(t2.getLine(), t2.getCol()-i), Direction.LEFT)){
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
					checkInterruption();
					if(isVoisinMur(zone.getTile(t2.getLine()+i,t2.getCol()),Direction.DOWN)){
						result=true;
						break;
					}
				}
			}
			if(t2.getLine()>t1.getLine()){
				int count=t2.getLine()-t1.getLine();
				for(int i=1;i<count;i++){
					checkInterruption();
					if(isVoisinMur(zone.getTile(t2.getLine()-i, t2.getCol()),Direction.UP)){
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
			checkInterruption();
			switch(d){
			case UP:
				xTemp=tile.getLine();
				yTemp=tile.getCol()-1;
				tileTemp=zone.getTile(xTemp,yTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			case DOWN:
				xTemp=tile.getLine();
				yTemp=tile.getCol()+1;
				tileTemp=zone.getTile(xTemp,yTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			case RIGHT:
				xTemp=tile.getLine()+1;
				yTemp=tile.getCol();
				tileTemp=zone.getTile(xTemp,yTemp);
				if(isWall(tileTemp))
					res=true;
				break;
			case LEFT:
				xTemp=tile.getLine()-1;
				yTemp=tile.getCol();
				tileTemp=zone.getTile(xTemp,yTemp);
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
	public AiTile FindSafeCible2(AiTile bombe, AiTile me) throws StopRequestException {
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
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, -2)) { // 2 kadar
					// asagi
					liste
					.add(zone.getTile(bombe.getLine()+2, bombe
							.getCol()));

				}
			}
			if (i == 1 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, -1)) {// 2 kadar
					// sol
					liste
					.add(zone.getTile(bombe.getLine(),
							bombe.getCol() - 2));

				}
			}
			if (i == 2 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, 1)) { // 2
					// kadar
					// saga
					liste
					.add(zone.getTile(bombe.getLine(), bombe.getCol()+2));

				}
			}
			if (i == 3 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
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
				checkInterruption();
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
	 * @throws StopRequestException 
	 */
	public  double[][] getFieldMatrix() throws StopRequestException {
		checkInterruption();
		return FieldMatrix;
	}
	/**
	 * 
	 * @return
	 * @throws StopRequestException 
	 */
	public  AiTile getCurrentTile() throws StopRequestException {
		checkInterruption();
		return currentTile;
	}
	/**
	 * 
	 * @return
	 * @throws StopRequestException 
	 */
	public  AiTile getPreviousTile() throws StopRequestException {
		checkInterruption();
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

	// liste des cases autour de la case de r�f�rence
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
		checkInterruption();
		Vector<AiBomb> result = new Vector<AiBomb>();
		for(int i = 1;i<=bomb.getRange();i++){
			checkInterruption();

			if (zone.getOwnHero().getCol() + i < zone.getWidth()) {
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
			if (zone.getOwnHero().getCol() - i > 0) {
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
			if (zone.getOwnHero().getLine() + i < zone.getHeight()) {
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
			if (zone.getOwnHero().getLine() - i > 0) {
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
		checkInterruption();
		AiTile cible=null;
		double min=100000;

		for (int i = 0; i < zone.getHeight(); i++){
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++){
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
	 * @throws StopRequestException 
	 */
	public double shorterFuseTime(AiBomb mainbomb, AiBomb bombinrange) throws StopRequestException{
		checkInterruption();
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
			double time = 150;

			int bx = temp.getLine();
			int by = temp.getCol();
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(bx-k>0)
					FieldMatrix[bx-k][by] = time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(bx+k<zone.getHeight())
					FieldMatrix[bx+k][by] = time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(by-k>0)
					FieldMatrix[bx][by-k] = time;
			}
			for(int k=1;k<temp.getRange();k++){
				checkInterruption();
				if(by+k<zone.getWidth())
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
			checkInterruption();
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
					checkInterruption();
					tempVectaDev.add(vectB.next());	

				}

				while(tempVectaDev.size() > 0){
					checkInterruption();
					AiBomb temp = tempVectaDev.elementAt(0);
					tempVect.add(temp);
					tempVectaDev.removeElementAt(0);
					Iterator<AiBomb>  vect = bombsInRange(bomb).iterator();
					if(vect != null){
						while(vect.hasNext()){
							checkInterruption();
							AiBomb b = vect.next();
							if(!tempVectaDev.contains(b) && !tempVect.contains(b))
								tempVectaDev.add(b);

						}//W
					}//IF

				}//W

				Iterator<AiBomb> b = tempVect.iterator();
				while(b.hasNext()){
					checkInterruption();
					min = shorterFuseTime(bomb, b.next());

				}
				b = tempVect.iterator();
				while(b.hasNext()){
					checkInterruption();
					AiBomb bmb = b.next();
					bombMap.put(bmb, min);
					iBomb.remove(bmb);

				}//W
			}//else

		}//W
		Object[] set = bombMap.keySet().toArray();
		for(int i = 0; i<set.length ;i++){
			checkInterruption();
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
		checkInterruption();
		Boolean result = false;
		AiTile hero = zone.getOwnHero().getTile();
		for(int i = 0;i<=4;i++){
			checkInterruption();

			if (hero.getCol() + i < zone.getWidth()) {
				AiTile t = zone.getTile(hero.getLine(), hero.getCol() + i);
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()){ 
					while(bombIt.hasNext()){
						checkInterruption();
						AiBomb br= bombIt.next();
						if(br.getRange() >= distance(br.getTile(),currentTile)){
							result = true;
							break;
						}
					}
				}
			}
		}
		for(int i = 0;i<=4;i++){
			checkInterruption();
			if (hero.getCol() - i > 0) {
				AiTile t = zone.getTile(hero.getLine(), hero.getCol() - i);
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()){ 
					while(bombIt.hasNext()){
						checkInterruption();
						AiBomb br= bombIt.next();
						if(br.getRange() >= distance(br.getTile(),currentTile)){
							result = true;
							break;
						}
					}
				}
			}
		}
		for(int i = 0;i<=4;i++){
			checkInterruption();
			if (hero.getLine() + i < zone.getHeight()) {
				AiTile t = zone.getTile(hero.getLine() + i, hero.getCol());
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()){ 
					while(bombIt.hasNext()){
						checkInterruption();
						AiBomb br= bombIt.next();
						if(br.getRange() >= distance(br.getTile(),currentTile)){
							result = true;
							break;
						}
					}
				}
			}
		}
		for(int i = 0;i<=4;i++){
			checkInterruption();
			if (hero.getLine() - i > 0) {
				AiTile t = zone.getTile(hero.getLine() - i, hero.getCol());
				Iterator<AiBomb> bombIt = t.getBombs().iterator();
				if(bombIt.hasNext()){ 
					while(bombIt.hasNext()){
						checkInterruption();
						AiBomb br= bombIt.next();
						if(br.getRange() >= distance(br.getTile(),currentTile)){
							result = true;
							break;
						}
					}
				}
			}
		}

		return result;
	}
	public AiTile chooseTile() throws StopRequestException{
		checkInterruption(); // APPEL OBLIGATOIRE
		double res = MaxValor();
		@SuppressWarnings("unused")
		int count = countPos();
		//if(count==3) res=res/2;
		AiTile targetTile = currentTile;
		if(res>0)
		{
			List<AiTile> list = new ArrayList<AiTile>();
			for (int i = 0; i < zone.getHeight(); i++){
				checkInterruption();
				for (int j = 0; j < zone.getWidth(); j++){
					checkInterruption();
					if(ActionMatrix[i][j]==res){
						list.add(zone.getTile(i, j));

					}
				} //j
			} //i	


			if(list.size() > 0){
				int random = (int) (Math.random() * list.size());
				targetTile = list.get(random);
			}
		}


		return targetTile;
	}

	public void decodeAction(AiTile tile) throws StopRequestException{
		checkInterruption(); // APPEL OBLIGATOIRE

		Iterator<AiHero> heroes = tile.getHeroes().iterator();
		Collection<AiHero> heroes2 = new LinkedList<AiHero>();

		while(heroes.hasNext()){
			checkInterruption();
			AiHero t = heroes.next();
			if(!t.equals(zone.getOwnHero()))
				heroes2.add(t);
		}

		this.lastmission = mission;
		if(heroes2.size() > 0){
			this.mission = Mission.ATTACK_RIVAL;}
		else{
			if(tile.getItem() != null){
				AiItemType item = tile.getItem().getType();
				if(item == AiItemType.EXTRA_BOMB && zone.getOwnHero().getBombNumber() < 5)
					this.mission = Mission.GATHER_EXTRA_BOMB;	
				else if(item == AiItemType.EXTRA_BOMB && zone.getOwnHero().getBombNumber() == 5)
					this.mission = Mission.DESTROY_SURPLUS_BOMB;
				else if(item == AiItemType.EXTRA_FLAME && zone.getOwnHero().getBombRange() < 5)
					this.mission = Mission.GATHER_RANGE_EXTENDER;
				else if(item == AiItemType.EXTRA_FLAME && zone.getOwnHero().getBombRange() == 5)
					this.mission = Mission.DESTROY_SURPLUS_RANGE_EXTENDER;
			}
			else
				this.mission = Mission.DESTROY_WALL;
		}


	}

	public double evaluateTile(AiTile tile) throws StopRequestException{
		checkInterruption(); // APPEL OBLIGATOIRE
		double resultat = 0;
		int wall_counter = 0;
		if(isObstacle(tile)){
			if(isBomb(tile))
				resultat=-10;
			else if(isFire(tile))
				resultat = -5;
			else if (isWall(tile)){
				if(tile.getBlock().isDestructible())
					resultat = -2;
				else
					resultat = -3;}
			else
				resultat = -1;
		}
		//else if(tile.getItem()==null && isTrap3(tile))
		//resultat = -8;
		else
		{
			Iterator<AiHero> heroes = tile.getHeroes().iterator();
			Collection<AiHero> heroes2 = new LinkedList<AiHero>();

			while(heroes.hasNext()){
				checkInterruption();
				AiHero t = heroes.next();
				if(!t.equals(zone.getOwnHero()))
					heroes2.add(t);
			}
			resultat += 41*heroes2.size(); 
			//-------------------------------------------


			int range = zone.getOwnHero().getBombRange();

			boolean u = true;
			boolean r = true;
			boolean d = true;
			boolean l = true;

			for(int i=0;i<range;i++){
				checkInterruption();
				if (tile.getCol() + i < zone.getWidth() && r) {
					AiTile t = zone.getTile(tile.getLine(), tile.getCol() + i);
					if(isObstacle(t)){
						if(isSoft(t)){
							wall_counter++;
							r= false;
						}
						else{
							r=false;
						}
					}
				}
				if (tile.getCol() - i > 0 && l) {
					AiTile t = zone.getTile(tile.getLine(), tile.getCol() - i);
					if(isObstacle(t)){
						if(isSoft(t)){
							wall_counter++;
							l= false;
						}
						else{
							l=false;
						}
					}
				}
				if (tile.getLine() + i < zone.getHeight() && d) {
					AiTile t = zone.getTile(tile.getLine() + i, tile.getCol());
					if(isObstacle(t)){
						if(isSoft(t)){
							wall_counter++;
							d= false;
						}
						else{
							d=false;
						}
					}
				}
				if (tile.getLine() - i > 0 && u) {
					AiTile t = zone.getTile(tile.getLine() - i, tile.getCol());
					if(isObstacle(t)){
						if(isSoft(t)){
							wall_counter++;
							u= false;
						}
						else{
							u=false;
						}
					}
				}


			}

			//--------------------------------------------			


			resultat += 15*wall_counter;
			AiItem bonus = tile.getItem();
			if(bonus!=null)
				resultat += 40;

			//resultat = resultat/((int)(distance(currentTile,tile)/4)+1);

		}
		return resultat;

	}


	public double MaxValor() throws StopRequestException{
		checkInterruption();
		double res=0;

		for (int i = 0; i < zone.getHeight(); i++){
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++){
				checkInterruption();
				if(ActionMatrix[i][j]>res){
					res=ActionMatrix[i][j];
				}
			} //j
		} //i


		return res;		
	}


	public double MinValor(double matrix[][]) throws StopRequestException{
		checkInterruption();
		double res=Double.MAX_VALUE;

		for (int i = 0; i < zone.getHeight(); i++){
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++){
				checkInterruption();
				if(matrix[i][j]<res){
					res=matrix[i][j];
				}
			} //j
		} //i


		return res;		
	}

	public double MinPosValor(double matrix[][]) throws StopRequestException{
		checkInterruption();
		double res=Double.MAX_VALUE;

		for (int i = 0; i < zone.getHeight(); i++){
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++){
				checkInterruption();
				if(matrix[i][j]<res && matrix[i][j]>0){
					res=matrix[i][j];
				}
			} //j
		} //i


		return res;		
	}

	/**
	 * 
	 * @throws StopRequestException
	 */
	public void getActionMatrixBombRanges() throws StopRequestException{
		checkInterruption();
		Iterator<AiBomb> iterIB = zone.getBombs().iterator();

		while(iterIB.hasNext()){
			checkInterruption();


			AiBomb temp = iterIB.next();
			double time = -33.0;

			int bx = temp.getLine();
			int by = temp.getCol();
			for(int k=0;k<=temp.getRange();k++){
				checkInterruption();
				if(bx-k>0)
					ActionMatrix[bx-k][by] = time;
			}
			for(int k=0;k<=temp.getRange();k++){
				checkInterruption();
				if(bx+k<zone.getHeight())
					ActionMatrix[bx+k][by] = time;
			}
			for(int k=0;k<=temp.getRange();k++){
				checkInterruption();
				if(by-k>0)
					ActionMatrix[bx][by-k] = time;
			}
			for(int k=0;k<=temp.getRange();k++){
				checkInterruption();
				if(by+k<zone.getWidth())
					ActionMatrix[bx][by+k] = time;
			}	

		}
	}





	public boolean FindSafeCible(AiTile bombe, AiTile me) throws StopRequestException {
		checkInterruption();
		Collection<AiTile> cases = getPercepts().getNeighborTiles(bombe);
		Iterator<AiTile> iterCase = cases.iterator();
		Vector<AiTile> liste = new Vector<AiTile>();
		@SuppressWarnings("unused")
		AiTile cible = null;
		boolean res=false;
		//int dist = 1000;
		int i = 0; // 0: yukari 1: sol 2: asagi 3: sag
		//int temp = 0;
		while (iterCase.hasNext()) {
			checkInterruption();
			AiTile t = iterCase.next();
			if (i == 0 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, -2)) { // 2 kadar
					// asagi
					liste.add(zone.getTile(bombe.getLine()+2, bombe.getCol()));

				}
			}
			if (i == 1 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, -1)) {// 2 kadar
					// sol
					liste.add(zone.getTile(bombe.getLine(),bombe.getCol() - 2));

				}
			}
			if (i == 2 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, 1)) { // 2
					// kadar
					// saga
					liste.add(zone.getTile(bombe.getLine(), bombe.getCol()+2));

				}
			}
			if (i == 3 && isWall(t)) {
				//System.out.println(i+" "+getPercepts().getDirection(bombe, t));
				if (possibleMoveD(me.getLine(), me.getCol(), 2, 2)) { // 2 kadar
					// yukari
					liste.add(zone.getTile(bombe.getLine()-2,bombe.getCol()));

				}
			}
			i++;
		}

		if (liste.size() != 0) {
			/*
		Iterator<AiTile> iterListe = liste.iterator();
		int mindistance = 1000;
		while (iterListe.hasNext()) {
		checkInterruption();
		AiTile t2 = iterListe.next();
		if (distance(t2, me) < mindistance) {
		mindistance = distance(t2, me);
		cible = t2;
		}*/
			synchronized(liste){
				Iterator<AiTile> itList= liste.iterator();
				while(itList.hasNext()){
					checkInterruption();
					AiTile t= itList.next();
					if(ActionMatrix[t.getLine()][t.getCol()]<0)
						liste.remove(t);
					//else
						//System.out.println("safe tile1: "+t.toString());
				}
				if(liste.size()!=0)
					res=true;
				else
					res=false;
			}
		} //else {
		List<AiTile> possible = new ArrayList<AiTile>();
		if(0<me.getLine()-2 && 0<me.getCol()-1)
			possible.add(zone.getTile(me.getLine()-2,me.getCol()-1));// sol ust capraz
		if(0<me.getLine()-2 && me.getCol()+1<zone.getWidth())
			possible.add(zone.getTile(me.getLine()-2,me.getCol()+1));// sag ust capraz
		if(me.getLine()+2<zone.getHeight() && 0<me.getCol()-1)
			possible.add(zone.getTile(me.getLine()+2,me.getCol()-1));// sol alt capraz
		if(me.getLine()+2<zone.getHeight() && me.getCol()+1<zone.getWidth())
			possible.add(zone.getTile(me.getLine()+2,me.getCol()+1));// sag alt capraz

		List<AiTile> result= new ArrayList<AiTile>();

		for(int k=0;k<possible.size();k++){
			checkInterruption();
			if(!isObstacle(possible.get(k))){
				result.add(possible.get(k));
			}
		}
		/*if(result.size()>0)
		cible= result.get(0);
		else result = null;*/

		if(result.size()>0){
			synchronized(result){
				Iterator<AiTile> itList= result.iterator();
				while(itList.hasNext()){
					checkInterruption();
					AiTile t= itList.next();
					if(ActionMatrix[t.getLine()][t.getCol()]<0)
						result.remove(t);
					//else
						//System.out.println("safe tile2: "+t.toString());
				}

			}}
		else
			res=false;

		if(result.size()>0){
			res=true;
		}
		//} else kaldirilan
		return res;
	}

	public AiTile findSafeTile() throws StopRequestException{
		checkInterruption();
		AiTile res = null;
		double matrix[][] =  new double[zone.getHeight()][zone.getWidth()];
		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();
				matrix[i][j]=FieldMatrix[i][j]/ActionMatrix[i][j];

			}
		}

		double count = MinPosValor(matrix);

		List<AiTile> list = new ArrayList<AiTile>();
		for (int i = 0; i < zone.getHeight(); i++){
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++){
				checkInterruption();
				if(matrix[i][j]==count){
					list.add(zone.getTile(i, j));
				}
			} //j
		} //i	

		if(list.size() > 0){
			int random = (int) (Math.random() * list.size());
			res = list.get(random);
		}

		return res;
	}

	public AiTile findSafeTileV2(double matrix[][]) throws StopRequestException{
		checkInterruption();
		AiTile res = null;


		double count = MinPosValor(matrix);
		if(count != Double.MAX_VALUE){
			List<AiTile> list = new ArrayList<AiTile>();
			for (int i = 0; i < zone.getHeight(); i++){
				checkInterruption();
				for (int j = 0; j < zone.getWidth(); j++){
					checkInterruption();
					if(matrix[i][j]==count){
						list.add(zone.getTile(i, j));
					}
				} //j
			} //i	

			if(list.size() > 0){
				int random = (int) (Math.random() * list.size());
				res = list.get(random);
			}
		}
		return res;
	}

	public boolean isTrap2(AiTile BombTile) throws StopRequestException{
		checkInterruption();
		boolean result = false;
		int range = zone.getOwnHero().getBombRange();

		double matrix[][] =  new double[zone.getHeight()][zone.getWidth()];
		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();
				if(BombTile.getLine()-range<=i && i<=BombTile.getLine()+range && BombTile.getCol() == j)
					matrix[i][j] = -1;
				else if(BombTile.getCol()-range<=j && j<=BombTile.getCol() + range && BombTile.getLine() == i)
					matrix[i][j] = -1;
				else{
					if(isObstacle(BombTile)){
						matrix[i][j]=-1;
					}
					else 
						matrix[i][j]=FieldMatrix[i][j];
				}
			}
		}

		double z = MinPosValor(matrix);
		if(z == Double.MAX_VALUE)
			result = true;

		return result;
	}


	public boolean isTrap3(AiTile BombTile) throws StopRequestException{
		checkInterruption();
		boolean result = false;
		int range = zone.getOwnHero().getBombRange();
		double matrix[][] = new double[zone.getHeight()][zone.getWidth()];
		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();
				if(BombTile.getLine()-range<=i && i<=BombTile.getLine()+range && BombTile.getCol() == j)
					matrix[i][j] = -10;
				else if(BombTile.getCol()-range<=j && j<=BombTile.getCol() + range && BombTile.getLine() == i)
					matrix[i][j] = -10;
				else
					matrix[i][j] = ActionMatrix[i][j];
			}
		}
		AiTile t=findSafeTileV2(matrix);
		if(t == null)
			result=true;

		return result;
	}

	public void updateAMWithTraps() throws StopRequestException{
		checkInterruption();
		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();
				if(zone.getTile(i, j).getItem()==null && !isObstacle(zone.getTile(i, j)) && isTrap3(zone.getTile(i, j))){
					ActionMatrix[i][j] = -9;
				}

			}
		}
	}

	public void printmatrix(double matrix[][]) throws StopRequestException{
		checkInterruption();
		for(int i = 0; i<zone.getHeight(); i++){
			checkInterruption();
			String s = "";
			for(int j = 0; j<zone.getWidth(); j++){
				checkInterruption();
				s += matrix[i][j] +  " ";
			}
			System.out.println(s);
		}

	}

	public boolean canIMove() throws StopRequestException{
		checkInterruption();
		boolean control = false;
		
		Collection<AiTile> coll = zone.getNeighborTiles(currentTile);
		Iterator<AiTile> iterColl = coll.iterator();
		
		while(iterColl.hasNext()){
			checkInterruption();
			AiTile t = iterColl.next();
			if(ActionMatrix[t.getLine()][t.getCol()]>=0)
				control = true;
		}
		
		return control;
		
	}

public int countPos() throws StopRequestException{
	checkInterruption();
	int count=0;
	for(int i = 0; i<zone.getHeight(); i++){
		checkInterruption();
		for(int j = 0; j<zone.getWidth(); j++){
			checkInterruption();
			if(ActionMatrix[i][j]>=0){
				count++;
			}
		}
	}
	
	return count;
}
	
	
	
}



