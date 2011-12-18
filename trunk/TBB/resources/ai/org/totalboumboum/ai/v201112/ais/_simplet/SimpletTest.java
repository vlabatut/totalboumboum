package org.totalboumboum.ai.v201112.ais._simplet;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201112.adapter.test.InitData;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Cette classe est un exemple de la manière de tester
 * un agent hors-ligne, c'est à dire sans lancer le jeu lui-même.
 * L'intérêt est de pouvoir définir programmatiquement exactement
 * la zone que l'on veut en utilisant {@link AiSimZone} : on peut
 * placer tous les objets et agents où on veut, dans l'état qu'on 
 * veut. Pour cet exemple, on utilise la zone créée dans les exemples
 * de l'API, i.e. la classe {@link InitData}.
 * <br/>
 * La limitation est qu'on ne peut pas enchaîner des actions :
 * pour ça on a besoin du moteur du jeu, bien sûr. Donc ce genre
 * de test doit passer par le jeu complet, pas seulement l'API.
 *
 * @author Vincent Labatut
 */
public class SimpletTest
{
	public static void main(String args[]) throws StopRequestException
	{	// on initialise la zone (cf. InitData)
		AiSimZone zone = InitData.initZone();
		
		// on rajoute un adversaire
		AiSimTile tile = zone.getTile(4,3);
		int bombRange = 3;
		int bombNumber = 1;
		PredefinedColor color = PredefinedColor.BLACK;
		zone.createHero(tile,color,bombNumber,bombRange,false);
		
		// on initialise l'agent
		Simplet ai = new Simplet();
		ai.setZone(zone);
		
		// le premier appel initialise les gestionnaires
		ai.call();
		// le second demande à l'agent de calculer son action
		ai.call();
	}
}
