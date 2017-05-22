
namespace VirtualCompiler.Components.variaveis
{
    public class Variavel
    {
        public int value { get; set; }
        private mnemonic.Block block;

        public Variavel(mnemonic.Block block)
        {
            this.block = block;
        }

    }
}
