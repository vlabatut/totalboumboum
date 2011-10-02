package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_1;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * Cette class est pour creer un noeud qui contient les valeur
 * <br><br>G : Cost <br>H : Heuristic <br>F = G + H 
 * @author DorukKüpelioğlu
 *
 */
@SuppressWarnings("deprecation")
public class Node {
	
	private AiTile tile;//bu node un çekirdeği // oluşmasına neden olan Tile
	private AiTile end; //bi anlamı yok sadece bir yerde kullanıldı
	private double[][] areaMatrix;
	private Node parent=null;// hangi node bu nodun anası 
	private double Gvalue;//cost
	private double Hvalue;//heuristic
	private double Fvalue;//cost + heuristic

	/**
	 * Constucteur a faire les initialisation
	 * @param tile la case base du noeud
	 * @param end elle va utiliser pour la calcule d'heuristique.
	 * @param parent le noeud parent de ce noeud
	 * @param areaMatrix cost hesabı yaparken incelenen tile da ne olduğunu öğrenmek için
	 */
	public Node(AiTile tile,AiTile end,Node parent,double[][] areaMatrix)
	{
		this.areaMatrix=areaMatrix;
		this.tile=tile;
		this.end=end;
		this.parent=parent;
		Gvalue=0;
		putValues();
	}
	
	/**
	 * Cette fonction va trouver les valeurs des G:cost , H:heuristique, F:la totale
	 */
	public void putValues()
	{
		this.Hvalue=Math.sqrt(Math.pow(tile.getCol()-end.getCol(), 2)+Math.pow(tile.getLine()-end.getLine(), 2));
		Node n= this.parent;
		if(n!=null)
			Gvalue=n.Gvalue;
		if(areaMatrix[tile.getLine()][tile.getCol()]<State.MALUS)//free ise sadece yol mesafesi
			Gvalue+=1;
		else if(areaMatrix[tile.getLine()][tile.getCol()]==State.MALUS)
			Gvalue+=10;
		else if(areaMatrix[tile.getLine()][tile.getCol()]<State.BONUSDANGERR)//astar ne permet pas de passer dans une chemin EXPLODED
			Gvalue+=20;
		else
			Gvalue+=15;
		
		List<AiTile> neighbors=tile.getNeighbors();
		for(int index=0;index<neighbors.size();index++)
		{
			double state=areaMatrix[neighbors.get(index).getLine()][neighbors.get(index).getCol()];
			if((n==null||n.getTile()!=neighbors.get(index))&&state>State.MALUS)
			{
				if(state<State.DESTRUCTIBLE)
					Gvalue++;
				else
					Gvalue+=5;
			}
		}
		this.Fvalue=Hvalue+Gvalue;
	}
	
	/**
	 * @return la valeur G:cost du noeud correspondant
	 */
	public double getG()
	{
		return Gvalue;
	}
	
	/**
	 * @return la valeur H:heuristique du noeud correspondant
	 */
	public double getH()
	{
		return Hvalue;
	}
	
	/**
	 * @return la valeur F du noeud correspondant
	 */
	public double getF()
	{
		return Fvalue;
	}
	
	/**
	 * @return le noeud parent du noeud correspondant
	 */
	public Node getParent()
	{
		return parent;
	}
	
	/**
	 * @return la case base du ce noeud
	 */
	public AiTile getTile()
	{
		return tile;
	}
}
