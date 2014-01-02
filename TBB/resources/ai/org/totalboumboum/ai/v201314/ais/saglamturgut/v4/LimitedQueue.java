package org.totalboumboum.ai.v201314.ais.saglamturgut.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;

import java.util.LinkedList;

/**
 * A simple limited queue implementation, used to hold the last N tiles of the agent.
 *
 * @param <E> Type of elements.
 *
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class LimitedQueue<E> extends AiAbstractHandler<Agent> {

	/**
	 * The linked list which holds the last N elements inside.
	 */
	private LinkedList<E> limitedQueue;

	/** The maximum number of objects to hold. */
	private final int limit;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit être appelée par une classe héritant de celle-ci
	 * grâce au mot-clé {@code this}.
	 *
	 * @param ai l'agent que cette classe doit gérer.
	 * @param limit The maximum number of tiles to hold
	 */
	protected LimitedQueue(Agent ai, int limit) {
		super(ai);
		ai.checkInterruption();
		limitedQueue = new LinkedList<E>();
		this.limit = limit;
	}

	/**
	 * Adds an element to the limited queue.
	 * @param o element to add.
	 * @return true if the add operation is successful, false otherwise.
	 */
	public boolean add(E o) {
		ai.checkInterruption();

		limitedQueue.add(o);
		while (limitedQueue.size() > limit) {
			ai.checkInterruption();
			limitedQueue.remove();
		}
		return true;
	}

	/**
	 * Returns the last N tiles of the agent.
	 * @return The last N tiles of the agent.
	 */
	public LinkedList<E> getTiles() {
		ai.checkInterruption();
		return limitedQueue;
	}
}