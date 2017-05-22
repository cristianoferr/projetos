
using VirtualCompiler.Components.mnemonic;
namespace VirtualCompiler.Components
{
    public interface IMnemonic
    {
        VCEnums.mnemonic tipo { get; set; }
        Block parentBlock { get; set; }

        //retorna o mnemonic preenchido se o 'code' for interpretavel, null se não
        IMnemonic ReadFromLine(string code);
    }
}
