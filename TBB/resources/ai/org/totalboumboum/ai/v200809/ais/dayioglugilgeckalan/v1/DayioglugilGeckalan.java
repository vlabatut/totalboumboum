
package org.totalboumboum.ai.v200809.ais.dayioglugilgeckalan.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ali Batuhan Dayioğlugil
 * @author Gökhan Geçkalan
 *
 */
@SuppressWarnings("deprecation")
public class DayioglugilGeckalan extends ArtificialIntelligence 
{
	
	private AiTile currentTile;	
	private AiTile nextTile = null;
	private AiTile previousTile = null;
	AiTile safe;
	Collection<AiTile> danger;
	AiHero tahta;
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		

		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(ownHero!=null)
		{	// on met à jour la position de l'ia dans la zone
			//currentTile = ownHero.getTile();
			danger=danger();
			currentTile = ownHero.getTile();
			if (currentTile.getBombs()!=null)
				danger.add(currentTile);
			tahta=getEnemy();
			safe= safePlace();		
			// premier appel : on initialise l'IA
			if(nextTile == null)
				init();
			/* if(danger.contains(currentTile)){
				AiTile t=getNextTile();
				nextTile=t;
			 }*/
			 else if (currentTile.getCol() == tahta.getCol()
						|| currentTile.getLine() == tahta.getLine())
					result = new AiAction(AiActionName.DROP_BOMB);
			 
			// arrivé à destination : on choisit une nouvelle destination
			 else if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contrôle manuel du personnage
			
			// sinon (on garde la même direction) on vérifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
			else
				checkNextTile();
			
			// on calcule la direction à prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			// on calcule l'action
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
		@SuppressWarnings("unused")
		AiTile currentTil = ownHero.getTile();
		Collection<AiBomb> bomb=zone.getBombs();
		Iterator<AiBomb> it=bomb.iterator();
		
		AiTile temp;
		int i=0;
		while(it.hasNext()){
			checkInterruption();
			 temp=it.next().getTile();
			 danger.add(temp);
			 for(i=0;i<5;i++)
			 danger.add(zone.getTile(temp.getCol(),temp.getLine()+i));
			 for(i=0;i<5;i++)
				 danger.add(zone.getTile(temp.getCol(),temp.getLine()-i));
			 for(i=0;i<5;i++)
				 danger.add(zone.getTile(temp.getCol()+i,temp.getLine()));
			 for(i=0;i<5;i++)
				 danger.add(zone.getTile(temp.getCol()-i,temp.getLine()));
				 
		}
			
	return danger;	
	}

	
	@SuppressWarnings("unused")
	private AiTile getNextTile() throws StopRequestException{
		checkInterruption();
		
		List<AiTile> list = getClearNeighbors(currentTile);
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
		List<AiTile> tiles = getClearNeighbors(currentTile);
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
	
	private List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
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
			List<AiTile> tiles = getClearNeighbors(currentTile);
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
