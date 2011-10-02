package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v1;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public abstract class Mode {

	public abstract Matrix calculateMatrix() throws StopRequestException;
}
