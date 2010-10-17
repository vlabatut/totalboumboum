package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 1
 * 
 * @author Erdem Bektas
 * @author Nedim Mazilyah
 *
 */
public class BektasMazilyah extends ArtificialIntelligence
{	
	private AiZone zone;

	// le personnage dirigé par cette IA
	private AiHero hero;
	//les adversaires
	private Vector<AiHero> others;
	//la prochaine action que l'IA veut réaliser
	@SuppressWarnings("unused")
	private AiAction action;
	private AiTile targetDeplacement;
	private AiTile previousTile;
	private AiTile currentTile;
	private AiTile nextTile;
	@SuppressWarnings("unused")
	private LinkedList <AiTile> path;
	boolean isDanger = false;
	private boolean initial=false;
	private DangerManager dangerManager=null;
	private AiTile matrix[][];
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		//Appel Obligatoire
		checkInterruption();
		if(hero==null)
			initMonAi();
		
		if(hero != null && !others.isEmpty())
		{
			checkDanger();
			
			if(isDanger)
			{//Il y a un danger, il faut se sauver.

				pickNextTile();
				if(isClear(hero.getTile()))
				{
					action = new AiAction(AiActionName.NONE);
				}
				else
				{
					action = deplace();
				}
			}
			
		}
		
		AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}
	


	private AiAction deplace() throws StopRequestException{
		checkInterruption(); //Appel Obligatoire
		if(hero!=null)
		{	// on met à jour la position de l'ia dans la zone
			currentTile = hero.getTile();
			
			// premier appel : on initialise l'IA
			if(nextTile == null)
			{
				nextTile = currentTile;
				previousTile = currentTile;
			}
			
			// arrivé à destination : on choisit une nouvelle destination
			if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contrôle manuel du personnage
			else if(previousTile!=currentTile)
			{	previousTile = currentTile;
				pickNextTile();			
			}
						
			// on calcule la direction à prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
			
			AiAction result = new AiAction(AiActionName.NONE);
			// on calcule l'action
			if(direction!=Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);			
			return result;
		}
		else
			return new AiAction(AiActionName.NONE);
	}



	public AiTile getCurrentTile() throws StopRequestException {
		checkInterruption();
		return currentTile;
	}



	private void initMonAi() throws StopRequestException{
		//APPEL OBLIGATOIRE
		checkInterruption();
		if(!initial)
		{
			//On initialise les instances
			zone = getPercepts();
			hero = zone.getOwnHero();
			action = new AiAction(AiActionName.NONE);
			others = new Vector<AiHero>();
			isDanger=false;
			currentTile = hero.getTile();
			nextTile = currentTile;
			previousTile = currentTile;
			if(targetDeplacement == null)
				targetDeplacement = currentTile;
			
			path = new LinkedList<AiTile>();
			
			Collection <AiHero> tempHeroCol = zone.getHeroes();
			Iterator <AiHero> itTempHero = tempHeroCol.iterator();
			while(itTempHero.hasNext())
			{	checkInterruption(); //Appel Obligatoire
				AiHero tempHero = itTempHero.next();
				if(!tempHero.equals(hero))
					others.add(tempHero);
			}
			initial = true;
			
		}
		else
		{
			//On initialise les instances
			zone = getPercepts();
			hero = zone.getOwnHero();		
			action = new AiAction(AiActionName.NONE);
			others = new Vector<AiHero>();
			isDanger = false;
			path = new LinkedList<AiTile>();
			Collection <AiHero> tempHeroCol = zone.getHeroes();
			Iterator <AiHero> itTempHero = tempHeroCol.iterator();
			while(itTempHero.hasNext())
			{	checkInterruption(); //Appel Obligatoire
				AiHero tempHero = itTempHero.next();
				if(!tempHero.equals(hero))
					others.add(tempHero);
			}
		}

		
	}
	
	void checkDanger() throws StopRequestException{
		checkInterruption(); //Appel obligatoire
		
		if(isClear(currentTile) || isClear(nextTile))
		{
			isDanger = false;
		}		
		else
			isDanger = true;
	}
	
	private boolean isClear(AiTile tile) throws StopRequestException {
		checkInterruption();
		
		return dangerManager.isClear(tile);
		
	}

	private void pickNextTile() throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
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
			{	tempTile = getNeighborTile(currentTile, dir);
				if(tiles.contains(tempTile))
					tiles.remove(tempTile);
			}
			// s'il reste des cases dans la liste
			if(tiles.size()>0)
			{	// on en tire une au hasard
				double p = Math.random()*Math.random()*tiles.size();
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
		Collection<AiTile> neighbors = getNeighborTiles(tile);
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
	
	public Collection<AiTile> getNeighborTiles(AiTile tile)
	{	Collection<AiTile> result = new ArrayList<AiTile>();
	List<Direction> directions = Direction.getPrimaryValues();
		Iterator<Direction> d = directions.iterator();
		while(d.hasNext())
		{	Direction dir = d.next();
			AiTile neighbor = getNeighborTile(tile, dir);
			result.add(neighbor);
		}
		result = Collections.unmodifiableCollection(result);
		return result;
	}
	
	public AiTile getNeighborTile(AiTile tile, Direction direction)
	{	AiTile result = null;
		int c,col=tile.getCol();
		int l,line=tile.getLine();
		Direction p[] = direction.getPrimaries(); 
		//
		if(p[0]==Direction.LEFT)
			c = (col+zone.getWidth()-1)%zone.getWidth();
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%zone.getWidth();
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (line+zone.getHeight()-1)%zone.getHeight();
		else if(p[1]==Direction.DOWN)
			l = (line+1)%zone.getHeight();
		else
			l = line;
		//
		result = getTile(l,c);
		return result;
	}

	public AiTile getTile(int line, int col)
	{	
		return matrix[line][col];
	}
	
	public DangerManager getDangerManager() throws StopRequestException {
		checkInterruption();
		return dangerManager;
	}


	//renvoie la zone de jeu
	public AiZone getZone() throws StopRequestException{
		checkInterruption();//Appel Obligatoire
		return zone;
	}
}
