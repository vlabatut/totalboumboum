package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2c;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * Cette class est pour creer un noeud qui contient les valeur
 * <br><br>G : Cost <br>H : Heuristic <br>F = G + H 
 *
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
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
	ArtificialIntelligence ai;
	/**
	 * Constucteur a faire les initialisation
	 * @param tile la case base du noeud
	 * @param end elle va utiliser pour la calcule d'heuristique.
	 * @param parent le noeud parent de ce noeud
	 * @param areaMatrix cost hesabı yaparken incelenen tile da ne olduğunu öğrenmek için
	 * @throws StopRequestException 
	 */
	public Node(AiTile tile,AiTile end,Node parent,double[][] areaMatrix, ArtificialIntelligence ai) throws StopRequestException
	{	ai.checkInterruption();
		this.ai = ai;
		this.areaMatrix=areaMatrix;
		this.tile=tile;
		this.end=end;
		this.parent=parent;
		Gvalue=0;
		putValues();
	}
	
	/**
	 * Cette fonction va trouver les valeurs des G:cost , H:heuristique, F:la totale
	 * @throws StopRequestException 
	 */
	public void putValues() throws StopRequestException
	{	ai.checkInterruption();
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
		{	ai.checkInterruption();
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
	 * @throws StopRequestException 
	 */
	public double getG() throws StopRequestException
	{	ai.checkInterruption();
		return Gvalue;
	}
	
	/**
	 * @return la valeur H:heuristique du noeud correspondant
	 * @throws StopRequestException 
	 */
	public double getH() throws StopRequestException
	{	ai.checkInterruption();
		return Hvalue;
	}
	
	/**
	 * @return la valeur F du noeud correspondant
	 * @throws StopRequestException 
	 */
	public double getF() throws StopRequestException
	{	ai.checkInterruption();
		return Fvalue;
	}
	
	/**
	 * @return le noeud parent du noeud correspondant
	 * @throws StopRequestException 
	 */
	public Node getParent() throws StopRequestException
	{	ai.checkInterruption();
		return parent;
	}
	
	/**
	 * @return la case base du ce noeud
	 * @throws StopRequestException 
	 */
	public AiTile getTile() throws StopRequestException
	{	ai.checkInterruption();
		return tile;
	}
}
