package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;
import cz.muni.fi.Base.Assignment;
import cz.muni.fi.Base.Mission;

import java.time.LocalDateTime;

/**
 * Created by Matúš on 21.3.2017.
 */
public class AssignmentBuilder {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Agent agent;
    private Mission mission;

    public AssignmentBuilder id(Long id){
        this.id = id;
        return this;
    }

    public AssignmentBuilder start(LocalDateTime start){
        this.start = start;
        return this;
    }

    public AssignmentBuilder end(LocalDateTime end){
        this.end = end;
        return this;
    }

    public AssignmentBuilder agent(Agent agent){
        this.agent = agent;
        return this;
    }

    public AssignmentBuilder mission(Mission mission){
        this.mission = mission;
        return this;
    }

    public Assignment build() {
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setStart(start);
        assignment.setEnd(end);
        assignment.setAgent(agent);
        assignment.setMission(mission);
        return assignment;
    }
}
