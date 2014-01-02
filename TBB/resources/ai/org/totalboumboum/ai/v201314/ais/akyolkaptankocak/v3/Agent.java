package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v3;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;

/**
 * This is the main Class for an non-human controlled AI agent.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class Agent extends ArtificialIntelligence
{
    /**
     * Instantiate the principal class of the agent.
     */
    public Agent()
    {
        checkInterruption();
    }

    @Override
    protected void initOthers()
    {
        checkInterruption();

        // cf. la Javadoc dans ArtificialIntelligence pour une description de la
        // méthode
    }

    // ///////////////////////////////////////////////////////////////
    // PERCEPTS /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected void initPercepts()
    {
        checkInterruption();

        // cf. la Javadoc dans ArtificialIntelligence pour une description de la
        // méthode
    }

    @Override
    protected void updatePercepts()
    {
        checkInterruption();

        // activate/deactivate the output text
        verbose = false;
        modeHandler.verbose = false;
        preferenceHandler.verbose = false;
        bombHandler.verbose = false;
        moveHandler.verbose = false;

        // cf. la Javadoc dans ArtificialIntelligence pour une description de la
        // méthode
    }

    // ///////////////////////////////////////////////////////////////
    // HANDLERS /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    /** Gestionnaire chargé de calculer le mode de l'agent */
    protected ModeHandler modeHandler;
    /** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
    protected PreferenceHandler preferenceHandler;
    /** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
    protected BombHandler bombHandler;
    /** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
    protected MoveHandler moveHandler;

    @Override
    protected void initHandlers()
    {
        checkInterruption();

        // création des gestionnaires standard (obligatoires)
        modeHandler = new ModeHandler(this);
        preferenceHandler = new PreferenceHandler(this);
        bombHandler = new BombHandler(this);
        moveHandler = new MoveHandler(this);

        // cf. la Javadoc dans ArtificialIntelligence pour une description de la
        // méthode
    }

    @Override
    protected AiModeHandler<Agent> getModeHandler()
    {
        checkInterruption();
        return modeHandler;
    }

    @Override
    protected AiPreferenceHandler<Agent> getPreferenceHandler()
    {
        checkInterruption();
        return preferenceHandler;
    }

    @Override
    protected AiBombHandler<Agent> getBombHandler()
    {
        checkInterruption();
        return bombHandler;
    }

    @Override
    protected AiMoveHandler<Agent> getMoveHandler()
    {
        checkInterruption();
        return moveHandler;
    }

    // ///////////////////////////////////////////////////////////////
    // OUTPUT /////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////
    @Override
    protected void updateOutput()
    {
        checkInterruption();

        // vous pouvez changer la taille du texte affiché, si nécessaire
        // attention: il s'agit d'un coefficient multiplicateur
        AiOutput output = getOutput();
        output.setTextSize(2);

        // ici, par défaut on affiche :
        // les chemins et destinations courants
        moveHandler.updateOutput();
        // les preferences courantes
        preferenceHandler.updateOutput();

        // cf. la Javadoc dans ArtificialIntelligence pour une description de la
        // méthode
    }
}
