package org.totalboumboum.ai.v201011.ais.kesimalvarol.v6;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.kesimalvarol.v6.KesimalVarol;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * La classe qui conduisse les op√©rations concernant la zone du jeu et les matrices.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
public class GameZoneAnalyzer
{	/** classe principale de l'IA, permet d'accÔøΩder ÔøΩ checkInterruption() */
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		GameZoneAnalyzer.monIA = monIA;
		Matrix.setMonIA(monIA);
		GameZoneFuturePredictions.setMonIA(monIA);
	}
	
	/** ctr. principal
	 * @param gOld Une objet GameZone -probablement celui de l'iteration precedente- pour initialisation . Peut etre null si c'est la premiere fois qu'on l'utilise (valeurs statiques ne sont pas utilisees comme on aura obtenu des resultats fausses aux manches suivantes)
	 * @throws StopRequestException 
	 */
	public GameZoneAnalyzer(GameZoneAnalyzer gOld) throws StopRequestException
	{
		monIA.checkInterruption();
		if(gOld!=null)
		{
		//searchtreeLatestTarget=gOld.searchtreeLatestTarget;
		//searchtreeLatestCumulativeVariable=gOld.searchtreeLatestCumulativeVariable;
		}
	}
	
	/** Structure interne pour gerer les explosions enchain√©s */
	private final class bombDetailed
	{
		public AiBomb bomb;
		public double delay;
		public bombDetailed(AiBomb b,double d) throws StopRequestException
		{
			monIA.checkInterruption();
			bomb=b;
			delay=d;
		}
	}
	
	/** Methode pour construire la matrice */
	public Matrix constructInterestMatrix() throws StopRequestException
	{
		monIA.checkInterruption();
		Matrix m=new Matrix(monIA.getZone());
		List<AiBomb> cbombs=monIA.getZone().getBombs();	
		
		for(int i=0;i<m.height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<m.width;j++)
			{
				monIA.checkInterruption();
				AiTile currtile=monIA.getZone().getTile(i,j);
				List<AiFire> fires=currtile.getFires();
				if (fires.size()>0) {
					m.representation[i][j]=-300;
				}
			}
		}
		
		TreeMap<Double, ArrayList<bombDetailed>> bombsCurrentMapForOrdering=new TreeMap<Double, ArrayList<bombDetailed>>();
		/**
		 * On doit gerer les bombes du maniere different, dans deux parties.
		 * Dans le 1er, on va initialiser l'arbre au dessus. On mettra les valeurs dans l'arbre, ayant des clés étant les valeurs d'interet.
		 * Dans la 2e, grace au nature des clés, on va les separer aux couches (en fonction des les valeurs d'interet). Chacune des bombes dans un couche va "attirer" les bombes dans les couches superieures qu'ils peuvent declencher.
		 * Celle-ci signifie que les bombes aux couches superieures peuvent etre detruites plus tot.
		 * Finalement, on va ajouter les valeurs dans la matrice d'interet.
		 */
		//Partie 1
		for (AiBomb bomb : cbombs)
		{
			monIA.checkInterruption();
			double delay=((double)bomb.getNormalDuration()-bomb.getTime());
			if (delay<0) { //√áa veut dire que bombe est tomb√© en panne
				delay=0;
			}
			AiTile bombpos=bomb.getTile();
			double lowestNeighboringValue=m.representation[bombpos.getLine()][bombpos.getCol()];
			for(AiTile voisines : bombpos.getNeighbors())
			{
				monIA.checkInterruption();
				double tmp=m.representation[voisines.getLine()][voisines.getCol()];
				AiTile voisinDevoisin=voisines.getNeighbor(monIA.getZone().getDirection(bombpos, voisines));
				if(voisinDevoisin!=null)
					if(m.representation[voisinDevoisin.getLine()][voisinDevoisin.getCol()]==tmp)
					{
						if(tmp<lowestNeighboringValue)
							lowestNeighboringValue=tmp;
						if(lowestNeighboringValue==-300)
							break;
					}
			}
			double bombPassTolerance=lowestNeighboringValue-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration());
			if(bombPassTolerance<=-300)
				delay=-300;//lowestNeighboringValue;//m.representation[bombpos.getLine()][bombpos.getCol()];//-300 
			else
				delay=-300+100.0*(delay/(double)bomb.getNormalDuration());
			
			ArrayList<bombDetailed> tmp = bombsCurrentMapForOrdering.get(Double.valueOf(delay));
			if(tmp==null)
			{
				tmp=new ArrayList<bombDetailed>();
				tmp.add(new bombDetailed(bomb,delay));
				bombsCurrentMapForOrdering.put(Double.valueOf(delay),tmp);
			}
			else
			{
				tmp.add(new bombDetailed(bomb,delay));
			}
		}
		
		//Partie 2
		for(Double bKey : bombsCurrentMapForOrdering.keySet())
		{
			monIA.checkInterruption();
			ArrayList<bombDetailed> tmp = bombsCurrentMapForOrdering.get(bKey);
			Iterator<bombDetailed> bCurrentIter=tmp.iterator();
			while(bCurrentIter.hasNext())
			{
				monIA.checkInterruption();
				bombDetailed bCurrent = (bombDetailed)bCurrentIter.next();
				List<AiTile>affectedCases=bCurrent.bomb.getBlast();
				for(Double bKeyLower : bombsCurrentMapForOrdering.keySet())
				{
					monIA.checkInterruption();
					if(bKeyLower>bKey)
					{
						ArrayList<bombDetailed> tmpLower = bombsCurrentMapForOrdering.get(bKeyLower);
						Iterator<bombDetailed> potentialAffectedIter=tmpLower.iterator();
						while(potentialAffectedIter.hasNext())
						{
							monIA.checkInterruption();
							bombDetailed potentialAffected=(bombDetailed)potentialAffectedIter.next();
							for(AiTile t : affectedCases)
							{
								monIA.checkInterruption();
								if(t!=bCurrent.bomb.getTile())
								{
									if(t.getBombs().contains(potentialAffected.bomb))
									{
										potentialAffected.delay=bKey;
										tmp.add(potentialAffected);
										tmpLower.remove(potentialAffected);
										potentialAffectedIter=tmpLower.iterator();
										bCurrentIter=tmp.iterator();
									}
								}
							}
						}
					}
				}
			}
			
			bCurrentIter=tmp.iterator();
			while(bCurrentIter.hasNext())
			{
				monIA.checkInterruption();
				bombDetailed bdet = (bombDetailed)bCurrentIter.next();
				List<AiTile>affectedCases=bdet.bomb.getBlast();
				for (AiTile t : affectedCases)
				{
					monIA.checkInterruption();
					int i=t.getLine(),j=t.getCol();
					if(m.representation[i][j]>bdet.delay) {
						m.representation[i][j]=bdet.delay;
					}
				}
			}
		}
		
		//Ensuite, on fait des calcules pour le reste des entit√©s
		for(int i=0;i<m.height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<m.width;j++)
			{
				monIA.checkInterruption();
				assignBasicNums(m,i,j);
			}
		}

		//Finalement, pour les murs et adversaires.
		handleWallAndEnemies(m);
		
		return m;
	}
	
	/** Pour attribuer des valeurs elementaires 
	 * 
	 * @param m Matrice necessaire pour les calculs
	 * @param i Ligne courante
	 * @param j Colonne courante
	 */
	private void assignBasicNums(Matrix m,int i,int j) throws StopRequestException
	{
		monIA.checkInterruption();
		AiTile currtile=monIA.getZone().getTile(i,j);
		
		int modifier;
		int a=currtile.getLine(),b=currtile.getCol();
		
		//Bonus
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=50;
		else if(monIA.getZone().getHeroes().size()-monIA.getZone().getRemainingHeroes().size()>2) {
			modifier=10;
		}
		else //Si on a seulement une adversaire, elle ne sera pas tuee par les autres, on ignore les bonus completement
			modifier=0;
		
		if(m.representation[i][j]>=0)
			for (int c=0;c<currtile.getItems().size();c++) {
				monIA.checkInterruption();
				m.representation[a][b]+=modifier;
			}
		
		AiZone azone=monIA.getZone();
		AiTile ourtile=monIA.getSelfHero().getTile();
		if (m.representation[i][j]>=0 && currtile.isCrossableBy(monIA.getSelfHero()))
		{
			int awardedval=2*((azone.getHeight()+azone.getWidth())-azone.getTileDistance(ourtile, currtile))/3;
			m.representationDistanceParameters[i][j]=awardedval;
		}
	}
	
	/** Mur et adversaires sont gerees ici 
	 * @param m Matrice necessaire pour les calculs
	 */
	private void handleWallAndEnemies(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		int modifier=0;
		
		//Adversaires
		List<AiHero> adversaires=monIA.getZone().getHeroes();
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=1;
		else {
			modifier=5;
		}
		for(AiHero adv : adversaires)
		{
			monIA.checkInterruption();
			double interest=0;
			if(!adv.hasEnded() && adv!=monIA.getSelfHero())
			{
				//adv.getTile();
				AiTile ctile=adv.getTile();
				List<AiTile> tAll=ctile.getNeighbors();
				tAll.add(ctile.getNeighbor(Direction.DOWN).getNeighbor(Direction.RIGHT));
				tAll.add(ctile.getNeighbor(Direction.DOWN).getNeighbor(Direction.LEFT));
				tAll.add(ctile.getNeighbor(Direction.UP).getNeighbor(Direction.RIGHT));
				tAll.add(ctile.getNeighbor(Direction.UP).getNeighbor(Direction.LEFT));
				for(AiTile t : tAll)
				{
					monIA.checkInterruption();
					if(!t.isCrossableBy(adv))
					{
						interest+=1.25*modifier;
					}
				}
				for(AiTile at : ctile.getNeighbors())
				{
					monIA.checkInterruption();
					if(at.isCrossableBy(adv)) {
						m.markForBombPlacementCandidate(adv,at,interest);
					}
				}
				m.regionEmplacementImportanceMatrix[ctile.getLine()][ctile.getCol()].importance+=1;
				m.regionEmplacementImportanceMatrix[ctile.getLine()][ctile.getCol()].forEnemy=true;
				m.bombRegionNodes.add(ctile);
			}
		}
		
		if(monIA.getMode()==Mode.COLLECTE)
			handleWalls(m,40);
		
		//La direction la plus preferable
		m.bestDirectionOptimisation();

	}
	
	/** Les murs peuvent etre geree separement d'adversaires (mode attaque), celle-ci nous permet de le faire 
	 * 
	 * @param m Matrice pour determiner les valeurs
	 * @param modifier La valeur a utiliser lors de affectation des valeurs.
	 */
	public void handleWalls(Matrix m,int modifier) throws StopRequestException
	{
		monIA.checkInterruption();
		//Murs
		for(AiBlock ablock : monIA.getZone().getDestructibleBlocks())
		{
			monIA.checkInterruption();
			for (AiTile t : ablock.getTile().getNeighbors()) {
				monIA.checkInterruption();
				if(t.getBlocks().size()==0 && m.representation[t.getLine()][t.getCol()]>=0) {
					if (true)
					{
						m.markForBombPlacementCandidate(ablock,t.getLine(), t.getCol(), modifier);
					}
				}
			}
		}
	}
}
