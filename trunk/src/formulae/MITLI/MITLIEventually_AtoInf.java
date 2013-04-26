package formulae.MITLI;

import java.util.ArrayList;
import java.util.List;
import delegateTranslator.CLTLTranslator;
import formulae.Formula;
import formulae.Temporized;

public class MITLIEventually_AtoInf extends MITLIFormula implements Temporized{


	private MITLIFormula subformula;
	private final int a;	
	
	
	public MITLIEventually_AtoInf(MITLIFormula subformula, int a) {
		super(new String("(F " + String.valueOf(a) + " +oo" + subformula.strFormula() + ")"));
		this.subformula = subformula;
		this.a = a;
	}



	@Override
	public List<Formula> subformulae() {
		ArrayList<Formula> r = new ArrayList<Formula>();
		
		r.add(subformula);
		
		return r;
	}
	
	

	@Override
	public MITLIFormula update(List<Formula> l) {
		// if the list of subformulae are logically equivalent to the subformulae then we can safely replace them
		if (l.get(0).equals(subformula)){
			subformula = (MITLIFormula)l.get(0);					
			return this;
		}
		//else rise an error. TODO: implement WrongUpdateException
		else
			return null;
	}


	@Override
	public Formula simplify() {
		return new MITLIEventually_AtoInf((MITLIFormula)subformula.simplify(), a);
	}




	@Override
	public String translate(CLTLTranslator t) {
		
		
		String orig = new String("O");
		
		List<Formula> subfs = this.subformulae();
		MITLIFormula subf = (MITLIFormula)subfs.get(0);
		
		
		// Formula (15)
		
		String f1;
		f1 = t.iff(
					high(t),
					t.and(
						orig,
						t.U(
								t.rel(">", z0(t), "0"), 
								t.and(
										subf.interval(t),
										t.or(
												t.rel("<=", z0(t), String.valueOf(lowerbound())),
												t.and(
														t.rel("<=", z0(t), String.valueOf(lowerbound())),
														t.X(t.rel(">", z0(t), String.valueOf(lowerbound())))
												)
										)
								)
						)
				)
			);
		
		// Formula (16)
		
		String f2;
		f2 = t.iff(
					low(t), 
					t.or(
							t.and(
									t.rel("=", z1(t), "0"),
									t.X(
											t.U(
													t.rel(">", z1(t), "0"),
													t.and(
															subf.low(t),
															t.rel("=", z1(t), String.valueOf(lowerbound())),
															t.G(t.neg(subf.high(t)))
													)
											)
									)
							),
							t.and(orig, t.neg(high(t)))
					)
			);
		
		String f3;
		f3 = t.implies(
						t.and(
								subf.low(t),
								t.G(t.neg(subf.high(t)))
						),
						t.rel("=", z1(t), String.valueOf(lowerbound()))	
			);
		
					
		
		return t.and(super.clocksEventsConstraints(t), t.G(t.and(f1,f2,f3)));
		
	}
	




	@Override
	public int upperbound() {		
		return -1;
	}



	@Override
	public int lowerbound() {
		return a;
	}
	
}
	


