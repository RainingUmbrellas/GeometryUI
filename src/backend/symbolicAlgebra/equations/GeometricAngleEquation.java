package backend.symbolicAlgebra.equations;

import backend.ast.GroundedClause;
public class GeometricAngleEquation extends AngleEquation
{
    private GeometricAngleEquation()
    {
        super();
    }
    
    public GeometricAngleEquation(Equation eq)
    {
        super(eq);
    }
    
    public GeometricAngleEquation(GroundedClause left, GroundedClause right)
    {
        super(left, right);
    }
    
    public GroundedClause deepCopy()
    {
        return new GeometricAngleEquation(this.lhs.deepCopy(), this.rhs.deepCopy());
    }
    
    public int getHashCode()
    {
        return super.getHashCode();
    }
    
    public String toString()
    {
        return "GeometricEquation(" + lhs + " = " + rhs + "): " + justification;
    }
}
