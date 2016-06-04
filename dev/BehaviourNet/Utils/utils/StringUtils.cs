using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils
{
    public class StringUtils
    {

        public static string SeparaStrings(string frase, List<string> stringsNaFrase)
        {
            String saida = "";
            string substring = "";

            bool status = false;
            for (int i = 0; i < frase.Length; i++)
            {
                if (frase[i] == '\"')
                {
                    if (status && substring != "")
                    {
                        saida = saida + "string_" + stringsNaFrase.Count;
                        stringsNaFrase.Add(substring);
                        substring = "";

                    }
                    status = !status;
                }
                else
                {

                    if (status)
                    {
                        substring = substring + frase[i];
                    }
                    else
                    {
                        saida = saida + frase[i];
                    }
                }
            }

            return saida;
        }

    }
}
