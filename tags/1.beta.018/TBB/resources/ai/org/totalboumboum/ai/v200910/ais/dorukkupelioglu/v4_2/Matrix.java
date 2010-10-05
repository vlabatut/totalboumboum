package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.*;



public class Matrix 
{
	private DorukKupelioglu dk;
	private AiZone zone; 
	private int width,height; // yaratacağımız matrisin (ya da oyunun) en ve boyu
	private AiHero ownHero; //bizim vatandaş
	private List<AiTile> safes;
	private List<AiTile> bonus; //bunda bonusların tile larını tutacağım - en yakından en uzağa doğru
	private List<AiTile> destructibles;
	private List<AiTile> rivals;//Bunda rakiplerin tile larını tutacağım - en yakından en uzağa doğru
	private List<AiHero> heroes;
	private int col,line;	//Genel olarak kullanım için
	private double[][] areaMatrix; //Alanın matrisi
	private double[][] timeLeft; // Bomba[line][col]  daki bombanın patlamasına kalan süre
	
	
	public Matrix(DorukKupelioglu dk)throws StopRequestException
	{
		dk.checkInterruption();
		this.zone=dk.getPercepts();
		this.dk=dk;
		init();
	}
	
	private void init()throws StopRequestException
	{
		dk.checkInterruption();
		width=zone.getWidth(); //largeur de la zone
		height=zone.getHeigh();//hauteur de la zone
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
	

	private void putFREE()throws StopRequestException//önce her yeri free yaptık
	{
		for (line = 0; line < zone.getHeigh(); line++) 
		{
			dk.checkInterruption();
			for (col = 0; col < zone.getWidth(); col++) 
			{
				dk.checkInterruption();
				areaMatrix[line][col] = State.FREE;
			}
	   	}
	}
	
	
	
	
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

			if(item.getType()==AiItemType.MALUS)
				areaMatrix[line][col]=State.MALUS;
			else
			{
				bonus.add(item.getTile());
				areaMatrix[line][col]=State.BONUS;
			}
		}
	}
	
	
	
	private void putRIVAL()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiHero> opponents = zone.getHeroes();//adversaires;
		Iterator<AiHero> opponit = opponents.iterator();
		AiHero opponent;

		while (opponit.hasNext()) 
		{
			dk.checkInterruption();
			opponent = opponit.next();
			line = opponent.getLine();
			col = opponent.getCol();

			if (!(opponent.hasEnded()) && opponent != ownHero) 
			{
					areaMatrix[line][col] = State.RIVAL;
					rivals.add(opponent.getTile());
					heroes.add(opponent);
			}
		}
	}

	
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
			if(!(ownHero.hasThroughFires()) || dk.getPercepts().getElapsedTime()<8000)
			{
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
						if(!b.isWorking() || b.getFailureProbability()>0)
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
						double time=(b.getNormalDuration()-b.getTime());
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
							{
								areaMatrix[l][c]=State.RIVALDANGER;
								rivals.remove(dk.getPercepts().getTile(l, c));
							}
							else
								areaMatrix[l][c]=State.DANGER;
						}
						if(!working)
							timeLeft[l][c]=-3;
						else if(timeLeft[l][c]==-1 || timeLeft[l][c]>mini)
							timeLeft[l][c]=mini;
					}
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
	
	private void findSafes()throws StopRequestException
	{
		dk.checkInterruption();
		for(line=0;line<zone.getHeigh();line++)
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
	//listedeki case lerdan tile case ine en yakın olanı gönderir
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
	private double findPixelDistance(AiTile tile1, AiTile tile2) throws StopRequestException
	{
		dk.checkInterruption();
		return Math.abs(tile1.getPosX()-tile2.getPosX())+Math.abs(tile1.getPosY()-tile2.getPosY());
	}
	public double ManhattanDistance(AiTile tile1, AiTile tile2) throws StopRequestException
	{
		dk.checkInterruption();
		return Math.abs(tile1.getLine()-tile2.getLine())+Math.abs(tile1.getCol()-tile2.getCol());
	}

	public void changeState(int line,int col,double state)throws StopRequestException
	{
		dk.checkInterruption();
		areaMatrix[line][col]=state;
	}
	
	public void changeTimeLeft(int line,int col,double time)throws StopRequestException
	{
		dk.checkInterruption();
		timeLeft[line][col]=time;
	}
	
	
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs)throws StopRequestException
	{	
		dk.checkInterruption();
		if(!bombs.contains(bomb))
		{	
			bombs.add(bomb);
		
			// on r�cup�re le souffle
			List<AiTile> tempBlast = bomb.getBlast();
			blast.addAll(tempBlast);
			
			// bombs
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
	
	/////GET FONCTIONS//////
	public double[][] getAreaMatrix()throws StopRequestException
	{
		dk.checkInterruption();
		return areaMatrix;
	}

	public double[][] getTimeLeft()throws StopRequestException
	{
		dk.checkInterruption();
		return timeLeft;
	}
	
	public List<AiHero> getHeroes()throws StopRequestException
	{
		dk.checkInterruption();
		return heroes;
	}

	public List<AiTile> getRivals()throws StopRequestException
	{
		dk.checkInterruption();
		return rivals;
	}
	
	public List<AiTile> getBonus()throws StopRequestException
	{
		dk.checkInterruption();
		return bonus;
	}
	
	public List<AiTile> getSafes()throws StopRequestException
	{
		dk.checkInterruption();
		return safes;
	}
	
	public List<AiTile> getDestructibles()throws StopRequestException
	{
		dk.checkInterruption();
		return destructibles;
	}
	
	public void printAreaMatrix()throws StopRequestException
	{
		dk.checkInterruption();
		for(line=0;line<zone.getHeigh();line++)
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
	
	
	public void printTimeLeft()throws StopRequestException
	{
		dk.checkInterruption();
		for(line=0;line<zone.getHeigh();line++)
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