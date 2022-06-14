package com.example.gymstat;

public class User {

    private long id;
    private String gender;
    private int weight;
    private int height;
    private int age;
    private String name;
    private String password;

    public User(long id, String gender, int age, int weight, int height, String name, String password) {
        this.id = id;
        this.gender = gender;
        this.weight = weight;
        this.age = age;
        this.height = height;
        this.name = name;
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String login) {
        this.name = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", age=" + age +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
