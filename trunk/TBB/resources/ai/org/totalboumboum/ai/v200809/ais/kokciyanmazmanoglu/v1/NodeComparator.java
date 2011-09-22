package org.totalboumboum.ai.v200809.ais.kokciyanmazmanoglu.v1;

import java.util.Comparator;

/**
*
* @author Nadin Kökciyan
* @author Hikmet Mazmanoğlu
*
*/
public class NodeComparator implements Comparator<Node>
{
	private Node start;
	private Node end;
	
	public NodeComparator(Node startNode, Node endNode){
		this.start = startNode;
		this.end = endNode;
	}
	
	public int compare(Node n1, Node n2)
	{	int res = 0;
		
	
		double val1 = n1.getCost()+n1.getH(start, end);
		double val2 = n2.getCost()+n2.getH(start, end);
		if(val1<val2)res = -1;
		else if(val1>val2)res = 1;
		else{
			res = n1.getName().compareTo(n2.getName());
			
		}
		return res;
	}
}
