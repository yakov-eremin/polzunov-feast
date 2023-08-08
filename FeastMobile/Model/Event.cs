using Newtonsoft.Json;

namespace FeastMobile.Model;

public class Event
{
    public int Id { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public DateTime StartTime { get; set; }
    public DateTime EndTime { get; set; }
    [JsonProperty("place")]
    public Place Location { get; set; }
    public bool Canceled { get; set; }
    public List<Category> Categories { get; set; }
    public int AgeLimit { get; set; }

    //necessary, but not realized in DB
    //public List<string> Images { get; set; }
}
