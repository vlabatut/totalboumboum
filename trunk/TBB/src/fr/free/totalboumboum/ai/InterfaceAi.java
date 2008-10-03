package fr.free.totalboumboum.ai;

import fr.free.totalboumboum.engine.player.Player;

public interface InterfaceAi
{
	public void init(String instance, Player player);
	public void update();
	public void finish();
	
}
