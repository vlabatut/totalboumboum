package tournament200910.bektasmazilyah.v4_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class BombMatrice {
	
	private BektasMazilyah source;
	private AiZone map;
	private AiHero hero;
	private Collection<AiBomb> bombes;
	private Collection<AiBlock> blocks;
	//private Collection<AiHero> rivals;
	private double bombMatrice[][];
	private DangerZone dangerZone;
	int x;
	int y;
	
	public BombMatrice(BektasMazilyah source , AiZone zone, DangerZone dangerZone) throws StopRequestException
	{
		source.checkInterruption();
		this.source=source;
		this.map=zone;
		this.hero=map.getOwnHero();
		this.bombes=map.getBombs();
		this.blocks=map.getBlocks();
		//this.rivals=map.getHeroes();
		this.x=map.getWidth();
		this.y=map.getHeigh();
		this.dangerZone=dangerZone;
		init();
	}

	private void init() throws StopRequestException {
		source.checkInterruption();
		bombMatrice= new double[x][y];
		int i,j;
		
		//Initialisation
		for(i = 0; i < x; i++)
		{	
			source.checkInterruption(); //Appel Obligatoire
			for(j = 0; j < y; j++)
			{
				source.checkInterruption(); //Appel Obligatoire
				bombMatrice[i][j] = -1;
			}
		}
		
		//Mettons les blocs
		Iterator <AiBlock> itBlock = blocks.iterator();
		while(itBlock.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBlock temp = itBlock.next();
			bombMatrice[temp.getCol()][temp.getLine()] = 255;
		}
		
		//Mettons les bombes et les feus possibles
		Iterator <AiBomb> itBombes = bombes.iterator();
		while(itBombes.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBomb temp = itBombes.next();
			if(temp.isWorking())
			{		
			//Les tiles dangeureux
			int k = 0;
			//Est-ce qu'on continue sur ces directions?
			boolean up = true;
			boolean down = true;
			boolean left = true;
			boolean right = true;
			while(k < temp.getRange()+1 && (up || down || left || right))
			{
				int a =0;
				source.checkInterruption(); //Appel Obligatoire
				
					AiTile temp1 = temp.getTile();
					while(up && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.UP);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if(bombMatrice[x][y] != 255)
						{
							bombMatrice[x][y] = temp.getNormalDuration()-temp.getTime();
							a++;
						}
						else up = false;
					}
					a = 0;
					temp1 = temp.getTile();
					
				
					while(down && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.DOWN);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if(bombMatrice[x][y] != 255)
						{
							bombMatrice[x][y] = temp.getNormalDuration()-temp.getTime();
							a++;
						}
						else down = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(left && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.LEFT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if(bombMatrice[x][y] != 255)
						{
							bombMatrice[x][y] = temp.getNormalDuration()-temp.getTime();
							a++;
						}
						else left = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(right && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.RIGHT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if(bombMatrice[x][y] != 255)
						{
							bombMatrice[x][y] = temp.getNormalDuration()-temp.getTime();
							a++;
						}
						else right = false;
					}
					a = 0;
					temp1 = temp.getTile();
				k++;
			}			
		}}
		
	
	}
	
	public ArrayList<AiTile> findPlusSafeTiles() throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(getValeur(hero.getCol(),hero.getLine())<getValeur(col, line))
					{
					  
						tile=map.getTile(line, col);
						
						Astar a=new Astar (dangerZone,hero.getCol(),hero.getLine(),col,line);
							if ((hero.getCol()!= col || hero.getLine() != line) && a.findPath())
								result.add(tile);
					}
			}
		}
		System.out.println(result.toString());
		
		return result;
	}	
	
	public double getValeur(int x, int y)
	{
		return bombMatrice[x][y];
	}
	
	public void afficheDangerZone() throws StopRequestException
	{
		source.checkInterruption();
		for(int i = 0; i<map.getWidth() ; i++)
		{
			source.checkInterruption();
			for(int j = 0; j < map.getHeigh(); j++)
			{
				source.checkInterruption();
				System.out.print("("+j+","+i+"): "+ bombMatrice[i][j]);
			}
			System.out.println();
		}
	}

}