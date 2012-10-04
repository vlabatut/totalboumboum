package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;

import java.util.List;


import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.GungorKavus;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe reprÃ©sente est un simple exemple de 
 * critÃ¨re binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter Ã vos besoin.
 * 
 * @author EyÃ¼p Burak GÃ¼ngÃ¶r
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class VDMDestDistance extends AiUtilityCriterionBoolean
{	/** Nom de ce critÃ¨re */
	public static final String NAME = "Distance";

	/**
	 * CrÃ©e un nouveau critÃ¨re binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * Au cas oÃ¹ le moteur demande la terminaison de l'agent.
	 */
	public VDMDestDistance(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	/**
	 * 
	 * @param d
	 * @param e
	 * @param f
	 * @param g
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int distance(int d,int e,int f,int g) throws StopRequestException{
		ai.checkInterruption();

		int resultat = Math.abs(d-f)+Math.abs(e-g);

		return resultat;

	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;



	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();

	List<AiBlock> blockL = zone.getDestructibleBlocks();
	int shortest = 200;
	AiBlock sBlock=null;
	Direction dBlock = null;
	if(blockL.size()>0){
		if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.UP))){
			shortest = zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.UP), ownHero.getTile());
			sBlock = blockL.get(0);
			dBlock = Direction.UP;
		}
		AiBlock sDBlock = null;
		Direction dSBlock = null;

		if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.DOWN))){
			if(shortest>zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.DOWN), ownHero.getTile())){
				shortest = zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.DOWN), ownHero.getTile());
				dBlock = Direction.DOWN;
			}
			if(shortest==zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.DOWN), ownHero.getTile())){
				dSBlock = Direction.DOWN;
			}
		}
		if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.LEFT))){
			if(shortest>zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.LEFT), ownHero.getTile())){
				shortest =zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.LEFT), ownHero.getTile());
				dBlock = Direction.LEFT;
			}
			if(shortest==zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.LEFT), ownHero.getTile())){
				dSBlock = Direction.LEFT;
			}
		}
		if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.RIGHT))){
			if(shortest>zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.RIGHT), ownHero.getTile())){
				shortest = zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.RIGHT), ownHero.getTile());
				dBlock = Direction.RIGHT;
			}
			if(shortest==zone.getTileDistance(blockL.get(0).getTile().getNeighbor(Direction.RIGHT), ownHero.getTile())){
				dSBlock = Direction.RIGHT;
			}
		}
		for(int i = 0;i<blockL.size()-1;i++){
			ai.checkInterruption();
			if(zone.getTiles().contains(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN))){
				if(shortest>zone.getTileDistance(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN), ownHero.getTile())){
					shortest = zone.getTileDistance(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN), ownHero.getTile());
					dBlock = Direction.DOWN;
					sBlock = blockL.get(i+1);	
				}
				if(shortest==zone.getTileDistance(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN), ownHero.getTile())){
					dSBlock = Direction.DOWN;
					sDBlock = blockL.get(i+1);
				}
			}
			if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.UP))){
				if(shortest>distance(blockL.get(i+1).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow())){
					shortest = distance(blockL.get(i+1).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow());
					dBlock = Direction.UP;
					sBlock = blockL.get(i+1);

				}
				if(shortest==distance(blockL.get(i+1).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow())){
					dSBlock = Direction.UP;
					sDBlock = blockL.get(i+1);	
				}
			}
			if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.LEFT))){
				if(shortest>distance(blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow())){
					shortest = distance(blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow());
					dBlock = Direction.LEFT;
					sBlock = blockL.get(i+1);
				}
				if(shortest==distance(blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow())){
					dSBlock = Direction.LEFT;
					sDBlock = blockL.get(i+1);	
				}
			}
			if(zone.getTiles().contains(blockL.get(0).getTile().getNeighbor(Direction.RIGHT))){
				if(shortest>distance(blockL.get(i+1).getTile().getNeighbor(Direction.RIGHT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.RIGHT).getRow(), ownHero.getCol(), ownHero.getRow())){
					shortest = distance(blockL.get(i+1).getTile().getNeighbor(Direction.RIGHT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.RIGHT).getRow(), ownHero.getCol(), ownHero.getRow());
					dBlock = Direction.RIGHT;
					sBlock = blockL.get(i+1);
				}
				if(shortest==distance(blockL.get(i+1).getTile().getNeighbor(Direction.RIGHT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.RIGHT).getRow(), ownHero.getCol(), ownHero.getRow())){
					dSBlock = Direction.RIGHT;
					sDBlock = blockL.get(i+1);	
				}
			}

		}

		AiTile sTile1 = sBlock.getTile().getNeighbor(dBlock);
		AiTile sTile2=null;
		if(dSBlock!=null&&sDBlock!=null)
			sTile2 = sDBlock.getTile().getNeighbor(dSBlock);

		if(distance(tile.getCol(), tile.getRow(), sTile1.getCol(), sTile1.getRow())==0){
			result = true;
		}
		
		if(sTile2!=null)
		if(distance(tile.getCol(), tile.getRow(), sTile2.getCol(), sTile2.getRow())==0){
			result = true;
		}

	}


	return result;
	}
}