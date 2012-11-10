package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v0;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v0.criterion.CriterionFirst;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v0.criterion.CriterionSecond;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v0.criterion.CriterionThird;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class UtilityHandler extends AiUtilityHandler<AlcarKayaYildirim>
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
	protected UtilityHandler(AlcarKayaYildirim ai) throws StopRequestException
	{	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
	
		// TODO à compléter
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à surcharger si nécessaire, pour réinitialiser certaines
		// structures de données à chaque itération
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		// TODO à compléter afin de sélectionner les cases
		// dont on veut calculer l'utilité
	
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter afin d'initialiser les critères 
		// la méthode est appelée une seule fois
		
		// le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un 
		// exemple, vous devez définir vos propres critères !
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode

		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new CriterionFirst(ai);
		new CriterionSecond(ai);
		new CriterionThird(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME1 = "CAS1";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME2 = "CAS2";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME3 = "CAS3";

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	
		// on définit un premier cas utilisant les deux premiers critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionFirst.NAME));
		criteria.add(criterionMap.get(CriterionSecond.NAME));
		new AiUtilityCase(ai,CASE_NAME1,criteria);
			
		// on définit un deuxième cas utilisant les deux derniers critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionSecond.NAME));
		criteria.add(criterionMap.get(CriterionThird.NAME));
		new AiUtilityCase(ai,CASE_NAME2,criteria);

		// on définit un troisième cas utilisant seulement le dernier critère
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionThird.NAME));
		new AiUtilityCase(ai,CASE_NAME3,criteria);
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		
		// TODO à compléter pour identifier le cas associé
		// à la case passée en paramètre
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException
	{	ai.checkInterruption();
		
		// on affecte les valeurs d'utilité
		int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		// on commence avec le mode collecte
		{	mode = AiMode.COLLECTING;
			utility = 1;
			// le premier cas a un critère binaire et un entier [1,3],
			// donc il y a 6 combinaisons possibles
			// ici, pour l'exemple, on leur affecte des utilités
			// en respectant l'ordre des valeurs, mais bien sûr
			// ce n'est pas du tout obligatoire
			{	// on crée la combinaison (vide pour l'instant)
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue((CriterionFirst)criterionMap.get(CriterionFirst.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),1);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				combi.setCriterionValue((CriterionFirst)criterionMap.get(CriterionFirst.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				combi.setCriterionValue((CriterionFirst)criterionMap.get(CriterionFirst.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				combi.setCriterionValue((CriterionFirst)criterionMap.get(CriterionFirst.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				combi.setCriterionValue((CriterionFirst)criterionMap.get(CriterionFirst.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				combi.setCriterionValue((CriterionFirst)criterionMap.get(CriterionFirst.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			// le deuxième cas a un critère entier [1,3] 
			// et un critère chaine de caractères à 5 valeurs
			// possibles, donc ça fait 15 combinaisons au total.
			// la définition de l'utilité de ces combinaisons
			// se fait de la même façon que ci dessus
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue((CriterionSecond)criterionMap.get(CriterionSecond.NAME),1);
				combi.setCriterionValue((CriterionThird)criterionMap.get(CriterionThird.NAME),CriterionThird.VALUE1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			// ......
			// etc. pour les 14 autres combinaisons (qui doivent toutes être définies
			// afin de leur associer une valeur d'utilité à chacune)
		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 1;
			// pour simplifier, on ne met qu'un seul cas : le troisième
			// il n'a qu'un seul critère, défini sur un domaine de 5 valeurs
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue((CriterionThird)criterionMap.get(CriterionThird.NAME),CriterionThird.VALUE1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue((CriterionThird)criterionMap.get(CriterionThird.NAME),CriterionThird.VALUE2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue((CriterionThird)criterionMap.get(CriterionThird.NAME),CriterionThird.VALUE3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue((CriterionThird)criterionMap.get(CriterionThird.NAME),CriterionThird.VALUE4);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue((CriterionThird)criterionMap.get(CriterionThird.NAME),CriterionThird.VALUE5);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
		}
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
