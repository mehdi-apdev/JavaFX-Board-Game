package models;

import view.PlayerView;

import java.util.List;

import models.Player;

public class BonusExtraSteps implements MysteryState {

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {
		// Avance le joueur
		int steps = 3;
		currentPlayer.move(steps);
		currentPlayerView.animateMovement(steps);

	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub
		
	}

}
