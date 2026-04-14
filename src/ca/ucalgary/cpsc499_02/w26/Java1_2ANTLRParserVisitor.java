// Generated from Java1_2ANTLRParser.g4 by ANTLR 4.13.2

	package ca.ucalgary.cpsc499_02.w26;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Java1_2ANTLRParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Java1_2ANTLRParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(Java1_2ANTLRParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(Java1_2ANTLRParser.QualifiedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(Java1_2ANTLRParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(Java1_2ANTLRParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#basicType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicType(Java1_2ANTLRParser.BasicTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#dims}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDims(Java1_2ANTLRParser.DimsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(Java1_2ANTLRParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(Java1_2ANTLRParser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expression1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression1(Java1_2ANTLRParser.Expression1Context ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expression1Rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression1Rest(Java1_2ANTLRParser.Expression1RestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expression2}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression2(Java1_2ANTLRParser.Expression2Context ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expression2Rest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression2Rest(Java1_2ANTLRParser.Expression2RestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#infixOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixOp(Java1_2ANTLRParser.InfixOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expression3}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression3(Java1_2ANTLRParser.Expression3Context ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#prefixOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixOp(Java1_2ANTLRParser.PrefixOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#postfixOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixOp(Java1_2ANTLRParser.PostfixOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(Java1_2ANTLRParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#identifierSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierSuffix(Java1_2ANTLRParser.IdentifierSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(Java1_2ANTLRParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#superSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperSuffix(Java1_2ANTLRParser.SuperSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(Java1_2ANTLRParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(Java1_2ANTLRParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#innerCreator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerCreator(Java1_2ANTLRParser.InnerCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorRest(Java1_2ANTLRParser.ArrayCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#classCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassCreatorRest(Java1_2ANTLRParser.ClassCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#arrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInitializer(Java1_2ANTLRParser.ArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(Java1_2ANTLRParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#parenthesizedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(Java1_2ANTLRParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(Java1_2ANTLRParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(Java1_2ANTLRParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclarationStatement(Java1_2ANTLRParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(Java1_2ANTLRParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(Java1_2ANTLRParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#elseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseClause(Java1_2ANTLRParser.ElseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(Java1_2ANTLRParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(Java1_2ANTLRParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#doStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoStatement(Java1_2ANTLRParser.DoStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#tryStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryStatement(Java1_2ANTLRParser.TryStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#switchStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchStatement(Java1_2ANTLRParser.SwitchStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#synchronizedStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSynchronizedStatement(Java1_2ANTLRParser.SynchronizedStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(Java1_2ANTLRParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#throwStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowStatement(Java1_2ANTLRParser.ThrowStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#breakStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStatement(Java1_2ANTLRParser.BreakStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#continueStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueStatement(Java1_2ANTLRParser.ContinueStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(Java1_2ANTLRParser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(Java1_2ANTLRParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#labeledStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabeledStatement(Java1_2ANTLRParser.LabeledStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#statementExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementExpression(Java1_2ANTLRParser.StatementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(Java1_2ANTLRParser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#catches}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatches(Java1_2ANTLRParser.CatchesContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(Java1_2ANTLRParser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#finallyClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinallyClause(Java1_2ANTLRParser.FinallyClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBlockStatementGroup(Java1_2ANTLRParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#switchLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchLabel(Java1_2ANTLRParser.SwitchLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(Java1_2ANTLRParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(Java1_2ANTLRParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier(Java1_2ANTLRParser.ModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#variableDeclarators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarators(Java1_2ANTLRParser.VariableDeclaratorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(Java1_2ANTLRParser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaratorId(Java1_2ANTLRParser.VariableDeclaratorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#constantDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclarator(Java1_2ANTLRParser.ConstantDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(Java1_2ANTLRParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(Java1_2ANTLRParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(Java1_2ANTLRParser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#classOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceDeclaration(Java1_2ANTLRParser.ClassOrInterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(Java1_2ANTLRParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#superclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperclass(Java1_2ANTLRParser.SuperclassContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#superinterfaces}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperinterfaces(Java1_2ANTLRParser.SuperinterfacesContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDeclaration(Java1_2ANTLRParser.InterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#extendsInterfaces}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtendsInterfaces(Java1_2ANTLRParser.ExtendsInterfacesContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(Java1_2ANTLRParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(Java1_2ANTLRParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#interfaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBody(Java1_2ANTLRParser.InterfaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyDeclaration(Java1_2ANTLRParser.ClassBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#emptyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyDeclaration(Java1_2ANTLRParser.EmptyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializer(Java1_2ANTLRParser.InitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#staticInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticInitializer(Java1_2ANTLRParser.StaticInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#memberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaration(Java1_2ANTLRParser.MemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(Java1_2ANTLRParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#result}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult(Java1_2ANTLRParser.ResultContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#throws_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrows_(Java1_2ANTLRParser.Throws_Context ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(Java1_2ANTLRParser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#constructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaration(Java1_2ANTLRParser.ConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#constructorBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorBody(Java1_2ANTLRParser.ConstructorBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#explicitConstructorInvocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitConstructorInvocation(Java1_2ANTLRParser.ExplicitConstructorInvocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBodyDeclaration(Java1_2ANTLRParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMemberDeclaration(Java1_2ANTLRParser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethodDeclaration(Java1_2ANTLRParser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#constantDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclaration(Java1_2ANTLRParser.ConstantDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#qualifiedIdentifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifiers(Java1_2ANTLRParser.QualifiedIdentifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(Java1_2ANTLRParser.FormalParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java1_2ANTLRParser#formalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameter(Java1_2ANTLRParser.FormalParameterContext ctx);
}