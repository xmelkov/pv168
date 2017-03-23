package cz.muni.fi.Base;

/**
 * Created by xmelkov on 8.3.17.
 */
public class Mission {
    private long id;
    private String description;
    private short numberOfRequiredAgents = 1;
    private int difficulty;
    private String place;
    private boolean successful;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getNumberOfRequiredAgents() {
        return numberOfRequiredAgents;
    }

    public void setNumberOfRequiredAgents(short numberOfRequiredAgents) {
        this.numberOfRequiredAgents = numberOfRequiredAgents;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", numberOfRequiredAgents=" + numberOfRequiredAgents +
                ", difficulty=" + difficulty +
                ", place='" + place + '\'' +
                ", successful=" + successful +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Mission)) {
            return false;
        }

        Mission mission = (Mission) o;

        return id == mission.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
