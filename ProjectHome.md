Given a formula of the Quantitative Temporal Logic (QTL) or its subclasses, like Metric Interval Temporal Logic (MITL), the tool produces an equisatisfiable CLTL-over-clocks formula.
The obtained formula can be solved by any CLTL-over-clocks solver (e.g., **ae2zot**).


The tool is based on the following papers:

  1. A Tool for Deciding the Satisfiability of Continuous-time Metric Temporal Logic http://bit.ly/16eisdU
  1. On the Satisfiability of Metric Temporal Logics over the Reals http://bit.ly/Xsg6UZ
  1. Deciding Continuous-time Metric Temporal Logic with Counting Modalities http://bit.ly/11sODlg




# Overview #

The tool translates MITL or QTL into CLTL-over-clocks, which
can be checked for satisfiability by **ae2zot**.
CLTL-over-clocks is an extension of LTL where formulae may contain variables behaving as clocks which can be used to measure the time elapsing between events.
The toolkit has 3 layers where CLTL-over-clocks is the intermediate step between
SMT-solvers and other temporal formalisms (such as TA o TPN) that can be reduced to CLTL-over-clocks.
Combining **qtlSolver** and **ae2Zot** not only supports (bounded) satisfiability verification of different languages, but it also
allows the expression of different degrees of abstraction.
While QTL and MITL abstract away the notion of clocks,
they are instead explicit in CLTL-over-clocks (see (1)) and actually available
to a user.
The layered structure allows the resolution
of a formula to be compliant also with constraints imposed at
lower layers, for instance by adding at the CLTL-over-clocks
layer some extra formula limiting the set of valid models
(e.g., by discarding certain edges of some events or by adding
particular timing requirements).
The SMT solver may be used to add further constraints, e.g., to force
the occurrence of a proposition or of a certain clock value at
a specific discrete position of the finite model.

The current implementation of qtlsolver supports various reductions.

  * It realizes the MITL-to-CLTL-over-clock translation tailored to left-closed/right-open signals (see (1))
  * It also implements a translation from a generalized version of QTL to CLTL-over-clocks (see (2))

The second translation assumes that signals are _finitely variable_, yet no special shape for signals is imposed.
It natively supports operators **F**<0,b> and **G**<0,b> (and their past counterparts), where the bounds can be either included or excluded.
These operators allow us to define concisely **F**<a,b> and **G**<a,b> as abbreviations.


A brief howto helping the installation of the tool can be found at [QuickStart](QuickStart.md) while some nice examples are available from [Samples](Samples.md).


**Remark:** the tool currently supports Counting modality `C_n` (the alpha version is available from Download section)!
