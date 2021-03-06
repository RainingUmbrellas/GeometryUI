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
package backend.ast.Descriptors.parallel;

import java.util.ArrayList;

import backend.ast.GroundedClause;
import backend.ast.Descriptors.Descriptor;
import backend.ast.figure.components.Segment;
import backend.utilities.exception.ExceptionHandler;
import backend.utilities.exception.ArgumentException;

/// <summary>
/// Describes a point that lies on a segment.
/// </summary>
public class Parallel extends Descriptor
{
    protected Segment segment1;
    protected Segment segment2;
    
    public Segment getSegment1()
    {
        return segment1;
    }
    public Segment getSegment2()
    {
        return segment2;
    }
    
    public Parallel(Segment segment1, Segment segment2)
    {
        super();
        this.segment1 = segment1;
        this.segment2 = segment2;
        
        if(!segment1.isParallel(segment2))
        {
            ExceptionHandler.throwException(new ArgumentException("Given lines are not parallel: " + segment1 +
                        " ; " + segment2));
        }
    }
    
    //This should never be true, otherwise they are coinciding
    @Override
    public boolean isReflexive()
    {
        return segment1.structurallyEquals(segment2);
    }
    
    @Override
    public int getHashCode()
    {
        return super.getHashCode();
    }
    
    public Segment OtherSegment(Segment that)
    {
        if(segment1.equals(that))
        {
            return segment2;
        }
        if(segment2.equals(that)) 
        {
            return segment1;
        }
        return null;
    }
    public Segment CoincidesWith(Segment that)
    {
        if (segment1.isCollinearWith(that)) 
        {
            return segment1;
        }
        if (segment2.isCollinearWith(that))
        {
            return segment2;
        }

        return null;
    }
    
    public Segment SharedSegment(Parallel thatParallel)
    {
        if (segment1.isCollinearWith(thatParallel.segment1) && segment1.isCollinearWith(thatParallel.segment2))
        {
            return segment1;
        }
        if (segment2.isCollinearWith(thatParallel.segment1) && segment2.isCollinearWith(thatParallel.segment2))
        {
            return segment2;
        }

        return null;
    }
    
    public int SharesNumClauses(Parallel thatParallel)
    {
        int shared = segment1.isCollinearWith(thatParallel.segment1) && segment1.isCollinearWith(thatParallel.segment2) ? 1 : 0;
        shared += segment2.isCollinearWith(thatParallel.segment1) && segment2.isCollinearWith(thatParallel.segment2) ? 1 : 0;

        return shared;
    }
    
    @Override
    public boolean structurallyEquals(Object obj)
    {
        if(obj != null)
        {
            Parallel p = (Parallel)obj;
            // should be uneeded but left it in since it was in the original
//            if (p == null)
//            {
//                return false;
//            }
            return (segment1.structurallyEquals(p.segment1) && segment2.structurallyEquals(p.segment2)) ||
                   (segment1.structurallyEquals(p.segment2) && segment2.structurallyEquals(p.segment1));
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(obj != null && obj instanceof Parallel)
        {
            Parallel p = (Parallel)obj;
         // should be uneeded but left it in since it was in the original
//            if(p == null)
//            {
//                return false;
//            }
            return (segment1.equals(p.segment1) && segment2.equals(p.segment2)) ||
                    (segment1.equals(p.segment2) && segment2.equals(p.segment1)) && super.equals(obj);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public String toString()
    {
        return "Parallel(" + segment1.toString() + ", " + segment2.toString() + ") " + justification;
    }
    
    //this is commented out to make things compile
//    private static final String NAME = "Transitivity";
//    private static Hypergraph.EdgeAnnotation annotation = new Hypergraph.EdgeAnnotation(NAME, EngineUIBrdige.JustificationSwitch.TRANSITIVE_PARALLEL);
//    
//    public static ArrayList<GenericInstatiator.EdgeAggregator> CreateTransitiveParallel(Parallel parallel1, Parallel parallel2)
//    {
//    	annotation.active = EngineUIBridge.JustificationSwitch.TRANSITIVE_PARALLEL;
//    	ArrayList<GenericInstatiator.EdgeAggregator> newGrounded = new ArrayList<GenericInstatiator.EdgeAggregator>();
//    	
//    	//
//        // Create the antecedent clauses
//        //
//    	ArrayList<GroundedClause> antecedent = new ArrayList<GroundedClause>();
//    	antecedent.add(parallel1);
//    	antecedent.add(parallel2);
//    	
//    	//
//    	// Create the consequent clause
//    	//
//    	Segment shared = parallel1.SharedSegment(parallel2);
//    	AlgebraicParallel newAP = new AlgebraicParallel(parallel1.OtherSegment(shared), parallel2.OtherSegment(shared));
//    	
//    	newGrounded.add(new GenericInstantiator.EdgeAggregator(antecedent, newAP, annotation));
//    	
//    	return newGrounded;
//    }
}
