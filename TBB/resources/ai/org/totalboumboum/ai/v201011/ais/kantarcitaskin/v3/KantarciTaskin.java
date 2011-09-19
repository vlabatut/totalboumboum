package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v3;


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
	/*
	 * La classe qui repond le moteur du jeu avec une action qui est calcule par IA
	 * 
	 * */
	AiActionName resultat = AiActionName.NONE;
	Direction direction = Direction.NONE;
	AiZone zone = this.getPercepts();

	
	/*
	 * Calcule l'action pour un instant donné
	 * @return restult
	 * 		action de l'hero dans cette iteration
	 * */
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
	
	
	/*
	 * Calcule l'action: soit se deplacer, soit poser une bombe. Avant tout elle determine
	 * la mode et puis d'apres la mode elle decide de poser une bombe ou pas. sinon on calcule
	 * la matrice et trouve un chemin pour se deplacer. si on se deplace la methode 
	 * calcule aussi le valeur de la variable direction.
	 * 
	 * @return resultat
	 * 		le nome d'action
	 * */
	public AiActionName matrix() throws StopRequestException
	{
		Move move;
		AiZone zone = this.getPercepts();
		double[][] matrix = new double[zone.getHeight()][zone.getWidth()];
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
			if(zone.getOwnHero().getBombNumberMax() <= 2)
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
			if(drop.decisionOfBomb() == true)
			{
				resultat = AiActionName.DROP_BOMB; 
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




