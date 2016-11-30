package channels.fromUI;

import backend.ast.figure.components.Point;
import backend.ast.figure.components.Segment;
import mainNonUI.Main;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.construction.Construction;
import rene.zirkel.objects.ConstructionObject;
import rene.zirkel.objects.IntersectionObject;
import rene.zirkel.objects.LineObject;
import rene.zirkel.objects.MidpointObject;
import rene.zirkel.objects.PointObject;
import rene.zirkel.objects.PrimitiveLineObject;
import rene.zirkel.objects.RayObject;
import rene.zirkel.objects.SegmentObject;
import rene.zirkel.objects.TwoPointLineObject;
import rene.zirkel.objects.VectorObject;

public class FromUI
{
    public static boolean sendToBackend(ZirkelCanvas zc/*, ZirkelFrame zf*/)
    {
        Construction uiRepresentation = zc.getConstruction();
        Diagram backendRepresentation = translateToDiagram(uiRepresentation);
        
        Main.receiveDiagram(backendRepresentation);
        return false;
    }
    
    private static Diagram translateToDiagram(Construction C)
    {
        Diagram D = new Diagram();
        
        for(int i = 0; i < C.V.size(); i++)
        {
            ConstructionObject co = (ConstructionObject) C.V.get(i);
            
            if(co instanceof PointObject)
            {
                if(co instanceof IntersectionObject)
                {
                    //Do something
                    //This is extended by
                    //  AxisFunctionIntersectionObject
                    //  CircleIntersectionObject
                    //  LineCircleIntersectionObject
                    //  LineIntersectionObject
                    //  LineQuadraticIntersectionObject
                    //  PointonObjectIntersectionObject
                    //  QuadraticQuadraticIntersectionObject
                }
                else if(co instanceof MidpointObject)
                {
                    //Do something
                }
                else
                {
                    PointObject point = (PointObject) co;
                    D.addPoint(FromUITranslate.translatePoint(point));
                }
            }
            
            if(co instanceof PrimitiveLineObject)
            {
                if(co instanceof TwoPointLineObject)
                {
                    if(co instanceof SegmentObject)
                    {
                        
                        if(co instanceof VectorObject)
                        {
                            System.out.println("Vector Detected...Converting to Segment.");
                            SegmentObject segment = (SegmentObject) co;
                            D.addSegment(FromUITranslate.translateSegment(segment));
                        }
                        else
                        {
                            SegmentObject segment = (SegmentObject) co;
                            D.addSegment(FromUITranslate.translateSegment(segment));
                        }
                    }
                    else if(co instanceof RayObject)
                    {
                        //Do something
                    }
                    else if(co instanceof LineObject)
                    {
                        //Do something
                    }
                    else
                    {
                        //Do something
                    }
                }
                else
                {
                    //Do something
                    //This is extended by
                    //  AxisObject
                    //  FixedAngleObject
                    //  ParallelObject
                    //  PlumbObject
                }
            }
        }
        
        return D;
    }
}
