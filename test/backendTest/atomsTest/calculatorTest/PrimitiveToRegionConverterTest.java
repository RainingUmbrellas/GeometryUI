package backendTest.atomsTest.calculatorTest;

import java.util.ArrayList;

import org.junit.Test;

import backend.ast.figure.components.Circle;
import backend.ast.figure.components.Point;
import backend.atoms.calculator.FacetCalculator;
import backend.atoms.calculator.Primitive;
import backend.atoms.calculator.PrimitiveToRegionConverter;
import backend.atoms.components.AtomicRegion;
import backend.atoms.undirectedPlanarGraph.EdgeType;
import backend.atoms.undirectedPlanarGraph.PlanarGraph;
import backend.atoms.undirectedPlanarGraph.PlanarGraphNode;

public class PrimitiveToRegionConverterTest
{
    @Test public void PrimitiveToRegionConverter_Convert_CycleGraphTest() throws Exception
    {
        System.out.println("Running PrimitiveToRegionConverter_ConvertCycleGraphTest...");
        // create the graph
        PlanarGraph graph = new PlanarGraph();
        
        // create the Points
        Point point1 = new Point("point1", 2, 4);
        Point point2 = new Point("point2", 4, 1);
        Point point3 = new Point("point3", 6, 4);
        
        // add the points to the graph
//        System.out.println("Adding Points:");
        graph.addNode(point1);
        graph.addNode(point2);
        graph.addNode(point3);
        
        // add edges
        graph.addUndirectedEdge(point3, point1, 0, EdgeType.REAL_SEGMENT);
        graph.addUndirectedEdge(point2, point1, 0, EdgeType.REAL_SEGMENT);
        graph.addUndirectedEdge(point3, point2, 0, EdgeType.REAL_SEGMENT);
        
        // create a copy of the graph for the atom converter
        PlanarGraph graphCopy = new PlanarGraph(graph);
        
        // create the facet calculator and get the primitives
        FacetCalculator calc = new FacetCalculator(graph);
        ArrayList<Primitive> primitives = calc.GetPrimitives();
        
        // convert the primitives to atomic regions
        ArrayList<AtomicRegion> atoms = PrimitiveToRegionConverter.Convert(graphCopy, primitives, new ArrayList<Circle>());
        
        // print out the atoms
        System.out.println("AtomicRegions: ");
        for (AtomicRegion atom : atoms)
        {
            System.out.println("\t" + atom.toString());
        }
        
        System.out.println("Done");
    }

}