using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld
{
   public class Utils
    {
       static Random rnd=new Random();
        internal static int Random(int p1, int p2)
        {
            return rnd.Next(p1, p2);
        }
    }
}
