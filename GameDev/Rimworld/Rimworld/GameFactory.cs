using Rimworld.model.entities;
using Rimworld.model.entities.physical;

namespace Rimworld
{
    public class GameFactory
    {
        public GameFactory(DataHolder holder)
        {
            this.holder = holder;
        }
        public HumanoidEntity SpawnPawn(int pointsToSpend)
        {
            return holder.AddEntity(new HumanoidEntity()) as HumanoidEntity;
        }

        public model.entities.physical.GEStockPile SpawnStockPile(float x, float y, float width, float height)
        {
            GEStockPile pile = new GEStockPile();
            pile.position.x = x;
            pile.position.y = y;
            pile.dimension.width = width;
            pile.dimension.height = height;
            return holder.AddEntity(pile) as GEStockPile;

        }

        public GETownCenter SpawnTownCenter(string name, int x, int y)
        {
            GETownCenter pile = new GETownCenter();
            pile.name = name;
            pile.position.x = x;
            pile.position.y = y;
            holder.AddEntity(pile);
            return pile;
        }

        public DataHolder holder { get; set; }
    }
}
