using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.engine.nlp
{
    public class Word
    {
        public string palavra { get; set; }

        public Word()
        {
            tipoPalavra = NLPConsts.tipoPalavra.VAZIO;
        }

        public Word(string str)
        {
            this.palavra = str;
            tipoPalavra = NLPConsts.tipoPalavra.VAZIO;
            tipoAdjetivo = NLPConsts.tipoAdjetivo.VAZIO;
        }
        public NLPConsts.tipoPalavra tipoPalavra { get; set; }
        public NLPConsts.tipoAdjetivo tipoAdjetivo { get; set; }



        internal bool PossivelSujeito()
        {
            return ((tipoPalavra == NLPConsts.tipoPalavra.PALAVRA_SUBSTANTIVO) ||
                (tipoPalavra == NLPConsts.tipoPalavra.VAZIO) ||
                (tipoPalavra == NLPConsts.tipoPalavra.PALAVRA_PRONOME_PESSOAL) ||
                (tipoPalavra) == NLPConsts.tipoPalavra.PALAVRA_ARTIGO_DEFINIDO);
        }

        public override string ToString()
        {
            return palavra;
        }
    }
}
