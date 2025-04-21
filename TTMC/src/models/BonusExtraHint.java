package models;

import view.PlayerView;

import java.util.List;

import models.Player;

/*
 * This class implements the BonusExtraHint mystery state, allowing a player to
 * advance by 1 step and gain an extra hint.
 */
public class BonusExtraHint implements MysteryState {
	
	/*
	 * This method is called when the player chooses to execute the mystery. It
	 * allows the player to advance by 1 step and gain an extra hint.
	 */
	@Override
	public void executeMystery(Game game,Player currentPlayer, PlayerView currentPlayerView) {
		// Increase player's hint by 1
		currentPlayer.setHint(1);
		// Move the current player forward by 1 step
		int steps = 1;
		currentPlayer.move(steps);
		currentPlayerView.animateMovement(steps);
	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub
		
	}

}
