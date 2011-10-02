package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;
import java.util.List;

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
public class Duvar {
	private KayaYerinde onder;
	private Matris matris;
	private Astar astar;
	private boolean running;
	private AiTile incelenecek;
	private boolean yaz;	
	
	public Duvar(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		astar=new Astar(onder);
		incelenecek=onder.getPercepts().getTile(0, 0);
		running=false;
	}

	public void matrisAyarla(Matris matris) throws StopRequestException
	{
		onder.checkInterruption();
		this.matris=matris;
	}
	
	public AiAction getNextAction() throws StopRequestException
	{
		onder.checkInterruption();
		AiAction result=new AiAction(AiActionName.NONE);
				
		AiTile own=onder.getOwnHero().getTile();

		//bir tile ı her döngünün her boğumunda incelemek yerine sadece 1 kere incelemek 
		if(!incelenecek.equals(own))
		{
			incelenecek=own;
			if(incele())
			{
				if(yaz)
					System.err.println("duvarda bomba bıraktı "+onder.getOwnHero().getTile());
				running=true;
				return new AiAction(AiActionName.DROP_BOMB);
			}
		}
		
		if(running)
		{
			if(astar.yoldaMi()==1)
				return astar.getNextAction(matris);
			running=false;
		}
		if(!running )
		{

			//zaten güvende ve bi kırılır duvar yanında olup olmadığımızı kontrol ediyoruz
			List<AiTile> komsular=onder.getOwnHero().getTile().getNeighbors();
			for(AiTile t:komsular)
			{
				onder.checkInterruption();
				if(matris.getVarlikMatrisi()[t.getLine()][t.getCol()]==Matris.DESTRUCTIBLE)
				{
					incelenecek=onder.getPercepts().getTile(0, 0);
					return result;
				}
			}
			
			ArrayList<AiTile> dest=matris.getDest();
			for(AiTile t:dest)
			{
				onder.checkInterruption();
				if(astar.yolBul(onder.getOwnHero().getTile(), t,Matris.DESTRUCTIBLE, matris))
				{
					result=astar.getNextAction(matris);
					running=true;
					break;
				}
			}
		}
		return result;
	}

	
	//bulunduğum yere bomba bıraksam; 
	//1)herhangi bir bombanın etkisi altında olmayan bir kırılır duvara etki eder mi?
	//2)kaçabilir miyim
	//her ikisini de yapabiliyorsa true, aksi halde false gönderir
	//işlem yaparken hayali bir bomba bırakıp matrisi değiştirir, etki alanına göre hareket eder
	//astarda daha önceden bulunmuş olan yol ve tür değerlerini değiştirir.
	//eğer yol bulamamışsa matrisi ve astarı eski haline getirir.
	private boolean incele() throws StopRequestException
	{
		onder.checkInterruption();
		if(onder.getOwnHero().getBombNumberCurrent()==0)
			return false;
		matris.hypoteticalBomb(onder.getOwnHero().getTile());
		//if(yaz)
			//System.err.println("vurduğum duvar sayısı: "+ matris.getHypoDuvar());
		if(matris.getHypoDuvar()>0)
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
			ArrayList<AiTile> safes=matris.getFrees();
			AiPath path=astar.getPath();
			int tur=astar.getTur();
			if(!safes.isEmpty())
			{
				for(int i=0;i<safes.size();i++)
				{
					onder.checkInterruption();
					if(astar.yolBul(onder.getOwnHero().getTile(), safes.get(i),Matris.FREE, matris))
						return true;
				}
			}
			astar.setPath(path);
			astar.setTur(tur);
		}
		matris.Undo();
		return false;
	}
	
	public void run() throws StopRequestException
	{
		onder.checkInterruption();
		running=true;
	}
	public boolean isRunning() throws StopRequestException
	{
		onder.checkInterruption();
		return running;
	}
	public Astar getAstar() throws StopRequestException
	{
		onder.checkInterruption();
		return astar;
	}
}
