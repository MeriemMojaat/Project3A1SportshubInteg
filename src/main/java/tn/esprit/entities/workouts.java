package tn.esprit.entities;

public class workouts {
    private int id_workout;
    private String workout_name;
    private String wk_description;
    private String wk_intensity;
    private String wk_image;
    private int coach_id;
    private int id_category;

    public workouts(int id_workout, String workout_name, String wk_description, String wk_intensity, String wk_image, int coach_id, int id_category) {
        this.id_workout = id_workout;
        this.workout_name = workout_name;
        this.wk_description = wk_description;
        this.wk_intensity = wk_intensity;
        this.wk_image = wk_image;
        this.coach_id = coach_id;
        this.id_category = id_category;
    }

    public workouts(String workout_name, String wk_description, String wk_intensity, String wk_image, int coach_id, int id_category) {
        this.workout_name = workout_name;
        this.wk_description = wk_description;
        this.wk_intensity = wk_intensity;
        this.wk_image = wk_image;
        this.coach_id = coach_id;
        this.id_category = id_category;
    }

    public int getId_workout() {
        return id_workout;
    }

    public void setId_workout(int id_workout) {
        this.id_workout = id_workout;
    }

    public String getWorkout_name() {
        return workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }

    public String getWk_description() {
        return wk_description;
    }

    public void setWk_description(String wk_description) {
        this.wk_description = wk_description;
    }

    public String getWk_intensity() {
        return wk_intensity;
    }

    public void setWk_intensity(String wk_intensity) {
        this.wk_intensity = wk_intensity;
    }

    public String getWk_image() {
        return wk_image;
    }

    public void setWk_image(String wk_image) {
        this.wk_image = wk_image;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    @Override
    public String toString() {
        return "workouts{" +
                "workout_name='" + workout_name + '\'' +
                ", wk_description='" + wk_description + '\'' +
                ", wk_intensity='" + wk_intensity + '\'' +
                ", wk_image='" + wk_image + '\'' +
                ", coach_id=" + coach_id +
                ", id_category=" + id_category +
                '}';
    }
    public workouts(){}

    public workouts(String workout_name, String wk_description, String wk_intensity, String wk_image) {
        this.workout_name = workout_name;
        this.wk_description = wk_description;
        this.wk_intensity = wk_intensity;
        this.wk_image = wk_image;
    }

    public workouts(String workout_name, String wk_description, String wk_intensity, String wk_image, int coach_id) {
        this.workout_name = workout_name;
        this.wk_description = wk_description;
        this.wk_intensity = wk_intensity;
        this.wk_image = wk_image;
        this.coach_id = coach_id;
    }
}
