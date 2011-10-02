package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
@SuppressWarnings("deprecation")
public class TargetDestructible {

	private DorukKupelioglu dk;
	private Astar astar;
	private Bomb bomb;
	private AiPath path;
	private AiPath[] pathesOwnHero;
	private double[] FvaluesOwnHero;
	private List<AiTile> destructibles;
	private List<Double> pathStates;
	private List<Double> pathStatesControl;
	private boolean hasPathFound;
	private boolean targetDestructibleEnded;
	private boolean pathWorks;
	private int MAX_DEST;
	
	public TargetDestructible(DorukKupelioglu dk) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		dk.init();
		destructibles=dk.getMatrix().getDestructibles();
		pathStates=new ArrayList<Double>();
		path=new AiPath();
		hasPathFound=false;
		targetDestructibleEnded=true;
		pathWorks=true;
		MAX_DEST=5;
		pathesOwnHero=new AiPath[MAX_DEST];
		FvaluesOwnHero=new double[MAX_DEST];
		astar=new Astar(dk,true);

		int dest;

		if((dest=findDestructible())>-1)//eğer BONUS_DEST tane duvarın içinde benim gidebileceğim bi tane varsa
		{
			path=pathesOwnHero[dest];
			if(path.getLength()==1)//1 ise zaten yanındayız demektir
			{
				if(dk.getHero().getTile().getNeighbors().contains(path.getTile(0)))
					hasPathFound=true;
			}
			else if(path.getLength()>1)//işte o duvarı artık bulduk
			{
				hasPathFound=true;
				targetDestructibleEnded=false;
				pathStates=new ArrayList<Double>();
				for(int index=0;index<path.getLength()-1;index++)
				{
					dk.checkInterruption();
					pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
				}
			}	
		}
	}
	
	public Direction moveTo()throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;	
		moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
		path.removeTile(0);
		pathStates.remove(0);
		return moveDir;
	}
	
	

	private int findDestructible()throws StopRequestException
	{
		dk.checkInterruption();
		int indextile=-1;

		if(!(destructibles.isEmpty()))//oyunda dest varsa
		{
			if(destructibles.size()>MAX_DEST)//yakınlığa göre sıralanmış destlerin sayısı maxdest ten fazla ise azaltıoruz
				destructibles=destructibles.subList(0, MAX_DEST);

			for(int index=0;index<destructibles.size();index++) // tüm destlere (varsa) olan yolları buluyoruz
			{
				dk.checkInterruption();
				astar.findPath(dk.getHero().getTile(), destructibles.get(index));
				pathesOwnHero[index]=astar.getPath();
				if(pathesOwnHero[index].getLength()==1)
				{
					bomb=new Bomb(dk,dk.getHero().getTile());//uzunluk 1 ise ben duvarın yanındayım demktir yani zaten duvara gelmişim şimdi bulunduğum yerden safe bi yere kaçış var mı ona bakıyorum
					if(bomb.pathAvailable())// eğer bir kaçış yolu olacaksa
						FvaluesOwnHero[index]=astar.getPathFValue(); // bu duvarı listeye ekliyoruz
					else
						FvaluesOwnHero[index]=Double.MAX_VALUE;// yoksa eklemiyoruz
					bomb=null;
				}
				else if(pathesOwnHero[index].getLength()>1)
				{
					bomb=new Bomb(dk,pathesOwnHero[index].getTile(pathesOwnHero[index].getLength()-2));//duvardan bi önceki tile a (duvarın yanına) bomba koyduğumuzu varsayıyorz
					if(bomb.pathAvailable())// eğer bir kaçış yolu olacaksa
						FvaluesOwnHero[index]=astar.getPathFValue(); // bu duvarı listeye ekliyoruz
					else
						FvaluesOwnHero[index]=Double.MAX_VALUE;// yoksa eklemiyoruz
					bomb=null;
				}
				else
					FvaluesOwnHero[index]=Double.MAX_VALUE;
				dk.init();
			}
			double difference=Double.MAX_VALUE;
			
			for(int index=0;index<destructibles.size();index++)// burda da eklediğimiz duvarlardan gidilmesi en kolayı buluyoruz
			{
				dk.checkInterruption();
				if(difference>FvaluesOwnHero[index])
				{
					difference=FvaluesOwnHero[index];
					indextile=index;
				}
			}
		}
		return indextile;
	}

	public boolean pathAvailable() throws StopRequestException
	{
		dk.checkInterruption();
		dk.init();
		pathStatesControl=new ArrayList<Double>();
		int control=0;
		
		if(path.getLength()==1 || path.getLength()==0)//1 ise yanına geldim 0 ise yanındaydım zaten
			targetDestructibleEnded=true;
		else
		{
			while(control<path.getLength()-1)
			{
				dk.checkInterruption();
				pathStatesControl.add(dk.getMatrix().getAreaMatrix()[path.getTile(control).getLine()][path.getTile(control).getCol()]);
				control++;
			}
			if(!(pathStates.equals(pathStatesControl)))
			{
				targetDestructibleEnded=true;
				hasPathFound=false;
				pathWorks=false;
			}
		}
		return hasPathFound && !targetDestructibleEnded;
	}	
	
	public boolean pathWorks() throws StopRequestException
	{	dk.checkInterruption();
		return pathWorks;
	}
	
	public boolean succeed() throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
	
	public int PathLength() throws StopRequestException
	{	dk.checkInterruption();
		return path.getLength();
	}
}
