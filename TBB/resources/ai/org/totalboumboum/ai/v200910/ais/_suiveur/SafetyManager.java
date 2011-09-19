package org.totalboumboum.ai.v200910.ais._suiveur;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;


/**
 * classe chargée d'extraire de la zone les informations
 * permettant de déterminer le niveau de s�ret� des cases.
 * Une matrice de réels représente la zone de jeu, chaque case
 * �tant représent�e par le temps restant avant qu'une flamme ne la
 * traverse. Donc plus le temps est long, et plus la case est s�re. 
 * La valeur maximale (Double.MAX_VALUE) signifie que la case n'est pas menac�e par une
 * bombe. Une valeur nulle signifie que la case est actuellement en feu.
 * Une valeur n�gative signifie que la case est menac�e par une bombe
 * télécommand�e, qui peut exploser n'importe quand (la valeur absolue
 * de la valeur correspond au temps depuis lequel la bombe a été pos�e)
 * 
 * @author Vincent Labatut
 *
 */
public class SafetyManager
{	
	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;
	
	public SafetyManager(Suiveur ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		zone = ai.getZone();
		matrix = new double[zone.getHeight()][zone.getWidth()];
		processedBombs = new ArrayList<AiBomb>();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** IA associée à ce gestionnaire de s�ret� */
	private Suiveur ai;

	/////////////////////////////////////////////////////////////////
	// MATRIX	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** valeur pour une case compl�tement s�re (temps restant avant explosion : maximal) */
	public static double SAFE = Double.MAX_VALUE;
	/** valeur pour une case pas du tout s�re (temps restant avant explosion : aucun) */
	public static double FIRE = 0;
	/** matrice contenant les valeurs de s�ret� */
	private double matrix[][];
	/** zone de jeu */
	private AiZone zone;
	
	/**
	 * renvoie la matrice de suret�
	 */
	public double[][] getMatrix() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		return matrix;		
	}
	
	/**
	 * mise à jour de la matrice de s�ret�
	 */
	private void updateMatrix() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE

		processedBombs.clear();
		
		// on initialise la matrice : toutes les cases sont s�res
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				matrix[line][col] = SAFE;			
			}
		}
		
		AiHero ownHero = ai.getOwnHero();
		// si le personnage est sensible au feu, on tient compte des explosions en cours et à venir
		if(!ownHero.hasThroughFires())
		{	for(int line=0;line<zone.getHeight();line++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				for(int col=0;col<zone.getWidth();col++)
				{	ai.checkInterruption(); //APPEL OBLIGATOIRE
					
					AiTile tile = zone.getTile(line, col);
					Collection<AiFire> fires = tile.getFires();
					Collection<AiBomb> bombs = tile.getBombs();
					Collection<AiBlock> blocks = tile.getBlocks();
					// s'il y a du feu : valeur z�ro (il ne reste pas de temps avant l'explosion)
					if(!fires.isEmpty())
					{	matrix[line][col] = FIRE;				
					}
					// s'il y a un mur en train de brûler : pareil
					else if(!blocks.isEmpty())
					{	AiBlock block = blocks.iterator().next();
						if(block.getState().getName()==AiStateName.BURNING)
							matrix[line][col] = FIRE;
					}
					// s'il y a une bombe : pour sa port�e, la valeur correspond au temps th�orique restant avant son explosion
					// (plus ce temps est court et plus la bombe est dangereuse)
					else if(bombs.size()>0)
					{	AiBomb bomb = bombs.iterator().next();
						processBomb(bomb);
					}
				}
			}
		}
		
		if(verbose)
		{	System.out.println(">>>>>>>>>> SAFETY MATRIX <<<<<<<<<<");
			for(int line=0;line<zone.getHeight();line++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				for(int col=0;col<zone.getWidth();col++)
				{	ai.checkInterruption(); //APPEL OBLIGATOIRE
					
					if(matrix[line][col]==SAFE)
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
	 * calcule une liste de cases correspondant au souffle indirect de la bombe
	 * passée en paramètre. Le terme "indirect" signifie que la fonction est r�cursive : 
	 * si une case à port�e de souffle contient une bombe, le souffle de cette bombe est rajout�
	 * dans la liste blast, et la bombe est rajout�e dans la liste bombs.
	 * 
	 * <b>Avertissement :</b> la fonction est simpliste, et pas compl�tement fiable.
	 * Ainsi, elle considère que si une bombe peut en faire exploser une autre,
	 * alors le contraire est vrai (la seconde peut faire exploser la première),
	 * mais ceci n'est pas toujours vrai (cela dépend de la port�e des bombes).
	 * De plus, elle ne prend pas en compte le fait que certaines bombes ne sont pas
	 * sensibles au feu (elles n'explosent pas quand elles sont touch�es par une explosion).
	 */
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		if(!bombs.contains(bomb))
		{	bombs.add(bomb);
		
			// on r�cup�re le souffle
			List<AiTile> tempBlast = bomb.getBlast();
			blast.addAll(tempBlast);
			
			// bombs
			for(AiTile tile: tempBlast)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				Collection<AiBomb> bList = tile.getBombs();
				if(bList.size()>0)
				{	AiBomb b = bList.iterator().next();
					getBlast(b,blast,bombs);
				}
			}
		}
		
		return blast;
	}	

	/**
	 * traite la bombe passée en paramètre
	 */
	private void processBomb(AiBomb bomb) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!processedBombs.contains(bomb))
		{	// r�cup�ration des cases à port�e
			List<AiTile> blast = new ArrayList<AiTile>();
			List<AiBomb> bombs = new ArrayList<AiBomb>();
			getBlast(bomb,blast,bombs);
			processedBombs.addAll(bombs);
			
			// on détermine quelle est la bombe la plus dangereuse (temps le plus court)
			double value = SAFE;
			for(AiBomb b: bombs)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				// calcul du temps restant th�oriquement avant l'explosion
				double time = b.getNormalDuration() - b.getTime();
				// m�j de value
				if(time<value)
					value = time;
			}
			
			// on met à jour toutes les cases situ�es à port�e
			for(AiTile t: blast)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				int l = t.getLine();
				int c = t.getCol();
				// on modifie seulement si la case n'a pas déjà un niveau de sécurité inf�rieur
				if(matrix[l][c]>value)
					matrix[l][c] = value;						
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// TILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le niveau de sécurité de la case passée en paramètre
	 * (i.e. le temps restant avant explosion)
	 */
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrix[line][col];
		return result;		
	}

	/**
	 * détermine si le niveau de sécurité de la case passée en paramètre
	 * est maximal (ce traitement n'est pas tr�s subtil : en cas d'explosion potentielle,
	 * on pourrait calculer le temps nécessaire pour atteindre la case et 
	 * déterminer si c'est possible de passer dessus avant l'explosion)
	 */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		double level = getSafetyLevel(tile);
		boolean result = level==SAFE;
		return result;
	}
	
	public List<AiTile> findSafeTiles(AiTile origin) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				AiTile tile = zone.getTile(line,col);
				if(isSafe(tile))
					result.add(tile);
			}
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour la matrice de s�ret�
	 */
	public void update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		updateMatrix();
		updateOutput();
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * met à jour la sortie graphique de l'IA en fonction du
	 * niveau de s�ret� calculé
	 */
	private void updateOutput() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiOutput output = ai.getOutput();
	
		// couleurs des cases
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				Color color = null;			
				if(matrix[line][col]==0)
					color = Color.YELLOW;
				else if(matrix[line][col]==SAFE)
					color = Color.WHITE;
				else if(matrix[line][col]>0)
					color = Color.RED;
				else if(matrix[line][col]<0)
					color = Color.BLACK;
				output.setTileColor(line,col,color);
			}
		}
		
		// texte des cases
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				String text = null;			
				if(matrix[line][col]==SAFE)
					text = "\u221E";
				else 
					text = nf.format(matrix[line][col]); 
				output.setTileText(line,col,text);
			}
		}
	}
}
