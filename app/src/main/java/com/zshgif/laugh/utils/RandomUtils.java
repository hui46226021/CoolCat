package com.zshgif.laugh.utils;

/**
 * Created by zhush on 15-10-30.
 */
public class RandomUtils {


    public static String getOrderId(){
      Long time = System.currentTimeMillis();
         String  str[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        StringBuilder orderid = new StringBuilder();
        orderid.append("APP");
        for(int i =0;i<3;i++){
            orderid.append(str[ (int)(Math.random()*24)]);

        }
        orderid.append(time+"");
        return orderid.toString();
    }
    public static String getRandom(){
        Long time = System.currentTimeMillis();
        String  str[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        StringBuilder orderid = new StringBuilder();
        for(int i =0;i<10;i++){
            orderid.append(str[ (int)(Math.random()*24)]);

        }
        return orderid.toString();
    }
}
