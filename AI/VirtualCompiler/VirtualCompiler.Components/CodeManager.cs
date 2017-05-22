
using System;
using System.Collections.Generic;
using VirtualCompiler.Components.io;
using VirtualCompiler.Components.mnemonic;
namespace VirtualCompiler.Components
{
    public class CodeManager
    {
        IOManager ioMan;
        public IList<IMnemonic> mnemonics { get; set; }
        public CodeManager()
        {
            ioMan = new IOManager(this);
            StartMnemonics();

        }

        private void StartMnemonics()
        {
            mnemonics = new List<IMnemonic>();
            mnemonics.Add(new Block());
        }
        public IMnemonic Create(VCEnums.mnemonic mnemonic, Block parentBlock = null)
        {
            switch (mnemonic)
            {
                case VCEnums.mnemonic.BLOCK: return new Block(parentBlock);
            }
            throw new Exception("Mnemonic desconhecido: " + mnemonic);
        }

        public Block CreateBlock(Block parentBlock = null)
        {
            return Create(VCEnums.mnemonic.BLOCK, parentBlock) as Block;
        }



        public Block LoadFromFile(string filename)
        {
            Block block = CreateBlock();
            return ioMan.LoadFromFile(filename, block);
        }
    }
}
