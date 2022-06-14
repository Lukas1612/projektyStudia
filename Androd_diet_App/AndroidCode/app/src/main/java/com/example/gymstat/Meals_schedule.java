package com.example.gymstat;

public class Meals_schedule {

    Long id;
    Long user_id;

    //time 24h = 1440 min
    //example: 19:30 = (19 + 30/60)*60 = 1170
    String first_meal_name;
    int first_meal_time;
    String second_meal_name;
    int second_meal_time;
    String third_meal_name;
    int third_meal_time;
    String fourth_meal_name;
    int fourth_meal_time;
    String fifth_meal_name;
    int fifth_meal_time;
    String sixth_meal_name;
    int sixth_meal_time;
    String day; //yyyy-mm-dd

    public Meals_schedule(Long id, Long user_id, String first_meal_name, int first_meal_time, String second_meal_name, int second_meal_time, String third_meal_name, int third_meal_time, String fourth_meal_name, int fourth_meal_time, String fifth_meal_name, int fifth_meal_time, String sixth_meal_name, int sixth_meal_time, String day) {
        this.id = id;
        this.user_id = user_id;
        this.first_meal_name = first_meal_name;
        this.first_meal_time = first_meal_time;
        this.second_meal_name = second_meal_name;
        this.second_meal_time = second_meal_time;
        this.third_meal_name = third_meal_name;
        this.third_meal_time = third_meal_time;
        this.fourth_meal_name = fourth_meal_name;
        this.fourth_meal_time = fourth_meal_time;
        this.fifth_meal_name = fifth_meal_name;
        this.fifth_meal_time = fifth_meal_time;
        this.sixth_meal_name = sixth_meal_name;
        this.sixth_meal_time = sixth_meal_time;
        this.day = day;
    }


}
