using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.utils
{
    public abstract class NLPConsts
    {
        public enum tempo_sujeito
        {
            VAZIO = -1, tempoPresente = 1, tempoPreteritoImperfeito = 2, tempoPreteritoPerfeito = 3,
            tempoPreteritoMQPerfeito = 4, tempoFuturoPresente = 5, tempoFuturoPreterito = 6, tempoPreteritoPerfeitoSimples = 7, tempoPreteritoComposto = 8
        }

        public enum tipo_sujeito
        {
            VAZIO = -1, sujSimples = 0, sujComposto = 1
        }

        public enum tipo_palavra
        {
            VAZIO = -1, palVerboAux = 0,
            palArtIndef = 1,
            palArtDef = 2,
            palAdj = 3,
            palSubst = 4,
            palVerbo = 5,
            palConjAditiva = 6,
            palConjAdversa = 7,
            palConjConclusiva = 8,
            palConjExplicativa = 9,
            palPronomePessoal = 10,
            palPronomePossessivo = 11,
            palPronomeDemonstrativo = 12,
            palPronomeIndefinido = 13,
            palPronomeRelativo = 14,
            palPronomeInterrogativo = 15,
            palPreposicao = 16,
            palAdverbio = 17,
            palString = 18
        }

        public enum tipo_adjetivo
        {
            VAZIO = -1,
            adjCor = 0,
            adjForma = 1,
            adjTemperatura = 2,
            adjIntensidade = 3,
            adjProporcao = 4,
            adjQualidade = 5,
            adjDefeito = 6,
            adjPatrio = 7
        }

        public enum tipo_genero
        {
            VAZIO = -1, genMasculino = 1, genFeminino = 2
        }
    }
}
