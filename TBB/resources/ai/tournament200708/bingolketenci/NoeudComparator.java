package tournament200708.bingolketenci;

import java.util.Comparator;

public class NoeudComparator implements Comparator<Noeud> {

	/** noeud cible:reference qu'on va utiliser en comparant les noeuds */
	private Noeud goal;

	/**
	 * Constructeur.
	 * 
	 * @param goal
	 *            noeud qu'on prend comme reference en comparant les noeuds
	 */
	public NoeudComparator(Noeud goal)
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
	public int compare(Noeud noeud1, Noeud noeud2) {
		int resultat;
	//somme de l'heuristique et du cout pour chaque noeud
	//on va utiliser pour l'algorithme de A_�toile
	int n_1=noeud1.getHeuristic(goal)+  noeud1.getCout();
	int n_2=noeud2.getHeuristic(goal)+  noeud2.getCout();
	
	
		if(n_1<n_2)
			resultat=-1;
		
		else 
			resultat=1;
		
		return resultat;
	}

}
