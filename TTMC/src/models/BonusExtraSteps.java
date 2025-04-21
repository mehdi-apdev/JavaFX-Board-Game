package models;

import view.PlayerView;

import java.util.List;

import models.Player;

/*
 * This class implements the BonusExtraSteps mystery state, allowing a player to advance by 3 steps.
 */
public class BonusExtraSteps implements MysteryState {
	
	/*
	 * This method is called when the player chooses to execute the mystery. It
	 * allows the player to advance by 3 steps.
	 */
	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {
		// Move the current player forward by 3 steps
		int steps = 3;
		currentPlayer.move(steps);
		currentPlayerView.animateMovement(steps);

	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// TODO Auto-generated method stub
		
	}

}
