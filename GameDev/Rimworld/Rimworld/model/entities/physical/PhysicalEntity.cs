
namespace Rimworld.model.entities
{
    public class PhysicalEntity : GameEntity
    {
        public PhysicalEntity()
            : base()
        {
            position = new Position(0, 0, 0);
            dimension = new Dimension(1, 1);
        }

        public string name { get; set; }
        public Position position { get; private set; }
        public Dimension dimension { get; private set; }
    }
}
