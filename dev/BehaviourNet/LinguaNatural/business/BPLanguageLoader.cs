using BehaviourNet.engine.metamodel;
using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.Linq;

namespace LinguaNatural.business
{
    class BPDictionaryLoader
    {
        internal void LoadFrom(NLPLoader loader, string xmlFile)
        {
            XElement root = XElement.Load(xmlFile);
            foreach (XElement gruposPalavras in root.Elements("condicionais").Elements())
            {
                AddGruposCondicionais(loader, gruposPalavras);
            }

           

        }

        private void AddGruposCondicionais(NLPLoader loader, XElement gruposPalavras)
        {
            foreach (XElement grupoPalavras in gruposPalavras.Elements())
            {
                AddCondicionais(loader, grupoPalavras);
                //Condicional cond = new Condicional();
            }

        }

        private void AddCondicionais(NLPLoader loader, XElement grupoPalavras)
        {
            foreach (XElement elCond in grupoPalavras.Elements())
            {
                Condicional cond = new Condicional(elCond);
                loader.AddCondicional(cond);
            }
        }
    }
}
