package org.totalboumboum.ai.v201011.ais.isbirkoyas.v4;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class PosageBombe {
	IsbirKoyas ai = new IsbirKoyas();
	public boolean print = false;

	private Securite securite = null;

	public PosageBombe(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;

	}

	/**
	 * Methode qui fait la decision de poser la bombe en mode collecte
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return poserBombe
	 */
	public boolean deciderPoserBombeCollect(AiZone gameZone)
			throws StopRequestException {
		Securite securite = new Securite(ai);
		AiTile tile = gameZone.getTile(ai.ourHero.getLine(),
				ai.ourHero.getCol());
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			ai.checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible())
				if (securite.controle(tile, gameZone)
						&& !securite.plein(gameZone, tile)) {
					ai.poserBombe = true;
				} else
					ai.poserBombe = false;
		}
		if (print)
			System.out.println("Collect: Poser bombe=" + ai.poserBombe);
		return ai.poserBombe;
	}

	/**
	 * Methode qui fait la decision de poser la bombe en mode attaque
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return poserBombe
	 */
	public boolean deciderPoserBombeAttaque(AiZone gameZone)
			throws StopRequestException {

		Securite securite = new Securite(ai);
		AiTile tile = gameZone.getTile(ai.ourHero.getLine(),
				ai.ourHero.getCol());
		if (!securite.plein(gameZone, tile)
				&& securite.controle(tile, gameZone))
			ai.poserBombe = true;
		else
			ai.poserBombe = false;
		if (print)
			System.out.println("Attaque: Poser bombe=" + ai.poserBombe);
		return ai.poserBombe;
	}

	// LES METHODES D'ACCES

	public Securite Securite() throws StopRequestException {
		ai.checkInterruption();
		return securite;
	}
}
