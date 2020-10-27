import java.util.*;
import java.util.function.Consumer;
import java.util.concurrent.ThreadLocalRandom;

public class BefungeInterpreter {

        
    enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    class Index {

        private int row, col; // indexes of current row and column
        private Direction d;
        char[][] code;

        public Index() {
            row = 0; col = -1;
            d = Direction.RIGHT;
        }

        public void setup(String src) {
            String[] ar = src.split("\n");
            code = new char[ar.length][];
            for (int i = 0; i < ar.length; ++i) {
                code[i] = ar[i].toCharArray();
            }
        }
        
        public char next() {
            switch (d) {
            case LEFT:
                shiftLeft();
                break;
            case RIGHT:
                shiftRight();
                break;
            case UP:
                shiftUp();
                break;
            case DOWN:
                shiftDown();
                break;
            }
            
            return code[row][col];
        }
        
        public void setDirection(Direction d) {
            this.d = d;
        }

        public char shiftLeft() {
            int m = code[row].length;
            col = (m + col - 1) % m;
            return code[row][col];
        }
        
        public char shiftRight() {
            int m = code[row].length;
            col = (col + 1) % m;
            return code[row][col];
        }
        
        public char shiftUp() {
            if (row > 0) {
                row--;
            } else {
                row = code.length - 1;
            }

            if (code[row].length <= col) {
                shiftUp();
            }

            return code[row][col];
        }
        
        public char shiftDown() {
            if (row < code.length - 1) {
                row++;
            } else {
                row = 0;
            }

            if (code[row].length <= col) {
                shiftDown();
            }

            return code[row][col];
        }

        public char get() { return code[row][col]; }

        public char charAt(int r, int c) { return code[r][c]; }

        public int getRow() { return row; }

        public int getCol() { return col; }
        
        public void set(char value, int row, int col) { code[row][col] = value; }
    }

    private Stack<Integer> stack;
    private static HashMap<Character, Consumer<Character>> interpreters = new HashMap<>();
    private Index index;
    private StringBuilder result;

    public BefungeInterpreter() {
        stack = new Stack<>();
        index = new Index();
        result = new StringBuilder();

        for (char c = '0'; c <= '9'; ++c) {
            interpreters.put(c, this::interpretDigit);
        }
        
        interpreters.put('+', this::interpretPlus);
        interpreters.put('-', this::interpretMinus);
        interpreters.put('*', this::interpretMultiplication);
        interpreters.put('/', this::interpretDivision);
        interpreters.put('%', this::interpretPercent);
        interpreters.put('!', this::interpretExclamation);
        interpreters.put('>', this::interpretGreater);
        interpreters.put('<', this::interpretLesser);
        interpreters.put('^', this::interpretUpArrow);
        interpreters.put('v', this::interpretV);
        interpreters.put('?', this::interpretQuestion);
        interpreters.put('_', this::interpretUnderscore);
        interpreters.put('_', this::interpretUnderscore);
        interpreters.put('|', this::interpretSlash);
        interpreters.put('\"', this::interpretDoubleQuot);
        interpreters.put(':', this::interpretColon);
        interpreters.put('.', this::interpretDot);
        interpreters.put('\\', this::interpretBackSlash);
        interpreters.put(',', this::interpretComa);
        interpreters.put('`', this::interpretBackQuot);
        interpreters.put('#', (c) -> index.next());
        interpreters.put('$', (c) -> stack.pop());
        interpreters.put('p', this::interpretP);
        interpreters.put('g', this::interpretG);
    }

    public void printStack() {
        System.out.println(stack);
    }
        
    public String interpret(String code) {
        
        System.out.println("Code:");
        System.out.println(code);

        while (!stack.isEmpty()) {
            stack.pop();
        }
        result.setLength(0);
        
        index.setup(code);
        Consumer<Character> cons;
        char c;
        
        while(true) {
            cons = interpreters.get(c = index.next());
            
            if (c == '@') {
                break;
            }
            if (cons != null) {
                cons.accept(c);
            }
            
            System.out.printf("%c --> ", c);
            printStack();
        }
        
        return result.toString(); 
    }


    private void interpretDigit(char c) {
        stack.push(c - '0');
    }

    private void interpretPlus(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(a + b);
    }
    
    private void interpretMinus(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(b - a);
    }

    private void interpretMultiplication(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(b * a);
    }

    private void interpretDivision(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(a == 0 ? 0 : b / a);
    }

    private void interpretPercent(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(a == 0 ? 0 : b % a);
    }

    private void interpretExclamation(char c) {
        int a = stack.pop();
        stack.push(a == 0 ? 1 : 0);
    }

    private void interpretQuotation(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(b > a ? 1 : 0);
    }
    
    private void interpretGreater(char c) {
        index.setDirection(Direction.RIGHT);
    }
    
    private void interpretLesser(char c) {
        index.setDirection(Direction.LEFT);
    }

    private void interpretUpArrow(char c) {
        index.setDirection(Direction.UP);
    }

    private void interpretV(char c) {
        index.setDirection(Direction.DOWN);
    }

    private void interpretQuestion(char c) {
        int direction = ThreadLocalRandom.current().nextInt(0, 4);
        switch (direction) {
        case 0:
            index.setDirection(Direction.LEFT);
            break;
        case 1:
            index.setDirection(Direction.RIGHT);
            break;
        case 2:
            index.setDirection(Direction.UP);
            break;
        case 3:
            index.setDirection(Direction.DOWN);
            break;
        }
    }

    private void interpretUnderscore(char c) {
       int a = stack.pop();
        if (a == 0) {
            index.setDirection(Direction.RIGHT);
        } else {
            index.setDirection(Direction.LEFT);
        }
    }

    private void interpretSlash(char c) {
        int a = stack.pop();
        if (a == 0) {
            index.setDirection(Direction.DOWN);
        } else {
            index.setDirection(Direction.UP);
        }
    }

    private void interpretDoubleQuot(char c) {
        while ((c = index.next()) != '\"') {
            stack.push((int)c);
        }
    }
    
    private void interpretColon(char c) {
        if (stack.isEmpty()) {
            stack.push(0);
        } else {
            int a = stack.peek();
            stack.push(a);
        }
    }

    private void interpretDot(char c) {
        int a = stack.pop();
        result.append(a);
    }

    private void interpretBackSlash(char c) {
        int a = stack.pop();
        int b = (stack.isEmpty()) ? 0 : stack.pop();
        stack.push(a);
        stack.push(b);
    }

    private void interpretComa(char c) {
        int a = stack.pop();
        result.append((char)a);
    }
    
    private void interpretBackQuot(char c) {
        int a = stack.pop();
        int b = stack.pop();
        stack.push(b > a ? 1 : 0);
    }

    private void interpretP(char c) {
        int row = stack.pop();
        int col = stack.pop();
        int v = stack.pop();
        index.set((char)v, row, col);
    }
    
    private void interpretG(char c) {
        int row = stack.pop();
        int col = stack.pop();
        stack.push((int)index.charAt(row, col));
    }

    public static void main(String[] args) {
        BefungeInterpreter bi = new BefungeInterpreter();
        String src = "2>:3g\" \"-!v\\  g30          <\n |!`\"&\":+1_:.:03p>03g+:\"&\"`|\n @               ^  p3\\\" \":<\n2 2345678901234567890123456789012345678";
        System.out.println(bi.interpret(src));
        // bi.printStack();
        
    }
}

