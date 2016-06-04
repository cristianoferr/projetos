using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LinguaNatural.model
{
    public class Frase : ComposicaoFrase
    {
       

        public Frase()
            : base()
        {

        }

        IList<Word> palavras_ = new List<Word>();
        public IList<Word> palavras { get { return palavras_; } }

        public void AddPalavra(Word palavra)
        {
            palavras.Add(palavra);
        }


        internal void AddPalavra(string str)
        {
            AddPalavra(new Word(str));
        }

        internal int Size()
        {
            return palavras.Count;
        }

        int posicaPalavra = 0;
        internal void SetPosicaoPalavra(int c)
        {
            posicaPalavra = c;
        }

        public Word wordAtual
        {
            get
            {
                return palavras[posicaPalavra];
            }
        }

        public Word wordAnterior
        {
            get
            {
                return palavras[posicaPalavra-1];
            }
        }

        public Word wordPosterior
        {
            get
            {
                return palavras[posicaPalavra + 1];
            }
        }

        public Word verbo { get; set; }


        IList<Word> sujeitos_ = new List<Word>();
        public IList<Word> sujeitos { get { return sujeitos_; } }
        internal void AddSujeito(Word word)
        {
            sujeitos.Add(word);
        }

        IList<Word> predicados_ = new List<Word>();
        public IList<Word> predicados { get { return predicados_; } }

        internal void AddPredicado(Word word)
        {
            predicados.Add(word);
        }
    }
}
