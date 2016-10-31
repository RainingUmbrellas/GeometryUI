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
package backend.precomputer;

import java.util.ArrayList;
import backend.ast.GroundedClause;
import backend.ast.Descriptors.Altitude;
import backend.ast.Descriptors.AngleBisector;
import backend.ast.Descriptors.Collinear;
import backend.ast.Descriptors.Complementary;
import backend.ast.Descriptors.Descriptor;
import backend.ast.Descriptors.InMiddle;
import backend.ast.Descriptors.Intersection;
import backend.ast.Descriptors.Median;
import backend.ast.Descriptors.Parallel;
import backend.ast.Descriptors.Perpendicular;
import backend.ast.Descriptors.PerpendicularBisector;
import backend.ast.Descriptors.SegmentBisector;
import backend.ast.Descriptors.Strengthened;
import backend.ast.Descriptors.Supplementary;
import backend.ast.Descriptors.Arcs_and_Circles.ArcInMiddle;
import backend.ast.Descriptors.Arcs_and_Circles.CircleCircleIntersection;
import backend.ast.Descriptors.Arcs_and_Circles.CircleSegmentIntersection;
import backend.ast.Descriptors.Relations.SimilarTriangles;
import backend.ast.Descriptors.Relations.Congruences.CongruentAngles;
import backend.ast.Descriptors.Relations.Congruences.CongruentArcs;
import backend.ast.Descriptors.Relations.Congruences.CongruentSegments;
import backend.ast.Descriptors.Relations.Congruences.CongruentTriangles;
import backend.ast.Descriptors.Relations.Proportionalities.ProportionalAngles;
import backend.ast.Descriptors.Relations.Proportionalities.SegmentRatio;
import backend.ast.figure.components.*;
import backend.ast.figure.components.Circle;
import backend.ast.figure.components.Quadrilateral;
import backend.ast.figure.components.Triangle;
import backend.utilities.Pair;
import backend.utilities.ast_helper.Utilities;
public class CoordinatePrecomputer
{
    private ArrayList<Circle> circles;
    private ArrayList<Quadrilateral> quadrilaterals;
    private ArrayList<Triangle> triangles;
    private ArrayList<Segment> segments;
    private ArrayList<Angle> angles;
    private ArrayList<Collinear> collinear;
    
    private ArrayList<InMiddle> inMiddles;
    private ArrayList<Intersection> intersections;
    private ArrayList<Perpendicular> perpendiculars;
    private ArrayList<Parallel> parallels;
    
    private ArrayList<MinorArc> minorArcs;
    private ArrayList<MajorArc> majorArcs;
    private ArrayList<Semicircle> semiCircles;
    private ArrayList<Sector> sectors;
    private ArrayList<ArcInMiddle> arcInMiddle;
    
    private ArrayList<CircleSegmentIntersection> csIntersections;
    private ArrayList<CircleCircleIntersection> ccIntersections;
    
    public CoordinatePrecomputer(ArrayList<GroundedClause> figure)
    {
        circles = new ArrayList<Circle>();
        quadrilaterals = new ArrayList<Quadrilateral>();
        triangles = new ArrayList<Triangle>();
        segments = new ArrayList<Segment>();
        angles = new ArrayList<Angle>();
        collinear = new ArrayList<Collinear>();

        inMiddles = new ArrayList<InMiddle>();
        intersections = new ArrayList<Intersection>();
        perpendiculars = new ArrayList<Perpendicular>();
        parallels = new ArrayList<Parallel>();

        minorArcs = new ArrayList<MinorArc>();
        majorArcs = new ArrayList<MajorArc>();
        semiCircles = new ArrayList<Semicircle>();
        sectors = new ArrayList<Sector>();
        arcInMiddle = new ArrayList<ArcInMiddle>();

        FilterClauses(figure);
    }
    
    //Split the figure into the constituent clauses
    public void FilterClauses(ArrayList<GroundedClause> figure)
    {
        for(GroundedClause clause : figure)
        {
            if(clause instanceof Circle && clause != null)
            {
                circles.add((Circle)clause);
            }
            else if(clause instanceof Quadrilateral && clause != null)
            {
                quadrilaterals.add((Quadrilateral)clause);
            }
            else if(clause instanceof Triangle && clause != null)
            {
                triangles.add((Triangle)clause);
            }
            else if(clause != null && clause instanceof Angle)
            {
            	angles.add((Angle)clause);
            }
            else if(clause != null && clause instanceof Segment)
            {
            	segments.add((Segment)clause);
            }
            else if(clause != null && clause instanceof InMiddle)
            {
            	inMiddles.add((InMiddle)clause);
            }
            else if(clause != null && clause instanceof Collinear)
            {
            	collinear.add((Collinear)clause);
            }
            else if(clause != null && clause instanceof Parallel)
            {
            	parallels.add((Parallel)clause);
            }
            else if(clause != null && clause instanceof Perpendicular)
            {
            	perpendiculars.add((Perpendicular)clause);
            }
            else if(clause != null && clause instanceof Intersection)
            {
            	intersections.add((Intersection)clause);
            }
            else if(clause !=null && clause instanceof MinorArc)
            {
            	minorArcs.add((MinorArc)clause);
            }
            else if(clause != null && clause instanceof MajorArc)
            {
            	majorArcs.add((MajorArc)clause);
            }
            else if(clause != null && clause instanceof Semicircle)
            {
            	semiCircles.add((Semicircle)clause);
            }
            else if(clause != null && clause instanceof Sector)
            {
            	sectors.add((Sector)clause);
            }
            else if(clause != null && clause instanceof ArcInMiddle)
            {
            	arcInMiddle.add((ArcInMiddle)clause);
            }
        }
    }
    
    //
    // Numerically (via the coordinates from the UI), calculate the relationships among all figures
    //    1) Congruences (angles, segments, triangles, arcs)
    //    2) Parallel
    //    3) Equalities
    //    4) Perpendicular
    //    5) Similarity
    //
    ArrayList<Descriptor> descriptors = new ArrayList<Descriptor>();
    public ArrayList<Descriptor> GetPrecomputedRelations()
    {
    	return descriptors;
    }
    
    public void CalculateRelations()
    {
    	//Segment, Parallel, and Perpendicular, and congruences
    	for(int s1 = 0; s1 < segments.size(); s1++)
    	{
    		for(int s2 = s1 + 1; s2 < segments.size(); s2++)
    		{
    			//Congruence
    			if(segments.get(s1).CoordinateCongruent(segments.get(s2)))
    			{
    				descriptors.add(new CongruentSegments(segments.get(s1),segments.get(s2)));
    			}
    			
    			//Parallel and Perpendicular lines
    			if(segments.get(s1).CoordinateParallel(segments.get(s2)))
    			{
    				descriptors.add(new Parallel(segments.get(s1),segments.get(s2)));
    			}
    			
    			//Perpendicular, bisector, perpendiculat bisector
    			else
    			{
    				//these are the general intersection points between the endpoints or on the endpoints of the segments (in some cases)
    				Point intersectionPerp = segments.get(s1).CoordinatePerpendicular(segments.get(s2));
    				// is segment[s2] a bisector of segment[s1]?
    				Point intersectionBisec = segments.get(s1).CoordinateBisector(segments.get(s2));//return the actual intersection point
    				if(intersectionPerp != null && intersectionBisec != null)
    				{
    					descriptors.add(new PerpendicularBisector(new Intersection(intersectionPerp, segments.get(s1), segments.get(s2)), segments.get(s2)));
    				}
    				else if(intersectionPerp != null)
    				{
    					descriptors.add(new Perpendicular(new Intersection(intersectionPerp, segments.get(s1), segments.get(s2))));
    				}
    				else if(intersectionBisec != null)
    				{
    					descriptors.add(new SegmentBisector(new Intersection(intersectionBisec, segments.get(s1), segments.get(s2)),segments.get(s2)));
    				}
    				
    				//we may have a bisector in the other direction
    				intersectionBisec = segments.get(s2).CoordinateBisector(segments.get(s1));
    				if(intersectionPerp != null && intersectionBisec != null)
    				{
    							descriptors.add(new PerpendicularBisector(new Intersection(intersectionPerp, segments.get(s1), segments.get(s2)),segments.get(s1)));
    				}
    				else if(intersectionBisec != null)
    				{
    					descriptors.add(new SegmentBisector(new Intersection(intersectionBisec, segments.get(s2), segments.get(s1)), segments.get(s1)));
    				}
    			}
    			
    			//Proportional line segments
    			//just generate if the ratio is really an integer or half-step
    			Pair<Integer,Integer> proportion = segments.get(s1).CoordinateProportional(segments.get(s2));
    			if(proportion.getValue() != -1)
    			{
    				if(proportion.getValue() <=2 || proportion.getKey() <= 2)
    				{
    					if(Utilities.DEBUG)
    					{
    						System.Diagnostics.Debug.WriteLine("< " + proportion.getKey() + ", " + proportion.getValue() + " >: " + segments.get(s1) + " : " + segments.get(s2));
    					}
    					descriptors.add(new SegmentRatio(segments.get(s1), segments.get(s2)));
    				}
    			}
    		}
    	}
    	
    	//Angle congruences; complementary and supplementary
    	for(int a1 = 0; a1 < angles.size(); a1++)
    	{
    		for(int a2 = a1 +1; a2< angles.size(); a2++)
    		{
    			if(angles.get(a1).CoordinateCongruent(angles.get(a2)) && !Utilities.CompareValues(angles.get(a1).getMeasure(), 180))
    			{
    				descriptors.add(new CongruentAngles(angles.get(a1),angles.get(a2)));
    			}
    			
    			if(angles.get(a1).IsComplementaryTo(angles.get(a2)))
    			{
    				descriptors.add(new Complementary(angles.get(a1),angles.get(a2)));
    			}
    			else if(angles.get(a1).IsSupplementaryTo(angles.get(a2)))
    			{
    				descriptors.add(new Supplementary(angles.get(a1), angles.get(a2)));
    			}
    			
    			//Proportional angle measure
    			//just generate if the ratio is really an integer or half-step
    			Pair<Integer,Integer> proportion = angles.get(a1).CoordinateProportional(angles.get(a2));
    			if(proportion.getValue() != -1)
    			{
    				if(proportion.getValue() <= 2 || proportion.getKey() <= 2)
    				{
    					if(Utilities.DEBUG)
    					{
    						System.Diagnostics.Debu.WriteLine("< " + proportion.getKey() + ", " + proportion.getValue() + " >: " + angles.get(a1) + " : " + angles.get(a2));
    					}
    					descriptors.add(new ProportionalAngles(angles.get(a1), angles.get(a2)));
    				}
    			}
    		}
    	}
    	
    	//
        // Triangle congruences OR similarity (congruence is a stronger relationship than similarity)
        //
    	for (int t1 = 0; t1< triangles.size(); t1++)
    	{
    		for(int t2 = t1 +1; t2 < triangles.size(); t2++)
    		{
    			Pair<Triangle,Triangle> corresponding = triangles.get(t1).CoordinateCongruent(triangles.get(t2));
    			if(corresponding.getKey() != null && corresponding.getValue() != null)
    			{
    				descriptors.add(new CongruentTriangles(corresponding.getKey(), corresponding.getValue()));
    			}
    			else if(triangles.get(t1).CoordinateSimilar(triangles.get(t2)))
    			{
    				descriptors.add(new SimilarTriangles(triangles.get(t1), triangles.get(t2)));
    			}
    		}
    	}
    	
    	
    	
    	//WARNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    	//THIS IS UNTESTED. IT SEEMS TO ONLY COMPILE IF I TAKE OUT THE TEMPLATE PARAMETER
    	//Arc congruences
    	CalculateArcCongruences(minorArcs);
    	CalculateArcCongruences(majorArcs);
    	CalculateArcCongruences(semiCircles);
    	
    	//
        // Calculate all segment relations to triangles: bisector, median, altitude, perpendicular bisector
        //
    	for(Triangle tri : triangles)
    	{
    		for(Segment segment : segments)
    		{
    			//median
    			if(tri.CoordinateMedian(segment))
    			{
    				descriptors.add(new Median(segment, tri));
    			}
    			
    			//Altitude
    			if(tri.CoordinateAltitude(segment))
    			{
    				descriptors.add(new Altitude(tri,segment));
    			}
    		}
    	}
    	
    	//calculate ande bisectors
    	for(Angle angle : angles)
    	{
    		if(!Utilities.CompareValues(angle.getMeasure(),  180))
    		{
    			for(Segment segment : segments)
    			{
    				//angle bisector
    				if(angle.CoordinateAngleBisector(segment))
    				{
    					descriptors.add(new AngleBisector(angle,segment));
    				}
    			}
    		}
    	}
    	
    	//Dumping the relations
    	if(Utilities.DEBUG)
    	{
    		System.Diagnostics.Debug.WriteLine("Precomputed Relations");
    		for(Descriptor descriptor : descriptors)
    		{
    			System.Diagnostics.Debug.WriteLine(descriptors.toString());
    		}
    		
    	}
    }
    
    private <T> void CalculateArcCongruences(ArrayList<T> arcs)
    {
    	for(int a1 = 0; a1< arcs.size(); a1++)
    	{
    		for(int a2 = a1+1; a2 < arcs.size(); a2++)
    		{
    			if(arcs.get(a1).CoordinateCongruent(arcs.get(a2)))
    			{ 
    				descriptors.add(new CongruentArcs(arcs.get(a1), arcs.get(a2)));
    			}
    		}
    	}
    }
    
    //can we determine any stregnthening in the figure class (scalene to equilateral, etc)
    ArrayList<Strengthened> strengthened = new ArrayList<Strengthened>();
    public ArrayList<Strengthened> GetStrengthenedClauses()
    {
    	return strengthened;
    }
    
    public void CalculateStrengthening()
    {
    	//
        // Can a quadrilateral be strenghtened? Quad -> trapezoid, Quad -> Parallelogram?, etc.
        //
    	for(Quadrilateral quad : quadrilaterals)
    	{
    		strengthened.AddRange(Quadrilateral.CanBeStrengthened(quad));
    	}
    	
    	//can a triangle be strengthened? scalene -> isosceles -> equilateral?
    	for(Triangle t : triangles)
    	{
    		strengthened.AddRange(Triangle.CanBeStrengthened(t));
    	}
    	
    	//can an inMiddle relationship be classified as a midpoint?
    	for(InMiddle im : inMiddles)
    	{
    		Strengthened s = im.canBeStrengthened();
    		if(s!=null)
    		{
    			strengthened.add(s);
    		}
    	}
    	
    	//right angles
    	for(Angle angle : angles)
    	{
    		if(Utilities.CompareValues(angle.getMeasure(), 90))
    		{
    			strengthened.add(new Strengthened(angle, new RightAngle(angle)));
    		}
    	}
    	
    	//Dumping the strengthening
    	if(Utilities.DEBUG)
    	{
    		System.Diagnostics.Debug.WriteLine("Precomputed Strengthening");
    		for(Strengthened s : strengthened)
    		{
    			System.Diagnostics.Debug.WriteLine(s.toString());
    		}
    	}
    }
}
