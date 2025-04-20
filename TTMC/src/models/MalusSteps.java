package models;
import view.PlayerView;

import java.util.List;

import controller.BoardController;
import models.Player;

public class MalusSteps implements MysteryState{

	@Override
	public void executeMystery(Game game,Player currentPlayer, PlayerView currentPlayerView) {
		
		//int randomSteps = ThreadLocalRandom.current().nextInt(2, 5);
		
		int steps = 2;
        currentPlayer.move(-steps);
        currentPlayerView.animateMovement(-steps);
        
        
     
		
	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub
		
	}




}
