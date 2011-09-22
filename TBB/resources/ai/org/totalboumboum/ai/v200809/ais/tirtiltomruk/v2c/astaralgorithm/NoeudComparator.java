package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.astaralgorithm;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
public class NoeudComparator implements Comparator<Noeud> {

	/** noeud cible:reference qu'on va utiliser en comparant les noeuds */
	private Noeud goal;
	ArtificialIntelligence ai;
	
	/**
	 * Constructeur.
	 * 
	 * @param goal
	 *            noeud qu'on prend comme reference en comparant les noeuds
	 * @throws StopRequestException 
	 */
	public NoeudComparator(Noeud goal, ArtificialIntelligence ai) throws StopRequestException
	{	ai.checkInterruption();
		this.ai = ai;		
		this.goal=goal;
	}

	/**
	 * Retourne une valeur servant à comparer les noeuds.
	 * On fait la somme de l'heuristique et le cout de chaque noeud.
	 * On compare ces valeurs et on tourne une valeur négative si le premier a un plus petit valeur,
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
	//on va utiliser pour l'algorithme de A_étoile
	
	int n_1,n_2;
	try {
		n_1 = noeud1.getHeuristic(goal)+  noeud1.getCout();
		 n_2=noeud2.getHeuristic(goal)+  noeud2.getCout();
	} catch (StopRequestException e) {
		//  Auto-generated catch block
		n_1 = 0;
		n_2 = 0;
	}
	
	
	
		if(n_1<n_2)
			resultat=-1;
		
		else 
			resultat=1;
		
		return resultat;
	}

}
