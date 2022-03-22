package it.polimi.ingsw.LM34.Model;

import it.polimi.ingsw.LM34.Enums.Model.DiceColor;
import it.polimi.ingsw.LM34.Utils.Copyable;

import java.io.Serializable;
import java.util.Random;

import static it.polimi.ingsw.LM34.Enums.Model.DiceColor.NEUTRAL;

public class Dice implements Serializable {
    private static final long serialVersionUID = 9147027093845168538L;

    private final DiceColor color;
    private Integer value;

    public Dice(DiceColor color) {
        this.color = color;
    }

    public DiceColor getColor() {
        return this.color;
    }

    public Integer getValue() {
        return value;
    }

    /**
     *Method used to set a new value for the Dice; this solution is preferred for sake of simplicity and integrity
     *against passing the new value from outside this class
     */
    public void rollDice(){
        this.value = (this.getColor() != NEUTRAL) ? new Random().nextInt(6) + 1 : 0;
    }
}
