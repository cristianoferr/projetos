using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.nlp
{
    public class PhraseManager
    {

        IList<Phrase> phrases_ = new List<Phrase>();
        public int phrasesCount { get { return phrases_.Count; } }

        internal void AddPhrase(Phrase phrase)
        {
            phrases_.Add(phrase);
        }


        public Phrase GetPhraseByName(string p)
        {
            return phrases_.FirstOrDefault<Phrase>(item => item.name == p);
        }

        public PhraseInterpreter GetPhraseInterpreter(string p)
        {
            throw new NotImplementedException();
        }
    }
}
