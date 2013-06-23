package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

public class Bonus 
{
	private AiZone zone; // La zone de jeu de cette instance
	private final int BONUS = 50;//La constant qui signifie les bonus
	
	//les objet pour l'affichage
	AiOutput outPut;
	Color couleur = null;
	
	//la methode qui ajoute une constante et l'effet des autre item sur un bonus aux case dans laquelles il se trouve des bonus
	public void fillMatrix(int [][] matrix) throws StopRequestException
	{
		
		Collection<AiItem> bonus = zone.getItems();
		Iterator<AiItem> iteratorBonus = bonus.iterator();
		while (iteratorBonus.hasNext()) 
		{
			AiItem bonuses = iteratorBonus.next();
			matrix[bonuses.getLine()][bonuses.getCol()] = BONUS;
			outPut.setTileText(bonuses.getLine(), bonuses.getCol(),Integer.toString(matrix[bonuses.getLine()][bonuses.getCol()]));		
		}
	}
/*	
	public int distance(int i, int j, int a, int b)
	{
		resultat = zone.getTileDistance(i, j, a, b);
		return resultat;
	}
*/	
	
	

}
