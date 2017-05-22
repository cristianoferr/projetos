
using System.Collections.Generic;
using VirtualCompiler.Components.variaveis;
namespace VirtualCompiler.Components.mnemonic
{
    public class Block : BaseMnemonic
    {
        Dictionary<string, Variavel> variaveis { get; set; }
        IList<IMnemonic> code;
        public Block(Block parentBlock = null)
            : base(VCEnums.mnemonic.BLOCK, parentBlock)
        {
            variaveis = new Dictionary<string, Variavel>();
            code = new List<IMnemonic>();
        }

        public void DefineVariavel(string key)
        {
            if (!variaveis.ContainsKey(key))
            {
                Variavel var = new Variavel(this);
                variaveis.Add(key, var);
            }
        }

        public Variavel GetVariavel(string key)
        {
            if (!variaveis.ContainsKey(key))
            {
                if (parentBlock != null)
                {
                    return parentBlock.GetVariavel(key);
                }
                return null;
            }
            return variaveis[key];
        }

        internal void AddMnemonic(IMnemonic mnemonic)
        {
            code.Add(mnemonic);
        }


        public override IMnemonic ReadFromLine(string code)
        {
            return null;
        }

    }
}
