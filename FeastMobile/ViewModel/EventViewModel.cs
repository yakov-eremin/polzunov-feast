using FeastMobile.Services;
using FeastMobile.View;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(CurrentFeast), nameof(CurrentFeast))]
public partial class EventViewModel : BaseViewModel
{
    [ObservableProperty]
    private Feast currentFeast;

    [ObservableProperty]
    private uint activitiensNumberLabelText;

    [ObservableProperty]
    private string feastNameLabelText;

    private EventLoader eventLoader;
    public ObservableCollection<Event> Events { get; set; }

    public EventViewModel(EventLoader eventLoader)
    {
        this.eventLoader = eventLoader;
        InitParamUnrelatedData();
    }

    async void InitParamUnrelatedData()
    {
        var tmpEvents = await eventLoader.LoadEventsAsync();
        Events = new ObservableCollection<Event>();
        foreach (var events in tmpEvents)
            Events.Add(events);
        OnPropertyChanged(nameof(Events));
    }

    [RelayCommand]
    private Task InitParamRelatedData()
    { 
        ActivitiensNumberLabelText = CurrentFeast.ActivitiesNumber;
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
