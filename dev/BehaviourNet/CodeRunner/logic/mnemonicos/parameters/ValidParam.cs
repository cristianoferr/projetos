using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner.logic.mnemonicos
{
    class ValidParam
    {
        private IList<EnumTipoObjeto> tipos=new List<EnumTipoObjeto>();

        public ValidParam(EnumTipoObjeto t1, EnumTipoObjeto t2 = EnumTipoObjeto.EMPTY, EnumTipoObjeto t3 = EnumTipoObjeto.EMPTY, EnumTipoObjeto t4 = EnumTipoObjeto.EMPTY, EnumTipoObjeto t5 = EnumTipoObjeto.EMPTY)
        {
            tipos.Add(t1);
            if (t2 != EnumTipoObjeto.EMPTY) tipos.Add(t2);
            if (t3 != EnumTipoObjeto.EMPTY) tipos.Add(t3);
            if (t4 != EnumTipoObjeto.EMPTY) tipos.Add(t4);
            if (t5 != EnumTipoObjeto.EMPTY) tipos.Add(t5);
        }

        internal bool IsParamValid(mnemonico.Code code)
        {
            for (int i = 0; i < tipos.Count; i++)
            {
                if (code.mnemonico.tipo == tipos[i]) { return true; }
            }
            return false;
        }
    }
}
