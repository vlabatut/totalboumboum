package fr.free.totalboumboum.ai.adapter200809;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


import fr.free.totalboumboum.ai.InterfaceAi;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.player.Player;

public class Adapter implements InterfaceAi
{

	private Player player;
//	private Configuration configuration;

	public Adapter(ArtificialIntelligence ai)
	{	this.ai = ai;
	}

	@Override
	public void init(String instance, Player player)
	{	this.player = player;
//		configuration = player.getConfiguration();
	}

	@Override
	public void finish()
	{	
	}

	@Override
	public void update()
	{	
	}

    /** objet implémentant l'IA */
    private ArtificialIntelligence ai;
    /** gestionnaire de threads pour exécuter l'IA */
    transient private ExecutorService executorAi = null;
    /** future utilisé pour récupérer le résultat de l'IA */
    transient private Future<Integer> futureAi;
	
	
	
	
}
