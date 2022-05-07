import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestSAT {

    @Test
    public void test1() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test2() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest2.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test3() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest3.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test4() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest4.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test5() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest5.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test6() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest6.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test7() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600);
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest7.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test8() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest8.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test9() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest9.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test10() throws Exception {
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest10.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test11() throws Exception { //UNSAT
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest11.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertTrue(solution.length == 0);
    }

    @Test
    public void test12() throws Exception { //UNSAT
        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest12.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertTrue(solution.length == 0);
    }
}