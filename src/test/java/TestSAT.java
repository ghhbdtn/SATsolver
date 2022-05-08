import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
    public void test4() throws Exception { //UNSAT
        String path = "src\\input\\inputtest4.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertEquals(0, solution.length);
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
        String path = "src\\input\\inputtest11.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertEquals(0, solution.length);
    }

    @Test
    public void test12() throws Exception { //UNSAT
        String path = "src\\input\\inputtest12.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertEquals(0, solution.length);
    }

    @Test
    public void test13() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest13.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test14() throws Exception { //UNSAT

        String path = "src\\input\\inputtest14.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertEquals(0, solution.length);
    }

    @Test
    public void test15() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest15.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test16() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest16.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test17() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest17.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test18() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest18.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test19() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest19.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test20() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest20.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test21() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest21.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test22() throws Exception {//UNSAT

        String path = "src\\input\\inputtest22.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertEquals(0, solution.length);
    }

    @Test
    public void test23() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest23.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }

    @Test
    public void test24() throws Exception { //UNSAT

        String path = "src\\input\\inputtest24.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        assertEquals(0, solution.length);
    }

    @Test
    public void test25() throws Exception {

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);
        String path = "src\\input\\inputtest25.txt";
        SATsolver saTsolver = new SATsolver(path);
        int[] solution = saTsolver.solve();
        IProblem problem = reader.parseInstance(path);
        assertTrue(problem.isSatisfiable(new VecInt(solution)));
    }
}