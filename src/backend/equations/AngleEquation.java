package equations;

import java.util.ArrayList;
import java.util.List;
import ast.figure.components.*;
import utilities.list.Utilities;
import utilities.ast_helper.*;
import utilities.exception.ArgumentException;
import utilities.exception.ExceptionHandler;

public class AngleEquation extends Equation
{
    public AngleEquation()
    {
        super();
    }

    public AngleEquation(GroundedClause left, GroundedClause right)
    {
        double sumL = SumSide(left.collectTerms());
        double sumR = SumSide(right.collectTerms());

        if (!utilities.ast_helper.Utilities.CompareValues(sumL, sumR))
        {
            try
            {
                throw new ArgumentException("Angle equation is inaccurate; sums differ " + left + " " + right);
            }
            catch (ArgumentException e)
            {
                // TODO Auto-generated catch block
                ExceptionHandler.throwException(e);
            }
        }
    }

    private double SumSide(List<GroundedClause> side)
    {
        double sum = 0;
        for (GroundedClause clause: side)
        {
            if (clause instanceof NumericValue)
            {
                sum += (((NumericValue) clause).getDoubleValue());
            }

            else if (clause instanceof Angle)
            {
                sum += clause.getMulitplier() * ((Angle)clause).measure;
            }
            
            else if (clause instanceof MinorArc)
            {
                sum += clause.getMulitplier() * ((MinorArc)clause).GetMinorArcMeasureDegrees();
            }

            else if (clause instanceof MajorArc)
            {
                sum += clause.getMulitplier() *  ((MajorArc)clause).GetMajorArcMeasureDegrees();
            }
        }

        return sum;
    }

    //public AngleEquation(GroundedClause l, GroundedClause r, string just) : base(l, r, just)
    //{
    //    double sumL = SumSide(l.CollectTerms());
    //    double sumR = SumSide(r.CollectTerms());

    //    if (!Utilities.CompareValues(sumL, sumR))
    //    {
    //        throw new ArgumentException("Angle equation is inaccurate; sums differ: " + l + " " + r);
    //    }

    //    //if (sumL == 0 && sumR == 0)
    //    //{
    //    //    throw new ArgumentException("Should not have an equation that is 0 = 0: " + this.ToString());
    //    //}
    //}

    public int getHashCode()
    {
        return super.getHashCode();
    }

    //
    // Equals checks that for both sides of this equation is the same as one entire side of the other equation
    //


    public boolean equals(Object obj)
    {
        if (obj == null || (AngleEquation)obj == null)
        {
            return false;
        }

        AngleEquation thatEquation = (AngleEquation) obj;
        //
        // Collect all basic terms on the left and right hand sides of both equations.
        //

        List<GroundedClause> thisLHS = lhs.collectTerms();
        List<GroundedClause> thisRHS = rhs.collectTerms();

        List<GroundedClause> thatLHS = thatEquation.lhs.collectTerms();
        List<GroundedClause> thatRHS = thatEquation.rhs.collectTerms();

        //Check side length counts as a first step
        if (!((thisLHS.size() == thatLHS.size() && thisRHS.size() == thatRHS.size())) ||
                (thisLHS.size() == thatRHS.size() && thisLHS.size() == thatLHS.size()))
        {
            return false;
        }

        // Seek one side equal to one side and then the other equals the other.
        // Cannot do this easily with a union / set intersection set theoretic approach since an equation may have multiple instances of a value
        // In theory, since we always deal with simplified equations, there should not be multiple instances of a particular value.
        // So, this should work.

        // Note operations like multiplication and subtraction have been taken into account.
        List<GroundedClause> unionLHS = new ArrayList<GroundedClause>(thisLHS);
        Utilities.AddUniqueList(unionLHS, thatLHS);

        List<GroundedClause> unionRHS = new ArrayList<GroundedClause>(thisRHS);
        Utilities.AddUniqueList(unionRHS, thatRHS);

        // Exact same sides means the union is the same as each list itself
        if (unionLHS.size() == thisLHS.size() && unionRHS.size() == thisRHS.size())
        {
            return true;
        }

        // Check the other combination of sides
        unionLHS = new ArrayList<GroundedClause>(thisLHS);
        Utilities.AddUniqueList(unionLHS, thatRHS);

        if (unionLHS.size() != thisLHS.size())
        {
            return false;
        }

        unionRHS = new ArrayList<GroundedClause>(thisRHS);
        Utilities.AddUniqueList(unionRHS, thatLHS);

        //Exact same sides means the union is the same as each list itself
        return unionRHS.size() == thisRHS.size();
    }
}