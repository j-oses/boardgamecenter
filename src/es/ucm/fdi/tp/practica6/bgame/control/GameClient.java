package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;
import es.ucm.fdi.tp.practica6.net.AbstractClient;
import es.ucm.fdi.tp.practica6.net.SocketEndpoint;

import java.util.List;

/**
 * Created by Jorge on 10-May-16.
 */
public class GameClient extends AbstractClient implements GameObserver {
    public VisualController clientController;


    @Override
    public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {

    }

    @Override
    public void onGameOver(Board board, Game.State state, Piece winner) {

    }

    @Override
    public void onMoveStart(Board board, Piece turn) {

    }

    @Override
    public void onMoveEnd(Board board, Piece turn, boolean success) {

    }

    @Override
    public void onChangeTurn(Board board, Piece turn) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    protected SocketEndpoint createEndpoint(String name) {
        return null;
    }
}
