package tournament200809.test;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.ai.adapter200809.AiHero;
import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.AiZone;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200809.action.AiAction;
import fr.free.totalboumboum.ai.adapter200809.action.AiActionName;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class Test extends ArtificialIntelligence
{
	private int[] currentTile = new int[2];
	private int[] nextTile = null;
	private int[] previousTile = null;
	
	@Override
	public AiAction call() throws Exception
	{	// on met à jour la position de l'ia dans la zone
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		currentTile[0] = ownHero.getLine();
		currentTile[1] = ownHero.getCol();
		
		// premier appel : on initialise l'IA
		if(nextTile == null)
			init();
		
		// arrivé à destination : on choisit une nouvelle destination
		if(currentTile[0]==nextTile[0] && currentTile[1]==nextTile[1])
			pickNextTile();

		// on avance vers la destination
		Direction direction = getNextDirection();

		// on renvoie le résultat
		AiActionName name = AiActionName.MOVE;
		AiAction result = new AiAction(name,direction);
		return result;
	}

	private void init()
	{	// prochaine destination
		nextTile = new int[2];
		nextTile[0] = currentTile[0];
		nextTile[1] = currentTile[1];		
		// ancienne destination
		previousTile = new int[2];
		previousTile[0] = currentTile[0];
		previousTile[1] = currentTile[1];		
	}
	
	/**
	 * Choisit comme destination une case voisine de la case actuellement occupée par l'IA.
	 * Cette case doit être accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * être différente de la case précédemment occupée
	 */
	private void pickNextTile()
	{	// liste des cases autour de la case actuelle
		ArrayList<AiTile> neighbours = new ArrayList<AiTile>();
		ArrayList<Direction> directions = Direction.getAllPrimaries(); // cette fonction renvoie la liste des directions primaires : haut, bas, droite, gauche
		Iterator<Direction> d = directions.iterator();
		while(d.hasNext())
		{	Direction temp = d.next();
			AiTile tile = 
		}
		
		
		int temp[] = new int[2];
		
		
	}
}
