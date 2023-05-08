package models;

public class Specialty {
    private long id;
    private String specialtyName;

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
    public String toString() {
        return specialtyName;
    }
}
