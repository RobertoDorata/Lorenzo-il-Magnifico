package it.polimi.ingsw.LM34.Controller.InteractivePlayerContexts.SpecialContexts;

import it.polimi.ingsw.LM34.Controller.AbstractGameContext;
import it.polimi.ingsw.LM34.Controller.NonInteractiveContexts.ResourceIncomeContext;
import it.polimi.ingsw.LM34.Exceptions.Controller.NetworkConnectionException;
import it.polimi.ingsw.LM34.Exceptions.Validation.IncorrectInputException;
import it.polimi.ingsw.LM34.Model.Effects.ResourceRelatedBonus.ResourcesBonus;
import it.polimi.ingsw.LM34.Model.Resources;
import it.polimi.ingsw.LM34.Utils.Validator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.RESOURCE_INCOME_CONTEXT;
import static it.polimi.ingsw.LM34.Enums.Controller.ContextType.USE_COUNCIL_PRIVILEGE_CONTEXT;
import static it.polimi.ingsw.LM34.Utils.Utilities.LOGGER;

/**
 *Building permanent effects make the player access
 *this contexts so that he can chooses what option he want to activate
 */
public class ResourcesExchangeContext extends AbstractGameContext {

    public ResourcesExchangeContext() {
        this.contextType = RESOURCE_INCOME_CONTEXT;
    }

    @Override
    public Void interactWithPlayer(Object... args) throws IncorrectInputException {
        List<?> resourceExchangeGenericList;
        List<Pair<Resources, ResourcesBonus>> resourcesExchange = new ArrayList<>();
        try {
            resourceExchangeGenericList = (List<?>) args[0];
            resourceExchangeGenericList.forEach(resExc -> {
                Pair <?, ?> resExcPair = (Pair < ?, ?>) resExc;
                if(this.getCurrentPlayer().hasEnoughResources((Resources) resExcPair.getLeft()))
                    resourcesExchange.add(new ImmutablePair<>(
                            (Resources) resExcPair.getLeft(),
                            (ResourcesBonus) resExcPair.getRight()
                ));
            });
        } catch(Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
            throw new IncorrectInputException();
        }

        if(!resourcesExchange.isEmpty()) {
            Integer selection = resourceExchangeSelection(resourcesExchange);
            if (selection >= 0) {
                Pair<Resources, ResourcesBonus> exchange = resourcesExchange.get(selection);
                this.gameManager.getCurrentPlayer().getResources().subResources(exchange.getLeft());
                ((ResourceIncomeContext) getContextByType(RESOURCE_INCOME_CONTEXT)).setIncome(exchange.getRight().getResources());
                ((UseCouncilPrivilegeContext) getContextByType(USE_COUNCIL_PRIVILEGE_CONTEXT)).interactWithPlayer(exchange.getRight().getCouncilPrivilege());
            }
        }

        return null;
    }

    private Integer resourceExchangeSelection(List<Pair<Resources, ResourcesBonus>> resourcesExchange) {
        if(!this.getCurrentPlayer().isConnected())
            return -1;

        try {
            Integer selectedPair = this.gameManager.getActivePlayerNetworkController().resourceExchangeSelection(resourcesExchange);
            if(selectedPair >= 0)
                Validator.checkValidity(selectedPair, resourcesExchange);
            return  selectedPair;
        } catch(IncorrectInputException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
            return resourceExchangeSelection(resourcesExchange);
        } catch(NetworkConnectionException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage(), ex);
            this.getCurrentPlayer().setDisconnected();
            return -1;
        }
    }

}


