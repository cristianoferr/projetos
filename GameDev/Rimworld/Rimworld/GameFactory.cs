using Rimworld.model;
using Rimworld.model.entities;
using Rimworld.model.entities.physical;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld
{
    public class GameFactory
    {
        public static HumanoidEntity SpawnPawn(int pointsToSpend)
        {
            return new HumanoidEntity("Noname");
        }

        public static model.entities.physical.GEStockPile SpawnStockPile(float x,float y,float width,float height)
        {
            GEStockPile pile = new GEStockPile();
            pile.position.x = x;
            pile.position.y = y;
            pile.dimension.width = width;
            pile.dimension.height = height;
            return pile;

        }
    }
}
