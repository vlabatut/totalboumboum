package tournament200809old.akpolatsener;

import java.util.Comparator;

import fr.free.totalboumboum.ai.adapter200809.StopRequestException;


/**
 * Compare deux XTiles en fonction de leur heuristique et de leur nombre de visites.
 * @author SenerAkpolat
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
