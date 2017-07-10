using Microsoft.VisualStudio.TestTools.UnitTesting;
using Rimworld.model;
using Rimworld.model.components;
using Rimworld.model.components.brain;
using Rimworld.model.entities;
using Rimworld.model.entities.physical;
using Rimworld.model.io;

namespace Rimworld.test
{
    [TestClass]
    public class MainTest
    {
        static DataHolder holder = new DataHolder();
        static GameFactory factory = new GameFactory(holder);

        [TestMethod]
        public void TestSpawnPawn()
        {
            HumanoidEntity pawn = factory.SpawnPawn(20);
            Assert.IsNotNull(pawn);
            Assert.IsTrue(holder.ContainsEntity(pawn));
            //Assert.IsTrue(pawn.name != null);

            /*  GameComponent brain = pawn.GetComponent(GameConsts.COMPONENT_TYPE.BRAIN);
              Assert.IsNotNull(brain);
              Assert.IsTrue(brain.type == GameConsts.COMPONENT_TYPE.BRAIN);
              Assert.IsTrue(brain.owner == pawn);*/

            Assert.IsNotNull(pawn.position);
            Assert.IsNotNull(pawn.dimension);

            /* TraitManagerComponent traits = pawn.GetComponent(GameConsts.COMPONENT_TYPE.TRAIT_MANAGER) as TraitManagerComponent;
             Assert.IsNotNull(traits);
             Assert.IsTrue(traits.traits.Count == GameConsts.allTraits.Count);
             int count = 0;
             foreach (Trait trait in traits.traits)
             {
                 Assert.IsTrue(trait.value >= 0 && trait.value <= GameConsts.MAX_TRAIT_VALUE);
                 count += trait.value;
             }
             Assert.IsTrue(count == GameConsts.HUMAN_TRAITS_POINTS);*/

        }

        [TestMethod]
        public void TestSpawnStockPile()
        {
            GEStockPile stockPile = factory.SpawnStockPile(10, 20, 5, 8);
            Assert.IsNotNull(stockPile);
            Assert.IsTrue(holder.ContainsEntity(stockPile));
            Assert.IsTrue(stockPile.position.x == 10);
            Assert.IsTrue(stockPile.position.y == 20);
            Assert.IsTrue(stockPile.dimension.width == 5);
            Assert.IsTrue(stockPile.dimension.height == 8);
        }

        [TestMethod]
        public void TestSpawnTownCenter()
        {
            GETownCenter townCenter = factory.SpawnTownCenter("town", 10, 20);
            Assert.IsNotNull(townCenter);
            Assert.IsTrue(holder.ContainsEntity(townCenter));
            Assert.IsTrue(townCenter.position.x == 10);
            Assert.IsTrue(townCenter.position.y == 20);
            Assert.IsTrue(townCenter.name == "town");
        }

        [TestMethod]
        public void TestTemplateInitializer()
        {
            Template templHuman = holder.templates.GetTemplate(GameConsts.TEMPL_HUMANOID);
            Assert.IsNotNull(templHuman);
            Assert.IsTrue(templHuman.name == GameConsts.TEMPL_HUMANOID);

            Template templHumanWithTag = holder.templates.GetTemplateWithTag(GameConsts.TAG_HUMANOID + " " + GameConsts.TAG_ORGANIC);
            Assert.IsNotNull(templHumanWithTag);
            Assert.IsTrue(templHumanWithTag == templHuman);

            Template templNullTag = holder.templates.GetTemplateWithTag(GameConsts.TAG_HUMANOID + " lixo " + GameConsts.TAG_ORGANIC);
            Assert.IsNull(templNullTag);

            HumanoidEntity human = templHuman.Spawn() as HumanoidEntity;
            Assert.IsNotNull(human);

            TraitManagerComponent traits = human.GetComponent(GameConsts.COMPONENT_TYPE.TRAIT_MANAGER) as TraitManagerComponent;
            Assert.IsNotNull(traits);
            Assert.IsTrue(traits.traits.Count == GameConsts.allTraits.Count);
            int count = 0;
            foreach (Trait trait in traits.traits)
            {
                Assert.IsTrue(trait.value >= 0 && trait.value <= GameConsts.MAX_TRAIT_VALUE);
                count += trait.value;
            }
            Assert.IsTrue(count > 0);
        }
    }
}
