using Newtonsoft.Json;

namespace FeastMobile.Services;

public static class EventService
{
    static string BaseUrl = "http://192.168.1.96:8080";
    static HttpClient httpClient;
    
    static EventService()
    {
        try
        {
            httpClient = new HttpClient()
            {
                BaseAddress = new Uri(BaseUrl)
            };
        }
        catch
        {

        }
    }

    public static async Task<List<Event>> LoadEventsFromInternetAsync()
    {
        List<Event> eventList = new List<Event>();

        try 
        {
            HttpResponseMessage response = await httpClient.GetAsync("/event");
            if (response.IsSuccessStatusCode)
            {
                var content = await response.Content.ReadAsStringAsync();
                eventList = JsonConvert.DeserializeObject<List<Event>>(content);

            }
        }
        catch (Exception ex) 
        { 
            
        }   

        return eventList;
    }

    public static async Task<List<Event>> LoadEventsLocalyAsync()
    {
        using var stream = await FileSystem.OpenAppPackageFileAsync("eventsData.json");
        using var reader = new StreamReader(stream);
        string json = await reader.ReadToEndAsync();
        List<Event> eventList = JsonConvert.DeserializeObject<List<Event>>(json);
        return eventList;
    }
}
