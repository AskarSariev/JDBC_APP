package models;

import java.util.Objects;

public class Skill {
    private long id;
    private String skillName;

    public Skill() {
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return skillName.equals(skill.skillName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillName);
    }

    @Override
    public String toString() {
        return skillName;
    }
}
