using BehaviourNet.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LinguaNatural
{
    public class Condicional : ComposicaoFrase
    {

        public Condicional(System.Xml.Linq.XElement elCond)
        {
            palavra = PegaValorString(elCond, "palavra", palavra);
            novaPalavra = PegaValorString(elCond, "novaPalavra", novaPalavra);
            prePalavra = PegaValorString(elCond, "prePalavra", prePalavra);
            fimPalavraNovo = PegaValorString(elCond, "fimPalavraNovo", fimPalavraNovo);
            fimPalavra = PegaValorString(elCond, "fimPalavra", fimPalavra);

            string val;
            if (elCond.Attribute("tipoPalavra") != null)
            {
                val = elCond.Attribute("tipoPalavra").Value;
                var tipo = typeof(NLPConsts.tipo_palavra);
                tipoPalavra = (NLPConsts.tipo_palavra)Enum.Parse(tipo, val);
            }
        }

        private static string PegaValorString(System.Xml.Linq.XElement elCond, string param,string val)
        {
            if (elCond.Attribute(param) != null)
            {
                val = elCond.Attribute(param).Value;
            }
            return val;
        }

    }
}
