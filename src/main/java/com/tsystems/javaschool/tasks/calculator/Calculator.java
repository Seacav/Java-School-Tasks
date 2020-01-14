package com.tsystems.javaschool.tasks.calculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.DoubleBinaryOperator;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
	
	//To convert from infix to postfix
	final HashMap<String, Integer> operands;
	
	//To process postfix notation
	final HashMap<String, DoubleBinaryOperator> operations;
	
	public Calculator() {
		operands = new HashMap<>();
		operands.put("+", 2);		
		operands.put("-", 2);
		operands.put("*", 3);
		operands.put("/", 3);
		operands.put("^", 4);
		
		operations = new HashMap<>();
		operations.put("+", (x, y) -> y + x);
		operations.put("*", (x, y) -> y * x);
		operations.put("-", (x, y) -> y - x);
		operations.put("/", (x, y) -> y/x);
		operations.put("^", (x, y) -> Math.pow(y, x));
	}
	
	public String evaluate(String statement) {
		
		if (statement == null)
			return null;
		
		String postf = formatExpression(statement);
		
		if (postf != null) {
			postf = infToPostf(postf);
		} else
			return null;
		
		Double result = evaluatePostfix(postf);
		
		if (result == null)
			return null;
			
		if (result % 1 == 0) 
			return Integer.toString(result.intValue());
		
		return Double.toString(Math.round(result*10000.0) / 10000.0);
	}
	
	//Check for validity and format given expression
	private String formatExpression(String exp) {
		
		String result = exp.replaceAll(" ","");
		
		if (result.isEmpty())
			return null;
		
		String[] matches = new String[] {",", "..", "++", "--", "**", "//"};
		
		for (String m : matches) {
			if (result.contains(m))
				return null;
		}
		
		//-------Replace *-[digit] or /-[digit] with *(0-[digit])
		// 4+3/-55.55 -> 4+3/(0-55.55)
		// or
		// 4+3*-55.55 -> 4+3*(0-55.55)
		result = result.replaceAll("([*///])([-][0-9]+[.]*[0-9]*)", "$1(0$2)");
		
		//Adds * before or after the bracket
		//1+2(9+4) -> 1+2*(9+4)
		result = result.replaceAll("(?<=\\d)(?=\\()|(?<=\\))(?=\\d)", "*");
		
		//-------Lookbehinds and lookaheads---------------
		// (?<=[^.\\d]) - look behind to see if there is any character except . or "0" to "9"
		//3+5.555*-9.398 -> 3+ 5.555* - 9.398
		//----------
		// (?<=\\d)(?=[^0-9.]) - look behind to see if there is any character from "0" to "9"
		//						 and look ahead if there is any character except . or "0" to "9"
		//3+5.555*-9.398 -> 3 +5.555 *-9.398
		//----------
		//(?<=[^.\\d])|(?<=\\d)(?=[^0-9.]) leads to:
		//3+5.555*-9.398 -> 3 + 5.555 * - 9.398
		result = result.replaceAll("(?<=[^.\\d])|(?<=\\d)(?=[^0-9.])", " ");
		return result;
	}
	
	//Convert from infix to postfix notation (Reverse Polish notation)
	//Shunting yard algorithm
	private String infToPostf(String infix) {
		
		String postfix = "";
		
		//Operator Stack
		LinkedList<String> stack = new LinkedList();
				
		for (String token : infix.split(" ")){
			if (operands.containsKey(token)){
				if (stack.isEmpty() || stack.getFirst()=="("){
					stack.push(token);
				} else if ( stack.getFirst().equals("(") ||
						( (operands.get(token)>operands.get(stack.getFirst())) || 
						( operands.get(token)==operands.get(stack.getFirst()) & token.equals("^") ) )){
					stack.push(token);
				} else {
					while ( !stack.isEmpty() && !stack.getFirst().equals("(") &&
							( (operands.get(token)<operands.get(stack.getFirst())) || 
							(operands.get(token)==operands.get(stack.getFirst()) & !token.equals("^") ))){
						postfix += stack.getFirst() + " ";
						stack.pop();
					}
					stack.push(token);
				}
			} else if(token.equals("(")){
				stack.push(token);
			} else if(token.equals(")")){
				while (!stack.getFirst().equals("(")){
					postfix += stack.getFirst() + " ";
					stack.pop();
				}
				stack.pop();
			} else {
				postfix += token + " ";
			}
		}
				
		for (String token : stack){
			postfix += token + " ";
		}
				
		return postfix;
	}
	
	
	//Evaluate postfix notation
	private Double evaluatePostfix(String postfix) {
		LinkedList<Double> stack = new LinkedList();
		
		try {
			for (String token : postfix.split(" ")){
				if (operations.containsKey(token)){
					stack.push(operations.get(token).applyAsDouble(stack.pop(), stack.pop()));
				} else {
					stack.push(Double.parseDouble(token));
				}
			}
		} catch (Exception e) {
			return null;
		}
		
		Double result = stack.pop();
		
		if (Double.isInfinite(result))
			return null;
		
		return result;
	}

}
