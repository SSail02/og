package SmartLibrary.model;

/**
 * Base domain class representing a person in the system.
 */
public class Person {
    protected String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
