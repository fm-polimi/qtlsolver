package formulae.cltloc.relations;

import formulae.cltloc.atoms.CLTLValueAtom;
import formulae.cltloc.atoms.CLTLVariable;

public class CLTLocGEQRelation extends CLTLocRelation{
	public CLTLocGEQRelation(CLTLVariable subformula1, CLTLValueAtom subformula2) {
		super(subformula1, subformula2, ">=");
	}
}
