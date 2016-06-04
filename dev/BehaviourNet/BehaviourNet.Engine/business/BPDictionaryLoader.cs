using BehaviourNet.engine.metamodel;
using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.Linq;

namespace BehaviourNet.engine.business
{
    class BPDictionaryLoader
    {
        internal void LoadFrom(engine.BehaviourLoader loader, string xmlFile)
        {
            XElement root = XElement.Load(xmlFile);
            foreach (XElement words in root.Elements("words").Elements()){
           //     AddWord(loader, words);
            }

            foreach (XElement frases in root.Elements("phrases").Elements())
            {
                AddPhrase(loader, frases);
            }

            foreach (XElement metaModel in root.Elements("metaModelsHierarchy").Elements())
            {
                AddHierarchy(loader, metaModel);
            }

        }

        private void AddHierarchy(engine.BehaviourLoader loader, XElement metaModel)
        {

            IList<string> allowedChild = new List<string>();
            foreach (XElement word in metaModel.Elements())
            {
                allowedChild.Add(word.Attribute("type").Value);
            }
            ModelType modelDef = new ModelType { typeName = metaModel.Attribute("type").Value, classe = metaModel.Attribute("class").Value, allowedChild = allowedChild };
            
            loader.AddModelDefinition(modelDef);
        }

        private void AddPhrase(engine.BehaviourLoader loader, XElement frase)
        {
            var type = Type.GetType(BNConsts.NLP_MODEL_NAMESPACE+"." + frase.Attribute("class").Value);
            var objFrase = (Phrase)Activator.CreateInstance(type);
            objFrase.name = frase.Attribute("name").Value;
            foreach (XElement word in frase.Elements())
            {
                objFrase.LoadFromXML(word);
            }
            loader.phraseManager.AddPhrase(objFrase);
        }

       /* private void AddWord(engine.BehaviourLoader loader, XElement words)
        {
            var type = Type.GetType(BNConsts.NLP_MODEL_NAMESPACE + "." + words.Attribute("class").Value);

            //verbo>word
            foreach (XElement word in words.Elements())
            {

                var objWord = (Word)Activator.CreateInstance(type);
                objWord.name=word.Attribute("name").Value;
                objWord.AddAlternative(word.Attribute("name").Value);
                //word>alt
                foreach (XElement alt in word.Elements())
                {
                    objWord.AddAlternative(alt.Attribute("name").Value);
                }
                objWord.LoadExtraInfo(word);
                loader.AddWord(objWord.tipo, objWord);
            }

        }*/
    }
}
