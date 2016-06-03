using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LinguaNatural
{
    public abstract class ComposicaoFrase
    {

        [System.ComponentModel.DefaultValue("")]
        public string palavra {get;set;}  // Palavra para verificar

        [System.ComponentModel.DefaultValue(null)]
        public string prePalavra { get; set; } //Palavra que vem antes

        [System.ComponentModel.DefaultValue(null)]
        public string novaPalavra { get; set; } // Palavra que estára substituindo

        //Exemplo: Verbo "canta", fimPalavra: a, fimPalavraNovo: ar, final: cantar
        [System.ComponentModel.DefaultValue(null)]
        public string fimPalavra { get; set; }

        [System.ComponentModel.DefaultValue(null)]
        public string fimPalavraNovo { get; set; }

        [System.ComponentModel.DefaultValue(NLPConsts.tipo_palavra.VAZIO)]
        public NLPConsts.tipo_palavra tipoPalavra { get; set; }

        [System.ComponentModel.DefaultValue(NLPConsts.tempo_sujeito.VAZIO)]
        public NLPConsts.tempo_sujeito tempoSujeito { get; set; }

        [System.ComponentModel.DefaultValue(NLPConsts.tipo_sujeito.VAZIO)]
        public NLPConsts.tipo_sujeito tipoSujeito { get; set; }

        [System.ComponentModel.DefaultValue(-1)]
        public int tipoPessoa { get; set; }

        [System.ComponentModel.DefaultValue(NLPConsts.tipo_genero.VAZIO)]
        public NLPConsts.tipo_genero generoSujeito { get; set; }

        [System.ComponentModel.DefaultValue(NLPConsts.tipo_adjetivo.VAZIO)]
        public NLPConsts.tipo_adjetivo tipoAdjetivo { get; set; }

        [System.ComponentModel.DefaultValue(0)]
        public int relacaoVerbo { get; set; }//-1 = antes, 0=indiferente, 1=depois

        [System.ComponentModel.DefaultValue(false)]
        public bool entreSujeitos { get; set; }

        /*
        public void setTipoSujeito(int tipoSujeito)
        {
            if (tipoSujeito != -1)
                this.tipoSujeito = tipoSujeito;
        }

        public void setGeneroSujeito(int generoSujeito)
        {
            if (generoSujeito != -1)
                this.generoSujeito = generoSujeito;
        }
        public void setTipoAdjetivo(int tipoAdjetivo)
        {
            if (tipoAdjetivo != -1)
                this.tipoAdjetivo = tipoAdjetivo;
        }
        */

     

    }
}
