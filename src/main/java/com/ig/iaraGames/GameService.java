package com.ig.iaraGames;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElseThrow();
    }

    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    // Método para registrar um novo usuário
    public Game registerGame(Game game) {
        // Verifica se o jogo já existe
        if (gameRepository.findByGameName(game.getgameName()).isPresent()) {
            throw new RuntimeException("Este jogo já existe");
        }

        return gameRepository.save(game);
    }

    public User findByGameName(String gameName) {
        Optional<User> user = gameRepository.findByGameName(gameName);
        return user.orElse(null);
    }
}


