package models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "specialties")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", unique = true)
    private String specialtyName;

    @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
    private List<Developer> developers;

    public Specialty() {
    }

    public Specialty(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialty specialty = (Specialty) o;
        return specialtyName.equals(specialty.specialtyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialtyName);
    }

    @Override
    public String toString() {
        return specialtyName;
    }
}
