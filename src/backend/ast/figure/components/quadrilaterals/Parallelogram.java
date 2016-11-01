package backend.ast.figure.components.quadrilaterals;

import backend.ast.figure.components.Point;
import backend.ast.figure.components.Polygon;
import backend.ast.figure.components.Quadrilateral;
import backend.ast.figure.components.Segment;

public class Parallelogram extends Quadrilateral 
{
    public Parallelogram(Quadrilateral quad)  
    { 
        this(quad.left, quad.right, quad.top, quad.bottom);
    }

    public Parallelogram(Segment left, Segment right, Segment top, Segment bottom)
    {
        super(left, right, top, bottom);
        if (!left.IsParallelWith(right))
        {
            throw new IllegalArgumentException("Given opposing segments are not parallel: " + left + " " + right);
        }

        if (!top.IsParallelWith(bottom))
        {
            throw new IllegalArgumentException("Given opposing segments are not parallel: " + top + " " + bottom);
        }
    }

    public boolean IsStrictParallelogram()
    {
        if (this instanceof Rhombus) return false;

        return true;
    }

    @Override
    public boolean IsStrongerThan(Polygon that)
    {
        if (that instanceof Kite) return false;
        if (that instanceof Trapezoid) return true;
        if (that instanceof Parallelogram) return false;
        if (that instanceof Quadrilateral) return true;

        return false;
    }

    @Override
    public boolean StructurallyEquals(Object obj)
    {
        if (obj == null) return false;
        if(!(obj instanceof Parallelogram));
        Parallelogram thatPara = (Parallelogram)obj;
        

        if (thatPara instanceof Rhombus || thatPara instanceof Rectangle) return false;

        //return base.StructurallyEquals(obj);
        return super.HasSamePoints((Quadrilateral)thatPara);
    }

    @Override
    public boolean Equals(Object obj)
    {
        if (obj == null) return false;
        if(!(obj instanceof Parallelogram));
        Parallelogram thatPara = (Parallelogram)obj;

        if (thatPara instanceof Rhombus) return false;

        return StructurallyEquals(obj);
    }

    @Override
    public String toString()
    {
        return "Parallelogram(" + topLeft.toString() + ", " + topRight.toString() + ", " +
                bottomRight.toString() + ", " + bottomLeft.toString() + ")";
    }

    @Override
    public int GetHashCode() { return super.GetHashCode(); }

    @Override
    public String CheapPrettyString()
    {
        StringBuilder str = new StringBuilder();
        for (Point pt : points) str.append(pt.CheapPrettyString());
        return "Parallelogram(" + str.toString() + ")";
    }
}