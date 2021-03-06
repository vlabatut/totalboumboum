package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.*;



/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
@SuppressWarnings("deprecation")
public class Matrix 
{
	/** */
	private DorukKupelioglu dk;
	/** */
	private AiZone zone; 
	/** */
	private AiHero ownHero; 
	/** */
	private List<AiTile> safes;
	/** */
	private List<AiTile> bonus; 
	/** */
	private List<AiTile> destructibles;
	/** */
	private List<AiTile> rivals;
	/** */
	private List<AiHero> heroes;
	/** */
	private int width;
	/** */
	private int height; 
	/** */
	private int col;
	/** */
	private int line;	//utilisation general
	/** */
	private double[][] areaMatrix; 
	/** */
	private double[][] timeLeft; 
	
	/**
	 * 
	 * @param dk
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Matrix(DorukKupelioglu dk)throws StopRequestException
	{
		dk.checkInterruption();
		this.zone=dk.getPercepts();
		this.dk=dk;
		init();
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void init()throws StopRequestException
	{
		dk.checkInterruption();
		width=zone.getWidth(); //largeur de la zone
		height=zone.getHeight();//hauteur de la zone
		ownHero = zone.getOwnHero(); //dk:DorukKupelioglu
		
		areaMatrix = new double[height][width];
		timeLeft=new double[height][width];
		for(int i=0;i<height;i++)
		{
			dk.checkInterruption();
			for(int j=0;j<width;j++)
			{
				dk.checkInterruption();
				timeLeft[i][j]=-1;
			}
		}
		heroes=new ArrayList<AiHero>();
		rivals=new ArrayList<AiTile>();
		bonus=new ArrayList<AiTile>();
		safes=new ArrayList<AiTile>();
		destructibles=new ArrayList<AiTile>();
	}
	
	/**
	 * Cette fonction creer les matrices areaMatrix et timeLeft 
	 * putRIVAL ve putITEM fonksiyonları rival ve bonus array listlerini sırasız olarak 
	 * dolduruyor. 
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public void createAreaMatrix()throws StopRequestException
	{
		dk.checkInterruption();
		init();		
		//State.FREE
		putFREE();
		//State.INDESTRUCTIBLE et State.DESTRUCTIBLE
		putDESTINDEST();
		//State.EXPLODED
		putFIRE();
		//State.RIVAL
		putRIVAL();
		//State.BONUS et State.MALUS
		putITEM();
		//State.BOMB , State.WILLEXPLODE et State.BONUSDANGER
		putBLAST();
		
		findSafes();
		findDestructibles();
		
		heroes=regulateHeroesList(ownHero.getTile(), heroes);
		rivals=regulateList(ownHero.getTile(), rivals);
		bonus=regulateList(ownHero.getTile(), bonus);
		safes=regulateList(ownHero.getTile(), safes);
		destructibles=regulateList(ownHero.getTile(), destructibles);
	}
	
	/////Creation de la matrice de la region et du temps rest/////
	
	/**
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private void putFREE()throws StopRequestException//önce her yeri free yaptık
	{	dk.checkInterruption();
		for (line = 0; line < zone.getHeight(); line++) 
		{	
			dk.checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) 
			{
				dk.checkInterruption();
				areaMatrix[line][col] = State.FREE;
			}
	   	}
	}	
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void putDESTINDEST()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiBlock> walls = zone.getBlocks();//blocks
		Iterator<AiBlock> wallsit = walls.iterator();
		AiBlock wall;
		while (wallsit.hasNext()) 
		{
			dk.checkInterruption();
			wall = wallsit.next();
			line = wall.getLine();
			col = wall.getCol();
			
			if(wall.getState().getName()==AiStateName.BURNING)
			{
				if(ownHero.hasThroughFires())
					areaMatrix[line][col]=State.FREE;
				else
					areaMatrix[line][col]=State.FIRE;
			}
			else 
			{
				if (!wall.isDestructible())
				{
					if(wall.isCrossableBy(ownHero) )
						areaMatrix[line][col]=State.FREEINDESTRUCTIBLE;
					else
						areaMatrix[line][col] = State.INDESTRUCTIBLE;
				}
				else if(wall.isCrossableBy(ownHero) || ownHero.hasThroughBlocks())
					areaMatrix[line][col] = State.FREEDESTRUCTIBLE;
				else
					areaMatrix[line][col]=State.DESTRUCTIBLE;
			}
		}
	}	
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void putITEM()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiItem> items = zone.getItems();//bonus
		Iterator<AiItem> itemit = items.iterator();
		AiItem item;
		while (itemit.hasNext()) 
		{
			dk.checkInterruption();
			item = itemit.next();
			line = item.getLine();
			col = item.getCol();

			if(item.getState().getName()==AiStateName.BURNING)
				areaMatrix[line][col]=State.FIRE;
			else if(item.getType()==AiItemType.MALUS)
				areaMatrix[line][col]=State.MALUS;
			else 
			{
				bonus.add(item.getTile());
				areaMatrix[line][col]=State.BONUS;
			}
		}
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void putRIVAL()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiHero> opponents = zone.getRemainingHeroes();//adversaires;
		opponents.remove(ownHero);
		Iterator<AiHero> opponit = opponents.iterator();
		AiHero opponent;

		while (opponit.hasNext()) 
		{
			dk.checkInterruption();
			opponent = opponit.next();
			line = opponent.getLine();
			col = opponent.getCol();
			areaMatrix[line][col] = State.RIVAL;
			rivals.add(opponent.getTile());
			heroes.add(opponent);
		}
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void putFIRE()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiFire> fires = zone.getFires();//feus
		Iterator<AiFire> fireit = fires.iterator();
		AiFire fire;
		while (fireit.hasNext()) 
		{
			dk.checkInterruption();
			fire = fireit.next();
			line = fire.getLine();
			col = fire.getCol();
			if(fire.isCrossableBy(ownHero))
				areaMatrix[line][col]=State.FREE;
			else
				areaMatrix[line][col] = State.FIRE;
		}
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void putBLAST()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiBomb> bombsopen = zone.getBombs();//bombes de la zone
		List<AiBomb> bombsclosed=new ArrayList<AiBomb>();
		Iterator<AiBomb> bombit=bombsopen.iterator();
		AiBomb currBomb;
		while(bombit.hasNext())
		{
			dk.checkInterruption();
			currBomb=bombit.next();
			
			if(!(bombsclosed.contains(currBomb)))//si la liste contient cet element alors l'element a ete explore
			{
				List<AiTile> tempblast=new ArrayList<AiTile>();
				List<AiBomb> tempbombs=new ArrayList<AiBomb>();
				getBlast(currBomb,tempblast, tempbombs);
				bombsclosed.addAll(tempbombs);//on ajoute les bombes explorees
			
				double mini=Double.MAX_VALUE;
				boolean working=true;
			
				for(AiBomb b:tempbombs)
				{
					dk.checkInterruption();
					if(!b.isWorking())
						working=false;
					//WILLEXPLODEC ET BONUSDANGERC ne sont pas des cases que je vais
					//utiliser pour trouver une chemin
					if(b.hasRemoteControlTrigger())
					{
						for(AiTile t:b.getBlast())
						{
							dk.checkInterruption();
							int l=t.getLine();
							int c=t.getCol();
							if(areaMatrix[l][c]!=State.FIRE)
							{
								if(areaMatrix[l][c]==State.FREE || areaMatrix[l][c]==State.DANGER)
									areaMatrix[l][c]=State.DANGERR;
								else if(areaMatrix[l][c]==State.BONUS || areaMatrix[l][c]==State.BONUSDANGER)
									areaMatrix[l][c]=State.BONUSDANGERR;
								else if(areaMatrix[l][c]==State.RIVAL || areaMatrix[l][c]==State.RIVALDANGER)
									areaMatrix[l][c]=State.RIVALDANGERR;
								timeLeft[l][c]=-2;
							}
						}
					}
					double time=(b.getNormalDuration()-b.getTime())-b.getNormalDuration()*b.getFailureProbability();
					if(mini>time)
						mini=time;
				}
				
				for(AiTile t:tempblast)
				{
					dk.checkInterruption();
					int l=t.getLine();
					int c=t.getCol();
					if(areaMatrix[l][c]==State.DESTRUCTIBLE || areaMatrix[l][c]==State.FREEDESTRUCTIBLE)
							areaMatrix[l][c]=State.DESTWILLEXPLODE;
					else if(areaMatrix[l][c]<State.DANGER)	
					{
						if(areaMatrix[l][c]==State.BONUS)
						{
							areaMatrix[l][c]=State.BONUSDANGER;
							bonus.remove(dk.getPercepts().getTile(l, c));
						}
						else if(areaMatrix[l][c]==State.RIVAL)
							areaMatrix[l][c]=State.RIVALDANGER;
						else
							areaMatrix[l][c]=State.DANGER;
					}
					if(!working)
						timeLeft[l][c]=-3;
					else if(timeLeft[l][c]==-1 || timeLeft[l][c]>mini)
						timeLeft[l][c]=mini;
				}
			}
			line=currBomb.getLine();
			col=currBomb.getCol();
			if(ownHero.hasThroughBombs()&& ownHero.hasThroughFires())
				areaMatrix[line][col]=State.FREE;
			else
				areaMatrix[line][col]=State.BOMB;
		}
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void findSafes()throws StopRequestException
	{
		dk.checkInterruption();
		for(line=0;line<zone.getHeight();line++)
		{
			dk.checkInterruption();
			for(col=0;col<zone.getWidth();col++)
			{
				dk.checkInterruption();
				if(areaMatrix[line][col]<=State.MALUS)
					safes.add(zone.getTile(line, col));
			}
		}
	}
	

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void findDestructibles()throws StopRequestException
	{
		dk.checkInterruption();
		boolean exist;
		for(int i=0;i<height;i++)
		{
			dk.checkInterruption();
			for(int j=0;j<width;j++)
			{
				dk.checkInterruption();
				if(areaMatrix[i][j]==State.DESTRUCTIBLE || areaMatrix[i][j]==State.FREEDESTRUCTIBLE)
				{	
					exist=false;
					for(int index=0;index<dk.getPercepts().getTile(i, j).getNeighbors().size();index++)
					{
						dk.checkInterruption();
						AiTile temp=dk.getPercepts().getTile(i, j).getNeighbors().get(index);
						if(areaMatrix[temp.getLine()][temp.getCol()]==State.FREE)
						{
							exist=true;
							break;
						}
					}
					if(exist)
						destructibles.add(zone.getTile(i, j));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @param list
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiHero> regulateHeroesList(AiTile tile,List<AiHero> list)throws StopRequestException
	{
		dk.checkInterruption();
		List<AiHero> result=new ArrayList<AiHero>();
	
		double distance,temp;
		while(list.size()!=0)
		{
			dk.checkInterruption();
			distance=Integer.MAX_VALUE;
			temp=0;
			AiHero rival=null;
			for(AiHero x:list)
			{
				dk.checkInterruption();
				temp=findPixelDistance(x.getTile(), tile);
				if(distance>temp)
				{
					distance=temp;
					rival=x;
				}
			}
			if(rival!=null)
			{
				result.add(rival);
				list.remove(rival);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @param list
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> regulateList(AiTile tile,List<AiTile> list)throws StopRequestException
	{
		dk.checkInterruption();
		List<AiTile> result=new ArrayList<AiTile>();
		AiTile temp;
		while(!list.isEmpty())
		{
			dk.checkInterruption();
			temp=findNearestTile(tile, list);
			result.add(temp);
			list.remove(temp);
		}
		return result;
	}
	/** listedeki case lerdan tile case ine en yakın olanı gönderir
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @param list
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiTile findNearestTile(AiTile tile,List<AiTile> list)throws StopRequestException
	{
		dk.checkInterruption();
		AiTile result=null;
		double distance=Double.MAX_VALUE,temp=0;
		for(AiTile x:list)
		{
			dk.checkInterruption();
			temp=findPixelDistance(x, tile);
			if(distance>temp)
			{
				distance=temp;
				result=x;
			}
		}
		return result;
	}
	/**
	 * 
	 * @param tile1
	 * 		Description manquante !
	 * @param tile2
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private double findPixelDistance(AiTile tile1, AiTile tile2) throws StopRequestException
	{
		dk.checkInterruption();
		return Math.abs(tile1.getPosX()-tile2.getPosX())+Math.abs(tile1.getPosY()-tile2.getPosY());
	}
	/**
	 * 
	 * @param tile1
	 * 		Description manquante !
	 * @param tile2
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double ManhattanDistance(AiTile tile1, AiTile tile2) throws StopRequestException
	{
		dk.checkInterruption();
		return Math.abs(tile1.getLine()-tile2.getLine())+Math.abs(tile1.getCol()-tile2.getCol());
	}

	/**
	 * 
	 * @param line
	 * 		Description manquante !
	 * @param col
	 * 		Description manquante !
	 * @param state
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void changeState(int line,int col,double state)throws StopRequestException
	{
		dk.checkInterruption();
		areaMatrix[line][col]=state;
	}
	
	/**
	 * 
	 * @param line
	 * 		Description manquante !
	 * @param col
	 * 		Description manquante !
	 * @param time
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void changeTimeLeft(int line,int col,double time)throws StopRequestException
	{
		dk.checkInterruption();
		timeLeft[line][col]=time;
	}
	
	/**
	 * 
	 * @param bomb
	 * 		Description manquante !
	 * @param blast
	 * 		Description manquante !
	 * @param bombs
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs)throws StopRequestException
	{	
		dk.checkInterruption();
		if(!bombs.contains(bomb))
		{	
			bombs.add(bomb);
			List<AiTile> tempBlast = bomb.getBlast();
			blast.addAll(tempBlast);
			for(AiTile tile: tempBlast)
			{
				dk.checkInterruption();
				Collection<AiBomb> bList = tile.getBombs();
				if(bList.size()>0)
				{	AiBomb b = bList.iterator().next();
					getBlast(b,blast,bombs);
				}
			}
		}
		return blast;
	}	

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double[][] getAreaMatrix()throws StopRequestException
	{
		dk.checkInterruption();
		return areaMatrix;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double[][] getTimeLeft()throws StopRequestException
	{
		dk.checkInterruption();
		return timeLeft;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiHero> getHeroes()throws StopRequestException
	{
		dk.checkInterruption();
		return heroes;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> getRivals()throws StopRequestException
	{
		dk.checkInterruption();
		return rivals;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> getBonus()throws StopRequestException
	{
		dk.checkInterruption();
		return bonus;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> getSafes()throws StopRequestException
	{
		dk.checkInterruption();
		return safes;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> getDestructibles()throws StopRequestException
	{
		dk.checkInterruption();
		return destructibles;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void printAreaMatrix()throws StopRequestException
	{
		dk.checkInterruption();
		for(line=0;line<zone.getHeight();line++)
		{
			dk.checkInterruption();
			for(col=0;col<zone.getWidth();col++)
			{
				dk.checkInterruption();
				System.out.print(areaMatrix[line][col] + "\t");
			}
			System.out.println();
		}
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void printTimeLeft()throws StopRequestException
	{
		dk.checkInterruption();
		for(line=0;line<zone.getHeight();line++)
		{
			dk.checkInterruption();
			for(col=0;col<zone.getWidth();col++)
			{
				dk.checkInterruption();
				System.out.print(timeLeft[line][col] + "\t");
			}
			System.out.println();
		}
	}
}