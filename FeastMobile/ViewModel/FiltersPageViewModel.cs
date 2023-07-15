using CommunityToolkit.Maui.Views;
using FeastMobile.Services;
using FeastMobile.View;


namespace FeastMobile.ViewModel;

[QueryProperty(nameof(Categories), nameof(Categories))]
public partial class FiltersPageViewModel : BaseViewModel
{
    public ObservableCollection<Category> Categories { get; set; }
    public ObservableCollection<Category> SelectedCategories { get; set; }
    public FiltersPageViewModel()
    {
        LoadCategories();
        //Categories = new ObservableCollection<Category>();
        SelectedCategories = new ObservableCollection<Category>();


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
                ["Categories"] = SelectedCategories
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
        await Shell.Current.ShowPopupAsync(new EventTimeFilter());
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

