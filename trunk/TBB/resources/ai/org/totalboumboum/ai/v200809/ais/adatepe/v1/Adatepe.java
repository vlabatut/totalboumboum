package org.totalboumboum.ai.v200809.ais.adatepe.v1;

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
 * @author Can Adatepe
 *
 */
public class Adatepe extends ArtificialIntelligence 
{
	
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
//	/** la dernière case par laquelle on est passé */ 
//	private AiTile previousTile = null;
	/** Representation de la
	 *  table du jeu : 0 = Empty, 1 = Wall, 
	 *  2 = Bomb, 3 = Yellow Zone, 4 = Red Zone */
	private int[][] boardTiles; 
	private Collection <AiBomb> bomb;
	private Collection <AiBlock> wall;
	private Collection <AiFire> fire;
	@SuppressWarnings("unused")
	private Collection <AiHero> hero;
	private Collection <AiZone> zone;
	/** Decide si on doit mettre un bombe ou pas */
	boolean dropbomb = false;
	/** Decide si on est en danger ou pas */
	boolean wearesafe = false;
	/** Decide si la direction suivante qu'on va aller est en danger ou pas */
	boolean nexttileissafe = false;
	/** La direction qu'on veut aller */
	Direction direction;
	
	
	public AiAction processAction() throws StopRequestException
{	
	checkInterruption();
	AiZone zone = getPercepts();
	AiHero ownHero = zone.getOwnHero();
	AiAction result = new AiAction(AiActionName.NONE);
	
	if(ownHero!=null)
		{
		if (!(dropbomb))
	{	
		currentTile = ownHero.getTile();
		boarddraw();
		drawdangerzones();
		if(nextTile == null)
			nextTile = currentTile;
		// arriv� à destination : on choisit une nouvelle destination
		if(currentTile==nextTile)
		{
			checksafety();
			if (wearesafe)
			{
			aggro();	
			}
			else
			{
			evade();
			}
			checknexttilesafe();
			if (!nexttileissafe)
			{
				result = new AiAction (AiActionName.NONE);
			}
			else
			{
				result = new AiAction (AiActionName.MOVE,direction);
			}
		}
	}
		else
		{
			result = new AiAction(AiActionName.DROP_BOMB);
		}
	}
	
	return result;
}
	
	/** Mets les blocs et les zones wides a la table*/
	private void boarddraw() throws StopRequestException
	{	
		checkInterruption(); 
		AiZone zone = getPercepts();
		int xMax = zone.getWidth();
		int yMax = zone.getHeight();
		int i,j;
		for(i = 0; i < xMax; i++)
		{
			for(j = 0; j < yMax; j++)
			{
				boardTiles[i][j] = 0;
			}
		}
		this.wall = ((AiZone) zone).getBlocks();
		Iterator <AiBlock> itBlocs = wall.iterator();
		while(itBlocs.hasNext())
		{
			AiBlock temp = itBlocs.next();
			boardTiles[temp.getCol()][temp.getLine()] = 1;
		}
	}
		
	
	
	/** Mets les bombes, les zones dangereux a traverser et les feus a la table*/
	private void drawdangerzones() throws StopRequestException
	{	
		checkInterruption(); 
		this.bomb = ((AiZone) zone).getBombs();
		Iterator <AiBomb> itBombes = bomb.iterator();
		while(itBombes.hasNext())
		{
			AiBomb temp = itBombes.next();
			boardTiles[temp.getCol()][temp.getLine()] = 2;
			int y1 = temp.getCol();
			int x1 = temp.getLine();
			while (boardTiles[x1+1][y1] != 1 && (x1 < (temp.getLine() + temp.getRange())))
			{
				boardTiles[x1+1][y1] = 2;
				x1 = x1 + 1;
			}
			while (boardTiles[x1-1][y1] != 1 && (x1 < (temp.getLine() - temp.getRange())))
			{
				boardTiles[x1-1][y1] = 2;
				x1 = x1 - 1;
			}
			while (boardTiles[x1][y1+1] != 1 && y1 < (temp.getCol() + temp.getRange()))
			{
				boardTiles[x1][y1+1] = 2;
				y1 = y1 + 1;
			}
			while (boardTiles[x1][y1+1] != 1 && y1 < (temp.getCol() - temp.getRange()))
			{
				boardTiles[x1][y1-1] = 2;
				y1 = y1 - 1;
			}
		}
		this.fire = ((AiZone) zone).getFires();
		Iterator <AiFire> itFire = fire.iterator();
		while(itFire.hasNext())
		{
			AiFire temp = itFire.next();
			boardTiles[temp.getCol()][temp.getLine()] = 4;
		}
	}
	
	
	/** Decide la destination suivante est dangereux ou pas */
	private void checknexttilesafe() throws StopRequestException
	{	checkInterruption(); 
	int x3 = currentTile.getLine();
	int y3 = currentTile.getCol();
	nexttileissafe = false;
	if (direction == Direction.LEFT)
	{
		if (boardTiles[x3-1][y3] != 4)
			nexttileissafe = true;
	}
	if (direction == Direction.RIGHT)
	{
		if (boardTiles[x3+1][y3] != 4)
			nexttileissafe = true;
	}
	if (direction == Direction.UP)
	{
		if (boardTiles[x3][y3-1] != 4)
			nexttileissafe = true;
	}
	if (direction == Direction.DOWN)
	{
		if (boardTiles[x3-1][y3+1] != 4)
			nexttileissafe = true;
	}
		
	}
	/** Decide les coordonnes qu'on trouve est dangereux ou pas */
	private void checksafety() throws StopRequestException
	{	checkInterruption(); 
	
		int x2 = currentTile.getLine();
		int y2 = currentTile.getCol();
		if (boardTiles[x2][y2] != 3 && boardTiles[x2+1][y2] !=3 && 
			boardTiles[x2+2][y2] !=3 && boardTiles[x2-1][y2] !=3 &&
			boardTiles[x2-2][y2] !=3 && boardTiles[x2][y2+1] !=3 &&
			boardTiles[x2][y2+2] !=3 && boardTiles[x2][y2-1] != 3 && 
			boardTiles[x2+1][y2-2] !=3)
		{
			wearesafe = true;
		}
		else
		{
			wearesafe = false;
		}
		
	}
	/** Esseye d'evider les bombes */
	private void evade() throws StopRequestException
	{	checkInterruption(); 
	
	int x4 = currentTile.getLine();
	int y4 = currentTile.getCol();
	while (boardTiles[x4][y4] == 3)
	{
		if (boardTiles[x4+1][y4] == 3)
			x4 = x4 + 1;
		if (boardTiles[x4-1][y4] == 3)
			x4 = x4 - 1;
		if (boardTiles[x4][y4+1] == 3)
			x4 = y4 - 1;
		if (boardTiles[x4][y4-1] == 3)
			x4 = y4 - 1;
		else
		{
			double p = Math.random();
			double r = Math.random();
			@SuppressWarnings("unused")
			int rand = (int)p;
			@SuppressWarnings("unused")
			int rand2 = (int)r;
			if (p==1)
			{if (r==1)
			x4 = x4 +1;
			else
				x4 = x4 -1;
			}
			else
			{if (r==1)
				y4 = y4 +1;
			else
				y4 = y4 - 1;
			}
		}
	if (x4 > currentTile.getLine())
		direction = Direction.RIGHT;
	if (x4 < currentTile.getLine())
		direction = Direction.LEFT;
	if (y4 > currentTile.getLine())
		direction = Direction.DOWN;
	if (y4 < currentTile.getLine())
		direction = Direction.UP;
	}	
	}
	/** Esseye de tuer les autres joueurs */
	private void aggro() throws StopRequestException
	{	checkInterruption(); 
	
	dropbomb = true;
	
		
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
	
	@SuppressWarnings("unused")
	private List<AiTile> getClearNeighbor(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbor= getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbor.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isClear(t))
				result.add(t);
		}
		return result;
	}
}
