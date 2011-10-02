package org.totalboumboum.ai.v200809.ais.akpolatsener.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Cem Akpolat
 * @author Emre Şener
 *
 */
@SuppressWarnings("deprecation")
public class Hero {

	/** classe principale de notre IA */
	AkpolatSener sa;
	
	/** enemie et notre IA */
	AiHero enemy;
	AiHero ownHero;
	
	/** zone du jeu */
	AiZone zone;

	public Hero(AkpolatSener sa, AiZone zone, AiHero ownHero) throws StopRequestException 
	{
		sa.checkInterruption();
		
		this.sa = sa;
		this.ownHero = ownHero;
		this.zone = zone;
	}

	/**
	 * determine si on est suffisamment proche d'une enemie pour poser une bombe
	 * @param distanceToEnemy condition d'etre proche où non
	 * @return true si on peut poser une bombe, false si on n'et pas
	 * @throws StopRequestException
	 */
	public boolean canPutBomb(int distanceToEnemy) throws StopRequestException {
		sa.checkInterruption();
		
		boolean result = false;
		
		enemy = getNearHero();
		
		int x = Math.abs(ownHero.getCol() - enemy.getCol());
		int y = Math.abs(ownHero.getLine() - enemy.getLine());

		if ((x < distanceToEnemy && y < distanceToEnemy) || (x == 0 || y == 0))
			result = true;
		/*
		 * else { collectionner les bonuses etc }
		 */
		return result;

	}

	/**
	 * renvoi l'enemie la plus proche de notre IA
	 * @return enemie la plus proche de notre IA
	 * @throws StopRequestException
	 */
	public AiHero getNearHero() throws StopRequestException {
		sa.checkInterruption();
		
		Collection<AiHero> allHeros = zone.getHeroes();
		Iterator<AiHero> iter = allHeros.iterator();
		
		List<AiHero> allHeros2 = new ArrayList<AiHero>();
		List<Integer> heroDistance = new ArrayList<Integer>();

		int heroNumber;
		
		while (iter.hasNext()) 
		{			
			sa.checkInterruption();
			
			int result = 0;
			AiHero otherHero = (AiHero) iter.next();

			if (otherHero != ownHero) {
				
				result = Math.abs(otherHero.getCol() + otherHero.getLine() - ownHero.getLine() - ownHero.getCol());
				heroDistance.add(result);
				allHeros2.add(otherHero);
			}
		}
		
		// on obtient l'index de l'element qui est le plus petit dans la liste 
		heroNumber = heroDistance.indexOf(Collections.min(heroDistance));
		
		// renvoie la personne qui est dans l'index qu'on a obtenu
		return allHeros2.get(heroNumber);

	}

	/**
	 * renvoie la distance de la case vers l'enemie la plus proche
	 * @param heroNear enemie la plus proche
	 * @param tile case à calculer la distance vers l'enemie
	 * @return distance distance hypotenuse entre la case et l'enemie
	 * @throws StopRequestException
	 */
	public double getHyponetuseToTarget(AiHero heroNear, AiTile tile) throws StopRequestException {
		sa.checkInterruption();
		
		int x = tile.getCol() - heroNear.getCol();
		int y = tile.getLine() - heroNear.getLine();
				
		return Math.sqrt( x*x + y*y );
	}

	/**
	 * renvoie le nombre des blocs sur la route que l'on utilisera pour arriver la case où l'enemie est.
	 * @param heroNear
	 * @return blockCount nombre de blocs
	 * @throws StopRequestException
	 */
	public int blockCountOnWay(AiHero heroNear,AiTile tile) throws StopRequestException {
		sa.checkInterruption();
		
		int countBlock = 0;
		
		// mouvement verticale
		int col = tile.getCol() - heroNear.getCol();
		
		// mouvement horizontale
		int line = tile.getLine() - heroNear.getLine();

		///////////////////////////////////////
		// nombre des blocs horizontalement  //
		///////////////////////////////////////
		
		// enemie est à gauche
		if (col >= 0) {
			for (int i = 0; i < col; i++) 
			{
				sa.checkInterruption();
				
				AiTile tempTile = zone.getTile(tile.getLine(),tile.getCol() - i);
				if (tempTile.getBlock() != null)
					countBlock++;
			}
		}
		
		// enemie est à droite
		else {
			for (int i = 0; i < -col; i++) 
			{
				sa.checkInterruption();
				
				AiTile tempTile = zone.getTile(tile.getLine(),tile.getCol() + i);
				if (tempTile.getBlock() != null)
					countBlock++;
			}
		}
		
		///////////////////////////////////////
		// nombre des blocs verticalement  //
		///////////////////////////////////////
		
		// enemie est en haut
		if (line >= 0) {
			for (int i = 0; i < line; i++) 
			{
				sa.checkInterruption();
				
				AiTile tempTile = zone.getTile(tile.getLine() - i,tile.getCol());
				if (tempTile.getBlock() != null)
					countBlock++;
			}
		}
		
		// enemie est en base
		else {
			for (int i = 0; i < -line; i++)
			{
				sa.checkInterruption();
				
				AiTile tempTile = zone.getTile(tile.getLine() + i,tile.getCol());
				if (tempTile.getBlock() != null)
					countBlock++;
			}
		}
		
		return countBlock;
	}

}
