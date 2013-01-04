package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v1;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Our bomb handler class.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class BombHandler extends AiBombHandler<AlcarKayaYildirim>
{	
	/** */
	public AiTile startTile=ai.getZone().getOwnHero().getTile();
	/** */
	protected AiZone zone;
	/** */
	protected AiHero ownHero;
	/** */
	protected AiTile ownTile;
	/** */
	private double currentSpeed = 0;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/**
	 * @param ai 
	 * @throws StopRequestException  */

	
	protected BombHandler(AlcarKayaYildirim ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
	}

	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	/**
	 * @param tile
	 * @return result
	 * @throws StopRequestException
	 */
	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();	
		
		currentSpeed=ownHero.getWalkingSpeed();
		long crossTime = 0;
		
		
		crossTime = Math.round(1000*tile.getSize()/currentSpeed);
		
		
		boolean result = false; 
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getElapsedTime();
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
	
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
	boolean result = false;
	zone = ai.getZone();
	ownHero=zone.getOwnHero();
	ownTile = ownHero.getTile();
	AiTile blockedTile=null;
	AiBomb ownBombPt = ownHero.getBombPrototype();
	boolean bombAbsence = ownTile.isCrossableBy(ownBombPt);


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

	//MODE COLLECTE
	
	if(ai.modeHandler.getMode()==AiMode.COLLECTING)
	{
		
		if(bombAbsence)	
		{
			AiTile currentDestination = ai.moveHandler.getCurrentDestination();

			boolean bombPrimaryDestination = ownTile.equals(currentDestination);

			boolean isSafeTileControl=false;
			List<AiTile> blast = ownBombPt.getBlast();
			isSafeTileControl= blast.contains(ai.moveHandler.safeTileControl);

			
			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTileControl==null)
				{
					if(bombPrimaryDestination && ownTile!=startTile && ai.moveHandler.getSafeTile())
					{
						result=true;
					}

					if(blockedTile==ownTile && ai.moveHandler.getSafeTile())
					{
						result=true;
					}
						
				}else
				{
					if(zone.getBombsByColor(PredefinedColor.BLUE).isEmpty())
					{

						isSafeTileControl=false;
				
					}
				if(bombPrimaryDestination && ownTile!=startTile && !isSafeTileControl)
					{
						result=true;
					}

				if(blockedTile==ownTile && !isSafeTileControl)
				{
					result=true;
				}
				}
			}else if(isTileThreatened(ai.moveHandler.safeTile))
				{
					if(bombPrimaryDestination && startTile!=ownTile && ownTile!=ai.moveHandler.safeTile && !isSafeTileControl)
					{
						result=true;
					}

					if(blockedTile==ownTile && !isSafeTileControl)
					{
						result=true;
					}
						
				
				}	

		}
	}
	
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
