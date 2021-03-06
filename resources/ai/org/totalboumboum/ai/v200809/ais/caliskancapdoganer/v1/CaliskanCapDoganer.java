package org.totalboumboum.ai.v200809.ais.caliskancapdoganer.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
 * @author Anıl Çalışkan
 * @author Fahrı Cap
 * @author Sedat Doğaner
 *
 */
@SuppressWarnings("deprecation")
public class CaliskanCapDoganer extends ArtificialIntelligence
{
	/*
	public AiAction processAction() throws StopRequestException
	{	AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}*/
	/*private boolean secondMoveAfterBomb;*/
	/** la case occupée actuellement par le personnage*/
	private AiTile bombedTile = null;
	/** */
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;
	/** */
	private static int i=1;

	@Override
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);

		if(i % 150 == 0) {
			i++;
			bombedTile = ownHero.getTile();
			result = new AiAction(AiActionName.DROP_BOMB);
		}
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		else if(ownHero != null)
		{	// on met à jour la position de l'ia dans la zone
			i++;
			currentTile = ownHero.getTile();
			
			// premier appel : on initialise l'IA
			if(nextTile == null)
				init();
			
			// arrivé à destination : on choisit une nouvelle destination
			if(currentTile == nextTile)
			{	try
				{	pickNextTile();
				}
				catch(StackOverflowError e)
				{	//
				}
			
			}
			// au cas ou quelqu'un prendrait le Contrôle manuel du personnage
			else if(previousTile != currentTile)
			{	previousTile = currentTile;
				try
				{	pickNextTile();
				}
				catch(StackOverflowError e)
				{	//
				}			
			}
			// sinon (on garde la même direction) on vérifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
			else
				checkNextTile();
			
			// on calcule la direction à prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			// on calcule l'action
			if(direction != Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);
		}
		return result;
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		nextTile = currentTile;		
		previousTile = currentTile;		
	}
	
	/**
	 * Choisit comme destination une case voisine de la case actuellement occupée par l'IA.
	 * Cette case doit être accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * être différente de la case précédemment occupée
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
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
		if(tiles.size() > 0)
		{	// si la liste contient la case située dans la direction déplacement précedente,
			// on évite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
			AiTile tempTile = null;
			Direction dir = getPercepts().getDirection(previousTile,currentTile);
			if(dir != Direction.NONE)
			{	tempTile = getPercepts().getNeighborTile(currentTile, dir);
				if(tiles.contains(tempTile))
					tiles.remove(tempTile);
			}
			// s'il reste des cases dans la liste
			if(tiles.size() > 0)
			{	// on en tire une au hasard
				double p = Math.random()*tiles.size();
				int index = (int)p;
				nextTile = tiles.get(index);
								
				if (!CheckAvailability())
				{	pickNextTile();
				}
						
				previousTile = currentTile;
			}
			// sinon (pas le choix) on continue dans la même direction
			else
			{	nextTile = tempTile;
				if (!CheckAvailability())
                {	pickNextTile();
                }
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
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block==null && bombs.size()==0 && fires.size()==0;
		return result;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private boolean CheckAvailability() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		int previousTileCol = previousTile.getCol();
		int previousTileLine = previousTile.getLine();
		int nextTileCol = nextTile.getCol();
		int nextTileLine = nextTile.getLine();
		if(bombedTile != null)
		{	int bombedTileCol = bombedTile.getCol();
			int bombedTileLine = bombedTile.getLine();
			
			if ((nextTileCol > bombedTileCol && bombedTileCol > previousTileCol) ||
				(nextTileCol < bombedTileCol && bombedTileCol < previousTileCol) ||
				(nextTileLine > bombedTileLine && bombedTileLine > previousTileLine) ||
				(nextTileLine < bombedTileLine && bombedTileLine < previousTileLine) ||
				(nextTileCol == bombedTileCol) ||
				(nextTileLine == bombedTileLine))
				return false;
		}
		return true;
	}
}
