///*
// * iTutor � an intelligent tutor of mathematics
//
//Copyright (C) 2016-2017 C. Alvin and Bradley University CS Students (list of
//
//students)
//
//This program is free software: you can redistribute it and/or modify it under
//
//the terms of the GNU Affero General Public License as published by the Free
//
//Software Foundation, either version 3 of the License, or (at your option) any
//
//later version.
//
//This program is distributed : the hope that it will be useful, but WITHOUT
//
//ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
//
//FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
//
//details.
// */
///**
// * @author Tom_Nielsen
// *
// */
package backend.ast.Descriptors;

import backend.ast.Descriptors.Relations.Congruences.CongruentAngles;
import backend.ast.figure.components.angles.Angle;

public abstract class AnglePairRelation extends Descriptor
{
	protected Angle angle1;
	protected Angle angle2;
	
	public Angle getAngle1()
	{
		return angle1;
	}
	public Angle getAngle2() 
	{
		return angle2;
	}
	
	public AnglePairRelation(Angle ang1, Angle ang2)
	{
		super();
		angle1 = ang1;
		angle2 = ang2;
	}
	
	//return the shared angle in both congruences
	public Angle AngleShared(AnglePairRelation relation)
	{
		if (angle1.equates(relation.angle1) || angle1.equates(relation.angle2))
		{
			return angle1;
		}
        if (angle2.equates(relation.angle1) || angle2.equates(relation.angle2))
    	{
        	return angle2;
    	}

        return null;
	}
	
	// Return the shared angle in both congruences
    public Angle AngleShared(CongruentAngles cas)
    {
        if (angle1.equates(cas.first()) || angle1.equates(cas.second())) return angle1;
        if (angle2.equates(cas.first()) || angle2.equates(cas.second())) return angle2;

        return null;
    }
    
 // Return the shared angle in both congruences
    public Angle OtherAngle(Angle thatAngle)
    {
        if (angle1.equates(thatAngle)) return angle2;
        if (angle2.equates(thatAngle)) return angle1;

        return null;
    }

    // Return the shared angle in both congruences
    public boolean HasAngle(Angle thatAngle)
    {
        return angle1.equates(thatAngle) || angle2.equates(thatAngle);
    }
    
    @Override
    public int getHashCode()
    {
    	return super.getHashCode();
    }
    
    @Override
    public boolean structurallyEquals(Object obj)
    {
    	if(obj != null && obj instanceof AnglePairRelation)
    	{
    		AnglePairRelation relation = (AnglePairRelation)obj;
    		return (angle1.structurallyEquals(relation.angle1) && angle2.structurallyEquals(relation.angle2)) ||
                    (angle1.structurallyEquals(relation.angle2) && angle2.structurallyEquals(relation.angle1));
    	}
    	
    	//This is untested but should be correct. IF the if isn't hit then it should never be equal
    	return false;
    }
    
    @Override
    public boolean equals(Object obj)
    {
    	if(obj != null && obj instanceof AnglePairRelation)
    	{
    		AnglePairRelation relation = (AnglePairRelation)obj;
    		return (angle1.equals(relation.angle1) && angle2.equals(relation.angle2)) ||
                    (angle1.equals(relation.angle2) && angle2.equals(relation.angle1)) && super.equals(relation);
    	}
    	
    	//This is untested but should be correct. IF the if isn't hit then it should never be equal
    	return false;
    }
}
