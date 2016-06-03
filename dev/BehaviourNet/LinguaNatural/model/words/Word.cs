using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.nlp
{
    public abstract class Word
    {
        public Word(string tipo)
        {
            this.tipo_ = tipo;
        }
        string tipo_="UNDEF";
        public string tipo{get{return tipo_;}}

        public string name { get; set; }


        IList<string> alternativeNames = new List<string>();
        internal void AddAlternative(string name)
        {
            alternativeNames.Add(name);
        }

        internal bool ContainsWord(string wordToFind)
        {
            return alternativeNames.FirstOrDefault<string>(item => item.ToString().ToLower() == wordToFind.ToLower())!=null;
        }

        public virtual void LoadExtraInfo(System.Xml.Linq.XElement word)
        {
            
        }
    }
}
