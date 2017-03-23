package backendTest.deductiveRulesTest.diagrams.segments;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import backend.ast.figure.components.Point;
import backend.ast.figure.components.Segment;
import backend.deductiveRules.RuleFactory;
import backendTest.deductiveRulesTest.TestDeductiveRule;
import channels.fromUI.Diagram;

public class ParallelSegmentTransitivityTest
{

    /**
     * There is a problem with the query in ParallelSegmentTransitivity -Nick 3/14
     * 
     * 
     * A---B  C---D  E---F
     * @throws IOException
     */
    @Test
    public void test() throws IOException
    {
        // create diagram
        Diagram diagram = new Diagram();
        
        // create points and segments
        Point a = new Point("A", 0, 0);
        Point b = new Point("B", 2, 0);
        
        Point c = new Point("C", 3, 0);
        Point d = new Point("D", 4, 0);
        
        Point e = new Point("E", 6, 0);
        Point f = new Point("F", 7, 0);
        
        Segment ab = new Segment(a, b);
        Segment cd = new Segment(c, d);
        Segment ef = new Segment(e, f);
        
        // add points and segments to diagram object
        diagram.addSegment(ab);
        diagram.addSegment(cd);
        diagram.addSegment(ef);
        
        
        // create flags array
        ArrayList<Integer> flags = new ArrayList<>();
        flags.add(RuleFactory.JustificationSwitch.DeductionJustType.PARALLEL_SEGMENTS_TRANSITIVITY.ordinal());
        
        assertTrue(TestDeductiveRule.test(diagram, 1, flags));
    }

}