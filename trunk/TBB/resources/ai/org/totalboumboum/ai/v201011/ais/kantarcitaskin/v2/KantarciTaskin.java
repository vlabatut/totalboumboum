package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v2;


import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.*;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Kantarci Taskin
 *
 */
public class KantarciTaskin extends ArtificialIntelligence
{	
	AiActionName resultat = AiActionName.NONE;
	Direction direction;
	AiZone zone = this.getPercepts();

	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption();
		AiActionName actionName;
		AiAction result;
		actionName= this.matrix();
		if( (actionName) == AiActionName.DROP_BOMB)
			result  = new AiAction(actionName);
		else
			result  = new AiAction(actionName, direction);
		return result;
	}
	
	public AiActionName matrix() throws StopRequestException
	{
		Move move;
		AiZone zone = this.getPercepts();
		int[][] matrix = new int[zone.getHeight()][zone.getWidth()];
		int mode=1;
		List<AiItem> bonus; 
		bonus = new ArrayList<AiItem>();
		bonus = zone.getItems();
		List<AiBlock> blocks = zone.getBlocks();
		List<AiBomb> bombs = zone.getBombs();
		List<AiHero> heroes = zone.getHeroes();
		List<AiFire> fires =zone.getFires();
		bonus.removeAll(bombs);
		bonus.removeAll(blocks);
		bonus.removeAll(heroes);
		bonus.removeAll(fires);
		int hidden = zone.getHiddenItemsCount();
		Iterator<AiBlock> walls = blocks.iterator();
		AiBlock wall;
		int nbwall=0;
		
		//On determine le mode//
		while(walls.hasNext())
		{
			wall=walls.next();
			if(wall.isDestructible())
				nbwall = nbwall + 1;
		}
		if(!bonus.isEmpty())
		{
			if(zone.getOwnHero().getBombNumberMax()<=2)
				mode = 1;
			if(zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() == 1)
				mode = 1;
			else
				mode = 0;
		}
		else if(hidden != 0)
		{
			if(zone.getOwnHero().getBombNumberCurrent() <= 2)
			{
				if((100*hidden/nbwall) > 50)
					mode = 1;
			}
			else
			if(zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() == 1)
				mode = 1;
			else
				mode = 0;	
		}
		else
		{
			mode = 0;
		}
		
		//On determine si on drop une bombe ou bien si on se deplace//
		DropBomb drop; 
			drop = new DropBomb(zone,mode,this);
			if(drop.decisionOfBomb() == true && resultat != AiActionName.DROP_BOMB)
			{	resultat = AiActionName.DROP_BOMB; 
			}
			else
			{
				MatrixCalculator collect = new MatrixCalculator(zone,mode,this);
				matrix = collect.matrixConstruction();
				move = new Move (zone, matrix,this);
				direction = move.getDirection();
				resultat = AiActionName.MOVE;	
			}
	
			
		//On fait l'affichage de matrice d'interet	
		for(int i=0; i<zone.getHeight(); i++)
		{
			for(int j=0; j<zone.getWidth(); j++)
			{
				checkInterruption();
				org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = this.getOutput();
				output.setTileText(i, j, ""+matrix[i][j]);			
			}
		}	
		return resultat;
	}	
	
	
	
}



