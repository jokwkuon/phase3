package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CIAToolTest {

    private static final Path SAMPLE = Paths.get("sample/CIASample.java");

    @Test
    void changePoint2ImpactsWhileAndBody() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 2);
        assertTrue(result.contains(5), "while condition should be impacted");
        assertTrue(result.contains(7), "sum=sum+i should be impacted");
        assertTrue(result.contains(8), "i++ should be impacted");
    }

    @Test
    void changePoint2DoesNotImpactOtherMethod() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 2);
        assertFalse(result.contains(15), "int a=x in min() must not be impacted");
        assertFalse(result.contains(16), "int b=y in min() must not be impacted");
        assertFalse(result.contains(17), "if(a>b) in min() must not be impacted");
    }

    @Test
    void changePoint2ImpactsReturnResult() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 2);
        assertTrue(result.contains(13), "return result should be impacted by i");
    }

    @Test
    void changePoint4ImpactsIfAndReturn() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 4);
        assertTrue(result.contains(10), "if(result>threshold) should be impacted");
        assertTrue(result.contains(12), "result=threshold should be impacted");
        assertTrue(result.contains(13), "return result should be impacted");
    }

    @Test
    void changePoint4DoesNotImpactLoop() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 4);
        assertFalse(result.contains(5),  "while loop should not be impacted by threshold");
        assertFalse(result.contains(7),  "sum=sum+i should not be impacted by threshold");
        assertFalse(result.contains(8),  "i++ should not be impacted by threshold");
    }

    @Test
    void changePoint7ImpactsResult() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 7);
        assertTrue(result.contains(9),  "int result=sum should be impacted");
        assertTrue(result.contains(10), "if condition should be impacted");
        assertTrue(result.contains(13), "return result should be impacted");
    }

    @Test
    void changePoint17ImpactsBranchBody() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 17);
        assertTrue(result.contains(19), "int temp=a should be control-impacted");
        assertTrue(result.contains(20), "a=b should be control-impacted");
        assertTrue(result.contains(21), "b=temp should be control-impacted");
        assertTrue(result.contains(22), "return a should be data-impacted");
    }

    @Test
    void changePoint17DoesNotImpactOtherMethod() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 17);
        assertFalse(result.contains(1), "int sum=0 in compute() must not be impacted");
        assertFalse(result.contains(5), "while in compute() must not be impacted");
    }

    @Test
    void invalidChangePointReturnsEmpty() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 999);
        assertTrue(result.isEmpty(), "Non-existent change point should return empty list");
    }

    @Test
    void extractCounterParsesValidLabel() {
        assertEquals(5, CIATool.extractCounter("5: while (i <= n)"));
        assertEquals(0, CIATool.extractCounter("0: block"));
        assertEquals(13, CIATool.extractCounter("13: return result ;"));
    }

    @Test
    void extractCounterRejectsInvalidLabel() {
        assertEquals(-1, CIATool.extractCounter(null));
        assertEquals(-1, CIATool.extractCounter("*ENTRY*"));
        assertEquals(-1, CIATool.extractCounter("*EXIT*"));
        assertEquals(-1, CIATool.extractCounter(""));
    }

    @Test
    void resultIsSorted() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 2);
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) < result.get(i + 1),
                "Result list must be sorted ascending");
        }
    }

    @Test
    void changePointNotIncludedInResult() throws Exception {
        List<Integer> result = CIATool.analyse(SAMPLE, 4);
        assertFalse(result.contains(4), "Change point itself must not appear in impacted list");
    }
}
