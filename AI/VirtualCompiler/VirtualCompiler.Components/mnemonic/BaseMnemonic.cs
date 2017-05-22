
namespace VirtualCompiler.Components.mnemonic
{
    public abstract class BaseMnemonic : IMnemonic
    {
        public BaseMnemonic(VCEnums.mnemonic type, Block parentBlock = null)
        {
            this.tipo = type;
            this.parentBlock = parentBlock;
        }

        public VCEnums.mnemonic tipo { get; set; }
        public Block parentBlock { get; set; }

        public abstract IMnemonic ReadFromLine(string code);
    }
}
