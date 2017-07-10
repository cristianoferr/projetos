using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Rimworld.model;
using Rimworld.model.components;
using Rimworld.model.entities;
using Rimworld.model.entities.physical;
using Rimworld.model.components.brain;

namespace Rimworld.test
{
    [TestClass]
    public class MainTest
    {
        [TestMethod]
        public void TestSpawnPawn()
        {
            HumanoidEntity pawn = GameFactory.SpawnPawn(20);
            Assert.IsNotNull(pawn);
            Assert.IsTrue(pawn.nane != null);

            GameComponent brain=pawn.GetComponent(GameConsts.COMPONENT_TYPE.BRAIN);
            Assert.IsNotNull(brain);
            Assert.IsTrue(brain.type == GameConsts.COMPONENT_TYPE.BRAIN);
            Assert.IsTrue(brain.owner == pawn);

            Assert.IsNotNull(pawn.position);
            Assert.IsNotNull(pawn.dimension);

            TraitManagerComponent traits = pawn.GetComponent(GameConsts.COMPONENT_TYPE.TRAIT_MANAGER) as TraitManagerComponent;
            Assert.IsNotNull(traits);
            Assert.IsTrue(traits.traits.Count == GameConsts.allTraits.Count);
            int count = 0;
            foreach (Trait trait in traits.traits)
            {
                Assert.IsTrue(trait.value >= 0 && trait.value <= GameConsts.MAX_TRAIT_VALUE);
                count += trait.value;
            }
            Assert.IsTrue(count == GameConsts.HUMAN_TRAITS_POINTS);
            
        }

        [TestMethod]
        public void TestSpawnStockPile()
        {
            GEStockPile stockPile = GameFactory.SpawnStockPile(10,20,5,8);
            Assert.IsNotNull(stockPile);
            Assert.IsTrue(stockPile.position.x == 10);
            Assert.IsTrue(stockPile.position.y == 20);
            Assert.IsTrue(stockPile.dimension.width == 5);
            Assert.IsTrue(stockPile.dimension.height == 8);
        }
    }
}
