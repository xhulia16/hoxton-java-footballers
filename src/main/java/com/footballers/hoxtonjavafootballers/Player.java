package com.footballers.hoxtonjavafootballers;

import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Player {
    @Id
    @GeneratedValue
    public int id;
    public String name;
    public String nationality;
    public int scoreOutOfTen;
    public Boolean isReplacement;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "teamId", nullable = false)
    public Team team;

    public Player() {

    }
}

@RestController
class PlayerController {
    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private TeamRepository teamRepo;

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getSinglePlayer(@PathVariable Integer id) {
        Optional<Player> match = playerRepo.findById(id);
        if (match.isPresent()) {
            return new ResponseEntity<>(match.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/teams/{teamId}/players")
    public Player createPlayer(@RequestBody Player player, @PathVariable Integer teamId) {
        player.team = teamRepo.findById(teamId).get();
        return playerRepo.save(player);
    }

    @PatchMapping("/players/{id}")
    public Player updatePlayer(@RequestBody Player player, @PathVariable Integer id) {
        Optional<Player> match = playerRepo.findById(id);

        if (match.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }

        Player playerToBeUpdated = match.get();

        if (playerToBeUpdated.scoreOutOfTen < player.scoreOutOfTen) {
            playerToBeUpdated.scoreOutOfTen = player.scoreOutOfTen;
        }
        else{
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Score should be higher than current" );}

        return playerRepo.save(playerToBeUpdated);
    
    }
}

interface PlayerRepository extends JpaRepository<Player, Integer> {

}

interface TeamRepository extends JpaRepository< Team, Integer> {

}