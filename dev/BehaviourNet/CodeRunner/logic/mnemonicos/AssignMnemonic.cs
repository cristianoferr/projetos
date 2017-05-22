using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CodeRunner.logic.mnemonicos;

namespace CodeRunner.mnemonico
{

    public class AssignMnemonic : RunnableMnemonic
    {

        public AssignMnemonic(Runner runner)
            : base(runner, CodeEnums.ASSIGN)
        {
            // <var>=<valor>
            AddValidParam(EnumTipoObjeto.VARIABLE);
            AddValidParam(EnumTipoObjeto.VALUE);
        }

        internal override bool Run(Code code)
        {
            Code var = code.GetParam(0);
            Code val = code.GetParam(1);
            var.Define(val.GetValue());
            return true;
        }

    }
}
