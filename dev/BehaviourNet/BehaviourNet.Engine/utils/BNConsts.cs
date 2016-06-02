using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.utils
{
    public abstract class BNConsts
    {
        public const string WORD_VERBO = "verbo";
        public const string WORD_ARTIGO = "artigo";
        public const string WORD_SUBSTANTIVO = "substantivo";
        public const string WORD_PRONOME = "pronome";


        public const string XML_PHRASE_WORD="word";
        public const string XML_PHRASE_ACTION = "action";
        public const string NLP_MODEL_NAMESPACE="BehaviourNet.engine.nlp";
        public const string META_MODEL_NAMESPACE = "BehaviourNet.engine.metamodel";


        public const string PROPERTY_NAME = "name";
    }
}
