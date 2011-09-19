package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;



public class Matris {
	KayaYerinde onder;
	
	private int varlikMatrisi[][];
	private ArrayList<int[][]> yedekVarlik;
	private double kalanZamanMatrisi[][];
	private ArrayList<double[][]> yedekZaman;
	
	private int hypoDuvar=0;
	private int hypoBonus=0;
	private int hypoHero;
	

	public static int FREE=0; 
	public static int BONUS=1;
	public static int RIVAL=2;
	public static int BONUSDANGER=3;
	public static int DANGER=4;
	public static int DESTRUCTIBLE=5;
	public static int DESTRUCTIBLEDANGER=6;
	public static int INDESTRUCTIBLE=7;
	public static int BOMB=8;
	public static int FIRE=9;
	
	public Matris(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		int line=onder.getPercepts().getHeight();
		int column=onder.getPercepts().getWidth();
		varlikMatrisi=new int[line][column];
		kalanZamanMatrisi=new double[line][column];
		yedekVarlik=new ArrayList<int[][]>();
		yedekZaman=new ArrayList<double[][]>();
		processedBombs=new ArrayList<AiBomb>();
	}
	
	public void refresh() throws StopRequestException
	{	
		onder.checkInterruption(); //APPEL OBLIGATOIRE

		processedBombs.clear();
		
		for(int l=0;l<onder.getPercepts().getHeight();l++)
		{
			onder.checkInterruption();
			for(int c=0;c<onder.getPercepts().getWidth();c++)
			{
				onder.checkInterruption();
				varlikMatrisi[l][c]=FREE;
				kalanZamanMatrisi[l][c]=Double.MAX_VALUE;
			}
		}
		
		List<AiBlock> duvarlar=onder.getPercepts().getBlocks();
		for(AiBlock block:duvarlar)
		{
			onder.checkInterruption();
			int l=block.getLine();
			int c=block.getCol();
			if(block.getState().getName()==AiStateName.BURNING)
				varlikMatrisi[l][c] = FIRE;
			else
			{
				if(block.isDestructible())
					varlikMatrisi[l][c] = DESTRUCTIBLE;
				else
					varlikMatrisi[l][c] = INDESTRUCTIBLE;
			}
		}		
		
		List<AiItem> bonuslar=onder.getPercepts().getItems();
		for(AiItem item:bonuslar)
		{
			onder.checkInterruption();
			int l=item.getLine();
			int c=item.getCol();
			if(item.getState().getName()==AiStateName.BURNING)
				varlikMatrisi[l][c] = FIRE;
			else if(!item.hasEnded())
				varlikMatrisi[l][c] = BONUS;
		}
		
		List<AiBomb> bombalar=onder.getPercepts().getBombs();
		for(AiBomb b:bombalar)
		{
			onder.checkInterruption();
			processBomb(b);
			varlikMatrisi[b.getLine()][b.getCol()]=BOMB;
		}
		
		List<AiFire> atesler=onder.getPercepts().getFires();
		for(AiFire fire:atesler)
		{
			onder.checkInterruption();
			varlikMatrisi[fire.getLine()][fire.getCol()]=FIRE;
		}
		List<AiHero> heroes=onder.getPercepts().getRemainingHeroes();
		heroes.remove(onder.getOwnHero());
		for(AiHero h:heroes)
		{
			onder.checkInterruption();
			if(varlikMatrisi[h.getLine()][h.getCol()]<RIVAL)
				varlikMatrisi[h.getLine()][h.getCol()]=RIVAL;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des bombes trait�es au cours de cette it�ration (pour ne pas les traiter plusieurs fois) */
	private List<AiBomb> processedBombs;
	
	
	private List<AiTile> getBlast(AiTile t,boolean incele) throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> blast=new ArrayList<AiTile>();
		blast.add(t);
		int line=t.getLine();
		int col=t.getCol();
		boolean up=true,down=true,left=true,right=true;
		boolean up2=incele,down2=incele,left2=incele,right2=incele;
		int range=onder.getOwnHero().getBombRange();
		ArrayList<AiTile> heroes=getRivals();
		if(onder.getOwnHero().getTile().getHeroes().size()>1)
			hypoHero++;
		for(int i=1;i<=range;i++)
		{
			onder.checkInterruption();
			if(up)
			{
				int l=(line-i+onder.getPercepts().getHeight())%onder.getPercepts().getHeight();
				int tur=varlikMatrisi[l][col];
				if(tur==DESTRUCTIBLE)
				{
					blast.add(onder.getPercepts().getTile(l, col));
					if(up2)
						hypoDuvar++;
					up=false;
				}
				else if(tur==INDESTRUCTIBLE)
					up=false;
				else
				{
					if(up2)
					{
						if(tur==BONUS)
						{
							hypoBonus++;
							up2=false;
						}
						if(tur==DESTRUCTIBLEDANGER || tur==BONUSDANGER)
							up2=false;
					}
					blast.add(onder.getPercepts().getTile(l, col));
				}
				if(up2)
				{
					if(heroes.contains(onder.getPercepts().getTile(l, col)))
						hypoHero++;
				}
			}
			if(down)
			{
				int l=(line+i)%onder.getPercepts().getHeight();
				int tur=varlikMatrisi[l][col];
				if(tur==DESTRUCTIBLE )
				{
					blast.add(onder.getPercepts().getTile(l, col));
					if(down2)
						hypoDuvar++;
					down=false;
				}
				else if(tur==INDESTRUCTIBLE)
					down=false;
				else
				{
					if(down2)
					{
						if(tur==BONUS)
						{
							hypoBonus++;
							down2=false;
						}
						if(tur==DESTRUCTIBLEDANGER || tur==BONUSDANGER)
							down2=false;
					}
					blast.add(onder.getPercepts().getTile(l, col));
				}
				if(down2)
				{
					if(heroes.contains(onder.getPercepts().getTile(l, col)))
						hypoHero++;
				}
			}
			if(left)
			{
				int c=(col-i+onder.getPercepts().getWidth())%onder.getPercepts().getWidth();
				int tur=varlikMatrisi[line][c];
				if(tur==DESTRUCTIBLE )
				{
					blast.add(onder.getPercepts().getTile(line, c));
					if(left2)
						hypoDuvar++;
					left=false;
				}
				else if(tur==INDESTRUCTIBLE)
					left=false;
				else
				{
					if(left2)
					{
						if(tur==BONUS)
						{
							hypoBonus++;
							left2=false;
						}
						if(tur==DESTRUCTIBLEDANGER || tur==BONUSDANGER)
							left2=false;
					}
					blast.add(onder.getPercepts().getTile(line, c));
				}
				if(left2)
				{
					if(heroes.contains(onder.getPercepts().getTile(line, c)))
						hypoHero++;
				}
			}
			if(right)
			{
				int c=(col+i)%onder.getPercepts().getWidth();
				int tur=varlikMatrisi[line][c];
				if(tur==DESTRUCTIBLE )
				{
					blast.add(onder.getPercepts().getTile(line, c));
					if(right2)
						hypoDuvar++;
					right=false;
				}
				else if(tur==INDESTRUCTIBLE)
					right=false;
				else
				{
					if(right2)
					{
						if(tur==BONUS)
						{
							hypoBonus++;
							right2=false;
						}
						if(tur==DESTRUCTIBLEDANGER || tur==BONUSDANGER)
							right2=false;
					}
					blast.add(onder.getPercepts().getTile(line, c));
				}
				if(right2)
				{
					if(heroes.contains(onder.getPercepts().getTile(line, c)))
						hypoHero++;
				}
			}
		}
		return blast;
	}
	
	/**
	 * calcule une liste de cases correspondant au souffle indirect de la bombe
	 * pass�e en param�tre. Le terme "indirect" signifie que la fonction est r�cursive : 
	 * si une case à port�e de souffle contient une bombe, le souffle de cette bombe est rajout�
	 * dans la liste blast, et la bombe est rajout�e dans la liste bombs.
	 */
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs) throws StopRequestException
	{	onder.checkInterruption(); 
	
		if(!bombs.contains(bomb))
		{	bombs.add(bomb);
		
			List<AiTile> tempBlast = getBlast(bomb.getTile(),false);
			blast.addAll(tempBlast);
			
			// bombs
			for(AiTile tile: tempBlast)
			{	onder.checkInterruption(); //APPEL OBLIGATOIRE
				
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
	 * traite la bombe pass�e en param�tre
	 */
	private void processBomb(AiBomb bomb) throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!processedBombs.contains(bomb))
		{			
			List<AiTile> blast = new ArrayList<AiTile>();
			List<AiBomb> bombs = new ArrayList<AiBomb>();
			getBlast(bomb,blast,bombs);
			processedBombs.addAll(bombs);
			
			double value = Double.MAX_VALUE;
			for(AiBomb b: bombs)
			{	
				onder.checkInterruption(); //APPEL OBLIGATOIRE
				
				double time = b.getNormalDuration() - b.getTime();
				if(time<value)
					value = time;
			}
			
			for(AiTile t: blast)
			{	onder.checkInterruption(); 
				
				int l = t.getLine();
				int c = t.getCol();
				if(kalanZamanMatrisi[l][c]>value)
					kalanZamanMatrisi[l][c] = value;
				
				if(varlikMatrisi[l][c]==FREE)
					varlikMatrisi[l][c]=DANGER;
				else if(varlikMatrisi[l][c]==BONUS)
					varlikMatrisi[l][c]=BONUSDANGER;
				else if(varlikMatrisi[l][c]==DESTRUCTIBLE)
					varlikMatrisi[l][c]=DESTRUCTIBLEDANGER;
			}
		}
	}
	
	public int[][] getVarlikMatrisi() throws StopRequestException
	{
		onder.checkInterruption();
		return varlikMatrisi;
	}
	public double[][] getKalanZamanMatrisi() throws StopRequestException
	{
		onder.checkInterruption();
		return kalanZamanMatrisi;
	}
	
	public boolean BonusVarMi() throws StopRequestException
	{
		onder.checkInterruption();
		if(onder.getPercepts().getHiddenItemsCount()!=0)
			return true;
		return false;
	}
	
	
	
	public void hypoteticalBomb(AiTile t) throws StopRequestException
	{
		onder.checkInterruption();
		yedekVarlik.add(clone(varlikMatrisi));
		yedekZaman.add(clone(kalanZamanMatrisi));
		
		hypoBonus=0;
		hypoDuvar=0;
		hypoHero=0;
		
		ArrayList<AiBomb> bombs=new ArrayList<AiBomb>();
		ArrayList<AiTile> blast=new ArrayList<AiTile>();
		ArrayList<AiTile> temp=(ArrayList<AiTile>)getBlast(t,true);
		blast.addAll(temp);
		for(AiTile tile: temp)
		{	onder.checkInterruption(); 
			
			Collection<AiBomb> bList = tile.getBombs();
			if(bList.size()>0)
			{	AiBomb b = bList.iterator().next();
				getBlast(b,blast,bombs);
			}
		}
	
		double value=2500;
		
		for(AiBomb b: bombs)
		{	
			onder.checkInterruption();
			
			double time = b.getNormalDuration() - b.getTime();
			if(time<value)
				value = time;
		}
		
		for(AiTile x: blast)
		{	onder.checkInterruption(); 
			
			int l = x.getLine();
			int c = x.getCol();
			if(kalanZamanMatrisi[l][c]>value)
				kalanZamanMatrisi[l][c] = value;
			
			if(varlikMatrisi[l][c]==FREE)
				varlikMatrisi[l][c]=DANGER;
			else if(varlikMatrisi[l][c]==BONUS)
				varlikMatrisi[l][c]=BONUSDANGER;
			else if(varlikMatrisi[l][c]==DESTRUCTIBLE)
				varlikMatrisi[l][c]=DESTRUCTIBLEDANGER;
		}

		varlikMatrisi[t.getLine()][t.getCol()]=BOMB;
		
	}

	public void Undo() throws StopRequestException
	{
		onder.checkInterruption();
		varlikMatrisi=yedekVarlik.remove(0);
		kalanZamanMatrisi=yedekZaman.remove(0);
		yedekVarlik=new ArrayList<int[][]>();
		yedekZaman=new ArrayList<double[][]>();
	}
	
	public int getHypoDuvar() throws StopRequestException
	{
		onder.checkInterruption();
		return hypoDuvar;
	}

	public int getHypoBonus() throws StopRequestException
	{
		onder.checkInterruption();
		return hypoBonus;
	}
	
	public int getHypoRivals() throws StopRequestException
	{
		onder.checkInterruption();
		return hypoHero;
	}
	
	public ArrayList<AiTile> getDest() throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> dests=new ArrayList<AiTile>();
		for(int l=0;l<onder.getPercepts().getHeight();l++)
		{
			onder.checkInterruption();
			for(int c=0;c<onder.getPercepts().getWidth();c++)
			{
				onder.checkInterruption();
				if(varlikMatrisi[l][c]==DESTRUCTIBLE)
				{
					AiTile t=onder.getPercepts().getTile(l, c);
					if(etrafi(t))
						dests.add(t);
				}
			}
		}
		return Sort(dests);
	}
	
	public ArrayList<AiTile> getRivals() throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> result=new ArrayList<AiTile>();
		for(AiHero h:onder.getPercepts().getRemainingHeroes())
		{
			onder.checkInterruption();
			if(!h.equals(onder.getOwnHero()) )
				result.add(h.getTile());
		}
		return result;
	}
	
	private boolean etrafi(AiTile t) throws StopRequestException
	{
		onder.checkInterruption();
		List<AiTile> komsular=t.getNeighbors();
		for(AiTile x:komsular)
		{
			onder.checkInterruption();
			if(varlikMatrisi[x.getLine()][x.getCol()]<DANGER)
				return true;
		}
		return false;
	}

	public ArrayList<AiTile> getFrees() throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> safes=new ArrayList<AiTile>();
		for(int l=0;l<onder.getPercepts().getHeight();l++)
		{
			onder.checkInterruption();
			for(int c=0;c<onder.getPercepts().getWidth();c++)
			{
				onder.checkInterruption();
				if(varlikMatrisi[l][c]==FREE)
					safes.add(onder.getPercepts().getTile(l, c));
			}
		}
		return Sort(safes);
	}
	
	public ArrayList<AiTile> getBonus() throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> result=new ArrayList<AiTile>();
		for(AiItem i:onder.getPercepts().getItems())
		{
			onder.checkInterruption();
			AiTile t=i.getTile();
			if(varlikMatrisi[t.getLine()][t.getCol()]==BONUS)
				result.add(t);
		}
		return Sort(result);
	}
	
	private ArrayList<AiTile> Sort(ArrayList<AiTile> list) throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<Integer> mesafeler=new ArrayList<Integer>();
		for(int i=0;i<list.size();i++)
		{
			onder.checkInterruption();
			int l=Math.abs(list.get(i).getLine()-onder.getOwnHero().getTile().getLine());
			int c=Math.abs(list.get(i).getCol()-onder.getOwnHero().getTile().getCol());
			mesafeler.add((l+c)*10);
		}
		return quickSort(list, mesafeler);
	}
	
 
	private ArrayList<AiTile> quickSort(ArrayList<AiTile> list,ArrayList<Integer> value) throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> sagl=new ArrayList<AiTile>();
		ArrayList<AiTile> soll=new ArrayList<AiTile>();
		if(list.size()>1)
		{	
			int pivot=list.size()/2;
			
			ArrayList<Integer> sagI=new ArrayList<Integer>();
			ArrayList<Integer> solI=new ArrayList<Integer>();
			
			for(int i=0;i<list.size();i++)
			{
				onder.checkInterruption();
				if(i!=pivot)
				{
					if(value.get(i)<=value.get(pivot))
					{
						solI.add(value.get(i));
						soll.add(list.get(i));
					}
					else 
					{
						sagI.add(value.get(i));
						sagl.add(list.get(i));
					}
				}
			}

			soll=quickSort(soll, solI);
			sagl=quickSort(sagl, sagI);
			soll.add(list.get(pivot));
			soll.addAll(sagl);
		}
		else
		{
			return list;
		}
		return soll;
	}
	

	
	public ArrayList<AiTile> getRivalInf() throws StopRequestException
	{
		onder.checkInterruption();
		ArrayList<AiTile> result=new ArrayList<AiTile>();
		for(AiTile t:getRivals())
		{
			onder.checkInterruption();
			boolean up=true,down=true,left=true,right=true;

			for(int i=1;i<=onder.getOwnHero().getBombRange();i++)
			{
				onder.checkInterruption();
				if(up)
				{
					int l=(t.getLine()-i+onder.getPercepts().getHeight())%onder.getPercepts().getHeight();
					int tur=varlikMatrisi[l][t.getCol()];
					if(tur==FREE || tur==RIVAL  || tur==DANGER)
						result.add(onder.getPercepts().getTile(l, t.getCol()));
					else
						up=false;
				}
				if(down)
				{
					int l=(t.getLine()+i)%onder.getPercepts().getHeight();
					int tur=varlikMatrisi[l][t.getCol()];
					if(tur==FREE || tur==RIVAL ||  tur==DANGER)
						result.add(onder.getPercepts().getTile(l, t.getCol()));
					else
						down=false;
				}
				if(left)
				{
					int c=(t.getCol()-i+onder.getPercepts().getWidth())%onder.getPercepts().getWidth();
					int tur=varlikMatrisi[t.getLine()][c];
					if(tur==FREE || tur==RIVAL ||tur==DANGER)
						result.add(onder.getPercepts().getTile(t.getLine(), c));
					else
						left=false;
				}
				if(right)
				{
					int c=(t.getCol()+i)%onder.getPercepts().getWidth();
					int tur=varlikMatrisi[t.getLine()][c];
					if(tur==FREE || tur==RIVAL || tur==DANGER)
						result.add(onder.getPercepts().getTile(t.getLine(), c));
					else
						right=false;
				}
			}
		}
		return Sort(result);
	}
	
	
	private int[][] clone(int[][]list) throws StopRequestException
	{
		onder.checkInterruption();
		int l=onder.getPercepts().getHeight();
		int c=onder.getPercepts().getWidth();
		int m[][]=new int[l][c];
		for(int i=0;i<l;i++)
		{
			onder.checkInterruption();
			for(int j=0;j<c;j++)
			{
				onder.checkInterruption();
				m[i][j]=list[i][j];
			}
		}
		return m;
	}
	
	private double[][] clone(double[][]list) throws StopRequestException
	{
		onder.checkInterruption();
		int l=onder.getPercepts().getHeight();
		int c=onder.getPercepts().getWidth();
		double m[][]=new double[l][c];
		for(int i=0;i<l;i++)
		{
			onder.checkInterruption();
			for(int j=0;j<c;j++)
			{
				onder.checkInterruption();
				m[i][j]=list[i][j];
			}
		}
		return m;
	} 
	
	
	public void print(int deger) throws StopRequestException
	{
		onder.checkInterruption();
		for(int l=0;l<onder.getPercepts().getHeight();l++)
		{
			onder.checkInterruption();
			for(int c=0;c<onder.getPercepts().getWidth();c++)
			{
				onder.checkInterruption();
				if(deger==1)
					System.out.print("\t"+varlikMatrisi[l][c]);
				else
					System.out.print("\t"+kalanZamanMatrisi[l][c]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
}



