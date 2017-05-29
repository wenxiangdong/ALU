
public class test {
	public static void main(String[] args) {
		ALU a=new ALU();
		System.out.println("integerRepresentation----------------");
		System.out.println(a.integerRepresentation("-123",32));
		System.out.println(a.integerRepresentation("-123",64));
		System.out.println(a.integerRepresentation("123",32));
		System.out.println(a.integerRepresentation("123",64));
		System.out.println(a.integerRepresentation("0",32));
		System.out.println(a.integerRepresentation("15",4));
		
		System.out.println("integerTrueValue----------------");
		System.out.println(a.integerTrueValue("1000000000"));
		System.out.println(a.integerTrueValue(Integer.toBinaryString(-128)));
		System.out.println(a.integerTrueValue("01000011"));
		System.out.println(a.integerTrueValue(Integer.toBinaryString(-45)));
		
		System.out.println("negation----------------");
		System.out.println(a.negation("10101010"));
		
		System.out.println("leftShift----------------");
		System.out.println(a.leftShift("00001111", 3));
		
		System.out.println("logRightShift----------------");
		System.out.println(a.logRightShift("11110000", 4));
		
		System.out.println("ariRightShift----------------");
		System.out.println(a.ariRightShift("11110000", 4));
		System.out.println(a.ariRightShift("01110000", 4));
		
		System.out.println("floatTrueValue----------------");
		System.out.println(a.floatTrueValue("01111111100000000",8, 8));
		System.out.println(a.floatTrueValue("11111111100000001",8, 8));
		System.out.println(a.floatTrueValue("01000001001101100000", 8, 11));
		System.out.println(a.floatTrueValue("11000001001101100000", 8, 11));
		System.out.println(a.floatTrueValue(a.floatRepresentation("25.8125", 8, 23), 8, 23));
		System.out.println(a.floatTrueValue(a.floatRepresentation("123.75", 8, 23), 8, 23));
		
		System.out.println("floatRepresentation----------------");
		System.out.println(a.floatRepresentation("0.25", 8, 11));
		System.out.println(a.floatRepresentation("11.375", 8, 11));		
		System.out.println("01000001001101100000");
		System.out.println(a.floatRepresentation("-11.375", 8, 11));
		System.out.println(a.floatRepresentation("11.375", 8, 23));
		System.out.println("25.8125-->"+a.floatRepresentation("25.8125", 8, 23));
		System.out.println(a.floatRepresentation("0", 8, 11));
		System.out.println(a.floatRepresentation("+Inf", 8, 11));
		System.out.println(a.floatRepresentation("-Inf", 8, 11));		
		System.out.println("123.75-->"+a.floatRepresentation("123.75", 8, 23));

		System.out.println("ieee754----------------");
		System.out.println(a.floatTrueValue("11000001011111000000000000000000",8,23)+"-->"+a.ieee754("-15.75",32));
		System.out.println("-15.75-->"+a.ieee754("-15.75",64));
		System.out.println(a.ieee754("-Inf",64));


		System.out.println("fullAdder----------------");
		System.out.println(a.fullAdder('1','0','1'));
		System.out.println(a.fullAdder('1','1','1'));


		System.out.println("claAdder----------------");
		System.out.println(a.claAdder("0001", "0001", '1'));
		System.out.println(a.claAdder("1101", "1001", '0'));
		System.out.println(a.claAdder("1101", "1100", '0'));

		System.out.println("oneAdder----------------");
		System.out.println(a.oneAdder("0111"));
		System.out.println(a.oneAdder("1111"));

		System.out.println("adder----------------");
		System.out.println("不溢出:"+a.adder("0111","1000",'1',8));
		System.out.println("不溢出:"+a.adder("0011","1000",'1',8));

		System.out.println("integerAddition----------------");
		System.out.println(a.integerAddition("0111","0011",32));

		System.out.println("integerSubtraction----------------");
		System.out.println(a.integerSubtraction("0111","1000",32));


		System.out.println("integerMultiplication----------------");
		System.out.println(a.integerMultiplication("0011","0011",4));
		System.out.println(a.integerMultiplication("0011","1101",4));
		System.out.println(a.integerTrueValue("00111100")+"*"+a.integerTrueValue("10111100")+"\n"+a.integerMultiplication("00111100","10111100",8));
		System.out.println(a.integerTrueValue("1111000000010000"));
		System.out.println("45*(-12)="+45*(-12)+"\n"+a.integerMultiplication(a.integerRepresentation("45",16),a.integerRepresentation("-12",16),16)+"\n"+a.integerTrueValue("11111111111111111111110111100100"));
		System.out.println(a.integerMultiplication("1000","1000",4));
		System.out.println(a.integerMultiplication("1001","1000",4));


		System.out.println("integerDivision----------------");
		System.out.println(a.integerDivision("1001","0011",4));
		System.out.println(a.integerDivision("0111","0011",4));
		System.out.println(a.integerDivision("0100","0010",4));
		System.out.println(a.integerDivision("01000011","00001000",8));
		System.out.println(a.integerTrueValue("01000011")+"/"+a.integerTrueValue("00001000"));

		System.out.println("signedAddition------------------");
		System.out.println(a.signedAddition("1100","1011",8));
		System.out.println(a.signedAddition("0100","1011",8));
		System.out.println(a.signedAddition("0100","1011",4));


		System.out.println("floatAddition----------------------");
		System.out.println(a.floatAddition("001110000","001111000",4,4,1));
		System.out.println(a.floatAddition("011101111","010001111",4,4,1));





		//		float f=(float)7/2+1;s
//		System.out.println(f);
//		
}
}
