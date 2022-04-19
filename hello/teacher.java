package se.BTH.ITProjectManagement.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.BTH.ITProjectManagement.models.Team;


import se.BTH.ITProjectManagement.models.User;
import se.BTH.ITProjectManagement.repositories.TeamRepository;
import se.BTH.ITProjectManagement.repositories.UserRepository;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api/team")
public class TeamController {

    private static org.apache.log4j.Logger log = Logger.getLogger(TeamController.class);

    @Autowired
    private TeamRepository repository;
    @Autowired
    private UserRepository userRepository;

    // Displaying the initial teams list.
    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    public String getTeams(Model model) {
        log.debug("Request to fetch all teams from the mongo database");
        List<Team> team_list = repository.findAll();
        model.addAttribute("teams", team_list);
        return "team";
    }
    // view team's members
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String getTeamMembers(@RequestParam(value="id", required=true) String id, Model model) {
        log.debug("Request to fetch all users from the mongo database for custom team");
        Set<User> member_list = repository.findById(id).get().getUsers();

        model.addAttribute("members",member_list );

        return "teammember";
    }
    @RequestMapping(value = "/detail/list", method = RequestMethod.GET)
    public String selectMemberToAdd(Model model) {
        log.debug("Request to fetch all users from the db for custom team and select member");
        model.addAttribute("members",userRepository.findAll());
        return "teammemberform";
    }
    @RequestMapping(value = "/detail/select", method = RequestMethod.POST)
    public String save(@ModelAttribute("userAttr") User user,String id) {
        Team team=repository.findById(id).get();
        Set<User> members=team.getUsers();
        members.add(user);
        team.setUsers(members);
        repository.save(team);
        return "redirect:teammember";
    }
    // Opening the add new team form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addTeam(Model model) {
        log.debug("Request to open the new team form page");
        Team team=Team.builder().name("").build();
        repository.save(team);
        model.addAttribute("teamAttr", team);
        return "teamform";
    }

    // Opening the edit team form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editTeam(@RequestParam(value="id", required=true) String id, Model model) {
        log.debug("Request to open the edit team form page");
        model.addAttribute("teamAttr", repository.findById(id));
        return "teamform";
    }

    // Deleting the specified team.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="id", required=true) String id, Model model) {
        repository.deleteById(id);
        return "redirect:teams";
    }

    // Adding a new team or updating an existing team.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("teamAttr") Team team) {                  // needs test for edit or create
        repository.save(team);
        return "redirect:teams";
    }
}
/*
@RestController
@RequestMapping("/api")
public class TeamController {
    private final Logger log = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping("/teams")
    Collection<Team> teams() {
        return teamRepository.findAll();
    }

    @GetMapping("/team/{id}")
    ResponseEntity<?> getTeam(@PathVariable String id) {
        Optional<Team> team = teamRepository.findById(id);
        return team.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/team", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) throws URISyntaxException {
        log.info("Request to create team: {}", team);
        Team result = teamRepository.save(team);
        return ResponseEntity.created(new URI("/api/team/" + result.getId())).body(result);
    }

    @PutMapping("/team")
    ResponseEntity<Team> updateTeam(@Valid @RequestBody Team team) {
        log.info("Request to update team: {}", team);
        Team result = teamRepository.save(team);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable String id) {
        log.info("Request to delete team: {}", id);
        teamRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
*/