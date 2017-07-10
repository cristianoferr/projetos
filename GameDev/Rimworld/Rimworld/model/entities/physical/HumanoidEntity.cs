using Rimworld.model.components;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld.model.entities
{
    public class HumanoidEntity:CreatureEntity
    {
        public HumanoidEntity(string name):base(name){
            AddComponent(new TraitManagerComponent(GameConsts.HUMAN_TRAITS_POINTS));
        }
    }
}
