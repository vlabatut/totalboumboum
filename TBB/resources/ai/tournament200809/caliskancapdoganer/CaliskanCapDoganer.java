package tournament200809.caliskancapdoganer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.AiBlock;
import fr.free.totalboumboum.ai.adapter200809.AiBomb;
import fr.free.totalboumboum.ai.adapter200809.AiFire;
import fr.free.totalboumboum.ai.adapter200809.AiHero;
import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.AiZone;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class CaliskanCapDoganer extends ArtificialIntelligence
{
	/*
	public AiAction processAction() throws StopRequestException
	{	AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}*/
	/*private boolean secondMoveAfterBomb;*/
	/** la case occup�e actuellement par le personnage*/
	private AiTile bombedTile = null;
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la derni�re case par laquelle on est pass� */ 
	private AiTile previousTile = null;
	private static int i=1;
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
		{	// on met � jour la position de l'ia dans la zone
			i++;
			currentTile = ownHero.getTile();
			
			// premier appel : on initialise l'IA
			if(nextTile == null)
				init();
			
			// arriv� � destination : on choisit une nouvelle destination
			if(currentTile == nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contr�le manuel du personnage
			else if(previousTile != currentTile)
			{	previousTile = currentTile;
				pickNextTile();			
			}
			// sinon (on garde la m�me direction) on v�rifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
			else
				checkNextTile();
			
			// on calcule la direction � prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			// on calcule l'action
			if(direction != Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);
		}
		return result;
	}

	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		nextTile = currentTile;		
		previousTile = currentTile;		
	}
	
	/**
	 * Choisit comme destination une case voisine de la case actuellement occup�e par l'IA.
	 * Cette case doit �tre accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * �tre diff�rente de la case pr�c�demment occup�e
	 * @throws StopRequestException 
	 */
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases voisines accessibles	
		ArrayList<AiTile> tiles = getClearNeighbours(currentTile);
		
		// on sort de la liste la case d'o� l'on vient (pour �viter de repasser au m�me endroit)
		boolean canGoBack = false;
		if(tiles.contains(previousTile))
		{	tiles.remove(previousTile);
			canGoBack = true;
		}
		// s'il reste des cases dans la liste
		if(tiles.size() > 0)
		{	// si la liste contient la case situ�e dans la direction d�placement pr�cedente,
			// on �vite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
			AiTile tempTile = null;
			Direction dir = getPercepts().getDirection(previousTile,currentTile);
			if(dir != Direction.NONE)
			{	tempTile = getPercepts().getNeighbourTile(currentTile, dir);
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
					pickNextTile();
						
				previousTile = currentTile;
			}
			// sinon (pas le choix) on continue dans la m�me direction
			else
			{	nextTile = tempTile;
				if (!CheckAvailability())
                {
                    try{
					    pickNextTile();
                    }catch(Exception e){
                       // e.printStackTrace();      //on ne montre pas d'erreurs
                    }
                }
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
	
	private ArrayList<AiTile> getClearNeighbours(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de r�f�rence
		Collection<AiTile> neighbours = getPercepts().getNeighbourTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbours.iterator();
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
			ArrayList<AiTile> tiles = getClearNeighbours(currentTile);
			// on sort l'ancienne destination (qui est maintenant bloqu�e) de la liste
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
	
	private boolean CheckAvailability() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		int previousTileCol = previousTile.getCol();
		int previousTileLine = previousTile.getLine();
		int nextTileCol = nextTile.getCol();
		int nextTileLine = nextTile.getLine();
		int bombedTileCol = bombedTile.getCol();
		int bombedTileLine = bombedTile.getLine();
		
		if ((nextTileCol > bombedTileCol && bombedTileCol > previousTileCol) ||
			(nextTileCol < bombedTileCol && bombedTileCol < previousTileCol) ||
			(nextTileLine > bombedTileLine && bombedTileLine > previousTileLine) ||
			(nextTileLine < bombedTileLine && bombedTileLine < previousTileLine) ||
			(nextTileCol == bombedTileCol) ||
			(nextTileLine == bombedTileLine))
			return false;
		
		return true;
	}
}
