package chap02.section2;

import java.util.ArrayList;

public class ie {

    public static void main(String[] args){

        ArrayList<String[]> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("123");
        list2.add("456");




//        for(int i = 0; i<2; i++){
//            list.add(new String[]{"eom" + i});
//        }
//
//        for(int i = 0;i <2; i++)
//        System.out.println(list.get(i)[0]);
//
//
//        for(String str : list2)
//        {
//            System.out.println(str);
//        }



        String str = "eom";
        String str2 = "eom2";

        System.out.println(String.format("%s plus %s", str, str2));

    }
}
