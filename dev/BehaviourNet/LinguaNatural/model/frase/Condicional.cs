using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

namespace LinguaNatural.model
{
    public class Condicional : ComposicaoFrase
    {

        public Condicional(System.Xml.Linq.XElement elCond):base()
        {
            palavra = null;
            prePalavra = null;
            novaPalavra = null;
            fimPalavra = null;
            fimPalavraNovo = null;
            relacaoVerbo = 0;
            

            palavra = XMLUtils.PegaValorString(elCond, "palavra", palavra);
            novaPalavra = XMLUtils.PegaValorString(elCond, "novaPalavra", novaPalavra);
            prePalavra = XMLUtils.PegaValorString(elCond, "prePalavra", prePalavra);
            fimPalavraNovo = XMLUtils.PegaValorString(elCond, "fimPalavraNovo", fimPalavraNovo);
            fimPalavra = XMLUtils.PegaValorString(elCond, "fimPalavra", fimPalavra);
            tipoPessoa = XMLUtils.PegaValorInt(elCond, "tipoPessoa", tipoPessoa);

            string val;
            if (elCond.Attribute("tipoPalavra") != null)
            {
                val = elCond.Attribute("tipoPalavra").Value;
                var tipo = typeof(NLPConsts.tipoPalavra);
                tipoPalavra = (NLPConsts.tipoPalavra)Enum.Parse(tipo, val);
            }
            if (elCond.Attribute("generoSujeito") != null)
            {
                val = elCond.Attribute("generoSujeito").Value;
                var tipo = typeof(NLPConsts.generoSujeito);
                generoSujeito = (NLPConsts.generoSujeito)Enum.Parse(tipo, val);
            }
            if (elCond.Attribute("tipoAdjetivo") != null)
            {
                val = elCond.Attribute("tipoAdjetivo").Value;
                var tipo = typeof(NLPConsts.tipoAdjetivo);
                tipoAdjetivo = (NLPConsts.tipoAdjetivo)Enum.Parse(tipo, val);
            }
            if (elCond.Attribute("tempoSujeito") != null)
            {
                val = elCond.Attribute("tempoSujeito").Value;
                var tipo = typeof(NLPConsts.tempoSujeito);
                tempoSujeito = (NLPConsts.tempoSujeito)Enum.Parse(tipo, val);
            }
            if (elCond.Attribute("tipoSujeito") != null)
            {
                val = elCond.Attribute("tipoSujeito").Value;
                var tipo = typeof(NLPConsts.tipoSujeito);
                tipoSujeito = (NLPConsts.tipoSujeito)Enum.Parse(tipo, val);
            }

            
            if (elCond.Attribute("tempoSujeito") != null)
            {
                val = elCond.Attribute("tempoSujeito").Value;
                var tipo = typeof(NLPConsts.tempoSujeito);
                tempoSujeito = (NLPConsts.tempoSujeito)Enum.Parse(tipo, val);
            }

        }


        public string palavra { get; set; }  // Palavra para verificar

        public string prePalavra { get; set; } //Palavra que vem antes

        public string novaPalavra { get; set; } // Palavra que estára substituindo

        //Exemplo: Verbo "canta", fimPalavra: a, fimPalavraNovo: ar, final: cantar
        public string fimPalavra { get; set; }

        public string fimPalavraNovo { get; set; }
        public int relacaoVerbo { get; set; }//-1 = antes, 0=indiferente, 1=depois
        


        
    }
}
