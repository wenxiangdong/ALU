import java.util.ArrayList;

/**
 * Created by wenxi on 2017/4/11.
 */
public class Try {
    public static void main(String[] args){
        ArrayList<Integer> list=new ArrayList<>();


        for (int i=0;i<10;i++){
            list.add(i);
        }

        //list.remove(3);
        //list.set(16,11);

        System.out.println(list.get(3));
        System.out.println(list.get(4));


    }
}
