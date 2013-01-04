package org.totalboumboum.ai.v201213.ais.balyerguven.v1;


import java.util.Set;
import java.util.TreeSet;


import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion.Adversaire;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion.Menace;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion.MurVoiAdv;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion.NbrMurDest;
import org.totalboumboum.ai.v201213.ais.balyerguven.v1.criterion.Pertinence;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 

 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class UtilityHandler extends AiUtilityHandler<BalyerGuven>
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
	protected UtilityHandler(BalyerGuven ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		
		
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
	Set<AiTile> result = new TreeSet<AiTile>();
	
	
	return result;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	@Override
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();
	   
	
		// le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un 
		// exemple, vous devez définir vos propres critères !
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode

		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new Duree(ai);
		new Menace(ai);
		new Adversaire(ai);
		new Pertinence(ai);
		new NbrMurDest(ai);
		new MurVoiAdv(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas, utilisé dans {@link #initCases} */
	private final String itemVisible = "itemVisible";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	private final String murDest = "murDest";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	private final String casVoiAdv = "casVoiAdv";
	

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	
		// on définit un premier cas utilisant les deux premiers critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(Pertinence.NAME));
		criteria.add(criterionMap.get(Adversaire.NAME));
		new AiUtilityCase(ai,itemVisible,criteria);
			
		// on définit un deuxième cas utilisant les deux derniers critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Pertinence.NAME));
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(NbrMurDest.NAME));
		new AiUtilityCase(ai,murDest,criteria);

		// on définit un troisième cas utilisant seulement le dernier critère
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Menace.NAME));
		criteria.add(criterionMap.get(Duree.NAME));
		criteria.add(criterionMap.get(MurVoiAdv.NAME));
		new AiUtilityCase(ai,casVoiAdv,criteria);
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		
	
		
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
				combi = new AiUtilityCombination(caseMap.get(itemVisible));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
			combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(itemVisible));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((Adversaire)criterionMap.get(Adversaire.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			
			// le deuxième cas a un critère entier [1,3] 
			// et un critère chaine de caractères à 5 valeurs
			// possibles, donc ça fait 15 combinaisons au total.
			// la définition de l'utilité de ces combinaisons
			// se fait de la même façon que ci dessus
			{	// on crée la combinaison (vide pour l'instant)
				combi = new AiUtilityCombination(caseMap.get(murDest));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
				combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(murDest));
			combi.setCriterionValue((Pertinence)criterionMap.get(Pertinence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrMurDest)criterionMap.get(NbrMurDest.NAME),Boolean.FALSE);
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
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
				combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
				combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
				combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.TRUE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.TRUE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(casVoiAdv));
			combi.setCriterionValue((Duree)criterionMap.get(Duree.NAME),Boolean.FALSE);
			combi.setCriterionValue((MurVoiAdv)criterionMap.get(MurVoiAdv.NAME),Boolean.FALSE);
			combi.setCriterionValue((Menace)criterionMap.get(Menace.NAME),Boolean.FALSE);
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
	
	}
}
