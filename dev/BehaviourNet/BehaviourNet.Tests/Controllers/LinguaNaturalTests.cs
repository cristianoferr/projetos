using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using BehaviourNet;
using BehaviourNet.Controllers;
using BehaviourNet.engine;
using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using BehaviourNet.engine.metamodel;
using LinguaNatural;
using LinguaNatural.model;
using Utils;

namespace BehaviourNet.Tests.Controllers
{
    [TestClass]
    public class LinguaNaturalTests
    {
        
        [TestMethod]
        public void TestaStringsUtils()
        {
            string frase = "a b c \"abcdef\" \"frase com espaço\" fim";
            List<string> stringsNaFrase = new List<string>();
            string fraseFinal = StringUtils.SeparaStrings(frase, stringsNaFrase);

            Assert.AreEqual(fraseFinal, "a b c string_0 string_1 fim");
            Assert.IsTrue(stringsNaFrase.Contains("abcdef"));
            Assert.IsTrue(stringsNaFrase.Contains("frase com espaço"));

        }

        [TestMethod]
        public void TestaLinguaNatural()
        {
            NLPLoader load = new NLPLoader();
            Assert.IsNotNull(load.condicionalManager);
            Assert.IsTrue(load.condicionalManager.condicionaisCount>0);
            // Assert
            //  Assert.AreEqual("Your application description page.", result.ViewBag.Message);

            Oracao fraseAnalisada = load.Interpretar("eu sou uma pessoa");
            Assert.IsNotNull(fraseAnalisada);
            Assert.IsNotNull(fraseAnalisada.frase);
            Assert.IsTrue(fraseAnalisada.frase.tipoSujeito == NLPConsts.tipoSujeito.SUJEITO_SIMPLES, "TipoSujeito:" + fraseAnalisada.frase.tipoSujeito);
            Assert.IsNotNull(fraseAnalisada.frase.verbo);
            Assert.IsTrue(fraseAnalisada.frase.verbo.palavra=="ser");

            fraseAnalisada = load.Interpretar("nós queremos um sistema");
            Assert.IsNotNull(fraseAnalisada);
            Assert.IsNotNull(fraseAnalisada.frase);
            Assert.IsTrue(fraseAnalisada.frase.tipoSujeito == NLPConsts.tipoSujeito.SUJEITO_SIMPLES, "TipoSujeito:" + fraseAnalisada.frase.tipoSujeito);
            Assert.IsNotNull(fraseAnalisada.frase.verbo);
            Assert.IsTrue(fraseAnalisada.frase.verbo.palavra == "querer");
        }


    }
}
