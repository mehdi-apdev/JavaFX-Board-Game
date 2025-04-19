package models;
import view.PlayerView;
import models.Player;
import java.util.List;
import models.Game;

public interface MysteryState {
	public void executeMystery(Game game,Player currentPlayer, PlayerView currentPlayerView);
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player);
	
	}

