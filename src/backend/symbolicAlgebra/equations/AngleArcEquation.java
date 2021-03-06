package backend.symbolicAlgebra.equations;

import backend.ast.figure.components.angles.Angle;
import backend.ast.figure.components.arcs.MajorArc;
import backend.ast.figure.components.arcs.MinorArc;
import backend.ast.figure.components.arcs.Semicircle;
import backend.symbolicAlgebra.NumericValue;
import backend.utilities.ast_helper.Utilities;
import backend.utilities.exception.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import backend.ast.GroundedClause;

public class AngleArcEquation extends Equation
{
    protected AngleArcEquation() { super(); }
    
    public AngleArcEquation(Equation eq)
    {
        this(eq.lhs, eq.rhs);
    }
    
    public AngleArcEquation(GroundedClause left, GroundedClause right)
    {
        double sumL = SumSide(left.collectTerms().getValue());
        double sumR = SumSide(right.collectTerms().getValue());

        if (!Utilities.CompareValues(sumL, sumR))
        {
            ExceptionHandler.throwException(new IllegalArgumentException("Angle-Arc equation is inaccurate; sums differ: " + left + " " + right));
        }
    }
    
    public double SumSide(List<GroundedClause> side)
    {
        double sum = 0;
        for (GroundedClause clause : side)
        {
            if (clause instanceof NumericValue)
            {
                sum += ((NumericValue)clause).getDoubleValue();
            }
            
            else if (clause instanceof Angle)
            {
                sum += ((Angle)clause).getMeasure();
            }

            else if (clause instanceof MinorArc)
            {
                sum += ((MinorArc)clause).GetMinorArcMeasureDegrees();
            }

            else if (clause instanceof MajorArc)
            {
                sum += ((MajorArc)clause).GetMajorArcMeasureDegrees();
            }

            else if (clause instanceof Semicircle)
            {
                sum += ((MajorArc)clause).GetMinorArcMeasureDegrees();
            }
            
        }
        return sum;
    }
     
    public int getHashCode() { return super.getHashCode(); }

    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Equation)) { return false;  }
        AngleArcEquation thatEquation = new AngleArcEquation((Equation) obj);

        //
        // Collect all basic terms on the left and right hand sides of both equations.
        //
        List<GroundedClause> thisLHS = lhs.collectTerms().getValue();
        List<GroundedClause> thisRHS = rhs.collectTerms().getValue();

        List<GroundedClause> thatLHS = thatEquation.lhs.collectTerms().getValue();
        List<GroundedClause> thatRHS = thatEquation.rhs.collectTerms().getValue();

        // Check side length counts as a first step.
        if (!((thisLHS.size() == thatLHS.size() && thisRHS.size() == thatRHS.size()) ||
                (thisLHS.size() == thatRHS.size() && thisLHS.size() == thatLHS.size()))) return false;

        // Seek one side equal to one side and then the other equals the other.
        // Cannot do this easily with a union / set intersection set theoretic approach since an equation may have multiple instances of a value
        // In theory, since we always deal with simplified equations, there should not be multiple instances of a particular value.
        // So, this should work.

        // Note operations like multiplication and subtraction have been taken into account.
        List<GroundedClause> unionLHS = new ArrayList<GroundedClause>(thisLHS);
        backend.utilities.list.Utilities.AddUniqueList(unionLHS, thatLHS);

        List<GroundedClause> unionRHS = new ArrayList<GroundedClause>(thisRHS);
        backend.utilities.list.Utilities.AddUniqueList(unionRHS, thatRHS);

        // Exact same sides means the union is the same as each list itself
        if (unionLHS.size() == thisLHS.size() && unionRHS.size() == thisRHS.size()) return true;

        // Check the other combination of sides
        unionLHS = new ArrayList<GroundedClause>(thisLHS);
        backend.utilities.list.Utilities.AddUniqueList(unionLHS, thatRHS);
        if (unionLHS.size() != thisLHS.size()) return false;

        unionRHS = new ArrayList<GroundedClause>(thisRHS);
        backend.utilities.list.Utilities.AddUniqueList(unionRHS, thatLHS);

        // Exact same sides means the union is the same as each list itself
        return unionRHS.size() == thisRHS.size();
    }
    
}