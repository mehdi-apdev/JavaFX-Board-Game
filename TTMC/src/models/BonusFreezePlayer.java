package models;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import models.DialogWindow;

import view.PlayerView;

public class BonusFreezePlayer implements MysteryState {

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {

		DialogWindow dialog = new DialogWindow();
	    // Check if there are other players to freeze
	    List<Player> otherPlayers = game.getPlayers().stream()
	        .filter(player -> !player.equals(currentPlayer))
	        .collect(Collectors.toList());

	    if (otherPlayers.isEmpty()) {
	        dialog.showAlert("No Opponents", "There are no other players to freeze.");
	        return;
	    }

	    // Let the current player choose an opponent to freeze
	    StringBuilder message = new StringBuilder("Choose a player to freeze:\n");
	    for (int i = 0; i < otherPlayers.size(); i++) {
	        message.append(i + 1).append(". ").append(otherPlayers.get(i).getName()).append("\n");
	    }

	    Optional<String> result = dialog.showInputDialog("Freeze Opponent", message.toString());
	    if (result.isPresent()) {
	        try {
	            int choice = Integer.parseInt(result.get()) - 1;
	            if (choice >= 0 && choice < otherPlayers.size()) {
	                Player selectedPlayer = otherPlayers.get(choice);
	                selectedPlayer.setBlocked(true);

	                dialog.showAlert("Player Frozen", selectedPlayer.getName() + " is frozen for one turn!");
	            } else {
	                dialog.showAlert("Invalid Choice", "Please select a valid player.");
	            }
	        } catch (NumberFormatException e) {
	            dialog.showAlert("Invalid Input", "Please enter a valid number.");
	        }

	    }
	    
	    int steps = 1;
		currentPlayer.move(steps);
		currentPlayerView.animateMovement(steps);

	}

	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView,
			List<PlayerView> player) {
		
		
	}
}
