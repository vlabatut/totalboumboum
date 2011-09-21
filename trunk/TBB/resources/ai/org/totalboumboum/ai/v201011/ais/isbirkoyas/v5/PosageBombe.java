package org.totalboumboum.ai.v201011.ais.isbirkoyas.v5;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class PosageBombe {
	IsbirKoyas ai = new IsbirKoyas();

	private Securite securite = null;

	public PosageBombe(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;

	}

	/**
	 * Cette méthode fait la décision de poser la bombe en mode collecte après
	 * avoir choisi le mode s'il ya des murs destructibles elle retourne true.
	 * Elle prend un seul argument qui est la zone du jeu.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return poserBombe
	 */
	public boolean deciderPoserBombeCollect(AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		Securite securite = new Securite(ai);
		AiTile tile = gameZone.getTile(ai.ourHero.getLine(),
				ai.ourHero.getCol());
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible())
				if (tile.isCrossableBy(ai.ourHero)
						&& !securite.plein(gameZone, tile)) {
					ai.poserBombe = true;
				} else
					ai.poserBombe = false;
		}
		if (ai.print)
			System.out.println("Collect: Poser bombe=" + ai.poserBombe);
		return ai.poserBombe;
	}

	/**
	 * Cette méthode fait la décision de poser la bombe en mode attaque après
	 * avoir choisi le mode s'il ya une case libre sans danger elle retourne
	 * true. Elle prend un seul argument qui est la zone du jeu.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return poserBombe
	 */
	public boolean deciderPoserBombeAttaque(AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		Securite securite = new Securite(ai);
		AiTile tile = gameZone.getTile(ai.ourHero.getLine(),
				ai.ourHero.getCol());
		if (!securite.plein(gameZone, tile) && tile.isCrossableBy(ai.ourHero))
			ai.poserBombe = true;
		else
			ai.poserBombe = false;
		if (ai.print)
			System.out.println("Attaque: Poser bombe=" + ai.poserBombe);
		return ai.poserBombe;
	}

	// LE METHODE D'ACCES
	/**
	 * METHODE D'ACCES a la classe Securite
	 * 
	 * @throws StopRequestException
	 * @return securite
	 */
	public Securite Securite() throws StopRequestException {
		ai.checkInterruption();
		return securite;
	}
}
