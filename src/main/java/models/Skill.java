package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String skillName;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private List<Developer> developers;

    public Skill() {
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }

    public Skill(long id, String skillName) {
        this.id = id;
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
