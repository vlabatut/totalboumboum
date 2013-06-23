package org.totalboumboum.ai.v200910.ais.findiksirin.v1_2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * >> c'est la classe qui est responsable de la securite
 */
public class SafetyManager
{	/** classe principale de l'IA, permet d'accéder à checkInterruption() */
	private FindikSirin monIa;
	
	public SafetyManager(FindikSirin monIa) throws StopRequestException
	{
		monIa.checkInterruption();		
		// initialisation du champ permettant d'appeler checkInterruption 
		this.monIa = monIa;	
		zone= monIa.getZone();
		matrix = new double[zone.getWidth()][zone.getHeight()];
	}
	
	///la matrice d'info
	public static double SAFE = Double.MAX_VALUE;
	public static double NOTSAFE = 0;
	public static double BLOCKED = 0;
	private double matrix[][]={};
	private AiZone zone;
	
	public double [][] getMatrix() throws StopRequestException{
		monIa.checkInterruption();
		return matrix;
	}
	
	//remplissage de la matrice d'info
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////IL Y A UN PROBLEME DE ARRAY DANS "updateMatrix() cause par matrix[][]",ON N'A PAS EU ASSEZ DE TEMPS POUR LE CORRIGER//
	//ON VA ESSAYER DE TROUVER UNE SOLUTION A L'ERREUR (java.lang.ArrayIndexOutOfBoundsException) DANS LE WEEKEND/////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	 private void updateMatrix() throws StopRequestException{
		monIa.checkInterruption();
		for (int line=0;line<5;line++){
			monIa.checkInterruption();
			for (int column=0; column<5;column++){
				monIa.checkInterruption();
				matrix[line][column]=SAFE;
			}
		}
		
		for (int line=0;line<zone.getHeight();line++){
			monIa.checkInterruption();
			for (int column=0; column<zone.getWidth();column++){
				monIa.checkInterruption();
				AiTile tile = zone.getTile(line, column);
				Collection<AiFire> fires = tile.getFires();
				Collection<AiBomb> bombs = tile.getBombs();
				Collection<AiBlock> blocks = tile.getBlocks();
				//si il y a du feu
				if(!fires.isEmpty()){
					matrix[line][column]=NOTSAFE;	
				}
				//si il est blocke
				else if(!blocks.isEmpty()){
					matrix[line][column]=BLOCKED;	
				}
				//si il y a un bombe
				else if(!bombs.isEmpty()){
					AiBomb bomb = bombs.iterator().next();
					double explosionTime= bomb.getExplosionDuration() - bomb.getTime();
					if( explosionTime > matrix[line][column] ){
						matrix[line][column]=explosionTime;
					}
				}
			}
		}
		
		
		
		//l'affichage du matrice
		System.out.println(">>>>>>>>>> SAFETY MATRIX <<<<<<<<<<");
		for(int line=0;line<zone.getHeight();line++)
		{	monIa.checkInterruption();
			
			for(int col=0;col<zone.getWidth();col++)
			{	monIa.checkInterruption();
				
				if(matrix[line][col]==SAFE)
					System.out.printf("\tSAFE");
				else
					System.out.printf("\t%.0f",matrix[line][col]);
			
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	//control si c'est une case sur
	public boolean isSafe (AiTile tile) throws StopRequestException{
		monIa.checkInterruption();
		return (matrix[tile.getLine()][tile.getCol()]>0);
	}
	
	// tile le plus sur
	public Direction safestNeighbour(AiTile currentTile) throws StopRequestException{
		monIa.checkInterruption();
		
		Direction safestNeighbourDir=Direction.NONE;
		List<AiTile> neighbours = new ArrayList<AiTile>();
		// on ajoute les neighbours dans un list 
		
		neighbours.add(currentTile);
		neighbours.add(currentTile.getNeighbor(Direction.UP));
		neighbours.add(currentTile.getNeighbor(Direction.DOWN));
		neighbours.add(currentTile.getNeighbor(Direction.LEFT));
		neighbours.add(currentTile.getNeighbor(Direction.RIGHT));
		
		//on choisit le tile qui a une valeur de surete max
		while(neighbours.iterator().hasNext()){
			AiTile tempTile=neighbours.iterator().next();
			if(matrix[tempTile.getLine()][tempTile.getCol()]>0){
				if (currentTile.getNeighbor(Direction.UP)==tempTile)
					safestNeighbourDir=Direction.UP;
				if (currentTile.getNeighbor(Direction.DOWN)==tempTile)
					safestNeighbourDir=Direction.DOWN;
				if (currentTile.getNeighbor(Direction.LEFT)==tempTile)
					safestNeighbourDir=Direction.LEFT;
				if (currentTile.getNeighbor(Direction.RIGHT)==tempTile)
					safestNeighbourDir=Direction.RIGHT;
			}		
		}			
		return safestNeighbourDir;
	}
	
	/// appel du fonction de remplissage
	public void update() throws StopRequestException{
		monIa.checkInterruption();
		updateMatrix();
	}
}
