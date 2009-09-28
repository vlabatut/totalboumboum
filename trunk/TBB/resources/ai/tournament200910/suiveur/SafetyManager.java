package tournament200910.suiveur;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiFire;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * classe chargée d'extraire de la zone les informations
 * permettant de déterminer le niveau de sûreté des cases
 */
public class SafetyManager
{	
	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = true;
	
	public SafetyManager(Suiveur ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		AiZone zone = ai.getZone();
		matrix = new double[zone.getHeigh()][zone.getWidth()];
		processedBombs = new ArrayList<AiBomb>();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Suiveur ai;

	/////////////////////////////////////////////////////////////////
	// MATRIX	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** valeur pour une case complètement sûre (temps restant avant explosion : maximal) */
	public static double SAFE = Double.MAX_VALUE;
	/** valeur pour une case pas du tout sûre (temps restant avant explosion : aucun) */
	public static double FIRE = 0;
	/** matrice contenant les valeurs de sûreté */
	private double matrix[][];
	
	public double[][] getMatrix() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		return matrix;		
	}
	
	private void updateMatrix(AiZone zone) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE

		processedBombs.clear();
		
		// on initialise la matrice : toutes les cases sont sûres
		for(int line=0;line<zone.getHeigh();line++)
			for(int col=0;col<zone.getWidth();col++)
				matrix[line][col] = SAFE;
		
		// on rajoute le feu et les cases à portée de bombe
		for(int line=0;line<zone.getHeigh();line++)
			for(int col=0;col<zone.getWidth();col++)
			{	AiTile tile = zone.getTile(line, col);
				Collection<AiFire> fires = tile.getFires();
				Collection<AiBomb> bombs = tile.getBombs();
				// s'il y a du feu : valeur zéro (il ne reste pas de temps avant l'explosion)
				if(fires.size()>0)
					matrix[line][col] = FIRE;
				// s'il y a une bombe : pour sa portée, la valeur correspond au temps théorique restant avant son explosion
				// (plus ce temps est court et plus la bombe est dangereuse)
				else if(bombs.size()>0)
				{	AiBomb bomb = bombs.iterator().next();
					processBomb(bomb);
				}
			}
		
		if(verbose)
		{	System.out.println(">>>>>>>>>> SAFETY MATRIX <<<<<<<<<<");
			for(int line=0;line<zone.getHeigh();line++)
			{	for(int col=0;col<zone.getWidth();col++)
				{	if(matrix[line][col]==SAFE)
						System.out.printf("\tSAFE");
					else
						System.out.printf("\t%.0f",matrix[line][col]);
				
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des bombes traitées au cours de cette itération (pour ne pas les traiter plusieurs fois) */
	private List<AiBomb> processedBombs;
	
	/**
	 * calcule une liste de cases correspondant à la portée indirecte de la bombe
	 * passée en paramètre. Le terme "indirecte" signifie que la fonction est récursive : 
	 * si une case à portée contient une bombe, la portée de cette bombe est rajoutée
	 * dans la liste blast, et la bombe est rajoutée dans la liste bombs.
	 */
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		if(!processedBombs.contains(bomb))
		{	// initialisation
			processedBombs.add(bomb);
			int range = bomb.getRange();
			
			// centre
			AiTile center = bomb.getTile();
			blast.add(center);
			bombs.add(bomb);
			
			// branches
			for(Direction direction: Direction.getPrimaryValues())
			{	AiTile tile = center;
				for(int i=0;i<range;i++)
				{	// on rajoute la case dans blast
					tile = tile.getNeighbor(direction);
					blast.add(tile);
					// on teste si la case contient une bombe
					Collection<AiBomb> bList = tile.getBombs();
					if(bList.size()>0)
					{	// si oui, on traite la bombe à son tour
						AiBomb b = bList.iterator().next();
						getBlast(b,blast,bombs);
					}
				}
			}
		}
		
		return blast;
	}	

	private void processBomb(AiBomb bomb) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		// récupération des cases à portée
		List<AiTile> blast = new ArrayList<AiTile>();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		getBlast(bomb,blast,bombs);
		
		// on détermine quelle est la bombe la plus dangereuse (temps le plus court)
		double value = SAFE;
		for(AiBomb b: bombs)
		{	// calcul du temps restant théoriquement avant l'explosion
			double time = b.getNormalDuration() - b.getTime();
			// màj de value
			if(time<value)
				value = time;
		}
		
		// on met à jour toutes les cases situées à portée
		for(AiTile t: blast)
		{	int l = t.getLine();
			int c = t.getCol();
			// on modifie seulement si la case n'a pas déjà un niveau de sécurité inférieur
			if(matrix[l][c]>value)
				matrix[l][c] = value;						
		}
	}

	/////////////////////////////////////////////////////////////////
	// TILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrix[line][col];
		return result;		
	}

	
	/////////////////////////////////////////////////////////////////
	// PROCESS		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour la matrice de sûreté
	 */
	public void update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiZone zone = ai.getZone();
		updateMatrix(zone);
	}
}
