namespace FeastMobile.Model
{
    internal class Event
    {
        public string RelatedEvent { get; set; }
        public string Organizer { get; set; }
        public string Name { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string Venue { get; set; }
        public uint AgeRestriction { get; set; }
    }
}
