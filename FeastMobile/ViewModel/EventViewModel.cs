using FeastMobile.Services;
using FeastMobile.View;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(CurrentFeast), nameof(CurrentFeast))]
public partial class EventViewModel : BaseViewModel
{
    [ObservableProperty]
    private Feast currentFeast;

    [ObservableProperty]
    private int activitiensNumberLabelText;

    [ObservableProperty]
    private string feastNameLabelText;

    public ObservableCollection<Event> Events { get; set; }

    public EventViewModel()
    {
        InitParamUnrelatedData();
    }

    async void InitParamUnrelatedData()
    {
        var tmpEvents = await EventService.LoadEventsFromInternetAsync();
        tmpEvents = tmpEvents.OrderBy(e => e.StartTime).ToList();

        Events = new ObservableCollection<Event>();
        foreach (var events in tmpEvents)
            Events.Add(events);
        OnPropertyChanged(nameof(Events));
        ActivitiensNumberLabelText = Events.Count;
    }

    [RelayCommand]
    private Task InitParamRelatedData()
    { 
        FeastNameLabelText = CurrentFeast.Name;
        return Task.CompletedTask;
    }

    [RelayCommand]
    static async Task GoBackAsync()
    {
        await Shell.Current.GoToAsync("..");
    }

    [RelayCommand]
    async Task GoToDetailsAsync(Event chosenEvent)
    {
        if (chosenEvent is null)
            return;

        await Shell.Current.GoToAsync($"{nameof(EventDetailsPage)}",
            new Dictionary<string, object>
            {
                ["CurrentEvent"] = chosenEvent
            });
    }

    [RelayCommand]
    async Task GoToFiltersAsync()
    {
        await Shell.Current.GoToAsync(nameof(FiltersPage));
    }

    [RelayCommand]
    async Task GoToRouteSelection()
    {
        await Shell.Current.GoToAsync(nameof(RouteSelectionPage));
    }
}
