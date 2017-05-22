using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner.mnemonico
{
    public class Code
    {
        public Runner runner { get; set; }
        public Mnemonic mnemonico { get; set; }
        public EnumTipoObjeto tipo { get; set; }

        IList<Code> values = new List<Code>();

        public Code(Mnemonic mnemonico)
        {
            this.mnemonico = mnemonico;
        }

        public virtual bool Run()
        {
            return mnemonico.Run(this);
        }




        public virtual void Define(object p)
        {
            throw new NotImplementedException();
        }

        public virtual int GetValueAsInt()
        {
            throw new NotImplementedException();
        }
        public virtual object GetValue()
        {
            throw new NotImplementedException();
        }
        
        public bool DefineParam(int p, Code code)
        {
            if (!mnemonico.IsParamValid(p, code)) { return false; }

            while (values.Count <= p) { values.Add(null); }
            values[p] = code;

            return true;
        }

        public Code GetParam(int p)
        {
            if (p >= values.Count) return null;
            return values[p];
        }
    }
}
