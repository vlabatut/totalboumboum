package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
public class TargetBonus {
	
	private DorukKupelioglu dk;
	private List<AiTile> bonuses; // matrixte tutualan haritadaki bonusları tutacak (en yakından en uzağa)
	private List<AiHero> rivals;// matrixte tutulan haritadaki hero ları tutacak (en yakından en uzağa) 
	private AiPath[] pathesOwnHero; // ownHero nun en yakın 5 bonusa olan yollarını tutacak
	private double[] FvaluesOwnHero; //ownHero nun en yakın 5 bonusa olan yollarının F ini tutacak
	private AiPath[][] pathesHeroes; //diğer heroların,ownHeroya en yakın 5 bonusa olan yollarını tutacak
	private double[][] FvaluesHeroes;
	private Astar astar;//yol bulma fonksiyonu kullanacağım
	private AiPath path;
	private boolean hasPathFound;
	private boolean targetBonusEnded;
	private boolean pathWorks;
	private int MAX_BONUS;
	private double RATIO;
	private List<Double> pathStates;
	private List<Double> pathStatesControl;
	
	
	public TargetBonus(DorukKupelioglu dk)throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		bonuses=dk.getMatrix().getBonus();
		rivals=dk.getMatrix().getHeroes();
		MAX_BONUS=10;
		RATIO=1000;
		pathesOwnHero=new AiPath[MAX_BONUS];
		FvaluesOwnHero=new double[MAX_BONUS];
		pathesHeroes=new AiPath[rivals.size()][MAX_BONUS];
		FvaluesHeroes=new double[rivals.size()][MAX_BONUS];
		
		this.path=new AiPath();
		astar=new Astar(dk,true);

		hasPathFound=false;
		targetBonusEnded=true;
		pathWorks=true;
		
		
		int bonus;
		if((bonus=findBonus())>-1)//eğer BONUS_MAX tane bonusun içinde benim gidebileceğim bi tane varsa
		{
			path=pathesOwnHero[bonus];
			if(!(path.isEmpty()))
			{
				//System.out.println("bonusa yol bulduk " +path.toString());
				targetBonusEnded=false;
				hasPathFound=true;
				pathStates=new ArrayList<Double>();
				for(int index=0;index<path.getLength();index++)
				{
					pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
				}
				System.out.println("bonus yolu: "+path.toString());
				System.out.println("bonus için stateler : "+pathStates.toString());
			}	
		}
	}
	
	public Direction moveTo()throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		if(!(path.isEmpty()))
		{
			moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
			path.removeTile(0);
			pathStates.remove(0);
		}
		System.err.println("burdayız");
		return moveDir;
	}
	
	
	/**
	 * bana en yakın MAX_BONUS tane bonustan rakibin alamayacağı ama rakibe en yakın bonusa gider
	 * @return
	 */
	private int findBonus()throws StopRequestException
	{
		dk.checkInterruption();
		int indextile=-1;

		//bonus aramak için ortada bi bonus olmalı
		if(bonuses.size()>0)
		{
			//eğer MAX_BONUS tan fazla bonus varsa sadece MAX_BONUS tanesini inceleyeceğim
			if(bonuses.size()>MAX_BONUS)
				bonuses=bonuses.subList(0, MAX_BONUS);
			//tüm heroların bonusa olan yollarını ve F lerini buluyorum
			for(int index=0;index<bonuses.size();index++)
			{
				dk.checkInterruption();
				astar.findPath(dk.getHero().getTile(), bonuses.get(index));
				pathesOwnHero[index]=astar.getPath();
				FvaluesOwnHero[index]=astar.getPathFValue()*(RATIO/dk.getHero().getWalkingSpeed());
				//System.out.println("benim yolum : "+pathesOwnHero[index].toString()+" f: "+FvaluesOwnHero[index]);

				
				for(int y=0;y<rivals.size();y++)
				{
					dk.checkInterruption();
					astar.findPath(rivals.get(y).getTile(), bonuses.get(index));
					pathesHeroes[y][index]=astar.getPath();
					FvaluesHeroes[y][index]=astar.getPathFValue()*(RATIO/rivals.get(y).getWalkingSpeed());
					//System.out.println("onun yolu : "+pathesHeroes[y][index].toString()+" f : "+FvaluesHeroes[y][index]);
				}
			}
			
			double smallest;
			double difference=Double.MAX_VALUE;
			
			for(int index=0;index<bonuses.size();index++)
			{
				dk.checkInterruption();
				smallest=Integer.MAX_VALUE;
				//heroların arasında bonusu en kolay alabilecek olanı buluyorum
				for(int x=0;x<rivals.size();x++)
				{
					dk.checkInterruption();
					if(smallest>FvaluesHeroes[x][index])
						smallest=FvaluesHeroes[x][index];
				}
				//benim alabileceğim ve rakibe en yakın bonusu seçiorum
				double y=smallest-FvaluesOwnHero[index];

				if(y>=0 && difference>y )
				{
					difference=y;
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
		if(path.getLength()==0)
			targetBonusEnded=true;
		else
		{
			while(control<path.getLength())
			{
				pathStatesControl.add(dk.getMatrix().getAreaMatrix()[path.getTile(control).getLine()][path.getTile(control).getCol()]);
				control++;
			}
			if(!(pathStates.equals(pathStatesControl)))
			{
				targetBonusEnded=true;
				hasPathFound=false;
				pathWorks=false;
			}
		}
		return hasPathFound && !targetBonusEnded;
	}
	
	public boolean pathWorks()
	{
		return pathWorks;
	}
	
	public boolean succeed() throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
}
