package parserHandler;

import java.util.Iterator;

import delegateTranslator.*;
import formulae.*;
import formulae.QTLI.QTLIFormula;

public class QTLIParserHandler extends TLParserHandler {

	@Override
	public String getHeader(CLTLTranslatorEnum e_transl) {
	
		String result = new String();
		switch(e_transl){
			case AE2ZOT : {
				result = result + new String("(asdf:operate 'asdf:load-op 'ae2zot) (use-package :trio-utils)\n");
				
				Iterator<Formula> f = super.iteratorOf();
				
				while (f.hasNext()){
					QTLIFormula qtlf = (QTLIFormula) f.next();
					if (qtlf.idFormula() != qtlf.isTheFormula)
						result = result + new String("(define-tvar " + qtlf.z0() + " *real*)\n(define-tvar " + qtlf.z1() + " *real*)\n");	
				}				
			};
			case NUZOT: ;		
		}
		return result;
	}

}
