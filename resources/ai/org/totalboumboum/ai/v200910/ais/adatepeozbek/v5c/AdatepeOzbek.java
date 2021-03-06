package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.*;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * 
 * @version 5.c
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
public class AdatepeOzbek extends ArtificialIntelligence 
{	
	/** */
	private AiHero ownHero = null;
	/** */
	private AiZone zone = null;
	/** */
	private AiAction actionToDo = null;
	/** */
	protected Astar astar = null;
	/** */
	private AiPath path = null;
	/** */
	private List<AiTile> allPassedTiles = null;
	/** */
	private boolean debug = false;
	/** */
	public boolean canTheyReachMe = false;
	/** */
	public TriedTiles triedTiles = null;
	/** */
	public List<Enemy> enemies = null;
	/** */
	public int urgentBombs = 0 ;
	/** */
	public AiTile lastBombedTile = null;
	/** */
	public long idleTime = 0;
	/** */
	public int privateRang = 0;
	/** */
	public static int aStarQueueSize = 0;
	/** */
	protected double costArray[][];
	
	@Override
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		if(ownHero==null)
			init();
		
		if(triedTiles == null)
			triedTiles = new TriedTiles(this);
		
		if(enemies == null)
		{
			enemies = new ArrayList<Enemy>();
			List<AiHero> remaining = zone.getRemainingHeroes();
			for(int i = 0; i<remaining.size();i++)
			{	checkInterruption();
				if(remaining.get(i) == zone.getOwnHero())
					continue;
				enemies.add(new Enemy(zone.getHeroes().get(i), EnemyTypes.UNKNOWN,this));
			}
		}
		
		if(!ownHero.hasEnded())
		{
			
			// Ajoute toutes les cases visités dans le liste
			if(allPassedTiles.size() > 0)
			{
				if(allPassedTiles.get(allPassedTiles.size()-1) != ownHero.getTile())
					allPassedTiles.add(ownHero.getTile());
			}
			
			actionToDo = new AiAction(AiActionName.NONE);
			
			AiEscape EscapeAI = new AiEscape(this);
			
			if(debug)
				EscapeAI.printTiles();
			
			
		}
		
		return actionToDo;
	}
	
	/**
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private void init() throws StopRequestException
	{	zone = getPercepts();
		ownHero = zone.getOwnHero();
		allPassedTiles = new ArrayList<AiTile>();
		
		costArray = new double[zone.getHeight()][zone.getWidth()];
		for(int i=0;i<zone.getHeight();i++)
			for(int j=0;j<zone.getWidth();j++)
				costArray[i][j] = 1;
		astar = new Astar(this,ownHero, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
		astar.setMaxNodes(50);
	}
	
	/**
	 * Test : maintains the maximal frange size.
	 * 
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public void updateAstarQueueSize() throws StopRequestException
	{	int newSize = astar.getQueueMaxSize();
		if(newSize>aStarQueueSize)
		{	aStarQueueSize = newSize;
//			System.out.println(">>>>>>> AdatepeOzbek: "+aStarQueueSize+"("+zone.getHeight()+"x"+zone.getWidth()+")");
		}
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiAction getActionToDo() throws StopRequestException
	{
		checkInterruption();
		
		return actionToDo;
	}
	
	/**
	 * 
	 * @param action
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setActionToDo(AiAction action) throws StopRequestException
	{
		checkInterruption();
		
		actionToDo = action;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiHero getOwnHero() throws StopRequestException
	{
		checkInterruption();
		return ownHero;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiZone getZone() throws StopRequestException
	{
		checkInterruption();
		return zone;
	}	
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiPath getPath() throws StopRequestException
	{
		checkInterruption();
		return path;
	}
	
	/**
	 * 
	 * @param path
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setPath(AiPath path) throws StopRequestException
	{
		checkInterruption();
		this.path = path;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> getPassedTiles() throws StopRequestException
	{
		checkInterruption();
		return allPassedTiles;
	}
}
