package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;





/**
 * Cette class est pour trouver une chemin 
 * de la case actuelle du hero vers la case
 * correspondante
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
public class Astar {
	
	private DorukKupelioglu dk;
	private Matrix matrix;
	private List<Node> closed;
	private List<Node> open;
	private boolean stop;
	private AiPath path;
	private double PathFValue;
	private AiHero hero;
	private AiTile start;
	private boolean useTime;
	/**
	 * Constructeur pour initialiser les valeurs
	 * 
	 * @param dk pour checkinterruption 
	 * @param matrix est la matrice qu'on utilise
	 * @param hero hızını hesaba katacağımız hero
	 */
	public Astar(DorukKupelioglu dk,boolean useTime)throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		this.matrix=dk.getMatrix();
		this.hero=dk.getHero();
		this.useTime=useTime;
	}
	
	
	
	/**
	 * Cette fonction trouve une chemin entre "start" et "end".
	 * le chemin "path" contient la case "end" mais ne contient pas la case "start".
	 * 
	 *
	 * @param endTile la case ou le hero veut aller
	 * @return la chemin trouvee
	 */
	public void findPath(AiTile startTile,AiTile endTile)throws StopRequestException
	{
		dk.checkInterruption();
		init();
		if(start!=endTile)
		{
			start=startTile;
			Node startNode=new Node(startTile,endTile,null,matrix.getAreaMatrix());
			CreateClosedList(startNode,endTile);//bu fonksiyonu çağırdığında init yapıldı
			
			Node endNode = isTileInList(endTile,closed);
			if(endNode!=null)
				NodePathToTilePath(startTile, endNode);
		}
	}
	
	
	public void init()throws StopRequestException
	{
		dk.checkInterruption();
		closed=new ArrayList<Node>();
		open=new ArrayList<Node>();
		stop=false;
		path=new AiPath();
		PathFValue=Double.MAX_VALUE;
	}
	
	
	/**
	 * Cette fonction trouve une chemin entre start et le premier case
	 * ou elle peut creer une chemin
	 * @param startTile
	 * @param listOfTarget
	 * @return
	 */
	public void findPath(AiTile startTile,List<AiTile> listOfTarget)throws StopRequestException
	{
		dk.checkInterruption();
		AiTile endTile;
		init();
		int index=0;
		List<AiPath> pathes=new ArrayList<AiPath>(); 
		double[] fs={Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE};
		int number=0;
		/*
		 * on continue a chercher une chemin 
		 * pendant que le path est null et la fonctionne
		 * n'est pas examine la derniere case du list
		 */
		
		//Eğer yol bulunursa uzunluk en azından 1 olur çünkü end i de ekleyecek
		while(path.getLength()==0 && index<listOfTarget.size())
		{
			dk.checkInterruption();
			endTile=listOfTarget.get(index);
			findPath(startTile, endTile);
			index++;
		}
		pathes.add(path);
		fs[number]=PathFValue;
		number++;
			
		for(int i=0;i<3;i++)	
		{
			dk.checkInterruption();
			if(index<listOfTarget.size() && MannhattanDistance(hero.getTile(), listOfTarget.get(index))==MannhattanDistance(hero.getTile(), listOfTarget.get(index-1)))
			{
					endTile=listOfTarget.get(index);
					findPath(startTile, endTile);
					index++;
					if(path.getLength()>0)
					{
						pathes.add(path);
						fs[number]=PathFValue;
						number++;
					}
					index++;
			}
		}

		path=pathes.get(0);
		PathFValue=fs[0];
		for(index=1;index<number;index++)
		{
			dk.checkInterruption();
			if(fs[index]<PathFValue)
			{
				path=pathes.get(index);
				PathFValue=fs[index];
			}
		}
	}
	
	
	public int MannhattanDistance(AiTile tile1, AiTile tile2)throws StopRequestException
	{
		dk.checkInterruption();
		return Math.abs(tile1.getLine()-tile2.getLine())+Math.abs(tile1.getCol()-tile2.getCol());
	}
	
	
	
	/**
	 * L'algorithme principale pour trouver une chemin entre 2 cases 
	 * s'appliquee avec cette fonction.
	 * <p>ce site : http://www.policyalmanac.org/games/aStarTutorial.htm
	 * nous a aide bien a construire cette algorithme.
	 * @param node
	 * @param end
	 */
	private void CreateClosedList(Node node,AiTile end)throws StopRequestException
	{
		dk.checkInterruption();
		//1) Add the starting square (or node) to the open list.
		//System.err.println("start ı open a koyduk");
		open.add(node);
		//2) Repeat the following:
		do
		{
			//a) Look for the lowest F cost square on the open list. We refer to this as the current square.
			Node smallest=smallestF();
			
			//b) Switch it to the closed list.
			if(smallest!=null)//listede eleman yoksa devam etmenin anlamı yok
			{
				closed.add(smallest);
				open.remove(smallest);

				if(smallest.getTile()==end)//Eğer hedefe ulaşmışsak zaten olay bitmiştir
				{
					stop=true;
				}
				
				else//eğer ulaşmamışsak
				{	

					//c) For each of the squares adjacent to this current square …
					List<AiTile> neighbors=smallest.getTile().getNeighbors();

					Iterator<AiTile> itne=neighbors.iterator();
					AiTile neighborTile;
					while(itne.hasNext() && !stop)
					{
						dk.checkInterruption();
						neighborTile=itne.next();

						//If it is not walkable or if it is on the closed list, ignore it.    
						if(isTileInList(neighborTile, closed)==null)
						{

							Node neighborNode= new Node(neighborTile,end,smallest,matrix.getAreaMatrix());//Eklenecek node
							if(neighborTile==end)
							{
								closed.add(neighborNode);
								stop=true;

							}
							else if(useTime)
							{
								if(matrix.getAreaMatrix()[neighborTile.getLine()][neighborTile.getCol()]<State.DESTRUCTIBLE)
								{
									if(matrix.getAreaMatrix()[neighborTile.getLine()][neighborTile.getCol()]>State.MALUS)
									{
										double tilenumber=1;
										Node x=neighborNode;
										while(x!=null)//start ile arasında,içinde bulundukları da dahil olmak üzere kaç case var
										{
											tilenumber++;
											x=x.getParent();
										}
										double man=0;
										if(hero.getTile()!=start)//start ile hero arasında kaç case var 
											man=MannhattanDistance(hero.getTile(), start);
										double pixel=(tilenumber+man)*hero.getTile().getSize();
										double time=matrix.getTimeLeft()[neighborTile.getLine()][neighborTile.getCol()];
										
										
									    if(time>(pixel/hero.getWalkingSpeed())*1000+200)
										{
									    	//System.out.println("geçebiliriz");
											Node inList=isTileInList(neighborTile, open);//zaten liste de var mı yok mu. yoksa null.
											
											if(inList==null)
											{
												open.add(neighborNode);
	
											}
											else
											{
												if(inList.getG()>neighborNode.getG())
												{
													open.set(open.indexOf(inList), neighborNode);
	
												}
											}
										}
									    else
									    {
									    	
									    	//System.out.println("geçemeyiz zamanlar"+ time+" "+(pixel/hero.getWalkingSpeed())*1000+50);
									    }
										
									}
									//Otherwise do the following.  
									//If it isn’t on the open list, add it to the open list. 
									//Make the current square the parent of this square. Record the F, G, and H costs of the square.
									else 
									{
										Node inList=isTileInList(neighborTile, open);//zaten liste de var mı yok mu. yoksa null.
										
										if(inList==null)
										{
											open.add(neighborNode);
		
										}
										//If it is on the open list already, check to see if this path to that square is better, 
										//using G cost as the measure. A lower G cost means that this is a better path. 
										//If so, change the parent of the square to the current square, 
										//and recalculate the G and F scores of the square. 
										//If you are keeping your open list sorted by F score, 
										//you may need to resort the list to account for the change.
										else
										{
											if(inList.getG()>neighborNode.getG())
											{
												open.set(open.indexOf(inList), neighborNode);
		
											}
										}
									}
								}
							}
							else//use time dışı
							{

								if(matrix.getAreaMatrix()[neighborTile.getLine()][neighborTile.getCol()]<State.DESTRUCTIBLE)
								{
									//Otherwise do the following.  
									//If it isn’t on the open list, add it to the open list. 
									//Make the current square the parent of this square. Record the F, G, and H costs of the square.
									
										Node inList=isTileInList(neighborTile, open);//zaten liste de var mı yok mu. yoksa null.
										
										if(inList==null)
										{
											open.add(neighborNode);
		
										}
										//If it is on the open list already, check to see if this path to that square is better, 
										//using G cost as the measure. A lower G cost means that this is a better path. 
										//If so, change the parent of the square to the current square, 
										//and recalculate the G and F scores of the square. 
										//If you are keeping your open list sorted by F score, 
										//you may need to resort the list to account for the change.
										else
										{
											if(inList.getG()>neighborNode.getG())
											{
												open.set(open.indexOf(inList), neighborNode);
		
											}
										}
								}
							
							}
						}
					}
				}
			}
			else
			{
				stop=true;

			}
		}
		while(!stop);
	}
	
	/**
	 * Cherche le noeud a la plus petite valeur dans la list "open"
	 * 
	 * @return "null" si la list est vide ou le noeud correspondant sinon
	 */
	private Node smallestF()throws StopRequestException
	{
		dk.checkInterruption();
		Node result=null;
		//Si la list n'est pas vide
		if(open.size()>0)
		{	
			result=open.get(0);
			for(Node n:open)
			{
				if(result.getF()>n.getF())
					result=n;
			}
		}
		return result;
	}

	/**
	 * Cherche si la case "tile" est dans la list de noeud "list"
	 * 
	 * @param tile case a chercher
	 * @param list 
	 * @return "null" si la case "tile" n'est pas dans la list ou le noeud correspondant de case "tile" sinon
	 */
	private Node isTileInList(AiTile tile,List<Node> list)throws StopRequestException
	{
		dk.checkInterruption();
		Node node=null;
		if(list.size()>0)// si la list n'est pas vide
		for(Node x:list)
		{
			dk.checkInterruption();
			if(x.getTile()==tile)
			{
				node=x;
				break;
			}
		}
		return node;
	}

	private void NodePathToTilePath(AiTile start,Node endNode)throws StopRequestException
	{
		dk.checkInterruption();
		AiTile tile;
		PathFValue=endNode.getF();
		while(endNode!=null)
		{
			dk.checkInterruption();
			tile=endNode.getTile();
			if(tile!=start)
				path.addTile(0,tile);
			endNode=endNode.getParent();
		}
	}
	
	public AiPath getPath()throws StopRequestException
	{
		dk.checkInterruption();
		return path;
	}
	
	public double getPathFValue()throws StopRequestException
	{
		dk.checkInterruption();
		return PathFValue;
	}
	
	
}


