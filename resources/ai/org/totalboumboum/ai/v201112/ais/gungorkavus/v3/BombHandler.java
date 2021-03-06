package org.totalboumboum.ai.v201112.ais.gungorkavus.v3;

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
import org.totalboumboum.tools.images.PredefinedColor;

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
	 * 
	 */
	protected BombHandler(GungorKavus ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		verbose = false;
    	
	}
	
	
	
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
		ai.checkInterruption();

	boolean result = false;
	zone = ai.getZone();
	ownHero=zone.getOwnHero();
	ownTile = ownHero.getTile();
	AiTile blockedTile=null;
	AiBomb ownBombPt = ownHero.getBombPrototype();
	boolean bombAbsence = ownTile.isCrossableBy(ownBombPt);

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

	
	/*
	 * 
	 * MODE COLLECTE
	 * 
	 */
	
	
	if(ai.modeHandler.getMode()==AiMode.COLLECTING)
	{
		
		if(bombAbsence)	
		{
			AiTile currentDest = ai.moveHandler.getCurrentDestination();

			boolean bombPrimaryDestination = ownTile.equals(currentDest);

			boolean isSafeTileKont=false;
			List<AiTile> blast = ownBombPt.getBlast();
			isSafeTileKont= blast.contains(ai.moveHandler.safeTilekont);

			
			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTilekont==null)
				{
					if(bombPrimaryDestination && ownTile!=startTile && ai.moveHandler.getSafeTile2())
					{
						result=true;
					}

					if(blockedTile==ownTile && ai.moveHandler.getSafeTile2())
						result=true;
				}else
				{
					if(zone.getBombsByColor(PredefinedColor.GREY).isEmpty())
						isSafeTileKont=false;
				
				if(bombPrimaryDestination && ownTile!=startTile && !isSafeTileKont)
					{
						result=true;
					}

				if(blockedTile==ownTile && !isSafeTileKont)
						result=true;
				}
			}else if(isTileThreatened(ai.moveHandler.safeTile))
				{
					if(bombPrimaryDestination && startTile!=ownTile && ownTile!=ai.moveHandler.safeTile && !isSafeTileKont)
					{
						result=true;
					}

					if(blockedTile==ownTile && !isSafeTileKont)
						result=true;
				
				}	

		}
	}

	
	/*
	 * 
	 * MODE ATTAQUE
	 * 
	 */
	
	
	
	if(ai.modeHandler.getMode()==AiMode.ATTACKING)
	{
		
		if(bombAbsence)
		{
			boolean isSafeTileKont2=false;
			boolean isSafeTile = false;
			List<AiTile> blast = ownBombPt.getBlast();
			isSafeTileKont2= blast.contains(ai.moveHandler.safeTilekont);
			isSafeTile = blast.contains(ai.moveHandler.safeTile);
			for(int i = 0;i<opponentL.size();i++)
			{
				ai.checkInterruption();
				
				

				if(tileL.contains(opponentL.get(i).getTile())/*&&ai.getMoveHandler().getCurrentDirection()!=Direction.NONE*/ )
				{
					if(bombL.isEmpty())
					{
						if(!isSafeTileKont2)
							result=true;
					}
					else
					{
						for(int j = 0;j<bombL.size();j++)
						{
							ai.checkInterruption();
							if(!bombL.get(j).getBlast().contains(ownHero.getTile())&&!bombL.get(j).getBlast().contains(nextTile))
							{
								if(!isSafeTileKont2)
									result = true;
							}
						}
					}
				}
			}
			
			
			/*
			 * 
			 * STRATEGIE D'ATTAQUE
			 * 
			 */
			
			if(!zone.getBombsByColor(PredefinedColor.GREY).isEmpty())
			{
			AiTile opp=null;
			for(int i=0;i<opponentL.size();i++)
			{
				ai.checkInterruption();
				opp = opponentL.get(i).getTile();
				
				if(ownTile == zone.getTile(opp.getRow(), opp.getCol()))
				{
					if(!isSafeTileKont2)
						result = true;
				}
				
				for(int j=1;j<3;j++)
				{
					ai.checkInterruption();
					
					
					if(opp.getRow()+j<zone.getHeight() && opp.getCol()+j<zone.getWidth())
						{
							if(ownTile == zone.getTile(opp.getRow()+j, opp.getCol()+j) && !isSafeTileKont2)
							{
									result = true;
							}
						}
				
					if(opp.getRow()+j<zone.getHeight() && opp.getCol()-j>0)
						{
							if(ownTile == zone.getTile(opp.getRow()+j, opp.getCol()-j) && !isSafeTileKont2)
							{
									result = true;
							}
						}
				
					if(opp.getRow()-j>0 && opp.getCol()+j<zone.getWidth())
					{
							if(ownTile == zone.getTile(opp.getRow()-j, opp.getCol()+j) && !isSafeTileKont2)
							{
									result = true;
							}
					}
				
					if(opp.getRow()-j>0 && opp.getCol()-j>0)
					{
							if(ownTile == zone.getTile(opp.getRow()-j, opp.getCol()-j) && !isSafeTileKont2)
							{
									result = true;
							}
					}
					
					
					/////
					
					if(opp.getCol()-j>0)
					{
						if(ownTile == zone.getTile(opp.getRow(), opp.getCol()-j) && !isSafeTile)
						{
					
							result = true;
						}
					}
					
					if(opp.getCol()-j>0 && opp.getRow()+1<zone.getHeight())
					{
						if(ownTile == zone.getTile(opp.getRow()+1, opp.getCol()-j)&& !isSafeTile)
						{
							result = true;
						}
					}
					
					if(opp.getCol()-j>0 && opp.getRow()-1>0)
					{
						if(ownTile == zone.getTile(opp.getRow()-1, opp.getCol()-j)&& !isSafeTile)
						{
							result = true;
						}
					}
					
					if(opp.getCol()+j<zone.getWidth())
					{
						if(ownTile == zone.getTile(opp.getRow(), opp.getCol()+j)&& !isSafeTile)
						{
							result = true;
					
						}
					}
					
					if(opp.getCol()+j<zone.getWidth() && opp.getRow()-1>0)
					{
						if(ownTile == zone.getTile(opp.getRow()-1, opp.getCol()+j)&& !isSafeTile)
						{
							result = true;
						}
					}
					
					if(opp.getCol()+j<zone.getWidth() && opp.getRow()+1<zone.getWidth())
					{
						if(ownTile == zone.getTile(opp.getRow()+1, opp.getCol()+j)&& !isSafeTile)
						{
							result = true;
						}
					}
					
					
					if(opp.getRow()+j<zone.getHeight())
					{
						if(ownTile == zone.getTile(opp.getRow()+j, opp.getCol())&& !isSafeTile )
						{
							result = true;
						}
					}
					
					if(opp.getRow()+j<zone.getHeight() && opp.getCol()-1>0)
					{
						if(ownTile == zone.getTile(opp.getRow()+j, opp.getCol()-1)&& !isSafeTile)
						{
							result = true;
						}
					}
					
					if(opp.getRow()+j<zone.getHeight() && opp.getCol()+1<zone.getWidth())
					{
						if(ownTile == zone.getTile(opp.getRow()+j, opp.getCol()+1) &&!isSafeTile)
						{
							result = true;
						}
					}
					
					
					if(opp.getRow()-j>0)
					{
						if(ownTile == zone.getTile(opp.getRow()-j, opp.getCol()) && !isSafeTile)
						{
							result = true;
					
						}
					}
					
					if(opp.getRow()-j>0 && opp.getCol()+1<zone.getWidth())
					{
						if(ownTile == zone.getTile(opp.getRow()-j, opp.getCol()+1) && !isSafeTile)
						{
							result = true;
						}
					}
					
					if(opp.getRow()-j>0 && opp.getCol()-1>0)
					{
						if(ownTile == zone.getTile(opp.getRow()-j, opp.getCol()-1) && !isSafeTile)
						{
							result = true;
						}
					}
					
					//////////
				
				}
			}
			
			}//if
			
			
			boolean bombPrimaryDestination = ownTile.equals(ai.moveHandler.getCurrentDestination());
			
			if(ai.moveHandler.safeTile==null)
			{
				if(ai.moveHandler.safeTilekont!=null)
				{
					if(bombPrimaryDestination && !isSafeTileKont2)
						result = true;
					
					if(blockedTile==ownTile && ai.moveHandler.getSafeTile2())
						result=true;
				}else
				{
					
					if(bombPrimaryDestination && ai.moveHandler.getSafeTile2() && ownTile!=startTile)
						result=true;
					if(blockedTile==ownTile && ai.moveHandler.getSafeTile2())
						result=true;
				}
					
			}
			if(ai.moveHandler.safeTile!=null)
			{
				if(isTileThreatened(ai.moveHandler.safeTile))
				{
					if(blockedTile!=null)
					{
					if(blockedTile.equals(ownTile) && !isSafeTileKont2)
						result=true;
					}
				}
			}
		}
	}
	return result;

	}
	
	
	/*
	 * 
	 *isTileThreatened 
	 * 
	 */
	
	
	
	/**
	 * @param tile
	 * 		description manquante !
	 * @return
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
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
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
	}
}
