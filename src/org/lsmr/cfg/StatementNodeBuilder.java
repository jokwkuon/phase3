package org.lsmr.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.lsmr.cfg.Edge.EdgeLabel;

import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ArgumentsContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ArrayCreatorRestContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ArrayInitializerContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.AssignmentOperatorContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.BasicTypeContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.BlockContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.BlockStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.BreakStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.CatchClauseContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.CatchesContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ClassBodyContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ClassBodyDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ClassCreatorRestContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ClassDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ClassOrInterfaceDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.CompilationUnitContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ConstantDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ConstantDeclaratorContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ConstantExpressionContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ConstructorBodyContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ConstructorDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ContinueStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.CreatorContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.DimsContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.DoStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ElseClauseContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.EmptyDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.EmptyStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ExplicitConstructorInvocationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.Expression1Context;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.Expression1RestContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.Expression2Context;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.Expression2RestContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.Expression3Context;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ExpressionContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ExpressionStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ExtendsInterfacesContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.FieldDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.FinallyClauseContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ForInitContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ForStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ForUpdateContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.FormalParameterContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.FormalParametersContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.IdentifierContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.IdentifierSuffixContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.IfStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ImportDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InfixOpContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InitializerContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InnerCreatorContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InterfaceBodyContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InterfaceBodyDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InterfaceDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InterfaceMemberDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.InterfaceMethodDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.LabeledStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.LiteralContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.LocalVariableDeclarationStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.MemberDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.MethodDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ModifierContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ParenthesizedExpressionContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.PostfixOpContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.PrefixOpContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.PrimaryContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.QualifiedIdentifierContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.QualifiedIdentifiersContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ResultContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ReturnStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SelectorContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.StatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.StatementExpressionContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.StaticInitializerContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SuperSuffixContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SuperclassContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SuperinterfacesContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SwitchBlockStatementGroupContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SwitchLabelContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SwitchStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.SynchronizedStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.ThrowStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.Throws_Context;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.TryStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.TypeContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.TypeDeclarationContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.TypeListContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.VariableDeclaratorContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.VariableDeclaratorIdContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.VariableDeclaratorsContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.VariableInitializerContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParser.WhileStatementContext;
import ca.ucalgary.cpsc499_02.w26.Java1_2ANTLRParserVisitor;

/**
 * This class will visit the nodes in a parse tree to: (1) find individual
 * methods to process, (2) process each method to construct the CFG nodes for
 * it, (3) determine the edge locations for each node, and (4) add the edges to
 * each graph.
 * <p>
 * Given the root of an AST and an instance of this class, call
 * {@code root.accept(instance}} to visit the pertinent nodes therein to
 * construct a set of CFGs.
 * <p>
 * At the end of the complete visit, use the {@link #getCFGs() getCFGs()} method
 * to obtain the constructed CFGs.
 * <p>
 * The resulting CFG will be based on statements, ignoring finer-grained
 * considerations.
 */
public class StatementNodeBuilder implements Java1_2ANTLRParserVisitor<WorkingGraph> {
	private Map<RuleContext, ControlFlowGraph> graphMap = new HashMap<>();
	private Stack<String> nameStack = new Stack<>();
	private int nodeCounter = 0;

	private String getCurrentName() {
		StringBuilder sb = new StringBuilder();

		for(String name : nameStack) {
			if(sb.length() == 0)
				sb.append(name);
			else {
				sb.append('.');
				sb.append(name);
			}
		}

		return sb.toString();
	}

	/**
	 * Obtains a list of the CFGs that have been constructed from the visit. It
	 * makes most sense to call this method once the visit has been completed.
	 * 
	 * @return An unmodifiable list of the constructed CFGs for the visited AST.
	 */
	public List<ControlFlowGraph> getCFGs() {
		return Collections.unmodifiableList(new ArrayList<>(graphMap.values()));
	}

	@Override
	public WorkingGraph visit(ParseTree arg0) {
		return arg0.accept(this);
	}

	@Override
	public WorkingGraph visitChildren(RuleNode arg0) {
		int childCount = arg0.getChildCount();
		WorkingGraph previousResult = null, currentResult = null;

		for(int i = 0; i < childCount; i++) {
			currentResult = visit(arg0.getChild(i));

			if(currentResult != null)
				if(previousResult != null)
					previousResult.connect(currentResult);
				else
					previousResult = currentResult;
		}

		return previousResult;
	}

	@Override
	public WorkingGraph visitErrorNode(ErrorNode arg0) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitTerminal(TerminalNode arg0) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitIdentifier(IdentifierContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitQualifiedIdentifier(QualifiedIdentifierContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitLiteral(LiteralContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitType(TypeContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitBasicType(BasicTypeContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExpression(ExpressionContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitAssignmentOperator(AssignmentOperatorContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExpression1(Expression1Context ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExpression1Rest(Expression1RestContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExpression2(Expression2Context ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExpression2Rest(Expression2RestContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitInfixOp(InfixOpContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExpression3(Expression3Context ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitPrefixOp(PrefixOpContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitPostfixOp(PostfixOpContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitPrimary(PrimaryContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitIdentifierSuffix(IdentifierSuffixContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitSelector(SelectorContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitSuperSuffix(SuperSuffixContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitArguments(ArgumentsContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitCreator(CreatorContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitInnerCreator(InnerCreatorContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitArrayCreatorRest(ArrayCreatorRestContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitClassCreatorRest(ClassCreatorRestContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitArrayInitializer(ArrayInitializerContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitVariableInitializer(VariableInitializerContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitBlock(BlockContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": block");
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		List<BlockStatementContext> statements = ctx.blockStatement();

		for(int i = 0, size = statements.size(); i < size; i++)
			s.connect(visitBlockStatement(statements.get(i)));

		return s;
	}

	@SuppressWarnings("javadoc")
	public WorkingGraph visitTryBlock(BlockContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": block");
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		List<BlockStatementContext> statements = ctx.blockStatement();

		for(int i = 0, size = statements.size(); i < size; i++) {
			WorkingGraph bs = visitBlockStatement(statements.get(i));

			bs.edges.add(g.buildEdge(bs.node, null, EdgeLabel.THROWN));
			s.connect(bs);
		}

		return s;
	}

	@SuppressWarnings("javadoc")
	public WorkingGraph visitFinallyBlock(BlockContext ctx, boolean delayedThrown) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": block");
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		if(delayedThrown)
			s.edges.add(g.buildEdge(s.node, null, EdgeLabel.THROWN_DELAYED));

		List<BlockStatementContext> statements = ctx.blockStatement();

		for(int i = 0, size = statements.size(); i < size; i++) {
			WorkingGraph bs = visitBlockStatement(statements.get(i));

			if(delayedThrown)
				if(i == size - 1)
					bs.edges.add(g.buildEdge(bs.node, null, EdgeLabel.THROWN));
				else
					bs.edges.add(g.buildEdge(bs.node, null, EdgeLabel.THROWN_DELAYED));

			s.connect(bs);
		}

		return s;
	}

	@Override
	public WorkingGraph visitBlockStatement(BlockStatementContext ctx) {
		return visitChildren(ctx);
	}

	ControlFlowGraph getGraph(RuleContext ctx) {
		RuleContext current = ctx;

		while(current != null && !(current instanceof MemberDeclarationContext)
			&& !(current instanceof ClassBodyDeclarationContext) && !(current instanceof InterfaceBodyContext))
			current = current.getParent();

		if(current != null)
			return graphMap.get(current);

		return null;
	}

	@Override
	public WorkingGraph visitLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitBreakStatement(BreakStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));

		IdentifierContext ic = ctx.identifier();
		String label = "";

		if(ic != null)
			label = ic.getText().intern();

		List<Edge> list = s.breakEdges.get(label);

		if(list == null) {
			list = new ArrayList<>();
			s.breakEdges.put(label, list);
		}

		list.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitContinueStatement(ContinueStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));

		IdentifierContext ic = ctx.identifier();
		String label = "";

		if(ic != null)
			label = ic.getText();

		List<Edge> list = s.continueEdges.get(label);

		if(list == null) {
			list = new ArrayList<>();
			s.continueEdges.put(label, list);
		}

		list.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitDoStatement(DoStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": do");

		WorkingGraph doBody = visitStatement(ctx.statement());

		g.buildEdge(s.node, doBody.node, EdgeLabel.BLANK);

		Node whileExpression = g
			.buildNode(nodeCounter++ + ": while " + ctx.parenthesizedExpression().accept(new TreePrinter()) + "; ");

		g.buildEdge(whileExpression, s.node, EdgeLabel.TRUE);

		for(Edge edge : doBody.edges) {
			if(edge.target() == null)
				if(edge.label() == EdgeLabel.THROWN)
					s.edges.add(edge);
				else
					edge.setTarget(whileExpression);
		}

		// Labelled edges will be handled by labelled statement

		List<Edge> nonLabelledBreakEdges = doBody.breakEdges.get("");

		if(nonLabelledBreakEdges != null) {
			s.edges.addAll(nonLabelledBreakEdges);
			s.breakEdges.remove("");
		}

		List<Edge> nonLabelledContinueEdges = doBody.continueEdges.get("");

		if(nonLabelledContinueEdges != null) {
			for(Edge edge : nonLabelledContinueEdges) {
				edge.setTarget(s.node);
				s.edges.add(edge);
			}

			s.continueEdges.remove("");
		}

		s.edges.add(g.buildEdge(whileExpression, null, EdgeLabel.FALSE));
		// s.connect(doBody);

		// Copy labelled break and continue edges

		for(String breakLabel : doBody.breakEdges.keySet()) {
			if(s.breakEdges.containsKey(breakLabel)) {
				List<Edge> edgeList = s.breakEdges.get(breakLabel);

				edgeList.addAll(doBody.breakEdges.get(breakLabel));
			}
			else
				s.breakEdges.put(breakLabel, doBody.breakEdges.get(breakLabel));
		}

		for(String continueLabel : doBody.continueEdges.keySet()) {
			if(s.continueEdges.containsKey(continueLabel)) {
				List<Edge> edgeList = s.continueEdges.get(continueLabel);

				edgeList.addAll(doBody.continueEdges.get(continueLabel));
			}
			else
				s.continueEdges.put(continueLabel, doBody.continueEdges.get(continueLabel));
		}

		return s;
	}

	@Override
	public WorkingGraph visitIfStatement(IfStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": if " + ctx.parenthesizedExpression().accept(new TreePrinter()));

		WorkingGraph trueNode = visitStatement(ctx.statement());

		s.edges.add(g.buildEdge(s.node, trueNode.node, EdgeLabel.TRUE));
		s.edges = trueNode.edges;

		ElseClauseContext elseClause = ctx.elseClause();

		if(elseClause != null) {
			WorkingGraph elseNode = visitElseClause(elseClause);

			s.edges.add(g.buildEdge(s.node, elseNode.node, EdgeLabel.FALSE));
			s.edges.addAll(elseNode.edges);
			s.breakEdges.putAll(elseNode.breakEdges);
			s.continueEdges.putAll(elseNode.continueEdges);
		}
		else
			s.edges.add(g.buildEdge(s.node, null, EdgeLabel.FALSE));

		return s;
	}

	@Override
	public WorkingGraph visitLabeledStatement(LabeledStatementContext ctx) {
		String label = ctx.identifier().getText();
		StatementContext sc = ctx.statement();
		WorkingGraph s = visitStatement(sc);
		List<Edge> breakEdges = s.breakEdges.get(label);

		if(breakEdges != null) {
			s.breakEdges.remove(label);
			s.edges.addAll(breakEdges);
		}

		List<Edge> continueEdges = s.continueEdges.get(label);

		if(continueEdges != null) {
			s.continueEdges.remove(label);

			if(sc.forStatement() != null || sc.whileStatement() != null || sc.doStatement() != null)
				for(Edge continueEdge : continueEdges)
					continueEdge.setTarget(s.node);
		}

		return s;
	}

	@Override
	public WorkingGraph visitTryStatement(TryStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		BlockContext tryBlock = ctx.block();
		CatchesContext catches = ctx.catches();
		List<CatchClauseContext> catchClauses = catches == null ? new ArrayList<>() : catches.catchClause();
		FinallyClauseContext finallyClause = ctx.finallyClause();

		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": try");
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		WorkingGraph tryBody = visitTryBlock(tryBlock);

		s.connect(tryBody);

		for(CatchClauseContext catch_ : catchClauses) {
			WorkingGraph catchClause = visitCatchClause(catch_);

			for(Edge edge : s.edges) {
				if(edge.label() == EdgeLabel.THROWN && edge.target() == null)
					edge.setTarget(catchClause.node);
			}

			s.edges.addAll(catchClause.edges);
		}

		if(finallyClause != null) {
			boolean delayedThrown = false;

			for(Edge edge : s.edges)
				if(edge.label() == EdgeLabel.THROWN && edge.target() == null) {
					delayedThrown = true;
					break;
				}

			WorkingGraph finallyBody = visitFinallyClause(finallyClause, delayedThrown);

			for(Edge edge : s.edges)
				if(edge.target() == null)
					edge.setTarget(finallyBody.node);

			s.edges.addAll(finallyBody.edges);
		}

		return s;
	}

	@Override
	public WorkingGraph visitForStatement(ForStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();
		ForInitContext forInit = ctx.forInit();
		ExpressionContext expression = ctx.expression();
		ForUpdateContext forUpdate = ctx.forUpdate();
		StringBuilder sb = new StringBuilder();

		sb.append(nodeCounter++);
		sb.append(": for ( ");
		sb.append(forInit == null ? "; " : forInit.accept(new TreePrinter()) + "; ");
		sb.append(expression == null ? "; " : expression.accept(new TreePrinter()) + "; ");

		if(forUpdate != null)
			sb.append(forUpdate.accept(new TreePrinter()));

		sb.append(")");
		s.node = g.buildNode(sb.toString());

		WorkingGraph forBody = visitStatement(ctx.statement());

		s.connect(forBody);
		s.edges.add(g.buildEdge(s.node, forBody.node, EdgeLabel.TRUE));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.FALSE));

		for(Edge edge : forBody.edges) {
			if(edge.target() == null && edge.label() != EdgeLabel.THROWN)
				edge.setTarget(s.node);
		}
		// s.edges.addAll(forBody.edges);

		// Labelled edges will be handled by labelled statement

		List<Edge> nonLabelledBreakEdges = forBody.breakEdges.get("");

		if(nonLabelledBreakEdges != null) {
			s.edges.addAll(nonLabelledBreakEdges);
			s.breakEdges.remove("");
		}

		List<Edge> nonLabelledContinueEdges = forBody.continueEdges.get("");

		if(nonLabelledContinueEdges != null) {
			for(Edge edge : nonLabelledContinueEdges) {
				edge.setTarget(s.node);
				s.edges.add(edge);
			}

			s.continueEdges.remove("");
		}

		// Copy labelled break and continue edges

		for(String breakLabel : forBody.breakEdges.keySet()) {
			if(s.breakEdges.containsKey(breakLabel)) {
				List<Edge> edgeList = s.breakEdges.get(breakLabel);

				edgeList.addAll(forBody.breakEdges.get(breakLabel));
			}
			else
				s.breakEdges.put(breakLabel, forBody.breakEdges.get(breakLabel));
		}

		for(String continueLabel : forBody.continueEdges.keySet()) {
			if(s.continueEdges.containsKey(continueLabel)) {
				List<Edge> edgeList = s.continueEdges.get(continueLabel);

				edgeList.addAll(forBody.continueEdges.get(continueLabel));
			}
			else
				s.continueEdges.put(continueLabel, forBody.continueEdges.get(continueLabel));
		}

		return s;
	}

	@Override
	public WorkingGraph visitWhileStatement(WhileStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();
		ParenthesizedExpressionContext condition = ctx.parenthesizedExpression();

		s.node = g.buildNode(nodeCounter++ + ": while " + condition.accept(new TreePrinter()));

		WorkingGraph whileBody = visitStatement(ctx.statement());

		s.connect(whileBody);
		g.buildEdge(s.node, whileBody.node, EdgeLabel.TRUE);
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.FALSE));

		for(Edge edge : whileBody.edges) {
			if(edge.target() == null)
				if(edge.label() == EdgeLabel.THROWN)
					s.edges.add(edge);
				else
					edge.setTarget(s.node);
		}

		// Labelled edges will be handled by labelled statement

		List<Edge> nonLabelledBreakEdges = whileBody.breakEdges.get("");

		if(nonLabelledBreakEdges != null) {
			s.edges.addAll(nonLabelledBreakEdges);
			s.breakEdges.remove("");
		}

		List<Edge> nonLabelledContinueEdges = whileBody.continueEdges.get("");

		if(nonLabelledContinueEdges != null) {
			for(Edge edge : nonLabelledContinueEdges) {
				edge.setTarget(s.node);
				s.edges.add(edge);
			}

			s.continueEdges.remove("");
		}

		// Copy labelled break and continue edges

		for(String breakLabel : whileBody.breakEdges.keySet()) {
			if(s.breakEdges.containsKey(breakLabel)) {
				List<Edge> edgeList = s.breakEdges.get(breakLabel);

				edgeList.addAll(whileBody.breakEdges.get(breakLabel));
			}
			else
				s.breakEdges.put(breakLabel, whileBody.breakEdges.get(breakLabel));
		}

		for(String continueLabel : whileBody.continueEdges.keySet()) {
			if(s.continueEdges.containsKey(continueLabel)) {
				List<Edge> edgeList = s.continueEdges.get(continueLabel);

				edgeList.addAll(whileBody.continueEdges.get(continueLabel));
			}
			else
				s.continueEdges.put(continueLabel, whileBody.continueEdges.get(continueLabel));
		}

		return s;
	}

	@Override
	public WorkingGraph visitSwitchStatement(SwitchStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();
		List<SwitchBlockStatementGroupContext> statementGroups = ctx.switchBlockStatementGroup();

		s.node = g.buildNode(nodeCounter++ + ": switch " + ctx.parenthesizedExpression().accept(new TreePrinter()));

		if(!statementGroups.isEmpty())
			for(SwitchBlockStatementGroupContext group : statementGroups) {
				List<SwitchLabelContext> labels = group.switchLabel();
				List<BlockStatementContext> statements = group.blockStatement();

				WorkingGraph first = visitBlockStatement(statements.get(0));
				TreePrinter printer = new TreePrinter();

				for(SwitchLabelContext label : labels)
					s.edges.add(g.buildEdge(s.node, first.node, EdgeLabel.CASE, label.accept(printer)));

				for(int i = 1, size = statements.size(); i < size; i++) {
					WorkingGraph later = visitBlockStatement(statements.get(i));

					first.connect(later);
				}

				s.connect(first);
			}
		else
			s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		if(s.breakEdges.containsKey("")) {
			List<Edge> breakEdges = s.breakEdges.get("");

			s.edges.addAll(breakEdges);
			s.breakEdges.remove("");
		}

		List<SwitchLabelContext> extraLabels = ctx.switchLabel();

		for(SwitchLabelContext label : extraLabels)
			s.edges.add(g.buildEdge(s.node, null, EdgeLabel.CASE, label.accept(new TreePrinter())));

		return s;
	}

	@Override
	public WorkingGraph visitSynchronizedStatement(SynchronizedStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g
			.buildNode(nodeCounter++ + ": synchronized " + ctx.parenthesizedExpression().accept(new TreePrinter()));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		WorkingGraph block = visitBlock(ctx.block());

		s.connect(block);

		return s;
	}

	@Override
	public WorkingGraph visitReturnStatement(ReturnStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitThrowStatement(ThrowStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.THROWN));

		return s;
	}

	@Override
	public WorkingGraph visitEmptyStatement(EmptyStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": *EMPTY* ; ");
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitExpressionStatement(ExpressionStatementContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitStatementExpression(StatementExpressionContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitConstantExpression(ConstantExpressionContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitCatches(CatchesContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitCatchClause(CatchClauseContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g
			.buildNode(nodeCounter++ + ": catch ( " + ctx.formalParameter().type().accept(new TreePrinter()) + ")");

		WorkingGraph body = visitBlock(ctx.block());

		s.edges.add(g.buildEdge(s.node, body.node, EdgeLabel.CAUGHT));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.THROWN));
		s.edges.addAll(body.edges);

		return s;
	}

	@Override
	public WorkingGraph visitSwitchBlockStatementGroup(SwitchBlockStatementGroupContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitSwitchLabel(SwitchLabelContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitForInit(ForInitContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitForUpdate(ForUpdateContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitModifier(ModifierContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitVariableDeclarators(VariableDeclaratorsContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitVariableDeclarator(VariableDeclaratorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitVariableDeclaratorId(VariableDeclaratorIdContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitConstantDeclarator(ConstantDeclaratorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitCompilationUnit(CompilationUnitContext ctx) {
		QualifiedIdentifierContext pkg = ctx.qualifiedIdentifier();

		if(pkg != null)
			nameStack.push(pkg.accept(new TreePrinter()));

		for(TypeDeclarationContext typeDeclaration : ctx.typeDeclaration())
			typeDeclaration.accept(this);

		return null;
	}

	@Override
	public WorkingGraph visitImportDeclaration(ImportDeclarationContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitTypeDeclaration(TypeDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitClassOrInterfaceDeclaration(ClassOrInterfaceDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitClassDeclaration(ClassDeclarationContext ctx) {
		nameStack.push(ctx.identifier().getText());
		visitClassBody(ctx.classBody());

		return null;
	}

	@Override
	public WorkingGraph visitInterfaceDeclaration(InterfaceDeclarationContext ctx) {
		// Possible nested classes
		nameStack.push(ctx.identifier().getText());
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitTypeList(TypeListContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitClassBody(ClassBodyContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitInterfaceBody(InterfaceBodyContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitClassBodyDeclaration(ClassBodyDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitMemberDeclaration(MemberDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	private String getSignature(String identifier, FormalParametersContext ctx) {
		StringBuilder sb = new StringBuilder();

		sb.append(identifier);
		sb.append("(");

		List<FormalParameterContext> formals = ctx.formalParameter();
		int size = formals.size();

		if(size > 0) {
			sb.append(formals.get(0).type().accept(new TreePrinter()));

			for(int i = 1; i < size; i++) {
				sb.append(", ");
				sb.append(formals.get(i).type().accept(new TreePrinter()));
			}
		}

		sb.append(")");

		return sb.toString();
	}

	@Override
	public WorkingGraph visitMethodDeclaration(MethodDeclarationContext ctx) {
		if(ctx.block() != null) {
			String identifier = ctx.identifier().getText();
			String signature = getSignature(identifier, ctx.formalParameters());
			ControlFlowGraph g = new ControlFlowGraph(getCurrentName() + "." + signature);

			graphMap.put(ctx.parent, g);

			WorkingGraph s = visitBlock(ctx.block());

			g.buildEdge(g.entry, s.node, EdgeLabel.BLANK);

			for(Edge edge : g.edges()) {
				EdgeLabel label = edge.label();

				if(edge.target() == null) {
					if(label == EdgeLabel.THROWN)
						edge.setTarget(g.abruptExit);
					else
						edge.setTarget(g.normalExit);
				}
			}

			for(Edge edge : g.edges())
				if(edge.target() == null)
					throw new IllegalStateException(
						"The edge " + edge + " should not have a null target at this point.");
		}

		return null;
	}

	@Override
	public WorkingGraph visitInterfaceBodyDeclaration(InterfaceBodyDeclarationContext ctx) {
		// Potentially nested classes
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitFormalParameters(FormalParametersContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitFormalParameter(FormalParameterContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitDims(DimsContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitStatement(StatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitSuperclass(SuperclassContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitSuperinterfaces(SuperinterfacesContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitExtendsInterfaces(ExtendsInterfacesContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitEmptyDeclaration(EmptyDeclarationContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitInitializer(InitializerContext ctx) {
		ControlFlowGraph g = new ControlFlowGraph(getCurrentName() + ".<init>");

		graphMap.put(ctx.parent, g);

		WorkingGraph s = visitBlock(ctx.block());

		g.buildEdge(g.entry, s.node, EdgeLabel.BLANK);

		for(Edge edge : s.edges) {
			EdgeLabel label = edge.label();

			if(edge.target() == null) {
				if(label == EdgeLabel.THROWN)
					edge.setTarget(g.abruptExit);
				else
					edge.setTarget(g.normalExit);
			}
		}

		if(!s.breakEdges.isEmpty())
			throw new IllegalStateException("WorkingGraph still contains break edges: " + s);

		if(!s.continueEdges.isEmpty())
			throw new IllegalStateException("WorkingGraph still contains continue edges: " + s);

		return null;
	}

	@Override
	public WorkingGraph visitStaticInitializer(StaticInitializerContext ctx) {
		ControlFlowGraph g = new ControlFlowGraph(getCurrentName() + ".<clinit>");

		graphMap.put(ctx.parent, g);

		WorkingGraph s = visitBlock(ctx.block());

		g.buildEdge(g.entry, s.node, EdgeLabel.BLANK);

		for(Edge edge : s.edges) {
			EdgeLabel label = edge.label();

			if(edge.target() == null) {
				if(label == EdgeLabel.THROWN)
					edge.setTarget(g.abruptExit);
				else
					edge.setTarget(g.normalExit);
			}
		}

		if(!s.breakEdges.isEmpty())
			throw new IllegalStateException("WorkingGraph still contains break edges: " + s);

		if(!s.continueEdges.isEmpty())
			throw new IllegalStateException("WorkingGraph still contains continue edges: " + s);

		return null;
	}

	@Override
	public WorkingGraph visitResult(ResultContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitThrows_(Throws_Context ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitFieldDeclaration(FieldDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitConstructorDeclaration(ConstructorDeclarationContext ctx) {
		String identifier = ctx.identifier().getText();
		String signature = getSignature(identifier, ctx.formalParameters());
		ControlFlowGraph g = new ControlFlowGraph(getCurrentName() + "." + signature);

		graphMap.put(ctx.parent, g);

		WorkingGraph s = visitConstructorBody(ctx.constructorBody());

		g.buildEdge(g.entry, s.node, EdgeLabel.BLANK);

		for(Edge edge : s.edges) {
			EdgeLabel label = edge.label();

			if(edge.target() == null) {
				if(label == EdgeLabel.THROWN)
					edge.setTarget(g.abruptExit);
				else
					edge.setTarget(g.normalExit);
			}
		}

		if(!s.breakEdges.isEmpty())
			throw new IllegalStateException("WorkingGraph still contains break edges: " + s);

		if(!s.continueEdges.isEmpty())
			throw new IllegalStateException("WorkingGraph still contains continue edges: " + s);

		return null;
	}

	@Override
	public WorkingGraph visitConstructorBody(ConstructorBodyContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": body");
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		WorkingGraph body = null;
		ExplicitConstructorInvocationContext eci = ctx.explicitConstructorInvocation();

		if(eci != null) {
			body = visitExplicitConstructorInvocation(eci);
			s.connect(body);
		}

		for(BlockStatementContext statement : ctx.blockStatement()) {
			body = visitBlockStatement(statement);
			s.connect(body);
		}

		return s;
	}

	@Override
	public WorkingGraph visitExplicitConstructorInvocation(ExplicitConstructorInvocationContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": " + ctx.accept(new TreePrinter()));
		s.edges.add(g.buildEdge(s.node, null, EdgeLabel.BLANK));

		return s;
	}

	@Override
	public WorkingGraph visitInterfaceMemberDeclaration(InterfaceMemberDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public WorkingGraph visitInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitConstantDeclaration(ConstantDeclarationContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitQualifiedIdentifiers(QualifiedIdentifiersContext ctx) {
		// Ignore
		return null;
	}

	@Override
	public WorkingGraph visitElseClause(ElseClauseContext ctx) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": else");

		WorkingGraph body = visitStatement(ctx.statement());

		s.edges.add(g.buildEdge(s.node, body.node, EdgeLabel.BLANK));
		s.connect(body);

		return s;
	}

	@Override
	public WorkingGraph visitFinallyClause(FinallyClauseContext ctx) {
		// Ignore
		return null;
	}

	@SuppressWarnings("javadoc")
	public WorkingGraph visitFinallyClause(FinallyClauseContext ctx, boolean delayedThrown) {
		ControlFlowGraph g = getGraph(ctx);
		WorkingGraph s = new WorkingGraph();

		s.node = g.buildNode(nodeCounter++ + ": finally");

		WorkingGraph body = visitFinallyBlock(ctx.block(), delayedThrown);

		s.edges.add(g.buildEdge(s.node, body.node, EdgeLabel.BLANK));

		if(delayedThrown)
			s.edges.add(g.buildEdge(s.node, body.node, EdgeLabel.THROWN_DELAYED));

		return s;
	}
}
