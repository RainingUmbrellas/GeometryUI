package backend.ast.Descriptors.Relations.Congruences;

import backend.ast.figure.components.arcs.Arc;
import backend.utilities.ast_helper.Utilities;
import backend.utilities.exception.ArgumentException;
import backend.utilities.exception.ExceptionHandler;

public class CongruentArcs extends Congruent
{
	protected Arc ca1;
	protected Arc ca2;
	
	public Arc getFirstArc()
	{
		return ca1;
	}
	public Arc getSecondArc()
	{
		return ca2;
	}
	
	public CongruentArcs(Arc a1, Arc a2)
	{
		ca1 = a1;
		ca2 = a2;
		
		if(!Utilities.CompareValues(a1.getCircle().getRadius(), a2.getCircle().getRadius()))
		{
			ExceptionHandler.throwException(new ArgumentException("Arcs deduced congruent when radii differ " + this));
		}
		
		if(!Utilities.CompareValues(a1.getMinorMeasure(), a2.getMinorMeasure()))
		{
			ExceptionHandler.throwException(new ArgumentException("Arcs deduced congruent when measure differ" +this));
		}
	}
	
	@Override
	public boolean isReflexive()
	{
		return ca1.structurallyEquals(ca2);
	}
	
	public Arc OtherArc(Arc thatArc)
	{
		if (ca1.structurallyEquals(thatArc))
		{
			return ca2;
		}
        if (ca2.structurallyEquals(thatArc))
    	{
    		return ca1;
    	}
        return null;
	}
	
	@Override
	public boolean structurallyEquals(Object obj)
	{
		if(obj != null && obj instanceof CongruentArcs)
		{
			CongruentArcs cts = (CongruentArcs)obj;
			return this.ca1.structurallyEquals(cts.ca1) || this.ca2.structurallyEquals(cts.ca2) ||
	                   this.ca2.structurallyEquals(cts.ca1) || this.ca1.structurallyEquals(cts.ca2);
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof CongruentArcs)
		{
			CongruentArcs cts = (CongruentArcs)obj;
			return this.ca1.equals(cts.ca1) || this.ca2.equals(cts.ca2) ||
	                   this.ca2.equals(cts.ca1) || this.ca1.equals(cts.ca2);
		}
		return false;
	}
	
	public boolean VerifyCongruentArcs()
	{
		return Utilities.CompareValues(ca1.getMinorMeasure(), ca2.getMinorMeasure()) && Utilities.CompareValues(ca1.getCircle().getRadius(), ca2.getCircle().getRadius());
	}
	
	@Override
	public int getHashCode()
	{
		return super.getHashCode();
	}
	@Override
	public String toString()
	{
		return "Congruent(" + ca1.toString() + ", " + ca2.toString() + ") " + justification;
	}
	
}
