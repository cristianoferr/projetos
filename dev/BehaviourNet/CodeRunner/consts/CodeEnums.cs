using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;

namespace CodeRunner
{
    public class CodeEnums
    {
       public const string IF = "if";
       public const string ASSIGN = "=";
       public const string VARIABLE = "var";
       public const string VALUE = "val";
        
    }
   


    public enum EnumTipoObjeto
    {

        [Description("EMPTY")]
        EMPTY = 0,
        [Description("Code")]
        CODE = 1,
        [Description("Boolean")]
        BOOLEAN = 2,
        [Description("Value")]
        VALUE = 3,
        [Description("Variable")]
        VARIABLE = 4
    }

   
}
