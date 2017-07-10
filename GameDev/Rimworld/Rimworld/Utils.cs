using System;

namespace Rimworld
{
    public class Utils
    {
        static Random rnd = new Random();
        internal static int Random(float p1, float p2)
        {
            return rnd.Next((int)p1, (int)p2);
        }
    }
}
