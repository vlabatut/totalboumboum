package org.totalboumboum.ai.v201314.ais.enginserhantipici.v1;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails.
 * 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	/**
	 * 
		* Pour selection des cases on a une facteur;
		* cette facteur est menace d'une bombe sur
		* si on est en menace, premierement on pense d'echaper, on choisi les tile voisines
		* si on n'est pas en menace, on pense d'attacker les adversaires.
		* ResultA contiens les tiles voisines
		* ResultB contiens les tiles proche aux adversaires.
		
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
	
		
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();
		
		
		
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles()
	{	
		
		ai.checkInterruption();
		
	
		Set<AiTile> result = new TreeSet<AiTile>();
		Set<AiTile> resultA = new TreeSet<AiTile>();
		List<AiTile> tiles = ai.getZone().getTiles();
		tiles = new ArrayList<AiTile>(tiles);
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		AiTile currentTile = ownHero.getTile();
		
		
		/**
		 * ici on ajoute les cases autour de notre agent a resultA.
		 */
		
		resultA.addAll(currentTile.getNeighbors());
		
		int i = 0;
		List<AiHero> heroes = new ArrayList<AiHero>();
		
		heroes.addAll(zone.getHeroes());
		for(AiHero hero : heroes){
			PredefinedColor color = hero.getColor();
			
			
			if(color == PredefinedColor.INDIGO){
				heroes.remove(i);
			}	
			i++;
		}
		
		for(AiHero hero : heroes){
			/**
			 * ici on choisi les cases diagonal d'une mur si on a deja une adversaire l'un des diagonals.
			 */
			ai.checkInterruption();
			AiTile tile = hero.getTile();
			if(!tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.DOWN).getBlocks().isEmpty()){
				if(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT));
				if(tile.getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN));
				if(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN));
			}
			if(!tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.DOWN).getBlocks().isEmpty()){
				if(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT));
				if(tile.getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN));
				if(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN));
			}
			if(!tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.UP).getBlocks().isEmpty()){
				if(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT));
				if(tile.getNeighbor(Direction.UP).getNeighbor(Direction.UP).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.UP).getNeighbor(Direction.UP));
				if(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).getNeighbor(Direction.UP).getNeighbor(Direction.UP).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).getNeighbor(Direction.UP).getNeighbor(Direction.UP));
			}
			if(!tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.UP).getBlocks().isEmpty()){
				if(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT));
				if(tile.getNeighbor(Direction.UP).getNeighbor(Direction.UP).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.UP).getNeighbor(Direction.UP));
				if(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).getNeighbor(Direction.UP).getNeighbor(Direction.UP).isCrossableBy(ownHero))
				resultA.add(tile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).getNeighbor(Direction.UP).getNeighbor(Direction.UP));
			}
		}
		
		
	
		

		
	
		result = resultA;
		

		
		
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "ECHAPE";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "ATTACK";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	//private static String CAT_NAME_3 = "THIRD_CATEGORY";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		

		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		if(mode==AiMode.ATTACKING){
			
			AiZone zone = ai.getZone();
			AiHero ownHero = zone.getOwnHero();
			AiTile currentTile = ownHero.getTile();
			
			List<AiBomb> bombs = new ArrayList<AiBomb>();
			bombs = zone.getBombs();
			if(!bombs.isEmpty())
				for(AiBomb bomb : bombs){
					ai.checkInterruption();

					List<AiTile> menaceTiles = new ArrayList<AiTile>();
					menaceTiles.addAll(bomb.getBlast());
					if(menaceTiles.contains(currentTile)){
						result = getCategory(CAT_NAME_1);
						break;
					}else{
						result = getCategory(CAT_NAME_2);
					}
				}else{
					result = getCategory(CAT_NAME_2);
				}
			
		}
	//	else
//		{	
			
	//	}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		super.updateOutput();
		
		
	}
}
