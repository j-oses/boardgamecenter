package es.ucm.fdi.tp.practica6.bgame.control;

import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.practica5.bgame.control.VisualController;

import java.util.List;

/**
 * Created by Jorge on 15-May-16.
 */
public class ClientController extends VisualController {
    private GameClient client;
    public void addClient(GameClient client){
        this.client = client;
    }
    public ClientController(Game game, List<Piece> pieces, Piece owner) {
        super(game, pieces, owner);
    }

    @Override
    public void didGenerateMove(GameMove move) {
        // It's not strictly necessary to check for current player, but it's
        // checked just in case
        if (isTurn && currentPlayer == null) {
            client.moveGenerated(move);
        }
    }

}
