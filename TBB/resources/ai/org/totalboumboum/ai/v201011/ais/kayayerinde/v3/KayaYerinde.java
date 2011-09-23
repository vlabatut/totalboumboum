package org.totalboumboum.ai.v201011.ais.kayayerinde.v3;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings("unused")
public class KayaYerinde extends ArtificialIntelligence
{
	private Matrice m;
	private double[][] areaMatrix;
	private AiZone zone;
	private GetSafe s;

	@Override
	public AiAction processAction() throws StopRequestException
	{
		checkInterruption();
		zone=getPercepts();
		m=new Matrice(this);
		s=new GetSafe(this);
		AiAction result=new AiAction(AiActionName.NONE);
		int i,j;
		areaMatrix=m.getMatrice();
		/*for(i=0;i<zone.getHeight();i++)
		{
			checkInterruption();
			for(j=0;j<zone.getWidth();j++)
			{
				checkInterruption();
				System.out.print(areaMatrix[i][j]+" ");
			}
			System.out.print('\n');
		}
		System.out.print('\n');*/
		if(s.getRescueDirection(m)!=null)
			System.out.println(s.getRescueDirection(m).toString()+"\n");
		return result;
	}
	
}
