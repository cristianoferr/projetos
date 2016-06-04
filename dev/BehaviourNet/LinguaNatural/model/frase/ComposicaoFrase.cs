using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LinguaNatural.model
{
    public abstract class ComposicaoFrase
    {

        public ComposicaoFrase()
        {
           
            tipoPalavra = NLPConsts.tipoPalavra.VAZIO;
            tempoSujeito = NLPConsts.tempoSujeito.VAZIO;
            tipoSujeito = NLPConsts.tipoSujeito.VAZIO;
            generoSujeito = NLPConsts.generoSujeito.VAZIO;
            tipoAdjetivo = NLPConsts.tipoAdjetivo.VAZIO;
            entreSujeitos = false;
            tipoPessoa = -1;
        }

        public bool entreSujeitos { get; set; }
       

        public NLPConsts.tipoPalavra tipoPalavra { get; set; }

        public NLPConsts.tempoSujeito tempoSujeito { get; set; }

        public NLPConsts.tipoSujeito tipoSujeito { get; set; }

        public int tipoPessoa { get; set; }

        public NLPConsts.generoSujeito generoSujeito { get; set; }

        public NLPConsts.tipoAdjetivo tipoAdjetivo { get; set; }

        

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
