using Rimworld.logic;
using Rimworld.model;
using System.Collections.Generic;

namespace Rimworld
{
    public class DataHolder
    {
        public DataHolder()
        {
            entities = new List<GameEntity>();
            templateInitializer = new TemplateInitializer(this);
        }
        public IList<GameEntity> entities { get; private set; }

        internal GameEntity AddEntity(GameEntity entity)
        {
            entities.Add(entity);
            return entity;
        }

        public bool ContainsEntity(GameEntity pawn)
        {
            return entities.Contains(pawn);
        }

        public TemplateInitializer templateInitializer { get; private set; }

        public model.io.Templates templates { get; set; }
    }
}
