package org.totalboumboum.ai.v201011.ais.kayayerinde.v2;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.ai.v201011.adapter.data.*;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings({ "unused", "deprecation" })
public class KayaYerinde extends ArtificialIntelligence{
	
	private Direction dir=Direction.NONE;
	private List<AiItem> items;
	private AiZone zone;
	private List<AiBlock> blocks;
	private List<AiHero> rivals;
	private List<AiBomb> bombes;
	private AiHero ownHero;
	private int x,y;
	private Astar astar;
	private AiTile loc;
	private double[][] matrix;
	
	public AiAction processAction() throws StopRequestException{
		checkInterruption();
		zone=getPercepts();
		ownHero=zone.getOwnHero();
		loc=ownHero.getTile();
		Matrice m=new Matrice(this);
		m.createMatrice();
		int i=0,j;
		/*List<AiBlock> blocks=m.getBlocks();
		Iterator<AiBlock> blockIt=blocks.iterator();
		AiBlock b;
		while(blockIt.hasNext())
		{
			b=blockIt.next();
			System.out.print(b.getTile().toString());
			i++;
		}
		System.out.print('\n');
		System.out.print(i);
		System.out.print('\n');
		System.out.print(ownHero.getTile().toString()+"\n");*/
		for(i=0;i<=14;i++)
		{
			for(j=0;j<=14;j++)
				System.out.print(m.getMatrice()[i][j]+" ");
			System.out.print('\n');
		}
		System.out.print('\n');
		AiAction action=new AiAction(AiActionName.NONE);
		/*GetSafe g=new GetSafe(this);
		if(g.destinationDanger(m)==Direction.NONE)
		{
			if(ownHero.getCol()<zone.getHeigh()/2 && ownHero.getLine()<zone.getWidth()/2)
				action=new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
			else if(ownHero.getCol()<zone.getHeigh()/2 && ownHero.getLine()>zone.getWidth()/2)
				action=new AiAction(AiActionName.MOVE, Direction.UPLEFT);
			else if(ownHero.getCol()>zone.getHeigh()/2 && ownHero.getLine()<zone.getWidth()/2)
				action=new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
			else
				action=new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		}
		else
		{
			if(g.destinationDanger(m)==Direction.UP)
				action=new AiAction(AiActionName.MOVE, Direction.DOWN);
			else if(g.destinationDanger(m)==Direction.DOWN)
				action=new AiAction(AiActionName.MOVE, Direction.UP);
			else if(g.destinationDanger(m)==Direction.RIGHT)
				action=new AiAction(AiActionName.MOVE, Direction.LEFT);
			else
				action=new AiAction(AiActionName.MOVE, Direction.RIGHT);
		}*/
		
		/*PathFinder p=new PathFinder(this);
		AiPath path=p.getPath();*/
		
		/*if(path.isEmpty() || path==null)
			action=new AiAction(AiActionName.MOVE, Direction.UP);
		AiTile tile = null;
		if(path.getLength()>1)
			tile = path.getTile(1);
		else if(path.getLength()>0)
			tile = path.getTile(0);
		if(tile!=null)
			dir = zone.getDirection(ownHero, tile);
		if(tile==ownHero.getTile())
			action=new AiAction(AiActionName.MOVE, Direction.DOWN);
		else
			action=new AiAction(AiActionName.MOVE, dir);*/
		
		/*if(loc.getPosX()<loc.getPosY())
			action=new AiAction(AiActionName.MOVE, Direction.UP);
		else if(loc.getPosX()>loc.getPosY())
			action=new AiAction(AiActionName.MOVE, Direction.DOWN);
		else
			action=new AiAction(AiActionName.MOVE, Direction.RIGHT);*/
		
		return action;
	}
		
	
	public AiZone getZone() throws StopRequestException
	{	checkInterruption();	
		return zone;
	}	

}
