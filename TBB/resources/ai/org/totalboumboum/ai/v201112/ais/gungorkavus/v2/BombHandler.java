package org.totalboumboum.ai.v201112.ais.gungorkavus.v2;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<GungorKavus>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(GungorKavus ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		verbose = false;
    	
	}
	
	public AiTile oootile=ai.getZone().getOwnHero().getTile();
	
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
		ai.checkInterruption();

	boolean result = false;
	AiZone zone = ai.getZone();
	AiHero ownHero=zone.getOwnHero();
	AiTile ownTile = ownHero.getTile();
	AiTile blockedTile=null;
	AiBomb bomb2 = ownHero.getBombPrototype();
	boolean bombAbsence = ownTile.isCrossableBy(bomb2);

	AiTile nextTile = ai.moveHandler.nextTile;
	List<AiTile> tileL = ownHero.getBombPrototype().getBlast();
	List<AiHero> opponentL = zone.getRemainingOpponents();
	List<AiBomb> bombL = zone.getBombs();

	if(ai.moveHandler.getCurrentPath()!=null)
	{
		AiPath yol = ai.moveHandler.getCurrentPath();
		Iterator<AiLocation> it = yol.getLocations().iterator();

		AiTile previousTile = null;
		while(it.hasNext() && blockedTile==null)
		{	
			ai.checkInterruption();

			AiLocation location = it.next();
			AiTile tile = location.getTile();
			List<AiBlock> blocks = tile.getBlocks();

			if(!blocks.isEmpty())
				blockedTile = previousTile;
				previousTile = tile;
		}
	}

	if(ai.modeHandler.getMode()==AiMode.COLLECTING)
	{
		if(bombAbsence)	
		{
			AiTile currentDest = ai.moveHandler.getCurrentDestination();

			boolean bombPrimaryDestination = ownTile.equals(currentDest);

			boolean result2=false;
			List<AiTile> blast = bomb2.getBlast();
			result2= blast.contains(ai.moveHandler.safeTilekont);

			
			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTilekont==null)
				{
				if(bombPrimaryDestination && ownTile!=oootile)
				{
					result=true;
				}

				if(blockedTile==ownTile)
					result=true;
			}else
			{
				if(bombPrimaryDestination && ownTile!=oootile && !result2)
				{
					result=true;
				}

				if(blockedTile==ownTile && !result2)
					result=true;
			}
			}else if(isTileThreatened(ai.moveHandler.safeTile))
			{
				if(bombPrimaryDestination && oootile!=ownTile && ownTile!=ai.moveHandler.safeTile && !result2)
				{
					result=true;
				}

				if(blockedTile==ownTile && !result2){
					result=true;
				}
			}	

		}
	}

	if(ai.modeHandler.getMode()==AiMode.ATTACKING)
	{
		
		
		if(bombAbsence)
		{
			boolean result2=false;
			List<AiTile> blast = bomb2.getBlast();
			result2= blast.contains(ai.moveHandler.safeTilekont);

			for(int i = 0;i<opponentL.size();i++){
				ai.checkInterruption();

				if(tileL.contains(opponentL.get(i).getTile())&&ai.getMoveHandler().getCurrentDirection()!=Direction.NONE ){
					if(bombL.isEmpty())
					{
						if(!result2)
							result=true;
					}
					else
					{
						for(int j = 0;j<bombL.size();j++){
							ai.checkInterruption();
							if(!bombL.get(j).getBlast().contains(ownHero.getTile())&&!bombL.get(j).getBlast().contains(nextTile)){
								if(!result2)
									result = true;
							}
						}

					}
				}




			}


			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTilekont!=null)
				{
				if(blockedTile==ownTile && !result2)
					result=true;
				}else
				{
					if(blockedTile==ownTile)
						result=true;
				}
					
			}
			if(ai.moveHandler.safeTile!=null)
			{
				if(isTileThreatened(ai.moveHandler.safeTile))
				{
					if(blockedTile.equals(ownTile) && !result2)
						result=true;
				}
			}
		}
	}
	return result;

	}
	
	private double currentSpeed = 0;
	
	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();	
		
		currentSpeed=ai.getZone().getOwnHero().getWalkingSpeed();
		long crossTime = 0;
		
		crossTime = Math.round(1000*tile.getSize()/currentSpeed);
		
		boolean result = false; 
		
		List<AiBomb> bombs = ai.getZone().getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		
		while(!result && it.hasNext())
		{	
			ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getTime();
			
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	
				List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
	
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
	}
}
