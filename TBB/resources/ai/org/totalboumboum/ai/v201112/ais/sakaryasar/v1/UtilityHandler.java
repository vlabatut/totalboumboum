package org.totalboumboum.ai.v201112.ais.sakaryasar.v1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion.CriterionChaineReaction;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion.CriterionDanger;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion.CriterionDestructibleWalls;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion.CriterionEnemies;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion.CriterionEnemyDirection;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion.CriterionPertinance;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<SakarYasar>
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
	protected UtilityHandler(SakarYasar ai) throws StopRequestException
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
	private final String caseName1 = "CAS1";
	private final String caseName2 = "CAS2";
	private final String caseName3 = "CAS3";


	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	Set<AiTile> result = new TreeSet<AiTile>();
		
		//  à compléter afin de sélectionner les cases
		// dont on veut calculer l'utilité
	
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode
		result.add(ai.getZone().getOwnHero().getTile());

		AiTile left , up, right, down;
		Queue<AiTile> qTile = new LinkedList<AiTile>();
		
		qTile.add(ai.getZone().getOwnHero().getTile());

		while(!qTile.isEmpty()){
			if(!result.contains(qTile.element()))
				result.add(qTile.element());

			left = qTile.element().getNeighbor(Direction.LEFT);
			right = qTile.element().getNeighbor(Direction.RIGHT);
			up = qTile.element().getNeighbor(Direction.UP);
			down =qTile.element().getNeighbor(Direction.DOWN);
			
			if(up.getBlocks().isEmpty() && !result.contains(up))
				qTile.add(up);
			if(down.getBlocks().isEmpty() && !result.contains(down))
				qTile.add(down);
			if(left.getBlocks().isEmpty() && !result.contains(left))
				qTile.add(left);
			if(right.getBlocks().isEmpty() && !result.contains(right))
				qTile.add(right);
			
			qTile.remove();
			
		}
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		//  à compléter afin d'initialiser les critères 
		// et les cas. la méthode est appelée une seule fois
		
		//  le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un 
		// exemple, vous devez définir vos propres critères !
		
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode

		// on définit les critères
		CriterionPertinance crPertinance = new CriterionPertinance(ai);
		CriterionChaineReaction crReaction = new CriterionChaineReaction(ai);
		CriterionDanger crDanger = new CriterionDanger(ai);
		CriterionDestructibleWalls crWalls = new CriterionDestructibleWalls(ai);
		CriterionEnemies  crEnemies = new CriterionEnemies(ai);
		CriterionEnemyDirection crEnemyDirection = new CriterionEnemyDirection(ai);
		
		
		// on définit un premier cas pour les cases ou il y a des item visibles			
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(crDanger);
		criteria.add(crPertinance);
		criteria.add(crEnemies);
		AiUtilityCase case1 = new AiUtilityCase(caseName1,criteria);

		//definition du 2 ieme critere
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(crDanger);
		criteria.add(crWalls);
		AiUtilityCase case2 = new AiUtilityCase(caseName2,criteria);
		
		//definition du 3 ieme critere - pour mode attaque
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(crDanger);
		criteria.add(crReaction);
		criteria.add(crEnemyDirection);
		AiUtilityCase case3 = new AiUtilityCase(caseName3,criteria);

		// on met les cas dans la map prévue à cet effet
		// ceci permettra de les retrouver facilement plus tard,
		// en particulier dans la méthode identifyCase()
		cases.put(caseName1,case1);
		
		// on affecte les valeurs d'utilité pour les cas itemvisibles
		int utility = 10;
		AiUtilityCombination combi;

		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,true);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}

		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,true);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,false);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,false);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}

		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,true);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}

		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,true);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,false);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,false);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}

		////////////////////////////////////////////////////////
		//cas 2		////////////////////////////////////////////
		////////////////////////////////////////////////////////
		utility = 5;
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crWalls,0);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crWalls,1);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crWalls,2);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}

		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crWalls,0);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crWalls,1);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case2);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crWalls,2);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}

		utility = 5;
		////////////////////////////////////////////////////////
		//cas 3		////////////////////////////////////////////
		////////////////////////////////////////////////////////
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crReaction,false);
			combi.setCriterionValue(crEnemyDirection,false);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}

		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crReaction,true);
			combi.setCriterionValue(crEnemyDirection,false);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crReaction,false);
			combi.setCriterionValue(crEnemyDirection,true);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crReaction,true);
			combi.setCriterionValue(crEnemyDirection,true);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crReaction,false);
			combi.setCriterionValue(crEnemyDirection,false);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}

		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crReaction,true);
			combi.setCriterionValue(crEnemyDirection,false);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crReaction,false);
			combi.setCriterionValue(crEnemyDirection,true);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
		
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case3);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crReaction,true);
			combi.setCriterionValue(crEnemyDirection,true);

			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,0);
			// on incrémente l'utilité pour la combinaison suivante
		}
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		AiUtilityCase result ;
		
		//  à compléter pour identifier le cas associé
		// à la case passée en paramètre
		
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode

		CriterionPertinance crPertinance = new CriterionPertinance(ai);
		CriterionChaineReaction crReaction = new CriterionChaineReaction(ai);
		CriterionDanger crDanger = new CriterionDanger(ai);
		CriterionDestructibleWalls crWalls = new CriterionDestructibleWalls(ai);
		CriterionEnemies  crEnemies = new CriterionEnemies(ai);
		CriterionEnemyDirection crEnemyDirection = new CriterionEnemyDirection(ai);
		
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		if(ai.getModeHandler().getMode() == AiMode.COLLECTING){

			if(!tile.getItems().isEmpty()){
				criteria.add(crDanger);
				criteria.add(crPertinance);
				criteria.add(crEnemies);
				result = new AiUtilityCase(caseName1,criteria);
			}
			else{
				criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(crDanger);
				criteria.add(crWalls);
				result = new AiUtilityCase(caseName2,criteria);
			}
		}
		else{
			criteria = new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(crDanger);
			criteria.add(crReaction);
			criteria.add(crEnemyDirection);
			result = new AiUtilityCase(caseName3,criteria);
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
//		super.updateOutput();
		//  à redéfinir, si vous voulez afficher d'autres informations
	}
}
