using Microsoft.Maui.Devices.Sensors;

namespace FeastMobile.Services;

public class FeastService
{
    List<Feast> feastData = new();
    public async Task<List<Feast>> GetFeastsAsync()
    {
        if (feastData?.Count > 0)
            return feastData;

        using var stream = await FileSystem.OpenAppPackageFileAsync("feastdata.json");
        using var reader = new StreamReader(stream);
        var contents = await reader.ReadToEndAsync();
        feastData = JsonSerializer.Deserialize<List<Feast>>(contents);

        return feastData;
    }
}
