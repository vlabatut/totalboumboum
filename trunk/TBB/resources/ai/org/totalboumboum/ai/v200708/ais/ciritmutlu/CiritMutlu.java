package org.totalboumboum.ai.v200708.ais.ciritmutlu;

//import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions.AbsentNodeException;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions.ImpossibleActionException;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.Problem;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.State;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.tree.SearchNode;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.tree.SearchTree;
//import src.ai.old200708.ciritmutlu.tree.SearchLink;

/**
 * 
 * @author Semen Cirit
 * @author Mine Mutlu
 *
 */
@SuppressWarnings("deprecation")
public class CiritMutlu extends ArtificialIntelligence {
	/** */
	private SearchTree tree;	// arbre de recherche a construire
	/** */
	private State tempState;	// etat temporaire qui va etre utilise pour l'action suvante
	/** */
	private Problem problem;	// le probleme a traiter lors de la construction de l'arbre
	/** */
	private int targetAction;   // l'action a realiser a l'appel suivant de la methode "call()"
	/** */
	private Vector<Integer> lastAction;  /* 	la derniere action realisee a l'etape precedente; elle  
											est placee dans ce vecteur pour pouvoir baser l'action suivante
									   		sur celle-ci
									   */  
	/** */
	private Vector<State> states;   // vecteur d'etats qui sert a tenir compte des etats precedents du joueur IA
	/** */
	private Vector<Integer> lastPosition; // vecteur contenant des deux pernieres positions du joueur IA
	
	/**
	 * Constructeur
	 */
	public CiritMutlu() 
	{
		super("CiritMutlu");
		states=new Vector<State>();
		lastPosition=new Vector<Integer>();
		lastAction =new Vector<Integer>();
	}
	
	/** */
	public static final long serialVersionUID = 1L;
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	/**
	 *  La methode qui fait deplacer le joueur IA
	 */
	@Override
	public Integer processAction() throws Exception
	{		
		if(firstTime)
			firstTime = false;
		else
		{	

		// position du joueur IA
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		// initialisation du probleme
		problem = new Problem();
		if(!states.isEmpty())
			problem.addInitialState(states.lastElement());
		tree = new SearchTree(problem);
		return makeTree(problem, x, y);
		}
		return AI_ACTION_DO_NOTHING;
	}

	/**
	 *  Construit l'arbre par lequel le joueur va determiner les actions qu'il peut 
	 * realiser
	 * @param problem  probleme initial avant chaque action du joueur: 
	 * 				choix de direction, choix d'action,etc.
	 * @param x    coordonne du joueur IA
	 * @param y	   coordonne du joueur IA
	 * @return l'action qui va etre realisee au prochain appel de la methode 'call()' 
	 * @throws ImpossibleActionException
	 * @throws AbsentNodeException
	 */
	public int makeTree(Problem problem,int x , int y) throws ImpossibleActionException, AbsentNodeException{
		double point=0;
		double tempPoint=Integer.MIN_VALUE;
//		 Creation de la liste chainee et un iterateur
		LinkedList<SearchNode> frange = new LinkedList<SearchNode>();
//		Iterator<SearchLink> sl = null;
		SearchNode sn = tree.getRoot();
		frange.offer(sn);
		State currentState=sn.getState();
		for(int action=ArtificialIntelligence.AI_ACTION_GO_UP;action<=ArtificialIntelligence.AI_ACTION_PUT_BOMB;action++){
			if(isPossibleAction(x, y, action)){
				State targetState=apply(currentState, x, y, action);
//				sl=tree.developNode(targetState, sn);
				point=getPoint(action,targetState,x,y)+returnCost(action);
				
				if(point>tempPoint){
					if(!lastPosition.isEmpty()){
						if(lastPositionComparator(x, y, action)){
							targetAction=action;
							tempPoint=point;
							tempState=targetState;
						}
					}else{
						targetAction=action;
						tempPoint=point;
						tempState=targetState;
					}
				}
			}
		}
		lastPosition.add(0, getOwnPosition()[0]);
		lastPosition.add(1, getOwnPosition()[1]);
		lastAction.add(targetAction);
		states.add(tempState);
		return targetAction;
	} 
	
	/**
	 * 	Compare les deux dernieres actions realisees par le joueur IA. Si le joueur va 
	 * realiser la meme action qu'il a realise lors de son dernier deplacement, la methode
	 * retourne 'false'. 
	 * @param x  coordonne courant du joueur IA  
	 * @param y  coordonne courant du joueur IA
	 * @param targetAction  l'action qui va etre realisee par le joueur IA au prochain
	 * 					 deplacement
	 * @return
	 * 		? 
	*/
	public boolean lastPositionComparator(int x, int y,int targetAction){
		switch(targetAction)
			{	case ArtificialIntelligence.AI_ACTION_GO_UP:	
					// la prochaine action est d'aller vers le haut
					y=y-1;
					break;
				case ArtificialIntelligence.AI_ACTION_GO_DOWN:
					// la prochaine action est d'aller vers le bas
					y=y+1;
					break;
				case ArtificialIntelligence.AI_ACTION_GO_LEFT:
					// la prochaine action est d'aller vers gauche
					x=x-1;
					break;
				case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
					// la prochaine action est d'aller vers droit
					x=x+1;
					break;
			}	
		if(lastPosition.get(0).equals(x) && lastPosition.get(1).equals(y) ){
			return false;
		}else return true;
	}
	
	/**
	 * 	Verifie si les actions passees en parametre sont possibles ou non
	 * @param x coordonne de la position du joueur IA
	 * @param y coordonne de la position du joueur IA
	 * @param action l'action pour laquelle on verifie la possibilite
	 * @return
	 * 		? 
	 */
	public boolean isPossibleAction(int x, int y,int action){
		boolean done=false;
	    if(ArtificialIntelligence.AI_ACTION_GO_DOWN==action && isMovePossible(x,y,action)){
			done=true;
	    }
	    if(ArtificialIntelligence.AI_ACTION_GO_LEFT==action && isMovePossible(x,y,action)){
			done=true;
	    }
	    if(ArtificialIntelligence.AI_ACTION_GO_RIGHT==action  && isMovePossible(x,y,action)){
	    	done=true;
	    }
	    if(ArtificialIntelligence.AI_ACTION_GO_UP==action && isMovePossible(x,y,action)){
			done=true;
	    }
		
	    if(ArtificialIntelligence.AI_ACTION_PUT_BOMB==action && isThereWallSoft(x, y))
			done=true;
	    return done;
	}
	
	/**
	 * 	Calcule le cout d'une action que le joueur IA peut realiser. Il choisit ses actions
	 * parmi les actions possibles qu'il peut realiser selon les criteres externes en 
	 * controlant leur cout. Le joueur choisit l'action qui possede le plus grand couts\ 
	 * @param action   action a realiser par le joueur
	 * @return
	 * 		? 
	 */
	public int returnCost(int action){
		// Coordonnnes du joueur IA dans la zone de matrice
		int x=getOwnPosition()[0]; 
		int y=getOwnPosition()[1];
		
		// Cout de l'action
		int cost=0;
		
		if(action==ArtificialIntelligence.AI_ACTION_DO_NOTHING)
			cost+=1000;
		else if(action==ArtificialIntelligence.AI_ACTION_PUT_BOMB)
			cost+=3000;
		else if(distance(x, y, 0,0 )<8){ 
			if(action==ArtificialIntelligence.AI_ACTION_GO_RIGHT || action==ArtificialIntelligence.AI_ACTION_GO_DOWN)
				cost+=1000;
		}else if(distance(x,y,16,0)<8){
			if(action==ArtificialIntelligence.AI_ACTION_GO_LEFT || action==ArtificialIntelligence.AI_ACTION_GO_DOWN)
				cost+=1000;
		}else if(distance(x,y,0,14)<8){
			if(action==ArtificialIntelligence.AI_ACTION_GO_RIGHT || action==ArtificialIntelligence.AI_ACTION_GO_UP)
				cost+=1000;
		}else if(distance(x,y,16,14)<8){
			if(action==ArtificialIntelligence.AI_ACTION_GO_LEFT || action==ArtificialIntelligence.AI_ACTION_GO_UP)
				cost+=1000;
		}
		if(lastAction.size()>=2 && lastAction.lastElement()==ArtificialIntelligence.AI_ACTION_PUT_BOMB ){
			for(int i=1; i<=4;i++){
				if(lastAction.get(lastAction.size()-1)==ArtificialIntelligence.AI_ACTION_GO_UP){
					boolean move;
					if( i!=1){
						move=isMovePossible(x, y, i) && !isMovePossible(x, y, 1);
						if(move){
							//if(action!=1){
							if(action==2){
								cost+=2000;
							}else
								cost-=2000;
						}
					}
				}
				if(lastAction.get(lastAction.size()-1)==ArtificialIntelligence.AI_ACTION_GO_DOWN){
					boolean move;
					if( i!=2){
						move=isMovePossible(x, y, i) && !isMovePossible(x, y, 2);
						if(move){
							//if(action!=2){
							if(action==1){
								cost+=8000;
							}else
								cost-=8000;
						}
					}
				}
				if(lastAction.get(lastAction.size()-1)==ArtificialIntelligence.AI_ACTION_GO_LEFT){
					boolean move;
					if( i!=3){
						move=isMovePossible(x, y, i) && !isMovePossible(x, y, 3);
						if(move){
							//if(action!=3){
							if(action==4){
								cost+=8000;
							}else
								cost-=8000;
						}
					}
				}
				if(lastAction.get(lastAction.size()-1)==ArtificialIntelligence.AI_ACTION_GO_RIGHT){
					boolean move;
					if( i!=4){
						move=isMovePossible(x, y, i) && !isMovePossible(x, y, 4);
						if(move){
							//if(action!=4){
							if(action==3){
								cost+=8000;
							}else
								cost-=8000;
						}
					}
				}
			}
	}
		return cost;
	}
	
	/**
	 * 	Renvoie 'true' si l'action est applicable 
	 * @param x  coordonne x de la direction d'une action possible 
	 * @param y  coordonne y de la direction d'une action possible
	 * @param action  action que le joueur peut faire en fonction des etats
	 * @return 
	 * 		?
	 */
	private boolean isMovePossible(int x, int y, int action)
	{	
		boolean result;
		int matrix[][]=getZoneMatrix();
		switch(action)
		{	case ArtificialIntelligence.AI_ACTION_GO_UP:
				result = y>0 && (!isObstacleWall(x,y-1) && !(matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_BOMB) && !(matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_FIRE) && (isObstacleEmpty(x, y-1) || isObstacleBonus(x, y-1))); //!isObstacleBombFire(x, y-1, move) && (isObstacleEmpty(x, y-1) || isObstacleBonus(x, y-1)));
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				result = y<(getZoneMatrixDimY()-1) && (!isObstacleWall(x,y+1) && !(isObstacleBombFire(x, y+1, action)) && (isObstacleEmpty(x, y+1) || isObstacleBonus(x,y+1)));
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				result = x>0 && (!isObstacleWall(x-1,y) && (!isObstacleBombFire(x-1, y, action)) && (isObstacleEmpty(x-1, y)||isObstacleBonus(x-1,y)));
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result = x<(getZoneMatrixDimX()-1) && (!isObstacleWall(x+1,y) && (!isObstacleBombFire(x+1, y, action)) && (isObstacleEmpty(x+1, y)||isObstacleBonus(x+1,y)));
				break;
			default:
				result = false;
				break;
		}
		return result;
	}
	
	/**
	 * 	Verifie si l'obstacle rencontre est une bombe ou portee d'une bombe
	 * @param x coordonne x de la bombe
	 * @param y coordonne y de la bombe
	 * @param action 
	 * @return
	 * 		? 
	 */	
	private boolean isObstacleBombFire (int x, int y,int action){
		int bombPower=getBombPowerAt(x, y);
		int[][] matrix = getZoneMatrix();
		boolean result = false;
		if(bombPower!=-1){
			for(int i=1;i<=bombPower;i++){
				result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB;
				if(action==ArtificialIntelligence.AI_ACTION_GO_UP){
					result = result || matrix[x][y-i]==7;//ArtificialIntelligence.AI_BLOCK_FIRE;
				}
				if(action==ArtificialIntelligence.AI_ACTION_GO_LEFT){
					result = result || matrix[x-i][y]==7;//ArtificialIntelligence.AI_BLOCK_FIRE;
				}
				if(action==ArtificialIntelligence.AI_ACTION_GO_DOWN){
					result = result || matrix[x][y+i]==7;//ArtificialIntelligence.AI_BLOCK_FIRE;
				}
				if(action==ArtificialIntelligence.AI_ACTION_GO_RIGHT){
					result = result || matrix[x+i][y]==7;//ArtificialIntelligence.AI_BLOCK_FIRE;
				}
			}
		}else{
			result=false;
		}
		return result;
	}
	/**
	 * 	Indique si la case situee a la position passee en parametre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position e etudier
	 * @param y	position e etudier
	 * @return	vrai si la case contient un obstacle
	 */
	private boolean isObstacleWall(int x, int y)
	{	
		int[][] matrix = getZoneMatrix();
		boolean result = false;
		// murs
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		if(getTimeBeforeShrink()<=11){
			result = result || (x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1]);
		}
		return result;
	}
	
	/**
	 * 	Verifie si la case en question est vide ou pas
	 * @param x coordonne de la case en question
	 * @param y coordonne de la case en question
	 * @return true si la case est vide
	 */
	private boolean isObstacleEmpty(int x, int y)
	{	
		int[][] matrix = getZoneMatrix();
		boolean result = false;
		// libre
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_EMPTY;
		return result;
	}
	/**
	 * 	Verifie si la case en question contient un bonus ou non
	 * @param x coordonne de la case en question
	 * @param y coordonne de la case en question
	 * @return true si la case contient un bonus
	 */
	private boolean isObstacleBonus(int x, int y)
	{	
		int[][] matrix = getZoneMatrix();
		boolean result = false;
		// libre
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_ITEM_BOMB;
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE;
		return result;
	}
	
	/**
	 *	Verifie s'il s'agit d'un mur destructible dans la case dont les coordonnes sont pas
	 *passees en parametre 
	 * @param x coordonne de la case en question
	 * @param y coordonne de la case en question
	 * @return
	 * 		?
	 */
	private boolean isThereWallSoft(int x, int y){
		boolean done=false;
		boolean move;
		int[][] matrix = getZoneMatrix();
		boolean result = false;
		// murs
		result = result || matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		if(result){
			for(int i=1; i<=4;i++){
				if( i!=1){
					move=isMovePossible(x, y-1, i);
					if(move){
						done=true;
						return done;
					}
				}
			}
		}
		result = result || matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		if(result){
			for(int i=1; i<=4;i++){
				if(i!=2){
					move=isMovePossible(x, y+1, i);
					if(move){
						done=true;
						return done;
					}
				}
			}
		}
		result = result || matrix[x-1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		if(result){
			for(int i=1; i<=4;i++){
				if(i!=3){
					move=isMovePossible(x-1, y, i);
					if(move){
						done=true;
						return done;
					}
				}
			}
		}
		result = result || matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		if(result){
			for(int i=1; i<=4;i++){
				if(i!=4){
					move=isMovePossible(x+1, y, i);
					if(move){
						done=true;
						return done;
					}
				}
			}
		}
		return done;
	}
	/**
	 * Parmi les blocs dont le type correspond a la valeur 'blockType'
	 * passee en parametre, cette methode cherche lequel est le plus proche
	 * du point de coordonnees (x,y) passees en parametres. Le resultat
	 * prend la forme d'un tableau des deux coordonees du bloc le plus proche.
	 * Le tableau est contient des -1 s'il n'y a aucun bloc du bon type dans la zone de jeu.
	 * @param x	position de reference
	 * @param y	position de reference
	 * @param blockType	le type du bloc recherche
	 * @return	les coordonnees du bloc le plus proche
	 */
	private int[] getClosestBlockPosition(int x, int y, int blockType)
	{	
		int minDistance = Integer.MAX_VALUE;
		int result[] = {-1,-1}; 
		int[][] matrix = getZoneMatrix();
		for(int i=0;i<getZoneMatrixDimX();i++)
			for(int j=0;j<getZoneMatrixDimY();j++)
				if(matrix[i][j] == blockType)
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
	 * 	Controle la petite matrice qui entoure le joueur IA en partant de la matrice
	 * de la zone de jeu grace a la portee des bombes de ce joueur
	 * @param x coordonne du joueur IA
	 * @param y coordonne du joueur IA
	 * @param blockType  le type des blocs aux alentours du joueur
	 * @return
	 * 		? 
	 */
	
	private int[] getPlayerMatrix(int x, int y, int blockType){
		int bombPower=getOwnFirePower()+1;
		int x1=x-bombPower;
		int x2=x+bombPower;
		int y1=y-bombPower;
		int y2=y+bombPower;
		if(x1<=0)
			x1 = 1;
		if(x2>=getZoneMatrixDimX())
			x2= getZoneMatrixDimX()-1;
		if(y1<=0)
			y1=1;
		if(y2>=getZoneMatrixDimY())
			y2=getZoneMatrixDimY()-1;
		int minDistance = Integer.MAX_VALUE;
		int result[] = {-1,-1}; 
		int[][] matrix = getZoneMatrix();
		for(int i=x1;i<=x2;i++)
			for(int j=y1;j<=y2;j++)
				if(matrix[i][j] == blockType)
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
	 * entre le point de coordonnees (x1,y1) et celui de coordonnees (x2,y2). 
	 * @param x1	position du premier point
	 * @param y1	position du premier point
	 * @param x2	position du second point
	 * @param y2	position du second point
	 * @return	la distance de Manhattan entre ces deux points
	 */
	private int distance(int x1,int y1,int x2,int y2)
	{	
		int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	}
	/**
	 * 	Verifie si le joueur IA est seul dans la zone de jeu ou pas
	 * @param state  etat du joueur dans la zone de jeu
	 * @return 'true' si le joueur IA est seul dans la zone de jeu
	 */
	public boolean isPlayerAlone(State state) {
		int k=0;
		boolean playerAlone;
		for(int i=0;i<getPlayerCount();i++){
			if(isPlayerAlive(i))
				k++;
		}
		if (k==1){
			state.setPlayerAlone(true);
			playerAlone=true;
		}else{ 
			state.setPlayerAlone(false);
			playerAlone=false;
		}
		return playerAlone;
	}
	
	/**
	 * 	Verifie s'il y a un autre joueur dans la zone de jeu autre que le joueur IA
	 * @param state etat du joueur dans la zone de jeu
	 * @return 'true' s'il y a qu'un seul joueur dans la zone de jeu a part le joueur IA
	 */
	public boolean isPlayerCount2(State state) {
		int k=0;
		boolean result;
		for(int i=0;i<getPlayerCount();i++){
			if(isPlayerAlive(i))
				k++;
		}
		if (k==2){
			result=true;
		}else{ 
			result=false;
		}
		state.setPlayerCount2(result);
		return result;
	}
	
	/**
	 * 	Verifie s'il y a deux autres joueurs dans la zone de jeu autre que le joueur IA
	 * @param state etat du joueur dans la zone de jeu
	 * @return 'true' s'il y a deux autres joueurs dans la zone de jeu a part le joueur IA
	 */
	public boolean isPlayerCount3(State state) {
		int k=0;
		boolean result;
		for(int i=0;i<getPlayerCount();i++){
			if(isPlayerAlive(i))
				k++;
		}
		if (k==3){
			//state.setPlayerAlone(true);
			result=true;
		}else{ 
			//state.setPlayerAlone(false);
			result=false;
		}
		state.setPlayerCount3(result);
		return result;
	}
	
	/**
	 * 	Verifie s'il y a trois autres joueurs dans la zone de jeu autre que le joueur IA
	 * @param state etat du joueur dans la zone de jeu
	 * @return 'true' s'il y a trois autres joueurs dans la zone de jeu a part le joueur IA
	 */
	public boolean isPlayerCount4(State state) {
		int k=0;
		boolean result;
		for(int i=0;i<getPlayerCount();i++){
			if(isPlayerAlive(i))
				k++;
		}
		if (k==4){
			//state.setPlayerAlone(true);
			result=true;
		}else{ 
			//state.setPlayerAlone(false);
			result=false;
		}
		state.setPlayerCount4(result);
		return result;
	}
	
	/**
	 * 	Verifie si le joueur IA se trouve pres d'une bombe ou pas
	 * @param state  etat du joueur IA
	 * @param x   coordonne du joueur IA
	 * @param y   coordonne du joueur IA
	 * @param action action a verifier pour la realiser au prochain mouvement du joueur IA
	 * @return 'true' s'il s'agit d'une bombe pres du joueur 
	 */
	public boolean isPlayerCloseBomb(int action,State state,int x,int y) {
		int matrix[][]=getZoneMatrix();
		boolean result=false;
		int dangerPos[] = getPlayerMatrix(x,y,ArtificialIntelligence.AI_BLOCK_BOMB);
		if(dangerPos[0]!=-1){
			int bombPower=getBombPowerAt(dangerPos[0],dangerPos[1]);
			for(int i=1;i<=bombPower;i++){
				result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB;
				int x1=x-i;
				int x2=x+i;
				int y1=y-i;
				int y2=y+i;
				
				if(x1<=0)
					x1 = 1;
				if(x2>=getZoneMatrixDimX())
					x2= getZoneMatrixDimX()-1;
				if(y1<=0)
					y1=1;
				if(y2>=getZoneMatrixDimY())
					y2=getZoneMatrixDimY()-1;
				
				if(action==ArtificialIntelligence.AI_ACTION_GO_UP){
					result = result || matrix[x][y1]==ArtificialIntelligence.AI_BLOCK_FIRE;
				}
				if(action==ArtificialIntelligence.AI_ACTION_GO_LEFT){
					result = result || matrix[x1][y]==ArtificialIntelligence.AI_BLOCK_FIRE;
				}
				if(action==ArtificialIntelligence.AI_ACTION_GO_DOWN){
					result = result || matrix[x][y2]==ArtificialIntelligence.AI_BLOCK_FIRE;
				}
				if(action==ArtificialIntelligence.AI_ACTION_GO_RIGHT){
					result = result || matrix[x2][y]==ArtificialIntelligence.AI_BLOCK_FIRE;
				}
			}
			state.setPlayerCloseBomb(result);
		}else{
			result=false;
			state.setPlayerCloseBomb(result);
		}
		return result;
	}
	
	/**
	 * 	Verifie s'il y un autre joueur pres du joueur IA ou pas
	 * @param state  etat du joueur IA
	 * @param x   coordonne du joueur IA
	 * @param y   coordonne du joueur IA
	 * @return 'true' s'il s'agit d'un autre joueur pres du joueur IA 
	 */
	public boolean isPlayerClosePlayer(State state,int x,int y) {
		boolean playerCloseDanger;
		int dangerPos[]= getClosestPlayerPosition(x,y);
		if(dangerPos[0]!=-1){
			playerCloseDanger=true;
			state.setPlayerClosePlayer(playerCloseDanger);
		}else{
			playerCloseDanger=false;
			state.setPlayerClosePlayer(playerCloseDanger);
		}
		return playerCloseDanger;
	}
	
	/**
	 * 	Verifie si la case est vide ou pas
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si la case est vide 
	 */
	public boolean isBlockEmpty(State state,int x,int y) {
		boolean result=false;
		int dangerPos[] = getClosestBlockPosition(x,y,ArtificialIntelligence.AI_BLOCK_EMPTY);
		// si case vide
		if(dangerPos[0]!=-1)
			result=true;
		state.setBlockEmpty(result);
		return result;
	}
	
	/**
	 * 	Verifie si le mur est destrubrible ou pas
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si le  mur est destructible 
	 */
	public boolean isBlockSoft(State state,int x,int y) {
		boolean result=false;
		int dangerPos[] = getClosestBlockPosition(x,y,ArtificialIntelligence.AI_BLOCK_WALL_SOFT);
		// si mur destructible
		if(dangerPos[0]!=-1)
			result=true;
		state.setWallSoft(result);
		return result;
	}
	
	/**
	 * 	Verifie si le mur est indestrubrible ou pas
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si le  mur est indestructible 
	 */
	public boolean isBlockHard(State state,int x,int y) {
		boolean result=false;
		int dangerPos[] = getClosestBlockPosition(x,y,ArtificialIntelligence.AI_BLOCK_WALL_HARD);
		// si mur indestructible
		if(dangerPos[0]!=-1)
			result=true;
		state.setWallHard(result);
		return result;
	}
	
	/**
	 * 	Retourne les coordonnes  du joueur le plus proche au joueur IA
	 * @param x coordonne du joueur
	 * @param y coordonne du joueur
	 * @return les coordonnes du joueur le plus proche au joueur IA
	 */
	private int[] getClosestPlayerPosition(int x, int y){	
		int minDistance = Integer.MAX_VALUE;
		int result[] = {-1,-1}; 
		for(int i=0;i<getPlayerCount();i++)
		{	int pos[] = getPlayerPosition(i);
			int temp = distance(x,y,pos[0],pos[1]);
			if(temp<minDistance)
			{	result[0] = pos[0];
				result[1] = pos[1];
				minDistance = temp;
			}
		}
		return result;
	}
	
	/**
	 * 	Verifie si le joueur se trouve loin du joueur IA
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si le joueur est loin de joueur IA
	 */
	public boolean isPlayerFarPlayer(State state,int x, int y) 
	{
		boolean playerFarDanger;
//		 on determine ou est le joueur le plus proche
		int dangerPos[]= getClosestPlayerPosition(x,y);
		if(dangerPos[0]==-1){
			playerFarDanger=true;
			state.setPlayerFarPlayer(playerFarDanger);
		}else{
			playerFarDanger=false;
			state.setPlayerFarPlayer(playerFarDanger);
		}
		return playerFarDanger;
	}
	
	/**
	 * 	Verifie si une bombe se trouve loin du joueur IA
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si la bombe  est loin de joueur IA
	 */
	public boolean isPlayerFarBomb(State state,int x, int y) 
	{
		boolean playerFarDanger;
//		 on determine ou est le feu le plus proche
		int dangerPos[] = getPlayerMatrix(x,y,ArtificialIntelligence.AI_BLOCK_FIRE);
		// si aucun feu, on determine ou est la bombe la plus proche
		if(dangerPos[0]==-1)
			dangerPos = getPlayerMatrix(x,y,ArtificialIntelligence.AI_BLOCK_BOMB);
		if(dangerPos[0]==-1){
			playerFarDanger=true;
			state.setPlayerFarBomb(playerFarDanger);
		}else{
			playerFarDanger=false;
			state.setPlayerFarBomb(playerFarDanger);
		}
		return playerFarDanger;		
	}
	
	/**
	 * 	Verifie si le joueur IA est au milieu de la zone de jeu
	 * @param state  etat du joueur IA
	 * @param x   coordonne du joueur IA
	 * @param y   coordonne du joueur IA
	 * @return 'true' si le joueur est au milieu de la zone de jeu ou si le joueur est pres 
	 *        du milieu de la zone de matrice
	 */
	public boolean isPlayerInMiddle(State state,int x,int y) {
		boolean playerInMiddle;
		if(distance(x, y, 8, 7)<=2){
			playerInMiddle=true;
			state.setPlayerInMiddle(playerInMiddle);
		}else{ 
			playerInMiddle=false;
			state.setPlayerInMiddle(playerInMiddle);
		}
		return playerInMiddle;
	}
	
	/**
	 * 	Verifie si un bonus se trouve pres du joueur IA
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si le bonus  est pres de joueur IA
	 */
	public boolean isBonusClose(State state,int x,int y) {
		boolean bonusClose;
		//on determine ou est la bombe bonus le plus proche
		int bonusPos[] = getClosestBlockPosition(x,y,ArtificialIntelligence.AI_BLOCK_ITEM_BOMB);
		// si aucun feu, on determine bonus fire plus proche
		if(bonusPos[0]==-1)
			bonusPos = getClosestBlockPosition(x,y,ArtificialIntelligence.AI_BLOCK_ITEM_FIRE);
		// si aucune bombe, on determine oe est le joueur le plus proche
		if(bonusPos[0]!=-1){
			bonusClose=true;
			state.setBonusClose(bonusClose);
		}else{
			bonusClose=false;
			state.setBonusClose(bonusClose);
		}
		return bonusClose;
	}
	
	/**
	 * 	Verifie si le 'shrink' se trouve pres du joueur IA
	 * @param state  etat du joueur IA
	 * @param x   coordonne de la case
	 * @param y   coordonne de la case
	 * @return 'true' si le shrink  est pres du joueur IA
	 */
	public boolean isShrinkClose(State state,int x,int y) {
		boolean shrinkClose;
		if(x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1] && getTimeBeforeShrink()< 1){
			shrinkClose=true;
			state.setShrinkClose(shrinkClose);
		}else{
			shrinkClose=false;
			state.setShrinkClose(shrinkClose);
		}
		return shrinkClose;
	}
	
	/**
	 * 	Calcule le point que le joueur IA peut obtenir selon l'etat dans lequel il  
	 * se trouve 
	 * @param action 
	 * @param state   etat du joueur
	 * @param x		  coordonne du joueur IA	
	 * @param y		  coordonne du joueur IA
	 * @return
	 * 		? 
	 */
	public double getPoint(int action, State state,int x, int y){
		double point=0;
		if(isPlayerAlone(state)){
			point+=1000;
		}
		if(isPlayerInMiddle(state,x,y)){
			point+=1000;
		}
		if(this.isPlayerFarPlayer(state,x,y)){
			point+=800;
		}
		if(this.isPlayerFarBomb(state,x,y)){
			point+=2000;
		}
		if(this.isBonusClose(state,x,y)){
			point+=10000;
		}
		if(this.isPlayerClosePlayer(state,x,y)){
			point+=-1000;
		}
		if(this.isPlayerCloseBomb(action,state,x,y)){
			point+=-10000;
		}
		if(this.isShrinkClose(state,x,y)){
			point+=-1000;
		}
		if(this.isBlockEmpty(state, x, y)){
			point+=250;
		}
		if(this.isBlockSoft(state, x, y)){
			point+=750;
		}
		if(this.isBlockHard(state, x, y)){
			point-=500;
		}
		if(this.isPlayerCount2(state)){
			point+=500;
		}
		if(this.isPlayerCount3(state)){
			point+=300;
		}
		if(this.isPlayerCount4(state)){
			point+=100;
		}
	
		state.setHeuristic(point);
		return point;
	}
	
	/**
	 * Renvoie l'etat obtenu si on applique l'action a l'etat passe
	 * en parametre.
	 * @param state	l'etat d'origine
	 * @param x 
	 * @param y 
	 * @param action 
	 * @return	l'etat cible obtenu en appliquant l'action
	 * @throws ImpossibleActionException
	 */
	public State apply(State state,int x, int y , int action) throws ImpossibleActionException
	{	
		State stateResult;
//		int result = -1;
//		int move = -1;
	    if(ArtificialIntelligence.AI_ACTION_GO_DOWN==action){
//			move=ArtificialIntelligence.AI_DIR_DOWN;
//			result=move;
			y=y+1;
	    }
	    if(ArtificialIntelligence.AI_ACTION_GO_LEFT==action){
//			move=ArtificialIntelligence.AI_DIR_LEFT;
//			result=move;
			x=x-1;
	    }
	    if(ArtificialIntelligence.AI_ACTION_GO_RIGHT==action){
//			move=ArtificialIntelligence.AI_DIR_RIGHT;
//			result=move;
			x=x+1;
	    }
	    if(ArtificialIntelligence.AI_ACTION_GO_UP==action){
//			move=ArtificialIntelligence.AI_DIR_UP;
//			result=move;
			y=y-1;
	    }
		
//	    if(ArtificialIntelligence.AI_ACTION_PUT_BOMB==action )
//	    	result= ArtificialIntelligence.AI_ACTION_PUT_BOMB;
    
//	    if(move==-1 && ArtificialIntelligence.AI_ACTION_DO_NOTHING==action)
//	    	result =ArtificialIntelligence.AI_ACTION_DO_NOTHING;

		boolean SplayerInMiddle=isPlayerInMiddle(state,x,y);
	    boolean SplayerFarPlayer=isPlayerFarPlayer(state,x,y);
	    boolean SplayerFarBomb=isPlayerFarBomb(state,x,y);
	    boolean SplayerClosePlayer=isPlayerClosePlayer(state,x,y);
	    boolean SplayerCloseBomb=isPlayerCloseBomb(action,state,x,y);
	    boolean SplayerAlone=isPlayerAlone(state);
	    boolean SbonusClose=isBonusClose(state,x,y);
	    boolean SshrinkClose=isShrinkClose(state,x,y);
	    boolean sblockEmpty=isBlockEmpty(state, x, y);
	    boolean SwallSoft=isBlockSoft(state, x, y);
	    boolean SwallHard=isBlockHard(state, x, y);
	    boolean SPcount2=isPlayerCount2(state);
	    boolean SPcount3=isPlayerCount3(state);
	    boolean SPcount4=isPlayerCount4(state);
	    stateResult=new State(SplayerInMiddle, SplayerFarPlayer,SplayerFarBomb, SplayerClosePlayer,SplayerCloseBomb, SplayerAlone, SbonusClose, SshrinkClose, sblockEmpty, SwallSoft,SwallHard,SPcount2,SPcount3,SPcount4);
		return stateResult;
	}
}