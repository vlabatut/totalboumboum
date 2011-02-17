package org.totalboumboum.ai.v201011.ais.goncuonat.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;



public class ModeAttack 
{
	
	//public AiHero monia.ourHero;
	public GoncuOnat monia;
	
	// la case vide qui ne contient aucuns sprites
	// est representée dans la matrice da la zone.
	public final int CASE_EMPTY=0;
	// la case qui contient un mur indestructible pour le mode
	// attaque est representée dans la matrice da la zone.
	public final int ATTACK_HARDWALL =1 ;
	// la case qui contient le feu d'une bombe pour le mode
	// attaque est representée dans la matrice da la zone.
	public final int ATTACK_FIRE =-20 ;
	// la case qui contient un bonus pour le mode
	// attaque est representée dans la matrice da la zone.
	public final int ATTACK_BONUS= 0;
	// la case qui contient un héro pour le mode
	// attaque est representée dans la matrice da la zone.
	public final int ATTACK_RIVAL = 100;
	// la case qui contient une bombe pour le mode
	// attaque est representée dans la matrice da la zone.
	public final int ATTACK_BOMB = -20;
	//public AiZone zone;
	//AiPath path =null;
	
	//public AiPath nextMove=null;
	// chemin a suivre pour attaquer
	//public AiPath nextMoveAttack=null;
	//public AiZone zone;
	
	public ModeAttack( GoncuOnat monia) throws StopRequestException
	{	
		monia.checkInterruption();
		this.monia=monia;
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des case possedant du feu. 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	
	public void valueFiresAttack(double[][] matrice, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		
		
		while (iteratorFires.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBLÝGATOÝRE
			AiFire fire=iteratorFires.next();
			
			Collection<AiTile> fireNeighbors=fire.getTile().getNeighbors();
			Iterator<AiTile> iteratorFire = fireNeighbors.iterator();
			while(iteratorFire.hasNext())
			{
				monia.checkInterruption(); // APPEL OBLÝGATOÝRE
				AiTile tile=iteratorFire.next();
				if(tile.isCrossableBy(monia.ourHero))
					matrice[tile.getLine()][tile.getCol()] += ATTACK_FIRE;
			}
		}
		
		
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des bonus pour le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBonusAttack(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBLÝGATOÝRE
			AiItem item = iteratorItems.next();
			Collection<AiTile> bonusNeighbors=item.getTile().getNeighbors();
			Iterator<AiTile> iteratorFire = bonusNeighbors.iterator();
			while(iteratorFire.hasNext())
			{
				monia.checkInterruption();
				AiTile tile=iteratorFire.next();
				if(tile.isCrossableBy(monia.ourHero))
					matrice[tile.getLine()][tile.getCol()]+=ATTACK_BONUS;
			}
			
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des bombes pour le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBombsAttack(double[][] matrice, AiZone zone) throws StopRequestException{
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		
		while (iteratorBombs.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBLÝGATOÝRE
			AiBomb bomb = iteratorBombs.next();
			
			Collection<AiTile> bombNeighbors=bomb.getTile().getNeighbors();
			Iterator<AiTile> iteratorBomb = bombNeighbors.iterator();
			while(iteratorBomb.hasNext())
			{
				monia.checkInterruption(); 
				AiTile tile=iteratorBomb.next();
				if(tile.isCrossableBy(monia.ourHero))
					matrice[bomb.getLine()][bomb.getCol()] += ATTACK_BOMB;
			}
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
		
			
			while (iteratorScope.hasNext())
			{
				monia.checkInterruption(); // APPEL OBLÝGATOÝRE
				AiTile blastCase=iteratorScope.next();
				matrice[blastCase.getLine()][blastCase.getCol()] += ATTACK_FIRE;
				
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des heros pour le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueRivalAttack(double[][] matrice, AiZone zone)throws StopRequestException 
    {
        monia.checkInterruption(); // APPEL OBLÝGATOÝRE
        Collection<AiHero> items = zone.getHeroes();
        Iterator<AiHero> iteratorHeroes = items.iterator();
   
        List<AiHero> rivals=new ArrayList<AiHero>();
        AiHero hero;
        
        while (iteratorHeroes.hasNext()) 
        {
            monia.checkInterruption();
            AiHero hero2 =iteratorHeroes.next();
            if(!(hero2.getLine()==zone.getOwnHero().getLine() && hero2.getCol()==zone.getOwnHero().getCol()))
                rivals.add(hero2);
        }
        for(int p=0;p<rivals.size();p++)
        {
            
            monia.checkInterruption(); 
            hero=rivals.get(p);
            //AiHero hero =iteratorHeroes.next();
            int x =hero.getLine();
            int y =hero.getCol();
            //int blast=monia.ourHero.getBombRange();
            int i=x-3;
            int j=y-3;
            if(i<0)
                i=0;
            if(j<0)
                j=0;
            int rangex=x+4;
            int rangey=y+4;
            if(rangex>zone.getHeight())
                rangex=zone.getHeight();
            if(rangey>zone.getHeight())
                rangey=zone.getHeight();
            
            for(int a=i;a<rangex;a++)
            {
                monia.checkInterruption(); // APPEL OBLÝGATOÝRE
                for(int k=j;k<rangey;k++)
                {
                    monia.checkInterruption(); // APPEL OBLÝGATOÝRE
                    if(zone.getTile(a, k)!=zone.getTile(monia.ourHero.getLine(),monia.ourHero.getCol()))
                    {
                        if((Math.abs(a-x)==2) ||(Math.abs(k-y)==2))
                        { 
                            
                            matrice[a][k]+=monia.ourHero.getBombRange()+ATTACK_RIVAL+2;
                        }
                        
                        else if((Math.abs(a-x)==(1) ||Math.abs(k-y)==(1)))
                        { 
                            matrice[a][k]+=monia.ourHero.getBombRange()+ATTACK_RIVAL+1;
                        }
                        
                        else if((Math.abs(a-x)==0 || Math.abs(k-y)==0))
                        {
                            matrice[a][k]+=monia.ourHero.getBombRange()+ATTACK_RIVAL;
                        }
                    }
                }
            }    
        }
    }
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des murs destructibles et indestructibles pour 
	 * le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBlocksAttack(double[][] matrice, AiZone zone) throws StopRequestException{
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		Collection<AiBlock> blocks = zone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBLÝGATOÝRE
			AiBlock block = iteratorBlocks.next();
		
			{
				Collection<AiTile> blockNeighbors= block.getTile().getNeighbors();
				Iterator<AiTile> iteratorBlock = blockNeighbors.iterator();
				
				while (iteratorBlock.hasNext()) 
				{
					monia.checkInterruption();
					AiTile tile=iteratorBlock.next();
					if(tile.isCrossableBy(monia.ourHero))
						matrice[tile.getLine()][tile.getCol()] += ATTACK_HARDWALL;
				}
			}
				
			
		}
	}
	
	/**
	 * 
	 * La methode pour remplir une liste avec les casses "crossableby" notre bonhomme
	 * 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns  endpoint
	 * 			 La liste des cases que notre ia peut passer
	 */
	public List<AiTile> endPoint(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		List<AiTile> endpoint = new ArrayList<AiTile>();
		//AiTile tile=new AiTile();
		for(int i=0;i<zone.getHeight();i++)
		{
			monia.checkInterruption(); // APPEL OBLÝGATOÝRE
			for(int j=0;j<zone.getWidth();j++)
			{
				monia.checkInterruption(); // APPEL OBLÝGATOÝRE
				if(zone.getTile(i, j).isCrossableBy(monia.ourHero) && matrice[i][j]>0)
					endpoint.add(zone.getTile(i,j));
					
			}	
		}	
		return endpoint;
		}
	
	
	/**
	 * 
	 *  
	 * La methode qui rempli 
	 * 
	 * @param  endPoint
	 * 				les casses "crossableby" notre hero etqui a une valeur plus grand que zero.
	 * @param matrice
	 * 			la matrice qui represent le zone
	 * 
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns   Liste des valeurs des dernieres cases des chemins possibles.
	 * 			  
	 */
	public List<Double> endpointValue(List<AiTile> endPoint, double[][] matrice)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		double temp;
		List<Double> result = new ArrayList<Double>();
		for(int i=0;i<endPoint.size();i++)
		{
			monia.checkInterruption();
			temp=matrice[endPoint.get(i).getLine()][endPoint.get(i).getCol()];
			result.add(temp);
		}
			
		return result;
	
	}

	/**
	 * 
	 *  
	 * La methode qui returne la plus logique selon notre algo.
	 * 
	 * @param  endpointValue
	 * 				 		La Liste pour tenir compte des valeur de dernier case d'un chemin.
	
	 *	@param	 shortestPathAttack
	 *						La liste des chemins pour l'attaquer.
	 *   @param matrice
	 *				La matrice qui represent le zone
	 * @throws StopRequestException
	 * 
	 * @returns   shortestPathAttack.get(index)
	 * 			  				Le chemin plus logique selon notre algorithm.
	 */
	public AiPath objectifPath(List<Double> endpointValue, List<AiPath> shortestPathAttack, double[][] matrice)throws StopRequestException 
	{
		
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		List<Double> result= new ArrayList<Double>();
		ShortestPath spath= new ShortestPath(monia);
		double temp ;
		
	    AiTile closestOne =spath.closestEnemy(monia.zone);
		List<Double> distanceToEnemy=spath.tileToClosestEnemy(closestOne, endPoint(matrice , monia.zone), monia.zone);
		for(int i=0;i<endpointValue.size();i++)
		{
			monia.checkInterruption();
			if(distanceToEnemy.get(i)!=null && distanceToEnemy.get(i)!=0)
				{	
					temp=(endpointValue.get(i))/(distanceToEnemy.get(i));
					result.add(temp);
				}
			else 
			{
				temp=0;
				result.add(temp);
			}
			
		}
		double tempmax=result.get(0);
		double temp2=tempmax;
		int index=0;
		for(int j=0;j<result.size();j++)
		{
			monia.checkInterruption();
			temp2=result.get(j);
			if(temp2>tempmax)
			{
				tempmax=temp2;
				index=j;
			}
		}
		
			return shortestPathAttack.get(index);

	}
	
	
	
	
	/**
	 * 
	 *  
	 * La methode qui returne la plus logique selon notre algo en utilisant  la liste des points qu'on avati calcule  pendant l'algo. general
	 * 
	 * @param  endpointValue
	 * 				 		La Liste pour tenir compte des valeur de dernier case d'un chemin.
	 * 
	 * @param check
	 * 			 la liste des points qu'on avati calcule  pendant l'algo. general
	 *	@param	 shortestPathAttack
	 *						La liste des chemins pour l'attaquer.
	 *   @param matrice
	 *				La matrice qui represent le zone
	 * @throws StopRequestException
	 * 
	 * @returns   shortestPathAttack.get(index)
	 * 			  				Le chemin plus logique selon notre algorithm.
	 */
	public AiPath objectifPathCheck(List<Double> endpointValue, List<AiTile> check, List<AiPath> shortestPathAttack, double[][] matrice)throws StopRequestException 
	{
		
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		List<Double> result= new ArrayList<Double>();
		ShortestPath spath= new ShortestPath(monia);
		double temp;
	    AiTile closestOne =spath.closestEnemy(monia.zone);
		List<Double> distanceToEnemy=spath.tileToClosestEnemy(closestOne, check, monia.zone);
		for(int i=0;i<endpointValue.size();i++)
		{
			monia.checkInterruption();
			if(distanceToEnemy.get(i)!=null && (distanceToEnemy.get(i)!=0) && (shortestPathAttack.get(i)!=null && shortestPathAttack.get(i).getLength()!=0 ))

				{	
					temp=(((endpointValue.get(i)/(distanceToEnemy.get(i)))/*/monia.ourHero.getWalkingSpeed()*/));
					result.add(temp);
				}
			else 
			{
				temp=0;
				result.add(temp);
			}
			
		}
		double tempmax=result.get(0);
		double temp2=tempmax;
		int index=0;
		for(int j=0;j<result.size();j++)
		{
			monia.checkInterruption();
			temp2=result.get(j);
			if(temp2>tempmax)
			{
				tempmax=temp2;
				index=j;
			}
		}
		return shortestPathAttack.get(index);

	}
	
	/**
	 * 
	 *  La methode pour appliquer l'algorithm d'attaque en utilisant les fonctions precedents.
	 * 
	 * 
	 * @param  zone
	 * 				La zone du jeu	 
	 * @param  matrice
	 * 				 La matrice de la zone
	 *			 
	 * @throws StopRequestException  
	 * 			  
	 */
	
	public void matriceAttack(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBLÝGATOÝRE
		if (monia.nextMoveAttack == null) 
		{
			ShortestPath spath= new ShortestPath(monia);
			List<AiTile> endPoint=this.endPoint(matrice, zone);
			List<Double> endPointValue=this.endpointValue(endPoint,matrice);
			List<AiPath> shortestPathAtt=spath.shortestPathAttack(monia.ourHero, monia.ourHero.getTile(), endPoint);
			monia.nextMoveAttack = this.objectifPath(endPointValue,shortestPathAtt, matrice);		
		}
		
		else 
		{
			if (monia.nextMoveAttack.getLength() == 0)
				monia.nextMoveAttack = null;
			else 
			{
				boolean danger = false;
				List<AiTile> nextTiles = monia.nextMoveAttack.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) 
				{
					monia.checkInterruption();
					if (!nextTiles.get(i).isCrossableBy(monia.ourHero)||matrice[nextTiles.get(i).getLine()][nextTiles.get(i).getCol()] <0)
							danger = true;
				}
			
				if (danger)
					monia.nextMoveAttack = null;
				
				else 
				{
					if ((monia.ourHero.getLine() == monia.nextMoveAttack.getTile(0).getLine())&& (monia.ourHero.getCol() == monia.nextMoveAttack.getTile(0).getCol())) 
					{
						monia.nextMoveAttack.getTiles().remove(0);
						if (monia.nextMoveAttack.getTiles().isEmpty()) 
						{
							monia.nextMoveAttack = null;
		
						}
					}
				}
			}
		}
	}

	
	
}
