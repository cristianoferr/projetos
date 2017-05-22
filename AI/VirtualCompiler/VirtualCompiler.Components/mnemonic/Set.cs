
namespace VirtualCompiler.Components.mnemonic
{
    public class Set : BaseMnemonic
    {
        public Set(Block parentBlock = null)
            : base(VCEnums.mnemonic.SET, parentBlock)
        {
        }
        public override IMnemonic ReadFromLine(string code)
        {
            return null;
        }
    }
}
