package org.totalboumboum.ai.v200809.ais.demiragsagar.v2;

import java.util.Comparator;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
public class NoeudComparator implements Comparator<Node> {

	public int compare(Node noeud1, Node noeud2) {
		int resultat;
		double n_1 = noeud1.getHeuristic();
		double n_2 = noeud2.getHeuristic();

		if (n_1 < n_2)
			resultat = -1;

		else
			resultat = 1;

		return resultat;
	}

}
