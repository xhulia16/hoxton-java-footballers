package com.footballers.hoxtonjavafootballers;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Entity
public class Team {
    @Id
    @GeneratedValue
    public int id;
    public String name;

    @OneToMany(mappedBy = "team")
    public Set<Player> players;

    public Team() {
    }
}

@RestController
class TeamController {

    @Autowired
    private TeamRepository teamRepo;

    // @Autowired
    // private PlayerRepository playerRepo;


    @GetMapping("/teams")
    public List<Team> getAllTeams(){
        return teamRepo.findAll();
    }

    @PostMapping("/teams")
    public Team createTeam(@RequestBody Team team){
        return teamRepo.save(team);
    }


}

interface TeamRepository extends JpaRepository<Team, Integer> {

}
