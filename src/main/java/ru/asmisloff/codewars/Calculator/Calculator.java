package ru.asmisloff.codewars.Calculator;

import java.util.*;
import java.util.stream.*;
import java.util.function.Function;


interface Executor {
    public void exec();
}

class Calculator {
    
    enum Operator {
        
        PLUS(
            "+",
            Calculator::evalPlus,
            () -> nums.push(nums.pop() + nums.pop())
        ),
        
        MINUS(
            "-",
            Calculator::evalMinus,
            () -> nums.push(-1 * nums.pop() + nums.pop())
        ),
        
        MUL(
            "*",
            Calculator::evalMul,
            () -> nums.push(nums.pop() * nums.pop())
        ),
        
        DIV(
            "/",
            Calculator::evalDiv,
            () -> {
                int a = nums.pop();
                int b = nums.pop();
                nums.push(b / a);
            }
        );
        
        private final Executor evaluator;
        private final Executor applier;
        private final String signature;
        private static Map<String, Operator> opMap = 
        	Stream.of(values()).collect(Collectors.toMap(v -> v.signature, Function.identity()));
        
        Operator(String signature, Executor evaluator, Executor applier) {
            this.signature = signature;
            this.evaluator = evaluator;
            this.applier = applier;
        }

        public void eval() {
            evaluator.exec();
        }
        
        public void apply() {
            applier.exec();
        }
        
        public static Operator fromString(String s) {
            return opMap.get(s);
        }
        
    }
    
	private static Stack<Integer> nums = new Stack<>();
	private static Stack<Operator> ops = new Stack<>();

	public static void eval(String exp) {
		Scanner sc = new Scanner(exp);
		sc.useDelimiter(" ()");
		while (sc.hasNext()) {
			if (sc.hasNextInt()) {
				nums.push(sc.nextInt());
			} else {
				Operator.fromString(sc.next()).eval();
            }
		}

		while (!ops.empty()) {
			ops.pop().apply();
		}

		System.out.println(nums.pop());
	}

    private static void evalPlus() {
        if (!ops.empty()) {
            ops.pop().apply();
        }
        ops.push(Operator.PLUS);
    }
    
    private static void evalMinus() {
        if (!ops.empty()) {
            ops.pop().apply();
        }
        ops.push(Operator.MINUS);
    }
    
    private static void evalMul() {
        if (!ops.empty()) {
            Operator op1 = ops.peek();
            if (op1 == Operator.MUL || op1 == Operator.DIV) {
                ops.pop().apply();
            }	
        }
        ops.push(Operator.MUL);
    }
    
    private static void evalDiv() {
        if (!ops.empty()) {
            Operator op1 = ops.peek();
            if (op1 == Operator.MUL || op1 == Operator.DIV) {
                ops.pop().apply();
            }	
        }
        ops.push(Operator.DIV);
    }

	public static void main(String[] args) {
		String exp = args[0];
		System.out.printf("expr: %s\n", exp);
		eval(exp);
	}
}
