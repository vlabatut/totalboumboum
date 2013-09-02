package org.totalboumboum.ai.v200809.ais.medeniuluer.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
*
* @author Ekin Medeni
* @author Pınar Uluer
*
*/
@SuppressWarnings("deprecation")
public class SearchNodeComp implements Comparator<SearchNode> {

	/** noeud cible:reference qu'on va utiliser en comparant les noeuds */
	private SearchNode goal;

	/**
	 * Constructeur.
	 * 
	 * @param goal
	 *            noeud qu'on prend comme reference en comparant les noeuds
	 * @param ai 
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public SearchNodeComp(SearchNode goal, ArtificialIntelligence ai) throws StopRequestException
	{
		ai.checkInterruption();
		this.goal=goal;
	}

	/**
	 * Retourne une valeur servant à comparer les noeuds.
	 * On fait la somme de l'heuristique et le cout de chaque noeud.
	 * On compare ces valeurs et on tourne une valeur négative si le premier a un plus petit valeur,
	 * une valeur positive si le premier a une plus grande valeur
	 * @param node1 
	 *            noeud qu'on va comparer
	 * @param node2
	 * 			  noeud qu'on va comparer
	 * @return valeur positive ou negative selon la comparaison          
	 */
	@Override
	public int compare(SearchNode node1, SearchNode node2) {
		int resultat;
	//somme de l'heuristique et du cout pour chaque noeud
	//on va utiliser pour l'algorithme de A_étoile
	int n_1=0;
	try {
		n_1 = node1.getHeuristic(goal)+  node1.getCout();
	} catch (StopRequestException e) {
		// 
		//e.printStackTrace();
	}
	int n_2=0;
	try {
		n_2 = node2.getHeuristic(goal)+  node2.getCout();
	} catch (StopRequestException e) {
		// 
		//e.printStackTrace();
	}
	
	
		if(n_1<n_2)
			resultat=-1;
		
		else 
			resultat=1;
		
		return resultat;
	}

}
