package org.totalboumboum.ai.v201011.ais.kesimalvarol.v3;

import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
//import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
//import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Gestion de deplacement & posage des bombes
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings("deprecation")
public class MovementController {
	
	/*
	private class costCalc extends CostCalculator
	{
		@SuppressWarnings("unused")
		private Matrix m;
		public costCalc(Matrix m)
		{
			super();
			this.m=m;
		}
		
		@Override
		public double processCost(AiTile start, AiTile end)
				throws StopRequestException {
			return monIA.getZone().getTileDistance(start,end);
		}
	}
	private class heurCalc extends HeuristicCalculator
	{
		private Matrix m;
		public heurCalc(Matrix m)
		{
			super();
			this.m=m;
		}
		@Override
		public double processHeuristic(AiTile tile) throws StopRequestException {
			double c=m.representation[tile.getLine()][tile.getCol()];
			if(m.representation[tile.getLine()][tile.getCol()]-(monIA.getCurrentToleranceCofficient()/2.5)<=-300)
				return 300;
			else
				return -c;
		}
		
	}*/

	/**Pour checkInterruption.*/
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) {
		MovementController.monIA = monIA;
	}
	
	/** Chemins a suivre */
	private AiPath lastPath,newPath;
	
	private AiTile bombSpecialRequest;
	public AiTile getBombSpecialRequest() {
		return bombSpecialRequest;
	}

	public void setBombSpecialRequest(AiTile bombSpecialRequest) {
		this.bombSpecialRequest = bombSpecialRequest;
	}

	public MovementController()
	{
		//...
	}
	
	/**
	 * Methode invoquee pour determiner l'action suivante, explicitement pour prevoir une bombe
	 * 
	 * @param m La matrice sur laquelle nos calculs se baseront
	 * 
	 * @return L'action a utiliser
	 */
	public AiAction commitMovementForFutureBomb(Matrix m) throws StopRequestException
	{
		return commitMovement(m,true);
	}
	
	/**
	 * Methode invoquee pour determiner l'action suivante
	 * 
	 * @param m La matrice sur laquelle nos calculs se baseront
	 * 
	 * @return L'action a utiliser
	 */
	public AiAction commitMovement(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		return commitMovement(m,false);
	}
	
	/**
	 * 
	 * @param p Le chemin a utiliser
	 * @param m La matrice a utiliser
	 * @return Si le chemin n'est pas en surete
	 * @throws StopRequestException
	 */
	private boolean isThisPathDangerous(AiPath p, Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		return isThisPathDangerous(p, m,0);
	}
	
	/**
	 * 
	 * @param p Le chemin a utiliser
	 * @param m La matrice a utiliser
	 * @param offset Si on ne commence pas de la case 1er du chemin
	 * @return Si le chemin n'est pas en surete
	 * @throws StopRequestException
	 */
	private boolean isThisPathDangerous(AiPath p, Matrix m, int offset) throws StopRequestException
	{
		monIA.checkInterruption();
		List<AiTile> tiles=p.getTiles();
		int CumulativeInterest=0;
		int stepsTaken=0;
		int safePlaceFoundBeforeDanger=-1;
		if(offset==p.getLength()-1)
			return true;
		if(tiles.size()>0) {
			//!!
		}
		for(int i=offset;i<tiles.size();i++)
		{
			AiTile t=p.getTile(i);
			if(stepsTaken>=1 && m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/2.5)*stepsTaken>=0) //-280
			{
				safePlaceFoundBeforeDanger=stepsTaken;
			}
			if(monIA.getSelfHero().getTile().getBombs().size()>0 && (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
			{
				//!!
				return true;
			}
			if(monIA.getMode()==Mode.COLLECTE)
				if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
				{
					//!!
					return true;
				}
			/*
			if(monIA.getMode()==Mode.ATTAQUE)
				if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
				{
					//!!
					retY=0;
					break;
				}*/
			if(t.getBombs().size()>0 && i!=offset)
				{
					//!!
					return true;
				}
			if(m.representation[t.getLine()][t.getCol()]<0)
			{
				if(m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/2.5)*stepsTaken<=-290 && i!=offset)
				{
					//!!
					//!!
					if(safePlaceFoundBeforeDanger!=-1)
					{
						//!!
					}
					else
						return true;
				}
			}
			CumulativeInterest+=m.representation[t.getLine()][t.getCol()];
			stepsTaken++;
			//count++;
			//retY=1; -270 -202 olayi
		}
		return false;
	}
	
	/**
	 * Determine si le chemin recemment choisi est devenu dangereux
	 * @param m La matrice d'interet
	 * @return Oui si le chemin recemment choisi est devenu dangereux.
	 * @throws StopRequestException
	 */
	private boolean hasLastPathBecomeDangerousOrObsolete(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		//!!
		
		if (monIA.lastPathChosenParameters==null)
			return true;
		
		AiPath p=monIA.lastPathChosenParameters.getLastPathChosen();
		int offset=monIA.lastPathChosenParameters.getCurrentPosition();
		return isThisPathDangerous(p,m,offset);
	}
	
	/**
	 * Methode invoquee pour determiner l'action suivante
	 * 
	 * @param m La matrice sur laquelle nos calculs se baseront
	 * @param invokedForSafeBombPlacement Oui si commitMovement est appelee pour prevoir si on peut laisser une bombe ou non
	 * 
	 * @return L'action a utiliser
	 */
	public AiAction commitMovement(Matrix m,boolean invokedForSafeBombPlacement) throws StopRequestException
	{
		monIA.checkInterruption();
		
		//AiTile forcedMovement=null;
		
		//costCalc cc=new costCalc(m);
		//heurCalc hc=new heurCalc(m); 
		double[][] costMatrix=new double[m.height][m.width];
		for(int i=0;i<m.height;i++)
		{
			for(int j=0;j<m.width;j++)
			{
				costMatrix[i][j]=-m.representation[i][j];
			}
		}
		MatrixCostCalculator cc=new MatrixCostCalculator(m.representation);
		PixelHeuristicCalculator hc=new PixelHeuristicCalculator();
		Astar astar=new Astar(monIA, monIA.getSelfHero(), cc, hc);

		
		//if(monIA.lastTargetCumulativeInterest)
		//int CumulativeInterest;
		
		//Attack : Path to enemies ? Yes : Proceed. No : Add wall variables and continue.
		if(monIA.getMode()==Mode.ATTAQUE)
		{
			// : astar too much time ?
			boolean enablewalls=true;
			for(AiHero ah : monIA.getZone().getHeroes())
			{
				if(ah!=monIA.getSelfHero())
				{
					AiPath path=null;
					try {
						path=astar.processShortestPath(monIA.getSelfHero().getTile(),ah.getTile());
					}
					catch(Exception e)
					{
						
					}
					if(path!=null && path.getLength()>0)
					{
						enablewalls=false;
						break;
					}
				}
			}
			if(enablewalls)
			{
				////!!
				monIA.requestWallEffects(m, 10);
				m.bestDirectionOptimisation();
			}
		}
		
		if(!invokedForSafeBombPlacement) //si appel pour emplacement des bombes, on passe au suivante
		{
			//Trouves-moi sur une case d'emplacement ?
			AiTile moniacurrtile=monIA.getSelfHero().getTile();
			if(moniacurrtile.getBombs().size()==0)
			{
				if(m.regionImportanceMatrix[moniacurrtile.getLine()][moniacurrtile.getCol()]>0)
				{
					//!!
					if(monIA.isBombDropPermitted(m)) {
						monIA.willReleaseBomb();
						return new AiAction(AiActionName.NONE); 
						//return new AiAction(AiActionName.DROP_BOMB); 
					}
					
					else {
						bombSpecialRequest=null;
						//et bonus tile au dessous.
					}
					//!!
				}
			}
			
			//On ne considere pas le chemin courante de meme.
			if(bombSpecialRequest==null && !hasLastPathBecomeDangerousOrObsolete(m))
			{
				//!!
				AiTile dest=monIA.lastPathChosenParameters.getLastPathChosen().getTile(monIA.lastPathChosenParameters.getCurrentPosition()+1);
				Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
				AiAction movement=new AiAction(AiActionName.MOVE,dir);
				////!!
				return movement;
			}
			/*
			else
				monIA.lastPathChosenParameters=null;
			*/
		}
		

		
		//Sinon,dirige vers une case		
		//return new AiAction(AiActionName.NONE);
		
		if(m.representation[monIA.getSelfHero().getLine()][monIA.getSelfHero().getCol()]<0)
		{
			for(int i=0;i<m.height;i++)
				for(int j=0;j<m.width;j++)
				{
					m.representation[i][j]+=m.representationDistanceParameters[i][j];
				}
		}
		
		double[] hval=new double[4];
		int[] hI=new int[4];
		int[] hJ=new int[4];
		if(true)//(!invokedForSafeBombPlacement)
		{
			for(int i=0;i<m.height;i++)
				for(int j=0;j<m.width;j++)
				{
					for(int a=0;a<4;a++)
					{
						if(m.representation[i][j]>hval[a])
						{
							double vTmp=hval[a];
							int iTmp=hI[a],jTmp=hJ[a];
							hval[a]=m.representation[i][j];
							hI[a]=i; hJ[a]=j;
							for(int offset=a+1;offset<4;offset++)
							{
								double vTmp2=hval[offset];
								int iTmp2=hI[offset],jTmp2=hJ[offset];
								hval[offset]=vTmp; hI[offset]=iTmp; hJ[offset]=jTmp;
								vTmp=vTmp2;iTmp=iTmp2;jTmp=jTmp2;
							}
							break;
						}
					}
				}
		}
		
		//!!
		for(int i=0;i<4;i++) {
			//!!
		}
		
		int a=0;
		boolean usedClosestCase=false;
		/*if(invokedForSafeBombPlacement)
			a=4;*/
		//a=4;
		while(a<4 || !usedClosestCase)
		{
			@SuppressWarnings("unused")
			double currhval=0;
			newPath=null;
			AiTile target=null; //
			
			if(a==4) {
				//!!
				
				//
				/*
				if(m.representation[monIA.getSelfHero().getTile().getLine()][monIA.getSelfHero().getTile().getCol()]<-200)
				{
					//!!
					target=monIA.getReachableSafestLocation(m);
					//!!
					if(target==null)
						return new AiAction(AiActionName.NONE);
				}*/
				if(true)//else 
				{
					////!!
					target=monIA.getReachablePreferableLocation(m);
					usedClosestCase=true;
					if(target==null) {
						return new AiAction(AiActionName.NONE);
					}
					////!!
				}
				currhval=m.representation[target.getLine()][target.getCol()];
				//!!
			}
			else {
				//!!
				if(bombSpecialRequest!=null)
				{
					target=bombSpecialRequest;
					bombSpecialRequest=null;
					a--;
				}
				else {
					AiTile b=monIA.getPrBonusTile();
					if(b!=null)
					{
						//!!
						target=b;
						a--;
						monIA.setPrBonusTile(null);
						//m.requestBonusImportanceIncrease();
					}
					else {
						target=monIA.getZone().getTile(hI[a], hJ[a]);
						currhval=m.representation[target.getLine()][target.getCol()];
					}
				}
			}
			
			try {
				//!!
				newPath=astar.processShortestPath(monIA.getSelfHero().getTile(),target);
				/*AiTile dest=newPath.getTile(0);
				Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
				return new AiAction(AiActionName.MOVE, dir);*/
				
				//CumulativeInterest=0;
				
				short retY=2;
				if(isThisPathDangerous(newPath,m))
					retY=0;
				
				/*
				
				List<AiTile> tiles=newPath.getTiles();
				short count=0;
				for(AiTile t : tiles)
				{
					if(monIA.getSelfHero().getTile().getBombs().size()>0 && (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
					{
						//!!
						retY=0;
						break;
					}
					if(monIA.getMode()==Mode.COLLECTE)
						if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
						{
							//!!
							retY=0;
							break;
						}
					/*
					if(monIA.getMode()==Mode.ATTAQUE)
						if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
						{
							//!!
							retY=0;
							break;
						}//*-/
					if(t.getBombs().size()>0 && t!=newPath.getTile(0))
						{
							//!!
							retY=0;
							break;
						}
					if(m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/2.5)<=-300 && t!=newPath.getTile(0))
					{
						//!!
						//!!
						retY=0;
						break;
					}
					CumulativeInterest+=m.representation[t.getLine()][t.getCol()];
					count++;
					//retY=1; -270 -202 olayi
				}*/
/*				if(lastPath==null || lastPath.getTiles().size()>0)
					retY=0;
*/
				
				//Inertie de chemin prp astar , le plus proche est conduite par lui-meme, pas ici. todo: In danger, contains more
				/*if((lastPath==null) || ( (newPath.getLastTile()!=lastPath.getLastTile()) && monIA.lastTargetCumulativeInterest<CumulativeInterest) || (newPath.getLastTile()==lastPath.getLastTile() && newPath.getLength()<lastPath.getLength()))
					lastPath=newPath;*/
				//!!
				
				lastPath=newPath;
				
				if(retY==2)
				{
					try {
						if(lastPath.getLength()>=2) { //2
							AiTile dest=lastPath.getTile(1);
							//!!
							Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
							//return new AiAction(AiActionName.NONE);
							/*//!!
							//!!
							//!!
							//monIA.setLastAction(new AiAction(AiActionName.MOVE,dir));
							
							if(!invokedForSafeBombPlacement)
								monIA.requestNewPath(lastPath,0);
							
							else if(monIA.isPredicting()) {
								monIA.requestSpecialBombEvasiveTarget(target);
							}
							
							/*
							if(a==4 && monIA.getLastTarget()==null || (monIA.getSelfHero().getTile()==monIA.getLastTarget()) && lastPath.getLastTile()!=monIA.getSelfHero().getTile()) {
								//!!
								monIA.setLastTarget(lastPath.getLastTile());
								monIA.setLastAction(new AiAction(AiActionName.MOVE,dir));
								monIA.lastTargetCumulativeInterest=CumulativeInterest;
							}
							else {
								//if(m.representation[monIA.getLastTarget().getLine()][monIA.getLastTarget().getCol()]<=currhval) {
								if(monIA.lastTargetCumulativeInterest<CumulativeInterest){
									//!!
									monIA.setLastTarget(lastPath.getLastTile());
									monIA.setLastAction(new AiAction(AiActionName.MOVE,dir));
									monIA.lastTargetCumulativeInterest=CumulativeInterest;
								}
								else {
									//!!
									AiAction movement=new AiAction(AiActionName.MOVE,dir);
									//memoriser au lieu de iterer a chaque fois
									/-*
									for(int tcount=0;tcount<lastPath.getLength()-1;tcount++)
									{
										if(monIA.getSelfHero().getTile()==lastPath.getTile(tcount))
										{
											Direction dir2=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), lastPath.getTile(tcount+1));
											movement=new AiAction(AiActionName.MOVE,dir2);
											break;
										}
									}--*-/
									monIA.setLastAction(movement);
								}
							}
							*/
							//return monIA.getLastAction();
							return new AiAction(AiActionName.MOVE,dir);
						}
						else if (lastPath.getLength()==1) {
							//!!
						}
					}
					catch (Exception e)
					{
						//!!
					}
				}
			}
			catch (LimitReachedException e) {
				//!!
			}
			catch (Exception e) {
				//!!
			}
			a++;
		}
		
		//
		//m.applyDistanceToAll();
		
		//!!
		return new AiAction(AiActionName.NONE); 
	}
	
	public void getNearestPositiveLocation()
	{
		
	}
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	/**
	 * Dessin d'un chemin sur ecran
	 */
	public void drawPathOnScreen()
	{
		AiOutput out = monIA.getOutput();
		out.addPath(lastPath, new Color(0, 255, 0));
	}
}
