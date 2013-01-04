package org.totalboumboum.ai.v201213.ais.besnilikangal.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v1.BesniliKangal;

/**
 * Cette critere a été utilisée pour donner un priorité de chaque item visible qui sont soit GOLDEN
 * soit EXTRA. On ne considere pas les malus. Car les valeurs d'utilité d'une case qui contient un malus
 * ne va pas etre calculée.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
public class Pertinence extends AiUtilityCriterionInteger<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "Pertinence";

	/**
	 * 
	 * 
	 * @param ai
	 *		l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Pertinence( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME, 1, 7 );
		ai.checkInterruption();
	}

	/** */
	private final static int GOLDEN_BOMB_VALUE = 7;
	/** */
	private final static int GOLDEN_SPEED_VALUE = 6;
	/** */
	private final static int GOLDEN_FLAME_VALUE = 5;
	/** */
	private final static int EXTRA_BOMB_VALUE = 5;
	/** */
	private final static int EXTRA_SPEED_VALUE = 4;
	/** */
	private final static int EXTRA_FLAME_VALUE = 3;
	/** */
	private final static int RANDOM_EXTRA_VALUE = 1;

	@Override
	public Integer processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();

		List<AiItem> items = tile.getItems();

		AiHero hero = ai.getHero();
		int bombCount = hero.getBombNumberMax();
		int bombRange = hero.getBombRange();
		double walkingSpeed = hero.getWalkingSpeed();

		AiItemType itemType = items.get( 0 ).getType();
		if ( itemType == AiItemType.EXTRA_BOMB )
			return -1 * bombCount + EXTRA_BOMB_VALUE;
		else if ( itemType == AiItemType.EXTRA_SPEED )
			return (int) ( -1 * walkingSpeed + EXTRA_SPEED_VALUE );
		else if ( itemType == AiItemType.EXTRA_FLAME )
			return -1 * bombRange + EXTRA_FLAME_VALUE;
		else if ( itemType == AiItemType.GOLDEN_BOMB )
			return GOLDEN_BOMB_VALUE;
		else if ( itemType == AiItemType.GOLDEN_SPEED )
			return GOLDEN_SPEED_VALUE;
		else if ( itemType == AiItemType.GOLDEN_FLAME )
			return GOLDEN_FLAME_VALUE;
		else if ( itemType == AiItemType.RANDOM_EXTRA )
			return RANDOM_EXTRA_VALUE;
		else
			throw new StopRequestException();
	}
}
