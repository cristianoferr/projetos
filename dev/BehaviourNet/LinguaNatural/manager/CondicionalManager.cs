
using BehaviourNet.engine.nlp;
using BehaviourNet.utils;
using LinguaNatural.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.Linq;
using Utils;

namespace LinguaNatural.manager
{
    public class CondicionalManager
    {

        public CondicionalManager(NLPLoader loader)
        {
            this.loader = loader;
        }

        IList<Condicional> condicionais_ = new List<Condicional>();
        public IList<Condicional> condicionais { get { return condicionais_; } }
        public int condicionaisCount { get { return condicionais_.Count; } }


        internal void AddCondicional(Condicional cond)
        {
            condicionais_.Add(cond);
        }

        internal Oracao Interpretar(string frase)
        {
            Oracao resultado = new Oracao(frase);
            resultado.Parser(this);
            return resultado;
        }





        public void ChecaCondicionais(Frase frase)
        {

            CheckPalavrasIguais(frase);

            DesconjugandoVerbos(frase);
        }

        private void DesconjugandoVerbos(Frase frase)
        {
            IList<Condicional> candidatas = new List<Condicional>();

            Word wordAtual = frase.wordAtual;
            foreach (Condicional condicaoAtual in condicionais)
            {
                // Desconjugando verbos
                if (condicaoAtual.fimPalavra != null)
                {
                    String palavraFinal = wordAtual.palavra;
                    if ((palavraFinal.Length > condicaoAtual.fimPalavra.Length) && (wordAtual.tipoPalavra == NLPConsts.tipoPalavra.VAZIO))
                    {
                        palavraFinal = palavraFinal.Substring(0, palavraFinal.Length - condicaoAtual.fimPalavra.Length) + condicaoAtual.fimPalavraNovo;
                        if ((condicaoAtual.fimPalavra.Length > 3) || (IsVerbo(palavraFinal)))
                        {
                            if (wordAtual.palavra.Length > condicaoAtual.fimPalavra.Length)
                            {
                                string aux = wordAtual.palavra.Substring(wordAtual.palavra.Length - condicaoAtual.fimPalavra.Length);
                                if (aux == condicaoAtual.fimPalavra)
                                {
                                    candidatas.Add(condicaoAtual);
                                }
                            }
                        }
                    }
                }
            }

            if (candidatas.Count == 1)
            {
                String palavraFinal = GetPalavraGeradaCondicao(candidatas[1], wordAtual);
                AceitaCondicao(candidatas[1], wordAtual, frase, palavraFinal);
            }
        }

        private void AceitaCondicao(Condicional condicional, Word wordAtual, Frase frase,string palavraFinal)
        {
            
            wordAtual.palavra = palavraFinal;
            frase.verbo = wordAtual;
            CondicaoPassou(frase, condicional, wordAtual);
        }

        private static string GetPalavraGeradaCondicao(Condicional condicional, Word wordAtual)
        {
            String palavraFinal = wordAtual.palavra;
            palavraFinal = palavraFinal.Substring(0, palavraFinal.Length - condicional.fimPalavra.Length) + condicional.fimPalavraNovo;
            return palavraFinal;
        }



        private bool IsVerbo(string s)
        {
            return loader.wordManager.IsWord(NLPConsts.tipoPalavra.PALAVRA_VERBO, s);
        }

        void CheckPalavrasIguais(Frase frase)
        {
            foreach (Condicional condicaoAtual in condicionais)
            {
                Word wordAtual = frase.wordAtual;


                // Palavras Iguais
                if (condicaoAtual.palavra == wordAtual.palavra)
                {
                    if ((condicaoAtual.prePalavra == null) ||
                            (condicaoAtual.prePalavra == (frase.wordAnterior.palavra)))
                    {
                        if ((condicaoAtual.relacaoVerbo == 0) || ((condicaoAtual.relacaoVerbo == -1) && (frase.verbo == null))
                            || ((condicaoAtual.relacaoVerbo == 1) && (frase.verbo != null)))
                        {
                            if ((!condicaoAtual.entreSujeitos) || ((condicaoAtual.entreSujeitos) && (frase.entreSujeitos)))
                            {
                                if (condicaoAtual.novaPalavra != null)
                                {
                                    wordAtual.palavra = condicaoAtual.novaPalavra;
                                }
                                CondicaoPassou(frase, condicaoAtual, wordAtual);
                                break;
                            }
                        }
                    }
                }
            }
        }

        private void CondicaoPassou(Frase frase, Condicional condicaoAtual, Word wordAtual)
        {
            wordAtual.tipoPalavra = condicaoAtual.tipoPalavra;
            wordAtual.tipoAdjetivo = condicaoAtual.tipoAdjetivo;
            frase.tempoSujeito = condicaoAtual.tempoSujeito;
            frase.tipoSujeito = condicaoAtual.tipoSujeito;
            frase.tipoPessoa = condicaoAtual.tipoPessoa;

            if (condicaoAtual.tipoPalavra == NLPConsts.tipoPalavra.PALAVRA_VERBO)
            {
                loader.wordManager.AddVerbo(wordAtual);
                frase.verbo = wordAtual;
            }

            if (condicaoAtual.tipoPalavra == NLPConsts.tipoPalavra.PALAVRA_VERBO_AUXILIAR)
            {
                if (frase.verbo == null)
                    frase.verbo = wordAtual;
            }
        }


        public NLPLoader loader { get; set; }
    }

}