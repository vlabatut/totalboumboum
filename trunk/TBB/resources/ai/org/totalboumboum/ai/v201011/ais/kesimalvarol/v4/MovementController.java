package org.totalboumboum.ai.v201011.ais.kesimalvarol.v4;

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
//import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
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
			if(monIA.verbose) System.out.println("Path security check , start from "+p.getTile(offset));
		}
		for(int i=offset;i<tiles.size();i++)
		{
			AiTile t=p.getTile(i);
			if(stepsTaken>=1 && m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())*stepsTaken>=0) //-280
			{
				safePlaceFoundBeforeDanger=stepsTaken;
			}
			if((monIA.isPredicting() || monIA.getSelfHero().getTile().getBombs().size()>0) && (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
			{
				if(monIA.verbose) System.out.println("POTENTIAL TRAP INCOMING");
				/*
				boolean willEvade=false;
				for(AiHero a : t.getHeroes())
				{
					if (a.getBombNumberMax()-a.getBombNumberCurrent()>0)
						willEvade=true;
				}
				if(willEvade) {
					if(monIA.verbose) System.out.println("IT'S A TRAP");
					return true;
				}*/
				return true;
			}
			if(monIA.getMode()==Mode.COLLECTE)
				if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
				{
					if(monIA.verbose) System.out.println("HEROES");
					return true;
				}
			/*
			if(monIA.getMode()==Mode.ATTAQUE)
				if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
				{
					if(monIA.verbose) System.out.println("HEROEZ");
					retY=0;
					break;
				}*/
			if(t.getBombs().size()>0 && i!=offset)
				{
					if(monIA.verbose) System.out.println("BOMB SIZE");
					return true;
				}
			if(m.representation[t.getLine()][t.getCol()]<0)
			{
				if(m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())*stepsTaken<=-290 && i!=offset)
				{
					if(monIA.verbose) System.out.println("DANGER");
					if(monIA.verbose) System.out.println(m.representation[t.getLine()][t.getCol()]-stepsTaken*(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())+" and step count "+stepsTaken+","+p.getTile(stepsTaken));
					if(safePlaceFoundBeforeDanger!=-1)
					{
						if(monIA.verbose) System.out.println("But found an empty case before : "+stepsTaken+","+p.getTile(stepsTaken));
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
		if(monIA.verbose) System.out.println("Looking for obselete check");
		
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
				costMatrix[i][j]=-m.representation[i][j];//300-m.representation[i][j];
			}
		}
		MatrixCostCalculator cc=new MatrixCostCalculator(costMatrix);
		PixelHeuristicCalculator hc=new PixelHeuristicCalculator();
		//BasicHeuristicCalculator hc=new BasicHeuristicCalculator();
		Astar astar=new Astar(monIA, monIA.getSelfHero(), cc, hc);

		//if(monIA.lastTargetCumulativeInterest)
		//int CumulativeInterest;
		
		//Attack -> Path to enemies ? Proceed : Add wall variables and continue.
		if(monIA.getMode()==Mode.ATTAQUE)
		{
			// : astar too much time ?
			boolean enablewalls=true;
			if(monIA.verbose) System.out.println("Bottleneck1");
			for(AiHero ah : monIA.getZone().getHeroes())
			{
				if(ah!=monIA.getSelfHero())
				{
					AiTile tileToEvaluate=ah.getTile();
					//A* : Si l'adversaire avait laissee une bombe au moment de calcul, on aura une calcul immensement lent.
					if(tileToEvaluate.getBombs().size()==0 && tileToEvaluate.getFires().size()==0) 
					{
						AiPath path=null;
						try {
							path=astar.processShortestPath(monIA.getSelfHero().getTile(),tileToEvaluate);
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
			}
			if(monIA.verbose) System.out.println("Bottleneck2");
			if(enablewalls)
			{
				//if(monIA.verbose) System.out.println("Wall addon made");
				monIA.requestWallEffects(m, 10);
				m.bestDirectionOptimisation();
			}
			if(monIA.verbose) System.out.println("Bottleneck3");
		}
		
		
		if(!invokedForSafeBombPlacement) //si appel est pour emplacement des bombes, on laisse celle-ci a tomber
		{
			//Trouves-moi sur une case d'emplacement ?
			AiTile moniacurrtile=monIA.getSelfHero().getTile();
			if(moniacurrtile.getBombs().size()==0)
			{
				if(m.regionImportanceMatrix[moniacurrtile.getLine()][moniacurrtile.getCol()]>0)
				{
					if(monIA.verbose) System.out.println("Is bomb permitted");
					if(monIA.isBombDropPermitted(m)) {
						monIA.willReleaseBomb();
						return new AiAction(AiActionName.NONE); 
						//return new AiAction(AiActionName.DROP_BOMB); 
					}
					
					else {
						bombSpecialRequest=null;
						//et bonus tile au dessous.
					}
					if(monIA.verbose) System.out.println("Bomb is not permitted");
				}
			}
			
			//On ne considere pas le chemin courante de meme.
			if(bombSpecialRequest==null && !hasLastPathBecomeDangerousOrObsolete(m))
			{
				if(monIA.verbose) System.out.println("Nope, not obsolete");
				AiTile dest=monIA.lastPathChosenParameters.getLastPathChosen().getTile(monIA.lastPathChosenParameters.getCurrentPosition()+1);
				Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
				AiAction movement=new AiAction(AiActionName.MOVE,dir);
				//if(monIA.verbose) System.out.println(monIA.lastPathChosenParameters.getCurrentPosition());
				return movement;
			}
			/*
			else
				monIA.lastPathChosenParameters=null;
			*/
		}
		
		if(m.representation[monIA.getSelfHero().getLine()][monIA.getSelfHero().getCol()]<0)
		{
			for(int i=0;i<m.height;i++)
				for(int j=0;j<m.width;j++)
				{
					m.representation[i][j]+=m.representationDistanceParameters[i][j];
				}
		}
		
		short changedUpTo=0; // Pour eviter une partie de ralentissement dans les zones ayant des frontieres.
		double[] hval=new double[4];
		int[] hI=new int[4];
		int[] hJ=new int[4];
		/*
		for(int i=0;i<4;i++)
		{
			hI[i]=monIA.getSelfHero().getTile().getLine();
			hJ[i]=monIA.getSelfHero().getTile().getCol();
			hval[i]=m.representation[monIA.getSelfHero().getTile().getLine()][monIA.getSelfHero().getTile().getCol()];
		}*/

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
							changedUpTo++;
							break;
						}
					}
				}
		}
		if(changedUpTo>4)
			changedUpTo=4;
		
		if(monIA.verbose) System.out.println("Greatest variables found : ");
		for(int i=0;i<4;i++) {
			if(monIA.verbose) System.out.println(hI[i]+","+hJ[i]+":"+hval[i]);
		}
		
		int a=0;
		boolean usedClosestCase=false;
		/*if(invokedForSafeBombPlacement)
			a=4;*/
		//a=4;
		//changedUpTo=4;
		if(monIA.verbose) System.out.println("Changedupto var : "+changedUpTo);
		while(a<changedUpTo || !usedClosestCase)
		{
			@SuppressWarnings("unused")
			double currhval=0;
			newPath=null;
			AiTile target=null; //
			
			if(a==changedUpTo) {
				if(monIA.verbose) System.out.println("Will look for last chance");
				
				//if(monIA.verbose) System.out.println("Preferable for bomb");
				target=monIA.getReachablePreferableLocation(m);
				usedClosestCase=true;
				if(target==null) {
					return new AiAction(AiActionName.NONE);
				}
				//if(monIA.verbose) System.out.println("Bomb target found ");
				
				currhval=m.representation[target.getLine()][target.getCol()];
				if(monIA.verbose) System.out.println("Target is closest now, "+target);
			}
			else {
				if(monIA.verbose) System.out.println("Bomb special req : "+bombSpecialRequest);
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
						if(monIA.verbose) System.out.println("Bonus tile before bomb "+target);
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
				if(monIA.verbose) System.out.println("Processing for target : "+target);
				if(monIA.getZone().getTileDistance(monIA.getSelfHero().getTile(), target)==1) {
					newPath=new AiPath();
					newPath.addTile(monIA.getSelfHero().getTile());
					newPath.addTile(target);
				}
				else if(target.getBombs().size()==0 && target.getFires().size()==0) //Pas vraiment necessaire mais meme raison que celui pour "chemin aux adversaires"
					newPath=astar.processShortestPath(monIA.getSelfHero().getTile(),target);
				
				short retY=2;
				if(isThisPathDangerous(newPath,m))
					retY=0;
				
				if(monIA.verbose) System.out.println("Processed for target : "+target);
				
				lastPath=newPath;
				
				if(retY==2)
				{
					try {
						if(lastPath.getLength()>=2) { //2
							AiTile dest=lastPath.getTile(1);
							if(monIA.verbose) System.out.println("Astar for target "+lastPath.getLastTile());
							Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
							
							if(!invokedForSafeBombPlacement)
								monIA.requestNewPath(lastPath,0);
							
							else if(monIA.isPredicting()) {
								monIA.requestSpecialBombEvasiveTarget(target);
							}
							return new AiAction(AiActionName.MOVE,dir);
						}
						else if (lastPath.getLength()==1) {
							if(monIA.verbose) System.out.println("Path too short for "+target);
						}
					}
					catch (Exception e)
					{
						if(monIA.verbose) System.out.println("Ees "+e);
					}
				}
			}
			catch (LimitReachedException e) {
				if(monIA.verbose) System.out.println("sdasdadad "+e);
			}
			catch (Exception e) {
				if(monIA.verbose) System.out.println("sdasdadad 2 "+e);
			}
			a++;
		}
		
		//
		//m.applyDistanceToAll();
		
		if(monIA.verbose) System.out.println("EeEsadkfsjfjdsfjs");
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
