package tournament200809v2.medeniuluer;



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

public class MedeniUluer extends ArtificialIntelligence 
{
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la derni�re case par laquelle on est pass� */ 
	private AiTile previousTile = null;
	
	private int lastBombTime = 0;
	
	private boolean poseBombe = false;
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(ownHero!=null)
		{	putBomb();
			if (!poseBombe){
			// on met � jour la position de l'ia dans la zone
			currentTile = ownHero.getTile();
			
			// premier appel : on initialise l'IA
			if(nextTile == null)
				init();
			
			// arriv� � destination : on choisit une nouvelle destination
			if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contr�le manuel du personnage
			else if(previousTile!=currentTile)
			{	previousTile = currentTile;
				pickNextTile();			
			}
			// sinon (on garde la m�me direction) on v�rifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
			else
				checkNextTile();
			
			// on calcule la direction � prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			// on calcule l'action
			if(direction!=Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);
			}
			else
			{	
				lastBombTime = (int)zone.getElapsedTime();
				result = new AiAction (AiActionName.DROP_BOMB);
			}
			
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
		if(tiles.size()>0)
		{	// si la liste contient la case situ�e dans la direction d�placement pr�cedente,
			// on �vite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
			AiTile tempTile = null;
			Direction dir = getPercepts().getDirection(previousTile,currentTile);
			if(dir!=Direction.NONE)
			{	tempTile =  getPercepts().getNeighbourTile(currentTile, dir);
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
			// sinon (pas le choix) on continue dans la m�me direction
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
	
	private void putBomb() throws StopRequestException
	{
		checkInterruption();
		
		Collection <AiHero> heroes = this.getPercepts().getHeroes();
		AiHero rival = null;
		Iterator <AiHero> itHeroes = heroes.iterator();
		while (itHeroes.hasNext())
			{
				AiHero temp = itHeroes.next();
				if (!temp.equals(this.getPercepts().getOwnHero()))
					rival = temp;
			}
		if (this.getPercepts().getElapsedTime() - this.lastBombTime >= 5 && this.getPercepts().getOwnHero().getTile().getCol()-rival.getTile().getCol() <= 3 && this.getPercepts().getOwnHero().getTile().getLine()-rival.getTile().getLine()<=3)
		{
			poseBombe=true;
		}
		else
		{	
			poseBombe = false;
		}
	}
	
	//private void runAway() throws StopRequestException
	//{
		
		
	//}
			
	}