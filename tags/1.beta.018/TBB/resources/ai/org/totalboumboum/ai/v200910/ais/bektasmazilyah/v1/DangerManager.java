package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * 
 * @version 1
 * 
 * @author Erdem Bektas
 * @author Nedim Mazilyah
 *
 */
public class DangerManager {
	
	private BektasMazilyah hero;
	private AiZone zone;
	private double matrix[][];//matrice contenant les valeurs de securité

	private List<AiBomb> processedBombs;
	public static double SAFE=Double.MAX_VALUE;
	public static double FIRE=0;
	
	public DangerManager(BektasMazilyah hero) throws StopRequestException
	{
		hero.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.hero=hero;
		zone=hero.getZone();
		matrix = new double[zone.getHeigh()][zone.getWidth()];
		processedBombs = new ArrayList<AiBomb>();
		
	}
	//renvoie la matrice de securite
	public double[][] getMatrix() throws StopRequestException {
		hero.checkInterruption();//APPEL OBLIGATOIRE
		return matrix;
	}
	
	// mise à jour de la matrice de securite

	@SuppressWarnings("unused")
	private void updateMatrix() throws StopRequestException
	{	hero.checkInterruption(); //APPEL OBLIGATOIRE

		processedBombs.clear();
		
		// on initialise la matrice : toutes les cases sont sûres
		for(int line=0;line<zone.getHeigh();line++)
		{	hero.checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	hero.checkInterruption(); //APPEL OBLIGATOIRE
				matrix[line][col] = SAFE;			
			}
		}
		
		AiHero ownHero = zone.getOwnHero();
		// si le personnage est sensible au feu, on tient compte des explosions en cours et à venir
		if(!ownHero.hasThroughFires())
		{	for(int line=0;line<zone.getHeigh();line++)
			{	hero.checkInterruption(); //APPEL OBLIGATOIRE
				for(int col=0;col<zone.getWidth();col++)
				{	hero.checkInterruption(); //APPEL OBLIGATOIRE
					AiTile tile = zone.getTile(line, col);
					Collection<AiFire> fires = tile.getFires();
					Collection<AiBomb> bombs = tile.getBombs();
					Collection<AiBlock> blocks = tile.getBlocks();
					// s'il y a du feu : valeur zéro (il ne reste pas de temps avant l'explosion)
					if(!fires.isEmpty())
					{	matrix[line][col] = FIRE;				
					}
					// s'il y a un mur en train de brûler : pareil
					else if(!blocks.isEmpty())
					{	AiBlock block = blocks.iterator().next();
						if(block.getState().getName()==AiStateName.BURNING)
							matrix[line][col] = FIRE;
					}
					// s'il y a une bombe : pour sa portée, la valeur correspond au temps théorique restant avant son explosion
					// (plus ce temps est court et plus la bombe est dangereuse)
					else if(bombs.size()>0)
					{	AiBomb bomb = bombs.iterator().next();
						processBomb(bomb);
					}
				}
			}
		}
	}
	
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs) throws StopRequestException
	{	hero.checkInterruption(); //APPEL OBLIGATOIRE
	
		if(!bombs.contains(bomb))
		{	bombs.add(bomb);
		
			// on récupère le souffle
			List<AiTile> tempBlast = bomb.getBlast();
			blast.addAll(tempBlast);
			
			// bombs
			for(AiTile tile: tempBlast)
			{	hero.checkInterruption(); //APPEL OBLIGATOIRE
				Collection<AiBomb> bList = tile.getBombs();
				if(bList.size()>0)
				{	AiBomb b = bList.iterator().next();
					getBlast(b,blast,bombs);
				}
			}
		}
		
		return blast;
	}
	
	private void processBomb(AiBomb bomb) throws StopRequestException
	{	hero.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!processedBombs.contains(bomb))
		{	// récupération des cases à portée
			List<AiTile> blast = new ArrayList<AiTile>();
			List<AiBomb> bombs = new ArrayList<AiBomb>();
			getBlast(bomb,blast,bombs);
			processedBombs.addAll(bombs);
			
			// on détermine quelle est la bombe la plus dangereuse (temps le plus court)
			double value = SAFE;
			for(AiBomb b: bombs)
			{	hero.checkInterruption(); //APPEL OBLIGATOIRE
				// calcul du temps restant théoriquement avant l'explosion
				double time = b.getNormalDuration() - b.getTime();
				// màj de value
				if(time<value)
					value = time;
			}
			
			// on met à jour toutes les cases situées à portée
			for(AiTile t: blast)
			{	hero.checkInterruption(); //APPEL OBLIGATOIRE
				int l = t.getLine();
				int c = t.getCol();
				// on modifie seulement si la case n'a pas déjà un niveau de sécurité inférieur
				if(matrix[l][c]>value)
					matrix[l][c] = value;						
			}
		}
	}

	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	hero.checkInterruption(); //APPEL OBLIGATOIRE
	
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrix[line][col];
		return result;		
	}

	public boolean isClear(AiTile tile) throws StopRequestException
	{	hero.checkInterruption(); //APPEL OBLIGATOIRE
		double level = getSafetyLevel(tile);
		boolean result = level==SAFE;
		return result;
	}

	public List<AiTile> findSafeTiles(AiTile origin) throws StopRequestException
	{	hero.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeigh();line++)
		{	hero.checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	hero.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				if(isClear(tile))
					result.add(tile);
			}
		}
		
		return result;
	}
	
}