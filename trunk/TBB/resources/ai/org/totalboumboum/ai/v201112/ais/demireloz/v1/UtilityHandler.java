package org.totalboumboum.ai.v201112.ais.demireloz.v1;

import java.util.Iterator;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion.Competition;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion.Convenience;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion.Danger;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion.NbrAdjacentWall;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion.NbrCloseEnemy;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion.Time;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */
public class UtilityHandler extends AiUtilityHandler<DemirelOz>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public UtilityHandler(DemirelOz ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	
		// 
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String caseName1 = "Visible Item";
	private final String caseName2 = "Tile close to a destructible wall in danger";
	private final String caseName3 = "Tile close to a destructible wall";
	
	private final String caseName4 = "Tile in range of a enemy";
	private final String caseName5 = "Tile adjacent to an enemy in a deadlock";
	private final String caseName6 = "Tile with visible item(s) in the range with enemies around";
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		Set<AiTile> result = new TreeSet<AiTile>();
		Iterator<AiTile> ls = ai.getZone().getOwnHero().getTile().getNeighbors().iterator();
			
			while(ls.hasNext())
			{
				result.add(ls.next());
			}
				return result;
	}
	
		
		

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		Time time = new Time(ai);
		Convenience convenience = new Convenience(ai);
		Competition competition = new Competition(ai);
		Danger danger = new Danger(ai);
		NbrAdjacentWall nbrAdjacentWall = new NbrAdjacentWall(ai);
		NbrCloseEnemy nbrCloseEnemy = new NbrCloseEnemy(ai);
		
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(convenience);
		criteria.add(danger);
		criteria.add(competition);
		AiUtilityCase case1 = new AiUtilityCase(caseName1,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(competition);
		criteria.add(danger);
		AiUtilityCase case2 = new AiUtilityCase(caseName2,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(danger);
		criteria.add(nbrAdjacentWall);
		AiUtilityCase case3 = new AiUtilityCase(caseName3,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(danger);
		criteria.add(nbrAdjacentWall);
		AiUtilityCase case4 = new AiUtilityCase(caseName4,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(danger);
		AiUtilityCase case5 = new AiUtilityCase(caseName5,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(danger);
		criteria.add(nbrCloseEnemy);
		AiUtilityCase case6 = new AiUtilityCase(caseName6,criteria);
		
		cases.put(caseName1,case1);
		cases.put(caseName2,case2);
		cases.put(caseName3,case3);
		cases.put(caseName4,case4);
		cases.put(caseName5,case5);
		cases.put(caseName6,case6);
		
	
		AiUtilityCombination combi;
		AiUtilityCombination combi1;
		
	 
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,false);
			combi.setCriterionValue(danger,false);
			referenceUtilities.put(combi,33);
			
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,false);
			combi.setCriterionValue(danger,false);
			referenceUtilities.put(combi,32);
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,false);
			referenceUtilities.put(combi,31);
		
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,false);
		    referenceUtilities.put(combi,30);
			
		}
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,false);
		    referenceUtilities.put(combi,21);
			
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,false);
			referenceUtilities.put(combi,20);
		}
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,false);
			referenceUtilities.put(combi,19);
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,false);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,18);
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,false);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,17);
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,16);
		}
		
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,false);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,15);
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,true);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,14);
		}
		
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,13);
		}
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,false);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,12);
		}
		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(convenience,false);
			combi.setCriterionValue(competition,true);
			combi.setCriterionValue(danger,true);
			referenceUtilities.put(combi,11);
		}
		
		
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(competition,false);
			referenceUtilities.put(combi,29);
		}
		
		{	
			combi = new AiUtilityCombination(case2);
	        combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(competition,true);
			referenceUtilities.put(combi,23);
		}
		
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(competition,true);
			referenceUtilities.put(combi,22);
			
		}
		
		
		{	
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(competition,false);
			referenceUtilities.put(combi,10);
		
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(competition,true);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,9);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(competition,false);
			referenceUtilities.put(combi,8);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(competition,true);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,7);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(nbrAdjacentWall,3);
			
			referenceUtilities.put(combi,28);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,27);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(nbrAdjacentWall,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,26);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,25);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,false);
			combi.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,24);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,6);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,5);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,true);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(nbrAdjacentWall,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,4);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,3);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,2);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque crit�re
			combi.setCriterionValue(time,false);
			combi.setCriterionValue(danger,true);
			combi.setCriterionValue(nbrAdjacentWall,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi,1);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		

		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,28);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,27);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,1);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,26);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,23);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,22);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,1);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,21);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrAdjacentWall,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,20);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,14);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,13);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,1);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,12);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,9);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,8);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,1);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,7);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,4);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case4);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrAdjacentWall,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,2);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case5);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
	 			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,29);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case5);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
	 			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,17);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case5);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
	 			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,15);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrCloseEnemy,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,25);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrCloseEnemy,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,24);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrCloseEnemy,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,19);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrCloseEnemy,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,18);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,false);
			combi1.setCriterionValue(nbrCloseEnemy,0);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,16);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrCloseEnemy,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,11);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrCloseEnemy,2);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,10);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrCloseEnemy,3);
			
			// on rajoute la combinaison dans la map, avec son utilit�
			referenceUtilities.put(combi1,6);
			// on incr�mente l'utilit� pour la combinaison suivante
		}
		{	// on cr�e la combinaison (vide pour l'instant)
			combi1 = new AiUtilityCombination(case6);
			// on affecte les valeurs de chaque crit�re
			combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrCloseEnemy,2);
			referenceUtilities.put(combi1,5);
		
		}
		{	
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(time,true);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrCloseEnemy,0);
			referenceUtilities.put(combi1,3);
			
		}
		
		{	
			combi1 = new AiUtilityCombination(case6);
		    combi1.setCriterionValue(time,false);
			combi1.setCriterionValue(danger,true);
			combi1.setCriterionValue(nbrCloseEnemy,0);
			referenceUtilities.put(combi1,1);
		
		}
	
	}
	
	@SuppressWarnings("unused")
	@Override
	

	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		AiUtilityCase result = null;
	
		 Time a = new Time(ai);
		 Convenience b = new Convenience(ai);
		 Danger c = new Danger(ai);
	     Competition d = new Competition (ai);
	     NbrAdjacentWall e = new NbrAdjacentWall(ai);
		 NbrCloseEnemy f = new NbrCloseEnemy(ai);
   
	AiMode mode= this.ai.modeHandler.getMode();

	if(AiMode.COLLECTING==mode)	
	{
		
	if(tile.getItems().contains(AiItemType.EXTRA_BOMB)||tile.getItems().contains(AiItemType.EXTRA_FLAME))
		
	{
      
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();

        criteria.add(a);
		criteria.add(b);
		criteria.add(c);
		criteria.add(d);

		result = new AiUtilityCase(caseName1,criteria);
		}
	

	
	
	
	for (AiBlock block : tile.getNeighbor(Direction.UP).getBlocks()) 
	{
		for (AiBlock block1 : tile.getNeighbor(Direction.DOWN).getBlocks()) 
		{
			for (AiBlock block2 : tile.getNeighbor(Direction.LEFT).getBlocks()) 
			{
				for (AiBlock block3 : tile.getNeighbor(Direction.RIGHT).getBlocks()) 
				{
			if(block.isDestructible() || block1.isDestructible()||block2.isDestructible() ||block3.isDestructible())
	{
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		
		for (AiBomb currentBomb : this.ai.getZone().getBombs()) 
		{
			if (currentBomb.getBlast().contains(tile))
			{
				criteria.add(a);
				criteria.add(d);
				criteria.add(c);
				result = new AiUtilityCase(caseName2,criteria);
				
			}
			else
			{
				criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(a);
				criteria.add(c);
				criteria.add(e);
				result= new AiUtilityCase(caseName3,criteria);
				
			}
	
	}
	}
	}
	}	
	}
	}
	}
	

	//if(AiMode.ATTACKING==mode)
	//{
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
	
		//int currentrange=ai.getZone().getOwnHero().getBombRange();
		
		/*if(this.ai.getZone().getRemainingOpponents().isEmpty()!=false)
		{	
		for (AiHero currentopponent : this.ai.getZone().getRemainingOpponents())
		{
		if(ai.getZone().getTileDistance(tile,currentopponent.getTile())<=currentrange )
			*/
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(a);
		criteria.add(c);
		criteria.add(e);
		result= new AiUtilityCase(caseName4,criteria);
		//}
		
		
		//}
	//}
		return result;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
	super.updateOutput();
		//  à redéfinir, si vous voulez afficher d'autres informations
	}
}
