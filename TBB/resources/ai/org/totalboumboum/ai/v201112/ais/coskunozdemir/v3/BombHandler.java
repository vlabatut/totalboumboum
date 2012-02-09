package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import java.util.ArrayList;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Our bomb handler class.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<CoskunOzdemir>
{
	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected BombHandler( CoskunOzdemir ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{
		ai.checkInterruption();

		this.ai.setUtilityMap( this.ai.getUtilityHandler().getUtilitiesByTile() );
		if ( this.ai.getHero().getTile().equals( this.ai.getTileWithBiggestUtility() ) && !this.ai.getHo().isHeroInDanger() && this.ai.getHo().canReachSafety() )
		{
			// ATTACK MODE
			if ( this.ai.getModeHandler().getMode().equals( AiMode.ATTACKING ) )
			{
				boolean enemyAccessible = false;
				ArrayList<AiTile> enemyTiles = new ArrayList<AiTile>();
				for ( AiHero aiHero : this.ai.getZone().getRemainingOpponents() )
				{
					ai.checkInterruption();
					enemyTiles.add( aiHero.getTile() );
				}
				for ( AiTile aiTile : enemyTiles )
				{
					ai.checkInterruption();
					if ( this.ai.getTo().getAccessibleTiles().contains( aiTile ) )
					{
						enemyAccessible = true;
						break;
					}
				}

				if ( !enemyAccessible )
				{
					boolean fireInAccessibleTiles = false;
					for ( AiTile aiTile : this.ai.getTo().getAccessibleTiles() )
					{
						ai.checkInterruption();
						if ( !aiTile.getFires().isEmpty() )
						{
							fireInAccessibleTiles = true;
							break;
						}
					}

					if ( this.ai.getZone().getBombsByColor( PredefinedColor.BLACK ).isEmpty() && !fireInAccessibleTiles )
					{
						return true;
					}
				}
				else
				{
					if ( this.ai.getHo().isHeroInRange() )
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			// COLLECT MODE
			else
			{
				return true;
			}
		}
		return false;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
	}
}
