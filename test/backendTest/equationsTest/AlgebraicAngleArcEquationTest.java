package backendTest.equationsTest;

import org.junit.*;

import backend.equations.AlgebraicAngleArcEquation;
import backend.equations.AlgebraicArcEquation;
import backend.instantiator.algebra.Simplification;
import backend.utilities.exception.ArgumentException;
import backend.utilities.exception.ExceptionHandler;

public class AlgebraicAngleArcEquationTest
{
    @Test
    public void algebraicAngleArcEquationTest()
    {
        System.out.println("Running AlgebraicAngleArcEquation Test...");
        for (int i = 0; i < 100; i++)
        {
            AlgebraicAngleArcEquation eq = null, eq2 = null;
            try
            {
                eq = new AlgebraicAngleArcEquation(EquationGenerator.genAdditionEquationPair());
                eq2 = new AlgebraicAngleArcEquation(eq);
                Simplification.simplify(eq);
            }
            catch (ArgumentException e)
            {
                ExceptionHandler.throwException(new ArgumentException(e.toString()));
            }
            catch (CloneNotSupportedException e)
            {
                ExceptionHandler.throwException(new CloneNotSupportedException());
            }

            if (eq == null)
            {
                System.out.println("Failed from eq being null at iteration " + i + "...");
                break;
            }
            if (eq2 == null)
            {
                System.out.println("Failed from eq2 being null at iteration " + i + "...");
                break;
            }
            else if (!eq.equals(eq2))
            {
                System.out.println("Failed from eq != eq2 at iteration " + i + "...");
                break;
            }
        }
        System.out.println("Done");
    }
}
