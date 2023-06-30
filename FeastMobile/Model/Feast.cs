namespace FeastMobile.Model;

public class Feast
{
    public string Name { get; set; }
    public uint ActivitiesNumber { get; set; }
    public DateTime StartDate { get; set; }
    public DateTime EndDate { get; set; }
    public string Description { get; set; }
    public List<string> Images { get; set; }
}
