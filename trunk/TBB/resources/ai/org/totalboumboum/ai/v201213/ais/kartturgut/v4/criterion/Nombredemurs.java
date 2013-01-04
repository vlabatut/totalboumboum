package org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.KartTurgut;
import org.totalboumboum.engine.content.feature.Direction;


/** 
 * @author Yunus Kart
 * @author Siyabend Turgut
 */

public class Nombredemurs extends AiUtilityCriterionInteger<KartTurgut>
{	/** */
	public static final String NAME = "Nombredemurs";
	/** */
	private final int			un = 1;
	/** */
	private final int			deux = 2;
	/** */
	private final int			trois	= 3;
	/** */
	private final int			zero	= 0;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Nombredemurs( KartTurgut ai ) throws StopRequestException
	{ 
		super(ai,NAME,1,3);
		ai.checkInterruption();
		this.ai = ai;
	}

	@Override
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int conteur = zero, resultat = zero;

		for ( AiBlock currentBlock : tile.getNeighbor( Direction.UP ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) 
				conteur++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.DOWN ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) 
				conteur++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.LEFT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) 
				conteur++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.RIGHT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) 
				conteur++;
		}

		if ( conteur == zero )
			resultat = un;
		else if ( conteur == un || conteur == deux )
			resultat = deux;
		else if ( conteur == trois ) resultat = trois;

		return resultat;
	}
}