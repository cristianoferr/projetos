using Microsoft.VisualStudio.TestTools.UnitTesting;
using VirtualCompiler.Components;
using VirtualCompiler.Components.io;
using VirtualCompiler.Components.mnemonic;
using VirtualCompiler.Components.variaveis;

namespace VirtualCompiler.Tests
{
    [TestClass]
    public class BasicTester
    {
        public static CodeManager program = new CodeManager();


        [TestMethod]
        public void TestFuncoesUteis()
        {
            string[] pars = IOManager.GetParams("var xyz;");
            Assert.IsNotNull(pars);
            Assert.IsTrue(pars.Length == 2);
            Assert.IsTrue(pars[0] == "var");
            Assert.IsTrue(pars[1] == "xyz");
        }

        [TestMethod]
        public void TestLoadSample1()
        {
            Block blocoSample1 = program.LoadFromFile("programs/sample1.txt");
            Assert.IsNotNull(blocoSample1);
        }

        [TestMethod]
        public void TestInicial()
        {
            IMnemonic mnemonico = program.Create(VCEnums.mnemonic.BLOCK);
            Assert.IsNotNull(mnemonico);
            Assert.IsTrue(mnemonico.tipo == VCEnums.mnemonic.BLOCK);

            Block rootBlock = program.CreateBlock();
            Assert.IsNotNull(rootBlock);
            Assert.IsTrue(rootBlock.tipo == VCEnums.mnemonic.BLOCK);

            rootBlock.DefineVariavel("var1");
            rootBlock.DefineVariavel("var2");

            Variavel var1 = rootBlock.GetVariavel("var1");
            Variavel var2 = rootBlock.GetVariavel("var2");
            Variavel var1b = rootBlock.GetVariavel("var1");
            Assert.IsNotNull(var1);
            Assert.IsNotNull(var2);
            Assert.IsFalse(var1 == var2);
            Assert.IsTrue(var1 == var1b);

            Block subBlock = program.CreateBlock(rootBlock);
            Assert.IsTrue(subBlock.parentBlock == rootBlock);
            Assert.IsTrue(rootBlock.parentBlock == null);

            subBlock.DefineVariavel("var1");
            Variavel var1Local = subBlock.GetVariavel("var1");
            Variavel var2Local = subBlock.GetVariavel("var2");
            Assert.IsNotNull(var1Local);
            Assert.IsFalse(var1 == var1Local);
            Assert.IsNotNull(var2);
            Assert.IsTrue(var2 == var2Local);

            //Testando valores
            var1.value = 10;
            int val1 = var1.value;
            Assert.IsTrue(val1 == 10);



        }
    }
}
