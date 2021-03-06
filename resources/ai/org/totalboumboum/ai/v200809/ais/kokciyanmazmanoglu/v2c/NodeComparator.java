package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
*
* @author Nadin Kökciyan
* @author Hikmet Mazmanoğlu
*
*/
@SuppressWarnings("deprecation")
public class NodeComparator implements Comparator<Node>
{
	/** */
	private Node start;
	/** */
	private Node end;
	/** */
	ArtificialIntelligence ai;
	
	/**
	 * 
	 * @param startNode
	 * 		Description manquante !
	 * @param endNode
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public NodeComparator(Node startNode, Node endNode, ArtificialIntelligence ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		this.start = startNode;
		this.end = endNode;
	}
	
	@Override
	public int compare(Node n1, Node n2)
	{	int res = 0;
		
	
		double val1 = 0;
		try {
			val1 = n1.getCost()+n1.getH(start, end);
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		double val2 = 0;
		try {
			val2 = n2.getCost()+n2.getH(start, end);
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		if(val1<val2)res = -1;
		else if(val1>val2)res = 1;
		else{
			try {
				res = n1.getName().compareTo(n2.getName());
			} catch (StopRequestException e) {
				// 
				//e.printStackTrace();
				throw new RuntimeException();
			}
			
		}
		return res;
	}
}
