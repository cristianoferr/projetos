using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BehaviourNet.Engine;

namespace BehaviourNet.engine
{
    public class BNLoader
    {
        public void LoadDefinitionsFrom(string file)
        {
            throw new NotImplementedException();
        }

        public int metaModelsCount { get { return models.Count; } }

        private IList<BNEntity> models_;
        protected IList<BNEntity> models
        {
            get
            {
                if (models_ == null)
                {
                    models_ = new List<BNEntity>();
                }
                return models_;
            }
        }

        public void LoadDefaultDictionary()
        {
            throw new NotImplementedException();
        }

        public BNEntity currentTopic { get; set; }

        public void AddMetaData(string metaData)
        {
            throw new NotImplementedException();
        }
    }
}
