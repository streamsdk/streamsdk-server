package com.ugc.test;

public class RandomTest
{
    public static void main(String[]args){

       for(int i = 0; i < 100; i++)
        System.out.println((int)(Math.random() * 1000000 % 8999) + 1000);

        
    }

}
