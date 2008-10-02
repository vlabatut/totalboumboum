package tournament200708.basmansandalli;

import java.util.ArrayList;

import fr.free.totalboumboum.ai.old200708.ArtificialIntelligence;

public class BasmanSandalli extends ArtificialIntelligence {
	private static final long serialVersionUID = 1L;
	
	/**
	 * La position de la bombe la plus proche
	 */
	private int[] bombPosition ={0,0};
	
	/**
	 * La derni�re position du personnage
	 */
	private int[] lastPosition ={0,0};
	
	/**
	 * Le dernier d�placement effectu�
	 */
	private int lastMove=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
	
	/**
	 * Compteur gardant le nombre de r�p�tition d'un mouvement tant que le personnage reste � la m�me place
	 */
	private int repetitionCounter=1;
	
	/**
	 * Le dernier �tat de s�curit�
	 */
	private boolean lastSafetyState=true;
	
	/**
	 * Constructeur
	 */
	public BasmanSandalli()
	{
		super("BasmnSndll");
	}
	
	public Integer call() throws Exception
	{ 
		
		Integer result=ArtificialIntelligence.AI_ACTION_DO_NOTHING ;
		
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];	
		int[][] matrix = getZoneMatrix();// la zone du jeu
		int moveRight =1;//coefficient indiquant le choix d'aller vers la droite qui sera calcul� � partir des valeurs de priorit�s  
		int moveLeft =1;//coefficient indiquant le choix d'aller vers la gauche qui sera calcul� � partir des valeurs de priorit�s
		int moveUp =1;//coefficient indiquant le choix d'aller vers le haut qui sera calcul� � partir des valeurs de priorit�s
		int moveDown =1;//coefficient indiquant le choix d'aller vers le bas qui sera calcul� � partir des valeurs de priorit�s
		int cons=1;//valeur qu'on va utiliser pour privil�ger les blocs vides
		
		int hr[][][]=new int[getZoneMatrixDimX()][getZoneMatrixDimY()][1];//matrice qui va garder les valeurs de priorit�s pour chaque coordonn�e (x,y)
		
		/*remplissage de la matrice hr gardant des valeurs de priorit�s pour chaque coordonn�es (x,y) qui servira ensuite � 
		choisir le mouvement � effectuer*/
		for(int i=0;i<getZoneMatrixDimX();i++)
		{
			for(int j=0;j<getZoneMatrixDimY();j++)
			{
				if(matrix[i][j] ==ArtificialIntelligence.AI_BLOCK_EMPTY)
					{
						if(lastSafetyState==true)
							hr[i][j][0]=100;
						else
							hr[i][j][0]=1000;
					}
				else if (matrix[i][j] ==ArtificialIntelligence.AI_BLOCK_WALL_HARD)
					hr[i][j][0]=1;
				else if(matrix[i][j] ==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
				{
					if(getTimeBeforeShrink()>4000 && matrix[bombPosition[0]][bombPosition[1]]!=ArtificialIntelligence.AI_BLOCK_BOMB)
					{ 
						hr[i][j][0]=300;
					}
					else{
						hr[i][j][0]=30;
					}
				}
				else if(matrix[i][j] ==ArtificialIntelligence.AI_BLOCK_BOMB)
					hr[i][j][0]=100;
				else if(matrix[i][j] ==ArtificialIntelligence.AI_BLOCK_ITEM_BOMB || matrix[i][j]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE )	
				{
					if(getTimeBeforeShrink()>4000)
					{ 
						hr[i][j][0]=3000;
					}
					else
					{
						hr[i][j][0]=100;
					}	
				}
				else if(matrix[i][j] == ArtificialIntelligence.AI_BLOCK_FIRE)
				{
					if(getTimeBeforeShrink()>4000)
						hr[i][j][0]=1;
					else
						hr[i][j][0]=100;
				}
				else 
					hr[i][j][0]=1;	
			}
		}
		
		//si on est dans le shrink (ou pr�s du shrink) on privil�ge le centre de la zone du jeu
		if(getTimeBeforeShrink()<4000)
			hr[9][7][0]=10000;
		
		// si le jeu est entre 4i�me et 45i�me secondes on privil�ge les directions o� se trouve le personnage le plus pr�s
		if( getTimeBeforeShrink()>4000 && getTimeBeforeShrink()<45000 )
			{
				if(getClosestPlayerPosition()[0]!=x && getClosestPlayerPosition()[1]!=y)
				{
					if(x-getClosestPlayerPosition()[0]>0)
					{
						moveLeft=moveLeft+10000;
					}
					else
					{
						moveRight=moveRight+10000;
					}
					if(y-getClosestPlayerPosition()[1]>0)
					{
						moveUp=moveUp+10000;
					}
					else
					{
						moveDown=moveDown+10000;
					}
				}
				else
				{
					hr[getClosestPlayerPosition()[0]][getClosestPlayerPosition()[1]][0]=10000;
				}
			}
		
		//calcul du coefficient indiquant le choix d'aller vers la droite � partir des valeurs de priorit�s gard�es	dans la matrice hr
		if(matrix[x+1][y] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x+1][y] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN )
		{//s'il est possible d'effectuer un mouvement vers la droite	
			int i=x+1;
			cons=1;
			while(i<getZoneMatrixDimX())
			{
				if(matrix[i][y]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privil�ge des blocs vides cons�cutifs
				{
					cons=cons+50;//on privil�ge des blocs vides cons�cutifs
					moveRight = moveRight+hr[i][y][0]+cons;
				}
				else
				{	
					cons=1;
					moveRight = moveRight+hr[i][y][0];
				}
				
				i++;
		}
			
	}
		else//le mouvement vers la droite est impossible
		{
			moveRight=1;
		}
		
		//calcul du coefficient indiquant le choix d'aller vers la gauche � partir des valeurs de priorit�s gard�es	dans la matrice hr
		if(matrix[x-1][y] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x-1][y] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN  ){//s'il est possible d'effectuer un mouvement vers la gauche	
			
			cons=1;
			int i=x-1;
			
			while( i>0 )
			{
				if(matrix[i][y]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privil�ge des blocs vides cons�cutifs
				{
					cons=cons+50;//on privil�ge des blocs vides cons�cutifs
					moveLeft = moveLeft+hr[i][y][0]+cons;
				}
				else
				{
					cons=1;
					moveLeft = moveLeft+hr[i][y][0];
				}
				
				i--;
		}
			
	}
		else//le mouvement vers la gauche est impossible
		{
			moveLeft=1;
		}
		
		//calcul du coefficient indiquant le choix d'aller vers le bas � partir des valeurs de priorit�s gard�e dans la matrice hr
		if(matrix[x][y+1] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x][y+1] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN ){//s'il est possible d'effectuer un mouvement vers le bas
			
			cons=1;
			int i=y+1;
			while(i<getZoneMatrixDimY())
			{
				if(matrix[x][i]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privil�ge des blocs vides cons�cutifs
				{
					cons=cons+50;//on privil�ge des blocs vides cons�cutifs
					moveDown = moveDown+hr[x][i][0]+cons;
				}
				else
				{
					cons=1;
					moveDown = moveDown+hr[x][i][0];
				}	
				
				i++;
		}
			
	}
		else//le mouvement vers le bas est impossible
		{
			moveDown=1;
		}
		
		//calcul du coefficient indiquant le choix d'aller vers le haut � partir des valeurs de priorit�s gard�es dans la matrice hr
		if(matrix[x][y-1] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x][y-1] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN )
		{//s'il est possible d'effectuer un mouvement vers le haut	
			cons=1;
			int i=y-1;
			while(i>0)
			{
				if(matrix[x][i]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privil�ge des blocs vides cons�cutifs
				{
					cons=cons+50;
					moveUp = moveUp+hr[x][i][0]+cons;
				}
				else
				{			
					cons=1;
					moveUp = moveUp+hr[x][i][0];
				}	
					
				i--;
		}
	}
		else//le mouvement vers le haut est impossible
		{
			moveUp=1;
		}
		
		
		if(isSafe(matrix,x,y))//Si le personnage est en securit�
		{
			
			lastSafetyState=true;
			
			//pour que le personnage ne se dirige pas vers le danger on multiplie par -1 les directions qui l'emporte au danger pour les �liminer
			if(matrix[bombPosition[0]][bombPosition[1]] == ArtificialIntelligence.AI_BLOCK_BOMB || matrix[bombPosition[0]][bombPosition[1]]== ArtificialIntelligence.AI_BLOCK_FIRE )
			{
				if(x-bombPosition[0]<0)
				{
					moveRight=-moveRight;
				}
				else if(x-bombPosition[0]>0)
				{
					moveLeft=-moveLeft;
				}
				if(y-bombPosition[1]>0)
				{
					moveUp=-moveUp;
				}	
				
				else if(y-bombPosition[1]<0)
				{
					moveDown=-moveDown;
				}
			}
			
			//on choisit le coefficient le plus grand	
			int choice =Math.max(Math.max(Math.max(moveRight, moveLeft), moveUp), moveDown);
				
			if(choice==moveRight){//si le choix est d'aller vers la droite
				result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;//par d�faut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le d�truire
				if(matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
					{
						if(matrix[bombPosition[0]][bombPosition[1]]!= ArtificialIntelligence.AI_BLOCK_BOMB)//Si la bombe par laquelle le personnage s'est enfuite a explos�
						{	
							result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
						}
						else
						{
							if(getTimeBeforeShrink()<0)//si le shrink a commenc�  
								result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						}
					
						Thread.sleep(200);
					}
				//s'il existe une bombe � un temps pr�s au shrink le personnage ne fera rien car il est en s�curit�
				if(getTimeBeforeShrink()<4000 && matrix[bombPosition[0]][bombPosition[1]]== ArtificialIntelligence.AI_BLOCK_BOMB )
					result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				//attaque: s'il existe un joeur dans une position agr�able pour pi�ger on pose une bombe
				if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1) && isTrap(matrix,x,y+1,x,y)))
					
				{
						if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
						{
							result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
						}
				}
			}
				
			else if(choice==moveLeft){//si le choix est d'aller vers la gauche
				
				result=ArtificialIntelligence.AI_ACTION_GO_LEFT;//par d�faut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le d�truire
				if(matrix[x-1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
					{
					if(matrix[bombPosition[0]][bombPosition[1]]!= ArtificialIntelligence.AI_BLOCK_BOMB )
					{		
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
					}
					else
					{
						if(getTimeBeforeShrink()<0)
							result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
					}
				
					Thread.sleep(200);
				}
				if(getTimeBeforeShrink()<4000 && matrix[bombPosition[0]][bombPosition[1]]== ArtificialIntelligence.AI_BLOCK_BOMB )
					result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				//attaque: s'il existe un joeur dans une position agr�able pour pi�ger on pose une bombe
				if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1)&& isTrap(matrix,x,y+1,x,y)))
				{	
					if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
					{		
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;		
					}
				}
			
			}
				
			else if(choice==moveDown){//si le choix est d'aller vers le bas
				result=ArtificialIntelligence.AI_ACTION_GO_DOWN;//par d�faut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le d�truire
				if(matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
					{
					if(matrix[bombPosition[0]][bombPosition[1]]!= ArtificialIntelligence.AI_BLOCK_BOMB   ){	
						
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;	
					}
					else
					{
						if(getTimeBeforeShrink()<0)
						result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
					}
					Thread.sleep(200);
					}
				if(getTimeBeforeShrink()<4000 && matrix[bombPosition[0]][bombPosition[1]]== ArtificialIntelligence.AI_BLOCK_BOMB )
					result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				//attaque: s'il existe un joeur dans une position agr�able pour pi�ger on pose une bombe
			    if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1)&& isTrap(matrix,x,y+1,x,y)))
				{
			    	if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
			    	{
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
			    	}
				}
				
			}
				
			else{//le choix est d'aller vers le haut
				
				result=ArtificialIntelligence.AI_ACTION_GO_UP;//par d�faut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le d�truire
				if(matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
					{
					if(matrix[bombPosition[0]][bombPosition[1]]!= ArtificialIntelligence.AI_BLOCK_BOMB   )
					{		
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
					}
					else
					{
						if(getTimeBeforeShrink()<0)
							result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
					}
				
					Thread.sleep(200);
					}
				if(getTimeBeforeShrink()<4000 && matrix[bombPosition[0]][bombPosition[1]]== ArtificialIntelligence.AI_BLOCK_BOMB )
					result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				//attaque: s'il existe un joeur dans une position agr�able pour pi�ger on pose une bombe
				if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1)&& isTrap(matrix,x,y+1,x,y)))
				{
					if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
					{
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
					}
					
				}
				
			}
			
				//pr�diction du danger: si le mouvement choisi emportera le personnage � une case dangereuse, il attend pour que ce danger d�passe
				if(result==ArtificialIntelligence.AI_ACTION_GO_RIGHT)//si le mouvement choisi est vers la droite
				{
					if(!isSafe(matrix,x+1,y) || matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_FIRE)//s'il est dangereux de faire ce mouvement
					{
						result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						Thread.sleep(200);
					}
					
					
				}
				else if(result==ArtificialIntelligence.AI_ACTION_GO_LEFT)//si le mouvement choisi est vers la gauche
				{
					if(!isSafe(matrix,x-1,y)|| matrix[x-1][y]==ArtificialIntelligence.AI_BLOCK_FIRE )//s'il est dangereux de faire ce mouvement
					{
						result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						Thread.sleep(200);
					}
					
					
				}
				else if(result==ArtificialIntelligence.AI_ACTION_GO_DOWN)//si le mouvement choisi est vers le bas
				{
					if(!isSafe(matrix,x,y+1) || matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_FIRE)//s'il est dangereux de faire ce mouvement
					{
						result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						Thread.sleep(200);
					}
			
				
				}
				else if(result==ArtificialIntelligence.AI_ACTION_GO_UP)//si le mouvement choisi est vers le haut
				{
					if(!isSafe(matrix,x,y-1) || matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_FIRE)//s'il est dangereux de faire ce mouvement
					{
						result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						Thread.sleep(200);
					}	
				}
		}
		
		
		
		else // le personnage est en danger
		{
			lastSafetyState=false;
			int bombX=bombPosition[0];//r�cup�ration des coordonn�es de la bombe qui est dangereuse pour le personnage
			int bombY=bombPosition[1];//r�cup�ration des coordonn�es de la bombe qui est dangereuse pour le personnage
			int power=getBombPowerAt(bombX,bombY);////r�cup�ration de la port�e de la bombe qui est dangereuse pour le personnage
			
			if(x-bombX>0)//la bombe est � gauche du personnage
			{
				// pour tomber en pi�ge plus rarement, le personnage pr�f�re d'abord de s'enfuir lin�arement si la port�e de la bombe est suffisement petite
				if(x+power<getZoneMatrixDimX() && possibleRightX(bombX,y,power+1) && getBombPowerAt(bombX,bombY)<3 ) 
					result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
				else//il est impossible de s'enfuir lin�arement
				{
					/*� chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires � la direction du personnage 
					 * par rapport � la bombe(haut et bas pour la bombe situ�e � gauche ici) en choissisant la direction poss�dant la plus grande constante*/
					if(moveUp>=moveDown && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) && isSafe(matrix,x,y-1) )
						{
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)  && isSafe(matrix,x,y+1))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						}
					
					}
					else if(moveUp<=moveDown && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)  && isSafe(matrix,x,y+1))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP)  && isSafe(matrix,x,y-1))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						}
					}
					else if(matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_EMPTY || matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_ITEM_BOMB || matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE)
							{
								result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
							}
					else//la bombe est � gauche donc aller vers la gauche est le dernier choix
					{
						result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
					}
				}
			}
			
			else if(x-bombX<0)//la bombe est � droite du personnage
			{
				// pour tomber en pi�ge plus rarement, le personnage pr�f�re d'abord de s'enfuir lin�arement si la port�e de la bombe est suffisement petite
				if(x-power>0 && possibleLeftX(bombX,y,power+1) && getBombPowerAt(bombX,bombY)<3 ) 
					result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
				else//il est impossible de s'enfuir lin�arement
				{
					/*� chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires � la direction du personnage 
					 * par rapport � la bombe(haut et bas pour la bombe situ�e � droite ici) en choissisant la direction poss�dant la plus grande constante*/
					if(moveUp>=moveDown  && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) && isSafe(matrix,x,y-1) )
						{
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)  && isSafe(matrix,x,y+1))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						}
					
					}
					else if(moveUp<=moveDown && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN)  && isSafe(matrix,x,y+1))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP)  && isSafe(matrix,x,y-1))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						}
					}
					else if(matrix[x-1][y]==ArtificialIntelligence.AI_BLOCK_EMPTY || matrix[x-1][y]==ArtificialIntelligence.AI_BLOCK_ITEM_BOMB || matrix[x-1][y]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE  )
						{
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						}
					else//la bombe est � droite donc aller vers la gauche est le dernier choix
					{
						result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;	
					}
				
				}	
			}
			
			else if(y-bombY>0)//la bombe est en haut du personnage
			{
				// pour tomber en pi�ge plus rarement, le personnage pr�f�re d'abord de s'enfuir lin�arement si la port�e de la bombe est suffisement petite 
				if(y+power<getZoneMatrixDimY() && possibleDownY(x,bombY,power+1) && getBombPowerAt(bombX,bombY)<3 )
					result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
				else//il est impossible de s'enfuir lin�arement
				{
					/*� chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires � la direction du personnage 
					 * par rapport � la bombe(gauche et droite pour la bombe situ�e en haut ici) en choissisant la direction poss�dant la plus grande constante*/
					if(moveRight>=moveLeft  && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) && isSafe(matrix,x+1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)&& isSafe(matrix,x-1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						}
					
					}
					else if(moveRight<=moveLeft && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)&& isSafe(matrix,x-1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT)&& isSafe(matrix,x+1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						}
					}
					else if(matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_EMPTY || matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_ITEM_BOMB || matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE)
							{
								result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
							}
					else//la bombe est en haut donc aller vers la gauche est le dernier choix
					{
						result=ArtificialIntelligence.AI_ACTION_GO_UP;
					}
				}	
			}
			
			else if(y-bombY<0)//la bombe est en bas du personnage
			{
				// pour tomber en pi�ge plus rarement, le personnage pr�f�re d'abord de s'enfuir lin�arement si la port�e de la bombe est suffisement petite
				if(y-power>0 && possibleUpY(x,bombY,power+1) && getBombPowerAt(bombX,bombY)<3 ) 
					result=ArtificialIntelligence.AI_ACTION_GO_UP;
				else//il est impossible de s'enfuir lin�arement
				{
					/*� chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires � la direction du personnage 
					 * par rapport � la bombe(droite et gauche pour la bombe situ�e en bas ici) en choissisant la direction poss�dant la plus grande constante*/
					if(moveRight>=moveLeft  && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT)&& isSafe(matrix,x+1,y) )
						{
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)&& isSafe(matrix,x-1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						}
					
					}
					else if(moveRight<=moveLeft && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) || isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)))
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT)&& isSafe(matrix,x-1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						}
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT)&& isSafe(matrix,x+1,y))
						{
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						}
					}
					else if(matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_EMPTY || matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_ITEM_BOMB || matrix[x][y-1]==ArtificialIntelligence.AI_BLOCK_ITEM_FIRE )
							{
								result=ArtificialIntelligence.AI_ACTION_GO_UP;
							}
					else//la bombe est en bas donc aller vers la gauche est le dernier choix
					{
						result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
					}
				}
			}
			else//la bombe est sur le personnage
			{   
				/*th�oriquement dans les pluparts des cas le personnage peut tomber en pi�ge s'il retourne par la direction parlaqulle il est venu,
				donc on essaie derni�rement de s'enfuir par cette direction gard�e dans le champs "lastMove"*/
				if(lastMove==ArtificialIntelligence.AI_ACTION_GO_LEFT)
				{
					
					 if(!isTrap(matrix,x,y-1,bombPosition[0],bombPosition[1]) && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) || anyPlayerExists(x,y-1)))
					{
						result=ArtificialIntelligence.AI_ACTION_GO_UP;
					}
					else
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) && !isTrap(matrix,x,y+1,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isTrap(matrix,x-1,y,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
					}
				}
					
				else if(lastMove==ArtificialIntelligence.AI_ACTION_GO_RIGHT)
				{
					
					 if(!isTrap(matrix,x,y-1,bombPosition[0],bombPosition[1]) && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) || anyPlayerExists(x,y-1)))
					{
						result=ArtificialIntelligence.AI_ACTION_GO_UP;
					}
					else
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) && !isTrap(matrix,x,y+1,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) && !isTrap(matrix,x+1,y,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
					}
					
				}
				else if(lastMove==ArtificialIntelligence.AI_ACTION_GO_UP)
				{
					
					 if(!isTrap(matrix,x+1,y,bombPosition[0],bombPosition[1]) && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) || anyPlayerExists(x+1,y) ))
					{
						result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
					}
					else
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isTrap(matrix,x-1,y,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isTrap(matrix,x,y-1,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
					}
					}
				else if(lastMove==ArtificialIntelligence.AI_ACTION_GO_DOWN)
				{
					
					 if(!isTrap(matrix,x+1,y,bombPosition[0],bombPosition[1]) && (isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT)  || anyPlayerExists(x+1,y)))
					{
						result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
					}
					else
					{
						if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isTrap(matrix,x-1,y,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) && !isTrap(matrix,x,y+1,bombPosition[0],bombPosition[1]))
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
					}
				}
				
				// si le personnage ne se d�place pas malgr� l'existance d'une sur lui, il peut y avoir un probl�me donc on augmente le compteur
				if(lastPosition[0]==bombPosition[0] && lastPosition[1]==bombPosition[1])
					{
						repetitionCounter++;
					}
				else //si le personnage s'est d�plac� pour pouvoir s'enfuir, on remet le compteur en 1
					{
						repetitionCounter=1;
					}
				//si compteur de r�p�tition a une grande valeur donc le personnage s'est attach� vraiment � une bombe et on doit le sauver
				if(repetitionCounter>21)
				{
					if(result==ArtificialIntelligence.AI_ACTION_GO_DOWN)
					{
						if(!isTrap(matrix,x,y-1,x,y))
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						else if(isSafe(matrix,x+1,y) && isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) )
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT))
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
					}	
					else if(result==ArtificialIntelligence.AI_ACTION_GO_UP)
					{
						if(!isTrap(matrix,x,y+1,x,y))
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						else if(isSafe(matrix,x+1,y) && isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) )
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT))
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
					}
					else if(result==ArtificialIntelligence.AI_ACTION_GO_RIGHT)
					{
						if(!isTrap(matrix,x-1,y,x,y))
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
						else if(isSafe(matrix,x,y+1) && isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN))
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						else if ( isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP))
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
					}
					else if(result==ArtificialIntelligence.AI_ACTION_GO_LEFT)
					{
						if(!isTrap(matrix,x+1,y,x,y))
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
						else if(isSafe(matrix,x,y+1)&& isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) )
							result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
						else if(isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP))
							result=ArtificialIntelligence.AI_ACTION_GO_UP;
						else
							result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
					}
				}
			}
			
		}
			
		//si le mouvement choisi changera la direction du personnage et la position changait
		if(result != ArtificialIntelligence.AI_ACTION_PUT_BOMB  && (lastPosition[0]!=x || lastPosition[1]!=y  ))
		{
			if( result != ArtificialIntelligence.AI_ACTION_DO_NOTHING )
			{
				lastMove=result;// enregistrement du dernier mouvement
				repetitionCounter=1;// on remet le compteur en 1
			}
			
		}
		lastPosition[0]=x;//enregistrement de la derni�re position 
		lastPosition[1]=y;//enregistrement de la derni�re position
		
		return result;
		
	}
	/**
	 * Indique si le personnage a la possibilit� de s'enfuir ou pas
	 * par les coordonn�es (x,y) pass�s en param�tre avec une bombe situ�e en (bombX,bombY) 
	 * @param matrix la zone du jeu
	 * @param x position � �tudier
	 * @param y position � �tudier
	 * @param bombX positon de la bombe
	 * @param bombY positon de la bombe
	 * @return vrai si le personnage n'a aucune chance de s'enfuir
	 */
	private boolean isTrap(int[][] matrix,int x,int y,int bombX,int bombY)
	{	
		int power = getBombPowerAt(bombX,bombY);//s'il existe une bombe en ces coordonn�es on parle d'un d�sir du d�fense (le personnage veut s'enfuir par une bombe)
	
		if(power==-1)//s'il n'existe pas une bombe en ces coordonn�es alors on parle d'un d�sir de l'attaque(donc d'une simulation) et on va utiliser la port�e de la bombe propre au personnage
			power=getOwnFirePower();
		
		boolean result=false;
		
		if(x-bombX>0){//si la bombe est � gauche
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers le bas c'est une pi�ge
			if(!isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN))
			{
				result=true;
			}
			else
			{
				while(i<power)//on va �tudier une distance i partant 1 jusqu'� la port�e de la bombe
				{
					if(bombX+i<getZoneMatrixDimX())//si le personnage+la port�e de la bombe est dans la zone du jeu
					{
						if(possibleRightX(x,y,i))//s'il est possible d'aller vers la droite pour la distance i
						{
							//s'il est impossible de trouver une place � se cacher en se d�placant pour une distance i
							if(!isMovePossible(x+i,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isMovePossible(x+i,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) )
							{
								result=true;
							}
							else//il est possible de se cacher
							{
								result=false;
								break;//pas besoin de continuer � �tudier
							}
						}
						else//s'il est impossible d'aller vers la droite pour la distance i, il sera impossible de s'enfuir 
						{
							result=true;
							break;
						}
					}
					i++;
				}
			}
		}
		
		if(x-bombX<0)//si la bombe est � droite
		{
			int i = 1;
			//s'il est impossible de faire un mouvement vers la gauche, vers le haut ou vers le bas c'est une pi�ge
			if(!isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN))
			{
				result=true;
			}
			else
			{
				while(i<power)
				{
					if(bombX-i>0)
					{
						if(possibleLeftX(x,y,i))
						{
							if(!isMovePossible(x-i,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isMovePossible(x-i,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) )
							{
								result=true;
							}
							else
							{
								result=false;
								break;
							}
						}
						else
						{
							result=true;
							break;
						}
					}
					i++;
				}
			}
		}
		
		
		if(y-bombY>0){//si la bombe est en haut
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers la gauche ou vers le bas c'est une pi�ge
			if(!isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN))
			{
				result=true;
			}
			else
			{
				while(i<power)
				{
					if(bombY+i<getZoneMatrixDimY())
					{
						if(possibleDownY(x,y,i))
						{
							if(!isMovePossible(x,y+i,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isMovePossible(x,y+i,ArtificialIntelligence.AI_ACTION_GO_RIGHT) )
							{
								result=true;
							}
							else
							{
								result=false;
								break;
							}
						}
						else
						{
							result=true;
							break;
						}
					}
					i++;
				}
			}
		}
		
		
		if(y-bombY<0){//si la bombe est en bas
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers la gauche c'est une pi�ge
			if(!isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP))
			{
				result=true;
			}
			else
			{
				while(i<power)
				{
					if(bombY-i>0)
					{
						if(possibleUpY(x,y,i))
						{
							if(!isMovePossible(x,y-i,ArtificialIntelligence.AI_ACTION_GO_LEFT) && !isMovePossible(x,y-i,ArtificialIntelligence.AI_ACTION_GO_RIGHT) )
							{
								result=true;
							}
							else
							{
								result=false;
								break;
							}
						}
						else
						{
							result=true;
							break;
						}
					}
					i++;
				}
			}
		}
		
		
		int i=power+1;
		//si la bombe est � gauche et le personnage peut aller vers la droite pour une distance plus grande que la port�e de la bombe alors ce n'est pas une pi�ge
		if(x-bombX>0 && possibleRightX(bombX,y,i))
			result=false;
		//si la bombe est � droite et le personnage peut aller vers la gauche pour une distance plus grande que la port�e de la bombe alors ce n'est pas une pi�ge
		else if(x-bombX<0 && possibleLeftX(bombX,y,i))
			 result=false;
		//si la bombe est en bas et le personnage peut aller vers le haut pour une distance plus grande que la port�e de la bombe alors ce n'est pas une pi�ge
		 if(y-bombY<0 && possibleUpY(x,bombY,i))
			 result=false;
		//si la bombe est en haut et le personnage peut aller vers le bas pour une distance plus grande que la port�e de la bombe alors ce n'est pas une pi�ge
		 else if(y-bombY>0 && possibleDownY(x,bombY,i))
			 result=false;
		 
		return result;
	}
	
	/**
	 * Indique si un personnage situ� en coordonn�es (x,y) pass�s en param�tre est en 
	 * securit� ou pas par rapport � toutes les bombes qui existent dans la zone du jeu
	 * @param matrix la zone du jeu
	 * @param x la coordonn�e actuelle du personnage en x
	 * @param y la coordonn�e actuelle du personnage en y
	 * @return vrai s'il n'existe aucun danger pour un personnage situ� en coordonn�es (x,y) 
	 */
	private boolean isSafe(int[][] matrix,int x,int y)
	{	
		boolean result=true;
		
		//r�cup�ration d'une liste des bombes existantes dans la zone du jeu
		ArrayList<int[]> bombList = new ArrayList<int[]>();
		for(int i=0;i<getZoneMatrixDimX();i++)
		{
			for(int j=0;j<getZoneMatrixDimY();j++)
			{
				if(matrix[i][j]==ArtificialIntelligence.AI_BLOCK_BOMB);
					{
						int[] bombCoordonnees=new int[2];
						bombCoordonnees[0]=i;
						bombCoordonnees[1]=j;
						bombList.add(bombCoordonnees);				
					}
			}
		}
		
		int i=0;
		while(i<bombList.size()){//pour toutes les bombes
			int power=getBombPowerAt(bombList.get(i)[0],bombList.get(i)[1]);
		
			while(power>0){
			
				if(x+power<getZoneMatrixDimX()){ //Contr�le des limites en x vers la droite pour ne pas tomber en exception en r�cup�rant si la port�e de la bombe+la coordonn�e en x du personnage est bien dans la zone du jeu
			
					if(x+power==bombList.get(i)[0] && y==bombList.get(i)[1] )//si le feu de la bombe attrape le personnage par la droite et a la chance(power) d'arriver au personnage
					{
						for(int l=1;l<power+1;l++)//On part par notre c�t� droite pour une distance relative � la port�e de la bombe
						{
							if(matrix[x+l][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD || matrix[x+l][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)// s�il existe une bloc entre la bombe et le personnage il est en s�curit�
							{
								result =true;// personnage est en s�curit�
								break;//pas besoin de continuer � contr�ler car il existe un bloc entre la bombe et le personnage
							}
							else//personnage est en danger
								result=false;
						}
					}
				}
				if(x-power>0){//Contr�le des limites en x vers la gauche
			
					if(x-power==bombList.get(i)[0] && y==bombList.get(i)[1] )//si le feu de la bombe attrape le personnage par la gauche et a la chance(power) d'arriver au personnage
					{
						for(int l=1;l<power+1;l++)
						{
							if(matrix[x-l][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD || matrix[x-l][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
							{
									result =true;
									break;
							}
							else
								result=false;
						}
					}
				}
				if(y-power>0){//Contr�le des limites en y vers le haut
			
					if(x==bombList.get(i)[0] && y-power==bombList.get(i)[1] )//si le feu de la bombe attrape le personnage par le haut et a la chance(power) d'arriver au personnage
					{
						
						for(int l=1;l<power+1;l++)
						{
							if(matrix[x][y-l]==ArtificialIntelligence.AI_BLOCK_WALL_HARD || matrix[x][y-l]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
							{
								result =true;
								break;
							}
							else
								result=false;
						}
					}
				}
				if(y+power<getZoneMatrixDimY()){//Contr�le des limites en y vers le bas
			
					if(x==bombList.get(i)[0] && y+power==bombList.get(i)[1] )//si le feu de la bombe attrape le personnage par le bas et a la chance(power) d'arriver au personnage
					{
						for(int l=1;l<power+1;l++)
						{
							if(matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_WALL_HARD || matrix[x][y+1]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
							{
								result =true;
								break;
							}
							else
								result=false;
						}
					}
				}
					power--;	
			}
				
			if(result==false)// s'il existe au moins une bombe qui met le personnage en danger on n'a pas besoin de continuer � contr�ler
			{
				bombPosition[0]=bombList.get(i)[0];
				bombPosition[1]=bombList.get(i)[1];
				break;
			}
			else//s'il existe aucun danger pour cette bombe on passe � la bombe suivante
				i++;
		}
		
		if(matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB)//si la bombe est sur le personnage il est bien en danger
		{
			bombPosition[0]=x;
			bombPosition[1]=y;
			result=false;
		}
		
		return result;
	}
	

	/**
	 * Calcule et renvoie la distance de Manhattan 
	 * (cf. : http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29)
	 * entre le point de coordonn�es (x1,y1) et celui de coordonn�es (x2,y2). 
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
	 * Indique si le d�placement dont le code a �t� pass� en param�tre 
	 * est possible pour un personnage situ� en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le d�placement � �tudier
	 * @return	vrai si ce d�placement est possible
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
	 * Indique si la case situ�e � la position pass�e en param�tre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position � �tudier
	 * @param y	position � �tudier
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
	 * Indique s'il est possible d'aller vers la droite par les coordonn�es (x,y) pour une distance d pass�e en param�tre
	 * @param x position de d�part en x
	 * @param y position de d�part en y
	 * @param d la distance � parcourir
	 * @return vrai s'il est possible d'aller vers la droite pour la distance d
	 */
	private boolean possibleRightX(int x,int y,int d)
	{
		boolean result=true;
		while(d>0){
			if(x+d<getZoneMatrixDimX()){
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
		return result;
	}
	
	/**
	 * Indique s'il est possible d'aller vers la gauche par les coordonn�es (x,y) pour une distance d pass�e en param�tre
	 * @param x position de d�part en x
	 * @param y position de d�part en y
	 * @param d la distance � parcourir
	 * @return vrai s'il est possible d'aller vers la gauche pour la distance d
	 */
	private boolean possibleLeftX(int x,int y,int d)
	{
		boolean result=true;
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
		return result;
	}
	
	/**
	 * Indique s'il est possible d'aller vers le haut par les coordonn�es (x,y) pour une distance d pass�e en param�tre
	 * @param x position de d�part en x
	 * @param y position de d�part en y
	 * @param d la distance � parcourir
	 * @return vrai s'il est possible d'aller vers le haut pour la distance d
	 */
	private boolean possibleUpY(int x,int y,int d)
	{
		boolean result=true;
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
		return result;
	}
	/**
	 * Indique s'il est possible d'aller vers le bas par les coordonn�es (x,y) pour une distance d pass�e en param�tre
	 * @param x position de d�part en x
	 * @param y position de d�part en y
	 * @param d la distance � parcourir
	 * @return vrai s'il est possible d'aller vers le bas pour la distance d
	 */
	private boolean possibleDownY(int x,int y,int d)
	{
		boolean result=true;
		while(d>0){
			if(y+d<getZoneMatrixDimY()){
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
		return result;
	}
	
	/**
	 * Indique s'il existe un autre joueur en coordonn�es (x,y) pass�s en param�tre
	 * @param x coordonn�e en x � tester
	 * @param y coordonn�e en y � tester
	 * @return vrai s'il existe un autre joueur en coordonn�es (x,y) pass�s en param�tre
	 */
	private boolean anyPlayerExists(int x,int y)
	{
		boolean result=false;
		int i=0;
		while(i<getPlayerCount())
		{
			if(getPlayerPosition(i)[0]==x && getPlayerPosition(i)[1]==y && isPlayerAlive(i))
			{
				result=true;
				break;
			}
			i++;
		}
		return result;
	}
	
	
	/**
	 * Cherche le joueur le plus proche au personnage et renvoie ses coordonn�es
	 * @return les coordonn�es du joueur le plus proche au personnage
	 */
	private int[] getClosestPlayerPosition()
	{
		
		int i=0;
		int[] position={0,0};
		int distance=100;
		
		while(i<=getPlayerCount())//pour tous les joueurs
		{
			//si le joueur i est plus proche au personnage on r�cup�re ses coordonn�es
			if(distance(getOwnPosition()[0],getOwnPosition()[1],getPlayerPosition(i)[0] , getPlayerPosition(i)[1])<distance && isPlayerAlive(i))
			{
				distance=distance(getOwnPosition()[0],getOwnPosition()[1],getPlayerPosition(i)[0] , getPlayerPosition(i)[1]);
				position[0]=getPlayerPosition(i)[0];
				position[1]=getPlayerPosition(i)[1];
			}
			i++;
		}
		return position;
	}
	
}
