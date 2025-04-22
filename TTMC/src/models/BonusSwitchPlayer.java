package models;
import view.PlayerView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView,
			List<PlayerView> playerViews) {
		DialogWindow dialog = new DialogWindow();
		int steps = 1;

		// Liste des joueurs disponibles pour l'échange
		List<Player> availablePlayers = game.getPlayers().stream()
				.filter(player -> player.getPosition() != currentPlayer.getPosition()) // Include players not at the same position
				.filter(player -> player.getPosition() > currentPlayer.getPosition()) // Include players behind the current player
				.filter(player -> !player.isAtTheEnd()) // Include players not at the end																		
				.collect(Collectors.toList());

		if (availablePlayers.isEmpty()) {
			dialog.showAlert("No Players to Switch", "There are no players available to switch places with.");
			currentPlayer.move(steps);
			currentPlayerView.animateMovement(steps);
			// Move the current player forward by 1 step
			return;
		}

		// Construire le message des options
		StringBuilder message = new StringBuilder("Choose a player to switch places with:\n");
		for (int i = 0; i < availablePlayers.size(); i++) {
			Player player = availablePlayers.get(i);
			message.append(i + 1).append(". ").append(player.getName()).append(" (Position: ")
					.append(player.getPosition()).append(")\n");
		}

		Optional<String> result = dialog.showInputDialog("Switch Places", message.toString());
		if (result.isPresent()) {
			try {
				int choice = Integer.parseInt(result.get());
				if (choice < 1 || choice > availablePlayers.size()) {
					dialog.showAlert("Invalid Choice", "Please select a valid player.");
					currentPlayer.move(steps);
					currentPlayerView.animateMovement(steps);
					return;
				}

				// Joueur sélectionné
				Player selectedPlayer = availablePlayers.get(choice - 1);

				// Échanger les positions
				int tempPosition = currentPlayer.getPosition();
				currentPlayer.setPosition(selectedPlayer.getPosition()+1);
				selectedPlayer.setPosition(tempPosition+1);

				// Mettre à jour les animations
				PlayerView selectedPlayerView = playerViews.get(game.getPlayers().indexOf(selectedPlayer));
				currentPlayerView.updatePosition();
				selectedPlayerView.updatePosition();

				dialog.showAlert("Switch Successful",
						"You have switched places with " + selectedPlayer.getName() + "!");
			} catch (NumberFormatException e) {
				dialog.showAlert("Invalid Input", "Please enter a valid number.");
			}
		} else {
			currentPlayer.move(steps);
			currentPlayerView.animateMovement(steps);
		}
	}

}
