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
        public List<ObjectValidationViolation> objectValidationViolations { get; set; }
        public List<FieldValidationViolation> fieldValidationViolations { get; set; }
        public List<HttpAttributeValidationViolation> httpAttributeValidationViolations { get; set; }
    }
}
public class FieldValidationViolation
{
    public string field { get; set; }
    public string message { get; set; }
}

public class HttpAttributeValidationViolation
{
    public string attribute { get; set; }
    public string message { get; set; }
}

public class ObjectValidationViolation
{
    public string message { get; set; }
}