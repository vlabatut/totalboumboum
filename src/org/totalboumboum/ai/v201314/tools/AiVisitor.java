package org.totalboumboum.ai.v201314.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.ai.AiPackageTools;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.tools.classes.ClassTools;

import japa.parser.ast.BlockComment;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
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
 * cette méthode parse les codes sources définissant un agent et vérifie
 * que les appels à {@code checkInterruption()} sont effectués correctement, c'est à dire :
 *  <ul><li>un appel à chaque début de boucle ({@code for}, {@code while}, {@code do})</li>
 * 		<li>un appel à chaque début de méthode, sauf :
 * 			<ul><li>dans un constructeur :
 * 				<ul><li>en cas d'appel à super() : {@code checkInterruption()} est appelé en deuxième (et non pas en premier)</li>
 * 					<li>dans une classe implémentant l'interface {@link ArtificialIntelligence}</li>
 * 				</ul>
 * 				<li>dans une méthode dont on ne contrôle pas la signature (du type {@code toString}, {code equals}, {@code compare}, etc.)</li>
 * 			</ul>
 * 		<li>l'appel ne doit pas être placé dans un {@code try-catch} qui annulerait son effet</li>
 * 	</ul>
 * 
 * 	- un appel à chaque début de boucle (for, while, do)
 * 	- un appel à chaque début de méthode, sauf :
 * 		- dans un constructeur :
 * 			- en cas d'appel à super() : checkInterruption() est appelé en deuxième (et non pas en premier)
 * 			- dans une classe implémentant l'interface ArtificialIntelligence
 * 		- dans une méthode dont on ne Contrôle pas l'interface (du type toString, equals, compare, etc.)
 * 	- l'appel ne doit pas être placé dans un try-catch qui annulerait son effet
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class AiVisitor extends VoidVisitorAdapter<Object>
{	
	/**
	 * Crée un visiteur
	 * 
	 * @param initLevel
	 * 		Niveau dans l'arbre des appels.
	 */
	public AiVisitor(int initLevel)
	{	indentLevel = initLevel;		
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC	CONSTANTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Classe principale des agents */
	private final static String ARTIFICIAL_INTELLIGENCE_CLASS = "ArtificialIntelligence";
	/** Package général du jeu */
	private final static String GAME_PACKAGE = "org.totalboumboum";
	/** Méthode recherchée */
	private final static String CHECK_INTERRUPTION_METHOD = "checkInterruption";
	/** Méthodes ignorées lors de l'analyse */
	private final static List<String> IGNORED_METHODS = Arrays.asList(new String[]
	{	"AiMain",			
//		"compare",
//		"equals",
//		"hashCode",
		"instantiateAgent",
		"main"
//		"toString"
	});
	/** Méthodes interdites */
	private final static List<String> FORBIDDEN_METHODS = Arrays.asList(new String[]
	{	"System.out.print",
		"System.out.println",
		"System.err.print",
		"System.err.println",
		"printStackTrace"
	});
	/** Exceptions à ne pas couvrir dans un bloc {@code try-catch} */
	private final static List<String> FORBIDDEN_EXCEPTIONS = Arrays.asList(new String[]
 	{	"Exception",			
 		"StopRequestException"
 	});
	/** Classe du jeu (hors API) autorisées */
	private final static List<String> ALLOWED_CLASSES = Arrays.asList(new String[]
	{	GAME_PACKAGE+".tools.images.PredefinedColor",
		GAME_PACKAGE+".engine.content.feature.Direction"
	});
	
	/////////////////////////////////////////////////////////////////
	// MISC VARIABLES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Classe mère de l'agent courant */
	private String agentClass = null;
	/** Package de l'agent courant */
	private String agentPackage = null;
	/** Package du pack courant */
	private String packPackage = null;
	/** Package de l'API courante */
	private String apiPackage = null;
	/** Méthode courante */
	private String currentMethod = null;
	/** Niveau hiérarchique courant */
	private int indentLevel;
	/** La méthode courante est un constructeur */
	private boolean checkConstructor;
	
	/////////////////////////////////////////////////////////////////
	// ERROR COUNT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Décompte des erreurs relevées */
	private int errorCount = 0;
	
	/**
	 * Renvoie le nombre total d'erreurs
	 * relevées lors de l'analyse.
	 * 
	 * @return
	 * 		Le nombre d'erreurs identifiées.
	 */
	public int getErrorCount()
	{	return errorCount;	
	}
	
	/**
	 * Affiche le message d'erreur spécifié
	 * 
	 * @param msg
	 * 		Message d'erreur à afficher.
	 */
	public void printErr(String msg)
	{	for(int i=0;i<indentLevel;i++)
			msg = ">>" + msg;
       	System.out.println(msg);
       	System.err.println(msg);
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
					{	printErr("Erreur ligne "+line+" : le catch("+exceptionName+") masque l'appel à "+CHECK_INTERRUPTION_METHOD+"()");
						errorCount++;
					}
				}
				else
				{	System.err.print(">>11111111111 problème : exception non conforme ("+t2.getClass()+")");
				}
			}
	    	else
			{	System.err.print(">>222222222 problème : exception non conforme ("+t.getClass()+")");
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
        	for(int i=0;i<indentLevel;i++)
				System.out.print("..");
        	System.out.println("Analyse de la méthode "+currentMethod);
        	checkBlock(block);  
        	block.accept(this, arg);
        }
        
        currentMethod = prevMethod;
    	indentLevel--;
	}
	
    @Override
    public void visit(MethodCallExpr n, Object arg)
    {	String name = n.getName();
    	String calledMethod = n.getScope()+ClassTools.CLASS_SEPARATOR+name;
    	if(FORBIDDEN_METHODS.contains(calledMethod) || FORBIDDEN_METHODS.contains(name))
		{	int line = n.getBeginLine();
			printErr("Erreur ligne "+line+" : il est interdit d'utiliser la méthode "+calledMethod+" ; à la place vous devez utiliser la méthode print de votre agent (héritant de ArtificialIntelligence) ou d'un gestionnaire (héritant de AiAbstractHandler)");
			errorCount++;
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

	/**
	 * Analyse d'un bloc de code source.
	 * 
	 * @param statement
	 * 		Le bloc à analyser
	 */
	private void checkBlock(Statement statement)
	{	indentLevel++;
		if(!IGNORED_METHODS.contains(currentMethod))
		{	int line = statement.getBeginLine();
			if(statement instanceof BlockStmt)
			{	BlockStmt block = (BlockStmt) statement;
				List<Statement> statements = block.getStmts();
				if(statements!=null && !statements.isEmpty())
				{	Statement firstStatement = statements.get(0);
					line = firstStatement.getBeginLine();
					if(firstStatement instanceof ExpressionStmt)
					{	ExpressionStmt expressionStmt = (ExpressionStmt) firstStatement;
						Expression expression = expressionStmt.getExpression();
						if(expression instanceof MethodCallExpr)
						{	MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
							String methodName = methodCallExpr.getName();
							if(!methodName.equals(CHECK_INTERRUPTION_METHOD))
			        		{	printErr("Erreur ligne "+line+" : la première instruction du bloc n'est pas un appel à "+CHECK_INTERRUPTION_METHOD+"()");
			        			errorCount++;
			        		}
						}
						else
						{	printErr("Erreur ligne "+line+" : la première instruction du bloc n'est pas un appel à "+CHECK_INTERRUPTION_METHOD+"()");
		        			errorCount++;
						}
					}
					else if(firstStatement instanceof ExplicitConstructorInvocationStmt)
					{	// c'est un appel à super/this, donc ça doit forcément être au début
						// mais la deuxième instruction doit être un appel à checkInterruption()
						if(statements.size()<2)
						{	printErr("Erreur ligne "+line+" : la deuxième instruction du bloc n'est pas un appel à "+CHECK_INTERRUPTION_METHOD+"()");
		        			errorCount++;
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
					        		{	printErr("Erreur ligne "+line+" : la deuxième instruction du bloc n'est pas un appel à "+CHECK_INTERRUPTION_METHOD+"()");
					        			errorCount++;
					        		}
								}
								else
								{	printErr("Erreur ligne "+line+" : la deuxième instruction du bloc n'est pas un appel à "+CHECK_INTERRUPTION_METHOD+"()");
				        			errorCount++;
								}
							}
						}
					}
					else
					{	printErr("Erreur ligne "+line+" : la première instruction du bloc n'est pas un appel à "+CHECK_INTERRUPTION_METHOD+"()");
	        			errorCount++;
					}
				}
				else
				{	printErr("Attention ligne "+line+" : le bloc est vide !");
				}
			}
			else
			{	printErr("Erreur ligne "+line+" : bloc manquant, appel à "+CHECK_INTERRUPTION_METHOD+"() manquant également");
				errorCount++;
			}
		}
		else
		{	for(int i=0;i<indentLevel;i++)
				System.out.print("..");
			System.out.println("Méthode ignorée");
		}
		indentLevel--;
	}
	
	@Override
	public void visit(SingleMemberAnnotationExpr expression, Object arg)
	{	// annotation de type @SuppressWarnings("javadoc")
		NameExpr name = expression.getName();
		if(name.getName().equalsIgnoreCase("SuppressWarnings"))
		{	Expression expr = expression.getMemberValue();
			if(expr instanceof StringLiteralExpr)
			{	StringLiteralExpr sle = (StringLiteralExpr) expr;
				if(sle.getValue().equalsIgnoreCase("javadoc"))
				{	int line = expression.getBeginLine();
					printErr("Erreur ligne "+line+" : utilisation de l'annotation SuppressWarnings sur 'javadoc'. Il faut supprimer cette annotation et compléter la Javadoc.");
					errorCount++;
				}
			}
		}
		//ne traite pas le cas où on a plusieurs membres, e.g. @SuppressWarnings({"javadoc","xxxx"})
	}

	@Override
    public void visit(BlockComment comment, Object arg)
	{	// ne marche pas (méthode jamais appelée)
    }
	
	@Override
	public void visit(JavadocComment comment, Object arg)
	{	// fonctionne, mais...
		// déjà traité dans le parser
    }

	@Override
    public void visit(LineComment comment, Object arg)
	{	// ne marche pas (méthode jamais appelée)
    }
	
	@Override
	public void visit(PackageDeclaration declaration, Object arg)
	{	String pack = declaration.getName().toString();
	
		// on met à jour le package de l'API courante
		apiPackage = AiPackageTools.getApiPackage(pack);

		// on met à jour le package du pack courant
		packPackage = AiPackageTools.getPackPackage(pack);
		
		// on met à jour le package de l'agent courant
		agentPackage = AiPackageTools.getAgentPackage(pack,true);
		
		// on met à jour la classe mère de l'agent courant
		agentClass = AiPackageTools.getAgentPackage(pack,false) + ClassTools.CLASS_SEPARATOR + "AiMain";
	}
	
	@Override
	public void visit(ImportDeclaration n, Object arg)
	{	String name = n.getName().toString();
	
		// s'agit-il d'une classe du jeu ?
		if(name.startsWith(GAME_PACKAGE))
		{	// s'agit-il d'une classe de l'API ?
			if(!(name.startsWith(apiPackage) && !name.startsWith(packPackage))
			// s'agit-il d'une autre classe de l'agent ?
			&& !name.startsWith(agentPackage)
			// s'agit-il de la classe AiMain de l'agent ?
			&& !name.equals(agentClass)
			// s'agit-il d'une classe du jeu autorisée ?
			&& !ALLOWED_CLASSES.contains(name))
			{	int line = n.getBeginLine();
				printErr("Erreur ligne "+line+" : le package ou la classe importé(e) ("+name+") n'est pas autorisé(e) pour cet agent.");
				errorCount++;
			}
		}
	}
}
