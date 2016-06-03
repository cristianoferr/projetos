using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.nlp
{
    public class WordHolder
    {

        public WordHolder(string tipo)
        {
            this.tipo = tipo;
        }

        IList<Word> words = new List<Word>();
        private string tipo;
        internal void AddWord(Word word)
        {
            words.Add(word);
        }

        internal Word GetWord(string wordToFind)
        {
            return words.FirstOrDefault<Word>(item=>item.ContainsWord(wordToFind));
        }

        internal bool ContainsWord(string wordToFind)
        {
            return GetWord(wordToFind)!=null;
        }
    }
}
