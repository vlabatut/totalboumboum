package org.totalboumboum.ai.v200708.ais.basmansandalli;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Can Başman
 * @author Halil İbrahim Sandallı
 */
@SuppressWarnings("deprecation")
public class BasmanSandalli extends ArtificialIntelligence {
	/** */
	private static final long serialVersionUID = 1L;
	
	/**
	 * La position de la bombe la plus proche
	 */
	private int[] bombPosition ={0,0};
	
	/**
	 * La dernière position du personnage
	 */
	private int[] lastPosition ={0,0};
	
	/**
	 * Le dernier déplacement effectué
	 */
	private int lastMove=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
	
	/**
	 * Compteur gardant le nombre de répétitions d'un mouvement tant que le personnage reste à la même place
	 */
	private int repetitionCounter=1;
	
	/**
	 * Le dernier état de sécurité
	 */
	private boolean lastSafetyState=true;
	
	/**
	 * Constructeur
	 */
	public BasmanSandalli()
	{
		super("BasmnSndll");
	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{ 
		Integer result=ArtificialIntelligence.AI_ACTION_DO_NOTHING ;

		if(firstTime)
			firstTime = false;
		else
		{	
		
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];	
		int[][] matrix = getZoneMatrix();// la zone du jeu
		int moveRight =1;//coefficient indiquant le choix d'aller vers la droite qui sera calculé à partir des valeurs de priorités  
		int moveLeft =1;//coefficient indiquant le choix d'aller vers la gauche qui sera calculé à partir des valeurs de priorités
		int moveUp =1;//coefficient indiquant le choix d'aller vers le haut qui sera calculé à partir des valeurs de priorités
		int moveDown =1;//coefficient indiquant le choix d'aller vers le bas qui sera calculé à partir des valeurs de priorités
		int cons=1;//valeur qu'on va utiliser pour privilégier les blocs vides
		
		int hr[][][]=new int[getZoneMatrixDimX()][getZoneMatrixDimY()][1];//matrice qui va garder les valeurs de priorités pour chaque coordonnée (x,y)
		
		/*remplissage de la matrice hr gardant des valeurs de priorités pour chaque coordonnées (x,y) qui servira ensuite à 
		choisir le mouvement à effectuer*/
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
		
		//si on est dans le shrink (ou près du shrink) on privilège le centre de la zone du jeu
		if(getTimeBeforeShrink()<4000)
			hr[9][7][0]=10000;
		
		// si le jeu est entre 4ième et 45ième secondes on privilège les directions où se trouve le personnage le plus près
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
		
		//calcul du coefficient indiquant le choix d'aller vers la droite à partir des valeurs de priorités gardées	dans la matrice hr
		if(matrix[x+1][y] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x+1][y] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN )
		{//s'il est possible d'effectuer un mouvement vers la droite	
			int i=x+1;
			cons=1;
			while(i<getZoneMatrixDimX())
			{
				if(matrix[i][y]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privilège des blocs vides consécutifs
				{
					cons=cons+50;//on privilège des blocs vides consécutifs
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
		
		//calcul du coefficient indiquant le choix d'aller vers la gauche à partir des valeurs de priorités gardées	dans la matrice hr
		if(matrix[x-1][y] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x-1][y] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN  ){//s'il est possible d'effectuer un mouvement vers la gauche	
			
			cons=1;
			int i=x-1;
			
			while( i>0 )
			{
				if(matrix[i][y]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privilège des blocs vides consécutifs
				{
					cons=cons+50;//on privilège des blocs vides consécutifs
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
		
		//calcul du coefficient indiquant le choix d'aller vers le bas à partir des valeurs de priorités gardée dans la matrice hr
		if(matrix[x][y+1] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x][y+1] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN ){//s'il est possible d'effectuer un mouvement vers le bas
			
			cons=1;
			int i=y+1;
			while(i<getZoneMatrixDimY())
			{
				if(matrix[x][i]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privilège des blocs vides consécutifs
				{
					cons=cons+50;//on privilège des blocs vides consécutifs
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
		
		//calcul du coefficient indiquant le choix d'aller vers le haut à partir des valeurs de priorités gardées dans la matrice hr
		if(matrix[x][y-1] !=ArtificialIntelligence.AI_BLOCK_WALL_HARD && matrix[x][y-1] !=ArtificialIntelligence.AI_BLOCK_UNKNOWN )
		{//s'il est possible d'effectuer un mouvement vers le haut	
			cons=1;
			int i=y-1;
			while(i>0)
			{
				if(matrix[x][i]==ArtificialIntelligence.AI_BLOCK_EMPTY)//on privilège des blocs vides consécutifs
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
		
		
		if(isSafe(matrix,x,y))//Si le personnage est en securité
		{
			
			lastSafetyState=true;
			
			//pour que le personnage ne se dirige pas vers le danger on multiplie par -1 les directions qui l'emporte au danger pour les éliminer
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
				result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;//par défaut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le détruire
				if(matrix[x+1][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)
					{
						if(matrix[bombPosition[0]][bombPosition[1]]!= ArtificialIntelligence.AI_BLOCK_BOMB)//Si la bombe par laquelle le personnage s'est enfuite a explosé
						{	
							result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
						}
						else
						{
							if(getTimeBeforeShrink()<0)//si le shrink a commencé  
								result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
						}
					
						Thread.sleep(200);
					}
				//s'il existe une bombe à un temps près au shrink le personnage ne fera rien car il est en sécurité
				if(getTimeBeforeShrink()<4000 && matrix[bombPosition[0]][bombPosition[1]]== ArtificialIntelligence.AI_BLOCK_BOMB )
					result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				//attaque: s'il existe un joeur dans une position agréable pour piéger on pose une bombe
				if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1) && isTrap(matrix,x,y+1,x,y)))
					
				{
						if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
						{
							result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
						}
				}
			}
				
			else if(choice==moveLeft){//si le choix est d'aller vers la gauche
				
				result=ArtificialIntelligence.AI_ACTION_GO_LEFT;//par défaut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le détruire
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
				//attaque: s'il existe un joeur dans une position agréable pour piéger on pose une bombe
				if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1)&& isTrap(matrix,x,y+1,x,y)))
				{	
					if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
					{		
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;		
					}
				}
			
			}
				
			else if(choice==moveDown){//si le choix est d'aller vers le bas
				result=ArtificialIntelligence.AI_ACTION_GO_DOWN;//par défaut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le détruire
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
				//attaque: s'il existe un joeur dans une position agréable pour piéger on pose une bombe
			    if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1)&& isTrap(matrix,x,y+1,x,y)))
				{
			    	if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
			    	{
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
			    	}
				}
				
			}
				
			else{//le choix est d'aller vers le haut
				
				result=ArtificialIntelligence.AI_ACTION_GO_UP;//par défaut on admet le choix	
				//s'il existe un mur destructible en direction de choix en va le détruire
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
				//attaque: s'il existe un joeur dans une position agréable pour piéger on pose une bombe
				if((anyPlayerExists(x+1,y) && isTrap(matrix,x+1,y,x,y)) || (anyPlayerExists(x-1,y) && isTrap(matrix,x-1,y,x,y)) || (anyPlayerExists(x,y-1) && isTrap(matrix,x,y-1,x,y)) || (anyPlayerExists(x,y+1)&& isTrap(matrix,x,y+1,x,y)))
				{
					if(getOwnBombCount()>0 && getTimeBeforeShrink()>0)
					{
						result=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
					}
					
				}
				
			}
			
				//prédiction du danger: si le mouvement choisi emportera le personnage à une case dangereuse, il attend pour que ce danger dépasse
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
			int bombX=bombPosition[0];//récupération des coordonnées de la bombe qui est dangereuse pour le personnage
			int bombY=bombPosition[1];//récupération des coordonnées de la bombe qui est dangereuse pour le personnage
			int power=getBombPowerAt(bombX,bombY);////récupération de la portée de la bombe qui est dangereuse pour le personnage
			
			if(x-bombX>0)//la bombe est à gauche du personnage
			{
				// pour tomber en piège plus rarement, le personnage préfére d'abord de s'enfuir linéarement si la portée de la bombe est suffisement petite
				if(x+power<getZoneMatrixDimX() && possibleRightX(bombX,y,power+1) && getBombPowerAt(bombX,bombY)<3 ) 
					result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;
				else//il est impossible de s'enfuir linéarement
				{
					/*à chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires à la direction du personnage 
					 * par rapport à la bombe(haut et bas pour la bombe située à gauche ici) en choissisant la direction possédant la plus grande constante*/
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
					else//la bombe est à gauche donc aller vers la gauche est le dernier choix
					{
						result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
					}
				}
			}
			
			else if(x-bombX<0)//la bombe est à droite du personnage
			{
				// pour tomber en piège plus rarement, le personnage préfére d'abord de s'enfuir linéarement si la portée de la bombe est suffisement petite
				if(x-power>0 && possibleLeftX(bombX,y,power+1) && getBombPowerAt(bombX,bombY)<3 ) 
					result=ArtificialIntelligence.AI_ACTION_GO_LEFT;
				else//il est impossible de s'enfuir linéarement
				{
					/*à chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires à la direction du personnage 
					 * par rapport à la bombe(haut et bas pour la bombe située à droite ici) en choissisant la direction possédant la plus grande constante*/
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
					else//la bombe est à droite donc aller vers la gauche est le dernier choix
					{
						result=ArtificialIntelligence.AI_ACTION_GO_RIGHT;	
					}
				
				}	
			}
			
			else if(y-bombY>0)//la bombe est en haut du personnage
			{
				// pour tomber en piège plus rarement, le personnage préfére d'abord de s'enfuir linéarement si la portée de la bombe est suffisement petite 
				if(y+power<getZoneMatrixDimY() && possibleDownY(x,bombY,power+1) && getBombPowerAt(bombX,bombY)<3 )
					result=ArtificialIntelligence.AI_ACTION_GO_DOWN;
				else//il est impossible de s'enfuir linéarement
				{
					/*à chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires à la direction du personnage 
					 * par rapport à la bombe(gauche et droite pour la bombe située en haut ici) en choissisant la direction possédant la plus grande constante*/
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
				// pour tomber en piège plus rarement, le personnage préfére d'abord de s'enfuir linéarement si la portée de la bombe est suffisement petite
				if(y-power>0 && possibleUpY(x,bombY,power+1) && getBombPowerAt(bombX,bombY)<3 ) 
					result=ArtificialIntelligence.AI_ACTION_GO_UP;
				else//il est impossible de s'enfuir linéarement
				{
					/*à chaque fois pour s'enfuir facilement on cherche d'abord les mouvements perpendiculaires à la direction du personnage 
					 * par rapport à la bombe(droite et gauche pour la bombe située en bas ici) en choissisant la direction possédant la plus grande constante*/
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
				/*théoriquement dans les pluparts des cas le personnage peut tomber en piège s'il retourne par la direction parlaqulle il est venu,
				donc on essaie dernièrement de s'enfuir par cette direction gardée dans le champs "lastMove"*/
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
				
				// si le personnage ne se déplace pas malgré l'existance d'une sur lui, il peut y avoir un problème donc on augmente le compteur
				if(lastPosition[0]==bombPosition[0] && lastPosition[1]==bombPosition[1])
					{
						repetitionCounter++;
					}
				else //si le personnage s'est déplacé pour pouvoir s'enfuir, on remet le compteur en 1
					{
						repetitionCounter=1;
					}
				//si compteur de répétition a une grande valeur donc le personnage s'est attaché vraiment à une bombe et on doit le sauver
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
		lastPosition[0]=x;//enregistrement de la dernière position 
		lastPosition[1]=y;//enregistrement de la dernière position
		}
		
		return result;
	}
	/**
	 * Indique si le personnage a la possibilité de s'enfuir ou pas
	 * par les coordonnées (x,y) passés en paramètre avec une bombe située en (bombX,bombY) 
	 * @param matrix la zone du jeu
	 * @param x position à étudier
	 * @param y position à étudier
	 * @param bombX positon de la bombe
	 * @param bombY positon de la bombe
	 * @return vrai si le personnage n'a aucune chance de s'enfuir
	 */
	private boolean isTrap(int[][] matrix,int x,int y,int bombX,int bombY)
	{	
		int power = getBombPowerAt(bombX,bombY);//s'il existe une bombe en ces coordonnées on parle d'un désir du défense (le personnage veut s'enfuir par une bombe)
	
		if(power==-1)//s'il n'existe pas une bombe en ces coordonnées alors on parle d'un désir de l'attaque(donc d'une simulation) et on va utiliser la portée de la bombe propre au personnage
			power=getOwnFirePower();
		
		boolean result=false;
		
		if(x-bombX>0){//si la bombe est à gauche
			int i = 1;
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers le bas c'est une piège
			if(!isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_RIGHT) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isMovePossible(x,y,ArtificialIntelligence.AI_ACTION_GO_DOWN))
			{
				result=true;
			}
			else
			{
				while(i<power)//on va étudier une distance i partant 1 jusqu'à la portée de la bombe
				{
					if(bombX+i<getZoneMatrixDimX())//si le personnage+la portée de la bombe est dans la zone du jeu
					{
						if(possibleRightX(x,y,i))//s'il est possible d'aller vers la droite pour la distance i
						{
							//s'il est impossible de trouver une place à se cacher en se déplacant pour une distance i
							if(!isMovePossible(x+i,y,ArtificialIntelligence.AI_ACTION_GO_UP) && !isMovePossible(x+i,y,ArtificialIntelligence.AI_ACTION_GO_DOWN) )
							{
								result=true;
							}
							else//il est possible de se cacher
							{
								result=false;
								break;//pas besoin de continuer à étudier
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
		
		if(x-bombX<0)//si la bombe est à droite
		{
			int i = 1;
			//s'il est impossible de faire un mouvement vers la gauche, vers le haut ou vers le bas c'est une piège
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
			//s'il est impossible de faire un mouvement vers la droite, vers la gauche ou vers le bas c'est une piège
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
			//s'il est impossible de faire un mouvement vers la droite, vers le haut ou vers la gauche c'est une piège
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
		//si la bombe est à gauche et le personnage peut aller vers la droite pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		if(x-bombX>0 && possibleRightX(bombX,y,i))
			result=false;
		//si la bombe est à droite et le personnage peut aller vers la gauche pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		else if(x-bombX<0 && possibleLeftX(bombX,y,i))
			 result=false;
		//si la bombe est en bas et le personnage peut aller vers le haut pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		 if(y-bombY<0 && possibleUpY(x,bombY,i))
			 result=false;
		//si la bombe est en haut et le personnage peut aller vers le bas pour une distance plus grande que la portée de la bombe alors ce n'est pas une piège
		 else if(y-bombY>0 && possibleDownY(x,bombY,i))
			 result=false;
		 
		return result;
	}
	
	/**
	 * Indique si un personnage situé en coordonnées (x,y) passés en paramètre est en 
	 * securité ou pas par rapport à toutes les bombes qui existent dans la zone du jeu
	 * @param matrix la zone du jeu
	 * @param x la coordonnée actuelle du personnage en x
	 * @param y la coordonnée actuelle du personnage en y
	 * @return vrai s'il n'existe aucun danger pour un personnage situé en coordonnées (x,y) 
	 */
	private boolean isSafe(int[][] matrix,int x,int y)
	{	
		boolean result=true;
		
		//récupération d'une liste des bombes existantes dans la zone du jeu
		List<int[]> bombList = new ArrayList<int[]>();
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
			
				if(x+power<getZoneMatrixDimX()){ //Contrôle des limites en x vers la droite pour ne pas tomber en exception en récupérant si la portée de la bombe+la coordonnée en x du personnage est bien dans la zone du jeu
			
					if(x+power==bombList.get(i)[0] && y==bombList.get(i)[1] )//si le feu de la bombe attrape le personnage par la droite et a la chance(power) d'arriver au personnage
					{
						for(int l=1;l<power+1;l++)//On part par notre côté droite pour une distance relative à la portée de la bombe
						{
							if(matrix[x+l][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD || matrix[x+l][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT)// s'il existe une bloc entre la bombe et le personnage il est en sécurité
							{
								result =true;// personnage est en sécurité
								break;//pas besoin de continuer à Contrôler car il existe un bloc entre la bombe et le personnage
							}
							else//personnage est en danger
								result=false;
						}
					}
				}
				if(x-power>0){//Contrôle des limites en x vers la gauche
			
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
				if(y-power>0){//Contrôle des limites en y vers le haut
			
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
				if(y+power<getZoneMatrixDimY()){//Contrôle des limites en y vers le bas
			
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
				
			if(result==false)// s'il existe au moins une bombe qui met le personnage en danger on n'a pas besoin de continuer à Contrôler
			{
				bombPosition[0]=bombList.get(i)[0];
				bombPosition[1]=bombList.get(i)[1];
				break;
			}
			else//s'il existe aucun danger pour cette bombe on passe à la bombe suivante
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
	 * Indique si le déplacement dont le code a été passé en paramètre 
	 * est possible pour un personnage situé en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le déplacement à étudier
	 * @return	vrai si ce déplacement est possible
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
	 * Indique si la case située à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à étudier
	 * @param y	position à étudier
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
	 * Indique s'il est possible d'aller vers la droite par les coordonnées (x,y) pour une distance d passée en paramètre
	 * @param x position de départ en x
	 * @param y position de départ en y
	 * @param d la distance à parcourir
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
	 * Indique s'il est possible d'aller vers la gauche par les coordonnées (x,y) pour une distance d passée en paramètre
	 * @param x position de départ en x
	 * @param y position de départ en y
	 * @param d la distance à parcourir
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
	 * Indique s'il est possible d'aller vers le haut par les coordonnées (x,y) pour une distance d passée en paramètre
	 * @param x position de départ en x
	 * @param y position de départ en y
	 * @param d la distance à parcourir
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
	 * Indique s'il est possible d'aller vers le bas par les coordonnées (x,y) pour une distance d passée en paramètre
	 * @param x position de départ en x
	 * @param y position de départ en y
	 * @param d la distance à parcourir
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
	 * Indique s'il existe un autre joueur en coordonnées (x,y) passés en paramètre
	 * @param x coordonnée en x à tester
	 * @param y coordonnée en y à tester
	 * @return vrai s'il existe un autre joueur en coordonnées (x,y) passés en paramètre
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
	 * Cherche le joueur le plus proche au personnage et renvoie ses coordonnées
	 * @return les coordonnées du joueur le plus proche au personnage
	 */
	private int[] getClosestPlayerPosition()
	{
		
		int i=0;
		int[] position={0,0};
		int distance=100;
		
		while(i<=getPlayerCount())//pour tous les joueurs
		{
			//si le joueur i est plus proche au personnage on récupére ses coordonnées
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
