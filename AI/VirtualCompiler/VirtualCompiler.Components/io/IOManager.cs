
using System;
using VirtualCompiler.Components.mnemonic;
namespace VirtualCompiler.Components.io
{
    public class IOManager
    {
        public IOManager(CodeManager codeMan)
        {
            this.codeMan = codeMan;
        }
        internal mnemonic.Block LoadFromFile(string filename, Block block)
        {
            System.IO.StreamReader file = new System.IO.StreamReader(filename);
            string line;
            int counter = 0;
            Block currBlock = block;
            while ((line = file.ReadLine()) != null)
            {
                counter++;
                IMnemonic mnemonic = GetFromLine(line.Trim(), currBlock);
                if (mnemonic == null)
                {
                    throw new Exception("Erro ao ler a linha: " + line);
                }
                block.AddMnemonic(mnemonic);
            }
            file.Close();

            return block;
        }



        private IMnemonic GetFromLine(string p, Block currBlock)
        {
            foreach (IMnemonic mnemonic in codeMan.mnemonics)
            {
                IMnemonic result = mnemonic.ReadFromLine(p);
                if (result != null)
                {
                    result.parentBlock = currBlock;
                    return result;
                }
            }
            return null;
        }

        public CodeManager codeMan { get; set; }

        //funcoes uteis

        public static string[] GetParams(string code)
        {
            string result = "";
            char separador = '§';
            bool ok = true;
            /* while (code.Length > 0)
             {

             }*/
            for (int i = 0; i < code.Length; i++)
            {
                if (ok)
                {
                    result += code[i];

                }
            }

            return result.Split(separador);
        }
    }
}
