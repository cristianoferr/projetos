using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner.mnemonico
{
    public abstract class RunnableMnemonic:Mnemonic
    {

        public RunnableMnemonic(Runner runner,string mnemonico )
            : base(runner, EnumTipoObjeto.CODE, mnemonico)
        {
            
        }


    }
}
