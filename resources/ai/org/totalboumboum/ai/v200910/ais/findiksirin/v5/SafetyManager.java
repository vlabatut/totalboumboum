package org.totalboumboum.ai.v200910.ais.findiksirin.v5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
 * @author Ali Fındık
 * @author Göknur Şırın
 */
@SuppressWarnings("deprecation")
public class SafetyManager
{	/** classe principale de l'IA, permet d'accéder à checkInterruption() */
	private FindikSirin ai;
	
	/**
	 * 
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public SafetyManager(FindikSirin ai) throws StopRequestException
	{
		ai.checkInterruption();
		
		this.ai = ai;
		zone = ai.getZone();
		matrix = new double[zone.getHeight()][zone.getWidth()];
		processedBombs = new ArrayList<AiBomb>();	
	}
	
	/** la marice du jeu */
	public static double SAFE = Double.MAX_VALUE;
	/** */
	public static double NOTSAFE = 0;
	/** */
	public static double BLOCKED = 0;
	/** */
	private double matrix[][];
	/** */
	private AiZone zone;
	
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double[][] getMatrix() throws StopRequestException
	{	ai.checkInterruption();
		return matrix;		
	}
	
	/**
	 * mise à jour de la matrice de sûreté
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private void updateMatrix() throws StopRequestException
	{	ai.checkInterruption();

		processedBombs.clear();
		
		// initialisation de la matrice du jeu
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption();
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption();
				matrix[line][col] = SAFE;			
			}
		}
		
		AiHero ownHero = ai.getOwnHero();

		if(!ownHero.hasThroughFires())
		{	for(int line=0;line<zone.getHeight();line++)
			{	ai.checkInterruption();
				for(int col=0;col<zone.getWidth();col++)
				{	ai.checkInterruption();
					AiTile tile = zone.getTile(line, col);
					Collection<AiFire> fires = tile.getFires();
					Collection<AiBomb> bombs = tile.getBombs();
					Collection<AiBlock> blocks = tile.getBlocks();
					// s'il y a du feu
					if(!fires.isEmpty())
					{	matrix[line][col] = NOTSAFE;				
					}
					// s'il y a un block
					else if(!blocks.isEmpty())
					{	AiBlock block = blocks.iterator().next();
						if(block.getState().getName()==AiStateName.BURNING)
							matrix[line][col] = NOTSAFE;
					}
					// s'il y a une bombe
					else if(bombs.size()>0)
					{	AiBomb bomb = bombs.iterator().next();
						processBomb(bomb);
					}
					//si nous avons deja posé une bombe **************************** 
					else if(!tile.getBombs().isEmpty()){
						matrix[line][col] = NOTSAFE;
					}
				}
			}
		}
		
	}

	/** LES BOMBES ET LES BLASTES */
	private List<AiBomb> processedBombs;	
	/** la liste des blasts
	 * 
	 * @param bomb
	 * 		Description manquante !
	 * @param blast
	 * 		Description manquante !
	 * @param bombs
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs) throws StopRequestException
	{	ai.checkInterruption(); 
	
		if(!bombs.contains(bomb))
		{	bombs.add(bomb);
		
			// les blastes
			List<AiTile> tempBlast = bomb.getBlast();
			blast.addAll(tempBlast);
			
			// les bombs
			for(AiTile tile: tempBlast)
			{	ai.checkInterruption(); 
				Collection<AiBomb> bList = tile.getBombs();
				if(bList.size()>0)
				{	AiBomb b = bList.iterator().next();
					getBlast(b,blast,bombs);
				}
			}
		}	
		return blast;
	}	

	/** le traitement avec les bombes
	 * 
	 * @param bomb
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void processBomb(AiBomb bomb) throws StopRequestException
	{	ai.checkInterruption();
		
		if(!processedBombs.contains(bomb))
		{	// on prend les bombes et les blastes
			List<AiTile> blast = new ArrayList<AiTile>();
			List<AiBomb> bombs = new ArrayList<AiBomb>();
			getBlast(bomb,blast,bombs);
			processedBombs.addAll(bombs);
			
			// on determine la bombe la plus dangereuse
			double value = SAFE;
			for(AiBomb b: bombs)
			{	ai.checkInterruption();
				// le temps restant avant l'explosion
				double time = b.getNormalDuration() - b.getTime();
				if(time<value)
					value = time;
			}
			
			// on met a jour les blastes
			for(AiTile t: blast)
			{	ai.checkInterruption();
				int l = t.getLine();
				int c = t.getCol();
				// on modifie seulement si la case n'a pas deja un niveau de securite inf
				if(matrix[l][c]>value)
					matrix[l][c] = value;						
			}
		}
	}

	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrix[line][col];
		return result;		
	}

	/** si le cas est completement sur
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		
		double level = getSafetyLevel(tile);
		boolean result = level==SAFE;
		return result;
	}

	/** retourne la liste des cases surs
	 * 
	 * @param origin
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> findSafeTiles(AiTile origin) throws StopRequestException
	{	ai.checkInterruption();
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption();
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption();
				AiTile tile = zone.getTile(line,col);
				if(isSafe(tile))
					result.add(tile);
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param currentTile
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean hasNoWhereToGo(AiTile currentTile) throws StopRequestException {
		ai.checkInterruption();
		int hasNoWhereToGo=0;
		AiTile tempTile=null;
		List <AiTile> neighbors = currentTile.getNeighbors();
		Iterator <AiTile> n = neighbors.iterator();
		while(n.hasNext()){
			ai.checkInterruption();
			tempTile=n.next();
			if(!( isSafe( tempTile ) || !(tempTile.isCrossableBy(zone.getOwnHero()))))
				hasNoWhereToGo++;
		}
		return (hasNoWhereToGo==4);
	}
	
	/**
	 * 
	 * @param destinationTile
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isReachable(AiTile destinationTile) throws StopRequestException{
		ai.checkInterruption();
		AiTile currentTile=zone.getOwnHero().getTile();
		double distance=zone.getPixelDistance(currentTile.getPosX(), currentTile.getPosY(), destinationTile.getPosX(), destinationTile.getPosY());
		double ourSpeed=zone.getOwnHero().getWalkingSpeed();
		
		if( (distance/ourSpeed) > 5  )
			return false;
		else 
			return true;
	}
	
	
	/** le processus
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE		
		updateMatrix();
	}
	
}
