package org.lsmr.cfg;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

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
 * This class will visit the nodes in a parse tree to print the resulting code.
 */
public class TreePrinter implements Java1_2ANTLRParserVisitor<String> {
	@Override
	public String visit(ParseTree arg0) {
		return arg0.accept(this);
	}

	@Override
	public String visitChildren(RuleNode arg0) {
		StringBuilder sb = new StringBuilder();
		int childCount = arg0.getChildCount();

		for(int i = 0; i < childCount; i++) {
			sb.append(visit(arg0.getChild(i)));
			
			if(sb.charAt(sb.length() - 1) != ' ')
				sb.append(" ");
		}

		return sb.toString();
	}

	@Override
	public String visitErrorNode(ErrorNode arg0) {
		return null;
	}

	@Override
	public String visitTerminal(TerminalNode arg0) {
		return arg0.getText();
	}

	@Override
	public String visitIdentifier(IdentifierContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitQualifiedIdentifier(QualifiedIdentifierContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitLiteral(LiteralContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitType(TypeContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBasicType(BasicTypeContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpression(ExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitAssignmentOperator(AssignmentOperatorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpression1(Expression1Context ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpression1Rest(Expression1RestContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpression2(Expression2Context ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpression2Rest(Expression2RestContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInfixOp(InfixOpContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpression3(Expression3Context ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitPrefixOp(PrefixOpContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitPostfixOp(PostfixOpContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitPrimary(PrimaryContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitIdentifierSuffix(IdentifierSuffixContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSelector(SelectorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSuperSuffix(SuperSuffixContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitArguments(ArgumentsContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitCreator(CreatorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInnerCreator(InnerCreatorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitArrayCreatorRest(ArrayCreatorRestContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitClassCreatorRest(ClassCreatorRestContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitArrayInitializer(ArrayInitializerContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitVariableInitializer(VariableInitializerContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBlock(BlockContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBlockStatement(BlockStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitLocalVariableDeclarationStatement(LocalVariableDeclarationStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitBreakStatement(BreakStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitContinueStatement(ContinueStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitDoStatement(DoStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitIfStatement(IfStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitTryStatement(TryStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitForStatement(ForStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitWhileStatement(WhileStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSwitchStatement(SwitchStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSynchronizedStatement(SynchronizedStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitReturnStatement(ReturnStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitThrowStatement(ThrowStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitEmptyStatement(EmptyStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExpressionStatement(ExpressionStatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitStatementExpression(StatementExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitConstantExpression(ConstantExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitCatches(CatchesContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitCatchClause(CatchClauseContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSwitchBlockStatementGroup(SwitchBlockStatementGroupContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSwitchLabel(SwitchLabelContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitForInit(ForInitContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitForUpdate(ForUpdateContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitModifier(ModifierContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitVariableDeclarators(VariableDeclaratorsContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitVariableDeclarator(VariableDeclaratorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitVariableDeclaratorId(VariableDeclaratorIdContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitConstantDeclarator(ConstantDeclaratorContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitCompilationUnit(CompilationUnitContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitImportDeclaration(ImportDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitTypeDeclaration(TypeDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitClassOrInterfaceDeclaration(ClassOrInterfaceDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitClassDeclaration(ClassDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInterfaceDeclaration(InterfaceDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitTypeList(TypeListContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitClassBody(ClassBodyContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInterfaceBody(InterfaceBodyContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitClassBodyDeclaration(ClassBodyDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitMemberDeclaration(MemberDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitMethodDeclaration(MethodDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInterfaceBodyDeclaration(InterfaceBodyDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFormalParameters(FormalParametersContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFormalParameter(FormalParameterContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitDims(DimsContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitStatement(StatementContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSuperclass(SuperclassContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitSuperinterfaces(SuperinterfacesContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExtendsInterfaces(ExtendsInterfacesContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitEmptyDeclaration(EmptyDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInitializer(InitializerContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitStaticInitializer(StaticInitializerContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitResult(ResultContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitThrows_(Throws_Context ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFieldDeclaration(FieldDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitConstructorDeclaration(ConstructorDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitConstructorBody(ConstructorBodyContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitExplicitConstructorInvocation(ExplicitConstructorInvocationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInterfaceMemberDeclaration(InterfaceMemberDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitInterfaceMethodDeclaration(InterfaceMethodDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitConstantDeclaration(ConstantDeclarationContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitQualifiedIdentifiers(QualifiedIdentifiersContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitElseClause(ElseClauseContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitFinallyClause(FinallyClauseContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	public String visitLabeledStatement(LabeledStatementContext ctx) {
		return visitChildren(ctx);
	}
}
