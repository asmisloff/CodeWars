import java.util.stream.IntStream;
import java.util.*;

class FactDecomp {
    
    private static SortedMap<Integer, Integer> primes = new TreeMap<>();
    private static StringBuilder sb = new StringBuilder();
    
    public static String decomp(int n) {
        
        primes.clear();
        sb.setLength(0);
        
        for (int i = 2; i <= n; ++i) {
            int k = i;
            for (Map.Entry<Integer, Integer> e : primes.entrySet()) {
                int prime = e.getKey();
                while (k % prime == 0) {
                    k /= prime;
                    e.setValue(e.getValue() + 1);
                }
                
            } 
            if (k == i) {
                primes.put(k, 1);
            }
        }
        
        for (Map.Entry<Integer, Integer> e : primes.entrySet()) {
            int prime = e.getKey();
            int pow = e.getValue();
            sb.append(prime);
            if (pow != 1) {
                sb.append("^" + pow);
            }
            sb.append(" * ");
        }
        
        sb.setLength(sb.length() - 3);
        
        return sb.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(decomp(5));
    }
}