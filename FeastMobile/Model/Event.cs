namespace FeastMobile.Model;

public class Event
{
    public string Organizer { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public DateTime StartTime { get; set; }
    public DateTime EndTime { get; set; }
    public string Address { get; set; }
    public uint AgeRestriction { get; set; }
    public List<string> Categories { get; set; }
    public List<string> Images { get; set; }
}
