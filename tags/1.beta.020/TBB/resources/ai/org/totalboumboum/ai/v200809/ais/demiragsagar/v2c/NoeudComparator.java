package org.totalboumboum.ai.v200809.ais.demiragsagar.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Dogus Burcu Demirag
 * @author Zeynep Sagar
 *
 */
public class NoeudComparator implements Comparator<Node> {

	public int compare(Node noeud1, Node noeud2) {
		int resultat;
		double n_1=0;
		try {
			n_1 = noeud1.getHeuristic();
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		double n_2=0;
		try {
			n_2 = noeud2.getHeuristic();
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}

		if (n_1 < n_2)
			resultat = -1;

		else
			resultat = 1;

		return resultat;
	}

}
