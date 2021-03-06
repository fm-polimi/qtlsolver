package formulae.QTLI;

import java.util.ArrayList;
import java.util.List;

import delegateTranslator.CLTLTranslator;
import formulae.Formula;

public class QTLIImplies extends QTLIFormula {

	
	private QTLIFormula subformula1;
	private QTLIFormula subformula2;
	
	public QTLIImplies(QTLIFormula subformula1, QTLIFormula subformula2){
		super(new String("(IMPL " + subformula1.strFormula() + " " + subformula2.strFormula()) + ")");
		this.subformula1 = subformula1;
		this.subformula2 = subformula2;
	}


	
	@Override
	public String translate(CLTLTranslator t) {
		
		QTLIFormula subf1 = subformula1;
		QTLIFormula subf2 = subformula2;
		
		String f1;
		f1 = t.and(
					t.iff(point(t), t.implies(subf1.point(t), subf2.point(t))),
					t.iff(interval(t), t.implies(subf1.interval(t), subf2.interval(t)))
			);
		
		return  t.and(super.clocksEventsConstraints(t), t.G(f1));
	}

	@Override
	public List<Formula> subformulae() {
		ArrayList<Formula> r = new ArrayList<Formula>();
		
		r.add(subformula1);
		r.add(subformula2);
		
		return r;
	}

	@Override
	public QTLIFormula update(List<Formula> l) {
		// if the list of subformulae are logically equivalent to the subformulae then we can safely replace them

		subformula1 = (QTLIFormula)l.get(0);	
		subformula2 = (QTLIFormula)l.get(1);	
		
		return this;
	}

	@Override
	public Formula simplify() {
		return new QTLIImplies((QTLIFormula)subformula1.simplify(),(QTLIFormula)subformula2.simplify());
	}
	
	
}
