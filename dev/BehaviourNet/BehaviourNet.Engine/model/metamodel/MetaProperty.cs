using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BehaviourNet.engine.nlp;
using BehaviourNet.utils;

namespace BehaviourNet.engine.metamodel
{
    public class MetaProperty
    {
        public string name { get; set; }
        public BNEnums.PropertyType type { get; set; }

        public object value { get; set; }

        public override string ToString()
        {
            return value.ToString();
        }
    }
}
