using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CodeRunner.logic.mnemonicos;

namespace CodeRunner.mnemonico
{

    public class ValueMnemonic : Mnemonic
    {

        public ValueMnemonic(Runner runner)
            : base(runner, EnumTipoObjeto.VALUE)
        {
            AddValidParam(EnumTipoObjeto.VALUE);
        }

        internal override Code CreateCode()
        {
            return new ValueCode(this);
        }

    }
}
