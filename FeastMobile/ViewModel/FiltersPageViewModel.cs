using CommunityToolkit.Maui.Views;
using FeastMobile.Services;
using FeastMobile.View;


namespace FeastMobile.ViewModel;

[QueryProperty(nameof(Categories), nameof(Categories))]
[QueryProperty(nameof(SelectedEventsTimeFrom), nameof(SelectedEventsTimeFrom))]
[QueryProperty(nameof(SelectedEventsTimeTo), nameof(SelectedEventsTimeTo))]
public partial class FiltersPageViewModel : BaseViewModel
{
    public TimeSpan SelectedEventsTimeFrom { get; set; }
    public TimeSpan SelectedEventsTimeTo { get; set; }

    public TimeSpan SelectedEventsTimeFrom1 { get; set; }
    public TimeSpan SelectedEventsTimeTo1 { get; set; }

    private string selectedTimeFromLabel;
    public string SelectedTimeFromLabel
    {
        get { return selectedTimeFromLabel; }
        set
        {
            if (selectedTimeFromLabel != value)
            {
                selectedTimeFromLabel = value;
                OnPropertyChanged();
            }
        }
    }
    private string selectedTimeToLabel;
    public string SelectedTimeToLabel
    {
        get { return selectedTimeToLabel; }
        set
        {
            if (selectedTimeToLabel != value)
            {
                selectedTimeToLabel = value;
                OnPropertyChanged();
            }
        }
    }
    public ObservableCollection<Category> Categories { get; set; }
    public ObservableCollection<Category> SelectedCategories { get; set; }
    public FiltersPageViewModel()
    {
        LoadCategories();
        //Categories = new ObservableCollection<Category>();
        
        SelectedEventsTimeFrom = new TimeSpan(0, 0, 0);
        SelectedEventsTimeTo = new TimeSpan(23, 59, 0);

        SelectedCategories = new ObservableCollection<Category>();

        OnPropertyChanged(nameof(SelectedEventsTimeFrom));
        OnPropertyChanged(nameof(SelectedEventsTimeTo));
        Console.WriteLine(SelectedEventsTimeFrom);
        Console.WriteLine(SelectedEventsTimeTo);

        SelectedTimeFromLabel = SelectedEventsTimeFrom.ToString("hh\\:mm");
        SelectedTimeToLabel = SelectedEventsTimeTo.ToString("hh\\:mm");

        Console.WriteLine("Selected Categories1:");
        foreach (var category in Categories)
        {
            Console.WriteLine(category.Name);
        }
    }
    [RelayCommand]
    private Task InitParamRelatedData()
    {
        return Task.CompletedTask;
    }

    

    [RelayCommand]  
    public async Task FindEventsAsync()
    {
        // var selectedCategoryNames = SelectedCategoryNames;
        //await Shell.Current.GoToAsync("..");
        await Shell.Current.GoToAsync((".."),
            new Dictionary<string, object>
            {
                ["Categories"] = SelectedCategories,
                ["SelectedEventsTimeFrom"] = SelectedEventsTimeFrom,
                ["SelectedEventsTimeTo"] = SelectedEventsTimeTo,
            });


        //await Shell.Current.GoToAsync(nameof(EventListPage));

    }

    
    [RelayCommand]  
    public void RemoveSelectedCategory(Category selectedcategory)
    {
        SelectedCategories.Remove(selectedcategory);
    }

    [RelayCommand]
    async Task GoToTimeAsync()
    {
        var timeFilterPage = new EventTimeFilter();
        timeFilterPage.SelectedTimeChanged += OnSelectedTimeChanged;
        await Shell.Current.ShowPopupAsync(timeFilterPage);
    }

    private void OnSelectedTimeChanged(TimeSpan selectedEventsTimeFrom, TimeSpan selectedEventsTimeTo)
    {
        // Сохраните выбранные значения времени в нужные поля на главной странице
        // например:
        SelectedEventsTimeFrom = selectedEventsTimeFrom;
        SelectedEventsTimeTo = selectedEventsTimeTo;
        SelectedTimeFromLabel = selectedEventsTimeFrom.ToString("hh\\:mm");
        SelectedTimeToLabel = selectedEventsTimeTo.ToString("hh\\:mm");
    }


    private void LoadCategories()
    {
        Categories = new ObservableCollection<Category>
        {
        new Category { Name = "Экскурсии", Image = "tour" },
        new Category { Name = "Активный отдых", Image = "forest" },
        new Category { Name = "Спорт", Image = "sportstennis" },
        new Category { Name = "Искусство", Image = "colorlens" },
        new Category { Name = "Концерты", Image = "musicnote" },
        new Category { Name = "Театр и кино", Image = "videocam" },
        new Category { Name = "Обучение", Image = "importcontacts" },
        new Category { Name = "Выставки и музеи", Image = "museum" },
        new Category { Name = "Гастрономия", Image = "localdining" },
        new Category { Name = "Благотворительность", Image = "emojipeople" },
        new Category { Name = "Квесты", Image = "quests" },
        new Category { Name = "Ярмарки", Image = "store" }
        };
    }
}

