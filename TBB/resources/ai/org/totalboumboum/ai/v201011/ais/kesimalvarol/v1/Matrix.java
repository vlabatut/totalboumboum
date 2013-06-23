package org.totalboumboum.ai.v201011.ais.kesimalvarol.v1;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * Representation de la zone du jeu
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings("unused")
public class Matrix
{
	//Pour checkInterruption.
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) {
		Matrix.monIA = monIA;
	}
	
	//Objet representant les matrices.
	public int representation[][];
	public int height,width;

	public Matrix(AiZone azone) throws StopRequestException
	{
		monIA.checkInterruption();
		height=azone.getHeight();
		width=azone.getWidth();
		representation=new int[height][width];
	}
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	public void colorizeMapAccordingly() throws StopRequestException
	{
		monIA.checkInterruption();
		AiOutput out=monIA.getOutput();
		for(int i=0;i<this.height;i++)
		{
			for(int j=0;j<this.width;j++)
			{
				int interet=(int)representation[i][j];
				if(interet!=0) {
					out.setTileText(i, j, Integer.toString(interet));
					int colR=(-255*interet)/300,colG=(255*interet)/100;
					if (colR>255)
						colR=255;
					if (colR<0)
						colR=0;
					if (colG>255)
						colG=255;
					if (colG<0)
						colG=0;
					out.setTileColor(i, j, new Color(colR, colG, 0));
				}
			}
		}
	}
}
