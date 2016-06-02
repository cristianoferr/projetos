using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BehaviourNet.engine.nlp
{
    class WordRule
    {
        public string expected { get; set; }

        public bool isTopic { get; set; }

        public IList<string> validWords { get; set; }

        public bool optional { get; set; }

        public string toVar { get; set; }
    }
}
