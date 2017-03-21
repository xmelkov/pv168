package cz.muni.fi.Tests;

import cz.muni.fi.Base.Agent;

/**
 * Created by Matúš on 21.3.2017.
 */

public class AgentBuilder {

    private Long id;
    private String name;
    private Short gender;
    private Short age;
    private String phoneNumber;
    private Boolean alive;

    public AgentBuilder id(Long id){
        this.id = id;
        return this;
    }

    public AgentBuilder name(String name){
        this.name = name;
        return this;
    }

    public AgentBuilder alive(Boolean alive){
        this.alive = alive;
        return this;
    }

    public AgentBuilder gender(Short gender){
        this.gender = gender;
        return this;
    }

    public AgentBuilder age(Short age){
        this.age = age;
        return this;
    }

    public AgentBuilder phoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Agent build() {
        Agent agent = new Agent();
        agent.setAge(age);
        agent.setAlive(alive);
        agent.setGender(gender);
        agent.setId(id);
        agent.setName(name);
        agent.setPhoneNumber(phoneNumber);
        return agent;
    }

}
