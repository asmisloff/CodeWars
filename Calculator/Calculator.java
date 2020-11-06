import java.util.*;


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
        
        public static Operator get(String s) {
            for (Operator op : values()) {
                if (op.signature.equals(s)) {
                    return op;
                }
            }
            
            return null;
        
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
				Operator.get(sc.next()).eval();
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
