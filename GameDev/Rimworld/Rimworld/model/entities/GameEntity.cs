using Rimworld.model.components;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld.model
{
    //gameentity: qualquer coisa spawnavel.
    public abstract class GameEntity
    {
        public GameEntity()
        {
           // components = new List<GameComponent>();
        }

        public IList<GameComponent> components { get; private set; }

        public void AddComponent(GameComponent component)
        {
            if (components == null)
            {
                components = new List<GameComponent>();
            }
            component.owner = this;
            components.Add(component);
        }

        public GameComponent GetComponent(GameConsts.COMPONENT_TYPE type)
        {
            if (components == null)
            {
                return null;
            }
            return components.Where(x => x.type == type).FirstOrDefault();
        }

       
    }
}
