using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using LinguaNatural.business;
using LinguaNatural.manager;
using LinguaNatural.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LinguaNatural
{
    public class NLPLoader
    {
        public CondicionalManager condicionalManager; 
        public WordManager wordManager; 

        public NLPLoader(){
            condicionalManager = new CondicionalManager(this);
            wordManager = new WordManager();
            new BPLanguageLoader().LoadFrom(this, "xml/nlpdata.xml");
        }


        internal void AddCondicional(Condicional cond)
        {
            condicionalManager.AddCondicional(cond);
        }

        public Oracao Interpretar(string frase)
        {
            return condicionalManager.Interpretar(frase);
        }
    }
}
