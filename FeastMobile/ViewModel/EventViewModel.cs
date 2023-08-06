using FeastMobile.Services;
using FeastMobile.View;
using System.Windows.Input;

namespace FeastMobile.ViewModel;

[QueryProperty(nameof(CurrentFeast), nameof(CurrentFeast))]

[QueryProperty(nameof(Categories), nameof(Categories))]

[QueryProperty(nameof(SelectedEventsTimeFrom), nameof(SelectedEventsTimeFrom))]
[QueryProperty(nameof(SelectedEventsTimeTo), nameof(SelectedEventsTimeTo))]
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

    public ObservableCollection<Event> FilteredEvents { get; set; }
    public ObservableCollection<Category> Categories { get; set; }

    public ObservableCollection<Category> SelectedCategories { get; set; }
    public TimeSpan SelectedEventsTimeFrom { get; set; }
    public TimeSpan SelectedEventsTimeTo { get; set; }
    public ICommand SearchCommand { get; private set; }
    private string searchText;
    public string SearchText
    {
        get { return searchText; }
        set
        {
            searchText = value;
            ApplyFilter();
        }
    }

    public EventViewModel(EventLoader eventLoader)
    {
        this.eventLoader = eventLoader;       
        InitParamUnrelatedData();
        SearchCommand = new Command(ApplyFilter);
        SelectedEventsTimeFrom = new TimeSpan(0, 0, 0);
        SelectedEventsTimeTo = new TimeSpan(23, 59, 0);
    }

    async void InitParamUnrelatedData()
    {
        var tmpEvents = await eventLoader.LoadEventsAsync();
        Events = new ObservableCollection<Event>();
        foreach (var events in tmpEvents)
            Events.Add(events);
        OnPropertyChanged(nameof(Events));

        FilteredEvents = Events;
        OnPropertyChanged(nameof(FilteredEvents));
    }

    [RelayCommand]
    private Task InitParamRelatedData()
    { 
        ActivitiensNumberLabelText = CurrentFeast.ActivitiesNumber;
        FeastNameLabelText = CurrentFeast.Name;

        OnPropertyChanged(nameof(SelectedEventsTimeFrom));
        OnPropertyChanged(nameof(SelectedEventsTimeTo));
        Console.WriteLine(SelectedEventsTimeFrom);
        Console.WriteLine(SelectedEventsTimeTo);


        //!!!! Сюда нужно добавить обновление всего при применении фильтров
        if (Categories != null)
        {
            Console.WriteLine("Selected Categories1222222222:");
            foreach (var category in Categories)
            {
                Console.WriteLine(category.Name);
            }
            
        }
        Console.WriteLine(SelectedEventsTimeFrom);
        Console.WriteLine(SelectedEventsTimeTo);
        FilterByCategories();
        return Task.CompletedTask;
    }

    private void FilterByCategories()
    {
        if (Events == null)
            Console.WriteLine("FFFFFFFFFFFFFFFFFFFFFFF ");
        //await DisplayAlert("Выбранные категории", string.Join(", ", selectedCategories), "OK");


        if (Categories != null && Categories.Count > 0 && Events != null)
        {
            FilteredEvents = new ObservableCollection<Event>(

            Events.Where(e => e.Categories != null && e.Categories.Any(c => Categories.Any(sc => sc.Name == c)) &&
               e.StartTime.TimeOfDay >= SelectedEventsTimeFrom && e.StartTime.TimeOfDay <= SelectedEventsTimeTo));

            OnPropertyChanged(nameof(FilteredEvents));
            // Обновляем количество выводимых мероприятий
            ActivitiensNumberLabelText = (uint)FilteredEvents.Count;
            OnPropertyChanged(nameof(ActivitiensNumberLabelText));
        }
        else
        {
            FilteredEvents = Events;
            OnPropertyChanged(nameof(FilteredEvents));
            // Обновляем количество выводимых мероприятий
            //ActivitiensNumberLabelText = (uint)FilteredEvents.Count;
            OnPropertyChanged(nameof(ActivitiensNumberLabelText));
        }
    }
    private void ApplyFilter()
    {
        InitParamRelatedData();
        if (!string.IsNullOrWhiteSpace(SearchText))
        {       
            FilteredEvents = new ObservableCollection<Event>(
                FilteredEvents.Where(e => e.Name.StartsWith(SearchText)));
            ActivitiensNumberLabelText = (uint)FilteredEvents.Count;
            OnPropertyChanged(nameof(ActivitiensNumberLabelText));
        }

        OnPropertyChanged(nameof(FilteredEvents));
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
        Console.WriteLine(SelectedEventsTimeFrom);
        Console.WriteLine(SelectedEventsTimeTo);
        await Shell.Current.GoToAsync($"{nameof(FiltersPage)}",
            new Dictionary<string, object>
            {
                ["SelectedCategories"] = SelectedCategories,
                ["SelectedEventsTimeFrom"] = SelectedEventsTimeFrom,
                ["SelectedEventsTimeTo"] = SelectedEventsTimeTo,
            });
    }

    [RelayCommand]
    async Task GoToRouteSelection()
    {
        await Shell.Current.GoToAsync(nameof(RouteSelectionPage));
    }


}
