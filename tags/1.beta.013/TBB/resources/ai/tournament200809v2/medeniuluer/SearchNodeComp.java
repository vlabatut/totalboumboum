package tournament200809v2.medeniuluer;

import java.util.Comparator;

public class SearchNodeComp implements Comparator<SearchNode> {

	/** noeud cible:reference qu'on va utiliser en comparant les noeuds */
	private SearchNode goal;

	/**
	 * Constructeur.
	 * 
	 * @param goal
	 *            noeud qu'on prend comme reference en comparant les noeuds
	 */
	public SearchNodeComp(SearchNode goal)
	{
		
		this.goal=goal;
	}

	/**
	 * Retourne une valeur servant � comparer les noeuds.
	 * On fait la somme de l'heuristique et le cout de chaque noeud.
	 * On compare ces valeurs et on tourne une valeur n�gative si le premier a un plus petit valeur,
	 * une valeur positive si le premier a une plus grande valeur
	 * @param noeud1 
	 *            noeud qu'on va comparer
	 * @param noeud2
	 * 			  noeud qu'on va comparer
	 * @return valeur positive ou negative selon la comparaison          
	 */
	@Override
	public int compare(SearchNode node1, SearchNode node2) {
		int resultat;
	//somme de l'heuristique et du cout pour chaque noeud
	//on va utiliser pour l'algorithme de A_�toile
	int n_1=node1.getHeuristic(goal)+  node1.getCout();
	int n_2=node2.getHeuristic(goal)+  node2.getCout();
	
	
		if(n_1<n_2)
			resultat=-1;
		
		else 
			resultat=1;
		
		return resultat;
	}

}
