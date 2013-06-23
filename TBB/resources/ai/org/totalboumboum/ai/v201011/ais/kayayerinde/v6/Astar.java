package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;



public class Astar {
	
	private KayaYerinde ky;
	private AiPath path;
	private int gidilenTur;
	int i=0;
	private boolean yaz;
	private static final int ARA=10;
	private boolean sictin;

	public Astar(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.ky=onder;
	}
	
	
	public boolean yolBul(AiTile baslangic,AiTile son,int gidilenTur,Matris matris) throws StopRequestException
	{
		ky.checkInterruption();
		if(baslangic==son)
			return false;

		if(son.getNeighbors().contains(baslangic))
		{
			if(matris.getVarlikMatrisi()[son.getLine()][son.getCol()]>Matris.DANGER)
				return false;
		}
		
		
		
		ArrayList<Node> open=new ArrayList<Node>();
		ArrayList<Node> closed=new ArrayList<Node>();
		path=new AiPath();
		this.gidilenTur=gidilenTur;
		Node sonuc;
		boolean bulundu=false;
		
		
		Node bas=new Node(baslangic,null,0,mesafe(baslangic, son,matris));
		sonuc=bas;
		
		open.add(bas);
		
		while(!open.isEmpty() && !bulundu)
		{
			ky.checkInterruption();
			Node siradaki=open.remove(0);
			closed.add(siradaki);
			if(siradaki.own==son || gidilenTur==matris.getVarlikMatrisi()[siradaki.own.getLine()][siradaki.own.getCol()])
			{
				bulundu=true;
				sonuc=siradaki;
				break;
			}
			ArrayList<AiTile> komsular=(ArrayList<AiTile>)siradaki.own.getNeighbors();
			
			for(AiTile komsuT:komsular)
			{
				ky.checkInterruption();
				if(komsuT==son || gidilenTur==matris.getVarlikMatrisi()[siradaki.own.getLine()][siradaki.own.getCol()])
				{
					bulundu=true;
					sonuc=new Node(komsuT,siradaki,siradaki.G,mesafe(komsuT, son,matris));
					break;
				}
				if(tileListedeMi(komsuT, closed)==null)
				{
					int tur=matris.getVarlikMatrisi()[komsuT.getLine()][komsuT.getCol()];
					if(tur<Matris.DESTRUCTIBLE)
					{
						Node komsuN=tileListedeMi(komsuT, open);
						if(komsuN==null)
						{
							komsuN=new Node(komsuT,siradaki,siradaki.G,mesafe(komsuT, son,matris));
							if(sictin || tur<Matris.BONUSDANGER)
							{
								siraliEkle(open, komsuN);
							}
							else
							{
								int i=0;
								Node x=komsuN;
								while(x.parent!=null)
								{
									ky.checkInterruption();
									i++;
									x=x.parent;
								}
								i++;
								double kalanZaman=matris.getKalanZamanMatrisi()[komsuT.getLine()][komsuT.getCol()];
								double gerekenZaman=(double)(i*1000*komsuT.getSize()/ky.getOwnHero().getWalkingSpeed())+200;
								if(kalanZaman>gerekenZaman)
									siraliEkle(open, komsuN);
							}
						}
						else
						{
							if(komsuN.G<siradaki.G+ARA)
							{
								komsuN.parent=siradaki;
								komsuN.G=siradaki.G+ARA;
							}
						}
					}
				}
			}
		}
		
		if(bulundu)
		{
			if(gidilenTur==Matris.DESTRUCTIBLE)
			{
				if(matris.getVarlikMatrisi()[sonuc.parent.own.getLine()][sonuc.parent.own.getCol()]>Matris.BONUS)
					return false;
			}
			while(sonuc.parent!=null)
			{
				ky.checkInterruption();
				path.addTile(0,sonuc.own);
				sonuc=sonuc.parent;
			}
			path.addTile(0,baslangic);
			return true;
		}
		
		return false;
	}
	
	
	private int mesafe(AiTile t1,AiTile t2,Matris m) throws StopRequestException
	{
		ky.checkInterruption();
		int result=real(t1, t2)*ARA;
		for(int i=Math.min(t1.getLine(), t2.getLine());i<Math.max(t1.getLine(), t2.getLine());i++)
		{
			ky.checkInterruption();
			if(m.getVarlikMatrisi()[i][t1.getCol()]>Matris.BONUSDANGER)
				result+=50;
			if(m.getVarlikMatrisi()[i][t2.getCol()]>Matris.BONUSDANGER)
				result+=50;
		}
		for(int i=Math.min(t1.getCol(), t2.getCol());i<Math.max(t1.getCol(), t2.getCol());i++)
		{
			ky.checkInterruption();
			if(m.getVarlikMatrisi()[t1.getLine()][i]>Matris.BONUSDANGER)
				result+=50;
			if(m.getVarlikMatrisi()[t2.getLine()][i]>Matris.BONUSDANGER)
				result+=50;
		}
		return result;
	}
	
	public int real(AiTile t1,AiTile t2) throws StopRequestException
	{
		ky.checkInterruption();
		int l=Math.abs(t1.getLine()-t2.getLine());
		int c=Math.abs(t1.getCol()-t2.getCol());
		return (int)Math.sqrt(l*l+c*c);
	}
	
	private void siraliEkle(ArrayList<Node> list,Node eleman) throws StopRequestException
	{
		ky.checkInterruption();
		if(list.isEmpty())
			list.add(eleman);
		else
		{
			int i=0;
			while(i<list.size() && list.get(i).getF()<eleman.getF())
			{
				ky.checkInterruption();
				i++;
			}
			list.add(i,eleman);
		}
	}
	

	private Node tileListedeMi(AiTile tile,ArrayList<Node> list) throws StopRequestException
	{
		ky.checkInterruption();
		Node node=null;
		if(list.size()>0)// si la list n'est pas vide
			for(Node x:list)
			{
				ky.checkInterruption();
				if(x.own==tile)
				{
					node=x;
					break;
				}
			}
		return node;
	}
	

	
	public AiPath getPath() throws StopRequestException
	{
		ky.checkInterruption();
		return path;
	}
	public void setPath(AiPath path) throws StopRequestException
	{
		ky.checkInterruption();
		this.path=path;
	}
	public int getTur() throws StopRequestException
	{
		ky.checkInterruption();
		return gidilenTur;
	}
	public void setTur(int gidilenTur) throws StopRequestException
	{
		ky.checkInterruption();
		this.gidilenTur=gidilenTur;
	}
	
	public int yoldaMi() throws StopRequestException
	{
		ky.checkInterruption();
		while(path.getLength()>1 && path.getTile(0)!=ky.getOwnHero().getTile())
		{
			ky.checkInterruption();
			path.removeTile(0);
		}
		if(path.getLength()==0)
			return -1;
		if(path.getLength()==1 || (path.getLength()==2 && gidilenTur>Matris.BONUSDANGER))
			return 0;
		return 1;
	}
	public AiAction getNextAction(Matris matris) throws StopRequestException
	{
		ky.checkInterruption();
		if(yaz && gidilenTur==Matris.BONUS)
			System.err.print("bonus ");

		else if(yaz && gidilenTur==Matris.DESTRUCTIBLE)
			System.err.print("duvar ");

		else if(yaz && gidilenTur==Matris.FREE)
			System.err.print("safe ");
		if(yaz)
			System.err.println(path.toString());
		AiAction result=new AiAction(AiActionName.NONE);
		AiTile next=path.getTile(1);
		double zaman=1000*(next.getSize()/ky.getOwnHero().getWalkingSpeed());

		if(matris.getVarlikMatrisi()[next.getLine()][next.getCol()]<Matris.DESTRUCTIBLE)
			if(sictin || matris.getKalanZamanMatrisi()[next.getLine()][next.getCol()]>zaman)
				result=new AiAction(AiActionName.MOVE,ky.getPercepts().getDirection(path.getTile(0), next));
		return result;
	}
	
	public void sictinAc() throws StopRequestException
	{
		ky.checkInterruption();
		sictin=true;
	}
	public void sictinKapat() throws StopRequestException
	{
		ky.checkInterruption();
		sictin=false;
	}
	
	class Node
	{
		private int G;
		private int H;
		
		private Node parent;
		private AiTile own;
		
		public Node(AiTile own,Node parent,int G,int H) throws StopRequestException
		{
			ky.checkInterruption();
			this.own=own;
			this.parent=parent;
			this.G=G;
			this.H=H;
		}
		
		public int getF() throws StopRequestException
		{
			ky.checkInterruption();
			return G+H;
		}
	}

}
