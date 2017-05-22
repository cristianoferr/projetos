using CodeRunner.logic.mnemonicos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner.mnemonico
{
    public abstract class Mnemonic
    {
        public Runner runner {get;set;}
        public string name { get; set; }
        public EnumTipoObjeto tipo { get; set; }

        //Contém relação de parâmetros válidos
        IList<ValidParam> parms = new List<ValidParam>();

        public Mnemonic(Runner runner, EnumTipoObjeto enumTipoObjeto, string mnemonico=null)
        {
            this.runner = runner;
            this.tipo = enumTipoObjeto;
            this.name = mnemonico;
        }


        protected void AddValidParam(EnumTipoObjeto t1, EnumTipoObjeto t2 = EnumTipoObjeto.EMPTY, EnumTipoObjeto t3 = EnumTipoObjeto.EMPTY, EnumTipoObjeto t4 = EnumTipoObjeto.EMPTY, EnumTipoObjeto t5 = EnumTipoObjeto.EMPTY)
        {
            parms.Add(new ValidParam(t1,t2,t3,t4,t5));
        }

        internal virtual bool Run(Code code)
        {
            throw new NotImplementedException();
        }

        internal virtual Code CreateCode()
        {
            return new Code(this);
        }

        internal bool IsParamValid(int p, Code code)
        {
            if (p >= parms.Count) { return false; }
            return parms[p].IsParamValid(code);
        }
    }
}
