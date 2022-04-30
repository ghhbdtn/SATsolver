import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.tools.ModelIterator;

import java.io.*;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;
/*public class Test {
}*/
