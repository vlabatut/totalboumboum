package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.util;

import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.Agent;

import java.util.LinkedList;

/**
 * A simple limited queue implementation.
 *
 * @param <E> Type of elements.
 *
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class LimitedQueue<E> extends LinkedList<E> {
	/** Required for proper serialization. */
	private static final long serialVersionUID = 546815586017594143L;

	/** The agent object (which can fire stop request). */
	private Agent agent;

	/** The maximum number of objects to hold. */
	private final int limit;

	/**
	 * Creates the queue.
	 * @param agent The agent.
	 * @param limit Limit number of elements to hold.
	 */
	public LimitedQueue(Agent agent, int limit) {
		agent.checkInterruption();
		this.agent = agent;
		this.limit = limit;
	}

	@Override
	public boolean add(E o) {
		agent.checkInterruption();
		super.add(o);
		while (size() > limit) {
			agent.checkInterruption();
			super.remove();
		}
		return true;
	}
}