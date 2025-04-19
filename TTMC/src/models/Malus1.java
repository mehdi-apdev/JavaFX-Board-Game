package models;
import view.PlayerView;

import java.util.List;

import controller.BoardController;
import models.Player;

public class Malus1 implements MysteryState{

	@Override
	public void executeMystery(Game game,Player currentPlayer, PlayerView currentPlayerView) {
		// Recule le joueur
		//int randomSteps = ThreadLocalRandom.current().nextInt(2, 5);
		
		int randomSteps = 2;
        currentPlayer.move(-randomSteps);
        currentPlayerView.animateMovement(-randomSteps);
        
        
     
		
	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub
		
	}




}
