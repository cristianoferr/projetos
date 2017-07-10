using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld.model.entities
{
    public class PhysicalEntity : GameEntity
    {
        public PhysicalEntity(string name)
            : base()
        {
            this.nane = name;
            position = new Position();
            dimension = new Dimension();
        }

        public string nane { get; set; }
        public Position position { get; private set; }
        public Dimension dimension { get; private set; }
    }
}
