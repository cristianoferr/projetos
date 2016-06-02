using BehaviourNet.engine.nlp;
using BehaviourNet.engine.nlp.phrase;
using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BehaviourNet.engine.nlp
{
    public class Phrase
    {

        IList<WordRule> rules = new List<WordRule>();
        IList<PhraseAction> actions = new List<PhraseAction>();
        internal void LoadFromXML(System.Xml.Linq.XElement word)
        {
            if (word.Name.LocalName == BNConsts.XML_PHRASE_WORD)
            {
                LoadRule(word);
            }
            if (word.Name.LocalName == BNConsts.XML_PHRASE_ACTION)
            {
                LoadAction(word);
            }
        }

        private void LoadAction(System.Xml.Linq.XElement word)
        {
            PhraseAction action = new PhraseAction();
            action.value = word.Attribute("value").Value;
            actions.Add(action);
        }

        private void LoadRule(System.Xml.Linq.XElement word)
        {
            WordRule rule = new WordRule();
            rule.expected = word.Attribute("expected").Value;
            if (word.Attribute("isTopic") != null && word.Attribute("isTopic").Value.ToLower() == "true")
            {
                rule.isTopic = true;
            }
            if (word.Attribute("optional") != null && word.Attribute("optional").Value.ToLower() == "true")
            {
                rule.optional = true;
            }
            if (word.Attribute("toVar") != null)
            {
                rule.toVar = word.Attribute("toVar").Value;
            }

            if (word.Attribute("validWords") != null)
            {
                rule.validWords = word.Attribute("validWords").Value.Split(',').ToList<string>();
            }
            rules.Add(rule);
        }

        public string name { get; set; }

        public int rulesCount { get { return rules.Count; } }

        public int actionCount { get { return actions.Count; } }
    }
}
