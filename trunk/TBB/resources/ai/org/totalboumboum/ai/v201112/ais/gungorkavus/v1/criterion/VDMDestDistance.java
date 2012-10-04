package org.totalboumboum.ai.v201112.ais.gungorkavus.v1.criterion;

import java.util.List;


import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v1.GungorKavus;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class VDMDestDistance extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDMDestDistance(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
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
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;



	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();

	List<AiBlock> blockL = zone.getDestructibleBlocks();

	if(blockL.size()>0){
		double shortest = distance(blockL.get(0).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(0).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow());
		AiBlock sBlock = blockL.get(0);
		Direction dBlock = Direction.UP;
		AiBlock sDBlock = null;
		Direction dSBlock = null;


		if(shortest>distance(blockL.get(0).getTile().getNeighbor(Direction.DOWN).getCol(), blockL.get(0).getTile().getNeighbor(Direction.DOWN).getRow(), ownHero.getCol(), ownHero.getRow())){
			shortest = distance(blockL.get(0).getTile().getNeighbor(Direction.DOWN).getCol(), blockL.get(0).getTile().getNeighbor(Direction.DOWN).getRow(), ownHero.getCol(), ownHero.getRow());
			dBlock = Direction.DOWN;
		}
		if(shortest==distance(blockL.get(0).getTile().getNeighbor(Direction.DOWN).getCol(), blockL.get(0).getTile().getNeighbor(Direction.DOWN).getRow(), ownHero.getCol(), ownHero.getRow())){
			dSBlock = Direction.DOWN;
		}

		if(shortest>distance(blockL.get(0).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(0).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow())){
			shortest = distance(blockL.get(0).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(0).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow());
			dBlock = Direction.LEFT;
		}
		if(shortest==distance(blockL.get(0).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(0).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow())){
			dSBlock = Direction.LEFT;
		}
		if(shortest>distance(blockL.get(0).getTile().getNeighbor(Direction.RIGHT).getCol(), blockL.get(0).getTile().getNeighbor(Direction.RIGHT).getRow(), ownHero.getCol(), ownHero.getRow())){
			shortest = distance(blockL.get(0).getTile().getNeighbor(Direction.RIGHT).getCol(), blockL.get(0).getTile().getNeighbor(Direction.RIGHT).getRow(), ownHero.getCol(), ownHero.getRow());
			dBlock = Direction.RIGHT;
		}
		if(shortest==distance(blockL.get(0).getTile().getNeighbor(Direction.RIGHT).getCol(), blockL.get(0).getTile().getNeighbor(Direction.RIGHT).getRow(), ownHero.getCol(), ownHero.getRow())){
			dSBlock = Direction.RIGHT;
		}

		for(int i = 0;i<blockL.size()-1;i++){
			ai.checkInterruption();

			if(shortest>distance(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.DOWN).getRow(), ownHero.getCol(), ownHero.getRow())){
				shortest = distance(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.DOWN).getRow(), ownHero.getCol(), ownHero.getRow());
				dBlock = Direction.DOWN;
				sBlock = blockL.get(i+1);	
			}
			if(shortest==distance(blockL.get(i+1).getTile().getNeighbor(Direction.DOWN).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.DOWN).getRow(), ownHero.getCol(), ownHero.getRow())){
				dSBlock = Direction.DOWN;
				sDBlock = blockL.get(i+1);
			}
			if(shortest>distance(blockL.get(i+1).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow())){
				shortest = distance(blockL.get(i+1).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow());
				dBlock = Direction.UP;
				sBlock = blockL.get(i+1);

			}
			if(shortest==distance(blockL.get(i+1).getTile().getNeighbor(Direction.UP).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.UP).getRow(), ownHero.getCol(), ownHero.getRow())){
				dSBlock = Direction.UP;
				sDBlock = blockL.get(i+1);	
			}
			if(shortest>distance(blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow())){
				shortest = distance(blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow());
				dBlock = Direction.LEFT;
				sBlock = blockL.get(i+1);
			}
			if(shortest==distance(blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getCol(), blockL.get(i+1).getTile().getNeighbor(Direction.LEFT).getRow(), ownHero.getCol(), ownHero.getRow())){
				dSBlock = Direction.LEFT;
				sDBlock = blockL.get(i+1);	
			}
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

		AiTile sTile1 = sBlock.getTile().getNeighbor(dBlock);
		AiTile sTile2 = sDBlock.getTile().getNeighbor(dSBlock);

		if(distance(tile.getCol(), tile.getRow(), sTile1.getCol(), sTile1.getRow())==0){
			result = true;
		}

		if(distance(tile.getCol(), tile.getRow(), sTile2.getCol(), sTile2.getRow())==0){
			result = true;
		}

	}


	return result;
	}
}
