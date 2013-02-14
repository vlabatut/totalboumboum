package org.totalboumboum.ai.v201314.ais._simplet;

import japa.parser.ParseException;

import java.io.IOException;
import java.net.URLDecoder;

import org.totalboumboum.ai.v201314.adapter.agent.AiManager;
import org.totalboumboum.tools.classes.ClassTools;

/**
 * Classe utilisée par le moteur du jeu pour retrouver les agents.
 * L'objet créé dans le constructeur de cette classe doit être de la
 * classe principale de l'agent.
 * <br/>
 * Les directives {@code imports} doivent être modifiées de manière
 * à utiliser la version la plus appropriée de l'agent : {@code v1},
 * {@code v2}, {@code v3}, etc. 
 * 
 * @author Vincent Labatut
 */
public class AiMain extends AiManager
{
	/**
	 * Constructeur utilisé pour créer une instance de l'IA.
	 * <br/>
	 * L'objet créé dans le constructeur de cette classe doit être de la
	 * classe principale de l'agent.
	 */
	public AiMain()
	{	super(new Simplet());		
	}
	
	/////////////////////////////////////////////////////////////////
	// VERIFICATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * L'exécution de cette méthode permet
	 * de faire une vérification du code source
	 * de cet agent.
	 * 
	 * @param args
	 *		Aucun argument n'est nécessaire. 
	 * @throws IOException
	 * 		Problème lors de l'accès aux fichiers. 
	 * @throws ParseException 
	 * 		Problème lors de l'analyse du code source.
	 */
	public static void main(String args[]) throws ParseException, IOException
	{	String path = Simplet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		String pckg = AiMain.class.getPackage().getName();
		String temp[] = pckg.split("\\"+ClassTools.CLASS_SEPARATOR);
		for(String t: temp)
			decodedPath = decodedPath + "/" + t;
		checkSourceCode(decodedPath);
	}
}
