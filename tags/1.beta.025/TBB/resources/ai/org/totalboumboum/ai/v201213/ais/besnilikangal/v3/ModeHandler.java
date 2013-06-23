package org.totalboumboum.ai.v201213.ais.besnilikangal.v3;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;

/**
 * Classe gérant les déplacements de l'agent.
 * 
 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<BesniliKangal>
{
	/** La limite de la portée d'une bombe pour qu'on puisse passer en mode attaque */
	private final int BOMB_RANGE_LIMIT = 3;
	/** La limite de bombe pour qu'on puisse passer en mode attaque */
	private final int BOMB_COUNT_LIMIT = 3;
	/** Accessible tile size limit si cette limit est egale ou plus petit que cette valeur,on passe en mode attaque */
	private final int ACCESSIBLE_TILE_SIZE_LIMIT = 3;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler( BesniliKangal ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING 				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{
		ai.checkInterruption();
		return ( ( ai.ownHero.getBombRange() >= BOMB_RANGE_LIMIT ) || ( ai.ownHero.getBombNumberMax() >= BOMB_COUNT_LIMIT ) || ai.tileOperation.getAccessibleTiles().size() <= ACCESSIBLE_TILE_SIZE_LIMIT );
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiItem itemVisible : ai.getZone().getItems() )
		{
			ai.checkInterruption();
			if ( ai.itemOperation.isGoodItem( itemVisible ) )
				return true;
		}
		return ( ai.getZone().getHiddenItemsCount() > 0 );
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
	}
}
