package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v1;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails..
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();	
	}
	/** La liste de tile accessinble
	 */
	public ArrayList<AiTile> accessibletiles;
	
	/** La liste de tile control
	 */
	public ArrayList<AiTile> controltiles;
	
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
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		this.accessibletiles = new ArrayList<AiTile>();
		this.controltiles = new ArrayList<AiTile>();
		
		  AiZone zone = ai.getZone();
		  AiTile mytile = zone.getOwnHero().getTile();
		 // List<AiTile> accessible_tiles = new ArrayList<AiTile>();
		  
		//List<AiTile> all_tiles = zone.getTiles();
		
		accessibletiles.add(mytile);
		controltiles.add(mytile);  
		
		for(int i=1;i<=10;i++){
				ai.checkInterruption();
				for(AiTile item : controltiles){
					ai.checkInterruption();					
					if(item.getNeighbor(Direction.RIGHT).isCrossableBy(zone.getOwnHero())){
						accessibletiles.add(item.getNeighbor(Direction.RIGHT));
					}
					if(item.getNeighbor(Direction.LEFT).isCrossableBy(zone.getOwnHero())){
						accessibletiles.add(item.getNeighbor(Direction.LEFT));
					}
					if(item.getNeighbor(Direction.UP).isCrossableBy(zone.getOwnHero())){
						accessibletiles.add(item.getNeighbor(Direction.UP));
					}
					if(item.getNeighbor(Direction.DOWN).isCrossableBy(zone.getOwnHero())){
						accessibletiles.add(item.getNeighbor(Direction.DOWN));
					}
				}	
				controltiles.addAll(accessibletiles);
		}
		result.addAll(accessibletiles);
		
			//if(item.isCrossableBy(zone.getOwnHero())){
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "CATEGORY1";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	//private static String CAT_NAME_2 = "SECOND_CATEGORY";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	//private static String CAT_NAME_3 = "THIRD_CATEGORY";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;	
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode		
		// à titre d'exemple, ici on sélectionne une catégorie de façon arbitraire.
		// bien sûr ceci n'a pas de sens au niveau de la conception d'un agent
		// c'est seulement pour illustrer cette étape du traitement en termes de code source.
		
		//AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		//AiMode mode = modeHandler.getMode();
		// pour le mode attaque, on n'avait défini qu'une seule catégorie possible dans le fichier XML de préférences
		
		//if(mode==AiMode.ATTACKING)
			
			result = getCategory(CAT_NAME_1);
		
		// pour le mode collecte, on avait le choix entre 2 categories differentes
		
		/*else
		{	// par exemple ici, on utilise la présence d'un item pour déterminer le cas
			// là encore, cela n'a pas de sens en termes de conception de l'agent,
			// il s'agit seulement d'illustrer comme le code source peut être écrit
			AiZone zone = ai.getZone();
			AiHero ownHero = zone.getOwnHero();
			AiTile currentTile = ownHero.getTile();
			List<AiItem> items = currentTile.getItems();
			if(items.isEmpty())
				result = getCategory(CAT_NAME_2);
			else
				result = getCategory(CAT_NAME_1);
		}*/
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();	
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();	
	}
}
