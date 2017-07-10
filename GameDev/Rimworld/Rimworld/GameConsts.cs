using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rimworld.model
{
   public class GameConsts
    {
       public enum JOBS
       {
           UNDEFINED=0,MINER=1,HAULER=2,FARMER=3,COOKER=4
       }

       public enum COMPONENT_TYPE
       {
           BRAIN = 1, STOMACH = 2, LEG = 3, ARM = 4,
           TRAIT_MANAGER=5
       }

       public enum TRAITS
       {
           SHOOTING, MELEE, CRAFTING, SMITH,
           SOCIAL
       }

       public static IList<TRAITS>allTraits=new List<TRAITS>();
       static GameConsts(){
           allTraits.Add(TRAITS.CRAFTING);
           allTraits.Add(TRAITS.MELEE);
           allTraits.Add(TRAITS.SHOOTING);
           allTraits.Add(TRAITS.SMITH);
           allTraits.Add(TRAITS.SOCIAL);
              
        }


       public const int HUMAN_TRAITS_POINTS = 20;

       public const int MAX_TRAIT_VALUE = 10;
    }
}
