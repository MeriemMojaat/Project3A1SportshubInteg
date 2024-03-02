package tn.esprit.tests;

import tn.esprit.services.workoutcategoryService;
import tn.esprit.services.workoutsService;

public class Main {
    public static void main(String[] args) {

     //   workoutcategory w1 = new workoutcategory("Cardio", "We have a lot of cardio exercises ", "image");
     //   workoutcategory w2 = new workoutcategory("yoga", "We have a lot of yoga exercises ", "image");
     //   workoutcategory w3 = new workoutcategory("Training", "We have a lot of yoga exercises ", "image");

     //   workouts t1 = new workouts("running", "the best option for your cardio ", "hard","image",1,3);
      //  workouts t2 = new workouts("weight lifting", "the best option for your training ", "easy","image",1,5);


        workoutcategoryService ps = new workoutcategoryService();
        workoutsService pst = new workoutsService();


       /*try {
             pst.delete(4);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/
       /* try {
            ps.update(w3);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/
      /*   try {
           ps.delete(e1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/

       /* try {
            System.out.println(pst.display());
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }*/

     /* try {
            pst.add(t2);
           System.out.println(pst.display());
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
    }
}