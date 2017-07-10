using Rimworld.model.components.brain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld.model.components
{
    public class TraitManagerComponent : GameComponent
    {
        public TraitManagerComponent(int pointsToDistribute)
            : base(GameConsts.COMPONENT_TYPE.TRAIT_MANAGER)
        {
            traits = new List<Trait>();
            InitTraits(pointsToDistribute);
        }

        private void InitTraits(int pointsToDistribute)
        {
            foreach (GameConsts.TRAITS trait in GameConsts.allTraits)
            {
                traits.Add(new Trait(trait, 0));
            }
            while (pointsToDistribute > 0)
            {
                Trait trait = traits[Utils.Random(0, traits.Count)];
                if (trait.value < GameConsts.MAX_TRAIT_VALUE)
                {
                    pointsToDistribute--;
                    trait.value++;
                }
            }
        }

        public IList<Trait> traits { get; private set; }
        
    }
}
