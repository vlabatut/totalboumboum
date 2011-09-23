package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;

import java.awt.Color;
import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
public class Matrix 
{
	// les matrices
	private int[][] collectionMatrix; // Matrice en mode collection 
	//private int[][] attaqueMatrix; // Matrice en mode attaque
	
	private org.totalboumboum.ai.v201011.adapter.data.AiZone zone; // La zone de jeu de cette instance
	
	//Les objets pour remplir le matrice
	public Bombe bombe = new Bombe();
	public Bonus bonus = new Bonus();
	public Fire fire = new Fire();
	public Heros heros = new Heros();
	public Walls walls = new Walls();
	//les objet pour affichage
	AiOutput outPut;
	Color couleur = null;
	
	//methode d'initialisation de l'environement interne de l'IA
	public Matrix(KantarciTaskin ai)
	{
		//ai.checkInterruption();
		zone=ai.getPercepts();
	}
	
	
	//methode pour remplir les matrice avec des items
	public void fillMatrix ()throws StopRequestException
	{
		//checkInterruption();
		//le grandeurs du zone
		int heigh = zone.getHeight();
		int width = zone.getWidth();	
		
		//les variables pour les boucles
		int i=0;
		int j=0;
				
		//initialisation du la matrice avec le valeur 0. Comme cela les murs durs seront 0 et on les connaitras grace à cela	
		for (i=0; i<heigh ;i++)
		{
			for (j=0; j<width ;i++)
			{
				System.out.print("lolo");
				collectionMatrix[i][j]=0;
				outPut.setTileColor(i, j,couleur.darker());
			}
		}
		
		//on ajoute les items dans le matrice
		bombe.fillMatrix(collectionMatrix);
		bonus.fillMatrix(collectionMatrix);
		fire.fillMatrix(collectionMatrix);
		heros.fillMatrix(collectionMatrix);
		walls.fillMatrix(collectionMatrix);
	
	}
}
