using BehaviourNet.engine.nlp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.metamodel
{
    public class MetaModelManager
    {
        #region modelTypes
        IList<ModelType> modelTypes_ = new List<ModelType>();
        public IList<ModelType> modelTypes { get { return modelTypes_; } }
        public int modelTypesCount { get { return modelTypes_.Count; } }

        public ModelType GetModelDefinitionByType(string p)
        {
            return modelTypes_.FirstOrDefault<ModelType>(item => item.typeName == p);
        }

        internal void AddModelDefinition(metamodel.ModelType modelDef)
        {
            modelTypes_.Add(modelDef);
        }
        #endregion
    }
}
