package org.totalboumboum.ai.v201213.ais.besnilikangal.v3;

import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * Cette class contient toutes les methodes qui sont liées aux agents et qu'on a
 * besoin d'utiliser plusieurs fois.Cette class a besoin de notre IA pour
 * fonctionner.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class HeroOperation
{
	/** Notre IA */
	private BesniliKangal ai;
	/** Notre hero */
	private AiHero ownHero;
	/** La valeur constante utilisé pour determiner s'il y a un ennemi dans un radius */
	private static final int RADIUS = 4;

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
		ownHero = ai.ownHero;
	}

	/**
	 * Determiner la position de danger de notre IA. Controle s'il se trouvent
	 * dans(blast ou fire)
	 * 
	 * @return si notre IA est en danger ou pas.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Boolean isHeroInDanger() throws StopRequestException
	{
		ai.checkInterruption();
		return ai.tileOperation.getDangerousTiles().contains( ownHero.getTile() );
	}

	/**
	 * Determiner la position en danger d'un ennemi passé par parametre
	 * 
	 * @param hero
	 * 		information manquante !?	
	 * @return Si l'ennemi passé par parametre est en danger
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Boolean isEnemyInDanger( AiHero hero ) throws StopRequestException
	{
		ai.checkInterruption();
		return ai.tileOperation.getDangerousTiles().contains( hero.getTile() );
	}

	/**
	 * Determiner s'il y a en ennemi dans un RADIUS.Ici, on considere les cases
	 * surs
	 * 
	 * @param givenTile
	 * 		information manquante !?	
	 * @return s'il y a un ennemi dans un RADIUS.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isEnemyInRadius( AiTile givenTile ) throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiHero hero : ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( ai.getZone().getTileDistance( givenTile, hero.getTile() ) < RADIUS )
				;
			return true;
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
		double radius = ownHero.getBombDuration() * ownHero.getWalkingSpeed() / 1000;
		Set<AiTile> safeTiles = ai.tileOperation.getSafeTilesWithinRadius( (int) radius );
		Set<AiTile> blastedTiles = ai.tileOperation.getAllBlastTiles( ownHero.getBombPrototype() );
		safeTiles.removeAll( blastedTiles );
		return !safeTiles.isEmpty();
	}

	/**
	 * Controler s'il y a un ennemi dans les cases que notre IA peut atteindre.
	 * 
	 * @param ennemy
	 * 		information manquante !?	
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isEnemyAccessible( AiHero ennemy ) throws StopRequestException
	{
		ai.checkInterruption();
		return ai.tileOperation.getAccessibleTiles().contains( ennemy.getTile() );
	}

	/**
	 * Controle s'il y a un ennemi dans une case qui se trouve dans notre bombe
	 * portée
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
			if ( ownHero.getBombPrototype().getBlast().contains( hero.getTile() ) )
				return true;
		}
		return false;
	}

	/**
	 * Controle si on pose une bombe,on peut detruire un malus
	 * 
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean canDestroyMalus() throws StopRequestException
	{
		ai.checkInterruption();
		for ( AiTile tile : ownHero.getBombPrototype().getBlast() )
		{
			ai.checkInterruption();
			if ( !tile.getItems().isEmpty() && !ai.itemOperation.isGoodItem( tile.getItems().get( 0 ) ) )
			{
				if ( ai.pathOperation.isCurrentPathContainsCurrentTile( tile ) )
					return true;
			}
		}
		return false;
	}

	/**
	 * Renvoyer la valeur en double qui correspond a la valeur que notre IA peux
	 * traverser une case.
	 * 
	 * @return double
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public double passTimeByTile() throws StopRequestException
	{
		ai.checkInterruption();
		return 1000 * ( ownHero.getTile().getSize() / ownHero.getWalkingSpeed() );
	}

	/**
	 * Renvoyer l'ennemi qui est le plus proche de notre IA.
	 * 
	 * @return AiHero
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiHero getClosestEnnemy() throws StopRequestException
	{
		ai.checkInterruption();
		int distance = Integer.MAX_VALUE;
		AiHero closestEnnemy = null;
		for ( AiHero ennemy : ai.getZone().getRemainingHeroes() )
		{
			ai.checkInterruption();
			int distanceEnnemy = ai.getZone().getTileDistance( ennemy.getTile(), ai.ownHero.getTile() );
			if ( distanceEnnemy < distance )
			{
				distance = distanceEnnemy;
				closestEnnemy = ennemy;
			}
		}
		return closestEnnemy;
	}
}
