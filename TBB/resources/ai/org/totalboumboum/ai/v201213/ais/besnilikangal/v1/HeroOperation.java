package org.totalboumboum.ai.v201213.ais.besnilikangal.v1;

import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * Cette class contient toutes les methodes qui sont liées aux agents et qu'on a besoin d'utiliser 
 * plusieurs fois.Cette class a besoin de notre IA pour fonctionner.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
public class HeroOperation
{
	/** Notre IA */
	private BesniliKangal ai;
	/** La valeur constante utilisé pour determiner s'il y a un ennemi dans un radius */
	private static final int RADIUS = 3;

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public HeroOperation( BesniliKangal ai ) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
	}

	/**
	 * Determiner la position de danger de notre IA. Controle s'il se trouvent dans(blast ou fire)
	 * 
	 * @return si notre IA est en danger ou pas.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Boolean isHeroInDanger() throws StopRequestException
	{
		ai.checkInterruption();
		return ai.getTileOperation().getDangerousTiles().contains( ai.getHero().getTile() );
	}

	/**
	 * Determiner la position en danger d'un ennemi passé par parametre
	 * 
	 * @param hero
	 * 
	 * @return Si l'ennemi passé par parametre est en danger
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Boolean isEnemyInDanger( AiHero hero ) throws StopRequestException
	{
		ai.checkInterruption();
		return ai.getTileOperation().getDangerousTiles().contains( hero.getTile() );
	}

	/**
	 * Determiner s'il y a en ennemi dans un RADIUS.Ici, on considere les cases surs
	 * 
	 * @param givenTile
	 * 
	 * @return s'il y a un ennemi dans un RADIUS.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isEnemyInRadius( AiTile givenTile ) throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> safeTiles = ai.getTileOperation().getSafeTilesWithinRadius( RADIUS );
		for ( AiHero hero : ai.getZone().getHeroes() )
		{
			ai.checkInterruption();
			return ( safeTiles.contains( hero.getTile() ) );
		}
		return false;
	}

	/**
	 * Controle si notre agent peut trover une case sure.
	 * 
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean canReachSafety() throws StopRequestException
	{
		ai.checkInterruption();
		double radius = ai.getHero().getBombDuration() * ai.getHero().getWalkingSpeed() / 1000;
		Set<AiTile> safeTiles = ai.getTileOperation().getSafeTilesWithinRadius( (int) radius );
		Set<AiTile> blastedTiles = ai.getTileOperation().getAllBlastTiles( ai.getHero().getBombPrototype() );
		safeTiles.removeAll( blastedTiles );
		return !safeTiles.isEmpty();
	}

	/**
	 * Controler s'il y a un ennemi dans les cases que notre IA peut atteindre.
	 * 
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isEnemyAccessible() throws StopRequestException
	{
		ai.checkInterruption();
		Set<AiTile> accessibleTiles = ai.getTileOperation().getAccessibleTiles();
		for ( AiHero ennemy : ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			return ( accessibleTiles.contains( ennemy.getTile() ) );
		}
		return false;
	}

	/**
	 * Controle s'il y a un ennemi dans une case qui se trouve dans notre bombe portée
	 * 
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean hasEnemyInBombRange() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiHero hero : ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			return ai.getHero().getBombPrototype().getBlast().contains( hero.getTile() );
		}
		return false;
	}
}
