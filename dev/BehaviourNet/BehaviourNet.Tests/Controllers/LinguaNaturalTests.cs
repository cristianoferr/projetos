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
    public class LinguaNaturalTests
    {

        [TestMethod]
        public void TestaLinguaNatural()
        {
            BehaviourLoader load = new BehaviourLoader();
            load.LoadProjectDefinitionsFrom("samples/sample_project.txt");
            Assert.IsNotNull(load.rootModel);
            // Assert
            //  Assert.AreEqual("Your application description page.", result.ViewBag.Message);
        }


    }
}
