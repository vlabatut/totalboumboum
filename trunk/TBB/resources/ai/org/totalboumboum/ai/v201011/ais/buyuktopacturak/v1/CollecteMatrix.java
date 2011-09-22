package org.totalboumboum.ai.v201011.ais.buyuktopacturak.v1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1.State;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Onur Büyüktopaç
 * @author Yiğit Turak
 */
@SuppressWarnings("unused")
public class CollecteMatrix
{	
	private BuyuktopacTurak ia;
	private AiZone zone;
	private int col,line;
	private double[][] collecteMatrix;
	private AiHero ownHero;
	private List<AiTile> bonus;
	
	public CollecteMatrix(BuyuktopacTurak ia) throws StopRequestException
	{	// avant tout : test d'interruption
		ia.checkInterruption();
		this.zone=ia.getPercepts();
		this.ia = ia;
		ownHero = zone.getOwnHero();
		collecteMatrix = new double[zone.getHeight()][zone.getWidth()];
//		ia.getPercepts().update(100);
		bonus = new ArrayList<AiTile>();
		updateCollecteMatrice();
	}
	
	public double[][] getMatrix() throws StopRequestException
	{	ia.checkInterruption(); //APPEL OBLIGATOIRE
		return collecteMatrix;		
	}
	private void updateCollecteMatrice() throws StopRequestException
	{
		ia.checkInterruption();
		for (line = 0; line < zone.getHeight(); line++) 
		{
			ia.checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) 
			{
				ia.checkInterruption();
				collecteMatrix[line][col]=State.FREE;
			}
	   	}
		
		
		AiOutput output = ia.getOutput();
		for (line = 0; line < zone.getHeight(); line++) 
		{
			ia.checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) 
			{
				ia.checkInterruption();
				//c'est pour tester d'afficher de la matrice 
				Color color = null;
				collecteMatrix[line][col] = (int) (Math.random()*5);
				if(collecteMatrix[line][col]<=1)
					color=Color.red;
				else
					color=Color.blue;
				output.setTileColor(line,col,color);
			}
	   	}
	}
	/*
	private void getbonus()throws StopRequestException
	{
		ia.checkInterruption();
		List<AiItem> items = zone.getItems(); //bonus
		Iterator<AiItem> itemit = items.iterator();
		AiItem item;
		while (itemit.hasNext()) 
		{
			ia.checkInterruption();
			item = itemit.next();
			line = item.getLine();
			col = item.getCol();

			if(item.getType()==AiItemType.MALUS)
				collecteMatrix[line][col]=State.MALUS;
			else
			{
				bonus.add(item.getTile());
				collecteMatrix[line][col]=State.BONUS;
			}
		}
	}*/
	
	public int bExtraFlame;
	public int bExtraBomb;
	public int rival;
	public int murDest;
	
	private double calculMatrixDistance(AiTile me, AiTile obs, int constant){
		double distance;
		double result = 0;
		distance=Math.abs(me.getCol()-obs.getCol())+Math.abs(me.getLine()-obs.getLine());
		result = constant/distance;
		return result;
	}
	
	/*
	 * private double calculBombImpact(){
	 *
	 *}
	 *
	 *
	*/
	
	public AiAction gagneRound() throws StopRequestException
	{	// avant tout : test d'interruption
		ia.checkInterruption();
		// traitement qui fait gagner le round
		AiAction result = null;
		return result;
	}
}
