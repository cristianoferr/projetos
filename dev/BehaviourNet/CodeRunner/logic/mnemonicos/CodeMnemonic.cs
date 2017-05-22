using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CodeRunner.logic.mnemonicos;

namespace CodeRunner.mnemonico
{

    public class CodeMnemonic : Mnemonic
    {

        public CodeMnemonic(Runner runner)
            : base(runner, EnumTipoObjeto.CODE)
        {
            AddValidParam(EnumTipoObjeto.VALUE, EnumTipoObjeto.CODE, EnumTipoObjeto.BOOLEAN, EnumTipoObjeto.VARIABLE);
        }


    }
}
