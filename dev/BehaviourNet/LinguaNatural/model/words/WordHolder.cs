using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.nlp
{
    public class WordHolder
    {

        public WordHolder(NLPConsts.tipoPalavra tipo)
        {
            this.tipo = tipo;
        }

        IList<Word> words = new List<Word>();
        private NLPConsts.tipoPalavra tipo;
        internal void AddWord(Word word)
        {
            if (!ContainsWord(word))
            {
                words.Add(word);
            }
        }

        public bool ContainsWord(Word word)
        {
            return words.Where(item => item.palavra == word.palavra).Any();
        }
        public bool ContainsWord(string word)
        {
            return words.Where(item => item.palavra == word).Any();
        }
    }
}
