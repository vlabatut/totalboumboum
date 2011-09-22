package org.totalboumboum.ai.v200809.ais.akpolatsener.v1;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * Compare deux XTiles en fonction de leur heuristique et de leur nombre de visites.
 * 
 * @author Cem Akpolat
 * @author Emre Şener
 *
 */
public class XAStarComparator implements Comparator<XTile> 
{
	public int compare(XTile xt1, XTile xt2) 
	{
		int result = 0;

		try {
			if (xt1.getHeuristic() > xt2.getHeuristic())
				result = 1;
			else if (xt1.getHeuristic() < xt2.getHeuristic())
				result = -1;
			else {
				if (xt1.visits > xt2.visits)
					result = 1;
				else if (xt1.visits <= xt2.visits)
					result = -1;
			}
		} catch (StopRequestException e) {
			e.printStackTrace();
		}

		return result;
	}
}
