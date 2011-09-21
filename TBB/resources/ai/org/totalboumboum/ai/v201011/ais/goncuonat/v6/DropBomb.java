package org.totalboumboum.ai.v201011.ais.goncuonat.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

public class DropBomb 
{
	public AiZone zone;
	AiPath path =null;
	public GoncuOnat monia;


	
	public DropBomb(GoncuOnat monia) throws StopRequestException 
	{
		monia.checkInterruption();
		this.monia = monia;
	}
	
	/**
	 * 
	 *  
	 * La methode pour la decision de mettre une bombe dans mode attack.
	 * 
	 * @param  matrice
	 * 				 La matrice de la zone
	 * @param  zone
	 * 				La zone du jeu 
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns  boolean  
	 * 			true si on peut mettre un bombe
	 * 			  
	 */
	public boolean dropBombCheckAttack(AiZone zone, double[][]matrice)throws StopRequestException
	{
		
		monia.checkInterruption();
		boolean result=false;
		AiPath path =null;
		List<AiTile> check= new ArrayList<AiTile>();
			for(int i=0; i<zone.getHeight(); i++)
		
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				for(int j=0; j<zone.getWidth(); j++)
				{
					monia.checkInterruption(); // APPEL OBLIGATOIRE
					
						if(zone.getTile(i, j).getFires().isEmpty()&&zone.getTile(i, j).getBombs().isEmpty() 
								&& zone.getTile(i,j).isCrossableBy(monia.ourHero))
						{
							check.add(zone.getTile(i,j));
							
						}
				}
				
			}
		
		List <AiTile> bombBlast =new ArrayList<AiTile>();
		int bombRange=monia.ourHero.getBombRange();
		int x=monia.ourHero.getTile().getLine();
		int y=monia.ourHero.getTile().getCol();
		int a=x-bombRange;
		int b=y-bombRange;
		int c=x+bombRange;
		int d=y+bombRange;
		if(a<=0)
			a=0;
		if(c>zone.getHeight());
			c=zone.getHeight();
		for(int i=a;i<c;i++)
		{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(zone.getTile(i, y).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(i,y));
		}
		
		if(b<=0)
			b=0;
		if(y+bombRange>zone.getWidth())
			d=zone.getWidth();
		for(int j=b;j<d;j++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(zone.getTile(x, j).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(x,j));
			}
		for(int k=0;k<bombBlast.size();k++)
		{
			monia.checkInterruption(); // APPEL OBLIGATOIRE
			for(int l=0;l<check.size();l++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(bombBlast.get(k).getLine()==check.get(l).getLine()&&bombBlast.get(k).getCol()==check.get(l).getCol())
					check.remove(l);
			}
		}
		Runaway raway=new Runaway(monia);
		if(check!=null)
			if(check.size()!=0)
				{
					raway.runAwayAlgoAttackCheck(matrice, zone,check);
					path=monia.nextMoveCheck;
					
					
				}
		if(path!=null)
			if(path.getLength()!=0)
			{
				if(path.getPixelDistance()/monia.ourHero.getWalkingSpeed()<(monia.ourHero.getExplosionDuration())
						&& this.checkSecure(path,monia.zone))
				{	
					result=true;
				}
			
			}
		return result; 
	}
	
	
	/**
	 * 
	 *  
	 * La methode qui teste si la liste de case contient la case donne
	 * 
	 * @param  list
	 * 				Liste de case a tester
	 * @param tile
	 * 			le case a tester
	 * 
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns   boolean
	 * 				renvoie true si la case se trove dans la liste.
	 * 			  
	 */
	public boolean isInList (List<AiTile> list , AiTile tile)throws StopRequestException
	{
		monia.checkInterruption();
		boolean result=false;
		for(int i=0;i<list.size();i++)
		{
			monia.checkInterruption();
			if(list.get(i)==tile)
				result=true;
		}
		return result;
	}

	/**
	 * La methode pour la decision de mettre une bombe dans mode collecte
	 * 
	 * @param zone
	 *            La zone du jeu
	 * 
	 * @throws StopRequestException
	 * 
	 * @return boolean
	 * 				true si on peut mettre un bombe
	 */
	
	public boolean dropBombCheckCollecte(AiZone zone /*double[][]matrice*/)throws StopRequestException
	{
		monia.checkInterruption();
		boolean result=false;
		AiPath path =null;
		List<AiTile> check= new ArrayList<AiTile>();
		
		Collection<AiBomb> bombs = zone.getBombs();
		List<AiTile> blast = new ArrayList<AiTile>();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		List<AiTile> blastTemp= new ArrayList<AiTile>();
		if(bombs!=null && bombs.size()!=0 )
		{	
			while(iteratorBombs.hasNext())
			{
				monia.checkInterruption();
				blast =iteratorBombs.next().getBlast();
			
				for(int k=0;k<blast.size();k++)
					{
						monia.checkInterruption();
						blastTemp.add(blast.get(k));
						
					}
				
			}
			
			for(int i=0; i<zone.getHeight(); i++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				for(int j=0; j<zone.getWidth(); j++)
				{
					monia.checkInterruption();
					
					if(zone.getTile(i,j).getFires().isEmpty()&& zone.getTile(i, j).getBombs().isEmpty() 
							&& zone.getTile(i,j).isCrossableBy(monia.ourHero)&& !isInList(blastTemp,zone.getTile(i, j)))	
					{	
						check.add(zone.getTile(i,j));
					}
					
				}
			}
		
		
		}
		else
		{
			
			for(int i=0; i<zone.getHeight(); i++)
		
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				for(int j=0; j<zone.getWidth(); j++)
				{
					monia.checkInterruption(); // APPEL OBLIGATOIRE
					
						if(zone.getTile(i, j).getFires().isEmpty()&&zone.getTile(i, j).getBombs().isEmpty() 
								&& zone.getTile(i,j).isCrossableBy(monia.ourHero))
						{
							check.add(zone.getTile(i,j));
							
						}
				}
				
			}
		}
		List <AiTile> bombBlast =new ArrayList<AiTile>();
		int bombRange=monia.ourHero.getBombRange();
		int x=monia.ourHero.getTile().getLine();
		int y=monia.ourHero.getTile().getCol();
		int a=x-bombRange;
		int b=y-bombRange;
		int c=x+bombRange;
		int d=y+bombRange;
		if(a<=0)
			a=0;
		if(c>zone.getHeight());
			c=zone.getHeight();
		for(int i=a;i<c;i++)
		{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(zone.getTile(i, y).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(i,y));
		}
		
		if(b<=0)
			b=0;
		if(y+bombRange>zone.getWidth())
			d=zone.getWidth();
		for(int j=b;j<d;j++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(zone.getTile(x, j).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(x,j));
			}
		for(int k=0;k<bombBlast.size();k++)
		{
			monia.checkInterruption(); // APPEL OBLIGATOIRE
			for(int l=0;l<check.size();l++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(bombBlast.get(k).getLine()==check.get(l).getLine()&&bombBlast.get(k).getCol()==check.get(l).getCol())
					check.remove(l);
			}
		}
		ShortestPath spath = new ShortestPath(monia);
		if(check!=null)
			if(check.size()!=0)
				 path=spath.shortestPath(monia.ourHero, monia.ourHero.getTile(), check);
		if(path!=null)
			if(path.getLength()!=0)
		{
			if((path.getDuration(monia.ourHero)/1000)<monia.ourHero.getExplosionDuration() && this.checkSecure(path,monia.zone))
			{
				
				result=true;
			}
			
		}
		
		return result; 
	}
	
	/**
	 * 
	 *  
	 * La methode qui teste si le chemin donne est secur�.
	 * 
	 * @param  path
	 * 				Le chemin
	 * @param zone
	 * 			La zone de la jeu.
	 * 
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns   boolean
	 * 			  
	 */
	public boolean checkSecure(AiPath path, AiZone zone)throws StopRequestException
	{
		monia.checkInterruption();
		boolean result=true;
		Collection<AiBomb> bombs = zone.getBombs();
		List<AiTile> blast = new ArrayList<AiTile>();
		List<AiTile> temp= new ArrayList<AiTile>();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		List<AiTile> blastTemp= new ArrayList<AiTile>();
		if(bombs.size()!=0 && bombs!=null)
		{
			while(iteratorBombs.hasNext())
		{
			monia.checkInterruption();
			blast =iteratorBombs.next().getBlast();
			for(int k=0;k<blast.size();k++)
			{
				monia.checkInterruption();
				blastTemp.add(blast.get(k));
			}
				
		}}
		for(int i=0;i<path.getLength();i++)
		{	
			monia.checkInterruption();
			if(blastTemp!=null && blastTemp.size()!=0)
			{	
				for(int j=0;j<blastTemp.size();j++)
				{
					monia.checkInterruption();
					if(path.getTile(i).equals(blastTemp.get(j)))
					
						temp.add(path.getTile(i));
					if(path.getTile(i).equals(bombs.iterator().next()));
						temp.add(path.getTile(i));
				}
			}
		}
		if(temp.size()!=0 && temp!=null)
			result=false;
		else
			result=true;
		
		return result;
	}
}
