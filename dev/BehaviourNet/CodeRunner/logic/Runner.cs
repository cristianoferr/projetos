using CodeRunner.mnemonico;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CodeRunner
{
    public class Runner
    {
        Dictionary<string, Mnemonic> codigos = new Dictionary<string, Mnemonic>();


        public Runner()
        {
            InitCodigos();
        }

        private void InitCodigos()
        {
            codigos.Add(CodeEnums.ASSIGN, new AssignMnemonic(this));
            codigos.Add(CodeEnums.IF, new IFMnemonic(this));
            codigos.Add(CodeEnums.VARIABLE, new VariableMnemonic(this));
            codigos.Add(CodeEnums.VALUE, new ValueMnemonic(this));
        }
        public Programa CreatePrograma()
        {
            Programa prog = new Programa(this);
            return prog;
        }

        public Code CreateCode(string mnemonico)
        {
            Code instance = codigos[mnemonico].CreateCode();
            return instance;
        }
    }
}
