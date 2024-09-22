package com.ig.iaraGames;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;



@Entity
@Table(name = "games")

public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    private String gameName;
    private String gameGenre; // Adicionado campo name

    // Construtor padrão
    public Game() {}

    // Construtor completo
    public Game(String game, String genre) {
        this.gameName = game;
        this.gameGenre = genre;

    }

    // Getter e Setter para id
    public Long getId() {
        return gameId;
    }

    public void setId(Long game_id) {
        this.gameId = game_id;
    }

    // Getter e Setter para username
    public String getgameName() {
        return gameName;
    }

    public void setGameName (String game) {
        this.gameName = game;
    }





    // Getter e Setter para email
    public String gameGenre(String genre) {
        return genre;
    }

    public void setGameGenre(String genre) {
        this.gameGenre = genre;
    }



    /**
     */
    // Método toString para facilitar a visualização dos dados
    @Override
    public String toString() {
        return "User{" +
                "id=" + gameId +
                ", username='" + gameName + '\'' +
                ", email='" + gameGenre + '\'' +

                '}';
    }
}