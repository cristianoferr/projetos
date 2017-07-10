using Rimworld.model.components;
using System.Runtime.Serialization;

namespace Rimworld.model.io
{
    [DataContract]
    public class Property : TagObject
    {
        public Property(string name)
        {
            this.name = name;
            randomValue = true;
        }

        [DataMember]
        public string name { get; set; }

        [DataMember]
        public GameValue value { get; set; }


        public bool randomValue { get; set; }
    }
}
