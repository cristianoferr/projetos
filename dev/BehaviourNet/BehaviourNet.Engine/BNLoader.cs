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

        private IList<BNModel> models_;
        protected IList<BNModel> models
        {
            get
            {
                if (models_ == null)
                {
                    models_ = new List<BNModel>();
                }
                return models_;
            }
        }
    }
}
