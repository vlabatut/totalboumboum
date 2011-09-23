package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
public class Bombe 
{
	private AiZone zone; // La zone de jeu de cette instance
	private final int BOMB = -200; //constante pour les bombes
	private final int BOMB_PORTE = -150;  //constantes pour leurs portés
	AiOutput outPut; //objet pour affichage
	Color couleur = null; //objet pour affichage
	
	
	//Methode qui donne les constant plus les valeurs de diffarence entre le hero et le bombe aux cases dans lequels il se trouve des bombes
	public void fillMatrix(int [][] matrix) throws StopRequestException
	{
		
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) 
		{
			AiBomb bomb = iteratorBombs.next();
			matrix[bomb.getLine()][bomb.getCol()] = BOMB + distance(bomb.getLine(), bomb.getCol(), zone.getOwnHero().getLine(),zone.getOwnHero().getCol())*5;
			Collection<AiTile> porteBombe = bomb.getBlast();
			Iterator<AiTile> iteratorScope = porteBombe.iterator();
			outPut.setTileColor(bomb.getLine(), bomb.getCol(),couleur.darker());
			while (iteratorScope.hasNext()) 
			{
				matrix[iteratorScope.next().getLine()][iteratorScope.next().getCol()] = BOMB_PORTE;
				outPut.setTileColor(bomb.getLine(), bomb.getCol(),couleur.darker()); //
			}
			
		}
	}
	
	
	//La methode qui calcule la distance entre une bombe et le hero
	public int distance(int i, int j, int a, int b)
	{
		int resultat = zone.getTileDistance(i, j, a, b);
		return resultat;
	}

}
