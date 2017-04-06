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
	
		switch(number){
		case "0":
			StringBuilder zero=new StringBuilder();
			for(int i=0;i<1+eLength+sLength;i++)
				zero.append("0");//����ȫ0
			return zero.toString();
		case "+Inf":
			StringBuilder positiveInf=new StringBuilder("0");
			positiveInf.append(integerRepresentation(String.valueOf(2*base+1), eLength));
			positiveInf.append(integerRepresentation("0", sLength));
			return positiveInf.toString();
		case "-Inf":
			StringBuilder negetiveInf=new StringBuilder("1");
			negetiveInf.append(integerRepresentation(String.valueOf(2*base+1), eLength));
			negetiveInf.append(integerRepresentation("0", sLength));
			return negetiveInf.toString();
		case "NaN":
			StringBuilder NaN=new StringBuilder("1");
			NaN.append(integerRepresentation(String.valueOf(2*base+1), eLength));
			NaN.append(integerRepresentation("1", sLength));
			return NaN.toString();
		default:
			double numberDouble=Double.parseDouble(number);			
			//����
			String flag=(numberDouble<0)?"1":"0";
			
			//��������
			int integerPattern=(int)numberDouble;
			
			//����integerRepresentation �õ��������ֵĶ����Ʊ�ʾ
			StringBuilder result=new StringBuilder();
			result.append(integerRepresentation(String.valueOf(
					Math.abs(integerPattern)),
					eLength));
			//System.out.println("�������֣�"+result.toString());
			
			//β��
			int dotIndex=result.length();//δ���ǰ��С����λ��
			//result.append(".");
			
			double s=numberDouble-integerPattern;
			if(s<0) s=0-s;
			//System.out.println(s);
			int temp=0;//���ڼ�¼ÿ�γ�2�Ƿ�õ�1
			for (int i = 0; i < sLength; i++) {
				temp=(s*2>=1)?1:0;
				//System.out.println(s*2);
				result.append(
						String.valueOf(
								temp));
				s=s*2-temp;
				//System.out.println(s);
			}
			
			//System.out.println("С�����֣�"+result.substring(dotIndex));
			
			//���
			char[] bits=result.toString().toCharArray();
			int dotIndexAfter=0;//��񻯺��С����λ��
			for(int i=0;i<bits.length;i++){
				dotIndexAfter++;
				if(bits[i]=='1'){//�������ҵ���һ�� 1
					break;
				}
			}
			String sString=result.substring(dotIndexAfter,dotIndexAfter+sLength);//�õ���񻯺��β��
			//����
			String eString=integerRepresentation(String.valueOf(dotIndex-dotIndexAfter+base), eLength);		
			return flag+eString+sString;
			
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
		 String flag=(operand.substring(0,1).equals("0"))?"+":"-";
		
		 //��ȡ����
		 String e=operand.substring(1,1+eLength);		
		//����integerTrueValue���õ��������ֵ
		 int eNum=Integer.parseInt(integerTrueValue("0"+e));
		 
		 
		 //��ȡβ��
		 String s=operand.substring(1+eLength);
		 char[] sChars=s.toCharArray();
		 double sNum=0;
		 for(int i=sChars.length-1;i>=0;i--){
			 sNum=sNum/2.0+sChars[i]-48;
		 }
		 sNum=sNum/2.0;//�ó�β��
		 //System.out.println(result);
		 
		 double result;
		 int base=(int) (Math.pow(2, eLength-1)-1);//ƫ�ó���
		 if(eNum==2*base+1){
			 if(sNum==0){
				 return flag+"Inf";
			 }else{
				 return "NaN";
			 }
		 }else{
			 if(eNum==0){//�ǹ��
				 result=sNum*Math.pow(2, -126);
			 }else{//���		
				 result = (1.0+sNum)*Math.pow(2, eNum-base);
			 }
			 //�����ж�
			if(flag.equals("+")){
				 return String.valueOf(result);
			}else{
				 return flag+String.valueOf(result);
			}
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
		char  c1=c;

		for (int i=bits_1.length-1;i>=0;i--){
			String temp=fullAdder(bits_1[i],bits_2[i],c);
			//System.out.println("temp:"+temp);
			//c1=c;
			c=temp.charAt(0);
			result.append(temp.substring(1,2));
		}
		result.append(c);
		//result.append(String.valueOf((c-48)^(c1-48)));//Cn^Cn-1
		//System.out.println((c-48)^(c1-48));
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
		int temp;

		char[] bits=operand.toCharArray();
		StringBuilder result=new StringBuilder();

		for(int i=bits.length-1;i>=0;i--){
			temp=(bits[i]-48)^c;
			c=(bits[i]-48)&c;
			result.append(temp);
		}
		result.append(c);
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
		String num_1=integerRepresentation(integerTrueValue(operand1),length);
		String num_2=integerRepresentation(integerTrueValue(operand2),length);
		StringBuilder result=new StringBuilder();
		StringBuilder tempBuilder;
		//char c1='0';

		for(int i=length-1;i>=3;i=i-4){
			String temp=claAdder(num_1.substring(i-3,i+1),num_2.substring(i-3,i+1),c);
			tempBuilder=new StringBuilder(temp);
			//c1=c;
			c=temp.charAt(0);
			result.append(tempBuilder.reverse().substring(0,4));
			//System.out.println(result);
		}

//		result.append(String.valueOf((c-48)^(c1-48)));
		if(num_1.charAt(0)!=num_2.charAt(0)){
			//�������
			result.append("0");
		}else{
			if(num_1.charAt(0)==result.charAt(result.length()-1)){
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
		for(int i =0;i<length;i++){
			resultBuilder.append("0");//��ʼP
		}
		resultBuilder.append(operand2);
		String result=resultBuilder.toString();
		//System.out.println(result);

		//������
		String adder=leftShift(
				integerRepresentation(integerTrueValue(operand1),2*length),
				length
		);
		//System.out.println("x:"+adder);

		//����λ
		String additionalBit="0";
		//[-x]��
		String adderOpposite=oneAdder(negation(adder)).substring(1);//����oneAdder �� negation �� adder ����ȡ����1
		//System.out.println("x����"+adderOpposite);

		String lastBits=result.substring(result.length()-1)+additionalBit;
		//��ʼ����
		for (int i=0;i<length;i++){
			//System.out.println(lastBits);
			if (lastBits.equals("01")){
				//System.out.println("01");
				result=adder(result.toString(),adder,'0',2*length).substring(1);
			}else if (lastBits.equals("10")){
				//System.out.println("10");
				result=adder(result.toString(),adderOpposite,'0',2*length).substring(1);
			}

			//System.out.println(result);
			//���ø���λ
			additionalBit=result.substring(result.length()-1);
			//����
			result=ariRightShift(result.toString(),1);
			//System.out.println(result);
			//���������жϵ������λ
			lastBits = result.substring(result.length()-1)+additionalBit;

		}
		return result.toString();
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
		return null;
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
		return null;
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
		return null;
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
		return null;
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


