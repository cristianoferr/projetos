using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BehaviourNet.utils
{
    public abstract class NLPConsts
    {
        public enum tempoSujeito
        {
            VAZIO = -1, TEMPO_PRESENTE = 1, TEMPO_PRETERITO_IMPERFEITO = 2, TEMPO_PRETERITO_PERFEITO = 3,
            TEMPO_PRETERITO_MAIS_QUE_PERFEITO = 4, TEMPO_FUTURO_PRESENTE = 5, TEMPO_FUTURO_PRETERITO = 6, TEMPO_PRETERITO_PERFEITO_SIMPLES = 7, TEMPO_PRETERITO_COMPOSTO = 8
        }

        public enum tipoSujeito
        {
            VAZIO = -1, SUJEITO_SIMPLES = 0, SUJEITO_COMPOSTO = 1
        }

        public enum tipoPalavra
        {
            VAZIO = -1, PALAVRA_VERBO_AUXILIAR = 0,
            PALAVRA_ARTIGO_INDEFINIDO = 1,
            PALAVRA_ARTIGO_DEFINIDO = 2,
            PALAVRA_ADJETIVO = 3,
            PALAVRA_SUBSTANTIVO = 4,
            PALAVRA_VERBO = 5,
            PALAVRA_CONJUNCAO_ADITIVA = 6,
            PALAVRA_CONJUNCAO_ADVERSA = 7,
            PALAVRA_CONJUNCAO_CONCLUSIVA = 8,
            PALAVRA_CONJUNCAO_EXPLICATIVA = 9,
            PALAVRA_PRONOME_PESSOAL = 10,
            PALAVRA_PRONOME_POSSESSIVO = 11,
            PALAVRA_PRONOME_DEMONSTRATIVO = 12,
            PALAVRA_PRONOME_INDEFINIDO = 13,
            PALAVRA_PRONOME_RELATIVO = 14,
            PALAVRA_PRONOME_INTERROGATIVO = 15,
            PALAVRA_PREPOSICAO = 16,
            PALAVRA_ADVERBIO = 17,
            PALAVRA_STRING = 18,
            PALAVRA_PRONOME = 19,
        }

        public enum tipoAdjetivo
        {
            VAZIO = -1,
            ADJETIVO_COR = 0,
            ADJETIVO_FORMA = 1,
            ADJETIVO_TEMPERATURA = 2,
            ADJETIVO_INTENSIDADE = 3,
            ADJETIVO_PROPORCAO = 4,
            ADJETIVO_QUALIDADE = 5,
            ADJETIVO_DEFEITO = 6,
            ADJETIVO_PATRIO = 7
        }

        public enum generoSujeito
        {
            VAZIO = -1, GENERO_MASCULINO = 1, GENERO_FEMININO = 2
        }
    }
}
