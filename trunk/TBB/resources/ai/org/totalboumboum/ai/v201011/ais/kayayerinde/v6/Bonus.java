package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings("deprecation")
public class Bonus {
	private KayaYerinde onder;
	private Matris matris;
	private Astar astar;
	private AiTile gittigim;
	private AiTile incelenecek;
	private boolean running;
	private boolean yaz;
	
	/**
	 * 
	 * @param onder
	 * @throws StopRequestException
	 */
	public Bonus(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		astar=new Astar(onder);
		incelenecek=onder.getPercepts().getTile(0, 0);
		running=false;
	}

	/**
	 * 
	 * @param matris
	 * @throws StopRequestException
	 */
	public void matrisAyarla(Matris matris) throws StopRequestException
	{
		onder.checkInterruption();
		this.matris=matris;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiAction getNextAction() throws StopRequestException
	{
		onder.checkInterruption();
		AiAction result=new AiAction(AiActionName.NONE);
		if(running)
		{
			AiTile own=onder.getOwnHero().getTile();
			if(!incelenecek.equals(own))
			{
				incelenecek=own;
				if(incele())
				{
					if(yaz)
						System.out.println("bonusta bomba bıraktı "+onder.getOwnHero().getTile());
					return new AiAction(AiActionName.DROP_BOMB);
				}
			}
			if(astar.yoldaMi()==1)
				return astar.getNextAction(matris);
			running=false;
		}
		if(!running )
		{
			ArrayList<AiTile> bonus=matris.getBonus();
			for(AiTile t:bonus)
			{
				onder.checkInterruption();
				if(astar.yolBul(onder.getOwnHero().getTile(), t,Matris.BONUS, matris))
				{
					if(astar.yoldaMi()==1)
					{
						result=astar.getNextAction(matris);
						gittigim=t;
						running=true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	private boolean incele() throws StopRequestException
	{
		onder.checkInterruption();
		matris.hypoteticalBomb(onder.getOwnHero().getTile());
		//if(yaz)	
			//System.out.println("vurduğum bonus sayısı: "+matris.getHypoBonus());
		if(matris.getHypoBonus()==0 && matris.getHypoDuvar()>0)
		{
			for(AiHero h:onder.getPercepts().getRemainingHeroes())
			{
				onder.checkInterruption();
				if(!h.equals(onder.getOwnHero()) && !h.getTile().equals(onder.getOwnHero().getTile()))
				{
					ArrayList<AiTile> neighbors=(ArrayList<AiTile>)h.getTile().getNeighbors();
					for(AiTile t:neighbors)
					{
						onder.checkInterruption();
						matris.hypoteticalBomb(t);
					}
				}
			}
			AiPath path=astar.getPath();
			int tur=astar.getTur();
			if(astar.yolBul(onder.getOwnHero().getTile(), gittigim,-1, matris))
				return true;
			astar.setPath(path);
			astar.setTur(tur);
		}
		matris.Undo();
		return false;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	public void run() throws StopRequestException
	{
		onder.checkInterruption();
		running=true;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isRunning() throws StopRequestException
	{
		onder.checkInterruption();
		return running;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public Astar getAstar() throws StopRequestException
	{
		onder.checkInterruption();
		return astar;
	}
}
