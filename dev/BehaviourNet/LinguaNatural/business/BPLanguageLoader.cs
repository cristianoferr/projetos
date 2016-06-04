
using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using LinguaNatural.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.Linq;

namespace LinguaNatural.business
{
    class BPLanguageLoader
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
                Condicional cond = new Condicional(grupoPalavras);
                loader.AddCondicional(cond);
            }

        }

    }
}
