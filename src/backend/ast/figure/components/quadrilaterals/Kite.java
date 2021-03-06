package backend.ast.figure.components.quadrilaterals;

import backend.ast.Descriptors.Intersection;
import backend.ast.figure.components.Point;
import backend.ast.figure.components.Segment;
import backend.ast.figure.components.polygon.Polygon;
import backend.utilities.exception.DebugException;
import backend.utilities.exception.ExceptionHandler;
import backend.utilities.math.MathUtilities;

public class Kite extends Quadrilateral
{
    private Segment pairASegment1;
    private Segment pairASegment2;
    private Segment pairBSegment1;
    private Segment pairBSegment2;

    public Segment getPairASegment1() { return pairASegment1; }
    public Segment getPairASegment2() { return pairASegment2; }
    public Segment getPairBSegment1() { return pairBSegment1; }
    public Segment getPairBSegment2() { return pairBSegment2; }

    public Kite(Quadrilateral quad)
    { 
        this(quad.left, quad.right, quad.top, quad.bottom,
                quad.TopLeftDiagonalIsValid(), quad.BottomRightDiagonalIsValid(), quad.getDiagonalIntersection());
    }

    public Kite(Segment left, Segment right, Segment top, Segment bottom)
    {
//        boolean tlDiag = false;
//        boolean brDiag = false;
//        Intersection inter = null;
        this(left, right, top, bottom, false, false,null);
    }
    
//    public Kite(Segment left, Segment right, Segment top, Segment bottom, boolean tlDiag, boolean brDiag, Intersection inter)
    public Kite(Segment left, Segment right, Segment top, Segment bottom, boolean tlDiag, boolean brDiag,Intersection inter)
    {
        super(left, right, top, bottom);
        
        if (MathUtilities.doubleEquals(left.length(), top.length()) && MathUtilities.doubleEquals(right.length(), bottom.length()))
        {
            pairASegment1 = left;
            pairASegment2 = top;

            pairBSegment1 = right;
            pairBSegment2 = bottom;
        }
        else if (MathUtilities.doubleEquals(left.length(), bottom.length()) && MathUtilities.doubleEquals(right.length(), top.length()))
        {
            pairASegment1 = left;
            pairASegment2 = bottom;

            pairBSegment1 = right;
            pairBSegment2 = top;
        }
        else
        {
            ExceptionHandler.throwException( new IllegalArgumentException("Quadrilateral does not define a kite; no two adjacent sides are equal lengths: " + this));
        }

        //Set the diagonal and intersection values
        if (!tlDiag) this.SetTopLeftDiagonalInValid();
        if (!brDiag) this.SetBottomRightDiagonalInValid();
        this.SetIntersection(inter);
    }

    @Override
    public boolean IsStrongerThan(Polygon that)
    {
        if (that instanceof Kite) return false;
        if (that instanceof Parallelogram) return false;
        if (that instanceof Trapezoid) return false;
        if (that instanceof Quadrilateral) return true;

        return false;
    }

    @Override
    public boolean structurallyEquals(Object obj)
    {
        if (obj == null) return false;
        if(!(obj instanceof Trapezoid)) return false;
//        Kite thatKite = (Kite)obj;

        //return base.StructurallyEquals(obj);
        return super.HasSamePoints((Quadrilateral)obj);
    }

    @Override
    public boolean equals(Object obj)
    {
        return structurallyEquals(obj);
    }

    @Override
    public String toString()
    {
        return "Kite(" + topLeft.toString() + ", " + topRight.toString() + ", " +
                bottomRight.toString() + ", " + bottomLeft.toString() + ")";
    }

    @Override
    public String CheapPrettyString()
    {
        StringBuilder str = new StringBuilder();
        for (Point pt : points)
        {
            str.append(pt.CheapPrettyString());
        }
        return "Kite(" + str.toString() + ")";
    }

    @Override
    public int getHashCode() { return super.getHashCode(); }
}
