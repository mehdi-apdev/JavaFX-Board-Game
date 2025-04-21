package models;
import view.PlayerView;

import java.util.List;
import java.util.Optional;

import models.DialogWindow;


public class BonusSwitchPlayer implements MysteryState {

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {
	
	}
	
	/*
	 * This method is called when the player chooses to execute the mystery. It
	 * allows the player to switch places with another player ahead or behind them.
	 */
	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> playerViews) {

	    int currentPlayerIndex = game.getCurrentPlayerIndex();
	    DialogWindow dialog = new DialogWindow();
	    int steps = 1;

	    // Find the player ahead
	    Player playerAhead = null;
	    Player playerBehind = null;
	    for (Player player : game.getPlayers()) {
	        if (player.getPosition() > currentPlayer.getPosition()) {
	            if (playerAhead == null || player.getPosition() < playerAhead.getPosition()) {
	                playerAhead = player;
	            }
	        } else if (player.getPosition() < currentPlayer.getPosition()) {
	            if (playerBehind == null || player.getPosition() > playerBehind.getPosition()) {
	                playerBehind = player;
	            }
	        }
	    }

	    if (playerAhead == null && playerBehind == null) {
	        dialog.showAlert("No Players to Switch", "There are no players ahead or behind to switch places with.");
	        return;
	    }

	    // Let the current player choose to switch with the player ahead or behind
	    StringBuilder message = new StringBuilder("Choose a player to switch places with:\n");
	    if (playerAhead != null) {
	        message.append("1. ").append(playerAhead.getName()).append(" (Ahead)\n");
	    }
	    if (playerBehind != null) {
	        message.append("2. ").append(playerBehind.getName()).append(" (Behind)\n");
	    }

	    Optional<String> result = dialog.showInputDialog("Switch Places", message.toString());
	    if (result.isPresent()) {
	        try {
	            int choice = Integer.parseInt(result.get());
	            Player selectedPlayer = null;

	            if (choice == 1 && playerAhead != null) {
	                selectedPlayer = playerAhead;
	            } else if (choice == 2 && playerBehind != null) {
	                selectedPlayer = playerBehind;
	            } else {
	                dialog.showAlert("Invalid Choice", ""); 
	        		currentPlayer.move(steps);
	        		currentPlayerView.animateMovement(steps);
	                return;
	            }

	            // Swap positions
	            int tempPosition = currentPlayer.getPosition();
	            currentPlayer.setPosition(selectedPlayer.getPosition());
	            selectedPlayer.setPosition(tempPosition);

	            // Update animations
	            PlayerView selectedPlayerView = playerViews.get(game.getPlayers().indexOf(selectedPlayer));
	            currentPlayerView.updatePosition();
	            selectedPlayerView.updatePosition();

	            dialog.showAlert("Switch Successful", "You have switched places with " + selectedPlayer.getName() + "!");
	        } catch (NumberFormatException e) {
	            dialog.showAlert("Invalid Input", "Please enter a valid number.");
	        }
	    }else {
	    	currentPlayer.move(steps);
    		currentPlayerView.animateMovement(steps);
	    }
		
	}
}
