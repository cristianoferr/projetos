using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using BehaviourNet;
using BehaviourNet.Controllers;
using BehaviourNet.engine;

namespace BehaviourNet.Tests.Controllers
{
    [TestClass]
    public class LibraryTests
    {
     /*   [TestMethod]
        public void Index()
        {
           // Arrange
            HomeController controller = new HomeController();

            // Act
            ViewResult result = controller.Index() as ViewResult;

            // Assert
            Assert.IsNotNull(result);
        }*/

        [TestMethod]
        public void TestaCarga()
        {
            BNLoader load = new BNLoader();
            Assert.IsTrue(load.metaModelsCount == 0);
            load.LoadDefinitionsFrom("sample_project.txt");
            Assert.IsTrue(load.metaModelsCount >= 0);
            // Assert
          //  Assert.AreEqual("Your application description page.", result.ViewBag.Message);
        }

     
    }
}
