package tournament200809.devecioglukorkmaz;

import java.util.Comparator;

import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;

public class AiTileComparator implements Comparator<AiTile> {

	AiTile t;
	DeveciogluKorkmaz dk;

	public AiTileComparator(AiTile t, DeveciogluKorkmaz dk)
			throws StopRequestException {

		this.t = t;
		dk.checkInterruption();
		this.dk = dk;
	}

	public int compare(AiTile n1, AiTile n2) {
		try {
			dk.checkInterruption();
		} catch (StopRequestException e1) {
			e1.printStackTrace();
		}
		int resultat = 0;
		try {
			if (dk.getDistance(t, n1) > dk.getDistance(t, n2))
				resultat = 1;
			else if (dk.getDistance(t, n1) < dk.getDistance(t, n2))
				resultat = -1;
		} catch (StopRequestException e) {
			e.printStackTrace();
		}
		return resultat;
	}

}
