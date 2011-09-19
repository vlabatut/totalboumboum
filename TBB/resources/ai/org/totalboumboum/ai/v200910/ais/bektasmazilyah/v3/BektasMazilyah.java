package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v3;

import java.util.LinkedList;
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
 * @version 3
 * 
 * @author Erdem Bektas
 * @author Nedim Mazilyah
 *
 */
public class BektasMazilyah extends ArtificialIntelligence
{	
	private AiZone zone;

	// le personnage dirig� par cette IA
	private AiHero hero;


	//les adversaires
	@SuppressWarnings("unused")
	private Vector<AiHero> others;
	//la prochaine action que l'IA veut réaliser
	private AiAction action;
	private AiTile targetDeplacement;
	@SuppressWarnings("unused")
	private AiTile previousTile;
	private AiTile currentTile;
	@SuppressWarnings("unused")
	private AiTile nextTile;
	@SuppressWarnings("unused")
	private LinkedList <AiTile> path;
	boolean isDanger = false;
	@SuppressWarnings("unused")
	private boolean initial=false;
	@SuppressWarnings("unused")
	private AiTile matrix[][];
	private long time;
	private DangerZone dangerZone;
	private EscapeManager escapeManager;
	

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		//Appel Obligatoire
		checkInterruption();
		
		if(hero==null)
			initMonAi();
		dangerZone= new DangerZone(zone,this);
		//System.out.println("zone yarat�ld�");
		
/*		for(int i = 0; i<zone.getWidth() ; i++)
		{	
			checkInterruption(); //Appel Obligatoire
			for(int j = 0; j < zone.getHeigh(); j++)
			{
				checkInterruption(); //Appel Obligatoire
				
				System.out.print(i+":"+j+":"+ dangerZone.getValeur(i,j)+"\n");
			}			
		
			System.out.println();
		}
*/
		
		if(hero != null)
		{
			Direction moveDir = Direction.NONE;
			System.out.println("tehlikeye bak�lacak");
			checkDanger();
			System.out.println("tehlikeye bak�ld�");
			if(isDanger)
			{//Il y a un danger, il faut se sauver.
				//System.out.println("danger var");
				if(escapeManager!=null)
				{	if(escapeManager.hasArrived())
						escapeManager = null;
					else
						moveDir = escapeManager.update();
				}
				
				action = new AiAction(AiActionName.MOVE,moveDir);
			}
			
		}		
		//AiAction result = new AiAction(AiActionName.NONE);
		return action;
	}
	
/*

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
			
			// arriv� à destination : on choisit une nouvelle destination
			if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contr�le manuel du personnage
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

*/

	public AiTile getCurrentTile() throws StopRequestException {
		checkInterruption();
		return currentTile;
	}


	private void initMonAi() throws StopRequestException{
		//APPEL OBLIGATOIRE
		checkInterruption();
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
			
			System.out.println("initialise edildi");
		}
		
	
	void checkDanger() throws StopRequestException{
		checkInterruption(); //Appel obligatoire
		
		if(isClear(currentTile.getCol(),currentTile.getLine()) )//|| isClear(nextTile))
		{
			isDanger = false;
		}		
		else
			isDanger = true;
	}

	
	public boolean isClear(int x,int y) throws StopRequestException {
		
		EtatEnum etat= dangerZone.getValeur(x, y);
		checkInterruption();
		return (etat==EtatEnum.LIBRE || etat==EtatEnum.BONUSBOMBE || etat==EtatEnum.BONUSFEU || etat==EtatEnum.HERO);
		
	}
	

	/*
	private void pickNextTile() throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases voisines accessibles	
		ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
		// on sort de la liste la case d'o� l'on vient (pour �viter de repasser au même endroit)
		boolean canGoBack = false;
		if(tiles.contains(previousTile))
		{	tiles.remove(previousTile);
			canGoBack = true;
		}
		// s'il reste des cases dans la liste
		if(tiles.size()>0)
		{	// si la liste contient la case situ�e dans la direction déplacement pr�cedente,
			// on �vite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
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
		// sinon (pas le choix) on tente de revenir en arri�re
		else
		{	if(canGoBack)
			{	nextTile = previousTile;
				previousTile = currentTile;
			}
			// et sinon on ne peut pas bouger, donc on ne fait rien du tout
		}
		
		
	}
	*/
	
	/*
	private ArrayList<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getNeighborTiles(tile);
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
	}*/
	/*
	public Collection<AiTile> getNeighborTiles(AiTile tile)
	{	Collection<AiTile> result = new ArrayList<AiTile>();
		ArrayList<Direction> directions = Direction.getPrimaryValues();
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
			l = (line+zone.getHeigh()-1)%zone.getHeigh();
		else if(p[1]==Direction.DOWN)
			l = (line+1)%zone.getHeigh();
		else
			l = line;
		//
		result = getTile(l,c);
		return result;
	}

	public AiTile getTile(int line, int col)
	{	
		return matrix[line][col];
	}*/
	
	public DangerZone getDangerZone() throws StopRequestException {
		checkInterruption();
		return dangerZone;
	}


	//renvoie la zone de jeu
	public AiZone getZone() throws StopRequestException{
		checkInterruption();//Appel Obligatoire
		return zone;
	}
	
	public long getTime() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		return time;
	}
	
	public AiHero getOwnHero() {
		return hero;
	}




}
