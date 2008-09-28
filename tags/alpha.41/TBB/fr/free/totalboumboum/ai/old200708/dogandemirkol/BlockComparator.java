package fr.free.totalboumboum.ai.old200708.dogandemirkol;

import java.util.Comparator;

public class BlockComparator implements Comparator<Block>
{

	//on compare deux block par rapport a l'addition des cout et des heuristique du block
	public int compare(Block b1, Block b2) {
		return ((b1.getCost() + b1.getHeuristic()) - (b2.getCost() + b2.getHeuristic()));
	}

}
