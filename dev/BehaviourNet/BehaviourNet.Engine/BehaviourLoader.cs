using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BehaviourNet.engine.nlp;
using BehaviourNet.engine.metamodel;
using BehaviourNet.engine.business;

namespace BehaviourNet.engine
{
    public class BehaviourLoader
    {

        PhraseManager phraseManager_ = new PhraseManager();
        public PhraseManager phraseManager { get { return phraseManager_; } }

        MetaModelManager metaModelManager_ = new MetaModelManager();
        public MetaModelManager metaModelManager { get { return metaModelManager_; } }

        public void LoadProjectDefinitionsFrom(string file)
        {
            new BPDictionaryLoader().LoadFrom(this, file);
        }

        public void LoadDefaultDictionary()
        {
            new BPDictionaryLoader().LoadFrom(this, "xml/definitions_test.xml");
        }

        public MetaModel rootModel { get; set; }

        public void AddMetaData(string metaData)
        {
            throw new NotImplementedException();
        }


        internal void AddModelDefinition( ModelType modelDef)
        {
            metaModelManager.AddModelDefinition( modelDef);
        }
    }
}
