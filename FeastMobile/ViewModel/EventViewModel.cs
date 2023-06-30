using FeastMobile.Services;
using FeastMobile.View;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(Feast), nameof(Feast))]
public partial class EventViewModel : BaseViewModel
{
    [ObservableProperty]
    Feast feast;

    private EventLoader eventLoader;
    public ObservableCollection<Event> Events { get; set; }

    public EventViewModel(EventLoader eventLoader)
    {
        this.eventLoader = eventLoader;
        InitializeData();
    }

    async void InitializeData()
    {
        var tmpEvents = await eventLoader.LoadEventsAsync();
        Events = new ObservableCollection<Event>();
        foreach (var events in tmpEvents)
            Events.Add(events);
        OnPropertyChanged(nameof(Events));
    }

    [RelayCommand]
    async Task GoBackAsync()
    {
        await Shell.Current.GoToAsync("..");
    }

    [RelayCommand]
    async Task GoToFiltersAsync()
    {
        await Shell.Current.GoToAsync(nameof(FiltersPage));
    }
}
