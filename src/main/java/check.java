import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class check {
    //для полной проверки солвера, с выводом модели другого солвера + вывод неразрешенных клозов, если таковые есть
    public static void main(String[] args) throws Exception {
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        String path = args[0];
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        BufferedReader br = new BufferedReader(new FileReader(path));
        System.out.println(problem.isSatisfiable(new VecInt(solution)));
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
                for (int i = 0; i < problem.model().length; i++){
                    System.out.print(problem.model()[i] + " ");
                }

            } else {
                System.out.println("Unsatisfiable !");
            }
    }
}
