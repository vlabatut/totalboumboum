package tournament200910.danesatir.v1;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;

public class TimeMatrice {

	private double[][] matrice;
	private AiZone zone;
	private List<AiBomb> bombs;
	private DaneSatir ai;
	
	public TimeMatrice(DaneSatir ai) {
		this.ai=ai;
		this.zone=ai.getPercepts();
		matrice = new double[this.zone.getHeigh()][this.zone.getWidth()];
		this.bombs = this.zone.getBombs();
		sortBombes();
		createMatrice(this.bombs);
		return;
	}
	
	public void sortBombes() {
		Collections.sort(this.bombs,new BombComparator());
		if(GeneralFuncs.checkVerboseLevel(4))
			GeneralFuncs.printBombs(bombs);
	}
	
	public void createMatrice(List<AiBomb> bombes) {
		for(AiBomb i : bombes ) {
			expandBomb(i);
		}
		if(GeneralFuncs.checkVerboseLevel(3))
			GeneralFuncs.printMatrice(this.zone.getWidth(), this.zone.getHeigh(), this.matrice);
	}

	private void expandBomb(AiBomb bomb) {
		// TODO Auto-generated method stub
		double duration = GeneralFuncs.getTimeToExplode(bomb);
		
		List<AiTile> blast = bomb.getBlast();
		blast.add(bomb.getTile());
		
		// seek intersections
		for(AiTile i : blast)
			if(getTime(i)!=0 && getTime(i)<duration)
				duration=getTime(i);

		// place duration
		for(AiTile i : blast)
			setTime(i,duration);
	}

	public double getTime(AiTile a) {
		return this.matrice[a.getLine()][a.getCol()];
	}
	
	private void setTime(AiTile a, double duration) {
		this.matrice[a.getLine()][a.getCol()] = duration;
	}
	
	public AiTile mostSafeCase(AiTile a) {
		Stack<AiTile> stack = new Stack<AiTile>();
		Stack<AiTile> processed = new Stack<AiTile>();
		stack.push(a);
		while(!stack.isEmpty()) {
			AiTile temp = stack.pop();
			processed.push(temp);
			System.out.println(temp.getNeighbors().size());
			for(AiTile i : temp.getNeighbors()) {
				System.out.println(i+" "+processed.contains(i)+" "+processed.size());
				if(i.isCrossableBy(this.ai.getOwnHero()) && !(processed.contains(i))) {
					if(getTime(i) == 0)
						return i;
					stack.push(i);
				}
			}

		}
		return null;
	}
}
