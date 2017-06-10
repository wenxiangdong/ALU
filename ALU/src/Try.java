import java.util.ArrayList;

/**
 * Created by wenxi on 2017/4/11.
 */
public class Try {
    public static void main(String[] args){
        ALU alu = new ALU();
        System.out.println(alu.floatTrueValue("0000001110010", 4, 8));
        System.out.println(alu.floatTrueValue("0111111001000", 4, 8));



    }
}
