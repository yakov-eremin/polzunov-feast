namespace FeastMobile.Services;

public class EventLoader
{
    List<Event> eventList = new();
    public async Task<List<Event>> LoadEventsAsync()
    {
        if (eventList.Count > 0)
            return eventList;

        using var stream = await FileSystem.OpenAppPackageFileAsync("eventsData.json");
        using var reader = new StreamReader(stream);

        var json = await reader.ReadToEndAsync();
        eventList = JsonSerializer.Deserialize<List<Event>>(json);
        return eventList;
    }
}
