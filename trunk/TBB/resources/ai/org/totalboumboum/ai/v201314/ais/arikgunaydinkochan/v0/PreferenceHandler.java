package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
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
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
	
		/*
		 *  TODO à compléter (si nécessaire).
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();
		
		/*
		 *  TODO à compléeter, si nécessaire, pour réinitialiser certaines 
		 *  structures de données à chaque itération.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles()
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		/*
		 *  TODO à compléter afin de sélectionner les cases dont on veut calculer la préférence.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
		
		// ici, à titre d'exemple, on sélectionne aléatoirement 10 cases
		// bien sûr ceci n'a pas de sens au niveau de la conception d'un agent
		// c'est seulement pour illustrer cette étape du traitement en termes de code source.
		List<AiTile> tiles = ai.getZone().getTiles();
		tiles = new ArrayList<AiTile>(tiles);
		Collections.shuffle(tiles);
		result.addAll(tiles.subList(0,Math.min(tiles.size(),10)));
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "FIRST_CATEGORY";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "SECOND_CATEGORY";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_3 = "THIRD_CATEGORY";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		
		/*
		 *  TODO à compléter pour identifier la catégorie associée à la case passée en paramètre.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
		
		// à titre d'exemple, ici on sélectionne une catégorie de façon arbitraire.
		// bien sûr ceci n'a pas de sens au niveau de la conception d'un agent
		// c'est seulement pour illustrer cette étape du traitement en termes de code source.
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		// pour le mode attaque, on n'avait défini qu'une seule catégorie possible dans le fichier XML de préférences
		if(mode==AiMode.ATTACKING)
			result = getCategory(CAT_NAME_3);
		// pour le mode collecte, on avait le choix entre 2 categories differentes
		else
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
		}
		
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
		
		/*
		 *  TODO à redéfinir, si vous voulez afficher d'autres informations.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	}
}
