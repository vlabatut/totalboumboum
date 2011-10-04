package tournament200809v1.akpolatsener;

import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;

/**
 * la classe XTile est l'abreviation de Extended Tile
 * @author SenerAkpolat
 *
 */
public class XTile {
	
	/** nombre de visites � cette case */
	int visits;
	
	/** nombre des blocs entre cette case et une case cible */
	int blockCount;
	
	/** nombre des voisins vides de cette case */
	int neighbourCount;
	
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
	Neighbours neighbours;
	

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
		neighbours = new Neighbours(sa, sa.zone, sa.currentTile);

		hypotenuse = hero.getHyponetuseToTarget(sa.enemy,tile);
		//distanceToBomb = neighbours.getHypotenuseToBomb(tile);
		//blockCount=hero.blockCountOnWay(sa.enemy,tile);
		//neighbourCount=neighbours.getNeighboursNumber(tile);

		double heur = blockCount - neighbourCount + hypotenuse- distanceToBomb*5;
		
		return heur;
	}

}