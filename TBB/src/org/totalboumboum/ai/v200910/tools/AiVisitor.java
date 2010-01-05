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

import java.util.Arrays;
import java.util.List;

import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

/**
 * cette m�thode parse les codes sources d�finissant une IA et v�rifie
 * que les appels � checkInterruption sont effectu�s correctement, c'est � dire :
 * 	- un appel � chaque d�but de boucle (for, while, do)
 * 	- un appel � chaque d�but de m�thode, sauf :
 * 		- dans un constructeur :
 * 			- en cas d'appel � super() : checkInterruption() est appel� en deuxi�me (et non pas en premier)
 * 			- dans une classe impl�mentant l'interface ArtificialIntelligence
 * 		- dans une m�thode dont on ne contr�le pas l'interface (du type toString, equals, compare, etc.)
 * 	- l'appel ne doit pas �tre plac� dans un try-catch qui annulerait son effet
 * 
 * @author Vincent Labatut
 */
public class AiVisitor extends VoidVisitorAdapter<Object>
{	

	public AiVisitor(int initLevel)
	{	indentLevel = initLevel;		
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC	CONSTANTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final static String ARTIFICIAL_INTELLIGENCE_CLASS = "ArtificialIntelligence";
	private final static String CHECK_INTERRUPTION_METHOD = "checkInterruption";
	private final static List<String> IGNORED_METHODS = Arrays.asList(new String[]
	{	"AiMain",			
		"compare",
		"equals",
		"toString"
	});
	private final static List<String> FORBIDDEN_EXCEPTIONS = Arrays.asList(new String[]
 	{	"Exception",			
 		"StopRequestException"
 	});

	/////////////////////////////////////////////////////////////////
	// MISC VARIABLES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String currentMethod = null;
	private int indentLevel;
	private boolean checkConstructor;
	
	/////////////////////////////////////////////////////////////////
	// ERROR COUNT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int errorCount = 0;
	
	public int getErrorCount()
	{	return errorCount;	
	}
	
	/////////////////////////////////////////////////////////////////
	// VISITOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    @Override
    public void visit(CatchClause n, Object arg)
    {	int line = n.getBeginLine();
    	Parameter e = n.getExcept();
    	if(!IGNORED_METHODS.contains(currentMethod))
    	{	Type t = e.getType();
	    	if(t instanceof ReferenceType)
			{	Type t2 = ((ReferenceType)t).getType();
				if(t2 instanceof ClassOrInterfaceType)
				{	String exceptionName = ((ClassOrInterfaceType)t2).getName();
					if(FORBIDDEN_EXCEPTIONS.contains(exceptionName))
					{	for(int i=0;i<indentLevel;i++)
							System.out.print(">>");
				       	System.out.println("Erreur ligne "+line+" : le catch("+exceptionName+") masque l'appel � "+CHECK_INTERRUPTION_METHOD+"()");
						errorCount++;
						//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
					}
				}
				else
				{	System.out.print(">>11111111111 probl�me : exception non conforme ("+t2.getClass()+")");
				}
			}
	    	else
			{	System.out.print(">>222222222 probl�me : exception non conforme ("+t.getClass()+")");
	    	}
    	}
    	e.accept(this, arg);
        n.getCatchBlock().accept(this, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg)
    {	checkConstructor = true;
    
    	if(n.getJavaDoc() != null)
    	{	n.getJavaDoc().accept(this, arg);
        }
        if(n.getAnnotations() != null)
        {	for(AnnotationExpr a : n.getAnnotations())
        	{	a.accept(this, arg);
            }
        }
        if(n.getTypeParameters() != null)
        {	for (TypeParameter t : n.getTypeParameters())
        	{	t.accept(this, arg);
            }
        }
        if(n.getExtends() != null)
        {	for (ClassOrInterfaceType c : n.getExtends())
        	{	if(c.getName().equals(ARTIFICIAL_INTELLIGENCE_CLASS))
        			checkConstructor = false;
        		c.accept(this, arg);
            }
        }
        if(n.getImplements() != null)
        {	for(ClassOrInterfaceType c : n.getImplements())
        	{	c.accept(this, arg);
            }
        }
        if(n.getMembers() != null)
        {	for (BodyDeclaration member : n.getMembers())
        	{	member.accept(this, arg);
            }
        }
    }

    @Override
    public void visit(ConstructorDeclaration n, Object arg)
    {	String prevMethod = currentMethod;
    	currentMethod = n.getName();
    	indentLevel++;

    	if (n.getJavaDoc() != null)
    	{	n.getJavaDoc().accept(this, arg);
        }
        if(n.getAnnotations() != null)
        {	for (AnnotationExpr a : n.getAnnotations())
        	{	a.accept(this, arg);
            }
        }
        if(n.getTypeParameters() != null)
        {	for (TypeParameter t : n.getTypeParameters())
        	{	t.accept(this, arg);
            }
        }
        if(n.getParameters() != null)
        {	for (Parameter p : n.getParameters())
        	{	p.accept(this, arg);
            }
        }
        if(n.getThrows() != null)
        {	for (NameExpr name : n.getThrows())
        	{	name.accept(this, arg);
            }
        }
        BlockStmt block = n.getBlock();
        for(int i=0;i<indentLevel;i++)
			System.out.print("..");
		System.out.println("Analyse du constructeur "+currentMethod);
    	if(checkConstructor)
    		checkBlock(block);
        block.accept(this, arg);
        
        currentMethod = prevMethod;
    	indentLevel--;
   }

    @Override
    public void visit(MethodDeclaration n, Object arg) 
	{	String prevMethod = currentMethod;
		currentMethod = n.getName();
    	indentLevel++;
		
		if(n.getJavaDoc() != null)
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
        {	BlockStmt block = n.getBody();
//if(name.equals("getZoneArray"))
//	System.out.println();
        	for(int i=0;i<indentLevel;i++)
				System.out.print("..");
        	System.out.println("Analyse de la m�thode "+currentMethod);
        	checkBlock(block);  
        	block.accept(this, arg);
        }
        
        currentMethod = prevMethod;
    	indentLevel--;
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
	{	if(!IGNORED_METHODS.contains(currentMethod))
		{	int line = statement.getBeginLine();
			if(statement instanceof BlockStmt)
			{	BlockStmt block = (BlockStmt) statement;
				List<Statement> statements = block.getStmts();
				if(!statements.isEmpty())
				{	Statement firstStatement = statements.get(0);
					line = firstStatement.getBeginLine();
					if(firstStatement instanceof ExpressionStmt)
					{	ExpressionStmt expressionStmt = (ExpressionStmt) firstStatement;
						Expression expression = expressionStmt.getExpression();
						if(expression instanceof MethodCallExpr)
						{	MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
							String methodName = methodCallExpr.getName();
							if(!methodName.equals(CHECK_INTERRUPTION_METHOD))
			        		{	// erreur
								for(int i=0;i<indentLevel;i++)
									System.out.print(">>");
						       	System.out.println("Erreur ligne "+line+" : la premi�re instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION_METHOD+"()");
			        			errorCount++;
			        			//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
			        		}
						}
						else
						{	// erreur
							for(int i=0;i<indentLevel;i++)
								System.out.print(">>");
					       	System.out.println("Erreur ligne "+line+" : la premi�re instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION_METHOD+"()");
		        			errorCount++;
							//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
						}
					}
					else if(firstStatement instanceof ExplicitConstructorInvocationStmt)
					{	// c'est un appel � super(), donc �a doit forc�ment �tre au d�but
						// mais la deuxi�me instruction doit �tre un appel � checkInterruption()
						if(statements.size()<2)
						{	// erreur
							for(int i=0;i<indentLevel;i++)
								System.out.print(">>");
					       	System.out.println("Erreur ligne "+line+" : la deuxi�me instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION_METHOD+"()");
		        			errorCount++;
							//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
						}
						else
						{	line = firstStatement.getBeginLine();
							Statement secondStatement = statements.get(1);
							if(secondStatement instanceof ExpressionStmt)
							{	ExpressionStmt expressionStmt = (ExpressionStmt) secondStatement;
								Expression expression = expressionStmt.getExpression();
								if(expression instanceof MethodCallExpr)
								{	MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
									String methodName = methodCallExpr.getName();
									if(!methodName.equals(CHECK_INTERRUPTION_METHOD))
					        		{	// erreur
										for(int i=0;i<indentLevel;i++)
											System.out.print(">>");
								       	System.out.println("Erreur ligne "+line+" : la deuxi�me instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION_METHOD+"()");
					        			errorCount++;
					        			//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
					        		}
								}
								else
								{	// erreur
									for(int i=0;i<indentLevel;i++)
										System.out.print(">>");
							       	System.out.println("Erreur ligne "+line+" : la deuxi�me instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION_METHOD+"()");
				        			errorCount++;
									//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
								}
							}
						}
					}
					else
					{	// erreur
						for(int i=0;i<indentLevel;i++)
							System.out.print(">>");
				       	System.out.println("Erreur ligne "+line+" : la premi�re instruction du bloc n'est pas un appel � "+CHECK_INTERRUPTION_METHOD+"()");
	        			errorCount++;
						//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
					}
				}
				else
				{	// erreur
					for(int i=0;i<indentLevel;i++)
						System.out.print("--");
			       	System.out.println("Attention ligne "+line+" : le bloc est vide !");
					//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
				}
			}
			else
			{	// erreur
				for(int i=0;i<indentLevel;i++)
					System.out.print(">>");
		       	System.out.println("Erreur ligne "+line+" : bloc manquant, appel � "+CHECK_INTERRUPTION_METHOD+"() manquant �galement");
				errorCount++;
				//TODO � compl�ter par la cr�ation d'un commentaire dans le code source
			}
		}
	}
}
