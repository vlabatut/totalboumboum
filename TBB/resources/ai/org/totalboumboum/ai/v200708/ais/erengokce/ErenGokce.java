package org.totalboumboum.ai.v200708.ais.erengokce;

import java.util.Vector;
import java.util.concurrent.ExecutionException;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Can Eren
 * @author Mustafa Mert Gökçe
 *
 */
@SuppressWarnings("deprecation")
public class ErenGokce extends ArtificialIntelligence {

	/**
	 * 
	 */
	//variable controleur(calcul une seul fois) du fonction ou ai-je commence? whereDidIStart()
	int once=0;
	//les positions du shrink
	int shrinkx=1000;
	int shrinky=1000;
	//les booleens indiquant si les positons but sont acquris 
	boolean xaccomplished=false,yaccomplished=false;
	int startx=15;
	int starty=13;
	//right top corner, left top corner, ..etc la position du joueur au debut
	boolean rtc=false,ltc=false,rdc=false,ldc=false;
	private static final long serialVersionUID = 1L; 
	//shrink a-t-elle commencé ou pas, y-a-t-il une bombe ou pas
	boolean mybomb=false,shrink=false;
	//position du bombe, dernier mouvement
	int[] bombpos=new int[2];
	int lastmove;
	int i=0;
	public ErenGokce() {
			super("GokceEren");
		//  Auto-generated constructor stub
	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer call() throws Exception {
		Integer result=AI_ACTION_DO_NOTHING;
		
		if(firstTime)
			firstTime = false;
		else
		{	
		//controle shrink
		if(getTimeBeforeShrink()<=0)
		{
			shrink=true;
		}
		int zoneMatrix[][]=getZoneMatrix();
		int[] own=getOwnPosition();
		boolean up,down,left,right;
		up=isMovePossible(own[0],own[1],AI_ACTION_GO_UP);
		down=isMovePossible(own[0],own[1],AI_ACTION_GO_DOWN);
		left=isMovePossible(own[0],own[1],AI_ACTION_GO_LEFT);
		right=isMovePossible(own[0],own[1],AI_ACTION_GO_RIGHT);
		whereDidIStart(up, down, left, right);
		//bombe dans l'environnement
		for(int i=0;i<=16;i++)
			for(int j=0;j<=14;j++)
				if(zoneMatrix[i][j]==AI_BLOCK_BOMB)
				{
					bombpos[0]=i;
					bombpos[1]=j;
					mybomb=true;
				}
		//pas de bombe
		if(!mybomb)
		{
			//mais shrink.
			if(shrink==true)
			{
				result=shrinkDanger(up,down,left,right,own[0],own[1]);
			}
			else
			{

			}
		}
		//une bombe
		else
		{
			int power=getBombPowerAt(bombpos[0], bombpos[1]);
			//si on se trompe, explosion prematuree
			if(power==-1)
			{
				mybomb=false;
				return AI_ACTION_DO_NOTHING;
			}
			//la bombe me concerne-t-il
			if(((bombpos[0]==own[0])&&((Math.abs(bombpos[1]-own[1]))<=power))||((bombpos[1]==own[1])&&((Math.abs(bombpos[0]-own[0]))<=power)))
			{
				//en danger - aetoile
				result=danger(up,down,left,right,own[0],own[1]);
				//au cas ou il y a un pb avec l'algorithme aetoile
				if(result==AI_ACTION_DO_NOTHING)
				{
//					System.out.println("Second Algorithm of Protection");
					if(bombpos[0]==own[0])
					{
						if(right&&lastmove!=AI_ACTION_GO_LEFT)
							{
							result=AI_ACTION_GO_RIGHT;
							}
						else
						{
							if(left&&lastmove!=AI_ACTION_GO_RIGHT)
								result=AI_ACTION_GO_LEFT;
							else
							if(up&&lastmove!=AI_ACTION_GO_DOWN)
								result=AI_ACTION_GO_UP;
							else
							{
								if(down&&lastmove!=AI_ACTION_GO_UP)
									result=AI_ACTION_GO_DOWN;
							}
						}
					}
					if(bombpos[1]==own[1])
					{
						if(up&&lastmove!=AI_ACTION_GO_DOWN)
							result=AI_ACTION_GO_UP;
						else
						{
							if(down&&lastmove!=AI_ACTION_GO_UP)
								result=AI_ACTION_GO_DOWN;
							else
								if(right&&lastmove!=AI_ACTION_GO_LEFT)
									result=AI_ACTION_GO_RIGHT;
								else
								{
									if(left&&lastmove!=AI_ACTION_GO_RIGHT)
										result=AI_ACTION_GO_LEFT;
								}
						}
					}
				}
			}
			else
			{
				//la bombe me concerne pas
				mybomb=false;
				if(shrink)
				{
					//cas du shrink aEtoile
					result=shrinkDanger(up, down, left, right, own[0], own[1]);
				}
				else
				{
					result=AI_ACTION_DO_NOTHING;
				}
			}
		}
		shrinkx=getNextShrinkPosition()[0];
		shrinky=getNextShrinkPosition()[1];
		lastmove=result;
		}
		return result;
	}
/*
	private int[][] getPossiblePositions(int[] ow) {
		int pos[][]=new int[4][2];
		pos[0][0]=ow[0];
		pos[0][1]=ow[1]-1;
		pos[1][0]=ow[0]+1;
		pos[1][1]=ow[1];
		pos[2][0]=ow[0];
		pos[2][1]=ow[1]+1;
		pos[3][0]=ow[0]-1;
		pos[3][1]=ow[1];
		return pos;
		
	}
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
	private int danger(boolean up,boolean down,boolean left,boolean right,int x,int y) throws ExecutionException
	{
		//implementation des structures
		int action=AI_ACTION_DO_NOTHING;
		//la queue qui retournera les mouvements
		MoveQueue queue=new MoveQueue(x,y);
		//noeud racine de l'algo
		Node root=new Node(x,y,new Node());
		int zoneMatrix[][]=getZoneMatrix();
		int power=getBombPowerAt(bombpos[0], bombpos[1]);
		if(bombpos[0]==x&&bombpos[1]==y)
		{
			//si on est sur la bombe
			int dir=getBombPosition();
			if(dir==AI_DIR_DOWN)
				if(up)
					action=AI_ACTION_GO_UP;
				else
					if(right)
						action=AI_ACTION_GO_RIGHT;
					else
						if(left)
							action=AI_ACTION_GO_LEFT;
			if(dir==AI_DIR_UP)
				if(down)
					action=AI_ACTION_GO_DOWN;
				else
					if(right)
						action=AI_ACTION_GO_RIGHT;
					else
						if(left)
							action=AI_ACTION_GO_LEFT;
			if(dir==AI_DIR_LEFT)
				if(right)
					action=AI_ACTION_GO_RIGHT;
				else
					if(up)
						action=AI_ACTION_GO_UP;
					else
						if(down)
							action=AI_ACTION_GO_DOWN;
			if(dir==AI_DIR_RIGHT)
				if(left)
					action=AI_ACTION_GO_LEFT;
				else
					if(up)
						action=AI_ACTION_GO_UP;
					else
						if(down)
							action=AI_ACTION_GO_DOWN;
			
		}
				
		else
		{
			//la queue appele aEtoile
		queue.aStar(root, bombpos[0], bombpos[1], power, zoneMatrix);
		queue.getActionsFromWay();
		Vector<Integer> actions=queue.getActions();
		//combien de mouvement
		int howMany=actions.size();
		if(!actions.isEmpty())
		{
			//retirer le dernier mouvement afin de l'appliquer
			action=actions.remove(howMany-1);
		}
		}
	return action;
	}
	
	private int shrinkDanger(boolean up,boolean down,boolean left,boolean right,int x,int y) throws ExecutionException
	{
		//on traite le shrink comme un bombe a pouvoir 4
//		System.out.println("Shrink Danger");
		int action=AI_ACTION_DO_NOTHING;
		MoveQueue queue=new MoveQueue(x,y);
		Node root=new Node(x,y,new Node());
		int zoneMatrix[][]=getZoneMatrix();
		queue.aStar(root, shrinkx, shrinky, 4, zoneMatrix);
		queue.getActionsFromWay();
		Vector<Integer> actions=queue.getActions();
		int howMany=actions.size();
		if(!actions.isEmpty())
		{
			action=actions.remove(howMany-1);
		}

		return action;
	}
/*	
	private int findYourWay(boolean up,boolean down,boolean left,boolean right)
	{
		int action=AI_ACTION_DO_NOTHING;
		up=isMovePossible(getOwnPosition()[0],getOwnPosition()[1],AI_ACTION_GO_UP);
		down=isMovePossible(getOwnPosition()[0],getOwnPosition()[1],AI_ACTION_GO_DOWN);
		left=isMovePossible(getOwnPosition()[0],getOwnPosition()[1],AI_ACTION_GO_LEFT);
		right=isMovePossible(getOwnPosition()[0],getOwnPosition()[1],AI_ACTION_GO_RIGHT);
		while(!xaccomplished)
		{
			if(rtc)
			{
				if(left)
					action=AI_ACTION_GO_LEFT;
				else
				{
					if(getOwnPosition()[1]!=1)
					{
						if(up&&lastmove!=AI_ACTION_GO_DOWN)
							action=AI_ACTION_GO_UP;
						else
							if(right&&lastmove!=AI_ACTION_GO_LEFT)
								action=AI_ACTION_GO_RIGHT;
							else
								if(left&&lastmove!=AI_ACTION_GO_RIGHT)
									action=AI_ACTION_GO_LEFT;
					}	
					else
					{
						action=AI_ACTION_PUT_BOMB;
					}
				}
			}
			if(ltc)
			{
				if(right)
					action=AI_ACTION_GO_RIGHT;
				else
				{
					if(getOwnPosition()[1]!=1)
					{
						if(up&&lastmove!=AI_ACTION_GO_DOWN)
							action=AI_ACTION_GO_UP;
						else
							if(right&&lastmove!=AI_ACTION_GO_LEFT)
								action=AI_ACTION_GO_RIGHT;
							else
								if(left&&lastmove!=AI_ACTION_GO_RIGHT)
									action=AI_ACTION_GO_LEFT;
					}	
					else
					{
						action=AI_ACTION_PUT_BOMB;
					}
				}
			}
			if(rdc)
			{
				if(left)
				{
				action=AI_ACTION_GO_LEFT;
				}
				else
				{
					if(getOwnPosition()[1]!=13)
					{
						if(down&&lastmove!=AI_ACTION_GO_UP)
							action=AI_ACTION_GO_DOWN;
						else
							if(right&&lastmove!=AI_ACTION_GO_LEFT)
								action=AI_ACTION_GO_RIGHT;
							else
								if(left&&lastmove!=AI_ACTION_GO_RIGHT)
									action=AI_ACTION_GO_LEFT;
					}	
					else
					{
						action=AI_ACTION_PUT_BOMB;
					}
				}
			}
			if(ldc)
			{
				if(right)
					action=AI_ACTION_GO_RIGHT;
				else
				{
					if(getOwnPosition()[1]!=13)
					{
						if(up&&lastmove!=AI_ACTION_GO_DOWN)
							action=AI_ACTION_GO_UP;
						else
							if(right&&lastmove!=AI_ACTION_GO_LEFT)
								action=AI_ACTION_GO_RIGHT;
							else
								if(left&&lastmove!=AI_ACTION_GO_RIGHT)
									action=AI_ACTION_GO_LEFT;
					}	
					else
					{
						action=AI_ACTION_PUT_BOMB;
					}
				}
			}
			if(getOwnPosition()[0]==9)
				{
				xaccomplished=true;
				action=AI_ACTION_DO_NOTHING;
				}
			return action;
		}
		while(xaccomplished&&!yaccomplished)
		{
			if(rdc||ldc)
			{
				if(up)
				{
				action=AI_ACTION_GO_UP;
				}
				else
				{
					if(getOwnPosition()[0]!=9)
					{
						if(right&&lastmove!=AI_ACTION_GO_LEFT)
							action=AI_ACTION_GO_RIGHT;
						else
							if(left&&lastmove!=AI_ACTION_GO_RIGHT)
								action=AI_ACTION_GO_LEFT;
							else
								if(down&&lastmove!=AI_ACTION_GO_UP)
									action=AI_ACTION_GO_DOWN;
					}	
					else
					{
						action=AI_ACTION_PUT_BOMB;
					}
				}
				if(getOwnPosition()[1]==6)
				{
				yaccomplished=true;
				action=AI_ACTION_DO_NOTHING;
				}
			}
			if(rtc||ltc)
			{
				if(down)
				{
				action=AI_ACTION_GO_DOWN;
				}
				else
				{
					if(getOwnPosition()[0]!=9)
					{
						if(right&&lastmove!=AI_ACTION_GO_LEFT)
							action=AI_ACTION_GO_RIGHT;
						else
							if(left&&lastmove!=AI_ACTION_GO_RIGHT)
								action=AI_ACTION_GO_LEFT;
							else
								if(up&&lastmove!=AI_ACTION_GO_DOWN)
									action=AI_ACTION_GO_UP;
					}	
					else
					{
						action=AI_ACTION_PUT_BOMB;
					}
				}
				if(getOwnPosition()[0]==6)
				{
				yaccomplished=true;
				action=AI_ACTION_DO_NOTHING;
				}
			}
			
			return action;
		}
		return action;
	}
*/	
	private void whereDidIStart(boolean up,boolean down,boolean left,boolean right)
	{
		if(once<=0)
		{
//			System.out.println("X: "+startx+" Y: "+starty);
			if(down&&right)
				{
//				System.out.println("ltc");
				ltc=true;
				}
			if(down&&left)
				{
//				System.out.println("rtc");
				rtc=true;
				}
			if(up&&right)
				{
//				System.out.println("ldc");
				ldc=true;
				}
			if(up&&left)
				{
//				System.out.println("rdc");
				rdc=true;
				}
			once++;	
		}
	}
	
}
