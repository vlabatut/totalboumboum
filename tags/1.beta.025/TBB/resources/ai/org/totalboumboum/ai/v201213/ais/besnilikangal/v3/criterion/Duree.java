package org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.BesniliKangal;

/**
 * Cette critere a été utilisé pour évaluer le temps d'arrivée pour une case. Le
 * temps est proportionnel à la distance entre notre IA et cette case.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class Duree extends AiUtilityCriterionInteger<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "Duree";

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, MIN, MAX );
		ai.checkInterruption();
	}

	/**
	 * @param ai
	 *            l'agent concerné.
	 * @param min
	 * 		information manquante !?	
	 * @param max
	 * 		information manquante !?	
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree( BesniliKangal ai, int min, int max ) throws StopRequestException
	{
		super( ai, NAME, min, max );
		ai.checkInterruption();
		MIN = min;
		MAX = max;
		ai.checkInterruption();
		this.ai = ai;
	}

	/**La valeur maximale pour cette critere */
	private static int MAX = 3;
	/**La valeur minimale pour cette critere*/
	private static int MIN = 0;
	/** La valeur multipliant puisque le critere renvoye la 0 pour 0-2 et 1 pour 3-5 etc*/
	private final int MULTIPLIER = 3;

	/////////////////////////////////////////////////////////////////
	// PROCESS 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( !ai.tileOperation.getAccessibleSafeTiles().contains( tile ) )
			return MAX;

		AiTile current = ai.ownHero.getTile();
		double[] delta = ai.getZone().getPixelDeltas( current.getPosX(), current.getPosY(), tile.getPosX(), tile.getPosY() );
		// sumDistance hero'nun bulundugu tile ile target tile arasindaki kus
		// ucusu uzakligi ifade eder
		double sumDistance = Math.abs( delta[0] ) + Math.abs( delta[1] );
		double tileSize = tile.getSize();

		int max = MAX;
		while ( MIN < max )
		{
			ai.checkInterruption();
			// uzaklik 0-3 arasinda ise 0, 3-6 arasinda ise 1, 6-9 arasinda ise
			// 2, 9 ve uzerinde ise 3 degerini alir
			// MULTIPLIER ise 3 verilerek katsayi belirlenmistir.
			if ( max * MULTIPLIER * tileSize < sumDistance )
				return max;
			max--;
		}
		return MIN;
	}
}
