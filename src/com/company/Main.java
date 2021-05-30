package com.company;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    static int i = 0;
    static Random random = new Random();
    static ArrayList<Place> places = new ArrayList<>();
    static ArrayList<Walker> walkers = new ArrayList();
    static int num_places = 20;
    static int num_walkers = 5;
    static JLabel main_text = new JLabel("my position = ");
    static JLabel params_text = new JLabel("good =  price = ");
    static JLabel coins_text = new JLabel("coins =  goods = ");
    static JLabel power_text = new JLabel("power");

    public static void main(String[] args) {
        for(int i = 0;i< num_places;i++){
            places.add(new Place());
        }

        Walker me = new Walker(random.nextInt(places.size()-0)+0);


        for(int i = 0; i< num_walkers;i++){
            walkers.add(new Walker(random.nextInt(places.size()-0)+0));
        }
        walkers.add(new Walker(random.nextInt(places.size()-0)+0));


        JFrame window = new JFrame();
        window.setSize(500,500);






        main_text.setBounds(150,-90,200,200);
        params_text.setBounds(130,-70,200,200);
        coins_text.setBounds(150,-40,200,200);
        power_text.setBounds(150,-20,200,200);

        window.add(main_text);
        window.add(params_text);
        window.add(coins_text);
        window.add(power_text);




        JButton btn1 = new JButton("left");
        btn1.setBounds(0,350,200,50);
        window.add(btn1);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walkers.get(num_walkers).turn_left();
                info();
                turn();
            }
        });



        JButton btn2 = new JButton("right");
        btn2.setBounds(300,350,200,50);
        window.add(btn2);
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                walkers.get(num_walkers).turn_right();
                info();
                turn();
            }
        });


        JButton btn3 = new JButton("buy");
        btn3.setBounds(0,300,200,50);
        window.add(btn3);
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                walkers.get(num_walkers).buy();
                info();
                turn();
            }
        });

        JButton btn4 = new JButton("sell");
        btn4.setBounds(300,300,200,50);
        window.add(btn4);
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walkers.get(num_walkers).sell();
                info();
                turn();
            }
        });


        JButton btn5 = new JButton("buy_power");
        btn5.setBounds(0,200,200,50);
        window.add(btn5);
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walkers.get(num_walkers).buy_power();
                info();
                turn();
            }
        });

        JButton btn6 = new JButton("attack");
        btn6.setBounds(300,200,200,50);
        window.add(btn6);
        btn6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                walkers.get(num_walkers).attack();
                info();
                turn();
            }
        });

        window.setLayout(null);
        window.setVisible(true);

        GameThread t1 = new GameThread();
        t1.start();

    }



    public static  void turn(){
        for(int i  = 0; i < walkers.size()-1;i++){
            int choice = random.nextInt(6-0)+0;
            switch(choice){
                case 0:walkers.get(i).turn_left();break;
                case 1:walkers.get(i).turn_right();break;
                case 2:walkers.get(i).buy();break;
                case 3:walkers.get(i).sell();break;
                case 4:walkers.get(i).buy_power();break;
                case 5:walkers.get(i).attack();break;
            }
        }
    }


    public static void info(){
        main_text.setText("my position = "+Integer.toString(walkers.get(num_walkers).position));
        params_text.setText(String.format("good = %d price = %.2f",places.get(walkers.get(num_walkers).position).good,places.get(walkers.get(num_walkers).position).price));
        coins_text.setText(String.format("coins = %d goods = %d",walkers.get(num_walkers).coins,walkers.get(num_walkers).good));
        power_text.setText(String.format("power = %d",walkers.get(num_walkers).power));
    }







}



class Place{
    Random random = new Random();
    int good = 5;
    float price = 7;
    public Place(){
        this.good = random.nextInt(100-10)+10;
        this.price = 100/this.good;
    }
    public void set_price(){
        this.price = 200/this.good;
    }

}

class Walker{
    int power = 0;
    int position = 0;
    int good = 0;
    int coins = 100;

    public Walker(int position){
        this.position = position;
    }

    public void turn_left(){
        if (this.position > 0){
            this.position -=1;
        }
    }

    public void turn_right(){
        if (this.position < Main.num_places-1){
            this.position +=1;
        }
    }

    public void buy(){
        if(Main.places.get(this.position).good > 0 && this.coins >= Main.places.get(this.position).price){
            this.good += 1;
            this.coins -= Main.places.get(this.position).price;
            Main.places.get(this.position).good -=1;
            Main.places.get(this.position).set_price();
        }
    }
    public void sell(){
        if (this.good > 0){
            this.good -=1;
            this.coins += Main.places.get(this.position).price;
            Main.places.get(this.position).good += 1;
            Main.places.get(this.position).set_price();
        }
    }

    public void buy_power(){
        if(this.coins >=100){
            this.power += 1;
            this.coins -= 100;
        }
    }

    public void attack(){
            for (int i = 0 ; i < Main.walkers.size();i++){
                if(Main.walkers.get(i).position == this.position
                        && Main.walkers.get(i) != this
                        && Main.walkers.get(i).power < this.power) {
                    this.coins += Main.walkers.get(i).coins/2;
                    Main.walkers.get(i).coins = Main.walkers.get(i).coins/2;
                }
            }

    }

}


class GameThread extends Thread{
    public void run() {
        while (true){

            Main.turn();
            Main.info();
            try{
                Thread.sleep(100);
            }
            catch (Exception e){

            }

        }
    }

}