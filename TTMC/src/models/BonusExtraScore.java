package models;

import view.PlayerView;

import java.util.List;

import models.Player;
/*
*This class implements the BonusExtraScore mystery state,allowing a player to increase their score by 200 points and advance by 1 step.
**/
public class BonusExtraScore implements MysteryState {
	
	/*
	 * This method is called when the random chooses to execute the mystery. It
	 * allows the player to increase their score by 200 points and advance by 1
	 * step.
	 */
	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {
		//Increase player' score by 200 points
		currentPlayer.increaseScore(200);
		
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
