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
public class TargetRival {

	private DorukKupelioglu dk;
	private Bomb bomb;
	private Astar astar;
	private AiPath path;
	private List<AiTile> rivals;
	private List<AiTile> available;
	private boolean hasPathFound;
	private boolean targetRivalEnded;
	private int[][] rivalavailable;
	
	/**
	 * 
	 * @param dk
	 * @throws StopRequestException
	 */
	public TargetRival(DorukKupelioglu dk) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		path=new AiPath();
		available=new ArrayList<AiTile>();
		hasPathFound=false;
		targetRivalEnded=true;		
		rivals=dk.getMatrix().getRivals();
		available=findAvailable();//bomba bırakınca rakibi öldürebileceğim yerler
		available=dk.getMatrix().regulateList(dk.getHero().getTile(), available);//yakınlığına göre sıraladım
		astar=new Astar(dk,true);//zamanlı astar oluşturdum
		boolean goahead=true;
		int number=0;

		if(!(available.isEmpty()) && dk.getHero().getTile().getHeroes().size()==1)//rakibe etki edebilecek yer varsa
		{
			int index=0;
			AiTile tile=dk.getHero().getTile();//bu eşitlik sadece initialisation için var 
			while(index<rivals.size()&&number<3)//tüm rakiplerin 4 tarafını inceleyeceğim
			{
				dk.checkInterruption();
				number=0;
				List<AiTile> neighbors=rivals.get(index).getNeighbors();
				for(int index2=0;index2<neighbors.size();index2++)//index inci rakip için komşulara bakıyorum
				{
					dk.checkInterruption();
					if(dk.getMatrix().getAreaMatrix()[neighbors.get(index2).getLine()][neighbors.get(index2).getCol()]<State.DESTRUCTIBLE)
						tile=neighbors.get(index2);//eğer 3 tarafı sıkışmışsa sıkışmayan case burda saklanacak
					else
						number++;//etrafında bulunan yürüyemeyeceği her case için numberı 1 arttırıyorum 
				}
				index++;//eğer number 3 ise döngüden çıkacağız ve index-1 inci rakibin 3 tarafı kapalı demek olacak
			}
			
			if(number==3)//eğer 3 tarafı sıkışmış ise rakip sıkıştırılabilir
			{
				astar.findPath(dk.getHero().getTile(), tile);//sıkışmış olmayan 4. köşesi
				path=astar.getPath();
				if(!(path.isEmpty()))//ve ben oraya gidebilen bi yol bulabiliosam
				{
					bomb=new Bomb(dk,tile);//aynı zamanda bomba bırakırsam kaçacak yer bulabiliosam
					if((bomb.pathAvailable()))
					{
						goahead=false;//o halde yolu bulduk demektir daha fazla incelemeye gerek yok
						targetRivalEnded=false;
						hasPathFound=true;
					}
				}
			}
			if(goahead)//rakip sıkıştırılamıosa zorlama, etki edebileceğin herhangi bi yere git
			{
				path=new AiPath();
				if(available.contains(dk.getHero().getTile()))//bunlardan biri, zaten bulunduğum yerse
				{

					if(dk.getMatrix().ManhattanDistance(dk.getHero().getTile(), rivals.get(rivalavailable[dk.getHero().getTile().getLine()][dk.getHero().getTile().getCol()]))>1)
					{
						//yolu bulduk ama şimdi bombayı 2 duvar arasına koysun die bi işlem daha deneyelim
						index=rivalavailable[dk.getHero().getTile().getLine()][dk.getHero().getTile().getCol()];
						if(dk.getHero().getLine()==rivals.get(index).getLine())//eğer aynı satırda iseler
						{
							AiTile up=dk.getHero().getTile().getNeighbor(Direction.UP);//üstündeki tile
							AiTile down=dk.getHero().getTile().getNeighbor(Direction.DOWN);//altındaki tile
							if(dk.getMatrix().getAreaMatrix()[up.getLine()][up.getCol()]<State.DESTRUCTIBLE// eğer alt veya üst bomba etkisi geçiren yerler değilse
								|| dk.getMatrix().getAreaMatrix()[down.getLine()][down.getCol()]<State.DESTRUCTIBLE)
							{
								if(dk.getHero().getTile().getCol()>rivals.get(index).getCol())//eğer rakip solda kalıosa
								{
									up=up.getNeighbor(Direction.LEFT);
									down=down.getNeighbor(Direction.LEFT);
									if(dk.getMatrix().getAreaMatrix()[up.getLine()][up.getCol()]>State.RIVALDANGERR//eğer soldakinin altı da üstü de etki geçirmeyen yerlerse orayı path e ekle
											&& dk.getMatrix().getAreaMatrix()[down.getLine()][down.getCol()]>State.RIVALDANGERR)
										path.addTile(dk.getHero().getTile().getNeighbor(Direction.LEFT));
								}
								else//rakip sağda kalıosa
								{
									up=up.getNeighbor(Direction.RIGHT);
									down=down.getNeighbor(Direction.RIGHT);
									if(dk.getMatrix().getAreaMatrix()[up.getLine()][up.getCol()]>State.RIVALDANGERR
											&& dk.getMatrix().getAreaMatrix()[down.getLine()][down.getCol()]>State.RIVALDANGERR)
										path.addTile(dk.getHero().getTile().getNeighbor(Direction.RIGHT));
								}
							}
						}
						else if(dk.getHero().getTile().getCol()==rivals.get(index).getCol())//eğer aynı sütunda iseler
						{
							AiTile left=dk.getHero().getTile().getNeighbor(Direction.LEFT);
							AiTile right=dk.getHero().getTile().getNeighbor(Direction.RIGHT);
							if(dk.getMatrix().getAreaMatrix()[left.getLine()][left.getCol()]<State.DESTRUCTIBLE
								|| dk.getMatrix().getAreaMatrix()[right.getLine()][right.getCol()]<State.DESTRUCTIBLE)
							{
								if(dk.getHero().getTile().getLine()>rivals.get(index).getLine())
								{
									left=left.getNeighbor(Direction.UP);
									right=right.getNeighbor(Direction.UP);
									if(dk.getMatrix().getAreaMatrix()[left.getLine()][left.getCol()]>State.RIVALDANGERR
											&& dk.getMatrix().getAreaMatrix()[right.getLine()][right.getCol()]>State.RIVALDANGERR)
										path.addTile(dk.getHero().getTile().getNeighbor(Direction.UP));
								}
								else
								{
									left=left.getNeighbor(Direction.DOWN);
									right=right.getNeighbor(Direction.DOWN);
									if(dk.getMatrix().getAreaMatrix()[left.getLine()][left.getCol()]>State.RIVALDANGERR
											&& dk.getMatrix().getAreaMatrix()[right.getLine()][right.getCol()]>State.RIVALDANGERR)
										path.addTile(dk.getHero().getTile().getNeighbor(Direction.DOWN));
								}
							}
						}
					}
					
					
					if(!(path.isEmpty()))
					{
						bomb=new Bomb(dk,path.getLastTile());
						if(bomb.pathAvailable())
						{						
							targetRivalEnded=false;
							hasPathFound=true;
							goahead=false;
						}
					}
					
					if(goahead)
					{
						bomb=new Bomb(dk,dk.getHero().getTile());
						if(bomb.pathAvailable())
						{						
							targetRivalEnded=true;
							hasPathFound=true;
							goahead=false;
						}
					}
				}
				if(goahead)//değilse yol bulurum
				{
					path=new AiPath();
					do
					{
						dk.checkInterruption();
						astar.findPath(dk.getHero().getTile(), available);
						path=astar.getPath();
						if(path.isEmpty())
							available.clear();
						else
						{
							available=available.subList(available.indexOf(path.getLastTile())+1, available.size());
							bomb=new Bomb(dk,path.getLastTile());
							if(!(bomb.pathAvailable()))
								available.remove(path.getLastTile());
						}
					}while(!path.isEmpty()&&!(bomb.pathAvailable())&&!(available.isEmpty()));
					
					if(!(path.isEmpty()))//eğer bu yol boşsa başarısız oldum
					{
						targetRivalEnded=false;
						hasPathFound=true;						
					}
				}
			}//rakibi sıkıştırma ihtimali var mı yok mu
		}//avaialable var mı yok mu
	}
	
	
	/**
	 * bomba bıraktığımda rakibe etki edecek yerleri bulur
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public List<AiTile> findAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		rivalavailable=new int[dk.getPercepts().getHeight()][dk.getPercepts().getWidth()];
		List<AiTile> result=new ArrayList<AiTile>();
		for(int index=0;index<rivals.size();index++)//rivalların yanı kendisi değil
		{
			dk.checkInterruption();
			boolean right=true,left=true,up=true,down=true;
			int line=rivals.get(index).getLine();
			int col=rivals.get(index).getCol();
			for(int pow=1;pow<=dk.getHero().getBombRange();pow++)
			{
				dk.checkInterruption();
				int cl=0;
				if(left)
				{
					cl=col-pow>=0?col-pow:dk.getPercepts().getWidth()+col-pow;
					if(dk.getMatrix().getAreaMatrix()[line][cl]!=State.FREE
					&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.DANGER
					&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.RIVAL
					&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.RIVALDANGER)
						left=false;
					else 
					{
						result.add(dk.getPercepts().getTile(line, cl));
						rivalavailable[line][cl]=index;
					}
				}
				if(right)
				{
					cl=(col+pow)%dk.getPercepts().getWidth();
					if(dk.getMatrix().getAreaMatrix()[line][cl]!=State.FREE
							&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.DANGER
							&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.RIVAL
							&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.RIVALDANGER)
						right=false;
					else 
					{
						result.add(dk.getPercepts().getTile(line, cl));
						rivalavailable[line][cl]=index;
					}
				}
				if(up)
				{
					cl=line-pow>=0?line-pow:dk.getPercepts().getHeight()+line-pow;
					if(dk.getMatrix().getAreaMatrix()[cl][col]!=State.FREE
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.DANGER
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.RIVAL
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.RIVALDANGER)
						up=false;
					else 
					{
						result.add(dk.getPercepts().getTile(cl, col));
						rivalavailable[cl][col]=index;
					}
				}	
				if(down)
				{
					cl=(line+pow)%dk.getPercepts().getHeight();
					if(dk.getMatrix().getAreaMatrix()[cl][col]!=State.FREE
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.DANGER
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.RIVAL
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.RIVALDANGER)
						down=false;
					else 
					{
						result.add(dk.getPercepts().getTile(cl, col));
						rivalavailable[cl][col]=index;
					}
				}	
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public Direction moveTo()throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		if(!(path.isEmpty()))
		{
			moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
			path.removeTile(0);
		}
		return moveDir;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean pathAvailable() throws StopRequestException
	{
		dk.checkInterruption();
		if(path.isEmpty())
			targetRivalEnded=true;
		return hasPathFound && !targetRivalEnded;
	}	
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean succeed() throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
}
