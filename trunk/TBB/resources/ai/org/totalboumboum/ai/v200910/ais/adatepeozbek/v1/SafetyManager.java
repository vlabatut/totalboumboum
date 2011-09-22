package org.totalboumboum.ai.v200910.ais.adatepeozbek.v1;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/** 
 * Cette classe d'abord initialise une matrice avec les valeurs initiales MAX_VALUE et
 * commence à Contrôler les feux, les bombes, les murs brulés.Puis, l'algorithme se tient
 * compte des bombes et remet les valeurs de leurs temps d'explosion à la matrice.
 * 
 * @version 1
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
public class SafetyManager
{		

	// Initialise la zone, l'IA, et la matrice
	public SafetyManager(AdatepeOzbek ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		zone = ai.getZone();
		matrix = new double[zone.getHeight()][zone.getWidth()];
	}
	
	// La variable d'IA de notre caractère
	private AdatepeOzbek ai;
	
	// Le feu est caracterisé par la valeur 0 et les cases secures avec MAX_VALUE
	public static double SAFE = Double.MAX_VALUE;	
	public static double FIRE = 0;	
	
	// La matrice contenant les valeurs secure, feu, temps d'explosion.
	private double matrix[][];
	
	private AiZone zone;
	
	// Retourne la variable privée 
	public double[][] getMatrix() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		return matrix;		
	}
	
	/*
	 * Remets les feux, les murs brulés et les temps d'explosions des bombes à la matrice
	 */
	private void updateMatrix() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		
		for(int line=0;line<zone.getHeight();line++)
		{	
			ai.checkInterruption(); //APPEL OBLIGATOIRE
			
				// Reinitalise toutes les cases à la valeur secure d'abord
				for(int col=0;col<zone.getWidth();col++)
				{	ai.checkInterruption(); //APPEL OBLIGATOIRE
					matrix[line][col] = SAFE;
					AiTile tile = zone.getTile(line, col);
					Collection<AiFire> fires = tile.getFires();
					List<AiBomb> bombs = tile.getBombs();
					Collection<AiBlock> blocks = tile.getBlocks();
					
					// Mets les feux
					if(!fires.isEmpty())
					{	matrix[line][col] = FIRE;				
					}
					
					// Mets les murs brulés
					else if(!blocks.isEmpty())
					{	AiBlock block = blocks.iterator().next();
						if(block.getState().getName()==AiStateName.BURNING)
							matrix[line][col] = FIRE;
					}
					
					// Mets les temps d'explosion des bombes
					else if(bombs.size()>0)
					{	
						processBombs(bombs);
					}
				}
		}
	}
	
	/*
	 * Calcule d'abord les cases qui vont être affectées par les bombes, puis 
	 * calcule les valeurs des temps d'explosion et les mets dans la matrice.
	 */
	private void processBombs(List<AiBomb> bombs) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			double value = SAFE;
			for(AiBomb b: bombs)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				List<AiTile> blasts = b.getBlast();
				
				double time = b.getNormalDuration() - b.getTime();
				
				if(time<value)
					value = time;
				
				for(AiTile t: blasts)
				{	ai.checkInterruption(); //APPEL OBLIGATOIRE
					int l = t.getLine();
					int c = t.getCol();
					
					if(matrix[l][c]>value)
						matrix[l][c] = value;						
				}
			}
			
	}

	/*
	 * Retourne la valeur de securité de la case passée en paramètre
	 */
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrix[line][col];
		return result;		
	}

	/*
	 * Retourne si la case est secure ou non
	 */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		double level = getSafetyLevel(tile);
		boolean result = level==SAFE;
		return result;
	}
	
	/*
	 * Retourne toutes les cases secures dans la zone du jeu
	 */
	public List<AiTile> findSafeTiles() throws StopRequestException
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
	
	/*
	 * Reinitialise toutes les valeurs de la matrice
	 */
	public void update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		updateMatrix();
	}
}
