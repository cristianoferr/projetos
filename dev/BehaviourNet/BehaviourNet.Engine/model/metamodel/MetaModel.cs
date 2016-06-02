using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BehaviourNet.engine.metamodel
{
    public class MetaModel
    {

        public string name { get{
            return properties[BNConsts.PROPERTY_NAME].ToString();
        }
            set {
                properties[BNConsts.PROPERTY_NAME].value = value;
        }
             }
        public ModelType type { get; set; }

        IDictionary<string, MetaProperty> properties = new Dictionary<string, MetaProperty>();

        public void AddProperty(string name, MetaProperty prop)
        {
            if (properties.ContainsKey(name))
            {
                properties.Remove(name);
            }
            properties.Add(name, prop);
        }

        public MetaProperty GetProperty(string name)
        {
            return properties[name];
        }
    }
}
