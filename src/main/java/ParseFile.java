import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

public class ParseFile {
    Collection<SATsolver.Clause> clauses;
    Map<Integer, Integer> watch;

    public ParseFile(String inputName) throws Exception {
        clauses = new ArrayList<>();
        watch = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            String line = br.readLine();
            while (line != null) {
                if (line.endsWith(" 0")) {
                    List<Integer> literals = Stream.of(line.substring(0, line.length() - 2).split("\\s+"))
                            .map(Integer::parseInt).toList();
                    SATsolver.Clause clause = new SATsolver.Clause(literals, watch);
                    clauses.add(clause);
                }
                line = br.readLine();
            }
        }
    }

    public Collection<SATsolver.Clause> getClauses() {
        return clauses;
    }

    public Map<Integer, Integer> getWatch() {
        return watch;
    }
}
