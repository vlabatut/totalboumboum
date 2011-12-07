package org.totalboumboum.ai.v201112.ais.demirsazan.v0;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demirsazan.v0.criterion.CriterionFirst;
import org.totalboumboum.ai.v201112.ais.demirsazan.v0.criterion.CriterionSecond;
import org.totalboumboum.ai.v201112.ais.demirsazan.v0.criterion.CriterionThird;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Serdil Demir
 * @author Gökhan Sazan
 */
public class UtilityHandler extends AiUtilityHandler<DemirSazan>
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
	protected UtilityHandler(DemirSazan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
	
		// TODO à compléter
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String caseName1 = "CAS1";
	private final String caseName2 = "CAS2";
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	Set<AiTile> result = new TreeSet<AiTile>();
		
		// TODO à compléter afin de sélectionner les cases
		// dont on veut calculer l'utilité
	
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode
		
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		// TODO à compléter afin d'initialiser les critères 
		// et les cas. la méthode est appelée une seule fois
		
		// TODO le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un 
		// exemple, vous devez définir vos propres critères !
		
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode

		// on définit les critères
		CriterionFirst criterionFirst = new CriterionFirst(ai);
		CriterionSecond criterionSecond = new CriterionSecond(ai);
		CriterionThird criterionThird = new CriterionThird(ai);
		
		// on définit un premier cas utilisant les deux premiers critères
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterionFirst);
		criteria.add(criterionSecond);
		AiUtilityCase case1 = new AiUtilityCase(caseName1,criteria);
			
		// on définit un sescond cas utilisant les deux derniers critères
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterionSecond);
		criteria.add(criterionThird);
		AiUtilityCase case2 = new AiUtilityCase(caseName2,criteria);

		// on met les cas dans la map prévue à cet effet
		// ceci permettra de les retrouver facilement plus tard,
		// en particulier dans la méthode identifyCase()
		cases.put(caseName1,case1);
		cases.put(caseName2,case2);
		
		// on affecte les valeurs d'utilité
		int utility = 1;
		AiUtilityCombination combi;
		// le premier cas a un critère binaire et un entier [1,3],
		// donc il y a 6 combinaisons possibles
		// ici, pour l'exemple, on leur affecte des utilités
		// en respectant l'ordre des valeurs, mais bien sûr
		// ce n'est pas du tout obligatoire
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(case1);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(criterionFirst,false);
			combi.setCriterionValue(criterionSecond,1);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionFirst,false);
			combi.setCriterionValue(criterionSecond,2);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionFirst,false);
			combi.setCriterionValue(criterionSecond,3);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionFirst,true);
			combi.setCriterionValue(criterionSecond,1);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionFirst,true);
			combi.setCriterionValue(criterionSecond,2);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criterionFirst,true);
			combi.setCriterionValue(criterionSecond,3);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		// le deuxième cas a un critère entier [1,3] 
		// et un critère chaine de caractères à 5 valeurs
		// possibles, donc ça faiut 15 combinaisons au total
		// la définition de l'utilité de ces combinaisons
		// se fait de la même façon que ci dessus
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criterionSecond,1);
			combi.setCriterionValue(criterionThird,CriterionThird.VALUE1);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		// etc.
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	AiUtilityCase result = null;
		
		// TODO à compléter pour identifier le cas associé
		// à la case passée en paramètre
		
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode
		
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
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
