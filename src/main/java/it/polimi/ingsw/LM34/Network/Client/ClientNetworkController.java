package it.polimi.ingsw.LM34.Network.Client;

import it.polimi.ingsw.LM34.Enums.Controller.LeaderCardsAction;
import it.polimi.ingsw.LM34.Enums.Controller.PlayerSelectableContexts;
import it.polimi.ingsw.LM34.Enums.Model.PawnColor;
import it.polimi.ingsw.LM34.Enums.UI.GameInformationType;
import it.polimi.ingsw.LM34.Exceptions.Controller.NetworkConnectionException;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.CouncilPalace;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.Market;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.Tower;
import it.polimi.ingsw.LM34.Model.Boards.GameBoard.WorkingArea;
import it.polimi.ingsw.LM34.Model.Boards.PlayerBoard.BonusTile;
import it.polimi.ingsw.LM34.Model.Cards.ExcommunicationCard;
import it.polimi.ingsw.LM34.Model.Cards.LeaderCard;
import it.polimi.ingsw.LM34.Model.Dice;
import it.polimi.ingsw.LM34.Model.Effects.ResourceRelatedBonus.ResourcesBonus;
import it.polimi.ingsw.LM34.Model.FamilyMember;
import it.polimi.ingsw.LM34.Model.Player;
import it.polimi.ingsw.LM34.Model.Resources;
import it.polimi.ingsw.LM34.Network.PlayerAction;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientNetworkController {
    private AbstractClient clientConnection;

    public ClientNetworkController(AbstractClient clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void login(String username, String password) {
        this.clientConnection.login(username, password);
    }

    public void loginResult(Boolean result) {
        this.clientConnection.getUI().loginResult(result);
    }

    public void loadMapTerritoriesToVictoryPoints(Map<Integer, Integer> mapTerritoriesToVictoryPoints) {
        this.clientConnection.getUI().loadMapTerritoriesToVictoryPoints(mapTerritoriesToVictoryPoints);
    }

    public void loadMapMilitaryPointsForTerritories(Map<Integer, Integer> mapMilitaryPointsForTerritories) {
        this.clientConnection.getUI().loadMapMilitaryPointsForTerritories(mapMilitaryPointsForTerritories);
    }

    public void loadMapCharactersToVictoryPoints(Map<Integer, Integer> mapCharactersToVictoryPoints) {
        this.clientConnection.getUI().loadMapCharactersToVictoryPoints(mapCharactersToVictoryPoints);
    }

    public void loadFaithPath(Map<Integer, Integer> faithPath) {
        this.clientConnection.getUI().loadFaithPath(faithPath);
    }

    public void setExcommunicationCards(List<ExcommunicationCard> excommunicationCards) {
        this.clientConnection.getUI().loadExcommunicationCards(excommunicationCards);
    }

    public void updateTowers(List<Tower> towers) {
        this.clientConnection.getUI().updateTowers(towers);
    }

    public void updateCouncilPalace(CouncilPalace councilPalace){
        this.clientConnection.getUI().updateCouncilPalace(councilPalace);
    }

    public void updateMarket(Market market) {
        this.clientConnection.getUI().updateMarket(market);
    }

    public void updateProductionArea(WorkingArea productionArea) {
        this.clientConnection.getUI().updateProductionArea(productionArea);
    }

    public void updateHarvestArea(WorkingArea harvestArea) {
        this.clientConnection.getUI().updateHarvestArea(harvestArea);
    }

    public void updatePlayersData(List<Player> players) {
        this.clientConnection.getUI().updatePlayersData(players);
    }

    public void updateDiceValues(List<Dice> diceValues) {
        this.clientConnection.getUI().updateDiceValues(diceValues);
    }

    public void startGame() {
        this.clientConnection.getUI().startGame();
    }

    public PlayerAction turnMainAction(Optional<Exception> lastActionValid) {
        return this.clientConnection.getUI().turnMainAction(lastActionValid);
    }

    public PlayerAction turnSecondaryAction(Optional<Exception> lastActionValid) {
        return this.clientConnection.getUI().turnSecondaryAction(lastActionValid);
    }

    public Integer familyMemberSelection(List<FamilyMember> familyMembers) {
        return this.clientConnection.getUI().familyMemberSelection(familyMembers);
    }

    public Integer servantsSelection(Integer servantsAvailable, Integer minimumServantsRequested) {
        return this.clientConnection.getUI().servantsSelection(servantsAvailable, minimumServantsRequested);
    }

    public Integer resourceExchangeSelection(List<Pair<Resources, ResourcesBonus>> choices) {
        return this.clientConnection.getUI().resourceExchangeSelection(choices);
    }

    public Pair<String, LeaderCardsAction> leaderCardSelection(List<LeaderCard> leaderCards) {
        return this.clientConnection.getUI().leaderCardSelection(leaderCards);
    }

    public Boolean churchSupport() {
        return this.clientConnection.getUI().churchSupport();
    }

    public Integer selectCouncilPrivilegeBonus(List<Resources> availableBonuses) {
        return this.clientConnection.getUI().selectCouncilPrivilegeBonus(availableBonuses);
    }

    public Integer bonusTileSelection(List<BonusTile> availableBonusTiles) {
        return this.clientConnection.getUI().bonusTileSelection(availableBonusTiles);
    }

    public Integer leaderCardSelectionPhase(List<LeaderCard> availableLeaderCards) {
        return this.clientConnection.getUI().leaderCardSelectionPhase(availableLeaderCards);
    }

    public Boolean alternativeRequirementsPayment() {
        return this.clientConnection.getUI().alternativeRequirementsPayment();
    }

    public void endGame(List<Player> players) {
        this.clientConnection.getUI().endGame(players);
    }

    public void endTurn() {
        this.clientConnection.getUI().endTurn();
    }

    public void informInGamePlayers(GameInformationType infoType, String playerName, PawnColor playerColor) {
        this.clientConnection.getUI().informInGamePlayers(infoType, playerName, playerColor);
    }

    public PlayerAction freeAction(PlayerAction availableAction, Optional<Exception> lastActionValid) {
        return this.clientConnection.getUI().freeAction(availableAction, lastActionValid);
    }

    public Integer leaderCardCopy(List<LeaderCard> leaderCards) {
        return this.clientConnection.getUI().leaderCardCopy(leaderCards);
    }
}
