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
public class Rakip {
	/** */
	private KayaYerinde onder;
	/** */
	private Matris matris;
	/** */
	private Astar astar;
	/** */
	private boolean kaciyor;
	/** */
//	private boolean yaz;
	
	/** */
	int i=0;
	
	/**
	 * 
	 * @param onder
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public Rakip(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		astar=new Astar(onder);
	}
	
	/**
	 * 
	 * @param matris
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public void matrisAyarla(Matris matris) throws StopRequestException
	{
		onder.checkInterruption();
		this.matris=matris;
	}
	
	/**
	 * 
	 * @return
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public AiAction getNextAction() throws StopRequestException
	{
		onder.checkInterruption();
		AiAction result=new AiAction(AiActionName.NONE);

		ArrayList<AiTile> rivals=matris.getRivalInf();
		
		if(rivals.contains(onder.getOwnHero().getTile()))
			if(incele())
			{
//				if(yaz)
//					System.out.println("rakipte bomba bıraktı "+onder.getOwnHero().getTile());
				if(onder.getOwnHero().getTile().getBombs().size()==0)
					return new AiAction(AiActionName.DROP_BOMB);
			}
		
		if(kaciyor)
		{
			if(astar.yoldaMi()==1)
				return astar.getNextAction(matris);
			if(astar.yoldaMi()==-1)
				return result;
			kaciyor=false;
		}
		
		for(AiTile r:rivals)
		{
			onder.checkInterruption();
			if(astar.yolBul(onder.getOwnHero().getTile(), r,-1, matris))
			{
				if(astar.yoldaMi()==1)
				{
					//System.err.println("yol buldı:"+astar.getPath().toString());
					result=astar.getNextAction(matris);
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private boolean incele() throws StopRequestException
	{
		onder.checkInterruption();
		matris.hypoteticalBomb(onder.getOwnHero().getTile());
		//if(yaz)
			//System.err.println("vurduğum rakip sayısı: "+matris.getHypoRivals());
		if(matris.getHypoRivals()>0)
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
			ArrayList<AiTile> safes=matris.getBonus();
			safes.addAll(matris.getFrees());
			AiPath path=astar.getPath();
			int tur=astar.getTur();
			if(!safes.isEmpty())
			{
				for(int i=0;i<safes.size();i++)
				{
					onder.checkInterruption();
					if(astar.yolBul(onder.getOwnHero().getTile(), safes.get(i),-1, matris))
					{
						boolean var=false;
						for(AiHero h:onder.getPercepts().getRemainingHeroes())
						{
							onder.checkInterruption();
							if(!h.equals(onder.getOwnHero()) && !h.getTile().equals(onder.getOwnHero().getTile()))
							{
								Astar a=new Astar(onder);
								if(a.yolBul(h.getTile(), safes.get(i),-1, matris))
								{
									if(a.getPath().getDuration(h)-100<astar.getPath().getDuration(onder.getOwnHero()))
									{
										var=true;
										break;
									}
								}
							}
						}
						if(!var)
						{
							kaciyor=true;
							return true;
						}
					}
				}
			}
			astar.setPath(path);
			astar.setTur(tur);
		}
		matris.Undo();
		return false;
	}
}
