package org.totalboumboum.ai.v200809.ais.demiragsagar.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class NoeudComparator implements Comparator<Node> {

	@Override
	public int compare(Node noeud1, Node noeud2) {
		int resultat;
		double n_1=0;
		try {
			n_1 = noeud1.getHeuristic();
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		double n_2=0;
		try {
			n_2 = noeud2.getHeuristic();
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}

		if (n_1 < n_2)
			resultat = -1;

		else
			resultat = 1;

		return resultat;
	}

}
