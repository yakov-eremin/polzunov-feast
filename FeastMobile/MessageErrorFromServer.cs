using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FeastMobile
{
    public class MessageErrorFromServer
    {
        public string message { get; set; }
        public string objectValidationViolations { get; set; }
        public string fieldValidationViolations { get; set; }
        public string httpAttributeValidationViolation { get; set; }

    }
}
