using Rimworld.model.components;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld.model.entities
{
    //C vvvxxxx
    public class CreatureEntity:PhysicalEntity
    {
        public CreatureEntity(string name)
            : base(name)
        {
            AddComponent(new BrainComponent());
            AddComponent(new StomachComponent());
        }

        
    }
}
