using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner.mnemonico
{
    //armazena um valor imutável
    public class ValueCode:Code
    {

        public ValueCode(Mnemonic mnemonic, string name=null)
            : base(mnemonic)
        {
            this.name = name;
        }
        private object valor;

        public override void Define(object p)
        {
            valor = p;
        }

        public override int GetValueAsInt()
        {
            if (valor == null) return 0;
            return (int)valor;
        }

        public override object GetValue()
        {
            return valor;
        }

        public string name { get; set; }
    }
}
