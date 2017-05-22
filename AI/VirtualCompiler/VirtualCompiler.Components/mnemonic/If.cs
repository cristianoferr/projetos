
namespace VirtualCompiler.Components.mnemonic
{
    public class If : BaseMnemonic
    {
        public If(Block parentBlock = null)
            : base(VCEnums.mnemonic.IF, parentBlock)
        {
        }
        public override IMnemonic ReadFromLine(string code)
        {
            return null;
        }
    }
}
