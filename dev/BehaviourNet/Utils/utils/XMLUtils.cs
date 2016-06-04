using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{
    public class XMLUtils
    {

        public static string PegaValorString(System.Xml.Linq.XElement elCond, string param, string val)
        {
            if (elCond.Attribute(param) != null)
            {
                val = elCond.Attribute(param).Value;
            }
            return val;
        }

        public static int PegaValorInt(System.Xml.Linq.XElement elCond, string param, int val)
        {
            if (elCond.Attribute(param) != null)
            {
                val = Int32.Parse(elCond.Attribute(param).Value);
            }
            return val;
        }
    }
}
