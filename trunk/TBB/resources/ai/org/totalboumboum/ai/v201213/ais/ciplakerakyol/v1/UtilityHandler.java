package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion.CriterionConcurrence;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion.CriterionDuree;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion.CriterionItemPertinent;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion.CriterionMenace;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion.CriterionNmbMur;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion.CriterionPertinenceAdv;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class UtilityHandler extends AiUtilityHandler<CiplakErakyol>
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
	protected UtilityHandler(CiplakErakyol ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		verbose = false;

	}
	/** */
	private final String	ItemVisible			= "ItemVisible";
	/** */
	private final String	VoisinageMurDest	= "VoisinageMurDest";
	/** */
	private final String	casNullCol				= "casNullCol";
	/** */
	private final String	VoisinageAdversaire		= "VoisinageAdversaire";
	/** */
	private final String	AdverseireFatiguer		= "AdverseireFatiguer	";
	/** */
	private final String	casNullAtt				= "casNullAtt";
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
		
		result.add(ai.getZone().getOwnHero().getTile());
		// dont on veut calculer l'utilité
	
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		// on définit les critères
		
		new CriterionNmbMur(ai);
		new CriterionPertinenceAdv(ai);
		new CriterionDuree(ai);
		new CriterionMenace(ai);
		new CriterionItemPertinent(ai);
		new CriterionConcurrence(ai);
		
		// la méthode est appelée une seule fois

		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode


	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms du premier cas, utilisé dans {@link #initCases} */
	//private final String CASE_NAME1 = "CAS1";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	//private final String CASE_NAME2 = "CAS2";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	//private final String CASE_NAME3 = "CAS3";

	@Override
	protected void initCases() throws StopRequestException
	{	
		ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	
		CriterionNmbMur nbMur= new CriterionNmbMur(ai);
		CriterionPertinenceAdv advPerti= new CriterionPertinenceAdv(ai);
		CriterionDuree dure= new CriterionDuree(ai);
		CriterionMenace menace= new CriterionMenace(ai);
		CriterionItemPertinent itemPerti=new CriterionItemPertinent(ai);
		CriterionConcurrence concur=new CriterionConcurrence(ai);
		
		// on définit un premier cas utilisant les deux premiers critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add( itemPerti );
		criteria.add( dure );
		criteria.add( menace );
		criteria.add( concur);
		new AiUtilityCase(ai, ItemVisible, criteria );
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add( dure );
		criteria.add( menace );
		criteria.add( nbMur );
		new AiUtilityCase(ai, VoisinageMurDest, criteria );
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add( nbMur );
		new AiUtilityCase(ai, casNullCol, criteria );
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add( advPerti );
		criteria.add( menace );
		criteria.add( nbMur );
		criteria.add( dure );
		new AiUtilityCase(ai, VoisinageAdversaire, criteria );
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add( concur );
		criteria.add( menace );
		criteria.add( dure );
		new AiUtilityCase(ai, AdverseireFatiguer, criteria );
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add( nbMur );
		new AiUtilityCase( ai, casNullAtt, criteria );
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
		
		CriterionNmbMur nbMur= new CriterionNmbMur(ai);
		CriterionPertinenceAdv advPerti= new CriterionPertinenceAdv(ai);
		CriterionDuree dure= new CriterionDuree(ai);
		CriterionMenace menace= new CriterionMenace(ai);
		CriterionItemPertinent itemPerti=new CriterionItemPertinent(ai);
		CriterionConcurrence concur=new CriterionConcurrence(ai);
		// on commence avec le mode collecte
		{	
			mode = AiMode.COLLECTING;
			utility = 1;
			// le premier cas a un critère binaire et un entier [1,3],
			// donc il y a 6 combinaisons possibles
			// ici, pour l'exemple, on leur affecte des utilités
			// en respectant l'ordre des valeurs, mais bien sûr
			// ce n'est pas du tout obligatoire
			{	// on crée la combinaison (vide pour l'instant)
				combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue( itemPerti, true);
				combi.setCriterionValue( dure, 2 );
				combi.setCriterionValue( menace, false );
				combi.setCriterionValue( concur, false );
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue( itemPerti, true);
				combi.setCriterionValue( dure, 1);
				combi.setCriterionValue( menace, false );
				combi.setCriterionValue( concur, false );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
			combi.setCriterionValue( itemPerti, true);
			combi.setCriterionValue( dure, 0 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( concur, false );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
			combi.setCriterionValue( itemPerti, true);
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( concur, true );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
				combi.setCriterionValue( itemPerti, true);
				combi.setCriterionValue( dure, 1 );
				combi.setCriterionValue( menace, false );
				combi.setCriterionValue( concur, true );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisible));
			combi.setCriterionValue( itemPerti, true);
			combi.setCriterionValue( dure, 0);
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( concur, true );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			// le deuxième cas a un critère entier [1,3] 
			// et un critère chaine de caractères à 5 valeurs
			// possibles, donc ça fait 15 combinaisons au total.
			// la définition de l'utilité de ces combinaisons
			// se fait de la même façon que ci dessus
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combi.setCriterionValue( dure, 2 );
				combi.setCriterionValue( menace, false );
				combi.setCriterionValue( nbMur, 3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( nbMur, 2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
			combi.setCriterionValue( dure, 1 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( nbMur, 3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( nbMur, 1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
			combi.setCriterionValue( dure, 1 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue( nbMur, 2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			
			{	combi = new AiUtilityCombination(caseMap.get(casNullCol));
			combi.setCriterionValue( nbMur, 0);
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
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
			combi.setCriterionValue( advPerti, 1);
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue(nbMur, 3 );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
			combi.setCriterionValue( advPerti, 1);
			combi.setCriterionValue( dure, 1 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue(nbMur, 3 );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
			combi.setCriterionValue( advPerti, 1);
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue(nbMur, 2 );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
			combi.setCriterionValue( advPerti, 1);
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue(nbMur, 1 );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
			combi.setCriterionValue( advPerti, 1);
			combi.setCriterionValue( dure, 1 );
			combi.setCriterionValue( menace, false );
			combi.setCriterionValue(nbMur, 2 );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{	combi = new AiUtilityCombination(caseMap.get(AdverseireFatiguer));
			combi.setCriterionValue( concur, false);
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(AdverseireFatiguer));
			combi.setCriterionValue( concur, false);
			combi.setCriterionValue( dure, 1 );
			combi.setCriterionValue( menace, false );

				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(AdverseireFatiguer));
			combi.setCriterionValue( concur, false);
			combi.setCriterionValue( dure, 0 );
			combi.setCriterionValue( menace, false );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(AdverseireFatiguer));
			combi.setCriterionValue( concur, true);
			combi.setCriterionValue( dure, 2 );
			combi.setCriterionValue( menace, false );
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{	combi = new AiUtilityCombination(caseMap.get(casNullCol));
			combi.setCriterionValue( nbMur, 0);
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
