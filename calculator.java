import java.util.*;
import java.io.*;
import java.lang.*;

public class calculator{
	private static Stack<String>stack;
	private static String []postfix;
	private static double sum = 0;
	private static String[]exp;
	public static boolean compile(String []input){
		int count = -1;
		exp = new String[500];
		boolean flag = true;
		int isNum = 0;
		for (int i = 0;i<input.length;i++){
			if(input[i].length()>1){
				for(int j = 0;j<input[i].length();j++){
					if(isoperator(Character.toString(input[i].charAt(j)))){
						
						int f = j;
							f++;
						if(input[i].charAt(f)==input[i].charAt(j)&&input[i].charAt(f)!='('&&input[i].charAt(j)!=')'){
							flag = false;
									return flag;
						}
						count++;
						if(input[i].charAt(j)=='-'){
							if(j==0){
								exp[count] = "0";
								count++;
								exp[count] = "-";
								continue;
							}
			                int t = count-1;
							int x = j-1;
							if(Character.isDigit(exp[t].charAt(0))||input[i].charAt(x)==')'){
								exp[count] = "-";
							}else{  
								exp[count] = "0";
								count++;
								exp[count] = "-";
							}								
						}
						if(input[i].charAt(j)!='('&&input[i].charAt(j)!=')'){//handle invalid expression like 1++, --1,etc
							if(j!=input[i].length()-1){
								int k = j;
								k++;
								if(isoperator(Character.toString(input[i].charAt(k)))&&input[i].charAt(k)!='('){
									flag = false;
									return flag;
								}else{
									exp[count] = Character.toString(input[i].charAt(j));
									isNum = 0;
								}
							}else{
								exp[count] = Character.toString(input[i].charAt(j));
								isNum = 0;
							}
						}else{
							exp[count] = Character.toString(input[i].charAt(j));
							isNum = 0;
						}
					}else{
					    count++;
						if(isNum == 1){
							count--;
							exp[count]+=Character.toString(input[i].charAt(j));							
						}else{
							exp[count] = Character.toString(input[i].charAt(j));
							isNum = 1;
						}
					}
				}
			}else{
				if(isoperator(input[i])){
					count++;
					int g = i;
					g++;
					if(input[i].equals(input[g])&&!input[i].equals("(")&&!input[i].equals(")")&&g!=input.length-1){
						flag = false;
							return flag;
					}
					if(input[i].equals("-")){
						int f = i;
						f++;
						if(i==0&&input[f].equals("(")){
							exp[count] = "0";
							count++;
							exp[count] = "-";
							continue;
						}else{
							int t = i;
							t--;
							if(Character.isDigit(exp[t].charAt(0))||exp[t].equals(")")){
								exp[count] = "-";
							}else{
								exp[count] = "0";
								count++;
								exp[count] = "-";
							}
						}
					}
					if(!input[i].equals(")")&&!input[i].equals("(")){
						if(i==input.length-1){
							flag = false;
							return flag;							
						}else{
						int f = i;
						f++;
							if(isoperator(input[f])&&!input[f].equals("(")&&!input[f].equals("-")){
								flag = false;
								return flag;
							}else{
							exp[count] = input[i];
							}
						}
					}else{
						exp[count] = input[i];						
					}
					isNum = 0;
				}else{
					count++;
					exp[count] = input[i];
				}
			}
		}
		int state = postfix(exp,count);	
		double temp = 0.0;
		for (int i = 0;i<=state;i++){
			if(Character.isDigit(postfix[i].charAt(0))){
				stack.push(postfix[i]);
			}else{
				double num1 = Double.parseDouble(stack.pop());
				double num2 = Double.parseDouble(stack.pop());
				if(postfix[i].equals("+")){
					temp = num1+num2;
				}else if(postfix[i].equals("-")){
					temp = num2-num1;
				}else if(postfix[i].equals("*")){
					temp = num1*num2;
				}else{
					temp = num2/num1;
				}
				stack.push(Double.toString(temp));
			}
		}
		sum = Double.parseDouble(stack.pop());
		return flag;
	}
	
	public static int postfix(String[]input,int length){
		stack = new Stack<String>();
		postfix = new String[length+1];
		int tracker = 0;
		for(int i = 0;i<=length;i++){
			if(Character.isDigit(input[i].charAt(0))){
				postfix[tracker] = input[i];
				tracker++;
			}else if(input[i].equals("(")){
				stack.push(input[i]);
			}else if(input[i].equals(")")){
				while(!stack.peek().equals("(")){
					postfix[tracker] = stack.pop();
					tracker++;
				}
				stack.pop();
			}
			else{
				while(!stack.isEmpty()&&priority(input[i])>=priority(stack.peek())){
					postfix[tracker] = stack.pop();
					tracker++;
				}
					stack.push(input[i]);
			}
		}
			while(!stack.isEmpty()){
				postfix[tracker] = stack.pop();
				tracker++;
			}
			tracker--;
		return tracker;
	}
	
	public static boolean isoperator(String op){
		switch(op){
			case "+":
			case "-":
			case "*":
			case "/":
			case "(":
			case ")":
				return true;
			default:
				return false;
		}
	}
	public static int priority(String op){
		switch(op){
			case "*":
			case "/":
				return 0;
			case "+":
			case "-":
				return 1;
			default:
				return 3;
			
		}
	}
	public static void main(String args[]){
		while(true){
			sum = 0;
			System.out.println("Enter your expression");
			Scanner scanner = new Scanner(System.in);
			String[]input = scanner.nextLine().split("\\s+");
			if(input.length==1&&input[0].equals("exit")){
				System.out.print("Game over66666");
				System.exit(0);
			}else{
				if(input[0].length()==1){
					System.out.println(input[0].charAt(input[0].length()-1));
					if(input[0].charAt(input[0].length()-1)!=')'&&!Character.isDigit(input[0].charAt(input[0].length()-1))){
					System.out.println("Invalid Expression");
					continue;
					}
				}else{
					if(!input[input.length-1].equals(")")&&!Character.isDigit(input[input.length-1].charAt(0))){
						System.out.println("Invalid Expression");
						continue;
					}
				}
				if(compile(input))System.out.println("The result will be: "+sum);
				else System.out.println("Invalid element in expression, try again");
			}		
		}
	}
}