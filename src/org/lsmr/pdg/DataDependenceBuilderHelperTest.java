package org.lsmr.pdg;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataDependenceBuilderHelperTest {

    private DataDependenceBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new DataDependenceBuilder();
    }

    private Object invoke(String methodName, Class<?>[] types, Object... args) throws Exception {
        Method m = DataDependenceBuilder.class.getDeclaredMethod(methodName, types);
        m.setAccessible(true);
        return m.invoke(builder, args);
    }

    private String defined(String label) throws Exception {
        return (String) invoke("getDefinedVariable", new Class<?>[]{String.class}, label);
    }

    @SuppressWarnings("unchecked")
    private Set<String> used(String label) throws Exception {
        return (Set<String>) invoke("getUsedVariables", new Class<?>[]{String.class}, label);
    }

    private String stripTypePrefix(String s) throws Exception {
        return (String) invoke("stripTypePrefix", new Class<?>[]{String.class}, s);
    }

    private int indexOfTopLevelAssignmentOp(String s) throws Exception {
        return (Integer) invoke("indexOfTopLevelAssignmentOp", new Class<?>[]{String.class}, s);
    }

    private String stripCommentsAndLiterals(String s) throws Exception {
        return (String) invoke("stripCommentsAndLiterals", new Class<?>[]{String.class}, s);
    }

    private String contentsInsideOuterParens(String s) throws Exception {
        return (String) invoke("contentsInsideOuterParens", new Class<?>[]{String.class}, s);
    }

    private boolean isPostIncDec(String s) throws Exception {
        return (Boolean) invoke("isPostIncDec", new Class<?>[]{String.class}, s);
    }

    private boolean isPreIncDec(String s) throws Exception {
        return (Boolean) invoke("isPreIncDec", new Class<?>[]{String.class}, s);
    }

    private String extractIncDecIdentifier(String s) throws Exception {
        return (String) invoke("extractIncDecIdentifier", new Class<?>[]{String.class}, s);
    }

    private String stripEnclosingParens(String s) throws Exception {
        return (String) invoke("stripEnclosingParens", new Class<?>[]{String.class}, s);
    }

    private boolean isSimpleIdentifier(String s) throws Exception {
        return (Boolean) invoke("isSimpleIdentifier", new Class<?>[]{String.class}, s);
    }

    private boolean looksLikeReferenceType(String s) throws Exception {
        return (Boolean) invoke("looksLikeReferenceType", new Class<?>[]{String.class}, s);
    }

    @Test
    void definedVariableForPostIncrement() throws Exception {
        assertEquals("x", defined("7: x++;"));
    }

    @Test
    void definedVariableForPreDecrement() throws Exception {
        assertEquals("x", defined("7: --x;"));
    }

    @Test
    void definedVariableForArrayAssignmentIsArrayName() throws Exception {
        assertEquals("a", defined("7: a[i] = 1;"));
    }

    @Test
    void definedVariableForFieldAssignmentUsesFieldName() throws Exception {
        assertEquals("field", defined("7: obj.field = x;"));
    }

    @Test
    void definedVariableForForInitializer() throws Exception {
        assertEquals("i", defined("7: for (int i = 0; i < n; i++)"));
    }

    @Test
    void definedVariableForReferenceTypeDeclaration() throws Exception {
        assertEquals("t", defined("7: String t = s;"));
    }

    @Test
    void usedVariablesForReturnStatement() throws Exception {
        Set<String> vars = used("7: return x + y;");
        assertTrue(vars.contains("x"));
        assertTrue(vars.contains("y"));
    }

    @Test
    void usedVariablesForThrowStatement() throws Exception {
        Set<String> vars = used("7: throw ex;");
        assertTrue(vars.contains("ex"));
    }

    @Test
    void usedVariablesForCaseLabel() throws Exception {
        Set<String> vars = used("7: case x + 1:");
        assertTrue(vars.contains("x"));
    }

    @Test
    void usedVariablesForCompoundAssignmentIncludeLhsAndRhs() throws Exception {
        Set<String> vars = used("7: x += y;");
        assertTrue(vars.contains("y"));
    }

    @Test
    void usedVariablesForIndexedAssignmentIncludeIndexVariables() throws Exception {
        Set<String> vars = used("7: a[i + j] = 1;");
        assertTrue(vars.contains("i"));
        assertTrue(vars.contains("j"));
    }

    @Test
    void usedVariablesIgnoreMethodNameButKeepArguments() throws Exception {
        Set<String> vars = used("7: foo(bar, baz);");
        assertFalse(vars.contains("foo"));
        assertTrue(vars.contains("bar"));
        assertTrue(vars.contains("baz"));
    }

    @Test
    void usedVariablesIgnoreStringLiteralAndComment() throws Exception {
        Set<String> vars = used("7: print(\"hello\") + y; // z");
        assertTrue(vars.contains("y"));
        assertFalse(vars.contains("hello"));
        assertFalse(vars.contains("z"));
    }

    @Test
    void stripTypePrefixHandlesPrimitive() throws Exception {
        assertEquals("x = 1;", stripTypePrefix("int x = 1;"));
    }

    @Test
    void stripTypePrefixHandlesReferenceType() throws Exception {
        assertEquals("t = s;", stripTypePrefix("String t = s;"));
    }

    @Test
    void assignmentIndexIgnoresEqualityOperator() throws Exception {
        assertEquals(-1, indexOfTopLevelAssignmentOp("x == y"));
    }

    @Test
    void assignmentIndexFindsCompoundAssignment() throws Exception {
        assertTrue(indexOfTopLevelAssignmentOp("x += y") >= 0);
    }

    @Test
    void stripCommentsAndLiteralsRemovesQuotedTextAndComments() throws Exception {
        String result = stripCommentsAndLiterals("x = \"abc\"; // comment");
        assertFalse(result.contains("abc"));
        assertFalse(result.contains("comment"));
        assertTrue(result.contains("x ="));
    }

    @Test
    void contentsInsideOuterParensExtractsCondition() throws Exception {
        assertEquals("x < 3", contentsInsideOuterParens("while (x < 3)"));
    }

    @Test
    void postIncDecDetectionWorks() throws Exception {
        assertTrue(isPostIncDec("x++;"));
        assertFalse(isPostIncDec("x = x + 1;"));
    }

    @Test
    void preIncDecDetectionWorks() throws Exception {
        assertTrue(isPreIncDec("++x;"));
        assertFalse(isPreIncDec("x++;"));
    }

    @Test
    void extractIncDecIdentifierWorks() throws Exception {
    	assertEquals("x;", extractIncDecIdentifier("++x;"));
    	assertEquals("y", extractIncDecIdentifier("y--;"));
    }

    @Test
    void stripEnclosingParensRemovesOuterLayers() throws Exception {
        assertEquals("x", stripEnclosingParens("(((x)))"));
    }

    @Test
    void simpleIdentifierCheckWorks() throws Exception {
        assertTrue(isSimpleIdentifier("abc123"));
        assertFalse(isSimpleIdentifier("a.b"));
    }

    @Test
    void looksLikeReferenceTypeWorks() throws Exception {
        assertTrue(looksLikeReferenceType("String"));
        assertFalse(looksLikeReferenceType("int"));
    }
}