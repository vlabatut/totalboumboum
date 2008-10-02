package tournament200809;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.loop.Loop;

public class AiPercepts
{
	
	public AiPercepts(Loop loop)
	{	// zone
		Level level = loop.getLevel();
		initZone(level);	
		// players
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Collection<Collection<AiTile>> lines;
	
	private void initZone(Level level)
	{	Tile[][] matrix = level.getMatrix();
		int lineNbr = level.getGlobalHeight();
		int colNbr = level.getGlobalWidth();
		ArrayList<Collection<AiTile>> tempLines = new ArrayList<Collection<AiTile>>();		
		for(int lineIndex=0;lineIndex<lineNbr;lineIndex++)
		{	ArrayList<AiTile> tempLine = new ArrayList<AiTile>();
			for(int colIndex=0;colIndex<colNbr;colIndex++)
			{	Tile tile = matrix[lineIndex][colIndex];
				AiTile aiTile = new AiTile(lineIndex,colIndex,tile);
				tempLine.add(aiTile);
			}
			Collection<AiTile> line = Collections.unmodifiableCollection(tempLine);
			tempLines.add(line);
		}
		lines = Collections.unmodifiableCollection(tempLines);
	}
	
	
	
	
	
	/* 
	 * stocker la mise à jour en statique et ne recalculer les valeurs
	 * que si le temps a changé (économie de calcul pour les IA utilisant le même adaptateur)
	 */
}
