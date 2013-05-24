package formulae.QTLI;

import delegateTranslator.CLTLTranslator;
import formulae.Bounds;
import formulae.Formula;

public abstract class QTLIFormula extends Formula {

	/* Moved in the class Formula
	 * 
	public static final QTLFormula qtlTrue = new QTLTrue();
	public static final QTLFormula qtlFalse = new QTLTrue();
	*/
	
	private int maxIntComparedto = 0;
	
	public QTLIFormula(String formula_p){
		super(formula_p);
		if (this.getClass() != QTLITrue.class && True == null)
			True = new QTLITrue();
		else if (this.getClass() == QTLITrue.class && True == null)
			True = this;
		if (this.getClass() != QTLIFalse.class && False == null && True != null)
			False = new QTLIFalse();
		else if (this.getClass() == QTLIFalse.class && False == null)
			False = this;
	}

		

	@Override
	public String clocksEventsConstraints(CLTLTranslator t) {
	
		String result;
		
		
		
		if ( idFormula() != isTheFormula ){
			
			if (maxIntComparedto() > 0) {
				String f1;
				f1 = t.rel("=", z0(t), "0");
				
				
				//Formula (2)
				String f2 = t.iff(
									t.or(befDnowU(t), befUnowD(t)),
									t.or(t.rel("=", z0(t), "0"), t.rel("=", z1(t), "0"))
							);
									
				
				
				
				//formula (3)
				String f3a = t.implies(
										t.rel("=", z0(t), "0"),
										t.X(t.R(t.rel("=", z1(t), "0"), t.rel(">", z0(t), "0"))));
				
				String f3b = t.implies(
										t.rel("=", z1(t), "0"),
										t.X(t.R(t.rel("=", z0(t), "0"), t.rel(">", z1(t), "0"))));
			
				
				// Clocks progression
				String f4a = t.and(
									t.G(
											t.or(
													t.rel("=", t.X(z0(t)), "0"),
													t.rel(">", t.X(z0(t)), z0(t)))),
									t.or(
											t.G(t.F(t.rel("=", z0(t), "0"))), 
											t.F(t.G(t.rel(">", z0(t), String.valueOf(maxIntComparedto()))))));
													
	
				String f4b = t.and(
						t.G(
								t.or(
										t.rel("=", t.X(z1(t)), "0"),
										t.rel(">", t.X(z1(t)), z1(t)))),
						t.or(
								t.G(t.F(t.rel("=", z1(t), "0"))), 
								t.F(t.G(t.rel(">", z1(t), String.valueOf(maxIntComparedto()))))));		
				
				// Clocks non negativeness in the origin
				String f5 = t.and(
									t.rel(">=", z0(t), "0"),
									t.rel(">=", z1(t), "0"));
				
			
				
				result = t.and(f1, t.G(t.and(f2, f3a, f3b)), f4a, f4b, f5);	
			}
			else
				result = new String("");
			
		}
		else 
			result = point(t); // t.and(point(t), t.rel("=", z0(t), "0"));
					
			
		
		
		return result;
	}
	
	
	public int maxIntComparedto(){
		return maxIntComparedto;
	}
	
	public int maxIntComparedto(int c){
		if (maxIntComparedto < c)
			maxIntComparedto = c;
		
		return maxIntComparedto;
	}
	
	
	public String interval(CLTLTranslator t){
		return t.atom("H_" + String.valueOf(idFormula()));	
	}
	
	public String point(CLTLTranslator t){
		return t.atom("P_" + String.valueOf(idFormula()));	
	}
	
	public String high(CLTLTranslator t){
		return t.and( t.or(t.atom("O"), t.neg(t.Y(interval(t)))), interval(t) );	
	}
	
	public String low(CLTLTranslator t){
		//return t.and( t.or(t.atom("O"), t.neg(t.Y(t.neg(interval(t))))), t.neg(interval(t)) );
		/* Modified because of explicit origin */
		return t.and( t.or(t.atom("O"), t.Y(interval(t))), t.neg(interval(t)) );
	}
	
	public String singU(CLTLTranslator t){
		return t.and( t.Y(t.neg(interval(t))), point(t), t.neg(interval(t)), t.neg(t.atom("O")) );
	}
	
	public String singD(CLTLTranslator t){
		//return t.and( t.neg(t.Y(t.neg(interval(t)))), t.neg(point(t)), interval(t), t.neg(t.atom("O")) );
		/* Modified because of explicit origin */
		return t.and( t.Y(interval(t)), t.neg(point(t)), interval(t), t.neg(t.atom("O")) );
	}
	
	public String befUnowD(CLTLTranslator t){
		return t.or( low(t), singD(t), t.and(t.atom("O"), t.neg(point(t))) );
	}
	
	public String befDnowU(CLTLTranslator t){
		return t.or( high(t), singU(t), t.and(t.atom("O"), point(t)) );		
	}
	
	public String nowOnD(CLTLTranslator t){		
		/* Modified because of explicit origin */
		return t.and( t.or(t.Y(interval(t)), t.atom("O"), point(t)), t.neg(interval(t)) );
		
		//return t.or( low(t), singU(t) );
	}
	
	public String nowOnU(CLTLTranslator t){
		/* Modified because of explicit origin */
		return t.and( t.or(t.neg(t.Y(interval(t))), t.atom("O"), t.neg(point(t))), interval(t) );
		
		//return t.or( high(t), singU(t) );
	}
	
	public String z0(){
		return "z0_" + String.valueOf(idFormula());	
	}	
	
	public String z1(){
		return "z1_" + String.valueOf(idFormula());	
	}
	
	public String z0(CLTLTranslator t){
		return t.var(z0());	
	}	
	
	public String z1(CLTLTranslator t){
		return t.var(z1());	
	}
	

	// Producers method to build CLTL formulae of the argument formula 
	
	public static QTLIFormula atom(String s){
		return new QTLIAtom(s);
	}
	
	public static QTLIFormula not(QTLIFormula f){
		return new QTLINegation(f);
	}
	
	public static QTLIFormula and(QTLIFormula... formulae){
		return new QTLIConjunction(formulae);
	}
		
	
	public static QTLIFormula U(QTLIFormula f1, QTLIFormula f2){
		return new QTLIUntil(f1, f2);
	}
	
	public static QTLIFormula S(QTLIFormula f1, QTLIFormula f2){
		return new QTLISince(f1, f2);
	}
	
	private static QTLIFormula first(QTLIFormula f){
		return U(not(f), f);
	}
	
	private static QTLIFormula last(QTLIFormula f){
		return S(not(f), f);
	}
	

	
	// Eventually: F_<0,b>
	public static QTLIFormula F(QTLIFormula f, Bounds aB, int b, Bounds bB){
		if (aB==Bounds.OPEN && bB==Bounds.OPEN)					//F_(0,b)
			return new QTLIEventually(f, b);
		
		else if (aB==Bounds.CLOSED && bB==Bounds.OPEN)			//F_[0,b)
			return or(f, F(f, Bounds.OPEN, b, bB));
		
		else if (aB==Bounds.OPEN && bB==Bounds.CLOSED)			//F_(0,b]
			return or(
						F(f,Bounds.OPEN,b,Bounds.OPEN), 
						and(first(f), G(F(f, Bounds.OPEN, b, Bounds.OPEN), Bounds.OPEN, b, Bounds.OPEN))
					);
		
		else return or(f, F(f, Bounds.OPEN, b, Bounds.CLOSED));	//F_[0,b]
			
	}
	
	
	// Past: P_<0,b>
	public static QTLIFormula P(QTLIFormula f, Bounds aB, int b, Bounds bB){
		if (aB==Bounds.OPEN && bB==Bounds.OPEN)					//F_(0,b)
			return new QTLIPast(f, b);
		
		else if (aB==Bounds.CLOSED && bB==Bounds.OPEN)			//F_[0,b)
			return or(f, P(f, Bounds.OPEN, b, bB));
		
		else if (aB==Bounds.OPEN && bB==Bounds.CLOSED)			//F_(0,b]
			return or(
						P(f,Bounds.OPEN,b,Bounds.OPEN), 
						and(last(f), G(P(f, Bounds.OPEN, b, Bounds.OPEN), Bounds.OPEN, b, Bounds.OPEN))
					);
		
		else return or(f, P(f, Bounds.OPEN, b, Bounds.CLOSED));	//F_[0,b]
			
	}
	
	
	// Producers method to build derived boolean CLTL formulae
	
	
	public static QTLIFormula or(QTLIFormula... formulae){
		return new QTLIDisjunction(formulae);		
	}
	
	public static QTLIFormula implies(QTLIFormula f1, QTLIFormula f2){
		return new QTLIImplies(f1, f2);
	}
	
	public static QTLIFormula iff(QTLIFormula f1, QTLIFormula f2){
		return new QTLIIff(f1, f2);
	}
	
	// Producers method to build derived temporal CLTL formulae

	public static QTLIFormula R(QTLIFormula f1, QTLIFormula f2){
		return not(U(not(f1), not(f2)));
	}
	
	public static QTLIFormula T(QTLIFormula f1, QTLIFormula f2){
		return not(S(not(f1), not(f2)));
	}		
	
	
	/*
	// Globally: G_(0,b>
	public static QTLFormula G(QTLFormula f, Bounds aB, int b, Bounds bB){
		if (aB==Bounds.OPEN)					//G_(0,b) or G_(0,b]
			return not(F(not(f), aB, b, bB));
		
		else 									//G_[0,b) or G_[0,b]
			return or(f, G(f, Bounds.OPEN, b, bB));
		
	}
	*/
	
	// Globally: G_<0,b> NATIVE IMPLMENTATION
	public static QTLIFormula G(QTLIFormula f, Bounds aB, int b, Bounds bB){
		if (aB==Bounds.OPEN && bB==Bounds.OPEN)					//G_(0,b)
			return new QTLIGlobally(f, b);
		
		else if (aB==Bounds.CLOSED && bB==Bounds.OPEN)			//G_[0,b)
			return and(f, G(f, Bounds.OPEN, b, bB));
		
		else if (aB==Bounds.OPEN && bB==Bounds.CLOSED)			//G_(0,b]
			return and (
						G(f,Bounds.OPEN,b,Bounds.OPEN), 
						or(not(first(not(f))), F(G(f, Bounds.OPEN, b, Bounds.OPEN), Bounds.OPEN, b, Bounds.OPEN))
					);
		
		else return and(f, G(f, Bounds.OPEN, b, Bounds.CLOSED));	//G_[0,b]
			
	}
	
	
	
	// Historically: H_(0,b>
	public static QTLIFormula H(QTLIFormula f, Bounds aB, int b, Bounds bB){
		if (aB==Bounds.OPEN)					//G_(0,b) or G_(0,b]
			return not(P(not(f), aB, b, bB));
		
		else 									//G_[0,b) or G_[0,b]
			return or(f, H(f, Bounds.OPEN, b, bB));
		
	}	
	
	// Eventually: F_<a,+oo)
	public static QTLIFormula F(QTLIFormula f, int a, Bounds aB){
		
		QTLIFormula qtlTrue = (QTLIFormula)True();
		
		if (a==0 && aB==Bounds.OPEN)				//F_(0,+oo)
			return U(qtlTrue, f);
		
		else if (a==0 && aB==Bounds.CLOSED)			//F_[0,+oo)
			return or(f, F(f, 0, Bounds.OPEN));
		
		else if (a>0 && aB==Bounds.OPEN)			//F_(a,+oo)
			return G(F(f, 0, Bounds.OPEN), Bounds.OPEN, a, Bounds.CLOSED);
		
		else										//F_[a,+oo)
			return G(F(f, 0, Bounds.CLOSED), Bounds.OPEN, a, Bounds.CLOSED);
	}
	
	
	// Globally: G_<a,+oo)
	public static QTLIFormula G(QTLIFormula f, int a, Bounds aB){
		return not(F(not(f), a, aB));

	}
		
	// Past: P_<a,+oo)
	public static QTLIFormula P(QTLIFormula f, int a, Bounds aB){
		
		QTLIFormula qtlTrue = (QTLIFormula)True();
		
		if (a==0 && aB==Bounds.OPEN)				//P_(0,+oo)
			return S(qtlTrue, f);
		
		else if (a==0 && aB==Bounds.CLOSED)			//P_[0,+oo)
			return or(f, P(f, 0, Bounds.OPEN));
		
		else if (a>0 && aB==Bounds.OPEN)			//P_(a,+oo)
			return G(P(f, 0, Bounds.OPEN), Bounds.OPEN, a, Bounds.CLOSED);
		
		else										//P_[a,+oo)
			return G(P(f, 0, Bounds.CLOSED), Bounds.OPEN, a, Bounds.CLOSED);
	}
	
	// Historically: H_<a,+oo)
	public static QTLIFormula H(QTLIFormula f, int a, Bounds aB){
		return not(P(not(f), a, aB));

	}
	
	
	public static QTLIFormula F(QTLIFormula f, int a, Bounds aB, int b, Bounds bB){
		if (a == 0)
			return F(f, aB, b, bB);
		else{
			
			/*
			int d = b-a;
			QTLIFormula fm = f;
			
			int low = a;
			int upp = b;
			for (; low >= d; low = low - d, upp = upp - d)
					fm = G(F(fm,0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.OPEN,d,Bounds.OPEN);
			
			if (low > 0){
					QTLIFormula[] _or =  new QTLIFormula[d];
					int i = 0;
			 		for (int j = low; j < upp; j++){
						if (j == d)
							_or[i++] = F(G(F(fm,0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.CLOSED,1,bB);		
						else{
							QTLIFormula orf = fm;
							for (int h=j; h>0; h--)
								orf = G(F(orf, 0, Bounds.OPEN, 1, Bounds.OPEN), 0, Bounds.OPEN, 1, Bounds.OPEN);
							if (i == 0)								
								orf = F(orf, 0, aB, 1, Bounds.OPEN);							
			 				else
								orf = F(orf, 0, Bounds.CLOSED, 1, Bounds.OPEN);
			
			 				_or[i++] = orf;
						}
					}
					return or(_or);
			 }
			else
			 		return F(fm,0,aB,d,Bounds.OPEN);
			*/
			
			// easier translation by G
			
			not(G(not(f), aB, b, bB));
		 		
		}		
		 		
		
	}
	

	public static QTLIFormula G(QTLIFormula f, int a, Bounds aB, int b, Bounds bB){
		if (a == 0)
			return G(f, aB, b, bB);
		else{
		
		
		/* Following implementation uses native encoding for Globally*/
		/*
			int d = b-a;
			QTLIFormula fm = f;
			
			int low = a;
			int upp = b;
			for (; low >= d; low = low - d, upp = upp - d)
				if (low == a)
					fm = F(G(fm,0,aB,d,bB),0,Bounds.OPEN,d,Bounds.OPEN);
				else
					fm = F(G(fm,0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.OPEN,d,Bounds.OPEN);
					
			
			if (low > 0){
				QTLIFormula orf = G(fm,0,Bounds.OPEN,d,Bounds.OPEN);
				
				for (int h=low; h>0; h--)
					orf = G(F(orf, 0, Bounds.OPEN, 1, Bounds.OPEN), 0, Bounds.OPEN, 1, Bounds.OPEN);
				
				return orf;
			}
			else
			 	return G(fm, 0, Bounds.OPEN, d, Bounds.OPEN);
			
			*/
			
			int d = b-a;
			QTLIFormula fm = G(f,0,aB,d,bB);
			
			int low = a;
			int upp = b;
			for (; low >= d; low = low - d, upp = upp - d)
				fm = G(F(fm,0,bB,d,aB),0,aB,d,bB);
								
			if (low > 0){
				for (int h=low; h>0; h--)
					fm = G(F(fm, 0, bB, 1, aB), 0, aB, 1, bB);			
			}
			
			return fm;		
		}
	}
	
	
	public static QTLIFormula H(QTLIFormula f, int a, Bounds aB, int b, Bounds bB){
		if (a == 0)
			return H(f, aB, b, bB);
		else
			return not(P(not(f), a, aB, b, bB));
	}
	
	
	public static QTLIFormula P(QTLIFormula f, int a, Bounds aB, int b, Bounds bB){
		if (a == 0)
			return P(f, aB, b, bB);
		else{
			int d = b-a;
			QTLIFormula fm = f;
			
			int low = a;
			int upp = b;
			for (; low >= d; low = low - d, upp = upp - d)
					fm = H(P(fm,0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.OPEN,d,Bounds.OPEN);
			
			if (low > 0){
					QTLIFormula[] _or =  new QTLIFormula[d];
					int i = 0;
			 		for (int j = low; j < upp; j++){
						if (j == d)
							_or[i++] = P(H(P(fm,0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.OPEN,d,Bounds.OPEN),0,Bounds.CLOSED,1,bB);		
						else{
							QTLIFormula orf = fm;
							for (int h=j; h>0; h--)
								orf = H(P(orf, 0, Bounds.OPEN, 1, Bounds.OPEN), 0, Bounds.OPEN, 1, Bounds.OPEN);
							if (i == 0)								
								orf = P(orf, 0, aB, 1, Bounds.OPEN);							
			 				else
								orf = P(orf, 0, Bounds.CLOSED, 1, Bounds.OPEN);
			
			 				_or[i++] = orf;
						}
					}
					return or(_or);
			 }
			else
		 		return P(fm,0,aB,d,Bounds.OPEN);
		}		
	}
	
}
