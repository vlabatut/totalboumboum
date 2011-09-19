package org.totalboumboum.ai.v200809.ais.akpolatsener.v1;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * la classe XTile est l'abreviation de Extended Tile
 * 
 * @author Cem Akpolat
 * @author Emre Sener
 *
 */
public class XTile {
	
	/** nombre de visites à cette case */
	int visits;
	
	/** nombre des blocs entre cette case et une case cible */
	int blockCount;
	
	/** nombre des voisins vides de cette case */
	int neighborCount;
	
	/** distance directe entre cette case et l'enemie la plus proche */
	double hypotenuse;
	
	/** distance directe entre cette case et la bombe la plus proche  */
	double distanceToBomb;
	
	/** la classe principale de notre IA */
	AkpolatSener sa;

	/** case elargie */
	AiTile tile;

	/** personne pour acceder aux information sur les personnages */
	Hero hero;
	
	/** voisin pour acceder aux informations sur les voisins */
	Neighbors neighbors;
	

	public XTile(AkpolatSener sa, AiTile tile) throws StopRequestException 
	{
		sa.checkInterruption();
		
		this.sa = sa;
		this.tile = tile;
		this.visits = 0;
	}

	/**
	 * renvoie la valeur heuristique de cette case
	 * @return heuristic valeur heuristique de cette case
	 * @throws StopRequestException
	 */
	public double getHeuristic() throws StopRequestException {
		sa.checkInterruption();

		hero = new Hero(sa, sa.zone, sa.ownHero);
		neighbors = new Neighbors(sa, sa.zone, sa.currentTile);

		hypotenuse = hero.getHyponetuseToTarget(sa.enemy,tile);
		//distanceToBomb = neighbors.getHypotenuseToBomb(tile);
		//blockCount=hero.blockCountOnWay(sa.enemy,tile);
		//neighborCount=neighbors.getNeighborsNumber(tile);

		double heur = blockCount - neighborCount + hypotenuse- distanceToBomb*5;
		
		return heur;
	}

}
