package formulae.QTLI;

import java.util.ArrayList;
import java.util.List;
import delegateTranslator.CLTLTranslator;
import formulae.Formula;
import formulae.Temporized;

public class QTLIGlobally extends QTLIFormula implements Temporized{

	private QTLIFormula subformula;
	private final int b;  
	
	public QTLIGlobally(QTLIFormula subformula, int b){
		super(new String("(G " + String.valueOf(b) + " " + subformula.strFormula() + ")"));
		this.subformula = subformula;
		this.b = b;
		
		subformula.maxIntComparedto(b);
		maxIntComparedto(b);
	}
	
	
	@Override
	public String translate(CLTLTranslator t) {
		
		// Some alias...
		QTLIFormula subf = subformula;
	
		String orig = t.atom("O");
				
		String f1;
		f1 = t.implies( interval(t), point(t) ); 
	
		String f2 = t.iff(
				low(t),
				t.or(
					t.and(
						t.neg(orig),
						point(t),
						t.or(
								t.and(
									t.rel("=", z0(t), "0"),
									t.X(
											t.U(
													t.and(t.rel(">", z0(t), "0"), t.neg(subf.befUnowD(t))), 
													t.and(
															subf.befUnowD(t),
															t.rel("=", z0(t), String.valueOf(b)),
															t.or(t.rel(">", subf.z0(t), String.valueOf(b)), t.rel(">", subf.z1(t), String.valueOf(b)))
														)
												)
										)
								),
								t.and(
										t.rel("=", z1(t), "0"),
										t.X(
												t.U(
														t.and(t.rel(">", z1(t), "0"), t.neg(subf.befUnowD(t))), 
														t.and(
																subf.befUnowD(t),
																t.rel("=", z1(t), String.valueOf(b)),
																t.or(t.rel(">", subf.z0(t), String.valueOf(b)), t.rel(">", subf.z1(t), String.valueOf(b)))
															)
													)
											)
								)
							)
						),
						t.and(
							orig,
							t.or(
								t.and(
										t.neg(point(t)),
										t.or(
												subf.low(t),
												t.X(
														t.U(
																t.rel(">", z0(t), "0"),
																t.and(
																		subf.befUnowD(t),
																		t.rel(">", z0(t), "0"),
																		t.rel("<", z0(t), String.valueOf(b))
																)
														)
												)
										)
								),
								t.and(
										point(t),
										subf.high(t),
										t.X(
												t.U(
														t.and(t.rel(">", z0(t), "0"), t.neg(subf.befUnowD(t))),
														t.and(
																subf.befUnowD(t),
																t.rel("=", z0(t), String.valueOf(b))													
														)
												)
										)
								)
							)
					)
			)
		);
					
		String f3 = t.implies(	
								t.and(
										subf.befUnowD(t),
										t.or(t.rel(">=", subf.z0(t), String.valueOf(b)), t.rel(">=", subf.z1(t), String.valueOf(b)))
								), 
								t.or(t.rel("=", z0(t), String.valueOf(b)), t.rel("=", z1(t), String.valueOf(b)))
		);
		
		String f4 = t.iff(
				high(t),
				t.and(
						subf.nowOnU(t),
						t.neg(
								t.X(
										t.U(
												t.neg(subf.befUnowD(t)),
												t.and(
														subf.befUnowD(t),
														t.or(
																t.and(
																		t.rel(">", subf.z0(t), "0"),
																		t.rel("<=", subf.z0(t), String.valueOf(b))
																),
																t.and(
																		t.rel(">", subf.z1(t), "0"),
																		t.rel("<=", subf.z1(t), String.valueOf(b))
																)
														)
												)
										)
								)
						)
				)
			);




		String f5 = t.iff(
				singU(t),
				t.and(
						subf.nowOnU(t),
						t.X(
								t.U(
										t.neg(subf.befUnowD(t)),
										t.and(
												subf.befUnowD(t),
												t.or(
														t.rel("=", subf.z0(t), String.valueOf(b)),
														t.rel("=", subf.z1(t), String.valueOf(b))	
												)
										)
								)
						),
						t.neg(orig)
				)
			);
		
		
		
		
		return t.and(super.clocksEventsConstraints(t), t.G(t.and(f1,f2,f3,f4,f5)));
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

		int m = subformula.maxIntComparedto();
		subformula = (QTLIFormula)l.get(0);	
		subformula.maxIntComparedto(m);
		return this;
	}


	@Override
	public Formula simplify() {
		return new QTLIGlobally((QTLIFormula)subformula.simplify(), b);
	}
	
}

