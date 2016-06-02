using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BehaviourNet.engine.metamodel
{
    public class ModelType
    {
        public string typeName { get; set; }

        public string classe { get; set; }

        public IList<string> allowedChild { get; set; }

        public MetaModel InstantiateModel() {
            var type = Type.GetType(BNConsts.META_MODEL_NAMESPACE + "." + classe);
            var model = (MetaModel)Activator.CreateInstance(type);
            model.type = this;
            return model;
        }

        public bool Allows(ModelType childType)
        {
            return allowedChild.Contains<string>(childType.typeName);
        }
    }
}
