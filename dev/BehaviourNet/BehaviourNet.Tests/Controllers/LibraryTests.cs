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

namespace BehaviourNet.Tests.Controllers
{
    [TestClass]
    public class LibraryTests
    {
        const string SISTEMA_TYPE = "SistemaWeb";

        

        [TestMethod]
        public void TestaCargaMetaModelsDefinitions()
        {
            BehaviourLoader load = new BehaviourLoader();
            Assert.IsNull(load.rootModel);
            MetaModelManager mm = load.metaModelManager;
            Assert.IsNotNull(mm);
            Assert.IsTrue(mm.modelTypesCount == 0);
            load.LoadDefaultDictionary();
            Assert.IsTrue(mm.modelTypesCount > 0);

            ModelType type0 = mm.modelTypes[0];
            Assert.IsNotNull(type0);
            MetaModel model0 = type0.InstantiateModel();
            Assert.IsNotNull(model0);
            Assert.IsTrue(model0.type==type0);

            ModelType typeNotAllowed=null;
            ModelType typeAllowed = null;
            foreach (ModelType mt in mm.modelTypes)
            {
                if (!type0.allowedChild.Contains(mt.typeName))
                {
                    typeNotAllowed = mt;
                }
                if (type0.allowedChild.Contains(mt.typeName))
                {
                    typeAllowed = mt;
                }
            }

            Assert.IsNotNull(typeNotAllowed);
            Assert.IsNotNull(typeAllowed);

            Assert.IsTrue(type0.Allows(typeAllowed));
            Assert.IsFalse(type0.Allows(typeNotAllowed));

        }

        [TestMethod]
        public void TestaCargaWords()
        {
            BehaviourLoader load = new BehaviourLoader();
            Assert.IsNull(load.rootModel);
            WordManager wm=load.wordManager;
            Assert.IsNotNull(wm);
            Assert.IsTrue(wm.wordsCount == 0);
            load.LoadDefaultDictionary();
            Assert.IsTrue(wm.wordsCount> 0);

            Assert.IsTrue(wm.IsWordOfType("ter",BNConsts.WORD_VERBO));
            Assert.IsTrue(wm.IsWordOfType("TER", BNConsts.WORD_VERBO));
            Assert.IsFalse(wm.IsWordOfType("ter", BNConsts.WORD_ARTIGO));
            Assert.IsTrue(wm.IsWordOfType("chamará", BNConsts.WORD_VERBO));
            Assert.IsTrue(wm.IsWordOfType("CHAMARÁ", BNConsts.WORD_VERBO));
            Assert.IsFalse(wm.IsWordOfType("será", BNConsts.WORD_ARTIGO));
            Assert.IsTrue(wm.IsWordOfType("se", BNConsts.WORD_PRONOME));
            Assert.IsTrue(wm.IsWordOfType("SERÁ", BNConsts.WORD_PRONOME));


        }

        [TestMethod]
        public void TestaCargaFrases()
        {
            BehaviourLoader load = new BehaviourLoader();
            PhraseManager fm = load.phraseManager;
            Assert.IsNotNull(fm);
            Assert.IsTrue(fm.phrasesCount == 0);
            load.LoadDefaultDictionary();
            Assert.IsTrue(fm.phrasesCount >= 0);

            Phrase fraseOriginal= fm.GetPhraseByName("defineVerbo");
            Assert.IsNotNull(fraseOriginal);
            Assert.IsTrue(fraseOriginal.rulesCount > 0);
            Assert.IsTrue(fraseOriginal.actionCount > 0);

            PhraseInterpreter frase1 = fm.GetPhraseInterpreter("O sistema se chamará XYZ");
            Assert.IsNotNull(frase1.phraseSource==fraseOriginal);
            Assert.IsTrue(frase1.phraseSource == fraseOriginal);

            PhraseInterpreter frase2 = fm.GetPhraseInterpreter("O sistema chamará \"Sistema de Cadastro de Alunos\"");
            Assert.IsNotNull(frase2.phraseSource == fraseOriginal);
            Assert.IsTrue(frase2.phraseSource == fraseOriginal);

        }

        //Testar uma forma do bdd lembrar do que está sendo feito, ex:"o sistema seguirá o template XXX", sendo que não existe o model "sistema" ainda
        [TestMethod]
        public void TestaAssignments()
        {
            BehaviourLoader load = new BehaviourLoader();
            Assert.IsNull(load.rootModel);
            load.LoadDefaultDictionary();
            load.AddMetaData("O sistema seguirá o template " + SISTEMA_TYPE);
            Assert.IsNotNull(load.rootModel);
            Assert.IsTrue(load.rootModel.type.typeName == SISTEMA_TYPE);
            load.AddMetaData("O sistema se chamará \"AlunoWeb\"");
            Assert.IsTrue(load.rootModel.name == "AlunoWeb");

        }

        [TestMethod]
        public void TestaCarga()
        {
            BehaviourLoader load = new BehaviourLoader();
            load.LoadProjectDefinitionsFrom("samples/sample_project.txt");
            Assert.IsNotNull(load.rootModel);
            // Assert
            //  Assert.AreEqual("Your application description page.", result.ViewBag.Message);
        }


    }
}
