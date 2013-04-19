package formulae.QTLI;

import java.util.List;

import delegateTranslator.CLTLTranslator;
import formulae.Formula;

public class QTLIFalse extends QTLIFormula {

	public QTLIFalse() {
		super(new String("FALSE"));
	}

	@Override
	public String translate(CLTLTranslator t) {
		
		return t.and(super.clocksEventsConstraints(t) + t.G(t.neg(t.and(point(t), interval(t)))));
	}

	@Override
	public List<Formula> subformulae() {
		return null;
	}

	@Override
	public QTLIFormula update(List<Formula> l) {
		return this;
	}

	@Override
	public Formula simplify() {
		return this;
	}

}
