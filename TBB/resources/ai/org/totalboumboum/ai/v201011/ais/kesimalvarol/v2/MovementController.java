package org.totalboumboum.ai.v201011.ais.kesimalvarol.v2;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
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
		
	}
	
	/**Pour checkInterruption.*/
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) {
		MovementController.monIA = monIA;
	}
	
	/** Chemins a suivre */
	private AiPath lastPath,newPath;
	
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
		return commitMovement(m,false);
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
		
		costCalc cc=new costCalc(m);
		heurCalc hc=new heurCalc(m); 
		Astar astar=new Astar(monIA, monIA.getSelfHero(), cc, hc);
		
		//if(monIA.lastTargetCumulativeInterest)
		int CumulativeInterest;
		
		//Attack : Path to enemies ? Yes : Proceed. No : Add wall variables and continue.
		if(monIA.getMode()==Mode.ATTAQUE)
		{
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
				////!!System.out.println("Wall addon made");
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
					//!!System.out.println("Is bomb permitted");
					if(monIA.isBombDropPermitted(m)) {
						monIA.willReleaseBomb();
						return new AiAction(AiActionName.NONE); 
					}
					
					else {
						AiTile b=monIA.getPrBonusTile();
						if(b!=null)
						{
							//m.requestBonusImportanceIncrease();
						}
					}
					//!!System.out.println("Bomb is not permitted");
				}
			}
		}
		
		//Sinon,dirige vers une case		
		//return new AiAction(AiActionName.NONE);
		
		int[] hval=new int[4];
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
							int vTmp=hval[a],iTmp=hI[a],jTmp=hJ[a];
							hval[a]=m.representation[i][j];
							hI[a]=i; hJ[a]=j;
							for(int offset=a+1;offset<4;offset++)
							{
								int vTmp2=hval[offset],iTmp2=hI[offset],jTmp2=hJ[offset];
								hval[offset]=vTmp; hI[offset]=iTmp; hJ[offset]=jTmp;
								vTmp=vTmp2;iTmp=iTmp2;jTmp=jTmp2;
							}
							break;
						}
					}
				}
		}
		
		//!!System.out.println("Greatest variables found : "+hval[0]+" "+hval[1]+" "+hval[2]+" "+hval[3]);
		
		int a=0;
		boolean usedClosestCase=false;
		/*if(invokedForSafeBombPlacement)
			a=4;*/
		//a=4;
		while(a<4 || !usedClosestCase)
		{
			@SuppressWarnings("unused")
			int currhval=0;
			AiTile target;
			
			if(a==4) {
				//!!System.out.println("Will look for last chance");
				
				//
				/*
				if(m.representation[monIA.getSelfHero().getTile().getLine()][monIA.getSelfHero().getTile().getCol()]<-200)
				{
					//!!System.out.println("Safest");
					target=monIA.getReachableSafestLocation(m);
					//!!System.out.println("Safest");
					if(target==null)
						return new AiAction(AiActionName.NONE);
				}*/
				if(true)//else 
				{
					////!!System.out.println("Preferable for bomb");
					target=monIA.getReachablePreferableLocation(m);
					usedClosestCase=true;
					if(target==null) {
						return new AiAction(AiActionName.NONE);
					}
					////!!System.out.println("Bomb target found ");
				}
				currhval=m.representation[target.getLine()][target.getCol()];
				//!!System.out.println("Target is closest now, "+target);
			}
			else {
				target=monIA.getZone().getTile(hI[a], hJ[a]);
				currhval=m.representation[target.getLine()][target.getCol()];
			}
			
			try {
				//!!System.out.println("Processing for target : "+target);
				newPath=astar.processShortestPath(monIA.getSelfHero().getTile(),target);
				/*AiTile dest=newPath.getTile(0);
				Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
				return new AiAction(AiActionName.MOVE, dir);*/
				
				CumulativeInterest=0;
				
				short retY=2;
				List<AiTile> tiles=newPath.getTiles();
				short count=0;
				for(AiTile t : tiles)
				{
					if(monIA.getSelfHero().getTile().getBombs().size()>0 && (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
					{
						//!!System.out.println("IT'S A TRAP");
						retY=0;
						break;
					}
					if(monIA.getMode()==Mode.COLLECTE)
						if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
						{
							//!!System.out.println("HEROES");
							retY=0;
							break;
						}
					/*
					if(monIA.getMode()==Mode.ATTAQUE)
						if(t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
						{
							//!!System.out.println("HEROEZ");
							retY=0;
							break;
						}*/
					if(t.getBombs().size()>0 && t!=newPath.getTile(0))
						{
							//!!System.out.println("BOMB SIZE");
							retY=0;
							break;
						}
					if(m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/2.5)<=-300 && t!=newPath.getTile(0))
					{
						//!!System.out.println();
						//!!System.out.println("DANGER");
						retY=0;
						break;
					}
					CumulativeInterest+=m.representation[t.getLine()][t.getCol()];
					count++;
					//retY=1; -270 -202 olayi
				}
/*				if(lastPath==null || lastPath.getTiles().size()>0)
					retY=0;
*/
				
				//Inertie de chemin prp astar , le plus proche est conduite par lui-meme, pas ici. todo: In danger, contains more
				/*if((lastPath==null) || ( (newPath.getLastTile()!=lastPath.getLastTile()) && monIA.lastTargetCumulativeInterest<CumulativeInterest) || (newPath.getLastTile()==lastPath.getLastTile() && newPath.getLength()<lastPath.getLength()))
					lastPath=newPath;*/
				//!!System.out.println("Processed for target : "+target);
				
				lastPath=newPath;
				
				if(retY==2)
				{
					try {
						if(lastPath.getLength()>=2) { //2
							AiTile dest=lastPath.getTile(1);
							//!!System.out.println("Astar for target "+lastPath.getLastTile());
							Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
							//return new AiAction(AiActionName.NONE);
							/*//!!System.out.println(dir);
							//!!System.out.println(dest);
							//!!System.out.println(monIA.getSelfHero().getTile());*/
							//monIA.setLastAction(new AiAction(AiActionName.MOVE,dir));
							if(a==4 && monIA.getLastTarget()==null || (monIA.getSelfHero().getTile()==monIA.getLastTarget()) && lastPath.getLastTile()!=monIA.getSelfHero().getTile()) {
								//!!System.out.println("Path renewing for arrival "+monIA.getLastTarget()+ " to "+lastPath.getLastTile());
								monIA.setLastTarget(lastPath.getLastTile());
								monIA.setLastAction(new AiAction(AiActionName.MOVE,dir));
								monIA.lastTargetCumulativeInterest=CumulativeInterest;
							}
							else {
								//if(m.representation[monIA.getLastTarget().getLine()][monIA.getLastTarget().getCol()]<=currhval) {
								if(monIA.lastTargetCumulativeInterest<CumulativeInterest){
									//!!System.out.println("Path renewing for costs "+monIA.getLastTarget()+" to "+lastPath.getLastTile()+" - "+monIA.lastTargetCumulativeInterest+","+CumulativeInterest);
									monIA.setLastTarget(lastPath.getLastTile());
									monIA.setLastAction(new AiAction(AiActionName.MOVE,dir));
									monIA.lastTargetCumulativeInterest=CumulativeInterest;
								}
								else {
									//!!System.out.println("Path remains unchanged");
									AiAction movement=new AiAction(AiActionName.MOVE,dir);
									//memoriser au lieu de iterer a chaque fois
									/*
									for(int tcount=0;tcount<lastPath.getLength()-1;tcount++)
									{
										if(monIA.getSelfHero().getTile()==lastPath.getTile(tcount))
										{
											Direction dir2=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), lastPath.getTile(tcount+1));
											movement=new AiAction(AiActionName.MOVE,dir2);
											break;
										}
									}*/
									monIA.setLastAction(movement);
								}
							}
							return monIA.getLastAction();
						}
						else if (lastPath.getLength()==1) {
							//!!System.out.println("Path too short for "+target);
						}
					}
					catch (Exception e)
					{
						//!!System.out.println("Ees "+e);
					}
				}
			}
			catch (LimitReachedException e) {
				//!!System.out.println("sdasdadad "+e);
			}
			catch (Exception e) {
				//!!System.out.println("sdasdadad 2 "+e);
			}
			a++;
		}
		
		//Adam gibi bir yer bulamazsak yol yok demek. Getnearestpositive'e kaldi is.
		//m.applyDistanceToAll();
		
		//!!System.out.println("EeEsadkfsjfjdsfjs");
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
