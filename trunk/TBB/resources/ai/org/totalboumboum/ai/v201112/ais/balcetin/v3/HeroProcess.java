package org.totalboumboum.ai.v201112.ais.balcetin.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Hero process class to get Hero processes.
 * 
 * @author Adnan BAL
 * 
 */
public class HeroProcess {
	BalCetin ai;

	public HeroProcess(BalCetin ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
	}

	/**
	 * Gets Worst Hero which has the least points.
	 * @return Hero which is the worst on the playground.(just counts alive heros)
	 * @throws StopRequestException
	 */
	public AiHero getWorstHero() throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		List<AiHero> opponentHeros = zone.getRemainingOpponents();
		AiHero targetHero = opponentHeros.get(0);
		for (int i = 1; i < opponentHeros.size(); i++) {
			ai.checkInterruption();
			if (opponentHeros.get(i).getMatchRank() > targetHero.getMatchRank())
				targetHero = opponentHeros.get(i);
		}

		return targetHero;

	}

	/**
	 * Gets Nearest Hero to our agent.
	 * 
	 * @return Nearest hero to us.(just counts alive heros)
	 * @throws StopRequestException
	 */
	public AiHero getNearestHero() throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = ai.getZone();

		List<AiHero> opponentHeros = zone.getRemainingOpponents();
		AiHero targetHero = opponentHeros.get(0);
		AiHero ownHero = zone.getOwnHero();
		int distance = zone.getTileDistance(ownHero.getTile(),
				targetHero.getTile());
		for (int i = 1; i < opponentHeros.size(); i++) {
			ai.checkInterruption();
			if (distance > zone.getTileDistance(ownHero.getTile(),
					opponentHeros.get(i).getTile()))
				targetHero = opponentHeros.get(i);
		}

		return targetHero;

	}

	/**
	 * Gets the least effective hero which has minimal ammo.
	 * 
	 * @return The least effective Hero alive.
	 * @throws StopRequestException
	 */
	public AiHero getTheLeastEffectiveHero() throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		List<AiHero> opponentHeros = zone.getRemainingOpponents();
		AiHero targetHero = opponentHeros.get(0);
		for (int i = 1; i < opponentHeros.size(); i++) {
			ai.checkInterruption();
			int extraBombCount = opponentHeros.get(i).getBombNumberMax();

			if (targetHero.getBombNumberMax() > extraBombCount) {
				targetHero = opponentHeros.get(i);
			} else if ((targetHero.getBombNumberMax() == extraBombCount)
					&& (targetHero.getBombRange() > opponentHeros.get(i)
							.getBombRange())) {
				targetHero = opponentHeros.get(i);
			}

		}

		return targetHero;
	}

	/**
	 * Gets heros which we have direct acces by walking.
	 * 
	 * @return Heros which we have direct acces by walking.(just counts alive heros)
	 * @throws StopRequestException
	 */
	public List<AiHero> directlyAccessibleHeros() throws StopRequestException {
		ai.checkInterruption();

		AiZone zone = ai.getZone();
		TileProcess tp = new TileProcess(ai);
		List<AiHero> opponentHeros = zone.getRemainingOpponents();
		List<AiHero> directAccessibleHeros = new ArrayList<AiHero>();

		for (AiHero Hero : opponentHeros) {
			ai.checkInterruption();

			if (tp.getwalkableTiles().contains(Hero.getTile()))
				directAccessibleHeros.add(Hero);
		}

		return directAccessibleHeros;
	}

	/**
	 * Gets most relevant hero for us.
	 * 
	 * @return if the hero is directly accessible, checks if he is the least effective or worst hero or nearest hero in this order. if not, than returns worst hero.
	 * @throws StopRequestException
	 */
	public AiHero mostRelevantHero() throws StopRequestException {
		ai.checkInterruption();
		AiHero targetHero = null;
		if (directlyAccessibleHeros().size() != 0) {
			for (AiHero Hero : directlyAccessibleHeros()) {
				ai.checkInterruption();
				if (Hero == getTheLeastEffectiveHero())
					targetHero = Hero;
				else if (Hero == getWorstHero())
					targetHero = Hero;
				else if (Hero == getNearestHero())
					targetHero = Hero;
			}
			
		} else
			targetHero = getWorstHero();
		return targetHero;

	}

	/**
	 * If our agent puts a bomb on a tile, how many opponent will be dead.
	 *  
	 * @return possible dead hero count . (0 to 3 , just counts alive heros)
	 * @throws StopRequestException
	 */
	public int possibleDeadHeroCountOnPut() throws StopRequestException {
		ai.checkInterruption();
		int count = 0;
		TileProcess tp = new TileProcess(ai);
		AiZone zone = ai.getZone();
		List<AiHero> opponentHeros = zone.getRemainingOpponents();
		for (AiHero aiHero : opponentHeros) {
			ai.checkInterruption();
			if (tp.getDangerousTilesOnBombDrop().contains(aiHero.getTile()))
				count++;
		}
		return count;
	}
}
