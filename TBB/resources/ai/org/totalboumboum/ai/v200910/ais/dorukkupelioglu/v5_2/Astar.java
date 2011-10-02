package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2;

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
@SuppressWarnings("deprecation")
public class Astar 
{	
	private DorukKupelioglu dk;
	private Matrix matrix;
	private AiPath path;
	private AiHero hero;
	private AiTile start;
	private List<Node> closed;
	private List<Node> open;
	private boolean stop;
	private boolean useTime;
	private double PathFValue;

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
	 * @param endTile la case ou le hero veut aller
	 * @return la chemin trouvee
	 */
	public void findPath(AiTile startTile,AiTile endTile)throws StopRequestException
	{
		dk.checkInterruption();
		init();
		start=startTile;
		if(start!=endTile)
		{
			Node startNode=new Node(start,endTile,null,matrix.getAreaMatrix());
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
		init();
		AiTile endTile;
		List<AiPath> pathes=new ArrayList<AiPath>(); 
		List<Double> fvs=new ArrayList<Double>();
		/*
		 * on continue a chercher une chemin 
		 * pendant que le path est null et la fonctionne
		 * n'est pas examine la derniere case du list
		 */
		
		//Eğer yol bulunursa uzunluk en azından 1 olur çünkü end i de ekleyecek
		int index=0;
		while(path.isEmpty() && index<listOfTarget.size())
		{
			dk.checkInterruption();
			endTile=listOfTarget.get(index);
			findPath(startTile, endTile);
			index++;
		}
		
		if(!(path.isEmpty()))
		{
			pathes.add(path);
			fvs.add(PathFValue);
			while(index<listOfTarget.size() 
			&& MannhattanDistance(hero.getTile(), listOfTarget.get(index))==MannhattanDistance(hero.getTile(), listOfTarget.get(index-1)))
			{
				dk.checkInterruption();
				endTile=listOfTarget.get(index);
				findPath(startTile, endTile);
				if((path.isEmpty()))
				{
					pathes.add(path);
					fvs.add(PathFValue);
				}
				index++;
			}
			index=0;
			PathFValue=Double.MAX_VALUE;
			while(index<pathes.size())
			{
				dk.checkInterruption();
				if(fvs.get(index)<PathFValue)
				{
					path=pathes.get(index);
					PathFValue=fvs.get(index);
				}
				index++;
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
		open.add(node);
		do
		{
			dk.checkInterruption();
			Node smallest=smallestF();
			if(smallest!=null)//listede eleman yoksa devam etmenin anlamı yok
			{
				closed.add(smallest);
				open.remove(smallest);
				
				if(smallest.getTile()==end)//Eğer hedefe ulaşmışsak zaten olay bitmiştir
					stop=true;
				else//eğer ulaşmamışsak
				{	
					List<AiTile> neighbors=smallest.getTile().getNeighbors();
					Iterator<AiTile> itne=neighbors.iterator();
					AiTile neighborTile;
					while(itne.hasNext() && !stop)
					{
						dk.checkInterruption();
						neighborTile=itne.next();
						if(isTileInList(neighborTile, closed)==null)
						{
							Node neighborNode= new Node(neighborTile,end,smallest,matrix.getAreaMatrix());//Eklenecek node
							if(neighborTile==end)//la list "open" ne contient pas les voisines donc il faut les explorer
							{
								if(matrix.getAreaMatrix()[end.getLine()][end.getCol()]<=State.DESTRUCTIBLE)
								{
									if(useTime && matrix.getAreaMatrix()[neighborTile.getLine()][neighborTile.getCol()]>State.MALUS)
									{
										double tilenumber=0;
										Node x=neighborNode;
										while(x!=null)//start ile arasında,içinde bulundukları da dahil olmak üzere kaç case var
										{
											dk.checkInterruption();
											tilenumber++;
											x=x.getParent();
										}
										double man=0;
										
										if(hero.getTile()!=start)//start ile hero arasında kaç case var 
											man=MannhattanDistance(hero.getTile(), start);
										
										double pixel=(tilenumber+man)*hero.getTile().getSize();
										double time=matrix.getTimeLeft()[neighborTile.getLine()][neighborTile.getCol()];
										
									    if((time>0 &&time>(pixel/hero.getWalkingSpeed())*1000+100)||time==-1)
											closed.add(neighborNode);
									}
									else
										closed.add(neighborNode);
								}
								stop=true;
							}//la voisine est la case "end"
								
							else if(matrix.getAreaMatrix()[neighborTile.getLine()][neighborTile.getCol()]<State.DESTRUCTIBLE)
							{
								if(useTime && matrix.getAreaMatrix()[neighborTile.getLine()][neighborTile.getCol()]>State.MALUS)
								{
									double tilenumber=0;
									Node x=neighborNode;
									while(x!=null)//start ile arasında,içinde bulundukları da dahil olmak üzere kaç case var
									{
										dk.checkInterruption();
										tilenumber++;
										x=x.getParent();
									}
									double man=0;
									
									if(hero.getTile()!=start)//start ile hero arasında kaç case var 
										man=MannhattanDistance(hero.getTile(), start);
									
									double pixel=(tilenumber+man)*hero.getTile().getSize();
									double time=matrix.getTimeLeft()[neighborTile.getLine()][neighborTile.getCol()];
									
								    if(time!=-1 && time>(pixel/hero.getWalkingSpeed())*1000+200)
									{
										Node inList=isTileInList(neighborTile, open);//zaten liste de var mı yok mu. yoksa null.
										if(inList==null)
											open.add(neighborNode);
										else if(inList.getG()>neighborNode.getG())
												open.set(open.indexOf(inList), neighborNode);
									}
								}//usetime true
								else 
								{
									Node inList=isTileInList(neighborTile, open);//zaten liste de var mı yok mu. yoksa null.
									if(inList==null)
										open.add(neighborNode);
									else if(inList.getG()>neighborNode.getG())
											open.set(open.indexOf(inList), neighborNode);
								}//usetime false ou usetime true mais les cases pour lesquels le temps nest pas important
							}//la voisine nest pas la case "end"
						}//la voisine courante nest pas dans la liste "closed"
					}//while: recherche de voisines
				}//else : smallest!=end
			}//premier "if": "smallest" existe
			else//smallest nexiste pas: la liste "open" est vide
				stop=true;
		}while(!stop);
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
				dk.checkInterruption();
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