package models;

import view.PlayerView;

import java.util.List;

import models.Player;

public class BonusExtraScore implements MysteryState {

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {
		currentPlayer.increaseScore(200);;
		
		int steps = 1;
		currentPlayer.move(steps);
		currentPlayerView.animateMovement(steps);
	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub

	}

}
