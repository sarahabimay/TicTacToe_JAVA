package jttt.Core.Players;

import jttt.Core.Board;
import jttt.Core.Mark;
import jttt.UI.UserInterface;

public abstract class Player {
    public static enum Type {
        AI,
        HUMAN
    }

    protected Mark mark;
    UserInterface userInterface;
    Type playerType;

    public Player(Mark mark, Type type, UserInterface userInterface) {
        this.playerType = type;
        this.mark = mark;
        this.userInterface = userInterface;
    }

    public Mark getMark() {
        return mark;
    }

    public Mark opponentCounter() {
        if (mark == Mark.X) {
            return Mark.O;
        }
        if (mark == Mark.O) {
            return Mark.X;
        }
        return Mark.EMPTY;
    }

    public Type getPlayerType() {
        return playerType;
    }

    abstract public Board playTurn(Board board);

    abstract public Board playTurn(Board board, int newPosition);


}
