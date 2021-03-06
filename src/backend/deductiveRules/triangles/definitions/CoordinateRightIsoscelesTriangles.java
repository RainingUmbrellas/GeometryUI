package backend.deductiveRules.triangles.definitions;

import java.util.HashSet;
import java.util.Set;

import backend.ast.GroundedClause;
import backend.ast.Descriptors.Strengthened;
import backend.deductiveRules.Deduction;
import backend.deductiveRules.RuleFactory;
import backend.deductiveRules.generalRules.Definition;
import backend.hypergraph.Annotation;
import backend.hypergraph.QueryableHypergraph;
import backend.utilities.exception.ExceptionHandler;

public class CoordinateRightIsoscelesTriangles extends Definition
{
    private static final String NAME = "Composing Right / Isosceles Triangles";
    public String getName() { return NAME; }
    public String getDescription() { return getName(); }

    private final static Annotation ANNOTATION = new Annotation(NAME, RuleFactory.JustificationSwitch.RIGHT_TRIANGLE_DEFINITION);
    @Override public Annotation getAnnotation() { return ANNOTATION; }

    public CoordinateRightIsoscelesTriangles(QueryableHypergraph<GroundedClause, Annotation> qhg)
    {
        super(qhg);
        ANNOTATION.active = RuleFactory.JustificationSwitch.RIGHT_TRIANGLE_DEFINITION;
    }

    @Override
    public Set<Deduction> deduce()
    {
        HashSet<Deduction> deductions = new HashSet<Deduction>();

        deductions.addAll(deduceCoordinateRightIsoscelesTriangles());

        return deductions;
    }
    
    // original C# for reference
//    public static void Instantiate(GroundedClause clause)
//    {
//        annotation.active = EngineUIBridge.JustificationSwitch.RIGHT_TRIANGLE_DEFINITION;
//
//        List<EdgeAggregator> newGrounded = new List<EdgeAggregator>();
//
//        //
//        // Instantiating FROM a right triangle
//        //
//        Strengthened streng = clause as Strengthened;
//        if (streng == null) return;
//
//        if (streng.strengthened is RightTriangle)
//        {
//            candidateRight.Add(streng);
//
//            foreach (Strengthened iso in candidateIsosceles)
//            {
//                InstantiateRightTriangle(streng, iso);
//            }
//        }
//        else if (streng.strengthened is IsoscelesTriangle)
//        {
//            candidateIsosceles.Add(streng);
//
//            foreach (Strengthened right in candidateRight)
//            {
//                InstantiateRightTriangle(right, streng);
//            }
//        }
//    }
    
    public Set<Deduction> deduceCoordinateRightIsoscelesTriangles()
    {
        HashSet<Deduction> deductions = new HashSet<Deduction>();

        Set<Strengthened> strengRight = _qhg.getStrengthenedRightTriangles();
        Set<Strengthened> strengIso = _qhg.getStrengthenedIsoscelesTriangles();

        for (Strengthened strRight : strengRight)
        {
            for (Strengthened strIso : strengIso)
            {
                deduceCoordinateRightIsoscelesTriangles(strRight, strIso);
            }
        }

        return deductions;
    }

    // shouldn't this return a deductions HashSet so the triangles actually get added to deductions?
    private static void deduceCoordinateRightIsoscelesTriangles(Strengthened right, Strengthened iso)
    {
        
        ExceptionHandler.throwException("Unimplemented: Dr. Alvin Needs to look at this");
        
//        Triangle rightTri = (Triangle) right.getStrengthened();
//        Triangle isoTri = (Triangle) iso.getStrengthened();
//
//        if (!rightTri.structurallyEquals(isoTri)) return;
//
//        rightTri.SetProvenToBeIsosceles();
//        rightTri.SetProvenToBeRight();
//        isoTri.SetProvenToBeRight();
//        isoTri.SetProvenToBeIsosceles();
    }
}
