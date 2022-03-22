package it.polimi.ingsw.LM34.Model.Effects.GameSpaceRelatedBonus;

import it.polimi.ingsw.LM34.Controller.AbstractGameContext;
import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.DiceDependentContexts.HarvestAreaContext;
import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.DiceDependentContexts.ProductionAreaContext;
import it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.SpecialContexts.FamilyMemberSelectionContext;
import it.polimi.ingsw.LM34.Enums.Controller.ContextType;
import it.polimi.ingsw.LM34.Enums.Controller.PlayerSelectableContexts;
import it.polimi.ingsw.LM34.Exceptions.Controller.NetworkConnectionException;
import it.polimi.ingsw.LM34.Exceptions.Controller.NotAvailableSpace;
import it.polimi.ingsw.LM34.Exceptions.Controller.NotEnoughServantsException;
import it.polimi.ingsw.LM34.Exceptions.Model.OccupiedSlotException;
import it.polimi.ingsw.LM34.Exceptions.Validation.IncorrectInputException;
import it.polimi.ingsw.LM34.Model.Effects.AbstractEffect;
import it.polimi.ingsw.LM34.Network.PlayerAction;
import it.polimi.ingsw.LM34.Utils.Validator;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.logging.Level;

import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.*;
import static it.polimi.ingsw.LM34.Utils.Utilities.LOGGER;

public class WorkingAreaValueEffect extends AbstractEffect implements Observer {
    private static final long serialVersionUID = -6008637768397328624L;
    
    private ContextType influencableContext; //PRODUCTION_CONTEXT OR HARVEST_CONTEXT
    private Integer diceValue;

    /**
     *The additional value on dice is absolute or relative depending on the card effects
     */
    private Boolean isRelative;

    public WorkingAreaValueEffect(ContextType influenceableContext, Integer value, Boolean relative) {
        this.influencableContext = influenceableContext;
        this.diceValue = value;
        this.isRelative = relative;
    }

    public ContextType getType() {
        return this.influencableContext;
    }

    public Integer getValue() {
        return this.diceValue;
    }

    public Boolean isRelative() {
        return this.isRelative;
    }

    @Override
    public void update(Observable o, Object arg) {
        AbstractGameContext callerContext = (AbstractGameContext) arg;

        if(callerContext instanceof FamilyMemberSelectionContext)
            ((FamilyMemberSelectionContext) callerContext).changeFamilyMemberValue(this.diceValue, this.isRelative, influencableContext);
        else if(callerContext instanceof ProductionAreaContext)
            ((ProductionAreaContext) callerContext.getContextByType(PRODUCTION_AREA_CONTEXT)).noFamilyMemberRequired();
        else if(callerContext instanceof HarvestAreaContext)
            ((HarvestAreaContext) callerContext.getContextByType(HARVEST_AREA_CONTEXT)).noFamilyMemberRequired();
    }

    @Override
    public void applyEffect(AbstractGameContext callerContext)  {
        if(this.isRelative)
            callerContext.getContextByType(FAMILY_MEMBER_SELECTION_CONTEXT).addObserver(this);
        else {
            AbstractGameContext context = callerContext.getContextByType(this.influencableContext);
            context.addObserver(this);
            playerInteraction(context, Optional.empty());
            context.deleteObserver(this);
        }
    }

    private void playerInteraction(AbstractGameContext context, Optional<Exception> error) {
        if(context.getCurrentPlayer().isConnected())
            try {
                PlayerAction action = context.getGameManager().getActivePlayerNetworkController().freeAction(
                        new PlayerAction(PlayerSelectableContexts.valueOf(this.influencableContext.name()), this.diceValue), error);

                Validator.checkPlayerActionValidity(action);

                if(action.getContext() == null)
                    return;

                if(action.getContext().name().equals(HARVEST_AREA_CONTEXT.name()))
                    ((HarvestAreaContext) context).interactWithPlayer(action.getAction(), this.diceValue);
                else if(action.getContext().name().equals(PRODUCTION_AREA_CONTEXT.name()))
                    ((ProductionAreaContext) context).interactWithPlayer(action.getAction(), this.diceValue);
                else
                    throw new IncorrectInputException();
            } catch (IncorrectInputException |
                    NotEnoughServantsException |
                    NotAvailableSpace |
                    OccupiedSlotException ex) {
                LOGGER.log(Level.WARNING, ex.getMessage(), ex);
                playerInteraction(context, Optional.of(ex));
            } catch (NetworkConnectionException ex) {
                LOGGER.log(Level.INFO, ex.getMessage(), ex);
                context.getCurrentPlayer().setDisconnected();
            }
    }
}
