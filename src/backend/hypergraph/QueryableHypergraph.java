package backend.hypergraph;

import java.util.HashSet;
import java.util.Set;

import backend.ast.GroundedClause;
import backend.ast.Descriptors.Midpoint;
import backend.ast.Descriptors.Strengthened;
import backend.ast.figure.components.*;
import backend.ast.figure.components.triangles.*;
import backend.symbolicAlgebra.equations.*;

public class QueryableHypergraph<T, A> extends Hypergraph<T, A>
{    
    public QueryableHypergraph()
    {
        super();

        initQueryContainers();
    }
    //
    // @author C. Alvin
    //
    // The intent here is two-fold:
    //      (1) add a node to the hypergraph like normal.
    //      (2) add the node to the appropriate queryable containers (for fast access)
    //
    @Override
    public boolean addNode(T data)
    {
        // Add to the hypergraph
        if (!super.addNode(data)) return false;

        // Add to the proper queryable containers
        addToQueryableContainers(data);

        return true;
    }

    //
    //
    //
    // ----------  The queryable containers -------------
    //
    //
    //
    private HashSet<Point> _points;
    private HashSet<Segment> _segments;

    //
    // Circles
    //
    private HashSet<Circle> _circles;
    private HashSet<MinorArc> _minorArcs;
    private HashSet<MajorArc> _majorArcs;
    private HashSet<Arc> _arcs;

    //
    // Descriptors
    //
    private HashSet<Midpoint> _midpoints;

    //
    // Equations
    //
    protected EquationQueryHandler _equationHandler;

    //
    // Strengthened Clauses
    //
    private HashSet<Strengthened> _sMidpoints;

    private HashSet<Strengthened> _sTriangles;
    private HashSet<Strengthened> _sIsoTriangles;
    private HashSet<Strengthened> _sRightTriangles;
    private HashSet<Strengthened> _sEqTriangles;
    
    private HashSet<Strengthened> _sQuadrilaterals;
    // All other quadrilaterals
    
    private void initQueryContainers()
    {
        //
        // Basics
        //
        _points = new HashSet<Point>();
        _segments = new HashSet<Segment>();

        //
        // Circles
        //
        _circles = new HashSet<Circle>();
        _minorArcs = new HashSet<MinorArc>();
        _majorArcs = new HashSet<MajorArc>();
        _arcs = new HashSet<Arc>();

        //
        // Descriptors
        //
        _midpoints = new HashSet<Midpoint>();
        
        //
        // Equations
        //
        _equationHandler = new EquationQueryHandler();

        //
        // Strengthened Clauses
        //
        _sMidpoints = new HashSet<Strengthened>();
        
        _sTriangles = new HashSet<Strengthened>();
        _sIsoTriangles = new HashSet<Strengthened>();
        _sRightTriangles = new HashSet<Strengthened>();
        _sEqTriangles = new HashSet<Strengthened>();

        _sQuadrilaterals = new HashSet<Strengthened>();

    }

    private void addToQueryableContainers(T data)
    {
        //
        // Basics
        //
        if (data instanceof Point)
        {
            _points.add((Point)data);
        }
        else if (data instanceof Segment)
        {
            _segments.add((Segment)data);
        }
        //
        // Circles
        //
        else if (data instanceof Circle)
        {
            _circles.add((Circle)data);
        }
        else if (data instanceof Arc)
        {
            _arcs.add((Arc)data);

            if (data instanceof MinorArc)
            {
                _minorArcs.add((MinorArc)data);
            }
            else if (data instanceof MajorArc)
            {
                _majorArcs.add((MajorArc)data);
            }
        }
        else if (data instanceof Midpoint)
        {
            _midpoints.add((Midpoint)data);
            
            //
            // Handle all equation types
            //
        }
        //
        // Defer equation processing to the query container
        //
        else if (data instanceof Equation)
        {
            _equationHandler.add((Equation)data);
        }

        //
        // Handle all strengthened clauses
        //
        else if (data instanceof Strengthened)
        {
            addToStrengthened((Strengthened)data);
        }
    }
    
    private void addToStrengthened(Strengthened s)
    {
        GroundedClause strengthened = s.getStrengthened();
        
        if (strengthened instanceof Midpoint)
        {
            _sMidpoints.add(s);
        }
        else if (strengthened instanceof Triangle)
        {
            _sTriangles.add(s);
            
            if (strengthened instanceof IsoscelesTriangle)
            {
                _sIsoTriangles.add(s);
            }
            else if (strengthened instanceof RightTriangle)
            {
                _sRightTriangles.add(s);
            }
            else if (strengthened instanceof EquilateralTriangle)
            {
                _sEqTriangles.add(s);
            }
        }
        else if (strengthened instanceof Triangle)
        {
            _sQuadrilaterals.add(s);
      
            // ...
        }
    }

    //
    //
    //
    //
    // The queries (as methods)
    //
    //
    //
    public Set<Midpoint> getMidpoints() { return _midpoints; }

    //
    // Strengthened Clauses
    //
    public Set<Strengthened> getStrengthenedMidpoints() { return _sMidpoints; }
    public Set<Strengthened> getStrengthenedTriangles() { return _sTriangles; }
    public Set<Strengthened> getStrengthenedQuadrilaterals() { return _sQuadrilaterals; }

    //
    // Equations: Defer to the Query Class
    //
    //
    public SegmentEquation getSegmentEquation(SegmentEquation eq) { return _equationHandler.getSegmentEquation(eq); }
    public AngleEquation getAngleEquation(AngleEquation eq) { return _equationHandler.getAngleEquation(eq); }
    public ArcEquation getArcEquation(ArcEquation eq) { return _equationHandler.getArcEquation(eq); }
    public AngleArcEquation getAngleArcEquation(AngleArcEquation eq) { return _equationHandler.getAngleArcEquation(eq); }
    public Equation getGneralEquation(Equation eq) { return _equationHandler.getGeneralEquation(eq); }
}
