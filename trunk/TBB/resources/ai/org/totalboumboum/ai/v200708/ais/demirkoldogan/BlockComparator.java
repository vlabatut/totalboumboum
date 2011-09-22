package org.totalboumboum.ai.v200708.ais.demirkoldogan;

import java.util.Comparator;

/**
 * 
 * @author Turkalp Göker Demirkol
 * @author Emre Doğan
 *
 */
public class BlockComparator implements Comparator<Block>
{

	//on compare deux block par rapport a l'addition des cout et des heuristique du block
	public int compare(Block b1, Block b2) {
		return ((b1.getCost() + b1.getHeuristic()) - (b2.getCost() + b2.getHeuristic()));
	}

}
