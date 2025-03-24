package edu.neu.csye6200;

public abstract class Person {
    private String firstName;
    private String lastName;
    private int age;

    // Constructors, getters and setters
    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return firstName + "," + lastName + "," + age;
    }
}

