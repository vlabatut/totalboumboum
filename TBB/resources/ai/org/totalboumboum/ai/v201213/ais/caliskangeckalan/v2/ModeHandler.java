package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class ModeHandler extends AiModeHandler<CaliskanGeckalan>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 */
	private CalculCommun calculCommun = null;
	/**
	 * @param ai 
	 * @throws StopRequestException  */
	protected ModeHandler(CaliskanGeckalan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		calculCommun = new CalculCommun(ai);
		calculCommun.update();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	
		ai.checkInterruption();		
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		Collection<AiHero> heroes = zone.getRemainingHeroes();
		int ownRange = ownHero.getBombRange();
		int ownBombs = ownHero.getBombNumberMax();
		//double ownSpeed = ownHero.getWalkingSpeed();
		
		Iterator<AiHero> it = heroes.iterator();
		int totalRange=0;
		int totalBomb = 0;
		int numberOfHeroes = 0;
	//	print("ownRange: " + ownRange + " ownBombs:" + ownBombs );
		if(ownRange >=3 && ownBombs>=2)
			return true;
		while(it.hasNext()) { //get all heroes range and bomb number
			ai.checkInterruption();
			AiHero tempHero = it.next();
			if(!tempHero.equals(ownHero))
			{
				totalRange = totalRange + tempHero.getBombRange();
				totalBomb = totalBomb + tempHero.getBombNumberMax();
				numberOfHeroes++;
			}
		}
		//control of average of rivals and our hero
		print("numberOfHeroes:" + numberOfHeroes);
		double ownCount = (double)ownRange*ownBombs;
		if(numberOfHeroes > 0 && ((double)(totalBomb * totalRange)/(double)numberOfHeroes)<ownCount)
		{
			print("ModeHandler avarage" + (double)(totalBomb * totalRange)/(double)numberOfHeroes + "ownCount:" + ownCount);
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
		CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Dijkstra dijkstra = new Dijkstra(ai,ai.getZone().getOwnHero(),costCalculator,successorCalculator);
		Map<AiTile, AiSearchNode> map = new HashMap<AiTile, AiSearchNode>();
		try { //find all tile that we can go
			map = dijkstra.startProcess(new AiLocation(ai.getZone().getOwnHero().getTile()));
		}
		catch (LimitReachedException e1) {
		}
		if(calculCommun.getItemsAccessible(map).size()>0)
			result = true;
		else if(calculCommun.getMursNeighBoorsForCollecte(map).size()>0)
			result = true;		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
