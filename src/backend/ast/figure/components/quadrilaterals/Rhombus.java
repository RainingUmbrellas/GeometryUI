package backend.ast.figure.components.quadrilaterals;

import backend.ast.Descriptors.Intersection;
import backend.ast.figure.components.Point;
import backend.ast.figure.components.Segment;
import backend.ast.figure.components.polygon.Polygon;
import backend.utilities.exception.ExceptionHandler;
import backend.utilities.math.MathUtilities;

public class Rhombus extends Parallelogram
{
    public Rhombus(Quadrilateral quad)
    {
        this(quad.left, quad.right, quad.top, quad.bottom,
                quad.TopLeftDiagonalIsValid(), quad.BottomRightDiagonalIsValid(), quad.getDiagonalIntersection());
    }

    //TODO: Need to find a way to determine the validity of diagonals, and to find the intersection if both diagonals are valid
    //These values are determined for base quadrilaterals by the Implied Component Calculator in the UI parser, but are never
    //computed for the specialized quads

    
    public Rhombus(Segment left, Segment right, Segment top, Segment bottom)
    {
        this(left, right, top, bottom, false, false, null);
        
    }
    
//    public Rhombus(Segment left, Segment right, Segment top, Segment bottom, boolean tlDiag, boolean brDiag, Intersction inter)
    public Rhombus(Segment left, Segment right, Segment top, Segment bottom, boolean tlDiag, boolean brDiag, Intersection inter)
    {
        super(left, right, top, bottom);
        if (!MathUtilities.doubleEquals(top.length(), left.length()))
        {
            ExceptionHandler.throwException( new IllegalArgumentException("Quadrilateral is not a Rhombus; sides are not equal length: " + top + " " + left));
        }
        if (!MathUtilities.doubleEquals(top.length(), right.length()))
        {
            ExceptionHandler.throwException( new IllegalArgumentException("Quadrilateral is not a Rhombus; sides are not equal length: " + top + " " + right));
        }
        if (!MathUtilities.doubleEquals(top.length(), bottom.length()))
        {
            ExceptionHandler.throwException( new IllegalArgumentException("Quadrilateral is not a Rhombus; sides are not equal length: " + top + " " + bottom));
        }

        //Set the diagonal and intersection values
        if (!tlDiag) this.SetTopLeftDiagonalInValid();
        if (!brDiag) this.SetBottomRightDiagonalInValid();
        this.SetIntersection(inter);
    }

    @Override
    public boolean IsStrongerThan(Polygon that)
    {
        if (that instanceof Trapezoid) return false;
        if (that instanceof Kite) return false;
        if (that instanceof Rectangle) return false;
        if (that instanceof Rhombus) return false;
        if (that instanceof Parallelogram) return true;
        if (that instanceof Quadrilateral) return true;

        return false;
    }

    @Override
    public boolean structurallyEquals(Object obj)
    {
        if(obj == null) return false;
        if(!(obj instanceof Rhombus));
        Rhombus thatRhom = (Rhombus)obj;

        if (thatRhom instanceof Square) return false;

        //return base.StructurallyEquals(obj);
        return super.HasSamePoints((Quadrilateral)obj);
    }

    @Override
    public boolean equals(Object obj)
    {
        //Rhombus thatRhom = obj as Rhombus;
        //if (thatRhom == null) return false;

        //if (thatRhom is Square) return false;

        //return this.StructurallyEquals(obj);
        return this.structurallyEquals(obj);
    }

    @Override
    public String toString()
    {
        return "Rhombus(" + topLeft.toString() + ", " + topRight.toString() + ", " +
                bottomRight.toString() + ", " + bottomLeft.toString() + ")";
    }

    @Override
    public String CheapPrettyString()
    {
        StringBuilder str = new StringBuilder();
        for (Point pt : points) str.append(pt.CheapPrettyString());
        return "Rhombus(" + str.toString() + ")";
    }

    @Override
    public int getHashCode() { return super.getHashCode(); }
}
