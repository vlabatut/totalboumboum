package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;



import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.GungorKavus;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
public class VDMDestIntersection extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Intersec";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDMDestIntersection(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
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

	

	AiTile tileUp = tile.getNeighbor(Direction.UP);
	AiTile tileDown = tile.getNeighbor(Direction.DOWN);
	AiTile tileLeft = tile.getNeighbor(Direction.LEFT);
	AiTile tileRight = tile.getNeighbor(Direction.RIGHT);
	
	int cont = 0;
	
	if(tileUp.getBlocks().size()>0)
	{
		if(tileUp.getBlocks().get(0).isDestructible())
			cont++;	
	}
	if(tileDown.getBlocks().size()>0)
	{
		if(tileDown.getBlocks().get(0).isDestructible())
			cont++;	
	}
	if(tileLeft.getBlocks().size()>0)
	{
		if(tileLeft.getBlocks().get(0).isDestructible())
			cont++;	
	}
	if(tileRight.getBlocks().size()>0)
	{
		if(tileRight.getBlocks().get(0).isDestructible())
			cont++;	
	}
	
	if(cont>1)
		result = true;
	


	return result;
	}
}