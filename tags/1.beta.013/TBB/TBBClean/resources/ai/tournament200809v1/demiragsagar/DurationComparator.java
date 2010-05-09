package tournament200809v1.demiragsagar;

import java.util.Comparator;

import fr.free.totalboumboum.ai.adapter200809.AiBomb;

public class DurationComparator implements Comparator<AiBomb> {
	public int compare(AiBomb bombe1, AiBomb bombe2) {
		int resultat;
		long t1 = bombe1.getNormalDuration();
		long t2 = bombe2.getNormalDuration();

		if (t1 < t2)
			resultat = -1;

		else
			resultat = 1;

		return resultat;

	}

}
