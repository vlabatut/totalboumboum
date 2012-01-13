package org.totalboumboum.ai.v201112.ais.unluyildirim.v1;
/*
 * J'ai ajouté les criteres de mode collecte , mais quand on essai de l'exécuter il me donne des erreurs.
 * Il ne marche pas exactement.
 * Il marche seulement avec un case que l'on défini si-dessous.
 * 
 * 
 * 
 */
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion.CriterionAccessibleBonus;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion.CriterionDistanceHero;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion.CriterionMenace;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion.CriterionTempsCollecte;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion.CriterionTypeBonus;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class UtilityHandler extends AiUtilityHandler<UnluYildirim>
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
	protected UtilityHandler(UnluYildirim ai) throws StopRequestException
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
	@SuppressWarnings("unused")
	private final String caseName2 = "CAS2";
	
	@SuppressWarnings("unused")
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	Set<AiTile> result = new TreeSet<AiTile>();
		
		//  à compléter afin de sélectionner les cases
		// dont on veut calculer l'utilité
	    AiZone zone=ai.getZone();
	    
		// cf. la java doc dans AiUtilityHandler pour une description de la méthode
		
		return result;
	}

	@SuppressWarnings("unused")
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
		CriterionTempsCollecte criteriontempscollecte = new CriterionTempsCollecte(ai);
		CriterionTypeBonus criteriontypebonus = new CriterionTypeBonus(ai);
		CriterionMenace criterionmenace = new CriterionMenace(ai);
		CriterionAccessibleBonus criterionaccessible = new CriterionAccessibleBonus(ai);
		CriterionDistanceHero criterionherodistance = new CriterionDistanceHero(ai);
		
		
		
		
		// on définit un premier cas utilisant les 4 premiers critères
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criteriontempscollecte);
		criteria.add(criteriontypebonus);
		criteria.add(criterionmenace);
		criteria.add(criterionaccessible);
		AiUtilityCase case1 = new AiUtilityCase(caseName1,criteria);
			
		/*
		// on définit un second cas utilisant les 3 critères
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criteriontempscollecte);
		criteria.add(criterionmenace);
		criteria.add(criterionherodistance);
		AiUtilityCase case2 = new AiUtilityCase(caseName2,criteria);
		
		*/
		// on met les cas dans la map prévue à cet effet
		// ceci permettra de les retrouver facilement plus tard,
		// en particulier dans la méthode identifyCase()
		
		cases.put(caseName1,case1);
	    //cases.put(caseName2,case2);
		

		
		
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
			combi.setCriterionValue(criteriontempscollecte,1);
			combi.setCriterionValue(criteriontypebonus,1);
			combi.setCriterionValue(criterionmenace,1);
			combi.setCriterionValue(criterionaccessible,true);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}/*
		{	combi = new AiUtilityCombination(case1);
		
		combi.setCriterionValue(criteriontempscollecte,1);
		combi.setCriterionValue(criteriontypebonus,1);
		combi.setCriterionValue(criterionmenace,1);
		combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}/*
		{	combi = new AiUtilityCombination(case1);
		combi.setCriterionValue(criteriontempscollecte,1);
		combi.setCriterionValue(criteriontypebonus,1);
		combi.setCriterionValue(criterionmenace,0);
		combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
	
		combi.setCriterionValue(criteriontempscollecte,1);
		combi.setCriterionValue(criteriontypebonus,1);
		combi.setCriterionValue(criterionmenace,0);
		combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}/*
		{	combi = new AiUtilityCombination(case1);
		combi.setCriterionValue(criteriontempscollecte,1);
		combi.setCriterionValue(criteriontypebonus,0);
		combi.setCriterionValue(criterionmenace,1);
		combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case1);
		combi.setCriterionValue(criteriontempscollecte,1);
		combi.setCriterionValue(criteriontypebonus,0);
		combi.setCriterionValue(criterionmenace,1);
		combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,1);
			combi.setCriterionValue(criteriontypebonus,0);
			combi.setCriterionValue(criterionmenace,0);
			combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,1);
			combi.setCriterionValue(criteriontypebonus,0);
			combi.setCriterionValue(criterionmenace,0);
			combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,1);
			combi.setCriterionValue(criterionmenace,1);
			combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,1);
			combi.setCriterionValue(criterionmenace,1);
			combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,1);
			combi.setCriterionValue(criterionmenace,0);
			combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,1);
			combi.setCriterionValue(criterionmenace,0);
			combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,0);
			combi.setCriterionValue(criterionmenace,1);
			combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,0);
			combi.setCriterionValue(criterionmenace,1);
			combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,0);
			combi.setCriterionValue(criterionmenace,0);
			combi.setCriterionValue(criterionaccessible,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(criteriontempscollecte,0);
			combi.setCriterionValue(criteriontypebonus,0);
			combi.setCriterionValue(criterionmenace,0);
			combi.setCriterionValue(criterionaccessible,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		// le deuxième cas a un critère entier [1,3] 
		// et un critère chaine de caractères à 5 valeurs
		// possibles, donc ça faiut 15 combinaisons au total
		// la définition de l'utilité de ces combinaisons
		// se fait de la même façon que ci dessus
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(criteriontempscollecte,1);
			combi.setCriterionValue(criterionmenace,1);
			combi.setCriterionValue(criterionherodistance,true);
			referenceUtilities.put(combi,utility);
			utility++;

		}
		{	combi = new AiUtilityCombination(case2);
		combi.setCriterionValue(criteriontempscollecte,1);
		combi.setCriterionValue(criterionmenace,0);
		combi.setCriterionValue(criterionherodistance,true);
		referenceUtilities.put(combi,utility);
		utility++; }
		{	combi = new AiUtilityCombination(case2);
		combi.setCriterionValue(criteriontempscollecte,0);
		combi.setCriterionValue(criterionmenace,1);
		combi.setCriterionValue(criterionherodistance,true);
		referenceUtilities.put(combi,utility);
		utility++;

	}
		{	combi = new AiUtilityCombination(case2);
		combi.setCriterionValue(criteriontempscollecte,0);
		combi.setCriterionValue(criterionmenace,0);
		combi.setCriterionValue(criterionherodistance,true);
		referenceUtilities.put(combi,utility);
		utility++;

	}*/
		// etc.
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	AiUtilityCase result = null;
		
		//  à compléter pour identifier le cas associé
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
		//super.updateOutput();
		//  à redéfinir, si vous voulez afficher d'autres informations
	}
}
