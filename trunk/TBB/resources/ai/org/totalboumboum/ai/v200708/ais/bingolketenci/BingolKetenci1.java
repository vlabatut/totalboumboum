package org.totalboumboum.ai.v200708.ais.bingolketenci;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;





/**
 * Classe implémentant un comportement agressif:on donne des points à chaque
 * case en considerant les murs,les autres joueurs,les bombes....On choisit un
 * cible et y arrive en choisissant le chemin le plus court (algorithme A
 * �toile). D�s qu'il arrive au cible,il le détruit.Avant de faire un mouvement,
 * il controle tout d'abord s'il y a un danger pour lui.S'il y en a,il bouge
 * seulement pour se sauver.
 * 
 * @author Gizem Bingol
 * @author Utku Gorkem Kentenci
 *
 */
public class BingolKetenci1 extends ArtificialIntelligence {
	private static final long serialVersionUID = 1L;
	/** position de la derniere bombe qui est mis par AI. */
	private int lastBombPos[];
	/** dernière position du personnage pour la fonction "danger". */
	private int lastPosition[];
	/** derniere position du personnage pour la fonction "move". */
	private int lastPos[];
	/**
	 * derniere position du personnage pour la fonction "danger" mais
	 * specialement pour une condition
	 */
	private int lastPosSS[];
	/** la derniere action du personnage pour la fonction "danger" */
	private int lastMove;
	/** la derniere action du personnage pour la fonction "danger" */
	private int lastMo;
	/**
	 * les variables utilisées pour empecher petits tremblements de l'ia en se
	 * sauvant de la bombe,cree la possibilit� de se sauver en allant vers la
	 * bombe
	 */
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	/**
	 * le percept de l'ia limit� avec les cases qu'il peut arriver sans détruire
	 * des murs(sous forme de matrice)
	 */
	public int[][] carte;

	/** la portee des bombes que l'ia peut poser. */
	private int bombF = 1;
	/** le nombre de bombes que l'ia peut poser. */
//	private int bombC = 1;
	/** variable indiquant si c'est le premier appel au jeu. */
	private boolean first;
	/** variables utilisées pour implementer l'algorithme Aetoile. */
	int index;

	/** le chemin permettant d'aller au cible. */
	private LinkedList<Noeud> path;
	/** la case qu'on veut arriver:le cible. */
	Noeud goal;
	/** shrink */
	int perimetre;
	/**
	 * le serpent de shrink,un serpent simulatif dont la queue est la derniere
	 * case du shrink qui arrive.
	 */
	int snake[][];
	/**
	 * Variables utilisées pour une situation sp�ciale de se sauver,empeche de
	 * faire des mouvement repetitifs s'il reste entre 2 bombes.
	 */
	private boolean operation;

	/***************************************************************************
	 * /** Constructeur.
	 */
	public BingolKetenci1() {
		super("BinglKtnci");
		lastMove = AI_ACTION_DO_NOTHING;

		left = false;
		right = false;
		up = false;
		down = false;

		operation = false;
		lastPosition = new int[2];
		lastPosition[0] = 0;
		lastPosition[1] = 0;

		bombF = 1;
//		bombC = 1;

		first = true;
		lastPos = new int[2];

		// specialement pour le cas où l'ia se trouve sur la bombe
		lastPosSS = new int[2];
		lastPosSS[0] = 0;
		lastPosSS[1] = 0;

		carte = new int[17][15];

		index = 0;

		path = new LinkedList<Noeud>();

		lastBombPos = new int[2];

		goal = null;

		// variables utilis�s pour shrink
		perimetre = 1;
		snake = new int[5][2];
		snake[0][0] = 1;
		snake[0][1] = 1;

		snake[1][0] = 2;
		snake[1][1] = 1;

		snake[2][0] = 3;
		snake[2][1] = 1;

		snake[3][0] = 4;
		snake[3][1] = 1;

		snake[4][0] = 5;
		snake[4][1] = 1;
	}

	public Integer call() throws Exception {

		int tab[][] = getZoneMatrix();

		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];

		// on utilise la carte pour la fonction "danger" et pour la fonction
		// "move".
		for (int i = 0; i < 17; i++) { // sur l'axe x
			for (int j = 0; j < 15; j++) { // sur l'axe y

				if (i == 0 || i == 16 || j == 0 || j == 14)
					carte[i][j] = AI_BLOCK_WALL_HARD;
				else
					carte[i][j] = AI_BLOCK_UNKNOWN;
			}
		}

		oCarte(pointCase(tab), x, y);

		// il prend ses capacit�s de bombe
		bombF = super.getOwnFirePower();
//		bombC = super.getOwnBombCount();

		// s'il est sur la bombe et en train de s'�chapper ,il doit repeter le
		// meme mouvement jusqu'� sortir de la case de la bombe,
		// variables utilis�s pour ce but.
		if (lastPosSS[0] != x || lastPosSS[1] != y) {
			lastMove = AI_ACTION_DO_NOTHING;
			lastPosSS[0] = x;
			lastPosSS[1] = y;
		}

		// tout au d�but du jeu et chaque fois que la fonction "danger" est
		// utilisée,sa position est lastPos
		if (first) {
			lastPos[0] = getOwnPosition()[0];
			lastPos[1] = getOwnPosition()[1];
			lastMo = AI_ACTION_DO_NOTHING;
			first = false;
		}

		int action = AI_ACTION_DO_NOTHING;

		// quand le shrink commence
		if (getTimeBeforeShrink() < 0
				&& (getNextShrinkPosition()[0] != snake[0][0] || getNextShrinkPosition()[1] != snake[0][1])) {
			shrink();
			int pos;
			if ((pos = posShrink(x, y)) != -1) {
				action = shrinkMove(tab, x, y, pos);
			}
		}
		// avant de faire un mouvement on appelle d'abord la fonction
		// "danger".Si la fonction "danger" ne retourne aucun mouvement
		// alors on peut faire une action d'attaque.Sinon on est en mode de
		// d�fense et on fait ce que la fonction "danger" retourne.
		if (action == AI_ACTION_DO_NOTHING) {
			action = danger(tab, x, y);
		}

		if (action == AI_ACTION_DO_NOTHING) {
			action = move(x, y);
		}
		// si on est arriv� au cible et la fonction danger a dit qu'il n y a pas
		// de danger
		if (goal != null && goal.getX() == x && goal.getY() == y
				&& action == AI_ACTION_DO_NOTHING) {
			x = getOwnPosition()[0];
			y = getOwnPosition()[1];

			// avant de poser une bombe,il faut simuler la situation, et donc
			// d�cider de mettre une bombe
			// sauf s'il peut se sauver.
			tab[x][y] = AI_BLOCK_BOMB;// comme s'il a pos� la bombe

			// on utilise la carte pour la fonction "danger" et pour la fonction
			// "move".
			for (int i = 0; i < 17; i++) { // sur l'axe x
				for (int j = 0; j < 15; j++) { // sur l'axe y

					if (i == 0 || i == 16 || j == 0 || j == 14)
						carte[i][j] = AI_BLOCK_WALL_HARD;
					else
						carte[i][j] = AI_BLOCK_UNKNOWN;
				}
			}
			// on appelle la fonction "pointCase" pour donner des points �
			// chaque case
			oCarte(pointCase(tab), x, y);
			// on met une bombe sauf si la fonction canEscape retourne true.
			if (canEscape(tab, x, y)) {
				action = AI_ACTION_PUT_BOMB;

				lastBombPos[0] = x;
				lastBombPos[1] = y;
			}

		}

		return action;
	}

	

	
	/**
	 * Indique si la case situ�e à la position passée en paramètre constitue un
	 * obstacle pour un personnage : bombe, feu, mur.
	 * 
	 * @param x
	 *            position à �tudier
	 * @param y
	 *            position à �tudier
	 * @param tab
	 *            le tableau du jeu
	 * @return action à faire quand le shrink commence
	 */
	private int shrinkMove(int tab[][], int x, int y, int pos) {
		int action = AI_ACTION_DO_NOTHING;
		if (pos == 0) {
			if (snake[pos][0] == snake[pos + 1][0]) { // mouvement sur l'axe y
				if (snake[pos][1] < snake[pos + 1][1]) { // vers le bas
					if (isMovePossible(x, y, AI_ACTION_GO_LEFT)
							&& carte[x - 1][y] >= 0)
						action = AI_ACTION_GO_LEFT;
					else if (isMovePossible(x, y, AI_ACTION_GO_DOWN))
						action = AI_ACTION_GO_DOWN;

				} else { // vers le haut
					if (isMovePossible(x, y, AI_ACTION_GO_RIGHT)
							&& carte[x + 1][y] >= 0)
						action = AI_ACTION_GO_RIGHT;
					else if (isMovePossible(x, y, AI_ACTION_GO_UP))
						action = AI_ACTION_GO_UP;

				}

			} else {
				if (snake[pos][1] == snake[pos + 1][1]) { // mouvement sur
															// l'axe x
					if (snake[pos][0] < snake[pos + 1][0]) { // vers la
																// droite
						if (isMovePossible(x, y, AI_ACTION_GO_DOWN)
								&& carte[x][y + 1] >= 0)
							action = AI_ACTION_GO_DOWN;
						else if (isMovePossible(x, y, AI_ACTION_GO_RIGHT))
							action = AI_ACTION_GO_RIGHT;
					} else { // vers la gauche
						if (isMovePossible(x, y, AI_ACTION_GO_UP)
								&& carte[x][y - 1] >= 0)
							action = AI_ACTION_GO_UP;
						else if (isMovePossible(x, y, AI_ACTION_GO_LEFT))
							action = AI_ACTION_GO_LEFT;
					}
				}
			}
		} else {
			if (snake[pos - 1][0] == snake[pos][0]) { // mouvement sur l'axe y
				if (snake[pos - 1][1] < snake[pos][1]) { // vers le bas
					if (isMovePossible(x, y, AI_ACTION_GO_LEFT))
						action = AI_ACTION_GO_LEFT;
					else if (isMovePossible(x, y, AI_ACTION_GO_DOWN))
						action = AI_ACTION_GO_DOWN;

				} else { // vers le haut
					if (isMovePossible(x, y, AI_ACTION_GO_RIGHT))
						action = AI_ACTION_GO_RIGHT;
					else if (isMovePossible(x, y, AI_ACTION_GO_UP))
						action = AI_ACTION_GO_UP;

				}

			} else {
				if (snake[pos - 1][1] == snake[pos][1]) { // mouvement sur
															// l'axe x
					if (snake[pos - 1][0] < snake[pos][0]) { // vers la
																// droite
						if (isMovePossible(x, y, AI_ACTION_GO_DOWN))
							action = AI_ACTION_GO_DOWN;
						else if (isMovePossible(x, y, AI_ACTION_GO_RIGHT))
							action = AI_ACTION_GO_RIGHT;
					} else { // vers la gauche
						if (isMovePossible(x, y, AI_ACTION_GO_UP))
							action = AI_ACTION_GO_UP;
						else if (isMovePossible(x, y, AI_ACTION_GO_LEFT))
							action = AI_ACTION_GO_LEFT;
					}
				}
			}
		}

		return action;
	}
	
	/**
	 * Determine le mouvement à faire en cas de danger.Si une bombe est
	 * pos�e,l'ia d�cide de faire un mouvement le plus vite possible sans faire
	 * des calculs.Il regarde les mouvement qu'il peut faire sur 3-4 cases.Il
	 * essaie de trouver un mouvement pratique qui peut lui sauver.
	 * 
	 * @param tab
	 *            le tableau du jeu
	 * @param x
	 *            position de l'ia
	 * @param y
	 *            position de l'ia
	 * @return action qu'il va faire
	 */
	  
	 
	private int danger(int tab[][],int x,int y){
		int action = AI_ACTION_DO_NOTHING;		
		int i;

		//utilisé dans la fonction "move"
		first = true;
		
		
		if(tab[x][y] == AI_BLOCK_BOMB){//s'il est sur la bombe

//les cas où il ne peut pas se sauver

			if(lastMove != AI_ACTION_DO_NOTHING && isMovePossible(x,y,lastMove) &&  lastPosSS[0]==x && lastPosSS[1]==y){
				action = lastMove;
			}else{
				
				lastPosSS[0]=x;
				lastPosSS[1]=y;
				boolean l=false;
				boolean r=false;
				boolean d=false;
				boolean u=false;
				
				//s'il peut se sauver en allant directement vers la gauche
				i = x-1;
				while(tab[i][y] != AI_BLOCK_WALL_HARD && tab[i][y]!= AI_BLOCK_WALL_SOFT){
					i--;
				}
				
				if((getBombPowerAt(x,y)+i)<x){
					l = true;
				}
				
				//s'il peut se sauver directement en allant directement vers la droite
				i = x+1;
				while(tab[i][y] != AI_BLOCK_WALL_HARD && tab[i][y]!= AI_BLOCK_WALL_SOFT){
					i++;
				}
				
				if(x+getBombPowerAt(x,y)<i){
					r = true;
				}
				
				//s'il peut se sauver directement en allant vers le haut
				i = y-1;
				while(tab[x][i] != AI_BLOCK_WALL_HARD && tab[x][i]!= AI_BLOCK_WALL_SOFT){
					i--;
				}
				if(getBombPowerAt(x,y)+i<y){
					u = true;
				}
				
				//s'il peut se sauver directement en allant vers le bas
				i = y+1;
				while(tab[x][i] != AI_BLOCK_WALL_HARD && tab[x][i]!= AI_BLOCK_WALL_SOFT){
					i++;
				}
				if(y+getBombPowerAt(x,y)<i){
					d = true;
				}
				
				if(lastBombPos[0]==x && lastBombPos[1]==y ){//Si l'ia a pos� la bombe , on n'a pas besoin d'utiliser getBombPosition,on utilise les ligne ci-dessous
				//regarde les possibilit�s d'aller d'abord en haut puis à gauche et à droite
				
					if(!isObstacle(tab,x,y-1) && ((!isObstacle(tab,x-1,y-1)&&carte[x-1][y-1]>=0) || (!isObstacle(tab,x+1,y-1)&&carte[x+1][y-1]>=0)) ){//possible d'aller vers le haut,ensuite d'aller à gauche ou à droite=>vers le haut.
						action = AI_ACTION_GO_UP;
						lastMove = action;
						}else if(!isObstacle(tab,x-1,y) && ((!isObstacle(tab,x-1,y+1)&&carte[x-1][y+1]>=0) || (!isObstacle(tab,x-1,y-1) && carte[x-1][y-1]>=0)) ){
								action = AI_ACTION_GO_LEFT;
								lastMove = action;
							}else if(!isObstacle(tab,x,y+1) && ((!isObstacle(tab,x-1,y+1)&&carte[x-1][y+1]>=0) || (!isObstacle(tab,x+1,y+1) && carte[x+1][y+1]>=0)) ){
									action = AI_ACTION_GO_DOWN;
									lastMove = action;
								}else if(!isObstacle(tab,x+1,y) && ((!isObstacle(tab,x+1,y+1) && carte[x+1][y+1]>=0)|| (!isObstacle(tab,x+1,y-1)&& carte[x+1][y-1]>=0)) ){
										action = AI_ACTION_GO_RIGHT;
										lastMove = action;
									}else if(!isObstacle(tab,x,y-1)&& !isObstacle(tab,x,y-2) && ((!isObstacle(tab,x-1,y-2)&& carte[x-1][y-2]>=0)|| (!isObstacle(tab,x+1,y-2) && carte[x+1][y-2]>=0 ))){//possibilit� d'aller 2 fois vers le haut puis à gauche ou à droite.=>vers la haut
											action = AI_ACTION_GO_UP;
											lastMove = action;
										}else if(!isObstacle(tab,x-1,y) && !isObstacle(tab,x-2,y) &&((!isObstacle(tab,x-2,y+1) && carte[x-2][y+1]>=0)|| (!isObstacle(tab,x-2,y-1))&& carte[x-2][y-1]>=0)){
												action = AI_ACTION_GO_LEFT;
												lastMove = action;
											}else if(!isObstacle(tab,x,y+1) && !isObstacle(tab,x,y+2) &&((!isObstacle(tab,x-1,y+2)&& carte[x-1][y+2]>=0)|| (!isObstacle(tab,x+1,y+2) && carte[x+1][y+2]>=0 ))){
													action = AI_ACTION_GO_DOWN;
													lastMove = action;
												}else if(!isObstacle(tab,x+1,y) && !isObstacle(tab,x+2,y) &&((!isObstacle(tab,x+2,y+1) && carte[x+2][y+1]>=0 )|| (!isObstacle(tab,x+2,y-1) && carte[x+2][y-1]>=0 ))){
														action = AI_ACTION_GO_RIGHT;
														lastMove = action;
													}else if(!isObstacle(tab,x,y-1)&& !isObstacle(tab,x,y-2) && !isObstacle(tab,x,y-3) && (u || (!isObstacle(tab,x-1,y-3)&& carte[x-1][y-3]>=0)|| (!isObstacle(tab,x+1,y-3) && carte[x+1][y-3]>=0 ))){//possibilit� d'aller vers le haut et puis possibiliter d'aller 3 fois vers le haut puis à gauche ou à droite.=>vers la haut
															action = AI_ACTION_GO_UP;
															lastMove = action;
														}else if(!isObstacle(tab,x-1,y) && !isObstacle(tab,x-2,y) && !isObstacle(tab,x-3,y) &&(l || (!isObstacle(tab,x-3,y+1) && carte[x-3][y+1]>=0)|| (!isObstacle(tab,x-3,y-1))&& carte[x-3][y-1]>=0)){
																action = AI_ACTION_GO_LEFT;
																lastMove = action;
															}else if(!isObstacle(tab,x,y+1) && !isObstacle(tab,x,y+2) && !isObstacle(tab,x,y+3) &&(d || (!isObstacle(tab,x-1,y+3)&& carte[x-1][y+3]>=0)|| (!isObstacle(tab,x+1,y+3) && carte[x+1][y+3]>=0 ))){
																	action = AI_ACTION_GO_DOWN;
																	lastMove = action;
																}else if(!isObstacle(tab,x+1,y) && !isObstacle(tab,x+2,y) && !isObstacle(tab,x+3,y) &&(r || (!isObstacle(tab,x+3,y+1) && carte[x+3][y+1]>=0 )|| (!isObstacle(tab,x+3,y-1) && carte[x+3][y-1]>=0 ))){
																		action = AI_ACTION_GO_RIGHT;
																		lastMove = action;
																}
					
				}else{//si la bombe est mis par un autre joueur
					int bPos = getBombPosition();

					if((bPos != AI_DIR_UP)&&!isObstacle(tab,x,y-1) && ((!isObstacle(tab,x-1,y-1)&&carte[x-1][y-1]>=0) || (!isObstacle(tab,x+1,y-1)&&carte[x+1][y-1]>=0)) ){//possible d'aller vers le haut puis à gauche ou à droite
						action = AI_ACTION_GO_UP;
						lastMove = action;
						}else if((bPos != AI_DIR_LEFT)&&!isObstacle(tab,x-1,y) && ((!isObstacle(tab,x-1,y+1)&&carte[x-1][y+1]>=0) || (!isObstacle(tab,x-1,y-1) && carte[x-1][y-1]>=0)) ){
								action = AI_ACTION_GO_LEFT;
								lastMove = action;
							}else if((bPos != AI_DIR_DOWN) && !isObstacle(tab,x,y+1) && ((!isObstacle(tab,x-1,y+1)&&carte[x-1][y+1]>=0) || (!isObstacle(tab,x+1,y+1) && carte[x+1][y+1]>=0)) ){
									action = AI_ACTION_GO_DOWN;
									lastMove = action;
								}else if((bPos != AI_DIR_RIGHT)&&!isObstacle(tab,x+1,y) && ((!isObstacle(tab,x+1,y+1) && carte[x+1][y+1]>=0)|| (!isObstacle(tab,x+1,y-1)&& carte[x+1][y-1]>=0)) ){
										action = AI_ACTION_GO_RIGHT;
										lastMove = action;
									}else if((bPos != AI_DIR_UP)&&!isObstacle(tab,x,y-1)&& !isObstacle(tab,x,y-2) && (u || (!isObstacle(tab,x-1,y-2)&& carte[x-1][y-2]>=0)|| (!isObstacle(tab,x+1,y-2) && carte[x+1][y-2]>=0 ))){//possibilit� d'aller deux fois vers le haut puis à gauche ou à droite											
										action = AI_ACTION_GO_UP;
											lastMove = action;
										}else if((bPos != AI_DIR_LEFT)&&!isObstacle(tab,x-1,y) && !isObstacle(tab,x-2,y) &&(l || (!isObstacle(tab,x-2,y+1) && carte[x-2][y+1]>=0)|| (!isObstacle(tab,x-2,y-1))&& carte[x-2][y-1]>=0)){
												action = AI_ACTION_GO_LEFT;
												lastMove = action;
											}else if((bPos != AI_DIR_DOWN)&&!isObstacle(tab,x,y+1) && !isObstacle(tab,x,y+2) &&(d || (!isObstacle(tab,x-1,y+2)&& carte[x-1][y+2]>=0)|| (!isObstacle(tab,x+1,y+2) && carte[x+1][y+2]>=0 ))){
													action = AI_ACTION_GO_DOWN;
													lastMove = action;
												}else if((bPos != AI_DIR_RIGHT)&&!isObstacle(tab,x+1,y) && !isObstacle(tab,x+2,y) &&(r || (!isObstacle(tab,x+2,y+1) && carte[x+2][y+1]>=0 )|| (!isObstacle(tab,x+2,y-1) && carte[x+2][y-1]>=0 ))){
														action = AI_ACTION_GO_RIGHT;
														lastMove = action;
													}else if((bPos != AI_DIR_UP)&&!isObstacle(tab,x,y-1)&& !isObstacle(tab,x,y-2) && !isObstacle(tab,x,y-3) && (u || (!isObstacle(tab,x-1,y-3)&& carte[x-1][y-3]>=0)|| (!isObstacle(tab,x+1,y-3) && carte[x+1][y-3]>=0 ))){////possibilit� d'aller 3 fois vers le haut puis à gauche ou à droite
															action = AI_ACTION_GO_UP;
															lastMove = action;
														}else if((bPos != AI_DIR_LEFT)&&!isObstacle(tab,x-1,y) && !isObstacle(tab,x-2,y) && !isObstacle(tab,x-3,y) &&(l || (!isObstacle(tab,x-3,y+1) && carte[x-3][y+1]>=0)|| (!isObstacle(tab,x-3,y-1))&& carte[x-3][y-1]>=0)){
																action = AI_ACTION_GO_LEFT;
																lastMove = action;
															}else if((bPos != AI_DIR_DOWN)&&!isObstacle(tab,x,y+1) && !isObstacle(tab,x,y+2) && !isObstacle(tab,x,y+3) &&(d || (!isObstacle(tab,x-1,y+3)&& carte[x-1][y+3]>=0)|| (!isObstacle(tab,x+1,y+3) && carte[x+1][y+3]>=0 ))){
																	action = AI_ACTION_GO_DOWN;
																	lastMove = action;
																}else if((bPos != AI_DIR_RIGHT)&&!isObstacle(tab,x+1,y) && !isObstacle(tab,x+2,y) && !isObstacle(tab,x+3,y) &&(r || (!isObstacle(tab,x+3,y+1) && carte[x+3][y+1]>=0 )|| (!isObstacle(tab,x+3,y-1) && carte[x+3][y-1]>=0 ))){
																		action = AI_ACTION_GO_RIGHT;
																		lastMove = action;
																}
									

				}
			}
						
		}else{//S'il y a une bombe dangereuse autour de lui.(pas sur lui)
			//o� est la bombe
			boolean bU = false;
			boolean bD = false;
			boolean bL = false;
			boolean bR = false; 
			//� quelle distance se trouve la bombe.
			int dR = 17;
			int dL = 17;
			int dU = 17;
			int dD = 17;
			
			
			i = x-1;
			while(tab[i][y] != AI_BLOCK_WALL_HARD && tab[i][y]!= AI_BLOCK_WALL_SOFT && tab[i][y] != AI_BLOCK_BOMB){
				i--;
			}
			if(tab[i][y]== AI_BLOCK_BOMB){
				if(x-(getBombPowerAt(i,y)+i)<=0){
					bL = true;
					dL= x-(getBombPowerAt(i,y)+i);
				}
			}
			
			//regarder à droite pour voir s'il y a un mur ou une bombe.sinon s'�chapper de la bombe.
			i = x+1;
			while(tab[i][y] != AI_BLOCK_WALL_HARD && tab[i][y]!= AI_BLOCK_WALL_SOFT && tab[i][y]!= AI_BLOCK_BOMB){
				i++;
			}
			if(tab[i][y]== AI_BLOCK_BOMB){
				if(x-(i-getBombPowerAt(i,y))>=0){
					bR = true;
					dR = x-(i-getBombPowerAt(i,y));
				}
			}
			
			//regarder vers le haut  pour voir s'il y a un mur ou une bombe.sinon s'�chapper de la bombe.	
			i = y-1;
			while(tab[x][i] != AI_BLOCK_WALL_HARD && tab[x][i]!= AI_BLOCK_WALL_SOFT && tab[x][i]!= AI_BLOCK_BOMB){
				i--;
			}
			if(tab[x][i]== AI_BLOCK_BOMB){
				if(y-(getBombPowerAt(x,i)+i)<=0){
					bU= true;
					dU = y-(getBombPowerAt(x,i)+i);
				}
			}
			
			//regarder vers le bas  pour voir s'il y a un mur ou une bombe.sinon s'�chapper de la bombe.
			i = y+1;
			while(tab[x][i] != AI_BLOCK_WALL_HARD && tab[x][i]!= AI_BLOCK_WALL_SOFT && tab[x][i]!= AI_BLOCK_BOMB){
				i++;
			}
			if(tab[x][i]== AI_BLOCK_BOMB){
				if(y-(i-getBombPowerAt(x,i))>=0){
					bD = true;
					dD = y-(i-getBombPowerAt(x,i));
				}
			}
			
			if(bL && bU){								//s'il y a une bombe en haut à gauche
				if(operation){
					if(isMovePossible(x,y,AI_ACTION_GO_DOWN)){
						action = AI_ACTION_GO_DOWN;
					}else if(isMovePossible(x,y,AI_ACTION_GO_RIGHT))
						action = AI_ACTION_GO_RIGHT;
					
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
						
				}else{
					if(isMovePossible(x,y,AI_ACTION_GO_RIGHT))
						action = AI_ACTION_GO_RIGHT;
					else if(isMovePossible(x,y,AI_ACTION_GO_DOWN))
						action = AI_ACTION_GO_DOWN;
					
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}
				
			}else if(bR && bU){							//s'il y a une bombe en haut et à droite
				if(operation){
					if(isMovePossible(x,y,AI_ACTION_GO_DOWN))
						action = AI_ACTION_GO_DOWN;
					else if(isMovePossible(x,y,AI_ACTION_GO_LEFT ))
							action = AI_ACTION_GO_LEFT;
					
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}else{
					if(isMovePossible(x,y,AI_ACTION_GO_LEFT ))
						action = AI_ACTION_GO_LEFT;
					else if(isMovePossible(x,y,AI_ACTION_GO_DOWN))
						action = AI_ACTION_GO_DOWN;
					
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}
			}else if(bL && bD){							//S'il y a une bombe à gauche et en bas.
				if(operation){
					if(isMovePossible(x,y,AI_ACTION_GO_RIGHT))
						action = AI_ACTION_GO_RIGHT;
					else if(isMovePossible(x,y,AI_ACTION_GO_UP))
							action = AI_ACTION_GO_UP;
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}else{
					if(isMovePossible(x,y,AI_ACTION_GO_UP))
						action = AI_ACTION_GO_UP;
					else if(isMovePossible(x,y,AI_ACTION_GO_RIGHT))
						action = AI_ACTION_GO_RIGHT;
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}
			}else if(bR && bD){							//s'il y a une bombe à droite et en bas.
				if(operation){
					if(isMovePossible(x,y,AI_ACTION_GO_LEFT))
						action = AI_ACTION_GO_LEFT;
					else if(isMovePossible(x,y,AI_ACTION_GO_UP))
							action = AI_ACTION_GO_UP;
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}else{
					if(isMovePossible(x,y,AI_ACTION_GO_UP))
						action = AI_ACTION_GO_UP;
					else if(isMovePossible(x,y,AI_ACTION_GO_LEFT))
						action = AI_ACTION_GO_LEFT;
					if(lastPosition[0]!=x || lastPosition[1]!=y ){
						operation = !operation;
						lastPosition[0]=x;
						lastPosition[1]=y;
					}
				}
			}else if(bL){								//S'il y a une bombe juste à gauche.
				if(isMovePossible(x,y,AI_ACTION_GO_UP) && carte[x][y-1]>=0){
					action = AI_ACTION_GO_UP;
					right = false;
				}
				else if(isMovePossible(x,y,AI_ACTION_GO_DOWN) && carte[x][y+1]>=0){
						action = AI_ACTION_GO_DOWN;
						right = false;
					}else if(isMovePossible(x,y,AI_ACTION_GO_RIGHT) && !right){
							if(dL==0){//aller une fois vers le droit pour s'�loigner de l'effet de la bombe.
								if(carte[x+1][y]>=0)	//s'il semble ne pas etre affect� par la bombe.
									action = AI_ACTION_GO_RIGHT;
								else{	//degilse bekle
									if(carte[x+1][y]==-400){
										right = true;
										action = AI_ACTION_GO_LEFT;
									}else{
										action = AI_ACTION_DO_NOTHING;
									}
								}
							}else{
								action = AI_ACTION_GO_RIGHT;
							}
						}else{
							if(isFire(x+1,y))	//si l'obstacle est le feu attendre.
								action = AI_ACTION_DO_NOTHING;
							else{				//sinon retourner et s'�chapper.
								if(isMovePossible(x,y,AI_ACTION_GO_LEFT)){
									right = true;
									action = AI_ACTION_GO_LEFT;
								}else{
									right = false;
									action = AI_ACTION_GO_RIGHT;
								}
								
							}
								
						}
			}else if(bR){								//S'il y a une bombe juste à droite.
				if(isMovePossible(x,y,AI_ACTION_GO_UP) && carte[x][y-1]>=0){
					action = AI_ACTION_GO_UP;
				 	left = false;
			 	}
				else if(isMovePossible(x,y,AI_ACTION_GO_DOWN) && carte[x][y+1]>=0){
						action = AI_ACTION_GO_DOWN;
						left = false;
					}
					else if(isMovePossible(x,y,AI_ACTION_GO_LEFT ) && !left){
							if(dR==0){//une fois à gauche et la bombe n'affecte plus.
								if(carte[x-1][y]>=0)//s'il semble ne pas etre affect� par la bombe.
									action = AI_ACTION_GO_LEFT;
								else{
									if(carte[x-1][y]==-400){
										left = true;
										action = AI_ACTION_GO_RIGHT;
									}else{
										action = AI_ACTION_DO_NOTHING;
									}
								}
									
							}else{
								action = AI_ACTION_GO_LEFT;
							}
						}else{
							 if(isFire(x-1,y)){	//Si l'obstacle est le feu,attendre.
								 action = AI_ACTION_DO_NOTHING;
							 }else{				//sinon retourne et s'�chappe.
								 if(isMovePossible(x,y,AI_ACTION_GO_RIGHT)){
									 left = true;
									 action = AI_ACTION_GO_RIGHT;  
								 }else{
									 left = false;
									 action = AI_ACTION_GO_LEFT;  
								 }
								 
							 }
							 
						 }
			}else if(bU){								//s'il y a une bombe juste en haut.
				if(isMovePossible(x,y,AI_ACTION_GO_LEFT) && carte[x-1][y]>=0 ){
					action = AI_ACTION_GO_LEFT;
					down = false;
				}						
				else if(isMovePossible(x,y,AI_ACTION_GO_RIGHT) && carte[x+1][y]>=0 ){
						action = AI_ACTION_GO_RIGHT;
						down = false;
					}
					else if(isMovePossible(x,y,AI_ACTION_GO_DOWN) && !down){
							if(dU==0){//il s'�chappe avec un seul mouvement en bas.
								if(carte[x][y+1]>=0)//s'il semble ne pas etre affect� vers le mouvement.
									action = AI_ACTION_GO_DOWN;
								else{
									if(carte[x][y+1]==-400){//si l'action suivant est par le shrink,s'�chapper.
										down = true;
										action = AI_ACTION_GO_UP;
									}else{
										action = AI_ACTION_DO_NOTHING;
									}
								}
									
							}else{
								action = AI_ACTION_GO_DOWN;
							}
						}else{
							if(isFire(x,y+1)){	//si l'obstacle est le feu attendre.
								 action = AI_ACTION_DO_NOTHING;
							}else{				//sinon retourne et s'�chappe.
								if(isMovePossible(x,y,AI_ACTION_GO_UP)){
									down = true;
									action = AI_ACTION_GO_UP;
								}else{
									down = false;
									action = AI_ACTION_GO_DOWN;
								}
								
							}
							
						}
			}else if(bD){								//S'il y a une bombe juste en bas.
				
				if(isMovePossible(x,y,AI_ACTION_GO_LEFT) && carte[x-1][y]>=0 ){
					action = AI_ACTION_GO_LEFT;
					up = false;
				}
				else if(isMovePossible(x,y,AI_ACTION_GO_RIGHT) && carte[x+1][y]>=0 ){
						action = AI_ACTION_GO_RIGHT;
						up = false;
					}
					else if(isMovePossible(x,y,AI_ACTION_GO_UP) && !up){
							if(dD==0){//il s'�chappe avec un seul mouvement vers le haut.
								if(carte[x][y-1]>=0)//s'il semble etre ne pas affect� par la bombe.
									action = AI_ACTION_GO_UP;
								else{
									if(carte[x][y-1]==-400){
										up = true;
										action = AI_ACTION_GO_DOWN;
									}else{
										action = AI_ACTION_DO_NOTHING;
									}
								}
									
							}else{
								action = AI_ACTION_GO_UP;
							}
						}else{
							if(isFire(x,y-1)){	//Si l'obstacle est le feu attendre.
								 action = AI_ACTION_DO_NOTHING;
							}else{				//sinon retourne et s'�chappe.
								if(isMovePossible(x,y,AI_ACTION_GO_DOWN)){
									up = true;
									action = AI_ACTION_GO_DOWN;
								}else{
									up = false;
									action = AI_ACTION_GO_UP;
								}
								
							}
							
						}
			
			}
			
			if(lastPosition[0]!=x || lastPosition[1]!=y ){
				lastPosition[0]=x;
				lastPosition[1]=y;
			}


		}
				
		return action;
	}
	
	
	/**
	 * Indique si la case situ�e à la position passée en paramètre constitue un
	 * obstacle pour un personnage : bombe, feu, mur.
	 * 
	 * @param x
	 *            position à �tudier
	 * @param y
	 *            position à �tudier
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
		result = result
				|| (getTimeBeforeShrink() == -1
						&& x == getNextShrinkPosition()[0] && y == getNextShrinkPosition()[1]);
		return result;
	}
	
	/**
	 * Indique si la case situ�e à la position passée en paramètre constitue un
	 * obstacle pour un personnage : bombe, feu, mur.
	 * 
	 * @param x
	 *            position à �tudier
	 * @param y
	 *            position à �tudier
	 * @param tab
	 *            matrice de la tableau du jeu.
	 * @return vrai si la case contient un obstacle
	 */
	private boolean isObstacle(int matrix[][],int x, int y)
	{	
		boolean result = false;
		
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB;
		// feu
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_FIRE;
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
	 * Indique si le déplacement dont le code a �t� passé en paramètre est
	 * possible pour un personnage situ� en (x,y).
	 * 
	 * @param x
	 *            position du personnage
	 * @param y
	 *            position du personnage
	 * @param move
	 *            le déplacement à �tudier
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
	 * Il donne des points à chaque case accessible par 'ia.Il donne des points
	 * en considerant les bombes,le feu des bombes,les autres joueurs
	 * vivants,les murs qu'on peut détruire. Les cases qui ont des points les
	 * plus elevees sont plus attractives pour l'ia. Les cases avec des poins
	 * n�gatifs presentent un danger,ce sont les case que l'ia devrait eviter
	 * d'y aller.Il retourne l'environnement sous forme de matrice,avec chaque
	 * case possedant un point.
	 * 
	 * @param tab
	 *            le tableau du jeu.
	 * @return matrice avec des cases ayant des points.
	 */
	private int[][] pointCase(int[][] tab) {

		int[][] pCase = new int[17][15];

		// il prend les dimensions de la zone.
		int xlim = super.getZoneMatrixDimX();
		int ylim = super.getZoneMatrixDimY();

		// il copie au tableau de pCase
		for (int i = 0; i < xlim; i++)
			for (int j = 0; j < ylim; j++)
				pCase[i][j] = tab[i][j];
		

		// on donne 10 points de plus à cot� des murs destructibles.
		for (int i = 1; i < xlim; i++) { // ur l'axe x
			for (int j = 1; j < ylim; j++) {
				// S'il y a un mur destructible.On donne +10 points.
				if (tab[i][j] % 10 == AI_BLOCK_WALL_SOFT) {
					if (i<xlim-1 && tab[i + 1][j] % 10 != AI_BLOCK_BOMB
							&& tab[i + 1][j] % 10 != AI_BLOCK_FIRE
							&& tab[i + 1][j] % 10 != AI_BLOCK_UNKNOWN
							&& tab[i + 1][j] % 10 != AI_BLOCK_WALL_HARD
							&& tab[i + 1][j] % 10 != AI_BLOCK_WALL_SOFT) {
						pCase[i + 1][j] = pCase[i + 1][j] + 10;//a droite
					}
					if (i>0 && tab[i - 1][j] % 10 != AI_BLOCK_BOMB
							&& tab[i - 1][j] % 10 != AI_BLOCK_FIRE
							&& tab[i - 1][j] % 10 != AI_BLOCK_UNKNOWN
							&& tab[i - 1][j] % 10 != AI_BLOCK_WALL_HARD
							&& tab[i - 1][j] % 10 != AI_BLOCK_WALL_SOFT) {
						pCase[i - 1][j] = pCase[i - 1][j] + 10;//a gauche
					}
					if (j>0 && tab[i][j - 1] % 10 != AI_BLOCK_BOMB
							&& tab[i][j - 1] % 10 != AI_BLOCK_FIRE
							&& tab[i][j - 1] % 10 != AI_BLOCK_UNKNOWN
							&& tab[i][j - 1] % 10 != AI_BLOCK_WALL_HARD
							&& tab[i][j - 1] % 10 != AI_BLOCK_WALL_SOFT) {
						pCase[i][j - 1] = pCase[i][j - 1] + 10;//en haut
					}
					if (j<ylim-1 && tab[i][j + 1] % 10 != AI_BLOCK_BOMB
							&& tab[i][j + 1] % 10 != AI_BLOCK_FIRE
							&& tab[i][j + 1] % 10 != AI_BLOCK_UNKNOWN
							&& tab[i][j + 1] % 10 != AI_BLOCK_WALL_HARD
							&& tab[i][j + 1] % 10 != AI_BLOCK_WALL_SOFT) {
						pCase[i][j + 1] = pCase[i][j + 1] + 10;//en bas
					}
				}
				// S'il y a un bonus.On donne +20 points.
				if (tab[i][j] % 10 == AI_BLOCK_ITEM_BOMB
						|| tab[i][j] % 10 == AI_BLOCK_ITEM_FIRE) {
					pCase[i][j] = pCase[i][j] + 20;
				}
			}
		}

		// +20 points pur les cases où on peut tuer l'autre joueur en posant une
		// bombe.
		int no = super.getPlayerCount();
		int c = 0;
		while (c < no) {
			if (isPlayerAlive(c)) {// si le joueur n'est pas mort.
				int xp = getPlayerPosition(c)[0];
				int yp = getPlayerPosition(c)[1];

//on regarde si on peut poser une bombe pour tuer.On donne +20 points si c'st possible de poser une bombe.
				// Regarder vers la gauche.
				int i = xp;
				while (tab[i][yp] % 10 != AI_BLOCK_WALL_HARD
						&& tab[i][yp] % 10 != AI_BLOCK_WALL_SOFT
						&& xp - i <= bombF
						&& tab[i][yp] % 10 != AI_BLOCK_ITEM_BOMB
						&& tab[i][yp] % 10 != AI_BLOCK_ITEM_FIRE) {
					pCase[i][yp] = 20;
					i--;
				}
				// Regarder vers la droite.
				i = xp + 1;
				while (tab[i][yp] % 10 != AI_BLOCK_WALL_HARD
						&& tab[i][yp] % 10 != AI_BLOCK_WALL_SOFT
						&& i - xp <= bombF
						&& tab[i][yp] % 10 != AI_BLOCK_ITEM_BOMB
						&& tab[i][yp] % 10 != AI_BLOCK_ITEM_FIRE) {
					pCase[i][yp] = 20;
					i++;
				}

				// Regarder vers le haut.
				i = yp - 1;
				while (tab[xp][i] % 10 != AI_BLOCK_WALL_HARD
						&& tab[xp][i] % 10 != AI_BLOCK_WALL_SOFT
						&& yp - i <= bombF
						&& tab[xp][i] % 10 != AI_BLOCK_ITEM_BOMB
						&& tab[xp][i] % 10 != AI_BLOCK_ITEM_FIRE) {
					pCase[xp][i] = 20;
					i--;
				}

				// Regarder vers le bas.
				i = yp + 1;
				while (tab[xp][i] % 10 != AI_BLOCK_WALL_HARD
						&& tab[xp][i] % 10 != AI_BLOCK_WALL_SOFT
						&& i - yp <= bombF
						&& tab[xp][i] % 10 != AI_BLOCK_ITEM_BOMB
						&& tab[xp][i] % 10 != AI_BLOCK_ITEM_FIRE) {
					pCase[xp][i] = 20;
					i++;
				}
			}
			c++;
		}

		// On donne -500 points aux cases qui sont dangereux pour l'ia.
		for (int i = 1; i < xlim; i++) { // sur l'axe x.
			for (int j = 1; j < ylim; j++) { // sur l'axe y.

				if (tab[i][j] % 10 == AI_BLOCK_FIRE) {// quand il y a le feu
														// d'un bombe.
					pCase[i][j] = -500;
				}

				if (tab[i][j] % 10 == AI_BLOCK_BOMB) { // quand il y a un
														// bombe.

					pCase[i][j] = -500;


					// on donne -500 points sur les cases affect�es par la bombe.
					int k = i - 1;
					while (tab[k][j] % 10 != AI_BLOCK_WALL_HARD
							&& tab[k][j] % 10 != AI_BLOCK_WALL_SOFT
							&& tab[k][j] % 10 != AI_BLOCK_ITEM_BOMB
							&& tab[k][j] % 10 != AI_BLOCK_ITEM_FIRE
							&& i - k <= getBombPowerAt(i, j)) {

						pCase[k][j] = -500;


						k--;
					}
					// si on est la  cause d'un bonus,on donne aussi -500 points.
					if (i - k <= getBombPowerAt(i, j)
							&& (tab[k][j] % 10 == AI_BLOCK_ITEM_BOMB || tab[k][j] % 10 == AI_BLOCK_ITEM_FIRE))
						pCase[k][j] = -500;

					//-500 points vers la droite.
					k = i + 1;
					while (tab[k][j] % 10 != AI_BLOCK_WALL_HARD
							&& tab[k][j] % 10 != AI_BLOCK_WALL_SOFT
							&& tab[k][j] % 10 != AI_BLOCK_ITEM_BOMB
							&& tab[k][j] % 10 != AI_BLOCK_ITEM_FIRE
							&& k - i <= getBombPowerAt(i, j)) {

						pCase[k][j] = -500;
					
						k++;
					}
					// si on est la  cause d'un bonus,on donne aussi -500 points.
					if (k - i <= getBombPowerAt(i, j)
							&& (tab[k][j] % 10 == AI_BLOCK_ITEM_BOMB || tab[k][j] % 10 == AI_BLOCK_ITEM_FIRE))
						pCase[k][j] = -500;

					// -500 points vers le bas.
					k = j + 1;
					while (tab[i][k] % 10 != AI_BLOCK_WALL_HARD
							&& tab[i][k] % 10 != AI_BLOCK_WALL_SOFT
							&& tab[i][k] % 10 != AI_BLOCK_ITEM_BOMB
							&& tab[i][k] % 10 != AI_BLOCK_ITEM_FIRE
							&& k - j <= getBombPowerAt(i, j)) {

						pCase[i][k] = -500;
					
						k++;
					}
					// si on est la  cause d'un bonus,on donne aussi -500 points.
					if (k - j <= getBombPowerAt(i, j)
							&& (tab[i][k] % 10 == AI_BLOCK_ITEM_BOMB && tab[i][k] % 10 == AI_BLOCK_ITEM_FIRE))
						pCase[i][k] = -500;

					// -500 points vers le haut.
					k = j - 1;
					while (tab[i][k] % 10 != AI_BLOCK_WALL_HARD
							&& tab[i][k] % 10 != AI_BLOCK_WALL_SOFT
							&& tab[i][k] % 10 != AI_BLOCK_ITEM_BOMB
							&& tab[i][k] % 10 != AI_BLOCK_ITEM_FIRE
							&& j - k <= getBombPowerAt(i, j)) {

						pCase[i][k] = -500;
					
						k--;
					}
					// si on est la  cause d'un bonus,on donne aussi -500 points.
					if (j - k <= getBombPowerAt(i, j)
							&& (tab[i][k] % 10 == AI_BLOCK_ITEM_BOMB && tab[i][k] % 10 == AI_BLOCK_ITEM_FIRE))
						pCase[i][k] = -500;

				}
			}
		}

		// shrink
		if (getTimeBeforeShrink() < 0) {

			
			for (int i = 1 + perimetre; i < xlim - perimetre - 1; i++) { //sur l'axe x
														
				for (int j = 1 + perimetre; j < ylim - perimetre - 1; j++) { //sur l'axe y
					if (tab[i][j] != AI_BLOCK_WALL_HARD
							&& tab[i][j] != AI_BLOCK_WALL_SOFT) {
						pCase[i][j] = pCase[i][j] + 10;
					}
				}
			}

			for (int i = 0; i < 5; i++) {
				pCase[snake[i][0]][snake[i][1]] = -400;
			}

		}

		return pCase;
	}

	
	/**
	 *Il determine une partie de la matrice que l'ia peut arriver sans détruire un mur.
	 *Fonction travaille r�cursivement.Il commence de la case dont les coordoonn�es sont passés en parametre
	 *et puis determine quels sont les cases que l'ia peut aller sans détruire un mur.La fonction 
	 *met des -1 sur les cases qui ne sont pas accessibles par l'ia.Les cases accesibles par l'ia ont les memes 
	 *valeurs que getZoneMatrix() retourne.
	 * @param tab
	 *            le tableau du jeu.
	 * @param x
	 *           le coordonné de x.
	 * @param y
	 *            le coordonné de y.
	 */
	public void oCarte(int[][] tab,int x,int y){
		if(tab[x][y]%10!=AI_BLOCK_WALL_HARD && tab[x][y]%10!=AI_BLOCK_WALL_SOFT && tab[x][y]%10!=AI_BLOCK_BOMB){
			carte[x][y] = tab[x][y];
			if(x+1 <= 16 && carte[x+1][y]== AI_BLOCK_UNKNOWN)
				oCarte(tab,x+1,y);
			if(x-1 >= 0 && carte[x-1][y]== AI_BLOCK_UNKNOWN)
				oCarte(tab,x-1,y);
			if(y+1 <= 14 && carte[x][y+1]== AI_BLOCK_UNKNOWN)
				oCarte(tab,x,y+1);
			if(y-1 >= 0 && carte[x][y-1]== AI_BLOCK_UNKNOWN)
				oCarte(tab,x,y-1);
		}else{
			carte[x][y] = tab[x][y]%10;
		}
		
		
		
	}

	/**
	 *Il determine une cible avec la fonction findGoal.Puis il determine un 
	 *chemin pour y arriver,avec la fonction findPath.
	 *Il fait en suite un mouvement pour bouger vers les cases qui se trouvent sur le path.
	 * @param x
	 *           le coordonné de x de l'ia.
	 * @param y
	 *            le coordonné de y de l'ia.
	 */
	public int move(int x,int y){
		int action = AI_ACTION_DO_NOTHING;
		if(lastPos[0]==x && lastPos[0]==y && lastMo!= AI_ACTION_DO_NOTHING){
			action = lastMo;
		}else{
			Noeud n = this.findGoal();//on trouve le cible
			findPath(carte, x, y, n);//on trouve le chemin
			//si le chemin a une longeur plus que 2 cases.
			if(path.size()>1){
				int sX = path.get(path.size()-2).getX();//le coordonne x du prochain case.
				int sY = path.get(path.size()-2).getY();//le coordonne y du prochain case.
			
			//les 4 mouvements qu'il peut faire pour arriver au case suivant.
				if(x+1==sX && y==sY)
					action = AI_ACTION_GO_RIGHT;
				if(x-1==sX && y==sY)
					action = AI_ACTION_GO_LEFT;
				if(y+1==sY && x==sX)
					action = AI_ACTION_GO_DOWN;
				if(y-1==sY && x==sX)
					action = AI_ACTION_GO_UP;
			lastPos[0]=x;
				lastPos[1]=y;
				lastMo = action;
			}
			//si le path possede une seule case.(sur le cible).
			if(path.size()==1){
				int sX = path.get(0).getX();
				int sY = path.get(0).getY();
			
				if(x+1==sX && y==sY)
					action = AI_ACTION_GO_RIGHT;
				if(x-1==sX && y==sY)
					action = AI_ACTION_GO_LEFT;
				if(y+1==sY && x==sX)
					action = AI_ACTION_GO_DOWN;
				if(y-1==sY && x==sX)
					action = AI_ACTION_GO_UP;
				lastPos[0]=x;
				lastPos[1]=y;
				lastMo = action;
			}
		}
		
		return action;
	}
	/**
	 *Il determine le chemin le plus court au cible.Il utilise l'algorithme de A �toile.
	 *(cf. : http://fr.wikipedia.org/wiki/Algorithme_A*)
	 * @param x
	 *           le coordonné de x de l'ia.
	 * @param y
	 *            le coordonné de y de l'ia.
	 * @param tab le tableau du jeu.
	 * @param goal la case qu'on veut y arriver.
	 */
	public void findPath(int[][] tab,int x,int y,Noeud goal)
	{	//� chaque appel de la fonction,on efface les anciens valeurs.
		path=new LinkedList<Noeud>();
		
		Noeud courant=new Noeud(x,y,tab[x][y],0);//case où se trouve ia.
		Tree tree=new Tree(courant);//on cree une arbre pour voir le path.
		NoeudComparator comparator=new NoeudComparator(goal);//utilise pour l'algorithme Aetoile
		PriorityQueue<Noeud> frange = new PriorityQueue<Noeud>(1,comparator);//les elements sont inseres en respectant l'ordre du cout et de l'heuristique.
		LinkedList<Noeud> open=new LinkedList<Noeud>();//liste des elements qu'on regarde.
		LinkedList<Noeud> closed=new LinkedList<Noeud>();//liste des elements qu'on a dej� regardé.
		Noeud temp=new Noeud();
	
		Noeud solution=new Noeud();
		solution=null;
		frange.offer(courant);
		open.add(courant);
		//jusqu'a trouver la solution mais attention si la frange est vide,on s'arrete sans trouver la solution.
		while((solution == null) && (!frange.isEmpty())){  

			temp=frange.poll();//on enleve de la frange
			open.remove(open.indexOf(temp));//on enleve de la liste open.
			closed.add(temp);//on met au liste des elements deja regardés.
			
			if(temp.equals(goal)){
				solution=temp;
			}else{ 	
				Noeud up=null;
				Noeud down=null;
				Noeud right=null;
				Noeud left=null;
				//la case qui est en haut de lui.
				if((temp.getY()-1>=1) && (tab[temp.getX()][temp.getY()-1]%10!=AI_BLOCK_FIRE) && (tab[temp.getX()][temp.getY()-1] >=0)  && (tab[temp.getX()][temp.getY()-1]%10!=AI_BLOCK_BOMB)&&(tab[temp.getX()][temp.getY()-1]%10!=AI_BLOCK_WALL_HARD)&&(tab[temp.getX()][temp.getY()-1]%10!=AI_BLOCK_WALL_SOFT)&&(tab[temp.getX()][temp.getY()-1]!=AI_BLOCK_UNKNOWN)) {	
					up=new Noeud(temp.getX(),temp.getY()-1,tab[temp.getX()][temp.getY()-1],temp.getCout()+1);
					if(!open.contains(up)  && !closed.contains(up)){
						open.add(up);
						tree.addNoeud(temp,up);
						frange.offer(up);
					}				
				
				}
			//la case qui est en bas de lui.
				if((temp.getY()+1<=13)&& (tab[temp.getX()][temp.getY()+1]%10!=AI_BLOCK_FIRE) && (tab[temp.getX()][temp.getY()+1] >=0) && (tab[temp.getX()][temp.getY()+1]%10!=AI_BLOCK_BOMB)&&(tab[temp.getX()][temp.getY()+1]%10!=AI_BLOCK_WALL_HARD)&&(tab[temp.getX()][temp.getY()+1]%10!=AI_BLOCK_WALL_SOFT)&&(tab[temp.getX()][temp.getY()+1]!=AI_BLOCK_UNKNOWN)) {
					down=new Noeud(temp.getX(),temp.getY()+1,tab[temp.getX()][temp.getY()+1],temp.getCout()+1);
					if(!open.contains(down)  && !closed.contains(down)){
						open.add(down);
						tree.addNoeud(temp,down);
						frange.offer(down);
					}	
				 
				 }
			//la case qui est à droite de lui.
				if((temp.getX()+1<=15)&& (tab[temp.getX()+1][temp.getY()]%10!=AI_BLOCK_FIRE) && (tab[temp.getX()+1][temp.getY()] >=0) && (tab[temp.getX()+1][temp.getY()]%10!=AI_BLOCK_BOMB)&&(tab[temp.getX()+1][temp.getY()]%10!=AI_BLOCK_WALL_HARD)&&(tab[temp.getX()+1][temp.getY()]%10!=AI_BLOCK_WALL_SOFT) && (tab[temp.getX()+1][temp.getY()]!=AI_BLOCK_UNKNOWN)){
					right=new Noeud(temp.getX()+1,temp.getY(),tab[temp.getX()+1][temp.getY()],temp.getCout()+1);
					if(!open.contains(right)  && !closed.contains(right)){
						open.add(right);
						tree.addNoeud(temp,right);
						frange.offer(right);
					}	
				}
			//la case qui est en à gauche de lui.
				if((temp.getX()-1>=1)&& (tab[temp.getX()-1][temp.getY()]%10!=AI_BLOCK_FIRE) && (tab[temp.getX()-1][temp.getY()] >=0)  && (tab[temp.getX()-1][temp.getY()]%10!=AI_BLOCK_BOMB)&&(tab[temp.getX()-1][temp.getY()]%10!=AI_BLOCK_WALL_HARD)&&(tab[temp.getX()-1][temp.getY()]%10!=AI_BLOCK_WALL_SOFT)&& (tab[temp.getX()-1][temp.getY()]!=AI_BLOCK_UNKNOWN)) {	
					left=new Noeud(temp.getX()-1,temp.getY(),tab[temp.getX()-1][temp.getY()],temp.getCout()+1);	
					if(!open.contains(left)  && !closed.contains(left)){	
						open.add(left);
						tree.addNoeud(temp,left);
						frange.offer(left);
					}	
				}
			}// fin de l'else
		
			
		}// fin de while

		if(solution!=null)//si on a trouv� la solution sans finir tous les elements de la frange.
			path=tree.getPath(solution);

		frange=null;//on vide la frange.
		tree=null;//on vide l'arbre.

	}

	/**
	 * Il determine la case qu'il faut aller.Pour l'ia la case qui a le meilleur
	 * point est la case la plus attractive.En cas de l'�galit�,il choisit la
	 * case qui semble etre plus proche à lui.
	 * 
	 * @reurn goal la case cible.
	 * 
	 */
	public Noeud findGoal() {

		int x = getOwnPosition()[0];

		int y = getOwnPosition()[1];

		int max = 0;

		int maxX = 0;

		int maxY = 0;
		// suivant les cases.Il trouve le max.
		for (int i = 0; i < carte.length; i++)

			for (int j = 0; j < carte[0].length; j++)

				if (carte[i][j] > max) {
					max =

					carte[i][j];
					maxX = i;

					maxY = j;

				}

				else if (carte[i][j] == max) {

					// en cas d'�galit�,il compare les distances de Manhattan.
					int d1 = distance(x, y, maxX, maxY);

					int d2 = distance(x, y, i, j);

					if (d2 < d1) {
						max =

						carte[i][j];
						maxX = i;

						maxY = j;
					}

				}

		goal = new Noeud(maxX, maxY, max);

		return goal;

	}


	/** 

	* Calcule et renvoie la distance de Manhattan 

	* (cf. : http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29) 

	* entre le point de coordonnées (x1,y1) et celui de coordonnées (x2,y2). 

	* @param x1 position du premier point 

	* @param y1 position du premier point 

	* @param x2 position du second point 

	* @param y2 position du second point 

	* @return la distance de Manhattan entre ces deux points 

	*/ 
	private int distance(int x1,int y1,int x2,int y2) 
	{ 

	int result = 0; 
	result = result + Math.abs(x1-x2);

	result = result + Math.abs(y1-y2);


	return result; 
	}
	/** 

	* Controle s'il y a du feu sur la case dont les coordonnées sont 
	* passés en parametre.Retourne vrai s'il y a du feu,false sinon.

	* @param x coordonné x.
	* @param y coordonné y. 


	* @return vrai si la case est le feu.

	*/	
	private boolean isFire(int x,int y){
		int tab[][] = getZoneMatrix();
		if(tab[x][y]==AI_BLOCK_FIRE){
			return true;
		}else{
			return false;
		}
	}
	/** 

	* Controle s'il peut se sauver de la bombe qu'il pense poser.
	* Ca sert à faire une simulation de la situation avant de poser la bombe.
	* @param tab le tableau du jeu.
	* @param x le coordonne x de l'ia.
	* @param y le coordonné y de l'ia.
	* @return true s'il peut se sauver de la bombe qu'il va pauser.
	*/ 	
	private boolean canEscape(int[][] tab,int x,int y){
		boolean result=false;
		
		boolean l=false;
		boolean r=false;
		boolean d=false;
		boolean u=false;
		
		//peut-il se sauver en allant juste à gauche.
		int i = x-1;
		while(tab[i][y] != AI_BLOCK_WALL_HARD && tab[i][y]!= AI_BLOCK_WALL_SOFT){
			i--;
		}
		
		if((getBombPowerAt(x,y)+i)<x){
			l = true;
		}
		
		//peut-il se sauver en allant juste à droite.
		i = x+1;
		while(tab[i][y] != AI_BLOCK_WALL_HARD && tab[i][y]!= AI_BLOCK_WALL_SOFT){
			i++;
		}
		
		if(x+getBombPowerAt(x,y)<i){
			r = true;
		}
		
		//peut-il se sauver en allant juste en haut.
		i = y-1;
		while(tab[x][i] != AI_BLOCK_WALL_HARD && tab[x][i]!= AI_BLOCK_WALL_SOFT){
			i--;
		}
		if(getBombPowerAt(x,y)+i<y){
			u = true;
		}
		
		//peut-il se sauver en allant juste en bas.
		i = y+1;
		while(tab[x][i] != AI_BLOCK_WALL_HARD && tab[x][i]!= AI_BLOCK_WALL_SOFT){
			i++;
		}
		if(y+getBombPowerAt(x,y)<i){
			d = true;
		}
		
		
		if(!isObstacle(tab,x,y-1) && ((!isObstacle(tab,x-1,y-1)&&carte[x-1][y-1]>=0) || (!isObstacle(tab,x+1,y-1)&&carte[x+1][y-1]>=0)) ){//s'il peut aller en haut puis à gauche ou à droite.
				result = true;
			}else if(!isObstacle(tab,x-1,y) && ((!isObstacle(tab,x-1,y+1)&&carte[x-1][y+1]>=0) || (!isObstacle(tab,x-1,y-1) && carte[x-1][y-1]>=0)) ){
					result = true;
				}else if(!isObstacle(tab,x,y+1) && ((!isObstacle(tab,x-1,y+1)&&carte[x-1][y+1]>=0) || (!isObstacle(tab,x+1,y+1) && carte[x+1][y+1]>=0)) ){
						result = true;
					}else if(!isObstacle(tab,x+1,y) && ((!isObstacle(tab,x+1,y+1) && carte[x+1][y+1]>=0)|| (!isObstacle(tab,x+1,y-1)&& carte[x+1][y-1]>=0)) ){
							result = true;
						}else if(!isObstacle(tab,x,y-1)&& !isObstacle(tab,x,y-2) && ((!isObstacle(tab,x-1,y-2)&& carte[x-1][y-2]>=0)|| (!isObstacle(tab,x+1,y-2) && carte[x+1][y-2]>=0 ))){//S'il peut aller deux fois en haut puis à gauche ou à droite.
							result = true;
							}else if(!isObstacle(tab,x-1,y) && !isObstacle(tab,x-2,y) &&((!isObstacle(tab,x-2,y+1) && carte[x-2][y+1]>=0)|| (!isObstacle(tab,x-2,y-1))&& carte[x-2][y-1]>=0)){
								result = true;
								}else if(!isObstacle(tab,x,y+1) && !isObstacle(tab,x,y+2) &&((!isObstacle(tab,x-1,y+2)&& carte[x-1][y+2]>=0)|| (!isObstacle(tab,x+1,y+2) && carte[x+1][y+2]>=0 ))){
									result = true;
									}else if(!isObstacle(tab,x+1,y) && !isObstacle(tab,x+2,y) &&((!isObstacle(tab,x+2,y+1) && carte[x+2][y+1]>=0 )|| (!isObstacle(tab,x+2,y-1) && carte[x+2][y-1]>=0 ))){
										result = true;
										}else if(!isObstacle(tab,x,y-1)&& !isObstacle(tab,x,y-2) && !isObstacle(tab,x,y-3) && (u || (!isObstacle(tab,x-1,y-3)&& carte[x-1][y-3]>=0)|| (!isObstacle(tab,x+1,y-3) && carte[x+1][y-3]>=0 ))){//S'il peut aller 3 fois en haut puis à gauche ou à droite.
												result = true;
											}else if(!isObstacle(tab,x-1,y) && !isObstacle(tab,x-2,y) && !isObstacle(tab,x-3,y) &&(l || (!isObstacle(tab,x-3,y+1) && carte[x-3][y+1]>=0)|| (!isObstacle(tab,x-3,y-1))&& carte[x-3][y-1]>=0)){
												result = true;
												}else if(!isObstacle(tab,x,y+1) && !isObstacle(tab,x,y+2) && !isObstacle(tab,x,y+3) &&(d || (!isObstacle(tab,x-1,y+3)&& carte[x-1][y+3]>=0)|| (!isObstacle(tab,x+1,y+3) && carte[x+1][y+3]>=0 ))){
														result = true;
													}else if(!isObstacle(tab,x+1,y) && !isObstacle(tab,x+2,y) && !isObstacle(tab,x+3,y) &&(r || (!isObstacle(tab,x+3,y+1) && carte[x+3][y+1]>=0 )|| (!isObstacle(tab,x+3,y-1) && carte[x+3][y-1]>=0 ))){
															result = true;
													}
		return result;
	}

	
	/**
	 * 
	 * Fait bouger le serpent de shrink.On pense qu'il y a un serpent de cinq
	 * cases dont la queue est la derniere case du shrink.Donc on fait bouger le
	 * serpent avec le shrink.
	 */ 	
	public void shrink(){
		if(snake[3][0]==snake[4][0]){					//mouvement sur l'axe y
			if(snake[3][1]<snake[4][1]){				//vers le bas.
				if(snake[4][1]+1 < getZoneMatrixDimY()-perimetre){
					add(snake[4][0],snake[4][1]+1);
				}else{									
					add(snake[4][0]-1,snake[4][1]);
				}
				
			}else{										//mouvement sur l'axe x.
				if(snake[4][1]-1>=perimetre){
					add(snake[4][0],snake[4][1]-1);
				}else{
					add(snake[4][0]+1,snake[4][1]);
				}
			}
			
		}else{						
			if(snake[3][1]==snake[4][1]){				//mouvement sur l'axe x.
				if(snake[3][0]<snake[4][0]){			//vers la droite.
					if(snake[4][0]+1 < getZoneMatrixDimX()-perimetre){
						add(snake[4][0]+1,snake[4][1]);
					}else{
						add(snake[4][0],snake[4][1]+1);
					}
				}else{									//vers le gauche.
					if(snake[4][0]-1>=perimetre){
						add(snake[4][0]-1,snake[4][1]);
					}else{
						add(snake[4][0],snake[4][1]-1);
					}
				}
			}
		}
		
		//update le perimetre.
		if(snake[4][0]+1==snake[4][1] && snake[4][1]<=7)
			perimetre = snake[4][1];
	}
	
	/**
	 * on ajoute la case passée en parametre au serpent de shrink.
	 * 
	 * @param x
	 *            la coordonné x.
	 * @param y
	 *            la coordonné y.
	 */
	public void add(int x, int y) {

		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 2; j++) {
				snake[i][j] = snake[i + 1][j];
			}
		snake[4][0] = x;
		snake[4][1] = y;
	}
	/**
	 * renvoie la position de la case(les coordnn�es sont passés en parametre)
	 * sur le serpent du shrink. Le r�sultat peut etre de -1 à 4.
	 * 
	 * @param x
	 *            le coordonné x
	 * @param y
	 *            le coordonné y
	 * @return la position de la case sur le serpent, -1 si la case n'est pas
	 *         sur le serpent.
	 */	
	public int posShrink(int x, int y) {
		int result = -1;
		int i = 0;
		while (i < 5 && (snake[i][0] != x || snake[i][1] != y)) {
			i++;
		}
		if (i < 5) {
			result = i;
		}

		return result;
	}
	

}
