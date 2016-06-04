using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.nlp
{
    public class WordManager
    {

        Dictionary<NLPConsts.tipoPalavra, WordHolder> words_ = new Dictionary<NLPConsts.tipoPalavra, WordHolder>();
        public int wordsCount { get { return words_.Count; } }

        internal void AddWord(NLPConsts.tipoPalavra tipo, Word objWord)
        {

            WordHolder holder = GetHolder(tipo);
            holder.AddWord(objWord);
        }

        private WordHolder GetHolder(NLPConsts.tipoPalavra tipo)
        {
            WordHolder holder = null;
            if (!words_.ContainsKey(tipo))
            {
                holder = new WordHolder(tipo);
                words_.Add(tipo, holder);
            }
            else
            {
                holder = words_[tipo];
            }
            return holder;
        }


     /*   public bool IsWordOfType(string word, string tipo)
        {
            WordHolder holder = GetHolder(tipo);
            return holder.ContainsWord(word);
        }*/

        public void AddVerbo(Word p)
        {
            AddWord(NLPConsts.tipoPalavra.PALAVRA_VERBO, p);
        }

        public bool IsWord(NLPConsts.tipoPalavra tipoPalavra, string palavra)
        {
            return GetHolder(tipoPalavra).ContainsWord(palavra);
        }
    }
}
