using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LinguaNatural
{
    public class NLPLoader
    {
        public NLPLoader()
        {
            new BPLanguageLoader().LoadFrom(this, "xml/nlpdata.xml");

        }
      
    }
}
