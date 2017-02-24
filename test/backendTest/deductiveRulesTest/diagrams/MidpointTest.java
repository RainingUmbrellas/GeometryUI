package backendTest.deductiveRulesTest.diagrams;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import backend.ast.figure.components.Point;
import backend.ast.figure.components.Segment;
import backend.deductiveRules.RuleFactory;
import backend.hypergraph.DeductionFlags;
import backend.precomputer.CoordinatePrecomputer;
import channels.fromUI.Diagram;

public class MidpointTest
{
    

    /**
     *  Midpoint Test
     *   A          M          B
     *   @----------@----------@
     *   
     */
    @Test
    public void test()
    {
        // create diagram
        Diagram midpointDiagram = new Diagram();
        
        // create points and segments
        Point a = new Point("A", 0, 0);
        Point b = new Point("B", 2, 0);
        Point m = new Point("M", 1, 0);
        Segment ab = new Segment(a, b);
        
        // add points and segments to diagram object
        midpointDiagram.addSegment(ab);
        midpointDiagram.addPoint(m);
        
        // create flags array
        ArrayList<Integer> flags = new ArrayList<>();
        flags.add(RuleFactory.JustificationSwitch.DeductionJustType.MIDPOINT_THEOREM.ordinal());
        
        assert(testFramework(midpointDiagram, 1, flags));
    }
    
    
    public static Boolean testFramework(Diagram d, int expected, ArrayList<Integer> flags)
    {        
        // set flags
        // activate specific flags
        DeductionFlags.setFlags(flags);
        
        // create the precomputer object
        CoordinatePrecomputer precomp = new CoordinatePrecomputer(null, null, d.getSegments(), d.getPoints());
        
        // get qhg
        
        // check number of edges
        
        // return expected == qhg.numberOfEdges
        return false;
    }

}