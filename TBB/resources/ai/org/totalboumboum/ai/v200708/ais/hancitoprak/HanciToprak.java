package org.totalboumboum.ai.v200708.ais.hancitoprak;

import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Aslıhan Hanci
 * @author Emine Canan Toprak
 *
 */
@SuppressWarnings("deprecation")
public class HanciToprak extends ArtificialIntelligence {
	private static final long serialVersionUID = 1L;
	/** le dernier déplacement effectué */
	private Integer lastMove = null;
//	private final int DELAY = 100;

//	private int artma = 0;
    int temp_putbomb = 0;

	/**
	 * Constructeur.
	 */
	public HanciToprak() {
		super("HanciToprk");
	}

	int index;
//	private int durma = 0;
	private int durma_art = 0;
//	private int yy = 0;

	private int x1 = 0;
	private int y1 = 0;
	private boolean condition = true;
	private boolean condition_1 = true;

	private Integer lastMove_ = null;

//	private int bomb_portee = 0;
//	private int bombakoydum = 0;

	private int[][] costMatrix = null;
//	private int[][] costMatrix_ = null;

	private int hasno_bomb = 0;
	private int danger=0;
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	/**
	 * la methode Contrôle le mouvement du personnage pour chaque itération 
	*/

	@Override
	public Integer processAction() throws Exception
	{	Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		
		if(firstTime)
			firstTime = false;
		else
		{	
		index = 0;

		// on détermine la position actuelle
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		
        // on détermine les déplacements possibles
		Vector<Integer> possibleMoves = getPossibleMoves(x, y);

//		costMatrix_ = costMatrix;
		costMatrix = getZoneMatrix();//initialisation du costMatrix(matrix à utiliser pour prendre décision)
		
		Decision();//pour chaque itération on renouvele le costMatrix (les couts)
		

		if (condition_1) {
			condition_1 = false;
			if (lastMove == null) {
				index = (int) (Math.random() * (possibleMoves.size()));
				result = possibleMoves.get(index);
				lastMove = result;
			}

			else if (lastMove == ArtificialIntelligence.AI_ACTION_PUT_BOMB
					|| (temp_putbomb > 0 && temp_putbomb < 8)) {

				
				Vector<Integer> temp = possibleMoves;
				
				for(int i=0;i<temp.size();i++)
					
				
				result = escapeFromBomb(x, y, temp);
				
				
				lastMove = result;

				if (temp_putbomb == 8) {
					
					temp_putbomb = 0;
//					bombakoydum = 1;

				}

			}

			else {
				
				if (possibleMoves.size() == 1) {

					
					Vector<Integer> tampon =escapeFromBombPower(x, y,possibleMoves);
					if (getOwnBombCount() > 0 && tampon.size()!=0) {
						
						
						//mais si on a seulement un case a deplacer il ne faut pas poser un bombe car a ce condition la on va mouirir a cause de notre bombe.
						//donc il faut calculer les cases adjacante pour nest pas etre un fire.
						int temp=possibleMoves.firstElement();
						
						Vector<Integer> temp_moves=new Vector<Integer>();
						
						int[] destination;
						
						for (int move = AI_ACTION_GO_UP; move <= AI_ACTION_GO_RIGHT; move++){
							if(temp!=move){
								temp_moves.add(move);
								
								destination=resultMovePosition(x, y, move);
								
								
								if(costMatrix[destination[0]][destination[1]]==300){
									
									danger=1;
									break;
									}					
							}
						}
						if(danger==0){
							
							result = ArtificialIntelligence.AI_ACTION_PUT_BOMB;
							lastMove_ = lastMove;
							lastMove = result;
						}
						else{
							
							danger=0;
							result=tampon.firstElement();
							lastMove=result;
						}
						
							
						
					} 
					else if(tampon.size()==0){
						result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						hasno_bomb = 1;
					}
					else {
						
						int move[] = resultMovePosition(x, y, possibleMoves.firstElement());

						if (costMatrix[x][y] >= costMatrix[move[0]][move[1]]){
							result = possibleMoves.firstElement();
							lastMove=result;
						}
						
						else{
							result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
							//si result est do_nothing,on ne doit pas considerer le mouvement plusieurs fois d'un case a un case 
							hasno_bomb = 1;
							
						}

					}

				} else {
					
				
					possibleMoves = escapeFromBombPower(x, y, possibleMoves);
					if (possibleMoves.size() == 0) {
						
						result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						hasno_bomb = 1;
						//lastMove = result;
					} 
					 else {
						
						if (possibleMoves.contains(lastMove)) {
							
							int[] lastMove_dest;
							lastMove_dest=resultMovePosition(x, y, lastMove);

							if (costMatrix[x][y] >= costMatrix[lastMove_dest[0]][lastMove_dest[1]]){
								result =lastMove;
								lastMove=result;
							}
							else{
								Vector<Integer> tampon =escapeFromBombPower(x, y,possibleMoves);
								if(tampon.size()==0){
									result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
									hasno_bomb = 1;
									
								}else{
									index = (int) (Math.random() * (tampon.size()));
									result = tampon.get(index);
									lastMove = result;
								}
								
							}
							}
						

						//}
						else {
							
							index = (int) (Math.random() * (possibleMoves
									.size()));
							
							result = possibleMoves.get(index);
							lastMove = result;

						}

					}
					

					}
				}
			}
		if (condition) {
			
			condition = false;
			

			
			x1 = x;
			
			y1 = y;
			

		}
		if (lastMove == ArtificialIntelligence.AI_ACTION_PUT_BOMB
				|| hasno_bomb == 1) {
			
			hasno_bomb = 0;
			condition = true;
			condition_1 = true;
		}

		else {
			
			result = hareket(result, x, y);
		}
		}
		
		return result;
	}

	private Integer hareket(Integer result, int x, int y) {

		
		if (x1 != x || y1 != y) {
			
			condition = true;
			condition_1 = true;
			if (durma_art == 18)
//				yy = 81
				;
			
		} else {
			

			int[] est_bombe=resultMovePosition(x, y, lastMove);
			
			if(getZoneMatrix()[est_bombe[0]][est_bombe[1]]==400 || getZoneMatrix()[est_bombe[0]][est_bombe[1]]==4){
				
				Vector<Integer> pm = getPossibleMoves(x, y);
				if(pm.size()==0)
					result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				else{
					index = (int) (Math.random() * (pm.size()));				
					result = pm.get(index);
					lastMove = result;
					condition = true;
					condition_1 = true;		
				}
						
			}
			else{
				
				result = lastMove;
			}
			
		}

		return result;

	}
	
	/**
	 * la methode à utiliser pour faire AI prendre décision 
	 * si il ya un danger du fire.
	 * Si le resultat d'un de ses mouvements possibles est un domaine dangereux
	 * la methode le sors de ses mouvements possibles
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param possiblemoves du personnages
	 * @return	au nouveau les moves possibles du peronnage. 
	 */

	private Vector<Integer> escapeFromBombPower(int x, int y,
			Vector<Integer> possibleMoves) {

		for (int i = 0; i < possibleMoves.size(); i++) {
			if (possibleMoves.get(i) == 1) {
				if (costMatrix[x][y - 1] == 300)
					possibleMoves.remove(i);
			} else if (possibleMoves.get(i) == 2) {
				if (costMatrix[x][y + 1] == 300)
					possibleMoves.remove(i);
			} else if (possibleMoves.get(i) == 3) {
				if (costMatrix[x - 1][y] == 300 )
					possibleMoves.remove(i);
			} else if (possibleMoves.get(i) == 4) {
				if (costMatrix[x + 1][y] == 300)
					possibleMoves.remove(i);
			}

		}
		return possibleMoves;

	}

	/**
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move le mouvement prochain du personnage
	 * @return les coordonnees de la case du personnage apres qu'il a realisé son deplacement prochain
	 */
	private int[] resultMovePosition(int x, int y, int move) {
		int[] result = { 5, 5 };

	    if (move == 1) {
			
			result[0] = x;
			result[1] = y - 1;
			
		} else if (move == 2) {
			
			result[0] = x;
			result[1] = y + 1;
		} else if (move == 3) {
		
			result[0] = x - 1;
			result[1] = y;
		} else if (move == 4) {
			
			result[0] = x + 1;
			result[1] = y;
		}
		
		return result;

	}
	
	/**
	 * la methode à utiliser pour faire AI prendre decision 
	 * si il ya un danger du bombe
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param possiblemoves du personnages apres qu'il a mis du bombe
	 * @return	vrai si ce déplacement est possible
	 */
	private Integer escapeFromBomb(int x, int y, Vector<Integer> temp)
			throws Exception {
	
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
//		yy = 0;
		Integer lastMove_ters = null;
		lastMove_ters = opposite_dirextion(lastMove_);
		
//		for (int m = 0; m < temp.size(); m++)
//			System.out.println(temp.get(m));
		
		if (lastMove_ters == 1 || lastMove_ters == 2) {
			for (int i = 0; i < temp.size(); i++) {
				if (temp.get(i) == 3 || temp.get(i) == 4) {
					
					result = temp.get(i);
					temp_putbomb = 7;//il n'y a aucun danger (il peut continuer son deplacement normal)
					durma_art = 18;
//					yy = 1;
					break;
				} else {
					
					result = lastMove_ters;
				}
			}
		} else if (lastMove_ters == 3 || lastMove_ters == 4) {
			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j) == 1 || temp.get(j) == 2) {
					
					result = temp.get(j);
					temp_putbomb = 7;//il n'y a aucun danger (il peut continuer son deplacement normal)
					durma_art = 18;
					break;
				} else {
					
					result = lastMove_ters;
				}
			}
		}
		
		temp_putbomb++;

		return result;

	}

	/**
	 * Renvoie la direction opposé du dernier mouvement
	 * 
	 * @param lastmove direction du dernier mouvement 
	 * @return	direction opposé du dernier movement
	 */
	private Integer opposite_dirextion(Integer lastmove) {

		
		Integer lastMove_ters = null;
		if (lastmove == 1)//si le dernier mouvement est AI_ACTION_GO_UP
			lastMove_ters = 2;
		else if (lastmove == 2)//si le dernier mouvement est AI_ACTION_GO_DOWN
			lastMove_ters = 1;
		else if (lastmove == 3)//si le dernier mouvement est AI_ACTION_GO_LEFT
			lastMove_ters = 4;
		else if (lastmove == 4)//si le dernier mouvement est AI_ACTION_GO_RIGHT
			lastMove_ters = 3;

		return lastMove_ters;

	}
	
	/**
	 * Renvoie la liste de tous les déplacements possibles
	 * pour un personnage situé à la position (x,y)
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @return	la liste des déplacements possibles
	 */

	private Vector<Integer> getPossibleMoves(int x, int y) {
		Vector<Integer> result = new Vector<Integer>();
		for (int move = AI_ACTION_GO_UP; move <= AI_ACTION_GO_RIGHT; move++)
			if (isMovePossible(x, y, move))
				result.add(move);
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
	 * Indique si la case située à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à étudier
	 * @param y	position à étudier
	 * @return	vrai si la case contient un obstacle
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
		if(getTimeBeforeShrink()<10000)//on ne controle pas le shrink avant 10 seconds de son commencement
			result = result
			|| (x == getNextShrinkPosition()[0] && y == getNextShrinkPosition()[1]);
		
		return result;
	}

	
	/**
	 * controle les cases où se trouvent les bombes.
	 * Initialise les  couts
	 * grâce auxquels AI prends décision. 
	 * 
	 */
	private void Decision() {//  initialisation de costMatrix pour prendre decision 
        boolean notendleftx1=true;
        boolean notendleftx2=true;
        boolean notendrightx1=true;
        boolean notendrightx2=true;
        boolean notendtopy1=true;
        boolean notendtopy2=true;
        boolean notendbottomy1=true;
        boolean notendbottomy2=true;
        
        boolean controleleftx=true;
        boolean controlerightx=true;
        boolean controletopy=true;
        boolean controlebottomy=true;
        
        
		for (int k = 1; k < getZoneMatrixDimX(); k++) {
			for (int j = 1; j < getZoneMatrixDimY(); j++) {
				if (costMatrix[k][j] == 4) {
					costMatrix[k][j] = 400;
					int portee = getBombPowerAt(k, j);
					for (int i = 1; i <= portee; i++) {
						if (k - i > 0) {//controle  cote gauche de costMatrix
							if((getZoneMatrix()[k-i][j]==0 ||getZoneMatrix()[k-i][j]==5 || getZoneMatrix()[k-i][j]==6)&&controleleftx){
							costMatrix[k - i][j] = 300;
							}
							else if(getZoneMatrix()[k-i][j]==1 && notendleftx1){
								costMatrix[k - i][j] = 300;
								notendleftx1 = false;
								controleleftx=false;
								
							}
							else if(getZoneMatrix()[k-i][j]==2 && notendleftx2){
								costMatrix[k - i][j] = 300;
								notendleftx2 = false;
								controleleftx=false;
							}
						}
						if (k + i < 16){//controle  cote droite de costMatrix
							if((getZoneMatrix()[k+i][j]==0 || getZoneMatrix()[k+i][j]==5 || getZoneMatrix()[k+i][j]==6) && controlerightx)
							costMatrix[k + i][j] = 300;
							else if(getZoneMatrix()[k+i][j]==1 && notendrightx1){
								costMatrix[k + i][j] = 300;
								notendrightx1 = false;
								controlerightx=false;
							}
							else if(getZoneMatrix()[k+i][j]==2 && notendrightx2){
									costMatrix[k + i][j] = 300;
									notendrightx2 = false;
									controlerightx=false;
								}	
							}
						if (j - i > 0) {//controle le dessus de getzonematrix
							if((getZoneMatrix()[k][j-i]==0 || getZoneMatrix()[k][j-i]==5 ||getZoneMatrix()[k][j-i]==6) && controletopy)
								costMatrix[k][j-i] = 300;
							else if(getZoneMatrix()[k][j-i]==1 && notendtopy1){
								costMatrix[k][j-i] = 300;
								notendtopy1 = false;
								controletopy = false;
							}
							else if(getZoneMatrix()[k][j-i]==2 && notendtopy2){
									costMatrix[k][j-i] = 300;
									notendtopy2 = false;
									controletopy = false;
								}	
							
							
							}
						if (j + i < 14) {//controle le dessous de getzonematrix
							costMatrix[k][j + i] = 300;
							
							if((getZoneMatrix()[k][j+i]==0 || getZoneMatrix()[k][j+i]==5 || getZoneMatrix()[k][j+i]==6) && controlebottomy)
								costMatrix[k][j+i] = 300;
							else if(getZoneMatrix()[k][j+i]==1 && notendbottomy1){
								costMatrix[k][j+i] = 300;
								notendbottomy1 = false;
								controlebottomy = false;
							}
							else if(getZoneMatrix()[k][j+i]==2 && notendbottomy2){
									costMatrix[k][j+i] = 300;
									notendtopy2 = false;
									controlebottomy = false;
								}	
						}

					}
				} else if (costMatrix[k][j] == 3)
					costMatrix[k][j] = 300;
			}
		}
	}
	
	
	/**
	 * affiche du matrice
	 * @param matrix 
	 */
	public void printMatrix(int[][] matrix) {
		
//		for (int i1 = 0; i1 < matrix[0].length; i1++) {
//			for (int i2 = 0; i2 < matrix.length; i2++) {

//				System.out.print(matrix[i2][i1] + " ");
//			}
//			System.out.println();
//		}
//		System.out.println("bizim matris");

	}
}
