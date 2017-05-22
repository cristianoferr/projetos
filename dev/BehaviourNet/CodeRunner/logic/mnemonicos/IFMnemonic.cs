using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner.mnemonico
{
    public  class IFMnemonic:Mnemonic
    {

        public IFMnemonic(Runner runner):base(runner,EnumTipoObjeto.CODE,CodeEnums.IF)
        {
            
        }


    }
}
