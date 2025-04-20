package models;

import view.PlayerView;

import java.util.List;

import models.Player;

public class BonusExtraHint implements MysteryState {

	@Override
	public void executeMystery(Game game,Player currentPlayer, PlayerView currentPlayerView) {
		currentPlayer.setHint(1);
		int steps = 1;
		currentPlayer.move(steps);
		currentPlayerView.animateMovement(steps);
	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub
		
	}

}
