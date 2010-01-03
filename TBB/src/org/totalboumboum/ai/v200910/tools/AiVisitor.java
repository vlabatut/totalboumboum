package org.totalboumboum.ai.v200910.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.List;

import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class AiVisitor extends VoidVisitorAdapter<Object>
{
	private static String CHECK_INTERRUPTION = "checkInterruption";
	
	@Override
    public void visit(MethodDeclaration n, Object arg) 
	{	if(n.getJavaDoc() != null)
		{	n.getJavaDoc().accept(this, arg);
        }
        if(n.getAnnotations() != null)
        {	for(AnnotationExpr a : n.getAnnotations())
        	{	a.accept(this, arg);
            }
        }
        if(n.getTypeParameters() != null)
        {	for(TypeParameter t : n.getTypeParameters())
        	{	t.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if(n.getParameters() != null)
        {	for (Parameter p : n.getParameters())
        	{	p.accept(this, arg);
            }
        }
        if(n.getThrows() != null)
        {	for(NameExpr name : n.getThrows())
        	{	name.accept(this, arg);
            }
        }
        if(n.getBody() != null)
        {	// init
        	String name = n.getName();
        	BlockStmt block = n.getBody();
        	System.out.println("Analyse de la m�thode "+name);
        	
        	List<Statement> statements = block.getStmts();
        	if(!statements.isEmpty())
        	{	Statement firstStatement = statements.get(0);
        		checkBlock(firstStatement);
        	}
        	
        	block.accept(this, arg);
        }
    }
	
	@Override
	public void visit(DoStmt n, Object arg)
	{	Statement statement = n.getBody();
		checkBlock(statement);
		statement.accept(this, arg);
        n.getCondition().accept(this, arg);
    }

    @Override
    public void visit(ForeachStmt n, Object arg)
    {	n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        
        Statement statement = n.getBody();
		checkBlock(statement);
		statement.accept(this, arg);
    }

	@Override
    public void visit(ForStmt n, Object arg)
	{	if(n.getInit() != null)
		{	for(Expression e : n.getInit())
            {	e.accept(this, arg);
            }
        }
        if(n.getCompare() != null)
        {	n.getCompare().accept(this, arg);
        }
        if(n.getUpdate() != null)
        {	for (Expression e : n.getUpdate())
        	{	e.accept(this, arg);
            }
        }
        Statement statement = n.getBody();
		checkBlock(statement);
		statement.accept(this, arg);
    }

	@Override
    public void visit(WhileStmt n, Object arg)
	{	n.getCondition().accept(this, arg);
		Statement statement = n.getBody();
		checkBlock(statement);
		statement.accept(this, arg);
    }

	private void checkBlock(Statement statement)
	{	int line = statement.getBeginLine();
		if(statement instanceof ExpressionStmt)
		{	ExpressionStmt expressionStmt = (ExpressionStmt) statement;
			Expression expression = expressionStmt.getExpression();
			if(expression instanceof MethodCallExpr)
			{	MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
				String methodName = methodCallExpr.getName();
				if(!methodName.equals(CHECK_INTERRUPTION))
        		{	// erreur
        			System.out.println("  Erreur ligne "+line+" : la premi�re instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION+"()");
        			//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
        		}
			}
		}
		else
		{	// erreur
			System.out.println("  Erreur ligne "+line+" : la premi�re instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION+"()");
			//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
		}		
	}
}
