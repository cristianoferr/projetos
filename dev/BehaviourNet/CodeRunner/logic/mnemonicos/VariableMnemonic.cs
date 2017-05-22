using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CodeRunner.logic.mnemonicos;

namespace CodeRunner.mnemonico
{

    public class VariableMnemonic : Mnemonic
    {
        int varCount = 0;

        public VariableMnemonic(Runner runner)
            : base(runner, EnumTipoObjeto.VARIABLE, CodeEnums.VARIABLE)
        {
            AddValidParam(EnumTipoObjeto.VALUE);
        }

        internal override Code CreateCode()
        {
            varCount++;
            return new ValueCode(this,"Var_"+varCount);
        }

    }
}
