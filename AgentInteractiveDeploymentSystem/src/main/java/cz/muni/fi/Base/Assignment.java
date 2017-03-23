package cz.muni.fi.Base;

import java.time.LocalDateTime;

/**
 * Created by xmelkov on 8.3.17.
 */
public class Assignment {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Agent agent;
    private Mission mission;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", agent=" + agent +
                ", mission=" + mission +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Assignment)) {
            return false;
        }

        Assignment that = (Assignment) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
