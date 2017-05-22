using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CodeRunner;

using Utils;
using CodeRunner.mnemonico;

namespace CodeRunner.Tests
{
    [TestClass]
    public class CodeRunnerTests
    {

        [TestMethod]
        public void TestaCodeRunner()
        {
            Runner runner = new Runner();

            Programa prog = runner.CreatePrograma();
            Assert.IsNotNull(prog);
            Assert.IsTrue(prog.runner == runner);

        }

        [TestMethod]
        public void TestaCodigo()
        {
            Runner runner = new Runner();

            int valTeste = 10;
            Code var1 = runner.CreateCode(CodeEnums.VARIABLE);
            Assert.IsNotNull(var1);
            Code value = CriaValor(runner, valTeste);

            Code ifCode = runner.CreateCode(CodeEnums.IF);
            Assert.IsNotNull(ifCode);
            Assert.IsNotNull(ifCode.mnemonico);
            Assert.IsTrue(ifCode.mnemonico.name == CodeEnums.IF);

            VerificaAssign(runner, var1, value, ifCode, valTeste);
        }

        private static Code CriaValor(Runner runner, int valTeste)
        {
            Code value = runner.CreateCode(CodeEnums.VALUE);
            Assert.IsNotNull(value);
            value.Define(valTeste);
            Int32 valStored = value.GetValueAsInt();
            Assert.IsTrue(valStored == valTeste);
            return value;
        }

        private static void VerificaAssign(Runner runner, Code var1, Code value, Code ifCode,int valTeste)
        {
            Code assignCode = runner.CreateCode(CodeEnums.ASSIGN);
            Assert.IsNotNull(assignCode);
            Assert.IsNotNull(assignCode.mnemonico);
            Assert.IsTrue(assignCode.mnemonico.name == CodeEnums.ASSIGN);

            Assert.IsFalse(assignCode.DefineParam(0, ifCode));
            Assert.IsTrue(assignCode.DefineParam(0, var1));
            Assert.IsTrue(assignCode.DefineParam(1, value));

            Assert.IsTrue(assignCode.GetParam(0) == var1);
            Assert.IsTrue(assignCode.GetParam(1) == value);
            Assert.IsTrue(assignCode.GetParam(2) == null);

            Assert.IsTrue(var1.GetValueAsInt() == 0);
            assignCode.Run();
            Assert.IsTrue(var1.GetValueAsInt() == valTeste);

            Code var2 = runner.CreateCode(CodeEnums.VARIABLE);
        }

        /*    Assert.IsNotNull(fraseAnalisada.frase);
            Assert.IsTrue(fraseAnalisada.frase.tipoSujeito == NLPConsts.tipoSujeito.SUJEITO_SIMPLES, "TipoSujeito:" + fraseAnalisada.frase.tipoSujeito);
            Assert.IsNotNull(fraseAnalisada.frase.verbo);
        */

    }
}
