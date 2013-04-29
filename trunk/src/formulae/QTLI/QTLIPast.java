package formulae.QTLI;

import java.util.ArrayList;
import java.util.List;
import delegateTranslator.CLTLTranslator;
import formulae.Formula;
import formulae.Temporized;

public class QTLIPast extends QTLIFormula implements Temporized{

	private QTLIFormula subformula;
	private final int b;  
	
	public QTLIPast(QTLIFormula subformula, int b){
		super(new String("(P " + String.valueOf(b) + " " + subformula.strFormula() + ")"));
		this.subformula = subformula;
		this.b = b;
	}
	
	
	@Override
	public String translate(CLTLTranslator t) {
		
		// Some alias...
		QTLIFormula subf = subformula;
	
		String orig = t.atom("O");
				
		String f1;
		f1 = t.and( t.implies(point(t), t.and(interval(t), t.Y(interval(t)))),
					t.iff(t.neg(point(t)), orig) );
	
		String f2 = t.iff(
							high(t),
							t.and(
									subf.befDnowU(t),
									t.or(
											orig,
											t.Y(
													t.S(
															t.neg(subf.befDnowU(t)),
															t.and(
																	orig,
																	t.neg(t.and(subf.point(t), subf.interval(t)))
															)
													)
											),
											t.or(t.rel(">=", subf.z0(t), String.valueOf(b)), t.rel(">=", subf.z1(t), String.valueOf(b)))
									)
							)
					);
						
							
		
		String f3;
		f3 = t.iff(
				low(t),
				t.or(
						t.and(orig, t.neg(t.and(subf.point(t), subf.interval(t)))),
						t.and(
								t.rel("=", subf.z0(t), String.valueOf(b)),
								t.S(
										t.neg(subf.befDnowU(t)),
										t.and(
												t.or(
														t.Y(subf.interval(t)),
														subf.point(t)
												),
												t.rel("=", subf.z0(t), String.valueOf(b))
										)
								)
						),
						t.and(
								t.rel("=", subf.z1(t), String.valueOf(b)),
								t.S(
										t.neg(subf.befDnowU(t)),
										t.and(
												t.or(
														t.Y(subf.interval(t)),
														subf.point(t)
												),
												t.rel("=", subf.z1(t), String.valueOf(b))
										)
								)
						)
				)
		);


		String f4;
		f4 = t.iff(
				singD(t),
				t.and(
						subf.befDnowU(t),
						t.or(					
							t.and(
									t.rel("=", subf.z0(t), String.valueOf(b)),
									t.S(
											t.neg(subf.befDnowU(t)),
											t.and(
													t.or(
															t.Y(subf.interval(t)),
															subf.point(t)
													),
													t.rel("=", subf.z0(t), String.valueOf(b))
											)
									)
							),
							t.and(
									t.rel("=", subf.z1(t), String.valueOf(b)),
									t.S(
											t.neg(subf.befDnowU(t)),
											t.and(
													t.or(
															t.Y(subf.interval(t)),
															subf.point(t)
													),
													t.rel("=", subf.z1(t), String.valueOf(b))
											)
									)
							)
					)
				)	
		);

		
		
		
		
		return t.and(super.clocksEventsConstraints(t), t.G(t.and(f1,f2,f3,f4)));
	}
	



	@Override
	public List<Formula> subformulae() {
		ArrayList<Formula> r = new ArrayList<Formula>();
		
		r.add(subformula);
		
		return r;
	}
	
	
	public int upperbound(){
		return b; 
	}
	
	public int lowerbound(){
		return 0; 
	}


	@Override
	public QTLIFormula update(List<Formula> l) {
		// if the list of subformulae are logically equivalent to the subformulae then we can safely replace them
		if (l.get(0).equals(subformula)){
			subformula = (QTLIFormula)l.get(0);					
			return this;
		}
		//else rise an error. TODO: implement WrongUpdateException
		else
			return null;
	}


	@Override
	public Formula simplify() {
		return new QTLIPast((QTLIFormula)subformula.simplify(), b);
	}
	
}
