import javax.crypto.CipherInputStream;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import org.w3c.dom.DOMConfiguration;

/**
 * ģ��ALU���������͸���������������
 * @author ������ 161250060
 *
 */

public class ALU {
	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {
		// TODO YOUR CODE HERE.
		//StringBuilder result=new StringBuilder();
		
		StringBuilder result=new StringBuilder();
		Long num=Long.parseLong(number);//������ת��Ϊ64λ����
		
		for (int i = 0; i < length; i++) {
			result.append(num&1);
			num=num>>1;
		}
		
		return result.reverse().toString();
		
	}
	
	
	/**
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		
		final int base=(int) Math.pow(2, eLength-1)-1;//ƫ�ó���
		System.out.println("base:"+base);

		StringBuilder s=new StringBuilder();

		StringBuilder inf=new StringBuilder();
		for (int i=0;i<eLength;i++){
			inf.append("1");//ȫ1����
		}
		for (int i=0;i<sLength;i++){
			inf.append("0");//ȫ��β��
		}

		StringBuilder zero=new StringBuilder();
		for(int i=0;i<eLength+sLength;i++){
			zero.append("0");
		}

		switch (number){
			case "0":
				return "0"+zero.toString();
			case "+Inf":
				return "0"+inf.toString();
			case "-Inf":
				return "1"+inf.toString();
			case "NaN":
				return "0"+inf.replace(inf.length()-1,inf.length(),"1").toString();
			default:

				char sign=(number.charAt(0)=='-')?'1':'0';
				System.out.println("after sign");
				double num = Double.parseDouble(number);
				if(num==0){
					System.out.println("is zero");
					return sign+zero.toString();
				}

//				String integerPattern=String.valueOf(Math.abs((int)num));

				s.append(
						Integer.toBinaryString(
							Math.abs((int)num)
						)
				);
				int oldIndexOfDot=s.length();
				System.out.println("old:"+s.toString());

				double doublePattern=Math.abs(num-(int)num);
				int temp=0;//���ڼ�¼ÿ�γ�2�Ƿ����1
				for(int i=0;i<sLength;i++){
					temp=(doublePattern*2>=1.0)?1:0;
					doublePattern=doublePattern*2-temp;
					s.append(temp);
				}
				System.out.println("new:"+s.toString());

				//�����ֵ�ĵ�һ��1
				int newIndexOfDot=0;
				for(;newIndexOfDot<s.length();newIndexOfDot++){
					if(s.charAt(newIndexOfDot)=='1'){
						break;
					}
				}

				//���ҹ沢�������
				int delt=oldIndexOfDot-(newIndexOfDot+1);
				if(newIndexOfDot>sLength) {
					delt=-base;
					newIndexOfDot=oldIndexOfDot-1;
				}
				System.out.println("delt:"+delt);
				String e=integerRepresentation(
						String.valueOf(delt+base),eLength
				);
				System.out.println("e"+e);

				if(s.length()-1-newIndexOfDot>sLength){
					System.out.println("to deleted:"+s.substring(sLength+1, s.length()));
					s.replace(sLength+1,s.length(),"");
				}else{
					for(int i=s.length()-1-newIndexOfDot;i<sLength;i++){
						s.append("0");
					}
				}

				return sign+e+s.substring(newIndexOfDot+1,newIndexOfDot+1+sLength);


		}


	}
	
	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int) floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754 (String number, int length) {
		// TODO YOUR CODE HERE.
		
		if(length==32){
			return floatRepresentation(number,8,23);
		}else {
			return floatRepresentation(number, 11, 52);
		}

	}
	
	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue (String operand) {
		// TODO YOUR CODE HERE.		
		//���� 
		if(operand.substring(0, 1).equals("1")){
			//ȡ��
			char[] temps=negation(operand).toCharArray();
	
			//��1
			for(int i=temps.length-1;i>=0;i--){
				if(temps[i]=='1'){
					temps[i]='0';
				}else {
					temps[i]='1';
					break;
				}				
			}			
			String opposite=String.copyValueOf(temps);
			//��С����
			if(opposite.equals(operand)) return String.valueOf((int)(0-Math.pow(2,operand.length()-1)));

			//���������ķ���
			return "-"+integerTrueValue(opposite);
			
			
		//���� 	
		}else{	
			int result=0;
			for(int i=0;i<operand.length();i++){
				result=result*2+Integer.parseInt(operand.substring(i,i+1));
			}

			return String.valueOf(result);
		}
		
		
	}
	
	/**
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		//ȷ������
		 String flag=(operand.substring(0,1).equals("0"))?"":"-";
		
		 //��ȡ����
		 String e=operand.substring(1,1+eLength);
		System.out.println("e:"+e);
		//����integerTrueValue���õ��������ֵ
		 int eNum=Integer.parseInt(integerTrueValue("0"+e));
		 
		 
		 //��ȡβ��
		 String s=operand.substring(1+eLength);
		 char[] sChars=s.toCharArray();
		 double sNum=0;
		 for(int i=sChars.length-1;i>=0;i--){
//			 System.out.println(i+":"+sChars[i]);
			 sNum=sNum/2.0+sChars[i]-48;
		 }
		 sNum=sNum/2.0;//�ó�β��
		System.out.println("dot:"+sNum);
		 //System.out.println(result);
		 
		 double result;
		 int base=(int) (Math.pow(2, eLength-1)-1);//ƫ�ó���
		System.out.println("base:"+base);
		 if(eNum==2*base+1){
			 if(sNum==0){
				 return (operand.charAt(0)=='0'?"+":"-")+"Inf";
			 }else{
				 return "NaN";
			 }
		 }else{
			 if(eNum==0){//�ǹ��
				 if(sNum==0){
				 	return "0";
				 }
				 result=sNum*Math.pow(2, -base+1);
			 }else{//���		
				 result = (1.0+sNum)*Math.pow(2, eNum-base);
			 }


			 //�����ж�

				 return flag+String.valueOf(result);

		 }
		 
		
	}
	
	/**
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation (String operand) {
		// TODO YOUR CODE HERE.
		char[] temps=operand.toCharArray();
		//System.out.println("length:"+temps.length);
		for (int i = 0; i < temps.length; i++) {
			if(temps[i]=='1'){
				temps[i]='0';
			}else {
				temps[i]='1';
			}	
		}
		return String.copyValueOf(temps);
	}
	
	/**
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
	 */
	public String leftShift (String operand, int n) {
		// TODO YOUR CODE HERE.
		
		char[] operandChars=operand.toCharArray();
		char[] newOperand=new char[operand.length()];
		
		for (int i = 0; i < operand.length(); i++) {
			if(i<operand.length()-n){
				newOperand[i]=operandChars[i+n];
			}else{
				newOperand[i]='0';
			}
		}
		
		return String.copyValueOf(newOperand);
	}
	
	/**
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
	 */
	public String logRightShift (String operand, int n) {
		// TODO YOUR CODE HERE.
		char[] operandChars=operand.toCharArray();
		char[] newOperand=new char[operand.length()];
		
		for (int i = 0; i < newOperand.length; i++) {
			if(i>=n){
				newOperand[i]=operandChars[i-n];
			}else{
				newOperand[i]='0';
			}
		}
		
		return String.copyValueOf(newOperand);
	}
	
	/**
	 * �������Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		// TODO YOUR CODE HERE.
		char[] operandChars=operand.toCharArray();
		char[] newOperand=new char[operand.length()];
		
		for (int i = 0; i < newOperand.length; i++) {
			if(i>=n){
				newOperand[i]=operandChars[i-n];
			}else{
				newOperand[i]=operandChars[0];
			}
		}	
		return String.copyValueOf(newOperand);
	}
	
	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder (char x, char y, char c) {
		// TODO YOUR CODE HERE.
		StringBuilder result =new StringBuilder();
		int temp = (x-48)+(y-48)+(c-48);

		result.append(temp/2);
		result.append(temp%2);

		return result.toString();
	}
	
	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder (String operand1, String operand2, char c) {
		// TODO YOUR CODE HERE.
		char[] bits_1=operand1.toCharArray();
		char[] bits_2=operand2.toCharArray();
		StringBuilder result=new StringBuilder();

		//���㸨����ʽ
		int[] P=new int[4];
		int[] G=new int[4];

		for(int i=0;i<4;i++){
			P[i]=(bits_1[3-i]-48)|(bits_2[3-i]-48);
			G[i]=(bits_1[3-i]-48)&(bits_2[3-i]-48);
		}

		//��λ��ã�ģ�Ⲣ��
		int[] C=new int[5];
		C[0]=c-48;
		for(int i=1;i<5;i++){
			C[i]=G[i-1]|(P[i-1]&C[i-1]);
			//System.out.println(C[i]);
		}


		for(int i=0;i<4;i++){
			result.append(fullAdder(bits_1[3-i],bits_2[3-i],(char)(C[i]+48)).substring(1));
			//System.out.println(fullAdder(bits_1[3-i],bits_2[3-i],(char)(C[i]+48)).substring(0));
		}

		result.append(C[4]);
		return result.reverse().toString();
	}
	
	/**
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder (String operand) {
		// TODO YOUR CODE HERE.
		int c=1;
		int c2=0;//�θ߽�λ
		int temp;

		char[] bits=operand.toCharArray();
		StringBuilder result=new StringBuilder();

		for(int i=bits.length-1;i>=0;i--){
			temp=(bits[i]-48)^c;
			c=(bits[i]-48)&c;
			if(i==1){
				c2=c;
			}
			result.append(temp);
		}
		result.append(c^c2);//�����־���
		return result.reverse().toString();
	}
	
	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", ��0��, 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		// TODO YOUR CODE HERE.
		//System.out.println(integerTrueValue(operand1)+"+"+integerTrueValue(operand2));
		String num1=integerRepresentation(integerTrueValue(operand1),length);
		String num2=integerRepresentation(integerTrueValue(operand2),length);
		StringBuilder result=new StringBuilder();
		StringBuilder tempBuilder;
		//char c1='0';

		for(int i=length-1;i>=3;i=i-4){//ÿ4λ4λ�ؼ���
			String temp=claAdder(num1.substring(i-3,i+1),num2.substring(i-3,i+1),c);
			tempBuilder=new StringBuilder(temp);
			//c1=c;
			c=temp.charAt(0);
			result.append(tempBuilder.reverse().substring(0,4));
			//System.out.println(result);
		}

//		result.append(String.valueOf((c-48)^(c1-48)));
		if(num1.charAt(0)!=num2.charAt(0)){
			//�������
			result.append("0");
		}else{
			if(num1.charAt(0)==result.charAt(result.length()-1)){
				result.append("0");
			}else{
				result.append("1");
			}
		}

		return result.reverse().toString();
	}
	
	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.

		return adder(operand1,operand2,'0',length);
	}
	
	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return adder(operand1,negation(operand2),'1',length);
	}
	
	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		//��ʼ��
		StringBuilder resultBuilder=new StringBuilder();
		for(int i =0;i<length-operand2.length();i++){
			resultBuilder.append("0");//��ʼP
		}
		resultBuilder.append(operand2);
		String result=resultBuilder.toString();
		System.out.println(result);

		//������
		int oLen1=operand1.length();
		String adder=leftShift(
				integerRepresentation(integerTrueValue(operand1),length),
				length-oLen1
		);
		//System.out.println("x:"+adder);
		System.out.println("adder:"+adder);

		//����λ
		String additionalBit="0";
		//[-x]��
		String adderOpposite=oneAdder(negation(adder)).substring(1);//����oneAdder �� negation �� adder ����ȡ����1
		System.out.println("x����"+adderOpposite);

		String lastBits=result.substring(result.length()-1)+additionalBit;
		//��ʼ����
		for (int i=0;i<oLen1;i++){
			System.out.println("lastBits:"+lastBits);
			if (lastBits.equals("01")){
				//System.out.println("01");
				result=adder(result.toString(),adder,'0',length).substring(1);
			}else if (lastBits.equals("10")){
				//System.out.println("10");
				result=adder(result.toString(),adderOpposite,'0',length).substring(1);
			}

			//System.out.println(result);
			//���ø���λ
			additionalBit=result.substring(result.length()-1);
			//����
			result=ariRightShift(result.toString(),1);
			System.out.println(result);
			//���������жϵ������λ
			lastBits = result.substring(result.length()-1)+additionalBit;

		}
		if(operand1.charAt(0)!=operand2.charAt(0)){
			if(result.charAt(0)=='0'){
				return "1"+result;
			}
		}else{
			if(result.charAt(0)=='1'){
				return "1"+result;
			}
		}
		return  "0"+result;
	}
	
	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.

		//��������ʼ��
		//���� ���Ƶ���չ
		StringBuilder operand1Builder=new StringBuilder();
		for (int i =0; i<length;i++){
			operand1Builder.append(operand1.charAt(0));
		}
		operand1Builder.append(operand1);
		operand1=operand1Builder.toString();
		System.out.println("ԭʼR:"+operand1);

		//�����ĳ�ʼ����
		//�����Ĳ�
		String operand2Opposite = leftShift(integerRepresentation(
				integerTrueValue(oneAdder(negation(operand2)).substring(1)),2*length),
		length);
		//����
		String operand2OldValue=operand2;
		operand2=leftShift(integerRepresentation(
				integerTrueValue(operand2),2*length),
				length);
		System.out.println("������"+operand2);


		//��һ������X+Y
		char OF='0';
		char Q;
		String temp;
		if(operand1.charAt(0)==operand2.charAt(0)){
			temp=integerAddition(operand1, operand2Opposite,2*length).substring(1);
		}else{
			temp = integerAddition(operand1,operand2,2*length).substring(1);
		}
		//Qn�ļ���
		if(temp.charAt(0)==operand2.charAt(0)){//ͬ1
			Q='1';
		}else{
			Q='0';
		}
		//System.out.println("Q:"+Q);
		//�ж�������
		//X��Yͬ����QnΪ1
		//X��Y�����QnΪ0
		OF=(operand1.charAt(0)==operand2.charAt(0)&&Q=='1')
				||(operand1.charAt(0)!=operand2.charAt(0)&&Q=='0')?'1':'0';
		//System.out.println("OF:"+OF);

		for(int i = 0;i<length;i++){
			//����һλ
			temp=leftShift(temp,1);
			if(Q=='1'){
				temp=oneAdder(temp).substring(1);
				//2R-Y
				temp=integerAddition(temp,operand2Opposite,2*length).substring(1);
			}else{
				//2R+Y
				temp=integerAddition(temp,operand2,2*length).substring(1);
			}

			//�ж�����
			if(temp.charAt(0)==operand2.charAt(0)){
				Q='1';
			}else{
				Q='0';
			}
		}

		//���һ������
		String reminder=temp.substring(0,length);
		String result=temp.substring(length);
		result=leftShift(result,1);
		//���һ������
		if(Q=='1'){
			result=oneAdder(result).substring(1);
		}

		System.out.println("����ǰ��"+result+","+reminder);
		//��������
		//�����뱻������ͬ��
		if(reminder.charAt(0)!=operand1.charAt(0)){
			System.out.println("�����뱻������ͬ��");
			//�����������
			if(operand2.charAt(0)!=operand1.charAt(0)){
//				result=oneAdder(result).substring(1);
				reminder=integerSubtraction(reminder,operand2OldValue,length).substring(1);
			}else{
				//�����ӳ���
				reminder=integerAddition(reminder,operand2OldValue,length).substring(1);
			}
		}
		//�̵�����
		//��x��y��ͬ�ţ���1
		if(operand2.charAt(0)!=operand1.charAt(0)){
			result=oneAdder(result).substring(1);
		}

		//����
		System.out.println( OF+result+reminder);
		return OF+result+reminder;
	}
	
	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int) integerAddition}��
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		//��չ
		String extendOperand1=integerRepresentation(integerTrueValue("0"+operand1.substring(1)),length);
		String extendOperand2=integerRepresentation(integerTrueValue("0"+operand2.substring(1)),length);
		String operandOpposite="0"+oneAdder(negation(operand2.substring(1))).substring(1);

		String result;
		if(operand1.charAt(0)==operand2.charAt(0)){
			//ͬ�����
			result = adder(extendOperand1, extendOperand2,'0',length);
			System.out.println("result:"+result);
			return result.charAt(0)+operand1.substring(0,1)+result.substring(1);
		}else{
			//������
			result = adder("0"+operand1.substring(1),operandOpposite,'0',operand1.length()).substring(1);
			System.out.println("result:"+result);
			//�����ֵλû�н�λ��˵�����Ϊ������Ҫ��
			if(result.charAt(0)=='0'){
				result=oneAdder(negation(result)).substring(1);
				return "0"+negation(operand1).substring(0,1)+
						integerRepresentation(integerTrueValue(result.substring(1)),length);
			}else{
				return "0"+operand1.substring(0,1)+
						integerRepresentation(integerTrueValue(result.substring(1)),length);
			}
		}
	}
	
	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
//		String fResult;
//		String eResult;
//		String sResult;
//
//
//		//�Խ�
//		String e1=operand1.substring(1,1+eLength);
//		String e2=operand2.substring(1,1+eLength);
//		//β��
//		StringBuilder s1Builder=new StringBuilder(operand1.substring(1+eLength,1+eLength+sLength));
//		for(int i=0;i<gLength;i++){
//			s1Builder.append("0");//����λ
//		}
//		StringBuilder s2Builder=new StringBuilder(operand2.substring(1+eLength,1+eLength+sLength));
//		for(int i=0;i<gLength;i++){
//			s2Builder.append("0");//����λ
//		}
//		String s1,s2;
//
//		//�ȽϽ���
//		int e1Integer=Integer.parseInt(integerTrueValue("0"+e1));
//		int e2Integer=Integer.parseInt(integerTrueValue("0"+e2));
//		int eResultInteger;
//		int deltE=e1Integer-e2Integer;
//
//		if(deltE<0){
//			eResultInteger=e2Integer;
//			eResult=e2;
//			s1=logRightShift(s1Builder.toString(),deltE);
//			s2=s2Builder.toString();
//		}else {
//			eResultInteger=e1Integer;
//			eResult=e1;
//			s2=logRightShift(s2Builder.toString(),deltE);
//			s1=s1Builder.toString();
//		}
//
//
//
//
//
//		//β�����
//		double sResultDouble=Double.parseDouble(floatTrueValue(operand1.substring(0,1)+s1,0,sLength+gLength))+
//				Double.parseDouble(floatTrueValue(operand2.substring(0,1)+s2,0,sLength+gLength));
//		System.out.println(sResultDouble);
//
//		sResult=floatRepresentation(String.valueOf(sResultDouble),2,sLength+gLength).substring(1);
//		int n=0;//��¼�����˶��ٴ�
//		while (sResult.charAt(0)!='1'){
//			sResult=leftShift(sResult, 1);
//			n++;
//		}
//		sResult=sResult.substring(1,1+sLength);
//
//		eResult=integerRepresentation(
//				integerSubtraction("0"+eResult,
//				integerRepresentation(String.valueOf(n),eLength+1),
//				eLength+1), eLength+1).substring(1);
//
//		//����λ
//		fResult=(sResultDouble<0.0)?"1":"0";
//
//
//
//
//
//		return "0"+fResult+eResult+fResult;
		Double d1=Double.parseDouble(floatTrueValue(operand1,eLength,sLength));
		System.out.println("d1:"+d1);
		Double d2=Double.parseDouble(floatTrueValue(operand2,eLength,sLength));
		System.out.println("d2:"+d2);

		String result=floatRepresentation(
				String.valueOf(d1+d2),
				eLength,sLength);

		Double reDouble=Double.parseDouble(floatTrueValue(result,eLength,sLength));

		return ((d1+d2==reDouble)?"0":"1")+result;
	}
	
	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int) floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		if(operand2.charAt(0)=='0'){
			operand2="1"+operand2.substring(1);
		}else{
			operand2="0"+operand2.substring(1);
		}

		return floatAddition(operand1,operand2,eLength,sLength,gLength);
	}
	
	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int) integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}



}


