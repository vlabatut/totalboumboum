package org.totalboumboum.ai.v201112.ais.demirsazan.v3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Serdil Demir
 * @author Gökhan Sazan
 */
public class MoveHandler extends AiMoveHandler<DemirSazan>
{	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	protected MoveHandler(DemirSazan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		verbose = false;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		CommonTools commonTools = new CommonTools(ai);
		AiHero ownHero = ai.getZone().getOwnHero();
		NoHeuristicCalculator heuristic = new NoHeuristicCalculator(ai);
		TileCostCalculator cost= new TileCostCalculator(ai);
		BasicSuccessorCalculator successor = new BasicSuccessorCalculator(ai);
		Collection<List<AiTile>> asd= ai.getUtilityHandler().getUtilitiesByValue().values();
		Iterator<List<AiTile>> itr = asd.iterator();
		HashMap<AiTile, Float> liste = ai.getUtilityHandler().getUtilitiesByTile();
		AiTile hedef = null;
		
		while(itr.hasNext()){
			ai.checkInterruption();
			List<AiTile> hedefler = itr.next();
			int sizeList = hedefler.size();
			for(int i = 0 ; i<sizeList;i++ ){
				ai.checkInterruption();
				if((hedef==null || liste.get(hedef)> liste.get(hedefler.get(i))) && !ownHero.getTile().equals(hedefler.get(i))){
					hedef = hedefler.get(i);
				}
			}
		}
		
		if( liste.get(currentDestination) != null){
			if(liste.get(currentDestination).equals(liste.get(hedef))){
				hedef = currentDestination;
			}
		}
		if(hedef != null){
			if(hedef.equals(ownHero.getTile())){
				this.currentPath = null;
				this.currentDirection = Direction.NONE;
			}
			else{
				Astar astar =new Astar(ai, ownHero, cost, heuristic, successor);
				AiLocation heroLoc= new AiLocation(ownHero);
				try {
					this.currentPath = astar.processShortestPath(heroLoc,hedef);
					if(!commonTools.possibleDePasser(ai.getZone().getOwnHero(), this.currentPath) && (commonTools.inDangeraous(heroLoc,ai.getZone())==null)){
						this.currentDirection = Direction.NONE;
						return this.currentDirection;
					}
				} catch (LimitReachedException e) {
					e.printStackTrace();
				}
				this.currentDestination = hedef;
				if(this.currentPath != null){
					if(this.currentPath.getLocations().size()>0){
						this.currentDirection = commonTools.calculateDirection(this.currentPath.getLocation(1));
					}	
				}
				else{
					this.currentDirection = Direction.NONE;
				}
			}
		}
		else{
			this.currentDirection = Direction.NONE;
		}
		return this.currentDirection;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		super.updateOutput();
	}
}
