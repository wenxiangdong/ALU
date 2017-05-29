import javax.crypto.CipherInputStream;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import org.w3c.dom.DOMConfiguration;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author 李培林 161250060
 *
 */

public class ALU {
	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {
		// TODO YOUR CODE HERE.
		//StringBuilder result=new StringBuilder();
		
		StringBuilder result=new StringBuilder();
		Long num=Long.parseLong(number);//将输入转换为64位整型
		
		for (int i = 0; i < length; i++) {
			result.append(num&1);
			num=num>>1;
		}
		
		return result.reverse().toString();
		
	}
	
	
	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		
		final int base=(int) Math.pow(2, eLength-1)-1;//偏置常量
		System.out.println("base:"+base);

		StringBuilder s=new StringBuilder();

		StringBuilder inf=new StringBuilder();
		for (int i=0;i<eLength;i++){
			inf.append("1");//全1阶码
		}
		for (int i=0;i<sLength;i++){
			inf.append("0");//全零尾数
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
				int temp=0;//用于记录每次乘2是否大于1
				for(int i=0;i<sLength;i++){
					temp=(doublePattern*2>=1.0)?1:0;
					doublePattern=doublePattern*2-temp;
					s.append(temp);
				}
				System.out.println("new:"+s.toString());

				//检测数值的第一个1
				int newIndexOfDot=0;
				for(;newIndexOfDot<s.length();newIndexOfDot++){
					if(s.charAt(newIndexOfDot)=='1'){
						break;
					}
				}

				//左右规并计算阶码
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
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
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
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		// TODO YOUR CODE HERE.		
		//负数 
		if(operand.substring(0, 1).equals("1")){
			//取反
			char[] temps=negation(operand).toCharArray();
	
			//加1
			for(int i=temps.length-1;i>=0;i--){
				if(temps[i]=='1'){
					temps[i]='0';
				}else {
					temps[i]='1';
					break;
				}				
			}			
			String opposite=String.copyValueOf(temps);
			//最小负数
			if(opposite.equals(operand)) return String.valueOf((int)(0-Math.pow(2,operand.length()-1)));

			//调用正数的方法
			return "-"+integerTrueValue(opposite);
			
			
		//正数 	
		}else{	
			int result=0;
			for(int i=0;i<operand.length();i++){
				result=result*2+Integer.parseInt(operand.substring(i,i+1));
			}

			return String.valueOf(result);
		}
		
		
	}
	
	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		//确定符号
		 String flag=(operand.substring(0,1).equals("0"))?"":"-";
		
		 //截取阶码
		 String e=operand.substring(1,1+eLength);
		System.out.println("e:"+e);
		//调用integerTrueValue来得到阶码的真值
		 int eNum=Integer.parseInt(integerTrueValue("0"+e));
		 
		 
		 //截取尾数
		 String s=operand.substring(1+eLength);
		 char[] sChars=s.toCharArray();
		 double sNum=0;
		 for(int i=sChars.length-1;i>=0;i--){
//			 System.out.println(i+":"+sChars[i]);
			 sNum=sNum/2.0+sChars[i]-48;
		 }
		 sNum=sNum/2.0;//得出尾数
		System.out.println("dot:"+sNum);
		 //System.out.println(result);
		 
		 double result;
		 int base=(int) (Math.pow(2, eLength-1)-1);//偏置常量
		System.out.println("base:"+base);
		 if(eNum==2*base+1){
			 if(sNum==0){
				 return (operand.charAt(0)=='0'?"+":"-")+"Inf";
			 }else{
				 return "NaN";
			 }
		 }else{
			 if(eNum==0){//非规格化
				 if(sNum==0){
				 	return "0";
				 }
				 result=sNum*Math.pow(2, -base+1);
			 }else{//规格化		
				 result = (1.0+sNum)*Math.pow(2, eNum-base);
			 }


			 //正负判断

				 return flag+String.valueOf(result);

		 }
		 
		
	}
	
	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
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
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
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
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
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
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
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
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
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
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {
		// TODO YOUR CODE HERE.
		char[] bits_1=operand1.toCharArray();
		char[] bits_2=operand2.toCharArray();
		StringBuilder result=new StringBuilder();

		//计算辅助公式
		int[] P=new int[4];
		int[] G=new int[4];

		for(int i=0;i<4;i++){
			P[i]=(bits_1[3-i]-48)|(bits_2[3-i]-48);
			G[i]=(bits_1[3-i]-48)&(bits_2[3-i]-48);
		}

		//进位获得，模拟并行
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
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		// TODO YOUR CODE HERE.
		int c=1;
		int c2=0;//次高进位
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
		result.append(c^c2);//溢出标志获得
		return result.reverse().toString();
	}
	
	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		// TODO YOUR CODE HERE.
		//System.out.println(integerTrueValue(operand1)+"+"+integerTrueValue(operand2));
		String num1=integerRepresentation(integerTrueValue(operand1),length);
		String num2=integerRepresentation(integerTrueValue(operand2),length);
		StringBuilder result=new StringBuilder();
		StringBuilder tempBuilder;
		//char c1='0';

		for(int i=length-1;i>=3;i=i-4){//每4位4位地计算
			String temp=claAdder(num1.substring(i-3,i+1),num2.substring(i-3,i+1),c);
			tempBuilder=new StringBuilder(temp);
			//c1=c;
			c=temp.charAt(0);
			result.append(tempBuilder.reverse().substring(0,4));
			//System.out.println(result);
		}

//		result.append(String.valueOf((c-48)^(c1-48)));
		if(num1.charAt(0)!=num2.charAt(0)){
			//不会溢出
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
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.

		return adder(operand1,operand2,'0',length);
	}
	
	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return adder(operand1,negation(operand2),'1',length);
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		//初始化
		StringBuilder resultBuilder=new StringBuilder();
		for(int i =0;i<length-operand2.length();i++){
			resultBuilder.append("0");//初始P
		}
		resultBuilder.append(operand2);
		String result=resultBuilder.toString();
		System.out.println(result);

		//被加数
		int oLen1=operand1.length();
		String adder=leftShift(
				integerRepresentation(integerTrueValue(operand1),length),
				length-oLen1
		);
		//System.out.println("x:"+adder);
		System.out.println("adder:"+adder);

		//附加位
		String additionalBit="0";
		//[-x]补
		String adderOpposite=oneAdder(negation(adder)).substring(1);//调用oneAdder 和 negation 对 adder 进行取反加1
		System.out.println("x补："+adderOpposite);

		String lastBits=result.substring(result.length()-1)+additionalBit;
		//开始计算
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
			//重置附加位
			additionalBit=result.substring(result.length()-1);
			//右移
			result=ariRightShift(result.toString(),1);
			System.out.println(result);
			//重置用于判断的最后两位
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
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.

		//被除数初始化
		//符号 右移的拓展
		StringBuilder operand1Builder=new StringBuilder();
		for (int i =0; i<length;i++){
			operand1Builder.append(operand1.charAt(0));
		}
		operand1Builder.append(operand1);
		operand1=operand1Builder.toString();
		System.out.println("原始R:"+operand1);

		//除数的初始处理
		//除数的补
		String operand2Opposite = leftShift(integerRepresentation(
				integerTrueValue(oneAdder(negation(operand2)).substring(1)),2*length),
		length);
		//除数
		String operand2OldValue=operand2;
		operand2=leftShift(integerRepresentation(
				integerTrueValue(operand2),2*length),
				length);
		System.out.println("除数："+operand2);


		//第一步，求X+Y
		char OF='0';
		char Q;
		String temp;
		if(operand1.charAt(0)==operand2.charAt(0)){
			temp=integerAddition(operand1, operand2Opposite,2*length).substring(1);
		}else{
			temp = integerAddition(operand1,operand2,2*length).substring(1);
		}
		//Qn的计算
		if(temp.charAt(0)==operand2.charAt(0)){//同1
			Q='1';
		}else{
			Q='0';
		}
		//System.out.println("Q:"+Q);
		//判断溢出与否
		//X与Y同号且Qn为1
		//X与Y异号且Qn为0
		OF=(operand1.charAt(0)==operand2.charAt(0)&&Q=='1')
				||(operand1.charAt(0)!=operand2.charAt(0)&&Q=='0')?'1':'0';
		//System.out.println("OF:"+OF);

		for(int i = 0;i<length;i++){
			//左移一位
			temp=leftShift(temp,1);
			if(Q=='1'){
				temp=oneAdder(temp).substring(1);
				//2R-Y
				temp=integerAddition(temp,operand2Opposite,2*length).substring(1);
			}else{
				//2R+Y
				temp=integerAddition(temp,operand2,2*length).substring(1);
			}

			//判断上商
			if(temp.charAt(0)==operand2.charAt(0)){
				Q='1';
			}else{
				Q='0';
			}
		}

		//最后一次左移
		String reminder=temp.substring(0,length);
		String result=temp.substring(length);
		result=leftShift(result,1);
		//最后一次上商
		if(Q=='1'){
			result=oneAdder(result).substring(1);
		}

		System.out.println("修正前："+result+","+reminder);
		//余数修正
		//余数与被除数不同号
		if(reminder.charAt(0)!=operand1.charAt(0)){
			System.out.println("余数与被除数不同号");
			//余数则减除数
			if(operand2.charAt(0)!=operand1.charAt(0)){
//				result=oneAdder(result).substring(1);
				reminder=integerSubtraction(reminder,operand2OldValue,length).substring(1);
			}else{
				//余数加除数
				reminder=integerAddition(reminder,operand2OldValue,length).substring(1);
			}
		}
		//商的修正
		//若x与y不同号，加1
		if(operand2.charAt(0)!=operand1.charAt(0)){
			result=oneAdder(result).substring(1);
		}

		//返回
		System.out.println( OF+result+reminder);
		return OF+result+reminder;
	}
	
	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		//扩展
		String extendOperand1=integerRepresentation(integerTrueValue("0"+operand1.substring(1)),length);
		String extendOperand2=integerRepresentation(integerTrueValue("0"+operand2.substring(1)),length);
		String operandOpposite="0"+oneAdder(negation(operand2.substring(1))).substring(1);

		String result;
		if(operand1.charAt(0)==operand2.charAt(0)){
			//同号求和
			result = adder(extendOperand1, extendOperand2,'0',length);
			System.out.println("result:"+result);
			return result.charAt(0)+operand1.substring(0,1)+result.substring(1);
		}else{
			//异号求差
			result = adder("0"+operand1.substring(1),operandOpposite,'0',operand1.length()).substring(1);
			System.out.println("result:"+result);
			//最高数值位没有进位，说明结果为负，需要求补
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
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
//		String fResult;
//		String eResult;
//		String sResult;
//
//
//		//对阶
//		String e1=operand1.substring(1,1+eLength);
//		String e2=operand2.substring(1,1+eLength);
//		//尾数
//		StringBuilder s1Builder=new StringBuilder(operand1.substring(1+eLength,1+eLength+sLength));
//		for(int i=0;i<gLength;i++){
//			s1Builder.append("0");//保护位
//		}
//		StringBuilder s2Builder=new StringBuilder(operand2.substring(1+eLength,1+eLength+sLength));
//		for(int i=0;i<gLength;i++){
//			s2Builder.append("0");//保护位
//		}
//		String s1,s2;
//
//		//比较阶码
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
//		//尾数相加
//		double sResultDouble=Double.parseDouble(floatTrueValue(operand1.substring(0,1)+s1,0,sLength+gLength))+
//				Double.parseDouble(floatTrueValue(operand2.substring(0,1)+s2,0,sLength+gLength));
//		System.out.println(sResultDouble);
//
//		sResult=floatRepresentation(String.valueOf(sResultDouble),2,sLength+gLength).substring(1);
//		int n=0;//记录左移了多少次
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
//		//符号位
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
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}



}


