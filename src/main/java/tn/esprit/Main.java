package tn.esprit;

import tn.esprit.entities.product;
import tn.esprit.services.productservice;
import tn.esprit.entities.trade;
import tn.esprit.services.tradeservice;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        product u1 = new product(7, "52973452", "jawher", "123456789", "sert", 99);
        trade u2 = new trade(13, 1, 1,"LIBYA","ASDF567","ANAS");
        productservice us = new productservice();
        tradeservice ur = new tradeservice();
       try {
            us.add(u1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ;
        }



/*

        try {
            List<trade> trades = ur.display();
            for (trade gm : trades) {
                System.out.println(gm.getID_TRADE() + " | " + gm.getID_PRODUCT()+ " | " + gm.getID_USER() +" | " + gm.getLOCATION() +" | " + gm.getTRADESTATUS() +" | " + gm.getNAME());
            }
        } catch (SQLException e) {
            System.out.println("Error" + e.getMessage());
        }

 */


       /*try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the idtrade to delete: ");
            int ID_TRADE = scanner.nextInt();
            ur.delete(ID_TRADE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        */

    }

    /*try{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the idtrade to delete: ");
        int ID_TRADE = scanner.nextInt();
        ur.delete(ID_TRADE );
    } catch (SQLException e) {
        throw new SQLException(e);
    }

     */

      /*  try {
            ur.update(u2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());



        }

       */


        /*try {
            System.out.println(us.diplayList());
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
         */
    }