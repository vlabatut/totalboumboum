package org.totalboumboum.ai.v200809.ais.dayioglugilgeckalan.v2;
/**
 * @author Ali Batuhan Dayioğlugil
 * @author Gökhan Geçkalan
 */

/*
package tournament200809.dayioglugilgeckalan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.totalboumboum.ai.adapter200809.AiAction;
import org.totalboumboum.ai.adapter200809.AiActionName;
import org.totalboumboum.ai.adapter200809.AiBlock;
import org.totalboumboum.ai.adapter200809.AiBomb;
import org.totalboumboum.ai.adapter200809.AiFire;
import org.totalboumboum.ai.adapter200809.AiHero;
import org.totalboumboum.ai.adapter200809.AiTile;
import org.totalboumboum.ai.adapter200809.AiZone;
import org.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import org.totalboumboum.ai.adapter200809.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

public class DayioglugilGeckalan extends ArtificialIntelligence 
{
	
	private AiTile currentTile;	
	private AiTile nextTile = null;
	private AiTile previousTile = null;
	AiTile safe;
	Collection<AiTile> danger;
	AiHero tahta;
	AiZone zone = getPercepts();
	int h = zone.getHeigh();
	int w = zone.getWidth();
	AiTile[][] mat = new AiTile[h][w];
	String[][] items = new String[h][w];
	
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {

				items[i][j] = null;
				mat[i][j] = zone.getTile(i, j);
				if (mat[i][j].getBombs() != null)
					items[i][j] = "boom";
				else if (mat[i][j].getHeroes() != null) {
					if (mat[i][j].getHeroes().contains(ownHero)) {
						items[i][j] = "zeka"; // pasa = true;
					} else {
						items[i][j] = "pasa"; // zeka = true;
					}
				} 
				else if (mat[i][j].getFires() != null)
					items[i][j] = "alev";
				else if (mat[i][j].getBlock() != null)
					items[i][j] = "tass";
				else if (mat[i][j].getBlock().isDestructible())
					items[i][j] = "soft";
				System.out.print(("(" + i + "," + j + ")" + items[i][j] + "").toString());
			}
			System.out.print("\n");
		}
		System.out.println("\n\n");
		

		
		if(ownHero!=null)
		{	
			danger=danger();
			currentTile = ownHero.getTile();
			if (currentTile.getBombs()!=null)
				danger.add(currentTile);
			tahta=getEnemy();
			safe= safePlace();		
			
			if(nextTile == null)
				init();
			
			 else if (currentTile.getCol() == tahta.getCol()
						|| currentTile.getLine() == tahta.getLine())
					result = new AiAction(AiActionName.DROP_BOMB);
			 
			
			 else if(currentTile==nextTile)
				pickNextTile();
			else
				checkNextTile();
			
			
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			
			if(direction!=Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);
			
		}
		return result;
	}
	
	private AiHero getEnemy() throws StopRequestException
	{checkInterruption();
	AiZone zone = getPercepts();
	AiHero ownHero = zone.getOwnHero();
	Collection<AiHero> pasa = zone.getHeroes();
	AiHero enemy=null;
	Iterator<AiHero> it = pasa.iterator();
		while (it.hasNext())
		{  checkInterruption();
		AiHero temp=it.next();
			if (!temp.equals(ownHero))
				enemy = temp;
		}
		
	return enemy;
	}
	
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		nextTile = currentTile;		
		previousTile = currentTile;		
	}

	public AiTile safePlace() throws StopRequestException{
		checkInterruption();
		AiTile safePlace = null;
		LinkedList<AiTile> file = new LinkedList<AiTile>();
		file.offer(currentTile);
		Collection<AiTile> x=new ArrayList<AiTile>();
		while (!file.isEmpty() && safePlace == null) {
			AiTile searchNode = file.poll();
			x.add(searchNode);
			if (!danger.contains(searchNode))
				safePlace=searchNode;
			else if(x.contains(searchNode))
				safePlace=null;
			
			else {
					Collection<AiTile> neighbors=getClearNeighbors(searchNode);
					Iterator<AiTile> i = neighbors.iterator();
					while (i.hasNext()) {
						checkInterruption();
						file.offer(i.next());						
					}
			}
		}
		return safePlace;
	}
	
	private int getDistance(AiTile t,AiTile y) throws StopRequestException
	{  checkInterruption();		
		int a=y.getCol();
		int b=y.getLine();
		int c=t.getCol();
		int d=t.getLine();
		int result = 0;
		result = result + Math.abs(a-c);
		result = result + Math.abs(b-d);
		return result;
	}
	
	private Collection<AiTile> danger()  throws StopRequestException
	{  checkInterruption();
		Collection<AiTile> danger=new ArrayList<AiTile>();
		AiZone zone=getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiTile currentTile = ownHero.getTile();
		Collection<AiBomb> bomb = zone.getBombs();
		Iterator<AiBomb> it = bomb.iterator();
		
		AiTile temp;
		int i = 0;
		while(it.hasNext()){
			checkInterruption();
			 temp = it.next().getTile();
			 danger.add(temp);
			 for(i=1;i<6;i++)
			 danger.add(zone.getTile(temp.getCol(),temp.getLine()+i));
			 for(i=0;i<6;i++)
				 danger.add(zone.getTile(temp.getCol(),temp.getLine()-i));
			 for(i=0;i<6;i++)
				 danger.add(zone.getTile(temp.getCol()+i,temp.getLine()));
			 for(i=0;i<6;i++)
				 danger.add(zone.getTile(temp.getCol()-i,temp.getLine()));
				 
		}
			
	return danger;	
	}
	
	private AiTile getNextTile() throws StopRequestException{
		checkInterruption();
		
		ArrayList<AiTile> list = getClearNeighbors(currentTile);
		Iterator<AiTile> it1 = list.iterator();
		AiTile temp;
	    AiTile min=it1.next();
		while(it1.hasNext())
		{ checkInterruption();
		temp=it1.next();
		if(getDistance(safe, temp)<getDistance(safe, min))
		min=temp;
		}
		
	return min;

	}
	
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases voisines accessibles	
		ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
		// on sort de la liste la case d'où l'on vient (pour éviter de repasser au même endroit)
		boolean canGoBack = false;
		if(tiles.contains(previousTile))
		{	tiles.remove(previousTile);
			canGoBack = true;
		}
		// s'il reste des cases dans la liste
		if(tiles.size()>0)
		{	// si la liste contient la case située dans la direction déplacement précedente,
			// on évite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
			AiTile tempTile = null;
			Direction dir = getPercepts().getDirection(previousTile,currentTile);
			if(dir!=Direction.NONE)
			{	tempTile =  getPercepts().getNeighborTile(currentTile, dir);
				if(tiles.contains(tempTile))
					tiles.remove(tempTile);
			}
			// s'il reste des cases dans la liste
			if(tiles.size()>0)
			{	// on en tire une au hasard
				double p = Math.random()*tiles.size();
				int index = (int)p;
				nextTile = tiles.get(index);
				previousTile = currentTile;
			}
			// sinon (pas le choix) on continue dans la même direction
			else
			{	nextTile = tempTile;
				previousTile = currentTile;
			}
		}
		// sinon (pas le choix) on tente de revenir en arrière
		else
		{	if(canGoBack)
			{	nextTile = previousTile;
				previousTile = currentTile;
			}
			// et sinon on ne peut pas bouger, donc on ne fait rien du tout
		}
	}
	
	private ArrayList<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isClear(t))
				result.add(t);
		}
		return result;
	}
	
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block==null && bombs.size()==0 && fires.size()==0;
		return result;
	}
	
	private void checkNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// si un obstacle est apparu sur la case destination, on change de destination
		if(!isClear(nextTile))
		{	// liste des cases voisines accessibles	
			ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
			// on sort l'ancienne destination (qui est maintenant bloquée) de la liste
			if(tiles.contains(nextTile))
				tiles.remove(nextTile);
			// s'il reste des cases dans la liste : on en tire une au hasard
			if(tiles.size()>0)
			{	double p = Math.random()*tiles.size();
				int index = (int)p;
				nextTile = tiles.get(index);
				previousTile = currentTile;
			}
		}
	}
}
*/