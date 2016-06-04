using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using LinguaNatural.manager;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

namespace LinguaNatural.model
{
    public class Oracao
    {
        

        Frase frase_ = new Frase();

        public Oracao(string fraseSource)
        {
            List<string> stringsNaFrase = new List<string>();
            fraseSource = StringUtils.SeparaStrings(fraseSource, stringsNaFrase);
            string[] palavras = fraseSource.Split(new Char[] { ' ', ',', '.', '!', '?', ':', ';', '-', '/', '|' });
            foreach (string str in palavras)
            {
                frase.AddPalavra(str);
            }

            RecolocaStrng(stringsNaFrase);
        }

        private void RecolocaStrng(List<string> stringsNaFrase)
        {
            foreach (Word word in frase.palavras)
            {
                if (word.palavra.StartsWith("string_"))
                {
                    int index = Int32.Parse(word.palavra.Replace("string_", ""));
                    word.palavra = stringsNaFrase[index];
                    word.tipoPalavra = NLPConsts.tipoPalavra.PALAVRA_STRING;
                }
            }
        }

        public Frase frase { get { return frase_; } }

        public void Parser(CondicionalManager condicionais)
        {
            int posVerbo = -1;
            // Inicial
            for (int c = 0; c < frase.Size(); c++)
            {
                frase.SetPosicaoPalavra(c);
                if (frase.wordAtual.tipoPalavra == NLPConsts.tipoPalavra.VAZIO)
                {
                    condicionais.ChecaCondicionais(frase);
                }
            }

            // Procurando Verbo Principal
            for (int c = 0; c < frase.Size(); c++)
            {
                frase.SetPosicaoPalavra(c);
                if ((frase.wordAtual.tipoPalavra == NLPConsts.tipoPalavra.PALAVRA_VERBO))
                {
                    posVerbo = c;
                    frase.verbo = frase.wordAtual;
                }
            }

            // Procurando sujeitos (substantivos e palavras desconhecidas.
            for (int c = 0; c < posVerbo; c++)
            {
                frase.SetPosicaoPalavra(c);
                if (frase.wordAtual.PossivelSujeito())
                {
                    frase.AddSujeito(frase.wordAtual);
                }
            }

            if (posVerbo > 0)
            {
                //		 Procurando predicados (adjetivos, substantivos e palavras desconhecidas.
                for (int c = posVerbo; c < frase.Size(); c++)
                {
                    frase.SetPosicaoPalavra(c);
                    if ((frase.wordAtual.tipoPalavra == NLPConsts.tipoPalavra.PALAVRA_ADJETIVO) ||
                            (frase.wordAtual.PossivelSujeito()))
                    {
                        frase.AddPredicado(frase.wordAtual);
                    }
                }
            }

        }
	

    }    
}
