package cz.muni.fi.Tests;

import cz.muni.fi.Base.Mission;

/**
 * Created by Matúš on 21.3.2017.
 */
public class MissionBuilder {

    private Long id;
    private String description;
    private Short numberOfRequiredAgents = 1;
    private Integer difficulty;
    private String place;
    private Boolean successful;

    public MissionBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public MissionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public MissionBuilder numberOfRequiredAgents(Short numberOfRequiredAgents) {
        this.numberOfRequiredAgents = numberOfRequiredAgents;
        return this;
    }

    public MissionBuilder difficulty(Integer difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public MissionBuilder place(String place) {
        this.place = place;
        return this;
    }

    public MissionBuilder successful(Boolean successful) {
        this.successful = successful;
        return this;
    }

    public Mission build() {
        Mission mission = new Mission();
        mission.setId(id);
        mission.setDescription(description);
        mission.setNumberOfRequiredAgents(numberOfRequiredAgents);
        mission.setDifficulty(difficulty);
        mission.setPlace(place);
        mission.setSuccessful(successful);
        return mission;
    }
}
