
using VirtualCompiler.Components.io;
namespace VirtualCompiler.Components.mnemonic
{
    public class Var : BaseMnemonic
    {
        public Var(Block parentBlock = null)
            : base(VCEnums.mnemonic.VAR, parentBlock)
        {
        }

        public override IMnemonic ReadFromLine(string code)
        {
            if (code.StartsWith("var "))
            {
                Var v = new Var();
                v.name = IOManager.GetParams(code)[1];
                return v;
            }
            return null;
        }



        public string name { get; set; }
    }
}
