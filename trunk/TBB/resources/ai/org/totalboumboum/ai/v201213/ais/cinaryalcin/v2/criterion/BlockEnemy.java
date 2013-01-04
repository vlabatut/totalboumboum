package org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.CinarYalcin;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Controle si bomber cette case bloque des ennemies
 * 
 * 
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class BlockEnemy extends AiUtilityCriterionBoolean<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "PeutTuer";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public BlockEnemy(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		AiZone zone = this.ai.getZone();
		AiHero ourhero = zone.getOwnHero();
		int bombrange=ourhero.getBombRange();
		int i=0;
		AiSimZone simzone = new AiSimZone(zone);
		ai.checkInterruption();
		AiBomb mybomb=simzone.createBomb(tile, simzone.getOwnHero());
		if (!simzone.getRemainingOpponents().isEmpty()) {
			for(AiHero hero:simzone.getRemainingOpponents())
			{
				ai.checkInterruption();
				AiTile tilehero=hero.getTile();
				i=0;
				while(tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
						&&i<=bombrange+1&& !tilehero.getNeighbor(Direction.DOWN).equals(mybomb.getTile()))
				{		
					ai.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.DOWN);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else result=false;
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
						&&i<=bombrange+1 && !tilehero.getNeighbor(Direction.UP).equals(mybomb.getTile()))
				{		
					ai.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.UP);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else
							result=false;					
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, false, true, true)
						&& i<=bombrange+1 && !tilehero.getNeighbor(Direction.LEFT).equals(mybomb.getTile()))
				{		
					ai.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.LEFT);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						else
							result=false;
					}
					i++;
				}
				i=0;
				tilehero=hero.getTile();
				while(tilehero.getNeighbor(Direction.RIGHT).isCrossableBy(hero, false, false, false, false, true, true)
						&& i<=bombrange+1 && !tilehero.getNeighbor(Direction.RIGHT).equals(mybomb.getTile()))
				{		
					ai.checkInterruption();
					tilehero=tilehero.getNeighbor(Direction.RIGHT);
					if(result==true){
						if(!tilehero.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, false, true, true)
								&&!tilehero.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, false, true, true)
								&&mybomb.getBlast().contains(tilehero))
							result=true;
						
						else
							result=false;
					}
					i++;
				}
				
			}
		}
		return result;
	}
}
